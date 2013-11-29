/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.setting;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.setting.DeptDto;
import jp.co.arkinfosys.entity.Dept;
import jp.co.arkinfosys.entity.join.DeptJoin;
import jp.co.arkinfosys.form.ajax.master.DeleteDeptAjaxForm;
import jp.co.arkinfosys.service.DeptService;
import jp.co.arkinfosys.service.AbstractMasterEditService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 * 部門情報画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteDeptAjaxAction extends
		AbstractDeleteAjaxAction<DeptDto, DeptJoin> {

	@ActionForm
	@Resource
	public DeleteDeptAjaxForm deleteDeptAjaxForm;

	@Resource
	public DeptService deptService;

	/**
	 * 削除前のレコードのチェックを行います.
	 * @return 表示するメッセージ
	 */
	@Override
	protected ActionMessages checkRecord() throws Exception {
		ActionMessages messages = new ActionMessages();

		List<Dept> deptList = this.deptService
				.findByParentId(this.deleteDeptAjaxForm.deptId);
		if (deptList.size() > 0) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.dept.childexists"));
		}
		return messages;
	}

	/**
	 * 削除レコードを識別する情報を持った部門情報DTOを返します.
	 * @return {@link DeptDto}
	 */
	@Override
	protected DeptDto getIdentifiedDto() {
		return Beans.createAndCopy(DeptDto.class, this.deleteDeptAjaxForm)
				.execute();
	}

	/**
	 * 削除処理を行う部門情報サービスを返します.
	 * @return {@link DeptService}
	 */
	@Override
	protected AbstractMasterEditService<DeptDto, DeptJoin> getService() {
		return this.deptService;
	}
}
