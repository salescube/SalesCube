/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.rorder;

import jp.co.arkinfosys.dto.AbstractLineDto;

/**
 * 受注伝票明細行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ROrderLineDto extends AbstractLineDto {

	/** 受注伝票行ＩＤ */
	public String roLineId;

	/** 受注伝票番号 */
	public String roSlipId;

	/** 商品コード */
	public String productCode;

	/** 得意先商品コード */
	public String customerPcode;

	/** 商品名 */
	public String productAbstract;

	/** 棚番 */
	public String rackCodeSrc;

	/** 数量 */
	public String quantity;
	/** 数量DB保存用 */
	public String quantityDB;

	/** 受注残数 */
	public String restQuantity;
	/** 受注残数DB保存用 */
	public String restQuantityDB;

	/** 完納区分 */
	public String status;
	public String statusName;

	/** 仕入単価 */
	public String unitCost;

	/** 仕入金額 */
	public String cost;

	/** 売上単価 */
	public String unitRetailPrice;

	/** 売上金額 */
	public String retailPrice;

	/** 備考 */
	public String remarks;

	/** ピッキング備考 */
	public String eadRemarks;

	/** 商品備考*/
	public String productRemarks;

	/** 相手商品コード */
	public String supplierPcode;

	/** 受注限度数 */
	public String roMaxNum;

	/** 引当可能数 */
	public String possibleDrawQuantity;

	/** 課税区分 */
	public String taxCategory;

	/** 消費税率 */
	public String ctaxRate;

	/** 消費税合計 */
	public String ctaxPrice;

	/** オンライン品番 */
	public String roItemId;

	/** 削除可能フラグ */
	public boolean deletable = true;

	/** 最終出荷日 */
	public String lastShipDate;

	/** 在庫管理区分 **/
	public String stockCtlCategory;

	public String estimateLineId;
	public String unitCategory;
	public String unitName;
	public String packQuantity;
	public String unitPrice;

	/**
	 * 商品コードがnull又は空白かどうか検査します.
	 * @return true:商品コーがnullまたは空白　false:商品コードはnullでも空白でもない
	 */
	@Override
	public boolean isBlank() {
		return (productCode == null || productCode.length() == 0);
	}
}
