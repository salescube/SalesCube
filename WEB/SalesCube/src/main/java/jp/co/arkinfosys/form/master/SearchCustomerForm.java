/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.master.CustomerDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 顧客画面（検索）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchCustomerForm extends AbstractSearchForm<CustomerDto> {

	/** 顧客コード */
	public String customerCode;

	/** 顧客名 */
	public String customerName;

	/** 顧客名カナ */
	public String customerKana;

	/** 事業所名 */
	public String customerOfficeName;

	/** 事業所名カナ */
	public String customerOfficeKana;

	/** 担当者名 */
	public String customerPcName;

	/** 担当者カナ */
	public String customerPcKana;

	/** TEL */
	public String customerTel;

	/** FAX */
	public String customerFax;

	/** 顧客ランク */
	public String customerRankCategory;

	/** 支払条件 */
	public String cutoffGroup;

	/** 振込名義 */
	public String paymentName;

	/** 備考 */
	public String remarks;

	/** 顧客ランクリスト */
	public List<LabelValueBean> customerRankList = new ArrayList<LabelValueBean>();

	/** 支払条件リスト */
	public List<LabelValueBean> cutoffGroupList = new ArrayList<LabelValueBean>();

}
