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
import jp.co.arkinfosys.entity.join.RackJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchRackForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 棚番検索結果Excelダウンロードのアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchRackResultOutputAction extends
		AbstractSearchResultAjaxAction<RackDto, RackJoin> {

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
	public SearchRackForm searchRackForm;

	@Resource
	public RackService rackService;

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
	 * @return {@link RackJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#execSearch(org.seasar.framework.beans.util.BeanMap, java.lang.String, boolean, int, int)
	 */
	protected List<RackJoin> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return this.getService().findByCondition(params, sortColumn,
				sortOrderAsc);
	}

	/**
	 * 棚に紐づく商品コードを設定します.
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#doAfterSearch()
	 */
	@Override
	protected void doAfterSearch() throws Exception {
		this.rackService
				.addProductInfoToRackDto(this.searchRackForm.searchResultList);
	}

	/**
	 *
	 * @return {@link SearchRackForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<RackDto> getActionForm() {
		return searchRackForm;
	}

	/**
	 *
	 * @return {@link RackDto}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getDtoClass()
	 */
	@Override
	protected Class<RackDto> getDtoClass() {
		return RackDto.class;
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
	protected MasterSearch<RackJoin> getService() {
		return rackService;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return {@link MENU_ID#MASTER_RACK}
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_RACK;
	}
}
