/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 数量割引マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Discount extends AuditInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "DISCOUNT_MST";

	@Id
	public String discountId;

	public String discountName;

	public String remarks;

	public String useFlag;

}
