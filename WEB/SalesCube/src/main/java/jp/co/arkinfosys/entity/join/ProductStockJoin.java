/*
 * Copyright 2009-2010 Ark Information Systems.
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
	//委託在庫
	public BigDecimal entrustQuantity;
	//発注残
	public BigDecimal poRestQuantity;
	//委託残
	public BigDecimal entrustRestQuantity;
	//受注残
	public BigDecimal roRestQuantity;

}
