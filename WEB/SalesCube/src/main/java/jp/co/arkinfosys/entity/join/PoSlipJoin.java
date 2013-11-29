/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;

/**
 * 発注伝票と仕入マスタのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class PoSlipJoin implements Serializable{
	private static final long serialVersionUID = 1L;

	public Integer poSlipId;
	public Date poDate;
	public String supplierCode;
	public String supplierName;
	public String rateId;
	public String taxShiftCategory;
	public String taxFractCategory;
	public String priceFractCategory;
}
