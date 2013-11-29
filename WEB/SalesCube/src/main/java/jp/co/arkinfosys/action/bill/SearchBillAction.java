/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.bill;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.bill.SearchBillForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

/**
 * 請求検索画面の表示アクションクラスです
 *
 * @author Ark Information Systems
 *
 */
public class SearchBillAction extends AbstractSearchAction<BeanMap> {

	@ActionForm
	@Resource
	public SearchBillForm searchBillForm;

	/**
	 * 検索結果列サービスクラス
	 */
	@Resource
	private DetailDispItemService detailDispItemService;

	@Resource
	protected CategoryService categoryService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList = new ArrayList<DetailDispItemDto>();

	/**
	 * 請求書検索結果画面で表示する項目をDBから取得します.<br>
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		// 検索結果表示項目の取得
		this.columnInfoList = detailDispItemService.createResult(null, null,
				this.getSearchMenuID(), Constants.SEARCH_TARGET.VALUE_SLIP);
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
	 * 請求書検索画面のメニューIDを返します.<br>
	 * @return 請求書検索画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_BILL;
	}

	/**
	 * 請求書検索画面で使用する請求書作成区分、支払条件の一覧を作成します.<br>
	 * 支払条件の先頭は空欄とします.
	 * @throws Exception
	 */
	@Override
	protected void createList() throws ServiceException {
		// 請求書作成区分リストの作成
		this.searchBillForm.billCrtCategoryList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.BILL_CRT_CATEGORY);

		// 支払条件リストの作成
		this.searchBillForm.cutoffGroupCategoryList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.CUTOFF_GROUP);
		this.searchBillForm.cutoffGroupCategoryList
				.add(0, new LabelValueBean());
	}
}
