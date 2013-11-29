/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax;

import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * 売上伝票種別情報を保持するアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchBillCategoryAjaxForm {


	public String salesSlipCategory;

	/**
	 * 入力された検索条件のバリデートを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		// 検索条件の有無チェック
		if (!StringUtil.hasLength(salesSlipCategory)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.condition.insufficiency"));
		}
		return errors;
	}

}
