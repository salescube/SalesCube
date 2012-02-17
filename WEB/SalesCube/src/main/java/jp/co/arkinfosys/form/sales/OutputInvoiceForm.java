/*
 *  Copyright 2009-2010 Ark Information Systems.
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
	public String salesDate;

	public boolean excludingOutput;

	public String salesCmCategory;

	public String dcCategory;

	public List<LabelValueBean> dcCategoryList;

	public List<LabelValueBean> salesCategoryList;

	
	public List<String> salesIdList = new ArrayList<String>();

	
	public List<OutputInvoiceSearchResultDto> allSearchResultList = null;

}
