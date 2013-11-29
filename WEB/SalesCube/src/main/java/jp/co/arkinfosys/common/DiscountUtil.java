/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 割引に関するユーティリティクラスです.
 * @author Ark Information Systems
 *
 */
public class DiscountUtil {

	// アイテム総数値引き用
	HashMap<String,BigDecimal> mapProductCode = null;

	/**
	 * 特殊商品コードの判定を行います.
	 * @param productCode　商品コード
	 * @return 特殊商品コードか否か
	 */
	public static boolean isExceptianalProduct( String productCode ) {

		for( int j = 0 ; j < Constants.EXCEPTIANAL_PRODUCT_CODE_LIST.length ; j++ ){
			if( Constants.EXCEPTIANAL_PRODUCT_CODE_LIST[j].equals(productCode) ){
				return true;
			}
		}
		return false;
	}
	/**
	 * 特殊商品コードの判定（特注品含む）を行います.
	 * @param productCode　商品コード
	 * @param productStandardCategory　商品マスタ.分類標準
	 * @return 特殊商品コード（特注品を含む）か否か
	 */
	public static boolean isExceptianalOrOrderProduct( String productCode, String productStandardCategory ) {

		if( isExceptianalProduct( productCode )){
			return true;
		}
		if( CategoryTrns.PRODUCT_STANDARD_ODR.equals(productStandardCategory)){
			return true;
		}
		return false;
	}

	/**
	 * 端数処理を施したBigDecimalを返します.
	 * @param taxFractCategory 税端数処理(区分コード)
	 * @param scale 返される BigDecimalの値のスケール
	 * @param in 変換するBigDecimal値
	 * @return 端数処理を施したBigDecimal値
	 */
	public static BigDecimal getScaleValue( String taxFractCategory, int scale, BigDecimal in ) {
		if( CategoryTrns.FLACT_CATEGORY_DOWN.equals( taxFractCategory )){
			return in.setScale(scale,BigDecimal.ROUND_DOWN);
		}else if( CategoryTrns.FLACT_CATEGORY_UP.equals( taxFractCategory )){
			return in.setScale(scale,BigDecimal.ROUND_UP);
		}else{
			return in.setScale(scale,BigDecimal.ROUND_HALF_UP);
		}
	}
}
