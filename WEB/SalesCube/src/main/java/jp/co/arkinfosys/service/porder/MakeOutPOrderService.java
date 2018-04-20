/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.porder;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.entity.join.POrderSlipLineJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
/**
 *
 * 発注書発行画面サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class MakeOutPOrderService extends AbstractService<PoSlipTrn> {

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String PO_SLIP_ID = "poSlipId";
		public static final String PO_SLIP_ID_FROM = "poSlipIdFrom";
		public static final String PO_SLIP_ID_TO = "poSlipIdTo";
		public static final String PO_DATE_FROM = "poDateFrom";
		public static final String PO_DATE_TO = "poDateTo";
		public static final String SUPPLIER_CODE = "supplierCode";
		public static final String SUPPLIER_NAME = "supplierName";
		public static final String USER_NAME = "userName";
		public static final String EXCEPT_ALREADY_OUTPUT = "exceptAlreadyOutput";

		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET_ROW = "offsetRow";
		public static final String SORT_COLUMN = "sortColumn";
		private static final String SORT_ORDER = "sortOrder";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";
	}

	/**
	 * 検索条件を指定して結果件数を返します.
	 * @param params 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer getSearchPOrderResultCount(BeanMap params)
			throws ServiceException {
		try {
			Integer count = Integer.valueOf(0);

			count = findPOrderSlipCntByCondition(params);

			return count;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果件数を返します.
	 *
	 * @param conditions　検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer findPOrderSlipCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"porder/FindSlipCntByConditionForOut.sql", param).getSingleResult();
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
		param.put(Param.PO_SLIP_ID_FROM, null);
		param.put(Param.PO_SLIP_ID_TO, null);
		param.put(Param.PO_DATE_FROM, null);
		param.put(Param.PO_DATE_TO, null);
		param.put(Param.SUPPLIER_CODE, null);
		param.put(Param.SUPPLIER_NAME, null);
		param.put(Param.USER_NAME, null);
		param.put(Param.EXCEPT_ALREADY_OUTPUT, null);
		param.put(Param.SORT_COLUMN, null);
		param.put(Param.SORT_ORDER, null);
		param.put(Param.ROW_COUNT, null);
		param.put(Param.OFFSET_ROW, null);
		return param;
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	private Map<String, Object> setConditionParam(
			Map<String, Object> conditions, Map<String, Object> param) {
		// 伝票番号（開始）
		if (conditions.containsKey(Param.PO_SLIP_ID_FROM)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PO_SLIP_ID_FROM))) {
				param.put(Param.PO_SLIP_ID_FROM, conditions.get(Param.PO_SLIP_ID_FROM));
			}
		}

		// 伝票番号（終了）
		if (conditions.containsKey(Param.PO_SLIP_ID_TO)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PO_SLIP_ID_TO))) {
				param.put(Param.PO_SLIP_ID_TO, conditions.get(Param.PO_SLIP_ID_TO));
			}
		}

		// 発注日（開始）
		if (conditions.containsKey(Param.PO_DATE_FROM)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.PO_DATE_FROM))) {
				param.put(Param.PO_DATE_FROM, (String) conditions
						.get(Param.PO_DATE_FROM));
			}
		}

		// 発注日（開始）全角半角変換
		if (conditions.containsKey(Param.PO_DATE_FROM)) {
			param.put(Param.PO_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.PO_DATE_FROM)));

		}


		// 発注日（終了）
		if (conditions.containsKey(Param.PO_DATE_TO)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PO_DATE_TO))) {
				param.put(Param.PO_DATE_TO, (String) conditions
						.get(Param.PO_DATE_TO));
			}
		}

		//発注日（終了）全角半角変換
		if (conditions.containsKey(Param.PO_DATE_TO)) {
			param.put(Param.PO_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.PO_DATE_TO)));

		}

		// 仕入先コード
		if (conditions.containsKey(Param.SUPPLIER_CODE)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SUPPLIER_CODE))) {
				param.put(Param.SUPPLIER_CODE, super
						.createPrefixSearchCondition((String) conditions
								.get(Param.SUPPLIER_CODE)));
			}
		}

		// 仕入先名
		if (conditions.containsKey(Param.SUPPLIER_NAME)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SUPPLIER_NAME))) {
				param.put(Param.SUPPLIER_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(Param.SUPPLIER_NAME)));
			}
		}

		// 入力担当者名
		if (conditions.containsKey(Param.USER_NAME)) {
			if (StringUtil.hasLength((String) conditions.get(Param.USER_NAME))) {
				param.put(Param.USER_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(Param.USER_NAME)));
			}
		}

		// 発行済みを除く
		if (conditions.containsKey(Param.EXCEPT_ALREADY_OUTPUT)) {
			if ((Boolean) conditions.get(Param.EXCEPT_ALREADY_OUTPUT)) {
				param.put(Param.EXCEPT_ALREADY_OUTPUT, "true"); // nullでなければなんでもよい
			}
		}

		// ソートカラムを設定する
		if (conditions.containsKey(Param.SORT_COLUMN)) {
			if (StringUtil
					.hasLength((String) conditions.get(Param.SORT_COLUMN))) {
				param.put(Param.SORT_COLUMN, StringUtil
						.convertColumnName((String) conditions
								.get(Param.SORT_COLUMN)));
			}
		}
		// ソートオーダーを設定する
		Boolean sortOrderAsc = (Boolean) conditions.get(Param.SORT_ORDER_ASC);
		if (sortOrderAsc) {
			param.put(Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER, Constants.SQL.DESC);
		}

		// 表示件数を設定する
		if (conditions.containsKey(Param.ROW_COUNT)) {
			param.put(Param.ROW_COUNT, conditions
					.get(Param.ROW_COUNT));
		}

		// オフセットを設定する
		if (conditions.containsKey(Param.OFFSET_ROW)) {
			param.put(Param.OFFSET_ROW, conditions.get(Param.OFFSET_ROW));
		}

		return param;
	}

	/**
	 * 検索条件を指定して結果リストを返します.
	 *
	 * @param conditions 検索条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<POrderSlipLineJoin> findPOrderSlipByCondition(
			Map<String, Object> conditions) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(POrderSlipLineJoin.class,
					"porder/FindSlipByConditionForOut.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
