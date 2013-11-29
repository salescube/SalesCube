/*
 * Copyright 2009-2010 Ark Information Systems.
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

	public String productCode;		// 商品コード

	public String productName;		// 商品名

	public String productKana;		// 商品名カナ

	public String onlinePcode;		// オンライン品番

	public String janPcode;			// JANコード

	public String discardDate;		// 廃棄予定日

	public String supplierCode;		// 仕入先コード

	/**
	 * 仕入先名
	 */
	public String supplierName;

	public String supplierPcode;		// 仕入先品番

	public String supplierPriceYen;		// 仕入単価（円）

	public String supplierPriceDol;		// 仕入単価（＄）

	public String retailPrice;			// 上代

	//在庫管理表示名
	public String stockCtlCategoryName;

	public String packQuantity;			// 入数

	public String avgShipCount;			// 平均出荷数

	public String rackCode;				// 標準棚番コード

	public String leadTime;				// リードタイム

	public String poNum;				// 発注点

	public Integer poUpdFlag;			// 発注点自動更新フラグ

	public String poLot;				// 発注ロット

	public Integer lotUpdFlag;			// 発注ロット自動更新フラグ

	public String maxStockNum;			// 在庫限度数

	public Integer stockUpdFlag;		// 在庫限度数自動更新フラグ

	public String maxPoNum;				// 発注限度数

	public Integer maxPoUpdFlag;		// 発注限度数自動更新フラグ

	public String roMaxNum;				// 受注限度数

	//数量割引名
	public String discountName;

	//状況表示名
	public String productStatusCategoryName;
	//保管表示名
	public String productStockCategoryName;
	//調達表示名
	public String productPurvayCategoryName;
	//標準化表示名
	public String productStandardCategoryName;
	//セット表示名
	public String setTypeCategoryName;

	public String soRate;				// 特注品掛率

	//カテゴリ大表示名
	public String product1Name;
	//カテゴリ中表示名
	public String product2Name;
	//カテゴリ小表示名
	public String product3Name;
	//単位表示名
	public String unitCategoryName;

	public String weight;				// 重さ

	//重量単位表示名
	public String weightUnitSizeCategoryName;

	public String length;				// 長さ

	//長さ単位表示名
	public String lengthUnitSizeCategoryName;

	public String width;				// 幅

	//サイズ幅単位表示名
	public String widthUnitSizeCategoryName;

	public String depth;				// 奥行き

	//サイズ奥行単位表示名
	public String depthUnitSizeCategoryName;

	public String height;				// 高さ

	//サイズ高さ単位表示名
	public String heightUnitSizeCategoryName;

	public String coreNum;				// 芯数

	public String remarks;				// 備考

	public String eadRemarks;			// ピッキング備考

	public String commentData;			// コメント
}
