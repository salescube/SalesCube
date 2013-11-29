/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.co.arkinfosys.dto.master.RateTrnDto;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * レート画面（登録・編集）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class EditRateForm extends AbstractEditForm {

	/** 削除された行 */
	public String deletedRateId;

	/** レートタイプID */
	public String rateId;

	/** レートタイプ名称 */
	@Required(arg0=@Arg(key = "labels.rate.name", resource = true))
	public String name;

	/** 通貨記号 */
	public String sign;

	/** レートタイプ備考 */
	public String remarks;

	/** レートデータ */
	public List<RateTrnDto> rateTrnList = new ArrayList<RateTrnDto>();

	// レートデータ検索用

	/** レートデータの件数 */
	public int rateTrnListSize;

	@Override
	public void initialize() {
		deletedRateId = "";
		rateId = "";
		name = "";
		sign = "";
		remarks = "";
		rateTrnList = new ArrayList<RateTrnDto>();
		rateTrnListSize = 0;
	}

	/**
	 * 初期値を設定します.
	 */
	public void reset() {
		this.initialize();
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		String labelName = MessageResourcesUtil.getMessage("labels.rate.name");
		String labelSign = MessageResourcesUtil.getMessage("labels.rate.sign");
		String labelRate = MessageResourcesUtil.getMessage("labels.rate.rate");
		String labelStartDate = MessageResourcesUtil.getMessage("labels.rate.startdate");
		String labelRemarks = MessageResourcesUtil.getMessage("labels.rate.remarks");
		String labelTrnRemarks = MessageResourcesUtil.getMessage("labels.ratetrn.remarks");

		// 長さチェック
		// レートタイプ名称
		checkMaxLength(name, 60, labelName, errors);

		// 記号
		checkMaxLength(sign, 1, labelSign, errors);

		// レートタイプ備考
		checkMaxLength(remarks, 120, labelRemarks, errors);

		//
		// レートデータチェック
		//
		if (this.rateTrnList != null) {
			int index = 1;
			Map<String, Object> checkMap = new TreeMap<String, Object>();
			for (RateTrnDto trn : this.rateTrnList) {
				//
				// 必須
				//

				// 適用開始日
				if (this.editMode) {
					if (checkRequired(index, trn.startDate, labelStartDate, errors)) {
						// 型チェック
						checkDate(index, trn.startDate, labelStartDate, errors);
					}
				} else {
					if (checkRequired(trn.startDate, labelStartDate, errors)) {
						// 型チェック
						checkDate(trn.startDate, labelStartDate, errors);
					}
				}

				// レート
				if (this.editMode) {
					if (checkRequired(index, trn.rate, labelRate, errors)) {
						// 型チェック
						/**
						 * 当該カラムのDB設計DECIMAL(8,3)に従い5+3形式のDECIMAL型Checkを採用する。
						 * ---以下参照---
						 * DECIMAL(M,D) カラムは小数点の左側に最大M – D 桁を容認します。
						 * http://dev.mysql.com/doc/refman/5.1/ja/precision-math-decimal-changes.html
						 */
						checkDecimal5_3(index, trn.rate, labelRate, errors);
					}
				} else {
					if (checkRequired(trn.rate, labelRate, errors)) {
						// 型チェック
						checkDecimal5_3(trn.rate, labelRate, errors);
					}
				}

				//
				// 長さ
				//

				// レートデータ備考
				if (this.editMode) {
					checkMaxLength(index, trn.remarks, 120, labelTrnRemarks, errors);
				} else {
					checkMaxLength(trn.remarks, 120, labelTrnRemarks, errors);
				}

				// 重複
				if (checkMap.containsKey(trn.startDate)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.doubleStartDate"));
					// ここまで
					break;
				}
				checkMap.put(trn.startDate, trn);

				index++;
			}
		}

		return errors;
	}
}
