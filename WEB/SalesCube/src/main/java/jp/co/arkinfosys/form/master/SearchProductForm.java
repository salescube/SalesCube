/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.master.ProductDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 商品画面（検索）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchProductForm extends AbstractSearchForm<ProductDto> {

	/** 商品コード */
	public String productCode;

	/** 商品名 */
	public String productName;

	/** 商品名カナ */
	public String productKana;

	/** 仕入先コード */
	public String supplierCode;

	/** 仕入先商品名 */
	public String supplierName;

	/** 標準分類 */
	public String productStandardCategory;

	/** 状況 */
	public String productStatusCategory;

	/** 保管 */
	public String productStockCategory;

	/** セット分類 */
	public String setTypeCategory;

	/** 分類（大） */
	public String product1;

	/** 分類（中） */
	public String product2;

	/** 分類（小） */
	public String product3;

	/** 備考 */
	public String remarks;

	// 標準
	public List<LabelValueBean> standardCategoryList = null;
	// 状況
	public List<LabelValueBean> statusCategoryList = null;
	// 保管
	public List<LabelValueBean> stockCategorylist = null;
	// セット分類
	public List<LabelValueBean> setCategoryList = null;
	// 分類（大）
	public List<LabelValueBean> product1List = null;
	// 分類（中）
	public List<LabelValueBean> product2List = new ArrayList<LabelValueBean>();
	// 分類（小）
	public List<LabelValueBean> product3List = new ArrayList<LabelValueBean>();

}
