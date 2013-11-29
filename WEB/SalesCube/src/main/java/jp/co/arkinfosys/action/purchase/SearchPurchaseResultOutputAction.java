/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.purchase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchResultAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.purchase.SearchPurchaseForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.purchase.SearchPurchaseService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 *
 * 仕入検索画面の検索結果をEXCELとしてクライアントに送信するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchPurchaseResultOutputAction extends
		AbstractSearchResultAction<List<Object>, BeanMap> {

	@ActionForm
	@Resource
	private SearchPurchaseForm searchPurchaseForm;

	@Resource
	private SearchPurchaseService searchPurchaseService;

	/**
	 * Excelを出力します.
	 *
	 * @return エラー発生時：エラー表示用のjspのURI<br>
	 *         正常終了時：Excel出力用のjspのURI
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.EXCEL)
	public String excel() throws Exception {
		this.outputExcel = true;
		return super.doSearch();
	}

	/**
	 * 検索条件を受け取って検索結果件数を返します.
	 * @param params 検索パラメータ
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
		params.put(SearchPurchaseService.Param.ROW_COUNT, null);
		return this.searchPurchaseService.getSearchResult(params);
	}

	/**
	 * 検索結果の変換を行います.
	 * @param entityList 変換前検索結果リスト(Entity)
	 * @return 変換された検索結果のリスト(DTO)
	 * @throws Exception
	 */
	@Override
	protected List<List<Object>> exchange(List<BeanMap> entityList)
			throws Exception {
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		super.detailDispItemService.createResult(entityList, resultList, this
				.getSearchMenuID(), this.searchPurchaseForm.searchTarget);
		return resultList;
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchPurchaseForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchPurchaseForm;
	}

	/**
	 * Excel出力用のjspのURIを返します.
	 * @return Excel出力用のjspのURI
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.EXCEL;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 仕入検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_PURCHASE;
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
