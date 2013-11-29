/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.EntrustEadSlipTrn;

/**
 * 委託入出庫伝票と委託入出庫明細と区分データのリレーションエンティティクラスです.
 * @author Ark Information Systems
 *
 */
@Entity
public class EntrustEadSlipLineJoin extends EntrustEadSlipTrn {

	private static final long serialVersionUID = 1L;

	public Integer entrustEadLineId;

	public Short lineNo;

	public String productCode;

	public String productAbstract;

	public BigDecimal quantity;

	public String lineRemarks;

	public Integer poLineId;

	public Integer poLineNo;

	public Integer relEntrustEadLineId;

	public Integer relEntrustEadSlipId;

	public String lineEntrustEadCategory;

	public String lineEntrustEadCategoryName;
}
