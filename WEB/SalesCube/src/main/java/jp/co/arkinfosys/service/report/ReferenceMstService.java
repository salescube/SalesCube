/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.report;

import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.util.BeanMap;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.Constants.REFERENCE_MST_TARGET;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * マスタリスト出力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ReferenceMstService extends AbstractService<BeanMap> {
	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String OUTPUT_TARGET = "outputTarget";
		public static final String CUSTOMER_CODE_FROM = "customerCodeFrom";
		public static final String CUSTOMER_CODE_TO = "customerCodeTo";
		public static final String CRE_DATE_FROM = "creDateFrom";
		public static final String CRE_DATE_TO = "creDateTo";
	}

	/**
	 * パラメータを配列化
	 */
	private static final String[] paramArray = {
		Param.OUTPUT_TARGET,
		Param.CUSTOMER_CODE_FROM,
		Param.CUSTOMER_CODE_TO,
		Param.CRE_DATE_FROM,
		Param.CRE_DATE_TO
	};

	/**
	 * マスタリストを返します.
	 * @param params 出力条件
	 * @return マスタリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getMasterListByCondition(BeanMap params) throws ServiceException {
		try {
			// 出力対象を取得する
			String outputTarget = (String)params.get(Param.OUTPUT_TARGET);

			// 出力対象毎に処理を分岐
			String sql = null;
			if (REFERENCE_MST_TARGET.VALUE_CUSTOMER.equals(outputTarget)) {
				sql="report/FindCustomerByCondition4MstList.sql";
			}

			return findRecordByCondition(sql, params);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 明細行リストを返します.
	 * @param params 出力条件
	 * @return 明細行リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getDetailListByCondition(BeanMap params) throws ServiceException {

		try {

			// 出力対象を取得する
			String outputTarget = (String)params.get(Param.OUTPUT_TARGET);

			// 出力対象毎に処理を分岐
			//String sql = null;

			if (REFERENCE_MST_TARGET.VALUE_CUSTOMER.equals(outputTarget)) {
				//sql="report/FindDeliveryCondition.sql";
			}
			else {
				return null;
			}

			return null;
			//return findRecordByCondition(sql, params);
		//} catch (ServiceException e) {
		//	throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 出力条件を指定して結果リストを返します.
	 *
	 * @param conditions 出力条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findRecordByCondition(String sql, Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class, sql, param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 出力条件
	 * @return 空の出力条件
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		for (int i=0;i<ReferenceMstService.paramArray.length;i++) {
			param.put(ReferenceMstService.paramArray[i], null);
		}

		return param;
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 *
	 * @param conditions 出力条件
	 * @param param 出力条件パラメータ
	 * @return 出力条件パラメータが設定された出力条件
	 */
	private Map<String, Object> setConditionParam(
			Map<String, Object> conditions, Map<String, Object> param) {
		for (int i=0;i<ReferenceMstService.paramArray.length;i++) {
			String key = ReferenceMstService.paramArray[i];
			if (conditions.containsKey(key)) {
				if (StringUtil.hasLength((String)conditions.get(key))) {
					param.put(key,(String)conditions.get(key));
				}
			}
		}

		return param;
	}
}
