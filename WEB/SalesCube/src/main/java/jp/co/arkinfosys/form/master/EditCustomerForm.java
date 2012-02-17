/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.master;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.FloatType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 顧客マスタ管理（登録・編集）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class EditCustomerForm extends AbstractEditForm {

	
	
	/** 顧客コード */
	@Required
	@Maxlength(maxlength = Constants.CODE_SIZE.CUSTOMER)
	public String customerCode;

	/** 顧客名 */
	@Required
	@Maxlength(maxlength = 60)
	public String customerName;

	/** 顧客名カナ */
	@Required
	@Maxlength(maxlength = 60)
	public String customerKana;

	/** 事業所名 */
	@Maxlength(maxlength = 60)
	public String customerOfficeName;

	/** 事業所名カナ */
	@Maxlength(maxlength = 60)
	public String customerOfficeKana;

	/** 略称 */
	@Maxlength(maxlength = 14)
	public String customerAbbr;

	/** 部署名 */
	@Maxlength(maxlength = 60)
	public String customerDeptName;

	/** 郵便番号 */
	@Required
	@Maxlength(maxlength = 8)
	public String customerZipCode;

	/** 住所１ */
	@Required
	@Maxlength(maxlength = 50)
	public String customerAddress1;

	/** 住所２ */
	@Maxlength(maxlength = 50)
	public String customerAddress2;

	/** 担当者役職 */
	@Maxlength(maxlength = 60)
	public String customerPcPost;

	/** 担当者名 */
	@Maxlength(maxlength = 60)
	public String customerPcName;

	/** 担当者カナ */
	@Maxlength(maxlength = 60)
	public String customerPcKana;

	/** 担当者敬称 */
	public String customerPcPreCategory;

	/** 電話番号 */
	@Maxlength(maxlength = 15)
	public String customerTel;

	/** FAX */
	@Maxlength(maxlength = 15)
	public String customerFax;

	/** E-MAIL */
	@Maxlength(maxlength = 255)
	public String customerEmail;

	/** URL */
	@Maxlength(maxlength = 60)
	public String customerUrl;

	/** 業種 */
	public String customerBusinessCategory;

	/** 職種 */
	public String customerJobCategory;

	/** 受注停止区分 */
	public String customerRoCategory;

	/** 得意先ランク */
	public String customerRankCategory;

	/** 自動更新チェックボックス */
	public String customerUpdFlag = "0";

	/** 取引区分 */
	@Required
	public String salesCmCategory;

	/** 税転嫁 */
	@Required
	public String taxShiftCategory;

	/** 与信限度額 */
	@FloatType
	public String maxCreditLimit;

	/** 最終締処理日 */
	public String lastCutoffDate;

	/** 支払条件 */
	@Required
	public String cutoffGroupCategory;

	/** 回収方法 */
	@Required
	public String paybackTypeCategory;

	/** 税端数処理 */
	@Required
	public String taxFractCategory;

	/** 単価端数処理 */
	@Required
	public String priceFractCategory;

	/** 請求書発行単位 */
	@Required
	public String billPrintUnit;

	/** 請求書日付有無 */
	@Required
	public String billDatePrint;

	/** 仮納品書出力不可 */
	public String tempDeliverySlipFlag = "1";

	/** 振込名義 */
	@Maxlength(maxlength = 60)
	public String paymentName;

	/** 備考 */
	@Maxlength(maxlength = 120)
	public String remarks;

	/** コメント */
	@Maxlength(maxlength = 1000)
	public String commentData;

	/** 納入先リスト */
	public List<DeliveryAndPre> deliveryList = new ArrayList<DeliveryAndPre>();

	/** 請求先 */
	public DeliveryAndPre billTo = new DeliveryAndPre();

	/**
	 * フォームをリセットします.
	 */
	public void reset() {
		customerUpdFlag = "0";
		tempDeliverySlipFlag = "1";
	}

	/** 選択された納入先（今のところ使わない） */
	public String selectedDelivery;

	public String selectedDeliveryIndex;

	public String newDeliveryName;

	public String newDeliveryKana;

	public String newDeliveryOfficeName;

	public String newDeliveryOfficeKana;

	public String newDeliveryDeptName;

	public String newDeliveryZipCode;

	public String newDeliveryAddress1;

	public String newDeliveryAddress2;

	public String newDeliveryPcName;

	public String newDeliveryPcKana;

	/** 新規追加欄の「敬称」 */
	public String newDeliveryPcPreCategory;

	public String newDeliveryTel;

	public String newDeliveryFax;

	public String newDeliveryEmail;

	public String lastSalesCutoffDate;

	
	/** 敬称 */
	public List<LabelValueBean> preTypeCategoryList = new ArrayList<LabelValueBean>();

	/** 顧客ランク */
	public List<LabelValueBean> customerRankList = new ArrayList<LabelValueBean>();

	/** 受注停止 */
	public List<LabelValueBean> customerRoCategoryList = new ArrayList<LabelValueBean>();

	/** 業種 */
	public List<LabelValueBean> customerBusinessCategoryList = new ArrayList<LabelValueBean>();

	/** 職種 */
	public List<LabelValueBean> customerJobCategoryList = new ArrayList<LabelValueBean>();

	/** 取引区分 */
	public List<LabelValueBean> salesCmCategoryList = new ArrayList<LabelValueBean>();

	/** 税転嫁 */
	public List<LabelValueBean> taxShiftCategoryList = new ArrayList<LabelValueBean>();

	/** 支払方法 */
	public List<LabelValueBean> cutoffGroupList = new ArrayList<LabelValueBean>();

	/** 回収方法 */
	public List<LabelValueBean> paybackTypeCategoryList = new ArrayList<LabelValueBean>();

	/** 税端数処理 */
	public List<LabelValueBean> taxFractCategoryList = new ArrayList<LabelValueBean>();

	/** 単価端数処理 */
	public List<LabelValueBean> priceFractCategoryList = new ArrayList<LabelValueBean>();

	/** 請求書発行単位 */
	public List<LabelValueBean> billPrintUnitList = new ArrayList<LabelValueBean>();

	/** 請求書日付有無 */
	public List<LabelValueBean> billDatePrintList = new ArrayList<LabelValueBean>();

	public List<DeliveryAndPre> originalDeliveryList;

	public void initialize() {
		isUpdate = true;
		editMode = false;
		customerCode = "";
		customerName = "";
		customerKana = "";
		customerOfficeName = "";
		customerOfficeKana = "";
		customerAbbr = "";
		customerDeptName = "";
		customerZipCode = "";
		customerAddress1 = "";
		customerAddress2 = "";
		customerPcPost = "";
		customerPcName = "";
		customerPcKana = "";
		customerPcPreCategory = "";
		customerTel = "";
		customerFax = "";
		customerEmail = "";
		customerUrl = "";
		customerBusinessCategory = "";
		customerJobCategory = "";
		customerRoCategory = "";
		customerRankCategory = "";
		customerUpdFlag = "0";
		salesCmCategory = "";
		taxShiftCategory = "";
		maxCreditLimit = "";
		lastCutoffDate = "";
		cutoffGroupCategory = "";
		paybackTypeCategory = "";
		taxFractCategory = "";
		priceFractCategory = "";
		billPrintUnit = "";
		billDatePrint = "";
		tempDeliverySlipFlag = "1";
		paymentName = "";
		remarks = "";
		commentData = "";
		creDatetmShow = "";
		updDatetm = "";
		updDatetmShow = "";
		selectedDelivery = "";
		selectedDeliveryIndex = "";
		newDeliveryName = "";
		newDeliveryKana = "";
		newDeliveryOfficeName = "";
		newDeliveryOfficeKana = "";
		newDeliveryDeptName = "";
		newDeliveryZipCode = "";
		newDeliveryAddress1 = "";
		newDeliveryAddress2 = "";
		newDeliveryPcName = "";
		newDeliveryPcKana = "";
		newDeliveryPcPreCategory = "";
		newDeliveryTel = "";
		newDeliveryFax = "";
		deliveryList = new ArrayList<DeliveryAndPre>();
		billTo = new DeliveryAndPre();
		preTypeCategoryList.clear();
		customerRankList.clear();
		customerRoCategoryList.clear();
		customerBusinessCategoryList.clear();
		customerJobCategoryList.clear();
		salesCmCategoryList.clear();
		taxShiftCategoryList.clear();
		cutoffGroupList.clear();
		paybackTypeCategoryList.clear();
		taxFractCategoryList.clear();
		priceFractCategoryList.clear();
		originalDeliveryList = null;
		lastSalesCutoffDate = "";
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		
		if (customerUrl == null) {
			customerUrl = "";
		}

		
		String labelCustomerZipCode = MessageResourcesUtil
				.getMessage("labels.customerZipCode");
		String labelDelivery = MessageResourcesUtil
				.getMessage("labels.delivery");
		String labelDeliveryName = MessageResourcesUtil
				.getMessage("labels.deliveryName");
		String labelDeliveryKana = MessageResourcesUtil
				.getMessage("labels.deliveryKana");
		String labelDeliveryOfficeName = MessageResourcesUtil
				.getMessage("labels.deliveryOfficeName");
		String labelDeliveryOfficeKana = MessageResourcesUtil
				.getMessage("labels.deliveryOfficeKana");
		String labelDeliveryDeptName = MessageResourcesUtil
				.getMessage("labels.deliveryDeptName");
		String labelDeliveryZipCode = MessageResourcesUtil
				.getMessage("labels.deliveryZipCode");
		String labelDeliveryAddress1 = MessageResourcesUtil
				.getMessage("labels.deliveryAddress1");
		String labelDeliveryAddress2 = MessageResourcesUtil
				.getMessage("labels.deliveryAddress2");
		String labelDeliveryPcName = MessageResourcesUtil
				.getMessage("labels.deliveryPcName");
		String labelDeliveryPcKana = MessageResourcesUtil
				.getMessage("labels.deliveryPcKana");
		String labelDeliveryTel = MessageResourcesUtil
				.getMessage("labels.deliveryTel");
		String labelDeliveryEmail = MessageResourcesUtil
				.getMessage("labels.deliveryEmail");
		String labelDeliveryFax = MessageResourcesUtil
				.getMessage("labels.deliveryFax");
		String labelBillName = MessageResourcesUtil
				.getMessage("labels.billName");
		String labelBillKana = MessageResourcesUtil
				.getMessage("labels.billKana");
		String labelBillOfficeName = MessageResourcesUtil
				.getMessage("labels.billOfficeName");
		String labelBillOfficeKana = MessageResourcesUtil
				.getMessage("labels.billOfficeKana");
		String labelBillDeptName = MessageResourcesUtil
				.getMessage("labels.billDeptName");
		String labelBillZipCode = MessageResourcesUtil
				.getMessage("labels.billZipCode");
		String labelBillAddress1 = MessageResourcesUtil
				.getMessage("labels.billAddress1");
		String labelBillAddress2 = MessageResourcesUtil
				.getMessage("labels.billAddress2");
		String labelBillPcName = MessageResourcesUtil
				.getMessage("labels.billPcName");
		String labelBillPcKana = MessageResourcesUtil
				.getMessage("labels.billPcKana");
		String labelBillPcPreTypeCategory = MessageResourcesUtil
				.getMessage("labels.billPcPreTypeCategory");
		String labelBillTel = MessageResourcesUtil.getMessage("labels.billTel");
		String labelBillEmail = MessageResourcesUtil
				.getMessage("labels.billEmail");
		String labelBillFax = MessageResourcesUtil.getMessage("labels.billFax");

		ActionMessages errors = new ActionMessages();

		
		if (deliveryList != null && deliveryList.size() > 0) {
			int index = 1;
			for (DeliveryAndPre delivery : deliveryList) {
				
				if (delivery.deliveryEmail == null) {
					delivery.deliveryEmail = "";
				}
				if (delivery.deliveryUrl == null) {
					delivery.deliveryUrl = "";
				}
				if (delivery.remarks == null) {
					delivery.remarks = "";
				}

				
				checkRequired(index, delivery.deliveryName, labelDeliveryName,
						errors);
				
				checkRequired(index, delivery.deliveryKana, labelDeliveryKana,
						errors);
				
				checkRequired(index, delivery.deliveryZipCode,
						labelDeliveryZipCode, errors);
				
				checkRequired(index, delivery.deliveryAddress1,
						labelDeliveryAddress1, errors);
				index++;
			}
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.required", labelDelivery));
		}

		
		
		if (billTo.deliveryEmail == null) {
			billTo.deliveryEmail = "";
		}
		if (billTo.deliveryUrl == null) {
			billTo.deliveryUrl = "";
		}
		if (billTo.remarks == null) {
			billTo.remarks = "";
		}
		
		checkRequired(billTo.deliveryName, labelBillName, errors);
		
		checkRequired(billTo.deliveryKana, labelBillKana, errors);
		
		checkRequired(billTo.deliveryZipCode, labelBillZipCode, errors);
		
		checkRequired(billTo.deliveryAddress1, labelBillAddress1, errors);
		
		checkRequired(billTo.deliveryPcPreCategory, labelBillPcPreTypeCategory,
				errors);

		
		
		
		
		

		
		if (deliveryList != null) {
			int index = 1;
			for (DeliveryAndPre delivery : deliveryList) {
				
				checkMaxLength(index, delivery.deliveryName, 60,
						labelDeliveryName, errors);
				
				checkMaxLength(index, delivery.deliveryKana, 60,
						labelDeliveryKana, errors);
				
				checkMaxLength(index, delivery.deliveryOfficeName, 60,
						labelDeliveryOfficeName, errors);
				
				checkMaxLength(index, delivery.deliveryOfficeKana, 60,
						labelDeliveryOfficeKana, errors);
				
				checkMaxLength(index, delivery.deliveryDeptName, 60,
						labelDeliveryDeptName, errors);
				
				checkMaxLength(index, delivery.deliveryAddress1, 50,
						labelDeliveryAddress1, errors);
				
				checkMaxLength(index, delivery.deliveryAddress2, 50,
						labelDeliveryAddress2, errors);
				
				checkMaxLength(index, delivery.deliveryPcName, 60,
						labelDeliveryPcName, errors);
				
				checkMaxLength(index, delivery.deliveryPcKana, 60,
						labelDeliveryPcKana, errors);
				
				checkMaxLength(index, delivery.deliveryTel, 15,
						labelDeliveryTel, errors);
				
				checkMaxLength(index, delivery.deliveryFax, 15,
						labelDeliveryFax, errors);
				
				checkMaxLength(index, delivery.deliveryEmail, 255,
						labelDeliveryEmail, errors);
				index++;
			}
		}

		
		
		checkMaxLength(billTo.deliveryName, 60, labelBillName, errors);
		
		checkMaxLength(billTo.deliveryKana, 60, labelBillKana, errors);
		
		checkMaxLength(billTo.deliveryOfficeName, 60, labelBillOfficeName,
				errors);
		
		checkMaxLength(billTo.deliveryOfficeKana, 60, labelBillOfficeKana,
				errors);
		
		checkMaxLength(billTo.deliveryDeptName, 60, labelBillDeptName, errors);
		
		checkMaxLength(billTo.deliveryAddress1, 50, labelBillAddress1, errors);
		
		checkMaxLength(billTo.deliveryAddress2, 50, labelBillAddress2, errors);
		
		checkMaxLength(billTo.deliveryPcName, 60, labelBillPcName, errors);
		
		checkMaxLength(billTo.deliveryPcKana, 60, labelBillPcKana, errors);
		
		checkMaxLength(billTo.deliveryTel, 15, labelBillTel, errors);
		
		checkMaxLength(billTo.deliveryFax, 15, labelBillFax, errors);
		
		checkMaxLength(billTo.deliveryEmail, 255, labelBillEmail, errors);

		

		
		checkMaxLength(customerZipCode, 8, labelCustomerZipCode, errors);

		if (deliveryList != null) {
			int index = 1;
			for (DeliveryAndPre delivery : deliveryList) {
				
				checkMaxLength(index, delivery.deliveryZipCode, 8,
						labelDeliveryZipCode, errors);
			}
		}

		
		checkMaxLength(billTo.deliveryZipCode, 8, labelBillZipCode, errors);

		return errors;
	}
}
