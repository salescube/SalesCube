/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.stock;

import java.util.List;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.stock.ProductStockInfoDto;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 在庫一覧表画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputStockListForm {

	public static class RadioCond2 {
		public static final String VALUE_0 = "0";
		public static final String VALUE_1 = "1";
		public static final String VALUE_2 = "2";
		public static final String VALUE_3 = "3";
		public static final String VALUE_4 = "4";
		public static final String VALUE_5 = "5";
		public static final String VALUE_6 = "6";
	}

	// JSP側のEL式で使用する定数
	public String RADIO_COND2_VALUE_0 = RadioCond2.VALUE_0;
	public String RADIO_COND2_VALUE_1 = RadioCond2.VALUE_1;
	public String RADIO_COND2_VALUE_2 = RadioCond2.VALUE_2;
	public String RADIO_COND2_VALUE_3 = RadioCond2.VALUE_3;
	public String RADIO_COND2_VALUE_4 = RadioCond2.VALUE_4;
	public String RADIO_COND2_VALUE_5 = RadioCond2.VALUE_5;
	public String RADIO_COND2_VALUE_6 = RadioCond2.VALUE_6;

	/**
	 * 商品の抽出条件１:ヶ月前～本日　で受注実績のあるもの
	 */
    @Required
    @IntRange(min=0, max=999)
	public String periodMonth;

	/**
	 * 商品の抽出条件２ラジオボタン
	 */
	public String radioCond2;

	/**
	 * 商品の抽出条件２:引当可能数
	 */
	public String allocatedQuantity;

	/**
	 * 商品の抽出条件２:引当可能数（カンマ付き）
	 */
	public String allocatedQuantityWithComma;

	/**
	 * 商品の抽出条件３:受注実績のない商品を除く
	 */
	public boolean excludeRoNotExists;

	/**
	 * 商品の抽出条件４:販売中止品(発注停止品)は除く
	 */
	public boolean excludeSalesCancel;

	/**
	 * 商品の抽出条件５:在庫管理しない商品（都度調達品）は除く
	 */
	public boolean excludeNotManagementStock;

	/**
	 * 商品の抽出条件６:棚番管理しない商品は除く
	 */
	public boolean excludeMultiRack;

    /**
     * 現在日付
     */
    public String currentDate;

	/**
	 * 検索結果リスト
	 */
	public List<ProductStockInfoDto> searchResultList;

	/**
	 * フォームをクリアします.
	 */
	public void reset() {
		periodMonth = null;
		radioCond2 = null;
		allocatedQuantity = null;
		allocatedQuantityWithComma = null;
		excludeRoNotExists = true;
		excludeSalesCancel = true;
		excludeNotManagementStock = true;
		excludeMultiRack = true;
	}

	 /**
     * Excel出力時のバリデートを行います.
     * @return 表示するメッセージ
     */
	public ActionErrors validate() {
		ActionErrors errors = new ActionErrors();
		String labelPeriodMonth = MessageResourcesUtil.getMessage("labels.periodMonth");
		String labelAllocatedQuantity = MessageResourcesUtil.getMessage("labels.allocatedQuantity");

		// 必須チェック

		// 引当可能数
		if(RadioCond2.VALUE_5.equals(radioCond2)
				&& !StringUtil.hasLength(allocatedQuantity)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.required", labelAllocatedQuantity));
		}

		// 型チェック

		// 抽出期間
		if(StringUtil.hasLength(periodMonth)) {
			try {
				Integer.valueOf(periodMonth);
			} catch(NumberFormatException e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.integer", labelPeriodMonth));
			}
		}
		// 引当可能数
		if(RadioCond2.VALUE_5.equals(radioCond2)
				&& StringUtil.hasLength(allocatedQuantity)) {
			try {
				Integer.valueOf(allocatedQuantity);
			} catch(NumberFormatException e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.integer", labelAllocatedQuantity));
			}
		}

		return errors;
	}
}
