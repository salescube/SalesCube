/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 仕入先マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Supplier extends AuditInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "SUPPLIER_MST";

	@Id
	public String supplierCode;

	public String supplierName;

	public String supplierKana;

	public String supplierAbbr;

	public String supplierZipCode;

	public String supplierAddress1;

	public String supplierAddress2;

	public String supplierDeptName;

	public String supplierPcName;

	public String supplierPcKana;

	public String supplierPcPreCategory;

	public String supplierPcPre;

	public String supplierPcPost;

	public String supplierTel;

	public String supplierFax;

	public String supplierEmail;

	public String supplierUrl;

	public String supplierCmCategory;

	public String taxShiftCategory;

	public String paymentTypeCategory;

	public String paymentCycleCategory;

	public Date lastCutoffDate;

	public Short paymentDate;

	public String taxFractCategory;

	public String priceFractCategory;

	public String poSlipComeoutCategory;

	public String serviceChargeCategory;

	public String transferTypeCategory;

	public String nationalCategory;

	public Integer rateId;

	public String fobName;

	public String remarks;

	public String commentData;

}
