/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Maxlength;

/**
 * お知らせ編集画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class NewsForm {
	/**
     * おしらせ
     */
	@Maxlength(maxlength = 512, arg0 = @Arg(key = "labels.newsDesc"))
    public String description;

	/**
	 * 更新権限
	 */
	public boolean isUpdate = true;

	/**
     * 更新日時
     */
    public String updDatetm;

	/**
	 * 登録時のバリデートを行います.
	 *
	 * @return　表示するメッセージ
	 */
    public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		return errors;
    }

}
