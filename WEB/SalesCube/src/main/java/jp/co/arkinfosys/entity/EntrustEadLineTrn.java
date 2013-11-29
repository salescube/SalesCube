/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 委託入出庫伝票明細行のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class EntrustEadLineTrn implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "ENTRUST_EAD_LINE_TRN";

	@Id
	public Integer entrustEadLineId;

	public Integer entrustEadSlipId;

	public Short lineNo;

	public String productCode;

	public String productAbstract;

	public String supplierPcode;

	public BigDecimal quantity;

	public String remarks;

	public Integer poSlipId;

	public Integer poLineNo;

	public Integer poLineId;

	public Integer relEntrustEadLineId;

	public Integer entrustEadCategory;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;
}