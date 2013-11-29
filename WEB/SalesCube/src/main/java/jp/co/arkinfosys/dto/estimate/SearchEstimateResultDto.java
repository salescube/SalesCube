/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.estimate;

import java.util.Date;

/**
 * 見積検索画面の検索結果リスト行のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchEstimateResultDto {
    /**
     * 見積番号
     */
    public String estimateSheetId;

    /**
     * 見積日
     */
    public Date estimateDate;

    /**
     * 納期または出荷日（納入期限）
     */
    public String deliveryInfo;


    /**
     * 有効期限
     */
    public Date validDate;

    /**
     * 入力担当者ID
     */
    public String userId;

    /**
     * 入力担当者
     */
    public String userName;


    /**
     * 件名
     */
    public String title;

    /**
     * 摘要（備考）
     */
    public String remarks;

    /**
     * 納入先名
     */
	public String deliveryName;

    /**
     * 見積条件
     */
	public String estimateCondition;

    /**
     * 提出先名
     */
    public String submitName;

    /**
     * 提出先敬称
     */
    public String submitPre;


    /**
     * 顧客コード（得意先コード）
     */
    public String customerCode;

    /**
     * 顧客名（得意先名）
     */
    public String customerName;

    /**
     * 粗利益
     */
    public String grossMargin;

    /**
     * 粗利益率
     */
    public String grossMarginRate;
    /**
     * 合計金額
     */
    public String retailPriceTotal;
    /**
     * 消費税
     */
    public String ctaxPriceTotal;
    /**
     * 伝票合計
     */
    public String estimateTotal;
}
