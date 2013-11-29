/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.sales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.sales.OutputInvoiceSearchResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.sales.OutputInvoiceForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.sales.SearchOutputInvoiceService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

/**
 * 送り状発行画面の検索を実行するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchOutputInvoiceAjaxAction extends
		AbstractSearchResultAjaxAction<OutputInvoiceSearchResultDto, BeanMap> {

	@ActionForm
	@Resource
	private OutputInvoiceForm outputInvoiceForm;

	@Resource
	private SearchOutputInvoiceService searchOutputInvoiceService;

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
		params.put(SearchOutputInvoiceService.Param.ROW_COUNT, null);
		return this.searchOutputInvoiceService.findSlipByCondition(params);
	}

	/**
	 * 検索結果の変換を行います.
	 * @param entityList 変換前検索結果リスト(Entity)
	 * @return 変換された検索結果のリスト(DTO)
	 * @throws Exception
	 */
	@Override
	protected List<OutputInvoiceSearchResultDto> exchange(
			List<BeanMap> entityList) throws Exception {
		return this.searchOutputInvoiceService.convertToDto(entityList);
	}

	/**
	 * 検索結果のページングを行います.
	 * @throws Exception
	 */
	@Override
	protected void doAfterSearch() throws Exception {
		List<OutputInvoiceSearchResultDto> allSearchResultList = this.outputInvoiceForm.searchResultList;
		this.outputInvoiceForm.searchResultCount = allSearchResultList.size();

		int pageNo = this.outputInvoiceForm.pageNo;
		int rowCount = this.outputInvoiceForm.rowCount;
		int offset = rowCount * (pageNo - 1);

		int maxRow = offset + rowCount;
		if (maxRow < 0 || maxRow > allSearchResultList.size()) {
			maxRow = allSearchResultList.size();
		}

		this.outputInvoiceForm.allSearchResultList = allSearchResultList;
		this.outputInvoiceForm.searchResultList = new ArrayList<OutputInvoiceSearchResultDto>(
				this.outputInvoiceForm.allSearchResultList.subList(offset,
						maxRow));
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link OutputInvoiceForm}
	 */
	@Override
	protected AbstractSearchForm<OutputInvoiceSearchResultDto> getActionForm() {
		return this.outputInvoiceForm;
	}

	/**
	 * 検索で使用するDTOを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected Class<OutputInvoiceSearchResultDto> getDtoClass() {
		return OutputInvoiceSearchResultDto.class;
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
	 * @return 送り状発行画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.OUTPUT_SALES_INVOICE;
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
