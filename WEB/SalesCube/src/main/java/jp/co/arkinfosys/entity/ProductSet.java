/*
 *  Copyright 2009-2010 Ark Information Systems.
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
	public String setProductCode; 

	@Id
	public String productCode; 

	public BigDecimal quantity; 

	public String creFunc; 

	public Timestamp creDatetm; 

	public String creUser; 

	public String updFunc; 

	public Timestamp updDatetm; 

	public String updUser; 

}
