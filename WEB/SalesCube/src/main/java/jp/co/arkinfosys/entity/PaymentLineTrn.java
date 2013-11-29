/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * 支払伝票明細行のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PaymentLineTrn  implements Serializable{
	private static final long serialVersionUID = 1L;

	// 支払伝票明細行テーブル情報(PAYMENT_SLIP_LINE)
	public Integer paymentLineId;
	public String status;
	public Integer paymentSlipId;
	public Short lineNo;
	public String paymentCategory;
	public String productCode;
	public String productAbstract;
	public String supplierDate;
	public BigDecimal quantity;
	public BigDecimal unitPrice;
	public BigDecimal price;
	public BigDecimal dolUnitPrice;
	public BigDecimal dolPrice;
	public BigDecimal rate;
	public BigDecimal ctaxRate;
	public BigDecimal ctaxPrice;
	public Integer poLineId;
	public Integer supplierLineId;
	public String remarks;
	public String creFunc;
	public Timestamp creDatetm;
	public String creUser;
	public String updFunc;
	public Timestamp updDatetm;
	public String updUser;
}
