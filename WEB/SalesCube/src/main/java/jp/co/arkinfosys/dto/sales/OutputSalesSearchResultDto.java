/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.sales;

import java.io.Serializable;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;

/**
 * 売上帳票発行画面の検索結果行情報を管理するDTOクラスです.
 * @author Ark Information Systems
 */
public class OutputSalesSearchResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public String roSlipId;/* 受注伝票番号 */
	public String salesSlipId;/* 売上番号 */
	public String salesDate;/* 売上日 */
	public String customerCode;/* 得意先コード */
	public String deliveryCode;/* 納入先コード */
	public String firstDeliveryCode; /* trueが入っている時には、顧客情報と納入先情報が一致（仮納品書出力制御に使用） */
	public String customerName;/* 得意先名 */
	public String billPrintCount;/* 請求書発行フラグ */
	public String deliveryPrintCount;/* 納品書発行フラグ */
	public String tempDeliveryPrintCount;/* 仮納品書発行フラグ */
	public String shippingPrintCount;/* 出荷指示書発行フラグ */
	public String tempDeliverySlipFlag;/* 仮納品書出力フラグ */
	public String siPrintCount;/* 送り状データ出力フラグ */
	public String billPrintUnit;/* 請求書発行単位 */
	public String billDatePrint;/* 請求書日付有無 */
	public String estimatePrintCount;/* 見積書 */
	public String delborPrintCount;/* 納品書兼領収書 */
	public String remarks;/* 備考 */
	public String salesCmCategory; /* 売上取引区分 */

	// 検索結果の各チェックボックス表示フラグ
	public boolean isEstimateCheckDisp = false;
	public boolean isBillCheckDisp = false;
	public boolean isDeliveryCheckDisp = false;
	public boolean isTempDeliveryCheckDisp = false;
	public boolean isPickingListCheckDisp = false;
	public boolean isDeliveryReceiptCheckDisp = false;

	// EXCEL,PDF出力ファイル(Excel,PDF出力のフォーマット)
	public String fileEstimate = "";
	public String fileBill = "";
	public String fileDelivery = "";
	public String fileTempDelivery = "";
	public String filePickingList = "";
	public String fileDeliveryReceipt = "";

	// 帳票種類チェックボックスのID(検索結果へのID設定用)
	// 注意：帳票種類が１０を超えた場合はIDの"1"～"9"を"01"～"09"と振り直す必要がある
	public String REPORT_ALL = Constants.REPORT_SELECTION.VALUE_ALL;// 全ての帳票
	public String REPORT_BILL = Constants.REPORT_SELECTION.VALUE_BILL;// 請求書(個別発行で商品に同梱の場合)
	public String REPORT_DELIVERY = Constants.REPORT_SELECTION.VALUE_DELIVERY;// 納品書
	public String REPORT_TEMP_DELIVERY = Constants.REPORT_SELECTION.VALUE_TEMP_DELIVERY;// 仮納品書
	public String REPORT_PICKING_LIST = Constants.REPORT_SELECTION.VALUE_PICKING;// ピッキングリスト
	public String REPORT_ESTIMATE = Constants.REPORT_SELECTION.VALUE_ESTIMATE;// 見積書
	public String REPORT_DELIVERY_RECEIPT = Constants.REPORT_SELECTION.VALUE_DELIVERY_RECEIPT;// 納品書兼領収書
	public String REPORT_PICKING_CONSTRACTION = Constants.REPORT_SELECTION.VALUE_PICKING_CONSTRACTION;// ピッキングリスト
																										// +
																										// 組立指示書
	public String REPORT_DELIVERY_6 = Constants.REPORT_SELECTION.VALUE_DELIVERY_6;// 納品書6行
	public String REPORT_BILL_POST = Constants.REPORT_SELECTION.VALUE_BILL_POST;// 請求書(個別発行で郵送の場合)

	/**
	 * 帳票種類チェックボックスの見積書IDを取得します.
	 * @return　帳票種類チェックボックス見積書ID
	 */
	public String getEstimateCheckId() {
		return Constants.REPORT_SELECTION.VALUE_ESTIMATE;
	}

	/**
	 * 帳票種類チェックボックスの請求書IDを取得します.
	 * @return　 帳票種類チェックボックスの請求書ID
	 */
	public String getBillCheckId() {
		if(REPORT_FILE_G.equals(this.fileBill)) {
			return Constants.REPORT_SELECTION.VALUE_BILL;
		}
		if(REPORT_FILE_H.equals(this.fileBill)) {
			return Constants.REPORT_SELECTION.VALUE_BILL_POST;
		}
		return null;
	}

	/**
	 * 帳票種類チェックボックスの納品書IDを取得します.
	 * @return　 帳票種類チェックボックスの納品書ID
	 */
	public String getDeliveryCheckId() {
		if(REPORT_FILE_C.equals(this.fileDelivery)) {
			return Constants.REPORT_SELECTION.VALUE_DELIVERY;
		}
		if(REPORT_FILE_D.equals(this.fileDelivery)) {
			return Constants.REPORT_SELECTION.VALUE_DELIVERY_6;
		}
		return null;
	}

	/**
	 * 帳票種類チェックボックスの仮納品書IDを取得します.
	 * @return　 帳票種類チェックボックスの仮納品書ID
	 */
	public String getTempDeliveryCheckId() {
		return Constants.REPORT_SELECTION.VALUE_TEMP_DELIVERY;
	}

	/**
	 * 帳票種類チェックボックスのピッキングリストIDを取得します.
	 * @return　 帳票種類チェックボックスのピッキングリストID
	 */
	public String getPickingListCheckId() {
		if(this.filePickingList != null && this.filePickingList.endsWith(REPORT_FILE_K)) {
			return Constants.REPORT_SELECTION.VALUE_PICKING_CONSTRACTION;
		} else {
			return Constants.REPORT_SELECTION.VALUE_PICKING;
		}
	}

	/**
	 * 帳票種類チェックボックスの納品書兼領収書IDを取得します.
	 * @return　 帳票種類チェックボックスの納品書兼領収書ID
	 */
	public String getDeliveryReceiptCheckId() {
		return Constants.REPORT_SELECTION.VALUE_DELIVERY_RECEIPT;
	}

	// 帳票種別
	public String REPORT_FILE_A = Constants.REPORT_TEMPLATE.REPORT_ID_A;// 御見積書
	public String REPORT_FILE_C = Constants.REPORT_TEMPLATE.REPORT_ID_C;// 納品書
	public String REPORT_FILE_D = Constants.REPORT_TEMPLATE.REPORT_ID_D;// 納品書６行
	public String REPORT_FILE_E = Constants.REPORT_TEMPLATE.REPORT_ID_E;// 仮納品書
	public String REPORT_FILE_F = Constants.REPORT_TEMPLATE.REPORT_ID_F;// 納品書兼領収書
	public String REPORT_FILE_G = Constants.REPORT_TEMPLATE.REPORT_ID_G;// 請求書(個別発行で商品に同梱の場合)
	public String REPORT_FILE_H = Constants.REPORT_TEMPLATE.REPORT_ID_H;// 請求書(個別発行で郵送の場合)
	public String REPORT_FILE_J = Constants.REPORT_TEMPLATE.REPORT_ID_J;// ピッキングリスト
	public String REPORT_FILE_K = Constants.REPORT_TEMPLATE.REPORT_ID_K;// 組立指示書

	// 出力する帳票の日付表示フラグ
	public boolean dispDateFlag = false;

	// 「全て出力済みを除く」チェックボックスの制御で使用する
	public boolean allOutput = false; // 全て出力済みフラグ
	public boolean tempDeliveryOutputFlag = false; // 仮納品書発行フラグ（発行対象/対象外）
	public boolean tempDeliveryOutput = false; // 仮納品書（発行済/未発行）

	/**
	 * デフォルトの出力ステータスを返します.
	 * @return 出力ステータス
	 */
	public String getDefaultOutputStatus() {
		// 出力帳票
		StringBuilder reportFile = new StringBuilder();
		StringBuilder reportPrintState = new StringBuilder();

		if(this.isEstimateCheckDisp) {
			reportFile.append(Constants.REPORT_SELECTION.VALUE_ESTIMATE);
			reportPrintState.append(this.estimatePrintCount);
		}
		if(this.isBillCheckDisp) {
			if(reportFile.length() > 0) {
				reportFile.append(",");
				reportPrintState.append(",");
			}

			if(REPORT_FILE_G.equals(this.fileBill)) {
				reportFile.append(Constants.REPORT_SELECTION.VALUE_BILL);
			}
			if(REPORT_FILE_H.equals(this.fileBill)) {
				reportFile.append(Constants.REPORT_SELECTION.VALUE_BILL_POST);
			}
			reportPrintState.append(this.billPrintCount);
		}
		if(this.isDeliveryCheckDisp) {
			if(reportFile.length() > 0) {
				reportFile.append(",");
				reportPrintState.append(",");
			}

			if(REPORT_FILE_C.equals(this.fileDelivery)) {
				reportFile.append(Constants.REPORT_SELECTION.VALUE_DELIVERY);
			}
			if(REPORT_FILE_D.equals(this.fileDelivery)) {
				reportFile.append(Constants.REPORT_SELECTION.VALUE_DELIVERY_6);
			}
			reportPrintState.append(this.deliveryPrintCount);
		}
		if(this.isTempDeliveryCheckDisp) {
			if(reportFile.length() > 0) {
				reportFile.append(",");
				reportPrintState.append(",");
			}
			reportFile.append(Constants.REPORT_SELECTION.VALUE_TEMP_DELIVERY);
			reportPrintState.append(this.tempDeliveryPrintCount);
		}
		if(this.isPickingListCheckDisp) {
			if(reportFile.length() > 0) {
				reportFile.append(",");
				reportPrintState.append(",");
			}

			if(this.filePickingList != null && this.filePickingList.endsWith(REPORT_FILE_K)) {
				reportFile.append(Constants.REPORT_SELECTION.VALUE_PICKING_CONSTRACTION);
			} else {
				reportFile.append(Constants.REPORT_SELECTION.VALUE_PICKING);
			}
			reportPrintState.append(this.shippingPrintCount);
		}
		if(this.isDeliveryReceiptCheckDisp) {
			if(reportFile.length() > 0) {
				reportFile.append(",");
				reportPrintState.append(",");
			}
			reportFile.append(Constants.REPORT_SELECTION.VALUE_DELIVERY_RECEIPT);
			reportPrintState.append(this.delborPrintCount);
		}

		if(reportFile.length() == 0) {
			return null;
		}

		String roSlipIdStr = "";
		if(StringUtil.hasLength(this.roSlipId)) {
			roSlipIdStr = this.roSlipId;
		}

		return String.format("%s:%s:%s:%s:%s:%s", roSlipIdStr, this.salesSlipId, reportFile.toString(), reportPrintState.toString(), this.dispDateFlag, this.tempDeliverySlipFlag);
	}

}