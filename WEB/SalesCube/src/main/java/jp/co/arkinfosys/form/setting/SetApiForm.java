/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import org.apache.struts.action.ActionMessages;


/**
 * セキュリティ設定画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class SetApiForm {

    /**
     * トークン
     */
    public String token;

    /**
     * トークン 確認用
     */
    public String detoken;

	/**
     * 更新日時
     */
    public String updDatetm;

    public boolean isUpdate = false;

    /**
     * 登録時のバリデートを行います.
     * @return 表示するメッセージ
     */
    public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		return errors;
    }

}
