/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 区分データのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class CategoryTrn extends AuditInfo {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "CATEGORY_TRN";

	@Id
	public Integer categoryId;

	@Id
	public String categoryCode;

	public String categoryCodeName;

	public String categoryStr;

	public Integer categoryNum;

	public BigDecimal categoryFlt;

	public String categoryBool;

	public String categoryDsp;

}
