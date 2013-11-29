/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import jp.co.arkinfosys.entity.PoLineTrn;
/**
 * 発注伝票明細行と商品マスタと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class PoLineTrnJoin extends PoLineTrn {

	private static final long serialVersionUID = 1L;

	/**
	 * 状態フラグの表示名
	 */
	public String dispStatus;

	// 棚番
	public String rackCode;

	// 発注ロット
	public String poLot;
	// 発注限度数
	public String maxPoNum;
	// 在庫限度数
	public String maxStockNum;

}
