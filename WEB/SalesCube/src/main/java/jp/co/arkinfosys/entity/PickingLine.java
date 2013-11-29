/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * ピッキングリスト明細行のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PickingLine {

	public static final String TABLE_NAME = "PICKING_LINE_TRN";

	public Integer pickingLineId;

	public Integer pickingListId;

	public Integer salesLineId;

	public Integer roLineId;

	public Short lineNo;

	public String salesDetailCategory;

	public String productCode;

	public String customerPcode;

	public String productAbstract;

	public BigDecimal quantity;

	public BigDecimal unitPrice;

	public String unitCategory;

	public String unitName;

	public Short packQuantity;

	public BigDecimal unitRetailPrice;

	public BigDecimal retailPrice;

	public BigDecimal unitCost;

	public BigDecimal cost;

	public String taxCategory;

	public BigDecimal ctaxRate;

	public BigDecimal ctaxPrice;

	public BigDecimal gm;

	public String rackCodeSrc;

	public BigDecimal stockNum;

	public String remarks;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

	public String pickingRemarks;

	public String setTypeCategory;

}
