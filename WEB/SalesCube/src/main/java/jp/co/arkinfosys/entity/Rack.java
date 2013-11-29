/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 棚番マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class Rack extends AuditInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "RACK_MST";

	public String warehouseCode;

	public String warehouseName;
	
	@Id
	public String rackCode;

	public String rackName;

	public String rackCategory;

	public String multiFlag;

	public String zipCode;

	public String address1;

	public String address2;

	public String rackPcName;

	public String rackTel;

	public String rackFax;

	public String rackEmail;

}
