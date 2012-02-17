/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.ajax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.dto.stock.OutputStockReportFormDto;
import jp.co.arkinfosys.dto.stock.ProductStockJoinDto;
import jp.co.arkinfosys.form.stock.OutputStockReportForm;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.OutputStockReportService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 在庫残高表を出力するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputStockReportAjaxAction extends CommonAjaxResources {

	/**
	 * 画面遷移用のマッピングクラス
	 */
	public static class Mapping {
		public static final String RESULT = "outputStockReportResult.jsp";
		public static final String EXCEL = "excel.jsp";
	}

	
	public String CONST_SALES = Constants.SRC_FUNC.SALES;
	public String CONST_PURCHASE = Constants.SRC_FUNC.PURCHASE;
	public String CONST_STOCK = Constants.SRC_FUNC.STOCK;
	public String CONST_STOCK_TRANSFER = Constants.SRC_FUNC.STOCK_TRANSFER;

	@ActionForm
	@Resource
	private OutputStockReportForm outputStockReportForm;

	@Resource
	private OutputStockReportService outputStockReportService;

	@Resource
	public OutputStockReportFormDto	outputStockReportFormDto;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList = new ArrayList<DetailDispItemDto>();

	/**
	 * 初期表示処理を行います.
	 *
	 * @return 遷移先URI
	 */
	@Execute(validator = false)
	public String index() {
		return Mapping.RESULT;
	}

	/**
	 * 検索条件を保存します.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = CommonAjaxResources.Mapping.ERROR_JSP)
	public String search() throws Exception {

		
		Boolean futureCheck = ValidateUtil.dateIsFuture( ValidateUtil.getLastDateOfMonthFromYmFormat(outputStockReportForm.targetYm) );
		if( futureCheck != null && futureCheck == true ) {
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.dateFuture", MessageResourcesUtil.getMessage("labels.targetYm")));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return CommonAjaxResources.Mapping.ERROR_JSP;
		}

		
		Beans.copy(outputStockReportForm, outputStockReportFormDto).execute();

		return Mapping.RESULT;
	}

	/**
	 * Excel出力処理を行います.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excel() throws Exception {
		try {
			
			Beans.copy(outputStockReportFormDto, outputStockReportForm).execute();

			
			List<ProductStockJoinDto> dtoList =
				outputStockReportService.createProductStockDto(outputStockReportForm.targetYm);
			
			outputStockReportForm.searchResultList = dtoList;
			
			outputStockReportForm.currentDate = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);
			
			BigDecimal sumStockPrice = BigDecimal.ZERO;
			for(ProductStockJoinDto dto : dtoList) {
				if(dto.stockPrice == null) {
					continue;
				}
				sumStockPrice = sumStockPrice.add(new BigDecimal(dto.stockPrice));
			}
			if(sumStockPrice != null) {
				sumStockPrice = sumStockPrice.setScale(0, BigDecimal.ROUND_DOWN);
			}
			outputStockReportForm.sumStockPrice = StringUtil.valueOf(sumStockPrice);
		} catch (ServiceException e) {
			super.errorLog(e);

			
			super.writeSystemErrorToResponse();
			return null;
		}
		return Mapping.EXCEL;
	}
}
