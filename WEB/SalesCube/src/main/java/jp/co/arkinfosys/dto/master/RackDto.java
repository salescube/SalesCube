/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.io.Serializable;
import java.util.List;

/**
 * 棚番マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class RackDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	public String warehouseCode;

	public String warehouseName;

	public String rackCode;

	public String rackName;

	public String rackCategory;

	public String rackCategoryName;

	public String multiFlag;

	public String zipCode;

	public String address1;

	public String address2;

	public String rackPcName;

	public String rackTel;

	public String rackFax;

	public String rackEmail;

	public String updDatetm;

	public List<String> duplicateList;

	public List<String> productCodeList;

	public List<String> productNameList;

	public boolean exist;

	/**
	 * 棚番コードを取得します.
	 * @return　 棚番コード
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.rackCode };
	}


}
