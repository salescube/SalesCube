/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.io.IOException;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;

import org.seasar.struts.annotation.Execute;
import org.seasar.struts.annotation.Required;

/**
 * 顧客名カナを振り込み名義に変換するAjaxアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ConvertPaymentNameAjaxAction extends CommonAjaxResources {

	@Required
	public String customerKana;

	/**
	 * 顧客名カナを振り込み名義に変換します.
	 * @return 振り込み名義
	 */
	@Execute(validator = true, input = CommonAjaxResources.Mapping.ERROR_JSP)
	public String convert() {

		try {
			super.httpResponse.setCharacterEncoding(Constants.CHARSET.UTF8);
			super.httpResponse.getWriter().write(StringUtil.convertPaymentName(this.customerKana));
		} catch (IOException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
		}

		return null;
	}

}
