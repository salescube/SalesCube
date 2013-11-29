/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.rorder;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.rorder.SearchROrderForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 *
 * 受注検索画面の表示アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchROrderAction extends AbstractSearchAction<List<Object>> {

	@ActionForm
	@Resource
	public SearchROrderForm searchROrderForm;

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
	 * 受注検索画面の初期表示情報を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.searchROrderForm.searchTarget = Constants.SEARCH_TARGET.VALUE_LINE;

		// 検索結果表示項目の取得
		this.columnInfoList = detailDispItemService.createResult(null, null,
				this.getSearchMenuID(), Constants.SEARCH_TARGET.VALUE_LINE);
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchROrderForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchROrderForm;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 受注入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_RORDER;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 受注検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_RORDER;
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		// 画面のプルダウンリストを初期化する
		this.searchROrderForm.searchTargetList = ListUtil.getSearchTargetList();
		this.searchROrderForm.product1List = productClassService
				.findAllProductClass1LabelValueBeanList();
		this.searchROrderForm.product1List = ListUtil
				.addEmptyLabelValue(this.searchROrderForm.product1List);
		this.searchROrderForm.product2List = ListUtil
				.addEmptyLabelValue(this.searchROrderForm.product2List);
		this.searchROrderForm.product3List = ListUtil
				.addEmptyLabelValue(this.searchROrderForm.product3List);
		this.searchROrderForm.salesCmCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.SALES_CM_CATEGORY);
	}
}
