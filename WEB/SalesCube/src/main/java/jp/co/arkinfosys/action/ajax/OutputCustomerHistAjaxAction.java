/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;


import jp.co.arkinfosys.dto.report.ReportColumnInfoDto;
import jp.co.arkinfosys.form.master.OutputCustomerHistForm;
import jp.co.arkinfosys.service.CustomerHistoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
* 顧客マスタ変更履歴を出力するアクションクラスです.
* @author Ark Information Systems
*
*/
public class OutputCustomerHistAjaxAction extends CommonAjaxResources {
	/**
	 * 画面遷移用のマッピングクラス
	 */
	public static class Mapping {
		public static final String EXCEL = "excel.jsp";
	}

	/**
	 * 添付ファイル名指定のための定数
	 */
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String ATTACHMENT_FORMAT = "attachment; filename=\"%1$s\"";
	public static final String ATTACHMENT_ENCODE = "UTF-8";

	/**
	 * 添付ファイル名定義
	 */
	public static class AttachFileName {
		public static final String FILENAME = "CUSTOMER_HISTORY.xls";
	}

	@ActionForm
	@Resource
	private OutputCustomerHistForm outputCustomerHistForm;

	@Resource
	public CustomerHistoryService customerHistoryservice;

	/**
	 * 検索結果リスト
	 */
	public List<BeanMap> nameList = new ArrayList<BeanMap>();
	public List<BeanMap> historyList = null;
	public List<BeanMap> unitList = null;

	/**
	 * 検索結果列情報リスト
	 */
	public List<ReportColumnInfoDto> nameColList = null;
	public List<ReportColumnInfoDto> historyColList = null;
	public List<ReportColumnInfoDto> unitColList = null;

	/**
	 * 添付ファイル名
	 */
	public String attachFileName;

	/**
	 * 出力準備を行います.
	 *
	 * @return null
	 */
	@Execute(validator = true, input="/ajax/errorResponse.jsp")
	public String prepare() {
		// 保存用検索条件にコピー
		//Beans.copy(outputCustomerHistForm, customerDto).execute();

		return null;
	}

	/**
	 * Excel出力処理を行います.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excel() throws Exception {
		try {
			// 保存用出力条件からコピー
			//Beans.copy(customerDto, outputCustomerHistForm).execute();

			// パラメータを作成する
			BeanMap params = createParamMap();

			// 検索を行う
			BeanMap codeName = new BeanMap();
			codeName.put("customerCode",outputCustomerHistForm.customerCode);
			codeName.put("customerName",outputCustomerHistForm.customerName);
			nameList.add( codeName );

			historyList = customerHistoryservice.getHistroyList(params);

			// 出力の設定
			setup4Customer();

			// 添付ファイル名設定
			String attach = String.format(ATTACHMENT_FORMAT, URLEncoder.encode(attachFileName,ATTACHMENT_ENCODE));
			httpResponse.setHeader(CONTENT_DISPOSITION, attach);
		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return Mapping.EXCEL;
	}

	/**
	 * パラメータマップを作成します.
	 * @return　パラメータマップ
	 */
	private BeanMap createParamMap() {
		BeanMap map = new BeanMap();
		map.put(CustomerHistoryService.Param.CUSTOMER_CODE, outputCustomerHistForm.customerCode);

		return map;
	}

	/**
	 * 顧客マスタ変更履歴出力の設定を行います.
	 */
	private void setup4Customer() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME;

		// 名称 カラム定義
		nameColList = new ArrayList<ReportColumnInfoDto>();
		nameColList.add(new ReportColumnInfoDto("customerCode",0,"labels.report.mst.customermst.customerCode"));
		nameColList.add(new ReportColumnInfoDto("customerName",0,"labels.report.mst.customermst.customerName"));

		// 変更履歴 カラム定義
		historyColList = new ArrayList<ReportColumnInfoDto>();
		historyColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.customer.hist.updDatetm"));
		historyColList.add(new ReportColumnInfoDto("kind",0,"labels.customer.hist.kind"));
		historyColList.add(new ReportColumnInfoDto("colName",0,"labels.customer.hist.colName"));
		historyColList.add(new ReportColumnInfoDto("before",0,"labels.customer.hist.before"));
		historyColList.add(new ReportColumnInfoDto("after",0,"labels.customer.hist.after"));
	}

}
