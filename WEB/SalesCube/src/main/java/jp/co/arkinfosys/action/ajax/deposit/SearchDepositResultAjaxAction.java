/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.deposit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.deposit.SearchDepositForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.deposit.SearchDepositService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

/**
 * 入金検索画面の検索実行アクションクラスです．
 *
 * @author Ark Information Systems
 *
 */
public class SearchDepositResultAjaxAction extends
		AbstractSearchResultAjaxAction<List<Object>, BeanMap> {

	@ActionForm
	@Resource
	private SearchDepositForm searchDepositForm;

	@Resource
	private SearchDepositService searchDepositService;

	/**
	 * 検索対象を設定します.
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#doBeforeSearch()
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchDepositForm.searchTarget = Constants.SEARCH_TARGET.VALUE_SLIP;
	}

	/**
	 * 入金伝票の検索結果件数を取得し、回収合計金額をフォームに設定します.
	 * @param params 検索条件
	 * @return 検索結果件数
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		// 検索結果件数を取得する
		BeanMap result = searchDepositService.getSearchResultCount(params);

		// 回収合計金額を設定する
		Number count = (Number)result.get("cnt");
		if (count.intValue() > 0) {
			this.searchDepositForm.depositTotal = ((Number) result.get("total"))
					.longValue();
		}

		return count.intValue();
	}
	/**
	 * 検索を実行し、検索結果を取得します.
	 * @param params 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount ページあたりの表示件数
	 * @param offset 表示ページ
	 * @return 検索結果リスト
	 */
	@Override
	protected List<BeanMap> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		params.put(SearchDepositService.Param.OFFSET_ROW, offset);
		return this.searchDepositService.getSearchResult(params);
	}

	/**
	 * 結果リストをENTITYからDTOに変換します.
	 * @param entityList 結果リスト（ENTITY)
	 * @return 検索結果リスト（DTO)
	 */
	@Override
	protected List<List<Object>> exchange(List<BeanMap> entityList)
			throws Exception {
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		super.detailDispItemService.createResult(entityList, resultList, this
				.getSearchMenuID(), this.searchDepositForm.searchTarget);
		return resultList;
	}

	/**
	 * 入金検索アクションフォームを取得します.
	 * @return 入金検索アクションフォーム
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchDepositForm;
	}


	/**
	 * 検索後の遷移URIを取得します.
	 * @return 遷移URI
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 *  入金検索画面のメニューIDを返します.
	 * @return 入金検索メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_DEPOSIT;
	}

	/**
	 *  入金入力画面のメニューIDを返します.
	 * @return 入金入力メニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_DEPOSIT;
	}

	/**
	 *  DTOのクラスを取得します（未実装）.
	 * @return null
	 */
	@Override
	protected Class<List<Object>> getDtoClass() {
		return null;
	}

	/**
	 *  入金検索サービスを取得します（未実装）.
	 * @return null
	 */
	@Override
	protected MasterSearch<BeanMap> getService() {
		return null;
	}
}
