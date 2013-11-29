/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.entity.Dept;
import jp.co.arkinfosys.entity.Role;
import jp.co.arkinfosys.entity.join.UserJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.ajax.dialog.SearchUserDialogForm;
import jp.co.arkinfosys.service.DeptService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.RoleService;
import jp.co.arkinfosys.service.UserService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 担当者検索ダイアログの表示・検索処理アクションクラスです.
 *
 * @author Ark Information Systems
 */
public class SearchUserDialogAction extends
		AbstractSearchDialogAction<UserDto, UserJoin> {

	/**
	 * 部署マスタに対するサービスクラスです.
	 */
	@Resource
	private DeptService deptService;

	/**
	 * 権限マスタに対するサービスクラスです.
	 */
	@Resource
	private RoleService roleService;

	/**
	 * ユーザーマスタに対するサービスクラスです.
	 */
	@Resource
	private UserService userService;

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public SearchUserDialogForm searchUserDialogForm;

	/**
	 * プルダウンの内容を初期化します.
	 *
	 * @throws ServiceException サービス例外発生時
	 */
	@Override
	protected void createList() throws ServiceException {
		// 部門リストを取得
		List<Dept> deptList = this.deptService.findAllDept();
		for (Dept dept : deptList) {
			LabelValueBean bean = new LabelValueBean();
			bean.setValue(dept.deptId);
			bean.setLabel(dept.name);
			this.searchUserDialogForm.deptList.add(bean);
		}
		this.searchUserDialogForm.deptList.add(0, new LabelValueBean());

		// 権限リストを取得
		List<Role> roleList = this.roleService.findAllRole();
		for (Role role : roleList) {
			LabelValueBean bean = new LabelValueBean();
			bean.setValue(role.roleId);
			bean.setLabel(role.name);
			this.searchUserDialogForm.roleList.add(bean);
		}
		this.searchUserDialogForm.roleList.add(0, new LabelValueBean());
	}

	/**
	 * 検索カラムと検索順序を設定します.
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchUserDialogForm.sortColumn = UserService.Param.USER_ID;
		this.searchUserDialogForm.sortOrderAsc = true;
	}

	/**
	 * アクションフォームを返します.
	 *
	 * @return {@link SearchUserDialogForm}
	 */
	@Override
	protected AbstractSearchForm<UserDto> getActionForm() {
		return this.searchUserDialogForm;
	}

	/**
	 * {@link UserDto}クラスのクラスオブジェクトを返します.
	 *
	 * @return {@link UserDto}クラス
	 */
	@Override
	protected Class<UserDto> getDtoClass() {
		return UserDto.class;
	}

	/**
	 * 検索キー値を返します.
	 *
	 * @return ユーザーID
	 */
	@Override
	protected String getId() {
		return this.searchUserDialogForm.userId;
	}

	/**
	 * キー値での検索結果が0件だったことを通知するために、メッセージリソースのキーを返します.
	 *
	 * @return メッセージキー
	 */
	@Override
	protected String getMissingRecordMessageKey() {
		return null;
	}

	/**
	 * {@link UserService}クラスのインスタンスを返します.
	 *
	 * @return {@link UserService}
	 */
	@Override
	protected MasterSearch<UserJoin> getService() {
		return this.userService;
	}
}
