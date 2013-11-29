/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.DiscountDto;
import jp.co.arkinfosys.entity.join.DiscountJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchDiscountForm;
import jp.co.arkinfosys.service.DiscountService;
import jp.co.arkinfosys.service.MasterSearch;

import org.seasar.struts.annotation.ActionForm;

/**
 * 数量割引マスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchDiscountAjaxAction extends
		AbstractSearchResultAjaxAction<DiscountDto, DiscountJoin> {

	@ActionForm
	@Resource
	public SearchDiscountForm searchDiscountForm;

	@Resource
	public DiscountService discountService;

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchDiscountForm}
	 */
	@Override
	protected AbstractSearchForm<DiscountDto> getActionForm() {
		return this.searchDiscountForm;
	}

	/**
	 * 数量割引マスタDTOを返します.
	 * @return {@link DiscountDto}
	 */
	@Override
	protected Class<DiscountDto> getDtoClass() {
		return DiscountDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索処理を行う数量割引マスタサービスを返します.
	 * @return {@link DiscountService}
	 */
	@Override
	protected MasterSearch<DiscountJoin> getService() {
		return discountService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 数量割引マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_DISCOUNT;
	}
}
