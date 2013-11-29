/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import jp.co.arkinfosys.entity.EstimateLineTrn;

/**
 * 見積伝票明細行と商品マスタのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class EstimateLineProductJoin extends EstimateLineTrn{

	public Short roMaxNum;			// 受注限度数
	public String supplierPcode;	// 仕入先品番



}
