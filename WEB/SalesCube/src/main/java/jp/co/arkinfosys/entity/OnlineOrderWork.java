/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * オンライン受注テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class OnlineOrderWork {

	@Id
	public String userId;
	@Id
	public String onlineOrderId;
	@Id
	public String onlineItemId;

	public Timestamp supplierDate;

	public Timestamp paymentDate;

	public String customerEmail;

	public String customerName;

	public String customerTel;

	public String sku;

	public String productName;

	public BigDecimal quantity;

	public String currency;

	public BigDecimal price;

	public BigDecimal taxPrice;

	public BigDecimal shippingPrice;

	public BigDecimal shippingTax;

	public String shipServiceLevel;

	public String recipientName;

	public String address1;

	public String address2;

	public String address3;

	public String city;

	public String state;

	public String zipCode;

	public String country;

	public String shipTel;

	public Timestamp deliveryStartDate;

	public Timestamp deliveryEndDate;

	public String deliveryTimeZone;

	public String deliveryInst;

	public String lineNo;

}
