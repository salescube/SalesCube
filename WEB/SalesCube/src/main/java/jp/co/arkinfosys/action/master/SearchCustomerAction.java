/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.CustomerDto;
import jp.co.arkinfosys.entity.CustomerRank;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchCustomerForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerRankService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 顧客検索画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchCustomerAction extends AbstractSearchAction<CustomerDto> {

	@ActionForm
	@Resource
	public SearchCustomerForm searchCustomerForm;

	/** 得意先マスタのアクセスクラス */
	@Resource
	public CustomerService customerService;

	/** 顧客ランクマスタのアクセスクラス */
	@Resource
	public CustomerRankService customerRankService;

	/** 区分マスタのアクセスクラス */
	public CategoryService categoryService;

	/**
	 * ソート条件を設定します.
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doBeforeIndex()
	 */
	@Override
	protected void doBeforeIndex() throws Exception {
		this.searchCustomerForm.sortColumn = CustomerService.Param.CUSTOMER_CODE;
		this.searchCustomerForm.sortOrderAsc = true;
	}

	/**
	 *
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		// 顧客ランクリストを取得
		List<CustomerRank> customerRankList = this.customerRankService
				.findAllCustomerRank();
		for (CustomerRank customerRank : customerRankList) {
			LabelValueBean bean = new LabelValueBean();
			bean.setValue(customerRank.rankCode);
			bean.setLabel(customerRank.rankName);
			this.searchCustomerForm.customerRankList.add(bean);
		}
		this.searchCustomerForm.customerRankList.add(0, new LabelValueBean());

		// 支払条件リストを取得
		List<CategoryJoin> categoryJoinList = this.categoryService
				.findCategoryJoinById(Categories.CUTOFF_GROUP);
		for (CategoryJoin categoryJoin : categoryJoinList) {
			LabelValueBean bean = new LabelValueBean();
			bean.setValue(categoryJoin.categoryCode);
			bean.setLabel(categoryJoin.categoryCodeName);
			this.searchCustomerForm.cutoffGroupList.add(bean);
		}

		this.searchCustomerForm.cutoffGroupList.add(0, new LabelValueBean());
	}

	/**
	 *
	 * @return {@link SearchCustomerForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<CustomerDto> getActionForm() {
		return this.searchCustomerForm;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_CUSTOMER}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_CUSTOMER;
	}
}
