/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import jp.co.arkinfosys.action.CommonResources;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * Ajax処理処理の基底アクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonAjaxResources extends CommonResources {

	/**
	 * 入力チェックエラーの結果遷移するJSPパス
	 */
	protected static class Mapping {
		public static final String ERROR_JSP = "/ajax/errorResponse.jsp";
	}

	/**
	 * application.resourcesにおけるシステムエラーメッセージのキー
	 */
	protected static final String SYSTEM_ERROR_MESSAGE = "errors.system.ajax";

	/**
	 * application.resourcesにおける検索結果件数オーバーメッセージのキー
	 */
	protected static final String SEARCH_THRESHOLD_OVER = "warns.search.thresholdover";

	/**
	 * srcとして渡されるオブジェクトをBeanMapに変換します.<br>
	 * その際null値は空文字列として変換されます.
	 *
	 * @param src 変換元オブジェクト
	 * @return BeanMap
	 */
	protected BeanMap createBeanMapWithNullToEmpty(Object src) {
		BeanMap lmap = Beans.createAndCopy(BeanMap.class, src)
				.execute();
		Set<Entry<String, Object>> entrySet = lmap.entrySet();
		Iterator<Entry<String, Object>> entryIte = entrySet.iterator();
		while (entryIte.hasNext()) {
			Map.Entry<String, Object> ent = entryIte.next();
			if (ent.getValue() == null) {
				ent.setValue("");
			}
		}
		return lmap;
	}

	/**
	 * Ajax処理用のエラーレスポンスオブジェクトを構築して返します.
	 */
	protected void writeSystemErrorToResponse() {
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
		} catch (IOException e) {
			super.errorLog(e);
		}
	}

	/**
	 * Ajax処理用のエラーレスポンスオブジェクトを構築して返します.(パラメータ不足)
	 */
	protected void writeErrorToResponse(String errorMessage) {
		this.httpResponse.setContentType("text/plain");
		this.httpResponse.setCharacterEncoding("UTF-8");
		this.httpResponse
				.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		try {
			this.httpResponse
					.getWriter()
					.write(errorMessage);
		} catch (IOException e) {
			super.errorLog(e);
		}
	}

	/**
	 * Ajax処理用のメッセージレスポンスオブジェクトを構築して返します
	 */
	protected void writeMessageToResponse(String message) {
		this.httpResponse.setContentType("text/plain");
		this.httpResponse.setCharacterEncoding("UTF-8");
		this.httpResponse
				.setStatus(HttpServletResponse.SC_OK);
		try {
			this.httpResponse
					.getWriter()
					.write(message);
		} catch (IOException e) {
			super.errorLog(e);
		}
	}
}
