/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.DiscountDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchDiscountForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.DiscountService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 数量割引画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchDiscountAction extends AbstractSearchAction<DiscountDto> {

	@ActionForm
	@Resource
	public SearchDiscountForm searchDiscountForm;

	@Resource
	public CategoryService categoryService;

	/**
	 * ソート条件を設定します.
	 * 
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() {
		this.searchDiscountForm.sortColumn = DiscountService.Param.DISCOUNT_ID;
		this.searchDiscountForm.sortOrderAsc = true;
	}

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		// 割引有効リスト
		this.searchDiscountForm.useFlagList = categoryService
				.findCategoryLabelValueBeanListById(Categories.USE_FLAG);
		this.searchDiscountForm.useFlagList.add(0, new LabelValueBean());
	}

	/**
	 * 
	 * @return {@link SearchDiscountForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<DiscountDto> getActionForm() {
		return this.searchDiscountForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_DISCOUNT}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_DISCOUNT;
	}
}
