/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 発注伝票と発注伝票明細のリレーションエンティティクラスです.<BR>
 * 委託発注残明細を取得します．
 *
 * @author Ark Information Systems
 *
 */
public class EntrustPorderRestDetail {
	/** 発注番号 */
	public Integer poSlipId;

	/** 行番号 */
	public Short lineNo;

	/** 発注日 */
	public Date poDate;

	/** 納期 */
	public Date deliveryDate;

	/** 発注残数 */
	public BigDecimal restQuantity;
}
