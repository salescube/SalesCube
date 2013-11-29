/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;
/**
 * 発注伝票と発注伝票明細と区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class POrderSlipLineJoin implements Serializable{

	private static final long serialVersionUID = 1L;

	//追加分

	//支払状況
	public String paymentStatus;
	//伝票税抜き合計金額
	public BigDecimal purePriceTotal;

	//伝票（DB対応あり）

	//発注番号
	public Integer poSlipId;

	// ステータス
	public String status;

	//発注日
	public Date poDate;

	//納期
	public Date deliveryDate;

	//入力担当者
	public String userId;
	public String userName;

	//備考
	public String remarks;

	//仕入先コード
	public String supplierCode;
	//仕入先名
	public String supplierName;

	//運送便区分
	public String transportCategory;
	public String transportCategoryString;

	//伝票合計
	public BigDecimal priceTotal;
	//消費税（伝票）
	public BigDecimal ctaxTotal;
	//外貨伝票合計
	public BigDecimal fePriceTotal;

	//発行済み伝票数
	public Integer printCount;

	//** 明細行（DB対応あり) */

	//状態（明細行）
	public String lineStatus;

	//伝票明細行番号
	public Short lineNo;

	//商品コード
	public String productCode;
	//商品名
	public String productAbstract;
	//数量
	public BigDecimal quantity;

	//円単価
	public BigDecimal unitPrice;
	//円金額
	public BigDecimal price;

	//外貨単価
	public BigDecimal dolUnitPrice;
	//外貨金額
	public BigDecimal dolPrice;

	//納期（明細行）
	public Date lineDeliveryDate;

	//備考（明細行）
	public String lineRemarks;

	//発注残数
	public BigDecimal restQuantity;

}
