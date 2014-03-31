/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.purchase;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.purchase.PurchaseLineDto;
import jp.co.arkinfosys.dto.purchase.PurchaseSlipDto;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

/**
 * 仕入入力画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputPurchaseForm extends AbstractSlipEditForm<PurchaseLineDto>{

	/**
	 * 仕入番号
	 */
	public String supplierSlipId;

	/**
	 * 状態フラグ
	 */
	public String status;

	/**
	 * 発注番号
	 */
	public String poSlipId;

	/**
	 * 仕入日
	 */
	@Required
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String supplierDate;

	/**
	 * 納期
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String deliveryDate;

	/**
	 * 担当者コード
	 */
	public String userId;

	/**
	 * 担当者名
	 */
	public String userName;

	/**
	 * レートID
	 */
	public String rateId;

	/**
	 * レート
	 */
	public String rate;

	/**
	 * レート名
	 */
	public String rateName;

	/**
	 * 備考
	 */
	@Maxlength(maxlength = 120)
	public String remarks;

	/**
	 * 仕入先コード
	 */
	public String supplierCode;

	/**
	 * 仕入先名
	 */
	public String supplierName;

	/**
	 * 仕入先外貨記号
	 */
	public String sign;

	/**
	 * 仕入先取引区分
	 */
	public String supplierCmCategory;

	/**
	 * 委託発注伝票の複写時にtrue、そうでない場合false
	 */
	public boolean isEntrustPorder;


	/**
	 * 明細行リスト
	 */
	public List<PurchaseLineDto> lineDtoList;

	public String nonTaxPriceTotal;

	/** 消費税合計 */
	public String ctaxTotal;
	
	/** 消費税率 */
	public String ctaxRate;

	/** 伝票合計金額 */
	public String priceTotal;

	/** 伝票合計外貨金額 */
	public String fePriceTotal;

	/** 支払実績締日付 */
	public String paymentCutoffDate;

	/**
	 * URLパターンで使うダミー
	 */
	public String tempSupplierCode;
	public String targetDate;
	public String tempPoLineId;

	public String copySlipId; // 複写伝票番号

	public boolean initCalc = false;

	// 明細行のタブ移動可能項目数
	public int lineElementCount = 15;

	/**
	 * 伝票複写かどうか判定します.
	 *
	 * @return　伝票複写か否か
	 */
	public boolean isCopySlip() {
		return StringUtil.hasLength(poSlipId);
	}

	/**
	 * 伝票複写ボタンが押下不可能であるか判定します.<BR>
	 * <条件> - 次の何れかに該当する場合、伝票複写が無効であると判断します.<BR>
	 *  ・ユーザが参照権限である
	 *
	 * @return 伝票複写ボタンが押下不可か否か
	 *
	 */
	public boolean isNotCopiable() {
		return (!menuUpdate);
	}

	 /**
     * 登録・更新ボタンが押下不可能であるか判定します.<BR>
     * <条件> - 次の何れかに該当する場合、登録が無効であると判断します.<BR>
     * ・ユーザが参照権限である
     *
     * @return 登録・更新ボタンが押下不可か否か
     *
     */
	public boolean isNotRegisterable() {
		return (!menuUpdate );
	}

	 /**
     * 削除ボタンが押下不可能であるか判定します.<BR>
     * <条件> - 次の何れかに該当する場合、削除が無効であると判断します.<BR>
     * ・ユーザが参照権限である
     * ・登録前の伝票である
     *
     * @return 削除ボタンが押下不可か否か
     *
     */
	public boolean isNotDeletable() {
		return (!menuUpdate || isNewData() );
	}

	/**
	 * 初期値を設定します.
	 */
	public void reset() {
		supplierSlipId = "";
		status = "";
		poSlipId = "";
		supplierDate = "";
		deliveryDate = "";
		userId = "";
		userName = "";
		rateId = "";
		rate = "";
		rateName = "";
		remarks = "";
		supplierCode = "";
		supplierName = "";
		sign= "";
		lineDtoList = new ArrayList<PurchaseLineDto>();

		nonTaxPriceTotal = null;
		ctaxTotal = null;
		ctaxRate = null;
		priceTotal = null;
		fePriceTotal = null;
		taxShiftCategory = null;
		taxFractCategory = null;
		priceFractCategory = null;
		paymentCutoffDate = null;

		tempSupplierCode = null;
		targetDate = null;
		tempPoLineId = null;
		copySlipId = null;
		updDatetm = null;
		initCalc = false;
	}

	/**
	 * @return {@link PurchaseSlipDto}
	 */
	@Override
	public AbstractSlipDto<PurchaseLineDto> copyToDto() {
		return Beans.createAndCopy(PurchaseSlipDto.class, this).execute();
	}

	/**
	 * @return {@link PurchaseLineDto}のリスト
	 */
	@Override
	public List<PurchaseLineDto> getLineList() {
		return this.lineDtoList;
	}

	/**
	 * @return {@link MENU_ID#INPUT_PURCHASE}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_PURCHASE;
	}

	/**
	 * 伝票複写時の初期値を設定します.<BR>
	 * 未使用です.
	 */
	@Override
	public void initCopy() throws ServiceException {
	}

	/**
	 * 入力担当者、消費税率を設定します.
	 */
	@Override
	public void initializeScreenInfo() {
		userId = userDto.userId;
		userName = userDto.nameKnj;
		
		// 消費税率
		this.ctaxRate = super.taxRate;
	}
	
	/**
	 * 税マスタから取得した現在有効な税率と、伝票作成当時の税率が異なる場合は、伝票作成時の税率を使用する
	 */
	@Override
	public void setSlipTaxRate() {
		if (super.taxRate != this.ctaxRate) {
			super.taxRate = this.ctaxRate;
		}
	}

	/**
	 * フォームのデフォルト値を設定します.<BR>
	 * 未使用です.
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<PurchaseLineDto> dto) {
	}

	/**
	 * 仕入伝票番号を設定します.
	 * @param keyValue 仕入伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.supplierSlipId = keyValue;
	}

	/**
	 * @param lineList {@link PurchaseLineDto}のリスト
	 */
	@Override
	public void setLineList(List<PurchaseLineDto> lineList) {
		this.lineDtoList = lineList;
	}
}
