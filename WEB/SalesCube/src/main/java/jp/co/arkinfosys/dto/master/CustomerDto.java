/*
 * Copyright 2009-2010 Ark Information Systems.
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
			// DB側に値が有る時には素直に比較する
			if( !dto.equals( form ) ){
				return false;
			}
		}else{
			// DB側がNULLの時には追加されている可能性がある
			if( form instanceof String ){
				// フォーム側に値があれば変更されている
				if( StringUtil.hasLength((String)form)){
					return false;
				}
			}else{
				if( form != null ){
					// 安全側に倒すため、常にfalse
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
		// 顧客コード
		if( !checkEquals( dto.customerCode, customerCode ) ){
			return false;
		}
		// 顧客名
		if( !checkEquals( dto.customerName, customerName ) ){
			return false;
		}
		// 顧客名カナ
		if( !checkEquals( dto.customerKana, customerKana ) ){
			return false;
		}
		// 事業所名
		if( !checkEquals( dto.customerOfficeName, customerOfficeName ) ){
			return false;
		}
		// 事業所名カナ
		if( !checkEquals( dto.customerOfficeKana, customerOfficeKana ) ){
			return false;
		}
		// 顧客略称
		if( !checkEquals( dto.customerAbbr, customerAbbr ) ){
			return false;
		}
		// 部署名
		if( !checkEquals( dto.customerDeptName, customerDeptName ) ){
			return false;
		}
		// 郵便番号
		if( !checkEquals( dto.customerZipCode, customerZipCode ) ){
			return false;
		}
		// 住所１
		if( !checkEquals( dto.customerAddress1, customerAddress1 ) ){
			return false;
		}
		// 住所２
		if( !checkEquals( dto.customerAddress2, customerAddress2 ) ){
			return false;
		}
		// 役職
		if( !checkEquals( dto.customerPcPost, customerPcPost ) ){
			return false;
		}
		// 担当者
		if( !checkEquals( dto.customerPcName, customerPcName ) ){
			return false;
		}
		// 担当者カナ
		if( !checkEquals( dto.customerPcKana, customerPcKana ) ){
			return false;
		}
		// 敬称
		if( !checkEquals( dto.customerPcPreCategory, customerPcPreCategory ) ){
			return false;
		}
		// TEL
		if( !checkEquals( dto.customerTel, customerTel ) ){
			return false;
		}
		// FAX
		if( !checkEquals( dto.customerFax, customerFax ) ){
			return false;
		}
		// E-MAIL
		if( !checkEquals( dto.customerEmail, customerEmail ) ){
			return false;
		}
		// 業種
		if( !checkEquals( dto.customerBusinessCategory, customerBusinessCategory ) ){
			return false;
		}
		// 職種
		if( !checkEquals( dto.customerJobCategory, customerJobCategory ) ){
			return false;
		}
		// 受注停止
		if( !checkEquals( dto.customerRoCategory, customerRoCategory ) ){
			return false;
		}
		// 顧客ランク
		if( !checkEquals( dto.customerRankCategory, customerRankCategory ) ){
			return false;
		}
		// 得意先ランク自動更新
		if( !checkEquals( dto.customerUpdFlag, customerUpdFlag ) ){
			return false;
		}
		// 取引区分
		if( !checkEquals( dto.salesCmCategory, salesCmCategory ) ){
			return false;
		}
		// 税転嫁
		if( !checkEquals( dto.taxShiftCategory, taxShiftCategory ) ){
			return false;
		}
		// 与信限度額
		if( !checkEquals( dto.maxCreditLimit, maxCreditLimit ) ){
			return false;
		}
		// 回収方法
		if( !checkEquals( dto.paybackTypeCategory, paybackTypeCategory ) ){
			return false;
		}
		// 支払条件
		if( !checkEquals( dto.cutoffGroup + dto.paybackCycleCategory, cutoffGroupCategory ) ){
			return false;
		}
		// 税端数処理
		if( !checkEquals( dto.taxFractCategory, taxFractCategory ) ){
			return false;
		}
		// 単価端数処理
		if( !checkEquals( dto.priceFractCategory, priceFractCategory ) ){
			return false;
		}
		// 請求書発行単位
		if( !checkEquals( dto.billPrintUnit, billPrintUnit ) ){
			return false;
		}
		// 請求書日付有無
		if( !checkEquals( dto.billDatePrint, billDatePrint ) ){
			return false;
		}
		// 仮納品書出力不可
		if( !checkEquals( dto.tempDeliverySlipFlag, tempDeliverySlipFlag ) ){
			return false;
		}
		// 振込名義
		if( !checkEquals( dto.paymentName, paymentName ) ){
			return false;
		}
		// コメント
		if( !checkEquals( dto.commentData, commentData ) ){
			return false;
		}
		// 備考
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
