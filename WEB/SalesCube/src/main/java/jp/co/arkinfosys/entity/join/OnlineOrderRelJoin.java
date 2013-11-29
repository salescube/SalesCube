/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * オンライン受注とオンライン受注関連のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class OnlineOrderRelJoin {

	public String userId;

	public String onlineOrderId;

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

	public Integer roSlipId;

	public Integer roLineId;

	public Date loadDate;
}
