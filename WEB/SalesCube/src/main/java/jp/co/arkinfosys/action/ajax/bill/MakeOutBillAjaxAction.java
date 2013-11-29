
/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.bill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.bill.MakeOutBillForm;
import jp.co.arkinfosys.service.BillJoinService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

/**
 * 請求書発行画面の検索実行アクションクラスです
 *
 * @author Ark Information Systems
 *
 */
public class MakeOutBillAjaxAction extends
		AbstractSearchResultAjaxAction<MakeOutBillSearchResultDto, BeanMap> {

	@ActionForm
	@Resource
	public MakeOutBillForm makeOutBillForm;

	@Resource
	private BillJoinService billJoinService;

	/**
	 * 検索結果件数カウントを返します.<br>
	 * 実際には使用されていないため、常に０を返します.<br>
	 * 使用しないでください.
	 * @param params 検索条件
	 * @return 常に0を返します
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return 0;
	}

	/**
	 * 検索条件に応じて請求書検索結果のリストを返します.<br>
	 * 検索結果はBeanMap形式で返ります.<br>
	 * @param params 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 表示数
	 * @param offset 表示ページ数
	 * @return 検索結果リスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#execSearch(org.seasar.framework.beans.util.BeanMap, java.lang.String, boolean, int, int)
	 * @see jp.co.arkinfosys.service.BillJoinService#findBillForMakeOut(java.util.Map)
	 */
	@Override
	protected List<BeanMap> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return this.billJoinService.findBillForMakeOut(params);
	}

	/**
	 * 検索結果の形式を変換します.<br>
	 * @param entityList BeanMap形式の検索結果リスト
	 * @return 検索結果リスト　MakeOutBillSearchResultDto形式
	 * @throws Exception
	 * @see jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto
	 */
	@Override
	protected List<MakeOutBillSearchResultDto> exchange(List<BeanMap> entityList)
			throws Exception {
		// 請求書発行日の型変換util.Date→sql.Date
		for (BeanMap map : entityList) {
			map.put("lastPrintDate", this.billJoinService
					.convertUtilDateToSqlDate((Date) map.get("lastPrintDate")));
		}
		return super.exchange(entityList);
	}

	/**
	 * 検索結果の一覧から指定されたページの情報を切り出してアクションフォームにセットします.<br>
	 * @throws Exception
	 * @see jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto
	 */
	@Override
	protected void doAfterSearch() throws Exception {
		List<MakeOutBillSearchResultDto> allSearchResultList = this.makeOutBillForm.searchResultList;
		this.makeOutBillForm.searchResultCount = allSearchResultList.size();

		int pageNo = this.makeOutBillForm.pageNo;
		int rowCount = this.makeOutBillForm.rowCount;
		int offset = rowCount * (pageNo - 1);

		int maxRow = offset + rowCount;
		if (maxRow < 0 || maxRow > allSearchResultList.size()) {
			maxRow = allSearchResultList.size();
		}

		this.makeOutBillForm.allSearchResultList = allSearchResultList;
		this.makeOutBillForm.searchResultList = new ArrayList<MakeOutBillSearchResultDto>(
				this.makeOutBillForm.allSearchResultList
						.subList(offset, maxRow));
	}

	/**
	 * 請求書発行アクションで使用するアクションフォームを返します.<br>
	 * @return 請求書発行用アクションフォーム
	 * @see jp.co.arkinfosys.form.bill.MakeOutBillForm
	 */
	@Override
	protected AbstractSearchForm<MakeOutBillSearchResultDto> getActionForm() {
		return this.makeOutBillForm;
	}

	/**
	 * 請求書発行アクションで使用するDTOクラスを返します.<br>
	 * @return 請求書発行用DTO
	 * @see jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto
	 */
	@Override
	protected Class<MakeOutBillSearchResultDto> getDtoClass() {
		return MakeOutBillSearchResultDto.class;
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
	 * 請求書発行画面のメニューIDを返します.<br>
	 * @return 請求書発行画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MAKE_OUT_BILL;
	}

	/**
	 * 請求書発行画面では検索サービスを返す必要が無いのでnullを返します.<br>
	 * @return null固定
	 */
	@Override
	protected MasterSearch<BeanMap> getService() {
		return null;
	}
}
