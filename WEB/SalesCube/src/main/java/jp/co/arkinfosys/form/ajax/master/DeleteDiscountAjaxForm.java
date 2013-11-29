/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.master;

import org.seasar.struts.annotation.Required;

/**
 * 割引マスタ管理（検索）画面の削除処理のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class DeleteDiscountAjaxForm {

	@Required
	public String discountId;

	@Required
	public String updDatetm;
}
