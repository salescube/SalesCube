/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.bill;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntegerType;

/**
 * 請求検索画面用のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class SearchBillForm extends AbstractSearchForm<BeanMap> {

    /**
     * 請求書番号
     */
	@IntegerType
    public String billId;

    /**
     * 請求書発行日（始め）
     */
    @DateType(datePatternStrict = "yyyy/MM/dd",arg0=@Arg(key="labels.billPrintDateFrom"))
    public String lastPrintDateFrom;

    /**
     * 請求書発行日（終わり）
     */
    @DateType(datePatternStrict = "yyyy/MM/dd",arg0=@Arg(key="labels.billPrintDateTo"))
    public String lastPrintDateTo;


    /**
     * 請求締日（始め）
     */
    @DateType(datePatternStrict = "yyyy/MM/dd",arg0=@Arg(key="labels.billCutOffDateFrom"))
    public String billCutoffDateFrom;

    /**
     * 請求締日（終わり）
     */
    @DateType(datePatternStrict = "yyyy/MM/dd",arg0=@Arg(key="labels.billCutOffDateTo"))
    public String billCutoffDateTo;

    /**
     * 最終売上日（始め）
     */
    @DateType(datePatternStrict = "yyyy/MM/dd",arg0=@Arg(key="labels.laseSalesDateFrom"))
    public String lastSalesDateFrom;

    /**
     * 最終売上日（終わり）
     */
    @DateType(datePatternStrict = "yyyy/MM/dd",arg0=@Arg(key="labels.laseSalesDateTo"))
    public String lastSalesDateTo;


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

	// 締め解除種別
	public String doa;

	// 請求書分類リストの内容
	public List<LabelValueBean> billCrtCategoryList = new ArrayList<LabelValueBean>();

	// 支払条件リストの内容
	public List<LabelValueBean> cutoffGroupCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 入金入力画面の権限有無
	 */
	public boolean isInputDepositValid;

}
