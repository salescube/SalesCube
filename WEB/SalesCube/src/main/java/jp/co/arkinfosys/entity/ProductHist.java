/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 商品マスタ履歴テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ProductHist implements Serializable {

		private static final long serialVersionUID = 1L;
		@Transient

		public static final String TABLE_NAME = "PRODUCT_MST_HIST";

		@Id
		@GeneratedValue
		@Column(name = "HIST_ID")
		public Integer histId;
		/**
		 *
		 */
		@Column(name = "ACTION_TYPE")
		public String actionType;
		/**
		 *
		*/
		public String productCode;
		public String productName;
		public String productKana;
		public String onlinePcode;
		public String janPcode;
		public Date discardDate;

		public String supplierCode;
		public String supplierName;
		public String supplierPcode;
		public BigDecimal supplierPriceYen;
		public BigDecimal supplierPriceDol;

		public String stockCtlCategoryCdx;
		public String stockCtlCategoryNm;

		public Short packQuantity;
		public String rackCode;
		public Integer leadTime;
		public Integer poNum;
		public String poUpdFlag;
		public Integer mineSafetyStock;
		public String mineSafetyStockUpdFlag;
		public BigDecimal poLot;
		public String lotUpdFlag;
		public Integer maxStockNum;
		public String stockUpdFlag;
		public Integer maxPoNum;
		public String maxPoUpdFlag;

		public Short roMaxNum;
		public BigDecimal retailPrice;
		public String dicountCdx;
		public String dicountNm;

		public String productStatusCategoryCdx;
		public String productStatusCategoryNm;
		public String productStockCategoryCdx;
		public String productStockCategoryNm;
		public String productPurvayCategoryCdx;
		public String productPurvayCategoryNm;
		public String productStandardCategoryCdx;
		public String productStandardCategoryNm;

		public BigDecimal soRate;
		public String setTypeCategory;

		public String productCdx;
		public String productNm;

		public String unitCategoryCdx;
		public String unitCategoryNm;
		public Float weight;
		public String weightUnitSizeCategoryCdx;
		public String weightUnitSizeCategoryNm;
		public Float length;
		public String lengthUnitSizeCategoryCdx;
		public String lengthUnitSizeCategoryNm;
		public Float width;
		public String widthUnitSizeCategoryCdx;
		public String widthUnitSizeCategoryNm;
		public Float depth;
		public String depthUnitSizeCategoryCdx;
		public String depthUnitSizeCategoryNm;
		public Float height;
		public String heightUnitSizeCategoryCdx;
		public String heightUnitSizeCategoryNm;
		public String coreNum;

		public String remarks;
		public String eadRemarks;
		public String commentData;

		public Timestamp updDatetm;

}
