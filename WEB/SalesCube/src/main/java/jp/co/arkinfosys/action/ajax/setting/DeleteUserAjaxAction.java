/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.setting;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.entity.join.UserJoin;
import jp.co.arkinfosys.form.ajax.setting.DeleteUserAjaxForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.UserService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 * 社員情報画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteUserAjaxAction extends
		AbstractDeleteAjaxAction<UserDto, UserJoin> {

	@ActionForm
	@Resource
	protected DeleteUserAjaxForm deleteUserAjaxForm;

	@Resource
	protected UserService userService;

	/**
	 * 削除レコードを識別する情報を持った社員情報DTOを返します.
	 * @return {@link UserDto}
	 */
	@Override
	protected UserDto getIdentifiedDto() {
		return Beans.createAndCopy(UserDto.class, this.deleteUserAjaxForm)
				.execute();
	}

	/**
	 * 削除処理を行う社員情報サービスを返します.
	 * @return {@link UserService}
	 */
	@Override
	protected AbstractMasterEditService<UserDto, UserJoin> getService() {
		return this.userService;
	}
}
