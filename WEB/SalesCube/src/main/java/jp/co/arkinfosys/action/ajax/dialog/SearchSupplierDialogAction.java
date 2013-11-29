/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import javax.annotation.Resource;

import jp.co.arkinfosys.dto.master.SupplierDto;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.ajax.dialog.SearchSupplierDialogForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 * 仕入先検索ダイアログの表示・検索処理アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchSupplierDialogAction extends
		AbstractSearchDialogAction<SupplierDto, SupplierJoin> {

	/**
	 * 仕入マスタに対するサービスクラスです.
	 */
	@Resource
	private SupplierService supplierService;

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public SearchSupplierDialogForm searchSupplierDialogForm;

	/**
	 * プルダウンの内容を初期化します.
	 */
	@Override
	protected void createList() throws ServiceException {
	}

	/**
	 * 検索カラムと検索順序を設定します.
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		this.searchSupplierDialogForm.sortColumn = SupplierService.Param.SUPPLIER_CODE;
		this.searchSupplierDialogForm.sortOrderAsc = true;
	}

	/**
	 * アクションフォームを返します.
	 *
	 * @return {@link SearchSupplierDialogForm}
	 */
	@Override
	protected AbstractSearchForm<SupplierDto> getActionForm() {
		return this.searchSupplierDialogForm;
	}

	/**
	 * {@link SupplierDto}クラスのクラスオブジェクトを返します.
	 *
	 * @return {@link SupplierDto}クラス
	 */
	@Override
	protected Class<SupplierDto> getDtoClass() {
		return SupplierDto.class;
	}

	/**
	 * 検索キー値を返します.
	 *
	 * @return 仕入先コード
	 */
	@Override
	protected String getId() {
		return this.searchSupplierDialogForm.supplierCode;
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
	 * {@link SupplierService}クラスのインスタンスを返します.
	 *
	 * @return {@link SupplierService}
	 */
	@Override
	protected MasterSearch<SupplierJoin> getService() {
		return this.supplierService;
	}
}
