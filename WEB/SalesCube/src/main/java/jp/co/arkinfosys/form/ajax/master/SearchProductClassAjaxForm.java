/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.master;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import jp.co.arkinfosys.common.StringUtil;

/**
 *  商品分類マスタ検索画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchProductClassAjaxForm {

	public String classCode1;

	public String classCode2;

	public String classCode3;

	/**
	 * 検索条件の入力チェックを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		// 検索条件の有無チェック
		if (!StringUtil.hasLength(classCode1)
				&& !StringUtil.hasLength(classCode2)
				&& !StringUtil.hasLength(classCode3)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.condition.insufficiency"));
		}
		return errors;
	}
}
