/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.sales;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.sales.OutputInvoiceSearchResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.sales.OutputInvoiceForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.sales.SearchOutputInvoiceService;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 送り状データ出力処理を実行するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputInvoiceAction extends
		AbstractSearchAction<OutputInvoiceSearchResultDto> {

	@ActionForm
	@Resource
	public OutputInvoiceForm outputInvoiceForm;

	/** 区分マスタサービス */
	@Resource
	public CategoryService categoryService;

	/**
	 * {@link AbstractSearchAction#index()}の初期化後に必要な処理を行います.<br>
	 * アクションフォームの初期値を設定します.<br>
	 * @throws Exception
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.outputInvoiceForm.excludingOutput = true;
		this.outputInvoiceForm.sortColumn = SearchOutputInvoiceService.Param.SI_PRINT_COUNT;
		this.outputInvoiceForm.sortOrderAsc = true;
		this.outputInvoiceForm.dcCategory = CategoryTrns.DC_CATEGORY_1;
	}

	/**
	 * プルダウンの要素を作成します.<BR>
	 * 1.配送業者区分リストを作成します.<BR>
	 * 2.売上取引区分リストを作成します.<BR>
	 * 3.売上取引区分リストの先頭に空欄の選択肢を追加します.<BR>
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		this.outputInvoiceForm.dcCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_CATEGORY);
		this.outputInvoiceForm.salesCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.SALES_CM_CATEGORY);
		this.outputInvoiceForm.salesCategoryList.add(0, new LabelValueBean("",
				""));

	}

	/**
	 * 送り状データ出力用アクションフォームを返します.
	 * @return OutputInvoiceForm形式のアクションフォーム
	 */
	@Override
	protected AbstractSearchForm<OutputInvoiceSearchResultDto> getActionForm() {
		return this.outputInvoiceForm;
	}

	/**
	 * 送り状データ出力画面のメニューIDを返します.
	 * @return 送り状データ出力画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.OUTPUT_SALES_INVOICE;
	}
}
