/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import jp.co.arkinfosys.action.AbstractSearchResultAction;

import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * Ajaxにおける検索処理の基底アクションクラスです.
 *
 * @author Ark Information Systems
 *
 * @param <DTOCLASS>
 * @param <ENTITY>
 */
public abstract class AbstractSearchResultAjaxAction<DTOCLASS, ENTITY> extends
		AbstractSearchResultAction<DTOCLASS, ENTITY> {

	/**
	 * 画面遷移用のマッピングを定義するクラスです
	 */
	public static class Mapping extends AbstractSearchResultAction.Mapping {
		/**
		 * Ajax処理中のエラー時に遷移するJSPパスです
		 */
		public static final String ERROR_JSP = "/ajax/errorResponse.jsp";
	}

	/**
	 * プロパティリソースにおけるシステムエラーメッセージのキー値です。
	 */
	protected static final String SYSTEM_ERROR_MESSAGE = "errors.system.ajax";

	/**
	 * 検索実行メソッドです.
	 *
	 * @return 遷移URI
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = AbstractSearchResultAjaxAction.Mapping.ERROR_JSP)
	public String search() throws Exception {
		return super.doSearch();
	}

	/**
	 * エラー発生後の処理を行います.<BR>
	 * デフォルト実装ではレスポンスにエラーコード500と、エラーメッセージをセットします.<BR>
	 * 必要に応じてオーバーライドしてください.
	 * @param e 発生したException
	 * @throws Exception
	 */
	@Override
	protected void doAfterError(Exception e) throws Exception {
		this.httpResponse.setContentType("text/plain");
		this.httpResponse.setCharacterEncoding("UTF-8");
		this.httpResponse
				.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try {
			this.httpResponse
					.getWriter()
					.write(
							MessageResourcesUtil
									.getMessage(CommonAjaxResources.SYSTEM_ERROR_MESSAGE));
		} catch (IOException ioe) {
			super.errorLog(ioe);
		}
	}
}
