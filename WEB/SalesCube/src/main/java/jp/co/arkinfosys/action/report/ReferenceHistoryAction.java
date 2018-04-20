/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.report;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.form.report.ReferenceHistoryForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 履歴参照画面の処理を実行するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ReferenceHistoryAction extends CommonResources {
	@ActionForm
	@Resource
	private ReferenceHistoryForm referenceHistoryForm;

	@Resource
	private CategoryService categoryService;

	/**
	 * 出力対象リスト
	 */
    public List<LabelValueBean> outputTargetList;

    /**
     * アクションタイプリスト
     */
    public List<LabelValueBean> actionTypeList;

    /**
     * 入出庫伝票区分リスト
     */
    public List<LabelValueBean> eadSlipCategoryList;

    /**
     * 画面遷移用のマッピングクラス
     */
    public static class Mapping {
		public static final String INPUT = "referenceHistory.jsp";
    }

	/**
	 * 初期表示処理を行います.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception{
		// プルダウンの初期化
		initList();

		// 出力対象プルダウンの初期値を設定
		referenceHistoryForm.outputTarget = Constants.REFERENCE_HISTORY_TARGET.VALUE_ESTIMATE;

		return ReferenceHistoryAction.Mapping.INPUT;
	}

	 /**
     * プルダウンの初期化を行います.
     * @throws Exception
     */
    protected void initList() throws ServiceException {
    	outputTargetList = ListUtil.getReferenceHistoryTargetList();
    	actionTypeList = ListUtil.getReferenceMstActionTypeList();
    	eadSlipCategoryList = categoryService.findCategoryLabelValueBeanListById(Categories.EAD_SLIP_CATEGORY);
    }
}