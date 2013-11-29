/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import javax.persistence.Entity;

/**
 * 銀行マスタテーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Bank extends AuditInfo {

	public static final String TABLE_NAME = "BANK_MST";
	public Integer bankId;
	public String accountOwnerName;
	public String accountOwnerKana;
	public String bankCode;
	public String bankName;
	public String storeCode;
	public String storeName;
	public String dwbType;
	public String accountNum;
	public String remarks;
	public String valid;
}
