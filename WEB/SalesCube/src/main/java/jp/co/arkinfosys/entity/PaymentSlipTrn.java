/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 支払伝票エンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PaymentSlipTrn implements Serializable{
	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "PAYMENT_SLIP_TRN";

	
	@Id
	public Integer paymentSlipId;
	public String status;
	public Date paymentDate;
	public Short paymentAnnual;
	public Short paymentMonthly;
	public Integer paymentYm;
	public String userId;
	public String userName;
	public String paymentSlipCategory;
	public String supplierCode;
	public String supplierName;
	public Integer rateId;
	public String taxShiftCategory;
	public String taxFractCategory;
	public String priceFractCategory;
	public BigDecimal priceTotal;
	public BigDecimal fePriceTotal;
	public Integer poSlipId;
	public Integer supplierSlipId;
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
