/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.payment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.payment.SearchPaymentForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.payment.SearchPaymentService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

/**
 * 支払検索画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 * @param List<Object> 検索結果リスト
 * @param BeanMap 検索条件マップ

 */
public class SearchPaymentResultAjaxAction extends
		AbstractSearchResultAjaxAction<List<Object>, BeanMap> {

	@ActionForm
	@Resource
	private SearchPaymentForm searchPaymentForm;

	@Resource
	private SearchPaymentService searchPaymentService;

	/**
	 * 各画面の権限を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		// 各画面の参照権限
		this.searchPaymentForm.isInputPOrderValid = super.userDto
				.isMenuValid(Constants.MENU_ID.INPUT_PORDER);
		this.searchPaymentForm.isInputPurchaseValid = super.userDto
				.isMenuValid(Constants.MENU_ID.INPUT_PURCHASE);
	}

	/**
	 * 検索条件を受け取って検索結果件数を返します.
	 * @param params 検索条件
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return this.searchPaymentService.getSearchResultCount(params);
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
		params.put(SearchPaymentService.Param.OFFSET_ROW, offset);
		return searchPaymentService.getSearchResult(params);
	}

	/**
	 * 検索結果の変換を行います.
	 * @param entityList 変換前検索結果リスト
	 * @return 変換された検索結果のリスト
	 * @throws Exception
	 */
	@Override
	protected List<List<Object>> exchange(List<BeanMap> entityList)
			throws Exception {
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		super.detailDispItemService.createResult(entityList, resultList, this
				.getSearchMenuID(), this.searchPaymentForm.searchTarget);
		return resultList;
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchPaymentForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchPaymentForm;
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
	 * @return 支払検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_PAYMENT;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 支払入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_PAYMENT;
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

	/**
	 * 検索で使用するDTOを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected Class<List<Object>> getDtoClass() {
		return null;
	}
}
