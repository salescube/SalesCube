/*
 *  Copyright 2009-2010 Ark Information Systems.
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

	public String roSlipId;
	public String salesSlipId;
	public String salesDate;
	public String customerCode;
	public String deliveryCode;
	public String firstDeliveryCode; 
	public String customerName;
	public String billPrintCount;
	public String deliveryPrintCount;
	public String tempDeliveryPrintCount;
	public String shippingPrintCount;
	public String tempDeliverySlipFlag;
	public String siPrintCount;
	public String billPrintUnit;
	public String billDatePrint;
	public String estimatePrintCount;
	public String delborPrintCount;
	public String remarks;
	public String salesCmCategory; 

	
	public boolean isEstimateCheckDisp = false;
	public boolean isBillCheckDisp = false;
	public boolean isDeliveryCheckDisp = false;
	public boolean isTempDeliveryCheckDisp = false;
	public boolean isPickingListCheckDisp = false;
	public boolean isDeliveryReceiptCheckDisp = false;

	
	public String fileEstimate = "";
	public String fileBill = "";
	public String fileDelivery = "";
	public String fileTempDelivery = "";
	public String filePickingList = "";
	public String fileDeliveryReceipt = "";

	
	
	public String REPORT_ALL = Constants.REPORT_SELECTION.VALUE_ALL;
	public String REPORT_BILL = Constants.REPORT_SELECTION.VALUE_BILL;
	public String REPORT_DELIVERY = Constants.REPORT_SELECTION.VALUE_DELIVERY;
	public String REPORT_TEMP_DELIVERY = Constants.REPORT_SELECTION.VALUE_TEMP_DELIVERY;
	public String REPORT_PICKING_LIST = Constants.REPORT_SELECTION.VALUE_PICKING;
	public String REPORT_ESTIMATE = Constants.REPORT_SELECTION.VALUE_ESTIMATE;
	public String REPORT_DELIVERY_RECEIPT = Constants.REPORT_SELECTION.VALUE_DELIVERY_RECEIPT;
	public String REPORT_PICKING_CONSTRACTION = Constants.REPORT_SELECTION.VALUE_PICKING_CONSTRACTION;
																										
																										
	public String REPORT_DELIVERY_6 = Constants.REPORT_SELECTION.VALUE_DELIVERY_6;
	public String REPORT_BILL_POST = Constants.REPORT_SELECTION.VALUE_BILL_POST;

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

	
	public String REPORT_FILE_A = Constants.REPORT_TEMPLATE.REPORT_ID_A;
	public String REPORT_FILE_C = Constants.REPORT_TEMPLATE.REPORT_ID_C;
	public String REPORT_FILE_D = Constants.REPORT_TEMPLATE.REPORT_ID_D;
	public String REPORT_FILE_E = Constants.REPORT_TEMPLATE.REPORT_ID_E;
	public String REPORT_FILE_F = Constants.REPORT_TEMPLATE.REPORT_ID_F;
	public String REPORT_FILE_G = Constants.REPORT_TEMPLATE.REPORT_ID_G;
	public String REPORT_FILE_H = Constants.REPORT_TEMPLATE.REPORT_ID_H;
	public String REPORT_FILE_J = Constants.REPORT_TEMPLATE.REPORT_ID_J;
	public String REPORT_FILE_K = Constants.REPORT_TEMPLATE.REPORT_ID_K;

	
	public boolean dispDateFlag = false;

	
	public boolean allOutput = false; 
	public boolean tempDeliveryOutputFlag = false; 
	public boolean tempDeliveryOutput = false; 

	/**
	 * デフォルトの出力ステータスを返します.
	 * @return 出力ステータス
	 */
	public String getDefaultOutputStatus() {
		
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