/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 税率マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class TaxRate {

	public static final String TABLE_NAME = "TAX_RATE_MST";

	@Id
	public String taxTypeCategory;

	@Id
	public Date startDate;

	public BigDecimal taxRate;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;
}
