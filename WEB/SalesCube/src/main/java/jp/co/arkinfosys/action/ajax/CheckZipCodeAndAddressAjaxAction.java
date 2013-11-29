/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import javax.annotation.Resource;

import jp.co.arkinfosys.service.ZipService;

import org.seasar.struts.annotation.Execute;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.ResponseUtil;

/**
 * 郵便番号と住所の一致をチェックするアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CheckZipCodeAndAddressAjaxAction extends CommonAjaxResources {

	@Resource
	private ZipService zipService;

	/**
	 * 郵便番号
	 */
	@Required
	public String zipCode;

	/**
	 * 住所1
	 */
	@Required
	public String zipAddress1;

	/**
	 * 郵便番号の整合性をチェックします.
	 * @return 郵便番号と住所が一致するか否か
	 * @throws Exception
	 */
	@Execute(validator = true, input = CommonAjaxResources.Mapping.ERROR_JSP)
	public String check() throws Exception {
		try {
			boolean result = true;
			if(zipCode != null && zipAddress1 != null) {
				// 郵便番号と住所両方が指定された場合のみチェックする
				result = zipService.checkZipCodeAndAddress(zipCode,
						zipAddress1);
			}
			ResponseUtil.write(String.valueOf(result));
		} catch (Exception e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return null;
	}

}
