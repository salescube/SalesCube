/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Maxbytelength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Required;

/**
 * パスワード変更画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class ChangePasswordForm {

	@Required
	public String userId;

	@Required
	@Maxbytelength(maxbytelength = 256)
	public String oldPassword;

	@Required
	@Maxbytelength(maxbytelength = 256)
	public String newPassword;

	@Required
	@Maxbytelength(maxbytelength = 256)
	public String newPasswordConfirm;

	/**
	 * パスワードとパスワード（確認）の入力値が同一であることをチェックします.
	 *
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		if (!this.newPassword.equals(this.newPasswordConfirm)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.password.difference"));
		}
		return errors;
	}

}
