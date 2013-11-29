/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 委託入出庫伝票のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class EntrustEadSlipTrn implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "ENTRUST_EAD_SLIP_TRN";

	public Integer entrustEadSlipId;

	public Date entrustEadDate;

	public Short entrustEadAnnual;

	public Short entrustEadMonthly;

	public Integer entrustEadYm;

	public String userId;

	public String userName;

	public String entrustEadCategory;

	public String entrustEadCategoryName;

	public String remarks;

	public Integer poSlipId;

	public Integer dispatchOrderPrintCount;

    public String supplierCode;

    public String supplierName;

	public String creFunc;
	@Temporal(TemporalType.DATE)
	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;
	@Temporal(TemporalType.DATE)
	public Timestamp updDatetm;

	public String updUser;

}
