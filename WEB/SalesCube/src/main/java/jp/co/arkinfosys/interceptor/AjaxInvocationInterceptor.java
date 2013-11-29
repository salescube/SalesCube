/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.interceptor;

import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * Ajaxリクエストに対してログインチェックを行うインターセプタです.
 *
 * @author Ark Information Systems
 *
 */
public class AjaxInvocationInterceptor extends AbstractLoginCheckInterceptor {

	private static final long serialVersionUID = 1L;

	/**
	 * エラーメッセージを設定します.
	 * @param invocation　MethodInvocationオブジェクト
	 * @throws Exception
	 */
	@Override
	protected void doAfterError(MethodInvocation invocation) throws Exception {
		super.httpResponse.setContentType("text/plain");
		super.httpResponse.setCharacterEncoding("UTF-8");
		super.httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		super.httpResponse.getWriter().write(
				MessageResourcesUtil.getMessage(UNAUTHORIZED_ERROR_MESSAGE));
	}

	/**
	 * 遷移先URIを設定します.
	 * @return null
	 */
	@Override
	protected String getErrorURIString() {
		return null;
	}

}
