/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.dto.master.CustomerDto;
import jp.co.arkinfosys.entity.CustomerRank;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.ajax.dialog.SearchCustomerDialogForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerRankService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 顧客検索ダイアログの表示・検索処理アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchCustomerDialogAction extends
		AbstractSearchDialogAction<CustomerDto, CustomerJoin> {

	/**
	 * 区分マスタに対するサービスクラスです.
	 */
	@Resource
	protected CategoryService categoryService;

	/**
	 * 顧客マスタに対するサービスクラスです.
	 */
	@Resource
	protected CustomerService customerService;

	/**
	 * 顧客ランクマスタに対するサービスクラスです.
	 */
	@Resource
	protected CustomerRankService customerRankService;

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public SearchCustomerDialogForm searchCustomerDialogForm;

	/**
	 * 顧客ランクプルダウンと支払条件プルダウンの内容を初期化します.
	 *
	 * @throws ServiceException サービス例外発生時
	 */
	@Override
	protected void createList() throws ServiceException {
		// 顧客ランクリストを取得
		List<CustomerRank> customerRankList = this.customerRankService
				.findAllCustomerRank();
		for (CustomerRank customerRank : customerRankList) {
			LabelValueBean bean = new LabelValueBean(customerRank.rankName,
					customerRank.rankCode);
			this.searchCustomerDialogForm.customerRankCategoryList.add(bean);
		}
		this.searchCustomerDialogForm.customerRankCategoryList.add(0,
				new LabelValueBean());

		// 支払条件リストを取得
		this.searchCustomerDialogForm.cutoffGroupList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.CUTOFF_GROUP);
		this.searchCustomerDialogForm.cutoffGroupList.add(0,
				new LabelValueBean());
	}

	/**
	 * 検索カラムと検索順序を設定します.
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchCustomerDialogForm.sortColumn = CustomerService.Param.CUSTOMER_CODE;
		this.searchCustomerDialogForm.sortOrderAsc = true;
	}

	/**
	 * アクションフォームを返します.
	 *
	 * @return {@link SearchCustomerDialogForm}
	 */
	@Override
	protected AbstractSearchForm<CustomerDto> getActionForm() {
		return this.searchCustomerDialogForm;
	}

	/**
	 * {@link CustomerDto}クラスのクラスオブジェクトを返します.
	 *
	 * @return {@link CustomerDto}クラス
	 */
	@Override
	protected Class<CustomerDto> getDtoClass() {
		return CustomerDto.class;
	}

	/**
	 * 検索キー値を返します.
	 *
	 * @return 顧客コード
	 */
	@Override
	protected String getId() {
		return this.searchCustomerDialogForm.customerCode;
	}

	/**
	 * キー値での検索結果が0件だったことを通知するために、メッセージリソースのキーを返します.
	 *
	 * @return メッセージキー
	 */
	@Override
	protected String getMissingRecordMessageKey() {
		return "errors.customer.not.exist.code";
	}

	/**
	 * {@link CustomerService}クラスのインスタンスを返します.
	 *
	 * @return {@link CustomerService}
	 */
	@Override
	protected MasterSearch<CustomerJoin> getService() {
		return this.customerService;
	}
}
