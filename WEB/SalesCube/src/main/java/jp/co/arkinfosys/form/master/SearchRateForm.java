/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import jp.co.arkinfosys.dto.master.RateDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

/**
 * レート画面（検索）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchRateForm extends AbstractSearchForm<RateDto> {

	/** レートタイプ名称 */
	public String name;

	/** レートタイプ備考 */
	public String remarks;

	/** 最新レート適用開始日（開始） */
	public String startDate1;

	/** 最新レート適用開始日（終了） */
	public String startDate2;

}
