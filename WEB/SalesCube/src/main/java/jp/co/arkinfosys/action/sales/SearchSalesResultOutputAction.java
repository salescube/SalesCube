/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.sales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchResultAction;
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
 * 売上検索画面の検索結果をEXCELとしてクライアントに送信するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchSalesResultOutputAction extends
		AbstractSearchResultAction<List<Object>, BeanMap> {

	@ActionForm
	@Resource
	public SearchSalesForm searchSalesForm;

	@Resource
	private SearchSalesService searchSalesService;


	/**
	 * Excelファイルを出力します.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.EXCEL)
	public String excel() throws Exception {
		this.outputExcel = true;
		return super.doSearch();
	}

	/**
	 * 検索結果件数を返します.<br>
	 * 使用しないため、必ず0を返します.
	 * @param params パラメータを設定したマップ
	 * @return 0固定
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		// カウント不要
		return 0;
	}

	/**
	 * 検索条件に応じて売上伝票を検索します.<br>
	 * @param params 検索パラメータを設定したマップ
	 * @param sortColumn ソート対象カラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得する検索件数
	 * @param offset 取得開始位置
	 * @return 検索結果のリスト
	 * @throws ServiceException
	 */
	@Override
	protected List<BeanMap> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		params.put(ROrderService.Param.ROW_COUNT, null);
		return this.searchSalesService.getSearchResult(params);
	}

	/**
	 * ENTITYのリストをDTOのリストに変換します.<br>
	 * @param entityList　BeanMapのリスト
	 * @return オブジェクトのリスト
	 * @throws Exception
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
	 * 売上伝票検索アクションで使用するアクションフォームを返します.<br>
	 * @return 売上伝票検索用アクションフォーム
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchSalesForm;
	}

	/**
	 * 売上伝票検索後の遷移URIを返します.<br>
	 * @return 売上伝票検索後の遷移URI
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.EXCEL;
	}

	/**
	 * 売上伝票検索画面のメニューIDを返します.<br>
	 * @return 売上伝票検索画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_SALES;
	}

	/**
	 * 売上伝票検索画面では検索サービスを返す必要が無いのでnullを返します.<br>
	 * @return null固定
	 */
	@Override
	protected MasterSearch<BeanMap> getService() {
		// 未使用
		return null;
	}

	/**
	 * 売上伝票検索アクションではDTOクラスを使用しないのでnullを返します.<br>
	 * @return null固定
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Class getDtoClass() {
		// 未使用
		return null;
	}
}
