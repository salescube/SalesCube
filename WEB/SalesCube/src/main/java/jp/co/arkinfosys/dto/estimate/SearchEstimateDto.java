/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.estimate;

import java.util.Date;

/**
 * 見積検索画面の検索条件のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchEstimateDto {
    /**
     * 見積番号
     */
    public String estimateSheetId;

    /**
     * 見積日（始め）
     */
    public Date estimateDateFrom;

    /**
     * 見積日（終わり）
     */
    public Date estimateDateTo;

    /**
     * 有効期限（始め）
     */
    public Date validDateFrom;

    /**
     * 有効期限（終わり）
     */
    public Date validDateTo;


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
     * 提出先名
     */
    public String submitName;


    /**
     * 顧客コード（得意先コード）
     */
    public String customerCode;

    /**
     * 顧客名（得意先名）
     */
    public String customerName;
}
