/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.DoubleRange;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.Required;

/**在庫管理画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class StockForm {
	/**
    * 月平均出荷数の計算期間
    */
    public String stockHoldTermCalcCategory;

    /**
     * 欠品率
     */
    @Required
    @DoubleRange(min="0.01", max="0.50")
    public String deficiencyRate;

    /**
     * 委託在庫発注の最大タイムラグ
     */
    @Required
    @IntRange(min=1, max=999)
    public String maxEntrustPoTimelag;

	/**
     * 最大保有数
     */
    @Required
    @IntRange(min=0, max=999)
    public String stockHoldDays;
	/**
     * 発注ロット
     */
    @Required
    @IntRange(min=0, max=999)
    public String minPoLotCalcDays;
	/**
     * 最小発注ロット
     */
    @Required
    @IntRange(min=0, max=999)
    public String minPoLotNum;
	/**
     * 単位発注限度数
     */
    @Required
    @IntRange(min=0, max=999)
    public String  maxPoNumCalcDays;
	/**
     * 最小発注点
     */
    @Required
    @IntRange(min=0, max=999)
     public String  minPoNum;
	/**
     * 更新日時
     */
    public String updDatetm;

    /**
     * 登録時のバリデートを行います.
     * @return 表示するメッセージ
     */
    public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		return errors;
    }

}
