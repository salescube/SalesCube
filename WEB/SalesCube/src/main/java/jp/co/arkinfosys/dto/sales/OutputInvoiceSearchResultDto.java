/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.dto.sales;

import java.io.Serializable;
import java.math.BigDecimal;

import org.seasar.struts.util.MessageResourcesUtil;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.setting.MineDto;

/**
 * 送り状データ出力画面の検索結果を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputInvoiceSearchResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public String roSlipId;
	public String salesSlipId;
	public String salesDate;
	public String customerCode;
	public String deliveryCode;
	public String customerName;
	public String billPrintCount;
	public String deliveryPrintCount;
	public String tempDeliveryPrintCount;
	public String shippingPrintCount;
	public String tempDeliverySlipFlag;
	public String siPrintCount;
	public String salesSlipCategory;
	public String billCategory;
	public String estimatePrintCount;
	public String delborPrintCount;
	public String remarks;


	public String status;
	public String salesAnnual;
	public String salesMonthly;
	public String salesYm;
	public String billId;
	public String salesBillId;
	public String billDate;
	public String billCutoffGroup;
	public String paybackCycleCategory;
	public String billCutoffDate;
	public String billCutoffPdate;
	public String deliveryDate;
	public String receptNo;
	public String customerSlipNo;
	public String salesCmCategory;
	public String salesCutoffDate;
	public String salesCutoffPdate;
	public String userId;
	public String userName;
	public String pickingRemarks;
	public String dcCategory;
	public String dcName;
	public String dcTimezoneCategory;
	public String dcTimezone;
	public String customerOfficeName;
	public String customerOfficeKana;
	public String customerAbbr;
	public String customerDeptName;
	public String customerZipCode;
	public String customerAddress1;
	public String customerAddress2;
	public String customerPcPost;
	public String customerPcName;
	public String customerPcKana;
	public String customerPcPreCategory;
	public String customerPcPre;
	public String customerTel;
	public String customerFax;
	public String customerEmail;
	public String customerUrl;
	public String deliveryName;
	public String deliveryKana;
	public String deliveryOfficeName;
	public String deliveryOfficeKana;
	public String deliveryDeptName;
	public String deliveryZipCode;
	public String deliveryAddress1;
	public String deliveryAddress2;
	public String deliveryPcName;
	public String deliveryPcKana;
	public String deliveryPcPreCategory;
	public String deliveryPcPre;
	public String deliveryTel;
	public String deliveryFax;
	public String deliveryEmail;
	public String deliveryUrl;
	public String baCode;
	public String baName;
	public String baKana;
	public String baOfficeName;
	public String baOfficeKana;
	public String baDeptName;
	public String baZipCode;
	public String baAddress1;
	public String baAddress2;
	public String baPcName;
	public String baPcKana;
	public String baPcPreCategory;
	public String baPcPre;
	public String baTel;
	public String baFax;
	public String baEmail;
	public String baUrl;
	public String taxShiftCategory;
	public String taxFractCategory;
	public String priceFractCategory;
	public String ctaxPriceTotal;
	
	public String priceTotal;
	public String gmTotal;
	public String codSc;
	public String poPrintCount;
	public String adlabel;
	public String disclaimer;
	public String creFunc;
	public String creDatetm;
	public String creUser;
	public String updFunc;
	public String updDatetm;
	public String updUser;

	
	public MineDto mineDto;

	
	public String itemName1 = MessageResourcesUtil.getMessage("labels.delivery.invoice.itemName1"); 
	public String luggageTreatment1 = MessageResourcesUtil.getMessage("labels.delivery.invoice.luggageTreatment1"); 
	public String luggageTreatment2 = MessageResourcesUtil.getMessage("labels.delivery.invoice.luggageTreatment2"); 
	public String notificationInputType = MessageResourcesUtil.getMessage("labels.delivery.invoice.notificationInputType");
	public String notificationMessage = MessageResourcesUtil.getMessage("labels.delivery.invoice.notificationMessage");
	public String companyName = MessageResourcesUtil.getMessage("labels.delivery.invoice.companyName"); 
	public String paymentNo = MessageResourcesUtil.getMessage("labels.delivery.invoice.paymentNo");	

	
	public String invoiceCategory = "";
	public String collectPrice = "";
	public String taxPriceIn = "";
	public String paymentDataRegister = "";
	public String notificationHopeCategory = "";

	
	
	public String companyZipCode = "";
	public String companyAddress1 = "";
	public String companyAddress2 = "";

	
	public boolean isSiPrinted = false;

	/**
	 * 出力用にデータを作成します.
	 * @param mineDto　自社情報
	 */
	public void createOutputData(MineDto mineDto){
		
		this.mineDto = mineDto;

		/**
		 * 送り状種別
		 */
		
		if(CategoryTrns.SALES_CM_CASH_ON_DELIVERY.equals(salesCmCategory)){
			invoiceCategory = "2";
		}else{
			invoiceCategory = "0";
		}

		/**
		 * 代金引換額(税込)
		 * 内消費税額等
		 */
		if(CategoryTrns.SALES_CM_CASH_ON_DELIVERY.equals(salesCmCategory)){
			BigDecimal ctaxPriceTotalDecimal;
			BigDecimal priceToralDecimal;
			
			if(StringUtil.hasLength(ctaxPriceTotal)){
				ctaxPriceTotalDecimal = new BigDecimal(ctaxPriceTotal);
			}else{
				ctaxPriceTotalDecimal = new BigDecimal("0");
			}
			if(StringUtil.hasLength(priceTotal)){
				priceToralDecimal = new BigDecimal(priceTotal);
			}else{
				priceToralDecimal = new BigDecimal("0");
			}

			collectPrice = String.valueOf(priceToralDecimal.add(ctaxPriceTotalDecimal));
			taxPriceIn = String.valueOf(ctaxPriceTotalDecimal);
		}

		/**
		 * ペイメントデータ登録
		 */
		
		if(StringUtil.hasLength(customerCode)){
			String tempCustomerCode =customerCode.substring(0,1);
			if("3".equals(tempCustomerCode) || "4".equals(tempCustomerCode)){
				paymentDataRegister = "1";
			}
		}

		/**
		 * 事前通知希望区分
		 */
		
		if(StringUtil.hasLength(deliveryEmail)){
			notificationHopeCategory = "1";
		}else{
			notificationHopeCategory = "0";
		}

		/** add 2010.04.28 kaki
		 * 届け先郵便番号（ハイフンなしの半角7文字）
		 */
		if(deliveryZipCode.length() >= 8){
			String tempdeliveryZipCode1 = deliveryZipCode.substring(0, 3);
			String tempdeliveryZipCode2 = deliveryZipCode.substring(4, 8);
			deliveryZipCode = tempdeliveryZipCode1 + tempdeliveryZipCode2;
		}

		/** add 2010.04.28 kaki
		 * 依頼主郵便番号（ハイフンなしの半角7文字）
		 */
		if(mineDto.companyZipCode.length() >= 8){
			String tempcompanyZipCode1 = mineDto.companyZipCode.substring(0, 3);
			String tempcompanyZipCode2 = mineDto.companyZipCode.substring(4, 8);
			companyZipCode = tempcompanyZipCode1 + tempcompanyZipCode2;
		}

		
		if(!StringUtil.hasLength(deliveryPcName)) {
			
			deliveryPcName = deliveryName;
		}
		
		if( StringUtil.hasLength(remarks)){
			remarks = StringUtil.substringByte(remarks,50);
		}else{
			remarks = "";
		}
	}

}
