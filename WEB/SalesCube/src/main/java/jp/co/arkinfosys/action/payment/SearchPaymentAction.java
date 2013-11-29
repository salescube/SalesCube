/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.payment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.payment.SearchPaymentForm;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.payment.SearchPaymentService;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 支払検索画面の表示アクションクラスです.
 *
 * @author Ark Information Systems
 * @param List<Object> アクションフォームに対応したDTOクラス
 */
public class SearchPaymentAction extends AbstractSearchAction<List<Object>> {

	@ActionForm
	@Resource
	private SearchPaymentForm searchPaymentForm;

	@Resource
	private ProductClassService productClassService;

	@Resource
	private DetailDispItemService detailDispItemService;

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList;

	/**
	 * 支払検索画面の初期表示情報を設定します.
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.searchPaymentForm.isInputPOrderValid = userDto
				.isMenuValid(Constants.MENU_ID.INPUT_PORDER);
		this.searchPaymentForm.isInputPurchaseValid = userDto
				.isMenuValid(Constants.MENU_ID.INPUT_PURCHASE);

		// 検索対象プルダウンの初期値を設定
		this.searchPaymentForm.searchTarget = Constants.SEARCH_TARGET.VALUE_LINE;

		// ソートカラムの初期値を設定
		this.searchPaymentForm.sortColumn = SearchPaymentService.Param.PAYMENT_SLIP_ID;

		// 検索結果表示項目の取得
		this.columnInfoList = detailDispItemService.createResult(null, null,
				this.getSearchMenuID(), searchPaymentForm.searchTarget);
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link SearchPaymentForm}
	 */
	@Override
	protected AbstractSearchForm<List<Object>> getActionForm() {
		return this.searchPaymentForm;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 支払検索画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SEARCH_PAYMENT;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 支払入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_PAYMENT;
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		this.searchPaymentForm.searchTargetList = ListUtil
				.getSearchTargetList();
		this.searchPaymentForm.product1List = this.productClassService
				.findAllProductClass1LabelValueBeanList();
		this.searchPaymentForm.product2List = new ArrayList<LabelValueBean>();
		this.searchPaymentForm.product3List = new ArrayList<LabelValueBean>();

		this.searchPaymentForm.product1List.add(0, new LabelValueBean());
		this.searchPaymentForm.product2List.add(new LabelValueBean());
		this.searchPaymentForm.product3List.add(new LabelValueBean());
	}
}