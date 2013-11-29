/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 送り状データテーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class InvoiceDataWork {

	@Id
	@GeneratedValue
	@Column(name = "USER_ID")
	public String userId;
	/**
	 *
	 */
	@Column(name = "CUSTOMER_CODE")
	public String customerCode;
	/**
	 *
	 */
	@Column(name = "SI_TYPE")
	public String siType;
	/**
	 *
	 */
	@Column(name = "COOL")
	public String cool;
	/**
	 *
	 */
	@Id
	@GeneratedValue
	@Column(name = "DELIVERY_SLIP_ID")
	public String deliverySlipId;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "SHIP_DATE")
	public Date shipDate;
	/**
	 *
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DELIVERY_DATE")
	public Date deliveryDate;
	/**
	 *
	 */
	@Column(name = "TIME_ZONE")
	public String timeZone;
	/**
	 *
	 */
	@Column(name = "DELIVERY_CODE")
	public String deliveryCode;
	/**
	 *
	 */
	@Column(name = "DELIVERY_TEL")
	public String deliveryTel;
	/**
	 *
	 */
	@Column(name = "DELIVERY_TEL2")
	public String deliveryTel2;
	/**
	 *
	 */
	@Column(name = "DELIVERY_ZIP_CODE")
	public String deliveryZipCode;
	/**
	 *
	 */
	@Column(name = "DELIVERY_ADDRESS")
	public String deliveryAddress;
	/**
	 *
	 */
	@Column(name = "DELIVERY_ADDRESS2")
	public String deliveryAddress2;
	/**
	 *
	 */
	@Column(name = "DELIVERY_OFFICE1")
	public String deliveryOffice1;
	/**
	 *
	 */
	@Column(name = "DELIVERY_OFFICE2")
	public String deliveryOffice2;
	/**
	 *
	 */
	@Column(name = "DELIVERY_NAME")
	public String deliveryName;
	/**
	 *
	 */
	@Column(name = "DELIVERY_KANA")
	public String deliveryKana;
	/**
	 *
	 */
	@Column(name = "DELIVERY_PRE")
	public String deliveryPre;
	/**
	 *
	 */
	@Column(name = "OWNER_CODE")
	public String ownerCode;
	/**
	 *
	 */
	@Column(name = "OWNER_TEL")
	public String ownerTel;
	/**
	 *
	 */
	@Column(name = "OWNER_TEL2")
	public String ownerTel2;
	/**
	 *
	 */
	@Column(name = "OWNER_ZIP_CODE")
	public String ownerZipCode;
	/**
	 *
	 */
	@Column(name = "OWNER_ADDRESS")
	public String ownerAddress;
	/**
	 *
	 */
	@Column(name = "OWNER_ADDRESS2")
	public String ownerAddress2;
	/**
	 *
	 */
	@Column(name = "OWNER_NAME")
	public String ownerName;
	/**
	 *
	 */
	@Column(name = "OWNER_KANA")
	public String ownerKana;
	/**
	 *
	 */
	@Column(name = "PRODUCT_CODE1")
	public String productCode1;
	/**
	 *
	 */
	@Column(name = "PRODUCT_NAME1")
	public String productName1;
	/**
	 *
	 */
	@Column(name = "PRODUCT_CODE2")
	public String productCode2;
	/**
	 *
	 */
	@Column(name = "PRODUCT_NAME2")
	public String productName2;
	/**
	 *
	 */
	@Column(name = "HANDLE1")
	public String handle1;
	/**
	 *
	 */
	@Column(name = "HANDLE2")
	public String handle2;
	/**
	 *
	 */
	@Column(name = "SALES_SLIP_ID")
	public String salesSlipId;
	/**
	 *
	 */
	@Column(name = "COLLECT_PRICE")
	public String collectPrice;
	/**
	 *
	 */
	@Column(name = "CTAX_PRICE")
	public String ctaxPrice;
	/**
	 *
	 */
	@Column(name = "LAYAWAY")
	public String layaway;
	/**
	 *
	 */
	@Column(name = "OFFICE_CODE")
	public String officeCode;
	/**
	 *
	 */
	@Column(name = "PRINT_CNT")
	public String printCnt;
	/**
	 *
	 */
	@Column(name = "NUM_DISP_FLAG")
	public String numDispFlag;
	/**
	 *
	 */
	@Column(name = "BA_CODE")
	public String baCode;
	/**
	 *
	 */
	@Column(name = "BA_TYPE")
	public String baType;
	/**
	 *
	 */
	@Column(name = "FARE_NO")
	public String fareNo;
	/**
	 *
	 */
	@Column(name = "PAYMENT_SET")
	public String PaymentSet;
	/**
	 *
	 */
	@Column(name = "PAYMENT_NO")
	public String paymentNo;
	/**
	 *
	 */
	@Column(name = "PAYMENT_NO1")
	public String paymentNo1;
	/**
	 *
	 */
	@Column(name = "PAYMENT_NO2")
	public String paymentNo2;
	/**
	 *
	 */
	@Column(name = "PAYMENT_NO3")
	public String paymentNo3;
	/**
	 *
	 */
	@Column(name = "EMAIL_USE")
	public String emailUse;
	/**
	 *
	 */
	@Column(name = "EMAIL_ADDRESS")
	public String emailAddress;
	/**
	 *
	 */
	@Column(name = "MACHINE_TYPE")
	public String machineType;
	/**
	 *
	 */
	@Column(name = "MAIL_MESSAGE")
	public String mailMessage;
	/**
	 *
	 */
	@Column(name = "DELIVERY_EMAIL_USE")
	public String deliveryEmailUse;
	/**
	 *
	 */
	@Column(name = "DELIVERY_EMAIL_ADDRESS")
	public String deliveryEmailAddress;
	/**
	 *
	 */
	@Column(name = "DELIVERY_EMAIL_MESSAGE")
	public String deliveryEmailMessage;
	/**
	 *
	 */
	@Column(name = "APS_USE")
	public String apsUse;
	/**
	 *
	 */
	@Column(name = "QR_PRINT_FLG")
	public String qrPrintFlg;
	/**
	 *
	 */
	@Column(name = "APS_BILL_PRICE")
	public String apsBillPrice;
	/**
	 *
	 */
	@Column(name = "APS_CTAX_PRICE")
	public String apsCtaxPrice;
	/**
	 *
	 */
	@Column(name = "APS_ZIP_CODE")
	public String apsZipCode;
	/**
	 *
	 */
	@Column(name = "APS_ADDRESS")
	public String apsAddress;
	/**
	 *
	 */
	@Column(name = "APS_ADDRESS2")
	public String apsAddress2;
	/**
	 *
	 */
	@Column(name = "APS_OFFICE1")
	public String apsOffice1;
	/**
	 *
	 */
	@Column(name = "APS_OFFICE2")
	public String apsOffice2;
	/**
	 *
	 */
	@Column(name = "APS_NAME")
	public String apsName;
	/**
	 *
	 */
	@Column(name = "APS_KANA")
	public String apsKana;
	/**
	 *
	 */
	@Column(name = "APS_QNAME")
	public String apsQname;
	/**
	 *
	 */
	@Column(name = "APS_QZIP_CODE")
	public String apsQzipCode;
	/**
	 *
	 */
	@Column(name = "APS_QADDRESS")
	public String apsQaddress;
	/**
	 *
	 */
	@Column(name = "APS_QADDRESS2")
	public String apsQaddress2;
	/**
	 *
	 */
	@Column(name = "APS_QTEL")
	public String apsQtel;
	/**
	 *
	 */
	@Column(name = "APS_NO")
	public String apsNo;
	/**
	 *
	 */
	@Column(name = "APS_PRODUCT_NAME")
	public String apsProductName;
	/**
	 *
	 */
	@Column(name = "APS_REMARK")
	public String apsRemark;

}
