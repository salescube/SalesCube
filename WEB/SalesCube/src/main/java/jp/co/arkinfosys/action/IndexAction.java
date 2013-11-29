/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action;

import org.seasar.struts.annotation.Execute;

/**
 * インデックスアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class IndexAction extends CommonResources {

	/**
	 * 初期表示処理を行います.<br>
	 * 処理終了後、"/login"に遷移します.
	 * @return 画面遷移先のURI文字列
	 */
	@Execute(validator = false)
	public String index() {
		return "/login";
	}
}
