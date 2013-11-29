/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import javax.persistence.Entity;

/**
 * レートマスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Rate extends AuditInfo {

	public static final String TABLE_NAME = "RATE_MST";

	public Integer rateId;
	public String name;
	public String sign;
	public String remarks;

}
