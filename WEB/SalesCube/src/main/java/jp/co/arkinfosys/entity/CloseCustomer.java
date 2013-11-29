/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 顧客締情報のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class CloseCustomer implements Serializable {

	private static final long serialVersionUID = 1L;

	public String customerCode;
	public String customerName;
	public Integer salesCount;
	public Integer depositCount;
	public BigDecimal covPrice;
	public Date billCutoffDate;
}
