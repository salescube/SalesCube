/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.math.BigDecimal;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.InitMst;
/**
 * 初期値マスタと区分マスタと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class InitMstJoin extends InitMst {

	public String categoryCode;

	public String categoryCodeName;

	public String categoryDataType;

	public String categoryStr;

	public Integer categoryNum;

	public BigDecimal categoryFlt;

	public String categoryBool;

}
