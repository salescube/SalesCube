/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.DoubleRange;
import org.seasar.struts.annotation.DoubleType;
import org.seasar.struts.annotation.FloatType;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 商品画面（登録・編集）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class EditProductForm extends AbstractEditForm {

	@Required
	@Mask(mask = Constants.CODE_MASK.PRODUCT_MASK)
	@Maxlength(maxlength = Constants.CODE_SIZE.PRODUCT)
	public String productCode; // 商品コード

	@Required
	@Maxlength(maxlength = 60)
	public String productName; // 商品名

	@Maxlength(maxlength = 60)
	public String productKana; // 商品名カナ

	@Maxlength(maxlength = 50)
	public String onlinePcode; // コード

	@Maxlength(maxlength = 20)
	public String supplierPcode; // 仕入先品番

	@Maxlength(maxlength = 10)
	public String supplierCode; // 仕入先商品コード

	@Maxlength(maxlength = 10)
	public String warehouseName; // 倉庫名

	@Maxlength(maxlength = 10)
	public String rackCode; // 標準棚番コード

	@Required
	@DoubleRange(min="-999999999", max="999999999")
	public String supplierPriceYen; // 仕入単価（円）

	@DoubleRange(min="-999999999", max="999999999")
	public String supplierPriceDol; // 仕入単価（＄）

	@DoubleRange(min="-999999999", max="999999999", arg0 = @Arg(key = "labels.retailPrice2", resource = true))
	public String retailPrice; // 上代

	@DoubleType
	@DoubleRange(min="0", max="999.999")
	public String soRate; // 特注品掛率

	public String unitCategory; // 単位コード

	@IntegerType
	@IntRange(min=0, max=Short.MAX_VALUE)
	public String packQuantity; // 入数

	public String janPcode; // JANコード

	@FloatType
	public String width; // 幅

	public String widthUnitSizeCategory; // 幅単位

	@FloatType
	public String depth; // 奥行き

	public String depthUnitSizeCategory; // 奥行き単位

	@FloatType
	public String height; // 高さ

	public String heightUnitSizeCategory; // 高さ単位

	@FloatType
	public String weight; // 重さ

	public String weightUnitSizeCategory; // 重さ単位

	@FloatType
	public String length; // 長さ

	public String lengthUnitSizeCategory; // 長さ単位

	@DoubleType
	public String poLot; // 発注ロット

	public String lotUpdFlag; // 発注ロット自動更新フラグ

	@IntegerType
	public String leadTime; // リードタイム

	@IntegerType
	public String poNum; // 発注点

	public String poUpdFlag; // 発注点自動更新フラグ

	@IntegerType
	public String mineSafetyStock; // 安全在庫数

	public String mineSafetyStockUpdFlag; // 安全在庫数自動更新フラグ

	@IntegerType
	public String entrustSafetyStock; // 委託安全在庫数

	public String salesStandardDeviation; // 標準偏差

	@IntegerType
	public String avgShipCount; // 平均出荷数

	@IntegerType
	public String maxStockNum; // 最大保有数

	public String stockUpdFlag; // 最大保有数自動更新フラグ

	@IntegerType
	public String termShipNum; // 期間出荷数

	@IntegerType
	public String maxPoNum; // 発注限度数(単位発注限度)

	public String maxPoUpdFlag; // 発注限度数自動更新フラグ

	public String fractCategory; // 商品端数処理

	public String taxCategory; // 課税区分

	@Required
	public String stockCtlCategory; // 在庫管理区分

	public String stockAssesCategory; // 在庫評価方法

	public String productCategory; // 製品区分

	public String product1; // 商品分類１

	public String product2; // 商品分類２

	public String product3; // 商品分類３

	@IntegerType
	@IntRange(min=0, max=Short.MAX_VALUE)
	public String roMaxNum; // 受注限度数

	public String productRank; // 商品ランク

	public String setTypeCategory; // セット商品フラグ

	public String productStatusCategory; // 分類状況

	public String productStockCategory; // 分類保管

	public String productPurvayCategory; // 分類調達

	public String productStandardCategory; // 分類標準

	public String coreNum; // 芯数

	public String num1; // 整数１

	public String num2; // 整数２

	public String num3; // 整数３

	public String num4; // 整数４

	public String num5; // 整数５

	public String dec1; // 小数１

	public String dec2; // 小数２

	public String dec3; // 小数３

	public String dec4; // 小数４

	public String dec5; // 小数５

	@DateType(datePattern = Constants.FORMAT.DATE)
	public String discardDate; // 廃棄予定日

	@Maxlength(maxlength = 120)
	public String remarks; // 備考

	@Maxlength(maxlength = 120)
	public String eadRemarks; // ピッキング備考

	@Maxlength(maxlength = 1000)
	public String commentData; // コメント

	public String lastRoDate; // 最終受注日

	public String supplierName; // 仕入先名

	public String discountId; // 数量割（暫定）

	public String discountName;

	public String discountUpdDatetm;

	//
	public String priceFractCategory;

	// 単価小数桁
	public String unitPriceDecAlignment;

	// 外貨単価小数桁
	public String dolUnitPriceDecAlignment;

	// 統計小数桁
	public String statsDecAlignment;

	public String supplierRate; // 仕入先レート

	// 仕入先の外貨記号
	public String sign;

	// 商品端数処理
	public String productFractCategory;
	// 数量小数桁
	public String numDecAlignment;

	// 在庫管理
	public List<LabelValueBean> stockCtlCategoryList = new ArrayList<LabelValueBean>();
	// 標準
	public List<LabelValueBean> standardCategoryList = new ArrayList<LabelValueBean>();
	// 状況
	public List<LabelValueBean> statusCategoryList = new ArrayList<LabelValueBean>();
	// 保管
	public List<LabelValueBean> stockCategoryList = new ArrayList<LabelValueBean>();
	// 調達
	public List<LabelValueBean> purvayCategoryList = new ArrayList<LabelValueBean>();
	// セット分類
	public List<LabelValueBean> setTypeCategoryList = new ArrayList<LabelValueBean>();
	// 分類（大）
	public List<LabelValueBean> product1List = new ArrayList<LabelValueBean>();
	// 分類（中）
	public List<LabelValueBean> product2List = new ArrayList<LabelValueBean>();
	// 分類（小）
	public List<LabelValueBean> product3List = new ArrayList<LabelValueBean>();
	// 単位
	public List<LabelValueBean> unitList = new ArrayList<LabelValueBean>();
	// 重さ
	public List<LabelValueBean> weightUnitList = new ArrayList<LabelValueBean>();
	// 長さ
	public List<LabelValueBean> lengthUnitList = new ArrayList<LabelValueBean>();

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages err = new ActionMessages();

		// 単品の場合の必須チェック
		if (CategoryTrns.PRODUCT_SET_TYPE_SINGLE.equals(this.setTypeCategory)) {
			// 仕入先
			if (!StringUtil.hasLength(this.supplierCode)) {
				err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"warns.product.single", MessageResourcesUtil.getMessage("labels.supplierCode")));
			}
			// 棚番
			if (!StringUtil.hasLength(this.rackCode)) {
				err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"warns.product.single", MessageResourcesUtil.getMessage("labels.rackCode")));
			}
		}

		// 在庫管理する商品の場合
		if (CategoryTrns.PRODUCT_STOCK_CTL_YES.equals(this.stockCtlCategory)) {
			if (CategoryTrns.PRODUCT_SET_TYPE_SET.equals(this.setTypeCategory)) {
				// セット品は許可しない
				err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"warns.productset.stock.ctl"));
			}
			else {
				// リードタイム
				if (!StringUtil.hasLength(this.leadTime)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.leadTime")));
				}
				// 発注点
				if (!StringUtil.hasLength(this.poNum)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.poNum")));
				}
				// 安全在庫数
				if (!StringUtil.hasLength(this.mineSafetyStock)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.mineSafetyStock")));
				}
				// 発注ロット
				if (!StringUtil.hasLength(this.poLot)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.poLot")));
				}
				// 在庫限度数
				if (!StringUtil.hasLength(this.maxStockNum)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.maxStockNum")));
				}
				// 発注限度数
				if (!StringUtil.hasLength(this.maxPoNum)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.maxPoNum")));
				}
			}
		}

		return err;
	}

	/**
	 * フォームを初期化します.
	 */
	public void reset() {
		productCode = null;
		productName = null;
		productKana = null;
		onlinePcode = null;
		supplierPcode = null;
		supplierCode = null;
		warehouseName = null;
		rackCode = null;
		supplierPriceYen = null;
		supplierPriceDol = null;
		retailPrice = null;
		soRate = null;
		unitCategory = null;
		packQuantity = null;
		janPcode = null;
		width = null;
		widthUnitSizeCategory = null;
		depth = null;
		depthUnitSizeCategory = null;
		height = null;
		heightUnitSizeCategory = null;
		weight = null;
		weightUnitSizeCategory = null;
		length = null;
		lengthUnitSizeCategory = null;
		poLot = null;
		lotUpdFlag = null;
		leadTime = null;
		poNum = null;
		poUpdFlag = null;
		mineSafetyStock = null;
		mineSafetyStockUpdFlag = null;
		entrustSafetyStock = null;
		salesStandardDeviation = null;
		avgShipCount = null;
		maxStockNum = null;
		stockUpdFlag = null;
		termShipNum = null;
		maxPoNum = null;
		maxPoUpdFlag = null;
		fractCategory = null;
		taxCategory = null;
		stockCtlCategory = null;
		stockAssesCategory = null;
		productCategory = null;
		product1 = null;
		product2 = null;
		product3 = null;
		roMaxNum = null;
		productRank = null;
		setTypeCategory = null;
		productStatusCategory = null;
		productStockCategory = null;
		productPurvayCategory = null;
		productStandardCategory = null;
		coreNum = null;
		num1 = null;
		num2 = null;
		num3 = null;
		num4 = null;
		num5 = null;
		dec1 = null;
		dec2 = null;
		dec3 = null;
		dec4 = null;
		dec5 = null;
		discardDate = null;
		remarks = null;
		eadRemarks = null;
		commentData = null;
		lastRoDate = null;
		creDatetm = null;
		creDatetmShow = null;
		updDatetm = null;
		updDatetmShow = null;
		supplierName = null;
		discountId = null;
		discountName = null;
		discountUpdDatetm = null;

		priceFractCategory = null;
		unitPriceDecAlignment = null;
		dolUnitPriceDecAlignment = null;
		statsDecAlignment = null;
		supplierRate = null;
		sign = null;
		productFractCategory = null;
		numDecAlignment = null;

		isUpdate = false;
		editMode = false;

		stockCtlCategoryList.clear();
		standardCategoryList.clear();
		statusCategoryList.clear();
		stockCategoryList.clear();
		purvayCategoryList.clear();
		setTypeCategoryList.clear();
		product1List.clear();
		product2List.clear();
		product3List.clear();
		unitList.clear();
		weightUnitList.clear();
		lengthUnitList.clear();

		// 初期値を設定する
		this.stockCtlCategory = CategoryTrns.PRODUCT_STOCK_CTL_NO; // 在庫管理
		this.productStatusCategory = CategoryTrns.PRODUCT_STATUS_ONSALE; // 在庫状況
		this.productStockCategory = CategoryTrns.PRODUCT_STOCK_INSTOCK; // 保管
		this.productStandardCategory = CategoryTrns.PRODUCT_STANDARD_STD;// 標準
		this.productPurvayCategory = CategoryTrns.PRODUCT_PURVAY_DOMESTIC; // 調達
		this.setTypeCategory = CategoryTrns.PRODUCT_SET_TYPE_SINGLE; // セット品
		this.unitCategory = CategoryTrns.PRODUCT_UNIT_HON; // 単位

		this.lotUpdFlag = Constants.FLAG.OFF;
		this.maxPoUpdFlag = Constants.FLAG.OFF;
		this.poUpdFlag = Constants.FLAG.OFF;
		this.stockUpdFlag = Constants.FLAG.OFF;
		this.mineSafetyStockUpdFlag = Constants.FLAG.OFF;
	}

	@Override
	public void initialize() {
		this.reset();
	}

}
