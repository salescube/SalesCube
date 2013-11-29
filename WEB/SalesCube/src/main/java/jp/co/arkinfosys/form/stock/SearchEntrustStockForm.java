/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.stock;

import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntegerType;

/**
 * 委託入出庫検索画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchEntrustStockForm extends AbstractSearchForm<List<Object>> {

	/**
	 * 委託入庫
	 */
	public boolean entrustEadCategoryEnter;

	/**
	 * 委託出庫
	 */
	public boolean entrustEadCategoryDispatch;

	/**
	 * 委託出庫（出庫指示印刷未実施のみ）
	 */
	public boolean entrustEadCategoryDispatchNoPrint;

	/**
	 * 委託入出庫伝票番号
	 */
	@IntegerType
	public String entrustEadSlipId;

	/**
	 * 発注伝票番号
	 */
	@IntegerType
	public String poSlipId;

	/**
	 * 入力担当者ID
	 */
	public String userId;

	/**
	 * 入力担当者名
	 */
	public String userName;

	/**
	 * 委託入出庫日（開始）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String entrustEadDateFrom;

	/**
	 * 委託入出庫日（終了）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String entrustEadDateTo;

	/**
	 * 理由
	 */
	public String remarks;

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
	 * 発注入力画面に遷移する権限があるか否か
	 */
	public boolean isInputPOrderValid = false;

}
