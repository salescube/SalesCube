/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchRackForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 棚番画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchRackAction extends AbstractSearchAction<RackDto> {

	@ActionForm
	@Resource
	public SearchRackForm searchRackForm;

	@Resource
	public CategoryService categoryService;

	/**
	 * ソート条件を設定します.
	 * 
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() {
		this.searchRackForm.sortColumn = RackService.Param.RACK_CODE;
		this.searchRackForm.sortOrderAsc = true;
	}

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		this.searchRackForm.rackList = categoryService
				.findCategoryLabelValueBeanListById(Categories.RACK_CATEGORY);
		this.searchRackForm.rackList.add(0, new LabelValueBean());
	}

	/**
	 * 
	 * @return {@link SearchRackForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<RackDto> getActionForm() {
		return this.searchRackForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_RACK}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_RACK;
	}

}
