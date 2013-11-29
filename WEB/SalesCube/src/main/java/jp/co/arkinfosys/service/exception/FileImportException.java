/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.exception;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * ファイルインポート時にスローされる例外クラスです.
 *
 * @author Ark Information Systems
 *
 */
public class FileImportException extends ServiceException {

	private static final long serialVersionUID = 1L;

	private ActionMessages messages = new ActionMessages();

	/**
	 * コンストラクタです.
	 */
	public FileImportException() {
		super();
	}

	/**
	 * 表示するメッセージにエラーを追加します.
	 * @param lineNo　行数
	 * @param propertyName　ラベルのプロパティ名
	 */
	public void addInvalidMessage(int lineNo, String propertyName) {
		this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.line.invalid", lineNo, MessageResourcesUtil
						.getMessage("labels.product.csv." + propertyName)));
	}

	/**
	 * 表示するメッセージにエラーを追加します.
	 * @param messages 追加するエラーメッセージ
	 */
	public void addMessages(ActionMessages messages) {
		this.messages.add(messages);
	}

	/**
	 * 表示するメッセージ数を取得します.
	 * @return メッセージ数
	 */
	public int getMessageCount() {
		return this.messages.size();
	}

	/**
	 * 表示するメッセージを取得します.
	 * @return 表示するメッセージ
	 */
	public ActionMessages getMessages() {
		return this.messages;
	}
}
