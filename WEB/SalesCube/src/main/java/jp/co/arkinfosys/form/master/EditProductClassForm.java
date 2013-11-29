/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 分類画面（登録・編集）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class EditProductClassForm extends AbstractEditForm {

	/** 登録（編集）分類 */
	public String targetClass;

	/** 親分類（大） */
	public String classCode1;

	/** 親分類（中） */
	public String classCode2;

	/** 分類（小） */
	public String classCode3;

	/** 分類コード */
	public String classCode;

	/** 分類名 */
	@Required
	public String className;

	@Override
	public void initialize() {
		targetClass = "1";
		classCode = "";
		classCode1 = "";
		classCode2 = "";
		classCode3 = "";
		className = "";
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages err = new ActionMessages();

		String labelClassCode1 = MessageResourcesUtil.getMessage("labels.classcode1");
		String labelClassCode2 = MessageResourcesUtil.getMessage("labels.classcode2");
		String labelClassName = MessageResourcesUtil.getMessage("labels.classname");

		// 必須チェック
		if ("2".equals(targetClass)) {	// 分類（中）の場合
			// 分類（大）は必須
			checkRequired(classCode1, labelClassCode1, err);
		} else if ("3".equals(targetClass)) {	// 分類（小）の場合
			// 分類（大）は必須
			checkRequired(classCode1, labelClassCode1, err);
			// 分類（中）は必須
			checkRequired(classCode2, labelClassCode2, err);
		}

		// 分類名の長さチェック
		checkMaxLength(className, 250, labelClassName, err);


		return err;
	}
}
