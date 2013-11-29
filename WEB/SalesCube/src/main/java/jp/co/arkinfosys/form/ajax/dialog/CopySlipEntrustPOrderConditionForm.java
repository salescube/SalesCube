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
 * 伝票呼出ダイアログ（呼出元伝票：委託発注伝票)のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CopySlipEntrustPOrderConditionForm {

	public static final String SLIP_NAME = "ENTRUST_PORDER";
	/**
	 * 発注伝票番号
	 */
	public String poSlipId;

	/**
	 * 開始日付
	 */
	public String poDateFrom;

	/**
	 * 終了日付
	 */
	public String poDateTo;

	/**
	 * 仕入先コード
	 */
	public String supplierCode;

	/**
	 * 仕入先名
	 */
	public String supplierName;

	/**
	 * 残分のみ
	 */
	public boolean onlyRestQuantityExist;

	/**
	 * 商品コード
	 */
	public String productCode;

	/**
	 * 商品名
	 */
	public String productAbstract;

	/**
	 * 伝票複写対象の発注伝票明細状態
	 */
	public String targetPoLineStatus;

	/**
	 * 未払いのみ
	 */
	public boolean onlyUnpaid;

	/**
	 * 入力チェックを行います.
	 *
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		if (StringUtil.hasLength(this.poSlipId)) {
			try {
				Integer.parseInt(StringUtil.trimBlank(this.poSlipId));
			} catch (NumberFormatException e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.integer", MessageResourcesUtil
								.getMessage("labels.poSlipId")));
			}
		}

		// 委託入出庫区分が選択されていない場合エラー
		if("".equals(targetPoLineStatus) ) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"errors.noEntrustEadCategory"));
		}

		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);
		df.setLenient(true);
		try {
			if (StringUtil.hasLength(this.poDateFrom)) {
				df.parse(StringUtil.trimBlank(this.poDateFrom));
			}
			if (StringUtil.hasLength(this.poDateTo)) {
				df.parse(StringUtil.trimBlank(this.poDateTo));
			}
		} catch (ParseException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.date", MessageResourcesUtil
							.getMessage("labels.poDate")));
		}
		return errors;
	}
}
