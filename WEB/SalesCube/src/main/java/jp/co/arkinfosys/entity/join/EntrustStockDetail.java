/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 委託入出庫伝票と委託入出庫明細と発注伝票明細行のリレーションエンティティクラスです.<BR>
 * 委託在庫明細を取得します．
 *
 * @author Ark Information Systems
 *
 */
public class EntrustStockDetail {
	/** 発注番号 */
	public Integer poSlipId;

	/** 行番号 */
	public Short lineNo;

	/** 委託入出庫日 */
	public Date entrustEadDate;

	/** 委託在庫数 */
	public BigDecimal quantity;
}
