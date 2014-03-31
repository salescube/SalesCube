/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.dto.master.DiscountDto;
import jp.co.arkinfosys.entity.join.DiscountJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.ajax.dialog.SearchDiscountDialogForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.DiscountService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 数量割引検索ダイアログの表示・検索処理ダイアログクラスです.
 *
 *
 * @author Ark Information Systems
 *
 */
public class SearchDiscountDialogAction extends
		AbstractSearchDialogAction<DiscountDto, DiscountJoin> {

	/**
	 * 数量割引マスタに対するサービスクラスです.
	 */
	@Resource
	private DiscountService discountService;

	/**
	 * 区分マスタに対するサービスクラスです.
	 */
	@Resource
	private CategoryService categoryService;

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public SearchDiscountDialogForm searchDiscountDialogForm;

	/**
	 * プルダウンリストの内容を初期化します.
	 *
	 * @throws ServiceException サービス例外発生時
	 */
	@Override
	protected void createList() throws ServiceException {
		// 有効無効リスト
		this.searchDiscountDialogForm.useFlagList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.USE_FLAG);
		this.searchDiscountDialogForm.useFlagList.add(0, new LabelValueBean());

	}

	/**
	 * 検索カラムと検索順序を設定します.
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchDiscountDialogForm.sortColumn = DiscountService.Param.DISCOUNT_ID;
		this.searchDiscountDialogForm.sortOrderAsc = true;
	}

	/**
	 * 検索結果の{@link DiscountJoin}リストを{@link DiscountDto}リストに変換します.
	 *
	 * @return {@link DiscountDto}リスト
	 * @throws Exception 値変換での例外発生時
	 */
	@Override
	protected List<DiscountDto> exchange(List<DiscountJoin> entityList)
			throws Exception {
		return this.discountService.convertDiscountJoinToDto(entityList);
	}

	/**
	 * アクションフォームを返します.
	 *
	 * @return {@link SearchDiscountDialogForm}
	 */
	@Override
	protected AbstractSearchForm<DiscountDto> getActionForm() {
		return this.searchDiscountDialogForm;
	}

	/**
	 * {@link DiscountDto}クラスのクラスオブジェクトを返します.
	 *
	 * @return {@link DiscountDto}クラス
	 */
	@Override
	protected Class<DiscountDto> getDtoClass() {
		return DiscountDto.class;
	}

	/**
	 * 検索キー値を返します.
	 *
	 * @return 割引ID
	 */
	@Override
	protected String getId() {
		return this.searchDiscountDialogForm.discountId;
	}

	/**
	 * キー値での検索結果が0件だったことを通知するために、メッセージリソースのキーを返します.
	 *
	 * @return メッセージキー
	 */
	@Override
	protected String getMissingRecordMessageKey() {
		return null;
	}

	/**
	 * {@link DiscountService}クラスのインスタンスを返します.
	 *
	 * @return {@link DiscountService}
	 */
	@Override
	protected MasterSearch<DiscountJoin> getService() {
		return this.discountService;
	}
	
	
	/**
	 * 検索実行後に行う処理のデフォルト実装です.通常は何も行いません.
	 *
	 * @throws Exception 例外発生時
	 */
	@Override
	protected void doAfterSearch() throws Exception {
		searchDiscountDialogForm.searchResultCount = searchDiscountDialogForm.searchResultList.size();
	}	
}
