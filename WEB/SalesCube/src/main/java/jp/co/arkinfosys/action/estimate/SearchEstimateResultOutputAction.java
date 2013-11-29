/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.estimate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchResultAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.estimate.SearchEstimateForm;
import jp.co.arkinfosys.service.EstimateSheetService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 見積検索画面の検索結果をEXCELとしてクライアントに送信するアクションクラスです.
 *
 * @author Ark Information Systems
 */
public class SearchEstimateResultOutputAction extends
		AbstractSearchResultAction<List<Object>, BeanMap> {

	@ActionForm
	@Resource
	public SearchEstimateForm searchEstimateForm;

	@Resource
	private EstimateSheetService estimateSheetService;

	/**
	 * Excelファイルを出力します.
	 * @return 遷移URI
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.EXCEL)
	public String excel() throws Exception {
		this.outputExcel = true;
		return super.doSearch();
	}

	/**
	 * 検索対象を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchEstimateForm.searchTarget = Constants.SEARCH_TARGET.VALUE_SLIP;
	}

	/**
	 * 検索条件を受け取って検索結果件数を返します.
	 * @param params 検索パラメータ
	 * @return 検索結果件数(固定値0を返します)
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		// カウント不要
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
		params.put(EstimateSheetService.Param.ROW_COUNT, null);
		return estimateSheetService.findEstimateSheetByCondition(params);
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
				.getSearchMenuID(), Constants.SEARCH_TARGET.VALUE_SLIP);
		return resultList;
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchEstimateForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchEstimateForm;
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
	 * @return 見積検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_ESTIMATE;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 見積入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_ESTIMATE;
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
