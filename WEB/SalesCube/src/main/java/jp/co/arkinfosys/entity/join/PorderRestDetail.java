/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 発注伝票明細と仕入伝票明細のリレーションエンティティクラスです.<BR>
 * 発注残明細を取得します．
 * @author Ark Information Systems
 *
 */
public class PorderRestDetail {
	/** 発注番号 */
	public Integer poSlipId;

	/** 行番号 */
	public Short lineNo;

	/** 納期 */
	public Date deliveryDate;

	/** 発注残数 */
	public BigDecimal restQuantity;
}
