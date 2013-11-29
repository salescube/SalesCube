/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.bill;

/**
 * 請求書発行画面の検索結果リスト行のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class MakeOutBillSearchResultDto {

	// 請求書番号
	public String billId;

	// 実行チェック
	public boolean printCheck;

	// 最終発行日
	public String lastPrintDate;

	// 月次請求書発行数
	public String billPrintCount;

	// 繰越金額
	public String covPrice;

	// 売上金額（税抜き）
	public String salesPrice;

	// 消費税
	public String ctaxPrice;

	// 売上金額（税込）
	public String taxInPrice;

	// 今回請求額
	public String thisBillPrice;

	// 顧客コード
	public String customerCode;

	// 顧客名
	public String customerName;

	// 請求先名
	public String deliveryName;

	// 個別印刷数
	public String singlePrintCount;

	// 請求書種別
	public String billCategory;

	// 締め解除種別
	public String doa;

}
