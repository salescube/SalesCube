
/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.bill;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.bill.CloseBillLineDto;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.bill.CloseBillForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;

/**
 * 請求締処理画面の検索実行アクションクラスです。
 *
 * @author Ark Information Systems
 *
 */
public class SearchCloseBillResultAjaxAction extends
		AbstractSearchResultAjaxAction<CloseBillLineDto, CustomerJoin> {

	@ActionForm
	@Resource
	public CloseBillForm closeBillForm;

	@Resource
	private CustomerService customerService;

	@Resource
	private CategoryService categoryService;

	/**
	 * 検索実行前に、検索条件の初期値を設定します.<br>
	 * 前回検索結果がある場合には、前回処理条件を初期値として設定します.<br>
	 * @throws Exception
	 */
	@Override
	protected void doBeforeSearch() throws Exception {
		List<CustomerJoin> cashList = customerService.findCustomerForCloseBill(
				false, null, null, null, null, false);
		if (cashList.size() == 0) {
			closeBillForm.otherUser.initialize();
		} else {
			closeBillForm.otherUser.initialize(cashList.get(0));
			closeBillForm.lastCutOffDate = closeBillForm.otherUser.billCutoffDate;
		}

		// 支払条件の分割
		devideCutoffGroupCategory();
	}

	/**
	 * 検索結果数を返します.<br>
	 * 請求締処理ではこのメソッドを使用しないため、常に0を返します.
	 * @param params 検索条件
	 * @return 検索結果数0固定
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return 0;
	}

	/**
	 * 検索を実行します.<br>
	 * @param params 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 表示数
	 * @param offset 表示ページ数
	 * @return 検索結果リスト CustomerJoin形式
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchResultAction#execSearch(org.seasar.framework.beans.util.BeanMap, java.lang.String, boolean, int, int)
	 * @see jp.co.arkinfosys.service.CustomerService#findCustomerForCloseBill
	 */
	@Override
	protected List<CustomerJoin> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return customerService
				.findCustomerForCloseBill(
						true,
						closeBillForm.customerCode,
						closeBillForm.customerName,
						this.categoryService
								.cutoffGroupCategoryToCutoffGroup(closeBillForm.cutoffGroupCategory),
						this.categoryService
								.cutoffGroupCategoryToPaybackCycleCategory(closeBillForm.cutoffGroupCategory),
						closeBillForm.notYetRequestedCheck);
	}

	/**
	 * 検索結果の形式を変換します.<br>
	 * @param entityList CustomerJoin形式の検索結果リスト
	 * @return 検索結果リスト　CloseBillLineDto形式
	 * @throws Exception
	 */
	@Override
	protected List<CloseBillLineDto> exchange(List<CustomerJoin> entityList)
			throws Exception {
		List<CloseBillLineDto> dtoList = new ArrayList<CloseBillLineDto>();
		for (CustomerJoin entity : entityList) {
			CloseBillLineDto dto = new CloseBillLineDto();
			dto.initialize(entity);
			dtoList.add(dto);
		}
		return dtoList;
	}

	private void devideCutoffGroupCategory() {
		if (StringUtil.hasLength(closeBillForm.cutoffGroupCategory) == true) {
			closeBillForm.cutoffGroup = closeBillForm.cutoffGroupCategory
					.substring(0, 2);
			closeBillForm.paybackCycleCategory = closeBillForm.cutoffGroupCategory
					.substring(2, 3);
		} else {
			closeBillForm.cutoffGroup = "";
			closeBillForm.paybackCycleCategory = "";
		}
	}

	/**
	 * 請求締処理アクションで使用するアクションフォームを返します.<br>
	 * @return 請求締処理用アクションフォーム
	 * @see jp.co.arkinfosys.form.bill.CloseBillForm
	 */
	@Override
	protected AbstractSearchForm<CloseBillLineDto> getActionForm() {
		return this.closeBillForm;
	}

	/**
	 * 請求締処理アクションで使用するDTOクラスを返します.<br>
	 * @return 請求締処理用DTO
	 * @see jp.co.arkinfosys.dto.bill.CloseBillLineDto
	 */
	@Override
	protected Class<CloseBillLineDto> getDtoClass() {
		return CloseBillLineDto.class;
	}

	/**
	 * 請求締処理後の遷移URIを返します.<br>
	 * @return 請求締処理後の遷移URI
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 請求締処理画面のメニューIDを返します.<br>
	 * @return 請求締処理画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.CLOSE_BILL;
	}

	/**
	 * 請求締処理画面では検索サービスを返す必要が無いのでnullを返します.<br>
	 * @return null固定
	 */
	@Override
	protected MasterSearch<CustomerJoin> getService() {
		return null;
	}

}
