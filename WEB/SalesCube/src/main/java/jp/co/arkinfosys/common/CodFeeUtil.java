/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import jp.co.arkinfosys.entity.Customer;


/**
 * 代引き手数料に関するユーティリティクラスです.
 * @author Ark Information Systems
 *
 */
public class CodFeeUtil {

	/**
	 * 代引き手数料を返します.<BR>
	 * 代引き手数料の必要性判断も実施します.<BR>
	 * 　戻り値がnullの場合は代引き手数料は必要がありません.
	 *
	 * @param normalTotalPrice　特殊コードを含まない商品税込合計金額
	 * @return　代引き手数料（代引き手数料の必要がない場合はnullを返します）
	 */
	public static String getCodFee( Customer customer, Double normalTotalPrice ) {

		if( !CategoryTrns.SALES_CM_CASH_ON_DELIVERY.equals( customer.salesCmCategory ) ){
			return null;
		}

		String codFeePrice;
		if( normalTotalPrice <= 10000 ){
			codFeePrice = "300";
		}else if( normalTotalPrice <= 30000 ){
			codFeePrice = "400";
		}else if( normalTotalPrice <= 100000 ){
			codFeePrice = "600";
		}else if( normalTotalPrice <= 300000 ){
			codFeePrice = "1000";
		}else if( normalTotalPrice <= 500000 ){
			codFeePrice = "2000";
		}else if( normalTotalPrice <= 1000000 ){
			codFeePrice = "3000";
		}else{
			codFeePrice = "4000";
		}
		return codFeePrice;
	}
}
