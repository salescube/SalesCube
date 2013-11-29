/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.ProductSet;

/**
 * セット商品マスタと商品マスタのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ProductSetJoin extends ProductSet implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * セット商品名
	 */
	public String setProductName;

	/**
	 * セット商品（親）の作成日時
	 */
	public Timestamp setProductCreDatetm;

	/**
	 * セット商品（親または子）の更新日時
	 */
	public Timestamp setProductUpdDatetm;

	/**
	 * 商品名
	 */
	public String productName;
}
