/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.sales;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.sales.SearchSalesForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 売上検索画面の表示アクションクラスです.<BR>
 *
 * @author Ark Information Systems
 *
 */
public class SearchSalesAction extends AbstractSearchAction<List<Object>> {

	@ActionForm
	@Resource
	private SearchSalesForm searchSalesForm;

	@Resource
	private ProductClassService productClassService;

	@Resource
	private DetailDispItemService detailDispItemService;

	@Resource
	private CategoryService categoryService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList;

	/**
	 * {@link AbstractSearchAction#index()}の初期化後に必要な処理を行います.<br>
	 * アクションフォームの初期値を設定します.<br>
	 * 検索結果表示項目をアクションフォームに設定します.<br>
	 * 受注入力画面への遷移権限情報をアクションフォームに設定します.
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.searchSalesForm.searchTarget = Constants.SEARCH_TARGET.VALUE_LINE;

		// 検索結果表示項目の取得
		this.columnInfoList = detailDispItemService.createResult(null, null,
				this.getSearchMenuID(), Constants.SEARCH_TARGET.VALUE_LINE);

		// 受注入力画面の権限フラグを設定
		this.searchSalesForm.isInputROrderValid = userDto
				.isMenuValid(Constants.MENU_ID.INPUT_RORDER);
	}

	/**
	 * 売上伝票検索用アクションフォームを返します.
	 * @return SearchSalesForm形式のアクションフォーム
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchSalesForm;
	}

	/**
	 * 売上伝票入力画面のメニューIDを返します.
	 * @return 売上伝票入力画面メニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_SALES;
	}

	/**
	 * 売上伝票検索画面のメニューIDを返します.
	 * @return 売上伝票検索画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_SALES;
	}

	/**
	 * プルダウンの要素を作成します.<BR>
	 * 1.検索対象リストを作成します.<BR>
	 * 2.配送業者区分リストを作成します.<BR>
	 * 3.配送業者区分リストの先頭に空欄の選択肢を追加します.<BR>
	 * 4.配送時間帯区分リストを作成します.<BR>
	 * 5.配送時間帯区分リストの先頭に空欄の選択肢を追加します.<BR>
	 * 6.分類大リストを作成します.<BR>
	 * 7.分類中リストを作成します.<BR>
	 * 8.分類小リストを作成します.<BR>
	 * 9.売上取引区分リストを作成します.<BR>
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		// 画面のプルダウンリストを初期化する
		this.searchSalesForm.searchTargetList = ListUtil.getSearchTargetList();
		this.searchSalesForm.dcCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_CATEGORY);
		this.searchSalesForm.dcCategoryList.add(0, new LabelValueBean("", ""));
		this.searchSalesForm.dcTimezoneCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_TIMEZONE_CATEGORY);
		this.searchSalesForm.dcTimezoneCategoryList.add(0, new LabelValueBean(
				"", ""));
		this.searchSalesForm.product1List = productClassService
				.findAllProductClass1LabelValueBeanList();
		this.searchSalesForm.product1List = ListUtil
				.addEmptyLabelValue(this.searchSalesForm.product1List);
		this.searchSalesForm.product2List = ListUtil
				.addEmptyLabelValue(this.searchSalesForm.product2List);
		this.searchSalesForm.product3List = ListUtil
				.addEmptyLabelValue(this.searchSalesForm.product3List);
		this.searchSalesForm.salesCmCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.SALES_CM_CATEGORY);
	}
}