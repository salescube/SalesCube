/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import javax.persistence.Column;
import javax.persistence.Transient;

import jp.co.arkinfosys.entity.Bank;

/**
 * 銀行マスタと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class BankDwb extends Bank {
	/**
	 * 検索条件のキー名称
	 */
	@Transient
	public static final String keyName = "categoryId";

	/**
	 * 預金種類名
	 */
	@Column(name = "CATEGORY_CODE_NAME")
	public String dwbName;
}
