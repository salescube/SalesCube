/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 受注伝票と受注伝票明細行と売上伝票明細行のリレーションエンティティクラスです.<BR>
 * 受注残明細を取得します．
 * @author Ark Information Systems
 *
 */
public class RorderRestDetail {
	/** 受注番号 */
	public Integer roSlipId;

	/** 行番号 */
	public Short lineNo;

	/** 出荷日 */
	public Date shipDate;

	/** 受注残数 */
	public BigDecimal restQuantity;
}
