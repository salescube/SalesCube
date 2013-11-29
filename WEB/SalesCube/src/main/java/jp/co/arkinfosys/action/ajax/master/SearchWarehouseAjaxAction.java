package jp.co.arkinfosys.action.ajax.master;

/*
 * Copyright 2009-2010 Ark Information Systems.
 */
import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.entity.join.WarehouseJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchWarehouseForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.WarehouseService;

import org.seasar.struts.annotation.ActionForm;

public class SearchWarehouseAjaxAction extends
		AbstractSearchResultAjaxAction<WarehouseDto, WarehouseJoin> {

	@ActionForm
	@Resource
	public SearchWarehouseForm searchWarehouseForm;

	@Resource
	public WarehouseService warehouseService;

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 倉庫マスタDTOを返します.
	 * @return {@link WarehouseDto}
	 */
	@Override
	protected Class<WarehouseDto> getDtoClass() {
		return WarehouseDto.class;
	}


	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchWarehouseForm}
	 */
	@Override
	protected AbstractSearchForm<WarehouseDto> getActionForm() {
		return searchWarehouseForm;
	}

	/**
	 * 検索処理を行う倉庫マスタサービスを返します.
	 * @return {@link WarehouseService}
	 */
	@Override
	protected MasterSearch<WarehouseJoin> getService() {
		return warehouseService;
	}
	
	/**
	 * 検索画面のメニューIDを返します.
	 * @return 倉庫マスタ画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_WAREHOUSE;
	}
	
}
