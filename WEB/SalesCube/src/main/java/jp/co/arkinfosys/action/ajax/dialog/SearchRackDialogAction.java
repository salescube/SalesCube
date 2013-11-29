/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.entity.join.RackJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.ajax.dialog.SearchRackDialogForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 棚番検索ダイアログの表示・検索処理アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchRackDialogAction extends
		AbstractSearchDialogAction<RackDto, RackJoin> {

	/**
	 * 棚番マスタに対するサービスクラスです.
	 */
	@Resource
	private RackService rackService;

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
	public SearchRackDialogForm searchRackDialogForm;

	/**
	 * プルダウンの内容を初期化します.
	 *
	 * @throws ServiceException サービス例外発生時
	 */
	@Override
	protected void createList() throws ServiceException {
		// 棚区分リスト
		this.searchRackDialogForm.rackCategoryList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.RACK_CATEGORY);
		this.searchRackDialogForm.rackCategoryList.add(0, new LabelValueBean());
	}

	/**
	 * 検索カラムと検索順序を設定します.
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchRackDialogForm.sortColumn = RackService.Param.RACK_CODE;
		this.searchRackDialogForm.sortOrderAsc = true;
	}

	/**
	 * 検索結果の{@link RackJoin}リストを{@link RackDto}リストに変換します.
	 *
	 * @return {@link RackDto}リスト
	 * @throws Exception 値変換での例外発生時
	 */
	@Override
	protected List<RackDto> exchange(List<RackJoin> entityList)
			throws Exception {
		List<RackDto> rackDtoList = super.exchange(entityList);
		// 棚に属する商品の情報を追加する
		this.rackService.addProductInfoToRackDto(rackDtoList);
		return rackDtoList;
	}

	/**
	 * アクションフォームを返します.
	 *
	 * @return {@link SearchRackDialogForm}
	 */
	@Override
	protected AbstractSearchForm<RackDto> getActionForm() {
		return this.searchRackDialogForm;
	}

	/**
	 * {@link RackDto}クラスのクラスオブジェクトを返します.
	 *
	 * @return {@link RackDto}クラス
	 */
	@Override
	protected Class<RackDto> getDtoClass() {
		return RackDto.class;
	}

	/**
	 * 検索キー値を返します.
	 *
	 * @return 棚番コード
	 */
	@Override
	protected String getId() {
		return this.searchRackDialogForm.rackCode;
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
	 * {@link RackService}クラスのインスタンスを返します.
	 *
	 * @return {@link RackService}
	 */
	@Override
	protected MasterSearch<RackJoin> getService() {
		return this.rackService;
	}
}
