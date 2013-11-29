/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.RateDto;
import jp.co.arkinfosys.entity.join.RateJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchRateForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.RateService;

import org.seasar.struts.annotation.ActionForm;

/**
 * レートマスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchRateAjaxAction extends
		AbstractSearchResultAjaxAction<RateDto, RateJoin> {

	@ActionForm
	@Resource
	public SearchRateForm searchRateForm;

	@Resource
	public RateService rateService;

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchRateForm}
	 */
	@Override
	protected AbstractSearchForm<RateDto> getActionForm() {
		return searchRateForm;
	}

	/**
	 * レートマスタDTOを返します.
	 * @return {@link RateDto}
	 */
	@Override
	protected Class<RateDto> getDtoClass() {
		return RateDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索処理を行うレートマスタサービスを返します.
	 * @return {@link RateService}
	 */
	@Override
	protected MasterSearch<RateJoin> getService() {
		return rateService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return レートマスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_RATE;
	}
}
