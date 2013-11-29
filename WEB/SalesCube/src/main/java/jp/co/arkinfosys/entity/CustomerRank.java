/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;

/**
 * 顧客ランクマスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class CustomerRank extends AuditInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "CUSTOMER_RANK_MST";

	public String rankCode;

	public String rankName;

	public BigDecimal rankRate;

	public Integer roCountFrom;

	public Integer enrollTermFrom;

	public Integer defectTermFrom;

	public BigDecimal roMonthlyAvgFrom;

	public String postageType;

	public String remarks;

	public Integer roCountTo;

	public Integer enrollTermTo;

	public Integer defectTermTo;

	public BigDecimal roMonthlyAvgTo;

}
