/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.RateDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchRateForm;
import jp.co.arkinfosys.service.RateService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 * レート画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchRateAction extends AbstractSearchAction<RateDto> {

	@ActionForm
	@Resource
	public SearchRateForm searchRateForm;

	/**
	 * ソート条件を設定します.
	 * 
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() {
		this.searchRateForm.sortColumn = RateService.Param.RATE_ID;
		this.searchRateForm.sortOrderAsc = true;
	}

	/**
	 * 何も処理しません.
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
	}

	/**
	 * 
	 * @return {@link SearchRateForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<RateDto> getActionForm() {
		return this.searchRateForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_RATE}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_RATE;
	}

}
