/*
 * Copyright 2009-2010 Ark Information Systems.
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

	//商品仕入先コード
	public String supplierCode;
	//商品仕入先名
	public String supplierName;
	//発注ロット
	public BigDecimal poLot;
	//平均出庫数
	public Integer avgShipCount;
	//発注点
	public Integer poNum;
	//現在庫数
	public BigDecimal stockQuantity;
	//発注残
	public BigDecimal poRestQuantity;
	//委託残
	public BigDecimal entrustRestQuantity;
	//受注残
	public BigDecimal roRestQuantity;
	//保有数
	public BigDecimal holdQuantity;
	//保有月数
	public String holdTerm;

}
