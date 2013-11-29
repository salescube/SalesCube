/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.dialog.ShowStockInfoDialogAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.ajax.CommonProductForm;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.ProductStockService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 商品情報を取得するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonProductAction extends CommonAjaxResources {

	@ActionForm
	@Resource
	protected CommonProductForm commonProductForm;

	@Resource
	protected EadService eadService;

	@Resource
	protected ProductService productService;

	@Resource
	public ProductStockService productStockService;

	/**
	 * 在庫数情報
	 */
	public StockInfoDto stockInfoDto;

	/**
	 * 日付の形式指定
	 */
	SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);

	public static class Param {
		public static final String PRODUCT_COUNT = "productCount";
		public static final String SUPPLIER_CODE = "supplierCode";
		public static final String SUPPLIER_PCODE = "supplierPcode";
		public static final String PRODUCT_CODE = "productCode";
		public static final String PRODUCT_NAME = "productName";
		public static final String PO_LOT = "poLot";
		public static final String MAX_PO_NUM = "maxPoNum";
		public static final String MAX_STOCK_NUM = "maxStockNum";
		public static final String DISCARD_DATE = "discardDate";
		public static final String SUPPLIER_PRICE_YEN = "supplierPriceYen";
		public static final String SUPPLIER_PRICE_DOL = "supplierPriceDol";
		public static final String RETAIL_PRICE = "retailPrice";
		public static final String RO_MAX_NUM = "roMaxNum";
		public static final String SET_TYPE_CATEGORY = "setTypeCategory";
		public static final String DISCARDED = "discarded";
		public static final String MOVABLE_QUANTITY = "movableQuantity";
		public static final String RACK_CODE = "rackCode";
		public static final String FRACT_CATEGORY = "fractCategory";
		public static final String TAX_CATEGORY = "taxCategory";
		public static final String REMARKS = "remarks";
		public static final String EAD_REMARKS = "eadRemarks";
		public static final String UNIT_CATEGORY = "unitCategory";
		public static final String UNIT_CATEGORY_NAME = "unitCategoryName";
		public static final String PACK_QUANTITY = "packQuantity";
		public static final String ONLINE_PCODE = "onlinePcode";
		public static final String LENGTH = "length";
		public static final String SO_RATE = "soRate";
		public static final String STOCK_CTL_CATEGORY = "stockCtlCategory";
		public static final String MOVABLE_DEST_QUANTITY = "movableDestQuantity";
	}

	/**
	 * 商品コードから商品情報を取得します.
	 * @return 商品情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getProductInfos() throws Exception {
		ProductJoin product;
		int movableQuantity;
		try {

			product = productService
					.findById(commonProductForm.productCode);
			this.stockInfoDto = this.productStockService
					.calcStockQuantityByProductCode(commonProductForm.productCode);

			// 棚番コードの指定がない時は標準棚を設定
			if (product != null
					&& !StringUtil.hasLength(commonProductForm.rackCode)) {
				commonProductForm.rackCode = product.rackCode;
			}
			// 移動可能数(棚在庫数)を計算
			int unclosedQuantity = eadService
					.countUnclosedQuantityByProductCode(
							commonProductForm.productCode,
							commonProductForm.rackCode);
			int closedQuantity = productStockService
					.countClosedQuantityByProductCode(
							commonProductForm.productCode,
							commonProductForm.rackCode);
			movableQuantity = unclosedQuantity + closedQuantity;
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		if (product != null) {
			map.put(Param.SUPPLIER_CODE, (product.supplierCode == null ? ""
					: product.supplierCode));
			map.put(Param.PRODUCT_NAME, (product.productName == null ? ""
					: product.productName));
			map.put(Param.SUPPLIER_PCODE, (product.supplierPcode == null ? ""
					: product.supplierPcode));
			map.put(Param.PO_LOT, (product.poLot == null ? "" : product.poLot
					.toString()));
			map.put(Param.MAX_PO_NUM, (product.maxPoNum == null ? ""
					: product.maxPoNum.toString()));
			map.put(Param.MAX_STOCK_NUM, (product.maxStockNum == null ? ""
					: product.maxStockNum.toString()));
			map.put(Param.DISCARD_DATE, (product.discardDate == null ? ""
					: DF_YMD.format(product.discardDate)));
			map.put(Param.SUPPLIER_PRICE_YEN,
					(product.supplierPriceYen == null ? ""
							: product.supplierPriceYen.toString()));
			map.put(Param.SUPPLIER_PRICE_DOL,
					(product.supplierPriceDol == null ? ""
							: product.supplierPriceDol.toString()));
			map.put(Param.RACK_CODE, (product.rackCode == null ? ""
					: product.rackCode));

			// 上代
			map.put(Param.RETAIL_PRICE, (product.retailPrice == null ? ""
					: product.retailPrice.toString()));
			// 受注限度数
			map.put(Param.RO_MAX_NUM, (product.roMaxNum == null ? ""
					: product.roMaxNum.toString()));
			// セット商品フラグ
			map.put(Param.SET_TYPE_CATEGORY,
					(product.setTypeCategory == null ? ""
							: product.setTypeCategory));

			// 廃棄されている場合1（廃棄日が現在日以下）　その他の場合0
			map.put(Param.DISCARDED, (product.discarded == null ? ""
					: product.discarded));

			// 課税区分
			map.put(Param.TAX_CATEGORY, (product.taxCategory == null ? ""
					: product.taxCategory));
			// 備考
			map.put(Param.REMARKS, (product.remarks == null ? ""
					: product.remarks));
			// ピッキング備考
			map.put(Param.EAD_REMARKS, (product.eadRemarks == null ? ""
					: product.eadRemarks));
			// 単位コード
			map.put(Param.UNIT_CATEGORY, (product.unitCategory == null ? ""
					: product.unitCategory));
			// 単位名
			map.put(Param.UNIT_CATEGORY_NAME,
					(product.unitCategoryName == null ? ""
							: product.unitCategoryName));
			// オンライン品番
			map.put(Param.ONLINE_PCODE, (product.onlinePcode == null ? ""
					: product.onlinePcode));
			// 入り数
			map.put(Param.PACK_QUANTITY, (product.packQuantity == null ? ""
					: product.packQuantity.toString()));

			// 長さ
			map.put(Param.LENGTH, (product.length == null ? "" : product.length
					.toString()));

			// 特注品掛率
			map.put(Param.SO_RATE, (product.soRate == null ? ""
					: product.soRate.toString()));

			// 在庫管理区分
			map.put(Param.STOCK_CTL_CATEGORY,
					(product.stockCtlCategory == null ? ""
							: product.stockCtlCategory));
		}
		//検索結果が空=Nullなら空文字列で返却する
		else {
			ResponseUtil.write("", "text/javascript");
			return null;
		}

		// 商品在庫情報
		map.put(ShowStockInfoDialogAction.Param.PRODUCT_CODE,
				(stockInfoDto.productCode == null ? ""
						: stockInfoDto.productCode));
		map.put(ShowStockInfoDialogAction.Param.CURRENT_TOTAL_QUANTITY, (String
				.valueOf(stockInfoDto.currentTotalQuantity)));
		map.put(ShowStockInfoDialogAction.Param.RORDER_REST_QUANTITY, (String
				.valueOf(stockInfoDto.rorderRestQuantity)));
		map.put(ShowStockInfoDialogAction.Param.PORDER_REST_QUANTITY, (String
				.valueOf(stockInfoDto.porderRestQuantity)));
		map.put(ShowStockInfoDialogAction.Param.ENTRUST_REST_QUANTITY, (String
				.valueOf(stockInfoDto.entrustRestQuantity)));
		map.put(ShowStockInfoDialogAction.Param.POSSIBLE_DROW_QUANTITY, (String
				.valueOf(stockInfoDto.possibleDrawQuantity)));
		map.put(ShowStockInfoDialogAction.Param.HOLDING_STOCK_QUANTITY, (String
				.valueOf(stockInfoDto.holdingStockQuantity)));

		// 移動可能数を設定
		if (StringUtil.hasLength(commonProductForm.rackCode)) {
			map.put(Param.MOVABLE_QUANTITY, String.valueOf(movableQuantity));
		} else {
			map.put(Param.MOVABLE_QUANTITY, "");
		}

		ResponseUtil.write(JSON.encode(map), "text/javascript");

		return null;
	}

	/**
	 * 商品コードから商品数を取得します（前方一致）.
	 * @return 商品数
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getProductByCodeLike() throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		try {
			// 該当件数を取得する（商品コードの前方一致）
			List<Product> productList = productService
					.findProductByCodeLike(commonProductForm.productCode);
			if (productList == null || productList.size() == 0) {
				map.put(Param.PRODUCT_COUNT, String.valueOf("0"));
				ResponseUtil.write(JSON.encode(map), "text/javascript");
			} else {
				// 1つに特定できたら、商品情報を設定する。
				if (productList.size() == 1) {
					Product product = productList.get(0);
					BeanMap bmap = super.createBeanMapWithNullToEmpty(product);
					bmap.put(Param.PRODUCT_COUNT, String.valueOf(productList
							.size()));
					ResponseUtil.write(JSON.encode(bmap), "text/javascript");
				} else {
					map.put(Param.PRODUCT_COUNT, String.valueOf(productList
							.size()));
					ResponseUtil.write(JSON.encode(map), "text/javascript");
				}
			}

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		return null;
	}

}
