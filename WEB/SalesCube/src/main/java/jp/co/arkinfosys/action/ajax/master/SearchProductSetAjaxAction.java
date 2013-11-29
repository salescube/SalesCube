/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.ProductSetDto;
import jp.co.arkinfosys.entity.join.ProductSetJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchProductSetForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.ProductSetService;

import org.seasar.struts.annotation.ActionForm;

/**
 * セット商品マスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchProductSetAjaxAction extends
		AbstractSearchResultAjaxAction<ProductSetDto, ProductSetJoin> {

	@Resource
	public ProductSetService productSetService;

	@ActionForm
	@Resource
	public SearchProductSetForm searchProductSetForm;

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchProductSetForm}
	 */
	@Override
	protected AbstractSearchForm<ProductSetDto> getActionForm() {
		return this.searchProductSetForm;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * セット商品マスタDTOを返します.
	 * @return {@link ProductSetDto}
	 */
	@Override
	protected Class<ProductSetDto> getDtoClass() {
		return ProductSetDto.class;
	}

	/**
	 * 検索処理を行うセット商品マスタサービスを返します.
	 * @return {@link ProductSetService}
	 */
	@Override
	protected MasterSearch<ProductSetJoin> getService() {
		return this.productSetService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return セット商品マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_PRODUCT_SET;
	}
}
