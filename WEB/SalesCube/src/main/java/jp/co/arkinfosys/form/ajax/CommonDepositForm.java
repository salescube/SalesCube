/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax;


/**
 * 納入先情報と入金情報を保持するアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class CommonDepositForm extends AbstractDeliveryForm  {

	// 編集中の伝票情報
	public String depositSlipId;			// 伝票番号

	// 締日グループ
	public String cutoffGroup;

	// 回収間隔
	public String paybackCycleCategory;

	// 税転嫁
	public String taxShiftCategory;

	// 受注停止
	public String customerRoCategory;

	// 集計情報
	public String lastBillingPrice;			// 前回請求額
	public String nowPaybackPrice;			// 今回回収額
	public String nowSalesPrice;			// 今回売上高

	public String taxFractCategory;		// 税端数処理　顧客マスタ
	public String priceFractCategory;	// 単価端数処理　顧客マスタ

}
