/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.sales;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.sales.OutputInvoiceSearchResultDto;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.join.SalesSlipTrnJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 送り状データ出力検索サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchOutputInvoiceService extends AbstractService<SalesSlipTrn> {
	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String SALES_DATE = "salesDate";// 売上日
		public static final String SALES_CM_CATEGORY = "salesCmCategory";// 取引区分
		public static final String DC_CATEGORY = "dcCategory";// 配送業者
		public static final String EXCLUDING_OUTPUT = "excludingOutput";// 出力済を除く
		public static final String SALES_SLIP_ID = "salesSlipId";// 売上番号ID
		public static final String SI_PRINT_COUNT = "siPrintCount";
		public static final String ROW_COUNT = "rowCount";// 検索結果最大表示件数

		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";
		public static final String OFFSET_ROW = "offsetRow";
	}

	/**
	 * 検索条件を指定して検索結果件数を返します.
	 * @param params 検索条件
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	public Integer getSearchResultCount(BeanMap params) throws ServiceException {
		try {
			return findSlipCntByCondition(params);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して検索結果件数を返します.
	 *
	 * @param conditions 検索条件
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	public Integer findSlipCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"sales/FindInvoiceCntByConditions.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索結果の変換を行います.
	 *
	 * @param beanMapList 変換前検索結果(List<BeanMap>)
	 * @return 変換後検索結果(List<{@link OutputInvoiceSearchResultDto}>)
	 */
	public List<OutputInvoiceSearchResultDto> convertToDto(
			List<BeanMap> beanMapList) throws ServiceException {
		try {
			List<OutputInvoiceSearchResultDto> resultList = new ArrayList<OutputInvoiceSearchResultDto>();
			for (BeanMap resultMap : beanMapList) {
				OutputInvoiceSearchResultDto dto = Beans.createAndCopy(
						OutputInvoiceSearchResultDto.class, resultMap)
						.dateConverter(Constants.FORMAT.DATE).execute();

				// 送り状データ出力フラグ(出力済 or 未出力)
				if (dto.siPrintCount != null
						&& Integer.valueOf(dto.siPrintCount) > 0) {
					// 出力済
					dto.isSiPrinted = true;
				}

				resultList.add(dto);
			}

			return resultList;
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
		param.put(Param.SALES_DATE, null);
		param.put(Param.SALES_CM_CATEGORY, null);
		param.put(Param.DC_CATEGORY, null);
		param.put(Param.EXCLUDING_OUTPUT, null);
		param.put(Param.SALES_SLIP_ID, null);
		param.put(Param.SORT_COLUMN, null);
		param.put(Param.SORT_ORDER_ASC, true);
		param.put(Param.OFFSET_ROW, 0);
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

		// 売上日
		if (conditions.containsKey(Param.SALES_DATE)) {
			if (StringUtil.hasLength((String) conditions.get(Param.SALES_DATE))) {
				param.put(Param.SALES_DATE, (String) conditions
						.get(Param.SALES_DATE));
			}
		}

		// 取引区分
		if (conditions.containsKey(Param.SALES_CM_CATEGORY)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SALES_CM_CATEGORY))) {
				param.put(Param.SALES_CM_CATEGORY, (String) conditions
						.get(Param.SALES_CM_CATEGORY));
			}
		}

		// 配送業者
		if (conditions.containsKey(Param.DC_CATEGORY)) {
			if (StringUtil
					.hasLength((String) conditions.get(Param.DC_CATEGORY))) {
				param.put(Param.DC_CATEGORY, (String) conditions
						.get(Param.DC_CATEGORY));
			}
		}

		// 「出力済を除く」チェックボックス
		if (conditions.containsKey(Param.EXCLUDING_OUTPUT)) {
			if ((Boolean) conditions.get(Param.EXCLUDING_OUTPUT)) {
				param.put(Param.EXCLUDING_OUTPUT, "true");
			}
		}

		// 売上番号
		if (conditions.containsKey(Param.SALES_SLIP_ID)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SALES_SLIP_ID))) {
				param.put(Param.SALES_SLIP_ID, (String) conditions
						.get(Param.SALES_SLIP_ID));
			}
		}

		// 取得件数制限
		if (conditions.containsKey(Param.ROW_COUNT)) {
			param.put(Param.ROW_COUNT, conditions
					.get(Param.ROW_COUNT));
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
		if (sortOrderAsc != null && sortOrderAsc) {
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.DESC);
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
	 * @param conditions 検索条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findSlipByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class,
					"sales/FindInvoiceByConditions.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果リストを返します.
	 * @param salesIdList 売上伝票番号のリスト
	 * @return 結果リスト(送り状データ)
	 * @throws ServiceException
	 */
	public List<SalesSlipTrnJoin> getOutputInvoiceList(List<String> salesIdList)
			throws ServiceException {
		try {
			// パラメータ設定
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			param.put(Param.SALES_SLIP_ID, salesIdList);

			// 検索実行
			return this.selectBySqlFile(SalesSlipTrnJoin.class,
					"sales/FindInvoiceByConditions.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
