/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.deposit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.deposit.DepositSlipDto;
import jp.co.arkinfosys.entity.DepositSlip;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 入金検索サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchDepositService extends AbstractService<DepositSlip> {

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String DEPOSIT_SLIP_ID = "depositSlipId";
		public static final String USER_ID = "userId";
		public static final String USER_NAME = "userName";
		public static final String DEPOSIT_DATE = "depositDate";
		public static final String DEPOSIT_DATE_FROM = "depositDateFrom";
		public static final String DEPOSIT_DATE_TO = "depositDateTo";
		public static final String INPUT_PDATE_FROM = "inputPdateFrom";
		public static final String INPUT_PDATE_TO = "inputPdateTo";
		public static final String DEPOSIT_TOTAL_FROM = "depositTotalFrom";
		public static final String DEPOSIT_TOTAL_TO = "depositTotalTo";
		public static final String DEPOSIT_ABSTRACT = "depositAbstract";
		public static final String CUSTOMER_CODE = "customerCode";
		public static final String CUSTOMER_NAME = "customerName";
		public static final String PAYMENT_NAME = "paymentName";
		public static final String DEPOSIT_METHOD_TYPE_CATEGORY = "depositMethodTypeCategory";
		public static final String DEPOSIT_CATEGORY = "depositCategory";
		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";
		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET_ROW = "offsetRow";
		public static final String DEPOSIT_CATEGORY_MST = "depositCategoryMst";
		public static final String DEPOSIT_METHOD_CATEGORY = "depositMethodCategory";
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
	}

	/**
	 * 検索条件を指定して結果件数を返します.
	 * @param params 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public BeanMap getSearchResultCount(BeanMap params) throws ServiceException {
		try {
			return findSlipCntByCondition(params);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果リストを返します.
	 * @param params 検索条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getSearchResult(BeanMap params)
			throws ServiceException {
		try {
			return findSlipByCondition(params);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果件数を返します.
	 *
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public BeanMap findSlipCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return (BeanMap)this.selectBySqlFile(BeanMap.class,
					"deposit/FindSlipCntByCondition.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果リストを返します.
	 *
	 * @param conditions 検索条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findSlipByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class,
					"deposit/FindSlipByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.DEPOSIT_SLIP_ID, null);
		param.put(Param.USER_ID, null);
		param.put(Param.USER_NAME, null);
		param.put(Param.DEPOSIT_DATE_FROM, null);
		param.put(Param.DEPOSIT_DATE_TO, null);
		param.put(Param.INPUT_PDATE_FROM, null);
		param.put(Param.INPUT_PDATE_TO, null);
		param.put(Param.DEPOSIT_TOTAL_FROM, null);
		param.put(Param.DEPOSIT_TOTAL_TO, null);
		param.put(Param.DEPOSIT_ABSTRACT, null);
		param.put(Param.CUSTOMER_CODE, null);
		param.put(Param.CUSTOMER_NAME, null);
		param.put(Param.PAYMENT_NAME, null);
		param.put(Param.DEPOSIT_METHOD_TYPE_CATEGORY, null);
		param.put(Param.DEPOSIT_CATEGORY, null);
		param.put(Param.SORT_COLUMN, null);
		param.put(Param.SORT_ORDER_ASC, null);
		param.put(Param.ROW_COUNT, null);
		param.put(Param.OFFSET_ROW, null);
		param.put(Param.DEPOSIT_CATEGORY_MST, Integer
				.valueOf(Categories.DEPOSIT_CATEGORY));
		param.put(Param.DEPOSIT_METHOD_CATEGORY, Integer
				.valueOf(Categories.DEPOSIT_METHOD_CATEGORY));
		return param;
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	private Map<String, Object> setConditionParam(
			Map<String, Object> conditions, Map<String, Object> param) {

		// 入金番号
		if (conditions.containsKey(Param.DEPOSIT_SLIP_ID)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.DEPOSIT_SLIP_ID))) {
				param.put(Param.DEPOSIT_SLIP_ID, new Long((String) conditions
						.get(Param.DEPOSIT_SLIP_ID)));
			}
		}

		// 入力担当者コード
		if (conditions.containsKey(Param.USER_ID)) {
			if (StringUtil.hasLength((String) conditions.get(Param.USER_ID))) {
				param.put(Param.USER_ID, super
						.createPrefixSearchCondition((String) conditions
								.get(Param.USER_ID)));
			}
		}

		// 入力担当者
		if (conditions.containsKey(Param.USER_NAME)) {
			if (StringUtil.hasLength((String) conditions.get(Param.USER_NAME))) {
				param.put(Param.USER_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(Param.USER_NAME)));
			}
		}

		// 入金日From
		if (conditions.containsKey(Param.DEPOSIT_DATE_FROM)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.DEPOSIT_DATE_FROM))) {
				param.put(Param.DEPOSIT_DATE_FROM, (String) conditions
						.get(Param.DEPOSIT_DATE_FROM));
			}
		}


		// 入金日From全角半角変換
		if (conditions.containsKey(Param.DEPOSIT_DATE_FROM)) {
			param.put(Param.DEPOSIT_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.DEPOSIT_DATE_FROM)));

		}

		// 入金日To
		if (conditions.containsKey(Param.DEPOSIT_DATE_TO)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.DEPOSIT_DATE_TO))) {
				param.put(Param.DEPOSIT_DATE_TO, (String) conditions
						.get(Param.DEPOSIT_DATE_TO));
			}
		}

		// 入金日To全角半角変換
		if (conditions.containsKey(Param.DEPOSIT_DATE_TO)) {
			param.put(Param.DEPOSIT_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.DEPOSIT_DATE_TO)));

		}

		// 入力日From
		if (conditions.containsKey(Param.INPUT_PDATE_FROM)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.INPUT_PDATE_FROM))) {
				param.put(Param.INPUT_PDATE_FROM, (String) conditions
						.get(Param.INPUT_PDATE_FROM));
			}
		}

		// 入力日From全角半角変換
		if (conditions.containsKey(Param.INPUT_PDATE_FROM)) {
			param.put(Param.INPUT_PDATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.INPUT_PDATE_FROM)));

		}

		// 入力日To
		if (conditions.containsKey(Param.INPUT_PDATE_TO)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.INPUT_PDATE_TO))) {
				param.put(Param.INPUT_PDATE_TO, (String) conditions
						.get(Param.INPUT_PDATE_TO));
			}
		}

		// 入力日To全角半角変換
		if (conditions.containsKey(Param.INPUT_PDATE_TO)) {
			param.put(Param.INPUT_PDATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.INPUT_PDATE_TO)));

		}

		// 回収金額From
		if (conditions.containsKey(Param.DEPOSIT_TOTAL_FROM)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.DEPOSIT_TOTAL_FROM))) {
				param.put(Param.DEPOSIT_TOTAL_FROM, new Long(
						(String) conditions.get(Param.DEPOSIT_TOTAL_FROM)));
			}
		}

		// 回収金額To
		if (conditions.containsKey(Param.DEPOSIT_TOTAL_TO)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.DEPOSIT_TOTAL_TO))) {
				param.put(Param.DEPOSIT_TOTAL_TO, new Long((String) conditions
						.get(Param.DEPOSIT_TOTAL_TO)));
			}
		}

		// 摘要
		if (conditions.containsKey(Param.DEPOSIT_ABSTRACT)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.DEPOSIT_ABSTRACT))) {
				param.put(Param.DEPOSIT_ABSTRACT, super
						.createPartialSearchCondition((String) conditions
								.get(Param.DEPOSIT_ABSTRACT)));
			}
		}

		// 顧客コード
		if (conditions.containsKey(Param.CUSTOMER_CODE)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.CUSTOMER_CODE))) {
				param.put(Param.CUSTOMER_CODE, super
						.createPrefixSearchCondition((String) conditions
								.get(Param.CUSTOMER_CODE)));
			}
		}

		// 顧客名
		if (conditions.containsKey(Param.CUSTOMER_NAME)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.CUSTOMER_NAME))) {
				param.put(Param.CUSTOMER_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(Param.CUSTOMER_NAME)));
			}
		}

		// 払込名義
		if (conditions.containsKey(Param.PAYMENT_NAME)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.PAYMENT_NAME))) {
				param.put(Param.PAYMENT_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(Param.PAYMENT_NAME)));
			}
		}

		// 入金取込み
		if (conditions.containsKey(Param.DEPOSIT_METHOD_TYPE_CATEGORY)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.DEPOSIT_METHOD_TYPE_CATEGORY))) {
				param.put(Param.DEPOSIT_METHOD_TYPE_CATEGORY,
						(String) conditions
								.get(Param.DEPOSIT_METHOD_TYPE_CATEGORY));
			}
		}

		// 入金区分
		if (conditions.containsKey(Param.DEPOSIT_CATEGORY)) {
			Object obj = conditions.get(Param.DEPOSIT_CATEGORY);
			if (obj != null) {
				String[] arry = (String[]) obj;
				if (arry.length > 0) {
					param.put(Param.DEPOSIT_CATEGORY, arry);
				}
			}
		}

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
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.DESC);
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

		return param;
	}
}
