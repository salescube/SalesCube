/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import javax.annotation.Resource;

import jp.co.arkinfosys.dto.ZipDto;
import jp.co.arkinfosys.entity.Zip;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.ajax.dialog.SearchZipCodeDialogForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.ZipService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

/**
 * 郵便番号検索ダイアログの表示・検索処理アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchZipCodeDialogAction extends
		AbstractSearchDialogAction<ZipDto, Zip> {

	/**
	 * 郵便番号マスタに対するサービスクラスです.
	 */
	@Resource
	private ZipService zipService;

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public SearchZipCodeDialogForm searchZipCodeDialogForm;

	/**
	 * アクションフォームを返します.
	 *
	 * @return {@link SearchZipCodeDialogForm}
	 */
	@Override
	protected AbstractSearchForm<ZipDto> getActionForm() {
		return this.searchZipCodeDialogForm;
	}

	/**
	 * {@link ZipDto}クラスのクラスオブジェクトを返します.
	 *
	 * @return {@link ZipDto}クラス
	 */
	@Override
	protected Class<ZipDto> getDtoClass() {
		return ZipDto.class;
	}

	/**
	 * 検索キー値を返します.
	 *
	 * @return 郵便番号
	 */
	@Override
	protected String getId() {
		return this.searchZipCodeDialogForm.zipCode;
	}

	/**
	 * キー値での検索結果が0件だったことを通知するために、メッセージリソースのキーを返します.
	 *
	 * @return メッセージキー
	 */
	@Override
	protected String getMissingRecordMessageKey() {
		return null;
	}

	/**
	 * {@link ZipService}クラスのインスタンスを返します.
	 *
	 * @return {@link ZipService}
	 */
	@Override
	protected MasterSearch<Zip> getService() {
		return this.zipService;
	}

	/**
	 * 何も行いません.
	 */
	@Override
	protected void createList() throws ServiceException {
	}
}
