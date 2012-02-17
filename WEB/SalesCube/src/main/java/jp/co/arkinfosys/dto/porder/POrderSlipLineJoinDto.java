/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.dto.porder;

import java.io.Serializable;

/**
 * 発注書発行画面の検索結果リスト行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class POrderSlipLineJoinDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 追加分
	 */
	
	public String paymentStatus;

	
	public String purePriceTotal;

	/**
	 * 伝票（DB対応あり）
	 */
	
	public String poSlipId;

	public String poSlipIdShow;

	
	public String poDate;

	
	public String deliveryDate;

	
	public String userId;
	public String userName;

	
	public String remarks;

	
	public String supplierCode;
	
	public String supplierName;

	
	public String transportCategory;
	public String transportCategoryString;

	
	public String priceTotal;
	
	public String ctaxTotal;
	
	public String fePriceTotal;

	
	public String printCount;

	/**
	 * 明細行（DB対応あり）
	 */

	
	/**
	 * 名称変更
	 * public String status;
	 */
	public String lineStatus;

	
	public String lineNo;

	
	public String productCode;
	
	public String productAbstract;
	
	public String quantity;

	
	public String unitPrice;
	
	public String price;

	
	public String dolUnitPrice;
	
	public String dolPrice;

	
	/**
	 * 名称変更
	 * public Date deliveryDate;
	 */
	public String lineDeliveryDate;

	
	/**
	 * 名称変更
	 * public String remarks;
	 */
	public String lineRemarks;

	
	public String restQuantity;

}
