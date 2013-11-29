/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * アクションクラスのメソッドから例外がスローされる場合にそのハンドリングを行うクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class GlobalExceptionHandler extends ExceptionHandler {

	/**
	 * Exceptionを取り扱います.<BR>
	 * (もしあれば) 呼ばれたExceptionHandlerによって返されるActionForwardインスタンスを返します.
	 *
	 * @param ex 例外オブジェクト
	 * @param ae 例外に対応するExceptionConfig
	 * @param mapping アクションマッピング
	 * @param formInstance アクションフォーム
	 * @param request サーブレットへのリクエスト
	 * @param response サーブレットのレスポンス
	 * @return ActionForwardインスタンス
	 * @throws ServletException
	 */
	@Override
	public ActionForward execute(Exception ex, ExceptionConfig ae,
			ActionMapping mapping, ActionForm formInstance,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		String message = ex.getCause() != null ? ex.getCause()
				.getMessage() : ex.getMessage();

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ae
				.getKey(), message));
		ActionMessagesUtil.addErrors(request, messages);

		return super.execute(ex, ae, mapping, formInstance, request, response);
	}

}
