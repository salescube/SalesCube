/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.porder;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.porder.POrderSlipLineJoinDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.porder.MakeOutPOrderForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 *
 * 発注書発行画面を表示するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class MakeOutPOrderAction extends
		AbstractSearchAction<POrderSlipLineJoinDto> {

	@ActionForm
	@Resource
	protected MakeOutPOrderForm makeOutPOrderForm;

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.<br>
	 * 未使用です.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link MakeOutPOrderForm}
	 */
	@Override
	protected AbstractSearchForm<POrderSlipLineJoinDto> getActionForm() {
		return this.makeOutPOrderForm;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 発注書発行画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MAKE_OUT_PORDER;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 発注入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_PORDER;
	}

}
