/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity.join;

import jp.co.arkinfosys.entity.Delivery;

/**
 * 納入先マスタと得意先関連マスタと顧客マスタと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeliveryAndPre extends Delivery {

	
	public String categoryCodeName;

	
	public String customerName;

	
	public String cutoffGroup;

	
	public String paybackCycleCategory;

	
	public String taxShiftCategory;

	
	public String taxFractCategory;

	
	public String priceFractCategory;

	
	public String salesCmCategory;

	
	public String salesCmCategoryName;

	
	public String statusName;

	
	public String customerRoCategory;

	
	public String customerRemarks;

	
	public String customerCommentData;

	
	public String customerPcPreCategoryName;

	
	public String salesSlipCategory;

	public boolean equals( DeliveryAndPre src ) {
		if( !super.equals(src ) ){	return false;	}
		return true;
	}
}
