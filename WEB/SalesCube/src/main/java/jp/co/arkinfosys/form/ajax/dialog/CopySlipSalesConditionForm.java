/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 伝票呼出ダイアログ（呼出元伝票：売上伝票）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CopySlipSalesConditionForm {
	@Resource
	public HttpServletRequest httpRequest;

	public static final String SLIP_NAME = "SALES";

	/**
	 * 売上伝票番号
	 */
	public String salesSlipId;

	/**
	 * 開始日付
	 */
	public String salesDateFrom;

	/**
	 * 終了日付
	 */
	public String salesDateTo;

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

		if (StringUtil.hasLength(this.salesSlipId)) {
			try {
				Integer.parseInt(StringUtil.trimBlank(this.salesSlipId));
			} catch (NumberFormatException e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.integer", MessageResourcesUtil
								.getMessage("labels.salesSlipId")));
			}
		}

		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);
		df.setLenient(true);
		try {
			if (StringUtil.hasLength(this.salesDateFrom)) {
				df.parse(StringUtil.trimBlank(this.salesDateFrom));
			}
			if (StringUtil.hasLength(this.salesDateTo)) {
				df.parse(StringUtil.trimBlank(this.salesDateTo));
			}
		} catch (ParseException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.date", MessageResourcesUtil
							.getMessage("labels.salesDate")));
		}
		return errors;
	}
}
