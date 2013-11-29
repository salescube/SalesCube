/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.rorder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.rorder.OnlineOrderWorkRelDto;
import jp.co.arkinfosys.entity.join.OnlineOrderRelJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.rorder.ImportOnlineOrderForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.OnlineOrderService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * オンライン受注データ取込画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 *
 */
public class ImportOnlineOrderResultAjaxAction
		extends
		AbstractSearchResultAjaxAction<OnlineOrderWorkRelDto, OnlineOrderRelJoin> {

	@ActionForm
	@Resource
	private ImportOnlineOrderForm importOnlineOrderForm;

	@Resource
	private OnlineOrderService onlineOrderService;

	/**
	 * 検索結果をアクションフォームにセットして検索結果jspのURIを返します.
	 * @return 検索結果jspのURI
	 * @throws Exception
	 */
	@Override
	@Execute(validator = false)
	public String search() throws Exception {
		return super.doSearch();
	}

	/**
	 * 検索条件を受け取って検索結果件数を返します.
	 * @param params 検索条件
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return this.onlineOrderService.findRoWorkRelCnt();
	}

	/**
	 * 検索条件を受け取って検索結果のリストを返します.
	 * @param params 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 取得件数(LIMIT)
	 * @param offset 取得開始位置(OFFSET)
	 * @return 検索結果リスト
	 * @throws ServiceException
	 */
	@Override
	protected List<OnlineOrderRelJoin> execSearch(BeanMap params,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return this.onlineOrderService.findRoWorkRel(null, null, sortColumn,
				sortOrderAsc);
	}

	/**
	 * 検索結果の変換を行います.
	 * @param entityList 変換前検索結果リスト(Entity)
	 * @return 変換された検索結果のリスト(DTO)
	 * @throws Exception
	 */
	@Override
	protected List<OnlineOrderWorkRelDto> exchange(
			List<OnlineOrderRelJoin> entityList) throws Exception {
		List<OnlineOrderWorkRelDto> dtoList = new ArrayList<OnlineOrderWorkRelDto>();
		for (OnlineOrderRelJoin onlineOrderRel : entityList) {
			dtoList.add(Beans.createAndCopy(OnlineOrderWorkRelDto.class,
					onlineOrderRel).execute());
		}
		return dtoList;
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link ImportOnlineOrderForm}
	 */
	@Override
	protected AbstractSearchForm<OnlineOrderWorkRelDto> getActionForm() {
		return this.importOnlineOrderForm;
	}

	/**
	 * 検索で使用するDTOクラスを返します.<br>
	 * @return {@link OnlineOrderWorkRelDto OnlineOrderWorkRelDto}クラス
	 */
	@Override
	protected Class<OnlineOrderWorkRelDto> getDtoClass() {
		return OnlineOrderWorkRelDto.class;
	}

	/**
	 * 検索結果jspのURIを返します.
	 * @return 検索結果jspのURI
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return オンライン受注データ取込画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.IMPORT_ONLINE_ORDER;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 受注入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_RORDER;
	}

	/**
	 * 検索で使用するサービスを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected MasterSearch<OnlineOrderRelJoin> getService() {
		return null;
	}

}
