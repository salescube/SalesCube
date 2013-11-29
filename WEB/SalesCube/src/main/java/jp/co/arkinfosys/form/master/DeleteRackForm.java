/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import org.apache.struts.action.ActionMessages;

/**
 * 棚番マスタ管理（削除）のアクションフォームクラスです.
 * 
 * @author Ark Information Systems
 * 
 */
public class DeleteRackForm {

	/** 棚番コード */
	public String rackCode;
	
	public String updDatetm;
	
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		return errors;
	}
}
