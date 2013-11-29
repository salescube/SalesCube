/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.porder;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractLineDto;
/**
 * 発注入力明細行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputPOrderLineDto extends AbstractLineDto {


	public String poLineId;
	public String status;
	public String poSlipId;
	public String productCode;
	public String supplierPcode;
	public String productAbstract;
	public String quantity;
	public String quantityDB;
	public String tempUnitPriceCategory;
	public String taxCategory;
	public String supplierCmCategory;
	public String unitPrice;
	public String price;
	public String ctaxPrice;
	public String ctaxRate;
	public String dolUnitPrice;
	public String dolPrice;
	public String rate;
	public String deliveryDate;
	public String remarks;
	public String restQuantity;
	public String productRemarks;
	public String creFunc;
	public String creDatetm;
	public String creUser;
	public String updFunc;
	public String updDatetm;
	public String updUser;

	public String productIsExist;
	public String poLot;
	public String maxPoNum;
	public String maxStockNum;
	public String holdingStockNum;
	public String productRestQuantity;
	public String dispStatus;

	public String rackCode;

	/**
	 * 明細の空白行の判定を行います.
	 * @return true:空行　false:空白ではない
	 */
	@Override
	public boolean isBlank() {
		return !StringUtil.hasLength(this.productCode);
	}
}
