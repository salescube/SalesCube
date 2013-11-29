/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action;

import org.seasar.struts.annotation.Execute;

/**
 * ログアウト処理アクションクラスです.
 * @author Ark Information Systems
 *
 */
public class LogoutAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String SUCCESS = "/login";
	}

	/**
	 * ログアウト処理を行います.<br>
	 * 処理実行後、{@link Mapping#SUCCESS}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String logout() throws Exception {
		// セッション情報を全て破棄する
		super.httpSession.invalidate();

		return LogoutAction.Mapping.SUCCESS + "/" + super.domainDto.domainId;
	}
}
