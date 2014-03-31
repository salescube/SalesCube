/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.rorder;

import jp.co.arkinfosys.common.SlipStatusCategoryTrns;
import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;

/**
 * 受注伝票情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ROrderSlipDto extends AbstractSlipDto<ROrderLineDto> {

	/** 受注番号 */
	public String roSlipId;

	/** 状態コード */
	public String status;

	/** 受注日 */
	public String roDate;

	/** 出荷日 */
	public String shipDate;

	/** 納期指定日 */
	public String deliveryDate;

	/** 受付番号 */
	public String receptNo;

	/** 客先伝票番号 */
	public String customerSlipNo;

	/** 担当者コード */
	public String userId;

	/** 担当者名 */
	public String userName;

	/** 備考 */
	public String remarks;

	/** 顧客コード */
	public String customerCode;

	/** 顧客名 */
	public String customerName;

	/** 備考（顧客） */
	public String customerRemarks;

	/** コメント（顧客） */
	public String customerCommentData;

	/** 税転嫁 */
	public String taxShiftCategory;

	/** 税端数処理 */
	public String taxFractCategory;

	/** 単価端数処理 */
	public String priceFractCategory;

	/** 支払条件 */
	public String cutoffGroupCategory;

	/** 締日グループ */
	public String cutoffGroup;

	/** 回収間隔 */
	public String paybackCycleCategory;

	/** 取引区分 */
	public String salesCmCategory;

	/** 納入先コード */
	public String deliveryCode;

	/** 納入先名 */
	public String deliveryName;

	/** 納入先名カナ */
	public String deliveryKana;

	/** 事業所名 */
	public String deliveryOfficeName;

	/** 事業所名カナ */
	public String deliveryOfficeKana;

	/** 部署名 */
	public String deliveryDeptName;

	/** 郵便番号 */
	public String deliveryZipCode;

	/** 住所１ */
	public String deliveryAddress1;

	/** 住所２ */
	public String deliveryAddress2;

	/** （納入先）担当者 */
	public String deliveryPcName;

	/** （納入先）担当者カナ */
	public String deliveryPcKana;

	/** 敬称 */
	public String deliveryPcPreCategory;

	/** 敬称（名称） */
	public String deliveryPcPre;

	/** 電話番号 */
	public String deliveryTel;

	/** E-MAIL */
	public String deliveryEmail;

	/** FAX */
	public String deliveryFax;

	/** URL */
	public String deliveryUrl;

	/** 最終更新日時（排他制御用） */
	public String updDatetm;

	/** 原価金額伝票合計 */
	public String costTotal;

	/** 粗利益 */
	public String gross;

	/** 粗利益率 */
	public String grossRatio;

	/** 金額合計 */
	public String retailPriceTotal;

	/** 消費税 */
	public String ctaxPriceTotal;

	/** 伝票合計 */
	public String priceTotal;

	/** 代引き手数料 */
	public String codSc;

	public String dcCategory;				// L 配送業者コード
	public String dcName;					// S 配送業者名
	public String dcTimezoneCategory;		// L 配送時間帯コード
	public String dcTimezone;				// S 配送時間帯文字列
	
	/**
	 * 消費税率
	 */
	public String ctaxRate;

	/**
	 * 受注伝票明細行を作成します.
	 * @return 受注伝票明細行オブジェクト
	 */
	@Override
	public AbstractLineDto createLineDto() {
		ROrderLineDto result = new ROrderLineDto();
		result.status = SlipStatusCategoryTrns.RO_LINE_NEW;

		return result;
	}
	/**
	 * 受注伝票番号を取得します.
	 * @return 受注伝票番号
	 */
	@Override
	public String getKeyValue() {
		return this.roSlipId;
	}


}
