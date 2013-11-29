/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.purchase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.purchase.SearchPurchaseForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.purchase.SearchPurchaseService;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 仕入検索画面の表示アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchPurchaseAction extends AbstractSearchAction<List<Object>> {

	@ActionForm
	@Resource
	private SearchPurchaseForm searchPurchaseForm;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductClassService productClassService;

	@Resource
	private DetailDispItemService detailDispItemService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList;

	/**
	 * 仕入検索画面の初期表示情報を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		// 発注入力画面・支払入力画面の権限を取得
		this.searchPurchaseForm.isInputPOrderValid = userDto
				.isMenuValid(Constants.MENU_ID.INPUT_PORDER);
		this.searchPurchaseForm.isInputPaymentValid = userDto
				.isMenuValid(Constants.MENU_ID.INPUT_PAYMENT);

		// 検索対象プルダウンの初期値を設定
		searchPurchaseForm.searchTarget = Constants.SEARCH_TARGET.VALUE_LINE;

		// ソートカラムの初期値を設定
		searchPurchaseForm.sortColumn = SearchPurchaseService.Param.SUPPLIER_SLIP_ID;

		// 検索結果表示項目の取得
		columnInfoList = detailDispItemService.createResult(null, null, this
				.getSearchMenuID(), searchPurchaseForm.searchTarget);
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchPurchaseForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchPurchaseForm;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 仕入検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_PURCHASE;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 仕入入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_PURCHASE;
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		this.searchPurchaseForm.searchTargetList = ListUtil
				.getSearchTargetList();
		this.searchPurchaseForm.deliveryProcessCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DELIVERY_PROCESS_CATEGORY);
		this.searchPurchaseForm.product1List = productClassService
				.findAllProductClass1LabelValueBeanList();
		this.searchPurchaseForm.product2List = new ArrayList<LabelValueBean>();
		this.searchPurchaseForm.product3List = new ArrayList<LabelValueBean>();

		this.searchPurchaseForm.product1List.add(0, new LabelValueBean());
		this.searchPurchaseForm.product2List.add(new LabelValueBean());
		this.searchPurchaseForm.product3List.add(new LabelValueBean());
	}
}