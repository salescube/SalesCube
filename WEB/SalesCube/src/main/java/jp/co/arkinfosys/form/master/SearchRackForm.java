/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.List;

import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 棚番画面（検索）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchRackForm extends AbstractSearchForm<RackDto> {

	/** 倉庫コード */
	public String warehouseCode;

	/** 倉庫名 */
	public String warehouseName;
	
	/** 倉庫状況 */
	public String warehouseState;
	
	/** 棚番コード */
	public String rackCode;

	/** 棚番名 */
	public String rackName;

	/** 空き棚 */
	public boolean emptyRack;

	/** 分類 */
	public String rackCategory;

	/** 倉庫分類リスト */
	public List<LabelValueBean> rackList;

	/**
	 * フォームを初期化します.
	 */
	public void reset() {
		this.emptyRack = false;
	}

}
