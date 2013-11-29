/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 発注伝票明細行のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PoLineTrn implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "PO_LINE_TRN";

	@Id
	@GeneratedValue
	@Column(name = "PO_LINE_ID")
	public Integer poLineId;
	public String status;
	@Column(name = "PO_SLIP_ID")
	public Integer poSlipId;
	@Column(name = "LINE_NO")
	public Short lineNo;
	@Column(name = "PRODUCT_CODE")
	public String productCode;
	@Column(name = "SUPPLIER_PCODE")
	public String supplierPcode;
	@Column(name = "PRODUCT_ABSTRACT")
	public String productAbstract;
	public BigDecimal quantity;
	@Column(name = "TEMP_UNIT_PRICE_CATEGORY")
	public String tempUnitPriceCategory;
	@Column(name = "TAX_CATEGORY")
	public String taxCategory;
	@Column(name = "SUPPLIER_CM_CATEGORY")
	public String supplierCmCategory;
	@Column(name = "UNIT_PRICE")
	public BigDecimal unitPrice;
	public BigDecimal price;
	@Column(name = "CTAX_PRICE")
	public BigDecimal ctaxPrice;
	@Column(name = "CTAX_RATE")
	public BigDecimal ctaxRate;
	@Column(name = "DOL_UNIT_PRICE")
	public BigDecimal dolUnitPrice;
	@Column(name = "DOL_PRICE")
	public BigDecimal dolPrice;
	public BigDecimal rate;
	@Temporal(TemporalType.DATE)
	@Column(name = "DELIVERY_DATE")
	public Date deliveryDate;
	@Column(name = "REMARKS")
	public String remarks;
	@Column(name = "PRODUCT_REMARKS")
	public String productRemarks;
	@Column(name = "REST_QUANTITY")
	public BigDecimal restQuantity;
	@Column(name = "CRE_FUNC")
	public String creFunc;
	@Temporal(TemporalType.DATE)
	@Column(name = "CRE_DATETM")
	public Timestamp creDatetm;
	@Column(name = "CRE_USER")
	public String creUser;
	@Column(name = "UPD_FUNC")
	public String updFunc;
	@Temporal(TemporalType.DATE)
	@Column(name = "UPD_DATETM")
	public Timestamp updDatetm;
	@Column(name = "UPD_USER")
	public String updUser;

}
