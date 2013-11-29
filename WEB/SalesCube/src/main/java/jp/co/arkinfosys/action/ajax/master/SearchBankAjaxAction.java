/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.BankDto;
import jp.co.arkinfosys.entity.join.BankDwb;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchBankForm;
import jp.co.arkinfosys.service.BankService;
import jp.co.arkinfosys.service.MasterSearch;

import org.seasar.struts.annotation.ActionForm;

/**
 * 銀行マスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchBankAjaxAction extends
		AbstractSearchResultAjaxAction<BankDto, BankDwb> {

	@ActionForm
	@Resource
	public SearchBankForm searchBankForm;

	@Resource
	public BankService bankService;

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchBankForm}
	 */
	@Override
	protected AbstractSearchForm<BankDto> getActionForm() {
		return this.searchBankForm;
	}

	/**
	 * 銀行マスタDTOを返します.
	 * @return {@link BankDto}
	 */
	@Override
	protected Class<BankDto> getDtoClass() {
		return BankDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索処理を行う銀行マスタサービスを返します.
	 * @return {@link BankService}
	 */
	@Override
	protected MasterSearch<BankDwb> getService() {
		return this.bankService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 銀行マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_BANK;
	}

}
