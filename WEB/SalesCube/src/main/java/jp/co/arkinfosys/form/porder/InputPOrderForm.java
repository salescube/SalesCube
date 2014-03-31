/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.porder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.porder.InputPOrderLineDto;
import jp.co.arkinfosys.dto.porder.InputPOrderSlipDto;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.ByteType;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.annotation.Validwhen;
/**
 * 発注入力画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputPOrderForm extends AbstractSlipEditForm<InputPOrderLineDto> {

	/**
	 * 伝票明細行の行数制御
	 */
	public static final int CONST_SLIP_LINE_DEFAULT = 6;
	//他の画面(現状：補充発注推奨リストのみ)から、CONST_SLIP_LINE_MAXを元に戻す際の値
	public static final int CONST_SLIP_LINE_MAX_DEFAULT = 35;
	//発注入力画面でのみ利用する明細行上限
	public int CONST_SLIP_LINE_MAX = CONST_SLIP_LINE_MAX_DEFAULT;
	/**
	 * 日付の形式指定
	 */
	private SimpleDateFormat DF_YMD = new SimpleDateFormat(
			Constants.FORMAT.DATE);

	/**
	 * MaxLength用
	 */
	public static final int ML_PRODUCTCODE  = 20;
	public static final int ML_SUPPLIERCODE = 9;
	public static final int ML_REMARK = 120;
	public static final int ML_PRODUCT_REMARK = 120;
	public static final int ML_DATE = 10;			//日付用 1234/67/90			=10
	public static final int ML_S_UNITPRICE = 9;		//単価用 -23,567,901.34		=14
	public static final int ML_S_PRICE = 9;			//金額用 -23,567,901,345.78	=18
	public static final int ML_S_QUANTITY = 6; 		//数量用 -23,567				=7
	public static final int ML_UNITPRICE = 9;		//単価用 符号なし				=13
	public static final int ML_PRICE = 9;			//金額用 符号なし				=17
	public static final int ML_QUANTITY = 6; 		//数量用 符号なし				=6

	/**
	 * 発注伝票の状態フラグ初期値
	 *
	 */
	public static final String DEFAULT_STATUS_CODE = Constants.STATUS_PORDER_SLIP.ORDERED; 	//"発注"

	// 各種コードの存在フラグ
	public static final int CODE_NOEXIST = 0;
	public static final int CODE_EXIST = 1;

	// モード制御用
	public boolean newMode;
	public boolean lockMode;
	public boolean ROMode;

	/**
	 * URLパターンで使うダミー
	 */
	public String tempSupplierCode;
	public String tempProductCode;
	public String targetDate;

	public String tempPoSlipId;

	public String defaultCUnit;

	/**
	 * 明細行の行数
	 */
	public int slipLineMax;
	public int slipLineActive;

	// 仕入完了の文字列
	public String lineStatusPurchased;

	/************************************
	 * 伝票
	 */

	/**
	 * 発注伝票番号
	 */
	@IntegerType
	public String poSlipId;

	/**
	 * 状態フラグ
	 */
	@IntegerType
	public String status;

	/**
	 * 発注日
	 */
	@Required
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String poDate;

	/**
	 * 納期
	 */
	@Required
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String deliveryDate;

	/**
	 * 入力担当者
	 */
	public String userId;
	public String userName;

	/**
	 * 運送便区分
	 */
	@Required
	@ByteType
	public String transportCategory;

	/**
	 * 摘要
	 */
	@Maxlength(maxlength = 120)
	public String remarks;

	/**
	 * 仕入先コード 半角英数字最大9桁
	 */
	public Integer supplierIsExist;
	@Required
	@Mask(mask = Constants.CODE_MASK.SUPPLIER_MASK,
			msg = @Msg(key = "errors.invalid"),
			args = @Arg(key = "labels.supplierCode", resource = true, position = 0))
	@Validwhen(test = "(supplierIsExist == 1)",
			msg = @Msg(key = "errors.invalidSupplierCode"),
			args = @Arg(key = "labels.supplierCode", resource = true, position = 0))
	public String supplierCode;

	public String supplierName;
	public String supplierKana;
	public String supplierZipCode;
	public String supplierAddress1;
	public String supplierAddress2;
	public String supplierPcName;
	public String supplierPcKana;
	public String supplierPcPreCategory;
	public String supplierPcPost;
	public String supplierTel;
	public String supplierFax;
	public String supplierEmail;

	public String supplierRate;

	public String supplierAbbr;
	public String supplierDeptName;
	public String supplierPcPre;

	public String unitPriceDecAlignment;
	public String dolUnitPriceDecAlignment;
	public String taxPriceDecAlignment;

	/**
	 * Fract 等の規定値(自社ﾏｽﾀから)
	 */
	public String defTaxFractCategory;
	public String defTaxPriceDecAlignment;

	public String defPriceFractCategory;
	public String defUnitPriceDecAlignment;
	public String defDolUnitPriceDecAlignment;

	public String defProductFractCategory;
	public String defNumDecAlignment;

	// 明細行のタブ移動可能項目数
	public int lineElementCount = 20;

	/**
	 * 仕入先通貨単位表示対応：全通貨単位リスト
	 */
	public List<String> cUnitSignList;

	/**
	 * レートID
	 */
	@IntegerType
	public String rateId;


	/**
	 * 伝票合計金額（円）
	 */
	public String priceTotal;

	/**
	 * 伝票合計消費税
	 */
	public String ctaxTotal;

	/**
	 * 消費税率
	 */
	public String ctaxRate;
	
	/**
	 * 伝票合計外貨金額
	 */
	public String fePriceTotal;

	/**
	 * 伝票発行フラグ
	 */
	@IntegerType
	public String printCount;

	/**
	 * 支払状況
	 */
	public String slipPaymentStatus;

	/**
	 * 支払日
	 */
	public String slipPaymentDate;

	/************************************
	 * 明細行
	 */
	// 売上伝票明細行
	public List<InputPOrderLineDto> poLineList;

	/**
	 * @return {@link MENU_ID#INPUT_PORDER}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_PORDER;
	}

	/**
	 * 画面情報を初期化します.
	 */
	@Override
	public void initializeScreenInfo() {

		/**
		 * 伝票
		 */
		poSlipId = null;
		status = null;
		poDate = null;
		deliveryDate = null;
		userId = userDto.userId;
		userName = userDto.nameKnj;
		remarks = null;
		//初期選択固定値：AIR便:仕様書より
		transportCategory = CategoryTrns.TRANSPORT_CATEGORY_AIR;
		/**
		 * 仕入先コード
		 */
		supplierIsExist = CODE_NOEXIST;
		supplierCode = null;
		supplierName = null;
		supplierKana = null;
		supplierZipCode = null;
		supplierAddress1 = null;
		supplierAddress2 = null;
		supplierPcName = null;
		supplierPcKana = null;
		supplierPcPreCategory = null;
		supplierPcPost = null;
		supplierTel = null;
		supplierFax = null;
		supplierEmail = null;

		supplierRate = null;

		supplierAbbr = null;
		supplierDeptName = null;
		supplierPcPre = null;

		taxFractCategory = null;
		priceFractCategory = null;
		unitPriceDecAlignment = null;
		dolUnitPriceDecAlignment = null;
		taxPriceDecAlignment = null;

		taxShiftCategory = null;
		rateId = null;

		cUnitSignList = null;
		defaultCUnit = null;
		
		/**
		 * 伝票合計
		 */
		priceTotal = null;
		ctaxTotal = null;
		fePriceTotal = null;
		
		// 消費税率
		this.ctaxRate = super.taxRate;

		printCount = null;

		slipPaymentStatus = null;
		slipPaymentDate = null;

		/**
		 * 明細行の行数
		 */
		slipLineMax = CONST_SLIP_LINE_MAX;
		slipLineActive = CONST_SLIP_LINE_DEFAULT;

		//初期値の端数処理
		defTaxFractCategory = mineDto.taxFractCategory;
		defTaxPriceDecAlignment = String.valueOf(0);

		defPriceFractCategory = mineDto.priceFractCategory;
		defDolUnitPriceDecAlignment = String.valueOf(mineDto.unitPriceDecAlignment.intValue());
		defUnitPriceDecAlignment = String.valueOf(0);

		defProductFractCategory = mineDto.productFractCategory;
		defNumDecAlignment = String.valueOf(mineDto.numDecAlignment.intValue());

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
	 * フォームのデフォルト値を設定します.<BR>
	 * 未使用です.
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<InputPOrderLineDto> dto) {

	}
	/**
	 * @return {@link InputPOrderSlipDto}
	 */
	@Override
	public AbstractSlipDto<InputPOrderLineDto> copyToDto() {
		return Beans.createAndCopy(InputPOrderSlipDto.class, this).execute();
	}

	/**
	 * 発注伝票番号を設定します.
	 * @param keyValue 発注伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.poSlipId = keyValue;
	}

	/**
	 * @return {@link InputPOrderLineDto}のリスト
	 */
	@Override
	public List<InputPOrderLineDto> getLineList() {
		return this.poLineList;
	}

	/**
	 * @param lineList {@link InputPOrderLineDto}のリスト
	 */
	@Override
	public void setLineList(List<InputPOrderLineDto> lineList) {
		this.poLineList = lineList;
	}

	@Override
	public void initCopy() throws ServiceException {
		/**
		 * 伝票
		 */
		//発注伝票番号の消去
		poSlipId = null;
		//伝票状態の消去
		status = null;
		//発注日の初期値=システム日付
		poDate = DF_YMD.format(new Date());
		//伝票納期の初期化
		deliveryDate = null;
		//ユーザIDの設定
		userId = userDto.userId;
		//入力担当者の設定
		userName = userDto.nameKnj;
		//仕入先情報読込み	JSP側で
		//仕入先レート設定		JSP側で
		//伝票合計の計算		JSP側で
		/**
		 * 明細行
		 */
		for(int i=0;i<=poLineList.size();i++){
			//伝票明細行IDの初期化
			InputPOrderLineDto ipld = poLineList.get(i);
			ipld.poLineId = null;
			//明細行状態の初期化
			ipld.status = null;
			//商品情報の補完		JSP側で

			//明細行納期の初期化
			ipld.deliveryDate = null;
		}
	}

	@Override
	public void initLoad() throws ServiceException {
		newData = false;

		supplierIsExist = CODE_EXIST;

		/**
		 * 明細行の行数
		 */
		slipLineMax = CONST_SLIP_LINE_MAX;
		slipLineActive = CONST_SLIP_LINE_DEFAULT;

		//初期値の端数処理
		defTaxFractCategory = mineDto.taxFractCategory;
		defTaxPriceDecAlignment = String.valueOf(0);

		defPriceFractCategory = mineDto.priceFractCategory;
		defDolUnitPriceDecAlignment = String.valueOf(mineDto.unitPriceDecAlignment.intValue());
		defUnitPriceDecAlignment = String.valueOf(0);

		defProductFractCategory = mineDto.productFractCategory;
		defNumDecAlignment = String.valueOf(mineDto.numDecAlignment.intValue());
	}

	/**
	 * MAP用デフォルトを返します(登録用).
	 * @return MAP用デフォルト
	 */
	public BeanMap POrderInsertDefaultValues(){
		BeanMap DefaultParam = new BeanMap();
		/******************************************
		 * 伝票
		 */
		DefaultParam.put("status", InputPOrderForm.DEFAULT_STATUS_CODE);
		DefaultParam.put("supplierCode","");
		DefaultParam.put("supplierIsExist",InputPOrderForm.CODE_NOEXIST);
		DefaultParam.put("printCount","0");
		/******************************************
		 * 明細行
		 */
		DefaultParam.put("lineStatus", InputPOrderForm.DEFAULT_STATUS_CODE);
		DefaultParam.put("productCode","");
		DefaultParam.put("productIsExist",InputPOrderForm.CODE_NOEXIST);
		DefaultParam.put("lineRemarks", null);
		DefaultParam.put("productRemarks", null);
		DefaultParam.put("lineDeliveryDate", null);
		/**
		 *
		 */
		return DefaultParam;
	}

	/**
	 * MAP用デフォルトを返します（更新用）.
	 * @return MAP用デフォルト
	 */
	public BeanMap POrderUpdateDefaultValues(){
		BeanMap DefaultParam = POrderInsertDefaultValues();
		/******************************************
		 * 伝票
		 */
		DefaultParam.put("status",null);
		DefaultParam.put("printCount",null);
		/**
		 *
		 */
		return DefaultParam;
	}


}
