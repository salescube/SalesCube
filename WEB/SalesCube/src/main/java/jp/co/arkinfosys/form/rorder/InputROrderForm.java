/*
 * Copyright 2009-2010 Ark Information Systems.
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
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.DiscountUtil;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.SlipStatusCategoryTrns;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.OnlineOrderWork;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
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

	/**
	 * 消費税率
	 */
	public String ctaxRate;

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
	public String nowPurchasingStatusName; // 分納中
	public String finishStatusName; // 完納

	/** 通販サイト受注データかどうか */
	public boolean isOnlineOrder;

	/** 取り込みデータかどうか */
	public boolean isImport;

	/** オンライン受注ＩＤ */
	public String onlineOrderId;

	public String dcCategory; // L 配送業者コード
	public String dcName; // S 配送業者名
	public String dcTimezoneCategory; // L 配送時間帯コード
	public String dcTimezone; // S 配送時間帯文字列

	public String copySlipId;		// 複写対象　伝票番号

	public CategoryService categoryService;


	// 区分トランザクションデータ
	// 課税
	public String TAX_CATEGORY_IMPOSITION = CategoryTrns.TAX_CATEGORY_IMPOSITION;
	// 課税（旧）
	public String TAX_CATEGORY_IMPOSITION_OLD = CategoryTrns.TAX_CATEGORY_IMPOSITION_OLD;
	// 課税（内税）
	public String TAX_CATEGORY_INCLUDED = CategoryTrns.TAX_CATEGORY_INCLUDED;
	// 税転嫁　内税
	public String TAX_SHIFT_CATEGORY_INCLUDE_CTAX = CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX;


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
		dcName = ""; // 配送業者名
		dcTimezoneCategory = ""; // 配送時間帯コード
		dcTimezone = ""; // 配送時間帯文字列

		copySlipId = ""; // 複写対象伝票番号
	}

	/**
	 *  配送業者コードの初期化を行います.
	 */
	public void initDc() {
		dcCategory = CategoryTrns.DC_CATEGORY_1; // 配送業者コード
	}

	/**
	 * フォームをセットアップします.
	 * @param list　オンライン受注データ
	 */
	public void setUp(List<OnlineOrderWork> list) {

		if (list.size() == 0) {
			return;
		}

		// 1番目をヘッダ情報として扱う
		OnlineOrderWork work = list.get(0);

		// 受注番号　→空欄
		this.roSlipId = "";

		// 受注日　→　仕入日付
		this.roDate = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);

		// 納期指定日　→空欄
		this.deliveryDate = "";

		// 出荷日　→仕入日付
		this.shipDate = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);

		// 受付番号　→order_id
		this.receptNo = work.onlineOrderId;

		// 客先伝票番号　→空欄
		this.customerSlipNo = "";

		// 備考 →　納入先注意
		this.remarks = work.deliveryInst;

		// 顧客コード　→固定値
		this.customerCode = Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER;

		// 顧客名～取引区分は後でActionで設定する（顧客マスタを引く必要があるため）

		// 納入先名
		this.deliveryName = work.address3;

		// 事業所名
		this.deliveryOfficeName = "";
		this.deliveryOfficeKana = "";

		// 部署名
		this.deliveryDeptName = "";

		// 郵便番号
		this.deliveryZipCode = work.zipCode;

		// 住所
		this.deliveryAddress1 = work.state + work.address1;
		this.deliveryAddress2 = work.address2;

		// 担当者
		this.deliveryPcName = work.recipientName;
		this.deliveryPcKana = "";

		// 敬称
		this.deliveryPcPre = "";
		this.deliveryPcPreCategory = CategoryTrns.PREFIX_SAMA;

		// TEL
		this.deliveryTel = work.shipTel;

		// E-MAIL
		this.deliveryEmail = "";

		// FAX
		this.deliveryFax = "";

		// オンライン受注ID（hidden）
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

		// 通販サイト受注の場合の必須条件
		if (Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER
				.equals(this.customerCode)) {
			// 納入先担当者名
			if (!StringUtil.hasLength(this.deliveryPcName)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.onlineorder.required", MessageResourcesUtil
								.getMessage("labels.deliveryPcName")));
			}

			// 納入先郵便番号
			if (!StringUtil.hasLength(this.deliveryZipCode)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.onlineorder.required", MessageResourcesUtil
								.getMessage("labels.deliveryZipCode")));
			}

			// 納入先住所1
			if (!StringUtil.hasLength(this.deliveryAddress1)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.onlineorder.required", MessageResourcesUtil
								.getMessage("labels.deliveryAddress1")));
			}

			// 納入先電話番号
			if (!StringUtil.hasLength(this.deliveryTel)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.onlineorder.required", MessageResourcesUtil
								.getMessage("labels.deliveryTel")));
			}
		}

		boolean inputLine = false;

		// 長さチェック
		// 受付番号
		if (StringUtil.hasLength(this.receptNo) && this.receptNo.length() > 30) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.maxlength", labelReceptNo, "30"));
		}

		// 客先伝票番号
		if (StringUtil.hasLength(this.customerSlipNo)
				&& this.customerSlipNo.length() > 30) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.maxlength", labelCustomerSlipNo, "20"));
		}

		// 摘要
		if (StringUtil.hasLength(this.remarks) && this.remarks.length() > 120) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.maxlength", labelMemorandum, "120"));
		}

		// 顧客コード
		if (StringUtil.hasLength(this.customerCode)
				&& this.customerCode.length() > Constants.CODE_SIZE.CUSTOMER) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.maxlength", labelCustomerCode,
					Constants.CODE_SIZE.CUSTOMER));
		}

		// 前後関係チェック
		try {
			Date dtRoDate = DateFormat.getDateInstance().parse(this.roDate);
			Date dtShipDate = DateFormat.getDateInstance().parse(this.shipDate);
			Date dtDeliveryDate = DateFormat.getDateInstance().parse(
					this.deliveryDate);
			// 受注日＜＝出荷日
			if (StringUtil.hasLength(this.roDate)
					&& StringUtil.hasLength(this.shipDate)) {
				if (dtRoDate.after(dtShipDate)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.date.roship"));

				}
			}
			// 受注日＜＝納期指定日
			if (StringUtil.hasLength(this.roDate)
					&& StringUtil.hasLength(this.deliveryDate)) {
				if (dtRoDate.after(dtDeliveryDate)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.date.rodelivery"));

				}
			}
			// 出荷日＜＝納期指定日
			if (StringUtil.hasLength(this.shipDate)
					&& StringUtil.hasLength(this.deliveryDate)) {
				if (dtShipDate.after(dtDeliveryDate)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.date.shipdelivery"));

				}
			}
		} catch (ParseException e) {
			// 型チェックは済んでいる
		}

		for (ROrderLineDto line : lineList) {
			// [商品コード][数量][売上単価][売価金額]のいずれも入力がない場合は無視
			if (!StringUtil.hasLength(line.productCode)
					&& !StringUtil.hasLength(line.quantity)
					&& !StringUtil.hasLength(line.unitRetailPrice)
					&& !StringUtil.hasLength(line.retailPrice)) {
				continue;
			}

			// 明細行が1件以上、存在する
			inputLine = true;

			// 必須チェック
			// 商品コード
			if (!StringUtil.hasLength(line.productCode)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelProductCode));
			}

			// 数量
			if (!StringUtil.hasLength(line.quantity)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelQuantity));
			}

			// 仕入単価
			if (!StringUtil.hasLength(line.unitCost)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelUnitCost));
			}

			// 仕入金額
			if (!StringUtil.hasLength(line.cost)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelCost));
			}

			// 売上単価
			if (!StringUtil.hasLength(line.unitRetailPrice)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo,
						labelUnitRetailPrice));
			}

			// 売価金額
			if (!StringUtil.hasLength(line.retailPrice)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", line.lineNo, labelRetailPrice));
			}

			// 長さチェック
			// 商品コード
			if (StringUtil.hasLength(line.productCode)
					&& line.productCode.length() > 20) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo, labelProductCode,
						"20"));
			}
			// 備考
			if (StringUtil.hasLength(line.remarks)
					&& line.remarks.length() > 120) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo, labelRemarks,
						"120"));
			}
			// ピッキング備考
			if (StringUtil.hasLength(line.eadRemarks)
					&& line.eadRemarks.length() > 120) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo, labelEadRemarks,
						"120"));
			}

			// 商品備考
			if (StringUtil.hasLength(line.productRemarks)
					&& line.productRemarks.length() > 120) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.maxlength", line.lineNo,
						labelProductRemarks, "120"));
			}

			// 型チェック
			// Float型
			// 数量
			if (StringUtil.hasLength(line.quantity)) {
				try {
					//Float.parseFloat(line.quantity);
					// 2010.04.21 Add Kaki
					// 数値０チェック
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

			// 仕入単価
			if (StringUtil.hasLength(line.unitCost)) {
				try {
					float funitCost = Float.parseFloat(line.unitCost);

					// 数値０チェック
					// 特殊コード商品の場合は0チェックを行わない。
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
			// 仕入金額
			if (StringUtil.hasLength(line.cost)) {
				try {
					float fcost = Float.parseFloat(line.cost);
					// 2010.04.21 Add Kaki
					// 数値０チェック
					// 特殊コード商品の場合は0チェックを行わない。
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
			// 売上単価
			if (StringUtil.hasLength(line.unitRetailPrice)) {
				try {
					//Float.parseFloat(line.unitRetailPrice);
					// 2010.04.21 Add Kaki
					// 数値０チェック
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
			// 売価金額
			if (StringUtil.hasLength(line.retailPrice)) {
				try {
					//Float.parseFloat(line.retailPrice);
					// 2010.04.21 Add Kaki
					// 数値０チェック
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
		// 明細行が1行以上、存在するかどうか
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
		// 入力担当者の設定
		this.userId = userDto.userId;
		this.userName = userDto.nameKnj;
		// 消費税率
		this.ctaxRate = super.taxRate;
	}
	/**
	 * 税マスタから取得した現在有効な税率と、伝票作成当時の税率が異なる場合は、伝票作成時の税率を使用する
	 */
	@Override
	public void setSlipTaxRate() {
		if (this.ctaxRate != null && super.taxRate != this.ctaxRate) {
			super.taxRate = this.ctaxRate;
		}

		if (this.ctaxRate == "" || this.ctaxRate == null) {
			this.ctaxRate = super.taxRate;
		}
	}
	/**
	 * @param dto {@link ROrderSlipDto}
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<ROrderLineDto> dto) {
		for (ROrderLineDto lineDto : this.lineList) {
			lineDto.status = this.defaultStatusCode;
		}

		// 受注日
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);
		this.roDate = sdf.format(new Date());

		// 出荷日
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
		// キー項目の初期化
		this.roSlipId = "";

		// 受注日
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);
		this.roDate = sdf.format(new Date());

		// 出荷日
		this.shipDate = sdf.format(new Date());

		for (int i = 0; i < this.lineList.size(); i++) {
			ROrderLineDto lineDto = this.lineList.get(i);
			lineDto.roLineId = "";
			lineDto.roSlipId = "";
			lineDto.restQuantity = lineDto.quantity;
			lineDto.status = this.defaultStatusCode;
			lineDto.statusName = this.defaultStatusName;
		}
	}


	/**
	 * 見積伝票から受注伝票を初期化します.
	 * @param estimateDto　見積伝票情報
	 * @param customer　顧客マスタ情報
	 */
	public void initialize( InputEstimateDto estimateDto, Customer customer ) {
		// 初期値を設定
		reset();

		// 共通情報を設定
		userId = this.userDto.userId;		// 担当者コード
		userName = this.userDto.nameKnj;	// 担当者名

		// 見積伝票の値を設定
		ctaxPriceTotal = estimateDto.ctaxPriceTotal;// 伝票合計消費税
		ctaxRate = estimateDto.ctaxRate;			// 消費税率
		priceTotal = estimateDto.estimateTotal;		// 伝票合計金額
		gross = estimateDto.grossMargin;			//伝票合計粗利益


		// 顧客の情報を設定
		// ※税処理に関する情報が見積伝票情報から取得できないものがあるため、顧客マスタ情報から設定する
		taxShiftCategory = customer.taxShiftCategory;		// 税転嫁
		taxFractCategory = customer.taxFractCategory;		// 税端数処理
		priceFractCategory = customer.priceFractCategory;	// 単価端数処理
		salesCmCategory = customer.salesCmCategory;			// 取引区分
		cutoffGroupCategory = customer.cutoffGroup
						+ customer.paybackCycleCategory;	// 支払条件
		customerCode = customer.customerCode;				// 得意先コード
		customerName = customer.customerName;				// 得意先名
		customerRemarks = customer.remarks;					// 備考
		customerCommentData = customer.commentData;			// コメント


		// 明細行
		lineList.clear();
		for( InputEstimateLineDto estimateLineDto : estimateDto.getLineDtoList() ){
			ROrderLineDto roLineDto = new ROrderLineDto();

			// 見積伝票明細から情報をコピーする
			roLineDto.initialize(estimateLineDto);

			// 見積伝票明細から取得できない値を設定する
			roLineDto.roLineId = "";
			roLineDto.roSlipId = "";
			roLineDto.status = this.defaultStatusCode;
			roLineDto.statusName = this.defaultStatusName;
			roLineDto.lineNo = String.valueOf( lineList.size() + 1 );

			// リストに追加
			lineList.add(roLineDto);
		}
	}


	/**
	 * 納入先情報の初期化を行います.
	 * @param delivery　納入先情報
	 */
	public void initializeDeliveryInfo(DeliveryAndPre dap, List<LabelValueBean> preTypeCategoryList) {
		deliveryCode = dap.deliveryCode;
		deliveryName = dap.deliveryName;
		deliveryKana = dap.deliveryKana;
		deliveryOfficeName = dap.deliveryOfficeName;
		deliveryOfficeKana = dap.deliveryOfficeKana;
		deliveryDeptName = dap.deliveryDeptName;
		deliveryZipCode = dap.deliveryZipCode;
		deliveryAddress1 = dap.deliveryAddress1;
		deliveryAddress2 = dap.deliveryAddress2;
		deliveryPcName = dap.deliveryPcName;
		deliveryPcKana = dap.deliveryPcKana;
		deliveryPcPreCategory = dap.deliveryPcPreCategory;
		deliveryPcPre = "";
		for(LabelValueBean lv : preTypeCategoryList){
			// 敬称カテゴリから敬称名を取得する
			if(lv.getValue().equals(deliveryPcPreCategory)){
				deliveryPcPre = lv.getLabel();
				break;
			}
		}
		deliveryTel = dap.deliveryTel;
		deliveryEmail = dap.deliveryEmail;
		deliveryFax = dap.deliveryFax;
		deliveryUrl = dap.deliveryUrl;
	}



}
