/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

/**
 * 得意先ランク集計データのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CustomerRankSummary {

	public String customerCode;
	public String customerName;
	public String customerCreUser;
	public String nameKnj;
	public String rankCode;
	public String rankName;
	public BigDecimal salesPrice6;
	public BigDecimal salesPrice5;
	public BigDecimal salesPrice4;
	public BigDecimal salesPrice3;
	public BigDecimal salesPrice2;
	public BigDecimal salesPrice1;
	public BigDecimal salesPriceLsm;
	public Date firstSalesDate;
	public Date lastSalesDate;
	public Integer enrollTermSpan;
	public Integer defectTermSpan;
	public Timestamp creDatetm;

}
