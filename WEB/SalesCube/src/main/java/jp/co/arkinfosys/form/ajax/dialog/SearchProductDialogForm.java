/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.master.ProductDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 商品検索ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchProductDialogForm extends AbstractSearchForm<ProductDto> {

	public String dialogId;

	public String productCode;

	public String supplierPcode;

	public String janPcode;

	public String setTypeCategory;

	public String productStandardCategory;

	public String productStatusCategory;

	public String productName;

	public String productKana;

	public String supplierCode;

	public String supplierName;

	/**
	 * セット分類リスト
	 */
	public List<LabelValueBean> setTypeCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 標準化分類リスト
	 */
	public List<LabelValueBean> standardCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 廃番品分類リスト
	 */
	public List<LabelValueBean> statusCategoryList = new ArrayList<LabelValueBean>();

}
