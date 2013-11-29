/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * ピッキングリストのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PickingList {

	public static final String TABLE_NAME = "PICKING_LIST_TRN";

	public static final String STATUS_INIT = "0";

	@Id
	public Integer pickingListId;

	public Date roDate;

	public Short salesAnnual;

	public Short salesMonthly;

	public Integer salesYm;

	public Integer roSlipId;

	public Integer salesSlipId;

	public String receptNo;

	public String salesCmCategory;

	public String pickingRemarks;

	public String remarks;

	public String customerCode;

	public String customerName;

	public String customerPcName;

	public String customerZipCode;

	public String customerAddress1;

	public String customerAddress2;

	public String customerTel;

	public String deliveryCode;

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

	public String taxShiftCategory;

	public BigDecimal ctaxPriceTotal;

	public BigDecimal priceTotal;

	public Timestamp printDate;

	public String codSc;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

	public String taxFractCategory;

	public String priceFractCategory;

	public String customerRemarks;

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

	public String customerUrl;

}
