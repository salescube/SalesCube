/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.payment;

import java.util.HashMap;
import java.util.Map;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;

/**
 * 支払入力情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 */
public class InputPaymentDto extends AbstractSlipDto<InputPaymentLineDto> {
	private static final long serialVersionUID = 1L;

	// 支払伝票情報
	/** 支払伝票番号 */
	public String paymentSlipId;

	/** 発注伝票番号 */
	public String poSlipId;

	/** 支払日 */
	public String paymentDate;

	/** 担当者名 */
	public String userName;

	/** 備考 */
	public String remarks;

	/** 仕入先コード */
	public String supplierCode;

	/** 仕入先名称 */
	public String supplierName;

	/** レートタイプ */
	public String rateId;

	/** 外貨記号 */
	public String cUnitSign;

	/** 伝票合計金額 */
	public String priceTotal;

	/** 伝票合計外貨金額 */
	public String fePriceTotal;

	/** 支払締日付 */
	public String paymentCutoffDate;

	/** 更新日時 */
	public String updDatetm;

	/** 税転嫁 */
	public String taxShiftCategory;

	/** 税端数処理 */
	public String taxFractCategory;

	/** 単価端数処理 */
	public String priceFractCategory;

	// 登録・更新で設定する項目
	/** 状態フラグ */
	public String status;

	/** 担当者コード */
	public String userId;

	/** 発注日 */
	public String poDate;

	/** 支払年度 */
	public String paymentAnnual;

	/** 支払月度 */
	public String paymentMonthly;

	/** 支払年月度 */
	public String paymentYm;


	/**
	 * 支払伝票IDとその更新時間のセットを返します.
	 * @return 支払伝票IDとその更新時間のマップ
	 */
	public Map<String, String> getSupplierSlipIdAndUpdateTime() {
		Map<String, String> map = new HashMap<String, String>();

		for(InputPaymentLineDto line : lineDtoList) {
			if(line.supplierSlipId != null && line.supplierSlipId.length() > 0) {
				map.put(line.supplierSlipId, line.supUpdDatetm);
			}
		}

		return map;
	}

	/**
	 * 支払伝票明細行を作成します.
	 * @return 支払伝票明細行オブジェクト
	 */
	@Override
	public AbstractLineDto createLineDto() {
		return new InputPaymentLineDto();
	}

	/**
	 * 支払伝票番号を取得します.
	 * @return 支払伝票番号
	 */
	@Override
	public String getKeyValue() {
		return this.paymentSlipId;
	}
}
