/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.purchase;

import java.util.List;

import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.LongType;

/**
 *
 * 仕入検索画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchPurchaseForm extends AbstractSearchForm<List<Object>> {

	/**
	 * 仕入番号
	 */
	@LongType
	public String supplierSlipId;

	/**
	 * 発注番号
	 */
	@LongType
	public String poSlipId;

	/**
	 * 入力担当者
	 */
	public String userName;

	/**
	 * 仕入日From
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String supplierDateFrom;

	/**
	 * 仕入日To
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String supplierDateTo;

	/**
	 * 完納区分
	 */
	public String[] deliveryProcessCategory;

	/**
	 * 仕入先コード
	 */
	public String supplierCode;

	/**
	 * 仕入先名
	 */
	public String supplierName;

	/**
	 * 備考
	 */
	public String remarks;

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
	 * 発注入力画面に遷移する権限があるか否か
	 */
	public boolean isInputPOrderValid = false;

	/**
	 * 支払入力画面に遷移する権限があるか否か
	 */
	public boolean isInputPaymentValid = false;

	/**
	 * 完納区分リスト
	 */
	public List<LabelValueBean> deliveryProcessCategoryList;

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
