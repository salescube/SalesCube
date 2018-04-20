/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import jp.co.arkinfosys.common.Constants;

import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Mask;
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
	@Mask(mask = Constants.CODE_MASK.HANKAKU_MASK)
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

	/** 口座名義 */
	@Required
	@Maxlength(maxlength=20)
	public String accountOwnerName;

	/** 口座名義カナ */
	@Required
	@Maxlength(maxlength=20)
	public String accountOwnerKana;

	/** 有効 */
	public String valid = "1";

	@Override
	public void initialize() {
		bankId = "";
		bankCode = "";
		bankName = "";
		storeCode = "";
		storeName = "";
		dwbType = "";
		accountNum = "";
		accountOwnerName = "";
		accountOwnerKana = "";
//		valid = "1";
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		return errors;
	}

    public void reset() {
    	// チェックされていないときに、nullを送信するためにリセット
		valid = Constants.VALID_FLAG.INVALID;
    }
}
