/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.sales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.RoLineTrn;
import jp.co.arkinfosys.entity.RoSlipTrn;
import jp.co.arkinfosys.entity.SalesLineTrn;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.TaxRate;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.DoubleType;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.annotation.ShortType;
import org.seasar.struts.annotation.Validwhen;


/**
 * 売上入力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class InputSalesForm extends AbstractSlipEditForm<SalesLineDto> {
	// 売上伝票情報

	public String salesSlipId;				// T 売上伝票番号
	public String status;					// H 状態フラグ
    @ShortType
	public String salesAnnual;				// S 売上年度
    @ShortType
	public String salesMonthly;				// S 売上月度
    @IntegerType
	public String salesYm;					// S 売上年月度
	@Maxlength(maxlength = 10 )
	public String roSlipId;					// T 受注伝票番号
	public String billId;					// H 請求書番号
	public String salesBillId;				// H 売上請求書番号
    @DateType(datePattern = Constants.FORMAT.DATE)
	public String billDate;					// H 請求日
	public String billCutoffGroup;			// H 締日グループ
	public String paybackCycleCategory;		// H 回収間隔
    @DateType(datePattern = Constants.FORMAT.DATE)
	public String billCutoffDate;			// H 請求締め日付
	public String billCutoffPdate;			// H 請求締め処理日
    @Required(arg0 = @Arg(key = "labels.salesDate"))
    @DateType(datePattern = Constants.FORMAT.DATE)
	public String salesDate;				// T 売上日
    @DateType(datePattern = Constants.FORMAT.DATE,
    		msg = @Msg(key = "errors.date"),
    		arg0 = @Arg(key = "labels.deliveryDate2", resource = true, position = 0))
	public String deliveryDate;				// T 納期指定日
	@Maxlength(maxlength = 30 )
	public String receptNo;					// T 受付番号
	@Maxlength(maxlength = 30 )
	public String customerSlipNo;			// T 客先伝票番号
	public String salesCmCategory;			// L 売上取引区分
	public String custsalesCmCategory;		// H 顧客取引区分
    @DateType(datePattern = Constants.FORMAT.DATE)
	public String salesCutoffDate;			// H 売掛締め日付
	public String salesCutoffPdate;			// H 売掛締め処理日
	public String userId;					// H 担当者コード
	public String userName;					// T 担当者名
	@Maxlength(maxlength = 120 )
	public String remarks;					// T 備考
	public String pickingRemarks;			// T ピッキング備考
	public String dcCategory;				// L 配送業者コード
	public String dcName;					// S 配送業者名
	public String dcTimezoneCategory;		// L 配送時間帯コード
	public String dcTimezone;				// S 配送時間帯文字列
	@Required
	@Mask(mask = Constants.CODE_MASK.CUSTOMER_MASK)
	public String customerCode;				// T 得意先コード
	public String customerName;				// T 得意先名
	public String customerRemarks;			// T 備考
	public String customerCommentData;		// T コメント
    @Validwhen(test = "((customerCode == '" + Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER + "') or (*this* != null))", msg = @Msg(key = "errors.required"), args = @Arg(key = "labels.deliveryCode", resource = false, position = 1))
	public String deliveryCode;				// L 納入先コード
	public String deliveryName;				// H 納入先名
	public String deliveryKana;				// H 納入先名カナ
	public String deliveryOfficeName;		// H 納入先事業所名
	public String deliveryOfficeKana;		// H 納入先事業所名カナ
	public String deliveryDeptName;			// T 納入先部署名
	public String deliveryZipCode;			// T 納入先郵便番号
	public String deliveryAddress1;			// T 納入先住所１
	public String deliveryAddress2;			// T 納入先住所２
	public String deliveryPcName;			// T 納入先担当者名
	public String deliveryPcKana;			// T 納入先担当者カナ
	public String deliveryPcPreCategory;	// T 納入先敬称コード
	public String deliveryPcPre;			// S 納入先敬称
	public String deliveryTel;				// T 納入先電話番号
	public String deliveryFax;				// T 納入先ＦＡＸ番号
	public String deliveryEmail;			// T 納入先Email
	public String deliveryUrl;				// H 納入先ＵＲＬ
	public String baCode;					// H 請求先コード
	public String baName;					// H 請求先会社名
	public String baKana;					// H 請求先カナ
	public String baOfficeName;				// H 請求先事業所名
	public String baOfficeKana;				// H 請求先事業所カナ
	public String baDeptName;				// H 請求先部署名
	public String baZipCode;				// H 請求先郵便番号
	public String baAddress1;				// H 請求先住所１
	public String baAddress2;				// H 請求先住所２
	public String baPcName;					// H 請求先担当者
	public String baPcKana;					// H 請求先担当者カナ
	public String baPcPreCategory;			// H 請求先敬称コード
	public String baPcPre;					// H 請求先敬称
	public String baTel;					// H 請求先電話番号
	public String baFax;					// H 請求先ＦＡＸ番号
	public String baEmail;					// H 請求先Email
	public String baUrl;					// H 請求先URL
    @DoubleType
	public String ctaxPriceTotal;			// S 伝票合計消費税
    @DoubleType
	public String priceTotal;				// S 伝票合計金額 売上伝票のpriceTotalには、消費税を含まない
    @DoubleType
	public String gmTotal;					// S 伝票合計粗利益
	public String codSc;					// S 代引手数料
    @IntegerType
	public String billPrintCount;			// H 請求書発行フラグ
    @IntegerType
	public String deliveryPrintCount;		// H 納品書発行フラグ
    @IntegerType
	public String tempDeliveryPrintCount;	// H 仮納品書発行フラグ
    @IntegerType
	public String shippingPrintCount;		// H 出荷指示書発行フラグ
    @IntegerType
	public String siPrintCount;				// H 送り状データ出力フラグ
	public String artId;					// H 売掛残高番号

	public String estimatePrintCount;
	public String delborPrintCount;
	public String poPrintCount;
    // 登録時にDBから設定
	public String customerUrl;
	public String customerOfficeName;
	public String customerOfficeKana;
	public String customerAbbr;
	public String customerDeptName;
	public String customerPcPost;
	public String customerPcKana;
	public String customerPcPreCategory;
	public String customerPcPre;
	public String customerFax;
	public String customerEmail;


    public String adlabel;					// T 宛名
	public String disclaimer;				// T 但書
	public String creFunc;					// 作成機能
	public String creDatetm;				// 作成日時
	public String creUser;					// 作成者
	public String updFunc;					// 更新機能


	public String cutoffGroupCategory;	// 締日グループと支払サイクルに分割する元のコード
	public String gmTotalPer;			// 伝票合計粗利益率
	public String total;				// 伝票合計(金額＋消費税）

	// 出荷指示書用
	public String pickingListId;
	public String roDate;					// 足りない情報は保存時にDBを参照して設定する
	public String customerPcName;
	public String customerZipCode;
	public String customerAddress1;
	public String customerAddress2;
	public String customerTel;
	public String printDate;

	// 受注伝票の最終更新日
	public String roUpdDatetm;				// H 更新日時

	// 伝票印刷用
	public String billPrintUnit;			// H 請求書発行単位
	public String billDatePrint;			// H 請求書日付有無
	public String tempDeliverySlipFlag;		// H 仮納品書出力


	// 伝票複写用情報
	public String copySlipName;		// 複写対象　伝票種類
	public String copySlipId;		// 複写対象　伝票番号

	// 売上伝票明細行
	public List<SalesLineDto> salesLineList = new ArrayList<SalesLineDto>();

	// ワークフラグ
	public boolean initCategory = true;			// 区分情報の作成状態を管理するフラグ

	// 初期作成サイズ
	public int initLineDefault = 6;

	// 明細行のタブ移動可能項目数
	public int lineElementCount = 15;

	// 最大作成サイズ
	public int initLineSize = 35;

	// 区分トランザクションデータ
	// 課税
	public String TAX_CATEGORY_IMPOSITION = CategoryTrns.TAX_CATEGORY_IMPOSITION;
	// 課税（旧）
	public String TAX_CATEGORY_IMPOSITION_OLD = CategoryTrns.TAX_CATEGORY_IMPOSITION_OLD;
	// 課税（内税）
	public String TAX_CATEGORY_INCLUDED = CategoryTrns.TAX_CATEGORY_INCLUDED;
	// 税転嫁　内税
	public String TAX_SHIFT_CATEGORY_INCLUDE_CTAX = CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX;

	// 完納区分
	public String DELIVERY_PROCESS_CATEGORY_NONE = CategoryTrns.DELIVERY_PROCESS_CATEGORY_NONE;
	public String DELIVERY_PROCESS_CATEGORY_PARTIAL = CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL;
	public String DELIVERY_PROCESS_CATEGORY_FULL = CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL;

	// 受注伝票情報
	public RoSlipTrn roSlipTrn;
	public List<RoLineTrn> roLineList = new ArrayList<RoLineTrn>();

	// 仮納品書出力フラグ（顧客情報と納入先情報が一致時はfalse)
	public Boolean reportEFlag;

	// 消費税率
	public String ctaxRate;


	/**
	 * フォームを初期化状態にします.
	 */
	public void clear() {
		salesSlipId = "";			// 売上伝票番号
		status = SalesSlipTrn.STATUS_INIT;	// 状態フラグ
		salesAnnual = "";			// 売上年度
		salesMonthly = "";			// 売上月度
		salesYm = "";				// 売上年月度
		roSlipId = "";				// 受注伝票番号
		billId = "";				// 請求書番号
		salesBillId = "";			// 売上請求書番号
		billDate = "";				// 請求日
		billCutoffGroup = "";		// 締日グループ
		paybackCycleCategory = "";	// 回収間隔
		billCutoffDate = "";		// 請求締め日付
		billCutoffPdate = "";		// 請求締め処理日
									// 売上日
		salesDate = ""; //new Date(GregorianCalendar.getInstance().getTimeInMillis()).toString();

		deliveryDate = "";			// 納期指定日
		receptNo = "";				// 受付番号
		customerSlipNo = "";		// 客先伝票番号
		salesCmCategory = "";		// 売上取引区分
		custsalesCmCategory = "";	// 顧客売上取引区分
		salesCutoffDate = "";		// 売掛締め日付
		salesCutoffPdate = "";		// 売掛締め処理日
		userId = this.userDto.userId;		// 担当者コード
		userName = this.userDto.nameKnj;	// 担当者名
		remarks = "";				// 備考
		pickingRemarks = "";		// ピッキング備考
		dcCategory = CategoryTrns.DC_CATEGORY_1;			// 配送業者コード
		dcName = "";				// 配送業者名
		dcTimezoneCategory = "";	// 配送時間帯コード
		dcTimezone = "";			// 配送時間帯文字列
		customerCode = "";			// 得意先コード
		customerName = "";			// 得意先名
		customerRemarks = "";		// 備考
		customerCommentData = "";	// コメント
		deliveryCode = "";			// 納入先コード
		deliveryName = "";			// 納入先名
		deliveryKana = "";			// 納入先名カナ
		deliveryOfficeName = "";	// 納入先事業所名
		deliveryOfficeKana = "";	// 納入先事業所名カナ
		deliveryDeptName = "";		// 納入先部署名
		deliveryZipCode = "";		// 納入先郵便番号
		deliveryAddress1 = "";		// 納入先住所１
		deliveryAddress2 = "";		// 納入先住所２
		deliveryPcName = "";		// 納入先担当者名
		deliveryPcKana = "";		// 納入先担当者カナ
		deliveryPcPreCategory = "";	// 納入先敬称コード
		deliveryPcPre = "";			// 納入先敬称
		deliveryTel = "";			// 納入先電話番号
		deliveryFax = "";			// 納入先ＦＡＸ番号
		deliveryEmail = "";			// 納入先Email
		deliveryUrl = "";			// 納入先ＵＲＬ
		baCode = "";				// 請求先コード
		baName = "";				// 請求先会社名
		baKana = "";				// 請求先カナ
		baOfficeName = "";			// 請求先事業所名
		baOfficeKana = "";			// 請求先事業所カナ
		baDeptName = "";			// 請求先部署名
		baZipCode = "";				// 請求先郵便番号
		baAddress1 = "";			// 請求先住所１
		baAddress2 = "";			// 請求先住所２
		baPcName = "";				// 請求先担当者
		baPcKana = "";				// 請求先担当者カナ
		baPcPreCategory = "";		// 請求先敬称コード
		baPcPre = "";				// 請求先敬称
		baTel = "";					// 請求先電話番号
		baFax = "";					// 請求先ＦＡＸ番号
		baEmail = "";				// 請求先Email
		baUrl = "";					// 請求先URL
		taxShiftCategory = "";		// 税転嫁
		ctaxPriceTotal = "";		// 伝票合計消費税
		priceTotal = "";			// 伝票合計金額
		gmTotal = "";				// 伝票合計粗利益
		codSc = "";					// 代引手数料
		billPrintCount = "0";		// 請求書発行フラグ
		deliveryPrintCount = "0";	// 納品書発行フラグ
		tempDeliveryPrintCount = "0";// 仮納品書発行フラグ
		shippingPrintCount = "0";	// 出荷指示書発行フラグ
		siPrintCount = "0";			// 送り状データ出力フラグ
		adlabel = "";				// 宛名
		disclaimer = "";			// 但書
		creFunc = "";				// 作成機能
		creDatetm = "";				// 作成日時
		creUser = "";				// 作成者
		updFunc = "";				// 更新機能

		reportEFlag = false;		// 仮納品書出力フラグ

		for (SalesLineDto lineDto : this.salesLineList) {
			lineDto.status = SalesLineTrn.STATUS_INIT;
		}
	}

	/**
	 * 受注伝票から売上伝票を初期化します.
	 * @param rosDto　受注伝票情報
	 * @param customer　顧客マスタ情報
	 */
	public void initialize( ROrderSlipDto rosDto, Customer customer ) {

		salesSlipId = "";			// 売上伝票番号
		status = SalesSlipTrn.STATUS_INIT;	// 状態フラグ
		salesAnnual = "";			// 売上年度
		salesMonthly = "";			// 売上月度
		salesYm = "";				// 売上年月度
		roSlipId = rosDto.roSlipId;	// 受注伝票番号
		billId = "";				// 請求書番号
		salesBillId = "";			// 売上請求書番号
		billDate = "";				// 請求日
		billCutoffGroup = rosDto.cutoffGroup;	// 締日グループ
		paybackCycleCategory = rosDto.paybackCycleCategory;	// 回収間隔
		cutoffGroupCategory = billCutoffGroup + paybackCycleCategory;
		billCutoffDate = "";		// 請求締め日付
		billCutoffPdate = "";		// 請求締め処理日
									// 売上日→受注伝票：出荷日
		salesDate = rosDto.shipDate;
		deliveryDate = rosDto.deliveryDate;			// 納期指定日
		receptNo = rosDto.receptNo;				// 受付番号
		customerSlipNo = rosDto.customerSlipNo;		// 客先伝票番号
		salesCmCategory = rosDto.salesCmCategory;	// 売上取引区分
		custsalesCmCategory = rosDto.salesCmCategory;	// 顧客売上取引区分
		salesCutoffDate = "";		// 売掛締め日付
		salesCutoffPdate = "";		// 売掛締め処理日
		userId = this.userDto.userId;		// 担当者コード
		userName = this.userDto.nameKnj;	// 担当者名
		remarks = rosDto.remarks;	// 備考
		pickingRemarks = "";		// ピッキング備考
		dcCategory = ( rosDto.dcCategory == null ) ? CategoryTrns.DC_CATEGORY_1 : rosDto.dcCategory;	// 配送業者コード
		dcName = ( rosDto.dcName == null ) ? "" : rosDto.dcName;											// 配送業者名
		dcTimezoneCategory = ( rosDto.dcTimezoneCategory == null ) ? "" : rosDto.dcTimezoneCategory;		// 配送時間帯コード
		dcTimezone = ( rosDto.dcTimezone == null ) ? "" : rosDto.dcTimezone;								// 配送時間帯文字列
		customerCode = rosDto.customerCode;			// 得意先コード
		customerName = rosDto.customerName;			// 得意先名
		customerRemarks = rosDto.customerRemarks;	// 備考
		customerCommentData = rosDto.customerCommentData; // コメント
		deliveryCode = rosDto.deliveryCode;			// 納入先コード
		deliveryName = rosDto.deliveryName;			// 納入先名
		deliveryKana = rosDto.deliveryKana;			// 納入先名カナ
		deliveryOfficeName = rosDto.deliveryOfficeName;	// 納入先事業所名
		deliveryOfficeKana = rosDto.deliveryOfficeKana;	// 納入先事業所名カナ
		deliveryDeptName = rosDto.deliveryDeptName;		// 納入先部署名
		deliveryZipCode = rosDto.deliveryZipCode;		// 納入先郵便番号
		deliveryAddress1 = rosDto.deliveryAddress1;		// 納入先住所１
		deliveryAddress2 = rosDto.deliveryAddress2;		// 納入先住所２
		deliveryPcName = rosDto.deliveryPcName;		// 納入先担当者名
		deliveryPcKana = rosDto.deliveryPcKana;		// 納入先担当者カナ
		deliveryPcPreCategory = rosDto.deliveryPcPreCategory;	// 納入先敬称コード
		deliveryPcPre = rosDto.deliveryPcPre;			// 納入先敬称
		deliveryTel = rosDto.deliveryTel;			// 納入先電話番号
		deliveryFax = rosDto.deliveryFax;			// 納入先ＦＡＸ番号
		deliveryEmail = rosDto.deliveryEmail;			// 納入先Email
		deliveryUrl = rosDto.deliveryUrl;			// 納入先ＵＲＬ

		taxShiftCategory = rosDto.taxShiftCategory;		// 税転嫁
		taxFractCategory = rosDto.taxFractCategory;		// 税端数処理
		priceFractCategory = rosDto.priceFractCategory;	// 単価端数処理
		ctaxPriceTotal = rosDto.ctaxPriceTotal;		// 伝票合計消費税
		ctaxRate = rosDto.ctaxRate;					// 消費税率
		priceTotal = rosDto.priceTotal;			// 伝票合計金額
		gmTotal = "";				// 伝票合計粗利益
		codSc = rosDto.codSc;					// 代引手数料
		billPrintCount = "0";		// 請求書発行フラグ
		deliveryPrintCount = "0";	// 納品書発行フラグ
		tempDeliveryPrintCount = "0";// 仮納品書発行フラグ
		shippingPrintCount = "0";	// 出荷指示書発行フラグ
		siPrintCount = "0";			// 送り状データ出力フラグ
		adlabel = rosDto.customerName + customer.customerPcPreCategoryName;		// 宛名→得意先名＋敬称
		disclaimer = "";							// 但書
		creFunc = "";				// 作成機能
		creFunc = "";				// 作成機能
		creDatetm = "";				// 作成日時
		creUser = "";				// 作成者
		updFunc = "";				// 更新機能
		updDatetm = "";				// 更新日時
		updUser = "";				// 更新者

		roUpdDatetm = rosDto.updDatetm;	// 更新日時
		roDate = rosDto.roDate;		// 受注日
		reportEFlag = false;		// 仮納品書出力フラグ

		billPrintUnit = customer.billPrintUnit;		// 請求書発行単位
		billDatePrint = customer.billDatePrint;		// 請求書日付有無

		// 明細行
		salesLineList.clear();
		for( ROrderLineDto rolDto : rosDto.getLineDtoList() ){
			try {
				// 完納区分が「完了」のものは取り込まない
				if(Constants.STATUS_RORDER_LINE.SALES_FINISH.equals(rolDto.status)){
					continue;
				}
			} catch (Exception e) {
				continue;
			}

			SalesLineDto lineDto = new SalesLineDto();
			lineDto.initialize(rolDto);
			lineDto.lineNo = String.valueOf( salesLineList.size() + 1 );

			// 引当可能数、在庫管理区分
			lineDto.stockCtlCategory = rolDto.stockCtlCategory;
			lineDto.possibleDrawQuantity = rolDto.possibleDrawQuantity;

			salesLineList.add(lineDto);
		}
	}

	/**
	 * 伝票印刷用の初期化を行います.
	 * @param customer　顧客情報
	 */
	public void initialize( Customer customer ) {
		billPrintUnit = customer.billPrintUnit;				// H 請求書発行単位
		billDatePrint = customer.billDatePrint;				// H 請求書日付有無
		tempDeliverySlipFlag = customer.tempDeliverySlipFlag;
	}

	/**
	 * 請求先情報の初期化を行います.
	 * @param dap　請求先情報
	 */
	public void initialize( DeliveryAndPre dap ) {
		baCode = dap.deliveryCode;					// 請求先コード
		baName = dap.deliveryName;					// 請求先会社名
		baKana = dap.deliveryKana;					// 請求先カナ
		baOfficeName = dap.deliveryOfficeName;		// 請求先事業所名
		baOfficeKana = dap.deliveryOfficeKana;		// 請求先事業所カナ
		baDeptName = dap.deliveryDeptName;			// 請求先部署名
		baZipCode = dap.deliveryZipCode;			// 請求先郵便番号
		baAddress1 = dap.deliveryAddress1;			// 請求先住所１
		baAddress2 = dap.deliveryAddress2;			// 請求先住所２
		baPcName = dap.deliveryPcName;				// 請求先担当者
		baPcKana = dap.deliveryPcKana;				// 請求先担当者カナ
		baPcPreCategory = dap.deliveryPcPreCategory;// 請求先敬称コード
		baPcPre = dap.categoryCodeName;				// 請求先敬称
		baTel = dap.deliveryTel;					// 請求先電話番号
		baFax = dap.deliveryFax;					// 請求先ＦＡＸ番号
		baEmail = dap.deliveryEmail;				// 請求先Email
		baUrl = dap.deliveryUrl;					// 請求先URL
	}





	/* (非 Javadoc)
	 * @see jp.co.arkinfosys.form.AbstractSlipEditForm#upsertInitialize()
	 */
	@Override
	public void upsertInitialize() throws ServiceException {
		super.upsertInitialize();
		// 税率は売上日
		setSalesDateTaxRate();
	}

	/**
	 * 売上日の税率を税率マスタから取得して、設定する。
	 * @throws ServiceException
	 *
	 */
	public void setSalesDateTaxRate() throws ServiceException{

		String date = "";

		// 現在の税率を取得し、画面に設定する
		if(StringUtil.hasLength(salesDate)){
			if(StringUtil.isYmString(salesDate)){
				date = salesDate;
			}else{
				// 売上日に日付以外の値が設定された場合
				date = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);
			}
		}else{
			date = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);
		}

		TaxRate tx = taxRateService.findTaxRateById(CategoryTrns.TAX_TYPE_CTAX,	date);
		this.taxRate = tx.taxRate.toString();

	}

	/**
	 * 登録エラーが発生した時の処理を行います.<BR>
	 * 登録時のみで、更新時は関係しません.<BR>
	 * 伝票番号が設定されているとJSP側が更新とみなしてUPDATEを呼び出してしまうので、伝票番号をクリアします.
	 */
	public void initializeForError() {
		this.salesSlipId = "";
	}

	/**
	 * @return {@link MENU_ID#INPUT_SALES}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_SALES;
	}

	/**
	 * 入力担当者、消費税率を設定します.
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
	 * @param dto {@link SalesSlipDto}
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<SalesLineDto> dto) {
		this.dcCategory = CategoryTrns.DC_CATEGORY_1;
	}

	/**
	 * @return {@link SalesSlipDto}
	 */
	@Override
	public AbstractSlipDto<SalesLineDto> copyToDto() {
		return Beans.createAndCopy(SalesSlipDto.class, this).execute();
	}

	/**
	 * 売上伝票番号を設定します.
	 * @param keyValue 売上伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.salesSlipId = keyValue;
	}

	/**
	 * @return {@link SalesLineDto}のリスト
	 */
	@Override
	public List<SalesLineDto> getLineList() {
		return this.salesLineList;
	}

	/**
	 * @param lineList {@link SalesLineDto}のリスト
	 */
	@Override
	public void setLineList(List<SalesLineDto> lineList) {
		this.salesLineList = lineList;
	}

	@Override
	public void initCopy() throws ServiceException {
		salesSlipId = "";					// 入金伝票番号
		status = SalesSlipTrn.STATUS_INIT;	// 状態フラグ
		// 入金日は当日日付
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);
		salesDate = sdf.format(new Date(GregorianCalendar.getInstance().getTimeInMillis()));
		// 売上伝票から複写した時には、受注伝票番号はクリアする
		roSlipId = "";
		userId = this.userDto.userId;				// 担当者コード
		userName = this.userDto.nameKnj;			// 担当者名

		for (SalesLineDto lineDto : this.salesLineList) {
			lineDto.salesLineId = "";					// 明細ID
			lineDto.status = SalesLineTrn.STATUS_INIT;	// 状態フラグ
			lineDto.salesSlipId = "";					// 伝票番号
			lineDto.roLineId = "";						// 受注伝票行ID

		}
	}

	/**
	 * 請求完了かどうか判定します.
	 * @return　請求完了か否か
	 */
	public boolean isClosed() {
		return SalesSlipTrn.STATUS_FINISH.equals(status);
	}

}
