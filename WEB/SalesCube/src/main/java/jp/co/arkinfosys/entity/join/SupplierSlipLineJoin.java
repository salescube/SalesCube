/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 仕入伝票と仕入伝票明細行のリレーションエンティティクラスです.
 * @author Ark Information Systems
 *
 */
@Entity
public class SupplierSlipLineJoin  implements Serializable{
	private static final long serialVersionUID = 1L;

	
	public Integer supplierSlipId;			
	public Integer supplierLineId;			
	public String supplierDetailCategory;	
	public BigDecimal quantity;				
	public BigDecimal price;				
	public BigDecimal unitPrice;			
	public BigDecimal dolUnitPrice;			
	public BigDecimal dolPrice;				
	public Integer poLineId;				
	public Date supplierDate;				
	public String supplierCode;				
}

