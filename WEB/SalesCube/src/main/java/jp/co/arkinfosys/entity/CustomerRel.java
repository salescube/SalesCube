/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 顧客マスタと納入先のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class CustomerRel {

	public static final String TABLE_NAME = "CUSTOMER_REL";

	@Id
	public String customerCode;

	@Id
	public String relCode;

	public String custRelCategory;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
