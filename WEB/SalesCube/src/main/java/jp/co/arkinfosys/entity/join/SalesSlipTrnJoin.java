/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 売上伝票と顧客マスタのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class SalesSlipTrnJoin {

	// 売上伝票
	@Id
	@GeneratedValue
	@Column(name = "SALES_SLIP_ID")
	public Integer salesSlipId;
	public String status;
	@Column(name = "SALES_ANNUAL")
	public Short salesAnnual;
	@Column(name = "SALES_MONTHLY")
	public Short salesMonthly;
	@Column(name = "SALES_YM")
	public Integer salesYm;
	@Column(name = "RO_SLIP_ID")
	public Integer roSlipId;
	@Column(name = "BILL_ID")
	public Integer billId;
	@Column(name = "SALES_BILL_ID")
	public Integer salesBillId;
	@Temporal(TemporalType.DATE)
	@Column(name = "BILL_DATE")
	public Date billDate;
	@Column(name = "BILL_CUTOFF_GROUP")
	public String billCutoffGroup;
	@Temporal(TemporalType.DATE)
	@Column(name = "BILL_CUTOFF_DATE")
	public Date billCutoffDate;
	@Temporal(TemporalType.DATE)
	@Column(name = "BILL_CUTOFF_PDATE")
	public Timestamp billCutoffPdate;
	@Temporal(TemporalType.DATE)
	@Column(name = "SALES_DATE")
	public Date salesDate;
	@Temporal(TemporalType.DATE)
	@Column(name = "DELIVERY_DATE")
	public Date deliveryDate;
	@Column(name = "RECEPT_NO")
	public String receptNo;
	@Column(name = "CUSTOMER_SLIP_NO")
	public String customerSlipNo;
	@Column(name = "SALES_CM_CATEGORY")
	public String salesCmCategory;
	@Temporal(TemporalType.DATE)
	@Column(name = "SALES_CUTOFF_DATE")
	public Date salesCutoffDate;
	@Temporal(TemporalType.DATE)
	@Column(name = "SALES_CUTOFF_PDATE")
	public Timestamp salesCutoffPdate;
	@Column(name = "USER_ID")
	public String userId;
	@Column(name = "USER_NAME")
	public String userName;
	public String remarks;
	@Column(name = "PICKING_REMARKS")
	public String pickingRemarks;
	@Column(name = "DC_CATEGORY")
	public String dcCategory;
	@Column(name = "DC_NAME")
	public String dcName;
	@Column(name = "DC_TIMEZONE_CATEGORY")
	public String dcTimezoneCategory;
	@Column(name = "DC_TIMEZONE")
	public String dcTimezone;
	@Column(name = "CUSTOMER_CODE")
	public String customerCode;
	@Column(name = "CUSTOMER_NAME")
	public String customerName;
	@Column(name = "DELIVERY_CODE")
	public Integer deliveryCode;
	@Column(name = "DELIVERY_NAME")
	public String deliveryName;
	@Column(name = "DELIVERY_KANA")
	public String deliveryKana;
	@Column(name = "DELIVERY_OFFICE_NAME")
	public String deliveryOfficeName;
	@Column(name = "DELIVERY_OFFICE_KANA")
	public String deliveryOfficeKana;
	@Column(name = "DELIVERY_DEPT_NAME")
	public String deliveryDeptName;
	@Column(name = "DELIVERY_ZIP_CODE")
	public String deliveryZipCode;
	@Column(name = "DELIVERY_ADDRESS_1")
	public String deliveryAddress1;
	@Column(name = "DELIVERY_ADDRESS_2")
	public String deliveryAddress2;
	@Column(name = "DELIVERY_PC_NAME")
	public String deliveryPcName;
	@Column(name = "DELIVERY_PC_KANA")
	public String deliveryPcKana;
	@Column(name = "DELIVERY_PC_PRE_CATEGORY")
	public String deliveryPcPreCategory;
	@Column(name = "DELIVERY_PC_PRE")
	public String deliveryPcPre;
	@Column(name = "DELIVERY_TEL")
	public String deliveryTel;
	@Column(name = "DELIVERY_FAX")
	public String deliveryFax;
	@Column(name = "DELIVERY_EMAIL")
	public String deliveryEmail;
	@Column(name = "DELIVERY_URL")
	public String deliveryUrl;
	@Column(name = "BA_CODE")
	public String baCode;
	@Column(name = "BA_NAME")
	public String baName;
	@Column(name = "BA_KANA")
	public String baKana;
	@Column(name = "BA_OFFICE_NAME")
	public String baOfficeName;
	@Column(name = "BA_OFFICE_KANA")
	public String baOfficeKana;
	@Column(name = "BA_DEPT_NAME")
	public String baDeptName;
	@Column(name = "BA_ZIP_CODE")
	public String baZipCode;
	@Column(name = "BA_ADDRESS_1")
	public String baAddress1;
	@Column(name = "BA_ADDRESS_2")
	public String baAddress2;
	@Column(name = "BA_PC_NAME")
	public String baPcName;
	@Column(name = "BA_PC_KANA")
	public String baPcKana;
	@Column(name = "BA_PC_PRE_CATEGORY")
	public String baPcPreCategory;
	@Column(name = "BA_PC_PRE")
	public String baPcPre;
	@Column(name = "BA_TEL")
	public String baTel;
	@Column(name = "BA_FAX")
	public String baFax;
	@Column(name = "BA_EMAIL")
	public String baEmail;
	@Column(name = "BA_URL")
	public String baUrl;
	@Column(name = "TAX_SHIFT_CATEGORY")
	public String taxShiftCategory;
	@Column(name = "CTAX_PRICE_TOTAL")
	public BigDecimal ctaxPriceTotal;
	@Column(name = "PRICE_TOTAL")
	public BigDecimal priceTotal;
	@Column(name = "GM_TOTAL")
	public BigDecimal gmTotal;
	@Column(name = "COD_SC")
	public String codSc;
	@Column(name = "BILL_PRINT_COUNT")
	public Integer billPrintCount;
	@Column(name = "DELIVERY_PRINT_COUNT")
	public Integer deliveryPrintCount;
	@Column(name = "TEMP_DELIVERY_PRINT_COUNT")
	public Integer tempDeliveryPrintCount;
	@Column(name = "SHIPPING_PRINT_COUNT")
	public Integer shippingPrintCount;
	@Column(name = "SI_PRINT_COUNT")
	public Integer siPrintCount;
	@Column(name = "ESTIMATE_PRINT_COUNT")
	public Integer estimatePrintCount;
	@Column(name = "DELBOR_PRINT_COUNT")
	public Integer delborPrintCount;
	public String adlabel;
	public String disclaimer;
	@Column(name = "CRE_FUNC")
	public String creFunc;
	@Temporal(TemporalType.DATE)
	@Column(name = "CRE_DATETM")
	public Timestamp creDatetm;
	@Column(name = "CRE_USER")
	public String creUser;
	@Column(name = "UPD_FUNC")
	public String updFunc;
	@Temporal(TemporalType.DATE)
	@Column(name = "UPD_DATETM")
	public Timestamp updDatetm;
	@Column(name = "UPD_USER")
	public String updUser;
	@Column(name = "PAYBACK_CYCLE_CATEGORY")
	public String paybackCycleCategory;
	@Column(name = "TAX_FRACT_CATEGORY")
	public String taxFractCategory;
	@Column(name = "PRICE_FRACT_CATEGORY")
	public String priceFractCategory;

	// 得意先マスタ
	@Column(name = "TEMP_DELIVERY_SLIP_FLAG")
	public String tempDeliverySlipFlag;
	@Column(name = "SALES_SLIP_CATEGORY")
	public String salesSlipCategory;
	@Column(name = "BILL_CATEGORY")
	public String billCategory;

}
