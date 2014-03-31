/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.sales;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;

/**
 * 売上伝票情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SalesSlipDto extends AbstractSlipDto<SalesLineDto> {

	/** 売上伝票番号 */
	public String salesSlipId;

	/** 状態フラグ */
	public String status;

	/** 売上年度 */
	public String salesAnnual;

	/** 売上月度 */
	public String salesMonthly;

	/** 売上年月度 */
	public String salesYm;

	/** 売上日 */
	public String salesDate;

	/** 税端数処理 */
	public String taxFractCategory;

	/** 税転嫁 */
	public String taxShiftCategory;

	/** 伝票合計消費税 */
	public String ctaxPriceTotal;

	/** 伝票合計金額 売上伝票のpriceTotalには、消費税を含まない */
	public String priceTotal;

	/** 伝票合計粗利益 */
	public String gmTotal;

	/** 更新日時 */
	public String updDatetm;

	/** 締日グループと支払サイクルに分割する元のコード */
	public String cutoffGroupCategory;

	/** 締日グループ */
	public String billCutoffGroup;

	/** 回収間隔 */
	public String paybackCycleCategory;

	/** 得意先コード */
	public String customerCode;

	/** 得意先名 */
	public String customerName;

	/** 顧客取引区分 */
	public String custsalesCmCategory;

	/** 請求書日付有無 */
	public String billDatePrint;

	/** 受注伝票番号 */
	public String roSlipId;

	/** 受注伝票の最終更新日 */
	public String roUpdDatetm;

	/** 売上取引区分 */
	public String salesCmCategory;

	/** 請求日 */
	public String billDate;

	/** 単価端数処理 */
	public String priceFractCategory;

	/** 得意先の郵便番号 */
	public String customerZipCode;

	/** 得意先の住所 */
	public String customerAddress1;
	public String customerAddress2;

	/** 得意先の敬称 */
	public String customerPcName;

	/** 得意先の電話番号 */
	public String customerTel;

	public String customerUrl;
	public String customerOfficeName;
	public String customerOfficeKana;
	public String customerAbbr;
	public String customerDeptName;
	public String customerPcPost;
	public String customerPcKana;
	public String customerPcPreCategory;
	public String customerPcPre;
	public String customerFax;
	public String customerEmail;

	/** 請求書発行フラグ */
	public String billPrintCount;

	/** 納品書発行フラグ */
	public String deliveryPrintCount;

	/** 仮納品書発行フラグ */
	public String tempDeliveryPrintCount;

	/** 出荷指示書発行フラグ */
	public String shippingPrintCount;

	/** 送り状データ出力フラグ */
	public String siPrintCount;

	public String estimatePrintCount;
	public String delborPrintCount;
	public String poPrintCount;

	/** 売上請求書番号 */
	public String salesBillId;

	/** 請求先コード */
	public String baCode;

	/** 請求先会社名 */
	public String baName;

	/** 請求先カナ */
	public String baKana;

	/** 請求先事業所名 */
	public String baOfficeName;

	/** 請求先事業所カナ */
	public String baOfficeKana;

	/** 請求先部署名 */
	public String baDeptName;

	/** 請求先郵便番号 */
	public String baZipCode;

	/** 請求先住所１ */
	public String baAddress1;

	/** 請求先住所２ */
	public String baAddress2;

	/** 請求先担当者 */
	public String baPcName;

	/** 請求先担当者カナ */
	public String baPcKana;

	/** 請求先敬称コード */
	public String baPcPreCategory;

	/** 請求先敬称 */
	public String baPcPre;

	/** 請求先電話番号 */
	public String baTel;

	/** 請求先ＦＡＸ番号 */
	public String baFax;

	/** 請求先Email */
	public String baEmail;

	/** 請求先URL */
	public String baUrl;

	/** 担当者コード */
	public String userId;

	/** 担当者名 */
	public String userName;

	// 出荷指示書用
	public String pickingListId;
	public String roDate;					// 足りない情報は保存時にDBを参照して設定する
	public String printDate;

	/** 請求書番号 */
	public String billId;
	/** 請求締め日付 */
	public String billCutoffDate;
	/** 請求締め処理日 */
	public String billCutoffPdate;
	/** 納期指定日 */
	public String deliveryDate;
	/** 受付番号 */
	public String receptNo;
	/** 客先伝票番号 */
	public String customerSlipNo;
	/** 売掛締め日付 */
	public String salesCutoffDate;
	/** 売掛締め処理日 */
	public String salesCutoffPdate;
	/** 備考 */
	public String remarks;
	/** ピッキング備考 */
	public String pickingRemarks;
	/** 配送業者コード */
	public String dcCategory;
	/** 配送業者名 */
	public String dcName;
	/** 配送時間帯コード */
	public String dcTimezoneCategory;
	/** 配送時間帯文字列 */
	public String dcTimezone;
	/** 備考 */
	public String customerRemarks;
	/** コメント */
	public String customerCommentData;
	/** 納入先コード */
	public String deliveryCode;
	/** 納入先名 */
	public String deliveryName;
	/** 納入先名カナ */
	public String deliveryKana;
	/** 納入先事業所名 */
	public String deliveryOfficeName;
	/** 納入先事業所名カナ */
	public String deliveryOfficeKana;
	/** 納入先部署名 */
	public String deliveryDeptName;
	/** 納入先郵便番号 */
	public String deliveryZipCode;
	/** 納入先住所１ */
	public String deliveryAddress1;
	/** 納入先住所２ */
	public String deliveryAddress2;
	/** 納入先担当者名 */
	public String deliveryPcName;
	/** 納入先担当者カナ */
	public String deliveryPcKana;
	/** 納入先敬称コード */
	public String deliveryPcPreCategory;
	/** 納入先敬称 */
	public String deliveryPcPre;
	/** 納入先電話番号 */
	public String deliveryTel;
	/** 納入先ＦＡＸ番号 */
	public String deliveryFax;
	/** 納入先Email */
	public String deliveryEmail;
	/** 納入先ＵＲＬ */
	public String deliveryUrl;
	/** 代引手数料 */
	public String codSc;
	/** 売掛残高番号 */
	public String artId;
	/** 宛名 */
	public String adlabel;
	/** 但書 */
	public String disclaimer;
	/** 作成機能 */
	public String creFunc;
	/** 作成日時 */
	public String creDatetm;
	/** 作成者 */
	public String creUser;
	/** 更新機能 */
	public String updFunc;
	/** 更新者 */
	public String updUser;
	/** 伝票合計粗利益率 */
	public String gmTotalPer;
	/** 伝票合計(金額＋消費税） */
	public String total;
	/** 請求書発行単位 */
	public String billPrintUnit;
	/** 仮納品書出力 */
	public String tempDeliverySlipFlag;
	/** 複写対象　伝票種類 */
	public String copySlipName;
	/** 複写対象　伝票番号 */
	public String copySlipId;
	
	/** 消費税率 */
	public String ctaxRate;

	/**
	 * 売上伝票明細行を作成します.
	 * @return 売上伝票明細行オブジェクト
	 */
	@Override
	public AbstractLineDto createLineDto() {
		return new SalesLineDto();
	}

	/**
	 * 売上伝票番号を取得します.
	 * @return 売上伝票番号
	 */
	@Override
	public String getKeyValue() {
		return this.salesSlipId;
	}
}
