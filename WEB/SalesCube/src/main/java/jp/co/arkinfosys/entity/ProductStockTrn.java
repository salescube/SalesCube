/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 商品在庫情報テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ProductStockTrn {

	public static final String TABLE_NAME = "PRODUCT_STOCK_TRN";

	@Id
	public short stockAnnual;

	@Id
	public short stockMonthly;

	@Id
	public String rackCode;
	
	public String warehouseName;

	@Id
	public String productCode;

	public Date stockPdate;

	public Integer stockYm;

	public BigDecimal stockNum;

	public BigDecimal enterNum;

	public BigDecimal dispatchNum;

	public String remarks;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
