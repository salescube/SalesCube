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
 * 伝票呼出ダイアログ（呼出元伝票：受注伝票）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CopySlipROrderConditionForm {

	public static final String SLIP_NAME = "RORDER";

	/**
	 * 受注伝票番号
	 */
	public String roSlipId;

	/**
	 * 受付番号
	 */
	public String receptNo;

	/**
	 * 受注開始日付
	 */
	public String roDateFrom;

	/**
	 * 受注終了日付
	 */
	public String roDateTo;

	/**
	 * 出荷開始日付
	 */
	public String shipDateFrom;

	/**
	 * 出荷終了日付
	 */
	public String shipDateTo;

	/**
	 * 顧客コード
	 */
	public String customerCode;

	/**
	 * 顧客名
	 */
	public String customerName;

	/**
	 * 残分のみ
	 */
	public String restOnly;

	/**
	 * 入力チェックを行います.
	 *
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		if (StringUtil.hasLength(this.roSlipId)) {
			try {
				Integer.parseInt(StringUtil.trimBlank(this.roSlipId));
			} catch (NumberFormatException e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.integer", MessageResourcesUtil
								.getMessage("labels.roSlipId")));
			}
		}

		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);
		df.setLenient(true);
		// 受注日フォーマットチェック
		try {
			if (StringUtil.hasLength(this.roDateFrom)) {
				df.parse(StringUtil.trimBlank(this.roDateFrom));
			}
			if (StringUtil.hasLength(this.roDateTo)) {
				df.parse(StringUtil.trimBlank(this.roDateTo));
			}
		} catch (ParseException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.date", MessageResourcesUtil
							.getMessage("labels.roDate")));
		}

		// 出荷日フォーマットチェック
		try {
			if (StringUtil.hasLength(this.shipDateFrom)) {
				df.parse(StringUtil.trimBlank(this.shipDateFrom));
			}
			if (StringUtil.hasLength(this.shipDateTo)) {
				df.parse(StringUtil.trimBlank(this.shipDateTo));
			}
		} catch (ParseException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.date", MessageResourcesUtil
							.getMessage("labels.shipDate")));
		}
		return errors;
	}
}
