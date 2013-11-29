/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.porder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.porder.POrderSlipLineJoinDto;
import jp.co.arkinfosys.entity.join.POrderSlipLineJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.porder.MakeOutPOrderForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.porder.MakeOutPOrderService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 * 発注書発行画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 *
 */
public class MakeOutPOrderAjaxAction
		extends
		AbstractSearchResultAjaxAction<POrderSlipLineJoinDto, POrderSlipLineJoin> {

	@ActionForm
	@Resource
	protected MakeOutPOrderForm makeOutPOrderForm;

	@Resource
	private MakeOutPOrderService makeOutPOrderService;

	/**
	 * 検索条件を受け取って検索結果件数を返します.<br>
	 * 未使用です.
	 * @param params 検索条件
	 * @return 0
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return 0;
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
	protected List<POrderSlipLineJoin> execSearch(BeanMap params,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return this.makeOutPOrderService.findPOrderSlipByCondition(params);
	}

	/**
	 * 検索結果の変換を行います.
	 * @param entityList 変換前検索結果リスト(Entity)
	 * @return 変換された検索結果のリスト(DTO)
	 * @throws Exception
	 */
	@Override
	protected List<POrderSlipLineJoinDto> exchange(
			List<POrderSlipLineJoin> entityList) throws Exception {
		List<POrderSlipLineJoinDto> dtoList = new ArrayList<POrderSlipLineJoinDto>();
		for (POrderSlipLineJoin porderSlipLineJoin : entityList) {
			POrderSlipLineJoinDto porderSlipLineJoinDto = Beans.createAndCopy(
					POrderSlipLineJoinDto.class, porderSlipLineJoin).execute();
			dtoList.add(porderSlipLineJoinDto);
		}
		return dtoList;
	}

	/**
	 * 検索結果のページングを行います.
	 * @throws Exception
	 */
	@Override
	protected void doAfterSearch() throws Exception {
		List<POrderSlipLineJoinDto> allSearchResultList = this.makeOutPOrderForm.searchResultList;
		this.makeOutPOrderForm.searchResultCount = allSearchResultList.size();

		int pageNo = this.makeOutPOrderForm.pageNo;
		int rowCount = this.makeOutPOrderForm.rowCount;
		int offset = rowCount * (pageNo - 1);

		int maxRow = offset + rowCount;
		if (maxRow < 0 || maxRow > allSearchResultList.size()) {
			maxRow = allSearchResultList.size();
		}

		this.makeOutPOrderForm.allSearchResultList = allSearchResultList;
		this.makeOutPOrderForm.searchResultList = new ArrayList<POrderSlipLineJoinDto>(
				this.makeOutPOrderForm.allSearchResultList.subList(offset,
						maxRow));
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link MakeOutPOrderForm}
	 */
	@Override
	protected AbstractSearchForm<POrderSlipLineJoinDto> getActionForm() {
		return this.makeOutPOrderForm;
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
	 * @return 発注書発行画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MAKE_OUT_PORDER;
	}

	/**
	 * 入力画面のメニューIDを返します.
	 * @return 発注入力画面のメニューID
	 */
	@Override
	protected String getInputMenuID() {
		return Constants.MENU_ID.INPUT_PORDER;
	}

	/**
	 * 検索で使用するサービスを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected MasterSearch<POrderSlipLineJoin> getService() {
		return null;
	}

	/**
	 * 検索で使用するDTOを返します.<br>
	 * 未使用です.
	 * @return null
	 */
	@Override
	protected Class<POrderSlipLineJoinDto> getDtoClass() {
		return null;
	}

}
