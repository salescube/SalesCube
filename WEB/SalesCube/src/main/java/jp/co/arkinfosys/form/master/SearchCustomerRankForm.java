/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.List;

import jp.co.arkinfosys.dto.master.CustomerRankDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.FloatType;

/**
 * 顧客ランク画面（検索）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class SearchCustomerRankForm extends AbstractSearchForm<CustomerRankDto> {

	/** 顧客ランクコード */
	public String rankCode;

	/** 顧客ランク名 */
	public String rankName;

	/** 値引率1 */
	@FloatType
	public String rankRate1;

	/** 値引率2 */
	@FloatType
	public String rankRate2;

	/** 送料区分 */
	public String postageType;

	/** EXCELで使用するラベル */
	public String labelSales6;
	public String labelSales5;
	public String labelSales4;
	public String labelSales3;
	public String labelSales2;
	public String labelSales1;

	/** 送料区分プルダウンリスト */
	public List<LabelValueBean> postageTypeList;
}
