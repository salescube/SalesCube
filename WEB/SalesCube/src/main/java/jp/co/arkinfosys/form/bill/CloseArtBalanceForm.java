/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.bill;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.arkinfosys.common.Constants;

import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Msg;


/**
 * 売掛締処理画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CloseArtBalanceForm {

	// 売掛締処理日
    @DateType(datePattern = Constants.FORMAT.DATE,
    		msg = @Msg(key = "errors.date"),
    		arg0 = @Arg(key = "labesl.closeArtBalanceDate", resource = true, position = 0))
	public String cutOffDate;

	// 最終売掛締処理日
	public String lastCutOffDate;


	/**
	 * アクションフォームの初期化を行います.
	 */
	public void initialize() {
		lastCutOffDate = "";

		SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);
		cutOffDate = DF_YMD.format(new Date());

	}

}
