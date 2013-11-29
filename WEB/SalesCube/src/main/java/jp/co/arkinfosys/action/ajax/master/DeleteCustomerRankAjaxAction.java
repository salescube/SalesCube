/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.master.CustomerRankDto;
import jp.co.arkinfosys.entity.CustomerRank;
import jp.co.arkinfosys.form.ajax.master.DeleteCustomerRankAjaxForm;
import jp.co.arkinfosys.service.CustomerRankService;
import jp.co.arkinfosys.service.AbstractMasterEditService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 * 顧客ランク画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteCustomerRankAjaxAction extends
		AbstractDeleteAjaxAction<CustomerRankDto, CustomerRank> {

	@Resource
	public CustomerRankService customerRankService;

	@ActionForm
	@Resource
	public DeleteCustomerRankAjaxForm deleteCustomerRankAjaxForm;

	/**
	 * 削除レコードを識別する情報を持った顧客ランクDTOを返します.
	 * @return {@link CustomerRankDto}
	 */
	@Override
	protected CustomerRankDto getIdentifiedDto() {
		return Beans.createAndCopy(CustomerRankDto.class,
				this.deleteCustomerRankAjaxForm).execute();
	}

	/**
	 * 削除処理を行う顧客ランクサービスを返します.
	 * @return {@link CustomerRankService}
	 */
	@Override
	protected AbstractMasterEditService<CustomerRankDto, CustomerRank> getService() {
		return this.customerRankService;
	}
}
