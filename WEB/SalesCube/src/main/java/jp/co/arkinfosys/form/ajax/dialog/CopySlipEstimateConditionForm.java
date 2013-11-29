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
 * 伝票呼出ダイアログ（呼出元伝票：見積伝票）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CopySlipEstimateConditionForm {

	public static final String SLIP_NAME = "ESTIMATE";

	/**
	 * 伝票番号
	 */
	public String estimateSheetId;

	/**
	 * 開始日付
	 */
	public String estimateDateFrom;

	/**
	 * 終了日付
	 */
	public String estimateDateTo;

	/**
	 * 提出先名
	 */
	public String submitName;

	/**
	 * 件名
	 */
	public String title;

	/**
	 * 入力チェックを行います.
	 *
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();


		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);
		df.setLenient(true);
		try {
			if (StringUtil.hasLength(this.estimateDateFrom)) {
				df.parse(StringUtil.trimBlank(this.estimateDateFrom));
			}
			if (StringUtil.hasLength(this.estimateDateTo)) {
				df.parse(StringUtil.trimBlank(this.estimateDateTo));
			}
		} catch (ParseException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.date", MessageResourcesUtil
							.getMessage("labels.estimateDate")));
		}
		return errors;
	}
}
