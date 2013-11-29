/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 仕入伝票と仕入伝票明細行のリレーションエンティティクラスです.
 * @author Ark Information Systems
 *
 */
@Entity
public class SupplierSlipLineJoin  implements Serializable{
	private static final long serialVersionUID = 1L;

	/*
	 *  ※以下のテーブルを結合
	 *  SUPPLIER_SLIP_TRN	仕入伝票
	 *  SUPPLIER_LINE_TRN	仕入伝票明細行
	 */
	public Integer supplierSlipId;			/* 仕入伝票明細行.仕入伝票番号 */
	public Integer supplierLineId;			/* 仕入伝票明細行.仕入伝票行ID */
	public String supplierDetailCategory;	/* 仕入伝票明細行.仕入明細区分 */
	public BigDecimal quantity;				/* 仕入伝票明細行.数量 */
	public BigDecimal price;				/* 仕入伝票明細行.金額 */
	public BigDecimal unitPrice;			/* 仕入伝票明細行.単価 */
	public BigDecimal dolUnitPrice;			/* 仕入伝票明細行.ドル単価 */
	public BigDecimal dolPrice;				/* 仕入伝票明細行.ドル金額 */
	public Integer poLineId;				/* 仕入伝票明細行.発注伝票行ID */
	public Date supplierDate;				/* 仕入伝票.仕入日 */
	public String supplierCode;				/* 仕入伝票.仕入先コード */
}

