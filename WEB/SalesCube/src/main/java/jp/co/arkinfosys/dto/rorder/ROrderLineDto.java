/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.rorder;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateLineDto;

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

	/**
	 * 見積伝票をもとに初期化処理を行います
	 * @param elDto 見積伝票明細行情報
	 */
	public void initialize( InputEstimateLineDto elDto ) {
		// ID
		roLineId = "";
		roSlipId = "";

		// 見積伝票明細から設定する項目
		productCode = elDto.productCode;		// 商品コード
		customerPcode = elDto.customerPcode;	// 得意先商品コード
		productAbstract = elDto.productAbstract;// 商品名
		quantity = elDto.quantity;				// 数量
		quantityDB = elDto.quantity;			// 数量(hidden)
		restQuantity = elDto.quantity;			// 未納数
		restQuantityDB = elDto.quantity;		// 未納数(hidden)
		unitCost = elDto.unitCost;				// 仕入単価
		cost = elDto.cost;						// 仕入金額
		unitRetailPrice = elDto.unitRetailPrice;// 売上単価
		retailPrice = elDto.retailPrice;		// 売上金額
		remarks = elDto.remarks;				// 備考
		deletable = true;
		estimateLineId = elDto.estimateLineId;	// 見積伝票明細行

		// 見積伝票明細からは設定できない項目
		eadRemarks = "";
		productRemarks = "";
		supplierPcode = "";
		taxCategory = "";
		ctaxRate = "";
		ctaxPrice = "";
		roItemId = "";
		lastShipDate = "";
		stockCtlCategory = "";
		possibleDrawQuantity = "";	// 引当可能数は最新の情報を設定するのでコピーしない
		roMaxNum = "";				// 受注限度数は最新の情報を設定するのでコピーしない

		// 売上入力画面使用項目
		unitCategory = "";	// 単位コード
		unitName = "";		// 単位名
		packQuantity = "";	// 入数
		unitPrice = "";		// 単価
	}

}
