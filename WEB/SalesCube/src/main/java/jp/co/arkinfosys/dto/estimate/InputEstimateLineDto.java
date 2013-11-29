/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.estimate;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractLineDto;

/**
 * 見積伝票明細行のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputEstimateLineDto extends AbstractLineDto {

	private static final long serialVersionUID = 1L;

	/** 見積伝票行ID */
	public String estimateLineId;
	/** 見積伝票番号 */
	public String estimateSheetId;

	/** 商品コード */
	public String productCode;
	/** 相手商品コード */
	public String customerPcode;

	/** 商品・摘要 */
	public String productAbstract;

	/** 数量 */
	public String quantity;

	/** 仕入単価（原単価） */
	public String unitCost;
	/** 売上単価 */
	public String unitRetailPrice;

	/** 仕入金額（原価金額） */
	public String cost;
	/** 売価金額 */
	public String retailPrice;

	/** 備考 */
	public String remarks;

	public String creFunc;
	public String creDatetm;
	public String creUser;
	public String updFunc;
	public String updDatetm;
	public String updUser;

	/** 受注限度数 */
	public String roMaxNum;

	/** 引当可能数 */
	public String possibleDrawQuantity;

	/**
	 * 明細行が空行かどうか判定します.
	 * @return true:空行　false:空行でない
	 */
	public boolean isEmpty() {
		if (StringUtil.hasLength(estimateLineId)
				|| StringUtil.hasLength(estimateSheetId)
				|| StringUtil.hasLength(customerPcode)
				|| StringUtil.hasLength(productCode)
				|| StringUtil.hasLength(productAbstract)
				|| StringUtil.hasLength(quantity)
				|| StringUtil.hasLength(unitCost)
				|| StringUtil.hasLength(unitRetailPrice)
				|| StringUtil.hasLength(cost)
				|| StringUtil.hasLength(retailPrice)
				|| StringUtil.hasLength(remarks)
				|| StringUtil.hasLength(creFunc)) {
			return false;
		}
		return true;
	}
	/** 在庫管理区分 **/
	public String stockCtlCategory;

	/**
	 * 商品コードがnull又は空白かどうか調べます.
	 * @return　true：null又は空 false:nullでも空白でもない
	 */
	@Override
	public boolean isBlank() {
		return (this.productCode == null || this.productCode.length() == 0);
	}
}
