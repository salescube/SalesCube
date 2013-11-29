/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.List;

import jp.co.arkinfosys.dto.master.InitMstDto;
import jp.co.arkinfosys.service.InitMstService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Required;

/**
 * マスタ初期値設定ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class MasterDefaultSettingDialogForm {

	@Required
	public String dialogId;

	@Required
	public String tableName;

	public List<InitMstDto> initMstDtoList;

	/**
	 * 検索条件の入力チェックを行います.
	 *
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		if (this.initMstDtoList != null) {
			for (InitMstDto dto : this.initMstDtoList) {
				if (InitMstService.DataType.STRING_VALUE
						.equals(dto.useDataType)) {
					if (dto.strData != null
							&& dto.strData.length() > dto.useStrSize.intValue()) {
						// 長さチェック
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.length", dto.title,
										dto.useStrSize));
					}
				} else if (InitMstService.DataType.INTEGER_VALUE
						.equals(dto.useDataType)) {
					try {
						new Integer(dto.numData);
					} catch (NumberFormatException e) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.integer", dto.title));
					}
				} else if (InitMstService.DataType.FLOAT_VALUE
						.equals(dto.useDataType)) {
					try {
						new Float(dto.fltData);
					} catch (NumberFormatException e) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.float",
										dto.title));
					}
				}
			}
		}

		return errors;
	}
}
