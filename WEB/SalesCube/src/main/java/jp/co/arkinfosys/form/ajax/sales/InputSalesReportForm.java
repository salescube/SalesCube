/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.sales;

/**
 *　売上帳票印刷実行のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class InputSalesReportForm {
	/**
	 * 	売上伝票番号
	 */
	public String printId;

	/**
	 * 帳票出力フラグ
	 */
	public boolean typeA;	// 見積書
	public boolean typeG;	// 請求書
	public boolean typeH;	// 請求書
	public boolean typeD;	// 納品書
	public boolean typeC;	// 納品書
	public boolean typeE;	// 仮納品書
	public boolean typeJ;	// ピッキングリスト
	public boolean typeF;	// 納品書兼領収書

}
