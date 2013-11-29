/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import jp.co.arkinfosys.dto.master.SupplierDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

/**
 *  仕入先検索ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchSupplierDialogForm extends AbstractSearchForm<SupplierDto> {

	public String dialogId;

	public String supplierCode;

	public String supplierName;

	public String supplierKana;

}
