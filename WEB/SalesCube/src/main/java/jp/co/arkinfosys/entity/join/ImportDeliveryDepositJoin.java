/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 配送業者入金関連と配送業者入金データと送り状データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ImportDeliveryDepositJoin  implements Serializable{
	private static final long serialVersionUID = 1L;

	

	public String 	status;				
	public Integer 	salesSlipId;		
	public Integer 	depositSlipId;		
	public String 	deliverySlipId;		
	public String 	customer;			
	public Date 	deliveryDate;	
	public BigDecimal 	productPrice;	
	public BigDecimal 	salesMoney;		
}