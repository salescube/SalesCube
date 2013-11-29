/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Required;

/**
 * 検索結果設定ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DetailDispSettingDialogForm {

	@Required
	public String dialogId;

	@Required
	public String menuId;

	@Required
	public String target;

	public String[] enabledDispItemList;

	/**
	 * 検索対象名
	 */
	public String targetName;

	/**
	 * 表示項目リスト
	 */
	public List<LabelValueBean> enabledItemList = new ArrayList<LabelValueBean>();

	/**
	 * 表示項目リスト(初期状態の記憶用)
	 */
	public String originalEnabledItemList;

	/**
	 * 非表示項目リスト
	 */
	public List<LabelValueBean> disabledItemList = new ArrayList<LabelValueBean>();

	/**
	 * 非表示項目リスト(初期状態の記憶用)
	 */
	public String originalDisabledItemList;

	/**
	 * 必須表示項目リスト
	 */
	public String requiredDispItemIdList;

	/**
	 * 排他制御用の旧設定情報
	 */
	public String lockItemId;

	public String lockUpdDatetm;

	/**
	 * 更新のチェックを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		return errors;
	}
}
