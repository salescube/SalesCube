/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity.join;

import jp.co.arkinfosys.entity.InvoiceDataWork;

/**
 * 送り状データと売上伝票のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InvoiceDataWorkJoin extends InvoiceDataWork{

	public String invoiceSalesSlipId; 

	public Integer salesSlipId;		

	public String customerCode;		
}
