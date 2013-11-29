/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

/**
 * 部門情報(登録・編集）画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class EditDeptForm {

	/**
	 * 部門ID
	 */
	@Required(arg0 = @Arg(key = "labels.deptId"))
	@Mask(mask = Constants.CODE_MASK.ASCII_ONLY_MASK, msg = @Msg(key = "errors.ascii"), arg0 = @Arg(key = "labels.deptId"))
	@Maxlength(maxlength = 30)
	public String deptId;

	/**
	 * 親部門ID
	 */
	@Maxlength(maxlength = 30)
	public String parentId;

	/**
	 * 部門名
	 */
	@Required(arg0 = @Arg(key = "labels.deptName"))
	@Maxlength(maxlength = 60)
	public String name;

	/**
	 * 作成日時
	 */
	public String creDatetm;

	/**
	 * 作成日時（表示用）
	 */
	public String creDatetmShow;

	/**
	 * 更新日時
	 */
	public String updDatetm;

	/**
	 * 更新日時（表示用）
	 */
	public String updDatetmShow;

	/**
	 * 部門リスト
	 */
	public List<LabelValueBean> parentList = new ArrayList<LabelValueBean>();

	/**
	 * 編集フラグ
	 */
	public boolean editMode = false;

	/**
	 * 更新権限
	 */
	public boolean isUpdate = false;

	/**
	 * フォームを初期化します.
	 */
	public void reset() {
		this.deptId = null;
		this.parentId = null;
		this.name = null;

		this.updDatetm = null;
		this.updDatetmShow = null;
		this.creDatetm = null;
		this.creDatetmShow = null;
		this.editMode = false;
		this.isUpdate = false;
		this.parentList.clear();
	}

	/**
	 * 新規作成時のバリデートを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validateForInsert() {
		ActionMessages errors = new ActionMessages();
		errors.add(this.validate());
		return errors;
	}

	/**
	 * 更新時のバリデートを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validateForUpdate() {
		ActionMessages errors = new ActionMessages();
		errors.add(this.validate());
		return errors;
	}

	/**
	 * 新規・更新時共通のバリデートを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		return errors;
	}
}
