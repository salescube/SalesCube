/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * オンライン受注テーブルと受注伝票のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class OnlineOrderRel {

	public static final String TABLE_NAME = "ONLINE_ORDER_REL";

	@Id
	public Integer roSlipId;

	@Id
	public Integer roLineId;

	@Id
	public String onlineOrderId;

	@Id
	public String onlineItemId;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
