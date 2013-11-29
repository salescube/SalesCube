/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.porder;

import java.io.Serializable;

/**
 * 発注書発行画面の検索結果リスト行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class POrderSlipLineJoinDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 追加分
	 */
	//支払状況
	public String paymentStatus;

	//伝票税抜き合計金額
	public String purePriceTotal;

	/**
	 * 伝票（DB対応あり）
	 */
	//発注番号
	public String poSlipId;

	public String poSlipIdShow;

	//発注日
	public String poDate;

	//納期
	public String deliveryDate;

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
	public String priceTotal;
	//消費税（伝票）
	public String ctaxTotal;
	//外貨伝票合計
	public String fePriceTotal;

	//発行済数
	public String printCount;

	/**
	 * 明細行（DB対応あり）
	 */

	//状態（明細行）
	/**
	 * 名称変更
	 * public String status;
	 */
	public String lineStatus;

	//伝票明細行番号
	public String lineNo;

	//商品コード
	public String productCode;
	//商品名
	public String productAbstract;
	//数量
	public String quantity;

	//円単価
	public String unitPrice;
	//円金額
	public String price;

	//外貨単価
	public String dolUnitPrice;
	//外貨金額
	public String dolPrice;

	//納期（明細行）
	/**
	 * 名称変更
	 * public Date deliveryDate;
	 */
	public String lineDeliveryDate;

	//備考（明細行）
	/**
	 * 名称変更
	 * public String remarks;
	 */
	public String lineRemarks;

	//発注残数
	public String restQuantity;

}
