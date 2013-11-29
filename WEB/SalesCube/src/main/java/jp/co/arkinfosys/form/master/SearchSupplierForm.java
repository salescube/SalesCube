/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import jp.co.arkinfosys.dto.master.SupplierDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

/**
 * 仕入先画面（検索）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchSupplierForm extends AbstractSearchForm<SupplierDto> {

	/** 仕入先コード */
	public String supplierCode;

	/** 仕入先名 */
	public String supplierName;

	/** 仕入先名カナ */
	public String supplierKana;

	/** 備考 */
	public String remarks;

}
