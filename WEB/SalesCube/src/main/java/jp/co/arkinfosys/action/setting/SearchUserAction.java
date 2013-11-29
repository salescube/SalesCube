/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.entity.Dept;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.setting.SearchUserForm;
import jp.co.arkinfosys.service.DeptService;
import jp.co.arkinfosys.service.UserService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 社員情報検索画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchUserAction extends AbstractSearchAction<UserDto> {

	@ActionForm
	@Resource
	private SearchUserForm searchUserForm;

	@Resource
	private DeptService deptService;

	/**
	 * ソート条件を設定します.
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.searchUserForm.sortColumn = UserService.Param.USER_ID;
		this.searchUserForm.sortOrderAsc = true;
	}

	/**
	 * 
	 * @return {@link SearchUserForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<UserDto> getActionForm() {
		return this.searchUserForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#SETTING_USER}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SETTING_USER;
	}

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		List<Dept> deptList = this.deptService.findAllDept();
		for (Dept dept : deptList) {
			LabelValueBean bean = new LabelValueBean();
			bean.setLabel(dept.name);
			bean.setValue(dept.deptId);
			this.searchUserForm.deptList.add(bean);
		}
		this.searchUserForm.deptList.add(0, new LabelValueBean());
	}
}
