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
 * 入出庫検索画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchStockForm extends AbstractSearchForm<List<Object>> {

	/**
	 * 入出庫
	 */
	public boolean srcFuncStock;

	/**
	 * 在庫移動
	 */
	public boolean srcFuncStockTransfer;

	/**
	 * 売上
	 */
	public boolean srcFuncSales;

	/**
	 * 仕入
	 */
	public boolean srcFuncPurchase;

	/**
	 * 登録元伝票番号
	 */
	@IntegerType
	public String srcSlipId;

	/**
	 * 入出庫伝票区分
	 */
	public String eadSlipCategory;

	/**
	 * 入力担当者ID
	 */
	public String userId;

	/**
	 * 入力担当者名
	 */
	public String userName;

	/**
	 * 入出庫日（開始）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String eadDateFrom;

	/**
	 * 入出庫日（終了）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String eadDateTo;

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
	 * 棚番
	 */
	public String rackCode;

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
     * 入出庫伝票区分の選択値リスト
     */
    public List<LabelValueBean> slipCategoryList;

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
