/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.master;

import org.seasar.struts.annotation.Required;

/**
 * 部門情報（検索）画面の削除処理のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteDeptAjaxForm {

	@Required
	public String deptId;

	@Required
	public String updDatetm;

}
