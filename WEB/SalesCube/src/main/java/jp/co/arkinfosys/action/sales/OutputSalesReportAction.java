/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.sales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.sales.OutputSalesSearchResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.sales.OutputSalesReportForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;

/**
 * 売上帳票発行処理を実行するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */

public class OutputSalesReportAction extends
		AbstractSearchAction<OutputSalesSearchResultDto> {

	@ActionForm
	@Resource
	public OutputSalesReportForm outputSalesReportForm;

	/** 区分マスタサービス */
	@Resource
	public CategoryService categoryService;

	/**
	 * 帳票選択プルダウンのリストを返します.<BR>
	 *
	 * @return LabelValueBeanのリスト
	 */
	public List<LabelValueBean> getReportSelectionList() {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();

		// 全ての帳票
		list.add(new LabelValueBean(Constants.REPORT_SELECTION.LABEL_ALL,
				Constants.REPORT_SELECTION.VALUE_ALL));
		// 見積書
		list.add(new LabelValueBean(Constants.REPORT_SELECTION.LABEL_ESTIMATE,
				Constants.REPORT_SELECTION.VALUE_ESTIMATE));
		// 請求書
		list.add(new LabelValueBean(Constants.REPORT_SELECTION.LABEL_BILL,
				Constants.REPORT_SELECTION.VALUE_BILL));
		// 納品書
		list.add(new LabelValueBean(Constants.REPORT_SELECTION.LABEL_DELIVERY,
				Constants.REPORT_SELECTION.VALUE_DELIVERY));
		// 仮納品書
		list.add(new LabelValueBean(
				Constants.REPORT_SELECTION.LABEL_TEMP_DELIVERY,
				Constants.REPORT_SELECTION.VALUE_TEMP_DELIVERY));
		// ピッキングリスト
		list.add(new LabelValueBean(Constants.REPORT_SELECTION.LABEL_PICKING,
				Constants.REPORT_SELECTION.VALUE_PICKING));
		// 納品書兼領収書
		list.add(new LabelValueBean(
				Constants.REPORT_SELECTION.LABEL_DELIVERY_RECEIPT,
				Constants.REPORT_SELECTION.VALUE_DELIVERY_RECEIPT));

		return list;
	}

	/**
	 * 状態選択プルダウンのリストを返します.<BR>
	 *
	 * @return LabelValueBeanのリスト
	 */
	public List<LabelValueBean> getStatusSelectionList() {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();

		// 全て
		list.add(new LabelValueBean(Constants.STATUS_SELECTION.LABEL_ALL,
				Constants.STATUS_SELECTION.VALUE_ALL));
		// 未出力
		list.add(new LabelValueBean(Constants.STATUS_SELECTION.LABEL_UNOUTPUT,
				Constants.STATUS_SELECTION.VALUE_UNOUTPUT));
		// 出力済
		list.add(new LabelValueBean(Constants.STATUS_SELECTION.LABEL_OUTPUT,
				Constants.STATUS_SELECTION.VALUE_OUTPUT));

		return list;
	}

	/**
	 * プルダウンの要素を作成します.<BR>
	 * 1.帳票選択リストを作成します.<BR>
	 * 2.状態選択リストを作成します.<BR>
	 * 3.売上取引区分リストを作成します.<BR>
	 * 4.全て出力済を除くをONにします.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException {
		// 帳票選択プルダウン初期化
		this.outputSalesReportForm.reportSelectionList = getReportSelectionList();

		// 状態選択プルダウン初期化
		this.outputSalesReportForm.statusSelectionList = getStatusSelectionList();

		this.outputSalesReportForm.salesCategoryList = this.categoryService
				.findCategoryLabelValueBeanListById(Categories.SALES_CM_CATEGORY);

		this.outputSalesReportForm.excludingOutputAll = true;
	}

	/**
	 * アクションフォームを返します.
	 * @return OutputSalesReportForm形式のアクションフォーム
	 */
	@Override
	protected AbstractSearchForm<OutputSalesSearchResultDto> getActionForm() {
		return this.outputSalesReportForm;
	}

	/**
	 * 売上帳票発行画面のメニューIDを返します.
	 * @return 売上帳票発行画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.OUTPUT_SALES_REPORT;
	}

}
