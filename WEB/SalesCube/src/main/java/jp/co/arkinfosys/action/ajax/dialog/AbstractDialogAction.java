/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import jp.co.arkinfosys.action.ajax.CommonAjaxResources;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.Execute;

/**
 * ダイアログを表示するアクションの基底クラスです.
 *
 * @author Ark Information Systems
 *
 */
public abstract class AbstractDialogAction extends CommonAjaxResources {

	/**
	 * 画面遷移用のマッピングを定義するクラスです.
	 */
	protected static class Mapping extends CommonAjaxResources.Mapping {
		/**
		 * ダイアログ本体のJSPパスです.
		 */
		public static final String DIALOG = "dialog.jsp";
	}

	/**
	 * ダイアログの初期表示処理です.
	 *
	 * @return ダイアログのJSPパス
	 * @throws Exception 例外発生時
	 */
	@Execute(validator = false, urlPattern = "{dialogId}")
	public String index() throws Exception {
		try {
			this.doBeforeIndex();

			this.createList();

			this.doAfterIndex();
		} catch (Exception e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();

			return null;
		}
		return Mapping.DIALOG;
	}

	/**
	 * 初期表示処理の開始直後に実行されるメソッドです.デフォルトでは何も行いません.
	 *
	 * @throws Exception
	 */
	protected void doBeforeIndex() throws Exception {
	}

	/**
	 * 初期表示処理の終了直前に実行されるメソッドです.デフォルトでは何も行いません.
	 *
	 * @throws Exception
	 */
	protected void doAfterIndex() throws Exception {
	}

	/**
	 * ダイアログの描画に必要なリスト項目の初期化を行うメソッドです.
	 *
	 * @throws ServiceException
	 */
	protected abstract void createList() throws ServiceException;

}
