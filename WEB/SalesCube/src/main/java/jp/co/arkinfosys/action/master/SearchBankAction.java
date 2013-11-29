/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.BankDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchBankForm;
import jp.co.arkinfosys.service.BankService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 銀行一覧画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchBankAction extends AbstractSearchAction<BankDto> {

	@ActionForm
	@Resource
	public SearchBankForm searchBankForm;

	@Resource
	public CategoryService categoryService;

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		this.searchBankForm.dwbTypeList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.DWB_TYPE);
		this.searchBankForm.dwbTypeList.add(0, new LabelValueBean());
	}

	/**
	 * ソート条件を設定します.
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.searchBankForm.sortColumn = BankService.Param.BANK_CODE;
		this.searchBankForm.sortOrderAsc = true;
	}

	/**
	 * 
	 * @return {@link SearchBankForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<BankDto> getActionForm() {
		return this.searchBankForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_BANK}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_BANK;
	}

}
