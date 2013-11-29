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
 * 納入先マスタ履歴テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeliveryHist {

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
	public String deliverDeliveryName;
	public String deliverDeliveryKana;
	public String deliverDeliveryOfficeName;
	public String deliverDeliveryOfficeKana;
	public String deliverDeliveryDeptName;
	public String deliverDeliveryZipCode;
	public String deliverDeliveryAddress1;
	public String deliverDeliveryAddress2;
	public String deliverDeliveryPcName;
	public String deliverDeliveryPcKana;
	public String deliverDeliveryPcPreCategoryCdx;
	public String deliverDeliveryPcPreCategoryNm;
	public String deliverDeliveryTel;
	public String deliverDeliveryFax;
	public String deliverDeliveryEmail;
	public String deliverDeliveryRemarks;
	public Timestamp updDatetm;

}
