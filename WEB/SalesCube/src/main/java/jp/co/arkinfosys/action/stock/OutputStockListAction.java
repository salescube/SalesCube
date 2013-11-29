/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.form.stock.OutputStockListForm;
import jp.co.arkinfosys.service.stock.OutputStockListService;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 在庫一覧表画面表示アクションクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputStockListAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "outputStockList.jsp";
	}

	@ActionForm
	@Resource
	private OutputStockListForm outputStockListForm;

	@Resource
	private OutputStockListService outputStockListService;

    /**
     * 棚種別の選択値リスト
     */
    public List<LabelValueBean> rackCategoryList;

	/**
	 * 初期表示処理を行います.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		// 初期値設定
		outputStockListForm.radioCond2 = "0";
		rackCategoryList = outputStockListService.getRackCategoryList();
		rackCategoryList = ListUtil.addEmptyLabelValue(rackCategoryList);

		return Mapping.INPUT;
	}

}
