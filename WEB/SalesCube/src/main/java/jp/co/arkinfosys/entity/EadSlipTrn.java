/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 入出庫伝票のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class EadSlipTrn implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "EAD_SLIP_TRN";

	@Id
	public Integer eadSlipId;

	public Date eadDate;

	public Short eadAnnual;

	public Short eadMonthly;

	public Integer eadYm;

	public String userId;

	public String userName;

	public String eadSlipCategory;

	public String eadCategory;

	public String remarks;

	public String srcFunc;

	public Integer salesSlipId;

	public Integer supplierSlipId;

	public Integer moveDepositSlipId;

	public Date stockPdate;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;
}