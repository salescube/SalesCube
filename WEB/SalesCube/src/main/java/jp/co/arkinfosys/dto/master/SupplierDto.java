/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.io.Serializable;

/**
 * 仕入先マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SupplierDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	public String supplierCode;

	public String supplierName;

	public String supplierKana;

	public String supplierAbbr;

	public String supplierZipCode;

	public String supplierAddress1;

	public String supplierAddress2;

	public String supplierDeptName;

	public String supplierPcName;

	public String supplierPcKana;

	public String supplierPcPreCategory;

	public String supplierPcPost;

	public String supplierTel;

	public String supplierFax;

	public String supplierEmail;

	public String supplierUrl;

	public String supplierCmCategory;

	public String supplierCmCategoryName;

	public String taxShiftCategory;

	public String paymentTypeCategory;

	public String paymentCycleCategory;

	public String lastCutoffDate;

	public Short paymentDate;

	public String taxFractCategory;

	public String priceFractCategory;

	public String poSlipComeoutCategory;

	public String serviceChargeCategory;

	public String transferTypeCategory;

	public String nationalCategory;

	public Integer rateId;

	public String fobName;

	public String remarks;

	public String commentData;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	/**
	 * 仕入先コードを取得します.
	 * @return　 仕入先コード
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.supplierCode };
	}

}
