/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * レートデータマスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class RateTrn extends AuditInfo {

	public static final String TABLE_NAME="RATE_TRN";

	@Id
	public Integer rateId;

	@Id
	public Date startDate;

	public BigDecimal rate;

	public String remarks;
}
