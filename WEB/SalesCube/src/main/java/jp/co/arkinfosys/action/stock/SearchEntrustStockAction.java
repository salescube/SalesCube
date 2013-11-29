/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.stock.SearchEntrustStockForm;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.SearchEntrustStockService;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 委託入出庫検索画面の表示アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchEntrustStockAction extends
		AbstractSearchAction<List<Object>> {

	@ActionForm
	@Resource
	private SearchEntrustStockForm searchEntrustStockForm;

	@Resource
	private SearchEntrustStockService searchEntrustStockService;

	@Resource
	private ProductClassService productClassService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList = new ArrayList<DetailDispItemDto>();

	/**
	 * 委託入出庫検索画面の初期表示情報を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		// 検索対象プルダウンの初期値を設定
		this.searchEntrustStockForm.searchTarget = Constants.SEARCH_TARGET.VALUE_LINE;

		// ソートカラムの初期値を設定
		this.searchEntrustStockForm.sortColumn = null;

		// 検索結果表示項目の取得
		this.columnInfoList = searchEntrustStockService
				.createSearchStockResult(null, null,
						searchEntrustStockForm.searchTarget);
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		// 検索対象
		this.searchEntrustStockForm.searchTargetList = ListUtil
				.getSearchTargetList();
		// 分類(大)
		this.searchEntrustStockForm.product1List = productClassService
				.findAllProductClass1LabelValueBeanList();
		this.searchEntrustStockForm.product1List.add(0, new LabelValueBean());
		// 分類(中)
		this.searchEntrustStockForm.product2List = new ArrayList<LabelValueBean>();
		this.searchEntrustStockForm.product2List.add(new LabelValueBean());
		// 分類(小)
		this.searchEntrustStockForm.product3List = new ArrayList<LabelValueBean>();
		this.searchEntrustStockForm.product3List.add(new LabelValueBean());
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchEntrustStockForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchEntrustStockForm;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 委託入出庫検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_ENTRUST_STOCK;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 委託入出庫入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_ENTRUST_STOCK;
	}
}
