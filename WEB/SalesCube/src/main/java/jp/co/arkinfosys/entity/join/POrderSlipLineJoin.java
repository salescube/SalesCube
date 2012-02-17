/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;
/**
 * 発注伝票と発注伝票明細と区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class POrderSlipLineJoin implements Serializable{

	private static final long serialVersionUID = 1L;

	

	
	public String paymentStatus;
	
	public BigDecimal purePriceTotal;

	

	
	public Integer poSlipId;

	
	public String status;

	
	public Date poDate;

	
	public Date deliveryDate;

	
	public String userId;
	public String userName;

	
	public String remarks;

	
	public String supplierCode;
	
	public String supplierName;

	
	public String transportCategory;
	public String transportCategoryString;

	
	public BigDecimal priceTotal;
	
	public BigDecimal ctaxTotal;
	
	public BigDecimal fePriceTotal;

	
	public Integer printCount;

	

	
	public String lineStatus;

	
	public Short lineNo;

	
	public String productCode;
	
	public String productAbstract;
	
	public BigDecimal quantity;

	
	public BigDecimal unitPrice;
	
	public BigDecimal price;

	
	public BigDecimal dolUnitPrice;
	
	public BigDecimal dolPrice;

	
	public Date lineDeliveryDate;

	
	public String lineRemarks;

	
	public BigDecimal restQuantity;

}
