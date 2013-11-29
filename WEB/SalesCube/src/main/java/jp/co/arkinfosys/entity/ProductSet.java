/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * セット商品マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ProductSet implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "PRODUCT_SET_MST";

	@Id
	public String setProductCode; // セット商品コード

	@Id
	public String productCode; // 商品コード

	public BigDecimal quantity; // 数量

	public String creFunc; // 作成機能

	public Timestamp creDatetm; // 作成日時

	public String creUser; // 作成者

	public String updFunc; // 更新機能

	public Timestamp updDatetm; // 更新日時

	public String updUser; // 更新者

}
