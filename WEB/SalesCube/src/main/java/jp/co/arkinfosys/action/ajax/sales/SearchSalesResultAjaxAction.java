/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.sales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.sales.SearchSalesForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.ROrderService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.sales.SearchSalesService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 売上検索画面の検索実行アクションクラスです.
 *
 *
 * @author Ark Information Systems
 *
 */
public class SearchSalesResultAjaxAction extends
		AbstractSearchResultAjaxAction<List<Object>, BeanMap> {

	@ActionForm
	@Resource
	private SearchSalesForm searchSalesForm;

	@Resource
	private SearchSalesService searchSalesService;

	/**
	 * 初期表示処理を行います.
	 *
	 * @return 検索結果jspのURI
	 */
	@Execute(validator = false)
	public String index() {
		return Mapping.RESULT;
	}

	/**
	 * 受注入力画面の権限を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		// 受注入力画面の権限フラグを設定する
		this.searchSalesForm.isInputROrderValid = userDto
				.isMenuValid(Constants.MENU_ID.INPUT_RORDER);
	}

	/**
	 * 検索条件を受け取って検索結果件数を返します.
	 * @param params 検索条件
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return searchSalesForm.searchResultCount = searchSalesService
				.getSearchResultCount(params);
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
		params.put(ROrderService.Param.OFFSET_ROW, offset);
		return this.searchSalesService.getSearchResult(params);
	}

	/**
	 * 検索結果の変換を行います.
	 * @param entityList 変換前検索結果リスト(Entity)
	 * @return 変換された検索結果のリスト(DTO)
	 * @throws ServiceException
	 */
	@Override
	protected List<List<Object>> exchange(List<BeanMap> entityList)
			throws ServiceException {
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		super.detailDispItemService.createResult(entityList, resultList, this
				.getSearchMenuID(), this.searchSalesForm.searchTarget);
		return resultList;
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchSalesForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchSalesForm;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 売上入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_SALES;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 売上検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_SALES;
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
	 * 検索で使用するサービスを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected MasterSearch<BeanMap> getService() {
		// 未使用
		return null;
	}

	/**
	 * 検索で使用するDTOを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected Class<List<Object>> getDtoClass() {
		// 未使用
		return null;
	}
}
