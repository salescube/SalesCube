/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.TaxRateDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchTaxRateForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 * 税画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchTaxRateAction extends AbstractSearchAction<TaxRateDto> {

	@ActionForm
	@Resource
	public SearchTaxRateForm searchTaxRateForm;

	@Resource
	public CategoryService categoryService;

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		this.searchTaxRateForm.categoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.TAX_TYPE_CATEGORY);
	}

	/**
	 * 
	 * @return {@link SearchTaxRateForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<TaxRateDto> getActionForm() {
		return this.searchTaxRateForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_TAX_RATE}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_TAX_RATE;
	}

}
