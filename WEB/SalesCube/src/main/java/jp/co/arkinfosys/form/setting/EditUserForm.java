/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.setting.MenuDto;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 社員情報（登録・編集）画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class EditUserForm {

	/**
	 * 社員コード
	 */
	@Required(arg0 = @Arg(key = "labels.userId.emp"))
	@Mask(mask = Constants.CODE_MASK.ASCII_ONLY_MASK, msg = @Msg(key = "errors.ascii"), arg0 = @Arg(key = "labels.userId.emp"))
	@Maxlength(maxlength = 30)
	public String userId;

	/**
	 * 社員名
	 */
	@Required
	@Maxlength(maxlength = 60)
	public String nameKnj;

	/**
	 * 社員名カナ
	 */
	@Required
	@Maxlength(maxlength = 60)
	public String nameKana;

	/**
	 * パスワード
	 */
	@Mask(mask = Constants.CODE_MASK.ASCII_ONLY_MASK, msg = @Msg(key = "errors.ascii"))
	@Maxlength(maxlength = 256)

	public String password;

	/**
	 * パスワード(確認）
	 */
	@Mask(mask = Constants.CODE_MASK.ASCII_ONLY_MASK, msg = @Msg(key = "errors.ascii"))
	@Maxlength(maxlength = 256)
	public String passwordConfirm;

	/**
	 * 部門
	 */
	public String deptId;

	/**
	 * E-MAIL
	 */
	@Maxlength(maxlength = 255)
	public String email;

	/**
	 * 作成日時
	 */
	public String creDatetm;

	/**
	 * 作成日時（表示用）
	 */
	public String creDatetmShow;

	/**
	 * 更新日時
	 */
	public String updDatetm;

	/**
	 * 更新日時（表示用）
	 */
	public String updDatetmShow;

	/**
	 * 権限設定を行うメニューの全件数
	 */
	public int menuCount = 0;

	/**
	 * メニュー毎の権限設定情報(変更後)
	 */
	public List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

	/**
	 * メニュー毎の権限設定情報(オリジナル)
	 */
	public List<MenuDto> originalMenuDtoList = new ArrayList<MenuDto>();

	/**
	 * 部門リスト
	 */
	public List<LabelValueBean> deptList = new ArrayList<LabelValueBean>();

	/**
	 * 編集フラグ
	 */
	public boolean editMode = false;

	/**
	 * 更新権限
	 */
	public boolean isUpdate = false;

	/** ロック */
	public String lockflg = "0";

	/**
	 * ログイン失敗カウント
	 */
	public String failCount = "0";

	/**
	 * ロック日時
	 */
	public String lockDatetm;

	/**
	 * フォームを初期化します.
	 */
	public void reset() {
		this.userId = null;
		this.nameKnj = null;
		this.nameKana = null;
		this.password = null;
		this.passwordConfirm = null;
		this.deptId = null;
		this.email = null;
		this.updDatetm = null;
		this.updDatetmShow = null;
		this.creDatetm = null;
		this.creDatetmShow = null;
		this.editMode = false;
		this.isUpdate = false;
		this.menuCount = 0;
		this.deptList.clear();
		this.menuDtoList.clear();
		this.originalMenuDtoList.clear();
		this.lockflg = "0";
		this.failCount = "0";
		this.lockDatetm = null;
	}

	/**
	 * 新規作成時のバリデートを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validateForInsert() {
		ActionMessages errors = new ActionMessages();
		// パスワード必須チェック
		if (!StringUtil.hasLength(this.password)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.required", MessageResourcesUtil
							.getMessage("labels.password")));
		}
		errors.add(this.validate());

		return errors;
	}

	/**
	 * 更新時のバリデートを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validateForUpdate() {
		ActionMessages errors = new ActionMessages();
		errors.add(this.validate());
		return errors;
	}

	/**
	 * 新規・更新時共通のバリデートを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		// パスワード違い
		if (!this.password.equals(this.passwordConfirm)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.password.difference"));
		}

		return errors;
	}
}
