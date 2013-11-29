/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.List;

import jp.co.arkinfosys.dto.master.TaxRateDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 税画面（検索）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchTaxRateForm extends AbstractSearchForm<TaxRateDto> {

	public List<LabelValueBean> categoryList;

}
