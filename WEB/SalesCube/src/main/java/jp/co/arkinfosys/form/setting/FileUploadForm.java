/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.setting.FileInfoDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

/**
 * ファイル登録（登録・編集）画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class FileUploadForm extends AbstractSearchForm<FileInfoDto> {

	/**ファイルID*/
	public String fileId;

	/**
	 * タイトル
	 */
	@Required(arg0 = @Arg(key = "labels.file.title", resource = true))
	@Maxlength(maxlength = 60, arg0 = @Arg(key = "labels.file.title", resource = true))
	public String title;

	/*ファイル名*/
	public String fileName;

	/**
	 * アップロードファイルオブジェクト
    */
	/* 削除 @Required(arg0 = @Arg(key = "labels.file.formFile", resource = true))*/

	@Binding(bindingType = BindingType.NONE)
	public FormFile formFile;

	/**
	 * 公開レベル
	 */
	@Required
	public String openLevel;

	/**
	 * 公開範囲リスト
	 */
	public List<LabelValueBean> openLevelList = new ArrayList<LabelValueBean>();

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
	 * 編集フラグ
	 */
	public boolean editMode = false;

	/**
	 * 更新権限
	 */
	public boolean isUpdate = false;

	/**
	 * フォームを初期化します.
	 */
	public void reset() {
		this.fileId = null;
		this.title = null;
		this.formFile = null;
		this.openLevel = Constants.MENU_VALID_LEVEL.VALID_LIMITATION;
		this.openLevelList.clear();


		this.editMode = false;
		this.isUpdate = false;
	}

	/**
	 * 登録時のバリデートを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		if (this.formFile.getFileSize() == 0) {
			// 0バイトファイル
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.upload.size.zero"));
		}

		return errors;
	}

}
