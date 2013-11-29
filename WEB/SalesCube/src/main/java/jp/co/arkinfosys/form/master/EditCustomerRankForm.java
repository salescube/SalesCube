/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.math.BigDecimal;

import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 顧客ランクマスタ管理（登録・編集）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class EditCustomerRankForm extends AbstractEditForm {

	/** 顧客ランクコード */
	public String rankCode;

	/** 顧客ランク名 */
	@Required
	public String rankName;

	/** 値引率 */
	@Required
	public String rankRate;

	/** 売上回数FROM */
	public String roCountFrom;

	/** 在籍期間FROM */
	public String enrollTermFrom;

	/** 離脱期間FROM */
	public String defectTermFrom;

	/** 月平均売上額FROM */
	public String roMonthlyAvgFrom;

	/** 送料区分 */
	@Required
	public String postageType;

	/** 備考 */
	public String remarks;

	/** 売上回数TO */
	public String roCountTo;

	/** 在籍期間TO */
	public String enrollTermTo;

	/** 離脱期間TO */
	public String defectTermTo;

	/** 月平均売上額TO */
	public String roMonthlyAvgTo;

	@Override
	public void initialize() {
		rankCode = "";
		rankName = "";
		rankRate = "";
		roCountFrom = "";
		enrollTermFrom = "";
		defectTermFrom = "";
		roMonthlyAvgFrom = "";
		postageType = "";
		remarks = "";
		updDatetm = "";
		roCountTo = "";
		enrollTermTo = "";
		defectTermTo = "";
		roMonthlyAvgTo = "";
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		String labelRankName = MessageResourcesUtil
				.getMessage("labels.rankName");
		String labelRoCountFrom = MessageResourcesUtil
				.getMessage("labels.roCountFrom");
		String labelRoCountTo = MessageResourcesUtil
				.getMessage("labels.roCountTo");
		String labelEnrollTermFrom = MessageResourcesUtil
				.getMessage("labels.enrollTermFrom");
		String labelEnrollTermTo = MessageResourcesUtil
				.getMessage("labels.enrollTermTo");
		String labelDefectTermFrom = MessageResourcesUtil
				.getMessage("labels.defectTermFrom");
		String labelDefectTermTo = MessageResourcesUtil
				.getMessage("labels.defectTermTo");
		String labelRoMonthlyAvgFrom = MessageResourcesUtil
				.getMessage("labels.roMonthlyAvgFrom");
		String labelRoMonthlyAvgTo = MessageResourcesUtil
				.getMessage("labels.roMonthlyAvgTo");

		//
		// 長さ
		//

		// 顧客ランク名 60文字
		checkMaxLength(rankName, 60, labelRankName, errors);

		// 売上回数
		boolean isError = true;
		if (StringUtil.hasLength(roCountFrom)) {
			isError = !checkInteger(roCountFrom, labelRoCountFrom, errors);
		}
		if (StringUtil.hasLength(roCountTo)) {
			isError = !checkInteger(roCountTo, labelRoCountTo, errors)
					|| isError;
		}
		// 整合性チェック
		if (!isError && StringUtil.hasLength(roCountFrom)
				&& StringUtil.hasLength(roCountTo)) {
			int roCountFromVal = Integer.parseInt(roCountFrom);
			int roCountToVal = Integer.parseInt(roCountTo);
			if (roCountFromVal >= roCountToVal) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.input.invalid", MessageResourcesUtil
								.getMessage("labels.roCount")));
			}
		}

		// 在籍期間
		isError = true;
		if (StringUtil.hasLength(enrollTermFrom)) {
			isError = !checkIntegerPlus(enrollTermFrom, labelEnrollTermFrom,
					errors);
		}
		if (StringUtil.hasLength(enrollTermTo)) {
			isError = !checkIntegerPlus(enrollTermTo, labelEnrollTermTo, errors)
					|| isError;
		}
		// 整合性チェック
		if (!isError && StringUtil.hasLength(enrollTermFrom)
				&& StringUtil.hasLength(enrollTermTo)) {
			int enrollTermFromVal = Integer.parseInt(enrollTermFrom);
			int enrollTermToVal = Integer.parseInt(enrollTermTo);
			if (enrollTermFromVal >= enrollTermToVal) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.input.invalid", MessageResourcesUtil
								.getMessage("labels.enrollTerm")));
			}
		}

		// 離脱期間
		isError = true;
		if (StringUtil.hasLength(defectTermFrom)) {
			isError = !checkIntegerPlus(defectTermFrom, labelDefectTermFrom,
					errors);
		}
		if (StringUtil.hasLength(defectTermTo)) {
			isError = !checkIntegerPlus(defectTermTo, labelDefectTermTo, errors)
					|| isError;
		}
		// 整合性チェック
		if (!isError && StringUtil.hasLength(defectTermFrom)
				&& StringUtil.hasLength(defectTermTo)) {
			int defectTermFromVal = Integer.parseInt(defectTermFrom);
			int defectTermToVal = Integer.parseInt(defectTermTo);
			if (defectTermFromVal >= defectTermToVal) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.input.invalid", MessageResourcesUtil
								.getMessage("labels.defectTerm")));
			}
		}

		//月平均売上額
		isError = true;
		if (StringUtil.hasLength(roMonthlyAvgFrom)) {
			isError = !checkDecimal(roMonthlyAvgFrom, labelRoMonthlyAvgFrom,
					12, 3, errors);
		}
		if (StringUtil.hasLength(roMonthlyAvgTo)) {
			isError = !checkDecimal(roMonthlyAvgTo, labelRoMonthlyAvgTo, 12, 3,
					errors)
					|| isError;
		}
		if (!isError && StringUtil.hasLength(roMonthlyAvgFrom)
				&& StringUtil.hasLength(roMonthlyAvgTo)) {
			BigDecimal roMonthlyAvgFromVal = new BigDecimal(roMonthlyAvgFrom);
			BigDecimal roMonthlyAvgToVal = new BigDecimal(roMonthlyAvgTo);
			if (roMonthlyAvgFromVal.compareTo(roMonthlyAvgToVal) >= 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.input.invalid", MessageResourcesUtil
								.getMessage("labels.roMonthlyAvg")));
			}
		}

		return errors;
	}

}
