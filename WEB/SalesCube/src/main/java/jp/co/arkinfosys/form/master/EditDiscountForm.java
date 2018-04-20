/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.DiscountTrnDto;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 数量割引マスタ管理（登録・編集）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.REQUEST)
public class EditDiscountForm extends AbstractEditForm {

	// 基本情報
	/** 割引コード */
	@Required
	@Mask(mask = Constants.CODE_MASK.HANKAKU_MASK)
	public String discountId;

	/** 割引有効フラグ */
	public String useFlag;

	/** 割引名 */
	@Required(arg0=@Arg(key="labels.discount.discountName"))
	public String discountName;

	/** 備考 */
	public String remarks;

	/** 数量スライド */
	public List<DiscountTrnDto> discountTrnList = new ArrayList<DiscountTrnDto>();

	/** 削除済みデータ(CSV) */
	public String deletedDataId;

	/** 表示時点の行数 */
	public int defaultRowCount;

	/**
	 * フォームをリセットします.
	 */
	public void reset() {
		useFlag = "0";
		discountTrnList = new ArrayList<DiscountTrnDto>();
	}

	public void initialize() {
		discountId = "";
		useFlag = "0";
		discountName = "";
		remarks = "";
		discountTrnList.clear();
		deletedDataId = "";
		defaultRowCount = 0;
		creDatetmShow = "";
		updDatetm = "";
		updDatetmShow = "";
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages err = new ActionMessages();

		String labelDiscountId = MessageResourcesUtil.getMessage("labels.discountId");
		String labelDiscountName = MessageResourcesUtil.getMessage("labels.discount.discountName");
		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");

		String labelDiscountTrn = MessageResourcesUtil.getMessage("labels.discountTrn");
		String labelDataFrom = MessageResourcesUtil.getMessage("labels.discountTrn.dataFrom");
		String labelDataTo = MessageResourcesUtil.getMessage("labels.discountTrn.dataTo");
		String labelDiscountRate = MessageResourcesUtil.getMessage("labels.discountTrn.discountRate");

		// 長さチェック
		// 割引コード
		checkMaxLength(discountId, 20, labelDiscountId, err);
		// 割引名
		checkMaxLength(discountName, 60, labelDiscountName, err);
		// 備考
		checkMaxLength(remarks, 120, labelRemarks, err);

		// 数量スライドの行数チェック
		if (discountTrnList == null || discountTrnList.size() == 0) {
			err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.required", labelDiscountTrn));
		}

		//必須・型チェック
		int index = 0;
		boolean isError = false;
		for (DiscountTrnDto dto : discountTrnList) {
			index++;
			// 開始
			if (!checkRequired(index, dto.dataFrom, labelDataFrom, err)) {
				isError = true;
			}else if (!checkDecimal(index, dto.dataFrom, labelDataFrom, 9, 3, err)) {
				isError = true;
			}
			// 終了
			if (!checkRequired(index, dto.dataTo, labelDataTo, err)) {
				isError = true;
			}else if (!checkDecimal(index, dto.dataTo, labelDataTo, 9, 3, err)) {
				isError = true;
			}
			// 割引率
			if (!checkRequired(index, dto.discountRate, labelDiscountRate, err)) {
				isError = true;
			}else if (!checkDecimal(index, dto.discountRate, labelDiscountRate, 3, 3, err)) {
				isError = true;
			}
		}

		//大小チェック
		boolean isCrossError = false;
		if(!isError){
			index = 0;
			for (DiscountTrnDto dto : discountTrnList) {
				index++;
				BigDecimal dataFrom1 = new BigDecimal(dto.dataFrom);
				BigDecimal dataTo1 =  new BigDecimal(dto.dataTo);
				if ( dataFrom1.compareTo(dataTo1) > 0 ) {
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.line.range.eq.more", index, labelDataTo, labelDataFrom));
					isCrossError = true;
				}
			}
		}

		//重なりチェック  N^2ループなので組み合わせチェックのみ2パターンでよい
		if(!isError && !isCrossError){
			for (DiscountTrnDto dto : discountTrnList) {
				BigDecimal dataFrom1 = new BigDecimal(dto.dataFrom);
				BigDecimal dataTo1 =  new BigDecimal(dto.dataTo);
				for (DiscountTrnDto dto2 : discountTrnList) {
					if (dto.equals(dto2)) {
						// 同じものは比較対象外
						continue;
					}
					BigDecimal dataFrom2 = new BigDecimal(dto2.dataFrom);
					BigDecimal dataTo2 =  new BigDecimal(dto2.dataTo);
					if (dataFrom1.compareTo(dataFrom2) <= 0 &&
						dataTo1.compareTo(dataTo2) >= 0) {
						// dtoがdto2を内包する場合
						err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.line.discounttrn.cross"));
						isCrossError = true;
						break;
					} else if (dataFrom1.compareTo(dataFrom2) >= 0 &&
							   dataTo1.compareTo(dataTo2) <= 0) {
						// dtoの開始がdto2に含まれる場合
						err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.line.discounttrn.cross"));
						isCrossError = true;
						break;
					}
				}
			}
		}

		return err;
	}
}
