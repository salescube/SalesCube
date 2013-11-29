/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.sales;

import java.util.List;

import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.LongType;

/**
 *
 * 売上検索画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchSalesForm extends AbstractSearchForm<List<Object>> {

    /**
     * 売上番号
     */
    @LongType
    public String salesSlipId;

    /**
     * 受注番号
     */
    @LongType
    public String roSlipId;

    /**
     * 受付番号
     */
    public String receptNo;

    /**
     * 売上日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String salesDateFrom;

    /**
     * 売上日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String salesDateTo;

    /**
     * 納期指定日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String deliveryDateFrom;

    /**
     * 納期指定日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String deliveryDateTo;

    /**
     * 配送業者名
     */
    public String dcCategory;

	/**
	 * 配送時間帯
	 */
    public String dcTimezoneCategory;

	/**
	 * ピッキング備考
	 */
    public String pickingRemarks;

	/**
	 * 顧客コード
	 */
    public String customerCode;

    /**
     * 顧客名
     */
    public String customerName;

    /**
     * 顧客担当者
     */
    public String customerPcName;

    /**
     * 商品コード
     */
    public String productCode;

    /**
     * 商品名
     */
    public String productAbstract;

	/**
	 * 分類(大)
	 */
    public String product1;

	/**
	 * 分類(中)
	 */
    public String product2;

	/**
	 * 分類(小)
	 */
    public String product3;

    /**
     * 仕入先コード
     */
    public String supplierCode;

    /**
     * 仕入先名
     */
    public String supplierName;

	/**
	 * 受注入力画面に遷移する権限があるか否か
	 */
	public boolean isInputROrderValid = false;

    /**
     * 配送業者リスト
     */
    public List<LabelValueBean> dcCategoryList;

    /**
     * 配送時間帯リスト
     */
    public List<LabelValueBean> dcTimezoneCategoryList;

    /**
     * 分類（大）の選択値リスト
     */
    public List<LabelValueBean> product1List;

    /**
     * 分類（中）の選択値リスト
     */
    public List<LabelValueBean> product2List;

    /**
     * 分類（小）の選択値リスト
     */
    public List<LabelValueBean> product3List;

	/**
	 * 売上取引区分リスト初期化(チェックボックス)
	 */
	public List<LabelValueBean> salesCmCategoryList;

}
