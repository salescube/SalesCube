/*
 *  Copyright 2009-2010 Ark Information Systems.
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
	public Integer orderdCnt;		
	public Integer entrustStockMakedCnt;	
	public Integer entrustStockDeliveredCnt;	
	public Integer nowpurchasedCnt;	
	public Integer purchasedCnt;	
}
