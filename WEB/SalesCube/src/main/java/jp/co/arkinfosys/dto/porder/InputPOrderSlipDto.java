/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.porder;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;

/**
 * 発注入力情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputPOrderSlipDto extends AbstractSlipDto<InputPOrderLineDto>{

	public String poSlipId;
	public String status;
	public String poDate;
	public String poAnnual;
	public String poMonthly;
	public String poYm;
	public String deliveryDate;
	public String userId;
	public String userName;
	public String remarks;
	public String supplierCode;
	public String supplierName;
	public String supplierKana;
	public String supplierAbbr;
	public String supplierDeptName;
	public String supplierZipCode;
	public String supplierAddress1;
	public String supplierAddress2;
	public String supplierPcName;
	public String supplierPcKana;
	public String supplierPcPreCategory;
	public String supplierPcPre;
	public String supplierPcPost;
	public String supplierTel;
	public String supplierFax;
	public String supplierEmail;
	public String supplierUrl;
	public String transportCategory;
	public String taxShiftCategory;
	public String taxFractCategory;
	public String priceFractCategory;
	public String rateId;
	public String supplierCmCategory;
	public String priceTotal;
	public String ctaxTotal;
	public String ctaxRate;
	public String fePriceTotal;
	public String printCount;
	public String creFunc;
	public String creDatetm;
	public String creUser;
	public String updFunc;
	public String updDatetm;
	public String updUser;

	public String supplierRate;
	public String taxRate;
	public String defaultCUnit;

	public String slipPaymentStatus;
	public String slipPaymentDate;

	/**
	 * 発注伝票明細行を作成します.
	 * @return 発注伝票明細行オブジェクト
	 */
	@Override
	public AbstractLineDto createLineDto() {
		return new InputPOrderLineDto();
	}

	/**
	 * 発注伝票番号を取得します.
	 * @return 発注伝票番号
	 */
	@Override
	public String getKeyValue() {
		return this.poSlipId;
	}



}