/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.bill;

import java.text.SimpleDateFormat;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.join.BillJoin;

import org.seasar.struts.util.MessageResourcesUtil;


/**
 * 請求締処理画面の検索結果リスト行のDTOクラスです.
 * @author Ark Information Systems
 *
 */
public class CloseBillLineDto {

	// 実行チェック
	public boolean closeCheck;

	// 顧客コード
	public String customerCode;

	// 顧客名
	public String customerName;

	// 前回請求締日
	public String billCutoffDate;

	static private SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);

	/**
	 * 初期化します.
	 * @param bill 請求書情報
	 */
	public void initialize( BillJoin bill ) {
		this.closeCheck = false;
		this.customerCode = bill.customerCode;
		this.customerName = bill.customerName;
		if( bill.billCutoffDate == null ){
			this.billCutoffDate = MessageResourcesUtil.getMessage("labels.billNoData");
		}else{
			this.billCutoffDate = sdf.format(bill.billCutoffDate);
		}
	}
	/**
	 * 初期化します.
	 */
	public void initialize() {
		this.closeCheck = false;
		this.customerCode = "";
		this.customerName = "";
		this.billCutoffDate = "";
	}

	/**
	 * 初期化します.
	 * @param　customer　顧客情報
	 */
	public void initialize( Customer customer ) {
		this.closeCheck = false;
		this.customerCode = customer.customerCode;
		this.customerName = customer.customerName;
		if( customer.lastCutoffDate == null ){
			this.billCutoffDate = MessageResourcesUtil.getMessage("labels.billNoData");
		}else{
			this.billCutoffDate = sdf.format(customer.lastCutoffDate);
		}
	}
}
