/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

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
 * 受注伝票のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class RoSlipTrn {

	/**
	 *
	 */
	@Id
	@GeneratedValue
	@Column(name = "RO_SLIP_ID")
	public Integer roSlipId;
	/**
	 *
	 */
	public String status;
	/**
	 *
	 */
	@Column(name = "RO_ANNUAL")
	public Short roAnnual;
	/**
	 *
	 */
	@Column(name = "RO_MONTHLY")
	public Short roMonthly;
	/**
	 *
	 */
	@Column(name = "RO_YM")
	public Integer roYm;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "RO_DATE")
	public Date roDate;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "SHIP_DATE")
	public Date shipDate;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DELIVERY_DATE")
	public Date deliveryDate;
	/**
	 *
	 */
	@Column(name = "RECEPT_NO")
	public String receptNo;
	/**
	 *
	 */
	@Column(name = "CUSTOMER_SLIP_NO")
	public String customerSlipNo;
	/**
	 *
	 */
	@Column(name = "SALES_CM_CATEGORY")
	public String salesCmCategory;
	/**
	 *
	 */
	@Column(name = "CUTOFF_GROUP")
	public String cutoffGroup;
	/**
	 *
	 */
	@Column(name = "USER_ID")
	public String userId;
	/**
	 *
	 */
	@Column(name = "USER_NAME")
	public String userName;
	/**
	 *
	 */
	public String remarks;
	/**
	 *
	 */
	@Column(name = "CUSTOMER_CODE")
	public String customerCode;
	/**
	 *
	 */
	@Column(name = "CUSTOMER_NAME")
	public String customerName;
	/**
	 *
	 */
	@Column(name = "CUSTOMER_REMARKS")
	public String customerRemarks;
	/**
	 *
	 */
	@Column(name = "CUSTOMER_COMMENT_DATA")
	public String customerCommentData;
	/**
	 *
	 */
	@Column(name = "DELIVERY_CODE")
	public String deliveryCode;
	/**
	 *
	 */
	@Column(name = "DELIVERY_NAME")
	public String deliveryName;
	/**
	 *
	 */
	@Column(name = "DELIVERY_KANA")
	public String deliveryKana;
	/**
	 *
	 */
	@Column(name = "DELIVERY_OFFICE_NAME")
	public String deliveryOfficeName;
	/**
	 *
	 */
	@Column(name = "DELIVERY_OFFICE_KANA")
	public String deliveryOfficeKana;
	/**
	 *
	 */
	@Column(name = "DELIVERY_DEPT_NAME")
	public String deliveryDeptName;
	/**
	 *
	 */
	@Column(name = "DELIVERY_ZIP_CODE")
	public String deliveryZipCode;
	/**
	 *
	 */
	@Column(name = "DELIVERY_ADDRESS_1")
	public String deliveryAddress1;
	/**
	 *
	 */
	@Column(name = "DELIVERY_ADDRESS_2")
	public String deliveryAddress2;
	/**
	 *
	 */
	@Column(name = "DELIVERY_PC_NAME")
	public String deliveryPcName;
	/**
	 *
	 */
	@Column(name = "DELIVERY_PC_KANA")
	public String deliveryPcKana;
	/**
	 *
	 */
	@Column(name = "DELIVERY_PC_PRE_CATEGORY")
	public String deliveryPcPreCategory;
	/**
	 *
	 */
	@Column(name = "DELIVERY_PC_PRE")
	public String deliveryPcPre;
	/**
	 *
	 */
	@Column(name = "DELIVERY_TEL")
	public String deliveryTel;
	/**
	 *
	 */
	@Column(name = "DELIVERY_FAX")
	public String deliveryFax;
	/**
	 *
	 */
	@Column(name = "DELIVERY_EMAIL")
	public String deliveryEmail;
	/**
	 *
	 */
	@Column(name = "DELIVERY_URL")
	public String deliveryUrl;
	/**
	 *
	 */
	@Column(name = "ESTIMATE_SHEET_ID")
	public Integer estimateSheetId;
	/**
	 *
	 */
	@Column(name = "TAX_SHIFT_CATEGORY")
	public String taxShiftCategory;
	/**
	 *
	 */
	@Column(name = "CTAX_PRICE_TOTAL")
	public BigDecimal ctaxPriceTotal;
	/**
	 *
	 */
	@Column(name = "CTAX_RATE")
	public BigDecimal ctaxRate;
	/**
	 *
	 */
	@Column(name = "COST_TOTAL")
	public BigDecimal costTotal;
	/**
	 *
	 */
	@Column(name = "RETAIL_PRICE_TOTAL")
	public BigDecimal retailPriceTotal;
	/**
	 *
	 */
	@Column(name = "PRICE_TOTAL")
	public BigDecimal priceTotal;
	/**
	 *
	 */
	@Column(name = "PRINT_COUNT")
	public Integer printCount;
	/**
	 *
	 */
	@Column(name = "COD_SC")
	public String codSc;
	/**
	 *
	 */
	@Column(name = "CRE_FUNC")
	public String creFunc;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "CRE_DATETM")
	public Timestamp creDatetm;
	/**
	 *
	 */
	@Column(name = "CRE_USER")
	public String creUser;
	/**
	 *
	 */
	@Column(name = "UPD_FUNC")
	public String updFunc;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "UPD_DATETM")
	public Timestamp updDatetm;
	/**
	 *
	 */
	@Column(name = "UPD_USER")
	public String updUser;
	/**
	 *
	 */
	@Column(name = "PAYBACK_CYCLE_CATEGORY")
	public String paybackCycleCategory;
	/**
	 *
	 */
	@Column(name = "TAX_FRACT_CATEGORY")
	public String taxFractCategory;
	/**
	 *
	 */
	@Column(name = "PRICE_FRACT_CATEGORY")
	public String priceFractCategory;
	/**
	 *
	 */
	@Column(name = "DC_CATEGORY")
	public String dcCategory;
	/**
	 *
	 */
	@Column(name = "DC_NAME")
	public String dcName;
	/**
	 *
	 */
	@Column(name = "DC_TIMEZONE_CATEGORY")
	public String dcTimezoneCategory;
	/**
	 *
	 */
	@Column(name = "DC_TIMEZONE")
	public String dcTimezone;

}
