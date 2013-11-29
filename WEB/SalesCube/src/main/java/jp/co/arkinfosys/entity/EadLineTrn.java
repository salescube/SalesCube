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
 * 入出庫伝票明細行のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class EadLineTrn implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "EAD_LINE_TRN";

	@Id
	public Integer eadLineId;

	public Integer eadSlipId;

	public Short lineNo;

	public String productCode;

	public String productAbstract;

	public String rackCode;

	public String rackName;

	public BigDecimal quantity;

	public String remarks;

	public Integer salesLineId;

	public Integer supplierLineId;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;
}