/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.entity.join.WarehouseJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchRackForm;
import jp.co.arkinfosys.form.master.SearchWarehouseForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.WarehouseService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 倉庫検索結果Excelダウンロードのアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchWarehouseResultOutputAction extends
		AbstractSearchResultAjaxAction<WarehouseDto, WarehouseJoin> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String EXCEL = "excel.jsp";
	}

	@ActionForm
	@Resource
	public SearchWarehouseForm searchWarehouseForm;

	@Resource
	public WarehouseService warehouseService;

	/**
	 * 処理終了後、{@link SearchRackResultOutputAction#excel()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excel() throws Exception {
		return super.doSearch();
	}

	/**
	 * 検索を実行します.
	 * @param params パラメータを設定したマップ
	 * @param sortColumn ソート対象カラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得する検索件数
	 * @param offset 取得開始位置
	 * @return {@link RackDto}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#execSearch(org.seasar.framework.beans.util.BeanMap, java.lang.String, boolean, int, int)
	 */
	protected List<WarehouseJoin> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return this.getService().findByCondition(params, sortColumn,
				sortOrderAsc);
	}

	@Override
	protected void doAfterSearch() throws Exception {
		return;
	}

	/**
	 *
	 * @return {@link SearchRackForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<WarehouseDto> getActionForm() {
		return searchWarehouseForm;
	}

	/**
	 *
	 * @return {@link RackDto}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getDtoClass()
	 */
	@Override
	protected Class<WarehouseDto> getDtoClass() {
		return WarehouseDto.class;
	}

	/**
	 *
	 * @return {@link Mapping#EXCEL}で定義されたURI文字列
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getResultURIString()
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.EXCEL;
	}

	/**
	 *
	 * @return {@link RackService}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getService()
	 */
	@Override
	protected MasterSearch<WarehouseJoin> getService() {
		return warehouseService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return {@link MENU_ID#MASTER_WAREHOUSE}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_WAREHOUSE;
	}
}
