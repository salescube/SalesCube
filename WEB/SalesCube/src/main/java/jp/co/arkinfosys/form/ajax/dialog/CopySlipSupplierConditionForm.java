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
 * 伝票呼出ダイアログ（呼出元伝票：仕入伝票）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CopySlipSupplierConditionForm {

	public static final String SLIP_NAME = "SUPPLIER";

	/**
	 * 仕入伝票番号
	 */
	public String supplierSlipId;

	/**
	 * 未払い分のみ
	 */
	public boolean unPaidFlag;

	/**
	 * 開始日付
	 */
	public String supplierDateFrom;

	/**
	 * 終了日付
	 */
	public String supplierDateTo;

	/**
	 * 仕入先コード
	 */
	public String supplierCode;

	/**
	 * 仕入先名
	 */
	public String supplierName;

	/**
	 * 入力チェックを行います.
	 *
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		if (StringUtil.hasLength(this.supplierSlipId)) {
			try {
				Integer.parseInt(StringUtil.trimBlank(this.supplierSlipId));
			} catch (NumberFormatException e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.integer", MessageResourcesUtil
								.getMessage("labels.supplierSlipId")));
			}
		}

		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);
		df.setLenient(true);
		try {
			if (StringUtil.hasLength(this.supplierDateFrom)) {
				df.parse(StringUtil.trimBlank(this.supplierDateFrom));
			}
			if (StringUtil.hasLength(this.supplierDateTo)) {
				df.parse(StringUtil.trimBlank(this.supplierDateTo));
			}
		} catch (ParseException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.date", MessageResourcesUtil
							.getMessage("labels.supplierDate")));
		}
		return errors;
	}
}
