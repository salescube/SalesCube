/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * ロールマスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	public String roleId;

	public String name;

	public String remarks;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
