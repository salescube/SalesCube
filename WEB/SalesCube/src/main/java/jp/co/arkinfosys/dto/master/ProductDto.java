/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

/**
 * 商品マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ProductDto implements MasterEditDto {

	private static final long serialVersionUID = 1L;

	public String productCode;

	public String productName;

	public String productKana;

	public String onlinePcode;

	public String supplierPcode;

	public String supplierCode;

	public String rackCode;

	public String warehouseName;

	public String supplierPriceYen;

	public String supplierPriceDol;

	public String retailPrice;

	public String soRate;

	public String unitCategory;

	public String packQuantity;

	public String janPcode;

	public String width;

	public String widthUnitSizeCategory;

	public String depth;

	public String depthUnitSizeCategory;

	public String height;

	public String heightUnitSizeCategory;

	public String weight;

	public String weightUnitSizeCategory;

	public String length;

	public String lengthUnitSizeCategory;

	public String poLot;

	public String lotUpdFlag;

	public String leadTime;

	public String poNum;

	public String poUpdFlag;

	public String mineSafetyStock;

	public String mineSafetyStockUpdFlag;

	public String entrustSafetyStock;

	public String salesStandardDeviation;

	public String avgShipCount;

	public String maxStockNum;

	public String stockUpdFlag;

	public String termShipNum;

	public String maxPoNum;

	public String maxPoUpdFlag;

	public String fractCategory;

	public String taxCategory;

	public String stockCtlCategory;

	public String stockAssesCategory;

	public String productCategory;

	public String product1;

	public String product2;

	public String product3;

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

	public String discardDate;

	public String remarks;

	public String eadRemarks;

	public String commentData;

	public String lastRoDate;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	/**
	 * 仕入先名
	 */
	public String supplierName;

	/**
	 * 棚番名
	 */
	public String rackName;
	/**
	 * 絶版商品フラグ
	 */
	public String discarded;

	/**
	 * 分類(大）名
	 */
	public String className;

	/**
	 * 数量割引ID
	 */
	public String discountId;

	/**
	 * 数量割引リレーション更新日時
	 */
	public String discountUpdDatetm;

	// 単位表示名
	public String unitCategoryName;
	// 重量単位表示名
	public String weightUnitSizeCategoryName;
	// 長さ単位表示名
	public String lengthUnitSizeCategoryName;
	// サイズ幅単位表示名
	public String widthUnitSizeCategoryName;
	// サイズ奥行単位表示名
	public String depthUnitSizeCategoryName;
	// サイズ高さ単位表示名
	public String heightUnitSizeCategoryName;

	/**
	 * 数量割引IDが変更されているか否か
	 */
	public boolean discountIdChanged;

	/**
	 * 商品コードを取得します.
	 * @return　 商品コード
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.productCode };
	}

}
