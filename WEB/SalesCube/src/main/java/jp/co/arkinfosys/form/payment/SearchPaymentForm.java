/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.payment;

import java.util.List;

import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.LongType;

/**
 * 支払検索画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchPaymentForm extends AbstractSearchForm<List<Object>> {

	/**
	 * 支払番号
	 */
	@LongType
	public String paymentSlipId;

	/**
	 * 発注番号
	 */
	@LongType
	public String poSlipId;

	/**
	 * 仕入番号
	 */
	@LongType
	public String supplierSlipId;

	/**
	 * 支払日From
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String paymentDateFrom;

	/**
	 * 支払日To
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String paymentDateTo;

	/**
	 * 仕入先コード
	 */
	public String supplierCode;

	/**
	 * 仕入先名
	 */
	public String supplierName;
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
	 * 発注入力画面の表示権限
	 */
	public boolean isInputPOrderValid;

	/**
	 * 仕入入力画面の表示権限
	 */
	public boolean isInputPurchaseValid;


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

}
