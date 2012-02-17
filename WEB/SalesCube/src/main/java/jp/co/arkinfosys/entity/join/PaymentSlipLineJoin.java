/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * 支払伝票明細行と仕入伝票と仕入伝票明細行のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PaymentSlipLineJoin  implements Serializable{
	private static final long serialVersionUID = 1L;

	
	public Integer paymentLineId;			
	public Short paymentLineNo;				
	public Integer supplierSlipId;			
	public Integer supplierLineId;			
	public Short supplierLineNo;			
	public String supplierDetailCategory;	
	public Date supplierDate;				
	public String productCode;				
	public String productAbstract;			
	public String paymentCategory;			
	public BigDecimal quantity;				
	public BigDecimal rate;					
	public BigDecimal unitPrice;			
	public BigDecimal price;				
	public BigDecimal dolUnitPrice;			
	public BigDecimal dolPrice;				
	public String remarks;					
	public Timestamp supUpdDatetm;			
	public String updSupId;					
	public BigDecimal ctaxRate;				
}
