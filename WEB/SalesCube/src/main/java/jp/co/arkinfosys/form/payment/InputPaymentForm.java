/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.payment;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.payment.InputPaymentDto;
import jp.co.arkinfosys.dto.payment.InputPaymentLineDto;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

/**
 * 支払入力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class InputPaymentForm extends AbstractSlipEditForm<InputPaymentLineDto> {

	/** 支払伝票番号 */
	public String paymentSlipId;

	/** 発注伝票番号 */
	public String poSlipId;

	/** 支払日 */
	@Required
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String paymentDate;

	/** 担当者名 */
	public String userName;

	/** 備考 */
	@Maxlength(maxlength = 120, arg0 = @Arg(key = "labels.memorandum"))
	public String remarks;

	/** 仕入先コード */
	public String supplierCode;

	/** 仕入先名称 */
	public String supplierName;

	/** レートタイプ */
	public String rateId;

	/** レート名 */
	public String rateName;

	/** 外貨記号 */
	public String cUnitSign;

	/** 伝票合計金額 */
	public String priceTotal;

	/** 伝票合計外貨金額 */
	public String fePriceTotal;

	/** 買掛残高 */
	public String aptBalance;

	/** 支払締日付 */
	public String paymentCutoffDate;

	/** 発注日 */
	public String poDate;

	/** 税転嫁 */
	public String taxShiftCategory;

	/**  */
	public boolean initCalc = false;

	/** 明細行リスト */
	public List<InputPaymentLineDto> lineDtoList = new ArrayList<InputPaymentLineDto>();

	// その他画面で使用する項目
	/** 伝票複写で選択された発注伝票番号 */
	public String selPoSlipId;

	/** 仕入入力画面の権限があるか否か */
	public boolean isInputPurchaseValid;

	/**
	 * 新期伝票かどうか判定します.
	 * @return 新期伝票か否か
	 */
	public boolean isNew() {
		return !StringUtil.hasLength(paymentSlipId);
	}

	/**
	 * 〆てあるかどうか判定します.
	 * @return　〆てあるか否か
	 */
	public boolean isCutOff() {
		return StringUtil.hasLength(paymentCutoffDate);
	}

	/**
	 * 登録・更新ボタンが押下不可能であるか判定します.<BR>
	 * <条件> - 次の何れかに該当する場合、登録が無効であると判断します.<BR>
	 * ・ユーザが参照権限である.<BR>
	 * ・伝票が締状態である.<BR>
	 * ・伝票呼出済みでない.
	 * @return true 押下不可 false 押下可
	 */
	public boolean isNotRegisterable() {
		return (!menuUpdate || isCutOff() || !isCopySlip());
	}

	/**
	 * 登録・更新ボタンが押下不可能であるか判定します.<BR>
	 * <条件> - 次の何れかに該当する場合、登録が無効であると判断します.<BR>
	 * ・ユーザが参照権限である.<BR>
	 * ・伝票が締状態である.
	 * @return true 押下不可 false 押下可
	 */
	public boolean isNotUpdatable() {
		return (!menuUpdate || isCutOff());
	}

	/**
	 * 削除ボタンが押下不可能であるか判定します.<BR>
	 * <条件> - 次の何れかに該当する場合、削除が無効であると判断します.<BR>
	 * ・ユーザが参照権限である.<BR>
	 * ・登録前の伝票である.<BR>
	 * ・伝票が締状態である.
	 * @return true 押下不可 false 押下可
	 */
	public boolean isNotDeletable() {
		return (!menuUpdate || isNew() || isCutOff());
	}

	/**
	 * 伝票複写ボタンが押下不可能であるか判定します.<BR>
	 * <条件> - 次の何れかに該当する場合、伝票複写が無効であると判断します.<BR>
	 * ・ユーザが参照権限である.<BR>
	 * ・伝票が締状態である.
	 * @return true 押下不可 false 押下可
	 */
	public boolean isNotCopiable() {
		return (!menuUpdate || isCutOff());
	}

	/**
	 * @return {@link InputPaymentDto}
	 */
	@Override
	public AbstractSlipDto<InputPaymentLineDto> copyToDto() {
		return Beans.createAndCopy(InputPaymentDto.class, this).execute();
	}

	/**
	 * @return {@link InputPaymentLineDto}のリスト
	 */
	@Override
	public List<InputPaymentLineDto> getLineList() {
		return this.lineDtoList;
	}

	/**
	 * @return {@link MENU_ID#INPUT_PAYMENT}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_PAYMENT;
	}

	@Override
	public void initCopy() throws ServiceException {
		// 伝票複写時はNo列に連番を設定(行番号が未発番のため)
		for(int i = 0; i < lineDtoList.size(); i++) {
			InputPaymentLineDto dto = lineDtoList.get(i);
			dto.paymentLineNo = String.valueOf(i + 1);
		}
	}

	/**
	 * 仕入入力画面の権限を設定します.
	 */
	@Override
	public void initialize() throws ServiceException {
		this.isInputPurchaseValid = super.userDto.isMenuValid(Constants.MENU_ID.INPUT_PURCHASE);
	}

	/**
	 * 担当者を設定します.
	 */
	@Override
	public void initializeScreenInfo() {
		// 担当者を設定する
		userName = this.userDto.nameKnj;
	}

	/**
	 * フォームのデフォルト値を設定します.<BR>
	 * 未使用です.
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<InputPaymentLineDto> dto) {
		// 処理なし
	}

	/**
	 * 支払伝票番号を設定します.
	 * @param keyValue 支払伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.paymentSlipId = keyValue;
	}

	/**
	 * @param lineList {@link InputPaymentLineDto}のリスト
	 */
	@Override
	public void setLineList(List<InputPaymentLineDto> lineList) {
		this.lineDtoList = lineList;
	}

	/**
	 * 初期値を設定します.
	 */
	@Override
	public void reset() {
		paymentSlipId = "";
		poSlipId = "";
		paymentDate = "";
		userName = "";
		remarks = "";
		supplierCode = "";
		supplierName = "";
		rateId = "";
		cUnitSign = "";
		priceTotal = "";
		fePriceTotal = "";
		aptBalance = "";
		paymentCutoffDate = "";
		updDatetm = "";
		poDate = "";
		initCalc = false;
		lineDtoList = new ArrayList<InputPaymentLineDto>();
		isInputPurchaseValid = false;
	}

	/**
	 * 伝票呼出済かどうか判定します.
	 * @return 伝票呼出済か否か
	 */
	public boolean isCopySlip(){
		return StringUtil.hasLength(poSlipId);
	}
}
