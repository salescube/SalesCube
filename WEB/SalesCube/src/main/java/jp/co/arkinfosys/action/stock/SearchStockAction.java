/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.stock.SearchStockForm;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.SearchStockService;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 入出庫検索画面の表示アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchStockAction extends AbstractSearchAction<List<Object>> {

	@ActionForm
	@Resource
	private SearchStockForm searchStockForm;

	@Resource
	private SearchStockService searchStockService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList = new ArrayList<DetailDispItemDto>();

	/**
	 * 入出庫検索画面の初期表示情報を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		// 検索対象プルダウンの初期値を設定
		this.searchStockForm.searchTarget = Constants.SEARCH_TARGET.VALUE_LINE;
		// ソートカラムの初期値を設定
		this.searchStockForm.sortColumn = EadService.Param.SRC_FUNC;
		// 検索結果表示項目の取得
		this.columnInfoList = searchStockService.createSearchStockResult(null,
				null, searchStockForm.searchTarget);
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		this.searchStockForm.searchTargetList = ListUtil.getSearchTargetList();
		this.searchStockForm.slipCategoryList = searchStockService
				.findCategoryLabelValueBeanListById(Categories.EAD_SLIP_CATEGORY);
		this.searchStockForm.slipCategoryList.add(0, new LabelValueBean());
		this.searchStockForm.product1List = searchStockService
				.findAllProductClass1LabelValueBeanList();
		this.searchStockForm.product1List.add(0, new LabelValueBean());
		this.searchStockForm.product2List = new ArrayList<LabelValueBean>();
		this.searchStockForm.product2List.add(new LabelValueBean());
		this.searchStockForm.product3List = new ArrayList<LabelValueBean>();
		this.searchStockForm.product3List.add(new LabelValueBean());
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchStockForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchStockForm;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 入出庫検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_STOCK;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 入出庫入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_STOCK;
	}

}
