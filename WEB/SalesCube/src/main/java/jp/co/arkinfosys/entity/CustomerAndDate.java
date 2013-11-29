/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Date;

import javax.persistence.Entity;

/**
 * 顧客マスタと売上伝票のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 */
@Entity
public class CustomerAndDate extends Customer {

	private static final long serialVersionUID = 1L;

	public Date salesDate;

	public Integer count;

}
