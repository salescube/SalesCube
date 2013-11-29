/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 配送業者入金と入金伝票のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class DeliveryDepositRel implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "DELIVERY_DEPOSIT_REL";

	@Id
	public Integer salesSlipId;

	@Id
	public Integer depositSlipId;

	@Id
	public String deliverySlipId;

	@Id
	public String dataCategory;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

	public String delFunc;

	public Timestamp delDatetm;

	public String delUser;

}
