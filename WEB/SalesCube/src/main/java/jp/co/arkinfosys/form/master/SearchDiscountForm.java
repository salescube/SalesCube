/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.List;

import jp.co.arkinfosys.dto.master.DiscountDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 数量割引画面（検索）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class SearchDiscountForm extends AbstractSearchForm<DiscountDto> {

	/** 割引コード */
	public String discountId;

	/** 割引名 */
	public String discountName;

	/** 割引有効 */
	public String useFlag;

	/** 備考 */
	public String remarks;

	/** 割引有効プルダウンリスト */
	public List<LabelValueBean> useFlagList;

}
