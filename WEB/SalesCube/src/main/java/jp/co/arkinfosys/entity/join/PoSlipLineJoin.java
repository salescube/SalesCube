/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * 発注伝票明細行と仕入伝票と仕入伝票明細のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PoSlipLineJoin implements Serializable{
	private static final long serialVersionUID = 1L;

	public String productCode;
	public String productAbstract;
	public String quantity;
	public BigDecimal rate;
	public BigDecimal unitPrice;
	public BigDecimal price;
	public BigDecimal dolUnitPrice;
	public BigDecimal dolPrice;
	public Integer poLineId;
	public Integer supplierLineId;
	public Integer supplierSlipId;
	public Short supplierLineNo;
	public String supplierDetailCategory;
	public Date supplierDate;
	public Timestamp supUpdDatetm;
	public BigDecimal ctaxRate;
	public BigDecimal ctaxPrice;
}
