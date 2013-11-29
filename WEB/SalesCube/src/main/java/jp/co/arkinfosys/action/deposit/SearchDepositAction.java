/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.deposit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.deposit.SearchDepositForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
*
* 入金検索画面の表示アクションクラスです.
*
* @author Ark Information Systems
*
*
*/
public class SearchDepositAction extends AbstractSearchAction<List<Object>> {

	@ActionForm
	@Resource
	private SearchDepositForm searchDepositForm;

	@Resource
	private CategoryService categoryService;

	@Resource
	private DetailDispItemService detailDispItemService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList = new ArrayList<DetailDispItemDto>();

	/**
	 * 入金検索画面の初期表示情報を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.searchDepositForm.searchTarget = Constants.SEARCH_TARGET.VALUE_LINE;

		// 検索結果表示項目の取得
		this.columnInfoList = detailDispItemService.createResult(null, null,
				this.getSearchMenuID(), Constants.SEARCH_TARGET.VALUE_SLIP);
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		this.searchDepositForm.depositMethodTypeCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DEPOSIT_METHOD_CATEGORY);
		this.searchDepositForm.depositCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DEPOSIT_CATEGORY);
		this.searchDepositForm.depositMethodTypeCategoryList.add(0,
				new LabelValueBean());
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchDepositForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchDepositForm;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 入金検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_DEPOSIT;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 入金入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_DEPOSIT;
	}
}