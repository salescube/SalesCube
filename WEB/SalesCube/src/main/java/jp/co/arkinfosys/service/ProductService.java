/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.ExistsCheckStateDto;
import jp.co.arkinfosys.dto.master.ProductDto;
import jp.co.arkinfosys.dto.master.ProductExcelRowDto;
import jp.co.arkinfosys.dto.stock.ProductStockInfoDto;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.ProductStockInfo;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.FileImportException;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.framework.beans.ConverterRuntimeException;
import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 商品サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ProductService extends
		AbstractMasterEditService<ProductDto, ProductJoin> implements
		MasterSearch<ProductJoin> {

	@Resource
	private DiscountRelService discountRelService;

	@Resource
	private UserTransaction userTransaction;

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String PRODUCT_CODE = "productCode";

		private static final String PRODUCT_CODE_LIST = "productCodeList";

		public static final String PRODUCT_NAME = "productName";

		public static final String PRODUCT_KANA = "productKana";

		public static final String ONLINE_PCODE = "onlinePcode";

		public static final String SUPPLIER_PCODE = "supplierPcode";

		public static final String SUPPLIER_CODE = "supplierCode";

		public static final String SUPPLIER_NAME = "supplierName";

		public static final String RACK_CODE = "rackCode";

		public static final String WAREHOUSE_NAME = "warehouseName";

		public static final String SUPPLIER_PRICE_YEN = "supplierPriceYen";

		public static final String SUPPLIER_PRICE_DOL = "supplierPriceDol";

		public static final String RETAIL_PRICE = "retailPrice";

		public static final String SO_RATE = "soRate";

		public static final String UNIT_CATEGORY = "unitCategory";

		public static final String PACK_QUANTITY = "packQuantity";

		public static final String JAN_PCODE = "janPcode";

		public static final String WIDTH = "width";

		public static final String WIDTH_UNIT_SIZE_CATEGORY = "widthUnitSizeCategory";

		public static final String DEPTH = "width";

		public static final String DEPTH_UNIT_SIZE_CATEGORY = "depthUnitSizeCategory";

		public static final String HEIGHT = "width";

		public static final String HEIGHT_UNIT_SIZE_CATEGORY = "heightUnitSizeCategory";

		public static final String WEIGHT = "width";

		public static final String WEIGHT_UNIT_SIZE_CATEGORY = "weightUnitSizeCategory";

		public static final String LENGTH = "width";

		public static final String LENGTH_UNIT_SIZE_CATEGORY = "lengthUnitSizeCategory";

		public static final String PO_LOT = "poLot";

		public static final String LOT_UPD_FLAG = "lotUpdFlag";

		public static final String LEAD_TIME = "leadTime";

		public static final String PO_NUM = "poNum";

		public static final String PO_UPD_FLAG = "poUpdFlag";

		public static final String MINE_SAFETY_STOCK = "mineSafetyStock";

		public static final String MINE_SAFETY_STOCK_UPD_FLAG = "mineSafetyStockUpdFlag";

		public static final String ENTRUST_SAFETY_STOCK = "entrustSafetyStock";

		public static final String AVG_SHIP_COUNT = "avgShipCount";

		public static final String MAX_STOCK_NUM = "maxStockNum";

		public static final String STOCK_UPD_FLAG = "stockUpdFlag";

		public static final String TERM_SHIP_NUM = "termShipNum";

		public static final String MAX_PO_NUM = "maxPoNum";

		public static final String MAX_PO_UPD_FLAG = "maxPoUpdFlag";

		public static final String FRACT_CATEGORY = "fractCategory";

		public static final String TAX_CATEGORY = "taxCategory";

		public static final String STOCK_CTL_CATEGORY = "stockCtlCategory";

		public static final String STOCK_CTL_CATEGORY_ID = "stockCtlCategoryId";

		public static final String STOCK_ASSES_CATEGORY = "stockAssesCategory";

		public static final String PRODUCT_CATEGORY = "productCategory";

		public static final String PRODUCT1 = "product1";

		public static final String PRODUCT2 = "product2";

		public static final String PRODUCT3 = "product3";

		public static final String RO_MAX_NUM = "roMaxNum";

		public static final String PRODUCT_RANK = "productRank";

		public static final String SET_TYPE_CATEGORY = "setTypeCategory";

		public static final String PRODUCT_STATUS_CATEGORY = "productStatusCategory";

		public static final String PRODUCT_STOCK_CATEGORY = "productStockCategory";

		public static final String PRODUCT_PURVAY_CATEGORY = "productPurvayCategory";

		public static final String PRODUCT_STANDARD_CATEGORY = "productStandardCategory";

		public static final String CORE_NUM = "coreNum";

		public static final String REMARKS = "remarks";

		public static final String EAD_REMARKS = "eadRemarks";

		public static final String COMMENT_DATA = "commentData";

		public static final String PRODUCT_STATUS_CATEGORY_ID = "productStatusCategoryId";

		public static final String PRODUCT_STOCK_CATEGORY_ID = "productStockCategoryId";

		public static final String PRODUCT_PURVAY_CATEGORY_ID = "productPurvayCategoryId";

		public static final String PRODUCT_STANDARD_CATEGORY_ID = "productStandardCategoryId";

		public static final String SET_TYPE_CATEGORY_ID = "setTypeCategoryId";

		public static final String UNIT_CATEGORY_ID = "unitCategoryId";

		public static final String WEIGHT_UNIT_SIZE_CATEGORY_ID = "weightUnitSizeCategoryId";

		public static final String LENGTH_UNIT_SIZE_CATEGORY_ID = "lengthUnitSizeCategoryId";

		private static final String SORT_BY_PRODUCT = "sortByProduct";

		private static final String SORT_BY_SUPPLIER = "sortBySupplier";

		private static final String SORT_COLUMN = "sortColumn";

		private static final String SORT_ORDER = "sortOrder";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";

		private static final String PRODUCT_STATUS_SALE_CANCEL = "productStatusSaleCancel";

		public static final String AGGREGATE_MONTHS_RANGE = "aggregateMonthsRange";

		public static final String RACK_MULTI_FLAG = "rackMultiFlag";

		public static final String RO_EXISTS = "roExists";

		public static final String HOLDING_STOCK_LESS_THAN_PO_NUM = "holdingStockLessThanPoNum";

		public static final String EXCLUDES_HOLDING_STOCK_ZERO = "excludeHoldingStockZero";

		public static final String EXCLUDES_AVG_SHIP_COUNT_ZERO = "excludeAvgShipCountZero";

		public static final String EXCLUDES_AVG_LESS_THAN_HOLDING_STOCK = "excludeAvgLessThanHoldingStock";

		public static final String ENTRUST_STOCK_ZERO = "entrustStockZero";

		public static final String ENTRUST_STOCK_LARGER_THAN_ZERO = "entrustStockLargerThanZero";

		public static final String ENTRUST_PORDER_QUANTITY_LARGER_THAN_ZERO = "entrustPOrderQuantityLargerThanZero";

		public static final String ADD_PORDER_INFO = "addPOrderInfo";

		private static final String SESSION_ID = "sessionId";

		private static final String YM = "yearMonth";

		private static final String SALES_STANDARD_DEVIATION = "salesStandardDeviation";
	}

	/**
	 *
	 * @param productCode 商品コード
	 * @return {@link ProductJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findById(java.lang.String)
	 */
	@Override
	public ProductJoin findById(String productCode) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);
			param.put(ProductService.Param.PRODUCT_CODE, productCode);

			this.setConditionCategoryId(param);

			return this.selectBySqlFile(ProductJoin.class,
					"product/FindProductByCode.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @return 検索結果数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#countByCondition(java.util.Map)
	 */
	@Override
	public int countByCondition(Map<String, Object> conditions)
			throws ServiceException {
		if (conditions == null) {
			return 0;
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			// 検索条件を設定する
			this.setCondition(conditions, null, false, param);

			return this.selectBySqlFile(Integer.class,
					"product/CountProductByCondition.sql", param)
					.getSingleResult().intValue();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得件数
	 * @param offset 取得開始位置
	 * @return {@link ProductJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByConditionLimit(java.util.Map, java.lang.String, boolean, int, int)
	 */
	@Override
	public List<ProductJoin> findByConditionLimit(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			param.put(ProductService.Param.SET_TYPE_CATEGORY_ID,
					Categories.PRODUCT_SET_TYPE);
			param.put(ProductService.Param.UNIT_CATEGORY_ID,
					Categories.PRODUCT_UNIT);
			param.put(ProductService.Param.WEIGHT_UNIT_SIZE_CATEGORY_ID,
					Categories.PRODUCT_UNIT_WEIGHT);
			param.put(ProductService.Param.LENGTH_UNIT_SIZE_CATEGORY_ID,
					Categories.PRODUCT_UNIT_SIZE);

			this.setCondition(conditions, sortColumn, sortOrderAsc, param);

			// LIMITを設定する
			if (rowCount > 0) {
				param.put(Param.ROW_COUNT, rowCount);
				param.put(Param.OFFSET, offset);
			}

			return this.selectBySqlFile(ProductJoin.class,
					"product/FindProductByConditionLimit.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @return {@link ProductJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByCondition(java.util.Map, java.lang.String, boolean)
	 */
	@Override
	public List<ProductJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			param.put(ProductService.Param.SET_TYPE_CATEGORY_ID,
					Categories.PRODUCT_SET_TYPE);
			param.put(ProductService.Param.UNIT_CATEGORY_ID,
					Categories.PRODUCT_UNIT);
			param.put(ProductService.Param.WEIGHT_UNIT_SIZE_CATEGORY_ID,
					Categories.PRODUCT_UNIT_WEIGHT);
			param.put(ProductService.Param.LENGTH_UNIT_SIZE_CATEGORY_ID,
					Categories.PRODUCT_UNIT_SIZE);

			this.setCondition(conditions, sortColumn, sortOrderAsc, param);

			return this.selectBySqlFile(ProductJoin.class,
					"product/FindProductByCondition.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を設定します.
	 * @param conditions 検索条件値マップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param param 検索条件のマップ
	 */
	private void setCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {
		// 商品コード
		if (conditions.containsKey(ProductService.Param.PRODUCT_CODE)) {
			param.put(ProductService.Param.PRODUCT_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ProductService.Param.PRODUCT_CODE)));
		}

		// 仕入先商品コード
		if (conditions.containsKey(ProductService.Param.SUPPLIER_PCODE)) {
			param.put(ProductService.Param.SUPPLIER_PCODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ProductService.Param.SUPPLIER_PCODE)));
		}

		// JANコード
		if (conditions.containsKey(ProductService.Param.JAN_PCODE)) {
			param.put(ProductService.Param.JAN_PCODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ProductService.Param.JAN_PCODE)));
		}

		// 商品名
		if (conditions.containsKey(ProductService.Param.PRODUCT_NAME)) {
			param.put(ProductService.Param.PRODUCT_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(ProductService.Param.PRODUCT_NAME)));
		}

		// 商品名カナ
		if (conditions.containsKey(ProductService.Param.PRODUCT_KANA)) {
			param.put(ProductService.Param.PRODUCT_KANA, super
					.createPartialSearchCondition((String) conditions
							.get(ProductService.Param.PRODUCT_KANA)));
		}

		// 仕入先コード
		if (conditions.containsKey(ProductService.Param.SUPPLIER_CODE)) {
			param.put(ProductService.Param.SUPPLIER_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ProductService.Param.SUPPLIER_CODE)));
		}

		// 仕入先名
		if (conditions.containsKey(ProductService.Param.SUPPLIER_NAME)) {
			param.put(ProductService.Param.SUPPLIER_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(ProductService.Param.SUPPLIER_NAME)));
		}

		// セット分類
		if (conditions.containsKey(ProductService.Param.SET_TYPE_CATEGORY)) {
			param.put(ProductService.Param.SET_TYPE_CATEGORY, conditions
					.get(ProductService.Param.SET_TYPE_CATEGORY));
		}

		// 標準化分類
		if (conditions
				.containsKey(ProductService.Param.PRODUCT_STANDARD_CATEGORY)) {
			param
					.put(
							ProductService.Param.PRODUCT_STANDARD_CATEGORY,
							conditions
									.get(ProductService.Param.PRODUCT_STANDARD_CATEGORY));
		}

		// 分類保管
		if (conditions.containsKey(ProductService.Param.PRODUCT_STOCK_CATEGORY)) {
			param.put(ProductService.Param.PRODUCT_STOCK_CATEGORY, conditions
					.get(ProductService.Param.PRODUCT_STOCK_CATEGORY));
		}

		// 分類状況
		if (conditions
				.containsKey(ProductService.Param.PRODUCT_STATUS_CATEGORY)) {
			param.put(ProductService.Param.PRODUCT_STATUS_CATEGORY, conditions
					.get(ProductService.Param.PRODUCT_STATUS_CATEGORY));
		}

		// 在庫管理
		if (conditions.containsKey(ProductService.Param.STOCK_CTL_CATEGORY)) {
			param.put(ProductService.Param.STOCK_CTL_CATEGORY, conditions
					.get(ProductService.Param.STOCK_CTL_CATEGORY));
		}

		if (conditions.containsKey(ProductService.Param.RACK_MULTI_FLAG)) {
			param.put(ProductService.Param.RACK_MULTI_FLAG, conditions
					.get(ProductService.Param.RACK_MULTI_FLAG));
		}

		// 備考
		if (conditions.containsKey(ProductService.Param.REMARKS)) {
			param.put(ProductService.Param.REMARKS, super
					.createPartialSearchCondition((String) conditions
							.get(ProductService.Param.REMARKS)));
		}

		// 商品分類（大）
		if (conditions.containsKey(ProductService.Param.PRODUCT1)) {
			param.put(ProductService.Param.PRODUCT1, conditions
					.get(ProductService.Param.PRODUCT1));
		}

		// 商品分類（中）
		if (conditions.containsKey(ProductService.Param.PRODUCT2)) {
			param.put(ProductService.Param.PRODUCT2, conditions
					.get(ProductService.Param.PRODUCT2));
		}

		// 商品分類（小）
		if (conditions.containsKey(ProductService.Param.PRODUCT3)) {
			param.put(ProductService.Param.PRODUCT3, conditions
					.get(ProductService.Param.PRODUCT3));
		}

		// ソートカラム
		if (StringUtil.hasLength(sortColumn)) {
			if (ProductService.Param.SUPPLIER_NAME.equals(sortColumn)) {
				// 仕入先マスタのカラムでソート
				param.put(ProductService.Param.SORT_BY_SUPPLIER, true);
			} else {
				// 商品マスタのカラムでソート
				param.put(ProductService.Param.SORT_BY_PRODUCT, true);
			}
			param.put(ProductService.Param.SORT_COLUMN, StringUtil
					.convertColumnName(sortColumn));
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(ProductService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(ProductService.Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * 集計処理用の検索条件を設定します.
	 * @param conditions 検索条件値のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param param 検索条件のマップ
	 */
	private void setConditionAggregate(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {
		// 商品コード(完全一致)
		if (conditions.containsKey(ProductService.Param.PRODUCT_CODE)) {
			param.put(ProductService.Param.PRODUCT_CODE, conditions
					.get(ProductService.Param.PRODUCT_CODE));
		}

		// 仕入先商品コード
		if (conditions.containsKey(ProductService.Param.SUPPLIER_PCODE)) {
			param.put(ProductService.Param.SUPPLIER_PCODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ProductService.Param.SUPPLIER_PCODE)));
		}

		// JANコード
		if (conditions.containsKey(ProductService.Param.JAN_PCODE)) {
			param.put(ProductService.Param.JAN_PCODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ProductService.Param.JAN_PCODE)));
		}

		// 商品名
		if (conditions.containsKey(ProductService.Param.PRODUCT_NAME)) {
			param.put(ProductService.Param.PRODUCT_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(ProductService.Param.PRODUCT_NAME)));
		}

		// 商品名カナ
		if (conditions.containsKey(ProductService.Param.PRODUCT_KANA)) {
			param.put(ProductService.Param.PRODUCT_KANA, super
					.createPartialSearchCondition((String) conditions
							.get(ProductService.Param.PRODUCT_KANA)));
		}

		// 仕入先コード(完全一致)
		if (conditions.containsKey(ProductService.Param.SUPPLIER_CODE)) {
			param.put(ProductService.Param.SUPPLIER_CODE, conditions
					.get(ProductService.Param.SUPPLIER_CODE));
		}

		// 仕入先名
		if (conditions.containsKey(ProductService.Param.SUPPLIER_NAME)) {
			param.put(ProductService.Param.SUPPLIER_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(ProductService.Param.SUPPLIER_NAME)));
		}

		// セット分類
		if (conditions.containsKey(ProductService.Param.SET_TYPE_CATEGORY)) {
			param.put(ProductService.Param.SET_TYPE_CATEGORY, conditions
					.get(ProductService.Param.SET_TYPE_CATEGORY));
		}

		// 標準化分類
		if (conditions
				.containsKey(ProductService.Param.PRODUCT_STANDARD_CATEGORY)) {
			param
					.put(
							ProductService.Param.PRODUCT_STANDARD_CATEGORY,
							conditions
									.get(ProductService.Param.PRODUCT_STANDARD_CATEGORY));
		}

		// 分類保管
		if (conditions.containsKey(ProductService.Param.PRODUCT_STOCK_CATEGORY)) {
			param.put(ProductService.Param.PRODUCT_STOCK_CATEGORY, conditions
					.get(ProductService.Param.PRODUCT_STOCK_CATEGORY));
		}

		// 分類状況
		if (conditions
				.containsKey(ProductService.Param.PRODUCT_STATUS_CATEGORY)) {
			param.put(ProductService.Param.PRODUCT_STATUS_CATEGORY, conditions
					.get(ProductService.Param.PRODUCT_STATUS_CATEGORY));
		}

		// 在庫管理
		if (conditions.containsKey(ProductService.Param.STOCK_CTL_CATEGORY)) {
			param.put(ProductService.Param.STOCK_CTL_CATEGORY, conditions
					.get(ProductService.Param.STOCK_CTL_CATEGORY));
		}

		if (conditions.containsKey(ProductService.Param.RACK_MULTI_FLAG)) {
			param.put(ProductService.Param.RACK_MULTI_FLAG, conditions
					.get(ProductService.Param.RACK_MULTI_FLAG));
		}

		// 備考
		if (conditions.containsKey(ProductService.Param.REMARKS)) {
			param.put(ProductService.Param.REMARKS, super
					.createPartialSearchCondition((String) conditions
							.get(ProductService.Param.REMARKS)));
		}

		// 商品分類（大）
		if (conditions.containsKey(ProductService.Param.PRODUCT1)) {
			param.put(ProductService.Param.PRODUCT1, conditions
					.get(ProductService.Param.PRODUCT1));
		}

		// 商品分類（中）
		if (conditions.containsKey(ProductService.Param.PRODUCT2)) {
			param.put(ProductService.Param.PRODUCT2, conditions
					.get(ProductService.Param.PRODUCT2));
		}

		// 商品分類（小）
		if (conditions.containsKey(ProductService.Param.PRODUCT3)) {
			param.put(ProductService.Param.PRODUCT3, conditions
					.get(ProductService.Param.PRODUCT3));
		}

		// 補充発注用の保有数＜＝発注点条件を付加するかどうか
		if (conditions
				.containsKey(ProductService.Param.HOLDING_STOCK_LESS_THAN_PO_NUM)) {
			param
					.put(
							ProductService.Param.HOLDING_STOCK_LESS_THAN_PO_NUM,
							conditions
									.get(ProductService.Param.HOLDING_STOCK_LESS_THAN_PO_NUM));
		}

		// 委託在庫数 ＝ 0
		if (conditions.containsKey(ProductService.Param.ENTRUST_STOCK_ZERO)) {
			param.put(ProductService.Param.ENTRUST_STOCK_ZERO, conditions
					.get(ProductService.Param.ENTRUST_STOCK_ZERO));
		}

		// 委託在庫数＞＝1
		if (conditions
				.containsKey(ProductService.Param.ENTRUST_STOCK_LARGER_THAN_ZERO)) {
			param
					.put(
							ProductService.Param.ENTRUST_STOCK_LARGER_THAN_ZERO,
							conditions
									.get(ProductService.Param.ENTRUST_STOCK_LARGER_THAN_ZERO));
		}

		// 委託在庫の発注量 > 0
		if (conditions
				.containsKey(ProductService.Param.ENTRUST_PORDER_QUANTITY_LARGER_THAN_ZERO)) {
			param
					.put(
							ProductService.Param.ENTRUST_PORDER_QUANTITY_LARGER_THAN_ZERO,
							conditions
									.get(ProductService.Param.ENTRUST_PORDER_QUANTITY_LARGER_THAN_ZERO));
		}

		// 保有数0の商品は除く
		if (conditions
				.containsKey(ProductService.Param.EXCLUDES_HOLDING_STOCK_ZERO)) {
			param
					.put(
							ProductService.Param.EXCLUDES_HOLDING_STOCK_ZERO,
							conditions
									.get(ProductService.Param.EXCLUDES_HOLDING_STOCK_ZERO));
		}

		// 平均出荷数0の商品は除く
		if (conditions
				.containsKey(ProductService.Param.EXCLUDES_AVG_SHIP_COUNT_ZERO)) {
			param
					.put(
							ProductService.Param.EXCLUDES_AVG_SHIP_COUNT_ZERO,
							conditions
									.get(ProductService.Param.EXCLUDES_AVG_SHIP_COUNT_ZERO));
		}

		// 保有数 > 平均出荷数 となる商品は除く
		if (conditions
				.containsKey(ProductService.Param.EXCLUDES_AVG_LESS_THAN_HOLDING_STOCK)) {
			param
					.put(
							ProductService.Param.EXCLUDES_AVG_LESS_THAN_HOLDING_STOCK,
							conditions
									.get(ProductService.Param.EXCLUDES_AVG_LESS_THAN_HOLDING_STOCK));
		}

		// 検索結果に発注情報を追加する
		if (conditions.containsKey(ProductService.Param.ADD_PORDER_INFO)) {
			param.put(ProductService.Param.ADD_PORDER_INFO, conditions
					.get(ProductService.Param.ADD_PORDER_INFO));
		}

		// ソートカラム
		if (StringUtil.hasLength(sortColumn)) {
			if (ProductService.Param.SUPPLIER_NAME.equals(sortColumn)) {
				// 仕入先マスタのカラムでソート
				param.put(ProductService.Param.SORT_BY_SUPPLIER, true);
			} else {
				// 商品マスタのカラムでソート
				param.put(ProductService.Param.SORT_BY_PRODUCT, true);
			}
			param.put(ProductService.Param.SORT_COLUMN, StringUtil
					.convertColumnName(sortColumn));
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(ProductService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(ProductService.Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件マップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(ProductService.Param.PRODUCT_CODE, null);
		param.put(ProductService.Param.SUPPLIER_PCODE, null);
		param.put(ProductService.Param.JAN_PCODE, null);
		param.put(ProductService.Param.PRODUCT_NAME, null);
		param.put(ProductService.Param.PRODUCT_KANA, null);
		param.put(ProductService.Param.SUPPLIER_CODE, null);
		param.put(ProductService.Param.SUPPLIER_NAME, null);
		param.put(ProductService.Param.SET_TYPE_CATEGORY, null);
		param.put(ProductService.Param.PRODUCT_STANDARD_CATEGORY, null);
		param.put(ProductService.Param.PRODUCT_STATUS_CATEGORY, null);
		param.put(ProductService.Param.PRODUCT_STOCK_CATEGORY, null);
		param.put(ProductService.Param.REMARKS, null);
		param.put(ProductService.Param.PRODUCT1, null);
		param.put(ProductService.Param.PRODUCT2, null);
		param.put(ProductService.Param.PRODUCT3, null);
		param.put(ProductService.Param.STOCK_CTL_CATEGORY_ID, null);
		param.put(ProductService.Param.PRODUCT_STATUS_CATEGORY_ID, null);
		param.put(ProductService.Param.PRODUCT_STOCK_CATEGORY_ID, null);
		param.put(ProductService.Param.PRODUCT_PURVAY_CATEGORY_ID, null);
		param.put(ProductService.Param.PRODUCT_STANDARD_CATEGORY_ID, null);
		param.put(ProductService.Param.SET_TYPE_CATEGORY_ID, null);
		param.put(ProductService.Param.UNIT_CATEGORY_ID, null);
		param.put(ProductService.Param.WEIGHT_UNIT_SIZE_CATEGORY_ID, null);
		param.put(ProductService.Param.LENGTH_UNIT_SIZE_CATEGORY_ID, null);
		param.put(ProductService.Param.SORT_BY_PRODUCT, null);
		param.put(ProductService.Param.SORT_BY_SUPPLIER, null);
		param.put(ProductService.Param.SORT_COLUMN, null);
		param.put(ProductService.Param.SORT_ORDER, null);
		param.put(ProductService.Param.ROW_COUNT, null);
		param.put(ProductService.Param.OFFSET, null);
		param.put(ProductService.Param.PRODUCT_STATUS_SALE_CANCEL,
				CategoryTrns.PRODUCT_STATUS_SALE_CANCEL);
		param.put(ProductService.Param.STOCK_CTL_CATEGORY, null);
		param.put(ProductService.Param.RACK_MULTI_FLAG, null);
		param.put(ProductService.Param.HOLDING_STOCK_LESS_THAN_PO_NUM, null);
		return param;
	}

	/**
	 * 商品コードを指定して、関連テーブル情報のマップを返します.
	 * @param productCode 商品コード
	 * @return 関連テーブル情報のマップ
	 * @throws ServiceException
	 */
	public Map<String, Object> countRelations(String productCode)
			throws ServiceException {
		try {
			// 関連データの存在チェック
			Map<String, Object> param = super.createSqlParam();
			param.put(ProductService.Param.PRODUCT_CODE, productCode);
			BeanMap result = this.selectBySqlFile(BeanMap.class,
					"product/CountRelations.sql", param).getSingleResult();

			HashMap<String, Object> temp = new HashMap<String, Object>();
			if (result == null) {
				return temp;
			}
			temp.putAll(result);
			return temp;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param dto {@link ProductDto}
	 * @throws ServiceException
	 * @throws UnabledLockException
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#deleteRecord(java.lang.Object)
	 */
	@Override
	public void deleteRecord(ProductDto dto) throws ServiceException,
			UnabledLockException {
		try {
			// 排他制御
			Map<String, Object> params = super.createSqlParam();
			params.put(ProductService.Param.PRODUCT_CODE, dto.productCode);
			super.lockRecordBySqlFile("product/LockProductByCode.sql", params,
					dto.updDatetm);

			// パラメータ作成
			params = super.createSqlParam();
			params.put(ProductService.Param.PRODUCT_CODE, dto.productCode);

			// 削除
			this.updateBySqlFile("product/DeleteProductByCode.sql", params)
					.execute();

			// 数量割引リレーションの削除
			this.updateProductDiscount(dto.productCode, null,
					dto.discountUpdDatetm);
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param dto {@link ProductDto}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#insertRecord(java.lang.Object)
	 */
	public void insertRecord(ProductDto dto) throws ServiceException {
		try {
			// パラメータ作成
			Map<String, Object> params = super.createSqlParam();

			ProductJoin entity = Beans.createAndCopy(ProductJoin.class, dto)
					.dateConverter(Constants.FORMAT.DATE).timestampConverter(
							Constants.FORMAT.TIMESTAMP).execute();
			BeanMap props = Beans.createAndCopy(BeanMap.class, entity)
					.excludes(AbstractService.Param.CRE_FUNC,
							AbstractService.Param.CRE_DATETM,
							AbstractService.Param.CRE_USER,
							AbstractService.Param.UPD_FUNC,
							AbstractService.Param.UPD_DATETM,
							AbstractService.Param.UPD_USER).execute();
			params.putAll(props);

			// 登録
			this.updateBySqlFile("product/InsertProduct.sql", params).execute();

			this.updateProductDiscount(dto.productCode, dto.discountId,
					dto.discountUpdDatetm);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param dto {@link ProductDto}
	 * @throws ServiceException
	 * @throws UnabledLockException
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#updateRecord(java.lang.Object)
	 */
	public void updateRecord(ProductDto dto) throws ServiceException,
			UnabledLockException {
		try {
			// 排他制御
			Map<String, Object> params = super.createSqlParam();
			params.put(Param.PRODUCT_CODE, dto.productCode);
			super.lockRecordBySqlFile("product/LockProductByCode.sql", params,
					dto.updDatetm);

			// パラメータ作成
			params = super.createSqlParam();

			ProductJoin entity = Beans.createAndCopy(ProductJoin.class, dto)
					.dateConverter(Constants.FORMAT.DATE).timestampConverter(
							Constants.FORMAT.TIMESTAMP).execute();
			BeanMap props = Beans.createAndCopy(BeanMap.class, entity)
					.excludes(AbstractService.Param.CRE_FUNC,
							AbstractService.Param.CRE_DATETM,
							AbstractService.Param.CRE_USER,
							AbstractService.Param.UPD_FUNC,
							AbstractService.Param.UPD_DATETM,
							AbstractService.Param.UPD_USER).execute();
			params.putAll(props);

			// 更新
			this.updateBySqlFile("product/UpdateProduct.sql", params).execute();

			if (dto.discountIdChanged) {
				this.updateProductDiscount(dto.productCode, dto.discountId,
						dto.discountUpdDatetm);
			}
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品の数量割引情報を更新します.
	 * @param productCode 商品コード
	 * @param discountId 割引ID
	 * @param updDatetm 更新日時の文字列
	 * @throws Exception
	 */
	private void updateProductDiscount(String productCode, String discountId,
			String updDatetm) throws Exception {
		if (StringUtil.hasLength(updDatetm)) {
			if (StringUtil.hasLength(discountId)) {
				// 更新
				this.discountRelService.updateDiscountRel(productCode,
						discountId, updDatetm);
			} else if (!StringUtil.hasLength(discountId)) {
				// 削除
				this.discountRelService.deleteDiscountRel(productCode, null,
						updDatetm);
			}
		} else {
			if (StringUtil.hasLength(discountId)) {
				// 追加
				this.discountRelService.insertDiscountRel(productCode,
						discountId);
			}
		}
	}

	/**
	 * 商品コードに前方一致する商品の件数を返します.
	 * @param prefixCode 検索文字列
	 * @return 検索結果数
	 * @throws ServiceException
	 */
	public Long findProductCntByCodeLike(String prefixCode)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(ProductService.Param.PRODUCT_CODE,
					createPrefixSearchCondition(prefixCode));

			BeanMap map = this.selectBySqlFile(BeanMap.class,
					"product/FindProductCntByCodeLike.sql", param)
					.getSingleResult();

			return (Long) map.get("count");

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードを指定して、商品情報のリストを返します.
	 * @param productCode 商品コード
	 * @return 商品情報{@link Product}のリスト
	 * @throws ServiceException
	 */
	public List<Product> findProductByCodeLike(String productCode)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(ProductService.Param.PRODUCT_CODE,
					createPrefixSearchCondition(productCode));
			return this.selectBySqlFile(Product.class,
					"product/FindProductByCodeLike.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードを指定して、商品情報のリストを返します.
	 * @param productCode 商品コード
	 * @return 商品情報{@link ProductJoin}のリスト
	 * @throws ServiceException
	 */
	public ProductJoin findProductInfosWithNamesByCode(String productCode)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(ProductService.Param.PRODUCT_CODE, productCode);
			// 各区分のコードをPUT
			param.put(ProductService.Param.STOCK_CTL_CATEGORY_ID,
					Categories.PRODUCT_STOCK_CTL);
			param.put(ProductService.Param.PRODUCT_STATUS_CATEGORY_ID,
					Categories.PRODUCT_STATUS);
			param.put(ProductService.Param.PRODUCT_STOCK_CATEGORY_ID,
					Categories.PRODUCT_STOCK);
			param.put(ProductService.Param.PRODUCT_PURVAY_CATEGORY_ID,
					Categories.PRODUCT_PURVAY);
			param.put(ProductService.Param.PRODUCT_STANDARD_CATEGORY_ID,
					Categories.PRODUCT_STANDARD);
			param.put(ProductService.Param.SET_TYPE_CATEGORY_ID,
					Categories.PRODUCT_SET_TYPE);
			param.put(ProductService.Param.UNIT_CATEGORY_ID,
					Categories.PRODUCT_UNIT);
			param.put(ProductService.Param.WEIGHT_UNIT_SIZE_CATEGORY_ID,
					Categories.PRODUCT_UNIT_WEIGHT);
			param.put(ProductService.Param.LENGTH_UNIT_SIZE_CATEGORY_ID,
					Categories.PRODUCT_UNIT_SIZE);

			return this.selectBySqlFile(ProductJoin.class,
					"product/FindProductInfosWithNamesByCode.sql", param)
					.getSingleResult();

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * オンライン品番を指定して、商品情報を返します.
	 * @param onlinePcode オンライン品番
	 * @return 商品情報{@link ProductJoin}
	 * @throws ServiceException
	 */
	public ProductJoin findProductByOnlinePCode(String onlinePcode)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);
			param.put(ProductService.Param.ONLINE_PCODE, onlinePcode);

			this.setConditionCategoryId(param);

			return this.selectBySqlFile(ProductJoin.class,
					"product/FindProductByOnlinePCode.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件にカテゴリコードを設定します.
	 * @param param 検索条件のマップ
	 */
	private void setConditionCategoryId(Map<String, Object> param) {
		// セット分類ID
		param.put(CategoryService.Param.CATEGORY_ID,
				Categories.PRODUCT_SET_TYPE);

		// 単位コードID
		param.put(ProductService.Param.UNIT_CATEGORY_ID,
				Categories.PRODUCT_UNIT);

		// 分類状況ID
		param.put(ProductService.Param.PRODUCT_STATUS_CATEGORY_ID,
				Categories.PRODUCT_STATUS);

		// 分類保管ID
		param.put(ProductService.Param.PRODUCT_STOCK_CATEGORY_ID,
				Categories.PRODUCT_STOCK);
	}

	/**
	 * 商品マスタを検索し、検索結果をExcelファイルとしてレスポンスに出力する
	 *
	 * @param productExcelDtoList
	 * @throws Exception
	 */
	/**
	 * 商品マスタの検索結果をExcel形式でレスポンスに出力します.
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param httpRequest {@link HttpServletRequest}
	 * @param httpResponse {@link HttpServletResponse }
	 * @throws Exception
	 */
	public void downloadProductExcel(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws Exception {
		try {
			// 商品マスタを検索
			List<ProductJoin> productJoinList = this.findByCondition(
					conditions, sortColumn, sortOrderAsc);

			// 検索結果をExcelデータオブジェクトに変換する
			List<ProductExcelRowDto> productExcelDtoList = new ArrayList<ProductExcelRowDto>();
			for (ProductJoin productJoin : productJoinList) {
				ProductExcelRowDto dto = Beans.createAndCopy(
						ProductExcelRowDto.class, productJoin)
						.timestampConverter(Constants.FORMAT.TIMESTAMP)
						.dateConverter(Constants.FORMAT.DATE).execute();
				productExcelDtoList.add(dto);
			}

			// Excelファイルデータを構築
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet(MessageResourcesUtil
					.getMessage("labels.product.csv.sheetName"));

			int rowCount = 0;
			ProductExcelRowDto.writeHeaderLine(sheet.createRow(rowCount));
			rowCount++;
			for (ProductExcelRowDto productExcelDto : productExcelDtoList) {
				productExcelDto.writeRow(sheet.createRow(rowCount));
				rowCount++;
			}

			OutputStream os = null;
			try {
				os = super.createOutputStream(Product.TABLE_NAME + ".xls",
						httpRequest, httpResponse);
				workbook.write(os);
			} finally {
				os.close();
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Excelファイル内容から、商品情報を更新します.
	 * @param workbook Excelファイル情報
	 * @return 更新した件数
	 * @throws ServiceException
	 * @throws FileImportException
	 * @throws UnabledLockException
	 */
	public int updateProductsFromExcel(HSSFWorkbook workbook)
			throws ServiceException, FileImportException, UnabledLockException {
		FileImportException e = new FileImportException();
		try {
			if (workbook == null) {
				return 0;
			}

			HSSFSheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return 0;
			}

			// Excelファイルの行データをDtoに変換する
			int rowCounter = 1;
			List<ProductExcelRowDto> productExcelDtoList = new ArrayList<ProductExcelRowDto>();
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				ProductExcelRowDto dto = new ProductExcelRowDto();
				dto.lineNo = rowCounter;
				dto.loadRow(sheet.getRow(i));
				productExcelDtoList.add(dto);
				rowCounter++;
			}

			// Dtoをエンティティクラスに変換する(Decimalは小数3桁で切り捨て)
			NumberConverter conv = new NumberConverter("0", 3, false);

			List<ProductJoin> productList = new ArrayList<ProductJoin>();
			for (ProductExcelRowDto dto : productExcelDtoList) {
				if (e.getMessageCount() >= 10) {
					throw e;
				}

				if (dto.isEmptyRow()) {
					// 空行
					continue;
				}

				// 入力値チェック
				e.addMessages(dto.validate());
				if (e.getMessageCount() > 0) {
					continue;
				}
				dto.productCode = dto.productCode.toUpperCase();

				try {
					// エンティティに変換
					ProductJoin entity = Beans.createAndCopy(ProductJoin.class,
							dto).lrTrim().dateConverter(Constants.FORMAT.DATE)
							.timestampConverter(Constants.FORMAT.TIMESTAMP)
							.converter(conv, Param.SUPPLIER_PRICE_YEN,
									Param.SUPPLIER_PRICE_DOL,
									Param.RETAIL_PRICE, Param.SO_RATE,
									Param.PO_LOT).execute();
					productList.add(entity);
				} catch (ConverterRuntimeException ex) {
					// 変換エラー
					e.addInvalidMessage(dto.lineNo, ex.getPropertyName());
				} catch (IllegalPropertyRuntimeException ex) {
					// 変換エラー
					e.addInvalidMessage(dto.lineNo, ex.getPropertyName());
				}
			}

			if (e.getMessageCount() != 0) {
				throw e;
			}
			return this.updateProducts(productList);
		} catch (FileImportException ex) {
			throw ex;
		} catch (UnabledLockException ule) {
			throw ule;
		} catch (ServiceException se) {
			throw se;
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}
	}

	/**
	 * 商品情報を更新します.
	 * @param productList 商品情報のリスト
	 * @return 更新した件数
	 * @throws Exception
	 * @throws UnabledLockException
	 */
	private int updateProducts(List<ProductJoin> productList) throws Exception,
			UnabledLockException {
		if (productList == null || productList.size() == 0) {
			return 0;
		}

		try {
			// 商品マスタ、商品マスタ履歴、数量割引関連、数量割引関連履歴テーブルをロックする
			try {
				super.updateBySqlFile("product/LockProductTable.sql",
						super.createSqlParam()).execute();
			} catch (Exception e) {
				throw new UnabledLockException(e);
			}

			int count = 0;
			for (ProductJoin product : productList) {
				// 商品
				Map<String, Object> param = super.createSqlParam();
				this.setEmptyCondition(param);
				param.put(Param.PRODUCT_CODE, product.productCode);
				this.setConditionCategoryId(param);

				// DBから既存の商品を取得する
				ProductJoin p = super.selectBySqlFile(ProductJoin.class,
						"product/FindProductByCodeAfterLock.sql", param)
						.getSingleResult();

				param = super.createSqlParam();
				this.setEmptyCondition(param);
				param.putAll(Beans.createAndCopy(BeanMap.class, product)
						.excludes(AbstractService.Param.CRE_FUNC,
								AbstractService.Param.CRE_DATETM,
								AbstractService.Param.CRE_USER,
								AbstractService.Param.UPD_FUNC,
								AbstractService.Param.UPD_DATETM,
								AbstractService.Param.UPD_USER).execute());
				 
				if (p == null) {
					// 新規：Excelで取り込んだデータがDBに存在しない場合は商品を登録する
					count += super.updateBySqlFile("product/InsertProduct.sql",
							param).execute();
					
					// 新規登録した商品を取得する
					param = super.createSqlParam();
					this.setEmptyCondition(param);
					param.put(Param.PRODUCT_CODE, product.productCode);
					this.setConditionCategoryId(param);
					
					p = super.selectBySqlFile(ProductJoin.class,
							"product/FindProductByCodeAfterLock.sql", param)
							.getSingleResult();
					
				} else {
					// 更新
					count += super.updateBySqlFile("product/UpdateProduct.sql",
							param).execute();
				}

				// 数量割引（ロック等不要なので履歴以外にDiscountRelServiceは使わない）
				param = super.createSqlParam();
				param.put(Param.PRODUCT_CODE, product.productCode);

				// 取り込んだExcelには数量割引コードがあり、実データの方にも数量割引コードがある場合
				if (StringUtil.hasLength(product.discountId)) {
					if (StringUtil.hasLength(p.discountId)) {
						if (!product.discountId.equals(p.discountId)) {
							// 数量割引リレーション更新

							// 一旦削除し、再投入する
							param.put(DiscountRelService.Param.DISCOUNT_ID,
									p.discountId);
							super
									.updateBySqlFile(
											"discount/DeleteDiscountRelByDiscountIdAndProductCode.sql",
											param).execute();
							param.put(DiscountRelService.Param.DISCOUNT_ID,
									product.discountId);
							super.updateBySqlFile(
									"discount/InsertDiscountRel.sql", param)
									.execute();
						}
					} else {
						// 追加
						param.put(DiscountRelService.Param.DISCOUNT_ID,
								product.discountId);
						super.updateBySqlFile("discount/InsertDiscountRel.sql",
								param).execute();
					}
				} else {
					if ((p != null) && (StringUtil.hasLength(p.discountId))) {
						param.put(DiscountRelService.Param.DISCOUNT_ID,
								p.discountId);
						super
								.updateBySqlFile(
										"discount/DeleteDiscountRelByDiscountIdAndProductCode.sql",
										param).execute();
					}
				}
			}

			return count;
		} catch (Exception e) {
			userTransaction.rollback();
			userTransaction.begin();
			throw e;
		} finally {
			// テーブルをアンロックする
			super.updateBySqlFile("product/UnlockProductTable.sql",
					super.createSqlParam()).execute();
		}
	}

	/**
	 * 商品コードを指定して、商品存在情報を返します.
	 * @param productCode 商品コード
	 * @return 商品存在情報{@link ExistsCheckStateDto}
	 * @throws ServiceException
	 */
	public ExistsCheckStateDto existsProductCode(String productCode)
			throws ServiceException {
		if (productCode == null) {
			return null;
		}
		List<ExistsCheckStateDto> checkResult = existsProductCode(Arrays
				.asList(new String[] { productCode }));
		if (checkResult.size() > 0) {
			return checkResult.get(0);
		}
		return null;
	}

	/**
	 * 商品コードを複数指定して、商品存在情報のリストを返します.
	 * @param productCode 商品コードの配列
	 * @return 商品存在情報{@link ExistsCheckStateDto}のリスト
	 * @throws ServiceException
	 */
	public List<ExistsCheckStateDto> existsProductCode(String[] productCode)
			throws ServiceException {
		if (productCode == null) {
			return new ArrayList<ExistsCheckStateDto>(0);
		}
		return existsProductCode(Arrays.asList(productCode));
	}

	/**
	 * 商品コードを複数指定して、商品存在情報のリストを返します.
	 * @param productCodeList 商品コードのリスト
	 * @return 商品存在情報{@link ExistsCheckStateDto}のリスト
	 * @throws ServiceException
	 */
	public List<ExistsCheckStateDto> existsProductCode(
			List<String> productCodeList) throws ServiceException {
		if (productCodeList == null || productCodeList.size() == 0) {
			return new ArrayList<ExistsCheckStateDto>(0);
		}

		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);
			param.put(Param.PRODUCT_CODE_LIST, productCodeList);

			// コードによる検索を行う
			List<BeanMap> rawCheckList = this.selectBySqlFile(BeanMap.class,
					"product/FindProductsByCodeList.sql", param)
					.getResultList();

			// 存在したコードをキーとするマップを作成する
			Map<String, Object> existsCodeMap = new HashMap<String, Object>();
			for (BeanMap map : rawCheckList) {
				existsCodeMap.put((String) map.get(Param.PRODUCT_CODE), map);
			}

			// それぞれのコードの存在をチェックする
			List<ExistsCheckStateDto> resultList = new ArrayList<ExistsCheckStateDto>(
					productCodeList.size());
			for (String code : productCodeList) {
				ExistsCheckStateDto dto = new ExistsCheckStateDto();
				dto.code = code;

				if (code != null) {
					code = code.toUpperCase();
				}

				if (existsCodeMap.containsKey(code)) {
					dto.exists = true;
				} else {
					dto.exists = false;
				}

				resultList.add(dto);
			}

			return resultList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、商品の在庫情報のリストを返します.
	 * @param conditions 検索条件のマップ
	 * @return 在庫情報{@link ProductStockInfoDto}のリスト
	 * @throws ServiceException
	 */
	public List<ProductStockInfoDto> aggregateProductStockInfoByCondition(
			Map<String, Object> conditions) throws ServiceException {

		return aggregateProductStockInfoByCondition(conditions,
				Param.PRODUCT_CODE, true);
	}

	/**
	 * 検索条件およびソート条件を指定して、商品の在庫情報のリストを返します.
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @return 在庫情報{@link ProductStockInfoDto}のリスト
	 * @throws ServiceException
	 */
	public List<ProductStockInfoDto> aggregateProductStockInfoByCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			// 集計期間
			if (conditions.containsKey(Param.AGGREGATE_MONTHS_RANGE)) {
				if (conditions.get(Param.AGGREGATE_MONTHS_RANGE) instanceof String) {
					// 期間条件を数値に変換
					conditions.put(Param.AGGREGATE_MONTHS_RANGE, Integer
							.parseInt((String) conditions
									.get(Param.AGGREGATE_MONTHS_RANGE)));
				}

				param.put(Param.AGGREGATE_MONTHS_RANGE, conditions
						.get(Param.AGGREGATE_MONTHS_RANGE));
			} else {
				// 指定がなければ期間集計は行わない
				param.put(Param.AGGREGATE_MONTHS_RANGE, null);
			}

			// 受注実績のある商品
			if (conditions.containsKey(Param.RO_EXISTS)) {
				param.put(Param.RO_EXISTS, conditions.get(Param.RO_EXISTS));
			} else {
				param.put(Param.RO_EXISTS, null);
			}

			this.setConditionAggregate(conditions, sortColumn, sortOrderAsc,
					param);

			List<BeanMap> productStdDevList = null;
			if (conditions.containsKey(Param.AGGREGATE_MONTHS_RANGE)) {
				// 集計期間指定がある場合には期間中の出荷数標準偏差を求める

				// 同一セッションからの集計リクエストが重複する事もありえるのでセッションIDに現在のミリ秒を連結する
				String uniqueId = super.httpSession.getId() + "-"
						+ String.valueOf(System.currentTimeMillis());
				param.put(Param.SESSION_ID, uniqueId);

				// 年月度一時テーブルに指定期間中の年月度データを登録する
				Calendar cal = Calendar.getInstance();
				for (int i = 0; i < (Integer) conditions
						.get(Param.AGGREGATE_MONTHS_RANGE) + 1; i++) {
					param.put(Param.YM, Integer.parseInt(StringUtil
							.getDateString(Constants.FORMAT.DATEYM, cal
									.getTime())));
					this.updateBySqlFile("product/InsertStdDevYmWork.sql",
							param).execute();
					cal.add(Calendar.MONTH, -1);
				}

				// 年月度と商品コードの直積表を作成
				this.updateBySqlFile("product/InsertStdDevAggregateYmWork.sql",
						param).execute();
				// 年月度・商品コードごとの売上数量を集計
				this.updateBySqlFile(
						"product/InsertStdDevProductSalesYmWork.sql", param)
						.execute();
				// 集計した数量で直積表を更新する
				this.updateBySqlFile("product/UpdateStdDevAggregateYmWork.sql",
						param).execute();

				// 出荷数偏差の計算結果を取得する
				productStdDevList = this.selectBySqlFile(BeanMap.class,
						"product/AggregateSalesStandardDeviation.sql", param)
						.getResultList();

				// 一時テーブルをクリアする
				this.updateBySqlFile("product/DeleteStdDevYmWork.sql", param)
						.execute();
				this.updateBySqlFile("product/DeleteStdDevAggregateYmWork.sql",
						param).execute();
				this.updateBySqlFile(
						"product/DeleteStdDevProductSalesYmWork.sql", param)
						.execute();
			}

			List<ProductStockInfo> infoList = this.selectBySqlFile(
					ProductStockInfo.class,
					"product/AggregateProductStockInfoByCondition.sql", param)
					.getResultList();

			List<ProductStockInfoDto> dtoList = new ArrayList<ProductStockInfoDto>();
			for (ProductStockInfo info : infoList) {
				ProductStockInfoDto dto = Beans.createAndCopy(
						ProductStockInfoDto.class, info).execute();
				if (productStdDevList != null) {
					dto.salesStandardDeviation = BigDecimal.ZERO;
					// 出荷数標準偏差を設定する
					for (BeanMap stdDev : productStdDevList) {
						if (dto.productCode.equals(stdDev
								.get(Param.PRODUCT_CODE))) {
							dto.salesStandardDeviation = new BigDecimal(
									(Double) stdDev
											.get(Param.SALES_STANDARD_DEVIATION));
							break;
						}
					}
				}
				dtoList.add(dto);
			}
			return dtoList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @return {PRODUCT_CODE}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getKeyColumnNames()
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "PRODUCT_CODE" };
	}

	/**
	 *
	 * @return {@link ProductJoin#TABLE_NAME}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getTableName()
	 */
	@Override
	protected String getTableName() {
		return ProductJoin.TABLE_NAME;
	}
}
