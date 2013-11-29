/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.stock;

import java.util.List;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.stock.ProductStockJoinDto;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 在庫残高表画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputStockReportForm {

    /**
     * 表出力対象期間（月単位）
     */
    @Required
    public String targetYm;

    /**
     * 検索結果件数
     */
    public Integer searchResultCount;

	/**
	 * 検索結果リスト
	 */
	public List<ProductStockJoinDto> searchResultList;

    /**
     * 現在日付
     */
    public String currentDate;

    /**
     * 在庫残高合計金額
     */
    public String sumStockPrice;

    /**
     * 検索条件か未入力かどうか判定します.
     * @return　未入力か否か
     */
	public boolean isConditionEmpty() {
		if(StringUtil.hasLength(targetYm)) {
			return false;
		}
		return true;
	}

	/**
     * Excel出力時のバリデートを行います.
     * @return 表示するメッセージ
     */
	public ActionErrors validate() {
		ActionErrors errors = new ActionErrors();
		String labelTargetYm = MessageResourcesUtil.getMessage("labels.targetYm");

		// 年月チェック
		if(StringUtil.hasLength(targetYm)) {
			if(!StringUtil.isYmString(targetYm)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.ym", labelTargetYm));
			}
		}

		return errors;
	}
}
