/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.report;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.form.report.OutputBalanceListForm;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * 残高一覧表出力画面の処理を実行するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputBalanceListAction extends CommonResources {
	@ActionForm
	@Resource
	private OutputBalanceListForm outputBalanceListForm;

	/**
	 * 出力対象リスト
	 */
    public List<LabelValueBean> outputTargetList;

    /**
     * 画面遷移用のマッピングクラス
     */
    public static class Mapping {
		public static final String INPUT = "outputBalanceList.jsp";
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
		outputBalanceListForm.outputTarget = Constants.OUTPUT_BALANCE_TARGET.VALUE_PORDER;

		return OutputBalanceListAction.Mapping.INPUT;
	}

    /**
     * プルダウンの初期化を行います.
     * @throws Exception
     */
    protected void initList() throws ServiceException {
    	outputTargetList = ListUtil.getOutputBalanceTargetList();
    }
}