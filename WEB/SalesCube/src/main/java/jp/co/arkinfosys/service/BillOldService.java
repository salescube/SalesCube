/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.entity.Bill;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 請求書退避データサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class BillOldService extends AbstractService<Bill> {


	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		
		private static final String BILL_ID = "billId";
	}

	public String[] params = {
			Param.BILL_ID
	};

	/**
	 * SQL用のパラメータマップを作成します.
	 * @param bill 請求書情報
	 * @return パラメータマップ
	 */
	private Map<String, Object> createParamMap(Bill bill){

		
		Map<String, Object> param = new HashMap<String, Object>();

		



		
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class,bill).execute();
		param.putAll(AFparam);

		
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return param;
	}

	/**
	 * 請求書情報を登録します.
	 * @param bill 請求書情報
	 * @return 登録した件数
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#insert(java.lang.Object)
	 */
	public int insert(Bill bill){
		int SuccessedLineCount = 0;
		
		Map<String, Object> param = createParamMap(bill);

		
		SuccessedLineCount = this.updateBySqlFile("bill/InsertOldBill.sql", param).execute();

		return SuccessedLineCount;
	}

	/**
	 * 請求書IDを指定して、請求書情報を返します.
	 * @param billId 請求書ID
	 * @return 請求書情報
	 * @throws ServiceException
	 */
	public Bill findBillById(Integer billId ) throws ServiceException {
		LinkedHashMap<String, Object> conditions =
			new LinkedHashMap<String, Object>();

		
		
		conditions.put(	Param.BILL_ID,billId);

		List<Bill> billList = findByCondition( conditions, params, "bill/FindBillOldById.sql" );
		if( billList.size() != 1 ){
			return null;
		}
		return billList.get(0);

	}

}
