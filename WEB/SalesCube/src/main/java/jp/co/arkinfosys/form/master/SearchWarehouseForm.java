/*
 * Copyright 2009-2011 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

public class SearchWarehouseForm extends AbstractSearchForm<WarehouseDto> {

	/** 倉庫コード */
	public String warehouseCode;

	/** 倉庫名 */
	public String warehouseName;
	
	/** 倉庫状況 */
	public String warehouseState;
}
