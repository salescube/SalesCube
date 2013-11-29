/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 部門マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Dept implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "DEPT_MST";

	@Id
	public String deptId;

	public String name;

	public String parentId;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
