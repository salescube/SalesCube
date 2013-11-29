/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import javax.annotation.Resource;

import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.entity.join.WarehouseJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.ajax.dialog.SearchWarehouseDialogForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.WarehouseService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

public class SearchWarehouseDialogAction  extends
		AbstractSearchDialogAction<WarehouseDto, WarehouseJoin> {

	/**
	 * 倉庫マスタに対するサービスクラスです.
	 */
	@Resource
	private WarehouseService warehouseService;

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public SearchWarehouseDialogForm searchWarehouseDialogForm;


	/**
	 * アクションフォームを返します.
	 *
	 * @return {@link SearchWarehouseDialogForm}
	 */
	@Override
	protected AbstractSearchForm<WarehouseDto> getActionForm() {
		return this.searchWarehouseDialogForm;
	}

	/**
	 * {@link WarehouseDto}クラスのクラスオブジェクトを返します.
	 *
	 * @return {@link WarehouseDto}クラス
	 */
	@Override
	protected Class<WarehouseDto> getDtoClass() {
		return WarehouseDto.class;
	}

	/**
	 * 検索キー値を返します.
	 *
	 * @return 倉庫コード
	 */
	@Override
	protected String getId() {
		return this.searchWarehouseDialogForm.warehouseCode;
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
	 * {@link WarehouseService}クラスのインスタンスを返します.
	 *
	 * @return {@link WarehouseService}
	 */
	@Override
	protected MasterSearch<WarehouseJoin> getService() {
		return this.warehouseService;
	}

	/**
	 * 何も行いません.
	 */
	@Override
	protected void createList() throws ServiceException {
	}
}
