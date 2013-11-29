/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.dto.master.TaxRateDto;
import jp.co.arkinfosys.entity.CategoryTrn;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 税画面（登録・編集）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class EditTaxRateForm extends AbstractEditForm {

	/** 引数の税区分ＩＤ */
	public String taxTypeCategory;

	/** 税区分名称 */
	public CategoryTrn category;

	/** リスト */
	public List<TaxRateDto> taxRateList;

	/**
	 * フォームを初期化します.
	 * 未使用です.
	 */
	@Override
	public void initialize() {
		// 削除がないので実装なし
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		String labelStartDate = MessageResourcesUtil.getMessage("labels.master.startDate");
		String labelTaxRate = MessageResourcesUtil.getMessage("labels.master.taxRate");

		int index = 1;
		if (taxRateList != null) {
			Map<String, TaxRateDto> doubleCheck = new HashMap<String, TaxRateDto>();
			for (TaxRateDto dto : taxRateList) {
				//
				// 基本情報チェック
				//

				// 開始年月日
				if (checkRequired(index, dto.startDate, labelStartDate, errors)) {

					// 日付型
					checkDate(index, dto.startDate, labelStartDate, errors);
				}

				// 税率
				if (checkRequired(index, dto.taxRate, labelTaxRate, errors)) {

					// 税率なので　０～１００の範囲で整数入力
					checkIntegerRange(index, dto.taxRate, labelTaxRate, errors, 0, 100);
				}

				// 開始日重複チェック
				if (dto.startDate!=""){
					if (doubleCheck.containsKey(dto.startDate)) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.doubleTaxRate"));
						break;
					}
					doubleCheck.put(dto.startDate, dto);
				}
				index++;
			}
		}
		return errors;
	}

	/**
	 * 税リストを初期化します.
	 */
	public void reset() {
		if (this.taxRateList == null) {
			this.taxRateList = new ArrayList<TaxRateDto>();
		}
	}
}
