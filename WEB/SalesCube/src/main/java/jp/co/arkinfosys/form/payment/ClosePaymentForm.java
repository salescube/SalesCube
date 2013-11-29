/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.payment;

import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Required;

/**
 * 支払実績締処理画面のアクションフォームクラスです
 * @author Ark Information Systems
 *
 */
public class ClosePaymentForm {
	@Required
    @DateType(datePatternStrict = "yyyy/MM/dd")

    /** 締年月日 */
    public String closeDate;

	/** 最終締日 */
	public String latestAptCutoffDate;

	public boolean menuUpdate;

    /**
     * 買掛残高データの有無を返します.
     * @return true : データあり
     *         false: データなし
     */
	public boolean isCutoffDataExist(){
		return !"".equals(latestAptCutoffDate);
	}

}
