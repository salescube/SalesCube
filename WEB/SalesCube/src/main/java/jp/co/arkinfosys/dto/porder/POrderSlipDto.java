/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.porder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 発注伝票情報を管理するDTOクラスです
 *
 * @author Ark Information Systems
 *
 */
public class POrderSlipDto implements Serializable{

	private static final long serialVersionUID = 1L;

	public Integer poSlipId;
	public String status;
	public Date poDate;
	public Short poAnnual;
	public Short poMonthly;
	public Integer poYm;
	public Date deliveryDate;
	public String userId;
	public String userName;
	public String remarks;
	public String supplierCode;
	public String supplierName;
	public String supplierKana;
	public String supplierZipCode;
	public String supplierAddress1;
	public String supplierAddress2;
	public String supplierPcName;
	public String supplierPcKana;
	public String supplierPcPreCategory;
	public String supplierPcPost;
	public String supplierTel;
	public String supplierFax;
	public String supplierEmail;
	public String supplierUrl;
	public String transportCategory;
	public String taxShiftCategory;
	public Integer rateId;
	public String supplierCmCategory;
	public BigDecimal priceTotal;
	public BigDecimal ctaxTotal;
	public BigDecimal ctaxRate;
	public BigDecimal fePriceTotal;
	public Integer printCount;
	public String creFunc;
	public Timestamp creDatetm;
	public String creUser;
	public String updFunc;
	public Timestamp updDatetm;
	public String updUser;
}