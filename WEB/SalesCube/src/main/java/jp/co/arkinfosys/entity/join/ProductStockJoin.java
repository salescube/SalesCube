/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
/**
 * 商品マスタと入出庫伝票明細行と商品在庫のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ProductStockJoin implements Serializable {

	private static final long serialVersionUID = 1L;

	public String productCode;

	public String productName;

	public String rackCode;

	public BigDecimal supplierPriceYen;

	public BigDecimal allStockNum;

	
	public String supplierCode;
	
	public String supplierName;
	
	public BigDecimal poLot;
	
	public Integer avgShipCount;
	
	public Integer poNum;
	
	public BigDecimal stockQuantity;
	
	public BigDecimal entrustQuantity;
	
	public BigDecimal poRestQuantity;
	
	public BigDecimal entrustRestQuantity;
	
	public BigDecimal roRestQuantity;

}
