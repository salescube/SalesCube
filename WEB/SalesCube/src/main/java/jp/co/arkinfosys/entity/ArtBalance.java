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
 * 売掛残高テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ArtBalance {

	public static final String TABLE_NAME = "ART_BALANCE_TRN";	// 売掛残高

	@Id
	public Integer artBalanceId;

	public Short artAnnual;

	public Short artMonthly;

	public Integer artYm;

	public Date artCutoffDate;

	public String userId;

	public String userName;

	public String baCode;

	public String baName;

	public String customerCode;

	public String customerName;

	public BigDecimal lastArtPrice;

	public BigDecimal depositPrice;

	public BigDecimal adjPrice;

	public BigDecimal covPrice;

	public BigDecimal salesPrice;

	public BigDecimal ctaxPrice;

	public BigDecimal rguPrice;

	public BigDecimal dctPrice;

	public BigDecimal etcPrice;

	public BigDecimal thisArtPrice;

	public BigDecimal gmPrice;

	public String artCutoffGroup;

	public Short salesSlipNum;

	public Timestamp artCutoffPdate;

	public BigDecimal depositCash;

	public BigDecimal depositCheck;

	public BigDecimal depositTransfer;

	public BigDecimal depositSc;

	public BigDecimal depositInst;

	public BigDecimal depositSetoff;

	public BigDecimal depositEtc;

	public String familyCategory;

	public Short deliveryPlaceNum;

	public String remarks;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

	public String paybackCycleCategory;

	public String salesCmCategory;

}
