/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * 画面リフレッシュタイプのサブミットによるリクエストに対してログインチェックを行うインターセプタです.
 *
 * @author Ark Information Systems
 *
 */
public class ActionMethodInvocationInterceptor extends
		AbstractLoginCheckInterceptor {

	private static final long serialVersionUID = 1L;

	/**
	 * エラーメッセージを設定します.
	 * @param invocation MethodInvocationオブジェクト
	 * @throws Exception
	 */
	@Override
	protected void doAfterError(MethodInvocation invocation) throws Exception {
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				UNAUTHORIZED_ERROR_MESSAGE));
		super.httpRequest.setAttribute(LOGIN_CHECK_MESSAGES_KEY, errors);
	}

	/**
	 * 遷移先URIを返します.
	 * @return 遷移先URI
	 */
	@Override
	protected String getErrorURIString() {
		return Mapping.LOGIN + "/" + super.getDomainId();
	}

}
