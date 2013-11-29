/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.arkinfosys.dto.DomainDto;
import jp.co.arkinfosys.dto.UserDto;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;

/**
 * アクションクラスのメソッド実行時に行うログインチェック処理の基底クラスです.
 *
 * @author Ark Information Systems
 *
 */
public abstract class AbstractLoginCheckInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	protected static class Mapping {
		public static final String LOGIN = "/login";
	}

	public static final String LOGIN_CHECK_MESSAGES_KEY = "login.check.messages";

	protected static final String UNAUTHORIZED_ERROR_MESSAGE = "errors.login.unauthorized";

	/**
	 * ロガー
	 */
	protected Logger logger = Logger
			.getLogger(AbstractLoginCheckInterceptor.class);

	/**
	 * ドメイン情報DTO
	 */
	@Resource
	protected DomainDto domainDto;

	/**
	 * ユーザー情報DTO
	 */
	@Resource
	protected UserDto userDto;

	/**
	 * リクエスト
	 */
	@Resource
	protected HttpServletRequest httpRequest;

	/**
	 * レスポンス
	 */
	@Resource
	protected HttpServletResponse httpResponse;

	/**
	 * ログイン確認を行います.
	 * @param invocation MethodInvocationオブジェクト
	 * @return 遷移先URI
	 * @throws Throwable
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// ログイン確認
		if (!this.isLogin()) {
			this.doAfterError(invocation);
			return this.getErrorURIString();
		}

		// ユーザーDTOに最終実行処理を記録
		this.recordRequestFunc(invocation);

		// メソッド実行前ログ出力
		this.logBeforeInvoke(invocation);

		Object result = invocation.proceed();

		// メソッド実行後ログ出力
		this.logBeforeInvoke(invocation);

		return result;
	}

	/**
	 * ログイン有無を確認するデフォルト実装です.
	 *
	 * @return ログイン済みか否か
	 */
	protected boolean isLogin() {
		if (this.userDto == null) {
			return false;
		}
		if (this.userDto.userId == null) {
			return false;
		}
		return true;
	}

	/**
	 * 未ログイン時に実施する処理を記述する抽象メソッドです.
	 * @param invocation MethodInvocationオブジェクト
	 * @throws Exception
	 */
	protected abstract void doAfterError(MethodInvocation invocation)
			throws Exception;

	/**
	 * ユーザーの最終実行処理を記録するデフォルトの実装です.
	 * @param invocation MethodInvocationオブジェクト
	 * @throws Exception
	 */
	protected void recordRequestFunc(MethodInvocation invocation)
			throws Exception {
		this.userDto.lastRequestFunc = this.getTargetClass(invocation)
				.getName()
				+ "#" + invocation.getMethod().getName() + "()";
	}

	/**
	 * メソッド実行をログ出力するデフォルトの実装です.
	 * @param invocation MethodInvocationオブジェクト
	 * @throws Exception
	 */
	protected void logBeforeInvoke(MethodInvocation invocation)
			throws Exception {
		this.logger.info("[" + domainDto.domainId + "] BEGIN "
				+ this.userDto.lastRequestFunc);
	}

	/**
	 * メソッド実行をログ出力するデフォルトの実装です.
	 * @param invocation MethodInvocationオブジェクト
	 * @throws Exception
	 */
	protected void logAfterInvoke(MethodInvocation invocation) throws Exception {
		logger.info("[" + domainDto.domainId + "] END   "
				+ this.userDto.lastRequestFunc);
	}

	/**
	 * 未ログイン時に遷移するURI文字列を返す抽象メソッドです.
	 *
	 * @return 遷移先URI
	 */
	protected abstract String getErrorURIString();

	/**
	 * ドメインIDを返します.
	 *
	 * @return ドメインID
	 */
	protected String getDomainId() {
		if (this.domainDto != null && this.domainDto.domainId != null) {
			return this.domainDto.domainId;
		}
		return this.httpRequest.getParameter("domainId");
	}
}
