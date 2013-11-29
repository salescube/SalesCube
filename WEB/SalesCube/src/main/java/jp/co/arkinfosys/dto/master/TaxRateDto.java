/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.TaxRate;

/**
 * 税率マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class TaxRateDto implements MasterEditDto {

	public String taxTypeCategory;

	public String startDate;

	public String taxRate;

	/**
	 * 同じ開始日のデータが存在するか検査します.
	 * @param taxRateEntity 検査する税率マスタ情報
	 * @return　 true:同じ開始日のデータが存在する　false:同じ開始日のデータは存在しない
	 */
	public boolean equalsKey(TaxRate taxRateEntity) {
		if (!StringUtil.hasLength(startDate)) {
			return false;
		}
		SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);
		if ((startDate.equals(DF_YMD.format(taxRateEntity.startDate)))) {
			return true;
		}
		return false;
	}
	/**
	 * 同じ開始日で同じ税率のデータが存在するか検査します.
	 * @param taxRateEntity 検査する税率マスタ情報
	 * @return　 true:同じ開始日で同じ税率のデータが存在する　false:同じ開始日で同じ税率のデータは存在しない
	 */
	public boolean equalsValue(TaxRate taxRateEntity) {
		if (!StringUtil.hasLength(startDate)) {
			return false;
		}
		SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);
		BigDecimal bd = new BigDecimal(taxRate);

		if (startDate.equals(DF_YMD.format(taxRateEntity.startDate))
				&& taxRateEntity.taxRate.compareTo(bd) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 税区分と開始日を取得します.
	 * @return　 文字列の配列　[税区分,開始日]
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.taxTypeCategory, this.startDate };
	}
}
