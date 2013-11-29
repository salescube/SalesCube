/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;


import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.setting.DeptDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 部門情報（検索）画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class SearchDeptForm extends AbstractSearchForm<DeptDto> {

	/** 部門ID */
	public String deptId;

	/** 親部門ID */
	public String parentId;

	/** 部門名 */
	public String name;

	/**
	 * 親部門リスト
	 */
	public List<LabelValueBean> parentList = new ArrayList<LabelValueBean>();;

}
