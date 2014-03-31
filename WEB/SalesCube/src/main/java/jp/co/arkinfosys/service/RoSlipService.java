/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.entity.RoSlipTrn;
import jp.co.arkinfosys.entity.join.ProductSetJoin;
import jp.co.arkinfosys.entity.join.StockQuantity;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.Beans;

/**
 * 受注伝票サービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class RoSlipService extends AbstractSlipService<RoSlipTrn,ROrderSlipDto> {
	@Resource
	private SeqMakerService seqMakerService;

	@Resource
	private ProductSetService productSetService;

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		private static final String PRODUCT_CODE = "productCode";

		// 受注伝票
		public static final String RO_SLIP_ID = "roSlipId";
		public static final String STATUS = "status";
		public static final String RO_ANNUAL = "roAnnual";
		public static final String RO_MONTHLY = "roMonthly";
		public static final String RO_YM = "roYm";
		public static final String RO_DATE = "roDate";
		public static final String SHIP_DATE = "shipDate";
		public static final String DELIVERY_DATE = "deliveryDate";
		public static final String RECEPT_NO = "receptNo";
		public static final String CUSTOMER_SLIP_NO = "customerSlipNo";
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
		public static final String CUTOFF_GROUP = "cutoffGroup";
		public static final String PAYBACK_CYCLE_CATEGORY = "paybackCycleCategory";
		public static final String USER_ID = "userId";
		public static final String USER_NAME = "userName";
		public static final String REMARKS = "remarks";
		public static final String CUSTOMER_CODE = "customerCode";
		public static final String CUSTOMER_NAME = "customerName";
		public static final String CUSTOMER_REMARKS = "customerRemarks";
		public static final String CUSTOMER_COMMENT_DATA = "customerCommentData";
		public static final String DELIVERY_CODE = "deliveryCode";
		public static final String DELIVERY_NAME = "deliveryName";
		public static final String DELIVERY_KANA = "deliveryKana";
		public static final String DELIVERY_OFFICE_NAME = "deliveryOfficeName";
		public static final String DELIVERY_OFFICE_KANA = "deliveryOfficeKana";
		public static final String DELIVERY_DEPT_NAME = "deliveryDeptName";
		public static final String DELIVERY_ZIP_CODE = "deliveryZipCode";
		public static final String DELIVERY_ADDRESS_1 = "deliveryAddress1";
		public static final String DELIVERY_ADDRESS_2 = "deliveryAddress2";
		public static final String DELIVERY_PC_NAME = "deliveryPcName";
		public static final String DELIVERY_PC_KANA = "deliveryPcKana";
		public static final String DELIVERY_PC_PRE_CATEGORY = "deliveryPcPreCategory";
		public static final String DELIVERY_PC_PRE = "deliveryPcPre";
		public static final String DELIVERY_TEL = "deliveryTel";
		public static final String DELIVERY_FAX = "deliveryFax";
		public static final String DELIVERY_EMAIL = "deliveryEmail";
		public static final String DELIVERY_URL = "deliveryUrl";
		public static final String ESTIMATE_SHEET_ID = "estimateSheetId";
		public static final String TAX_SHIFT_CATEGORY = "taxShiftCategory";
		public static final String TAX_FRACT_CATEGORY = "taxFractCategory";
		public static final String PRICE_FRACT_CATEGORY = "priceFractCategory";
		public static final String CTAX_PRICE_TOTAL = "ctaxPriceTotal";
		public static final String CTAX_RATE = "ctaxRate";
		public static final String COST_TOTAL = "costTotal";
		public static final String RETAIL_PRICE_TOTAL = "retailPriceTotal";
		public static final String PRICE_TOTAL = "priceTotal";
		public static final String PRINT_COUNT = "printCount";
		public static final String COD_SC = "codSc";
		public static final String CRE_FUNC = "creFunc";
		public static final String CRE_DATETM = "creDatetm";
		public static final String CRE_USER = "creUser";
		public static final String UPD_FUNC = "updFunc";
		public static final String UPD_DATETM = "updDatetm";
		public static final String UPD_USER = "updUser";
		public static final String CATEGORY_ID = "categoryId";
		public static final String DC_CATEGORY = "dcCategory";
		public static final String DC_NAME = "dcName";
		public static final String DC_TIMEZONE_CATEGORY = "dcTimezoneCategory";
		public static final String DC_TIMEZONE = "dcTimezone";
	}

	/**
	 *
	 * テーブル名定義クラスです.
	 *
	 */
	public static class Table {
		/** テーブル名：受注伝票 */
		private static final String RO_SLIP_TRN = "RO_SLIP_TRN";
	}

	/**
	 * 受注伝票番号を指定して、受注伝票情報を取得します.
	 * @param slipId 受注伝票番号
	 * @return 受注伝票情報エンティティ
	 * @throws ServiceException
	 */
	protected RoSlipTrn getRoSlipTrn(String slipId) throws ServiceException {

		// SQLパラメータを構築する
		Map<String, Object> param = super.createSqlParam();
		param.put(RoSlipService.Param.RO_SLIP_ID, slipId);

		return this.selectBySqlFile(RoSlipTrn.class,
				"rorder/FindRoSlipTrnBySlipId.sql", param).getSingleResult();
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param entity 受注伝票情報エンティティ
	 * @return 検索条件パラメータ
	 */
	protected Map<String, Object> setEntityToParam(RoSlipTrn entity) {
		Map<String, Object> param = super.createSqlParam();

		param.put(RoSlipService.Param.RO_SLIP_ID, entity.roSlipId);
		param.put(RoSlipService.Param.STATUS, entity.status);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		if (entity.roDate != null) {
			param.put(RoSlipService.Param.RO_ANNUAL, sdf.format(entity.roDate));
			sdf = new SimpleDateFormat("MM");
			param
					.put(RoSlipService.Param.RO_MONTHLY, sdf
							.format(entity.roDate));
			sdf = new SimpleDateFormat("yyyyMM");
			param.put(RoSlipService.Param.RO_YM, sdf.format(entity.roDate));
			param.put(RoSlipService.Param.RO_DATE, entity.roDate);
		} else {
			param.put(RoSlipService.Param.RO_ANNUAL, "");
			param.put(RoSlipService.Param.RO_MONTHLY, "");
			param.put(RoSlipService.Param.RO_YM, "");
			param.put(RoSlipService.Param.RO_DATE, entity.roDate);
		}
		param.put(RoSlipService.Param.SHIP_DATE, entity.shipDate);
		param.put(RoSlipService.Param.DELIVERY_DATE, entity.deliveryDate);
		param.put(RoSlipService.Param.RECEPT_NO, entity.receptNo);
		param.put(RoSlipService.Param.CUSTOMER_SLIP_NO, entity.customerSlipNo);
		param.put(RoSlipService.Param.SALES_CM_CATEGORY, entity.salesCmCategory);
		param.put(RoSlipService.Param.CUTOFF_GROUP, entity.cutoffGroup);
		param.put(RoSlipService.Param.PAYBACK_CYCLE_CATEGORY, entity.paybackCycleCategory);
		param.put(RoSlipService.Param.USER_ID, entity.userId);
		param.put(RoSlipService.Param.USER_NAME, entity.userName);
		param.put(RoSlipService.Param.REMARKS, entity.remarks);
		param.put(RoSlipService.Param.CUSTOMER_CODE, entity.customerCode);
		param.put(RoSlipService.Param.CUSTOMER_NAME, entity.customerName);
		param.put(RoSlipService.Param.CUSTOMER_REMARKS, entity.customerRemarks);
		param.put(RoSlipService.Param.CUSTOMER_COMMENT_DATA, entity.customerCommentData);
		param.put(RoSlipService.Param.DELIVERY_CODE, entity.deliveryCode);
		param.put(RoSlipService.Param.DELIVERY_NAME, entity.deliveryName);
		param.put(RoSlipService.Param.DELIVERY_KANA, entity.deliveryKana);
		param.put(RoSlipService.Param.DELIVERY_OFFICE_NAME, entity.deliveryOfficeName);
		param.put(RoSlipService.Param.DELIVERY_OFFICE_KANA, entity.deliveryOfficeKana);
		param.put(RoSlipService.Param.DELIVERY_DEPT_NAME, entity.deliveryDeptName);
		param.put(RoSlipService.Param.DELIVERY_ZIP_CODE, entity.deliveryZipCode);
		param.put(RoSlipService.Param.DELIVERY_ADDRESS_1, entity.deliveryAddress1);
		param.put(RoSlipService.Param.DELIVERY_ADDRESS_2, entity.deliveryAddress2);
		param.put(RoSlipService.Param.DELIVERY_PC_NAME, entity.deliveryPcName);
		param.put(RoSlipService.Param.DELIVERY_PC_KANA, entity.deliveryPcKana);
		param.put(RoSlipService.Param.DELIVERY_PC_PRE_CATEGORY,
				entity.deliveryPcPreCategory);
		param.put(RoSlipService.Param.DELIVERY_PC_PRE, entity.deliveryPcPre);
		param.put(RoSlipService.Param.DELIVERY_TEL, entity.deliveryTel);
		param.put(RoSlipService.Param.DELIVERY_FAX, entity.deliveryFax);
		param.put(RoSlipService.Param.DELIVERY_EMAIL, entity.deliveryEmail);
		param.put(RoSlipService.Param.DELIVERY_URL, entity.deliveryUrl);
		param.put(RoSlipService.Param.ESTIMATE_SHEET_ID, entity.estimateSheetId);
		param.put(RoSlipService.Param.TAX_SHIFT_CATEGORY, entity.taxShiftCategory);
		param.put(RoSlipService.Param.TAX_FRACT_CATEGORY, entity.taxFractCategory);
		param.put(RoSlipService.Param.PRICE_FRACT_CATEGORY, entity.priceFractCategory);
		param.put(RoSlipService.Param.CTAX_PRICE_TOTAL, entity.ctaxPriceTotal);
		param.put(RoSlipService.Param.CTAX_RATE, entity.ctaxRate);
		param.put(RoSlipService.Param.COST_TOTAL, entity.costTotal);
		param.put(RoSlipService.Param.RETAIL_PRICE_TOTAL, entity.retailPriceTotal);
		param.put(RoSlipService.Param.PRICE_TOTAL, entity.priceTotal);
		param.put(RoSlipService.Param.PRINT_COUNT, entity.printCount);
		param.put(RoSlipService.Param.COD_SC, entity.codSc);
		param.put(RoSlipService.Param.DC_CATEGORY, entity.dcCategory);
		param.put(RoSlipService.Param.DC_NAME, entity.dcName);
		param.put(RoSlipService.Param.DC_TIMEZONE, entity.dcTimezone);
		param.put(RoSlipService.Param.DC_TIMEZONE_CATEGORY, entity.dcTimezoneCategory);
		return param;

	}

	/**
	 * 商品コードを指定して、受注残数を取得します.
	 *
	 * @param productCode 商品コード
	 * @return 受注残数
	 * @throws ServiceException
	 */
	public int countRestQuantityByProductCode(String productCode)
			throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();

			// 商品コード
			params.put(RoSlipService.Param.PRODUCT_CODE, productCode);

			// 指定商品コードの全親品番を取得する
			List<ProductSetJoin> setProductList = productSetService.findProductSetByChildProductCode(productCode);

			// 親品番を設定
			List<String> setProductCode = null;
			Iterator<ProductSetJoin> it = setProductList.iterator();
			while (it.hasNext()) {
				if (setProductCode==null) {
					setProductCode = new ArrayList<String>();
				}
				ProductSetJoin psj = it.next();
				setProductCode.add(psj.setProductCode);
			}
			params.put(ProductSetService.Param.SET_PRODUCT_CODE, setProductCode);

			StockQuantity result = this.selectBySqlFile(StockQuantity.class,
					"rorder/CountRestQuantityByProductCode.sql", params)
					.getSingleResult();

			if (result != null && result.quantity != null) {
				return result.quantity.intValue();
			}
			return 0;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 受注伝票番号を指定して、受注伝票情報を取得します.
	 * @param id 受注伝票番号
	 * @return 受注伝票DTO
	 * @throws ServiceException
	 */
	@Override
	public ROrderSlipDto loadBySlipId(String id) throws ServiceException {
		if(id == null || id.equals("")){
			return null;
		}

		// 受注伝票データ取得
		RoSlipTrn record = getRoSlipTrn(id);

		// 検索結果が０件の場合はnullを返す
		if (record == null) {
			return null;
		}

		// Entity情報をDTOにコピーする
		ROrderSlipDto trnDto = Beans.createAndCopy(ROrderSlipDto.class, record)
				.dateConverter(Constants.FORMAT.TIMESTAMP, "updDatetm")
				.execute();

		// 支払条件の調整
		trnDto.cutoffGroupCategory = trnDto.cutoffGroup + trnDto.paybackCycleCategory;

		return trnDto;
	}

	/**
	 * 受注伝票の新規登録・更新処理を行います.
	 * @param dto 受注伝票DTO
	 * @param abstractServices 保存で使用するサービス
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int save(ROrderSlipDto dto, AbstractService<?>... abstractServices)
			throws ServiceException, UnabledLockException {
		int lockResult = LockResult.SUCCEEDED;

		if (dto.roSlipId == null || dto.roSlipId.length() == 0) {

			// 受注伝票を登録する
			insertRecord(dto);
		} else {

			// 受注伝票を更新する
			lockResult = updateRecord(dto);
		}
		return lockResult;
	}

	/**
	 * 受注伝票を登録します.
	 * @param dto 受注伝票DTO
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(ROrderSlipDto dto) throws ServiceException {

		// 受注伝票番号を採番
		dto.roSlipId = Long.toString(seqMakerService
				.nextval(RoSlipService.Table.RO_SLIP_TRN));
		NumberConverter convUP = createUnitPriceConverter( dto.priceFractCategory );
		NumberConverter convTax = createUnitPriceConverter( dto.taxFractCategory );

		RoSlipTrn entity = Beans
				.createAndCopy(RoSlipTrn.class, dto)
				.converter(convUP, "retailPriceTotal", "priceTotal", "costTotal")
				.converter(convTax, "ctaxPriceTotal")
				.dateConverter(Constants.FORMAT.DATE, "roDate", "validDate")
				.dateConverter(Constants.FORMAT.DATE, "shipDate",
						"validDate").dateConverter(Constants.FORMAT.DATE,
						"deliveryDate", "validDate").execute();

		// 締日グループと回収間隔を調整
		if (dto.cutoffGroupCategory != null
				&& dto.cutoffGroupCategory.length() == 3) {
			// 前２桁は締日グループ
			entity.cutoffGroup = dto.cutoffGroupCategory.substring(0, 2);

			// 後ろ１桁が回収間隔
			entity.paybackCycleCategory = dto.cutoffGroupCategory
					.substring(2, 3);
		}

		Map<String, Object> param = setEntityToParam(entity);

		return this.updateBySqlFile("rorder/InsertRoSlip.sql", param)
				.execute();
	}

	/**
	 * 受注伝票を更新します.
	 * @param dto 受注伝票DTO
	 * @return ロック結果
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(ROrderSlipDto dto)
			throws UnabledLockException, ServiceException {

		RoSlipTrn entity = Beans.createAndCopy(RoSlipTrn.class, dto)
		.dateConverter(Constants.FORMAT.DATE, "roDate", "validDate")
		.dateConverter(Constants.FORMAT.DATE, "shipDate", "validDate")
		.dateConverter(Constants.FORMAT.DATE, "deliveryDate",
				"validDate").dateConverter(Constants.FORMAT.TIMESTAMP,
				"updDatetm").execute();

		// 締日グループと回収間隔を調整
		if (dto.cutoffGroupCategory != null
				&& dto.cutoffGroupCategory.length() == 3) {
			// 前２桁は締日グループ
			entity.cutoffGroup = dto.cutoffGroupCategory.substring(0, 2);

			// 後ろ１桁が回収間隔
			entity.paybackCycleCategory = dto.cutoffGroupCategory.substring(2,
					3);
		}

		// 排他制御
		int lockResult = this.lockRecord(RoSlipService.Param.RO_SLIP_ID, entity.roSlipId.toString(), entity.updDatetm, "rorder/LockSlip.sql");

		Map<String, Object> param = setEntityToParam(entity);
		this.updateBySqlFile("rorder/UpdateSlip.sql", param).execute();

		return lockResult;
	}

	/**
	 *
	 * @param id 受注伝票番号
	 * @param updDatetm 更新日時
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int deleteById(String id, String updDatetm) throws ServiceException,
			UnabledLockException {

		// 排他制御
		int lockResult = this.lockRecord(RoSlipService.Param.RO_SLIP_ID, id, updDatetm, "rorder/LockSlip.sql");

		// 伝票番号を指定して、見積を削除する
		Map<String, Object> param = createSqlParam();
		param.put(RoSlipService.Param.RO_SLIP_ID, id);
		this.updateBySqlFile("rorder/DeleteSlip.sql", param).execute();

		return lockResult;
	}

	/**
	 * テーブル名を返します.
	 * @return 受注伝票テーブル名
	 */
	@Override
	protected String getTableName() {
		return Table.RO_SLIP_TRN;
	}

	/**
	 * キーカラム名を返します.
	 * @return 受注伝票テーブルのキーカラム名
	 */
	@Override
	protected String getKeyColumnName() {
		return "RO_SLIP_ID";
	}
}
