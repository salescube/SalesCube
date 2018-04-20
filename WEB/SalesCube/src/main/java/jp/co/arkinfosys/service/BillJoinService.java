/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.join.BillJoin;
import jp.co.arkinfosys.service.EstimateSheetService.LikeType;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;

/**
 * 請求書エンティティサービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class BillJoinService extends AbstractService<BillJoin> {

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String SORT_COLUMN = "sortColumn";
		// ソート方向
		private static final String SORT_ORDER = "sortOrder";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";
		// 取得件数
		public static final String ROW_COUNT = "rowCount";
		// 取得件数
		public static final String OFFSET_ROW = "offsetRow";

		// 顧客コード
		public static final String CUSTOMER_CODE = "customerCode";
		// 顧客名
		public static final String CUSTOMER_NAME = "customerName";
		// 請求締日
		public static final String BILL_CUTOFF_DATE = "billCutoffDate";
		// 請求締日From
		public static final String BILL_CUTOFF_DATE_FROM = "billCutoffDateFrom";
		// 請求締日To
		public static final String BILL_CUTOFF_DATE_TO = "billCutoffDateTo";
		// 請求書作成区分
		public static final String BILL_CRT_CATEGORY = "billCrtCategory";
		// 締日グループ
		public static final String CUTOFF_GROUP = "cutoffGroup";
		// 支払条件
		public static final String CUTOFF_GROUP_CATEGORY = "cutoffGroupCategory";
		// 回収間隔
		public static final String PAYBACK_CYCLE_CATEGORY = "paybackCycleCategory";
		// 取引区分
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
		// 請求締日のソート条件
		private static final String SORT_COLUMN_CUTOFF_DATE = "sortColumnBillCutoffDate";
		// 顧客コードのソート条件
		private static final String SORT_COLUMN_CUSTOMER_CODE = "sortColumnCustomerCode";
		// 請求書番号
		public static final String BILL_ID = "billId";
		// 請求書種別
		private static final String BILL_CATEGORY = "billCategory";
		// 最終売上日
		private static final String LAST_SALES_DATE = "lastSalesDate";
		// 最終売上日From
		private static final String LAST_SALES_DATE_FROM = "lastSalesDateFrom";
		// 最終売上日To
		private static final String LAST_SALES_DATE_TO = "lastSalesDateTo";
		// 最終請求書発行日
		private static final String LAST_PRINT_DATE = "lastPrintDate";
		// 最終請求書発行日From
		private static final String LAST_PRINT_DATE_FROM = "lastPrintDateFrom";
		// 最終請求書発行日To
		private static final String LAST_PRINT_DATE_TO = "lastPrintDateTo";
		// 請求書作成区分の区分マスタコード
		private static final String BILL_CRT_CATEGORY_CODE = "billCrtCategoryCode";
		// 発行済を除く
		private static final String EXCLUDE_PRINT = "excludePrint";

		// 繰越金額　なし
		private static final String COV_PRICE_ZERO = "covPriceZero";
		// 繰越金額　過入金
		private static final String COV_PRICE_MINUS = "covPriceMinus";
		// 繰越金額　不足
		private static final String COV_PRICE_PLUS = "covPricePlus";
		// 今回請求金額　あり
		private static final String THIS_BILL_PLUS = "thisBillPricePlus";
		// 今回請求金額　なし
		private static final String THIS_BILL_ZERO = "thisBillPriceZero";
		// 今回請求金額　過入金
		private static final String THIS_BILL_MINUS = "thisBillPriceMinus";

	}

	public String[] paramNames = { Param.SORT_COLUMN, Param.SORT_ORDER,
			Param.SORT_ORDER_ASC,
			Param.ROW_COUNT, Param.OFFSET_ROW,
			Param.CUSTOMER_CODE, Param.CUSTOMER_NAME, Param.BILL_CUTOFF_DATE,
			Param.BILL_CUTOFF_DATE_FROM, Param.BILL_CUTOFF_DATE_TO,
			Param.BILL_CRT_CATEGORY, Param.CUTOFF_GROUP,
			Param.CUTOFF_GROUP_CATEGORY, Param.PAYBACK_CYCLE_CATEGORY,
			Param.SALES_CM_CATEGORY, Param.SORT_COLUMN_CUTOFF_DATE,
			Param.SORT_COLUMN_CUSTOMER_CODE, Param.BILL_ID,
			Param.BILL_CATEGORY, Param.LAST_SALES_DATE,
			Param.LAST_SALES_DATE_FROM, Param.LAST_SALES_DATE_TO,
			Param.LAST_PRINT_DATE, Param.LAST_PRINT_DATE_FROM,
			Param.LAST_PRINT_DATE_TO, Param.BILL_CRT_CATEGORY_CODE,
			Param.COV_PRICE_ZERO, Param.COV_PRICE_MINUS, Param.COV_PRICE_PLUS,
			Param.THIS_BILL_PLUS, Param.THIS_BILL_ZERO, Param.THIS_BILL_MINUS,
			Param.EXCLUDE_PRINT };

	public static final String SORT_COLUMN_BILL_CUTOFF_DATE = "BILL_CUTOFF_DATE";
	public static final String SORT_COLUMN_CUSTOMER_CODE = "CUSTOMER_CODE";

	/**
	 * 売掛以外の顧客の直近の月締め請求書を取得します.
	 * @return 請求書エンティティのリスト
	 * @throws ServiceException
	 */
	public List<BillJoin> findLastBillOfCash() throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		conditions.put(Param.BILL_CRT_CATEGORY, CategoryTrns.BILL_CRT_BILL);
		conditions.put(Param.SALES_CM_CATEGORY, CategoryTrns.SALES_CM_CREDIT);

		return findByCondition(conditions, paramNames,
				"bill/FindLastBillCash.sql");

	}

	/**
	 * 売掛の顧客の直近の月締め請求書を取得します.
	 * @param customerCode 顧客コード
	 * @param customerName 顧客名
	 * @param cutoffGroup 締日グループ
	 * @param paybackCycleCategory 回収間隔
	 * @return 請求書エンティティのリスト
	 * @throws ServiceException
	 */
	public List<BillJoin> findLastBillOfAR(String customerCode,
			String customerName, String cutoffGroup, String paybackCycleCategory)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.CUSTOMER_NAME, customerName);
		conditions.put(Param.CUTOFF_GROUP, cutoffGroup);
		conditions.put(Param.PAYBACK_CYCLE_CATEGORY, paybackCycleCategory);
		conditions.put(Param.BILL_CRT_CATEGORY, CategoryTrns.BILL_CRT_BILL);
		conditions.put(Param.SALES_CM_CATEGORY, CategoryTrns.SALES_CM_CREDIT);
		conditions.put(Param.SORT_COLUMN_CUSTOMER_CODE,
				SORT_COLUMN_CUSTOMER_CODE);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);

		return findByCondition(conditions, paramNames,
				"bill/FindLastBillCredit.sql");

	}

	/**
	 * 検索条件パラメータにLike検索条件を設定します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @param key キー項目
	 * @param likeType Like検索条件
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
	 * 検索条件パラメータを設定して返します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	private void setConditionParam(Map<String, Object> conditions,
			Map<String, Object> param) {

		// 請求書番号
		if (conditions.containsKey(Param.BILL_ID)) {
			if (StringUtil.hasLength((String) conditions.get(Param.BILL_ID))) {
				param.put(Param.BILL_ID, new Long((String) conditions
						.get(Param.BILL_ID)));
			}
		}

		// 請求書発行日From
		setConditionItemString(conditions, param, Param.LAST_PRINT_DATE_FROM,
				LikeType.NOTHING);

		// 請求書発行日From全角半角変換
		if (conditions.containsKey(Param.LAST_PRINT_DATE_FROM)) {
			param.put(Param.LAST_PRINT_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.LAST_PRINT_DATE_FROM)));
		}

		// 請求書発行日To
		setConditionItemString(conditions, param, Param.LAST_PRINT_DATE_TO,
				LikeType.NOTHING);

		//  請求書発行日To全角半角変換
		if (conditions.containsKey(Param.LAST_PRINT_DATE_TO)) {
			param.put(Param.LAST_PRINT_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.LAST_PRINT_DATE_TO)));
		}

		// 請求締日From
		setConditionItemString(conditions, param, Param.BILL_CUTOFF_DATE_FROM,
				LikeType.NOTHING);

		//  請求締日From全角半角変換
		if (conditions.containsKey(Param.BILL_CUTOFF_DATE_FROM)) {
			param.put(Param.BILL_CUTOFF_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.BILL_CUTOFF_DATE_FROM)));
		}

		// 請求締日To
		setConditionItemString(conditions, param, Param.BILL_CUTOFF_DATE_TO,
				LikeType.NOTHING);

		//  請求締日To全角半角変換
		if (conditions.containsKey(Param.BILL_CUTOFF_DATE_TO)) {
			param.put(Param.BILL_CUTOFF_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.BILL_CUTOFF_DATE_TO)));

		}
		// 最終売上日From
		setConditionItemString(conditions, param, Param.LAST_SALES_DATE_FROM,
				LikeType.NOTHING);

		// 最終売上日From全角半角変換
		if (conditions.containsKey(Param.LAST_SALES_DATE_FROM)) {
			param.put(Param.LAST_SALES_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.LAST_SALES_DATE_FROM)));
		}

		// 最終売上日To
		setConditionItemString(conditions, param, Param.LAST_SALES_DATE_TO,
				LikeType.NOTHING);

		// 最終売上日To全角半角変換
		if (conditions.containsKey(Param.LAST_SALES_DATE_TO)) {
			param.put(Param.LAST_SALES_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.LAST_SALES_DATE_TO)));
		}

		// 請求書分類
		setConditionItemString(conditions, param, Param.BILL_CRT_CATEGORY,
				LikeType.NOTHING);
		// 請求書分類区分マスタコード
		param.put(Param.BILL_CRT_CATEGORY_CODE, Categories.BILL_CRT_CATEGORY);

		// 請求書種別
		setConditionItemString(conditions, param, Param.BILL_CATEGORY,
				LikeType.NOTHING);

		// 支払条件(回収間隔）
		setConditionItemString(conditions, param, Param.PAYBACK_CYCLE_CATEGORY,
				LikeType.NOTHING);

		// 支払条件(締日グループ）
		setConditionItemString(conditions, param, Param.CUTOFF_GROUP,
				LikeType.NOTHING);

		// 支払条件
		setConditionItemString(conditions, param, Param.CUTOFF_GROUP_CATEGORY,
				LikeType.NOTHING);

		// 顧客コード
		setConditionItemString(conditions, param, Param.CUSTOMER_CODE,
				LikeType.PREFIX);

		// 顧客名
		setConditionItemString(conditions, param, Param.CUSTOMER_NAME,
				LikeType.PARTIAL);

		// 繰越金額　なし
		if (conditions.containsKey(Param.COV_PRICE_ZERO)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.COV_PRICE_ZERO))) {
				param.put(Param.COV_PRICE_ZERO, conditions
						.get(Param.COV_PRICE_ZERO));
			}
		}
		// 繰越金額　過入金
		if (conditions.containsKey(Param.COV_PRICE_MINUS)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.COV_PRICE_MINUS))) {
				param.put(Param.COV_PRICE_MINUS, conditions
						.get(Param.COV_PRICE_MINUS));
			}
		}
		// 繰越金額　不足
		if (conditions.containsKey(Param.COV_PRICE_PLUS)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.COV_PRICE_PLUS))) {
				param.put(Param.COV_PRICE_PLUS, conditions
						.get(Param.COV_PRICE_PLUS));
			}
		}
		// 今回請求金額　あり
		if (conditions.containsKey(Param.THIS_BILL_PLUS)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.THIS_BILL_PLUS))) {
				param.put(Param.THIS_BILL_PLUS, conditions
						.get(Param.THIS_BILL_PLUS));
			}
		}
		// 今回請求金額　なし
		if (conditions.containsKey(Param.THIS_BILL_ZERO)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.THIS_BILL_ZERO))) {
				param.put(Param.THIS_BILL_ZERO, conditions
						.get(Param.THIS_BILL_ZERO));
			}
		}
		// 今回請求金額　過入金
		if (conditions.containsKey(Param.THIS_BILL_MINUS)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.THIS_BILL_MINUS))) {
				param.put(Param.THIS_BILL_MINUS, conditions
						.get(Param.THIS_BILL_MINUS));
			}
		}

		// 発行済を除く
		if (conditions.containsKey(Param.EXCLUDE_PRINT)) {
			Boolean value = (Boolean) conditions.get(Param.EXCLUDE_PRINT);
			if (value) {
				param.put(Param.EXCLUDE_PRINT, "ON");
			}
		}

		// ソートカラムを設定する
		if (conditions.containsKey(Param.SORT_COLUMN)) {
			if (StringUtil
					.hasLength((String) conditions.get(Param.SORT_COLUMN))) {
				param.put(Param.SORT_COLUMN, super.convertVariableNameToColumnName((String) conditions
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

		// 請求書番号
		param.put(Param.BILL_ID, null);

		// 請求書発行日From
		param.put(Param.LAST_PRINT_DATE_FROM, null);

		// 請求書発行日To
		param.put(Param.LAST_PRINT_DATE_TO, null);

		// 請求締日From
		param.put(Param.BILL_CUTOFF_DATE_FROM, null);

		// 請求締日To
		param.put(Param.BILL_CUTOFF_DATE_TO, null);

		// 最終売上日From
		param.put(Param.LAST_SALES_DATE_FROM, null);

		// 最終売上日To
		param.put(Param.LAST_SALES_DATE_TO, null);

		// 請求書分類
		param.put(Param.BILL_CRT_CATEGORY, null);

		// 請求書種別
		param.put(Param.BILL_CATEGORY, null);

		// 支払条件(回収間隔）
		param.put(Param.PAYBACK_CYCLE_CATEGORY, null);

		// 支払条件(締日グループ）
		param.put(Param.CUTOFF_GROUP, null);

		// 支払条件
		param.put(Param.CUTOFF_GROUP_CATEGORY, null);

		// 顧客コード
		param.put(Param.CUSTOMER_CODE, null);

		// 顧客名
		param.put(Param.CUSTOMER_NAME, null);

		// 繰越金額　なし
		param.put(Param.COV_PRICE_ZERO, null);

		// 繰越金額　過入金
		param.put(Param.COV_PRICE_MINUS, null);

		// 繰越金額　不足
		param.put(Param.COV_PRICE_PLUS, null);

		// 今回請求金額　あり
		param.put(Param.THIS_BILL_PLUS, null);

		// 今回請求金額　なし
		param.put(Param.THIS_BILL_ZERO, null);

		// 今回請求金額　過入金
		param.put(Param.THIS_BILL_MINUS, null);

		// 発行済を除く
		param.put(Param.EXCLUDE_PRINT, null);

		// ソートカラムを設定する
		param.put(Param.SORT_COLUMN, null);
		// ソートオーダーを設定する
		param.put(Param.SORT_ORDER, null);

		param.put(Param.ROW_COUNT, null);

		param.put(Param.OFFSET_ROW, null);

		return param;

	}

	/**
	 * 検索条件を指定して、結果件数を取得します.
	 *
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public int findBillCntByCondition(BeanMap conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			return this.selectBySqlFile(Integer.class,
					"bill/FindBillCntByCondition.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 検索条件を指定して、請求書情報を取得します.
	 * @param conditions 検索条件
	 * @return 請求書情報リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findBillByCondition(Map<String, Object> conditions)
			throws ServiceException {

		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class,
					"bill/FindBillByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 取得範囲を指定して、請求書情報を取得します.
	 * @param conditions 取得範囲を含む検索条件
	 * @return 請求書情報リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findBillByConditionLimit(Map<String, Object> conditions)
			throws ServiceException {

		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class,
					"bill/FindBillByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、請求書発行対象の請求書情報を取得します.
	 * @param conditions 検索条件
	 * @return 請求書情報リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findBillForMakeOut(Map<String, Object> conditions)
			throws ServiceException {

		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			param.put(Param.BILL_CRT_CATEGORY, CategoryTrns.BILL_CRT_BILL);

			return this.selectBySqlFile(BeanMap.class,
					"bill/FindBillForMakeOut.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、請求書発行対象の結果件数を取得します.
	 *
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public int countBillForMakeOut(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			param.put(Param.BILL_CRT_CATEGORY, CategoryTrns.BILL_CRT_BILL);

			return this.selectBySqlFile(Integer.class,
					"bill/CountBillForMakeOut.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}
}
