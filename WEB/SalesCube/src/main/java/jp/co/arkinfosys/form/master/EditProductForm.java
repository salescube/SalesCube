/*
 *  Copyright 2009-2010 Ark Information Systems.
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
	public String productCode; 

	@Required
	@Maxlength(maxlength = 60)
	public String productName; 

	@Maxlength(maxlength = 60)
	public String productKana; 

	@Maxlength(maxlength = 50)
	public String onlinePcode; 

	@Maxlength(maxlength = 20)
	public String supplierPcode; 

	@Maxlength(maxlength = 10)
	public String supplierCode; 

	@Maxlength(maxlength = 10)
	public String warehouseName; 
	
	@Maxlength(maxlength = 10)
	public String rackCode; 

	@DoubleRange(min="-999999999", max="999999999")
	public String supplierPriceYen; 

	@DoubleRange(min="-999999999", max="999999999")
	public String supplierPriceDol; 

	@DoubleRange(min="-999999999", max="999999999", arg0 = @Arg(key = "labels.retailPrice2", resource = true))
	public String retailPrice; 

	@DoubleType
	@DoubleRange(min="0", max="999.999")
	public String soRate; 

	public String unitCategory; 

	@IntegerType
	@IntRange(min=0, max=Short.MAX_VALUE)
	public String packQuantity; 

	public String janPcode; 

	@FloatType
	public String width; 

	public String widthUnitSizeCategory; 

	@FloatType
	public String depth; 

	public String depthUnitSizeCategory; 

	@FloatType
	public String height; 

	public String heightUnitSizeCategory; 

	@FloatType
	public String weight; 

	public String weightUnitSizeCategory; 

	@FloatType
	public String length; 

	public String lengthUnitSizeCategory; 

	@DoubleType
	public String poLot; 

	public String lotUpdFlag; 

	@IntegerType
	public String leadTime; 

	@IntegerType
	public String poNum; 

	public String poUpdFlag; 

	@IntegerType
	public String mineSafetyStock; 

	public String mineSafetyStockUpdFlag; 

	@IntegerType
	public String entrustSafetyStock; 

	public String salesStandardDeviation; 

	@IntegerType
	public String avgShipCount; 

	@IntegerType
	public String maxStockNum; 

	public String stockUpdFlag; 

	@IntegerType
	public String termShipNum; 

	@IntegerType
	public String maxPoNum; 

	public String maxPoUpdFlag; 

	public String fractCategory; 

	public String taxCategory; 

	@Required
	public String stockCtlCategory; 

	public String stockAssesCategory; 

	public String productCategory; 

	public String product1; 

	public String product2; 

	public String product3; 

	@IntegerType
	@IntRange(min=0, max=Short.MAX_VALUE)
	public String roMaxNum; 

	public String productRank; 

	public String setTypeCategory; 

	public String productStatusCategory; 

	public String productStockCategory; 

	public String productPurvayCategory; 

	public String productStandardCategory; 

	public String coreNum; 

	public String num1; 

	public String num2; 

	public String num3; 

	public String num4; 

	public String num5; 

	public String dec1; 

	public String dec2; 

	public String dec3; 

	public String dec4; 

	public String dec5; 

	@DateType(datePattern = Constants.FORMAT.DATE)
	public String discardDate; 

	@Maxlength(maxlength = 120)
	public String remarks; 

	@Maxlength(maxlength = 120)
	public String eadRemarks; 

	@Maxlength(maxlength = 1000)
	public String commentData; 

	public String lastRoDate; 

	public String supplierName; 

	public String discountId; 

	public String discountName;

	public String discountUpdDatetm;

	
	public String priceFractCategory;

	
	public String unitPriceDecAlignment;

	
	public String dolUnitPriceDecAlignment;

	
	public String statsDecAlignment;

	public String supplierRate; 

	
	public String sign;

	
	public String productFractCategory;
	
	public String numDecAlignment;

	
	public List<LabelValueBean> stockCtlCategoryList = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> standardCategoryList = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> statusCategoryList = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> stockCategoryList = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> purvayCategoryList = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> setTypeCategoryList = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> product1List = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> product2List = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> product3List = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> unitList = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> weightUnitList = new ArrayList<LabelValueBean>();
	
	public List<LabelValueBean> lengthUnitList = new ArrayList<LabelValueBean>();

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages err = new ActionMessages();

		
		if (CategoryTrns.PRODUCT_SET_TYPE_SINGLE.equals(this.setTypeCategory)) {
			
			if (!StringUtil.hasLength(this.supplierCode)) {
				err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"warns.product.single", MessageResourcesUtil.getMessage("labels.supplierCode")));
			}
			
			if (!StringUtil.hasLength(this.rackCode)) {
				err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"warns.product.single", MessageResourcesUtil.getMessage("labels.rackCode")));
			}
		}

		
		if (CategoryTrns.PRODUCT_STOCK_CTL_YES.equals(this.stockCtlCategory)) {
			if (CategoryTrns.PRODUCT_SET_TYPE_SET.equals(this.setTypeCategory)) {
				
				err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"warns.productset.stock.ctl"));
			}
			else {
				
				if (!StringUtil.hasLength(this.leadTime)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.leadTime")));
				}
				
				if (!StringUtil.hasLength(this.poNum)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.poNum")));
				}
				
				if (!StringUtil.hasLength(this.mineSafetyStock)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.mineSafetyStock")));
				}
				
				if (!StringUtil.hasLength(this.poLot)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.poLot")));
				}
				
				if (!StringUtil.hasLength(this.maxStockNum)) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"warns.product.stock.ctl", MessageResourcesUtil
									.getMessage("labels.maxStockNum")));
				}
				
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

		
		this.stockCtlCategory = CategoryTrns.PRODUCT_STOCK_CTL_NO; 
		this.productStatusCategory = CategoryTrns.PRODUCT_STATUS_ONSALE; 
		this.productStockCategory = CategoryTrns.PRODUCT_STOCK_INSTOCK; 
		this.productStandardCategory = CategoryTrns.PRODUCT_STANDARD_STD;
		this.productPurvayCategory = CategoryTrns.PRODUCT_PURVAY_DOMESTIC; 
		this.setTypeCategory = CategoryTrns.PRODUCT_SET_TYPE_SINGLE; 
		this.unitCategory = CategoryTrns.PRODUCT_UNIT_HON; 

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
