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
 * 入出庫入力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InputStockService extends CommonInputStockService {

	/**
	 * 入出庫入力伝票を登録します.
	 *
	 * @param dto 入出庫伝票DTO
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(EadSlipTrnDto dto) throws ServiceException {
		try {
			// 入出庫伝票の処理
			// 入出庫伝票番号を採番
			dto.eadSlipId = Long.toString(seqMakerService
					.nextval(EadService.Table.EAD_SLIP_TRN));

			// 入出庫年度、月度、年月度を計算
			YmDto ymDto = ymService.getYm(dto.eadDate);
			if (ymDto == null) {
				ServiceException se = new ServiceException(MessageResourcesUtil
						.getMessage("errors.system"));
				se.setStopOnError(true);
				throw se;
			}
			dto.eadAnnual = ymDto.annual.toString();
			dto.eadMonthly = ymDto.monthly.toString();
			dto.eadYm = ymDto.ym.toString();

			// 登録元機能
			dto.srcFunc = Constants.SRC_FUNC.STOCK;

			// 売上伝票番号
			dto.salesSlipId = null;

			// 仕入伝票番号
			dto.supplierSlipId = null;

			// 移動入出庫伝票番号
			dto.moveDepositSlipId = null;

			// Insert
			//eadService.insertSlipAndLine(dto);
			EadSlipTrn eadSlipTrn = Beans.createAndCopy(EadSlipTrn.class, dto)
					.execute();
			return eadService.insertSlip(eadSlipTrn);

			//return Integer.parseInt(dto.eadSlipId);
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票の新規登録・更新処理を行います.
	 *
	 * @param dto 入出庫入力伝票DTO
	 * @param abstractServices サービスリスト
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int save(EadSlipTrnDto dto, AbstractService<?>... abstractServices)
			throws ServiceException, UnabledLockException {
		int lockResult = LockResult.SUCCEEDED;

		if (dto.newData == null || dto.newData) {
			// 入出庫伝票を登録する
			insertRecord(dto);
		} else {
			// 入出庫伝票を更新する
			lockResult = updateRecord(dto);
		}
		return lockResult;

	}

	/**
	 * 入出庫伝票を更新します.<br>
	 * 未実装です.
	 *
	 * @param dto 入出庫伝票DTO
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
	 * 入出庫伝票DTOを返します.
	 * @param eadSlipId 入出庫伝票番号
	 * @return 入出庫伝票DTO
	 * @throws ServiceException
	 */
	@Override
	public EadSlipTrnDto loadBySlipId(String eadSlipId) throws ServiceException {
		try {
			// 入力された入出庫番号が数値であるかチェック
			try {
				Integer.valueOf(eadSlipId);
			} catch (NumberFormatException e) {
				return null;
			}

			// EadSlipTrnDtoを生成する
			EadSlipTrn eadSlipTrn = eadService.findSlipByEadSlipId(Integer
					.valueOf(eadSlipId));
			List<EadLineTrn> eadLineTrnList = eadService
					.findLineByEadSlipId(Integer.valueOf(eadSlipId));
			if (eadSlipTrn == null
					|| (eadLineTrnList == null || eadLineTrnList.size() == 0)) {
				// 伝票または明細を取得できなかった場合はnullを返す
				return null;
			}

			EadSlipTrnDto eadSlipTrnDto = Beans.createAndCopy(
					EadSlipTrnDto.class, eadSlipTrn).dateConverter(
					Constants.FORMAT.TIMESTAMP, "updDatetm").execute();
			List<EadLineTrnDto> eadLineTrnDtoList = new ArrayList<EadLineTrnDto>();
			Converter conv = createProductNumConverter();
			for (EadLineTrn eadLineTrn : eadLineTrnList) {
				EadLineTrnDto eadLineTrnDto = Beans.createAndCopy(
						EadLineTrnDto.class, eadLineTrn).converter(conv,
						EadService.Param.QUANTITY).execute();

				// 現在庫数,変更後在庫数は取得し、設定
				ProductJoin pj = findProductByCode(eadLineTrnDto.productCode);
				if (pj != null) {
					if (CategoryTrns.PRODUCT_STOCK_CTL_YES
							.equals(pj.stockCtlCategory)) { // 在庫管理区分
						StockInfoDto dto = productStockService
								.calcStockQuantityByProductCode(eadLineTrnDto.productCode);
						eadLineTrnDto.stockCount = Integer
								.toString(dto.currentTotalQuantity);

						int updQuantity;
						// 編集時の初期表示時は、変更後在庫数は、現在庫数とする。
						updQuantity = Integer
								.parseInt(eadLineTrnDto.stockCount);
						eadLineTrnDto.updateQuantity = Integer
								.toString(updQuantity);
					}

					// 商品備考を読み込む
					eadLineTrnDto.productRemarks = pj.remarks;
				}

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
