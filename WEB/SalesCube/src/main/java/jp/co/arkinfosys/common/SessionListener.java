/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * ユーザーセッションの生成時と消滅時に処理を行うリスナクラスです.
 * @author Ark Information Systems
 *
 */
public class SessionListener implements HttpSessionListener {

	/**
	 * ユーザーセッションの生成時の処理を行います.
	 * @param ev 通知するイベント
	 */
	@Override
	public void sessionCreated(HttpSessionEvent ev) {
		System.out.println("Session[" + ev.getSession().getId() + "]BEGIN");
	}

	/**
	 * ユーザーセッションの消滅時の処理を行います.
	 * @param ev 通知するイベント
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent ev) {
		System.out.println("Session[" + ev.getSession().getId() + "]END");
	}

}
