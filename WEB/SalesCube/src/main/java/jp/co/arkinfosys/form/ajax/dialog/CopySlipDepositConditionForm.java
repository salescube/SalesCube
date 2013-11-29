/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 伝票呼出ダイアログ（呼出元伝票：入金伝票）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CopySlipDepositConditionForm {

	public static final String SLIP_NAME = "DEPOSIT";

	/**
	 * 入金伝票番号
	 */
	public String depositSlipId;

	/**
	 * 開始日付
	 */
	public String depositDateFrom;

	/**
	 * 終了日付
	 */
	public String depositDateTo;

	/**
	 * 顧客コード
	 */
	public String customerCode;

	/**
	 * 顧客名
	 */
	public String customerName;

	/**
	 * 入力チェックを行います.
	 *
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		if (StringUtil.hasLength(this.depositSlipId)) {
			try {

				Integer.parseInt(StringUtil.trimBlank(this.depositSlipId));
			} catch (NumberFormatException e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.integer", MessageResourcesUtil
								.getMessage("labels.depositSlipId")));
			}
		}

		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);
		df.setLenient(true);
		try {
			if (StringUtil.hasLength(this.depositDateFrom)) {
				df.parse(StringUtil.trimBlank(this.depositDateFrom));
			}
			if (StringUtil.hasLength(this.depositDateTo)) {
				df.parse(StringUtil.trimBlank(this.depositDateTo));
			}
		} catch (ParseException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.date", MessageResourcesUtil
							.getMessage("labels.depositDate")));
		}
		return errors;
	}

}
