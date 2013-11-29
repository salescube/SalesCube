/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 商品マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Product extends AuditInfo {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "PRODUCT_MST";

	@Id
	@Column(length = 20)
	public String productCode;

	@Column(length = 60)
	public String productName;

	@Column(length = 60)
	public String productKana;

	@Column(length = 50)
	public String onlinePcode;

	@Column(length = 20)
	public String supplierPcode;

	@Column(length = 10)
	public String supplierCode;

	@Column(length = 10)
	public String rackCode;

	public BigDecimal supplierPriceYen;

	public BigDecimal supplierPriceDol;

	public BigDecimal retailPrice;

	public BigDecimal soRate;

	@Column(length = 2)
	public String unitCategory;

	public Short packQuantity;

	@Column(length = 13)
	public String janPcode;

	public Float width;

	@Column(length = 2)
	public String widthUnitSizeCategory;

	public Float depth;

	@Column(length = 2)
	public String depthUnitSizeCategory;

	public Float height;

	@Column(length = 2)
	public String heightUnitSizeCategory;

	public Float weight;

	@Column(length = 2)
	public String weightUnitSizeCategory;

	public Float length;

	@Column(length = 2)
	public String lengthUnitSizeCategory;

	public BigDecimal poLot;

	public Short lotUpdFlag;

	public Integer leadTime;

	public Integer poNum;

	public Short poUpdFlag;

	public Integer mineSafetyStock;

	public Short mineSafetyStockUpdFlag;

	public Integer entrustSafetyStock;

	public BigDecimal salesStandardDeviation;

	public Integer avgShipCount;

	public Integer maxStockNum;

	public Short stockUpdFlag;

	public Integer termShipNum;

	public Integer maxPoNum;

	public Short maxPoUpdFlag;

	@Column(length = 1)
	public String fractCategory;

	@Column(length = 1)
	public String taxCategory;

	@Column(length = 1)
	public String stockCtlCategory;

	@Column(length = 1)
	public String stockAssesCategory;

	@Column(length = 1)
	public String productCategory;

	@Column(length = 4)
	public String product1;

	@Column(length = 4)
	public String product2;

	@Column(length = 4)
	public String product3;

	public Short roMaxNum;

	@Column(length = 2)
	public String productRank;

	@Column(length = 1)
	public String setTypeCategory;

	@Column(length = 2)
	public String productStatusCategory;

	@Column(length = 2)
	public String productStockCategory;

	@Column(length = 2)
	public String productPurvayCategory;

	@Column(length = 2)
	public String productStandardCategory;

	@Column(length = 5)
	public String coreNum;

	public Short num1;

	public Short num2;

	public Short num3;

	public Short num4;

	public Short num5;

	public Float dec1;

	public Float dec2;

	public Float dec3;

	public Float dec4;

	public Float dec5;

	public Date discardDate;

	@Column(length = 120)
	public String remarks;

	@Column(length = 120)
	public String eadRemarks;

	@Column(length = 1000)
	public String commentData;

	public Date lastRoDate;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Product)) {
			return false;
		}

		try {
			for (int i = 0; i < Product.class.getFields().length; i++) {
				Field f1 = Product.class.getFields()[i];
				Field f2 = obj.getClass().getField(f1.getName());

				if (!this.compareField(f1.get(this), f2.get(obj))) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * フィールド値比較メソッドです.
	 *
	 * @param obj1　比較対象オブジェクト１
	 * @param obj2　比較対象オブジェクト２
	 * @return　等しいか否か
	 */
	protected boolean compareField(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		}
		if (obj1 == null || obj2 == null) {
			return false;
		}
		if (obj1 instanceof String && obj2 instanceof String) {
			return obj1.equals(obj2);
		}
		if (obj1 instanceof Short && obj2 instanceof Short) {
			return ((Short) obj1).shortValue() == ((Short) obj2).shortValue();
		}

		if (obj1 instanceof Integer && obj2 instanceof Integer) {
			return ((Integer) obj1).intValue() == ((Integer) obj2).intValue();
		}

		if (obj1 instanceof Float && obj2 instanceof Float) {
			return ((Float) obj1).floatValue() == ((Float) obj2).floatValue();
		}

		if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
			return ((BigDecimal) obj1).compareTo(((BigDecimal) obj2)) == 0;
		}
		if (obj1 instanceof java.util.Date && obj2 instanceof java.util.Date) {
			return ((java.util.Date) obj1).compareTo((java.util.Date) obj2) == 0;
		}
		if (obj1 instanceof java.sql.Date && obj2 instanceof java.sql.Date) {
			return ((java.sql.Date) obj1).compareTo((java.sql.Date) obj2) == 0;
		}
		return false;
	}

	@Override
	public int hashCode() {
		// とりあえずequalsとhashCodeの要件は満たすが必要に応じてちゃんと実装する必要がある
		return 0;
	}
}
