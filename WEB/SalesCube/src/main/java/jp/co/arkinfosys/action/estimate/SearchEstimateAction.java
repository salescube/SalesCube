/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.estimate;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.estimate.SearchEstimateForm;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 *
 * 見積検索画面の表示アクションクラスです.
 *
 * @author Ark Information Systems
 */
public class SearchEstimateAction extends AbstractSearchAction<List<Object>> {

	@ActionForm
	@Resource
	public SearchEstimateForm searchEstimateForm;

	/**
	 * 検索結果列サービスクラス
	 */
	@Resource
	private DetailDispItemService detailDispItemService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList;

	/**
	 * 見積検索画面の初期表示情報を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.searchEstimateForm.searchTarget = Constants.SEARCH_TARGET.VALUE_SLIP;

		// 検索結果表示項目の取得
		this.columnInfoList = detailDispItemService.createResult(null, null,
				this.getSearchMenuID(), Constants.SEARCH_TARGET.VALUE_SLIP);
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
	 * 入力画面のメニューIDを返します.
	 * @return 見積入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_ESTIMATE;
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
	 * 画面表示に使用しているプルダウン等の情報を作成します.<br>
	 * 未使用です.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
	}

}
