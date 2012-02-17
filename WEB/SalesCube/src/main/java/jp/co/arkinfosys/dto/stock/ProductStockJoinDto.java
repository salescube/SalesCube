/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.dto.stock;

import java.math.BigDecimal;

/**
 * 在庫残高表画面の検索結果行情報を管理するDTOクラスです.
 * @author Ark Information Systems
 *
 */
public class ProductStockJoinDto {

	public String productCode;

	public String productName;

	public String rackCode;

	public String supplierPriceYen;

	public String ownStockNum;

	public String entrustStockNum;

	public String allStockNum;

	public String stockPrice;

	
	public String supplierCode;
	
	public String supplierName;
	
	public BigDecimal poLot;
	
	public Integer avgShipCount;
	
	public Integer poNum;
	
	public BigDecimal stockQuantity;
	
	public BigDecimal poRestQuantity;
	
	public BigDecimal entrustRestQuantity;
	
	public BigDecimal roRestQuantity;
	
	public BigDecimal holdQuantity;
	
	public String holdTerm;

}
