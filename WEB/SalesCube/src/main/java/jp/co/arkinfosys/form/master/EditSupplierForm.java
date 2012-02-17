/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.master;


import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 仕入先画面（登録・編集）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class EditSupplierForm extends AbstractEditForm {

	/** 仕入先コード */
	@Required
	public String supplierCode;

	/** 仕入先名 */
	@Required
	public String supplierName;

	/** 仕入先カナ */
	@Required
	public String supplierKana;

	/** 仕入先略称 */
	public String supplierAbbr;

	/** 仕入先郵便番号 */
	@Required
	@Maxlength(maxlength = 8)
	public String supplierZipCode;

	/** 仕入先住所１ */
	@Required
	public String supplierAddress1;

	/** 仕入先住所２ */
	public String supplierAddress2;

	/** 仕入先担当者 */
	public String supplierPcName;

	/** 仕入先担当者カナ */
	public String supplierPcKana;

	/** 仕入先担当者敬称 */
	@Required(msg = @Msg(key = "errors.required")
			,arg0 = @Arg(key = "labels.pcPrefix", resource = true, position = 0))
	public String supplierPcPreCategory;

	/** 仕入先部署名 */
	public String supplierDeptName;

	/** 仕入先担当者役職名 */
	public String supplierPcPost;

	/** 仕入先TEL */
	public String supplierTel;

	/** 仕入先FAX */
	public String supplierFax;

	/** 仕入先E-MAIL */
	public String supplierEmail;

	/** 仕入取引区分 */
	@Required
	public String supplierCmCategory;

	/** 税転嫁 */
	@Required
	public String taxShiftCategory;

	/** 支払方法 */
	@Required
	public String paymentTypeCategory;

	/** 支払間隔 */
	@Required
	public String paymentCycleCategory;

	/** 最終締処理日 */
	public String lastCutoffDate;

	/** 支払日 */
	public String paymentDate;

	/** レート */
	public String rateId;

	/** 振込方法 */
	@Required
	public String transferTypeCategory;

	/** 税端数処理 */
	@Required
	public String taxFractCategory;

	/** 単価端数処理 */
	@Required
	public String priceFractCategory;

	/** 発注伝票発行 */
	public String poSlipComeoutCategory;

	/** 手数料負担 */
	@Required
	public String serviceChargeCategory;

	/** F.O.B */
	public String fobName;

	/** 備考 */
	public String remarks;

	/** コメント */
	public String commentData;

	/**
	 * 初期値を設定します.
	 */
	public void reset() {
		poSlipComeoutCategory = "0";
	}

	public void initialize() {
		isUpdate = true;
		editMode = false;
		supplierCode = "";
		supplierName = "";
		supplierKana = "";
		supplierAbbr = "";
		supplierZipCode = "";
		supplierAddress1 = "";
		supplierAddress2 = "";
		supplierPcName = "";
		supplierPcKana = "";
		supplierPcPreCategory = "";
		supplierDeptName = "";
		supplierPcPost = "";
		supplierTel = "";
		supplierFax = "";
		supplierEmail = "";
		supplierCmCategory = "";
		taxShiftCategory = "";
		paymentTypeCategory = "";
		paymentCycleCategory = "";
		lastCutoffDate = "";
		paymentDate = "";
		rateId = "";
		transferTypeCategory = "";
		taxFractCategory = "";
		priceFractCategory = "";
		poSlipComeoutCategory = "";
		serviceChargeCategory = "";
		remarks = "";
		commentData = "";
		creDatetmShow = "";
		updDatetm = "";
		updDatetmShow = "";
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		
		String labelSupplierName = MessageResourcesUtil
				.getMessage("labels.supplierName");
		String labelSupplierKana = MessageResourcesUtil
				.getMessage("labels.supplierKana");
		String labelSupplierAbbr = MessageResourcesUtil
				.getMessage("labels.supplierAbbr");
		String labelSupplierZipCode = MessageResourcesUtil
				.getMessage("labels.supplierZipCode");
		String labelSupplierAddress1 = MessageResourcesUtil
				.getMessage("labels.supplierAddress1");
		String labelSupplierAddress2 = MessageResourcesUtil
				.getMessage("labels.supplierAddress2");
		String labelSupplierTel = MessageResourcesUtil
				.getMessage("labels.supplierTel");
		String labelSupplierFax = MessageResourcesUtil
				.getMessage("labels.supplierFax");
		String labelSupplierEmail = MessageResourcesUtil
				.getMessage("labels.supplierEmail");
		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");
		String labelCommentData = MessageResourcesUtil
				.getMessage("labels.commentData");

		ActionMessages errors = new ActionMessages();
		

		
		
		checkMaxLength(supplierName, 60, labelSupplierName, errors);
		
		checkMaxLength(supplierKana, 60, labelSupplierKana, errors);
		
		checkMaxLength(supplierAbbr, 14, labelSupplierAbbr, errors);
		
		checkMaxLength(supplierAddress1, 50, labelSupplierAddress1, errors);
		
		checkMaxLength(supplierAddress2, 50, labelSupplierAddress2, errors);
		
		checkMaxLength(supplierTel, 15, labelSupplierTel, errors);
		
		checkMaxLength(supplierFax, 15, labelSupplierFax, errors);
		
		checkMaxLength(supplierEmail, 255, labelSupplierEmail, errors);
		
		checkMaxLength(remarks, 120, labelRemarks, errors);
		
		checkMaxLength(commentData, 1000, labelCommentData, errors);

		
		checkMaxLength(supplierZipCode, 8, labelSupplierZipCode, errors);

		return errors;
	}
}
