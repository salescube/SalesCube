/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.sales;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.entity.SalesLineTrn;

/**
 * 売上伝票明細行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SalesLineDto extends AbstractLineDto {

	/** 売上伝票行ID */
	public String salesLineId;

	/** 状態フラグ */
	public String status;

	/** 売上伝票番号 */
	public String salesSlipId;

	/** 上代金額 */
	public String retailPrice;

	/** 粗利益 */
	public String gm;

	/** 課税区分 */
	public String taxCategory;

	/** 消費税率 */
	public String ctaxRate;

	/** 数量 */
	public String quantity;

	/** 変更前数量 */
	public String bkQuantity;

	/** 受注伝票行ID */
	public String roLineId;

	/** 受注数量 */
	public String roQuantity;

	/** 商品コード */
	public String productCode;

	/** 商品名・摘要 */
	public String productAbstract;

	/** 引当可能数 */
	public String possibleDrawQuantity;

	/** 受注限度数 */
	public String roMaxNum;

	/** 在庫管理フラグ */
	public String stockCtlCategory;

	/** 得意先商品コード */
	public String customerPcode;

	/** 単位コード */
	public String unitCategory;

	/** 単位名 */
	public String unitName;

	/** 入数 */
	public String packQuantity;

	/** 完納区分 */
	public String deliveryProcessCategory;

	/** 消費税 */
	public String ctaxPrice;

	/** 上代単価 */
	public String unitRetailPrice;

	/** 原価金額 */
	public String cost;

	/** 原価単価 */
	public String unitCost;

	/** セット商品フラグ */
	public String setTypeCategory;

	/** 出荷元棚番コード */
	public String rackCodeSrc;

	/** 売上明細区分 */
	public String salesDetailCategory;
	/** 単価 */
	public String unitPrice;
	/** 備考 */
	public String remarks;
	/** ピッキング備考 */
	public String eadRemarks;
	/** 商品備考 */
	public String productRemarks;
	/** 作成機能 */
	public String creFunc;
	/** 作成日時 */
	public String creDatetm;
	/** 作成者 */
	public String creUser;
	/** 更新機能 */
	public String updFunc;
	/** 更新日時 */
	public String updDatetm;
	/** 更新者 */
	public String updUser;
	public String pickingLineId;
	public String pickingListId;
	public String pickingLineNo;

	/**
	 * 商品コードがnull又は空白かどうか検査します.
	 * @return true:商品コーがnullまたは空白　false:商品コードはnullでも空白でもない
	 */
	@Override
	public boolean isBlank() {
		return this.productCode == null || this.productCode.length() == 0;
	}

	/**
	 * 登録時の初期化処理を行います.
	 * @param newLineId 新たな伝票行番号
	 * @param newSlipId 新たな伝票番号
	 */
	public void initForDB(Long newLineId, String newSlipId ) {
		this.salesLineId = newLineId.toString();
		this.salesSlipId = newSlipId;
		this.status = SalesLineTrn.STATUS_INIT;
	}

	/**
	 * 受注伝票をもとに初期化処理を行います
	 * @param rolDto 受注伝票明細行情報
	 */
	public void initialize( ROrderLineDto rolDto ) {
		salesLineId = "";						// H 売上伝票行ID
		status = SalesLineTrn.STATUS_INIT;		// H 状態フラグ　
		salesSlipId = "";						// H 売上伝票番号
		lineNo = rolDto.lineNo;			// H 売上伝票行番
		roLineId = rolDto.roLineId;				// H 受注伝票行ID
		salesDetailCategory = "";				// H 売上明細区分
		productCode = rolDto.productCode;		// T 商品コード
		productAbstract = rolDto.productAbstract;// H 商品名・摘要
		quantity = rolDto.restQuantity;			// T 残数量
												// L 完納区分
		deliveryProcessCategory = CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL;
		unitRetailPrice = rolDto.unitRetailPrice;	// T 上代単価
		retailPrice = rolDto.retailPrice;	// T 上代金額
		unitCost = rolDto.unitCost;			// T 原価単価
		cost = rolDto.cost;					// T 原価金額
		taxCategory = rolDto.taxCategory;	// H 課税区分
		ctaxRate = rolDto.ctaxRate;			// H 消費税率
		ctaxPrice = rolDto.ctaxPrice;		// H 消費税
		gm = "";							// H 粗利益
		remarks = rolDto.remarks;			// T 備考
		eadRemarks = rolDto.eadRemarks;		// T ピッキング備考
		productRemarks = rolDto.productRemarks;		// T 商品備考
		rackCodeSrc = rolDto.rackCodeSrc;	// T 出荷元棚番コード
		creFunc = "";						// 作成機能
		creDatetm = "";						// 作成日時
		creUser = "";						// 作成者
		updFunc = "";						// 更新機能
		updDatetm = "";						// 更新日時
		updUser = "";						// 更新者

		roQuantity = rolDto.restQuantity;		// H 受注数量保持
		bkQuantity = quantity.replaceAll(",", "");				// H 変更前数量保持
	}

}
