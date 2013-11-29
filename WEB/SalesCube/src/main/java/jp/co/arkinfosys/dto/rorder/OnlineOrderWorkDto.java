/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.rorder;

import java.sql.Date;

/**
 * オンライン受注テーブル情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OnlineOrderWorkDto {

	public String userId;

	public String onlineOrderId;

	public String onlineItemId;

	public String supplierDate;

	public String paymentDate;

	public String customerEmail;

	public String customerName;

	public String customerTel;

	public String sku;

	public String productName;

	public String quantity;

	public String currency;

	public String price;

	public String taxPrice;

	public String shippingPrice;

	public String shippingTax;

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

	public String deliveryStartDate;

	public String deliveryEndDate;

	public String deliveryTimeZone;

	public String deliveryInst;

	public int lineNo;

	public Date loadDate;	// 取込日付
}
