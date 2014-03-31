/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

/**
 * 自社情報画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class CompanyForm {
	/**
    * 会社名
    */
    @Required
    @Maxlength(maxlength = 60)
    public String companyName;
	/**
     * 会社略名
     */
    @Required
    @Maxlength(maxlength = 14)
    public String companyAbbr;
	/**
     * 会社名カナ
     */
    @Required
    @Maxlength(maxlength = 60)
    public String companyKana;
	/**
     * 代表取締役
     */
    @Required
    @Maxlength(maxlength = 14)
    public String companyCeoName;
	/**
     * 代表者肩書
     */
    @Required
    @Maxlength(maxlength = 20)
    public String companyCeoTitle;
	/**
     * 会社ロゴファイル名
     */
	@Binding(bindingType = BindingType.NONE)
	public FormFile logoImgPath;
	/**
     * 会社ロゴ初期化
     */
	public boolean logoInit;
	/**
     * 郵便番号
     */
    @Required
    @Maxlength(maxlength = 8)
    public String companyZipCode;
	/**
     * 住所1
     */
    @Required
    @Maxlength(maxlength = 50)
    public String companyAddress1;
	/**
     * 住所2
     */
    @Maxlength(maxlength = 50)
    public String companyAddress2;
	/**
     * TEL
     */
    @Required
    @Maxlength(maxlength = 13)
    public String companyTel;
	/**
     * FAX
     */
    @Required
    @Maxlength(maxlength = 13)
    public String companyFax;
	/**
     * E-MAIL
     */
    @Required
    @Maxlength(maxlength = 30)
    public String companyEmail;
	/**
     * Webサイト
     */
    @Maxlength(maxlength = 60)
    public String companyWebSite;
	/**
     * 締日
     */
    public String cutoffGroup;
	/**
     * 決算月
     */
    public String closeMonth;


	/**
     * 更新日時
     */
    public String updDatetm;

    public boolean isUpdate = false;

    /**
     * 登録時のバリデートを行います.
     * @return 表示するメッセージ
     */
    public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		if(this.logoImgPath == null || this.logoImgPath.getFileSize() == 0){
			return errors;
		}

		String selectFileType = this.logoImgPath.getContentType();
		if(!selectFileType.startsWith("image/") && !selectFileType.endsWith("gif") &&
				!selectFileType.endsWith("png") && !selectFileType.endsWith("jpeg")) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.company.logoFileFormat"));
		}

		return errors;
    }

}
