/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import jp.co.arkinfosys.entity.Product;

/**
 * 商品マスタと仕入先マスタと商品分類マスタと棚番マスタと割引関連データと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ProductJoin extends Product {

	private static final long serialVersionUID = 1L;

	/**
	 * 廃番か否か
	 */
	public String discarded;

	/**
	 * 仕入先名
	 */
	public String supplierName;

	/**
	 * 棚番名
	 */
	public String rackName;

	/**
	 * 倉庫名
	 */
	public String warehouseName;

	/**
	 * 商品分類（大）名
	 */
	public String className;

	/**
	 * セット分類名
	 */
	public String setTypeCategoryName;

	/**
	 * 表示名取得用
	 */
	// 在庫管理表示名
	public String stockCtlCategoryName;
	// 状況表示名
	public String productStatusCategoryName;
	// 保管表示名
	public String productStockCategoryName;
	// 調達表示名
	public String productPurvayCategoryName;
	// 標準化表示名
	public String productStandardCategoryName;
	// カテゴリ大表示名
	public String product1Name;
	// カテゴリ中表示名
	public String product2Name;
	// カテゴリ小表示名
	public String product3Name;
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
	// 数量割引ID
	@Column(length=20)
	public String discountId;
	// 数量割引名
	public String discountName;
	// 数量割引リレーション更新日時
	public Timestamp discountUpdDatetm;
	// 外貨記号
	public String sign;
}
