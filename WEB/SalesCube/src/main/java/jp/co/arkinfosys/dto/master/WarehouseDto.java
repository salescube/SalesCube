/*
 * Copyright 2009-2011 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import jp.co.arkinfosys.entity.join.RackJoin;

/**
 * 倉庫マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class WarehouseDto implements Serializable, MasterEditDto {

	public static final long serialVersionUID = 1L;

	public String warehouseCode;

	public String warehouseName;

	public String warehouseZipCode;

	public String warehouseAddress1;

	public String warehouseAddress2;

	public String warehouseTel;

	public String warehouseFax;

	public String warehouseEmail;

	public String managerName;

	public String managerKana;

	public String managerTel;

	public String managerFax;

	public String managerEmail;

	public String warehouseState;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	public String delFunc;

	public Timestamp delDatetm;

	public String delUser;
	
	public List<RackJoin> rackJoinList;

	/**
	 * 倉庫コードを取得します.
	 * @return　 倉庫コード
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.warehouseCode };
	}
}
