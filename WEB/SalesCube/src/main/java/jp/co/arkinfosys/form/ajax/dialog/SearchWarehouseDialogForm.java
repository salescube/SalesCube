/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

/**
 * 棚番検索ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchWarehouseDialogForm extends AbstractSearchForm<WarehouseDto>{

	/** ダイアログId */
	public String dialogId;

	/** 倉庫コード */
	public String warehouseCode;

	/** 倉庫名 */
	public String warehouseName;
	
	/** 倉庫状況 */
	public String warehouseState;
}
