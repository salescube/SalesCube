/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.form.master.SearchProductForm;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 商品マスタExcelファイル出力を行うアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class DownloadProductExcelAction extends CommonResources {

	@ActionForm
	@Resource
	private SearchProductForm searchProductForm;

	@Resource
	private ProductService productService;

	/**
	 * ダウンロードを行います.
	 * @return null
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String download() throws Exception {
		try {
			BeanMap params = Beans.createAndCopy(BeanMap.class,
					this.searchProductForm).excludesWhitespace().execute();

			this.productService.downloadProductExcel(params,
					this.searchProductForm.sortColumn,
					this.searchProductForm.sortOrderAsc, super.httpRequest,
					super.httpResponse);

			return null;
		} catch (ServiceException e) {
			super.errorLog(e);
		}
		return null;
	}
}
