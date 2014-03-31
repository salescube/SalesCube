/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 発注伝票のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PoSlipTrn implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "PO_SLIP_TRN";

	@Id
	@GeneratedValue
	@Column(name = "PO_SLIP_ID")
	public Integer poSlipId;
	public String status;
	@Temporal(TemporalType.DATE)
	@Column(name = "PO_DATE")
	public Date poDate;
	@Column(name = "PO_ANNUAL")
	public Short poAnnual;
	@Column(name = "PO_MONTHLY")
	public Short poMonthly;
	@Column(name = "PO_YM")
	public Integer poYm;
	@Temporal(TemporalType.DATE)
	@Column(name = "DELIVERY_DATE")
	public Date deliveryDate;
	@Column(name = "USER_ID")
	public String userId;
	@Column(name = "USER_NAME")
	public String userName;
	public String remarks;
	@Column(name = "SUPPLIER_CODE")
	public String supplierCode;
	@Column(name = "SUPPLIER_NAME")
	public String supplierName;
	@Column(name = "SUPPLIER_KANA")
	public String supplierKana;
	@Column(name = "SUPPLIER_ZIP_CODE")
	public String supplierZipCode;
	@Column(name = "SUPPLIER_ADDRESS_1")
	public String supplierAddress1;
	@Column(name = "SUPPLIER_ADDRESS_2")
	public String supplierAddress2;
	@Column(name = "SUPPLIER_PC_NAME")
	public String supplierPcName;
	@Column(name = "SUPPLIER_PC_KANA")
	public String supplierPcKana;
	@Column(name = "SUPPLIER_PC_PRE_CATEGORY")
	public String supplierPcPreCategory;
	@Column(name = "SUPPLIER_PC_POST")
	public String supplierPcPost;
	@Column(name = "SUPPLIER_TEL")
	public String supplierTel;
	@Column(name = "SUPPLIER_FAX")
	public String supplierFax;
	@Column(name = "SUPPLIER_EMAIL")
	public String supplierEmail;
	@Column(name = "SUPPLIER_URL")
	public String supplierUrl;
	@Column(name = "TRANSPORT_CATEGORY")
	public String transportCategory;
	@Column(name = "TAX_SHIFT_CATEGORY")
	public String taxShiftCategory;
	@Column(name = "TAX_FRACT_CATEGORY")
	public String taxFractCategory;
	@Column(name = "PRICE_FRACT_CATEGORY")
	public String priceFractCategory;
	@Column(name = "RATE_ID")
	public Integer rateId;
	@Column(name = "SUPPLIER_CM_CATEGORY")
	public String supplierCmCategory;
	@Column(name = "PRICE_TOTAL")
	public BigDecimal priceTotal;
	@Column(name = "CTAX_TOTAL")
	public BigDecimal ctaxTotal;
	@Column(name = "CTAX_RATE")
	public BigDecimal ctaxRate;
	@Column(name = "FE_PRICE_TOTAL")
	public BigDecimal fePriceTotal;
	@Column(name = "PRINT_COUNT")
	public Integer printCount;
	@Column(name = "CRE_FUNC")
	public String creFunc;
	@Temporal(TemporalType.DATE)
	@Column(name = "CRE_DATETM")
	public Timestamp creDatetm;
	@Column(name = "CRE_USER")
	public String creUser;
	@Column(name = "UPD_FUNC")
	public String updFunc;
	@Temporal(TemporalType.DATE)
	@Column(name = "UPD_DATETM")
	public Timestamp updDatetm;
	@Column(name = "UPD_USER")
	public String updUser;

	public String supplierAbbr;
	public String supplierDeptName;
	public String supplierPcPre;
}
