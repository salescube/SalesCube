/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import java.io.Serializable;

/**
 * 入出庫検索画面の検索結果行情報を管理するDTOクラスです.
 * @author Ark Information Systems
 *
 */
public class EadSlipLineJoinDto implements Serializable {

    private static final long serialVersionUID = 1L;

	public String slipId;

	public String eadSlipId;

	public String eadDate;

	public String eadAnnual;

	public String eadMonthly;

	public String eadYm;

	public String userId;

	public String userName;

	public String eadSlipCategory;

	public String eadSlipCategoryName;

	public String eadCategory;

	public String eadCategoryName;

	public String reason;

	public String srcFunc;

	public String srcFuncName;

	public String salesSlipId;

	public String supplierSlipId;

	public String moveDepositSlipId;

	public String stockPdate;

	public String eadLineId;

	public String lineNo;

	public String productCode;

	public String productAbstract;

	public String rackCode;

	public String rackName;

	public String quantity;

	public String remarks;

	public String salesLineId;

	public String supplierLineId;

	public String rackCodeMove;

	public boolean menuValid;
}