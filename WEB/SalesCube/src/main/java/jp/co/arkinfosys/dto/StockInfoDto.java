/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

import java.io.Serializable;
import java.text.DecimalFormat;

import jp.co.arkinfosys.common.NumberUtil;

/**
 * 在庫数情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class StockInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private DecimalFormat commaQuantityFormat = NumberUtil.createDecimalFormat(0, true);

	/**
	 * 商品コード
	 */
	public String productCode;

	/**
	 * 仕入先商品コード
	 */
	public String supplierPcode;

	/**
	 * セット分類コード
	 */
	public String setTypeCategory;

	/**
	 * セット分類名
	 */
	public String setTypeCategoryName;

	/**
	 * 在庫管理区分
	 */
	public String stockCtlCategory;

	/**
	 * 商品名
	 */
	public String productName;

	/**
	 * 倉庫名
	 */
	public String warehouseName;

	/**
	 * 棚番
	 */
	public String rackCode;

	/**
	 * 分類状況
	 */
	public String productStatusCategory;

	/**
	 * 分類状況名
	 */
	public String productStatusCategoryName;

	/**
	 * 分類保管
	 */
	public String productStockCategory;

	/**
	 * 分類保管名
	 */
	public String productStockCategoryName;

	/**
	 * 月平均出荷数
	 */
	public Integer avgShipCount;

	/**
	 * 現在庫総数
	 */
	public int currentTotalQuantity = 0;

	/**
	 * 委託在庫数
	 */
	public int entrustStockQuantity = 0;

	/**
	 * 保有数
	 */
	public int holdingStockQuantity = 0;

	/**
	 * 引当可能数
	 */
	public int possibleDrawQuantity = 0;

	/**
	 * 受注残数
	 */
	public int rorderRestQuantity = 0;

	/**
	 * 発注残数
	 */
	public int porderRestQuantity = 0;

	/**
	 * 船便発注残数
	 */
	public int porderRestQuantityShip = 0;

	/**
	 * AIR便発注残数
	 */
	public int porderRestQuantityAir = 0;

	/**
	 * 宅配便発注残数
	 */
	public int porderRestQuantityDerivary = 0;

	/**
	 * 委託残数
	 */
	public int entrustRestQuantity = 0;

	/**
	 * カンマ付きフォーマットの月平均出荷数を取得します.
	 * @return カンマ付きフォーマットの月平均出荷数
	 */
	public String getFormattedAvgShipCount() {
		if (this.avgShipCount==null) {
			return "";
		}
		return this.formatQuantity(this.avgShipCount.intValue());
	}

	/**
	 * カンマ付きフォーマットの現在庫総数を取得します.
	 * @return   カンマ付きフォーマットの現在庫総数
	 */
	public String getFormattedCurrentTotalQuantity() {
		return this.formatQuantity(this.currentTotalQuantity);
	}

	/**
	 * カンマ付きフォーマットの引当可能数を取得します.
	 * @return  カンマ付きフォーマットの引当可能数
	 */
	public String getFormattedPossibleDrawQuantity() {
		return this.formatQuantity(this.possibleDrawQuantity);
	}

	/**
	 * カンマ付きフォーマットの受注残数を取得します.
	 * @return カンマ付きフォーマットの受注残数
	 */
	public String getFormattedRorderRestQuantity() {
		return this.formatQuantity(this.rorderRestQuantity);
	}

	/**
	 * カンマ付きフォーマットの発注残数を取得します.
	 * @return カンマ付きフォーマットの発注残数
	 */
	public String getFormattedPorderRestQuantity() {
		return this.formatQuantity(this.porderRestQuantity);
	}

	/**
	 * カンマ付きフォーマットの船便発注残数を取得します.
	 * @return カンマ付きフォーマットの船便発注残数
	 */
	public String getFormattedPorderRestQuantityShip() {
		return this.formatQuantity(this.porderRestQuantityShip);
	}

	/**
	 * カンマ付きフォーマットのAIR便発注残数を取得します.
	 * @return カンマ付きフォーマットのAIR便発注残数
	 */
	public String getFormattedPorderRestQuantityAir() {
		return this.formatQuantity(this.porderRestQuantityAir);
	}

	/**
	 * カンマ付きフォーマットの宅配便発注残数を取得します.
	 * @return カンマ付きフォーマットの宅配便発注残数
	 */
	public String getFormattedPorderRestQuantityDerivary() {
		return this.formatQuantity(this.porderRestQuantityDerivary);
	}

	/**
	 * カンマ付きフォーマットの委託残数を取得します.
	 * @return カンマ付きフォーマットの委託残数
	 */
	public String getFormattedEntrustRestQuantity() {
		return this.formatQuantity(this.entrustRestQuantity);
	}

	/**
	 * 数値をカンマ付きフォーマットに変換します.
	 * @param quantity 数値
	 * @return カンマ付きフォーマットの数値
	 */
	private String formatQuantity(int quantity) {
		return this.commaQuantityFormat.format(quantity);
	}

	/**
	 * 数量のフォーマットオプションを設定します.
	 * @param fractCategory 端数処理コード
	 * @param alignment　小数桁数
	 */
	public void setQuantityFormatOptions(String fractCategory, int alignment) {
		this.commaQuantityFormat = NumberUtil.createDecimalFormat(fractCategory, alignment, true);
	}
}
