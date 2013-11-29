/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 配送業者入金関連と配送業者入金データと送り状データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ImportDeliveryDepositJoin  implements Serializable{
	private static final long serialVersionUID = 1L;

	/*
	 *  ※以下のテーブルを結合
	 *  DELIVERY_DEPOSIT_WORK_	配送業者入金データ
	 *  INVOICE_DATA_WORK_		送り状データ
	 */

	public String 	status;				/* 状態 */
	public Integer 	salesSlipId;		/* 売上番号 */
	public Integer 	depositSlipId;		/* 入金番号 */
	public String 	deliverySlipId;		/* 配送業者伝票番号 */
	public String 	customer;			/* 顧客 */
	public Date 	deliveryDate;	/* 発送日 */
	public BigDecimal 	productPrice;	/* 品代金 */
	public BigDecimal 	salesMoney;		/* 売上金額 */
}