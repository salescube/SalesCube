/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 仕入伝票明細行のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class SupplierLineTrn implements Serializable {

    private static final long serialVersionUID = 1L;

    public Integer supplierLineId;
    public String status;
    public Integer supplierSlipId;
    public Integer lineNo;
    public String productCode;
	public String supplierPcode;
	public String productAbstract;
	public String supplierDetailCategory;
	public String deliveryProcessCategory;
	public String tempUnitPriceCategory;
	public String taxCategory;
    public Date deliveryDate;
    public BigDecimal quantity;
    public BigDecimal unitPrice;
    public BigDecimal price;
    public BigDecimal ctaxRate;
    public BigDecimal ctaxPrice;
    public BigDecimal dolUnitPrice;
    public BigDecimal dolPrice;
    public BigDecimal rate;
    public String remarks;
    public String productRemarks;
    public String rackCode;
    public String rackName;
    public String warehouseName;
    public Integer poLineId;
    public Integer paymentLineId;
	public String creFunc;
	public Timestamp creDatetm;
	public String creUser;
	public String updFunc;
	public Timestamp updDatetm;
	public String updUser;
}