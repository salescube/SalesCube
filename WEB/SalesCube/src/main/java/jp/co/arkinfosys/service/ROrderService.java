/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.SlipStatusCategoryTrns;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.PaymentSlipTrn;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;

/**
 * 受注伝票サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ROrderService extends AbstractService<PaymentSlipTrn> {
	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {

		public static final String SEARCH_TARGET = "searchTarget";

		public static final String RO_SLIP_ID = "roSlipId";
		public static final String RECEPT_NO = "receptNo";
		public static final String REST_ONLY = "restOnly";	
		public static final String RAZY_ONLY = "razyOnly";	
		public static final String RO_DATE = "roDate";
		public static final String RO_DATE_FROM = "roDateFrom";
		public static final String RO_DATE_TO = "roDateTo";
		public static final String SHIP_DATE_FROM = "shipDateFrom";
		public static final String SHIP_DATE_TO = "shipDateTo";
		public static final String DELIVERY_DATE_FROM = "deliveryDateFrom";
		public static final String DELIVERY_DATE_TO = "deliveryDateTo";
		public static final String CUSTOMER_CODE = "customerCode";
		public static final String CUSTOMER_NAME = "customerName";
		public static final String DELIVERY_PC_NAME = "deliveryPcName";
		public static final String SALES_CM_CATEGORY_LIST = "salesCmCategoryList";

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
		public static final String TAX_SHIFT_CATEGORY = "taxShiftCategory";
		public static final String CUTOFF_CATEGORY = "cutoffCategory";
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
		public static final String RO_LINE_STATUS = "roLineStatus";
		public static final String RO_LINE_STATUS_CODE = "roLineStatusCode";

		public static final String RO_YM = "roYm";

		public static final String SORT_COLUMN_SLIP = "sortColumnSlip";
		public static final String SORT_COLUMN_LINE = "sortColumnLine";

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
	 *
	 * カラム名定義クラスです.
	 *
	 */
	public static class Column {
		public static final String RO_SLIP_ID = "RO_SLIP_ID";
		public static final String SORT_RO_SLIP_ID = "SORT_RO_SLIP_ID";
		public static final String SORT_RO_LINE_NO = "SORT_RO_LINE_NO";
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

			
			String searchTarget = (String)params.get(Param.SEARCH_TARGET);

			
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
	 * 検索条件を指定して結果件数を返します.
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
					"rorder/FindSlipCntByCondition.sql", param).getSingleResult();
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
	public Integer findSlipLineCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"rorder/FindSlipLineCntByCondition.sql", param)
					.getSingleResult();
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
					"rorder/FindSlipByCondition.sql", param).getResultList();
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
	public List<BeanMap> findSlipLineByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			
			String sortColumn = (String)param.get(Param.SORT_COLUMN);
			if (Column.RO_SLIP_ID.equals(sortColumn)) {
				
				param.put(Param.SORT_COLUMN_SLIP, Column.SORT_RO_SLIP_ID);
				param.put(Param.SORT_COLUMN_LINE, Column.SORT_RO_LINE_NO);
				param.put(Param.SORT_COLUMN, null);
			}

			return this.selectBySqlFile(BeanMap.class,
					"rorder/FindSlipLineByCondition.sql", param).getResultList();
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

		param.put(Param.RO_SLIP_ID, null);
		param.put(Param.RECEPT_NO, null);
		param.put(Param.REST_ONLY, null);
		param.put(Param.RAZY_ONLY, null);
		param.put(Param.RO_DATE_FROM, null);
		param.put(Param.RO_DATE_TO, null);
		param.put(Param.SHIP_DATE_FROM, null);
		param.put(Param.SHIP_DATE_TO, null);
		param.put(Param.DELIVERY_DATE_FROM, null);
		param.put(Param.DELIVERY_DATE_TO, null);
		param.put(Param.CUSTOMER_CODE, null);
		param.put(Param.CUSTOMER_NAME, null);
		param.put(Param.DELIVERY_PC_NAME, null);
		param.put(Param.SALES_CM_CATEGORY_LIST, null);

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
		param.put(Param.TAX_SHIFT_CATEGORY, Integer.valueOf(Categories.ART_TAX_SHIFT_CATEGORY));
		param.put(Param.CUTOFF_CATEGORY, Integer.valueOf(Categories.CUTOFF_GROUP));
		param.put(Param.SALES_CM_CATEGORY, Integer.valueOf(Categories.SALES_CM_CATEGORY));
		param.put(Param.RO_LINE_STATUS, Integer.valueOf(Categories.RO_LINE_STATUS));
		param.put(Param.SORT_COLUMN_SLIP,null);
		param.put(Param.SORT_COLUMN_LINE,null);
		return param;
	}

	/**
	 *
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @param key キー カラム名
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
	 * 検索条件パラメータを設定して返します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	private Map<String, Object> setConditionParam(
			Map<String, Object> conditions, Map<String, Object> param) {

		
		if (conditions.containsKey(Param.RO_SLIP_ID)) {
			if (StringUtil.hasLength((String)conditions.get(Param.RO_SLIP_ID))) {
				param.put(Param.RO_SLIP_ID,new Long((String)conditions.get(Param.RO_SLIP_ID)));
			}
		}
		
		setConditionItemString(conditions, param, Param.RECEPT_NO, LikeType.PREFIX);

		
		setConditionItemString(conditions, param, Param.REST_ONLY, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.RAZY_ONLY, LikeType.NOTHING);
		if(param.containsKey(Param.RAZY_ONLY)) {
			param.put(Param.RO_LINE_STATUS_CODE, SlipStatusCategoryTrns.RO_LINE_PROCESSED);
		}

		
		setConditionItemString(conditions, param, Param.RO_DATE_FROM, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.RO_DATE_TO, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.SHIP_DATE_FROM, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.SHIP_DATE_TO, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.DELIVERY_DATE_FROM, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.DELIVERY_DATE_TO, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.CUSTOMER_CODE, LikeType.PREFIX);

		
		setConditionItemString(conditions, param, Param.CUSTOMER_NAME, LikeType.PARTIAL);

		
		setConditionItemString(conditions, param, Param.DELIVERY_PC_NAME, LikeType.PARTIAL);

		
		setConditionItemString(conditions, param, Param.PRODUCT_CODE, LikeType.PREFIX);

		
		setConditionItemString(conditions, param, Param.PRODUCT_ABSTRACT, LikeType.PARTIAL);

		
		setConditionItemString(conditions, param, Param.PRODUCT1, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.PRODUCT2, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.PRODUCT3, LikeType.NOTHING);

		
		setConditionItemString(conditions, param, Param.SUPPLIER_CODE, LikeType.PREFIX);

		
		setConditionItemString(conditions, param, Param.SUPPLIER_NAME, LikeType.PARTIAL);

		
		if(conditions.containsKey(Param.SALES_CM_CATEGORY_LIST)) {
			param.put(Param.SALES_CM_CATEGORY_LIST, conditions.get(Param.SALES_CM_CATEGORY_LIST));
		}

		
		if (conditions.containsKey(Param.SORT_COLUMN)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SORT_COLUMN))) {
				param.put(Param.SORT_COLUMN,
						StringUtil.convertColumnName((String)conditions.get(Param.SORT_COLUMN)));
			}
		}

		
		Boolean sortOrderAsc = (Boolean)conditions.get(Param.SORT_ORDER_ASC);
		if (sortOrderAsc) {
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.DESC);
		}

		
		if (conditions.containsKey(Param.ROW_COUNT)) {
			param.put(Param.ROW_COUNT,
					conditions.get(Param.ROW_COUNT));
		}

		
		if (conditions.containsKey(Param.OFFSET_ROW)) {
			param.put(Param.OFFSET_ROW,conditions.get(Param.OFFSET_ROW));
		}

		return param;
	}

	/**
	 * 検索条件を指定して結果リストを返します.
	 * @param params 検索条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getSearchResult(BeanMap params) throws ServiceException {


		try {
			
			String searchTarget = (String)params.get(Param.SEARCH_TARGET);

			
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
	 * 期間を指定して商品コードリストを返します.
	 *
	 * @param roDate 期間(何ヶ月)
	 * @return 商品コードリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findProductCodeByRoDate(String roDate)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();

			param.put(Param.RO_DATE, roDate);

			return this.selectBySqlFile(BeanMap.class,
					"rorder/FindProductCodeByRoDate.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 指定期間における商品の総個数を取得します.
	 * @param productCode 商品コード
	 * @param roDate 期間
	 * @return 総個数
	 * @throws ServiceException
	 */
	public Integer countQuantityByProductCodeAndRoDate(
			String productCode, String roDate) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();

			
			params.put(Param.PRODUCT_CODE, productCode);
			
			params.put(Param.RO_DATE, roDate);

			return this.selectBySqlFile(Integer.class,
					"rorder/CountQuantityByProductCodeAndRoDate.sql", params)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
