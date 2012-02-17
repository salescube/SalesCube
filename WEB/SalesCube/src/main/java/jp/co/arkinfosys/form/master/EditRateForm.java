/*
 *  Copyright 2009-2010 Ark Information Systems.
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

		
		
		checkMaxLength(name, 60, labelName, errors);

		
		checkMaxLength(sign, 1, labelSign, errors);

		
		checkMaxLength(remarks, 120, labelRemarks, errors);

		
		
		
		if (this.rateTrnList != null) {
			int index = 1;
			Map<String, Object> checkMap = new TreeMap<String, Object>();
			for (RateTrnDto trn : this.rateTrnList) {
				
				
				

				
				if (this.editMode) {
					if (checkRequired(index, trn.startDate, labelStartDate, errors)) {
						
						checkDate(index, trn.startDate, labelStartDate, errors);
					}
				} else {
					if (checkRequired(trn.startDate, labelStartDate, errors)) {
						
						checkDate(trn.startDate, labelStartDate, errors);
					}
				}

				
				if (this.editMode) {
					if (checkRequired(index, trn.rate, labelRate, errors)) {
						
						/**
						 * 当該カラムのDB設計DECIMAL(8,3)に従い5+3形式のDECIMAL型Checkを採用する。
						 * ---以下参照---
						 * DECIMAL(M,D) カラムは小数点の左側に最大M – D 桁を容認します。
						 * http:
						 */
						checkDecimal5_3(index, trn.rate, labelRate, errors);
					}
				} else {
					if (checkRequired(trn.rate, labelRate, errors)) {
						
						checkDecimal5_3(trn.rate, labelRate, errors);
					}
				}

				
				
				

				
				if (this.editMode) {
					checkMaxLength(index, trn.remarks, 120, labelTrnRemarks, errors);
				} else {
					checkMaxLength(trn.remarks, 120, labelTrnRemarks, errors);
				}

				
				if (checkMap.containsKey(trn.startDate)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.doubleStartDate"));
					
					break;
				}
				checkMap.put(trn.startDate, trn);

				index++;
			}
		}

		return errors;
	}
}
