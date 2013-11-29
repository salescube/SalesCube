/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 初期値マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class InitMst {

	public static final String TABLE_NAME = "INIT_MST";

	@Id
	public String tableName;

	@Id
	public String columnName;
	public String title;
	public Integer categoryId;
	public String useDataType;
	public Short useStrSize;
	public String strData;
	public Integer numData;
	public BigDecimal fltData;
	public String remarks;
	public String creFunc;
	public Timestamp creDatetm;
	public String creUser;
	public String updFunc;
	public Timestamp updDatetm;
	public String updUser;

}
