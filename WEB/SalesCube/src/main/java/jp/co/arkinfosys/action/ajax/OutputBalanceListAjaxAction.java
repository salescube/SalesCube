/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.Constants.OUTPUT_BALANCE_TARGET;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.dto.report.OutputBalanceListFormDto;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.report.OutputBalanceListForm;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.report.OutputBalanceListService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 残高一覧表を出力するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputBalanceListAjaxAction extends CommonAjaxResources {
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
		public static final String PORDER = "PORDER_BALANCE_LIST.xls";
		public static final String RORDER = "RORDER_BALANCE_LIST.xls";
	}

	@ActionForm
	@Resource
	private OutputBalanceListForm outputBalanceListForm;

	@Resource
	public OutputBalanceListFormDto outputBalanceListFormDto;

	@Resource
	public OutputBalanceListService outputBalanceListService;

	@Resource
	private DetailDispItemService detailDispItemService;

	@Resource
	private SupplierService supplierService;

	/**
	 * ヘッダ部出力内容リスト
	 */
	public List<LabelValueBean> headerList = new ArrayList<LabelValueBean>();

	/**
	 * 日付の形式指定
	 */
	private static SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);

	// 出力日
	public String exceloutputdate;

	// 仕入先外貨記号
	public String sign;

	/**
	 * 検索結果リスト
	 */
	public List<List<Object>> searchResultList = new ArrayList<List<Object>>();

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList = new ArrayList<DetailDispItemDto>();

	/**
	 * 出力準備を行います.
	 *
	 * @return 遷移先URI（エラー時）
	 */
	@Execute(validator = true, input="/ajax/errorResponse.jsp")
	public String prepare() {
		ActionMessages errors = outputBalanceListForm.validate();
		if (!errors.isEmpty()) {
			// 検索条件エラー
			ActionMessagesUtil.addErrors(super.httpRequest, errors);
			this.httpResponse.setStatus(450);
			return "/ajax/errorResponse.jsp";
		}
		try {
			// パラメータを作成する
			BeanMap params = Beans.createAndCopy(BeanMap.class, outputBalanceListForm).execute();

			// 出力件数を取得する
			int count  = outputBalanceListService.getOutputResultCount(params);
			if (count<1) {
				// 買掛残高の場合、仕入先の最終締処理日を取得
				if (Constants.OUTPUT_BALANCE_TARGET.VALUE_PORDER.equals(outputBalanceListForm.outputTarget)) {
					SupplierJoin supp = supplierService.findById(outputBalanceListForm.supplierCode);
					if (supp!=null && supp.lastCutoffDate!=null) {
						// 最終締処理日を数値に変換
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(supp.lastCutoffDate.getTime());
						int yy = cal.get(Calendar.YEAR);
						int mm = cal.get(Calendar.MONTH)+1;	// 0～11が返却されるので＋１
						int lastCutoffYM = yy*100+mm;

						// 出力対象年月を数値に変換
						int targetYM = this.outputBalanceListService
								.convertTargetYM(outputBalanceListForm.targetDate);

						// 出力対象がない
						if (lastCutoffYM>=targetYM) {
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.report.targetDataNotExists"));
							ActionMessagesUtil.addErrors(super.httpRequest, errors);
							this.httpResponse.setStatus(450);
							return "/ajax/errorResponse.jsp";
						}
					}
				}

				// 検索結果がない（締処理がされていない）
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.report.NotClosed"));
				ActionMessagesUtil.addErrors(super.httpRequest, errors);
				this.httpResponse.setStatus(450);
				return "/ajax/errorResponse.jsp";
			}

			// 保存用検索条件にコピー
			Beans.copy(outputBalanceListForm, outputBalanceListFormDto).execute();

		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
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
			Beans.copy(outputBalanceListFormDto, outputBalanceListForm).execute();

			// パラメータを作成する
			BeanMap params = Beans.createAndCopy(BeanMap.class, outputBalanceListForm).execute();

			// 検索を行う
			List<BeanMap> resultMapList = outputBalanceListService.getOutputResult(params);

			// 出力日の設定
			exceloutputdate = MessageResourcesUtil.getMessage("labels.outputDate") +
								DF_YMD.format(new Date());

			// 出力タイプごとの処理
			String attachFileName;
			if (OUTPUT_BALANCE_TARGET.VALUE_PORDER.equals(outputBalanceListForm.outputTarget)) {
				// 添付ファイル名
				attachFileName = AttachFileName.PORDER;

				// ヘッダ部の値設定
				headerList.add(
						new LabelValueBean(MessageResourcesUtil.getMessage("labels.report.porder.title"),
						""));
				headerList.add(
						new LabelValueBean(MessageResourcesUtil.getMessage("labels.report.targetDate"),
						outputBalanceListForm.targetDate));
				headerList.add(
						new LabelValueBean(MessageResourcesUtil.getMessage("labels.report.porder.supplierCode"),
						outputBalanceListForm.supplierCode));
				headerList.add(
						new LabelValueBean(MessageResourcesUtil.getMessage("labels.report.porder.supplierName"),
						(String)resultMapList.get(0).get("supplierName")));
			}
			else {
				// 添付ファイル名
				attachFileName = AttachFileName.RORDER;

				// ヘッダ部の値設定
				headerList.add(
						new LabelValueBean(MessageResourcesUtil.getMessage("labels.report.rorder.title"),
						""));
				headerList.add(
						new LabelValueBean(MessageResourcesUtil.getMessage("labels.report.targetDate"),
						outputBalanceListForm.targetDate));
				if (outputBalanceListForm.customerCodeFrom!=null || outputBalanceListForm.customerCodeTo!=null) {
					StringBuffer customerCodeRange = new StringBuffer();
					if (outputBalanceListForm.customerCodeFrom!=null) {
						customerCodeRange.append(outputBalanceListForm.customerCodeFrom);
					}
					customerCodeRange.append(MessageResourcesUtil.getMessage("labels.betweenSign"));
					if (outputBalanceListForm.customerCodeTo!=null) {
						customerCodeRange.append(outputBalanceListForm.customerCodeTo);
					}
					headerList.add(
							new LabelValueBean(MessageResourcesUtil.getMessage("labels.report.rorder.customerCodeRange"),
							customerCodeRange.toString()));
				}
			}

			// 出力調整
			columnInfoList = detailDispItemService.createResult(
					resultMapList, searchResultList, Constants.MENU_ID.OUTPUT_BALANCE_LIST,outputBalanceListForm.outputTarget);

			// 添付ファイル名設定
			String attach = String.format(ATTACHMENT_FORMAT, URLEncoder.encode(attachFileName,ATTACHMENT_ENCODE));
			httpResponse.setHeader(CONTENT_DISPOSITION, attach);

			// 仕入先の通貨記号を取得
			String supplierCode = outputBalanceListForm.supplierCode;
			if(StringUtil.hasLength(supplierCode)){
				SupplierJoin supJoin = supplierService.findSupplierRateBySupplierCode(supplierCode);
				this.sign = supJoin.cUnitSign;
			}
		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}

		return Mapping.EXCEL;
	}
}
