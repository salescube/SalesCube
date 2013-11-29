/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.CustomerRankDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchCustomerRankForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerRankService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 顧客ランク画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchCustomerRankAction extends
		AbstractSearchAction<CustomerRankDto> {

	@ActionForm
	@Resource
	public SearchCustomerRankForm searchCustomerRankForm;

	@Resource
	public CategoryService categoryService;

	/**
	 * ソート条件を設定します.
	 *
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() {
		this.searchCustomerRankForm.sortColumn = CustomerRankService.Param.RANK_CODE;
		this.searchCustomerRankForm.sortOrderAsc = true;
	}

	/**
	 *
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		this.searchCustomerRankForm.postageTypeList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.POSTAGE_CATEGORY);
		this.searchCustomerRankForm.postageTypeList
				.add(0, new LabelValueBean());
	}

	/**
	 *
	 * @return {@link SearchCustomerRankForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<CustomerRankDto> getActionForm() {
		return this.searchCustomerRankForm;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_CUSTOMER_RANK}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_CUSTOMER_RANK;
	}

}
