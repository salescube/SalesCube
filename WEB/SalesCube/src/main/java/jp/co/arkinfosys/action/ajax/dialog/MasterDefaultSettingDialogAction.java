/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.CommonAjaxResources;
import jp.co.arkinfosys.entity.join.InitMstJoin;
import jp.co.arkinfosys.form.ajax.dialog.MasterDefaultSettingDialogForm;
import jp.co.arkinfosys.service.InitMstService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * マスタ初期値設定ダイアログの表示・更新処理アクションクラスです.
 *
 *
 * @author Ark Information Systems
 *
 */
public class MasterDefaultSettingDialogAction extends AbstractDialogAction {

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public MasterDefaultSettingDialogForm masterDefaultSettingDialogForm;

	/**
	 * 初期値マスタに対するサービスクラスです.
	 */
	@Resource
	private InitMstService initMstService;

	/**
	 * 初期値マスタから初期値を取得します.
	 */
	@Override
	protected void createList() throws ServiceException {
		// 初期値マスタを検索する
		List<InitMstJoin> initMstJoinList = this.initMstService
				.findInitDataByTableNameWithCategory(this.masterDefaultSettingDialogForm.tableName);

		// 画面表示に適したデータに変換する
		this.masterDefaultSettingDialogForm.initMstDtoList = this.initMstService
				.convertEntityToDto(initMstJoinList);
	}

	/**
	 * マスタ初期値を更新する処理を行うメソッドです.
	 *
	 * @return null
	 * @throws Exception 例外発生時
	 */
	@Execute(validator = true, validate = "validate", urlPattern = "update/{dialogId}", input = Mapping.ERROR_JSP)
	public String update() throws Exception {
		try {
			this.initMstService
					.updateInitData(this.masterDefaultSettingDialogForm.initMstDtoList);
		} catch (UnabledLockException e) {
			super.errorLog(e);

			// ロックエラー
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e
					.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, errors);

			return CommonAjaxResources.Mapping.ERROR_JSP;
		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
		}
		return null;
	}

}
