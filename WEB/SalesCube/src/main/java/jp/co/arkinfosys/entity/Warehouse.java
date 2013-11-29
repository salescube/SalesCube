package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="WAREHOUSE_MST")
public class Warehouse extends AuditInfo implements Serializable  {

	public static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "WAREHOUSE_MST";
	
	@Id
	@GeneratedValue
	@Column(name = "WAREHOUSE_CODE")
	public String warehouseCode;
	@Column(name = "WAREHOUSE_NAME")
	public String warehouseName;
	@Column(name = "WAREHOUSE_ZIP_CODE")
	public String warehouseZipCode;
	@Column(name = "WAREHOUSE_ADDRESS_1")
	public String warehouseAddress1;
	@Column(name = "WAREHOUSE_ADDRESS_2")
	public String warehouseAddress2;
	@Column(name = "WAREHOUSE_TEL")
	public String warehouseTel;
	@Column(name = "WAREHOUSE_FAX")
	public String warehouseFax;
	@Column(name = "MANAGER_NAME")
	public String managerName;
	@Column(name = "MANAGER_KANA")
	public String managerKana;
	@Column(name = "MANAGER_TEL")
	public String managerTel;
	@Column(name = "MANAGER_FAX")
	public String managerFax;
	@Column(name = "MANAGER_EMAIL")
	public String managerEmail;
	@Column(name = "WAREHOUSE_STATE")
	public String warehouseState;
	@Column(name = "CRE_FUNC")
	public String creFunc;
	@Temporal(TemporalType.DATE)
	@Column(name = "CRE_DATETM")
	public Timestamp creDatetm;
	@Column(name = "CRE_USER")
	public String creUser;
	@Column(name = "UPD_FUNC")
	public String updFunc;
	@Temporal(TemporalType.DATE)
	@Column(name = "UPD_DATETM")
	public Timestamp updDatetm;
	@Column(name = "UPD_USER")
	public String updUser;
	@Column(name = "DEL_FUNC")
	public String delFunc;
	@Temporal(TemporalType.DATE)
	@Column(name = "DEL_DATETM")
	public Timestamp delDatetm;
	@Column(name = "DEL_USER")
	public String delUser;
}
