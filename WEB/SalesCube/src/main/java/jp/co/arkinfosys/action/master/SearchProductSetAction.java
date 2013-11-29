/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.ProductSetDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchProductSetForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 * セット商品画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchProductSetAction extends AbstractSearchAction<ProductSetDto> {

	@ActionForm
	@Resource
	public SearchProductSetForm searchProductSetForm;

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
	 * @return {@link SearchProductSetForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<ProductSetDto> getActionForm() {
		return this.searchProductSetForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_PRODUCT_SET}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_PRODUCT_SET;
	}

}
