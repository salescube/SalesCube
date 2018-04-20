/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.payment;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.PaymentSlipTrn;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;

/**
 *
 * 支払検索サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchPaymentService extends AbstractService<PaymentSlipTrn> {

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String SEARCH_TARGET = "searchTarget";
		public static final String PAYMENT_SLIP_ID = "paymentSlipId";
		public static final String PO_SLIP_ID = "poSlipId";
		public static final String SUPPLIER_SLIP_ID = "supplierSlipId";
		public static final String PAYMENT_DATE_FROM = "paymentDateFrom";
		public static final String PAYMENT_DATE_TO = "paymentDateTo";
		public static final String SUPPLIER_CODE = "supplierCode";
		public static final String SUPPLIER_NAME = "supplierName";
		public static final String PRODUCT_CODE = "productCode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String PRODUCT1 = "product1";
		public static final String PRODUCT2 = "product2";
		public static final String PRODUCT3 = "product3";
		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";
		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET_ROW = "offsetRow";
		public static final String PAYMENT_SLIP_STATUS = "paymentSlipStatus";
		public static final String PAYMENT_LINE_STATUS = "paymentLineStatus";
		public static final String PAYMENT_DETAIL = "paymentDetail";
		public static final String SORT_COLUMN_SLIP = "sortColumnSlip";
		public static final String SORT_COLUMN_LINE = "sortColumnLine";
	}

	/**
	 *
	 * カラム定義クラスです.
	 *
	 */
	public static class Column {
		public static final String SUPPLIER_LINE_ID = "SUPPLIER_LINE_ID";
		public static final String PAYMENT_SLIP_ID = "PAYMENT_SLIP_ID";
		public static final String PO_LINE_ID = "PO_LINE_ID";

		public static final String SORT_SUPPLIER_SLIP_ID = "SORT_SUPPLIER_SLIP_ID";
		public static final String SORT_SUPPLIER_LINE_NO = "SORT_SUPPLIER_LINE_NO";
		public static final String SORT_PAYMENT_SLIP_ID = "SORT_PAYMENT_SLIP_ID";
		public static final String SORT_PAYMENT_LINE_NO = "SORT_PAYMENT_LINE_NO";
		public static final String SORT_PO_SLIP_ID = "SORT_PO_SLIP_ID";
		public static final String SORT_PO_LINE_NO = "SORT_PO_LINE_NO";
	}

	/**
	 * 検索条件を指定して結果件数を返します.
	 * @param params 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer getSearchResultCount(BeanMap params) throws ServiceException {
		try {
			Integer count = Integer.valueOf(0);

			// 検索対象を取得する
			String searchTarget = (String)params.get(Param.SEARCH_TARGET);

			// 伝票単位か明細単位か
			if(Constants.SEARCH_TARGET.VALUE_SLIP.equals(searchTarget)) {
				count = findSlipCntByCondition(params);
			} else if(Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {
				count = findSlipLineCntByCondition(params);
			}

			return count;
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
	public List<BeanMap> getSearchResult(BeanMap params) throws ServiceException {
		try {
			// 検索対象を取得する
			String searchTarget = (String)params.get(Param.SEARCH_TARGET);

			// 伝票単位か明細単位か
			if(Constants.SEARCH_TARGET.VALUE_SLIP.equals(searchTarget)) {
				return findSlipByCondition(params);
			} else if(Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {
				return findSlipLineByCondition(params);
			}

			return null;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して伝票単位の結果件数を返します.
	 *
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer findSlipCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"payment/FindSlipCntByCondition.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して明細行単位の結果件数を返します.
	 *
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer findSlipLineCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"payment/FindSlipLineCntByCondition.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して伝票単位の結果リストを返します.
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
					"payment/FindSlipByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して明細行単位の結果リストを返します.
	 *
	 * @param conditions 検索条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findSlipLineByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			// 特定のカラムがsortキーに指定された場合は伝票、行番をキーにする
			String sortColumn = (String)param.get(Param.SORT_COLUMN);
			if (Column.SUPPLIER_LINE_ID.equals(sortColumn)) {
				// 仕入番号 - 行
				param.put(Param.SORT_COLUMN_SLIP, Column.SORT_SUPPLIER_SLIP_ID);
				param.put(Param.SORT_COLUMN_LINE, Column.SORT_SUPPLIER_LINE_NO);
				param.put(Param.SORT_COLUMN, null);
			}else if(Column.PO_LINE_ID.equals(sortColumn)){
				// 発注番号 - 行
				param.put(Param.SORT_COLUMN_SLIP, Column.SORT_PO_SLIP_ID);
				param.put(Param.SORT_COLUMN_LINE, Column.SORT_PO_LINE_NO);
				param.put(Param.SORT_COLUMN, null);
			}else if(Column.PAYMENT_SLIP_ID.equals(sortColumn)){
				// 支払番号 - 行
				param.put(Param.SORT_COLUMN_SLIP, Column.SORT_PAYMENT_SLIP_ID);
				param.put(Param.SORT_COLUMN_LINE, Column.SORT_PAYMENT_LINE_NO);
				param.put(Param.SORT_COLUMN, null);
			}

			return this.selectBySqlFile(BeanMap.class,
					"payment/FindSlipLineByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 空の検索条件オブジェクトを作成します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.SEARCH_TARGET, null);
		param.put(Param.PAYMENT_SLIP_ID, null);
		param.put(Param.PO_SLIP_ID, null);
		param.put(Param.SUPPLIER_SLIP_ID, null);
		param.put(Param.PAYMENT_DATE_FROM, null);
		param.put(Param.PAYMENT_DATE_TO, null);
		param.put(Param.SUPPLIER_CODE, null);
		param.put(Param.SUPPLIER_NAME, null);
		param.put(Param.PRODUCT_CODE, null);
		param.put(Param.PRODUCT_ABSTRACT, null);
		param.put(Param.PRODUCT1, null);
		param.put(Param.PRODUCT2, null);
		param.put(Param.PRODUCT3, null);
		param.put(Param.SORT_COLUMN, null);
		param.put(Param.SORT_ORDER_ASC, null);
		param.put(Param.ROW_COUNT, null);
		param.put(Param.OFFSET_ROW, null);
		param.put(Param.PAYMENT_SLIP_STATUS, Integer.valueOf(SlipStatusCategories.PAYMENT_SLIP_STATUS));
		param.put(Param.PAYMENT_LINE_STATUS, Integer.valueOf(SlipStatusCategories.PAYMENT_LINE_STATUS));
		param.put(Param.PAYMENT_DETAIL, Integer.valueOf(Categories.PAYMENT_DETAIL));
		param.put(Param.SORT_COLUMN_SLIP, null);
		param.put(Param.SORT_COLUMN_LINE, null);
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

		// 支払番号
		if (conditions.containsKey(Param.PAYMENT_SLIP_ID)) {
			if (StringUtil.hasLength((String)conditions.get(Param.PAYMENT_SLIP_ID))) {
				param.put(Param.PAYMENT_SLIP_ID,new Long((String)conditions.get(Param.PAYMENT_SLIP_ID)));
			}
		}

		// 発注番号
		if (conditions.containsKey(Param.PO_SLIP_ID)) {
			if (StringUtil.hasLength((String)conditions.get(Param.PO_SLIP_ID))) {
				param.put(Param.PO_SLIP_ID,new Long((String)conditions.get(Param.PO_SLIP_ID)));
			}
		}

		// 仕入番号
		if (conditions.containsKey(Param.SUPPLIER_SLIP_ID)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SUPPLIER_SLIP_ID))) {
				param.put(Param.SUPPLIER_SLIP_ID,new Long((String)conditions.get(Param.SUPPLIER_SLIP_ID)));
			}
		}

		// 支払日From
		if (conditions.containsKey(Param.PAYMENT_DATE_FROM)) {
			if (StringUtil.hasLength((String)conditions.get(Param.PAYMENT_DATE_FROM))) {
				param.put(Param.PAYMENT_DATE_FROM,(String)conditions.get(Param.PAYMENT_DATE_FROM));
			}
		}

		// 支払日From 全角半角変換
		if (conditions.containsKey(Param.PAYMENT_DATE_FROM)) {
			param.put(Param.PAYMENT_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.PAYMENT_DATE_FROM)));

		}

		// 支払日To
		if (conditions.containsKey(Param.PAYMENT_DATE_TO)) {
			if (StringUtil.hasLength((String)conditions.get(Param.PAYMENT_DATE_TO))) {
				param.put(Param.PAYMENT_DATE_TO,(String)conditions.get(Param.PAYMENT_DATE_TO));
			}
		}

		// 支払日To 全角半角変換
		if (conditions.containsKey(Param.PAYMENT_DATE_TO)) {
			param.put(Param.PAYMENT_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.PAYMENT_DATE_TO)));

		}

		// 仕入先コード
		if (conditions.containsKey(Param.SUPPLIER_CODE)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SUPPLIER_CODE))) {
				param.put(Param.SUPPLIER_CODE,
					super.createPrefixSearchCondition((String)conditions.get(Param.SUPPLIER_CODE)));
			}
		}

		// 仕入先名
		if (conditions.containsKey(Param.SUPPLIER_NAME)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SUPPLIER_NAME))) {
				param.put(Param.SUPPLIER_NAME,
					super.createPartialSearchCondition((String)conditions.get(Param.SUPPLIER_NAME)));
			}
		}

		// 商品コード
		if (conditions.containsKey(Param.PRODUCT_CODE)) {
			if (StringUtil.hasLength((String)conditions.get(Param.PRODUCT_CODE))) {
				param.put(Param.PRODUCT_CODE,
					super.createPrefixSearchCondition((String)conditions.get(Param.PRODUCT_CODE)));
			}
		}

		// 商品名
		if (conditions.containsKey(Param.PRODUCT_ABSTRACT)) {
			if (StringUtil.hasLength((String)conditions.get(Param.PRODUCT_ABSTRACT))) {
				param.put(Param.PRODUCT_ABSTRACT,
					super.createPartialSearchCondition((String)conditions.get(Param.PRODUCT_ABSTRACT)));
			}
		}

		// 分類（大）
		if (conditions.containsKey(Param.PRODUCT1)) {
			if (StringUtil.hasLength((String)conditions.get(Param.PRODUCT1))) {
				param.put(Param.PRODUCT1,(String)conditions.get(Param.PRODUCT1));
			}
		}

		// 分類（中）
		if (conditions.containsKey(Param.PRODUCT2)) {
			if (StringUtil.hasLength((String)conditions.get(Param.PRODUCT2))) {
				param.put(Param.PRODUCT2,(String)conditions.get(Param.PRODUCT2));
			}
		}

		// 分類（小）
		if (conditions.containsKey(Param.PRODUCT3)) {
			if (StringUtil.hasLength((String)conditions.get(Param.PRODUCT3))) {
				param.put(Param.PRODUCT3,(String)conditions.get(Param.PRODUCT3));
			}
		}

		// ソートカラムを設定する
		if (conditions.containsKey(Param.SORT_COLUMN)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SORT_COLUMN))) {
				param.put(Param.SORT_COLUMN,
						StringUtil.convertColumnName((String)conditions.get(Param.SORT_COLUMN)));
			}
		}

		// ソートオーダーを設定する
		Boolean sortOrderAsc = (Boolean)conditions.get(Param.SORT_ORDER_ASC);
		if (sortOrderAsc) {
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.DESC);
		}

		// 表示件数を設定する
		if (conditions.containsKey(Param.ROW_COUNT)) {
			param.put(Param.ROW_COUNT,
					conditions.get(Param.ROW_COUNT));
		}

		// オフセットを設定する
		if (conditions.containsKey(Param.OFFSET_ROW)) {
			param.put(Param.OFFSET_ROW,conditions.get(Param.OFFSET_ROW));
		}

		return param;
	}
}
