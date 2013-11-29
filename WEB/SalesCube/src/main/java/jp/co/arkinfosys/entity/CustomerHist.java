/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 顧客マスタ履歴テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class CustomerHist implements Serializable {

	private static final long serialVersionUID = 1L;
	@Transient

	public static final String TABLE_NAME = "CUSTOMER_MST_HIST";

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
	public String customerCode;

	public String customerName;

	public String customerKana;

	public String customerOfficeName;

	public String customerOfficeKana;

	public String customerAbbr;

	public String customerZipCode;

	public String customerAddress1;

	public String customerAddress2;

	public String customerPcName;

	public String customerPcKana;

	public String customerPcPreCategoryCdx;
	public String customerPcPreCategoryNm;

	public String customerDeptName;

	public String customerPcPost;

	public String customerTel;

	public String customerFax;

	public String customerEmail;

	public String customerRankCategoryCdx;
	public String rankName;

	public String customerUpdFlag;

	public String customerRoCategoryCdx;
	public String customerRoCategoryNm;

	public BigDecimal maxCreditLimit;

	public String customerBusinessCategoryCdx;
	public String customerBusinessCategoryNm;

	public String customerJobCategoryCdx;
	public String customerJobCategoryNm;

	public String taxFractCategoryCdx;
	public String taxFractCategoryNm;

	public String priceFractCategoryCdx;
	public String priceFractCategoryNm;

	public String taxShiftCategoryCdx;
	public String taxShiftCategoryNm;

	public Timestamp lastCutoffDate;

	public String salesCmCategoryCdx;
	public String salesCmCategoryNm;

	public String cutoffGroup;
	public String categoryCodeName;

	public String paybackTypeCategoryCdx;
	public String paybackTypeCategoryNm;

	public String salesSlipCategoryCdx;
	public String salesSlipCategoryNm;

	public String billCategoryCdx;
	public String billCategoryNm;

	public String tempDeliverySlipFlag;

	public String paymentName;

	public String remarks;

	public String commentData;

	public Timestamp updDatetm;

}
