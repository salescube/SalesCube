/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.dto.master;

import java.io.Serializable;
import java.math.BigDecimal;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.Customer;
/**
 * 顧客マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CustomerDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	/**
	 * 顧客マスタ
	 */
	public String customerCode;

	public String customerName;

	public String customerKana;

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

	public String customerTel;

	public String customerFax;

	public String customerEmail;

	public String customerUrl;

	public String customerBusinessCategory;

	public String customerJobCategory;

	public String customerRoCategory;

	public String customerRankCategory;

	public String customerUpdFlag;

	public String salesCmCategory;

	public String taxShiftCategory;

	public BigDecimal rate;

	public BigDecimal maxCreditLimit;

	public String lastCutoffDate;

	public String cutoffGroup;

	/** 支払条件 */
	public String cutoffGroupCategory;

	public String paybackTypeCategory;

	public String paybackCycleCategory;

	public String taxFractCategory;

	public String priceFractCategory;

	public String billPrintUnit;

	public String billDatePrint;

	public String tempDeliverySlipFlag;

	public String paymentName;

	public String rankRemarks;

	public String firstSalesDate;

	public String lastSalesDate;

	public BigDecimal salesPriceTotal;

	public BigDecimal salesPriceLsm;

	public String commentData;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	public String remarks;

	public String lastSalesCutoffDate;

	/**
	 * 顧客ランク名
	 */
	public String rankName;

	/**
	 * 区分コード（支払条件）
	 */
	public Integer categoryId;

	/**
	 * 支払条件コード
	 */
	public String categoryCode;

	/**
	 * 支払条件名
	 */
	public String categoryCodeName;

	/**
	 * 区分コード（税転嫁）
	 */
	public Integer categoryId2;

	/**
	 * 税転嫁コード
	 */
	public String categoryCode2;

	/**
	 * 税転嫁名称
	 */
	public String categoryCodeName2;

	/**
	 * 区分コード（売上取引区分）
	 */
	public Integer categoryId3;

	/**
	 * 売上取引区分コード
	 */
	public String categoryCode3;

	/**
	 * 売上取引区分名
	 */
	public String categoryCodeName3;

	/**
	 * 区分コード（顧客敬称）
	 */
	public Integer categoryId4;

	/**
	 * 宛名（顧客）
	 */
	public String customerPcPreCategoryName;

	/**
	 * DTOの内容とFORMの内容が一致しているか確認します.<BR>
	 * FORM側は型がStringに変わっている可能性があります.
	 * @param dto
	 * @param form
	 * @return　true　一致
	 */
	private boolean checkEquals( Object dto, Object form ) {
		if( dto != null ){
			
			if( !dto.equals( form ) ){
				return false;
			}
		}else{
			
			if( form instanceof String ){
				
				if( StringUtil.hasLength((String)form)){
					return false;
				}
			}else{
				if( form != null ){
					
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 編集画面で更新状態を管理する為の関数です.<BR>
	 * 画面の入力項目のみチェックし、システムによって更新される項目は除外します.<BR>
	 * 【重要】　画面の入力項目が増えた場合には、ここも修正する必要があります.
	 * @param dto 比較する顧客マスタ情報
	 * @return 更新されていないか　否か
	 */
	public boolean equals( Customer dto ) {
		
		if( !checkEquals( dto.customerCode, customerCode ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerName, customerName ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerKana, customerKana ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerOfficeName, customerOfficeName ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerOfficeKana, customerOfficeKana ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerAbbr, customerAbbr ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerDeptName, customerDeptName ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerZipCode, customerZipCode ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerAddress1, customerAddress1 ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerAddress2, customerAddress2 ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerPcPost, customerPcPost ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerPcName, customerPcName ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerPcKana, customerPcKana ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerPcPreCategory, customerPcPreCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerTel, customerTel ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerFax, customerFax ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerEmail, customerEmail ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerBusinessCategory, customerBusinessCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerJobCategory, customerJobCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerRoCategory, customerRoCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerRankCategory, customerRankCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.customerUpdFlag, customerUpdFlag ) ){
			return false;
		}
		
		if( !checkEquals( dto.salesCmCategory, salesCmCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.taxShiftCategory, taxShiftCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.maxCreditLimit, maxCreditLimit ) ){
			return false;
		}
		
		if( !checkEquals( dto.paybackTypeCategory, paybackTypeCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.cutoffGroup + dto.paybackCycleCategory, cutoffGroupCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.taxFractCategory, taxFractCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.priceFractCategory, priceFractCategory ) ){
			return false;
		}
		
		if( !checkEquals( dto.billPrintUnit, billPrintUnit ) ){
			return false;
		}
		
		if( !checkEquals( dto.billDatePrint, billDatePrint ) ){
			return false;
		}
		
		if( !checkEquals( dto.tempDeliverySlipFlag, tempDeliverySlipFlag ) ){
			return false;
		}
		
		if( !checkEquals( dto.paymentName, paymentName ) ){
			return false;
		}
		
		if( !checkEquals( dto.commentData, commentData ) ){
			return false;
		}
		
		if( !checkEquals( dto.remarks, remarks ) ){
			return false;
		}

		return true;
	}

	/**
	 * 顧客コードを取得します.
	 * @return　 顧客コード
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.customerCode };
	}

}
