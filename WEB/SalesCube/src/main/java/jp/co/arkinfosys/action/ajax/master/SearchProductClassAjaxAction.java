/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.ProductClassDto;
import jp.co.arkinfosys.entity.join.ProductClassJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchProductClassForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.ProductClassService;

import org.seasar.struts.annotation.ActionForm;

/**
 * 分類マスタ画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchProductClassAjaxAction extends
		AbstractSearchResultAjaxAction<ProductClassDto, ProductClassJoin> {

	@ActionForm
	@Resource
	public SearchProductClassForm searchProductClassForm;

	@Resource
	public ProductClassService productClassService;

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchProductClassForm}
	 */
	@Override
	protected AbstractSearchForm<ProductClassDto> getActionForm() {
		return this.searchProductClassForm;
	}

	/**
	 * 分類マスタDTOを返します.
	 * @return {@link ProductClassDto}
	 */
	@Override
	protected Class<ProductClassDto> getDtoClass() {
		return ProductClassDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索処理を行う分類マスタサービスを返します.
	 * @return {@link ProductClassService}
	 */
	@Override
	protected MasterSearch<ProductClassJoin> getService() {
		return productClassService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 分類マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_PRODUCT_CLASS;
	}
}
