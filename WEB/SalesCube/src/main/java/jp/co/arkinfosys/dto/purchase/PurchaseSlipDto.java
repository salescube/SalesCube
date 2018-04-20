/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.purchase;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;

/**
 * 仕入伝票情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class PurchaseSlipDto extends AbstractSlipDto<PurchaseLineDto> {

    private static final long serialVersionUID = 1L;


	public String supplierSlipId;
	public String status;
	public String supplierDate;
	public String supplierAnnual;
	public String supplierMonthly;
	public String supplierYm;
	public String userId;
	public String userName;
	public String supplierSlipCategory;
	public String supplierCode;
	public String supplierName;
	public String supplierCmCategory;
	public String deliveryDate;
	public String rateId;
	public String taxShiftCategory;/* 税転嫁 */
	public String taxFractCategory;/* 税端数処理 */
	public String priceFractCategory;/* 単価端数処理 */
	public String ctaxTotal;
	public String ctaxRate;
	public String priceTotal;
	public String fePriceTotal;
	//public String costTotal; 仕入画面ではcostTotalは使用されない
	public String poSlipId;
	public String paymentSlipId;
	public String supplierPaymentDate;
	public String paymentCutoffDate;
	public String paymentPdate;
	public String remarks;

    public String creFunc;
    public String creDatetm;
    public String creUser;
    public String updFunc;
    public String updDatetm;
    public String updUser;

    // 作業用
    public String rate;
    public String rateName;
    public String sign;

	// 新規作成状態の管理フラグ
	public Boolean newData;

	/**
	 * 削除明細IDリスト
	 */
	public String deleteLineIds;

	/**
	 * 仕入伝票明細行を作成します.
	 * @return 仕入伝票明細行オブジェクト
	 */
	@Override
	public AbstractLineDto createLineDto() {
		return new PurchaseLineDto();
	}

	/**
	 * 仕入伝票番号を取得します.
	 * @return 仕入伝票番号
	 */
	@Override
	public String getKeyValue() {
		return this.supplierSlipId;
	}
}