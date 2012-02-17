/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service.porder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.dto.porder.POrderSlipLineJoinDto;
import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.entity.join.POrderSlipLineJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 *
 * 発注検索サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchPOrderService extends AbstractService<PoSlipTrn> {

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {

		
		public static final String SEARCH_TARGET = "searchTarget";
		
		public static final String PO_SLIP_ID = "poSlipId";
		public static final String STATUS = "status";
		public static final String USER_NAME = "userName";
		public static final String PO_DATE_FROM = "poDateFrom";
		public static final String PO_DATE_TO = "poDateTo";
		public static final String DELIVERY_DATE_FROM = "deliveryDateFrom";
		public static final String DELIVERY_DATE_TO = "deliveryDateTo";
		public static final String REMARKS = "remarks";
		public static final String TRANSPORT_CATEGORY = "transportCategory";
		public static final String ONLY_REST_QUANTITY_EXIST = "onlyRestQuantityExist";
		public static final String ONLY_UNPAID = "onlyUnpaid";
		public static final String SUPPLIER_CODE = "supplierCode";
		public static final String SUPPLIER_NAME = "supplierName";
		public static final String SUPPLIER_PC_NAME = "supplierPcName";
		
		public static final String PRODUCT_CODE = "productCode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String PRODUCT1 = "product1";
		public static final String PRODUCT2 = "product2";
		public static final String PRODUCT3 = "product3";
		public static final String SORT_ORDER = "sortOrder";

		public static final String ENTRUST_PO_REST = "entrustPoRest";	
		public static final String ENTRUST_PO_MAKED = "entrustPoMaked";	
		public static final String ENTRUST_PO_DELIVERED = "entrustPoDelivered";	
		public static final String NORMAL_PO_REST = "normalPoRest";		

		
		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET_ROW = "offsetRow";

		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";

		public static final String SORT_COLUMN_SLIP = "sortColumnSlip";
		public static final String SORT_COLUMN_LINE = "sortColumnLine";

		
		
		private static final String PAYMENT_STATUS = "paymentStatus";

		
		public static final String PO_DATE = "poDate";
		private static final String DELIVERY_DATE = "deliveryDate";
		private static final String USER_ID = "userId";
		
		
		
		

		private static final String PURE_PRICE_TOTAL = "purePriceTotal";
		private static final String PRICE_TOTAL = "priceTotal";
		private static final String CTAX_TOTAL = "ctaxTotal";
		private static final String FE_PRICE_TOTAL = "fePriceTotal";
		
		
		
		private static final String QUANTITY = "quantity";
		private static final String UNIT_PRICE = "unitPrice";
		private static final String PRICE = "price";
		private static final String DOL_UNIT_PRICE = "dolUnitPrice";
		private static final String DOL_PRICE = "dolPrice";
		private static final String REST_QUANTITY = "restQuantity";

		private static final String LINE_STATUS = "lineStatus";
		private static final String LINE_DELIVERY_DATE = "lineDeliveryDate";

	}

	/**
	 *
	 * ローカルのみで使用するパラメータ定義クラスです.
	 *
	 */
	public static class ParamLocal {
		public static final String UNPAID = "slipPaymentStatusUnpaid";
		public static final String PAID = "slipPaymentStatusPaid";
		public static final String TRANSPORT_CATEGORY_CODE = "transportCategoryCode";
		public static final String PO_LINE_STATUS = "poLineStatusCategory";
		public static final String STATUS_SUPPLIER_SLIP_UNPAID = "statusSupplierSlipUnpaid";
		public static final String STATUS_PORDER_SLIP_PURCHASED = "statusPorderSlipPurchased";
	}

	/**
	 * カラム定義クラスです.
	 */
	public static class Column {

		
		private static final String PAYMENT_STATUS = "PAYMENT_STATUS";

		private static final String PO_SLIP_ID = "PO_SLIP_ID";
		private static final String PO_DATE = "PO_DATE";
		private static final String DELIVERY_DATE = "DELIVERY_DATE";
		private static final String USER_ID = "USER_ID";
		private static final String USER_NAME = "USER_NAME";
		private static final String SUPPLIER_CODE = "SUPPLIER_CODE";
		private static final String SUPPLIER_NAME = "SUPPLIER_NAME";
		private static final String TRANSPORT_CATEGORY = "TRANSPORT_CATEGORY";

		private static final String PURE_PRICE_TOTAL = "PURE_PRICE_TOTAL";
		private static final String PRICE_TOTAL = "PRICE_TOTAL";
		private static final String CTAX_TOTAL = "CTAX_TOTAL";
		private static final String FE_PRICE_TOTAL = "FE_PRICE_TOTAL";
		
		private static final String PRODUCT_CODE = "PRODUCT_CODE";
		private static final String PRODUCT_ABSTRACT = "PRODUCT_ABSTRACT";
		private static final String QUANTITY = "QUANTITY";
		private static final String UNIT_PRICE = "UNIT_PRICE";
		private static final String PRICE = "PRICE";
		private static final String DOL_UNIT_PRICE = "DOL_UNIT_PRICE";
		private static final String DOL_PRICE = "DOL_PRICE";

		private static final String REST_QUANTITY = "REST_QUANTITY";

		private static final String LINE_STATUS = "LINE_STATUS";

		private static final String LINE_NO = "LINE_NO";

		private static final Object LINE_DELIVERY_DATE = "LINE_DELIVERY_DATE";

	}

	@Resource
	private DetailDispItemService detailDispItemService;

	/**
	 * 受け取った検索結果を各ユーザの設定に応じた検索結果表示に変換します.
	 * @param porderSlipLineJoinDtoList 検索結果
	 * @param searchResultList 変換後検索結果
	 * @param searchTarget 検索対象
	 * @return 検索結果表示カラム
	 * @throws ServiceException
	 */
	public List<DetailDispItemDto> createSearchPOrderResult(
			List<POrderSlipLineJoinDto> porderSlipLineJoinDtoList,
			List<List<Object>> searchResultList, String searchTarget)
			throws ServiceException {
		try {
			
			List<BeanMap> resultMapList = new ArrayList<BeanMap>();
			if (porderSlipLineJoinDtoList != null) {
				for (POrderSlipLineJoinDto porderSlipLineJoinDto : porderSlipLineJoinDtoList) {
					BeanMap map = Beans.createAndCopy(BeanMap.class,
							porderSlipLineJoinDto).execute();
					map.put(Param.PO_SLIP_ID, porderSlipLineJoinDto);
					map.put(Param.TRANSPORT_CATEGORY, porderSlipLineJoinDto);
					resultMapList.add(map);
				}
			}

			
			List<DetailDispItemDto> columnInfoList = detailDispItemService
					.createResult(resultMapList, searchResultList,
							Constants.MENU_ID.SEARCH_PORDER, searchTarget);

			return columnInfoList;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 検索条件を指定して結果件数を返します.
	 * @param params 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer getSearchPOrderResultCount(BeanMap params)
			throws ServiceException {
		try {
			Integer count = Integer.valueOf(0);

			
			String searchTarget = (String) params.get(Param.SEARCH_TARGET);

			
			if (Constants.SEARCH_TARGET.VALUE_SLIP.equals(searchTarget)) {
				count = findPOrderSlipCntByCondition(params);
			} else if (Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {
				count = findPOrderSlipLineCntByCondition(params);
			}

			return count;
		} catch (ServiceException e) {
			throw e;
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
		param.put(Param.SEARCH_TARGET, null);
		param.put(Param.PO_SLIP_ID, null);
		param.put(Param.USER_NAME, null);
		param.put(Param.PO_DATE_FROM, null);
		param.put(Param.PO_DATE_TO, null);
		param.put(Param.DELIVERY_DATE_FROM, null);
		param.put(Param.DELIVERY_DATE_TO, null);
		param.put(Param.REMARKS, null);
		param.put(Param.TRANSPORT_CATEGORY, null);
		param.put(Param.ONLY_REST_QUANTITY_EXIST, null);
		param.put(Param.ONLY_UNPAID, null);
		param.put(Param.SUPPLIER_CODE, null);
		param.put(Param.SUPPLIER_NAME, null);
		param.put(Param.SUPPLIER_PC_NAME, null);
		param.put(Param.PRODUCT_CODE, null);
		param.put(Param.PRODUCT_ABSTRACT, null);
		param.put(Param.PRODUCT1, null);
		param.put(Param.PRODUCT2, null);
		param.put(Param.PRODUCT3, null);
		param.put(Param.SORT_ORDER, null);
		param.put(Param.SORT_COLUMN_LINE, null);
		param.put(Param.ROW_COUNT, null);
		param.put(Param.OFFSET_ROW, null);
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

		
		
		param.put(ParamLocal.UNPAID, MessageResourcesUtil
				.getMessage("labels.slipPaymentStatus.unpaid"));
		
		param.put(ParamLocal.PAID, MessageResourcesUtil
				.getMessage("labels.slipPaymentStatus.paid"));
		
		param.put(ParamLocal.STATUS_PORDER_SLIP_PURCHASED,
				Constants.STATUS_PORDER_SLIP.PURCHASED);
		
		param.put(ParamLocal.STATUS_SUPPLIER_SLIP_UNPAID,
				Constants.STATUS_SUPPLIER_SLIP.UNPAID);
		
		param.put(ParamLocal.TRANSPORT_CATEGORY_CODE,
				Categories.TRANSPORT_CATEGORY);
		
		param.put(ParamLocal.PO_LINE_STATUS,
				SlipStatusCategories.PO_LINE_STATUS);

		
		if (conditions.containsKey(Param.PO_SLIP_ID)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PO_SLIP_ID))) {
				param.put(Param.PO_SLIP_ID, new Integer((String) conditions
						.get(Param.PO_SLIP_ID)));
			}
		}

		
		if (conditions.containsKey(Param.USER_NAME)) {
			if (StringUtil.hasLength((String) conditions.get(Param.USER_NAME))) {
				param.put(Param.USER_NAME, super
						.createPartialSearchCondition((String) conditions
										.get(Param.USER_NAME)));
			}
		}

		
		if (conditions.containsKey(Param.PO_DATE_FROM)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.PO_DATE_FROM))) {
				param.put(Param.PO_DATE_FROM, (String) conditions
						.get(Param.PO_DATE_FROM));
			}
		}

		
		if (conditions.containsKey(Param.PO_DATE_TO)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PO_DATE_TO))) {
				param.put(Param.PO_DATE_TO, (String) conditions
						.get(Param.PO_DATE_TO));
			}
		}

		
		if (conditions.containsKey(Param.DELIVERY_DATE_FROM)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.DELIVERY_DATE_FROM))) {
				param.put(Param.DELIVERY_DATE_FROM, (String) conditions
						.get(Param.DELIVERY_DATE_FROM));
			}
		}

		
		if (conditions.containsKey(Param.DELIVERY_DATE_TO)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.DELIVERY_DATE_TO))) {
				param.put(Param.DELIVERY_DATE_TO, (String) conditions
						.get(Param.DELIVERY_DATE_TO));
			}
		}

		
		if (conditions.containsKey(Param.REMARKS)) {
			if (StringUtil.hasLength((String) conditions.get(Param.REMARKS))) {
				param.put(Param.REMARKS, super
						.createPartialSearchCondition((String) conditions
										.get(Param.REMARKS)));
			}
		}

		
		if (conditions.containsKey(Param.TRANSPORT_CATEGORY)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.TRANSPORT_CATEGORY))) {
				param.put(Param.TRANSPORT_CATEGORY, (String) conditions
						.get(Param.TRANSPORT_CATEGORY));
			}
		}

		
		if (conditions.containsKey(Param.ONLY_REST_QUANTITY_EXIST)) {
			if ((Boolean) conditions.get(Param.ONLY_REST_QUANTITY_EXIST)) {
				param.put(Param.ONLY_REST_QUANTITY_EXIST, "true"); 
			}
		}

		
		if (conditions.containsKey(Param.ONLY_UNPAID)) {
			if ((Boolean) conditions.get(Param.ONLY_UNPAID)) {
				param.put(Param.ONLY_UNPAID, "true"); 
			}
		}

		
		if (conditions.containsKey(Param.SUPPLIER_CODE)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SUPPLIER_CODE))) {
				param.put(Param.SUPPLIER_CODE, super
						.createPrefixSearchCondition((String) conditions
								.get(Param.SUPPLIER_CODE)));
			}
		}

		
		if (conditions.containsKey(Param.SUPPLIER_NAME)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SUPPLIER_NAME))) {
				param.put(Param.SUPPLIER_NAME, super
						.createPartialSearchCondition((String) conditions
										.get(Param.SUPPLIER_NAME)));
			}
		}

		
		if (conditions.containsKey(Param.SUPPLIER_PC_NAME)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SUPPLIER_PC_NAME))) {
				param.put(Param.SUPPLIER_PC_NAME, super
						.createPartialSearchCondition((String) conditions
										.get(Param.SUPPLIER_PC_NAME)));
			}
		}

		
		if (conditions.containsKey(Param.PRODUCT_CODE)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.PRODUCT_CODE))) {
				param.put(Param.PRODUCT_CODE, super
						.createPrefixSearchCondition((String) conditions
								.get(Param.PRODUCT_CODE)));
			}
		}

		
		if (conditions.containsKey(Param.PRODUCT_ABSTRACT)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.PRODUCT_ABSTRACT))) {
				param.put(Param.PRODUCT_ABSTRACT, super
						.createPartialSearchCondition((String) conditions.get(Param.PRODUCT_ABSTRACT)));
			}
		}

		
		if (conditions.containsKey(Param.PRODUCT1)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PRODUCT1))) {
				param.put(Param.PRODUCT1, (String) conditions
						.get(Param.PRODUCT1));
			}
		}

		
		if (conditions.containsKey(Param.PRODUCT2)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PRODUCT2))) {
				param.put(Param.PRODUCT2, (String) conditions
						.get(Param.PRODUCT2));
			}
		}

		
		if (conditions.containsKey(Param.PRODUCT3)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PRODUCT3))) {
				param.put(Param.PRODUCT3, (String) conditions
						.get(Param.PRODUCT3));
			}
		}

		
		if (conditions.containsKey(Param.ROW_COUNT)) {
			param.put(Param.ROW_COUNT, conditions
					.get(Param.ROW_COUNT));
		}

		
		if (conditions.containsKey(Param.OFFSET_ROW)) {
			param.put(Param.OFFSET_ROW, conditions.get(Param.OFFSET_ROW));
		}

		
		if (conditions.containsKey(Param.ENTRUST_PO_REST)) {
			param.put(Param.ENTRUST_PO_REST, conditions.get(Param.ENTRUST_PO_REST));
		}

		
		if (conditions.containsKey(Param.ENTRUST_PO_MAKED)) {
			param.put(Param.ENTRUST_PO_MAKED, conditions.get(Param.ENTRUST_PO_MAKED));
		}

		
		if (conditions.containsKey(Param.ENTRUST_PO_DELIVERED)) {
			param.put(Param.ENTRUST_PO_DELIVERED, conditions.get(Param.ENTRUST_PO_DELIVERED));
		}

		
		if (conditions.containsKey(Param.NORMAL_PO_REST)) {
			param.put(Param.NORMAL_PO_REST, conditions.get(Param.NORMAL_PO_REST));
		}

		return param;
	}

	/**
	 * 検索条件を指定して伝票単位の結果件数を返します.
	 *
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer findPOrderSlipCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"porder/FindSlipCntByCondition.sql", param)
					.getSingleResult();
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
	public Integer findPOrderSlipLineCntByCondition(
			Map<String, Object> conditions) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"porder/FindSlipLineCntByCondition.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して伝票単位の結果リストを返します.
	 *
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<POrderSlipLineJoin> findPOrderSlipByCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			
			if (Param.PAYMENT_STATUS.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PAYMENT_STATUS);
			} else if (Param.PO_SLIP_ID.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PO_SLIP_ID);
			} else if (Param.PO_DATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PO_DATE);
			} else if (Param.DELIVERY_DATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.DELIVERY_DATE);
			} else if (Param.USER_ID.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.USER_ID);
			} else if (Param.USER_NAME.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.USER_NAME);
			} else if (Param.SUPPLIER_CODE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.SUPPLIER_CODE);
			} else if (Param.SUPPLIER_NAME.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.SUPPLIER_NAME);
			} else if (Param.TRANSPORT_CATEGORY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.TRANSPORT_CATEGORY);
			} else if (Param.PURE_PRICE_TOTAL.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PURE_PRICE_TOTAL);
			} else if (Param.PRICE_TOTAL.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PRICE_TOTAL);
			} else if (Param.CTAX_TOTAL.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.CTAX_TOTAL);
			} else if (Param.FE_PRICE_TOTAL.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.FE_PRICE_TOTAL);
			} else if (Param.PRODUCT_CODE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.PRODUCT_CODE);
			} else if (Param.PRODUCT_ABSTRACT.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.PRODUCT_ABSTRACT);
			} else if (Param.QUANTITY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.QUANTITY);
			} else if (Param.UNIT_PRICE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.UNIT_PRICE);
			} else if (Param.PRICE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.PRICE);
			} else if (Param.DOL_UNIT_PRICE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.DOL_UNIT_PRICE);
			} else if (Param.DOL_PRICE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.DOL_PRICE);
			} else if (Param.REST_QUANTITY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.REST_QUANTITY);
			} else if (Param.LINE_STATUS.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.LINE_STATUS);
			}

			
			if (sortOrderAsc) {
				param.put(Param.SORT_ORDER, Constants.SQL.ASC);
			} else {
				param.put(Param.SORT_ORDER, Constants.SQL.DESC);
			}

			return this.selectBySqlFile(POrderSlipLineJoin.class,
					"porder/FindSlipByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して明細行単位の結果リストを返します.
	 *
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<POrderSlipLineJoin> findPOrderSlipLineByCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			
			if (Param.PAYMENT_STATUS.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PAYMENT_STATUS);
			} else if (Param.PO_SLIP_ID.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PO_SLIP_ID);
				
				param.put(Param.SORT_COLUMN_LINE, Column.LINE_NO);
			} else if (Param.PO_DATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PO_DATE);
			} else if (Param.DELIVERY_DATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.DELIVERY_DATE);
			} else if (Param.USER_ID.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.USER_ID);
			} else if (Param.USER_NAME.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.USER_NAME);
			} else if (Param.SUPPLIER_CODE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.SUPPLIER_CODE);
			} else if (Param.SUPPLIER_NAME.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.SUPPLIER_NAME);
			} else if (Param.TRANSPORT_CATEGORY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.TRANSPORT_CATEGORY);
			} else if (Param.PURE_PRICE_TOTAL.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PURE_PRICE_TOTAL);
			} else if (Param.PRICE_TOTAL.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.PRICE_TOTAL);
			} else if (Param.CTAX_TOTAL.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.CTAX_TOTAL);
			} else if (Param.FE_PRICE_TOTAL.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.FE_PRICE_TOTAL);
			} else if (Param.PRODUCT_CODE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.PRODUCT_CODE);
			} else if (Param.PRODUCT_ABSTRACT.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.PRODUCT_ABSTRACT);
			} else if (Param.QUANTITY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.QUANTITY);
			} else if (Param.UNIT_PRICE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.UNIT_PRICE);
			} else if (Param.PRICE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.PRICE);
			} else if (Param.DOL_UNIT_PRICE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.DOL_UNIT_PRICE);
			} else if (Param.DOL_PRICE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.DOL_PRICE);
			} else if (Param.REST_QUANTITY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.REST_QUANTITY);
			} else if (Param.LINE_STATUS.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.LINE_STATUS);
			} else if (Param.LINE_DELIVERY_DATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.LINE_DELIVERY_DATE);
			}

			
			if (sortOrderAsc) {
				param.put(Param.SORT_ORDER, Constants.SQL.ASC);
			} else {
				param.put(Param.SORT_ORDER, Constants.SQL.DESC);
			}

			return this.selectBySqlFile(POrderSlipLineJoin.class,
					"porder/FindSlipLineByCondition.sql", param)
					.getResultList();
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
	public List<POrderSlipLineJoinDto> createPOrderSlipJoinDtoList(
			BeanMap params) throws ServiceException {
		try {
			
			String searchTarget = (String) params.get(Param.SEARCH_TARGET);

			
			String sortColumn = (String) params.get(Param.SORT_COLUMN);

			
			boolean sortOrderAsc = (Boolean) params.get(Param.SORT_ORDER_ASC);

			
			List<POrderSlipLineJoin> porderSlipLineJoinList = new ArrayList<POrderSlipLineJoin>();
			if (Constants.SEARCH_TARGET.VALUE_SLIP.equals(searchTarget)) {
				
				porderSlipLineJoinList = findPOrderSlipByCondition(params,
						sortColumn, sortOrderAsc);
			} else if (Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {
				
				porderSlipLineJoinList = findPOrderSlipLineByCondition(params,
						sortColumn, sortOrderAsc);
			}

			
			List<POrderSlipLineJoinDto> porderSlipLineJoinDtoList = new ArrayList<POrderSlipLineJoinDto>();
			for (POrderSlipLineJoin porderSlipLineJoin : porderSlipLineJoinList) {
				
				POrderSlipLineJoinDto porderSlipLineJoinDto = Beans
						.createAndCopy(POrderSlipLineJoinDto.class,
								porderSlipLineJoin).execute();

				
				if (Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {

					porderSlipLineJoinDto.poSlipIdShow = porderSlipLineJoinDto.poSlipId
							+ " - " + porderSlipLineJoinDto.lineNo;
				}
				else {
					porderSlipLineJoinDto.poSlipIdShow = porderSlipLineJoinDto.poSlipId;
				}

				
				porderSlipLineJoinDtoList.add(porderSlipLineJoinDto);
			}

			return porderSlipLineJoinDtoList;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

}
