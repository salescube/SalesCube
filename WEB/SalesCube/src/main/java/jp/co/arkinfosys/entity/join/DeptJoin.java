/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.Dept;
/**
 * 部門マスタと部門マスタのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class DeptJoin extends Dept {

	private static final long serialVersionUID = 1L;

	public String parentName;

}
