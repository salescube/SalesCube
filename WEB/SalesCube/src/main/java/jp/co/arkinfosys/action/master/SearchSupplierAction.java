/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.SupplierDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchSupplierForm;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 * 仕入先画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchSupplierAction extends AbstractSearchAction<SupplierDto> {

	@ActionForm
	@Resource
	public SearchSupplierForm searchSupplierForm;

	/**
	 * ソート条件を設定します.
	 *
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() {
		searchSupplierForm.sortColumn = SupplierService.Param.SUPPLIER_CODE;
		searchSupplierForm.sortOrderAsc = true;
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
	 * @return {@link SearchSupplierForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<SupplierDto> getActionForm() {
		return this.searchSupplierForm;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_SUPPLIER}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_SUPPLIER;
	}

}
