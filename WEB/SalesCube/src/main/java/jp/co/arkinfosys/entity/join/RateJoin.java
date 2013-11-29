/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;

import jp.co.arkinfosys.entity.Rate;

/**
 * レートマスタとレートデータのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class RateJoin extends Rate {

	/** レート */
	public BigDecimal rate;

	/** 適用開始日 */
	public String startDate;

}

