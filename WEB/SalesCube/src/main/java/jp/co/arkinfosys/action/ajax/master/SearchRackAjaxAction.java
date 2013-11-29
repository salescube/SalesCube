/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.entity.join.RackJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchRackForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.RackService;

import org.seasar.struts.annotation.ActionForm;

/**
 * 棚番マスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchRackAjaxAction extends
		AbstractSearchResultAjaxAction<RackDto, RackJoin> {

	@ActionForm
	@Resource
	public SearchRackForm searchRackForm;

	@Resource
	public RackService rackService;

	/**
	 * 検索後に必要な処理を行います.<BR>
	 * 棚に紐づく商品コードの情報を追加します.
	 */
	@Override
	protected void doAfterSearch() throws Exception {
		this.rackService
				.addProductInfoToRackDto(this.searchRackForm.searchResultList);
	}

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchRackForm}
	 */
	@Override
	protected AbstractSearchForm<RackDto> getActionForm() {
		return searchRackForm;
	}

	/**
	 * 棚番マスタDTOを返します.
	 * @return {@link RackDto}
	 */
	@Override
	protected Class<RackDto> getDtoClass() {
		return RackDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索処理を行う棚番マスタサービスを返します.
	 * @return {@link RackService}
	 */
	@Override
	protected MasterSearch<RackJoin> getService() {
		return rackService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 棚番マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_RACK;
	}
}
