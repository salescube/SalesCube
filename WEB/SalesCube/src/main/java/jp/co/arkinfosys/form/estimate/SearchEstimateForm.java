/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.estimate;

import java.util.List;

import jp.co.arkinfosys.form.AbstractSearchForm;

import org.seasar.struts.annotation.DateType;

/**
 * 見積検索画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class SearchEstimateForm extends AbstractSearchForm<List<Object>> {

	/**
	 * 見積番号
	 */
	public String estimateSheetId;

	/**
	 * 見積日（始め）
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String estimateDateFrom;

	/**
	 * 見積日（終わり）
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String estimateDateTo;

	/**
	 * 有効期限（始め）
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String validDateFrom;

	/**
	 * 有効期限（終わり）
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String validDateTo;

	/**
	 * 入力担当者ID
	 */
	public String userId;

	/**
	 * 入力担当者
	 */
	public String userName;

	/**
	 * 件名
	 */
	public String title;

	/**
	 * 摘要（備考）
	 */
	public String remarks;

	/**
	 * 提出先名
	 */
	public String submitName;

	/**
	 * 顧客コード（得意先コード）
	 */
	public String customerCode;

	/**
	 * 顧客名（得意先名）
	 */
	public String customerName;

}
