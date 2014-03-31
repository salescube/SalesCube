/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.payment;

import jp.co.arkinfosys.dto.AbstractLineDto;

/**
 * 支払入力明細行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 */
public class InputPaymentLineDto extends AbstractLineDto {
	private static final long serialVersionUID = 1L;

	/** 支払伝票明細.支払伝票行ID(更新のキー) */
	public String paymentLineId;

	/** 支払伝票明細.支払伝票行番 */
	public String paymentLineNo;

	/** 仕入伝票明細.仕入伝票番号 */
	public String supplierSlipId;

	/** 仕入伝票明細.仕入伝票行番 */
	public String supplierLineNo;

	/** 仕入伝票明細.仕入明細区分 */
	public String supplierDetailCategory;

	/** 仕入伝票.仕入日 */
	public String supplierDate;

	/** 仕入伝票明細.商品コード */
	public String productCode;

	/** 仕入伝票明細.商品名 */
	public String productAbstract;

	/** 支払伝票明細.支払区分コード */
	public String paymentCategory;

	/** 仕入伝票明細.数量 */
	public String quantity;

	/** 仕入伝票.レート */
	public String rate;

	/** 支払伝票明細.単価 */
	public String unitPrice;

	/** 支払伝票明細.金額 */
	public String price;

	/** 支払伝票明細.ドル単価 */
	public String dolUnitPrice;

	/** 支払伝票明細.ドル金額 */
	public String dolPrice;

	/** 支払伝票明細.備考 */
	public String remarks;

	/** 仕入伝票.更新日時 */
	public String supUpdDatetm;

	/** 発注伝票明細行.発注伝票行ID */
	public String poLineId;

	/** 仕入伝票明細行.仕入伝票明細行ID */
	public String supplierLineId;

	/** 仕入伝票明細行.消費税率 */
	public String ctaxRate;

	/** 仕入伝票明細行.消費税額 */
	public String ctaxPrice;				

	
	/** 支払対象明細の選択に使用するチェックボックスの状態 */
	public Boolean checkPayLine = false;

	/** 仕入明細区分名 ※Actionで設定(supplierDetailCategoryをキーに名称を取得) */
	public String supplierDetailCategoryName;

	/**
	 * 商品コードがnull又は空白かどうか検査します.
	 * @return true:商品コーがnullまたは空白　false:商品コードはnullでも空白でもない
	 */
	@Override
	public boolean isBlank() {
		return (this.productCode == null || this.productCode.length() == 0);
	}

}
