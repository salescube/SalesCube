/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.sales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.sales.OutputSalesSearchResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.sales.OutputSalesReportForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.sales.SearchOutputSalesReportService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

/**
 * 売上帳票発行画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchOutputSalesReportAjaxAction extends
		AbstractSearchResultAjaxAction<OutputSalesSearchResultDto, BeanMap> {

	@Resource
	private SearchOutputSalesReportService searchOutputSalesReportService;

	@ActionForm
	@Resource
	private OutputSalesReportForm outputSalesReportForm;

	/**
	 * 検索条件を受け取って検索結果件数を返します.
	 * @param params 検索条件
	 * @return 検索結果件数(固定値0を返します)
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return 0;
	}

	/**
	 * 検索条件を受け取って検索結果のリストを返します.
	 * @param params 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 取得件数(LIMIT)
	 * @param offset 取得開始位置(OFFSET)
	 * @return 検索結果リスト
	 * @throws ServiceException
	 */
	@Override
	protected List<BeanMap> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		params.put(SearchOutputSalesReportService.Param.ROW_COUNT, null);
		return searchOutputSalesReportService.findSlipByCondition(params);
	}

	/**
	 * 検索結果の変換を行います.
	 * @param entityList 変換前検索結果リスト(Entity)
	 * @return 変換された検索結果のリスト(DTO)
	 * @throws Exception
	 */
	@Override
	protected List<OutputSalesSearchResultDto> exchange(List<BeanMap> entityList)
			throws Exception {
		return this.searchOutputSalesReportService.convertToDto(entityList);
	}

	/**
	 * 検索結果のページングを行います.
	 * @throws Exception
	 */
	@Override
	protected void doAfterSearch() throws Exception {
		List<OutputSalesSearchResultDto> allSearchResultList = this.outputSalesReportForm.searchResultList;
		this.outputSalesReportForm.searchResultCount = allSearchResultList
				.size();

		int pageNo = this.outputSalesReportForm.pageNo;
		int rowCount = this.outputSalesReportForm.rowCount;
		int offset = rowCount * (pageNo - 1);

		int maxRow = offset + rowCount;
		if (maxRow < 0 || maxRow > allSearchResultList.size()) {
			maxRow = allSearchResultList.size();
		}

		this.outputSalesReportForm.allSearchResultList = allSearchResultList;
		this.outputSalesReportForm.searchResultList = new ArrayList<OutputSalesSearchResultDto>(
				this.outputSalesReportForm.allSearchResultList.subList(offset,
						maxRow));
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link OutputSalesReportForm}
	 */
	@Override
	protected AbstractSearchForm<OutputSalesSearchResultDto> getActionForm() {
		return this.outputSalesReportForm;
	}

	/**
	 * 検索で使用するDTOクラスを返します.<br>
	 * @return {@link OutputSalesSearchResultDto OutputSalesSearchResultDto}クラス
	 */
	@Override
	protected Class<OutputSalesSearchResultDto> getDtoClass() {
		return OutputSalesSearchResultDto.class;
	}

	/**
	 * 検索結果jspのURIを返します.
	 * @return 検索結果jspのURI
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 売上帳票発行画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.OUTPUT_SALES_REPORT;
	}

	/**
	 * 検索で使用するサービスを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected MasterSearch<BeanMap> getService() {
		return null;
	}
}
