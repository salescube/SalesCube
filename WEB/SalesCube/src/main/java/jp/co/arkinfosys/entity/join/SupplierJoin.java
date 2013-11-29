/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import jp.co.arkinfosys.entity.Supplier;
/**
 * 仕入先マスタと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SupplierJoin extends Supplier {

	private static final long serialVersionUID = 1L;

	/**
	 * 仕入取引区分名
	 */
	public String supplierCmCategoryName;

	/**
	 * レートタイプID
	 */
	public String supplierRateTypeId;

	/**
	 * レート名
	 */
	public String supplierRateName;

	/**
	 * レート適用開始日(特定日付以前で最新の)
	 */
	public String supplierRateStartDate;

	/**
	 * 仕入先レート（適用開始日時点での）
	 */
	public String supplierRate;

	/**
	 * 単価端数処理小数桁
	 */
	public String unitPriceDecAlignment;
	/**
	 * 外貨単価端数処理小数桁
	 */
	public String dolUnitPriceDecAlignment;
	/**
	 * 税端数処理小数桁
	 */
	public String taxPriceDecAlignment;

	/**
	 * 仕入先レートに対応する通貨単位文字(列)
	 */
	public String cUnitSign;
}
