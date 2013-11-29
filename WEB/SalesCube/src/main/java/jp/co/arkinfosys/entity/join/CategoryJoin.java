/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.Category;

/**
 * 区分マスタと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class CategoryJoin extends Category {

	private static final long serialVersionUID = 1L;

	/**
	 * 区分データコード
	 */
	public String categoryCode;

	/**
	 * 区分データ名
	 */
	public String categoryCodeName;

	/**
	 * 区分データ文字列
	 */
	public String categoryStr;

	/**
	 * 区分データ整数
	 */
	public Integer categoryNum;

	/**
	 * 区分データ小数
	 */
	public BigDecimal categoryFlt;

	/**
	 * 区分データ論理
	 */
	public String categoryBool;

	/**
	 * 区分データ
	 */
	public String categoryTrnDsp;

}
