/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.form.stock.OutputStockReportForm;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 在庫残高表画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputStockReportAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "outputStockReport.jsp";
	}

	@ActionForm
	@Resource
	private OutputStockReportForm outputStockReportForm;

	/**
	 * 初期表示処理を行います.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		outputStockReportForm.targetYm = null;
		return Mapping.INPUT;
	}
}
