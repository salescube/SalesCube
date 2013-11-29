/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.bill;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Msg;

/**
 * 請求書発行画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class MakeOutBillForm extends
		AbstractSearchForm<MakeOutBillSearchResultDto> {

	/**
	 * 請求書番号
	 */
	@IntegerType(msg = @Msg(key = "errors.notExist"), arg0 = @Arg(key = "labels.billId"))
	public String billId;

	/**
	 * 請求締日（始め）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE, arg0 = @Arg(key = "labels.billCutOffDateFrom"))
	public String billCutoffDateFrom;

	/**
	 * 請求締日（終わり）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE, arg0 = @Arg(key = "labels.billCutOffDateTo"))
	public String billCutoffDateTo;

	/**
	 * 請求書分類
	 */
	public String billCrtCategory;

	/**
	 * 顧客コード（得意先コード）
	 */
	public String customerCode;

	/**
	 * 顧客名（得意先名）
	 */
	public String customerName;

	/**
	 * 支払条件
	 */
	public String cutoffGroupCategory;

	/**
	 * 請求書種別
	 */
	public String billCategory;

	/**
	 * 発行済は除く
	 */
	public boolean excludePrint;

	/**
	 * 繰越金額　なし
	 */
	public String covPriceZero;
	/**
	 * 繰越金額　過入金（請求側から見て不足していれば請求額プラス、余分なら請求額マイナス）
	 */
	public String covPriceMinus;

	/**
	 * 繰越金額　不足（請求側から見て不足していれば請求額プラス、余分なら請求額マイナス）
	 */
	public String covPricePlus;

	/**
	 * 今回請求額　あり（請求側から見て不足していれば請求額プラス、余分なら請求額マイナス）
	 */
	public String thisBillPricePlus;

	/**
	 * 今回請求額　なし
	 */
	public String thisBillPriceZero;

	/**
	 * 今回請求額　過入金（請求側から見て不足していれば請求額プラス、余分なら請求額マイナス）
	 */
	public String thisBillPriceMinus;

	/**
	 * 支払条件リスト
	 */
	public List<LabelValueBean> cutoffGroupCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 全ての検索結果リスト
	 */
	public List<MakeOutBillSearchResultDto> allSearchResultList = null;

}
