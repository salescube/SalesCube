/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import java.io.Serializable;

/**
 * 委託入出庫検索画面の検索結果行情報を管理するDTOクラスです.
 * @author Ark Information Systems
 *
 */
public class EntrustEadSlipLineJoinDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public String entrustEadSlipId;

	public String entrustEadDate;

	public String entrustEadAnnual;

	public String entrustEadMonthly;

	public String entrustEadYm;

	public String userId;

	public String userName;

	public String entrustEadCategory;

	public String entrustEadCategoryName;

	public String remarks;

	public String poSlipId;

	public String dispatchOrderPrintCount;

	public String entrustEadLineId;

	public String lineNo;

	public String productCode;

	public String productAbstract;

	public String quantity;

	public String lineRemarks;

	public String poLineId;

	public String poLineNo;

	public String relEntrustEadLineId;

	public String relEntrustEadSlipId;

	public String lineEntrustEadCategory;

	public String lineEntrustEadCategoryName;
}
