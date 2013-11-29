/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.CategoryGroupDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchCategoryForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 * 区分名一覧画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchCategoryAction extends
		AbstractSearchAction<CategoryGroupDto> {

	@ActionForm
	@Resource
	public SearchCategoryForm searchCategoryForm;

	@Resource
	public CategoryService categoryService;

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		// グループ名リストを取得
		this.searchCategoryForm.groupList = categoryService
				.findUpdatableGroups();

		for (CategoryGroupDto group : this.searchCategoryForm.groupList) {
			group.categoryList = categoryService
					.findCategoryByGroupName(group.name);
		}
	}

	/**
	 * 
	 * @return {@link SearchCategoryForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<CategoryGroupDto> getActionForm() {
		return this.searchCategoryForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_CATEGORY}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_CATEGORY;
	}
}
