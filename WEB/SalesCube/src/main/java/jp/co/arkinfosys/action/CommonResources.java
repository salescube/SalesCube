/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.arkinfosys.dto.DomainDto;
import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.dto.setting.MineDto;
import jp.co.arkinfosys.service.TaxRateService;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;

/**
 * アクションクラスの共通基底クラスです.
 * @author Ark Information Systems
 *
 */
public class CommonResources {

	protected ActionMessages messages = new ActionMessages();

	@Resource
	public HttpServletRequest httpRequest;

	@Resource
	public HttpServletResponse httpResponse;

	@Resource
	public HttpSession httpSession;

	@Resource
	public DomainDto domainDto;

	@Resource
	public UserDto userDto;

	@Resource
	public MineDto mineDto;

	/** 税率計算用サービス */
	@Resource
	public TaxRateService taxRateService;

	/**
	 * セッション管理DTOの内容をクリアします.
	 */
	protected void clearResources() {
		Beans.copy(new UserDto(), this.userDto).execute();
		Beans.copy(new MineDto(), this.mineDto).execute();
	}

	/**
	 * エラーメッセージをログファイルに出力します.
	 *
	 * @param ex 例外オブジェクト
	 */
	protected void errorLog(Exception ex) {
		Logger logger = Logger.getLogger(this.getClass());

		StringWriter strWriter = null;
		PrintWriter printWriter = null;
		try {
			strWriter = new StringWriter();
			printWriter = new PrintWriter(strWriter);

			ex.printStackTrace(printWriter);
			printWriter.flush();
			strWriter.flush();

			logger
					.error("[" + domainDto.domainId + "] "
							+ strWriter.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (strWriter != null) {
					strWriter.close();
					strWriter = null;
				}
				if (printWriter != null) {
					printWriter.close();
					printWriter = null;
				}
			} catch (IOException e) {
				// とりあえず書いとく
				e.printStackTrace();
			}
		}
	}

}
