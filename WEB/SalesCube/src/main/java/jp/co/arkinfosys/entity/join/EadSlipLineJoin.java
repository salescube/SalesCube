/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 入出庫伝票と入出庫明細と区分データのリレーションエンティティクラスです.
 * @author Ark Information Systems
 *
 */
@Entity
public class EadSlipLineJoin implements Serializable{
	private static final long serialVersionUID = 1L;

	public Integer eadSlipId;

	public Date eadDate;

	public Short eadAnnual;

	public Short eadMonthly;

	public Integer eadYm;

	public String userId;

	public String userName;

	public String eadSlipCategory;

	public String eadSlipCategoryName;

	public String eadCategory;

	public String eadCategoryName;

	public String reason;

	public String srcFunc;

	public Integer salesSlipId;

	public Integer supplierSlipId;

	public Integer moveDepositSlipId;

	public Date stockPdate;

	public Integer eadLineId;

	public Short lineNo;

	public String productCode;

	public String productAbstract;

	public String rackCode;

	public String rackName;

	public BigDecimal quantity;

	public String remarks;

	public Integer salesLineId;

	public Integer supplierLineId;

	public String rackCodeMove;
}
