/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * ドメインマスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Domain implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "DOMAIN_MST";

	@Id
	public String domainId;

	public String managerName;

	public String telno;

	public String email;

	public String remarks;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;
}