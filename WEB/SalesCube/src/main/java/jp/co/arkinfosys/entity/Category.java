/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 区分マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Category extends AuditInfo {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "CATEGORY_MST";

	@Id
	public Integer categoryId;

	public String groupName;

	public String categoryName;

	public String categoryAdd;

	public String categoryUpd;

	public String categoryDel;

	public String categoryDsp;

	public String categoryDataType;

	public Integer categoryStrSize;

	public Integer categoryCodeSize;

	public String categoryTitle;

}
