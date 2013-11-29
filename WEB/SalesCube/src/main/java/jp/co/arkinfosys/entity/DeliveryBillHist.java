/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 請求先の履歴情報のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeliveryBillHist {

	private static final long serialVersionUID = 1L;
	@Transient

	public static final String TABLE_NAME = "DELIVERY_MST_HIST";

	@Id
	@GeneratedValue
	@Column(name = "HIST_ID")
	public Integer histId;
	/**
	 *
	 */
	@Column(name = "ACTION_TYPE")
	public String actionType;
	/**
	 *
	 */
	public String deliverCode;
	public String billDeliveryName;
	public String billDeliveryKana;
	public String billDeliveryOfficeName;
	public String billDeliveryOfficeKana;
	public String billDeliveryDeptName;
	public String billDeliveryZipCode;
	public String billDeliveryAddress1;
	public String billDeliveryAddress2;
	public String billDeliveryPcName;
	public String billDeliveryPcKana;
	public String billDeliveryPcPreCategoryCdx;
	public String billCategoryCodeName;
	public String billDeliveryTel;
	public String billDeliveryFax;
	public String billDeliveryEmail;
	public String billDeliveryRemarks;
	public Timestamp updDatetm;

}
