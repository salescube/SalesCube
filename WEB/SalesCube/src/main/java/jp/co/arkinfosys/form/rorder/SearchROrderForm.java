/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.rorder;

import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.LongType;

/**
 *
 * 受注検索画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchROrderForm extends AbstractSearchForm<List<Object>> {

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

	/**
	 * 受注伝票番号
	 */
	@LongType
	public String roSlipId;

	/**
	 * 受付番号
	 */
	public String receptNo;

	/**
	 * 残分のみ
	 */
	public String restOnly;

	/**
	 * 遅延分のみ
	 */
	public String razyOnly;

	/**
	 * 受注日From
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String roDateFrom;
	/**
	 * 受注日To
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String roDateTo;

	/**
	 * 出荷日From
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String shipDateFrom;

	/**
	 * 出荷日To
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String shipDateTo;

	/**
	 * 納期指定日From
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String deliveryDateFrom;
	/**
	 * 納期指定日To
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String deliveryDateTo;

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
	public String deliveryPcName;

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

}
