/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.report;

import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.util.BeanMap;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.Constants.REFERENCE_HISTORY_TARGET;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * 履歴参照サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ReferenceHistoryService extends AbstractService<BeanMap> {

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String OUTPUT_TARGET = "outputTarget";
		public static final String ACTION_TYPE = "actionType";
		public static final String REC_DATE_FROM = "recDateFrom";
		public static final String REC_DATE_TO = "recDateTo";
		public static final String ESTIMATE_DATE_FROM = "estimateDateFrom";
		public static final String ESTIMATE_DATE_TO = "estimateDateTo";
		public static final String CUSTOMER_CODE_FROM = "customerCodeFrom";
		public static final String CUSTOMER_CODE_TO = "customerCodeTo";
		public static final String PRODUCT_CODE_FROM = "productCodeFrom";
		public static final String PRODUCT_CODE_TO = "productCodeTo";
		public static final String SHIP_DATE_FROM = "shipDateFrom";
		public static final String SHIP_DATE_TO = "shipDateTo";
		public static final String SUPPLIER_CODE_FROM = "supplierCodeFrom";
		public static final String SUPPLIER_CODE_TO = "supplierCodeTo";
		public static final String DELIVERY_DATE_FROM = "deliveryDateFrom";
		public static final String DELIVERY_DATE_TO = "deliveryDateTo";
		public static final String PAYMENT_DATE_FROM = "paymentDateFrom";
		public static final String PAYMENT_DATE_TO = "paymentDateTo";
		public static final String EAD_SLIP_CATEGORY = "eadSlipCategory";
		public static final String CRE_DATE_FROM = "creDateFrom";
		public static final String CRE_DATE_TO = "creDateTo";
	}

	/**
	 * パラメータを配列化
	 */
	private static final String[] paramArray = {
		Param.OUTPUT_TARGET,
		Param.ACTION_TYPE,
		Param.REC_DATE_FROM,
		Param.REC_DATE_TO,
		Param.ESTIMATE_DATE_FROM,
		Param.ESTIMATE_DATE_TO,
		Param.CUSTOMER_CODE_FROM,
		Param.CUSTOMER_CODE_TO,
		Param.PRODUCT_CODE_FROM,
		Param.PRODUCT_CODE_TO,
		Param.SHIP_DATE_FROM,
		Param.SHIP_DATE_TO,
		Param.SUPPLIER_CODE_FROM,
		Param.SUPPLIER_CODE_TO,
		Param.DELIVERY_DATE_FROM,
		Param.DELIVERY_DATE_TO,
		Param.PAYMENT_DATE_FROM,
		Param.PAYMENT_DATE_TO,
		Param.CRE_DATE_FROM,
		Param.CRE_DATE_TO
	};

	/**
	 * 出力対象の伝票リストを返します.
	 * @param params 出力条件
	 * @return 伝票リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getSlipListByCondition(BeanMap params) throws ServiceException {
		try {
			// 出力対象を取得する
			String outputTarget = (String)params.get(Param.OUTPUT_TARGET);

			// 出力対象毎に処理を分岐
			String sql = null;
			if (REFERENCE_HISTORY_TARGET.VALUE_ESTIMATE.equals(outputTarget)) {
				sql="report/FindEstimateSCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_RORDER.equals(outputTarget)) {
				sql="report/FindROrderSCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_SALES.equals(outputTarget)) {
				sql="report/FindSalesSCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_DEPOSIT.equals(outputTarget)) {
				sql="report/FindDepositSCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PORDER.equals(outputTarget)) {
				sql="report/FindPOrderSCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PURCHASE.equals(outputTarget)) {
				sql="report/FindPurchaseSCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PAYMENT.equals(outputTarget)) {
				sql="report/FindPaymentSCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_STOCK.equals(outputTarget)) {
				sql="report/FindStockSCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_CUSTOMER.equals(outputTarget)) {
				sql="report/FindCustomerCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PRODUCT.equals(outputTarget)) {
				sql="report/FindProductCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_SUPPLIER.equals(outputTarget)) {
				sql="report/FindSupplierCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_USER.equals(outputTarget)) {
				sql="report/FindUserCondition.sql";
			}

			return findRecordByCondition(sql, params);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 出力対象の明細行リストを取得します.
	 * @param params 出力条件
	 * @return 明細行リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getDetailListByCondition(BeanMap params) throws ServiceException {
		try {
			// 出力対象を取得する
			String outputTarget = (String)params.get(Param.OUTPUT_TARGET);

			// 出力対象毎に処理を分岐
			String sql = null;
			if (REFERENCE_HISTORY_TARGET.VALUE_ESTIMATE.equals(outputTarget)) {
				sql="report/FindEstimateLCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_RORDER.equals(outputTarget)) {
				sql="report/FindROrderLCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_SALES.equals(outputTarget)) {
				sql="report/FindSalesLCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_DEPOSIT.equals(outputTarget)) {
				sql="report/FindDepositLCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PORDER.equals(outputTarget)) {
				sql="report/FindPOrderLCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PURCHASE.equals(outputTarget)) {
				sql="report/FindPurchaseLCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_PAYMENT.equals(outputTarget)) {
				sql="report/FindPaymentLCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_STOCK.equals(outputTarget)) {
				sql="report/FindStockLCondition.sql";
			}
			else if (REFERENCE_HISTORY_TARGET.VALUE_CUSTOMER.equals(outputTarget)) {
				sql="report/FindDeliveryCondition.sql";
			}
			else {
				return null;
			}

			return findRecordByCondition(sql, params);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 出力条件を指定して結果リストを返します.
	 *
	 * @param sql　検索用SQL文
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
	 * @param param 出力条件オブジェクト
	 * @return 空の出力条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		for (int i=0;i<ReferenceHistoryService.paramArray.length;i++) {
			param.put(ReferenceHistoryService.paramArray[i], null);
		}

		// 入出庫伝票の初期化（これだけ型が異なるので別扱い）
		param.put(Param.EAD_SLIP_CATEGORY, null);

		return param;
	}

	/**
	 * 出力条件パラメータを設定して返します.
	 *
	 * @param conditions 出力条件
	 * @param param 出力条件パラメータ
	 * @return 出力条件パラメータが設定された出力条件
	 */
	private Map<String, Object> setConditionParam(
			Map<String, Object> conditions, Map<String, Object> param) {
		for (int i=0;i<ReferenceHistoryService.paramArray.length;i++) {
			String key = ReferenceHistoryService.paramArray[i];
			if (conditions.containsKey(key)) {
				if (StringUtil.hasLength((String)conditions.get(key))) {
					param.put(key,(String)conditions.get(key));

					if(0<i && i<5 ){
						// 入力／変更日範囲 ・見積日範囲 の日付全角→半角
							param.put(key, StringUtil.zenkakuNumToHankaku((String) conditions
									.get(key)));
					}
				}
			}
		}



		// 入出庫伝票の初期化（これだけ型が異なるので別扱い）
		if (conditions.containsKey(Param.EAD_SLIP_CATEGORY)) {
			Object obj = conditions.get(Param.EAD_SLIP_CATEGORY);
			if (obj != null) {
				String[] arry = (String[]) obj;
				if (arry.length > 0) {
					param.put(Param.EAD_SLIP_CATEGORY, arry);
				}
			}
		}

		return param;
	}
}
