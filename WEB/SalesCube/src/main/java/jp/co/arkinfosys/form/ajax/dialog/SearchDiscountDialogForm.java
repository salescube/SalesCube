/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.master.DiscountDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 数量割引検索ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchDiscountDialogForm extends AbstractSearchForm<DiscountDto> {

	public String dialogId;

	public String discountId;

	public String discountName;

	public String useFlag;

	public String remarks;

	public List<LabelValueBean> useFlagList = new ArrayList<LabelValueBean>();

}
