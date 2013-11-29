/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import jp.co.arkinfosys.entity.Bill;

/**
 * 請求書テーブルと顧客マスタのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class BillJoin extends Bill {

	// 顧客名
	public String customerName;
}
