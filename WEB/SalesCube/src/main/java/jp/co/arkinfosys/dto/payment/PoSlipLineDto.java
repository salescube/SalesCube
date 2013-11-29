/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * 発注伝票明細行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class PoSlipLineDto implements Serializable{
	private static final long serialVersionUID = 1L;

	public String productCode;
	public String quantity;
	public BigDecimal unitPrice;
	public BigDecimal price;
	public BigDecimal dolUnitPrice;
	public BigDecimal dolPrice;
	public Integer poLineId;
	public Integer supplierSlipId;
	public String supplierDetailCategory;
	public Date supplierDate;

}
