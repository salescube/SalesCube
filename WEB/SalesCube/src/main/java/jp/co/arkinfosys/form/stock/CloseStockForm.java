/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.stock;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;

import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Required;

/**
 * 在庫締処理画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class CloseStockForm {

    /**
     * 締年月日
     */
    @Required
    @DateType(datePatternStrict = Constants.FORMAT.DATE)
    public String cutoffDate;

    /**
     * 最終締年月日
     */
    public String lastCutoffDate;

    /**
     * 前回締めが行われているかどうかを判定します.
     * @return　前回締めが行われているか否か
     */
    public boolean isCutoff() {
    	if(StringUtil.hasLength(lastCutoffDate)) {
    		return true;
    	}
    	return false;
    }

}
