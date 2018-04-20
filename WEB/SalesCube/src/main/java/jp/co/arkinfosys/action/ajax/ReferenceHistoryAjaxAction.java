/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants.REFERENCE_HISTORY_TARGET;
import jp.co.arkinfosys.dto.report.ReferenceHistoryFormDto;
import jp.co.arkinfosys.dto.report.ReportColumnInfoDto;
import jp.co.arkinfosys.form.report.ReferenceHistoryForm;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.report.ReferenceHistoryService;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 履歴参照リストを出力するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class ReferenceHistoryAjaxAction extends CommonAjaxResources {
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
		public static final String FILENAME01 = "HIST_ESTIMATE.xls";
		public static final String FILENAME02 = "HIST_RORDER.xls";
		public static final String FILENAME03 = "HIST_SALES.xls";
		public static final String FILENAME04 = "HIST_DEPOSIT.xls";
		public static final String FILENAME05 = "HIST_PORDER.xls";
		public static final String FILENAME06 = "HIST_PURCHASE.xls";
		public static final String FILENAME07 = "HIST_PAYMENT.xls";
		public static final String FILENAME08 = "HIST_STOCK.xls";
		public static final String FILENAME09 = "HIST_CUSTOMER.xls";
		public static final String FILENAME10 = "HIST_PRODUCT.xls";
		public static final String FILENAME11 = "HIST_SUPPLIER.xls";
		public static final String FILENAME12 = "HIST_USER.xls";
	}

	@ActionForm
	@Resource
	private ReferenceHistoryForm referenceHistoryForm;

	@Resource
	public ReferenceHistoryFormDto referenceHistoryFormDto;

	@Resource
	public ReferenceHistoryService referenceHistoryService;

	/**
	 * 検索結果リスト
	 */
	public List<BeanMap> slipList = null;
	public List<BeanMap> detailList = null;

	/**
	 * 検索結果列情報リスト
	 */
	public List<ReportColumnInfoDto> slipColList = null;
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
		ActionMessages errors = referenceHistoryForm.validate();
		if (!errors.isEmpty()) {
			// 検索条件エラー
			ActionMessagesUtil.addErrors(super.httpRequest, errors);
			this.httpResponse.setStatus(450);
			return "/ajax/errorResponse.jsp";
		}

		// 保存用検索条件にコピー
		Beans.copy(referenceHistoryForm, referenceHistoryFormDto).execute();

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
			Beans.copy(referenceHistoryFormDto, referenceHistoryForm).execute();

			// パラメータを作成する
			BeanMap params = createParamMap();

			// 検索を行う
			slipList = referenceHistoryService.getSlipListByCondition(params);
			detailList = referenceHistoryService.getDetailListByCondition(params);

			// 出力タイプごとの設定
			if (REFERENCE_HISTORY_TARGET.VALUE_ESTIMATE.equals(referenceHistoryForm.outputTarget)) {
				setup4Estimate();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_RORDER.equals(referenceHistoryForm.outputTarget)) {
				setup4Rorder();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_SALES.equals(referenceHistoryForm.outputTarget)) {
				setup4Sales();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_DEPOSIT.equals(referenceHistoryForm.outputTarget)) {
				setup4Deposit();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PORDER.equals(referenceHistoryForm.outputTarget)) {
				setup4Porder();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PURCHASE.equals(referenceHistoryForm.outputTarget)) {
				setup4Purchase();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PAYMENT.equals(referenceHistoryForm.outputTarget)) {
				setup4Payment();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_STOCK.equals(referenceHistoryForm.outputTarget)) {
				setup4Stock();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_CUSTOMER.equals(referenceHistoryForm.outputTarget)) {
				setup4Customer();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PRODUCT.equals(referenceHistoryForm.outputTarget)) {
				setup4Product();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_SUPPLIER.equals(referenceHistoryForm.outputTarget)) {
				setup4Supplier();
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_USER.equals(referenceHistoryForm.outputTarget)) {
				setup4User();
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
		map.put(ReferenceHistoryService.Param.OUTPUT_TARGET, referenceHistoryForm.outputTarget);
		map.put(ReferenceHistoryService.Param.ACTION_TYPE, referenceHistoryForm.actionType);
		map.put(ReferenceHistoryService.Param.REC_DATE_FROM, referenceHistoryForm.recDateFrom);
		map.put(ReferenceHistoryService.Param.REC_DATE_TO, referenceHistoryForm.recDateTo);

		// 出力タイプごとの設定
		// 見積入力
		if (REFERENCE_HISTORY_TARGET.VALUE_ESTIMATE.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.ESTIMATE_DATE_FROM, referenceHistoryForm.estimateDateFrom1);
			map.put(ReferenceHistoryService.Param.ESTIMATE_DATE_TO, referenceHistoryForm.estimateDateTo1);
		}
		// 受注入力
		else if (REFERENCE_HISTORY_TARGET.VALUE_RORDER.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.CUSTOMER_CODE_FROM, referenceHistoryForm.customerCodeFrom2);
			map.put(ReferenceHistoryService.Param.CUSTOMER_CODE_TO, referenceHistoryForm.customerCodeTo2);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_FROM, referenceHistoryForm.productCodeFrom2);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_TO, referenceHistoryForm.productCodeTo2);
			map.put(ReferenceHistoryService.Param.SHIP_DATE_FROM, referenceHistoryForm.shipDateFrom2);
			map.put(ReferenceHistoryService.Param.SHIP_DATE_TO, referenceHistoryForm.shipDateTo2);
		}
		// 売上入力
		else if (REFERENCE_HISTORY_TARGET.VALUE_SALES.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.CUSTOMER_CODE_FROM, referenceHistoryForm.customerCodeFrom3);
			map.put(ReferenceHistoryService.Param.CUSTOMER_CODE_TO, referenceHistoryForm.customerCodeTo3);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_FROM, referenceHistoryForm.productCodeFrom3);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_TO, referenceHistoryForm.productCodeTo3);
		}
		// 入金入力
		else if (REFERENCE_HISTORY_TARGET.VALUE_DEPOSIT.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.CUSTOMER_CODE_FROM, referenceHistoryForm.customerCodeFrom4);
			map.put(ReferenceHistoryService.Param.CUSTOMER_CODE_TO, referenceHistoryForm.customerCodeTo4);
		}
		// 発注入力
		else if (REFERENCE_HISTORY_TARGET.VALUE_PORDER.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.SUPPLIER_CODE_FROM, referenceHistoryForm.supplierCodeFrom5);
			map.put(ReferenceHistoryService.Param.SUPPLIER_CODE_TO, referenceHistoryForm.supplierCodeTo5);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_FROM, referenceHistoryForm.productCodeFrom5);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_TO, referenceHistoryForm.productCodeTo5);
		}
		// 仕入入力
		else if (REFERENCE_HISTORY_TARGET.VALUE_PURCHASE.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.SUPPLIER_CODE_FROM, referenceHistoryForm.supplierCodeFrom6);
			map.put(ReferenceHistoryService.Param.SUPPLIER_CODE_TO, referenceHistoryForm.supplierCodeTo6);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_FROM, referenceHistoryForm.productCodeFrom6);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_TO, referenceHistoryForm.productCodeTo6);
			map.put(ReferenceHistoryService.Param.DELIVERY_DATE_FROM, referenceHistoryForm.deliveryDateFrom6);
			map.put(ReferenceHistoryService.Param.DELIVERY_DATE_TO, referenceHistoryForm.deliveryDateTo6);
		}
		// 支払入力
		else if (REFERENCE_HISTORY_TARGET.VALUE_PAYMENT.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.SUPPLIER_CODE_FROM, referenceHistoryForm.supplierCodeFrom7);
			map.put(ReferenceHistoryService.Param.SUPPLIER_CODE_TO, referenceHistoryForm.supplierCodeTo7);
			map.put(ReferenceHistoryService.Param.PAYMENT_DATE_FROM, referenceHistoryForm.paymentDateFrom7);
			map.put(ReferenceHistoryService.Param.PAYMENT_DATE_TO, referenceHistoryForm.paymentDateTo7);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_FROM, referenceHistoryForm.productCodeFrom7);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_TO, referenceHistoryForm.productCodeTo7);
		}
		// 入出庫入力
		else if (REFERENCE_HISTORY_TARGET.VALUE_STOCK.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_FROM, referenceHistoryForm.productCodeFrom8);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_TO, referenceHistoryForm.productCodeTo8);
			map.put(ReferenceHistoryService.Param.EAD_SLIP_CATEGORY, referenceHistoryForm.eadSlipCategory8);
		}
		// 顧客マスタ
		else if (REFERENCE_HISTORY_TARGET.VALUE_CUSTOMER.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.CUSTOMER_CODE_FROM, referenceHistoryForm.customerCodeFrom9);
			map.put(ReferenceHistoryService.Param.CUSTOMER_CODE_TO, referenceHistoryForm.customerCodeTo9);
			map.put(ReferenceHistoryService.Param.CRE_DATE_FROM, referenceHistoryForm.creDateFrom9);
			map.put(ReferenceHistoryService.Param.CRE_DATE_TO, referenceHistoryForm.creDateTo9);
		}
		// 商品マスタ
		else if (REFERENCE_HISTORY_TARGET.VALUE_PRODUCT.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_FROM, referenceHistoryForm.productCodeFrom10);
			map.put(ReferenceHistoryService.Param.PRODUCT_CODE_TO, referenceHistoryForm.productCodeTo10);
			map.put(ReferenceHistoryService.Param.CRE_DATE_FROM, referenceHistoryForm.creDateFrom10);
			map.put(ReferenceHistoryService.Param.CRE_DATE_TO, referenceHistoryForm.creDateTo10);
		}
		// 仕入先マスタ
		else if (REFERENCE_HISTORY_TARGET.VALUE_SUPPLIER.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.SUPPLIER_CODE_FROM, referenceHistoryForm.supplierCodeFrom11);
			map.put(ReferenceHistoryService.Param.SUPPLIER_CODE_TO, referenceHistoryForm.supplierCodeTo11);
			map.put(ReferenceHistoryService.Param.CRE_DATE_FROM, referenceHistoryForm.creDateFrom11);
			map.put(ReferenceHistoryService.Param.CRE_DATE_TO, referenceHistoryForm.creDateTo11);
		}
		else if (REFERENCE_HISTORY_TARGET.VALUE_USER.equals(referenceHistoryForm.outputTarget)) {
			map.put(ReferenceHistoryService.Param.CRE_DATE_FROM, referenceHistoryForm.creDateFrom12);
			map.put(ReferenceHistoryService.Param.CRE_DATE_TO, referenceHistoryForm.creDateTo12);
		}

		return map;
	}

	/**
	 * 見積入力履歴出力の設定をします.
	 */
	private void setup4Estimate() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME01;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("estimateSheetId",0,"labels.report.hist.estimateSheet.estimateSheetId"));
		slipColList.add(new ReportColumnInfoDto("estimateAnnual",0,"labels.report.hist.estimateSheet.estimateAnnual"));
		slipColList.add(new ReportColumnInfoDto("estimateMonthly",0,"labels.report.hist.estimateSheet.estimateMonthly"));
		slipColList.add(new ReportColumnInfoDto("estimateYm",0,"labels.report.hist.estimateSheet.estimateYm"));
		slipColList.add(new ReportColumnInfoDto("estimateDate",10,"labels.report.hist.estimateSheet.estimateDate"));
		slipColList.add(new ReportColumnInfoDto("deliveryInfo",0,"labels.report.hist.estimateSheet.deliveryInfo"));
		slipColList.add(new ReportColumnInfoDto("validDate",10,"labels.report.hist.estimateSheet.validDate"));
		slipColList.add(new ReportColumnInfoDto("userId",0,"labels.report.hist.estimateSheet.userId"));
		slipColList.add(new ReportColumnInfoDto("userName",0,"labels.report.hist.estimateSheet.userName"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.estimateSheet.remarks"));
		slipColList.add(new ReportColumnInfoDto("title",0,"labels.report.hist.estimateSheet.title"));
		slipColList.add(new ReportColumnInfoDto("estimateCondition",0,"labels.report.hist.estimateSheet.estimateCondition"));
		slipColList.add(new ReportColumnInfoDto("submitName",0,"labels.report.hist.estimateSheet.submitName"));
		slipColList.add(new ReportColumnInfoDto("submitPreCategory",0,"labels.report.hist.estimateSheet.submitPreCategory"));
		slipColList.add(new ReportColumnInfoDto("submitPre",0,"labels.report.hist.estimateSheet.submitPre"));
		slipColList.add(new ReportColumnInfoDto("customerCode",0,"labels.report.hist.estimateSheet.customerCode"));
		slipColList.add(new ReportColumnInfoDto("customerName",0,"labels.report.hist.estimateSheet.customerName"));
		slipColList.add(new ReportColumnInfoDto("customerRemarks",0,"labels.report.hist.estimateSheet.customerRemarks"));
		slipColList.add(new ReportColumnInfoDto("customerCommentData",0,"labels.report.hist.estimateSheet.customerCommentData"));
		slipColList.add(new ReportColumnInfoDto("deliveryName",0,"labels.report.hist.estimateSheet.deliveryName"));
		slipColList.add(new ReportColumnInfoDto("deliveryOfficeName",0,"labels.report.hist.estimateSheet.deliveryOfficeName"));
		slipColList.add(new ReportColumnInfoDto("deliveryDeptName",0,"labels.report.hist.estimateSheet.deliveryDeptName"));
		slipColList.add(new ReportColumnInfoDto("deliveryZipCode",0,"labels.report.hist.estimateSheet.deliveryZipCode"));
		slipColList.add(new ReportColumnInfoDto("deliveryAddress1",0,"labels.report.hist.estimateSheet.deliveryAddress1"));
		slipColList.add(new ReportColumnInfoDto("deliveryAddress2",0,"labels.report.hist.estimateSheet.deliveryAddress2"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcName",0,"labels.report.hist.estimateSheet.deliveryPcName"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcKana",0,"labels.report.hist.estimateSheet.deliveryPcKana"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcPreCategory",0,"labels.report.hist.estimateSheet.deliveryPcPreCategory"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcPre",0,"labels.report.hist.estimateSheet.deliveryPcPre"));
		slipColList.add(new ReportColumnInfoDto("deliveryTel",0,"labels.report.hist.estimateSheet.deliveryTel"));
		slipColList.add(new ReportColumnInfoDto("deliveryFax",0,"labels.report.hist.estimateSheet.deliveryFax"));
		slipColList.add(new ReportColumnInfoDto("deliveryEmail",0,"labels.report.hist.estimateSheet.deliveryEmail"));
		slipColList.add(new ReportColumnInfoDto("deliveryUrl",0,"labels.report.hist.estimateSheet.deliveryUrl"));
		slipColList.add(new ReportColumnInfoDto("ctaxPriceTotal",1,"labels.report.hist.estimateSheet.ctaxPriceTotal"));
		slipColList.add(new ReportColumnInfoDto("costTotal",1,"labels.report.hist.estimateSheet.costTotal"));
		slipColList.add(new ReportColumnInfoDto("retailPriceTotal",1,"labels.report.hist.estimateSheet.retailPriceTotal"));
		slipColList.add(new ReportColumnInfoDto("estimateTotal",1,"labels.report.hist.estimateSheet.estimateTotal"));
		slipColList.add(new ReportColumnInfoDto("taxFractCategory",0,"labels.report.hist.estimateSheet.taxFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceFractCategory",0,"labels.report.hist.estimateSheet.priceFractCategory"));
		slipColList.add(new ReportColumnInfoDto("memo",0,"labels.report.hist.estimateSheet.memo"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義
		detailColList = new ArrayList<ReportColumnInfoDto>();
		detailColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		detailColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		detailColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		detailColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		detailColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		detailColList.add(new ReportColumnInfoDto("estimateLineId",0,"labels.report.hist.estimateLine.estimateLineId"));
		detailColList.add(new ReportColumnInfoDto("estimateSheetId",0,"labels.report.hist.estimateLine.estimateSheetId"));
		detailColList.add(new ReportColumnInfoDto("lineNo",1,"labels.report.hist.estimateLine.lineNo"));
		detailColList.add(new ReportColumnInfoDto("productCode",0,"labels.report.hist.estimateLine.productCode"));
		detailColList.add(new ReportColumnInfoDto("customerPcode",0,"labels.report.hist.estimateLine.customerPcode"));
		detailColList.add(new ReportColumnInfoDto("productAbstract",0,"labels.report.hist.estimateLine.productAbstract"));
		detailColList.add(new ReportColumnInfoDto("quantity",1,"labels.report.hist.estimateLine.quantity"));
		detailColList.add(new ReportColumnInfoDto("unitCost",1,"labels.report.hist.estimateLine.unitCost"));
		detailColList.add(new ReportColumnInfoDto("unitRetailPrice",1,"labels.report.hist.estimateLine.unitRetailPrice"));
		detailColList.add(new ReportColumnInfoDto("cost",1,"labels.report.hist.estimateLine.cost"));
		detailColList.add(new ReportColumnInfoDto("retailPrice",1,"labels.report.hist.estimateLine.retailPrice"));
		detailColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.estimateLine.remarks"));
		detailColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		detailColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		detailColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		detailColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		detailColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		detailColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
	}

	/**
	 * 受注入力履歴出力の設定をします.
	 */
	private void setup4Rorder() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME02;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("roSlipId",0,"labels.report.hist.roSlip.roSlipId"));
		slipColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.roSlip.status"));
		slipColList.add(new ReportColumnInfoDto("roAnnual",0,"labels.report.hist.roSlip.roAnnual"));
		slipColList.add(new ReportColumnInfoDto("roMonthly",0,"labels.report.hist.roSlip.roMonthly"));
		slipColList.add(new ReportColumnInfoDto("roYm",0,"labels.report.hist.roSlip.roYm"));
		slipColList.add(new ReportColumnInfoDto("roDate",10,"labels.report.hist.roSlip.roDate"));
		slipColList.add(new ReportColumnInfoDto("shipDate",10,"labels.report.hist.roSlip.shipDate"));
		slipColList.add(new ReportColumnInfoDto("deliveryDate",10,"labels.report.hist.roSlip.deliveryDate"));
		slipColList.add(new ReportColumnInfoDto("receptNo",0,"labels.report.hist.roSlip.receptNo"));
		slipColList.add(new ReportColumnInfoDto("customerSlipNo",0,"labels.report.hist.roSlip.customerSlipNo"));
		slipColList.add(new ReportColumnInfoDto("salesCmCategory",0,"labels.report.hist.roSlip.salesCmCategory"));
		slipColList.add(new ReportColumnInfoDto("cutoffGroup",0,"labels.report.hist.roSlip.cutoffGroup"));
		slipColList.add(new ReportColumnInfoDto("userId",0,"labels.report.hist.roSlip.userId"));
		slipColList.add(new ReportColumnInfoDto("userName",0,"labels.report.hist.roSlip.userName"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.roSlip.remarks"));
		slipColList.add(new ReportColumnInfoDto("customerCode",0,"labels.report.hist.roSlip.customerCode"));
		slipColList.add(new ReportColumnInfoDto("customerName",0,"labels.report.hist.roSlip.customerName"));
		slipColList.add(new ReportColumnInfoDto("customerRemarks",0,"labels.report.hist.roSlip.customerRemarks"));
		slipColList.add(new ReportColumnInfoDto("customerCommentData",0,"labels.report.hist.roSlip.customerCommentData"));
		slipColList.add(new ReportColumnInfoDto("deliveryCode",0,"labels.report.hist.roSlip.deliveryCode"));
		slipColList.add(new ReportColumnInfoDto("deliveryName",0,"labels.report.hist.roSlip.deliveryName"));
		slipColList.add(new ReportColumnInfoDto("deliveryKana",0,"labels.report.hist.roSlip.deliveryKana"));
		slipColList.add(new ReportColumnInfoDto("deliveryOfficeName",0,"labels.report.hist.roSlip.deliveryOfficeName"));
		slipColList.add(new ReportColumnInfoDto("deliveryOfficeKana",0,"labels.report.hist.roSlip.deliveryOfficeKana"));
		slipColList.add(new ReportColumnInfoDto("deliveryDeptName",0,"labels.report.hist.roSlip.deliveryDeptName"));
		slipColList.add(new ReportColumnInfoDto("deliveryZipCode",0,"labels.report.hist.roSlip.deliveryZipCode"));
		slipColList.add(new ReportColumnInfoDto("deliveryAddress1",0,"labels.report.hist.roSlip.deliveryAddress1"));
		slipColList.add(new ReportColumnInfoDto("deliveryAddress2",0,"labels.report.hist.roSlip.deliveryAddress2"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcName",0,"labels.report.hist.roSlip.deliveryPcName"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcKana",0,"labels.report.hist.roSlip.deliveryPcKana"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcPreCategory",0,"labels.report.hist.roSlip.deliveryPcPreCategory"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcPre",0,"labels.report.hist.roSlip.deliveryPcPre"));
		slipColList.add(new ReportColumnInfoDto("deliveryTel",0,"labels.report.hist.roSlip.deliveryTel"));
		slipColList.add(new ReportColumnInfoDto("deliveryFax",0,"labels.report.hist.roSlip.deliveryFax"));
		slipColList.add(new ReportColumnInfoDto("deliveryEmail",0,"labels.report.hist.roSlip.deliveryEmail"));
		slipColList.add(new ReportColumnInfoDto("deliveryUrl",0,"labels.report.hist.roSlip.deliveryUrl"));
		slipColList.add(new ReportColumnInfoDto("estimateSheetId",0,"labels.report.hist.roSlip.estimateSheetId"));
		slipColList.add(new ReportColumnInfoDto("taxShiftCategory",0,"labels.report.hist.roSlip.taxShiftCategory"));
		slipColList.add(new ReportColumnInfoDto("ctaxPriceTotal",1,"labels.report.hist.roSlip.ctaxPriceTotal"));
		slipColList.add(new ReportColumnInfoDto("costTotal",1,"labels.report.hist.roSlip.costTotal"));
		slipColList.add(new ReportColumnInfoDto("retailPriceTotal",1,"labels.report.hist.roSlip.retailPriceTotal"));
		slipColList.add(new ReportColumnInfoDto("priceTotal",1,"labels.report.hist.roSlip.priceTotal"));
		slipColList.add(new ReportColumnInfoDto("printCount",1,"labels.report.hist.roSlip.printCount"));
		slipColList.add(new ReportColumnInfoDto("codSc",0,"labels.report.hist.roSlip.codSc"));
		slipColList.add(new ReportColumnInfoDto("paybackCycleCategory",0,"labels.report.hist.roSlip.paybackCycleCategory"));
		slipColList.add(new ReportColumnInfoDto("taxFractCategory",0,"labels.report.hist.roSlip.taxFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceFractCategory",0,"labels.report.hist.roSlip.priceFractCategory"));
		slipColList.add(new ReportColumnInfoDto("dcCategory",0,"labels.report.hist.roSlip.dcCategory"));
		slipColList.add(new ReportColumnInfoDto("dcName",0,"labels.report.hist.roSlip.dcName"));
		slipColList.add(new ReportColumnInfoDto("dcTimezoneCategory",0,"labels.report.hist.roSlip.dcTimezoneCategory"));
		slipColList.add(new ReportColumnInfoDto("dcTimezone",0,"labels.report.hist.roSlip.dcTimezone"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義
		detailColList = new ArrayList<ReportColumnInfoDto>();
		detailColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		detailColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		detailColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		detailColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		detailColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		detailColList.add(new ReportColumnInfoDto("roLineId",0,"labels.report.hist.roLine.roLineId"));
		detailColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.roLine.status"));
		detailColList.add(new ReportColumnInfoDto("roSlipId",0,"labels.report.hist.roLine.roSlipId"));
		detailColList.add(new ReportColumnInfoDto("lineNo",1,"labels.report.hist.roLine.roLineNo"));
		detailColList.add(new ReportColumnInfoDto("estimateLineId",0,"labels.report.hist.roLine.estimateLineId"));
		detailColList.add(new ReportColumnInfoDto("lastShipDate",10,"labels.report.hist.roLine.lastShipDate"));
		detailColList.add(new ReportColumnInfoDto("productCode",0,"labels.report.hist.roLine.productCode"));
		detailColList.add(new ReportColumnInfoDto("customerPcode",0,"labels.report.hist.roLine.customerPcode"));
		detailColList.add(new ReportColumnInfoDto("productAbstract",0,"labels.report.hist.roLine.productAbstract"));
		detailColList.add(new ReportColumnInfoDto("productRemarks",0,"labels.report.hist.roLine.productRemarks"));
		detailColList.add(new ReportColumnInfoDto("quantity",1,"labels.report.hist.roLine.quantity"));
		detailColList.add(new ReportColumnInfoDto("unitPrice",1,"labels.report.hist.roLine.unitPrice"));
		detailColList.add(new ReportColumnInfoDto("unitCategory",0,"labels.report.hist.roLine.unitCategory"));
		detailColList.add(new ReportColumnInfoDto("unitName",0,"labels.report.hist.roLine.unitName"));
		detailColList.add(new ReportColumnInfoDto("packQuantity",1,"labels.report.hist.roLine.packQuantity"));
		detailColList.add(new ReportColumnInfoDto("unitRetailPrice",1,"labels.report.hist.roLine.unitRetailPrice"));
		detailColList.add(new ReportColumnInfoDto("retailPrice",1,"labels.report.hist.roLine.retailPrice"));
		detailColList.add(new ReportColumnInfoDto("unitCost",1,"labels.report.hist.roLine.unitCost"));
		detailColList.add(new ReportColumnInfoDto("cost",1,"labels.report.hist.roLine.cost"));
		detailColList.add(new ReportColumnInfoDto("taxCategory",0,"labels.report.hist.roLine.taxCategory"));
		detailColList.add(new ReportColumnInfoDto("ctaxRate",2,"labels.report.hist.roLine.ctaxRate"));
		detailColList.add(new ReportColumnInfoDto("ctaxPrice",1,"labels.report.hist.roLine.ctaxPrice"));
		detailColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.roLine.remarks"));
		detailColList.add(new ReportColumnInfoDto("eadRemarks",0,"labels.report.hist.roLine.eadRemarks"));
		detailColList.add(new ReportColumnInfoDto("restQuantity",1,"labels.report.hist.roLine.restQuantity"));
		detailColList.add(new ReportColumnInfoDto("rackCodeSrc",0,"labels.report.hist.roLine.rackCodeSrc"));
		detailColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		detailColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		detailColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		detailColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		detailColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		detailColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
	}

	/**
	 * 売上入力履歴出力の設定をします.
	 */
	private void setup4Sales() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME03;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("salesSlipId",0,"labels.report.hist.salesSlip.salesSlipId"));
		slipColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.salesSlip.status"));
		slipColList.add(new ReportColumnInfoDto("salesAnnual",0,"labels.report.hist.salesSlip.salesAnnual"));
		slipColList.add(new ReportColumnInfoDto("salesMonthly",0,"labels.report.hist.salesSlip.salesMonthly"));
		slipColList.add(new ReportColumnInfoDto("salesYm",0,"labels.report.hist.salesSlip.salesYm"));
		slipColList.add(new ReportColumnInfoDto("roSlipId",0,"labels.report.hist.salesSlip.roSlipId"));
		slipColList.add(new ReportColumnInfoDto("billId",0,"labels.report.hist.salesSlip.billId"));
		slipColList.add(new ReportColumnInfoDto("salesBillId",0,"labels.report.hist.salesSlip.salesBillId"));
		slipColList.add(new ReportColumnInfoDto("billDate",10,"labels.report.hist.salesSlip.billDate"));
		slipColList.add(new ReportColumnInfoDto("billCutoffGroup",0,"labels.report.hist.salesSlip.billCutoffGroup"));
		slipColList.add(new ReportColumnInfoDto("paybackCycleCategory",0,"labels.report.hist.salesSlip.paybackCycleCategory"));
		slipColList.add(new ReportColumnInfoDto("billCutoffDate",10,"labels.report.hist.salesSlip.billCutoffDate"));
		slipColList.add(new ReportColumnInfoDto("billCutoffPdate",11,"labels.report.hist.salesSlip.billCutoffPdate"));
		slipColList.add(new ReportColumnInfoDto("salesDate",10,"labels.report.hist.salesSlip.salesDate"));
		slipColList.add(new ReportColumnInfoDto("deliveryDate",10,"labels.report.hist.salesSlip.deliveryDate"));
		slipColList.add(new ReportColumnInfoDto("receptNo",0,"labels.report.hist.salesSlip.receptNo"));
		slipColList.add(new ReportColumnInfoDto("customerSlipNo",0,"labels.report.hist.salesSlip.customerSlipNo"));
		slipColList.add(new ReportColumnInfoDto("salesCmCategory",0,"labels.report.hist.salesSlip.salesCmCategory"));
		slipColList.add(new ReportColumnInfoDto("salesCutoffDate",10,"labels.report.hist.salesSlip.salesCutoffDate"));
		slipColList.add(new ReportColumnInfoDto("salesCutoffPdate",11,"labels.report.hist.salesSlip.salesCutoffPdate"));
		slipColList.add(new ReportColumnInfoDto("userId",0,"labels.report.hist.salesSlip.userId"));
		slipColList.add(new ReportColumnInfoDto("userName",0,"labels.report.hist.salesSlip.userName"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.salesSlip.remarks"));
		slipColList.add(new ReportColumnInfoDto("pickingRemarks",0,"labels.report.hist.salesSlip.pickingRemarks"));
		slipColList.add(new ReportColumnInfoDto("dcCategory",0,"labels.report.hist.salesSlip.dcCategory"));
		slipColList.add(new ReportColumnInfoDto("dcName",0,"labels.report.hist.salesSlip.dcName"));
		slipColList.add(new ReportColumnInfoDto("dcTimezoneCategory",0,"labels.report.hist.salesSlip.dcTimezoneCategory"));
		slipColList.add(new ReportColumnInfoDto("dcTimezone",0,"labels.report.hist.salesSlip.dcTimezone"));
		slipColList.add(new ReportColumnInfoDto("customerCode",0,"labels.report.hist.salesSlip.customerCode"));
		slipColList.add(new ReportColumnInfoDto("customerName",0,"labels.report.hist.salesSlip.customerName"));
		slipColList.add(new ReportColumnInfoDto("customerRemarks",0,"labels.report.hist.salesSlip.customerRemarks"));
		slipColList.add(new ReportColumnInfoDto("customerCommentData",0,"labels.report.hist.salesSlip.customerCommentData"));
		slipColList.add(new ReportColumnInfoDto("customerOfficeName",0,"labels.report.hist.salesSlip.customerOfficeName"));
		slipColList.add(new ReportColumnInfoDto("customerOfficeKana",0,"labels.report.hist.salesSlip.customerOfficeKana"));
		slipColList.add(new ReportColumnInfoDto("customerAbbr",0,"labels.report.hist.salesSlip.customerAbbr"));
		slipColList.add(new ReportColumnInfoDto("customerDeptName",0,"labels.report.hist.salesSlip.customerDeptName"));
		slipColList.add(new ReportColumnInfoDto("customerZipCode",0,"labels.report.hist.salesSlip.customerZipCode"));
		slipColList.add(new ReportColumnInfoDto("customerAddress1",0,"labels.report.hist.salesSlip.customerAddress1"));
		slipColList.add(new ReportColumnInfoDto("customerAddress2",0,"labels.report.hist.salesSlip.customerAddress2"));
		slipColList.add(new ReportColumnInfoDto("customerPcPost",0,"labels.report.hist.salesSlip.customerPcPost"));
		slipColList.add(new ReportColumnInfoDto("customerPcName",0,"labels.report.hist.salesSlip.customerPcName"));
		slipColList.add(new ReportColumnInfoDto("customerPcKana",0,"labels.report.hist.salesSlip.customerPcKana"));
		slipColList.add(new ReportColumnInfoDto("customerPcPreCategory",0,"labels.report.hist.salesSlip.customerPcPreCategory"));
		slipColList.add(new ReportColumnInfoDto("customerPcPre",0,"labels.report.hist.salesSlip.customerPcPre"));
		slipColList.add(new ReportColumnInfoDto("customerTel",0,"labels.report.hist.salesSlip.customerTel"));
		slipColList.add(new ReportColumnInfoDto("customerFax",0,"labels.report.hist.salesSlip.customerFax"));
		slipColList.add(new ReportColumnInfoDto("customerEmail",0,"labels.report.hist.salesSlip.customerEmail"));
		slipColList.add(new ReportColumnInfoDto("customerUrl",0,"labels.report.hist.salesSlip.customerUrl"));
		slipColList.add(new ReportColumnInfoDto("deliveryCode",0,"labels.report.hist.salesSlip.deliveryCode"));
		slipColList.add(new ReportColumnInfoDto("deliveryName",0,"labels.report.hist.salesSlip.deliveryName"));
		slipColList.add(new ReportColumnInfoDto("deliveryKana",0,"labels.report.hist.salesSlip.deliveryKana"));
		slipColList.add(new ReportColumnInfoDto("deliveryOfficeName",0,"labels.report.hist.salesSlip.deliveryOfficeName"));
		slipColList.add(new ReportColumnInfoDto("deliveryOfficeKana",0,"labels.report.hist.salesSlip.deliveryOfficeKana"));
		slipColList.add(new ReportColumnInfoDto("deliveryDeptName",0,"labels.report.hist.salesSlip.deliveryDeptName"));
		slipColList.add(new ReportColumnInfoDto("deliveryZipCode",0,"labels.report.hist.salesSlip.deliveryZipCode"));
		slipColList.add(new ReportColumnInfoDto("deliveryAddress1",0,"labels.report.hist.salesSlip.deliveryAddress1"));
		slipColList.add(new ReportColumnInfoDto("deliveryAddress2",0,"labels.report.hist.salesSlip.deliveryAddress2"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcName",0,"labels.report.hist.salesSlip.deliveryPcName"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcKana",0,"labels.report.hist.salesSlip.deliveryPcKana"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcPreCategory",0,"labels.report.hist.salesSlip.deliveryPcPreCategory"));
		slipColList.add(new ReportColumnInfoDto("deliveryPcPre",0,"labels.report.hist.salesSlip.deliveryPcPre"));
		slipColList.add(new ReportColumnInfoDto("deliveryTel",0,"labels.report.hist.salesSlip.deliveryTel"));
		slipColList.add(new ReportColumnInfoDto("deliveryFax",0,"labels.report.hist.salesSlip.deliveryFax"));
		slipColList.add(new ReportColumnInfoDto("deliveryEmail",0,"labels.report.hist.salesSlip.deliveryEmail"));
		slipColList.add(new ReportColumnInfoDto("deliveryUrl",0,"labels.report.hist.salesSlip.deliveryUrl"));
		slipColList.add(new ReportColumnInfoDto("baCode",0,"labels.report.hist.salesSlip.baCode"));
		slipColList.add(new ReportColumnInfoDto("baName",0,"labels.report.hist.salesSlip.baName"));
		slipColList.add(new ReportColumnInfoDto("baKana",0,"labels.report.hist.salesSlip.baKana"));
		slipColList.add(new ReportColumnInfoDto("baOfficeName",0,"labels.report.hist.salesSlip.baOfficeName"));
		slipColList.add(new ReportColumnInfoDto("baOfficeKana",0,"labels.report.hist.salesSlip.baOfficeKana"));
		slipColList.add(new ReportColumnInfoDto("baDeptName",0,"labels.report.hist.salesSlip.baDeptName"));
		slipColList.add(new ReportColumnInfoDto("baZipCode",0,"labels.report.hist.salesSlip.baZipCode"));
		slipColList.add(new ReportColumnInfoDto("baAddress1",0,"labels.report.hist.salesSlip.baAddress1"));
		slipColList.add(new ReportColumnInfoDto("baAddress2",0,"labels.report.hist.salesSlip.baAddress2"));
		slipColList.add(new ReportColumnInfoDto("baPcName",0,"labels.report.hist.salesSlip.baPcName"));
		slipColList.add(new ReportColumnInfoDto("baPcKana",0,"labels.report.hist.salesSlip.baPcKana"));
		slipColList.add(new ReportColumnInfoDto("baPcPreCategory",0,"labels.report.hist.salesSlip.baPcPreCategory"));
		slipColList.add(new ReportColumnInfoDto("baPcPre",0,"labels.report.hist.salesSlip.baPcPre"));
		slipColList.add(new ReportColumnInfoDto("baTel",0,"labels.report.hist.salesSlip.baTel"));
		slipColList.add(new ReportColumnInfoDto("baFax",0,"labels.report.hist.salesSlip.baFax"));
		slipColList.add(new ReportColumnInfoDto("baEmail",0,"labels.report.hist.salesSlip.baEmail"));
		slipColList.add(new ReportColumnInfoDto("baUrl",0,"labels.report.hist.salesSlip.baUrl"));
		slipColList.add(new ReportColumnInfoDto("taxShiftCategory",0,"labels.report.hist.salesSlip.taxShiftCategory"));
		slipColList.add(new ReportColumnInfoDto("taxFractCategory",0,"labels.report.hist.salesSlip.taxFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceFractCategory",0,"labels.report.hist.salesSlip.priceFractCategory"));
		slipColList.add(new ReportColumnInfoDto("ctaxPriceTotal",1,"labels.report.hist.salesSlip.ctaxPriceTotal"));
		slipColList.add(new ReportColumnInfoDto("priceTotal",1,"labels.report.hist.salesSlip.priceTotal"));
		slipColList.add(new ReportColumnInfoDto("gmTotal",1,"labels.report.hist.salesSlip.gmTotal"));
		slipColList.add(new ReportColumnInfoDto("codSc",0,"labels.report.hist.salesSlip.codSc"));
		slipColList.add(new ReportColumnInfoDto("billPrintCount",1,"labels.report.hist.salesSlip.billPrintCount"));
		slipColList.add(new ReportColumnInfoDto("deliveryPrintCount",1,"labels.report.hist.salesSlip.deliveryPrintCount"));
		slipColList.add(new ReportColumnInfoDto("tempDeliveryPrintCount",1,"labels.report.hist.salesSlip.tempDeliveryPrintCount"));
		slipColList.add(new ReportColumnInfoDto("shippingPrintCount",1,"labels.report.hist.salesSlip.shippingPrintCount"));
		slipColList.add(new ReportColumnInfoDto("siPrintCount",1,"labels.report.hist.salesSlip.siPrintCount"));
		slipColList.add(new ReportColumnInfoDto("estimatePrintCount",1,"labels.report.hist.salesSlip.estimatePrintCount"));
		slipColList.add(new ReportColumnInfoDto("delborPrintCount",1,"labels.report.hist.salesSlip.delborPrintCount"));
		slipColList.add(new ReportColumnInfoDto("poPrintCount",1,"labels.report.hist.salesSlip.poPrintCount"));
		slipColList.add(new ReportColumnInfoDto("adlabel",0,"labels.report.hist.salesSlip.adlabel"));
		slipColList.add(new ReportColumnInfoDto("disclaimer",0,"labels.report.hist.salesSlip.disclaimer"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義
		detailColList = new ArrayList<ReportColumnInfoDto>();
		detailColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		detailColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		detailColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		detailColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		detailColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		detailColList.add(new ReportColumnInfoDto("salesLineId",0,"labels.report.hist.salesLine.salesLineId"));
		detailColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.salesLine.status"));
		detailColList.add(new ReportColumnInfoDto("salesSlipId",0,"labels.report.hist.salesLine.salesSlipId"));
		detailColList.add(new ReportColumnInfoDto("lineNo",1,"labels.report.hist.salesLine.salesLineNo"));
		detailColList.add(new ReportColumnInfoDto("roLineId",0,"labels.report.hist.salesLine.roLineId"));
		detailColList.add(new ReportColumnInfoDto("salesDetailCategory",0,"labels.report.hist.salesLine.salesDetailCategory"));
		detailColList.add(new ReportColumnInfoDto("productCode",0,"labels.report.hist.salesLine.productCode"));
		detailColList.add(new ReportColumnInfoDto("customerPcode",0,"labels.report.hist.salesLine.customerPcode"));
		detailColList.add(new ReportColumnInfoDto("productAbstract",0,"labels.report.hist.salesLine.productAbstract"));
		detailColList.add(new ReportColumnInfoDto("productRemarks",0,"labels.report.hist.salesLine.productRemarks"));
		detailColList.add(new ReportColumnInfoDto("quantity",1,"labels.report.hist.salesLine.quantity"));
		detailColList.add(new ReportColumnInfoDto("deliveryProcessCategory",0,"labels.report.hist.salesLine.deliveryProcessCategory"));
		detailColList.add(new ReportColumnInfoDto("unitPrice",1,"labels.report.hist.salesLine.unitPrice"));
		detailColList.add(new ReportColumnInfoDto("unitCategory",0,"labels.report.hist.salesLine.unitCategory"));
		detailColList.add(new ReportColumnInfoDto("unitName",0,"labels.report.hist.salesLine.unitName"));
		detailColList.add(new ReportColumnInfoDto("packQuantity",1,"labels.report.hist.salesLine.packQuantity"));
		detailColList.add(new ReportColumnInfoDto("unitRetailPrice",1,"labels.report.hist.salesLine.unitRetailPrice"));
		detailColList.add(new ReportColumnInfoDto("retailPrice",1,"labels.report.hist.salesLine.retailPrice"));
		detailColList.add(new ReportColumnInfoDto("unitCost",1,"labels.report.hist.salesLine.unitCost"));
		detailColList.add(new ReportColumnInfoDto("cost",1,"labels.report.hist.salesLine.cost"));
		detailColList.add(new ReportColumnInfoDto("taxCategory",0,"labels.report.hist.salesLine.taxCategory"));
		detailColList.add(new ReportColumnInfoDto("ctaxRate",2,"labels.report.hist.salesLine.ctaxRate"));
		detailColList.add(new ReportColumnInfoDto("ctaxPrice",1,"labels.report.hist.salesLine.ctaxPrice"));
		detailColList.add(new ReportColumnInfoDto("gm",1,"labels.report.hist.salesLine.gm"));
		detailColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.salesLine.remarks"));
		detailColList.add(new ReportColumnInfoDto("eadRemarks",0,"labels.report.hist.salesLine.eadRemarks"));
		detailColList.add(new ReportColumnInfoDto("rackCodeSrc",0,"labels.report.hist.salesLine.rackCodeSrc"));
		detailColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		detailColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		detailColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		detailColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		detailColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		detailColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
	}

	/**
	 * 入金入力履歴出力の設定をします.
	 */
	private void setup4Deposit() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME04;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("depositSlipId",0,"labels.report.hist.depositSlip.depositSlipId"));
		slipColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.depositSlip.status"));
		slipColList.add(new ReportColumnInfoDto("depositDate",10,"labels.report.hist.depositSlip.depositDate"));
		slipColList.add(new ReportColumnInfoDto("inputPdate",10,"labels.report.hist.depositSlip.inputPdate"));
		slipColList.add(new ReportColumnInfoDto("depositAnnual",0,"labels.report.hist.depositSlip.depositAnnual"));
		slipColList.add(new ReportColumnInfoDto("depositMonthly",0,"labels.report.hist.depositSlip.depositMonthly"));
		slipColList.add(new ReportColumnInfoDto("depositYm",0,"labels.report.hist.depositSlip.depositYm"));
		slipColList.add(new ReportColumnInfoDto("userId",0,"labels.report.hist.depositSlip.userId"));
		slipColList.add(new ReportColumnInfoDto("userName",0,"labels.report.hist.depositSlip.userName"));
		slipColList.add(new ReportColumnInfoDto("depositAbstract",0,"labels.report.hist.depositSlip.depositAbstract"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.depositSlip.remarks"));
		slipColList.add(new ReportColumnInfoDto("customerCode",0,"labels.report.hist.depositSlip.customerCode"));
		slipColList.add(new ReportColumnInfoDto("customerName",0,"labels.report.hist.depositSlip.customerName"));
		slipColList.add(new ReportColumnInfoDto("customerRemarks",0,"labels.report.hist.depositSlip.customerRemarks"));
		slipColList.add(new ReportColumnInfoDto("customerCommentData",0,"labels.report.hist.depositSlip.customerCommentData"));
		slipColList.add(new ReportColumnInfoDto("cutoffGroup",0,"labels.report.hist.depositSlip.cutoffGroup"));
		slipColList.add(new ReportColumnInfoDto("paybackCycleCategory",0,"labels.report.hist.depositSlip.paybackCycleCategory"));
		slipColList.add(new ReportColumnInfoDto("baCode",0,"labels.report.hist.depositSlip.baCode"));
		slipColList.add(new ReportColumnInfoDto("baName",0,"labels.report.hist.depositSlip.baName"));
		slipColList.add(new ReportColumnInfoDto("baKana",0,"labels.report.hist.depositSlip.baKana"));
		slipColList.add(new ReportColumnInfoDto("baOfficeName",0,"labels.report.hist.depositSlip.baOfficeName"));
		slipColList.add(new ReportColumnInfoDto("baOfficeKana",0,"labels.report.hist.depositSlip.baOfficeKana"));
		slipColList.add(new ReportColumnInfoDto("baDeptName",0,"labels.report.hist.depositSlip.baDeptName"));
		slipColList.add(new ReportColumnInfoDto("baZipCode",0,"labels.report.hist.depositSlip.baZipCode"));
		slipColList.add(new ReportColumnInfoDto("baAddress1",0,"labels.report.hist.depositSlip.baAddress1"));
		slipColList.add(new ReportColumnInfoDto("baAddress2",0,"labels.report.hist.depositSlip.baAddress2"));
		slipColList.add(new ReportColumnInfoDto("baPcName",0,"labels.report.hist.depositSlip.baPcName"));
		slipColList.add(new ReportColumnInfoDto("baPcKana",0,"labels.report.hist.depositSlip.baPcKana"));
		slipColList.add(new ReportColumnInfoDto("baPcPreCatrgory",0,"labels.report.hist.depositSlip.baPcPreCatrgory"));
		slipColList.add(new ReportColumnInfoDto("baPcPre",0,"labels.report.hist.depositSlip.baPcPre"));
		slipColList.add(new ReportColumnInfoDto("baTel",0,"labels.report.hist.depositSlip.baTel"));
		slipColList.add(new ReportColumnInfoDto("baFax",0,"labels.report.hist.depositSlip.baFax"));
		slipColList.add(new ReportColumnInfoDto("baEmail",0,"labels.report.hist.depositSlip.baEmail"));
		slipColList.add(new ReportColumnInfoDto("baUrl",0,"labels.report.hist.depositSlip.baUrl"));
		slipColList.add(new ReportColumnInfoDto("salesCmCategory",0,"labels.report.hist.depositSlip.salesCmCategory"));
		slipColList.add(new ReportColumnInfoDto("depositCategory",0,"labels.report.hist.depositSlip.depositCategory"));
		slipColList.add(new ReportColumnInfoDto("depositTotal",1,"labels.report.hist.depositSlip.depositTotal"));
		slipColList.add(new ReportColumnInfoDto("billId",0,"labels.report.hist.depositSlip.billId"));
		slipColList.add(new ReportColumnInfoDto("billCutoffDate",10,"labels.report.hist.depositSlip.billCutoffDate"));
		slipColList.add(new ReportColumnInfoDto("billCutoffPdate",11,"labels.report.hist.depositSlip.billCutoffPdate"));
		slipColList.add(new ReportColumnInfoDto("artId",0,"labels.report.hist.depositSlip.artId"));
		slipColList.add(new ReportColumnInfoDto("salesSlipId",0,"labels.report.hist.depositSlip.salesSlipId"));
		slipColList.add(new ReportColumnInfoDto("depositMethodTypeCategory",0,"labels.report.hist.depositSlip.depositMethodTypeCategory"));
		slipColList.add(new ReportColumnInfoDto("taxFractCategory",0,"labels.report.hist.depositSlip.taxFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceFractCategory",0,"labels.report.hist.depositSlip.priceFractCategory"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義
		detailColList = new ArrayList<ReportColumnInfoDto>();
		detailColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		detailColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		detailColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		detailColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		detailColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		detailColList.add(new ReportColumnInfoDto("depositLineId",0,"labels.report.hist.depositLine.depositLineId"));
		detailColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.depositLine.status"));
		detailColList.add(new ReportColumnInfoDto("depositSlipId",0,"labels.report.hist.depositLine.depositSlipId"));
		detailColList.add(new ReportColumnInfoDto("lineNo",1,"labels.report.hist.depositLine.depositLineNo"));
		detailColList.add(new ReportColumnInfoDto("depositCategory",0,"labels.report.hist.depositLine.depositCategory"));
		detailColList.add(new ReportColumnInfoDto("price",1,"labels.report.hist.depositLine.price"));
		detailColList.add(new ReportColumnInfoDto("instDate",10,"labels.report.hist.depositLine.instDate"));
		detailColList.add(new ReportColumnInfoDto("instNo",0,"labels.report.hist.depositLine.instNo"));
		detailColList.add(new ReportColumnInfoDto("bankId",0,"labels.report.hist.depositLine.bankId"));
		detailColList.add(new ReportColumnInfoDto("bankInfo",0,"labels.report.hist.depositLine.bankInfo"));
		detailColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.depositLine.remarks"));
		detailColList.add(new ReportColumnInfoDto("salesLineId",0,"labels.report.hist.depositLine.salesLineId"));
		detailColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		detailColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		detailColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		detailColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		detailColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		detailColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
	}

	/**
	 * 発注入力履歴出力の設定をします.
	 */
	private void setup4Porder() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME05;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("poSlipId",0,"labels.report.hist.poSlip.poSlipId"));
		slipColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.poSlip.status"));
		slipColList.add(new ReportColumnInfoDto("poDate",10,"labels.report.hist.poSlip.poDate"));
		slipColList.add(new ReportColumnInfoDto("poAnnual",0,"labels.report.hist.poSlip.poAnnual"));
		slipColList.add(new ReportColumnInfoDto("poMonthly",0,"labels.report.hist.poSlip.poMonthly"));
		slipColList.add(new ReportColumnInfoDto("poYm",0,"labels.report.hist.poSlip.poYm"));
		slipColList.add(new ReportColumnInfoDto("deliveryDate",10,"labels.report.hist.poSlip.deliveryDate"));
		slipColList.add(new ReportColumnInfoDto("userId",0,"labels.report.hist.poSlip.userId"));
		slipColList.add(new ReportColumnInfoDto("userName",0,"labels.report.hist.poSlip.userName"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.poSlip.remarks"));
		slipColList.add(new ReportColumnInfoDto("supplierCode",0,"labels.report.hist.poSlip.supplierCode"));
		slipColList.add(new ReportColumnInfoDto("supplierName",0,"labels.report.hist.poSlip.supplierName"));
		slipColList.add(new ReportColumnInfoDto("supplierKana",0,"labels.report.hist.poSlip.supplierKana"));
		slipColList.add(new ReportColumnInfoDto("supplierAbbr",0,"labels.report.hist.poSlip.supplierAbbr"));
		slipColList.add(new ReportColumnInfoDto("supplierDeptName",0,"labels.report.hist.poSlip.supplierDeptName"));
		slipColList.add(new ReportColumnInfoDto("supplierZipCode",0,"labels.report.hist.poSlip.supplierZipCode"));
		slipColList.add(new ReportColumnInfoDto("supplierAddress1",0,"labels.report.hist.poSlip.supplierAddress1"));
		slipColList.add(new ReportColumnInfoDto("supplierAddress2",0,"labels.report.hist.poSlip.supplierAddress2"));
		slipColList.add(new ReportColumnInfoDto("supplierPcName",0,"labels.report.hist.poSlip.supplierPcName"));
		slipColList.add(new ReportColumnInfoDto("supplierPcKana",0,"labels.report.hist.poSlip.supplierPcKana"));
		slipColList.add(new ReportColumnInfoDto("supplierPcPreCategory",0,"labels.report.hist.poSlip.supplierPcPreCategory"));
		slipColList.add(new ReportColumnInfoDto("supplierPcPre",0,"labels.report.hist.poSlip.supplierPcPre"));
		slipColList.add(new ReportColumnInfoDto("supplierPcPost",0,"labels.report.hist.poSlip.supplierPcPost"));
		slipColList.add(new ReportColumnInfoDto("supplierTel",0,"labels.report.hist.poSlip.supplierTel"));
		slipColList.add(new ReportColumnInfoDto("supplierFax",0,"labels.report.hist.poSlip.supplierFax"));
		slipColList.add(new ReportColumnInfoDto("supplierEmail",0,"labels.report.hist.poSlip.supplierEmail"));
		slipColList.add(new ReportColumnInfoDto("supplierUrl",0,"labels.report.hist.poSlip.supplierUrl"));
		slipColList.add(new ReportColumnInfoDto("transportCategory",0,"labels.report.hist.poSlip.transportCategory"));
		slipColList.add(new ReportColumnInfoDto("taxShiftCategory",0,"labels.report.hist.poSlip.taxShiftCategory"));
		slipColList.add(new ReportColumnInfoDto("taxFractCategory",0,"labels.report.hist.poSlip.taxFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceFractCategory",0,"labels.report.hist.poSlip.priceFractCategory"));
		slipColList.add(new ReportColumnInfoDto("rateId",0,"labels.report.hist.poSlip.rateId"));
		slipColList.add(new ReportColumnInfoDto("supplierCmCategory",0,"labels.report.hist.poSlip.supplierCmCategory"));
		slipColList.add(new ReportColumnInfoDto("priceTotal",1,"labels.report.hist.poSlip.priceTotal"));
		slipColList.add(new ReportColumnInfoDto("ctaxTotal",1,"labels.report.hist.poSlip.ctaxTotal"));
		slipColList.add(new ReportColumnInfoDto("fePriceTotal",2,"labels.report.hist.poSlip.fePriceTotal"));
		slipColList.add(new ReportColumnInfoDto("printCount",1,"labels.report.hist.poSlip.printCount"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義
		detailColList = new ArrayList<ReportColumnInfoDto>();
		detailColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		detailColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		detailColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		detailColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		detailColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		detailColList.add(new ReportColumnInfoDto("poLineId",0,"labels.report.hist.poLine.poLineId"));
		detailColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.poLine.status"));
		detailColList.add(new ReportColumnInfoDto("poSlipId",0,"labels.report.hist.poLine.poSlipId"));
		detailColList.add(new ReportColumnInfoDto("lineNo",1,"labels.report.hist.poLine.lineNo"));
		detailColList.add(new ReportColumnInfoDto("productCode",0,"labels.report.hist.poLine.productCode"));
		detailColList.add(new ReportColumnInfoDto("supplierPcode",0,"labels.report.hist.poLine.supplierPcode"));
		detailColList.add(new ReportColumnInfoDto("productAbstract",0,"labels.report.hist.poLine.productAbstract"));
		detailColList.add(new ReportColumnInfoDto("productRemarks",0,"labels.report.hist.poLine.productRemarks"));
		detailColList.add(new ReportColumnInfoDto("quantity",1,"labels.report.hist.poLine.quantity"));
		detailColList.add(new ReportColumnInfoDto("tempUnitPriceCategory",0,"labels.report.hist.poLine.tempUnitPriceCategory"));
		detailColList.add(new ReportColumnInfoDto("taxCategory",0,"labels.report.hist.poLine.taxCategory"));
		detailColList.add(new ReportColumnInfoDto("supplierCmCategory",0,"labels.report.hist.poLine.supplierCmCategory"));
		detailColList.add(new ReportColumnInfoDto("unitPrice",1,"labels.report.hist.poLine.unitPrice"));
		detailColList.add(new ReportColumnInfoDto("price",1,"labels.report.hist.poLine.price"));
		detailColList.add(new ReportColumnInfoDto("ctaxPrice",1,"labels.report.hist.poLine.ctaxPrice"));
		detailColList.add(new ReportColumnInfoDto("ctaxRate",2,"labels.report.hist.poLine.ctaxRate"));
		detailColList.add(new ReportColumnInfoDto("dolUnitPrice",2,"labels.report.hist.poLine.dolUnitPrice"));
		detailColList.add(new ReportColumnInfoDto("dolPrice",2,"labels.report.hist.poLine.dolPrice"));
		detailColList.add(new ReportColumnInfoDto("rate",2,"labels.report.hist.poLine.rate"));
		detailColList.add(new ReportColumnInfoDto("deliveryDate",10,"labels.report.hist.poLine.deliveryDate"));
		detailColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.poLine.remarks"));
		detailColList.add(new ReportColumnInfoDto("restQuantity",1,"labels.report.hist.poLine.restQuantity"));
		detailColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		detailColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		detailColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		detailColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		detailColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		detailColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
	}

	/**
	 * 仕入入力履歴出力の設定をします.
	 */
	private void setup4Purchase() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME06;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("supplierSlipId",0,"labels.report.hist.purchaseSlip.supplierSlipId"));
		slipColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.purchaseSlip.status"));
		slipColList.add(new ReportColumnInfoDto("supplierDate",10,"labels.report.hist.purchaseSlip.supplierDate"));
		slipColList.add(new ReportColumnInfoDto("supplierAnnual",0,"labels.report.hist.purchaseSlip.supplierAnnual"));
		slipColList.add(new ReportColumnInfoDto("supplierMonthly",0,"labels.report.hist.purchaseSlip.supplierMonthly"));
		slipColList.add(new ReportColumnInfoDto("supplierYm",0,"labels.report.hist.purchaseSlip.supplierYm"));
		slipColList.add(new ReportColumnInfoDto("userId",0,"labels.report.hist.purchaseSlip.userId"));
		slipColList.add(new ReportColumnInfoDto("userName",0,"labels.report.hist.purchaseSlip.userName"));
		slipColList.add(new ReportColumnInfoDto("supplierSlipCategory",0,"labels.report.hist.purchaseSlip.supplierSlipCategory"));
		slipColList.add(new ReportColumnInfoDto("supplierCode",0,"labels.report.hist.purchaseSlip.supplierCode"));
		slipColList.add(new ReportColumnInfoDto("supplierName",0,"labels.report.hist.purchaseSlip.supplierName"));
		slipColList.add(new ReportColumnInfoDto("supplierCmCategory",0,"labels.report.hist.purchaseSlip.supplierCmCategory"));
		slipColList.add(new ReportColumnInfoDto("deliveryDate",10,"labels.report.hist.purchaseSlip.deliveryDate"));
		slipColList.add(new ReportColumnInfoDto("rateId",0,"labels.report.hist.purchaseSlip.rateId"));
		slipColList.add(new ReportColumnInfoDto("taxShiftCategory",0,"labels.report.hist.purchaseSlip.taxShiftCategory"));
		slipColList.add(new ReportColumnInfoDto("taxFractCategory",0,"labels.report.hist.purchaseSlip.taxFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceFractCategory",0,"labels.report.hist.purchaseSlip.priceFractCategory"));
		slipColList.add(new ReportColumnInfoDto("ctaxTotal",1,"labels.report.hist.purchaseSlip.ctaxTotal"));
		slipColList.add(new ReportColumnInfoDto("priceTotal",1,"labels.report.hist.purchaseSlip.priceTotal"));
		slipColList.add(new ReportColumnInfoDto("fePriceTotal",2,"labels.report.hist.purchaseSlip.fePriceTotal"));
		slipColList.add(new ReportColumnInfoDto("poSlipId",0,"labels.report.hist.purchaseSlip.poSlipId"));
		slipColList.add(new ReportColumnInfoDto("paymentSlipId",0,"labels.report.hist.purchaseSlip.paymentSlipId"));
		slipColList.add(new ReportColumnInfoDto("supplierPaymentDate",10,"labels.report.hist.purchaseSlip.supplierPaymentDate"));
		slipColList.add(new ReportColumnInfoDto("paymentCutoffDate",10,"labels.report.hist.purchaseSlip.paymentCutoffDate"));
		slipColList.add(new ReportColumnInfoDto("paymentPdate",10,"labels.report.hist.purchaseSlip.paymentPdate"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.purchaseSlip.remarks"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義
		detailColList = new ArrayList<ReportColumnInfoDto>();
		detailColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		detailColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		detailColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		detailColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		detailColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		detailColList.add(new ReportColumnInfoDto("supplierLineId",0,"labels.report.hist.purchaseLine.supplierLineId"));
		detailColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.purchaseLine.status"));
		detailColList.add(new ReportColumnInfoDto("supplierSlipId",0,"labels.report.hist.purchaseLine.supplierSlipId"));
		detailColList.add(new ReportColumnInfoDto("lineNo",1,"labels.report.hist.purchaseLine.supplierLineNo"));
		detailColList.add(new ReportColumnInfoDto("productCode",0,"labels.report.hist.purchaseLine.productCode"));
		detailColList.add(new ReportColumnInfoDto("supplierPcode",0,"labels.report.hist.purchaseLine.supplierPcode"));
		detailColList.add(new ReportColumnInfoDto("productAbstract",0,"labels.report.hist.purchaseLine.productAbstract"));
		detailColList.add(new ReportColumnInfoDto("productRemarks",0,"labels.report.hist.purchaseLine.productRemarks"));
		detailColList.add(new ReportColumnInfoDto("supplierDetailCategory",0,"labels.report.hist.purchaseLine.supplierDetailCategory"));
		detailColList.add(new ReportColumnInfoDto("deliveryProcessCategory",0,"labels.report.hist.purchaseLine.deliveryProcessCategory"));
		detailColList.add(new ReportColumnInfoDto("tempUnitPriceCategory",0,"labels.report.hist.purchaseLine.tempUnitPriceCategory"));
		detailColList.add(new ReportColumnInfoDto("taxCategory",0,"labels.report.hist.purchaseLine.taxCategory"));
		detailColList.add(new ReportColumnInfoDto("deliveryDate",10,"labels.report.hist.purchaseLine.deliveryDate"));
		detailColList.add(new ReportColumnInfoDto("quantity",1,"labels.report.hist.purchaseLine.quantity"));
		detailColList.add(new ReportColumnInfoDto("unitPrice",1,"labels.report.hist.purchaseLine.unitPrice"));
		detailColList.add(new ReportColumnInfoDto("price",1,"labels.report.hist.purchaseLine.price"));
		detailColList.add(new ReportColumnInfoDto("ctaxRate",2,"labels.report.hist.purchaseLine.ctaxRate"));
		detailColList.add(new ReportColumnInfoDto("ctaxPrice",1,"labels.report.hist.purchaseLine.ctaxPrice"));
		detailColList.add(new ReportColumnInfoDto("dolUnitPrice",2,"labels.report.hist.purchaseLine.dolUnitPrice"));
		detailColList.add(new ReportColumnInfoDto("dolPrice",2,"labels.report.hist.purchaseLine.dolPrice"));
		detailColList.add(new ReportColumnInfoDto("rate",1,"labels.report.hist.purchaseLine.rate"));
		detailColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.purchaseLine.remarks"));
		detailColList.add(new ReportColumnInfoDto("rackCode",0,"labels.report.hist.purchaseLine.rackCode"));
		detailColList.add(new ReportColumnInfoDto("rackName",0,"labels.report.hist.purchaseLine.rackName"));
		detailColList.add(new ReportColumnInfoDto("poLineId",0,"labels.report.hist.purchaseLine.poLineId"));
		detailColList.add(new ReportColumnInfoDto("paymentLineId",0,"labels.report.hist.purchaseLine.paymentLineId"));
		detailColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		detailColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		detailColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		detailColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		detailColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		detailColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
	}

	/**
	 * 支払履歴出力の設定をします.
	 */
	private void setup4Payment() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME07;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("paymentSlipId",0,"labels.report.hist.paymentSlip.paymentSlipId"));
		slipColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.paymentSlip.status"));
		slipColList.add(new ReportColumnInfoDto("paymentDate",10,"labels.report.hist.paymentSlip.paymentDate"));
		slipColList.add(new ReportColumnInfoDto("paymentAnnual",0,"labels.report.hist.paymentSlip.paymentAnnual"));
		slipColList.add(new ReportColumnInfoDto("paymentMonthly",0,"labels.report.hist.paymentSlip.paymentMonthly"));
		slipColList.add(new ReportColumnInfoDto("paymentYm",0,"labels.report.hist.paymentSlip.paymentYm"));
		slipColList.add(new ReportColumnInfoDto("userId",0,"labels.report.hist.paymentSlip.userId"));
		slipColList.add(new ReportColumnInfoDto("userName",0,"labels.report.hist.paymentSlip.userName"));
		slipColList.add(new ReportColumnInfoDto("paymentSlipCategory",0,"labels.report.hist.paymentSlip.paymentSlipCategory"));
		slipColList.add(new ReportColumnInfoDto("supplierCode",0,"labels.report.hist.paymentSlip.supplierCode"));
		slipColList.add(new ReportColumnInfoDto("supplierName",0,"labels.report.hist.paymentSlip.supplierName"));
		slipColList.add(new ReportColumnInfoDto("rateId",0,"labels.report.hist.paymentSlip.rateId"));
		slipColList.add(new ReportColumnInfoDto("taxShiftCategory",0,"labels.report.hist.paymentSlip.taxShiftCategory"));
		slipColList.add(new ReportColumnInfoDto("taxFractCategory",0,"labels.report.hist.paymentSlip.taxFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceFractCategory",0,"labels.report.hist.paymentSlip.priceFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceTotal",1,"labels.report.hist.paymentSlip.priceTotal"));
		slipColList.add(new ReportColumnInfoDto("fePriceTotal",2,"labels.report.hist.paymentSlip.fePriceTotal"));
		slipColList.add(new ReportColumnInfoDto("poSlipId",0,"labels.report.hist.paymentSlip.poSlipId"));
		slipColList.add(new ReportColumnInfoDto("supplierSlipId",0,"labels.report.hist.paymentSlip.supplierSlipId"));
		slipColList.add(new ReportColumnInfoDto("aptBalanceId",0,"labels.report.hist.paymentSlip.aptBalanceId"));
		slipColList.add(new ReportColumnInfoDto("paymentCutoffDate",10,"labels.report.hist.paymentSlip.paymentCutoffDate"));
		slipColList.add(new ReportColumnInfoDto("paymentPdate",10,"labels.report.hist.paymentSlip.paymentPdate"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.paymentSlip.remarks"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義
		detailColList = new ArrayList<ReportColumnInfoDto>();
		detailColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		detailColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		detailColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		detailColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		detailColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		detailColList.add(new ReportColumnInfoDto("paymentLineId",0,"labels.report.hist.paymentLine.paymentLineId"));
		detailColList.add(new ReportColumnInfoDto("status",0,"labels.report.hist.paymentLine.status"));
		detailColList.add(new ReportColumnInfoDto("paymentSlipId",0,"labels.report.hist.paymentLine.paymentSlipId"));
		detailColList.add(new ReportColumnInfoDto("lineNo",1,"labels.report.hist.paymentLine.paymentLineNo"));
		detailColList.add(new ReportColumnInfoDto("paymentCategory",0,"labels.report.hist.paymentLine.paymentCategory"));
		detailColList.add(new ReportColumnInfoDto("productCode",0,"labels.report.hist.paymentLine.productCode"));
		detailColList.add(new ReportColumnInfoDto("productAbstract",0,"labels.report.hist.paymentLine.productAbstract"));
		detailColList.add(new ReportColumnInfoDto("supplierDate",10,"labels.report.hist.paymentLine.supplierDate"));
		detailColList.add(new ReportColumnInfoDto("quantity",1,"labels.report.hist.paymentLine.quantity"));
		detailColList.add(new ReportColumnInfoDto("unitPrice",1,"labels.report.hist.paymentLine.unitPrice"));
		detailColList.add(new ReportColumnInfoDto("price",1,"labels.report.hist.paymentLine.price"));
		detailColList.add(new ReportColumnInfoDto("dolUnitPrice",2,"labels.report.hist.paymentLine.dolUnitPrice"));
		detailColList.add(new ReportColumnInfoDto("dolPrice",2,"labels.report.hist.paymentLine.dolPrice"));
		detailColList.add(new ReportColumnInfoDto("rate",2,"labels.report.hist.paymentLine.rate"));
		detailColList.add(new ReportColumnInfoDto("ctaxRate",2,"labels.report.hist.paymentLine.ctaxRate"));
		detailColList.add(new ReportColumnInfoDto("ctaxPrice",1,"labels.report.hist.paymentLine.ctaxPrice"));
		detailColList.add(new ReportColumnInfoDto("poLineId",0,"labels.report.hist.paymentLine.poLineId"));
		detailColList.add(new ReportColumnInfoDto("supplierLineId",0,"labels.report.hist.paymentLine.supplierLineId"));
		detailColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.paymentLine.remarks"));
		detailColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		detailColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		detailColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		detailColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		detailColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		detailColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
	}

	/**
	 * 入出庫入力履歴出力の設定をします.
	 */
	private void setup4Stock() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME08;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("eadSlipId",0,"labels.report.hist.stockSlip.eadSlipId"));
		slipColList.add(new ReportColumnInfoDto("eadDate",10,"labels.report.hist.stockSlip.eadDate"));
		slipColList.add(new ReportColumnInfoDto("eadAnnual",0,"labels.report.hist.stockSlip.eadAnnual"));
		slipColList.add(new ReportColumnInfoDto("eadMonthly",0,"labels.report.hist.stockSlip.eadMonthly"));
		slipColList.add(new ReportColumnInfoDto("eadYm",0,"labels.report.hist.stockSlip.eadYm"));
		slipColList.add(new ReportColumnInfoDto("userId",0,"labels.report.hist.stockSlip.userId"));
		slipColList.add(new ReportColumnInfoDto("userName",0,"labels.report.hist.stockSlip.userName"));
		slipColList.add(new ReportColumnInfoDto("eadSlipCategory",0,"labels.report.hist.stockSlip.eadSlipCategory"));
		slipColList.add(new ReportColumnInfoDto("eadCategory",0,"labels.report.hist.stockSlip.eadCategory"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.stockSlip.remarks"));
		slipColList.add(new ReportColumnInfoDto("srcFunc",0,"labels.report.hist.stockSlip.srcFunc"));
		slipColList.add(new ReportColumnInfoDto("salesSlipId",0,"labels.report.hist.stockSlip.salesSlipId"));
		slipColList.add(new ReportColumnInfoDto("supplierSlipId",0,"labels.report.hist.stockSlip.supplierSlipId"));
		slipColList.add(new ReportColumnInfoDto("moveDepositSlipId",0,"labels.report.hist.stockSlip.moveDepositSlipId"));
		slipColList.add(new ReportColumnInfoDto("stockPdate",10,"labels.report.hist.stockSlip.stockPdate"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義
		detailColList = new ArrayList<ReportColumnInfoDto>();
		detailColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		detailColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		detailColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		detailColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		detailColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		detailColList.add(new ReportColumnInfoDto("eadLineId",0,"labels.report.hist.stockLine.eadLineId"));
		detailColList.add(new ReportColumnInfoDto("eadSlipId",0,"labels.report.hist.stockLine.eadSlipId"));
		detailColList.add(new ReportColumnInfoDto("lineNo",1,"labels.report.hist.stockLine.eadLineNo"));
		detailColList.add(new ReportColumnInfoDto("productCode",0,"labels.report.hist.stockLine.productCode"));
		detailColList.add(new ReportColumnInfoDto("productAbstract",0,"labels.report.hist.stockLine.productAbstract"));
		detailColList.add(new ReportColumnInfoDto("rackCode",0,"labels.report.hist.stockLine.rackCode"));
		detailColList.add(new ReportColumnInfoDto("rackName",0,"labels.report.hist.stockLine.rackName"));
		detailColList.add(new ReportColumnInfoDto("quantity",1,"labels.report.hist.stockLine.quantity"));
		detailColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.stockLine.remarks"));
		detailColList.add(new ReportColumnInfoDto("salesLineId",0,"labels.report.hist.stockLine.salesLineId"));
		detailColList.add(new ReportColumnInfoDto("supplierLineId",0,"labels.report.hist.stockLine.supplierLineId"));
		detailColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		detailColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		detailColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		detailColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		detailColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		detailColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
	}

	/**
	 * 顧客マスタ履歴出力の設定をします.
	 */
	private void setup4Customer() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME09;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("customerCode",0,"labels.report.hist.customerMst.customerCode"));
		slipColList.add(new ReportColumnInfoDto("customerName",0,"labels.report.hist.customerMst.customerName"));
		slipColList.add(new ReportColumnInfoDto("customerKana",0,"labels.report.hist.customerMst.customerKana"));
		slipColList.add(new ReportColumnInfoDto("customerOfficeName",0,"labels.report.hist.customerMst.customerOfficeName"));
		slipColList.add(new ReportColumnInfoDto("customerOfficeKana",0,"labels.report.hist.customerMst.customerOfficeKana"));
		slipColList.add(new ReportColumnInfoDto("customerAbbr",0,"labels.report.hist.customerMst.customerAbbr"));
		slipColList.add(new ReportColumnInfoDto("customerDeptName",0,"labels.report.hist.customerMst.customerDeptName"));
		slipColList.add(new ReportColumnInfoDto("customerZipCode",0,"labels.report.hist.customerMst.customerZipCode"));
		slipColList.add(new ReportColumnInfoDto("customerAddress1",0,"labels.report.hist.customerMst.customerAddress1"));
		slipColList.add(new ReportColumnInfoDto("customerAddress2",0,"labels.report.hist.customerMst.customerAddress2"));
		slipColList.add(new ReportColumnInfoDto("customerPcPost",0,"labels.report.hist.customerMst.customerPcPost"));
		slipColList.add(new ReportColumnInfoDto("customerPcName",0,"labels.report.hist.customerMst.customerPcName"));
		slipColList.add(new ReportColumnInfoDto("customerPcKana",0,"labels.report.hist.customerMst.customerPcKana"));
		slipColList.add(new ReportColumnInfoDto("customerPcPreCategory",0,"labels.report.hist.customerMst.customerPcPreCategory"));
		slipColList.add(new ReportColumnInfoDto("customerTel",0,"labels.report.hist.customerMst.customerTel"));
		slipColList.add(new ReportColumnInfoDto("customerFax",0,"labels.report.hist.customerMst.customerFax"));
		slipColList.add(new ReportColumnInfoDto("customerEmail",0,"labels.report.hist.customerMst.customerEmail"));
		slipColList.add(new ReportColumnInfoDto("customerUrl",0,"labels.report.hist.customerMst.customerUrl"));
		slipColList.add(new ReportColumnInfoDto("customerBusinessCategory",0,"labels.report.hist.customerMst.customerBusinessCategory"));
		slipColList.add(new ReportColumnInfoDto("customerJobCategory",0,"labels.report.hist.customerMst.customerJobCategory"));
		slipColList.add(new ReportColumnInfoDto("customerRoCategory",0,"labels.report.hist.customerMst.customerRoCategory"));
		slipColList.add(new ReportColumnInfoDto("customerRankCategory",0,"labels.report.hist.customerMst.customerRankCategory"));
		slipColList.add(new ReportColumnInfoDto("customerUpdFlag",0,"labels.report.hist.customerMst.customerUpdFlag"));
		slipColList.add(new ReportColumnInfoDto("salesCmCategory",0,"labels.report.hist.customerMst.salesCmCategory"));
		slipColList.add(new ReportColumnInfoDto("taxShiftCategory",0,"labels.report.hist.customerMst.taxShiftCategory"));
		slipColList.add(new ReportColumnInfoDto("rate",2,"labels.report.hist.customerMst.rate"));
		slipColList.add(new ReportColumnInfoDto("maxCreditLimit",1,"labels.report.hist.customerMst.maxCreditLimit"));
		slipColList.add(new ReportColumnInfoDto("lastCutoffDate",10,"labels.report.hist.customerMst.lastCutoffDate"));
		slipColList.add(new ReportColumnInfoDto("cutoffGroup",0,"labels.report.hist.customerMst.cutoffGroup"));
		slipColList.add(new ReportColumnInfoDto("paybackTypeCategory",0,"labels.report.hist.customerMst.paybackTypeCategory"));
		slipColList.add(new ReportColumnInfoDto("paybackCycleCategory",0,"labels.report.hist.customerMst.paybackCycleCategory"));
		slipColList.add(new ReportColumnInfoDto("taxFractCategory",0,"labels.report.hist.customerMst.taxFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceFractCategory",0,"labels.report.hist.customerMst.priceFractCategory"));
		slipColList.add(new ReportColumnInfoDto("tempDeliverySlipFlag",0,"labels.report.hist.customerMst.tempDeliverySlipFlag"));
		slipColList.add(new ReportColumnInfoDto("paymentName",0,"labels.report.hist.customerMst.paymentName"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.customerMst.remarks"));
		slipColList.add(new ReportColumnInfoDto("firstSalesDate",10,"labels.report.hist.customerMst.firstSalesDate"));
		slipColList.add(new ReportColumnInfoDto("lastSalesDate",10,"labels.report.hist.customerMst.lastSalesDate"));
		slipColList.add(new ReportColumnInfoDto("salesPriceTotal",1,"labels.report.hist.customerMst.salesPriceTotal"));
		slipColList.add(new ReportColumnInfoDto("salesPriceLsm",1,"labels.report.hist.customerMst.salesPriceLsm"));
		slipColList.add(new ReportColumnInfoDto("commentData",0,"labels.report.hist.customerMst.commentData"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義（明細なし）
		detailColList = new ArrayList<ReportColumnInfoDto>();
		detailColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		detailColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		detailColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		detailColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		detailColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));

		detailColList.add(new ReportColumnInfoDto("customerCode",0,"labels.report.hist.deliveryMst.customerCode"));
		detailColList.add(new ReportColumnInfoDto("categoryCodeName",0,"labels.report.hist.deliveryMst.categoryCodeName"));

		detailColList.add(new ReportColumnInfoDto("deliveryCode",0,"labels.report.hist.deliveryMst.deliveryCode"));
		detailColList.add(new ReportColumnInfoDto("deliveryName",0,"labels.report.hist.deliveryMst.deliveryName"));
		detailColList.add(new ReportColumnInfoDto("deliveryKana",0,"labels.report.hist.deliveryMst.deliveryKana"));
		detailColList.add(new ReportColumnInfoDto("deliveryOfficeName",0,"labels.report.hist.deliveryMst.deliveryOfficeName"));
		detailColList.add(new ReportColumnInfoDto("deliveryOfficeKana",0,"labels.report.hist.deliveryMst.deliveryOfficeKana"));
		detailColList.add(new ReportColumnInfoDto("deliveryDeptName",0,"labels.report.hist.deliveryMst.deliveryDeptName"));
		detailColList.add(new ReportColumnInfoDto("deliveryZipCode",0,"labels.report.hist.deliveryMst.deliveryZipCode"));
		detailColList.add(new ReportColumnInfoDto("deliveryAddress1",0,"labels.report.hist.deliveryMst.deliveryAddress1"));
		detailColList.add(new ReportColumnInfoDto("deliveryAddress2",0,"labels.report.hist.deliveryMst.deliveryAddress2"));
		detailColList.add(new ReportColumnInfoDto("deliveryPcName",0,"labels.report.hist.deliveryMst.deliveryPcName"));
		detailColList.add(new ReportColumnInfoDto("deliveryPcKana",0,"labels.report.hist.deliveryMst.deliveryPcKana"));
		detailColList.add(new ReportColumnInfoDto("deliveryPcPreCategory",0,"labels.report.hist.deliveryMst.deliveryPcPreCategory"));
		detailColList.add(new ReportColumnInfoDto("deliveryTel",0,"labels.report.hist.deliveryMst.deliveryTel"));
		detailColList.add(new ReportColumnInfoDto("deliveryFax",0,"labels.report.hist.deliveryMst.deliveryFax"));
		detailColList.add(new ReportColumnInfoDto("deliveryEmail",0,"labels.report.hist.deliveryMst.deliveryEmail"));
		detailColList.add(new ReportColumnInfoDto("deliveryUrl",0,"labels.report.hist.deliveryMst.deliveryUrl"));
		detailColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.deliveryMst.remarks"));
		detailColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		detailColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		detailColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		detailColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		detailColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		detailColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
	}

	/**
	 * 商品マスタ履歴出力の設定をします.
	 */
	private void setup4Product() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME10;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("productCode",0,"labels.report.hist.productMst.productCode"));
		slipColList.add(new ReportColumnInfoDto("productName",0,"labels.report.hist.productMst.productName"));
		slipColList.add(new ReportColumnInfoDto("productKana",0,"labels.report.hist.productMst.productKana"));
		slipColList.add(new ReportColumnInfoDto("onlinePcode",0,"labels.report.hist.productMst.onlinePcode"));
		slipColList.add(new ReportColumnInfoDto("supplierPcode",0,"labels.report.hist.productMst.supplierPcode"));
		slipColList.add(new ReportColumnInfoDto("supplierCode",0,"labels.report.hist.productMst.supplierCode"));
		slipColList.add(new ReportColumnInfoDto("rackCode",0,"labels.report.hist.productMst.rackCode"));
		slipColList.add(new ReportColumnInfoDto("supplierPriceYen",1,"labels.report.hist.productMst.supplierPriceYen"));
		slipColList.add(new ReportColumnInfoDto("supplierPriceDol",2,"labels.report.hist.productMst.supplierPriceDol"));
		slipColList.add(new ReportColumnInfoDto("retailPrice",1,"labels.report.hist.productMst.retailPrice"));
		slipColList.add(new ReportColumnInfoDto("soRate",2,"labels.report.hist.productMst.soRate"));
		slipColList.add(new ReportColumnInfoDto("unitCategory",0,"labels.report.hist.productMst.unitCategory"));
		slipColList.add(new ReportColumnInfoDto("packQuantity",1,"labels.report.hist.productMst.packQuantity"));
		slipColList.add(new ReportColumnInfoDto("janPcode",0,"labels.report.hist.productMst.janPcode"));
		slipColList.add(new ReportColumnInfoDto("width",2,"labels.report.hist.productMst.width"));
		slipColList.add(new ReportColumnInfoDto("widthUnitSizeCategory",0,"labels.report.hist.productMst.widthUnitSizeCategory"));
		slipColList.add(new ReportColumnInfoDto("depth",2,"labels.report.hist.productMst.depth"));
		slipColList.add(new ReportColumnInfoDto("depthUnitSizeCategory",0,"labels.report.hist.productMst.depthUnitSizeCategory"));
		slipColList.add(new ReportColumnInfoDto("height",2,"labels.report.hist.productMst.height"));
		slipColList.add(new ReportColumnInfoDto("heightUnitSizeCategory",0,"labels.report.hist.productMst.heightUnitSizeCategory"));
		slipColList.add(new ReportColumnInfoDto("weight",2,"labels.report.hist.productMst.weight"));
		slipColList.add(new ReportColumnInfoDto("weightUnitSizeCategory",0,"labels.report.hist.productMst.weightUnitSizeCategory"));
		slipColList.add(new ReportColumnInfoDto("length",2,"labels.report.hist.productMst.length"));
		slipColList.add(new ReportColumnInfoDto("lengthUnitSizeCategory",0,"labels.report.hist.productMst.lengthUnitSizeCategory"));
		slipColList.add(new ReportColumnInfoDto("poLot",1,"labels.report.hist.productMst.poLot"));
		slipColList.add(new ReportColumnInfoDto("lotUpdFlag",1,"labels.report.hist.productMst.lotUpdFlag"));
		slipColList.add(new ReportColumnInfoDto("leadTime",1,"labels.report.hist.productMst.leadTime"));
		slipColList.add(new ReportColumnInfoDto("poNum",1,"labels.report.hist.productMst.poNum"));
		slipColList.add(new ReportColumnInfoDto("poUpdFlag",1,"labels.report.hist.productMst.poUpdFlag"));
		slipColList.add(new ReportColumnInfoDto("avgShipCount",1,"labels.report.hist.productMst.avgShipCount"));
		slipColList.add(new ReportColumnInfoDto("maxStockNum",1,"labels.report.hist.productMst.maxStockNum"));
		slipColList.add(new ReportColumnInfoDto("stockUpdFlag",1,"labels.report.hist.productMst.stockUpdFlag"));
		slipColList.add(new ReportColumnInfoDto("termShipNum",1,"labels.report.hist.productMst.termShipNum"));
		slipColList.add(new ReportColumnInfoDto("maxPoNum",1,"labels.report.hist.productMst.maxPoNum"));
		slipColList.add(new ReportColumnInfoDto("maxPoUpdFlag",1,"labels.report.hist.productMst.maxPoUpdFlag"));
		slipColList.add(new ReportColumnInfoDto("fractCategory",0,"labels.report.hist.productMst.fractCategory"));
		slipColList.add(new ReportColumnInfoDto("taxCategory",0,"labels.report.hist.productMst.taxCategory"));
		slipColList.add(new ReportColumnInfoDto("stockCtlCategory",0,"labels.report.hist.productMst.stockCtlCategory"));
		slipColList.add(new ReportColumnInfoDto("stockAssesCategory",0,"labels.report.hist.productMst.stockAssesCategory"));
		slipColList.add(new ReportColumnInfoDto("productCategory",0,"labels.report.hist.productMst.productCategory"));
		slipColList.add(new ReportColumnInfoDto("product1",0,"labels.report.hist.productMst.product1"));
		slipColList.add(new ReportColumnInfoDto("product2",0,"labels.report.hist.productMst.product2"));
		slipColList.add(new ReportColumnInfoDto("product3",0,"labels.report.hist.productMst.product3"));
		slipColList.add(new ReportColumnInfoDto("roMaxNum",1,"labels.report.hist.productMst.roMaxNum"));
		slipColList.add(new ReportColumnInfoDto("productRank",0,"labels.report.hist.productMst.productRank"));
		slipColList.add(new ReportColumnInfoDto("setTypeCategory",0,"labels.report.hist.productMst.setTypeCategory"));
		slipColList.add(new ReportColumnInfoDto("productStatusCategory",0,"labels.report.hist.productMst.productStatusCategory"));
		slipColList.add(new ReportColumnInfoDto("productStockCategory",0,"labels.report.hist.productMst.productStockCategory"));
		slipColList.add(new ReportColumnInfoDto("productPurvayCategory",0,"labels.report.hist.productMst.productPurvayCategory"));
		slipColList.add(new ReportColumnInfoDto("productStandardCategory",0,"labels.report.hist.productMst.productStandardCategory"));
		slipColList.add(new ReportColumnInfoDto("coreNum",0,"labels.report.hist.productMst.coreNum"));
		slipColList.add(new ReportColumnInfoDto("num1",1,"labels.report.hist.productMst.num1"));
		slipColList.add(new ReportColumnInfoDto("num2",1,"labels.report.hist.productMst.num2"));
		slipColList.add(new ReportColumnInfoDto("num3",1,"labels.report.hist.productMst.num3"));
		slipColList.add(new ReportColumnInfoDto("num4",1,"labels.report.hist.productMst.num4"));
		slipColList.add(new ReportColumnInfoDto("num5",1,"labels.report.hist.productMst.num5"));
		slipColList.add(new ReportColumnInfoDto("dec1",2,"labels.report.hist.productMst.dec1"));
		slipColList.add(new ReportColumnInfoDto("dec2",2,"labels.report.hist.productMst.dec2"));
		slipColList.add(new ReportColumnInfoDto("dec3",2,"labels.report.hist.productMst.dec3"));
		slipColList.add(new ReportColumnInfoDto("dec4",2,"labels.report.hist.productMst.dec4"));
		slipColList.add(new ReportColumnInfoDto("dec5",2,"labels.report.hist.productMst.dec5"));
		slipColList.add(new ReportColumnInfoDto("discardDate",10,"labels.report.hist.productMst.discardDate"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.productMst.remarks"));
		slipColList.add(new ReportColumnInfoDto("eadRemarks",0,"labels.report.hist.productMst.eadRemarks"));
		slipColList.add(new ReportColumnInfoDto("commentData",0,"labels.report.hist.productMst.commentData"));
		slipColList.add(new ReportColumnInfoDto("lastRoDate",10,"labels.report.hist.productMst.lastRoDate"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));
		slipColList.add(new ReportColumnInfoDto("discountId",0,"labels.report.hist.discountMst.discountName"));

		// 明細部カラム定義（明細なし）
		detailColList = null;
	}

	/**
	 * 仕入先マスタ履歴出力の設定をします.
	 */
	private void setup4Supplier() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME11;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("supplierCode",0,"labels.report.hist.supplierMst.supplierCode"));
		slipColList.add(new ReportColumnInfoDto("supplierName",0,"labels.report.hist.supplierMst.supplierName"));
		slipColList.add(new ReportColumnInfoDto("supplierKana",0,"labels.report.hist.supplierMst.supplierKana"));
		slipColList.add(new ReportColumnInfoDto("supplierAbbr",0,"labels.report.hist.supplierMst.supplierAbbr"));
		slipColList.add(new ReportColumnInfoDto("supplierZipCode",0,"labels.report.hist.supplierMst.supplierZipCode"));
		slipColList.add(new ReportColumnInfoDto("supplierAddress1",0,"labels.report.hist.supplierMst.supplierAddress1"));
		slipColList.add(new ReportColumnInfoDto("supplierAddress2",0,"labels.report.hist.supplierMst.supplierAddress2"));
		slipColList.add(new ReportColumnInfoDto("supplierDeptName",0,"labels.report.hist.supplierMst.supplierDeptName"));
		slipColList.add(new ReportColumnInfoDto("supplierPcName",0,"labels.report.hist.supplierMst.supplierPcName"));
		slipColList.add(new ReportColumnInfoDto("supplierPcKana",0,"labels.report.hist.supplierMst.supplierPcKana"));
		slipColList.add(new ReportColumnInfoDto("supplierPcPreCategory",0,"labels.report.hist.supplierMst.supplierPcPreCategory"));
		slipColList.add(new ReportColumnInfoDto("supplierPcPost",0,"labels.report.hist.supplierMst.supplierPcPost"));
		slipColList.add(new ReportColumnInfoDto("supplierTel",0,"labels.report.hist.supplierMst.supplierTel"));
		slipColList.add(new ReportColumnInfoDto("supplierFax",0,"labels.report.hist.supplierMst.supplierFax"));
		slipColList.add(new ReportColumnInfoDto("supplierEmail",0,"labels.report.hist.supplierMst.supplierEmail"));
		slipColList.add(new ReportColumnInfoDto("supplierUrl",0,"labels.report.hist.supplierMst.supplierUrl"));
		slipColList.add(new ReportColumnInfoDto("supplierCmCategory",0,"labels.report.hist.supplierMst.supplierCmCategory"));
		slipColList.add(new ReportColumnInfoDto("taxShiftCategory",0,"labels.report.hist.supplierMst.taxShiftCategory"));
		slipColList.add(new ReportColumnInfoDto("paymentTypeCategory",0,"labels.report.hist.supplierMst.paymentTypeCategory"));
		slipColList.add(new ReportColumnInfoDto("paymentCycleCategory",0,"labels.report.hist.supplierMst.paymentCycleCategory"));
		slipColList.add(new ReportColumnInfoDto("lastCutoffDate",11,"labels.report.hist.supplierMst.lastCutoffDate"));
		slipColList.add(new ReportColumnInfoDto("paymentDate",1,"labels.report.hist.supplierMst.paymentDate"));
		slipColList.add(new ReportColumnInfoDto("taxFractCategory",0,"labels.report.hist.supplierMst.taxFractCategory"));
		slipColList.add(new ReportColumnInfoDto("priceFractCategory",0,"labels.report.hist.supplierMst.priceFractCategory"));
		slipColList.add(new ReportColumnInfoDto("poSlipComeoutCategory",0,"labels.report.hist.supplierMst.poSlipComeoutCategory"));
		slipColList.add(new ReportColumnInfoDto("serviceChargeCategory",0,"labels.report.hist.supplierMst.serviceChargeCategory"));
		slipColList.add(new ReportColumnInfoDto("transferTypeCategory",0,"labels.report.hist.supplierMst.transferTypeCategory"));
		slipColList.add(new ReportColumnInfoDto("nationalCategory",0,"labels.report.hist.supplierMst.nationalCategory"));
		slipColList.add(new ReportColumnInfoDto("rateId",0,"labels.report.hist.supplierMst.rateId"));
		slipColList.add(new ReportColumnInfoDto("remarks",0,"labels.report.hist.supplierMst.remarks"));
		slipColList.add(new ReportColumnInfoDto("commentData",0,"labels.report.hist.supplierMst.commentData"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義（明細なし）
		detailColList = null;
	}

	/**
	 * 社員マスタ履歴出力の設定をします.
	 */
	private void setup4User() {
		// 添付ファイル名
		attachFileName = AttachFileName.FILENAME12;

		// 伝票部カラム定義
		slipColList = new ArrayList<ReportColumnInfoDto>();
		slipColList.add(new ReportColumnInfoDto("histId",0,"labels.report.hist.common.histId"));
		slipColList.add(new ReportColumnInfoDto("actionType",0,"labels.report.hist.common.actionType"));
		slipColList.add(new ReportColumnInfoDto("actionFunc",0,"labels.report.hist.common.actionFunc"));
		slipColList.add(new ReportColumnInfoDto("recDatetm",11,"labels.report.hist.common.recDatetm"));
		slipColList.add(new ReportColumnInfoDto("recUser",0,"labels.report.hist.common.recUser"));
		slipColList.add(new ReportColumnInfoDto("userId",0,"labels.report.hist.userMst.userId"));
		slipColList.add(new ReportColumnInfoDto("nameKnj",0,"labels.report.hist.userMst.nameKnj"));
		slipColList.add(new ReportColumnInfoDto("nameKana",0,"labels.report.hist.userMst.nameKana"));
		slipColList.add(new ReportColumnInfoDto("deptId",0,"labels.report.hist.userMst.deptId"));
		slipColList.add(new ReportColumnInfoDto("email",0,"labels.report.hist.userMst.email"));
		slipColList.add(new ReportColumnInfoDto("password",0,"labels.report.hist.userMst.password"));
		slipColList.add(new ReportColumnInfoDto("expireDate",10,"labels.report.hist.userMst.expireDate"));
		slipColList.add(new ReportColumnInfoDto("creFunc",0,"labels.report.hist.common.creFunc"));
		slipColList.add(new ReportColumnInfoDto("creDatetm",11,"labels.report.hist.common.creDatetm"));
		slipColList.add(new ReportColumnInfoDto("creUser",0,"labels.report.hist.common.creUser"));
		slipColList.add(new ReportColumnInfoDto("updFunc",0,"labels.report.hist.common.updFunc"));
		slipColList.add(new ReportColumnInfoDto("updDatetm",11,"labels.report.hist.common.updDatetm"));
		slipColList.add(new ReportColumnInfoDto("updUser",0,"labels.report.hist.common.updUser"));

		// 明細部カラム定義（明細なし）
		detailColList = null;
	}
}
