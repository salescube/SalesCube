/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.setting;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.setting.DeptDto;
import jp.co.arkinfosys.entity.join.DeptJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.setting.SearchDeptForm;
import jp.co.arkinfosys.service.DeptService;
import jp.co.arkinfosys.service.MasterSearch;

import org.seasar.struts.annotation.ActionForm;

/**
 * 部門情報検索画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchDeptResultAjaxAction extends
		AbstractSearchResultAjaxAction<DeptDto, DeptJoin> {

	@Resource
	public DeptService deptService;

	@ActionForm
	@Resource
	public SearchDeptForm searchDeptForm;

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchDeptForm}
	 */
	@Override
	protected AbstractSearchForm<DeptDto> getActionForm() {
		return this.searchDeptForm;
	}

	/**
	 * 部門情報DTOを返します.
	 * @return {@link DeptDto}
	 */
	@Override
	protected Class<DeptDto> getDtoClass() {
		return DeptDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索処理を行う部門情報サービスを返します.
	 * @return {@link DeptService}
	 */
	@Override
	protected MasterSearch<DeptJoin> getService() {
		return this.deptService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 部門情報画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SETTING_DEPT;
	}

}
