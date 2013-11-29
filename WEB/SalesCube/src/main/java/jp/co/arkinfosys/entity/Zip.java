/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 郵便番号マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Zip implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	public Integer zipId;

	public String zipCode;

	public String zipAddress1;

	public String zipAddress2;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
