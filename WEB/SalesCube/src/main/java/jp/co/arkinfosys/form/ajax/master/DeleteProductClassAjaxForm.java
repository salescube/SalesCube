/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.master;

import org.seasar.struts.annotation.Required;

/**
 *  分類マスタ管理（検索）画面の削除処理のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class DeleteProductClassAjaxForm {

	@Required
	public String classCode1;

	public String classCode2;

	public String classCode3;

	@Required
	public String updDatetm;

}
