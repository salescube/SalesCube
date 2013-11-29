/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.LinkedHashMap;
import java.util.List;

import jp.co.arkinfosys.entity.CloseCustomer;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * 顧客締処理のサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class CloseCustomerService extends AbstractService<CloseCustomer> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		private static final String SALES_DATE = "salesDate";
		private static final String SALES_STATUS = "salesStatus";
		private static final String DEPOSIT_DATE = "depositDate";
		private static final String DEPOSIT_STATUS = "depositStatus";
		private static final String SALES_CM_CATEGORY = "salesCmCategory";
		private static final String CUTOFF_GROUP = "cutoffGroup";
		private static final String PAYBACK_CYCLE_CATEGORY = "paybackCycleCategory";
		private static final String LAST_CUTOFF_DATE = "lastCutoffDate";
	};

	public String[] params = {
			Param.SALES_DATE
			,Param.SALES_STATUS
			,Param.DEPOSIT_DATE
			,Param.DEPOSIT_STATUS
			,Param.SALES_CM_CATEGORY
			,Param.CUTOFF_GROUP
			,Param.PAYBACK_CYCLE_CATEGORY
			,Param.LAST_CUTOFF_DATE
	};

	/**
	 * 請求締日を指定して、指定した日付以前に発行された未請求の売上伝票あるいは未請求の入金伝票を持つか、<br>
	 * 前回締日の繰越金額が0以外の全て取引区分の顧客コードを昇順で返します.
	 * @param cutoffDate 請求締日
	 * @return 締め処理対象顧客のリスト
	 * @throws ServiceException
	 */
	public List<CloseCustomer> findCloseArtBalanceCustomer(
			String cutoffDate )
				throws ServiceException {

		LinkedHashMap<String, Object> conditions =
			new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(	Param.SALES_DATE,cutoffDate);
		conditions.put(	Param.DEPOSIT_DATE,cutoffDate);
		return findByCondition( conditions, params, "customer/FindCloseCustomerOther.sql" );

	}

	/**
	 * 前回売掛締日を指定して、指定した日付に発行された売掛残高データが存在する全ての顧客コードを昇順で返します.
	 * @param lastCutOffDate 前回請求締日
	 * @return 締め処理対象顧客のリスト
	 * @throws ServiceException
	 */
	public List<CloseCustomer> findReOpenArtBalanceCustomer( String lastCutOffDate )
				throws ServiceException {

		LinkedHashMap<String, Object> conditions =
			new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(	Param.LAST_CUTOFF_DATE,lastCutOffDate);

		return findByCondition( conditions, params, "customer/FindReOpenCustomerOther.sql" );

	}

}
