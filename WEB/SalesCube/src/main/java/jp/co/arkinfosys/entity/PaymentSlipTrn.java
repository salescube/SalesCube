/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 支払伝票エンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PaymentSlipTrn implements Serializable{
	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "PAYMENT_SLIP_TRN";

	// 支払伝票テーブル情報(PAYMENT_SLIP_TRN)
	@Id
	public Integer paymentSlipId;/* 支払伝票番号 */
	public String status;
	public Date paymentDate;/* 支払日 */
	public Short paymentAnnual;
	public Short paymentMonthly;
	public Integer paymentYm;
	public String userId;
	public String userName;/* 担当者名 */
	public String paymentSlipCategory;
	public String supplierCode;/* 仕入先コード */
	public String supplierName;/* 仕入先名称 */
	public Integer rateId;/* レートタイプ */
	public String taxShiftCategory;/* 税転嫁 */
	public String taxFractCategory;/* 税端数処理 */
	public String priceFractCategory;/* 単価端数処理*/
	public BigDecimal priceTotal;/* 伝票合計金額 */
	public BigDecimal fePriceTotal;/* 伝票合計外貨金額 */
	public Integer poSlipId;/* 発注伝票番号 */
	public Integer supplierSlipId;
	public Date paymentCutoffDate;
	public Date paymentPdate;
	public String remarks;/* 備考 */
	public String creFunc;
	public Timestamp creDatetm;
	public String creUser;
	public String updFunc;
	public Timestamp updDatetm;/* 更新日時 */
	public String updUser;

}
