/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.exception;

import java.sql.Timestamp;

import org.seasar.struts.util.MessageResourcesUtil;

/**
 * DBレコードのロック失敗時にスローされる例外クラスです.
 * @author Ark Information Systems
 *
 */
public class UnabledLockException extends Exception {

	private static final long serialVersionUID = 1L;

	private int lockStatus = 0;

	private Timestamp targetTm = null;

	private String targetUpdUserId = null;

	private String targetUpdUserName = null;

	private Timestamp selfTm = null;

	private String key = null;

	/**
	 * コンストラクタです.
	 * @param key　キー値
	 */
	public UnabledLockException(String key) {
		super(MessageResourcesUtil.getMessage(key));
		this.key = key;
	}

	/**
	 * コンストラクタです.
	 * @param e 例外
	 */
	public UnabledLockException(Exception e) {
		super(e);
	}

	/**
	 * ロック結果を返します.
	 * @return　ロック結果
	 */
	public int getLockStatus() {
		return lockStatus;
	}

	/**
	 * ロック結果を設定します.
	 * @param lockStatus　ロック結果
	 */
	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}

	/**
	 * レコードの最終更新時間を返します．
	 * @return　レコードの最終更新時間
	 */
	public Timestamp getTargetTm() {
		return targetTm;
	}

	/**
	 * レコードの最終更新時間を設定します.
	 * @param targetTm　レコードの最終更新時間
	 */
	public void setTargetTm(Timestamp targetTm) {
		this.targetTm = targetTm;
	}

	/**
	 * 更新ユーザIDをを返します.
	 * @return　更新ユーザID
	 */
	public String getTargetUpdUserId() {
		return targetUpdUserId;
	}

	/**
	 *　更新ユーザIDを設定します.
	 * @param targetUpdUserId　更新ユーザID
	 */
	public void setTargetUpdUserId(String targetUpdUserId) {
		this.targetUpdUserId = targetUpdUserId;
	}

	/**
	 * 更新ユーザ名を返します.
	 * @return　更新ユーザ名
	 */
	public String getTargetUpdUserName() {
		return targetUpdUserName;
	}

	/**
	 *　更新ユーザ名を設定します.
	 * @param targetUpdUserName　更新ユーザ名
	 */
	public void setTargetUpdUserName(String targetUpdUserName) {
		this.targetUpdUserName = targetUpdUserName;
	}

	/**
	 * ロック可否判定に使用する日時を返します.
	 * @return　ロック可否判定に使用する日時
	 */
	public Timestamp getSelfTm() {
		return selfTm;
	}

	/**
	 * ロック可否判定に使用する日時を設定します.
	 *
	 * @param selfTm　ロック可否判定に使用する日時
	 */
	public void setSelfTm(Timestamp selfTm) {
		this.selfTm = selfTm;
	}

	/**
	 * キー値を返します.
	 * @return　キー値
	 */
	public String getKey() {
		return key;
	}

	/**
	 * キー値を設定します.
	 * @param key　キー値
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
