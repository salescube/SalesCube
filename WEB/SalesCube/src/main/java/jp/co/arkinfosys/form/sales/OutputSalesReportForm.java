/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.sales;

import java.util.List;

import jp.co.arkinfosys.dto.sales.OutputSalesSearchResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntegerType;

/**
* 売上帳票発行画面のアクションフォームクラスです.
* @author Ark Information Systems
*
*/
public class OutputSalesReportForm extends
		AbstractSearchForm<OutputSalesSearchResultDto> {

	/**
	 * 売上日（開始）
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd", arg0 = @Arg(key = "labels.salesDate.start"))
	public String salesDateFrom;

	/**
	 * 売上日（終了）
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd", arg0 = @Arg(key = "labels.salesDate.end"))
	public String salesDateTo;

	/**
	 * 受注番号（開始）
	 */
	@IntegerType
	public String roSlipIdFrom;

	/**
	 * 受注番号（終了）
	 */
	@IntegerType
	public String roSlipIdTo;

	/**
	 * 売上番号（開始）
	 */
	@IntegerType
	public String salesSlipIdFrom;

	/**
	 * 売上番号（終了）
	 */
	@IntegerType
	public String salesSlipIdTo;

	/**
	 * 受付番号
	 */
	public String receptNo;

	/**
	 * 全て出力済を除く
	 */
	public boolean excludingOutputAll;

	/**
	 * 帳票選択
	 */
	public String reportSelection;

	/**
	 * 状態選択
	 */
	public String statusSelection;

	/**
	 * 取引区分
	 */
	public List<LabelValueBean> salesCategoryList;

	/**
	 * 帳票選択プルダウンリスト
	 */
	public List<LabelValueBean> reportSelectionList;

	/**
	 * 状態選択プルダウンリスト
	 */
	public List<LabelValueBean> statusSelectionList;

	/**
	 * 全ての検索結果リスト
	 */
	public List<OutputSalesSearchResultDto> allSearchResultList;

}
