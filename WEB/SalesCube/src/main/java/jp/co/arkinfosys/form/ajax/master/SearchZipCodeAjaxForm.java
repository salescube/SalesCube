/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.master;

import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * 郵便番号検索画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchZipCodeAjaxForm {
	public String zipCode;

	/**
	 * 検索条件の入力チェックを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		// 検索条件の有無チェック
		if (!StringUtil.hasLength(zipCode)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.condition.insufficiency"));
		}
		return errors;
	}
}
