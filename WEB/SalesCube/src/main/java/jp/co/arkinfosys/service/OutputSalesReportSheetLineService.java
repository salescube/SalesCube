/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.SalesLineTrn;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;

/**
 * 売上帳票明細行出力サービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputSalesReportSheetLineService extends
		AbstractService<SalesLineTrn> {
	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String SALES_SLIP_ID = "salesSlipId";// 売上伝票番号
		public static final String SALES_LINE_ID = "salesLineId";
		public static final String SORT_COLUMN_LINE_NO = "sortColumnLineNo";
		public static final String SORT_ORDER = "sortOrder";
		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET_ROW = "offsetRow";
		public static final String LOCK_RECORD = "lockRecord";
		public static final String SALES_DATE = "salesDate";
		public static final String PICKING_LIST_ID = "pickingListId";
		public static final String SET_TYPE_CATEGORY = "setTypeCategory";
	}

	/**
	 * 検索条件を指定して、売上帳票出力のデータを取得します.<br>
	 * (ピッキングリスト・組み立て指示書以外)
	 *
	 * @param conditions 検索条件
	 * @return 結果リスト
	 */
	public List<BeanMap> findSalesLineSheetByCondition(BeanMap conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			return this.selectBySqlFile(BeanMap.class,
					"sales/FindSalesLineSheet.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、ピッキングリスト・組み立て指示書の売上帳票データを取得します.
	 *
	 * @param conditions 検索条件
	 * @return 結果リスト
	 */
	public List<BeanMap> findPickingLineSheetByCondition(BeanMap conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			return this.selectBySqlFile(BeanMap.class,
					"sales/FindPickingLine.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.SALES_SLIP_ID, null);// 売上番号
		param.put(Param.SALES_LINE_ID, null);
		param.put(Param.SORT_COLUMN_LINE_NO, null);
		param.put(Param.SORT_ORDER, null);
		param.put(Param.ROW_COUNT, null);
		param.put(Param.OFFSET_ROW, null);
		param.put(Param.LOCK_RECORD, null);
		param.put(Param.SET_TYPE_CATEGORY, null);

		return param;
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	private void setConditionParam(Map<String, Object> conditions,
			Map<String, Object> param) {
		// 売上伝票番号
		if (conditions.containsKey(Param.SALES_SLIP_ID)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SALES_SLIP_ID))) {
				param.put(Param.SALES_SLIP_ID, new Long((String) conditions
						.get(Param.SALES_SLIP_ID)));
			}
		}

		// ピッキングリストID
		if (conditions.containsKey(Param.PICKING_LIST_ID)) {
			param.put(Param.PICKING_LIST_ID, (Integer) conditions
					.get(Param.PICKING_LIST_ID));
		}

		// セット商品フラグ
		if (conditions.containsKey(Param.SET_TYPE_CATEGORY)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SET_TYPE_CATEGORY))) {
				param.put(Param.SET_TYPE_CATEGORY, (String) conditions
						.get(Param.SET_TYPE_CATEGORY));
			}
		}

	}

}
