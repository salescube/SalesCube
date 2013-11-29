/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

/**
 * 発注伝票明細行の状態の集計用クラスです.
 *
 * @author Ark Information Systems
 *
 */
public class PoLineStatusCnt {
	private static final long serialVersionUID = 1L;

	public Integer allCnt;
	public Integer orderdCnt;		//発注
	public Integer entrustStockMakedCnt;	//委託在庫生産完了
	public Integer entrustStockDeliveredCnt;	//委託在庫出庫完了
	public Integer nowpurchasedCnt;	//分納
	public Integer purchasedCnt;	//仕入完了
}
