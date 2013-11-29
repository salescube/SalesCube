/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.master.BankDto;
import jp.co.arkinfosys.entity.join.BankDwb;
import jp.co.arkinfosys.form.ajax.master.DeleteBankAjaxForm;
import jp.co.arkinfosys.service.BankService;
import jp.co.arkinfosys.service.AbstractMasterEditService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 * 銀行マスタ画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 */
public class DeleteBankAjaxAction extends AbstractDeleteAjaxAction<BankDto, BankDwb> {

	@ActionForm
	@Resource
	public DeleteBankAjaxForm deleteBankAjaxForm;

	@Resource
	public BankService bankService;

	/**
	 * 削除レコードを識別する情報を持った銀行マスタDTOを返します.
	 * @return {@link BankDto}
	 */
	@Override
	protected BankDto getIdentifiedDto() {
		return Beans.createAndCopy(BankDto.class, this.deleteBankAjaxForm).execute();
	}

	/**
	 * 削除処理を行う銀行マスタサービスを返します.
	 * @return {@link BankService}
	 */
	@Override
	protected AbstractMasterEditService<BankDto, BankDwb> getService() {
		return this.bankService;
	}
}
