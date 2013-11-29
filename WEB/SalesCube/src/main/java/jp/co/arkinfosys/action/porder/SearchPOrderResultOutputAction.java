/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.porder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchResultAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.porder.POrderSlipLineJoinDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.porder.SearchPOrderForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.porder.SearchPOrderService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 発注検索画面の検索結果をEXCELとしてクライアントに送信するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchPOrderResultOutputAction extends
		AbstractSearchResultAction<List<Object>, POrderSlipLineJoinDto> {

	@ActionForm
	@Resource
	protected SearchPOrderForm searchPOrderForm;

	@Resource
	private SearchPOrderService searchPOrderService;

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
	 * 検索条件を受け取って検索結果件数を返します.<br>
	 * 未使用です.
	 * @param params 検索パラメータ
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		// カウントしない
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
	protected List<POrderSlipLineJoinDto> execSearch(BeanMap params,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		params.put(SearchPOrderService.Param.ROW_COUNT, null);
		return this.searchPOrderService.createPOrderSlipJoinDtoList(params);
	}

	/**
	 * 検索結果の変換を行います.
	 * @param entityList 変換前検索結果リスト
	 * @return 変換された検索結果のリスト
	 * @throws Exception
	 */
	@Override
	protected List<List<Object>> exchange(List<POrderSlipLineJoinDto> entityList)
			throws Exception {
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		this.searchPOrderService.createSearchPOrderResult(entityList,
				resultList, this.searchPOrderForm.searchTarget);
		return resultList;
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchPOrderForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchPOrderForm;
	}

	/**
	 * Excel表示用のjspのURIを返します.
	 * @return Excel表示用のjspのURI
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.EXCEL;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 発注検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_PORDER;
	}

	/**
	 * 検索で使用するサービスを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected MasterSearch<POrderSlipLineJoinDto> getService() {
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
