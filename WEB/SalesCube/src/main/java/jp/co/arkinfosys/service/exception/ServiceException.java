/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.exception;

/**
 * サービスパッケージのクラスで例外が発生した場合にスローされる例外クラスです.
 * @author Ark Information Systems
 *
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 処理の継続可否
	 */
	private boolean stopOnError = false;

	/**
	 * コンストラクタです.
	 *
	 * @param message　エラーメッセージ
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * コンストラクタです.
	 *
	 * @param e　例外
	 */
	public ServiceException(Exception e) {
		super(e);
	}

	/**
	 * コンストラクタです.
	 */
	protected ServiceException() {
		super();
	}

	/**
	 * 処理の継続可否を返します.
	 *
	 * @return　処理を中断するか否か
	 */
	public boolean isStopOnError() {
		return stopOnError;
	}

	/**
	 * 処理の継続可否を設定します.
	 *
	 * @param stopOnError　処理を中断する(true)か否か(false)
	 */
	public void setStopOnError(boolean stopOnError) {
		this.stopOnError = stopOnError;
	}

}
