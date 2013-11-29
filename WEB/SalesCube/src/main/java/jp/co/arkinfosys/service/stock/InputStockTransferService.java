/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadLineTrn;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 在庫移動入力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InputStockTransferService extends CommonInputStockService {

	/**
	 * 入出庫伝票を登録します.
	 * <p>
	 * 在庫移動では対となる出庫伝票と入庫伝票が同時に作成されます.<br>
	 * 戻り値は固定値「0」を返します.
	 * </p>
	 *
	 * @param dto 入出庫伝票DTO
	 * @return 0
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(EadSlipTrnDto dto) throws ServiceException {
		// 入出庫伝票番号を採番
		String dispatchSlipId = Long.toString(seqMakerService
				.nextval(EadService.Table.EAD_SLIP_TRN));
		String enterSlipId  = Long.toString(seqMakerService
				.nextval(EadService.Table.EAD_SLIP_TRN));

		// 出庫側の処理

		// 入出庫伝票の処理
		// 入出庫伝票番号
		dto.eadSlipId = dispatchSlipId;

		// 入出庫年度、月度、年月度を計算
		YmDto ymDto = ymService.getYm(dto.eadDate);
		if(ymDto == null) {
			ServiceException se = new ServiceException(
					MessageResourcesUtil.getMessage("errors.system"));
			se.setStopOnError(true);
			throw se;
		}
		dto.eadAnnual = ymDto.annual.toString();
		dto.eadMonthly = ymDto.monthly.toString();
		dto.eadYm = ymDto.ym.toString();

		// 入出庫伝票区分
		dto.eadSlipCategory = CategoryTrns.EAD_SLIP_CATEGORY_NORMAL;

		// 入出庫区分
		dto.eadCategory = CategoryTrns.EAD_CATEGORY_DISPATCH;

		// 登録元機能
		dto.srcFunc = Constants.SRC_FUNC.STOCK_TRANSFER;

		// 売上伝票番号
		dto.salesSlipId = null;

		// 仕入伝票番号
		dto.supplierSlipId = null;

		// 移動入出庫伝票番号
		dto.moveDepositSlipId = enterSlipId;

		// 在庫締処理日
		dto.stockPdate = null;

		// Insert(出庫)
		EadSlipTrn deliveryTrn = Beans.createAndCopy(EadSlipTrn.class,dto).execute();
		eadService.insertSlip(deliveryTrn);

		// 出庫のDTOから入庫のDTOを作成する
		EadSlipTrnDto stockDto = dto.createStockDto();
		EadSlipTrn stockTrn = Beans.createAndCopy(EadSlipTrn.class,stockDto).execute();

		// Insert(入庫)
		eadService.insertSlip(stockTrn);

		return 0;
	}

	/**
	 * 入出庫伝票の新規登録を行います.<br>
	 * ロック結果は必ず{@link LockResult}のSUCCEEDEDが返ります.
	 * @param dto 入出庫伝票DTO
	 * @param abstractServices サービスリスト
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	// 在庫移動入力画面は登録(insert)のみ
	@Override
	public int save(EadSlipTrnDto dto, AbstractService<?>... abstractServices)
			throws ServiceException, UnabledLockException {

		int lockResult = LockResult.SUCCEEDED;

		// 在庫移動入力を登録する
		insertRecord(dto);

		return lockResult;
	}


	/**
	 * 入出庫伝票を更新します.<br>
	 * 未実装です.
	 * @param dto　入出庫伝票DTO
	 * @return 0
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(EadSlipTrnDto dto) throws UnabledLockException,
			ServiceException {
		return 0;
	}

	/**
	 * 在庫移動画面用のDTOを作成します.
	 * <p>
	 * 引数の入出庫伝票番号に入庫側の伝票番号が指定された場合、<br>
	 * 入庫側の伝票番号から出庫側の伝票番号を取得して、出庫側のDTOを作成して返します.<br>
	 * 出庫側の伝票番号が指定された場合は、そのまま出庫側のDTOを作成して返します.<br><br>
	 *
	 * 在庫移動以外の伝票番号(入出庫入力画面で登録した伝票番号)が指定された場合にはnullを返します.
	 * </p>
	 * @param eadSlipId 入出庫伝票番号
	 * @return EadSlipTrnDto 入出庫伝票DTO
	 * @throws ServiceException
	 */
	@Override
	public EadSlipTrnDto loadBySlipId(String eadSlipId) throws ServiceException{
		try {
			// 入力された入出庫番号が数値であるかチェック
			try {
				Integer.valueOf(eadSlipId);
			} catch (NumberFormatException e) {
				return null;
			}
			// EadSlipTrnDtoを生成する
			// 伝票を検索する
			EadSlipTrn eadSlipTrn = eadService.findSlipByEadSlipId(Integer.valueOf(eadSlipId));
			if( (eadSlipTrn == null) ||(eadSlipTrn.moveDepositSlipId == null)){
				// 伝票を取得できなかった場合はnullを返す
				return null;
			}
			// 入庫側伝票番号が指定された場合は出庫側伝票番号で再検索する
			if(CategoryTrns.EAD_CATEGORY_ENTER.equals(eadSlipTrn.eadCategory)) {
				eadSlipTrn = eadService.findSlipByEadSlipId(Integer.valueOf(eadSlipTrn.moveDepositSlipId));
				if( (eadSlipTrn == null)){
					// 伝票を取得できなかった場合はnullを返す
					return null;
				}
			}
			// 出庫側明細を検索する
			List<EadLineTrn> eadLineTrnList = eadService.findLineByEadSlipId(Integer.valueOf(eadSlipTrn.eadSlipId));
			if (eadLineTrnList.size() == 0) {
				// 明細を取得できなかった場合はnullを返す
				return null;
			}
			// 入庫側明細を検索する
			List<EadLineTrn> eadLineTrnDestList = eadService.findLineByEadSlipId(
													Integer.valueOf(eadSlipTrn.moveDepositSlipId));
			if (eadLineTrnDestList.size() == 0) {
				// 明細を取得できなかった場合はnullを返す
				return null;
			}
			if(eadLineTrnList.size() != eadLineTrnDestList.size()) {
				// 出庫側と入庫側の明細行数に相違がある場合はnullを返す
				return null;
			}

			EadSlipTrnDto eadSlipTrnDto = Beans.createAndCopy(
					EadSlipTrnDto.class, eadSlipTrn).dateConverter(
							Constants.FORMAT.TIMESTAMP, "updDatetm").execute();

			List<EadLineTrnDto> eadLineTrnDtoList = new ArrayList<EadLineTrnDto>();
			int i = 0;
			for (EadLineTrn eadLineTrn : eadLineTrnList) {
				Converter conv = createProductNumConverter();
				EadLineTrnDto eadLineTrnDto = Beans.createAndCopy(
						EadLineTrnDto.class, eadLineTrn).converter(conv, EadService.Param.QUANTITY).execute();
				EadLineTrn eadLineTrnDest = eadLineTrnDestList.get(i);
				i++;

				// 移動先棚番を設定する
				eadLineTrnDto.rackCodeDest = eadLineTrnDest.rackCode;
				eadLineTrnDto.rackNameDest = eadLineTrnDest.rackName;

				// 現在庫数,変更後在庫数は取得し、設定
				ProductJoin pj = findProductByCode(eadLineTrnDto.productCode);
				if( pj != null ){
					if( CategoryTrns.PRODUCT_STOCK_CTL_YES.equals(pj.stockCtlCategory)){ // 在庫管理区分
						StockInfoDto dto = productStockService.calcStockQuantityByProductCode(eadLineTrnDto.productCode);
						eadLineTrnDto.stockCount = Integer.toString( dto.currentTotalQuantity);
					}
				}
				// 移動可能数(棚在庫数)を計算
				int unclosedQuantity = eadService.countUnclosedQuantityByProductCode(
						eadLineTrnDto.productCode, eadLineTrnDto.rackCode);
				int closedQuantity = productStockService.countClosedQuantityByProductCode(
						eadLineTrnDto.productCode, eadLineTrnDto.rackCode);
				int movableQuantity = unclosedQuantity + closedQuantity;
				eadLineTrnDto.movableStockCount = Integer.toString(movableQuantity);

				// 移動元在庫数
				eadLineTrnDto.quantitySrc = Integer.toString(movableQuantity);

				// 移動先在庫数
				int unclosedQuantityDest = eadService.countUnclosedQuantityByProductCode(
						eadLineTrnDto.productCode, eadLineTrnDto.rackCodeDest);
				int closedQuantityDest = productStockService.countClosedQuantityByProductCode(
						eadLineTrnDto.productCode, eadLineTrnDto.rackCodeDest);
				int movableQuantityDest = unclosedQuantityDest + closedQuantityDest;
				eadLineTrnDto.quantityDest = Integer.toString(movableQuantityDest);

				eadLineTrnDtoList.add(eadLineTrnDto);
			}
			eadSlipTrnDto.setLineDtoList(eadLineTrnDtoList);

			return eadSlipTrnDto;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}
}
