/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.SupplierDto;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchSupplierForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.SupplierService;

import org.seasar.struts.annotation.ActionForm;

/**
 * 仕入先マスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchSupplierAjaxAction extends
		AbstractSearchResultAjaxAction<SupplierDto, SupplierJoin> {

	@Resource
	public SupplierService supplierService;

	@ActionForm
	@Resource
	public SearchSupplierForm searchSupplierForm;

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchSupplierForm}
	 */
	@Override
	protected AbstractSearchForm<SupplierDto> getActionForm() {
		return this.searchSupplierForm;
	}

	/**
	 * 仕入先マスタDTOを返します.
	 * @return {@link SupplierDto}
	 */
	@Override
	protected Class<SupplierDto> getDtoClass() {
		return SupplierDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索処理を行う仕入先マスタサービスを返します.
	 * @return {@link SupplierService}
	 */
	@Override
	protected MasterSearch<SupplierJoin> getService() {
		return supplierService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 仕入先マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_SUPPLIER;
	}
}
