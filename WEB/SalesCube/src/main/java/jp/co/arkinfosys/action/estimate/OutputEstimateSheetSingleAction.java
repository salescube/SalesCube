/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.estimate;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractReportWriterAction;
import jp.co.arkinfosys.common.PrintUtil;
import jp.co.arkinfosys.form.estimate.InputEstimateForm;
import jp.co.arkinfosys.service.EstimateLineService;
import jp.co.arkinfosys.service.EstimateSheetService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 見積入力画面の帳票出力アクションクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputEstimateSheetSingleAction extends AbstractReportWriterAction {

	@ActionForm
	@Resource
	public InputEstimateForm inputEstimateForm;

	@Resource
	protected EstimateSheetService estimateSheetService;

	@Resource
	protected EstimateLineService estimateLineService;

	/**
	 * 帳票テンプレートID
	 */
	public static final String REPORT_ID = "0000B";

	/**
	 * ダウンロードファイル名定義
	 */
	public static final String FILE_PREFFIX = "Estimate";

	public static class Param {
		public static final String ESTIMATE_SHEETID = "estimateSheetId";
	}

	/**
	 * PDFファイルを出力します.
	 * @return null
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String pdf() throws Exception {
		return super.pdf();
	}

	/**
	 * レポートテンプレートIDを取得します.
	 * @param index 出力データのインデックス
	 * @return レポートテンプレートID
	 */
	@Override
	protected String getReportId(int index) {
		if (index != 0) {
			return null;
		}
		return REPORT_ID;
	}

	/**
	 * 拡張子を除くファイル名を取得します.
	 * @return 拡張子を除くファイル名
	 */
	@Override
	protected String getFilePreffix() {
		return FILE_PREFFIX;
	}

	/**
	 * 伝票データを取得します.
	 * @param index
	 * @return 伝票データ
	 * @throws ServiceException
	 */
	@Override
	protected BeanMap getSlip(int index) throws ServiceException {
		if (index != 0) {
			return null;
		}
		return this.estimateSheetService
				.findEstimateSheetByIdSimple(this.inputEstimateForm.estimateSheetId);
	}

	/**
	 * 明細行データを取得します.
	 * @param index
	 * @return 明細行データ
	 * @throws ServiceException
	 */
	@Override
	protected List<BeanMap> getDetailList(int index) throws ServiceException {
		if (index != 0) {
			return null;
		}
		List<BeanMap> lbm = this.estimateLineService
				.findEstimateLinesBySheetIdSimple(this.inputEstimateForm.estimateSheetId);
		PrintUtil.setSpaceToExceptianalProductCode(lbm);
		return lbm;
	}
}
