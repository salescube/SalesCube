/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.CheckUtil;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.purchase.PurchaseLineDto;
import jp.co.arkinfosys.dto.purchase.PurchaseSlipDto;
import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadLineTrn;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.Rack;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.entity.join.ProductSetJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.ProductSetService;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 仕入伝票入出庫入力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InputStockPurchaseService extends AbstractService<EadSlipTrn> {


	@Resource
	protected EadService eadService;

    @Resource
    protected RackService rackService;

	@Resource
	protected SeqMakerService seqMakerService;

	@Resource
	protected YmService ymService;

	@Resource
	protected ProductSetService productSetService;

	@Resource
	protected ProductService productService;


	/**
	 * 仕入伝票入力情報を使用して、対応する在庫締め済みの入出庫伝票が存在するか否かを返します.
	 * @param sstd 仕入伝票DTO
	 * @return 在庫締め済みの入出庫伝票が存在するか否か
	 * @throws ServiceException
	 */
	public boolean existsClosedEadSlip(PurchaseSlipDto sstd) throws ServiceException {

		if(sstd == null || sstd.supplierSlipId == null || sstd.supplierSlipId.length() == 0 ) {
			return false;
		}

		EadSlipTrn eadSlipTrn = this.eadService.findSlipBySupplierSlipId( Integer.valueOf(sstd.supplierSlipId) );
		if(eadSlipTrn == null) {
			return false;
		}

		if(eadSlipTrn.stockPdate != null) {
			return true;
		}
		return false;
	}

	/**
	 * 仕入伝票入力情報を使用して、入出庫入力の伝票・明細行を削除します.
	 * @param sstd 仕入伝票DTO
	 * @throws ServiceException
	 */
	public void delete(PurchaseSlipDto sstd)
			throws ServiceException {
		try {
			EadSlipTrn eadSlipTrn = this.eadService.findSlipBySupplierSlipId(Integer.parseInt(sstd.supplierSlipId));
			if(eadSlipTrn == null) {
				return;
			}

			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.EAD_SLIP_ID, eadSlipTrn.eadSlipId);
			lockRecordBySqlFile("ead/LockSlipByEadSlipId.sql",
					param, eadSlipTrn.updDatetm);

			// 入出庫伝票明細の削除
			List<EadLineTrn> eadLineTrnList = this.eadService.findLineByEadSlipId(eadSlipTrn.eadSlipId);
			for (EadLineTrn eadLineTrn : eadLineTrnList) {
				this.eadService.deleteLineByEadLineId(eadLineTrn.eadLineId);
			}

			// 入出庫伝票の削除
			this.eadService.deleteSlipByEadSlipId(eadSlipTrn.eadSlipId);
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 仕入伝票入力情報を使用して、入出庫伝票・明細行を更新します.<br>
	 * (明細数量のみを更新します)
	 * <p>
	 * 1.入出庫伝票更新<br>
	 * 　マイナス数量の仕入明細に対しては入出庫明細を作成しない仕様なので、<br>
	 * 　全仕入明細がマイナス数量の場合は入出庫明細が0件となります.<br>
	 * 　その場合は伝票ごと削除する必要があります.<br>
	 * 　但し、仕入明細の数量がマイナスのものでも更新処理で再びプラス数量に変更することも可能なため、<br>
	 * 　全仕入明細の数量がマイナスの状態からプラスの状態に変わった場合、入出庫伝票を再度作成することも<br>
	 * 　必要となります.(更新処理ではありますが、伝票・明細を新規作成する場面もありえます.)<br>
	 * <br>
	 * 2.明細行更新<br>
	 * 　画面の仕入明細を軸にループをします(但し、マイナス数量の明細は処理しません)<br>
	 * 　同じ仕入明細IDとなる入出庫明細が存在する場合、入出庫明細の数量をUPDATEします.<br>
	 * 　同じ仕入明細IDとなる入出庫明細が存在しない場合、入出庫明細を新規作成します.<br>
	 * 　上記で処理されない入出庫明細を全て削除します.
	 * </p>
	 * @param sstd 仕入明細Dto(更新ボタン押下時点で画面に見えている明細を含んだ仕入伝票DTO)
	 * @throws ServiceException
	 */
	public void update(PurchaseSlipDto sstd)
			throws ServiceException {

		// 入出庫伝票・明細の更新処理
		// 【伝票更新の考え方】
		// マイナス数量の仕入明細に対しては入出庫明細を作成しない仕様なので、
		// 全仕入明細がマイナス数量の場合は入出庫明細が0件となる。
		// その場合は伝票ごと削除する必要がある。
		// 但し、仕入明細の数量がマイナスのものでも更新処理で再びプラス数量に変更することも可能なので、
		// 全仕入明細の数量がマイナスの状態からプラスの状態に変わった場合、入出庫伝票を再度作成することも
		// 必要となる。(更新処理ではあるが、伝票・明細を新規作成する場面もありうることに注意。)
		// 【明細更新の手順】
		// 画面の仕入明細を軸にループをする(但し、マイナス数量の明細は処理しない)
		// 同じ仕入明細IDとなる入出庫明細が存在する場合、入出庫明細の数量をUPDATE
		// 同じ仕入明細IDとなる入出庫明細が存在しない場合、入出庫明細を新規作成
		// 上記で処理されない入出庫明細を全て削除する

		try {
			EadSlipTrn eadSlipTrn = this.eadService.findSlipBySupplierSlipId(Integer.parseInt(sstd.supplierSlipId));
			if(eadSlipTrn == null) {
				// 入出庫伝票が存在しない場合は入出庫伝票の新規作成処理を行う(伝票・明細両方が作成される)
				insert(sstd);
				return;
			}

			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.EAD_SLIP_ID, eadSlipTrn.eadSlipId);
			lockRecordBySqlFile("ead/LockSlipByEadSlipId.sql",
					param, eadSlipTrn.updDatetm);

			// 入出庫明細を、仕入明細IDをキーとしたMapに格納する
			List<EadLineTrn> eadLineTrnList = this.eadService.findLineByEadSlipId(eadSlipTrn.eadSlipId);
			Map<String, EadLineTrn> supplierEadLineMap = new HashMap<String, EadLineTrn>();
			for(EadLineTrn eadLineTrn : eadLineTrnList) {
				supplierEadLineMap.put(eadLineTrn.supplierLineId.toString(), eadLineTrn);
			}

			// 入出庫明細の更新処理(既に存在する入出庫明細は更新し、存在しない入出庫明細は新規作成する)
			short lineNo = 1;
			Map<String, PurchaseLineDto> supplierLineTrnUpdateTargetMap = new HashMap<String, PurchaseLineDto>();
			List<PurchaseLineDto> l = sstd.getLineDtoList();
			for ( PurchaseLineDto supplierLineTrnDto : l) {
				// 数量がマイナスの場合、入出庫明細は更新・作成しない (あと、更新時に削除された明細は全明細フィールドがnullで来るのでそれらも処理しない)
				if( supplierLineTrnDto.productCode == null ) {
					continue;
				}
				BigDecimal quantity = new BigDecimal(supplierLineTrnDto.quantity );
				if( new BigDecimal(0).compareTo(quantity) > 0 ){
					continue;
				}
				boolean delLineExist = false;
			    StringTokenizer st = new StringTokenizer(sstd.deleteLineIds, ",");
			    while (st.hasMoreTokens()) {
			    	if( st.nextToken().equals(supplierLineTrnDto.supplierLineId)){
			    		delLineExist = true;
			    		break;
			    	}
			    }
			    if( delLineExist ){
			    	continue;
			    }

				// 更新対象の明細を格納する(後で更新されなかった入出庫明細を全削除するためのマーク)
				supplierLineTrnUpdateTargetMap.put(supplierLineTrnDto.supplierLineId, supplierLineTrnDto);

				if(supplierEadLineMap.containsKey(supplierLineTrnDto.supplierLineId)) {
					// 同一仕入明細IDの入出庫明細が存在する仕入明細なので、数量をUPDATEする
					EadLineTrn eadLineTrn = supplierEadLineMap.get(supplierLineTrnDto.supplierLineId);
					eadLineTrn.quantity = new BigDecimal(supplierLineTrnDto.quantity);
					eadLineTrn.lineNo = lineNo++;
					this.eadService.updateLine(eadLineTrn);

				} else {
					// 同一仕入明細IDの入出庫明細が存在しない仕入明細なので、入出庫明細を新規作成する
					EadLineTrnDto eadLineTrnDto = setToDto( eadSlipTrn.eadSlipId.toString(), sstd, supplierLineTrnDto , (int)lineNo++);
					EadLineTrn eadLineTrn = Beans.createAndCopy(EadLineTrn.class, eadLineTrnDto).execute();
					this.eadService.insertLine(eadLineTrn);
				}
			}

			// 更新処理の対象とならなかった入出庫明細は全て削除する
			for( EadLineTrn eadLineTrn : eadLineTrnList) {
				if( ! supplierLineTrnUpdateTargetMap.containsKey(eadLineTrn.supplierLineId.toString())) {
					this.eadService.deleteLineByEadLineId(eadLineTrn.eadLineId);
				}
			}

			// 新規作成or更新した明細があれば伝票を更新、なければ伝票を削除する
			if(supplierLineTrnUpdateTargetMap.size() == 0) {
				// 全ての仕入明細がマイナス数量の場合は、入出庫伝票も削除する
				this.eadService.deleteSlipByEadSlipId(eadSlipTrn.eadSlipId);
			} else {
				// プラス数量の仕入明細があれば、入出庫伝票の更新を行う(伝票自体には変更ないが、明細は変更されたので伝票を更新した形跡のみ残しておく)
				EadSlipTrnDto updatedEadSlipTrnDto = Beans.createAndCopy(EadSlipTrnDto.class, eadSlipTrn ).execute();
				updatedEadSlipTrnDto = updateDtoEadSlipPartOnly( sstd, updatedEadSlipTrnDto );
				EadSlipTrn updatedEadSlipTrn = Beans.createAndCopy(EadSlipTrn.class, updatedEadSlipTrnDto ).execute();
				this.eadService.updateSlip(updatedEadSlipTrn);
			}
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}

	}

	/**
	 * 仕入伝票と明細行のDTOの内容から、入出庫伝票と明細行のDTOを更新します。<br>
	 * (DBは更新しません.)
	 * @param fromSupplierSlipTrnDto 更新のデータ元となる仕入伝票DTO
	 * @param toEadSlipTrnDto 更新対象の入出庫伝票DTO
	 * @return 更新された入出庫伝票DTO
	 * @throws ServiceException
	 */
	private EadSlipTrnDto updateDtoEadSlipPartOnly( PurchaseSlipDto fromSupplierSlipTrnDto, EadSlipTrnDto toEadSlipTrnDto ) throws ServiceException {
		// 入出庫年度、月度、年月度を計算
		YmDto ymDto = ymService.getYm(fromSupplierSlipTrnDto.supplierDate);
		if(ymDto == null) {
			ServiceException se = new ServiceException(
					MessageResourcesUtil.getMessage("errors.system"));
			se.setStopOnError(true);
			throw se;
		}
		toEadSlipTrnDto.eadDate = fromSupplierSlipTrnDto.supplierDate;
		toEadSlipTrnDto.eadAnnual = ymDto.annual.toString();
		toEadSlipTrnDto.eadMonthly = ymDto.monthly.toString();
		toEadSlipTrnDto.eadYm = ymDto.ym.toString();

		// ユーザ登録
		toEadSlipTrnDto.userId = fromSupplierSlipTrnDto.userId;
		toEadSlipTrnDto.userName = fromSupplierSlipTrnDto.userName;

		// 入出庫伝票区分 通常
		toEadSlipTrnDto.eadSlipCategory = CategoryTrns.EAD_SLIP_CATEGORY_NORMAL;

		// 入出庫区分 入庫
		toEadSlipTrnDto.eadCategory = CategoryTrns.EAD_CATEGORY_ENTER;

		// 登録元機能
		toEadSlipTrnDto.srcFunc = Constants.SRC_FUNC.PURCHASE;

		// 売上伝票番号
		toEadSlipTrnDto.salesSlipId = null;

		// 仕入伝票番号
		toEadSlipTrnDto.supplierSlipId = fromSupplierSlipTrnDto.supplierSlipId;

		// 移動入出庫伝票番号
		toEadSlipTrnDto.moveDepositSlipId = null;

		return toEadSlipTrnDto;
	}

	/**
	 * 仕入伝票入力情報を使用して入出庫入力の伝票・明細行を登録します.
	 * @param sstd 仕入伝票DTO
	 * @return 採番した伝票番号(未登録の場合はnull)
	 * @throws ServiceException
	 */
	public String insert(PurchaseSlipDto sstd)
			throws ServiceException {
		try {
			EadSlipTrnDto eadSlipTrnDto = new EadSlipTrnDto();
			eadSlipTrnDto.setLineDtoList(new ArrayList<EadLineTrnDto>());

			// 入出庫伝票の処理
			// 入出庫伝票番号を採番
			eadSlipTrnDto.eadSlipId = Long.toString(seqMakerService
					.nextval(EadService.Table.EAD_SLIP_TRN));

			// 伝票部分の値更新
			eadSlipTrnDto = updateDtoEadSlipPartOnly( sstd, eadSlipTrnDto );

			// 入出庫伝票明細の処理
			Integer lineNo = 0;
			List<PurchaseLineDto> l = sstd.getLineDtoList();
			for (PurchaseLineDto sltd : l) {

				// 商品コードに入力のない行、またはマイナス数量の明細はInsertしない
				if (!StringUtil.hasLength(sltd.productCode) || Integer.valueOf(sltd.quantity.trim()) < 0 ) {
					continue;
				}
				ProductJoin product = productService. findById( sltd.productCode );

				if( CategoryTrns.PRODUCT_SET_TYPE_SET.equals( product.setTypeCategory )){
					// セット商品は展開して登録
					// セット商品取得
					List<ProductSetJoin> listSet = productSetService.findProductSetByProductCode( sltd.productCode );
					for( ProductSetJoin psj : listSet ){

						// 商品取得
						ProductJoin pj = productService.findById(psj.productCode);

						// 入出庫伝票作成対象チェック
						if( !CheckUtil.isRackCheck(pj) ){
							continue;
						}

						// 明細情報を設定
						EadLineTrnDto eadLineTrnDto = setToDto( eadSlipTrnDto.eadSlipId, sstd, sltd , pj, psj, ++lineNo);

						// 明細行を追加
						eadSlipTrnDto.getLineDtoList().add(eadLineTrnDto);
					}
				}else{
					// 入出庫伝票作成対象チェック
					if( !CheckUtil.isRackCheck(product) ){
						continue;
					}

					// 明細情報を設定
					EadLineTrnDto eadLineTrnDto = setToDto( eadSlipTrnDto.eadSlipId, sstd, sltd , ++lineNo);

					// 明細行を追加
					eadSlipTrnDto.getLineDtoList().add(eadLineTrnDto);
				}
			}

			if( lineNo > 0 ){
				// Insert
				eadService.insertSlipAndLine(eadSlipTrnDto);

				return eadSlipTrnDto.eadSlipId;
			}
			// 登録しなかった
			return null;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票明細行DTO情報を設定します.
	 * @param eadSlipId 追加対象入出庫伝票番号
	 * @param sstd 登録元仕入れ伝票DTO
	 * @param sltd 登録元仕入れ伝票明細行DTO
	 * @param lineNo 登録行番号
	 * @return 登録対象入出庫伝票明細行DTO
	 * @throws ServiceException
	 */
	protected EadLineTrnDto setToDto(
				String eadSlipId,
				PurchaseSlipDto sstd, PurchaseLineDto sltd,
				Integer lineNo) throws ServiceException {

		EadLineTrnDto eadLineTrnDto = new EadLineTrnDto();

		// 入出庫伝票行IDを採番
		eadLineTrnDto.eadLineId = Long.toString(seqMakerService
				.nextval(EadService.Table.EAD_LINE_TRN));
		// 入出庫伝票番号を設定
		eadLineTrnDto.eadSlipId = eadSlipId;

		// 棚情報を取得する
		Rack rack = rackService.findById(sltd.rackCode);
		if( rack == null ){
			String strLabel = MessageResourcesUtil.getMessage("labels.product.csv.rackCode");
			String strMsg = MessageResourcesUtil.getMessage("errors.line.invalid");
			strMsg = strMsg.replace("{0}", sltd.lineNo);
			strMsg = strMsg.replace("{1}", strLabel);
			throw new ServiceException(strMsg);
		}
		// 行番号
		eadLineTrnDto.lineNo = lineNo.toString();

		// 商品コード
		eadLineTrnDto.productCode = sltd.productCode;

		// 商品名
		eadLineTrnDto.productAbstract = sltd.productAbstract;

		// 数量
		Converter conv = createProductNumConverter();
		Number num = (Number) conv.getAsObject(sltd.quantity);
		eadLineTrnDto.quantity = num.toString();

		// 備考
		eadLineTrnDto.remarks = sstd.supplierSlipId + "-" + sltd.lineNo;

		// 棚コード
		eadLineTrnDto.rackCode = sltd.rackCode;

		// 棚番名
		eadLineTrnDto.rackName = rack.rackName;

		// 売上伝票行ID
		eadLineTrnDto.salesLineId = null;

		// 仕入伝票行ID
		eadLineTrnDto.supplierLineId = sltd.supplierLineId;

		return eadLineTrnDto;
	}

	/**
	 * 入出庫伝票明細行DTO情報を設定します.
	 * @param eadSlipId 追加対象入出庫伝票番号
	 * @param sstd 登録元仕入れ伝票DTO
	 * @param sltd 登録元仕入れ伝票明細行DTO
	 * @param pj 商品情報エンティティ
	 * @param psj セット商品情報エンティティ
	 * @param lineNo 登録行番号
	 * @return 登録対象入出庫伝票明細行DTO
	 * @throws ServiceException
	 */
	protected EadLineTrnDto setToDto(
				String eadSlipId,
				PurchaseSlipDto sstd, PurchaseLineDto sltd,
				ProductJoin pj, ProductSetJoin psj,
				Integer lineNo) throws ServiceException {

		EadLineTrnDto eadLineTrnDto = new EadLineTrnDto();

		// 入出庫伝票行IDを採番
		eadLineTrnDto.eadLineId = Long.toString(seqMakerService
				.nextval(EadService.Table.EAD_LINE_TRN));
		// 入出庫伝票番号を設定
		eadLineTrnDto.eadSlipId = eadSlipId;

		// 行番号
		eadLineTrnDto.lineNo = lineNo.toString();

		// 商品コード
		eadLineTrnDto.productCode = pj.productCode;

		// 商品名
		eadLineTrnDto.productAbstract = pj.productName;

		// 数量は売上伝票の数量×セット商品の数量
		Double quantity = Double.valueOf(sltd.quantity);
		Double setQuantity = psj.quantity.doubleValue();
		String setNum = Double.toString( quantity * setQuantity );
		Converter conv = createProductNumConverter();
		Number num = (Number) conv.getAsObject( setNum );
		eadLineTrnDto.quantity = num.toString();

		// 備考
		eadLineTrnDto.remarks = sstd.supplierSlipId + "-" + sltd.lineNo
						+ " " + sltd.productCode + ":" + sltd.productAbstract;

		// 棚コード
		eadLineTrnDto.rackCode = pj.rackCode;

		// 棚番名
		eadLineTrnDto.rackName = pj.rackName;

		// 売上伝票行ID
		eadLineTrnDto.salesLineId = null;

		// 仕入伝票行ID
		eadLineTrnDto.supplierLineId = sltd.supplierLineId;

		return eadLineTrnDto;
	}
}
