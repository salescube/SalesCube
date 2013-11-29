/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.List;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.CategoryDto;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * 区分情報ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ShowCategoryDialogForm {

	public String dialogId;

	public String categoryId;

	public String formType;

	public List<CategoryDto> categoryDtoList;

	/**
	 * パラメータの入力チェックを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		// 検索条件の有無チェック
		if (!StringUtil.hasLength(categoryId)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.condition.insufficiency"));
		}
		return errors;
	}
}
