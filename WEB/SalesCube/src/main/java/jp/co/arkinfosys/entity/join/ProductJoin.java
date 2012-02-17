/*
 *  Copyright 2009-2010 Ark Information Systems.
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
	
	public String stockCtlCategoryName;
	
	public String productStatusCategoryName;
	
	public String productStockCategoryName;
	
	public String productPurvayCategoryName;
	
	public String productStandardCategoryName;
	
	public String product1Name;
	
	public String product2Name;
	
	public String product3Name;
	
	public String unitCategoryName;
	
	public String weightUnitSizeCategoryName;
	
	public String lengthUnitSizeCategoryName;
	
	public String widthUnitSizeCategoryName;
	
	public String depthUnitSizeCategoryName;
	
	public String heightUnitSizeCategoryName;
	
	@Column(length=20)
	public String discountId;
	
	public String discountName;
	
	public Timestamp discountUpdDatetm;
	
	public String sign;
}
