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
 * 請求書テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Bill {

	public static final String TABLE_NAME = "BILL_TRN"; // 請求書

	public static final String STATUS_INIT = "0"; // 請求完了

	@Id
	public Integer billId;

	public String status;

	public Short billYear;

	public Short billMonth;

	public Integer billYm;

	public Date billCutoffDate;

	public String cutoffGroup;

	public Timestamp cutoffPdate;

	public String remarks;

	public String baCode;

	public String customerCode;

	public BigDecimal lastBillPrice;

	public BigDecimal depositPrice;

	public BigDecimal adjPrice;

	public BigDecimal covPrice;

	public BigDecimal salesPrice;

	public BigDecimal ctaxPrice;

	public BigDecimal rguPrice;

	public BigDecimal dctPrice;

	public BigDecimal etcPrice;

	public BigDecimal thisBillPrice;

	public Short slipNum;

	public BigDecimal codLastBillPrice;

	public BigDecimal codDepositPrice;

	public BigDecimal codAdjPrice;

	public BigDecimal codCovPrice;

	public BigDecimal codSalesPrice;

	public BigDecimal codCtaxPrice;

	public BigDecimal codRguPrice;

	public BigDecimal codDctPrice;

	public BigDecimal codEtcPrice;

	public BigDecimal codThisBillPrice;

	public Short codSlipNum;

	public String userId;

	public String userName;

	public Date paybackPlanDate;

	public Date lastPrintDate;

	public Integer billPrintCount;

	public String billCrtCategory;

	public Date lastSalesDate;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

	public String paybackCycleCategory;

}
