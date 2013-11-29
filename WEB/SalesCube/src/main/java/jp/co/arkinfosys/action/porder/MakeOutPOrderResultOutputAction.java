/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.porder;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractReportWriterAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.MessageResourcesUtil;
import jp.co.arkinfosys.form.ajax.porder.MakeOutPOrderResultOutputForm;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.porder.OutputPOrderSlipService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;


/**
 * 発注書発行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class MakeOutPOrderResultOutputAction extends AbstractReportWriterAction {

	@ActionForm
	@Resource
	protected MakeOutPOrderResultOutputForm makeOutPOrderResultOutputForm;

	/**
	 * 伝票情報取得(伝票発行用)
	 */
	@Resource
	protected OutputPOrderSlipService outputPOrderSlipService;

	/**
	 * ファイル名の装飾
	 */
	private static final String fileNamePrefix = "ORDER";
	private static final String fileNameSuffix = "";
	private static final String fileNamePrefixMulti = "ORDERS";
	private static final String fileNameSuffixMulti = "etc";
	private static final String pdfFileNameExt = ".pdf";
	private static final String xlsFileNameExt = ".xls";

	/**
	 * Excelファイル名を返します(外部呼出し用).
	 * @param slipId 伝票番号
	 * @return ファイル名
	 */
	public String getXlsFileName(String slipId) {
		return fileNamePrefix + slipId + fileNameSuffix + xlsFileNameExt;
	}

	/**
	 * PDFファイル名を返します(外部呼出し用).
	 * @param slipId 伝票番号
	 * @return ファイル名
	 */
	public String getPdfFileName(String slipId) {
		return fileNamePrefix + slipId + fileNameSuffix + pdfFileNameExt;
	}

	/**
	 * 発注伝票の発行数を増加させます.
	 * @throws ServiceException
	 */
	private void incrementPrintCount() throws ServiceException {
		//発行数を＋１する
		for (String slipId : makeOutPOrderResultOutputForm.slipIdList) {
			try {
				outputPOrderSlipService.incrementSlipPrintCount(slipId);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
	}

	/**
	 * Excelファイルを出力します.
	 * @return null
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excel() throws Exception {
		if (makeOutPOrderResultOutputForm.slipIdList != null) {
			super.excel();
			//伝票発行済み
			incrementPrintCount();
		} else {
			ResponseUtil.write(MessageResourcesUtil
					.getMessage("errors.slipIdIsnotChosen"));
		}
		makeOutPOrderResultOutputForm.slipIdList = null;
		return null;
	}

	/**
	 * PDFファイルを出力します.
	 * @return　null
	 * @throws Exception
	 */
	@Execute(urlPattern = "pdf", validator = false)
	public String pdf() throws Exception {
		if (makeOutPOrderResultOutputForm.slipIdList != null) {

			super.pdf();

			//伝票発行済み
			incrementPrintCount();
		} else {
			ResponseUtil.write(MessageResourcesUtil
					.getMessage("errors.slipIdIsnotChosen"));
		}
		makeOutPOrderResultOutputForm.slipIdList = null;
		return null;
	}

	/**
	 * 拡張子を除くファイル名を返します.
	 * @return 拡張子を除くファイル名
	 */
	@Override
	protected String getFilePreffix() {
		if (makeOutPOrderResultOutputForm.slipIdList.size() == 1) {
			return fileNamePrefix
					+ makeOutPOrderResultOutputForm.slipIdList.get(0)
					+ fileNameSuffix;
		} else if (makeOutPOrderResultOutputForm.slipIdList.size() > 1) {
			return fileNamePrefixMulti
					+ makeOutPOrderResultOutputForm.slipIdList.get(0)
					+ fileNameSuffixMulti;
		}
		return null;
	}

	/**
	 * レポートテンプレートIDを返します.
	 * @param index 出力データのインデックス
	 * @return レポートテンプレートID
	 */
	@Override
	protected String getReportId(int index) {
		if ((index >= 0)
				&& (index < makeOutPOrderResultOutputForm.slipIdList.size())) {
			return Constants.REPORT_TEMPLATE.REPORT_ID_L;
		}
		return null;
	}

	/**
	 * 伝票データを返します.
	 * @param index 出力データのインデックス
	 * @return 伝票データ
	 * @throws ServiceException
	 */
	@Override
	protected BeanMap getSlip(int index) throws ServiceException {
		if ((index >= 0)
				&& (index < makeOutPOrderResultOutputForm.slipIdList.size())) {
			return this.outputPOrderSlipService
					.getBeanMapPOrderSlipBySlipId(makeOutPOrderResultOutputForm.slipIdList
							.get(index));
		}
		return null;
	}

	/**
	 * 明細行データを返します.
	 * @param index 出力データのインデックス
	 * @return 明細行データ
	 * @throws ServiceException
	 */
	@Override
	protected List<BeanMap> getDetailList(int index) throws ServiceException {
		if ((index >= 0)
				&& (index < makeOutPOrderResultOutputForm.slipIdList.size())) {
			return this.outputPOrderSlipService
					.getBeanMapListPOrderLinesBySlipId(makeOutPOrderResultOutputForm.slipIdList
							.get(index));
		}
		return null;
	}

}
