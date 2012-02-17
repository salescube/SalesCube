/*
 *  Copyright 2009-2010 Ark Information Systems.
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

	
	public String exceloutputdate;

	
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
			
			ActionMessagesUtil.addErrors(super.httpRequest, errors);
			this.httpResponse.setStatus(450);
			return "/ajax/errorResponse.jsp";
		}
		try {
			
			BeanMap params = Beans.createAndCopy(BeanMap.class, outputBalanceListForm).execute();

			
			int count  = outputBalanceListService.getOutputResultCount(params);
			if (count<1) {
				
				if (Constants.OUTPUT_BALANCE_TARGET.VALUE_PORDER.equals(outputBalanceListForm.outputTarget)) {
					SupplierJoin supp = supplierService.findById(outputBalanceListForm.supplierCode);
					if (supp!=null && supp.lastCutoffDate!=null) {
						
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(supp.lastCutoffDate.getTime());
						int yy = cal.get(Calendar.YEAR);
						int mm = cal.get(Calendar.MONTH)+1;	
						int lastCutoffYM = yy*100+mm;

						
						int targetYM = this.outputBalanceListService
								.convertTargetYM(outputBalanceListForm.targetDate);

						
						if (lastCutoffYM>=targetYM) {
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.report.targetDataNotExists"));
							ActionMessagesUtil.addErrors(super.httpRequest, errors);
							this.httpResponse.setStatus(450);
							return "/ajax/errorResponse.jsp";
						}
					}
				}

				
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.report.NotClosed"));
				ActionMessagesUtil.addErrors(super.httpRequest, errors);
				this.httpResponse.setStatus(450);
				return "/ajax/errorResponse.jsp";
			}

			
			Beans.copy(outputBalanceListForm, outputBalanceListFormDto).execute();

		} catch (ServiceException e) {
			super.errorLog(e);

			
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
			
			Beans.copy(outputBalanceListFormDto, outputBalanceListForm).execute();

			
			BeanMap params = Beans.createAndCopy(BeanMap.class, outputBalanceListForm).execute();

			
			List<BeanMap> resultMapList = outputBalanceListService.getOutputResult(params);

			
			exceloutputdate = MessageResourcesUtil.getMessage("labels.outputDate") +
								DF_YMD.format(new Date());

			
			String attachFileName;
			if (OUTPUT_BALANCE_TARGET.VALUE_PORDER.equals(outputBalanceListForm.outputTarget)) {
				
				attachFileName = AttachFileName.PORDER;

				
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
				
				attachFileName = AttachFileName.RORDER;

				
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

			
			columnInfoList = detailDispItemService.createResult(
					resultMapList, searchResultList, Constants.MENU_ID.OUTPUT_BALANCE_LIST,outputBalanceListForm.outputTarget);

			
			String attach = String.format(ATTACHMENT_FORMAT, URLEncoder.encode(attachFileName,ATTACHMENT_ENCODE));
			httpResponse.setHeader(CONTENT_DISPOSITION, attach);

			
			String supplierCode = outputBalanceListForm.supplierCode;
			if(StringUtil.hasLength(supplierCode)){
				SupplierJoin supJoin = supplierService.findSupplierRateBySupplierCode(supplierCode);
				this.sign = supJoin.cUnitSign;
			}
		} catch (ServiceException e) {
			super.errorLog(e);

			
			super.writeSystemErrorToResponse();
			return null;
		}

		return Mapping.EXCEL;
	}
}
