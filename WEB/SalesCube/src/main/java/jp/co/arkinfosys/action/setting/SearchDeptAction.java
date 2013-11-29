/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.setting.DeptDto;
import jp.co.arkinfosys.entity.Dept;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.setting.SearchDeptForm;
import jp.co.arkinfosys.service.DeptService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 部門情報検索画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchDeptAction extends AbstractSearchAction<DeptDto> {

	@ActionForm
	@Resource
	public SearchDeptForm searchDeptForm;

	@Resource
	private DeptService deptService;

	/**
	 * ソート条件を設定します.
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.searchDeptForm.sortColumn = DeptService.Param.DEPT_ID;
		this.searchDeptForm.sortOrderAsc = true;
	}

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		// 親部門リストを作成
		List<Dept> deptList = this.deptService.findAllDept();
		for (Dept dept : deptList) {
			this.searchDeptForm.parentList.add(new LabelValueBean(dept.name,
					dept.deptId));
		}
		this.searchDeptForm.parentList.add(0, new LabelValueBean());
	}

	/**
	 * 
	 * @return {@link SearchDeptForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<DeptDto> getActionForm() {
		return this.searchDeptForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#SETTING_DEPT}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SETTING_DEPT;
	}

}
