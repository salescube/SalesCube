/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.porder;

import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.LongType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;

/**
 * 発注検索画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchPOrderForm extends AbstractSearchForm<List<Object>> {

	/**
	 * 伝票番号
	 */
	@LongType
	public String poSlipId;

	/**
	 * 入力担当者名
	 */
	public String userName;

	/**
	 * 発注日（開始）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String poDateFrom;

	/**
	 * 発注日（終了）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String poDateTo;

	/**
	 * 納期（開始）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE, msg = @Msg(key = "errors.date"), arg0 = @Arg(key = "labels.poDeliveryDateFrom", resource = true, position = 0))
	public String deliveryDateFrom;

	/**
	 * 納期（終了）
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE, msg = @Msg(key = "errors.date"), arg0 = @Arg(key = "labels.poDeliveryDateTo", resource = true, position = 0))
	public String deliveryDateTo;

	/**
	 * 摘要
	 */
	@Maxlength(maxlength = 120)
	public String remarks;

	/**
	 * 運送便区分
	 */
	public String transportCategory;

	/**
	 * 発注残のみ
	 */
	public boolean onlyRestQuantityExist;

	/**
	 * 未払いのみ
	 */
	public boolean onlyUnpaid;

	/**
	 * 仕入先コード
	 */
	@Mask(mask = Constants.CODE_MASK.SUPPLIER_MASK, msg = @Msg(key = "errors.invalid"), args = @Arg(key = "labels.supplierCode", resource = true, position = 0))
	public String supplierCode;

	/**
	 * 仕入先名
	 */
	public String supplierName;

	/**
	 * 仕入先担当者名
	 */
	public String supplierPcName;

	/**
	 * 商品コード
	 */
	@Mask(mask = Constants.CODE_MASK.PRODUCT_MASK, msg = @Msg(key = "errors.invalid"), args = @Arg(key = "labels.productCode", resource = true, position = 0))
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
	 * 運送便区分の選択値リスト
	 */
	public List<LabelValueBean> transportCategoryList;

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
