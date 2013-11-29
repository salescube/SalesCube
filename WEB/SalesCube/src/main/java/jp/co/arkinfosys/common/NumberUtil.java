/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 実数のユーティリティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public final class NumberUtil {

	/**
	 * 小数桁数を指定してDecimalFormatクラスのインスタンスを取得します.<br>
	 * 端数処理は四捨五入が採用されます.
	 * @param alignment　小数桁数
	 * @param comma　3桁ごとにカンマを付与するか否か
	 * @return　DecimalFormatクラスのインスタンス
	 */
	public static DecimalFormat createDecimalFormat(int alignment, boolean comma) {
		return NumberUtil.createDecimalFormat(null, alignment, comma);
	}

	/**
	 * 端数処理コード、小数桁数を指定してDecimalFormatクラスのインスタンスを取得します.
	 * @param fract　端数処理コード
	 * @param alignment　小数桁数
	 * @param comma　3桁ごとにカンマを付与するか否か
	 * @return　DecimalFormatクラスのインスタンス
	 */
	public static DecimalFormat createDecimalFormat(String fract,
			int alignment, boolean comma) {
		StringBuffer format = new StringBuffer("##0");

		// 小数桁数
		for (int i = 0; i < alignment; i++) {
			if (i == 0) {
				format.append(".");
			}
			format.append("0");
		}

		// カンマ付与
		if (comma) {
			format.insert(0, ",");
		}

		// 端数処理方式
		RoundingMode mode = NumberUtil.getRoundingMode(fract);
		DecimalFormat df = new DecimalFormat(format.toString());
		if (mode != null) {
			df.setRoundingMode(mode);
		}

		return df;
	}

	/**
	 * 端数処理コードを指定してRoundingModeEnumオブジェクトを取得します.
	 * @param fractCategory　端数処理コード
	 * @return　RoundingModeEnumオブジェクト
	 */
	public static RoundingMode getRoundingMode(String fractCategory) {
		RoundingMode mode = RoundingMode.HALF_UP;
		if (CategoryTrns.FLACT_CATEGORY_DOWN.equals(fractCategory)) {
			// 切り捨て
			mode = RoundingMode.DOWN;
		} else if (CategoryTrns.FLACT_CATEGORY_HALF_UP.equals(fractCategory)) {
			// 四捨五入
			mode = RoundingMode.HALF_UP;
		} else if (CategoryTrns.FLACT_CATEGORY_UP.equals(fractCategory)) {
			// 切り上げ
			mode = RoundingMode.UP;
		}
		return mode;
	}

}
