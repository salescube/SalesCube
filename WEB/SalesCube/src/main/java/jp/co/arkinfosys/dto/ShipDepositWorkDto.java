/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

import java.io.Serializable;

/**
 * 配送業者入金データを管理するDTOクラスです.
 * @author Ark Information Systems
 *
 */
public class ShipDepositWorkDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public String userId;
	public String paymentCategory;
	public String customerCode;
	public String deliverySlipId;
	public String dataCategory;
	public String changeCount;
	public String serviceCategory;
	public String settleCategory;
	public String deliveryDate;
	public String productPrice;
	public String codPrice;
	public String servicePrice;
	public String splitPrice;
	public String stampPrice;
	public String rgDate;
	public String rgSlipId;
}
