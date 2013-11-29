/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 銀行入金と入金伝票のリレーションエンティティクラスです.
 *
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class BankDepositRel {
	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "BANK_DEPOSIT_REL";

	@Id
	public Integer depositSlipId;

	public Date paymentDate;

	public String paymentName;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;
}
