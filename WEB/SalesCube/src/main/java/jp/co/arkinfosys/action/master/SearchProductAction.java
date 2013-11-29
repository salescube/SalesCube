/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.ProductDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchProductForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 商品画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchProductAction extends AbstractSearchAction<ProductDto> {

	@ActionForm
	@Resource
	public SearchProductForm searchProductForm;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductClassService productClassService;

	/**
	 * ソート条件を設定します.
	 * 
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() {
		this.searchProductForm.sortColumn = ProductService.Param.PRODUCT_CODE;
		this.searchProductForm.sortOrderAsc = true;
	}

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	protected void createList() throws ServiceException {
		// 標準化分類リスト
		this.searchProductForm.standardCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PRODUCT_STANDARD);
		this.searchProductForm.standardCategoryList
				.add(0, new LabelValueBean());

		// 分類状況
		this.searchProductForm.statusCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PRODUCT_STATUS);
		this.searchProductForm.statusCategoryList.add(0, new LabelValueBean());

		// 分類保管
		this.searchProductForm.stockCategorylist = categoryService
				.findCategoryLabelValueBeanListById(Categories.PRODUCT_STOCK);
		this.searchProductForm.stockCategorylist.add(0, new LabelValueBean());

		// セット分類
		this.searchProductForm.setCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PRODUCT_SET_TYPE);
		this.searchProductForm.setCategoryList.add(0, new LabelValueBean());

		// 分類（大）
		this.searchProductForm.product1List = this.productClassService
				.findAllProductClass1LabelValueBeanList();
		this.searchProductForm.product1List.add(0, new LabelValueBean());

		this.searchProductForm.product2List = new ArrayList<LabelValueBean>();
		this.searchProductForm.product2List.add(0, new LabelValueBean());
		this.searchProductForm.product3List = new ArrayList<LabelValueBean>();
		this.searchProductForm.product3List.add(0, new LabelValueBean());
	}

	/**
	 * 
	 * @return {@link SearchProductForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<ProductDto> getActionForm() {
		return this.searchProductForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_PRODUCT}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_PRODUCT;
	}
}
