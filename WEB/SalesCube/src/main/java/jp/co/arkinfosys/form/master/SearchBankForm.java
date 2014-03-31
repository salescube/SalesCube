/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.master.BankDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 銀行画面（検索）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchBankForm extends AbstractSearchForm<BankDto> {

	/** 銀行コード */
	public String bankCode;

	/** 銀行名 */
	public String bankName;

	/** 店番 */
	public String storeCode;

	/** 店名 */
	public String storeName;

	/** 科目 */
	public String dwbType;

	/** 口座番号 */
	public String accountNum;

	/** 口座名義 */
	public String accountOwnerName;

	/** 口座名義カナ */
	public String accountOwnerKana;

	/** 科目プルダウンリスト */
	public List<LabelValueBean> dwbTypeList = new ArrayList<LabelValueBean>();

}
