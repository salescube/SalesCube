/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;

import javax.persistence.Entity;

/**
 * 商品数量のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class StockQuantity {

	public String productCode;

	public BigDecimal quantity;

}
