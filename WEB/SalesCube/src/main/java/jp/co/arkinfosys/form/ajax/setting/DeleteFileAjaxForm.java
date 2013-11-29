/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.setting;

import org.seasar.struts.annotation.Required;

/**
 * ファイル情報（検索）の削除処理のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class DeleteFileAjaxForm {

	@Required
	public String fileId;

	@Required
	public String updDatetm;

}
