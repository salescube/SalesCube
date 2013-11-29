/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import java.util.Locale;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.seasar.struts.util.ServletContextUtil;

/**
 * メッセージに関するユーティリティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class MessageResourcesUtil
// extends org.seasar.struts.util.MessageResourcesUtil
{
	/**
	 * コンストラクタです.
	 * @return
	 */
	private MessageResourcesUtil() {
	}

	/**
	 * メッセージリソースを返します.
	 * @return メッセージリソース
	 */
	public static MessageResources getMessageResources() {
		return (MessageResources) ServletContextUtil.getServletContext()
				.getAttribute(Globals.MESSAGES_KEY);
	}

	/**
	 * メッセージを返します.
	 * @param locale　ロケール
	 * @param key　キー
	 * @return メッセージ
	 */
	public static String getMessage(Locale locale, String key) {
		return getMessageResources().getMessage(locale, key);
	}

	/**
	 * メッセージを返します.
	 * @param key　キー
	 * @return メッセージ
	 */
	public static String getMessage(String key) {
		return getMessageResources().getMessage(key);
	}



	/**
	 * メッセージを返します.
	 * @param locale　ロケール
	 * @param key　キー
	 * @param args　引数の配列
	 * @return メッセージ
	 */
	public static String getMessage(Locale locale, String key, Object... args) {
		return getMessageResources().getMessage(locale, key, args);
	}

	/**
	 * メッセージを返します.
	 * @param key　キー
	 * @param args　引数の配列
	 * @return メッセージ
	 */
	public static String getMessage(String key, Object... args) {
		return getMessageResources().getMessage(key, args);
	}
}
