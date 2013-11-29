/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.entity.RoLineTrn;
import jp.co.arkinfosys.entity.RoSlipTrn;
import jp.co.arkinfosys.entity.SalesLineTrn;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;

/**
 * 売上伝票作成に関わる受注伝票サービスクラスです.
 *
 * @author Ark Information Systems
 */
public class RoSlipSalesService extends RoSlipService {

	/** 売上伝票明細行用サービス */
	@Resource
	public SalesLineService salesLineService = new SalesLineService();

	@Resource
	public RoLineService roLineService;

	/**
	 * 処理対象伝票をロックします.
	 * @param roDto 受注伝票
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public void lock( ROrderSlipDto roDto ) throws ServiceException, UnabledLockException {

		Map<String, Object> param = super.createSqlParam();
		param.put(Param.RO_SLIP_ID, roDto.roSlipId );
		param.put(AbstractService.Param.LOCK_RECORD, AbstractService.FOR_UPDATE);
		lockRecordBySqlFile("rorder/LockSlip.sql", param, roDto.updDatetm);
	}

	/**
	 * 受注伝票を更新します.<br>
	 * 売上伝票が追加された時に呼び出されます.
	 *
	 * @param dto 売上伝票DTO
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public int updateSlipForInsert(SalesSlipDto dto) throws UnabledLockException,
			ServiceException {


		// 伝票の排他制御
		ROrderSlipDto roDto = new ROrderSlipDto();
		// 更新日付設定
		roDto.updDatetm = dto.roUpdDatetm;
		roDto.roSlipId = dto.roSlipId;

		lock(roDto);

		// 伝票取得
		ROrderSlipDto roDtoOld = loadBySlipId(dto.roSlipId);
		if( roDtoOld == null ){
			throw new ServiceException("errors.system");
		}

		List<ROrderLineDto> lineList = roLineService.loadBySlip(roDtoOld);
		roDtoOld.setLineDtoList(lineList);

		boolean complete = true;
		boolean partial = false;
		// 明細行更新
		for( ROrderLineDto rold : roDtoOld.getLineDtoList() ){

			boolean exist = false;
			List<SalesLineDto> salesLineList = dto.getLineDtoList();
			for (SalesLineDto lineDto : salesLineList ) {
				if( rold.roLineId.equals(lineDto.roLineId)){
					exist = true;

					Double numRest = Double.valueOf(rold.restQuantity);
					Double numInput = Double.valueOf(lineDto.quantity);

					// 残数計算
					numRest -= numInput;
					// 残数設定
					Converter conv = createProductNumConverter();
					Number num = (Number) conv.getAsObject(numRest.toString());
					rold.restQuantity = num.toString();

					// 最終出荷日は売上日
					rold.lastShipDate = getLastShipDate(dto, lineDto);

					// ステータス設定
					setStatus(lineDto.deliveryProcessCategory, rold);
					break;
				}
			}
			if( exist ){
				// 明細行更新
				RoLineTrn entity = Beans
									.createAndCopy(RoLineTrn.class, rold).dateConverter(
										Constants.FORMAT.TIMESTAMP, "updDatetm")
											.execute();
				if( roLineService.updateRecord(entity) == 0 ){
					throw new ServiceException("errors.system");
				}
			}
			// 全体の状態確認
			if( !Constants.STATUS_RORDER_LINE.SALES_FINISH.equals(rold.status)){
				complete = false;
			}
			if( Constants.STATUS_RORDER_LINE.NOWPURCHASING.equals(rold.status)){
				partial = true;
			}
		}
		// 伝票状態確認
		if( partial ){
			roDtoOld.status = Constants.STATUS_RORDER_SLIP.SALES_NOW;
		}else if( complete ){
			roDtoOld.status = Constants.STATUS_RORDER_SLIP.SALES_FINISH;
		}else{
			roDtoOld.status = Constants.STATUS_RORDER_SLIP.RECEIVED;
		}
		// 伝票更新
		RoSlipTrn entity = Beans.createAndCopy(RoSlipTrn.class, roDtoOld)
			.dateConverter(Constants.FORMAT.DATE, "roDate", "validDate")
				.dateConverter(Constants.FORMAT.DATE, "shipDate", "validDate")
					.dateConverter(Constants.FORMAT.DATE, "deliveryDate",
						"validDate").dateConverter(Constants.FORMAT.TIMESTAMP,
						"updDatetm").execute();

		Map<String, Object> param = setEntityToParam(entity);
		int cnt = this.updateBySqlFile("rorder/UpdateSlipSales.sql", param).execute();
		return cnt;
	}

	/**
	 * 受注伝票を更新します.
	 * 売上伝票が更新された時に呼び出されます.
	 *
	 * @param dto 売上伝票DTO
	 * @return 更新行数
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public int updateSlipBySales(SalesSlipDto dto) throws UnabledLockException,
			ServiceException {

		// 伝票取得
		ROrderSlipDto roDtoOld = loadBySlipId(dto.roSlipId);
		if( roDtoOld == null ){
			throw new ServiceException("errors.system");
		}

		List<ROrderLineDto> lineList = roLineService.loadBySlip(roDtoOld);
		roDtoOld.setLineDtoList(lineList);

		boolean complete = true;
		boolean partial = false;
		// 明細行更新
		for( ROrderLineDto rold : roDtoOld.getLineDtoList() ){
			// 全体の状態確認
			if( !Constants.STATUS_RORDER_LINE.SALES_FINISH.equals(rold.status)){
				complete = false;
			}
			if( Constants.STATUS_RORDER_LINE.NOWPURCHASING.equals(rold.status)){
				partial = true;
			}
		}
		// 伝票状態確認
		if( partial ){
			roDtoOld.status = Constants.STATUS_RORDER_SLIP.SALES_NOW;
		}else if( complete ){
			roDtoOld.status = Constants.STATUS_RORDER_SLIP.SALES_FINISH;
		}else{
			roDtoOld.status = Constants.STATUS_RORDER_SLIP.RECEIVED;
		}
		// 伝票更新
		RoSlipTrn entity = Beans.createAndCopy(RoSlipTrn.class, roDtoOld)
			.dateConverter(Constants.FORMAT.DATE, "roDate", "validDate")
				.dateConverter(Constants.FORMAT.DATE, "shipDate", "validDate")
					.dateConverter(Constants.FORMAT.DATE, "deliveryDate",
						"validDate").dateConverter(Constants.FORMAT.TIMESTAMP,
						"updDatetm").execute();

		Map<String, Object> param = setEntityToParam(entity);
		int cnt = this.updateBySqlFile("rorder/UpdateSlipSales.sql", param).execute();
		return cnt;
	}

	/**
	 * 受注伝票明細行を更新します.<br>
	 * 関連する売上伝票明細行が追加された時に呼び出されます.
	 * @param dto 売上伝票DTO
	 * @param lineDto 売上伝票明細行DTO
	 * @return 更新件数
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	public int insertSlipLine(SalesSlipDto dto, SalesLineDto lineDto) throws UnabledLockException,
			ServiceException {

		ROrderLineDto rold = findSlipLineById(lineDto.roLineId);
		if( rold == null ){
			throw new ServiceException("errors.system");
		}

		BigDecimal numRest = new BigDecimal(rold.restQuantity);
		BigDecimal numInput = new BigDecimal(lineDto.quantity);

		// 残数計算
		numRest = numRest.subtract(numInput);
		rold.restQuantity = numRest.toString();

		// 最終出荷日は売上日
		rold.lastShipDate = getLastShipDate(dto, lineDto);

		// ステータス設定
		setStatus(lineDto.deliveryProcessCategory, rold);

		// 明細行更新
		RoLineTrn entity = Beans
							.createAndCopy(RoLineTrn.class, rold).dateConverter(
								Constants.FORMAT.TIMESTAMP, "updDatetm")
									.execute();
		int cnt = roLineService.updateRecord(entity);
		if( cnt == 0 ){
			throw new ServiceException("errors.system");
		}
		return cnt;
	}

	/**
	 * 受注伝票明細行を更新します.<br>
	 * 関連する売上伝票明細行が更新された時に呼び出されます.
	 *
	 * @param dto 売上伝票DTO
	 * @param lineDto 売上伝票明細行DTO
	 * @return 更新件数
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	public int updateSlipLine(SalesSlipDto dto, SalesLineDto lineDto) throws UnabledLockException,
			ServiceException {

		ROrderLineDto rold = findSlipLineById(lineDto.roLineId);
		if( rold == null ){
			throw new ServiceException("errors.system");
		}

		BigDecimal numRest = new BigDecimal(rold.restQuantity);
		BigDecimal numInput = new BigDecimal(lineDto.quantity);
		BigDecimal numBack = new BigDecimal(lineDto.bkQuantity);

		// 残数計算
		numRest = numRest.subtract(numInput.subtract(numBack));
		rold.restQuantity = numRest.toString();

		// 最終出荷日は売上日
		rold.lastShipDate = getLastShipDate(dto, lineDto);

		// ステータス設定
		setStatus(lineDto.deliveryProcessCategory, rold);

		// 明細行更新
		RoLineTrn entity = Beans
							.createAndCopy(RoLineTrn.class, rold).dateConverter(
								Constants.FORMAT.TIMESTAMP, "updDatetm")
									.execute();
		int cnt = roLineService.updateRecord(entity);
		if( cnt == 0 ){
			throw new ServiceException("errors.system");
		}
		return cnt;
	}

	/**
	 * 受注伝票明細行を更新します.<br>
	 * 関連する売上伝票明細行が削除された時に呼び出されます.
	 * @param dto 売上伝票DTO
	 * @param sl 売上伝票明細行DTO
	 * @return 更新件数
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	public int deleteSlipLine(SalesSlipDto dto, SalesLineTrn sl ) throws UnabledLockException,
			ServiceException {

		ROrderLineDto rold = findSlipLineById(sl.roLineId.toString());
		if( rold == null ){
			throw new ServiceException("errors.system");
		}

		BigDecimal numRo = new BigDecimal(rold.quantity);
		BigDecimal numRest = new BigDecimal(rold.restQuantity);
		BigDecimal numBack = sl.quantity;

		// 残数計算
		numRest = numRest.add(numBack);
		rold.restQuantity = numRest.toString();

		// ステータス設定（削除した場合は、数量で判断）
		setStatusRestNum( numRest, numRo, rold, dto.salesCmCategory );

		// 最終出荷日更新
		rold.lastShipDate = salesLineService.getLastShipDate(dto.salesSlipId, sl.roLineId.toString());

		// 明細行更新
		RoLineTrn entity = Beans
							.createAndCopy(RoLineTrn.class, rold).dateConverter(
								Constants.FORMAT.TIMESTAMP, "updDatetm")
									.execute();
		int cnt = roLineService.updateRecord(entity);
		if( cnt == 0 ){
			throw new ServiceException("errors.system");
		}
		return cnt;
	}

	/**
	 * 明細行のステータスを設定します.
	 * @param numRest 残数
	 * @param numRo 受注数
	 * @param rold 設定対象受注伝票明細行DTO
	 * @param salesCmCategory 取引区分
	 * @throws ServiceException
	 */
	protected void setStatusRestNum( BigDecimal numRest, BigDecimal numRo, ROrderLineDto rold, String salesCmCategory )
	throws ServiceException {
		if( BigDecimal.ZERO.compareTo(numRest) == 0 ){
			// 残数 = 0であれば「売上完了」
			rold.status = Constants.STATUS_RORDER_LINE.SALES_FINISH;
		}else if( numRest.compareTo(numRo) == 0  ){
			// 受注数 = 残数であれば「受注」
			rold.status = Constants.STATUS_RORDER_LINE.RECEIVED;
		}else{
			// 「分納中」
			rold.status = Constants.STATUS_RORDER_LINE.NOWPURCHASING;
		}
	}

	/**
	 * 明細行のステータスを設定します.<br>
	 * ステータスは売上明細行の完納区分を元に設定されます.
	 * @param deliveryProcessCategory 完納区分
	 * @param rold 設定対象受注伝票明細行DTO
	 * @throws ServiceException
	 */
	protected void setStatus( String deliveryProcessCategory, ROrderLineDto rold )
	throws ServiceException {
		if( CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL.equals(deliveryProcessCategory) ){
			rold.status = Constants.STATUS_RORDER_LINE.SALES_FINISH;
		}else if( CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL.equals(deliveryProcessCategory) ){
			rold.status = Constants.STATUS_RORDER_LINE.NOWPURCHASING;
		}else{
			rold.status = Constants.STATUS_RORDER_LINE.RECEIVED;
		}
	}
	/**
	 * 受注伝票行IDを指定して、受注明細行情報を取得します.
	 * @param lineId 受注伝票行ID
	 * @return 受注明細行DTO
	 * @throws ServiceException
	 */
	public ROrderLineDto findSlipLineById(String lineId) throws ServiceException {

		// SQLパラメータを構築する
		Map<String, Object> param = super.createSqlParam();
		param.put(RoLineService.Param.RO_LINE_ID, lineId);

		List<RoLineTrn> listLine = selectBySqlFile(RoLineTrn.class,
				"rorder/FindRoLineTrnByLineId.sql", param)
				.getResultList();

		if( listLine.size() != 1 ){
			return null;
		}
		ROrderLineDto lineDto = Beans.createAndCopy(ROrderLineDto.class,
				listLine.get(0)).dateConverter(Constants.FORMAT.TIMESTAMP,
				"updDatetm").execute();
		return lineDto;
	}

	/**
	 * 最終売上日を現状のアクションフォームの内容を考慮して取得します.
	 * @param dto 受注伝票DTO
	 * @param lineDto 受注伝票明細行DTO
	 * @return 最終売上日
	 */
	protected String getLastShipDate(SalesSlipDto dto, SalesLineDto lineDto) {
		SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);
		String dbLast;
		try {
			// DB上の最も新しい売上日を取得する
			dbLast = salesLineService.getLastShipDate(dto.salesSlipId, lineDto.roLineId);
			if( dbLast == null ){
				// 見つからない時には現状の売上伝票の日付
				return dto.salesDate;
			}
		} catch (ServiceException e) {
			// 見つからない時には現状の売上伝票の日付
			return dto.salesDate;
		}
		java.util.Date dbDate;
		try {
			dbDate = DF_YMD.parse(dbLast);
		} catch (ParseException e) {
			// 見つからない時には現状の売上伝票の日付
			return dto.salesDate;
		}
		java.util.Date fmDate;
		try {
			fmDate = DF_YMD.parse(dto.salesDate);
		} catch (ParseException e) {
			// 見つからない時にはDBの売上伝票の日付（ここは通らない）
			return dbLast;
		}
		if( fmDate.after(dbDate)){
			return dto.salesDate;
		}else{
			return dbLast;
		}
	}
}
