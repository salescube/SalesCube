/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;

import jp.co.arkinfosys.entity.Discount;

/**
 * 数量割引マスタと数量割引データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DiscountJoin extends Discount {

	private static final long serialVersionUID = 1L;

	public String useFlagName;

	public Integer discountDataId;

	public Integer lineNo;

	public BigDecimal dataFrom;

	public BigDecimal dataTo;

	public BigDecimal discountRate;

}
