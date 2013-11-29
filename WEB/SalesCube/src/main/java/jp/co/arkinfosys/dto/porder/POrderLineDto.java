/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.porder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 発注伝票明細行情報を管理するDTOクラスです
 *
 * @author Ark Information Systems
 *
 */
public class POrderLineDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public Integer poLineId;
	public String status;
	public Integer poSlipId;
	public Short poLineNo;
	public String productCode;
	public String supplierPcode;
	public String productAbstract;
	public BigDecimal quantity;
	public String tempUnitPriceCategory;
	public String taxCategory;
	public String supplierCmCategory;
	public BigDecimal unitPrice;
	public BigDecimal price;
	public BigDecimal ctaxPrice;
	public BigDecimal ctaxRate;
	public BigDecimal dolUnitPrice;
	public BigDecimal dolPrice;
	public BigDecimal rate;
	public Date deliveryDate;
	public String remarks;
	public BigDecimal restQuantity;
	public String creFunc;
	public Timestamp creDatetm;
	public String creUser;
	public String updFunc;
	public Timestamp updDatetm;
	public String updUser;

}
