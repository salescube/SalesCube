/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.sales;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.sales.OutputInvoiceSearchResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;

/**
 * 送り状発行画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputInvoiceForm extends
		AbstractSearchForm<OutputInvoiceSearchResultDto> {

	@DateType(datePatternStrict = Constants.FORMAT.DATE, arg0 = @Arg(key = "labels.salesDate"))
	public String salesDate;// 売上日

	public boolean excludingOutput;// 出力済を除く

	public String salesCmCategory;// 取引区分

	public String dcCategory;// 配送業者

	public List<LabelValueBean> dcCategoryList;

	public List<LabelValueBean> salesCategoryList;

	// 送り状出力対象の売上伝票番号
	public List<String> salesIdList = new ArrayList<String>();

	// 検索結果表示用
	public List<OutputInvoiceSearchResultDto> allSearchResultList = null;

}
