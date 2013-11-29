/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.dto.master.ProductDto;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.ajax.dialog.SearchProductDialogForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 商品検索ダイアログの表示・検索処理アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchProductDialogAction extends
		AbstractSearchDialogAction<ProductDto, ProductJoin> {

	/**
	 * 区分マスタに対するサービスクラスです.
	 */
	@Resource
	protected CategoryService categoryService;

	/**
	 * 商品マスタに対するサービスクラスです.
	 */
	@Resource
	protected ProductService productService;

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public SearchProductDialogForm searchProductDialogForm;

	/**
	 * プルダウンの内容を初期化します.
	 *
	 * @throws ServiceException サービス例外発生時
	 */
	@Override
	protected void createList() throws ServiceException {
		// セット分類リストを取得
		this.searchProductDialogForm.setTypeCategoryList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.PRODUCT_SET_TYPE);
		this.searchProductDialogForm.setTypeCategoryList.add(0,
				new LabelValueBean());

		// 標準化分類リストを取得
		this.searchProductDialogForm.standardCategoryList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.PRODUCT_STANDARD);
		this.searchProductDialogForm.standardCategoryList.add(0,
				new LabelValueBean());

		// 状況分類リストを取得
		this.searchProductDialogForm.statusCategoryList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.PRODUCT_STATUS);
		this.searchProductDialogForm.statusCategoryList.add(0,
				new LabelValueBean());
	}

	/**
	 * 検索カラムと検索順序を設定します.
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchProductDialogForm.sortColumn = ProductService.Param.PRODUCT_CODE;
		this.searchProductDialogForm.sortOrderAsc = true;
	}

	/**
	 * アクションフォームを返します.
	 *
	 * @return {@link SearchProductDialogForm}
	 */
	@Override
	protected AbstractSearchForm<ProductDto> getActionForm() {
		return this.searchProductDialogForm;
	}

	/**
	 * {@link ProductDto}クラスのクラスオブジェクトを返します.
	 *
	 * @return {@link ProductDto}クラス
	 */
	@Override
	protected Class<ProductDto> getDtoClass() {
		return ProductDto.class;
	}

	/**
	 * 検索キー値を返します.
	 *
	 * @return 商品コード
	 */
	@Override
	protected String getId() {
		return this.searchProductDialogForm.productCode;
	}

	/**
	 * キー値での検索結果が0件だったことを通知するために、メッセージリソースのキーを返します.
	 *
	 * @return メッセージキー
	 */
	@Override
	protected String getMissingRecordMessageKey() {
		return "warns.product.not.exist";
	}

	/**
	 * {@link ProductService}クラスのインスタンスを返します.
	 *
	 * @return {@link ProductService}
	 */
	@Override
	protected MasterSearch<ProductJoin> getService() {
		return this.productService;
	}
}
