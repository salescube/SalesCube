/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

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
     * 送料区分
     */
    @Required
    public String iniPostageType;

	/**
     * 送料対象金額
     */
    public String targetPostageCharges;

	/**
     * 送料
     */
    public String postage;

    /**
     * 単価端数処理
     */
	public String priceFractCategory;


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

		if(!(this.logoImgPath == null || this.logoImgPath.getFileSize() == 0)){
			String selectFileType = this.logoImgPath.getContentType();
			if(!selectFileType.startsWith("image/") && !selectFileType.endsWith("gif") &&
					!selectFileType.endsWith("png") && !selectFileType.endsWith("jpeg")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.company.logoFileFormat"));
			}

		}


		// 送料有料の場合
		if (CategoryTrns.POSTAGE_PAY.equals(this.iniPostageType)) {
			// 送料対象金額
			if (!StringUtil.hasLength(this.targetPostageCharges)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"warns.company.postage.ctl", MessageResourcesUtil
								.getMessage("labels.company.targetPostageCharges")));
			}else{
				// 送料対象額 数値チェック
				ActionMessage targetPostageChargeError = ValidateUtil.integerType(this.targetPostageCharges, "errors.integer",new Object[] {"送料対象金額"});
				if(targetPostageChargeError != null){
					errors.add(ActionMessages.GLOBAL_MESSAGE, targetPostageChargeError);
				}
			}


			// 送料
			if ((!StringUtil.hasLength(this.postage)) ||  this.postage.equals("0")) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"warns.company.postage.ctl", MessageResourcesUtil
								.getMessage("labels.company.postage")));
			}else{
				// 送料 数値チェック
				ActionMessage postageError = ValidateUtil.integerType(this.postage, "errors.integer",new Object[] {"送料"});
				if(postageError != null){
					errors.add(ActionMessages.GLOBAL_MESSAGE, postageError);
				}
			}

		}else{

			//区分が送料無料の場合は、"0"を設定する
			this.targetPostageCharges = "0";
			this.postage = "0";

		}

		return errors;
    }


}
