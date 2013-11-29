/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.deposit;

import java.io.Serializable;

/**
 * 配送業者入金関連テーブル（DELIVERY_DEPOSIT_REL_）に紐づくDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeliveryDepositRelDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public String salesSlipId;

	public String depositSlipId;

	public String deliverySlipId;

	public String dataCategory;
}
