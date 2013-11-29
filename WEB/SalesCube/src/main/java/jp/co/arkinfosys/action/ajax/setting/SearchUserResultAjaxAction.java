/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.setting;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.entity.join.UserJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.setting.SearchUserForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.UserService;

import org.seasar.struts.annotation.ActionForm;

/**
 * 社員情報検索画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchUserResultAjaxAction extends
		AbstractSearchResultAjaxAction<UserDto, UserJoin> {

	@ActionForm
	@Resource
	private SearchUserForm searchUserForm;

	@Resource
	private UserService userService;

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchUserForm}
	 */
	@Override
	protected AbstractSearchForm<UserDto> getActionForm() {
		return this.searchUserForm;
	}

	/**
	 * 社員情報DTOを返します.
	 * @return {@link UserDto}
	 */
	@Override
	protected Class<UserDto> getDtoClass() {
		return UserDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索処理を行う社員情報サービスを返します.
	 * @return {@link UserService}
	 */
	@Override
	protected MasterSearch<UserJoin> getService() {
		return this.userService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 社員情報画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SETTING_USER;
	}
}
