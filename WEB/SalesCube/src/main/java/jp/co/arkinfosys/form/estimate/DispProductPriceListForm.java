/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.estimate;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.dto.master.DiscountTrnDto;
import jp.co.arkinfosys.dto.setting.MineDto;

import org.apache.struts.action.ActionErrors;

/**
 * 単価照会検索画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class DispProductPriceListForm {

    /**
     * 商品コード
     */
    public String productCode;

    /**
     * 商品名
     */
    public String productName;

    /**
     * 売上単価
     */
    public String retailPrice;

    /**
     * 商品備考
     */
    public String productRemarks;

    /**
     * 割引コード
     */
    public String discountId;

    /**
     * 割引名
     */
    public String discountName;

    /**
     * 備考
     */
    public String remarks;

	/** 数量スライド */
	public List<DiscountTrnDto> discountTrnList = new ArrayList<DiscountTrnDto>();

	/**
	 * 入力チェックを行います.
	 * @return 表示するメッセージ
	 */
	public ActionErrors validate() {
		ActionErrors errors = new ActionErrors();
		return errors;
	}


    /**
     * 商品コードの入力状態を判定します.
     * @return　入力されているか否か
     */
	public boolean isConditionEmpty() {
		if(StringUtil.hasLength( productCode)
		) {
			return false;
		}
		return true;
	}


	/**
	 * 初期化処理を行います.
	 *
	 * @param userDto　社員情報
	 * @param mineDto　自社情報
	 */
	public void init(UserDto userDto,MineDto mineDto) {

		productCode = "";
		productName = "";
		retailPrice = "";
		productRemarks = "";
		discountId = "";
		discountName = "";
		remarks = "";
		discountTrnList = new ArrayList<DiscountTrnDto>();
	}

}
