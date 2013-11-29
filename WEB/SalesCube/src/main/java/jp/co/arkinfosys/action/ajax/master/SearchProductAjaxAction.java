/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.ProductDto;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchProductForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.ProductService;

import org.seasar.struts.annotation.ActionForm;

/**
 * 商品マスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 */
public class SearchProductAjaxAction extends
		AbstractSearchResultAjaxAction<ProductDto, ProductJoin> {

	@Resource
	public ProductService productService;

	@ActionForm
	@Resource
	public SearchProductForm searchProductForm;

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchProductForm}
	 */
	@Override
	protected AbstractSearchForm<ProductDto> getActionForm() {
		return this.searchProductForm;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 商品マスタDTOを返します.
	 * @return {@link ProductDto}
	 */
	@Override
	protected Class<ProductDto> getDtoClass() {
		return ProductDto.class;
	}

	/**
	 * 検索処理を行う商品マスタサービスを返します.
	 * @return {@link ProductService}
	 */
	@Override
	protected MasterSearch<ProductJoin> getService() {
		return this.productService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 商品マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_PRODUCT;
	}
}
