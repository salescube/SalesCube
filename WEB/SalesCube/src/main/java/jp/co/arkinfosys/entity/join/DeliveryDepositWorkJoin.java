/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity.join;


import java.math.BigDecimal;

import jp.co.arkinfosys.entity.DeliveryDepositWork;
/**
 * 配送業者入金データと配送業者入金関連と売上伝票のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeliveryDepositWorkJoin extends DeliveryDepositWork {

	private static final long serialVersionUID = 1L;

	public Integer salesSlipId;			
	public Integer depositSlipId;		
	public BigDecimal priceTotal;		


}
