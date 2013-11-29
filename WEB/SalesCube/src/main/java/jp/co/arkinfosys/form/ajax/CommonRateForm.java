/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax;

import org.seasar.struts.annotation.Required;

/**
 * レートID情報を保持するアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class CommonRateForm {
	/**
	 * 	検索条件となるレートID
	 */
	@Required
	public String rateId;


}
