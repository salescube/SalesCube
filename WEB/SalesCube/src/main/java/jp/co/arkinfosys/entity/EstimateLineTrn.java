/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 見積伝票明細行のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class EstimateLineTrn {

	@Id
	@GeneratedValue
	@Column(name = "ESTIMATE_LINE_ID")
	public Integer estimateLineId;
	@Column(name = "ESTIMATE_SHEET_ID")
	public String estimateSheetId;
	@Column(name = "LINE_NO")
	public Short lineNo;
	@Column(name = "PRODUCT_CODE")
	public String productCode;
	@Column(name = "CUSTOMER_PCODE")
	public String customerPcode;
	@Column(name = "PRODUCT_ABSTRACT")
	public String productAbstract;
	public BigDecimal quantity;
	@Column(name = "UNIT_COST")
	public BigDecimal unitCost;
	@Column(name = "UNIT_RETAIL_PRICE")
	public BigDecimal unitRetailPrice;
	public BigDecimal cost;
	@Column(name = "RETAIL_PRICE")
	public BigDecimal retailPrice;
	public String remarks;
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
