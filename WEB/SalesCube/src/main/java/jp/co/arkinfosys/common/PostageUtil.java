/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import jp.co.arkinfosys.entity.Customer;


/**
 * 送料に関するユーティリティクラスです.
 * @author Ark Information Systems
 *
 */
public class PostageUtil {

	/**
	 * 送料を返します.<BR>
	 * 送料の必要性判断も実施します.<BR>
	 * 　戻り値がnullの場合は送料の必要はありません.
	 * @param customer　顧客情報
	 * @param normalTotalPrice　特殊コードを含まない商品税込合計金額
	 * @return　送料(送料の必要がない場合はnullを返します）
	 */
	public static String getPostagePrice( Customer customer, Double normalTotalPrice ) {
		String postagePrice = null;
		// 3000円未満は登録しない
		if( normalTotalPrice >= 3000 ){
			return null;
		}
		postagePrice = "500";
		return postagePrice;
	}
}
