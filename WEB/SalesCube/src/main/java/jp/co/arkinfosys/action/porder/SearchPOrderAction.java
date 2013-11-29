/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.porder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.porder.SearchPOrderForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.porder.SearchPOrderService;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 発注検索画面の表示アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchPOrderAction extends AbstractSearchAction<List<Object>> {

	@ActionForm
	@Resource
	protected SearchPOrderForm searchPOrderForm;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductClassService productClassService;

	@Resource
	private SearchPOrderService searchPOrderService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList;

	/**
	 * 発注検索画面の初期表示情報を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.searchPOrderForm.searchTarget = Constants.SEARCH_TARGET.VALUE_LINE;
		this.searchPOrderForm.sortColumn = SearchPOrderService.Param.PO_SLIP_ID;
		this.searchPOrderForm.sortOrderAsc = true;

		// 検索結果表示項目の取得
		this.columnInfoList = searchPOrderService.createSearchPOrderResult(
				null, null, searchPOrderForm.searchTarget);
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		this.searchPOrderForm.searchTargetList = ListUtil.getSearchTargetList();
		this.searchPOrderForm.transportCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.TRANSPORT_CATEGORY);
		this.searchPOrderForm.product1List = productClassService
				.findAllProductClass1LabelValueBeanList();
		this.searchPOrderForm.product2List = new ArrayList<LabelValueBean>();
		this.searchPOrderForm.product3List = new ArrayList<LabelValueBean>();

		this.searchPOrderForm.transportCategoryList.add(0, new LabelValueBean());
		this.searchPOrderForm.product1List.add(0, new LabelValueBean());
		this.searchPOrderForm.product2List.add(new LabelValueBean());
		this.searchPOrderForm.product3List.add(new LabelValueBean());
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchPOrderForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchPOrderForm;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 発注検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_PORDER;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 発注入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_PORDER;
	}

}
