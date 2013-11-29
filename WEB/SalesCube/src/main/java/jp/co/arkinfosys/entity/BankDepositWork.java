/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 銀行入金一時データのエンティティクラスです.
 *
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class BankDepositWork {

	public String userId;

	public String column1;

	public Date paymentDate;

	public String column2;

	public String paymentType;

	public String paymentName;

	public BigDecimal paymentPrice;

}
