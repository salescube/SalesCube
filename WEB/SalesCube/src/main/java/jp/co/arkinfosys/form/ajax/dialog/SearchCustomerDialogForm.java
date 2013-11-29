/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.master.CustomerDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 顧客検索ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchCustomerDialogForm extends AbstractSearchForm<CustomerDto> {

	public String dialogId;

	public String customerCode;

	public String customerName;

	public String customerKana;

	public String customerRankCategory;

	public String cutoffGroup;

	/**
	 * 顧客ランクリスト
	 */
	public List<LabelValueBean> customerRankCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 締日グループリスト
	 */
	public List<LabelValueBean> cutoffGroupList = new ArrayList<LabelValueBean>();

}
