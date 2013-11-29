/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.purchase;

import java.sql.Timestamp;

import jp.co.arkinfosys.dto.AbstractLineDto;

/**
 * 仕入伝票明細行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class PurchaseLineDto extends AbstractLineDto {
    public String supplierLineId;
    public String status;
    public String supplierSlipId;
    public String productCode;
	public String supplierPcode;
	public String productAbstract;
	public String supplierDetailCategory;
	public String deliveryProcessCategory;
	public String tempUnitPriceCategory;
	public String taxCategory;
    public String deliveryDate;
    public String quantity;
    public String unitPrice;
    public String price;
    public String ctaxRate;
    public String ctaxPrice;
    public String dolUnitPrice;
    public String dolPrice;
    public String rate;
    public String remarks;
	public String productRemarks;
    public String warehouseName;
    public String rackCode;
    public String rackName;
    public String poLineId;
    public String paymentLineId;

    public String creFunc;
    public Timestamp creDatetm;
    public String creUser;
    public String updFunc;
    public Timestamp updDatetm;
    public String updUser;

    public String oldQuantity;
    public String restQuantity;
    public String totalQuantity;

    @Override
    public String toString() {
    	return warehouseName;
    }
	/**
	 * 商品コードがnull又は空白かどうか検査します.
	 * @return true:商品コーがnullまたは空白　false:商品コードはnullでも空白でもない
	 */
    @Override
	public boolean isBlank() {
		return (this.productCode == null || this.productCode.length() == 0);
	}
}
