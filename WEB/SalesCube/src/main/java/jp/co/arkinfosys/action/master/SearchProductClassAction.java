/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.ProductClassDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchProductClassForm;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 顧客ランク画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchProductClassAction extends
		AbstractSearchAction<ProductClassDto> {

	@ActionForm
	@Resource
	public SearchProductClassForm searchProductClassForm;

	@Resource
	public ProductClassService productClassService;

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		// 分類（大）
		this.searchProductClassForm.classCode1List = productClassService
				.findAllProductClass1LabelValueBeanList();
		this.searchProductClassForm.classCode1List.add(0, new LabelValueBean());

		// 分類（中）
		this.searchProductClassForm.classCode2List = new ArrayList<LabelValueBean>();
		this.searchProductClassForm.classCode2List.add(new LabelValueBean());

		// 分類（小）
		this.searchProductClassForm.classCode3List = new ArrayList<LabelValueBean>();
		this.searchProductClassForm.classCode3List.add(new LabelValueBean());
	}

	/**
	 * 
	 * @return {@link SearchProductClassForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<ProductClassDto> getActionForm() {
		return this.searchProductClassForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_PRODUCT_CLASS}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_PRODUCT_CLASS;
	}

}
