/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.setting.DeptDto;
import jp.co.arkinfosys.entity.Dept;
import jp.co.arkinfosys.form.setting.EditDeptForm;
import jp.co.arkinfosys.service.DeptService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 部門情報編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditDeptAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "editDept.jsp";
	}

	@ActionForm
	@Resource
	private EditDeptForm editDeptForm;

	@Resource
	private DeptService deptService;

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		try {
			this.init(null);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}

		return EditDeptAction.Mapping.INPUT;
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{deptId}")
	public String edit() throws Exception {
		try {
			this.editDeptForm.deptId = StringUtil.decodeSL(this.editDeptForm.deptId);
			this.init(this.editDeptForm.deptId);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		return EditDeptAction.Mapping.INPUT;
	}

	/**
	 * 登録処理を行います.<br>
	 * 登録完了時および何かしらの問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validateForInsert", input = EditDeptAction.Mapping.INPUT)
	public String insert() throws Exception {
		try {
			Dept dept = this.deptService
					.findById(this.editDeptForm.deptId);
			if (dept != null) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dept.already.exists"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return EditDeptAction.Mapping.INPUT;
			}

			DeptDto dto = Beans.createAndCopy(DeptDto.class, this.editDeptForm)
					.execute();

			// ユーザー情報登録
			this.deptService.insertRecord(dto);

			this.init(this.editDeptForm.deptId);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.insert"));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		return EditDeptAction.Mapping.INPUT;
	}

	/**
	 * 更新処理を行います.<br>
	 * 更新完了時に、画面にメッセージを表示します.
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validateForUpdate", input = EditDeptAction.Mapping.INPUT)
	public String update() throws Exception {
		try {
			DeptDto dto = Beans.createAndCopy(DeptDto.class, this.editDeptForm)
					.execute();

			// ユーザー情報更新
			this.deptService.updateRecord(dto);

			this.init(this.editDeptForm.deptId);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.update"));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (UnabledLockException e) {
			super.errorLog(e);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(e.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		return EditDeptAction.Mapping.INPUT;
	}

	/**
	 * 削除処理を行います.<br>
	 * 削除完了時および何かしら問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		try {
			List<Dept> deptList = this.deptService
					.findByParentId(this.editDeptForm.deptId);
			if (deptList.size() > 0) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dept.childexists"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return EditDeptAction.Mapping.INPUT;
			}

			// ユーザー情報更新
			this.deptService.deleteRecord(Beans.createAndCopy(DeptDto.class, this.editDeptForm).execute());
			this.init(null);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.delete"));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (UnabledLockException e) {
			super.errorLog(e);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(e.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		return EditDeptAction.Mapping.INPUT;
	}

	/**
	 * リセット処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		if (this.editDeptForm.editMode) {
			this.init(this.editDeptForm.deptId);
		} else {
			this.init(null);
		}
		return EditDeptAction.Mapping.INPUT;
	}

	/**
	 * アクションフォームを初期化します.
	 * @param deptId 部門ID
	 * @throws ServiceException
	 */
	private void init(String deptId) throws ServiceException {
		this.editDeptForm.reset();

		// 更新権限
		this.editDeptForm.isUpdate = super.userDto
				.isMenuUpdate(Constants.MENU_ID.SETTING_DEPT);

		// 親部門リスト
		List<Dept> deptList = this.deptService.findAllDept();
		for (Dept dept : deptList) {
			this.editDeptForm.parentList.add(new LabelValueBean(dept.name,
					dept.deptId));
		}
		this.editDeptForm.parentList.add(0, new LabelValueBean());

		if (!StringUtil.hasLength(deptId)) {
			return;
		}

		// 編集の場合、親部門プルダウンリストから自身とその子孫部門を削除する
		List<DeptDto> deptDtoList = this.deptService
				.convertEntityToDto(deptList);
		for (DeptDto dto : deptDtoList) {
			if (!deptId.equals(dto.deptId)) {
				continue;
			}

			// 子孫部門を取得
			List<DeptDto> descDeptList = dto.getDescDetp();

			Iterator<LabelValueBean> ite = this.editDeptForm.parentList
					.iterator();
			while (ite.hasNext()) {
				LabelValueBean bean = ite.next();
				if (bean.getValue() == null) {
					// 先頭項目(空白)
					continue;
				}

				if (bean.getValue().equals(deptId)) {
					// 自身なら削除
					ite.remove();
					continue;
				}

				for (DeptDto desc : descDeptList) {
					if (bean.getValue().equals(desc.deptId)) {
						// 子孫部門なら削除
						ite.remove();
						break;
					}
				}
			}
		}

		// ユーザー情報を取得してフォームにセットする
		Dept dept = this.deptService.findById(deptId);
		if (dept == null) {
			return;
		}
		Beans.copy(dept, this.editDeptForm).timestampConverter(
				Constants.FORMAT.TIMESTAMP)
				.dateConverter(Constants.FORMAT.DATE).execute();

		// 画面表示用の更新
		this.editDeptForm.creDatetmShow = StringUtil.getDateString(
				Constants.FORMAT.DATE, dept.creDatetm);
		this.editDeptForm.updDatetmShow = StringUtil.getDateString(
				Constants.FORMAT.DATE, dept.updDatetm);

		this.editDeptForm.editMode = true;
	}
}
