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
import javax.persistence.Transient;

/**
 * 入金伝票の履歴テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class DepositSlipHist {
	@Transient
	static final public String TABLE_NAME = "DEPOSIT_SLIP_TRN_HIST";	// 入金伝票履歴

	/**
	 *
	 */
	@Id
	@GeneratedValue
	@Column(name = "HIST_ID")
	public Integer histId;
	/**
	 *
	 */
	@Column(name = "ACTION_TYPE")
	public String actionType;
	/**
	 *
	 */
	@Column(name = "ACTION_FUNC")
	public String actionFunc;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "REC_DATETM")
	public Timestamp recDatetm;
	/**
	 *
	 */
	@Column(name = "REC_USER")
	public String recUser;
	/**
	 *
	 */
	@Column(name = "DEPOSIT_SLIP_ID")
	public Integer depositSlipId;
	/**
	 *
	 */
	public String status;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DEPOSIT_DATE")
	public Date depositDate;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "INPUT_PDATE")
	public Date inputPdate;
	/**
	 *
	 */
	@Column(name = "DEPOSIT_ANNUAL")
	public Short depositAnnual;
	/**
	 *
	 */
	@Column(name = "DEPOSIT_MONTHLY")
	public Short depositMonthly;
	/**
	 *
	 */
	@Column(name = "DEPOSIT_YM")
	public Integer depositYm;
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
	@Column(name = "DEPOSIT_ABSTRACT")
	public String depositAbstract;
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
	@Column(name = "BA_CODE")
	public String baCode;
	/**
	 *
	 */
	@Column(name = "BA_NAME")
	public String baName;
	/**
	 *
	 */
	@Column(name = "BA_KANA")
	public String baKana;
	/**
	 *
	 */
	@Column(name = "BA_OFFICE_NAME")
	public String baOfficeName;
	/**
	 *
	 */
	@Column(name = "BA_OFFICE_KANA")
	public String baOfficeKana;
	/**
	 *
	 */
	@Column(name = "BA_DEPT_NAME")
	public String baDeptName;
	/**
	 *
	 */
	@Column(name = "BA_ZIP_CODE")
	public String baZipCode;
	/**
	 *
	 */
	@Column(name = "BA_ADDRESS_1")
	public String baAddress1;
	/**
	 *
	 */
	@Column(name = "BA_ADDRESS_2")
	public String baAddress2;
	/**
	 *
	 */
	@Column(name = "BA_PC_NAME")
	public String baPcName;
	/**
	 *
	 */
	@Column(name = "BA_PC_KANA")
	public String baPcKana;
	/**
	 *
	 */
	@Column(name = "BA_PC_PRE_CATRGORY")
	public String baPcPreCatrgory;
	/**
	 *
	 */
	@Column(name = "BA_PC_PRE")
	public String baPcPre;
	/**
	 *
	 */
	@Column(name = "BA_TEL")
	public String baTel;
	/**
	 *
	 */
	@Column(name = "BA_FAX")
	public String baFax;
	/**
	 *
	 */
	@Column(name = "BA_EMAIL")
	public String baEmail;
	/**
	 *
	 */
	@Column(name = "BA_URL")
	public String baUrl;
	/**
	 *
	 */
	@Column(name = "DEPOSIT_CATEGORY")
	public String depositCategory;
	/**
	 *
	 */
	@Column(name = "DEPOSIT_TOTAL")
	public BigDecimal depositTotal;
	/**
	 *
	 */
	@Column(name = "BILL_ID")
	public Integer billId;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "BILL_CUTOFF_DATE")
	public Date billCutoffDate;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "BILL_CUTOFF_PDATE")
	public Timestamp billCutoffPdate;
	/**
	 *
	 */
	@Column(name = "ART_ID")
	public Integer artId;
	/**
	 *
	 */
	@Column(name = "SALES_SLIP_ID")
	public Integer salesSlipId;
	/**
	 *
	 */
	@Column(name = "DEPOSIT_METHOD_TYPE_CATEGORY")
	public String depositMethodTypeCategory;

	@Column(name = "TAX_FRACT_CATEGORY")
	public String taxFractCategory;

	@Column(name = "PRICE_FRACT_CATEGORY")
	public String priceFractCategory;

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

}
