/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.bill;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchResultAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.bill.SearchBillForm;
import jp.co.arkinfosys.service.BillJoinService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 請求検索画面の検索結果をEXCELとしてクライアントに送信するアクションクラスです。
 *
 * @author Ark Information Systems
 *
 */
public class SearchBillResultOutputAction extends
		AbstractSearchResultAction<BeanMap, BeanMap> {

	@ActionForm
	@Resource
	public SearchBillForm searchBillForm;

	@Resource
	private BillJoinService billJoinService;

	/**
	 * 請求書検索結果をExcel形式で出力します.<br>
	 * @return 画面遷移先のURI文字列
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.EXCEL)
	public String excel() throws Exception {
		this.outputExcel = true;
		return super.doSearch();
	}

	/**
	 * 請求書検索の初期値を設定します.<br>
	 * 請求書分類は請求締を初期値とします.<br>
	 * 入金伝票の入力権限を取得します。検索結果から入金伝票入力画面へ遷移するか否かの判断に使用します.<br>
	 * @throws Exception
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchBillForm.searchTarget = Constants.SEARCH_TARGET.VALUE_SLIP;
		this.searchBillForm.isInputDepositValid = super.userDto.isMenuUpdate(Constants.MENU_ID.INPUT_DEPOSIT);
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
		return 0;
	}

	/**
	 * 検索条件に応じて請求書を検索します.<br>
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
		params.put(BillJoinService.Param.ROW_COUNT, null);
		return this.billJoinService.findBillByCondition(params);
	}

	/**
	 * 何も行いません.<br>
	 * 引数のリストをそのまま返します.<br>
	 * @param entityList ENTITYのリスト
	 * @return BeanMapのリスト
	 * @throws Exception
	 */
	@Override
	protected List<BeanMap> exchange(List<BeanMap> entityList) throws Exception {
		return entityList;
	}

	/**
	 * 請求書Excel出力アクションで使用するアクションフォームを返します.<br>
	 * @return 請求書検索用アクションフォーム
	 * @see jp.co.arkinfosys.form.bill.SearchBillForm
	 */
	@Override
	protected AbstractSearchForm<BeanMap> getActionForm() {
		return this.searchBillForm;
	}

	/**
	 * Excel出力後の遷移URIを返します.<br>
	 * @return Excel出力後の遷移URI
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.EXCEL;
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
	 * 請求書検索アクションではDTOクラスを使用しないのでnullを返します.<br>
	 * @return null固定
	 */
	@Override
	protected Class<BeanMap> getDtoClass() {
		return null;
	}

}
