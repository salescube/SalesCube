/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action;

import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.Execute;

/**
 * 検索画面初期表示の基底アクションクラスです.
 * @author Ark Information Systems
 *
 * @param <DTOCLASS>
 */
public abstract class AbstractSearchAction<DTOCLASS> extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		/**
		 * 検索画面JSPへのパス
		 */
		public static final String SEARCH = "search.jsp";
	}

	/**
	 * 初期表示処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. 前処理<br>
	 * 2. 検索、入力画面の権限をアクションフォームに設定<br>
	 * 3. 後処理
	 * </p>
	 * 処理実行後、{@link AbstractSearchAction#getInputURIString()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		try {
			this.doBeforeIndex();

			this.createList();

			AbstractSearchForm<DTOCLASS> form = this.getActionForm();

			// 検索画面の権限を取得する
			form.isUpdate = super.userDto.isMenuUpdate(this.getSearchMenuID());

			// 入力画面の表示権限を取得する
			form.isInputValid = super.userDto
					.isMenuValid(this.getInputMenuID());

			this.doAfterIndex();
		} catch (Exception e) {
			super.errorLog(e);
			throw e;
		}

		return this.getInputURIString();
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return 検索画面メニューID
	 */
	protected abstract String getSearchMenuID();

	/**
	 * 入力画面のメニューIDを返します.<br>
	 * デフォルト実装ではnullを返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @return 入力画面メニューID
	 */
	protected String getInputMenuID() {
		return null;
	}

	/**
	 * プルダウンの要素を作成します.
	 * @throws ServiceException
	 */
	protected abstract void createList() throws ServiceException;

	/**
	 * {@link AbstractSearchAction#index()}の初期化前に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @throws Exception
	 */
	protected void doBeforeIndex() throws Exception {
	}

	/**
	 * {@link AbstractSearchAction#index()}の初期化後に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @throws Exception
	 */
	protected void doAfterIndex() throws Exception {
	}

	/**
	 * 画面遷移先のURI文字列を返します.<br>
	 * デフォルト実装では{@link Mapping#SEARCH}を返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @return 画面遷移先のURI文字列
	 */
	protected String getInputURIString() {
		return Mapping.SEARCH;
	}

	/**
	 * アクションフォームを返します.
	 * @return アクションフォーム
	 */
	protected abstract AbstractSearchForm<DTOCLASS> getActionForm();

}
