/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.dto.stock.OutputStockListFormDto;
import jp.co.arkinfosys.dto.stock.ProductStockInfoDto;
import jp.co.arkinfosys.form.stock.OutputStockListForm;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.OutputStockListService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 在庫一覧表を出力するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputStockListAjaxAction extends CommonAjaxResources {

	/**
	 * 画面遷移用のマッピングクラス
	 */
	public static class Mapping {
		public static final String RESULT = "outputStockListResult.jsp";
		public static final String EXCEL = "excel.jsp";
	}

	@ActionForm
	@Resource
	private OutputStockListForm outputStockListForm;

	@Resource
	private OutputStockListService outputStockListService;

	@Resource
	public OutputStockListFormDto outputStockListFormDto;

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
	@Execute(validator = true, stopOnValidationError = false, validate = "validate", input = CommonAjaxResources.Mapping.ERROR_JSP)
	public String search() throws Exception {
		// 保存用検索条件にコピー
		Beans.copy(outputStockListForm, outputStockListFormDto).execute();

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
			// 検索条件の調整
			Beans.copy(outputStockListFormDto, outputStockListForm).execute();

			// 検索を行う
			List<ProductStockInfoDto> dtoList =
				outputStockListService.createOutputStockListDtoList(outputStockListFormDto);
			// 検索結果設定
			outputStockListForm.searchResultList = dtoList;

			// 出力日
			outputStockListForm.currentDate = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);
		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return Mapping.EXCEL;
	}
}
