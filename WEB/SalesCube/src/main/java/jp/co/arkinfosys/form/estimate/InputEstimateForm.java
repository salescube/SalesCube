/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.estimate;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateLineDto;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.DoubleType;
import org.seasar.struts.annotation.LongRange;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 見積入力画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class InputEstimateForm extends
		AbstractSlipEditForm<InputEstimateLineDto> {

	/**
	 * 見積番号
	 */
	@Required
	@Mask(mask = Constants.CODE_MASK.HANKAKU_MASK)
	public String estimateSheetId;

	/**
	 * 見積日
	 */
	@Required
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String estimateDate;

	/**
	 * 納期または出荷日（納入期限）
	 */
	@Maxlength(maxlength = 120)
	public String deliveryInfo;

	/**
	 * 有効期限
	 */
	@DateType(datePatternStrict = Constants.FORMAT.DATE)
	public String validDate;

	/**
	 * 入力担当者ID
	 */
	public String userId;

	/**
	 * 入力担当者
	 */
	public String userName;

	/**
	 * 件名
	 */
	@Maxlength(maxlength = 100)
	public String title;

	/**
	 * 納入先
	 */
	@Maxlength(maxlength = 60)
	public String deliveryName;

	/**
	 * 見積条件
	 */
	@Maxlength(maxlength = 120)
	public String estimateCondition;

	/**
	 * 提出先名
	 */
	@Required
	@Maxlength(maxlength = 60)
	public String submitName;

	/**
	 * 提出先敬称コード
	 */
	public String submitPreCategory;
	/**
	 * 提出先敬称
	 */
	public String submitPre;

	/**
	 * 顧客コード（得意先コード）
	 */
	@Maxlength(maxlength = 15)
	public String customerCode;

	/**
	 * 顧客名（得意先名）
	 */
	public String customerName;

	/**
	 * 備考（顧客）
	 */
	public String customerRemarks;

	/**
	 * 取引区分（顧客）
	 */
	public String salesCmCategory;

	/**
	* コメント（顧客）
	*/
	public String customerCommentData;

	/**
	 * 摘要（備考）
	 */
	@Maxlength(maxlength = 120, arg0 = @Arg(key = "labels.tekiyou"))
	public String remarks;

	/**
	 * メモ
	 */
	@Maxlength(maxlength = 1000)
	public String memo;

	/**
	 * 消費税率
	 */
	public String ctaxRate;

	/**
	 * 明細行リスト
	 */
	public List<InputEstimateLineDto> estimateLineTrnDtoList;

	/**
	 * 粗利益
	 */
	@LongRange(min = -999999999, max = 999999999)
	@DoubleType
	public String grossMargin;

	/**
	 * 粗利益率
	 */
	public String grossMarginRate;

	/**
	 * 合計金額
	 */
	@LongRange(min = -999999999, max = 999999999)
	@DoubleType
	public String retailPriceTotal;

	/**
	 * 消費税
	 */
	@LongRange(min = -999999999, max = 999999999)
	@DoubleType
	public String ctaxPriceTotal;

	/**
	 * 伝票合計
	 */
	@LongRange(min = -999999999, max = 999999999)
	@DoubleType
	public String estimateTotal;

	/**
	 * 原価合計（仕入金額合計）
	 */
	@LongRange(min = -999999999, max = 999999999)
	@DoubleType
	public String costTotal;

	@Override
	public void initCopy() throws ServiceException {
		// 値をクリアする
		estimateSheetId = null;
		updDatetm = null;
		updUser = null;
		for (InputEstimateLineDto dto : estimateLineTrnDtoList) {
			dto.estimateSheetId = null;
			dto.estimateLineId = null;
			dto.creFunc = null;
			dto.creDatetm = null;
			dto.creUser = null;
			dto.updFunc = null;
			dto.updDatetm = null;
			dto.updUser = null;
		}

		// 初期値を設定する
		initialize();
	}

	/**
	 * 入力担当者、消費税率を設定します.
	 */
	@Override
	public void initializeScreenInfo() {
		// 入力担当者
		userId = this.userDto.userId;
		userName = this.userDto.nameKnj;

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
	 * フォームをリセットします.
	 */
	@Override
	public void reset() {
		estimateSheetId = null;
		estimateDate = null;
		deliveryInfo = null;
		validDate = null;
		userId = null;
		userName = null;
		title = null;
		deliveryName = null;
		submitName = null;
		submitPreCategory = null;
		submitPre = null;
		customerCode = null;
		customerName = null;
		customerRemarks = null;
		customerCommentData = null;
		remarks = null;
		memo = null;
		estimateLineTrnDtoList = null;

		grossMargin = null;
		grossMarginRate = null;
		retailPriceTotal = null;
		ctaxPriceTotal = null;
		estimateTotal = null;

		taxFractCategory = ""; // 税端数処理
		priceFractCategory = ""; // 単価端数処理

	}

	/**
	 * @return {@link MENU_ID#INPUT_ESTIMATE}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_ESTIMATE;
	}

	/**
	 * @param dto {@link InputEstimateDto}
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<InputEstimateLineDto> dto) {

		deliveryName = MessageResourcesUtil
				.getMessage("labels.deliveryDefault");

		// 敬称は「様」（初期値マスタは使わない）
		submitPreCategory = CategoryTrns.PREFIX_SAMA;

		// 明細行を再設定する
		if (this.estimateLineTrnDtoList == null) {
			this.estimateLineTrnDtoList = new ArrayList<InputEstimateLineDto>();
		}
	}

	/**
	 * @return {@link InputEstimateDto}
	 */
	@Override
	public AbstractSlipDto<InputEstimateLineDto> copyToDto() {
		return Beans.createAndCopy(InputEstimateDto.class, this).execute();
	}

	/**
	 * 見積番号を設定します.
	 * @param keyValue 見積番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.estimateSheetId = keyValue;
	}

	/**
	 * @return {@link InputEstimateLineDto}のリスト
	 */
	@Override
	public List<InputEstimateLineDto> getLineList() {
		return this.estimateLineTrnDtoList;
	}

	/**
	 * @param lineList {@link InputEstimateLineDto}のリスト
	 */
	@Override
	public void setLineList(List<InputEstimateLineDto> lineList) {
		this.estimateLineTrnDtoList = lineList;
	}

}
