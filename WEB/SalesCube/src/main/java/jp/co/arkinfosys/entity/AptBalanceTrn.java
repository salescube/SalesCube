/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 買掛残高テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class AptBalanceTrn implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "APT_BALANCE_TRN";

	@Id
	public Integer aptBalanceId;

	public Short aptAnnual;

	public Short aptMonthly;

	public Integer aptYm;

	public Date aptCutoffDate;

	public String userId;

	public String userName;

	public String supplierCode;

	public String supplierName;

	public String productCode;

	public String productName;

	public String supplierPcode;

	public BigDecimal quantity;

	public BigDecimal unitRetailPrice;

	public BigDecimal retailPrice;

	public BigDecimal dolUnitPrice;

	public BigDecimal dolPrice;

	public Integer poSlipId;

	public Integer poLineId;

	public Integer supplierSlipId;

	public Integer supplierLineId;

	public Date supplierDate;

	public BigDecimal unpaidPrice;

	public String remarks;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
