/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import jp.co.arkinfosys.entity.Product;

/**
 * 商品に関するチェックを行うユーティリティクラスです.
 * @author Ark Information Systems
 *
 */
public class CheckUtil {

	private static final String START_WITH_XXX = "XXX";
	private static final String START_WITH_ZZZ = "ZZZ";

	/**
	 * 特殊商品コードのルーズ判定を行います.<BR>
	 * 先頭３文字で判断します.
	 * @param product {@link Product}
	 * @return 特殊商品コードか否か
	 */
	public static boolean isLooseExceptianalProduct( Product product ) {
		return isLooseExceptianalProductCode( product.productCode );
	}

	/**
	 * 特殊商品コードのルーズ判定を行います.<BR>
	 * 先頭３文字で判断します.
	 * @param productCode 商品コード
	 * @return 特殊商品コードか否か
	 */
	public static boolean isLooseExceptianalProductCode( String productCode ) {
		if( productCode == null ){
			return false;
		}
		if( !StringUtil.hasLength(productCode) ){
			return false;
		}
		if( productCode.startsWith(START_WITH_XXX)){
			return true;
		}
		if( productCode.startsWith(START_WITH_ZZZ)){
			return true;
		}
		return false;
	}

	/**
	 * 棚番必須の商品か判定します.<BR>
	 * この関数がtrueを返す商品に対して入出庫伝票を作成します.
	 * @param product {@link Product}
	 * @return 棚番必須の商品か否か
	 */
	public static boolean isRackCheck( Product product ) {
		// セット品は無視
		if( CategoryTrns.PRODUCT_SET_TYPE_SET.equals( product.setTypeCategory ) ){
			return false;
		}
		// 特殊商品コードは無視
		if( isLooseExceptianalProduct(product)){
			return false;
		}
		return true;
	}

}
