/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import java.util.List;

import org.seasar.framework.beans.util.BeanMap;


/**
 * プリント用のユーティリティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class PrintUtil {

	/**
	 * リスト内の特殊商品コードを空文に置換します.
	 * @param beanMapList　リスト
	 */
	public static void setSpaceToExceptianalProductCode(List<BeanMap> beanMapList) {
		for( BeanMap bm : beanMapList ){
			if( CheckUtil.isLooseExceptianalProductCode(bm.get("productCode").toString())){
				bm.put("productCode", "");
			}
		}
	}

	/**
	 * リスト内の特殊商品コード行を削除します.
	 * @param beanMapList　リスト
	 */
	public static void removeSpaceToExceptianalProductLine(List<BeanMap> beanMapList) {
		for( int i = beanMapList.size()-1 ; i >= 0 ; i-- ){
			BeanMap bm = beanMapList.get(i);
			if( CheckUtil.isLooseExceptianalProductCode(bm.get("productCode").toString())){
				beanMapList.remove(i);
			}
		}
	}


}
