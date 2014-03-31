/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * 支払伝票明細行と仕入伝票と仕入伝票明細行のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PaymentSlipLineJoin  implements Serializable{
	private static final long serialVersionUID = 1L;

	/*
	 *  ※以下のテーブルを結合
	 *  PAYMENT_LINE_TRN	支払伝票明細行
	 *  SUPPLIER_SLIP_TRN	仕入伝票
	 *  SUPPLIER_LINE_TRN	仕入伝票明細行
	 *  */
	public Integer paymentLineId;			/* 支払伝票明細行.支払伝票行ID */
	public Short paymentLineNo;				/* 支払伝票明細行.支払伝票行番 */
	public Integer supplierSlipId;			/* 仕入伝票明細行.仕入伝票番号 */
	public Integer supplierLineId;			/* 仕入伝票明細行.仕入伝票行ID */
	public Short supplierLineNo;			/* 仕入伝票明細行.仕入伝票行番 */
	public String supplierDetailCategory;	/* 仕入伝票明細行.仕入明細区分 */
	public Date supplierDate;				/* 仕入伝票.仕入日 */
	public String productCode;				/* 仕入伝票明細行.商品コード */
	public String productAbstract;			/* 仕入伝票明細行.商品名 */
	public String paymentCategory;			/* 支払伝票明細行.支払区分コード */
	public BigDecimal quantity;				/* 仕入伝票明細行.数量 */
	public BigDecimal rate;					/* 仕入伝票.レート */
	public BigDecimal unitPrice;			/* 支払伝票明細行.単価 */
	public BigDecimal price;				/* 支払伝票明細行.金額 */
	public BigDecimal dolUnitPrice;			/* 支払伝票明細行.ドル単価 */
	public BigDecimal dolPrice;				/* 支払伝票明細行.ドル金額 */
	public String remarks;					/* 支払伝票明細行.備考 */
	public Timestamp supUpdDatetm;			/* 仕入伝票.更新日時 */
	public String updSupId;					/* 仕入伝票.更新ID(更新時に使用) */
	public BigDecimal ctaxRate;				/* 仕入伝票.消費税率 */
	public BigDecimal ctaxPrice;			/* 仕入伝票.消費税額 */
}
