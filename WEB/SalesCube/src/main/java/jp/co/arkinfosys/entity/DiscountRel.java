/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 割引マスタと商品マスタのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class DiscountRel {

	public static final String TABLE_NAME = "DISCOUNT_REL";

	@Id
	public String productCode;

	@Id
	public String discountId;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
