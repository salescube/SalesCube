/*
 *  Copyright 2009-2010 Ark Information Systems.
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

	
	public String rackCode;

	
	public String poLot;
	
	public String maxPoNum;
	
	public String maxStockNum;

}
