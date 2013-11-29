/*
 * Copyright 2009-2010 Ark Information Systems.
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

	// 敬称文字列　コードはDeliveryを参照のこと
	public String categoryCodeName;

	// 顧客名
	public String customerName;

	// 締日グループ
	public String cutoffGroup;

	// 回収間隔
	public String paybackCycleCategory;

	// 税転嫁
	public String taxShiftCategory;

	// 税端数処理
	public String taxFractCategory;

	// 単価端数処理
	public String priceFractCategory;

	// 売上区分
	public String salesCmCategory;

	// 売上区分名
	public String salesCmCategoryName;

	// 状態名称
	public String statusName;

	// 受注停止
	public String customerRoCategory;

	// 備考（顧客）
	public String customerRemarks;

	// コメント（顧客）
	public String customerCommentData;

	// 宛名（顧客）
	public String customerPcPreCategoryName;

	// 売上帳票区分
	public String salesSlipCategory;
	
	// 請求書発行単位
	public String billPrintUnit;

	public boolean equals( DeliveryAndPre src ) {
		if( !super.equals(src ) ){	return false;	}
		return true;
	}
}
