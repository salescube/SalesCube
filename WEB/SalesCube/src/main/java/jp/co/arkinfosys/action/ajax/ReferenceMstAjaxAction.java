/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants.REFERENCE_MST_TARGET;
import jp.co.arkinfosys.dto.report.ReferenceMstFormDto;
import jp.co.arkinfosys.dto.report.ReportColumnInfoDto;
import jp.co.arkinfosys.form.report.ReferenceMstForm;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.report.ReferenceMstService;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;


/**
* マスタリストを出力するアクションクラスです.
* @author Ark Information Systems
*
*/
public class ReferenceMstAjaxAction extends CommonAjaxResources {
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
		public static final String FILENAME09 = "MST_CUSTOMER.xls";
	}

	@ActionForm
	@Resource
	private ReferenceMstForm referenceMstForm;

	@Resource
	public ReferenceMstFormDto referenceMstFormDto;

	@Resource
	public ReferenceMstService referenceMstService;

	/**
	 * 検索結果リスト
	 */
	public List<BeanMap> masterList = null;
	public List<BeanMap> detailList = null;

	/**
	 * 検索結果列情報リスト
	 */
	public List<ReportColumnInfoDto> masterColList = null;
	public List<ReportColumnInfoDto> detailColList = null;

	/**
	 * 添付ファイル名
	 */
	public String attachFileName;

	/**
	 * 出力準備を行います.
	 *
	 * @return 遷移先URI(エラー時）
	 */
	@Execute(validator = true, input="/ajax/errorResponse.jsp")
	public String prepare() {
		ActionMessages errors = referenceMstForm.validate();
		if (!errors.isEmpty()) {
			// 検索条件エラー
			ActionMessagesUtil.addErrors(super.httpRequest, errors);
			this.httpResponse.setStatus(450);
			return "/ajax/errorResponse.jsp";
		}

		// 保存用検索条件にコピー
		Beans.copy(referenceMstForm, referenceMstFormDto).execute();

		return null;
	}

	/**
	 * Excel出力処理を行います．
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excel() throws Exception {
		try {
			// 保存用出力条件からコピー
			Beans.copy(referenceMstFormDto, referenceMstForm).execute();

			// パラメータを作成する
			BeanMap params = createParamMap();

			// 検索を行う
			masterList = referenceMstService.getMasterListByCondition(params);
			detailList = referenceMstService.getDetailListByCondition(params);

			// 出力タイプごとの設定
			if (REFERENCE_MST_TARGET.VALUE_CUSTOMER.equals(referenceMstForm.outputTarget)) {
				setup4Customer();
			}

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
	 * @return パラメータマップ
	 */
	private BeanMap createParamMap() {
		BeanMap map = new BeanMap();

		// 共通部
		map.put(ReferenceMstService.Param.OUTPUT_TARGET, referenceMstForm.outputTarget);

		// 出力タイプごとの設定

		// 顧客マスタ
		if (REFERENCE_MST_TARGET.VALUE_CUSTOMER.equals(referenceMstForm.outputTarget)) {
			map.put(ReferenceMstService.Param.CUSTOMER_CODE_FROM, referenceMstForm.customerCodeFrom9);
			map.put(ReferenceMstService.Param.CUSTOMER_CODE_TO, referenceMstForm.customerCodeTo9);
			map.put(ReferenceMstService.Param.CRE_DATE_FROM, referenceMstForm.creDateFrom9);
			map.put(ReferenceMstService.Param.CRE_DATE_TO, referenceMstForm.creDateTo9);
		}

		return map;
	}

	/**
	 * 顧客マスタリスト出力の設定をします.
	 */
	private void setup4Customer() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME09;

		// カラム定義
		masterColList = new ArrayList<ReportColumnInfoDto>();
		masterColList.add(new ReportColumnInfoDto("customerCode",0,"labels.report.mst.customermst.customerCode"));
		masterColList.add(new ReportColumnInfoDto("customerName",0,"labels.report.mst.customermst.customerName"));
		masterColList.add(new ReportColumnInfoDto("customerKana",0,"labels.report.mst.customermst.customerKana"));
		masterColList.add(new ReportColumnInfoDto("customerOfficeName",0,"labels.report.mst.customermst.customerOfficeName"));
		masterColList.add(new ReportColumnInfoDto("customerOfficeKana",0,"labels.report.mst.customermst.customerOfficeKana"));
		masterColList.add(new ReportColumnInfoDto("customerAbbr",0,"labels.report.mst.customermst.customerAbbr"));

		masterColList.add(new ReportColumnInfoDto("customerZipCode",0,"labels.report.mst.customermst.customerZipCode"));
		masterColList.add(new ReportColumnInfoDto("customerAddress1",0,"labels.report.mst.customermst.customerAddress1"));
		masterColList.add(new ReportColumnInfoDto("customerAddress2",0,"labels.report.mst.customermst.customerAddress2"));
		masterColList.add(new ReportColumnInfoDto("customerPcName",0,"labels.report.mst.customermst.customerPcName"));
		masterColList.add(new ReportColumnInfoDto("customerPcKana",0,"labels.report.mst.customermst.customerPcKana"));
		masterColList.add(new ReportColumnInfoDto("customerPcPreCategoryNm",0,"labels.report.mst.customermst.customerPcPreCategoryNm"));
		masterColList.add(new ReportColumnInfoDto("customerDeptName",0,"labels.report.mst.customermst.customerDeptName"));
		masterColList.add(new ReportColumnInfoDto("customerPcPost",0,"labels.report.mst.customermst.customerPcPost"));
		masterColList.add(new ReportColumnInfoDto("customerTel",0,"labels.report.mst.customermst.customerTel"));
		masterColList.add(new ReportColumnInfoDto("customerFax",0,"labels.report.mst.customermst.customerFax"));
		masterColList.add(new ReportColumnInfoDto("customerEmail",0,"labels.report.mst.customermst.customerEmail"));

		masterColList.add(new ReportColumnInfoDto("rankName",0,"labels.report.mst.customermst.rankName"));
		masterColList.add(new ReportColumnInfoDto("customerUpdFlag",0,"labels.report.mst.customermst.customerUpdFlag"));
		masterColList.add(new ReportColumnInfoDto("customerRoCategoryNm",0,"labels.report.mst.customermst.customerRoCategoryNm"));
		masterColList.add(new ReportColumnInfoDto("maxCreditLimit",1,"labels.report.mst.customermst.maxCreditLimit"));
		masterColList.add(new ReportColumnInfoDto("customerBusinessCategoryNm",0,"labels.report.mst.customermst.customerBusinessCategoryNm"));
		masterColList.add(new ReportColumnInfoDto("customerJobCategoryNm",0,"labels.report.mst.customermst.customerJobCategoryNm"));

		masterColList.add(new ReportColumnInfoDto("taxFractCategoryNm",0,"labels.report.mst.customermst.taxFractCategoryNm"));
		masterColList.add(new ReportColumnInfoDto("priceFractCategoryNm",0,"labels.report.mst.customermst.priceFractCategoryNm"));
		masterColList.add(new ReportColumnInfoDto("taxShiftCategoryNm",0,"labels.report.mst.customermst.taxShiftCategoryNm"));
		masterColList.add(new ReportColumnInfoDto("lastCutoffDate",10,"labels.report.mst.customermst.lastCutoffDate"));



		masterColList.add(new ReportColumnInfoDto("salesCmCategoryNm",0,"labels.report.mst.customermst.salesCmCategoryNm"));
		masterColList.add(new ReportColumnInfoDto("categoryCodeName",0,"labels.report.mst.customermst.categoryCodeName"));
		masterColList.add(new ReportColumnInfoDto("paybackTypeCategoryNm",0,"labels.report.mst.customermst.paybackTypeCategoryNm"));
		masterColList.add(new ReportColumnInfoDto("tempDeliverySlipFlag",0,"labels.report.mst.customermst.tempDeliverySlipFlag"));

		masterColList.add(new ReportColumnInfoDto("paymentName",0,"labels.report.mst.customermst.paymentName"));
		masterColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.mst.customermst.remarks"));
		masterColList.add(new ReportColumnInfoDto("commentData",0,"labels.report.mst.customermst.commentData"));

		masterColList.add(new ReportColumnInfoDto("deliverDeliveryName",0,"labels.report.mst.customermst.deliverDeliveryName"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryKana",0,"labels.report.mst.customermst.deliverDeliveryKana"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryOfficeName",0,"labels.report.mst.customermst.deliverDeliveryOfficeName"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryOfficeKana",0,"labels.report.mst.customermst.deliverDeliveryOfficeKana"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryDeptName",0,"labels.report.mst.customermst.deliverDeliveryDeptName"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryZipCode",0,"labels.report.mst.customermst.deliverDeliveryZipCode"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryAddress1",0,"labels.report.mst.customermst.deliverDeliveryAddress1"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryAddress2",0,"labels.report.mst.customermst.deliverDeliveryAddress2"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryPcName",0,"labels.report.mst.customermst.deliverDeliveryPcName"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryPcKana",0,"labels.report.mst.customermst.deliverDeliveryPcKana"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryPcPreCategoryNm",0,"labels.report.mst.customermst.deliverDeliveryPcPreCategoryNm"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryTel",0,"labels.report.mst.customermst.deliverDeliveryTel"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryFax",0,"labels.report.mst.customermst.deliverDeliveryFax"));
		masterColList.add(new ReportColumnInfoDto("deliverDeliveryEmail",0,"labels.report.mst.customermst.deliverDeliveryEmail"));

		masterColList.add(new ReportColumnInfoDto("billDeliveryName",0,"labels.report.mst.customermst.billDeliveryName"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryKana",0,"labels.report.mst.customermst.billDeliveryKana"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryOfficeName",0,"labels.report.mst.customermst.billDeliveryOfficeName"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryOfficeKana",0,"labels.report.mst.customermst.billDeliveryOfficeKana"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryDeptName",0,"labels.report.mst.customermst.billDeliveryDeptName"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryZipCode",0,"labels.report.mst.customermst.billDeliveryZipCode"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryAddress1",0,"labels.report.mst.customermst.billDeliveryAddress1"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryAddress2",0,"labels.report.mst.customermst.billDeliveryAddress2"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryPcName",0,"labels.report.mst.customermst.billDeliveryPcName"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryPcKana",0,"labels.report.mst.customermst.billDeliveryPcKana"));
		masterColList.add(new ReportColumnInfoDto("billCategoryCodeName",0,"labels.report.mst.customermst.billCategoryCodeName"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryTel",0,"labels.report.mst.customermst.billDeliveryTel"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryFax",0,"labels.report.mst.customermst.billDeliveryFax"));
		masterColList.add(new ReportColumnInfoDto("billDeliveryEmail",0,"labels.report.mst.customermst.billDeliveryEmail"));

		masterColList.add(new ReportColumnInfoDto("creDatetm",10,"labels.report.mst.customermst.creDate"));
		masterColList.add(new ReportColumnInfoDto("updDatetm",10,"labels.report.mst.customermst.updDate"));

		// 明細部カラム定義（明細なし）
		detailColList = null;
	}

}
