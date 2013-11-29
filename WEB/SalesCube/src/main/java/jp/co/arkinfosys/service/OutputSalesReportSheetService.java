/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.Map;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
/**
 * 売上帳票出力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputSalesReportSheetService extends AbstractService<SalesSlipTrn>{
	/**
	 * パラメータ
	 *
	 */
	public static class Param {
		public static final String SALES_SLIP_ID = "salesSlipId";// 売上伝票番号
		public static final String SALES_DATE = "salesDate";
		public static final String CUSTOMER_CODE = "customerCode";
		public static final String SORT_COLUMN_SALES_DATE = "sortColumnSalesDate";
		public static final String SORT_ORDER = "sortOrder";
		public static final String ROW_COUNT = "rowCount";
		public static final String LOCK_RECORD = "lockRecord";
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
	}

	/**
	 * 検索処理(ピッキングリスト、組み立て指示書以外)を行います.
	 * @return 検索結果
	 *
	 */
	public BeanMap findSalesSheetByCondition(BeanMap conditions) throws ServiceException{
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			return this.selectBySqlFile(BeanMap.class,
					"sales/FindSalesSlipForReport.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索処理(大学セット)を行います.
	 * @return 検索結果
	 */
	public BeanMap findSalesSheetByConditionAddDate(BeanMap conditions) throws ServiceException{
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			return this.selectBySqlFile(BeanMap.class,
					"sales/FindSalesSlipForReportAddDate.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索処理(ピッキングリスト、組み立て指示書)を行います.
	 * @return 検索結果
	 */
	public BeanMap findPickingSheetByCondition(BeanMap conditions) throws ServiceException{
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			return this.selectBySqlFile(BeanMap.class,
					"sales/FindPickingSlip.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * デフォルトパラメータ設定
	 *
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.SALES_SLIP_ID, null);
		param.put(Param.SALES_DATE, null);
		param.put(Param.CUSTOMER_CODE, null);
		param.put(Param.SORT_COLUMN_SALES_DATE, null);
		param.put(Param.SORT_ORDER, null);
		param.put(Param.ROW_COUNT, null);
		param.put(Param.LOCK_RECORD, null);

		return param;
	}

	/**
	 * 検索条件パラメータを設定して返す
	 *
	 * @param param
	 * @return
	 */
	private void setConditionParam(Map<String, Object> conditions,
			Map<String, Object> param) {
		// 売上伝票番号
		if (conditions.containsKey(Param.SALES_SLIP_ID)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SALES_SLIP_ID))) {
				param.put(Param.SALES_SLIP_ID,new Long((String)conditions.get(Param.SALES_SLIP_ID)));
			}
		}

		// 取引区分
		param.put(Param.SALES_CM_CATEGORY,Categories.SALES_CM_CATEGORY);
	}
}
