/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.rorder;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.rorder.OnlineOrderWorkRelDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * オンライン受注データ取込画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ImportOnlineOrderForm extends
		AbstractSearchForm<OnlineOrderWorkRelDto> {

	/**
	 * 取込みファイル
	 */
	public FormFile uploadFile;

	/**
	 * 取込済を除く
	 */
	public boolean showExist;

	/**
	 * 削除用受注番号
	 */
	public String roId;

	/**
	 * 初期化を行います.
	 */
	public void reset() {
		uploadFile = null;
		searchResultList = null;
		sortColumn = "loadDate";
		sortOrderAsc = true;
		roId = null;
	}

	/**
	 * 取込時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		// 必須チェック
		if (uploadFile == null
				|| !StringUtil.hasLength(uploadFile.getFileName())) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.required", MessageResourcesUtil
							.getMessage("labels.onlineorder.file")));
		} else {
			if (uploadFile.getFileSize() == 0) {
				// 0バイトファイル
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.upload.content.none"));
			}
		}

		return errors;
	}
}
