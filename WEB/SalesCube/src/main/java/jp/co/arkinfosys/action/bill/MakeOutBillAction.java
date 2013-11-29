/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.bill;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.bill.MakeOutBillForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 請求書発行画面の表示アクションクラスです
 *
 * @author Ark Information Systems
 *
 */
public class MakeOutBillAction extends
		AbstractSearchAction<MakeOutBillSearchResultDto> {

	@ActionForm
	@Resource
	public MakeOutBillForm makeOutBillForm;

	@Resource
	protected CategoryService categoryService;


	/**
	 * 請求書発行の初期値として発行済は除くのチェックをONにして、アクションフォームにセットします.<br>
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.makeOutBillForm.excludePrint = true;
	}

	/**
	 * 請求書発行画面で使用する支払条件の一覧を作成します.<br>
	 * 支払条件の先頭は空欄とします.
	 * @throws Exception
	 */
	@Override
	protected void createList() throws ServiceException {
		this.makeOutBillForm.cutoffGroupCategoryList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.CUTOFF_GROUP);
		this.makeOutBillForm.cutoffGroupCategoryList.add(0,
				new LabelValueBean());

	}

	/**
	 * 請求書発行アクションで使用するアクションフォームを返します.<br>
	 * @return 請求書発行用アクションフォーム
	 * @see jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto
	 */
	@Override
	protected AbstractSearchForm<MakeOutBillSearchResultDto> getActionForm() {
		return this.makeOutBillForm;
	}

	/**
	 * 請求書発行画面のメニューIDを返します.<br>
	 * @return 請求書発行画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MAKE_OUT_BILL;
	}

}
