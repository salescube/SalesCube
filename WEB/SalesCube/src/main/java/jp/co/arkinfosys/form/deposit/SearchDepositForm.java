/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.deposit;

import java.util.List;

import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.LongType;

/**
 * 入金検索画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchDepositForm extends AbstractSearchForm<List<Object>> {

	/**
	 * 入金伝票番号
	 */
	@LongType
	public String depositSlipId;

	/**
	 * 入力担当者コード
	 */
	public String userId;

	/**
	 * 入力担当者名
	 */
	public String userName;

	/**
	 * 入金日From
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String depositDateFrom;

	/**
	 * 入金日To
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String depositDateTo;

	/**
	 * 入力日From
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String inputPdateFrom;

	/**
	 * 入力日To
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String inputPdateTo;

	/**
	 * 回収金額From
	 */
	@IntegerType
	public String depositTotalFrom;

	/**
	 * 回収金額To
	 */
	@IntegerType
	public String depositTotalTo;

	/**
	 * 摘要
	 */
	public String depositAbstract;

	/**
	 * 顧客コード
	 */
	public String customerCode;

	/**
	 * 顧客名
	 */
	public String customerName;

	/**
	 * 振込名義
	 */
	public String paymentName;

	/**
	 * 入金取込区分
	 */
	public String depositMethodTypeCategory;

	/**
	 * 入金区分
	 */
	public String[] depositCategory;

	/**
	 * 合計回収金額
	 */
	public long depositTotal;

	/**
	 * 入金取込区分リスト
	 */
	public List<LabelValueBean> depositMethodTypeCategoryList;

	/**
	 * 入金区分リスト
	 */
	public List<LabelValueBean> depositCategoryList;

}
