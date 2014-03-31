/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 社員情報（検索）画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class SearchUserForm extends AbstractSearchForm<UserDto> {

	/**
	 * 社員コード
	 */
	public String userId;

	/**
	 * ロックフラグ
	 */
	public String lockflg;

	/**
	 * ロックフラグ
	 */
	public String lockdatetm;

	/**
	 * 社員名
	 */
	public String nameKnj;
	/**
	 * 社員名カナ
	 */
	public String nameKana;
	/**
	 * 部門
	 */
	public String deptId;
	/**
	 * E-MAIL
	 */
	public String email;

	/**
	 * 部門リスト
	 */
	public List<LabelValueBean> deptList = new ArrayList<LabelValueBean>();;

}
