/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.dto.master;

import java.io.Serializable;

/**
 * 商品情報ダイアログの情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ProductInfosWithNameDto implements Serializable  {

	private static final long serialVersionUID = 1L;

	public String productCode;		

	public String productName;		

	public String productKana;		

	public String onlinePcode;		

	public String janPcode;			

	public String discardDate;		

	public String supplierCode;		

	/**
	 * 仕入先名
	 */
	public String supplierName;

	public String supplierPcode;		

	public String supplierPriceYen;		

	public String supplierPriceDol;		

	public String retailPrice;			

	
	public String stockCtlCategoryName;

	public String packQuantity;			

	public String avgShipCount;			

	public String rackCode;				

	public String leadTime;				

	public String poNum;				

	public Integer poUpdFlag;			

	public String poLot;				

	public Integer lotUpdFlag;			

	public String maxStockNum;			

	public Integer stockUpdFlag;		

	public String maxPoNum;				

	public Integer maxPoUpdFlag;		

	public String roMaxNum;				

	
	public String discountName;

	
	public String productStatusCategoryName;
	
	public String productStockCategoryName;
	
	public String productPurvayCategoryName;
	
	public String productStandardCategoryName;
	
	public String setTypeCategoryName;

	public String soRate;				

	
	public String product1Name;
	
	public String product2Name;
	
	public String product3Name;
	
	public String unitCategoryName;

	public String weight;				

	
	public String weightUnitSizeCategoryName;

	public String length;				

	
	public String lengthUnitSizeCategoryName;

	public String width;				

	
	public String widthUnitSizeCategoryName;

	public String depth;				

	
	public String depthUnitSizeCategoryName;

	public String height;				

	
	public String heightUnitSizeCategoryName;

	public String coreNum;				

	public String remarks;				

	public String eadRemarks;			

	public String commentData;			
}
