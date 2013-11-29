/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 顧客マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Customer extends AuditInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "CUSTOMER_MST";

	public String customerCode;

	public String customerName;

	public String customerKana;

	public String customerOfficeName;

	public String customerOfficeKana;

	public String customerAbbr;

	public String customerDeptName;

	public String customerZipCode;

	public String customerAddress1;

	public String customerAddress2;

	public String customerPcPost;

	public String customerPcName;

	public String customerPcKana;

	public String customerPcPreCategory;

	public String customerTel;

	public String customerFax;

	public String customerEmail;

	public String customerUrl;

	public String customerBusinessCategory;

	public String customerJobCategory;

	public String customerRoCategory;

	public String customerRankCategory;

	public String customerUpdFlag;

	public String salesCmCategory;

	public String taxShiftCategory;

	public BigDecimal rate;

	public BigDecimal maxCreditLimit;

	public Date lastCutoffDate;

	public String cutoffGroup;

	public String paybackTypeCategory;

	public String paybackCycleCategory;

	public String taxFractCategory;

	public String priceFractCategory;

	public String billPrintUnit;

	public String billDatePrint;

	public String tempDeliverySlipFlag;

	public String paymentName;

	public String remarks;

	public Date firstSalesDate;

	public Date lastSalesDate;

	public BigDecimal salesPriceTotal;

	public BigDecimal salesPriceLsm;

	public String commentData;

	/**
	 * 宛名（顧客）
	 */
	public String customerPcPreCategoryName;
	/**
	 * 区分コード（顧客敬称）
	 */
	public Integer categoryId4;

	public Date lastSalesCutoffDate;
}
