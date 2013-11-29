/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 棚番検索ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchRackDialogForm extends AbstractSearchForm<RackDto> {

	public String dialogId;
	
	public String warehouseCode;

	public String warehouseName;

	public String warehouseState;
	
	public String rackCode;

	public String rackName;

	public String rackCategory;

	public boolean emptyRack;

	public List<LabelValueBean> rackCategoryList = new ArrayList<LabelValueBean>();

}
