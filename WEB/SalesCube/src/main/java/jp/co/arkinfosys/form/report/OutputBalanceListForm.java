/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.report;

import java.text.ParseException;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.annotation.Validwhen;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 残高一覧表出力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputBalanceListForm {
	/**
	 * 検索対象
	 */
    public String outputTarget;

    /**
     * 対象年月
     */
    @Required
    @DateType(datePatternStrict = "yyyy/MM", msg = @Msg(key = "errors.report.invalidTargetDate"))
    public String targetDate;

    /**
     * 仕入先コード
     */
    @Validwhen(test="((outputTarget!='1') or (*this*!=null))", msg = @Msg(key = "errors.required"))
    public String supplierCode;

    /**
     * 顧客コードFrom
     */
    @Maxlength(maxlength = 15)
    public String customerCodeFrom;

    /**
     * 顧客コードTo
     */
    @Maxlength(maxlength = 15)
    public String customerCodeTo;

    /**
	 * 全フィールドをクリアします.
	 */
	public void reset() {
		outputTarget = null;
		targetDate = null;
		supplierCode = null;
		customerCodeFrom = null;
		customerCodeTo = null;
	}

	/**
	 * Excel出力時のバリデートを行います.
	 * @return　表示するメッセージ
	 */
	public ActionErrors validate() {
		targetDate = StringUtil.trimBlank(targetDate);

		ActionErrors errors = new ActionErrors();

		if(!StringUtil.hasLength(targetDate)) {
			// 検索条件未入力の場合
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.required","targetDate"));
		}

		try {
			//未来日チェック
			if( ValidateUtil.dateIsFuture(ValidateUtil.getLastDateOfMonthFromYmFormat(targetDate)) ) {
				//未来日の場合、当月かどうかチェック
				if( ValidateUtil.dateIsNotThisMoon (targetDate)) {
						//当月出ない場合、未来日エラーとする
						errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dateFuture", MessageResourcesUtil.getMessage("labels.targetDate")));
				}
			}
		} catch(ParseException e) {
		}

		return errors;
	}
}
