/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;

/**
 * 商品の在庫情報のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ProductStockInfo {

	public Integer aggregateMonthsRange;

	public Short numDecAlignment;

	public Short unitPriceDecAlignment;

	public Short statsDecAlignment;

	public String priceFractCategory;

	public String productFractCategory;

	public String productCode;

	public String productName;

	public String rackCode;

	public String rackName;

	public Integer avgShipCount;

	public Integer maxStockNum;

	public Integer poNum;

	public BigDecimal poLot;

	public BigDecimal supplierPriceYen;

	public BigDecimal supplierPriceDol;

	public BigDecimal retailPrice;

	public Integer leadTime;

	public BigDecimal salesStandardDeviation;

	public Integer mineSafetyStock;

	public Integer entrustSafetyStock;

	public BigDecimal stockQuantityEadUnclosed;

	public BigDecimal stockQuantityEadClosed;

	public BigDecimal stockQuantityEntrustEad;

	public BigDecimal restQuantityRo;

	public BigDecimal restQuantityPoShip;

	public BigDecimal restQuantityPoAir;

	public BigDecimal restQuantityPoDelivery;

	public BigDecimal restQuantityEntrust;

	public Date deliveryDate;

	public BigDecimal restQuantityPo;

	public BigDecimal currentStockQuantity;

	public BigDecimal availableStockQuantity;

	public BigDecimal holdingStockQuantity;

	public BigDecimal holdingStockMonth;

	public BigDecimal roQuantity;

	public BigDecimal salesQuantity;

	public BigDecimal maxSalesQuantity;

	public BigDecimal retailPriceTotal;

	public BigDecimal grossMarginTotal;

	public BigDecimal entrustPoNum;

	public Integer poSlipId;

}
