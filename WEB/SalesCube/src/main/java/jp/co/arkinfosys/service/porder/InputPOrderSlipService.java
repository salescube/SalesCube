/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.porder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.porder.InputPOrderSlipDto;
import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.entity.join.PoSlipTrnJoin;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 発注入力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InputPOrderSlipService extends AbstractSlipService<PoSlipTrn,InputPOrderSlipDto> {

	/**
	 *
	 * テーブル名定義クラスです.
	 *
	 */
	public static class Table {
		/** 発注伝票テーブル名 */
		public static final String SLIP_TABLE_NAME = "PO_SLIP_TRN";
	}

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	// 独自に代入あるいはチェックすべきパラメータ(DB対応有)
	public static class Param {
		public static final String PO_SLIP_ID = "poSlipId";
		public static final String PO_DATE = "poDate";
		public static final String PO_ANNUAL = "poAnnual";
		public static final String PO_MONTHLY = "poMonthly";
		public static final String PO_YM = "poYm";
		public static final String CRE_DATETM = "creDatetm";
		public static final String UPD_DATETM = "updDatetm";
		public static final String RATE = "rate";
		public static final String SUPPLIER_RATE = "supplierRate";
		public static final String CTAX_RATE = "ctaxRate";
//		public static final String SUPPLIER_TAX_RATE = "supplierTaxRate";
		public static final String PRODUCT_CODE_LIST = "productCodeList";
		public static final String STATUS = "status";
	}

	/**
	 *
	 * ローカルのみで使用するパラメータ定義クラスです.
	 *
	 */
	// その他ローカルでしか使わないパラメータ(DB対応無)
	public static class ParamLocal {
		public static final String productIsExist = "productIsExist";
		public static final String PO_LINE_STATUS = "poLineStatusCategory";
		public static final String PRODUCT_STATUS_SALE_CANCEL = "productStatusSaleCancel";

		private static final String UNPAID = "slipPaymentStatusUnpaid";
		private static final String PAYING = "slipPaymentStatusPaying";
		private static final String PAID = "slipPaymentStatusPaid";
		private static final String STATUS_SUPPLIER_SLIP_UNPAID = "statusSupplierSlipUnpaid";
		private static final String STATUS_SUPPLIER_SLIP_PAYING = "statusSupplierSlipPaying";
		private static final String STATUS_SUPPLIER_SLIP_PAID = "statusSupplierSlipPaid";
		private static final String STATUS_PORDER_SLIP_PURCHASED = "statusPorderSlipPurchased";

	}

	/**
	 * 見積番号のカラム名
	 */
	public static final String COLUMN_PO_SLIP_ID = "PO_SLIP_ID";

	// 発番エラー検出用初期値
	public static final Long DEFAULT_ID = -1L;
	// 有効明細行無しの場合の戻り値
	public static final Long NO_VALID_LINE = -2L;
	// 発番エラー時の戻り値
	public static final Long CANNOT_GET_ID = DEFAULT_ID;
	// 伝票登録失敗時の戻り値
	public static final Long CANNOT_CREATE_SLIP = -3L;
	public static final Long CANNOT_UPDATE_SLIP = CANNOT_CREATE_SLIP;
	public static final Long CANNOT_DELETE_SLIP = CANNOT_CREATE_SLIP;
	// アクションフォームに対応する値がないです
	public static final Long LACK_OF_VALUES = -4L;

	// 発番のため
	@Resource
	private SeqMakerService seqMakerService;

	// 仕入先情報取得のため
	@Resource
	private SupplierService supplierService;

	// 年月度取得のため
	@Resource
	protected YmService ymService;


	/**
	 * 発注伝票を削除します.
	 * @param id 発注伝票番号
	 * @param updDatetm 更新日時
	 * @return 削除件数
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int deleteById(String id, String updDatetm) throws ServiceException,
			UnabledLockException {

		// 排他制御
		int lockResult = this.lockRecord(Param.PO_SLIP_ID, id, updDatetm, "porder/LockPOrderSlipByPOSlipId.sql");

		// 伝票番号を指定して発注伝票を削除する
		Map<String, Object> param = createSqlParam();
		param.put(Param.PO_SLIP_ID, id);
		this.updateBySqlFile("porder/DeletePOrderSlip.sql", param)
				.execute();

		return lockResult;
	}

	/**
	 * キーカラム名を返します.
	 * @return 発注伝票テーブルのキーカラム名
	 */
	@Override
	protected String getKeyColumnName() {
		return COLUMN_PO_SLIP_ID;
	}

	/**
	 * テーブル名を返します.
	 * @return 発注伝票テーブル名
	 */
	@Override
	protected String getTableName() {
		return Table.SLIP_TABLE_NAME;
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param entity 発注伝票エンティティ
	 * @return 検索条件パラメータ
	 */
	private Map<String, Object> setEntityToParam(PoSlipTrn entity){

		//MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		//アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class,entity).execute();
		param.putAll(AFparam);

		//更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return param;
	}

	/**
	 * 発注伝票を登録します.
	 * @param dto 発注伝票DTO
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(InputPOrderSlipDto dto) throws ServiceException {
		try {
			// 伝票番号発番
			Long newPoSlipId = DEFAULT_ID;
			newPoSlipId = seqMakerService.nextval(PoSlipTrn.TABLE_NAME);
			if (newPoSlipId.equals(DEFAULT_ID)) {
				return 0;
			}
			dto.poSlipId = newPoSlipId.toString();

			PoSlipTrn entity = Beans.createAndCopy(
					PoSlipTrn.class, dto).dateConverter(
					Constants.FORMAT.DATE, "poDate", "deliveryDate")
					.excludesWhitespace().execute();

			Map<String, Object> param = setEntityToParam(entity);

			return this.updateBySqlFile("porder/InsertPOrderSlip.sql",
					param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 発注伝票を取得します.<br>
	 * 引数の伝票番号をキーに発注伝票情報を取得します.
	 *
	 * @param id 伝票番号
	 * @return 発注伝票DTO
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public InputPOrderSlipDto loadBySlipId(String id) throws ServiceException,
			UnabledLockException {
		try {
			Map<String, Object> param = super.createSqlParam();

			param.put(Param.PO_SLIP_ID, id);

			PoSlipTrn entity = this.selectBySqlFile(
					PoSlipTrn.class,
					"porder/FindPOrderSlipByPOSlipId.sql", param)
					.getSingleResult();

			if (entity != null) {
				InputPOrderSlipDto dto = new InputPOrderSlipDto();
				Beans.copy(entity, dto).dateConverter(Constants.FORMAT.DATE,
						"poDate", "deliveryDate").dateConverter(
						Constants.FORMAT.TIMESTAMP, "updDatetm", "creDatetm").execute();
				SupplierJoin supplier = supplierService.findById(dto.supplierCode);
				//外貨通貨記号
				dto.defaultCUnit = supplier.cUnitSign;

				setSlipPaymentStatus(dto);

				return dto;
			}
			return null;
		} catch (SNonUniqueResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 発注伝票の新規登録・更新処理を行います.
	 *
	 * @param dto 発注伝票DTO
	 * @param abstractServices サービスリスト
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int save(InputPOrderSlipDto dto, AbstractService<?>... abstractServices)
			throws ServiceException, UnabledLockException {

		// 見積年度、月度、年月度を計算
		YmDto ymDto = ymService.getYm(dto.poDate);
		if (ymDto == null) {
			// DTOの中身を空にしておく
			dto.poAnnual = "";
			dto.poMonthly = "";
			dto.poYm = "";
		} else {
			dto.poAnnual = ymDto.annual.toString();
			dto.poMonthly = ymDto.monthly.toString();
			dto.poYm = ymDto.ym.toString();
		}

		int lockResult = LockResult.SUCCEEDED;

		if ( !StringUtil.hasLength(dto.poSlipId) ) {
			// 見積を登録する
			insertRecord(dto);
		} else {
			// 見積を更新する
			lockResult = updateRecord(dto);
		}
		return lockResult;
	}

	/**
	 * 発注伝票を更新します.
	 *
	 * @param dto 発注伝票DTO
	 * @return レコードロック結果
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(InputPOrderSlipDto dto)
			throws UnabledLockException, ServiceException {

		PoSlipTrn entity = Beans.createAndCopy(PoSlipTrn.class, dto)
			.dateConverter(Constants.FORMAT.DATE, "poDate", "deliveryDate")
			.dateConverter(Constants.FORMAT.TIMESTAMP, "updDatetm")
			.excludesWhitespace().execute();

		// 排他制御
		int lockResult = this.lockRecord(Param.PO_SLIP_ID, entity.poSlipId, entity.updDatetm, "porder/LockPOrderSlipByPOSlipId.sql");

		Map<String, Object> param = setEntityToParam(entity);
		this.updateBySqlFile("porder/UpdatePOrderSlip.sql", param).execute();

		return lockResult;
	}

	/**
	 * 発注伝票支払状況を設定します.
	 * @param dto 発注伝票DTO
	 * @throws ServiceException
	 */
	public void setSlipPaymentStatus(InputPOrderSlipDto dto) throws ServiceException {
		dto.slipPaymentStatus = "";
		dto.slipPaymentDate = "";

		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.PO_SLIP_ID,
					dto.poSlipId);

			// 未払い
			param.put(ParamLocal.UNPAID, MessageResourcesUtil
					.getMessage("labels.slipPaymentStatus.unpaid"));
			// 支払中
			param.put(ParamLocal.PAYING, MessageResourcesUtil
					.getMessage("labels.slipPaymentStatus.paying"));
			// 済
			param.put(ParamLocal.PAID, MessageResourcesUtil
					.getMessage("labels.slipPaymentStatus.paid"));
			
			// 発注伝票状態 仕入完了：Constants.STATUS_PORDER_SLIP.PURCHASED
			param.put(ParamLocal.STATUS_PORDER_SLIP_PURCHASED, Constants.STATUS_PORDER_SLIP.PURCHASED);
			// 仕入伝票状態 未払い：Constants.STATUS_SUPPLIER_SLIP.UNPAID
			param.put(ParamLocal.STATUS_SUPPLIER_SLIP_UNPAID, Constants.STATUS_SUPPLIER_SLIP.UNPAID);
			param.put(ParamLocal.STATUS_SUPPLIER_SLIP_PAYING, Constants.STATUS_SUPPLIER_SLIP.PAYING);
			param.put(ParamLocal.STATUS_SUPPLIER_SLIP_PAID, Constants.STATUS_SUPPLIER_SLIP.PAID);

			PoSlipTrnJoin poSlipTrnJoin = this.selectBySqlFile(
					PoSlipTrnJoin.class,
					"porder/FindPaymentSlipByPOSlipId.sql", param)
					.getSingleResult();

			if (poSlipTrnJoin != null) {
				if (poSlipTrnJoin.paymentDate != null) {
					BeanMap dt = Beans.createAndCopy(BeanMap.class,poSlipTrnJoin)
						.includes("paymentDate").dateConverter(Constants.FORMAT.DATE).execute();
					dto.slipPaymentDate = (String)dt.get("paymentDate");
				}
				dto.slipPaymentStatus = poSlipTrnJoin.paymentStatus;
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


}
