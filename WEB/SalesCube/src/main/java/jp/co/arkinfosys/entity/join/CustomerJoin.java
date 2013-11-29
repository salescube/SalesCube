/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.Customer;

/**
 * 顧客マスタと顧客ランクマスタと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class CustomerJoin extends Customer {

	private static final long serialVersionUID = 1L;

	/**
	 * 顧客ランク名
	 */
	public String rankName;

	/**
	 * 区分コード（支払条件）
	 */
	public Integer categoryId;

	/**
	 * 支払条件コード
	 */
	public String categoryCode;

	/**
	 * 支払条件名
	 */
	public String categoryCodeName;

	/**
	 * 区分コード（税転嫁）
	 */
	public Integer categoryId2;

	/**
	 * 税転嫁コード
	 */
	public String categoryCode2;

	/**
	 * 税転嫁名称
	 */
	public String categoryCodeName2;

	/**
	 * 区分コード（売上取引区分）
	 */
	public Integer categoryId3;

	/**
	 * 売上取引区分コード
	 */
	public String categoryCode3;

	/**
	 * 売上取引区分名
	 */
	public String categoryCodeName3;




}
