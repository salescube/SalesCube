/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 見積伝票のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class EstimateSheetTrn {

	@Id
	@Column(name = "ESTIMATE_SHEET_ID")
	public String estimateSheetId;
	@Column(name = "ESTIMATE_ANNUAL")
	public Short estimateAnnual;
	@Column(name = "ESTIMATE_MONTHLY")
	public Short estimateMonthly;
	@Column(name = "ESTIMATE_YM")
	public Integer estimateYm;
	@Temporal(TemporalType.DATE)
	@Column(name = "ESTIMATE_DATE")
	public Date estimateDate;
	@Column(name = "DELIVERY_INFO")
	public String deliveryInfo;
	@Temporal(TemporalType.DATE)
	@Column(name = "VALID_DATE")
	public Date validDate;
	@Column(name = "USER_ID")
	public String userId;
	@Column(name = "USER_NAME")
	public String userName;
	public String remarks;
	public String title;
	@Column(name = "ESTIMATE_CONDITION")
	public String estimateCondition;
	@Column(name = "SUBMIT_NAME")
	public String submitName;
	@Column(name = "SUBMIT_PRE_CATEGORY")
	public String submitPreCategory;
	@Column(name = "SUBMIT_PRE")
	public String submitPre;
	@Column(name = "CUSTOMER_CODE")
	public String customerCode;
	@Column(name = "CUSTOMER_NAME")
	public String customerName;
	@Column(name = "CUSTOMER_REMARKS")
	public String customerRemarks;
	@Column(name = "CUSTOMER_COMMENT_DATA")
	public String customerCommentData;
	@Column(name = "DELIVERY_NAME")
	public String deliveryName;
	@Column(name = "DELIVERY_OFFICE_NAME")
	public String deliveryOfficeName;
	@Column(name = "DELIVERY_DEPT_NAME")
	public String deliveryDeptName;
	@Column(name = "DELIVERY_ZIP_CODE")
	public String deliveryZipCode;
	@Column(name = "DELIVERY_ADDRESS_1")
	public String deliveryAddress1;
	@Column(name = "DELIVERY_ADDRESS_2")
	public String deliveryAddress2;
	@Column(name = "DELIVERY_PC_NAME")
	public String deliveryPcName;
	@Column(name = "DELIVERY_PC_KANA")
	public String deliveryPcKana;
	@Column(name = "DELIVERY_PC_PRE_CATEGORY")
	public String deliveryPcPreCategory;
	@Column(name = "DELIVERY_PC_PRE")
	public String deliveryPcPre;
	@Column(name = "DELIVERY_TEL")
	public String deliveryTel;
	@Column(name = "DELIVERY_FAX")
	public String deliveryFax;
	@Column(name = "DELIVERY_EMAIL")
	public String deliveryEmail;
	@Column(name = "DELIVERY_URL")
	public String deliveryUrl;
	@Column(name = "CTAX_PRICE_TOTAL")
	public BigDecimal ctaxPriceTotal;
	@Column(name = "CTAX_RATE")
	public BigDecimal ctaxRate;
	@Column(name = "COST_TOTAL")
	public BigDecimal costTotal;
	@Column(name = "RETAIL_PRICE_TOTAL")
	public BigDecimal retailPriceTotal;
	@Column(name = "ESTIMATE_TOTAL")
	public BigDecimal estimateTotal;
	public String memo;
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
	@Column(name = "TAX_FRACT_CATEGORY")
	public String taxFractCategory;
	@Column(name = "PRICE_FRACT_CATEGORY")
	public String priceFractCategory;

}
