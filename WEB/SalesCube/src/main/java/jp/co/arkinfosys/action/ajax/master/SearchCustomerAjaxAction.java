/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.CustomerDto;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchCustomerForm;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.MasterSearch;

import org.seasar.struts.annotation.ActionForm;

/**
 * 顧客マスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchCustomerAjaxAction extends
		AbstractSearchResultAjaxAction<CustomerDto, CustomerJoin> {

	@ActionForm
	@Resource
	public SearchCustomerForm searchCustomerForm;

	@Resource
	public CustomerService customerService;

	/**
	 * 顧客マスタDTOを返します.
	 * @return {@link CustomerDto}
	 */
	@Override
	protected Class<CustomerDto> getDtoClass() {
		return CustomerDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 顧客マスタ画面のメニューID
	 */
	protected String getMenuID() {
		return Constants.MENU_ID.MASTER_CUSTOMER;
	}

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchCustomerForm}
	 */
	protected AbstractSearchForm<CustomerDto> getActionForm() {
		return this.searchCustomerForm;
	}

	/**
	 * 検索処理を行う顧客マスタサービスを返します.
	 * @return {@link CustomerService}
	 */
	@Override
	protected MasterSearch<CustomerJoin> getService() {
		return customerService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 顧客マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_CUSTOMER;
	}
}
