/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.stock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.stock.EntrustEadSlipLineJoinDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.stock.SearchEntrustStockForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.SearchEntrustStockService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

/**
 * 委託入出庫検索画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchEntrustStockResultAjaxAction extends
		AbstractSearchResultAjaxAction<List<Object>, EntrustEadSlipLineJoinDto> {

	@ActionForm
	@Resource
	private SearchEntrustStockForm searchEntrustStockForm;

	@Resource
	private SearchEntrustStockService searchEntrustStockService;

	/**
	 * 委託入出庫入力権限
	 */
	public boolean entrustStockMenuValid = false;

	/**
	 * 発注入力画面の権限を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchEntrustStockForm.isInputPOrderValid = super.userDto
				.isMenuValid(Constants.MENU_ID.INPUT_PORDER);
	}

	/**
	 * 検索条件を受け取って検索結果件数を返します.
	 * @param params 検索条件
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return searchEntrustStockService.getSearchStockResultCount(params);
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
	protected List<EntrustEadSlipLineJoinDto> execSearch(BeanMap params,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		params.put(SearchEntrustStockService.Param.OFFSET_ROW, offset);
		return searchEntrustStockService.createEadSlipJoinDtoList(params);
	}

	/**
	 * 検索結果の変換を行います.
	 * @param entityList 変換前検索結果リスト(Entity)
	 * @return 変換された検索結果のリスト(DTO)
	 * @throws Exception
	 */
	@Override
	protected List<List<Object>> exchange(
			List<EntrustEadSlipLineJoinDto> entityList) throws Exception {
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		this.searchEntrustStockService.createSearchStockResult(entityList,
				resultList, this.searchEntrustStockForm.searchTarget);
		return resultList;
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchEntrustStockForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchEntrustStockForm;
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
	 * @return 委託入出庫検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_ENTRUST_STOCK;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 委託入出庫検索画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_ENTRUST_STOCK;
	}

	/**
	 * 検索で使用するサービスを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected MasterSearch<EntrustEadSlipLineJoinDto> getService() {
		// 未使用メソッド
		return null;
	}

	/**
	 * 検索で使用するDTOを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected Class<List<Object>> getDtoClass() {
		// 未使用メソッド
		return null;
	}
}
