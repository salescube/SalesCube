/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 割引データテーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class DiscountTrn implements Serializable {

	public static final String TABLE_NAME = "DISCOUNT_TRN";

	private static final long serialVersionUID = 1L;

	@Id
	public Integer discountDataId;

	public String discountId;

	public Integer lineNo;

	public BigDecimal dataFrom;

	public BigDecimal dataTo;

	public BigDecimal discountRate;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
