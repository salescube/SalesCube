/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.IntRange;

/**
 * セキュリティ設定画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class SetSecurityForm {
    /**
     * パスワード有効日数
     */
    @IntRange(min=1, max=999)
    public String passwordValidDays;
    
    /**
     * パスワードリトライ回数 
     */
    @IntRange(min=1, max=999)
    public String totalFailCount;

    /**
     * 使用可能過去パスワード 
     */
    @IntRange(min=1, max=999)
    public String passwordHistCount;
    
    /**
     * パスワード桁数
     */
    @IntRange(min=1, max=999)
    public String passwordLength;
    
    /**
     * パスワード文字種
     */
    public String passwordCharType;
    

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
