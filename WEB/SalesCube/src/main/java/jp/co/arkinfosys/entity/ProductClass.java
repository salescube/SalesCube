/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 商品分類マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ProductClass extends AuditInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "PRODUCT_CLASS_MST";

	@Id
	public String classCode1;

	@Id
	public String classCode2;

	@Id
	public String classCode3;

	public String className;

}
