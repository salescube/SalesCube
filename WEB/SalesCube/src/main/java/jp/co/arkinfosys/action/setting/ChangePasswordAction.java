/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.Mine;
import jp.co.arkinfosys.entity.User;
import jp.co.arkinfosys.form.setting.ChangePasswordForm;
import jp.co.arkinfosys.service.UserService;
import jp.co.arkinfosys.service.MineService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * パスワード変更画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class ChangePasswordAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "changePassword.jsp";
	}

	@ActionForm
	@Resource
	public ChangePasswordForm changePasswordForm;

	@Resource
	private UserService userService;

	@Resource
	private MineService mineService;


	public boolean isUpdate;

	/**
	 * 初期表示を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		if (super.mineDto.passwordValidDays != null
				&& this.userDto.isPasswordExpired()) {
			// パスワード有効期限切れ
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("warns.password.expired"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		}

		initialSet();

		return ChangePasswordAction.Mapping.INPUT;
	}

	/**
	 * 更新処理を行います.
	 * 更新完了時および現パスワード間違い時に、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validate = "validate", validator = true, input = "init")
	public String update() throws Exception {
		User user = this.userService.findUserByIdAndPassword(
				changePasswordForm.userId, changePasswordForm.oldPassword);
		if (user == null) {
			// 現在のパスワード誤り
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.invalid.password"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

			this.isUpdate = this.userDto
					.isMenuUpdate(Constants.MENU_ID.SETTING_CHANGE_PASSWORD);
			this.changePasswordForm.userId = this.userDto.userId;

			return ChangePasswordAction.Mapping.INPUT;
		}


		//最新の自社マスタ情報を取得
		Mine mine = this.mineService.getMine();

		//パスワード文字数チェック　セキュリテイ設定画面で設定したパスワード桁数と比較
		if( mine.passwordLength != null)	{
			if(changePasswordForm.newPassword.length() < mine.passwordLength){

				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.userPassword.length",mine.passwordLength));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

				this.isUpdate = this.userDto
						.isMenuUpdate(Constants.MENU_ID.SETTING_CHANGE_PASSWORD);
				this.changePasswordForm.userId = this.userDto.userId;
				return ChangePasswordAction.Mapping.INPUT;
			}
		}

		//パスワードの文字種チェック
		if( mine.passwordCharType != null)	{
			if(!this.userService.checkPasswordCharType(mine.passwordCharType,changePasswordForm.newPassword)){
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.userPassword.passwordCharType"+ mine.passwordCharType));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

				this.isUpdate = this.userDto
						.isMenuUpdate(Constants.MENU_ID.SETTING_CHANGE_PASSWORD);
				this.changePasswordForm.userId = this.userDto.userId;
				return ChangePasswordAction.Mapping.INPUT;
			}
		}


		//パスワード更新時のチェック
		//自社マスタで設定した回数前まで同一のパスワードが存在した場合は、更新不可とする
		if( mine.passwordHistCount != null)	{
			if(userService.countPasswordMakerByIdAndPass(userDto.userId,changePasswordForm.newPassword,mine.passwordHistCount) > 0){

				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.userPassword.already.exists",mineDto.passwordHistCount));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

				this.isUpdate = this.userDto
						.isMenuUpdate(Constants.MENU_ID.SETTING_CHANGE_PASSWORD);
				this.changePasswordForm.userId = this.userDto.userId;
				return ChangePasswordAction.Mapping.INPUT;
			}
		}


		// パスワードを更新する
		this.userService.updatePassword(super.userDto.userId,
				changePasswordForm.newPassword,mine);

		// 更新したユーザー情報でセッションのDTOを更新する
		user = this.userService.findUserByIdAndPassword(
				changePasswordForm.userId, changePasswordForm.newPassword);
		Beans.copy(user, super.userDto).execute();

		super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"infos.password.changed"));
		ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

		this.isUpdate = this.userDto
				.isMenuUpdate(Constants.MENU_ID.SETTING_CHANGE_PASSWORD);

		return ChangePasswordAction.Mapping.INPUT;
	}

	/**
	 * リセット処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		initialSet();
		return ChangePasswordAction.Mapping.INPUT;
	}

	/**
	 * 初期化処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String init() throws Exception {
		initialSet();
		return ChangePasswordAction.Mapping.INPUT;
	}

	/**
	 * アクションフォーム等に初期化で必要な情報を設定します.
	 */
	private void initialSet() {
		this.isUpdate = this.userDto
				.isMenuUpdate(Constants.MENU_ID.SETTING_CHANGE_PASSWORD);
		this.changePasswordForm.userId = this.userDto.userId;
	}
}
