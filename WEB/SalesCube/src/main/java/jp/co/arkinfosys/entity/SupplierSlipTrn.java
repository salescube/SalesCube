/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 仕入伝票のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class SupplierSlipTrn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	public Integer supplierSlipId;

	public String status;

	public Date supplierDate;

	public Short supplierAnnual;

	public Short supplierMonthly;

	public Integer supplierYm;

	public String userId;

	public String userName;

	public String supplierSlipCategory;

	public String supplierCode;

	public String supplierName;

	public String supplierCmCategory;

	public Date deliveryDate;

	public Integer rateId;

	public String sign;

	/** 税転嫁 */
	public String taxShiftCategory;

	/** 税端数処理 */
	public String taxFractCategory;

	/** 単価端数処理*/
	public String priceFractCategory;

	public BigDecimal ctaxTotal;
	
	public BigDecimal ctaxRate;

	public BigDecimal priceTotal;

	public BigDecimal fePriceTotal;

	public BigDecimal costTotal;

	public Integer poSlipId;

	public Integer paymentSlipId;

	public Date supplierPaymentDate;

	public Date paymentCutoffDate;

	public Date paymentPdate;

	public String remarks;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
