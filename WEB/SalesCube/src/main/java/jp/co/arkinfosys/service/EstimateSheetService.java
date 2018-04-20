/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.EstimateSheetTrn;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 *
 * 見積伝票サービスクラスです.
 * @author Ark Information Systems
 */
public class EstimateSheetService extends AbstractSlipService<EstimateSheetTrn, InputEstimateDto> {
	/**
	 *
	 * テーブル定義クラスです.
	 *
	 */
	public static class Table {
		/** 見積伝票テーブル名 */
		public static final String SHEET_TABLE_NAME = "ESTIMATE_SHEET_TRN";
	}

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String ESTIMATE_SHEET_ID = "estimateSheetId";
		public static final String ESTIMATE_ANNUAL = "estimateAnnual";
		public static final String ESTIMATE_MONTHLY = "estimateMonthly";
		public static final String ESTIMATE_YM = "estimateYm";
		public static final String ESTIMATE_DATE = "estimateDate";
		public static final String ESTIMATE_DATE_FROM = "estimateDateFrom";
		public static final String ESTIMATE_DATE_TO = "estimateDateTo";
		public static final String DELIVERY_INFO = "deliveryInfo";
		public static final String VALID_DATE = "validDate";
		public static final String VALID_DATE_FROM = "validDateFrom";
		public static final String VALID_DATE_TO = "validDateTo";
		public static final String USER_ID = "userId";
		public static final String USER_NAME = "userName";
		public static final String REMARKS = "remarks";
		public static final String TITLE = "title";
		public static final String DELIVERY_NAME = "deliveryName";
		public static final String ESTIMATE_CONDITION = "estimateCondition";
		public static final String SUBMIT_NAME = "submitName";
		public static final String SUBMIT_PRE_CATEGORY = "submitPreCategory";
		public static final String SUBMIT_PRE = "submitPre";
		public static final String CUSTOMER_CODE = "customerCode";
		public static final String CUSTOMER_NAME = "customerName";
		public static final String CUSTOMER_REMARKS = "customerRemarks";
		public static final String CUSTOMER_COMMENT_DATA = "customerCommentData";
		public static final String DELIVERY_ZIP_CODE = "deliveryZipCode";
		public static final String CTAX_PRICE_TOTAL = "ctaxPriceTotal";
		public static final String CTAX_RATE = "ctaxRate";
		public static final String COST_TOTAL = "costTotal";
		public static final String RETAIL_PRICE_TOTAL = "retailPriceTotal";
		public static final String ESTIMATE_TOTAL = "estimateTotal";
		public static final String MEMO = "memo";

		public static final String GROSS_MARGIN = "grossMargin";
		public static final String GROSS_MARGIN_RATE = "grossMarginRate";

		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_ORDER = "sortOrder";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";
		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET_ROW = "offsetRow";
		public static final String PRICE_FRACT_CATEGORY = "priceFractCategory";
		public static final String TAX_FRACT_CATEGORY = "taxFractCategory";

	}

	/**
	 *
	 * LIKE検索条件定義クラスです.
	 *
	 */
	public static class LikeType {
		public static final int NOTHING = 0;
		public static final int PREFIX = 1;
		public static final int PARTIAL = 2;

	}

	/**
	 * 見積番号のカラム名
	 */
	public static final String COLUMN_ESTIMATE_SHEET_ID = "ESTIMATE_SHEET_ID";

	/**
	 * 見積日のカラム名
	 */
	public static final String COLUMN_ESTIMATE_DATE = "ESTIMATE_DATE";

	/**
	 * 有効期限のカラム名
	 */
	public static final String COLUMN_VALID_DATE = "VALID_DATE";

	/**
	 * 入力担当者コードのカラム名
	 */
	public static final String COLUMN_USER_ID = "USER_ID";

	/**
	 * 入力担当者名のカラム名
	 */
	public static final String COLUMN_USER_NAME = "USER_NAME";

	/**
	 * 顧客コードのカラム名
	 */
	public static final String COLUMN_CUSTOMER_CODE = "CUSTOMER_CODE";

	/**
	 * 顧客名のカラム名
	 */
	public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";

	/**
	 * 粗利益のカラム名
	 */
	public static final String COLUMN_GROSS_MARGIN = "GROSS_MARGIN";

	/**
	 * 粗利益率のカラム名
	 */
	public static final String OLUMN_GROSS_MARGIN_RATE = "GROSS_MARGIN_RATE";

	/**
	 * 合計金額のカラム名
	 */
	public static final String COLUMN_RETAIL_PRICE_TOTAL = "RETAIL_PRICE_TOTAL";

	/**
	 * 消費税のカラム名
	 */
	public static final String COLUMN_CTAX_PRICE_TOTAL = "CTAX_PRICE_TOTAL";

	/**
	 * 伝票合計のカラム名
	 */
	public static final String COLUMN_ESTIMATE_TOTAL = "ESTIMATE_TOTAL";

	@Resource
	private YmService ymService;

	/**
	 * 見積伝票を登録します.
	 *
	 * @param dto 見積入力伝票DTO
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(InputEstimateDto dto)
			throws ServiceException {

		// 見積伝票番号を採番
		try {
			EstimateSheetTrn entity = Beans.createAndCopy(
					EstimateSheetTrn.class, dto).dateConverter(
					Constants.FORMAT.DATE, "estimateDate", "validDate")
					.execute();

			Map<String, Object> param = setEntityToParam(entity);

			return this.updateBySqlFile("estimate/InsertEstimateSheet.sql",
					param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 見積伝票を更新します.
	 *
	 * @param dto 見積入力伝票DTO
	 * @return ロック結果
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(InputEstimateDto dto)
			throws UnabledLockException, ServiceException {
		EstimateSheetTrn entity = Beans.createAndCopy(EstimateSheetTrn.class, dto)
										.dateConverter(Constants.FORMAT.DATE, "estimateDate", "validDate")
										.dateConverter(Constants.FORMAT.TIMESTAMP, "updDatetm")
										.execute();

		// 排他制御
		int lockResult = this.lockRecord(Param.ESTIMATE_SHEET_ID, entity.estimateSheetId, entity.updDatetm, "estimate/LockEstimateSheet.sql");

		Map<String, Object> param = setEntityToParam(entity);
		this.updateBySqlFile("estimate/UpdateEstimateSheet.sql", param)
				.execute();

		return lockResult;

	}

	/**
	 *
	 * @param id 伝票ID
	 * @param updDatetm 更新日時
	 * @return 削除件数
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int deleteById(String id, String updDatetm) throws ServiceException,
			UnabledLockException {


		// 排他制御
		int lockResult = this.lockRecord(Param.ESTIMATE_SHEET_ID, id, updDatetm, "estimate/LockEstimateSheet.sql");

		// 伝票番号を指定して見積伝票を削除する
		Map<String, Object> param = createSqlParam();
		param.put(Param.ESTIMATE_SHEET_ID, id);
		this.updateBySqlFile("estimate/DeleteEstimateSheet.sql", param)
				.execute();

		return lockResult;
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param entity 見積伝票エンティティ
	 * @return 検索条件パラメータ
	 */
	private Map<String, Object> setEntityToParam(EstimateSheetTrn entity) {
		Map<String, Object> param = super.createSqlParam();

		param.put(Param.ESTIMATE_SHEET_ID, entity.estimateSheetId);
		param.put(Param.ESTIMATE_ANNUAL, entity.estimateAnnual);
		param.put(Param.ESTIMATE_MONTHLY, entity.estimateMonthly);
		param.put(Param.ESTIMATE_YM, entity.estimateYm);
		param.put(Param.ESTIMATE_DATE, entity.estimateDate);
		param.put(Param.DELIVERY_INFO, entity.deliveryInfo);
		param.put(Param.VALID_DATE, entity.validDate);
		param.put(Param.USER_ID, entity.userId);
		param.put(Param.USER_NAME, entity.userName);
		param.put(Param.REMARKS, entity.remarks);
		param.put(Param.TITLE, entity.title);
		param.put(Param.DELIVERY_NAME, entity.deliveryName);
		param.put(Param.ESTIMATE_CONDITION, entity.estimateCondition);
		param.put(Param.SUBMIT_NAME, entity.submitName);
		param.put(Param.SUBMIT_PRE_CATEGORY, entity.submitPreCategory);
		param.put(Param.SUBMIT_PRE, entity.submitPre);
		param.put(Param.CUSTOMER_CODE, entity.customerCode);
		param.put(Param.CUSTOMER_NAME, entity.customerName);
		param.put(Param.CUSTOMER_REMARKS, entity.customerRemarks);
		param.put(Param.CUSTOMER_COMMENT_DATA, entity.customerCommentData);
		param.put(Param.DELIVERY_ZIP_CODE, entity.deliveryZipCode);
		param.put(Param.CTAX_PRICE_TOTAL, entity.ctaxPriceTotal);
		param.put(Param.CTAX_RATE, entity.ctaxRate);
		param.put(Param.COST_TOTAL, entity.costTotal);
		param.put(Param.RETAIL_PRICE_TOTAL, entity.retailPriceTotal);
		param.put(Param.ESTIMATE_TOTAL, entity.estimateTotal);
		param.put(Param.MEMO, entity.memo);
		param.put(Param.PRICE_FRACT_CATEGORY, entity.priceFractCategory);
		param.put(Param.TAX_FRACT_CATEGORY, entity.taxFractCategory);

		return param;

	}

	/**
	 * 検索条件を指定して、結果件数を取得します.
	 *
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public int findEstimateSheetCntByCondition(BeanMap conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			return this.selectBySqlFile(Integer.class,
					"estimate/FindEstimateSheetCntByCondition.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 検索条件を指定して、結果リストを取得します.
	 *
	 * @param conditions 検索条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findEstimateSheetByCondition(
			Map<String, Object> conditions) throws ServiceException {

		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class,
					"estimate/FindEstimateSheetByCondition.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、結果リストを取得します.<br>
	 * findEstimateSheetByCondition(Map<String, Object>)と同一の処理を行います.
	 *
	 * @param conditions 検索条件
	 * @return 結果リスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.EstimateSheetService#findEstimateSheetByCondition(Map)
	 */
	public List<BeanMap> findEstimateSheetByConditionLimit(
			Map<String, Object> conditions) throws ServiceException {

		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class,
					"estimate/FindEstimateSheetByCondition.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、結果リストを取得します.<br>
	 * 見積伝票呼出専用のメソッドです.
	 *
	 *
	 * @param conditions 検索条件
	 * @return 顧客コードが空でない見積伝票のリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findEstimateSheetFromCopySlipByCondition(
			Map<String, Object> conditions) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class,
					"estimate/FindEstimateSheetFromCopySlipByCondition.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}


	/**
	 * 検索条件パラメータを設定します.
	 * @param conditions 検索条件
	 * @param param パラメータ
	 * @param key キー項目
	 * @param likeType LIKE検索条件
	 */
	private void setConditionItemString(Map<String, Object> conditions,
			Map<String, Object> param, String key, int likeType) {
		if (!conditions.containsKey(key)) {
			return;
		}
		String value = (String) conditions.get(key);
		if (!StringUtil.hasLength(value)) {
			return;
		}

		if (likeType == LikeType.PARTIAL) {
			value = createPartialSearchCondition(value);
		} else if (likeType == LikeType.PREFIX) {
			value = createPrefixSearchCondition(value);
		}

		param.put(key, value);

	}

	/**
	 * 検索条件パラメータを設定します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 */
	private void setConditionParam(Map<String, Object> conditions,
			Map<String, Object> param) {

		setConditionItemString(conditions, param, Param.ESTIMATE_SHEET_ID,
				LikeType.PREFIX);

		// 見積日From
		setConditionItemString(conditions, param, Param.ESTIMATE_DATE_FROM,
				LikeType.NOTHING);

		// 見積日From全角半角変換
		if (conditions.containsKey(Param.ESTIMATE_DATE_FROM)) {
			param.put(Param.ESTIMATE_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.ESTIMATE_DATE_FROM)));

		}
		// 見積日To
		setConditionItemString(conditions, param, Param.ESTIMATE_DATE_TO,
				LikeType.NOTHING);

		// 見積日To 全角半角変換
		if (conditions.containsKey(Param.ESTIMATE_DATE_TO)) {
			param.put(Param.ESTIMATE_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.ESTIMATE_DATE_TO)));

		}

		// 有効期限From
		setConditionItemString(conditions, param, Param.VALID_DATE_FROM,
				LikeType.NOTHING);


		// 有効期限From 全角半角変換
		if (conditions.containsKey(Param.VALID_DATE_FROM)) {
			param.put(Param.VALID_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.VALID_DATE_FROM)));

		}

		// 有効期限To
		setConditionItemString(conditions, param, Param.VALID_DATE_TO,
				LikeType.NOTHING);


		// 有効期限To 全角半角変換
		if (conditions.containsKey(Param.VALID_DATE_TO)) {
			param.put(Param.VALID_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.VALID_DATE_TO)));

		}

		// 入力担当者コード
		setConditionItemString(conditions, param, Param.USER_ID,
				LikeType.PREFIX);

		// 入力担当者名
		setConditionItemString(conditions, param, Param.USER_NAME,
				LikeType.PARTIAL);

		// 件名
		setConditionItemString(conditions, param, Param.TITLE,
				LikeType.PARTIAL);

		// 摘要
		setConditionItemString(conditions, param, Param.REMARKS,
				LikeType.PARTIAL);

		// 提出先名
		setConditionItemString(conditions, param, Param.SUBMIT_NAME,
				LikeType.PARTIAL);

		// 顧客コード
		setConditionItemString(conditions, param, Param.CUSTOMER_CODE,
				LikeType.PREFIX);

		// 顧客名
		setConditionItemString(conditions, param, Param.CUSTOMER_NAME,
				LikeType.PARTIAL);

		// ソートカラムを設定する
		if (conditions.containsKey(Param.SORT_COLUMN)) {
			if (StringUtil
					.hasLength((String) conditions.get(Param.SORT_COLUMN))) {
				param.put(Param.SORT_COLUMN, StringUtil
						.convertColumnName((String) conditions
								.get(Param.SORT_COLUMN)));
			}
		}
		// ソートオーダーを設定する
		Boolean sortOrderAsc = (Boolean) conditions.get(Param.SORT_ORDER_ASC);
		if (sortOrderAsc) {
			param.put(Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER, Constants.SQL.DESC);
		}

		// 表示件数を設定する
		if (conditions.containsKey(Param.ROW_COUNT)) {
			param.put(Param.ROW_COUNT, conditions
					.get(Param.ROW_COUNT));
		}

		// オフセットを設定する
		if (conditions.containsKey(Param.OFFSET_ROW)) {
			param.put(Param.OFFSET_ROW, conditions.get(Param.OFFSET_ROW));
		}

	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {

		// 見積番号
		param.put(Param.ESTIMATE_SHEET_ID, null);

		// 見積日From
		param.put(Param.ESTIMATE_DATE_FROM, null);

		// 見積日To
		param.put(Param.ESTIMATE_DATE_TO, null);

		// 有効期限From
		param.put(Param.VALID_DATE_FROM, null);

		// 有効期限To
		param.put(Param.VALID_DATE_TO, null);

		// 入力担当者コード
		param.put(Param.USER_ID, null);

		// 入力担当者名
		param.put(Param.USER_NAME, null);

		// 件名
		param.put(Param.TITLE, null);

		// 摘要
		param.put(Param.REMARKS, null);

		// 提出先名
		param.put(Param.SUBMIT_NAME, null);

		// 顧客コード
		param.put(Param.CUSTOMER_CODE, null);

		// 顧客名
		param.put(Param.CUSTOMER_NAME, null);

		// 顧客名
		param.put(Param.DELIVERY_ZIP_CODE, null);
		// ソートカラムを設定する
		param.put(Param.SORT_COLUMN, null);
		// ソートオーダーを設定する
		param.put(Param.SORT_ORDER, null);

		param.put(Param.ROW_COUNT, null);

		param.put(Param.OFFSET_ROW, null);

		return param;

	}

	/**
	 * 見積伝票番号を指定して、見積伝票情報を取得します.
	 *
	 * @param estimateSheetId 見積番号
	 * @return 見積伝票情報
	 * @throws ServiceException
	 */
	public BeanMap findEstimateSheetByIdSimple(String estimateSheetId)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.ESTIMATE_SHEET_ID, estimateSheetId);
			return this.selectBySqlFile(BeanMap.class,
					"estimate/FindEstimateSheetById.sql", param)
					.getSingleResult();
		} catch (SNonUniqueResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 見積伝票の新規登録・更新処理を行います.
	 * @param dto 見積入力伝票DTO
	 * @param abstractServices 保存で使用するサービス
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int save(InputEstimateDto dto, AbstractService<?>... abstractServices)
			throws ServiceException, UnabledLockException {

		CustomerService customerService = (CustomerService) abstractServices[0];

		// 見積年度、月度、年月度を計算
		YmDto ymDto = ymService.getYm(dto.estimateDate);
		if (ymDto == null) {
			// DTOの中身を空にしておく
			dto.estimateAnnual = "";
			dto.estimateMonthly = "";
			dto.estimateYm = "";
		} else {
			dto.estimateAnnual = ymDto.annual.toString();
			dto.estimateMonthly = ymDto.monthly.toString();
			dto.estimateYm = ymDto.ym.toString();
		}

		if(StringUtil.hasLength(dto.customerCode)){
			Customer c = customerService.findCustomerByCode(dto.customerCode);
			if(c != null){
				// カラム名はdelivery（納入先）だが顧客の郵便番号を登録する
				dto.deliveryZipCode = c.customerZipCode;
			}
		}

		int lockResult = LockResult.SUCCEEDED;

		if (dto.newData == null || dto.newData ) {
			// 見積を登録する
			insertRecord(dto);
		} else {
			// 見積を更新する
			lockResult = updateRecord(dto);
		}
		return lockResult;
	}

	/**
	 * 見積伝票番号を指定して、見積伝票情報を取得します.
	 *
	 * @param id 見積伝票番号
	 * @return 見積伝票DTO
	 * @throws ServiceException
	 */
	@Override
	public InputEstimateDto loadBySlipId(String id) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();

			param.put(Param.ESTIMATE_SHEET_ID, id);

			EstimateSheetTrn entity = this.selectBySqlFile(
					EstimateSheetTrn.class,
					"estimate/FindEstimateSheetById.sql", param)
					.getSingleResult();

			if (entity != null) {
				InputEstimateDto dto = new InputEstimateDto();
				Beans.copy(entity, dto).dateConverter(Constants.FORMAT.DATE,
						"estimateDate", "validDate").dateConverter(
						Constants.FORMAT.TIMESTAMP, "updDatetm").execute();

				return dto;
			}
			return null;
		} catch (SNonUniqueResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * キーカラム名を返します.
	 * @return 見積伝票テーブルのキーカラム名
	 */
	@Override
	protected String getTableName() {
		return Table.SHEET_TABLE_NAME;
	}

	/**
	 * テーブル名を返します.
	 * @return 見積伝票テーブル名
	 */
	@Override
	protected String getKeyColumnName() {
		return COLUMN_ESTIMATE_SHEET_ID;
	}
}
