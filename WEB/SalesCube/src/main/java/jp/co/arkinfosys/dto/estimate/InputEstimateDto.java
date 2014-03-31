/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.estimate;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;

/**
 * 見積伝票のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 *  @param InputEstimateLineDto 見積伝票明細行
 */
public class InputEstimateDto extends AbstractSlipDto<InputEstimateLineDto> {

	/**
     * 見積番号
     */
    public String estimateSheetId;

    /**
     * 見積日
     */
    public String estimateDate;

    /**
     * 見積年度
     */
	public String estimateAnnual;
    /**
     * 見積月度
     */
	public String estimateMonthly;
    /**
     * 見積年月度
     */
	public String estimateYm;

    /**
     * 納期または出荷日（納入期限）
     */
    public String deliveryInfo;


    /**
     * 有効期限
     */
    public String validDate;

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
     * 提出先敬称コード
     */
    public String submitPreCategory;

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
     * 備考（顧客）
     */
    public String customerRemarks;

    /**
     * コメント（顧客）
     */
    public String customerCommentData;

    /**
     * 取引区分（顧客）
     */
 	public String salesCmCategory;

    /**
     * 郵便番号　カラム名はdelivery（納入先）だが顧客の郵便番号を登録する
     */
    public String deliveryZipCode;

    /**
     * 摘要（備考）
     */
    public String remarks;

    /**
     * メモ
     */
    public String memo;
    
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
    
    /**
     * 原価合計（仕入金額合計）
     */
    public String costTotal;

    /**
     * 更新日（排他制御のため）
     */
	public String updDatetm;
	
    /**
     * 更新者（排他制御のため）
     */
	public String updUser;

	/**
	 * 税端数処理
	 */
	public String taxFractCategory;
	
	/**
	 * 単価端数処理
	 */
	public String priceFractCategory;

	/**
	 * 消費税率
	 */
	public String ctaxRate;

	// 新規作成状態の管理フラグ
	public Boolean newData;

	/**
	 * 見積明細行を1行作成します.
	 * @return 見積明細行
	 */
	@Override
	public AbstractLineDto createLineDto() {
		return new InputEstimateLineDto();
	}

	/**
	 * 見積番号を取得します.
	 * @return 見積番号
	 */
	@Override
	public String getKeyValue() {
		return this.estimateSheetId;
	}
}
