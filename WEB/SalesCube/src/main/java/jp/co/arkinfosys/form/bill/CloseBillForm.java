/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.bill.CloseBillLineDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 請求締処理画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CloseBillForm extends AbstractSearchForm<CloseBillLineDto> {

	// 締処理日
	public String cutOffDate;

	// 支払条件
	public String cutoffGroupCategory;

	// 締日グループ
	public String cutoffGroup;

	// 回収間隔
	public String paybackCycleCategory;

	// 請求漏れチェック
	public Boolean notYetRequestedCheck;

	// 顧客コード
	public String customerCode;

	// 顧客名
	public String customerName;

	// 売掛以外の全ユーザ
	public boolean closeCheck;

	public String lastCutOffDate;

	public CloseBillLineDto otherUser = new CloseBillLineDto();

	// 支払条件リストの内容
	public List<LabelValueBean> cutoffGroupCategoryList = new ArrayList<LabelValueBean>();

	// 区分作成フラグ
	public boolean initCategory;

	// 明細行のタブ移動可能項目数
	public int lineElementCount = 1;

	/**
	 * アクションフォームの初期化を行います.
	 */
	public void reset() {
		cutoffGroupCategory = "";
		cutoffGroup = "";
		paybackCycleCategory = "";
		customerCode = "";
		customerName = "";

		closeCheck = false;
		lastCutOffDate = "";

		SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);
		cutOffDate = DF_YMD.format(new Date());
	}
}
