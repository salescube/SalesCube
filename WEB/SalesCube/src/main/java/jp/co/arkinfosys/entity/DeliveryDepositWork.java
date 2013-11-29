/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 配送業者入金データのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class DeliveryDepositWork implements Serializable {

	private static final long serialVersionUID = 1L;

	public String userId;

	public String paymentCategory;

	public String customerCode;

	public String deliverySlipId;

	public String dataCategory;

	public String changeCount;

	public String serviceCategory;

	public String settleCategory;

	public Date deliveryDate;

	public BigDecimal productPrice;

	public BigDecimal codPrice;

	public BigDecimal servicePrice;

	public BigDecimal splitPrice;

	public BigDecimal stampPrice;

	public Date rgDate;

	public String rgSlipId;

}
