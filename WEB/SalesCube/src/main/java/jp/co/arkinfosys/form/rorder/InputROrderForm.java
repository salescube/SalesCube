/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.rorder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.DiscountUtil;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.SlipStatusCategoryTrns;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.entity.OnlineOrderWork;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 受注入力画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.REQUEST)
public class InputROrderForm extends AbstractSlipEditForm<ROrderLineDto> {

	public static final long serialVersionUID = 1L;

	/** 受注番号 */
	@IntegerType
	public String roSlipId;

	/** 状態コード */
	public String status;

	/** 受注日 */
	@Required
	@DateType
	public String roDate;

	/** 出荷日 */
	@DateType
	public String shipDate;

	/** 納期指定日 */
	@DateType(arg0 = @Arg(key = "labels.deliveryDate2"))
	public String deliveryDate;

	/** 受付番号 */
	public String receptNo;

	/** 客先伝票番号 */
	public String customerSlipNo;

	/** 担当者コード */
	public String userId;

	/** 担当者名 */
	public String userName;

	/** 備考 */
	public String remarks;

	/** 顧客コード */
	@Required
	public String customerCode;

	/** 顧客名 */
	public String customerName;

	/** 支払条件 */
	public String cutoffGroupCategory;
	public String cutoffGroup;
	public String paybackCycleCategory;

	/** 取引区分 */
	public String salesCmCategory;

	/** 備考（顧客） */
	public String customerRemarks;

	/** コメント（顧客） */
	public String customerCommentData;

	/** 納入先コード */
	@IntegerType
	public String deliveryCode;

	/** 納入先名 */
	public String deliveryName;

	/** 納入先名カナ */
	public String deliveryKana;

	/** 事業所名 */
	public String deliveryOfficeName;

	/** 事業所名カナ */
	public String deliveryOfficeKana;

	/** 部署名 */
	public String deliveryDeptName;

	/** 郵便番号 */
	public String deliveryZipCode;

	/** 住所１ */
	public String deliveryAddress1;

	/** 住所２ */
	public String deliveryAddress2;

	/** （納入先）担当者 */
	public String deliveryPcName;

	/** （納入先）担当者カナ */
	public String deliveryPcKana;

	/** 敬称（コード） */
	public String deliveryPcPreCategory;

	/** 敬称（名称） */
	public String deliveryPcPre;

	/** 電話番号 */
	public String deliveryTel;

	/** E-MAIL */
	public String deliveryEmail;

	/** FAX */
	public String deliveryFax;

	/** URL */
	public String deliveryUrl;

	/** 明細行 */
	public List<ROrderLineDto> lineList = new ArrayList<ROrderLineDto>();

	/** 粗利益 */
	public String gross;

	/** 原価金額伝票合計 */
	public String costTotal;

	/** 粗利益率 */
	public String grossRatio;

	/** 金額合計 */
	public String retailPriceTotal;

	/** 消費税 */
	public String ctaxPriceTotal;

	/** 伝票合計 */
	public String priceTotal;

	/** 削除可能フラグ */
	public boolean deletable = true;

	/** 状態フラグからの更新権限 */
	public boolean statusUpdate = true;

	/** 完納区分初期値 */
	public String defaultStatusName;
	public String defaultStatusCode;
	public String nowPurchasingStatusName; 
	public String finishStatusName; 

	/** 通販サイト受注データかどうか */
	public boolean isOnlineOrder;

	/** 取り込みデータかどうか */
	public boolean isImport;

	/** オンライン受注ＩＤ */
	public String onlineOrderId;

	public String dcCategory; 
	public String dcName; 
	public String dcTimezoneCategory; 
	public String dcTimezone; 

	public CategoryService categoryService;

	/**
	 * 初期値を設定します.
	 */
	public void reset() {
		roSlipId = "";

		/** 受注日 */
		roDate = null;

		/** 状態コード */
		status = "";

		/** 出荷日 */
		shipDate = null;

		/** 納期指定日 */
		deliveryDate = null;

		/** 受付番号 */
		receptNo = "";

		/** 客先伝票番号 */
		customerSlipNo = "";

		/** 担当者コード */
		userId = "";

		/** 担当者名 */
		userName = "";

		/** 備考 */
		remarks = "";

		/** 顧客コード */
		customerCode = "";

		/** 顧客名 */
		customerName = "";

		/** 税転嫁 */
		taxShiftCategory = "";

		/** 税端数処理 */
		taxFractCategory = "";

		/** 支払条件 */
		cutoffGroupCategory = "";

		/** 取引区分 */
		salesCmCategory = "";

		/** 備考（顧客） */
		customerRemarks = "";

		/** コメント（顧客） */
		customerCommentData = "";

		/** 納入先コード */
		deliveryCode = "";

		/** 納入先名 */
		deliveryName = "";

		/** 納入先名カナ */
		deliveryKana = "";

		/** 事業所名 */
		deliveryOfficeName = "";

		/** 事業所名カナ */
		deliveryOfficeKana = "";

		/** 部署名 */
		deliveryDeptName = "";

		/** 郵便番号 */
		deliveryZipCode = "";

		/** 住所１ */
		deliveryAddress1 = "";

		/** 住所２ */
		deliveryAddress2 = "";

		/** （納入先）担当者 */
		deliveryPcName = "";

		/** （納入先）担当者カナ */
		deliveryPcKana = "";

		/** 敬称（コード） */
		deliveryPcPreCategory = "";

		/** 敬称（名称） */
		deliveryPcPre = "";

		/** 電話番号 */
		deliveryTel = "";

		/** E-MAIL */
		deliveryEmail = "";

		/** FAX */
		deliveryFax = "";

		/** URL */
		deliveryUrl = "";

		/** 最終更新日時（排他制御用） */
		updDatetm = "";

		/** 明細行 */
		lineList = new ArrayList<ROrderLineDto>();

		/** 粗利益 */
		gross = "";

		/** 原価金額伝票合計 */
		costTotal = "";

		/** 粗利益率 */
		grossRatio = "";

		/** 金額合計 */
		retailPriceTotal = "";

		/** 消費税 */
		ctaxPriceTotal = "";

		/** 伝票合計 */
		priceTotal = "";

		/** 単価端数処理 */
		priceFractCategory = "";

		/** 画面上で削除されたもの */
		deleteLineIds = "";

		/** 更新権限 */
		menuUpdate = false;

		/** 削除可能フラグ */
		deletable = true;

		/** 状態フラグからの更新権限 */
		statusUpdate = true;

		/** 税率 */
		taxRate = "";

		/** 完納区分初期値 */
		try {
			this.defaultStatusName = this.categoryService
					.findCategoryNameByIdAndCode(
							SlipStatusCategories.RO_LINE_STATUS,
							SlipStatusCategoryTrns.RO_LINE_NEW);

			this.nowPurchasingStatusName = this.categoryService
					.findCategoryNameByIdAndCode(
							SlipStatusCategories.RO_LINE_STATUS,
							Constants.STATUS_RORDER_LINE.NOWPURCHASING);

			this.finishStatusName = this.categoryService
					.findCategoryNameByIdAndCode(
							SlipStatusCategories.RO_LINE_STATUS,
							Constants.STATUS_RORDER_LINE.SALES_FINISH);

		} catch (ServiceException e) {
		}
		this.defaultStatusCode = SlipStatusCategoryTrns.RO_LINE_NEW;

		/** オンライン受注データかどうか */
		isOnlineOrder = false;
		isImport = false;

		/** オンライン受注ＩＤ */
		onlineOrderId = "";

		initDc();
		dcName = ""; 
		dcTimezoneCategory = ""; 
		dcTimezone = ""; 

	}

	/**
	 *  配送業者コードの初期化を行います.
	 */
	public void initDc() {
		dcCategory = CategoryTrns.DC_CATEGORY_1; 
	}

	/**
	 * フォームをセットアップします.
	 * @param list　オンライン受注データ
	 */
	public void setUp(List<OnlineOrderWork> list) {

		if (list.size() == 0) {
			return;
		}

		
		OnlineOrderWork work = list.get(0);

		
		this.roSlipId = "";

		
		this.roDate = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);

		
		this.deliveryDate = "";

		
		this.shipDate = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);
		;

		
		this.receptNo = work.onlineOrderId;

		
		this.customerSlipNo = "";

		
		this.remarks = work.deliveryInst;

		
		this.customerCode = Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER;

		

		
		this.deliveryName = work.address3;

		
		this.deliveryOfficeName = "";
		this.deliveryOfficeKana = "";

		
		this.deliveryDeptName = "";

		
		this.deliveryZipCode = work.zipCode;

		
		this.deliveryAddress1 = work.state + work.address1;
		this.deliveryAddress2 = work.address2;

		
		this.deliveryPcName = work.recipientName;
		this.deliveryPcKana = "";

		
		this.deliveryPcPre = "";
		this.deliveryPcPreCategory = CategoryTrns.PREFIX_SAMA;

		
		this.deliveryTel = work.shipTel;

		
		this.deliveryEmail = "";

		
		this.deliveryFax = "";

		
		this.onlineOrderId = work.onlineOrderId;
	}

	/**
	 * 伝票登録時のバリデータを行います.
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		String labelProductCode = MessageResourcesUtil
				.getMessage("labels.productCode");

		String labelQuantity = MessageResourcesUtil
				.getMessage("labels.quantity");
		String labelUnitCost = MessageResourcesUtil
				.getMessage("labels.unitCost");
		String labelCost = MessageResourcesUtil.getMessage("labels.cost");
		String labelUnitRetailPrice = MessageResourcesUtil
				.getMessage("labels.unitRetailPrice");
		String labelRetailPrice = MessageResourcesUtil
				.getMessage("labels.retailPrice");

		String labelMemorandum = MessageResourcesUtil
				.getMessage("labels.memorandum");
		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");

		String labelReceptNo = MessageResourcesUtil
				.getMessage("labels.receptNo");
		String labelEadRemarks = MessageResourcesUtil
				.getMessage("labels.eadRemarks");
		String labelCustomerSlipNo = MessageResourcesUtil
				.getMessage("labels.customerSlipNo");
		String labelCustomerCode = MessageResourcesUtil
				.getMessage("labels.customerCode");
		String labelProductRemarks = MessageResourcesUtil
				.getMessage("labels.productRemarks");

		
		if (Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER
				.equals(this.customerCode)) {
			
			if (!StringUtil.hasLength(this.deliveryPcName)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.onlineorder.required", MessageResourcesUtil
								.getMessage("labels.deliveryPcName")));
			}

			
			if (!StringUtil.hasLength(this.deliveryZipCode)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.onlineorder.required", MessageResourcesUtil
								.getMessage("labels.deliveryZipCode")));
			}

			
			if (!StringUtil.hasLength(this.deliveryAddress1)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.onlineorder.required", MessageResourcesUtil
								.getMessage("labels.deliveryAddress1")));
			}

			
			if (!StringUtil.hasLength(this.deliveryTel)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.onlineorder.required", MessageResourcesUtil
								.getMessage("labels.deliveryTel")));
			}
		}

		boolean inputLine = false;

		
		
		if (StringUtil.hasLength(this.receptNo) && this.receptNo.length() > 30) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.maxlength", labelReceptNo, "30"));
		}

		
		if (StringUtil.hasLength(this.customerSlipNo)
				&& this.customerSlipNo.length() > 30) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.maxlength", labelCustomerSlipNo, "20"));
		}

		
		if (StringUtil.hasLength(this.remarks) && this.remarks.length() > 120) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.maxlength", labelMemorandum, "120"));
		}

		
		if (StringUtil.hasLength(this.customerCode)
				&& this.customerCode.length() > Constants.CODE_SIZE.CUSTOMER) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.maxlength", labelCustomerCode,
					Constants.CODE_SIZE.CUSTOMER));
		}

		
		try {
			Date dtRoDate = DateFormat.getDateInstance().parse(this.roDate);
			Date dtShipDate = DateFormat.getDateInstance().parse(this.shipDate);
			Date dtDeliveryDate = DateFormat.getDateInstance().parse(
					this.deliveryDate);
			
			if (StringUtil.hasLength(this.roDate)
					&& StringUtil.hasLength(this.shipDate)) {
				if (dtRoDate.after(dtShipDate)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.date.roship"));

				}
			}
			
			if (StringUtil.hasLength(this.roDate)
					&& StringUtil.hasLength(this.deliveryDate)) {
				if (dtRoDate.after(dtDeliveryDate)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.date.rodelivery"));

				}
			}
			
			if (StringUtil.hasLength(this.shipDate)
					&& StringUtil.hasLength(this.deliveryDate)) {
				if (dtShipDate.after(dtDeliveryDate)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.date.shipdelivery"));

				}
			}
		} catch (ParseException e) {
			
		}

		for (ROrderLineDto line : lineList) {
			
			if (!StringUtil.hasLength(line.productCode)
					&& !StringUtil.hasLength(line.quantity)
					&& !StringUtil.hasLength(line.unitRetailPrice)
					&& !StringUtil.hasLength(line.retailPrice)) {
				continue;
			}

			
			inputLine = true;

			
			
			if (!StringUtil.hasLength(line.productCode)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelProductCode));
			}

			
			if (!StringUtil.hasLength(line.quantity)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelQuantity));
			}

			
			if (!StringUtil.hasLength(line.unitCost)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelUnitCost));
			}

			
			if (!StringUtil.hasLength(line.cost)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelCost));
			}

			
			if (!StringUtil.hasLength(line.unitRetailPrice)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo,
						labelUnitRetailPrice));
			}

			
			if (!StringUtil.hasLength(line.retailPrice)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelRetailPrice));
			}

			
			
			if (StringUtil.hasLength(line.productCode)
					&& line.productCode.length() > 20) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo, labelProductCode,
						"20"));
			}
			
			if (StringUtil.hasLength(line.remarks)
					&& line.remarks.length() > 120) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo, labelRemarks,
						"120"));
			}
			
			if (StringUtil.hasLength(line.eadRemarks)
					&& line.eadRemarks.length() > 120) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo, labelEadRemarks,
						"120"));
			}

			
			if (StringUtil.hasLength(line.productRemarks)
					&& line.productRemarks.length() > 120) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo,
						labelProductRemarks, "120"));
			}

			
			
			
			if (StringUtil.hasLength(line.quantity)) {
				try {
					
					
					
					float fquantity = Float.parseFloat(line.quantity);
					if (Double.compare(fquantity, 0) == 0) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.num0",
										line.lineNo, labelQuantity));
					}
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.integer",
									line.lineNo, labelQuantity));
				}
			}

			
			if (StringUtil.hasLength(line.unitCost)) {
				try {
					float funitCost = Float.parseFloat(line.unitCost);

					
					
					if (!DiscountUtil.isExceptianalProduct(line.productCode)) {
						if (Double.compare(funitCost, 0) == 0) {
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.line.num0",
											line.lineNo, labelUnitCost));
						}
					}
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									labelUnitCost));
				}
			}
			
			if (StringUtil.hasLength(line.cost)) {
				try {
					float fcost = Float.parseFloat(line.cost);
					
					
					
					if (!DiscountUtil.isExceptianalProduct(line.productCode)) {
						if (Double.compare(fcost, 0) == 0) {
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.line.num0",
											line.lineNo, labelCost));
						}
					}
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									labelCost));
				}
			}
			
			if (StringUtil.hasLength(line.unitRetailPrice)) {
				try {
					
					
					
					float funitRetailPrice = Float
							.parseFloat(line.unitRetailPrice);
					if (Double.compare(funitRetailPrice, 0) == 0) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.num0",
										line.lineNo, labelUnitRetailPrice));
					}
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									labelUnitRetailPrice));
				}
			}
			
			if (StringUtil.hasLength(line.retailPrice)) {
				try {
					
					
					
					float fretailPrice = Float.parseFloat(line.retailPrice);
					if (Double.compare(fretailPrice, 0) == 0) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.num0",
										line.lineNo, labelRetailPrice));
					}
				} catch (NumberFormatException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									labelRetailPrice));
				}
			}
		}
		
		if (!inputLine) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.noline"));
		}

		return errors;

	}

	/**
	 * @return {@link MENU_ID#INPUT_RORDER}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_RORDER;
	}
	/**
	 * 入力担当者の設定をします.
	 */
	@Override
	public void initializeScreenInfo() {
		
		this.userId = userDto.userId;
		this.userName = userDto.nameKnj;
	}

	/**
	 * @param dto {@link ROrderSlipDto}
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<ROrderLineDto> dto) {
		for (ROrderLineDto lineDto : this.lineList) {
			lineDto.status = this.defaultStatusCode;
		}

		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);
		this.roDate = sdf.format(new Date());

		
		this.shipDate = sdf.format(new Date());
	}

	/**
	 * @return {@link ROrderSlipDto}
	 */
	@Override
	public AbstractSlipDto<ROrderLineDto> copyToDto() {
		return Beans.createAndCopy(ROrderSlipDto.class, this).execute();
	}

	/**
	 * 受注伝票番号を設定します.
	 * @param keyValue 受注伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.roSlipId = keyValue;
	}

	/**
	 * @return {@link ROrderLineDto}のリスト
	 */
	@Override
	public List<ROrderLineDto> getLineList() {
		return this.lineList;
	}

	/**
	 * @param lineList {@link ROrderLineDto}のリスト
	 */
	@Override
	public void setLineList(List<ROrderLineDto> lineList) {
		this.lineList = lineList;
	}

	@Override
	public void initCopy() throws ServiceException {
		
		this.roSlipId = "";

		for (int i = 0; i < this.lineList.size(); i++) {
			ROrderLineDto lineDto = this.lineList.get(i);
			lineDto.roLineId = "";
			lineDto.roSlipId = "";
			lineDto.restQuantity = lineDto.quantity;
			lineDto.status = this.defaultStatusCode;
			lineDto.statusName = this.defaultStatusName;
		}
	}
}
