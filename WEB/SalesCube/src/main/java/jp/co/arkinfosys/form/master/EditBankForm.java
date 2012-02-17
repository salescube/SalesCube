/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.master;

import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

/**
 * 銀行画面（登録・編集）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class EditBankForm extends AbstractEditForm {

	/** マスタID */
	public String bankId;

	/** 銀行コード */
	@Required
	@Maxlength(maxlength=4)
	public String bankCode;

	/** 銀行名 */
	@Required
	@Maxlength(maxlength=20)
	public String bankName;

	/** 店番 */
	@Required
	@Maxlength(maxlength=3)
	public String storeCode;

	/** 店名 */
	@Required
	@Maxlength(maxlength=20)
	public String storeName;

	/** 科目 */
	@Required
	public String dwbType;

	/** 口座番号 */
	@Required
	@Maxlength(maxlength=7)
	public String accountNum;

	@Override
	public void initialize() {
		bankId = "";
		bankCode = "";
		bankName = "";
		storeCode = "";
		storeName = "";
		dwbType = "";
		accountNum = "";
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		return errors;
	}
}
