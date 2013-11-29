/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.CommonAjaxResources;
import jp.co.arkinfosys.entity.Zip;
import jp.co.arkinfosys.form.ajax.master.SearchZipCodeAjaxForm;
import jp.co.arkinfosys.service.ZipService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 郵便番号検索Ajax検索実行アクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchZipCodeAjaxAction extends CommonAjaxResources {
	@ActionForm
	@Resource
	public SearchZipCodeAjaxForm searchZipCodeAjaxForm;

	@Resource
	public ZipService zipService;

	/**
	 * 郵便番号で郵便番号辞書を検索します.
	 * @return {@link Zip}リストの文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.ERROR_JSP)
	public String search() throws Exception {
		try {
			// 住所検索
			List<Zip> resultList = zipService.findAddressByZipCode(
					searchZipCodeAjaxForm.zipCode, null, true);
			if (resultList != null) {
				Iterator<Zip> it = resultList.iterator();
				while (it.hasNext()) {
					Zip item = it.next();
					item.zipAddress1 = (item.zipAddress1 == null ? ""
							: item.zipAddress1);
					item.zipAddress2 = (item.zipAddress2 == null ? ""
							: item.zipAddress2);
				}
			}

			// 検索結果件を設定する
			ResponseUtil.write(JSON.encode(resultList), "text/javascript");
		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}

		return null;
	}
}
