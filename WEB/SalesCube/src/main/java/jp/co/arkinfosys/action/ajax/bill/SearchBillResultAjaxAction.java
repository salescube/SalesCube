/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.bill;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.bill.SearchBillForm;
import jp.co.arkinfosys.service.BillJoinService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

/**
 *
 * 請求検索画面の検索実行アクションクラスです。
 *
 * @author Ark Information Systems
 *
 */
public class SearchBillResultAjaxAction extends
		AbstractSearchResultAjaxAction<BeanMap, BeanMap> {

	@ActionForm
	@Resource
	public SearchBillForm searchBillForm;

	@Resource
	private BillJoinService billJoinService;

	/**
	 * 検索実行前に、検索条件の初期値を設定します.<br>
	 * 検索結果に指定する更新権限情報を保持します.<br>
	 * @throws Exception
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchBillForm.searchTarget = Constants.SEARCH_TARGET.VALUE_SLIP;
		this.searchBillForm.isInputDepositValid = super.userDto.isMenuUpdate(Constants.MENU_ID.INPUT_DEPOSIT);
	}

	/**
	 * 検索結果数を返します.<br>
	 * @param params 検索条件
	 * @return 検索結果数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.BillJoinService
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return this.billJoinService.findBillCntByCondition(params);
	}

	/**
	 * 検索を実行します.<br>
	 * @param params 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 表示数
	 * @param offset 表示ページ数
	 * @return 検索結果リスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#execSearch(org.seasar.framework.beans.util.BeanMap, java.lang.String, boolean, int, int)
	 * @see jp.co.arkinfosys.service.BillJoinService#findBillByConditionLimit(java.util.Map)
	 */
	@Override
	protected List<BeanMap> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		params.put(BillJoinService.Param.OFFSET_ROW, offset);
		return this.billJoinService.findBillByConditionLimit(params);
	}

	/**
	 * 検索結果の形式を変換します.<br>
	 * 請求書検索では検索結果の形式変換を行う必要が無いため、引数をそのまま返します.
	 * @param entityList BeanMap形式の検索結果リスト
	 * @return 検索結果リスト　BeanMap形式
	 * @throws Exception
	 */
	@Override
	protected List<BeanMap> exchange(List<BeanMap> entityList) throws Exception {
		return entityList;
	}

	/**
	 * 請求書検索アクションで使用するアクションフォームを返します.<br>
	 * @return 請求書検索用アクションフォーム
	 * @see jp.co.arkinfosys.form.bill.SearchBillForm
	 */
	@Override
	protected AbstractSearchForm<BeanMap> getActionForm() {
		return this.searchBillForm;
	}

	/**
	 * 検索後の遷移URIを返します.<br>
	 * @return 検索後の遷移URI
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 請求書検索画面のメニューIDを返します.<br>
	 * @return 請求書検索画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_BILL;
	}

	/**
	 * 請求書検索画面では検索サービスを返す必要が無いのでnullを返します.<br>
	 * @return null固定
	 */
	@Override
	protected MasterSearch<BeanMap> getService() {
		return null;
	}

	/**
	 * 請求書検索アクションで使用するDTOクラスを返します.<br>
	 * 請求書検索画面ではDTOを返す必要が無いのでnullを返します.<br>
	 * @return null固定
	 * @see jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto
	 */
	@Override
	protected Class<BeanMap> getDtoClass() {
		return null;
	}

}
