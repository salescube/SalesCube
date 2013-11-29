/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import jp.co.arkinfosys.dto.ZipDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

/**
 * 郵便番号検索ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchZipCodeDialogForm extends AbstractSearchForm<ZipDto> {

	public String dialogId;

	public String zipCode;

	public String zipAddress1;

}
