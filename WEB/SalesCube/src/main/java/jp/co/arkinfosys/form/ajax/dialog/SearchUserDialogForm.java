/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 担当者検索ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchUserDialogForm extends AbstractSearchForm<UserDto> {

	public String dialogId;

	public String userId;

	public String nameKnj;

	public String nameKana;

	public String deptId;

	public String roleId;

	/**
	 * 部門リスト
	 */
	public List<LabelValueBean> deptList = new ArrayList<LabelValueBean>();

	/**
	 * 権限リスト
	 */
	public List<LabelValueBean> roleList = new ArrayList<LabelValueBean>();

}
