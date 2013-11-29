/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.report;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.entity.ArtBalance;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.ArtBalanceService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 残高一覧表出力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputBalanceListService extends AbstractService<BeanMap> {

	@Resource
	ArtBalanceService artBalanceService;

	@Resource
	CustomerService customerService;

	@Resource
	CategoryService categoryService;

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String OUTPUT_TARGET = "outputTarget";
		public static final String TARGET_DATE = "targetDate";
		public static final String SUPPLIER_CODE = "supplierCode";
		public static final String CUSTOMER_CODE = "customerCode";
		public static final String CUSTOMER_CODE_FROM = "customerCodeFrom";
		public static final String CUSTOMER_CODE_TO = "customerCodeTo";
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
		public static final String THIS_SALES_PRICE = "thisSalesPrice";
		public static final String DEPOSIT_RATE = "depositRate";
		public static final String DEPOSIT_PRICE = "depositPrice";
		public static final String LATEST_SALES_DATE = "latestSalesDate";
		public static final String LATEST_DEPOSIT_DATE = "latestDepositDate";
		public static final String DATE_TO = "dateTo";
	}

	/**
	 * 出力条件を指定して結果件数を返します.
	 * @param params 出力条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer getOutputResultCount(BeanMap params) throws ServiceException {
		try {
			Integer count = Integer.valueOf(0);

			// 出力対象を取得する
			String outputTarget = (String) params.get(Param.OUTPUT_TARGET);

			// 買掛残高一覧か売上残高一覧か
			if (Constants.OUTPUT_BALANCE_TARGET.VALUE_PORDER
					.equals(outputTarget)) {
				count = findPOrderBalanceCntByCondition(params);
			} else if (Constants.OUTPUT_BALANCE_TARGET.VALUE_RORDER
					.equals(outputTarget)) {
				count = findROrderBalanceCntByCondition(params);
			}

			return count;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 出力条件を指定して結果リストを返します.
	 * @param params 出力条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getOutputResult(BeanMap params)
			throws ServiceException {
		try {
			// 出力対象を取得する
			String outputTarget = (String) params.get(Param.OUTPUT_TARGET);

			// 買掛残高一覧か売上残高一覧か
			if (Constants.OUTPUT_BALANCE_TARGET.VALUE_PORDER
					.equals(outputTarget)) {
				return findPOrderBalanceByCondition(params);
			} else if (Constants.OUTPUT_BALANCE_TARGET.VALUE_RORDER
					.equals(outputTarget)) {
				return findROrderBalanceByCondition(params);
			}

			return null;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 出力条件を指定して買掛残高一覧表の結果件数を返します.
	 *
	 * @param conditions 出力条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer findPOrderBalanceCntByCondition(
			Map<String, Object> conditions) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"report/FindPOrderCntByCondition.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 出力条件を指定して売掛残高一覧表の結果件数を返します.
	 *
	 * @param conditions 出力条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer findROrderBalanceCntByCondition(
			Map<String, Object> conditions) throws ServiceException {
		try {
			return 1; // この時点ではカウントしない。(対象年月に未来日は指定されないことから、ほぼ間違いないく件数0件はあり得ないため。)
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 出力条件を指定して買掛残高一覧表の結果リストを返します.
	 *
	 * @param conditions 出力条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findPOrderBalanceByCondition(
			Map<String, Object> conditions) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class,
					"report/FindPOrderByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 出力条件を指定して売掛残高一覧表の結果リストを返します.
	 *
	 * @param conditions 出力条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findROrderBalanceByCondition(
			Map<String, Object> conditions) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);
			param.put(Param.SALES_CM_CATEGORY, Categories.SALES_CM_CATEGORY);

			// 新システムの売掛残高を表示する場合
			List<BeanMap> beanList = new ArrayList<BeanMap>();
			if (conditions.containsKey(Param.TARGET_DATE)
					&& conditions.containsKey(Param.CUSTOMER_CODE_FROM)
					&& conditions.containsKey(Param.CUSTOMER_CODE_TO)) {

				// 売掛残高の出力対象年月度
				String targetYmLastDate = ValidateUtil
				.getLastDateOfMonthFromYmFormat((String) conditions
						.get(Param.TARGET_DATE));

				List<String> targetCustomerCodeList = customerService
				.findExistArtBalanceCustomerCodeList(
						targetYmLastDate, (String) conditions
						.get(Param.CUSTOMER_CODE_FROM),
						(String) conditions
						.get(Param.CUSTOMER_CODE_TO));
				for (String targetCustomerCode : targetCustomerCodeList) {

					// 売掛残高の取得
					ArtBalance artBalance = artBalanceService
					.getArtBalanceByDate(targetYmLastDate,
							targetCustomerCode);
					// 売掛残高一覧表に表示できる値が無い場合は売掛残高一覧表に出力しない
					if (isZeroPriceArtBalance(artBalance)) {
						continue;
					}

					BeanMap beanMap = Beans.createAndCopy(BeanMap.class,
							artBalance).execute();
					// 当月売掛残高を計算する
					beanMap.put(Param.THIS_SALES_PRICE,
							artBalance.salesPrice.add(artBalance.dctPrice)
							.add(artBalance.rguPrice).add(
									artBalance.etcPrice));
					// 売上取引区分の表示名を取得する
					String salesCmCategory = (String) beanMap
					.get(Param.SALES_CM_CATEGORY);
					beanMap.remove(Param.SALES_CM_CATEGORY);
					beanMap.put(Param.SALES_CM_CATEGORY, categoryService
							.findCategoryTrnByIdAndCode(
									Categories.SALES_CM_CATEGORY,
									salesCmCategory).categoryCodeName);
					// 入金額の補正
					//						BigDecimal depositPrice = (BigDecimal)beanMap.get(Param.DEPOSIT_PRICE);
					beanMap.remove(Param.DEPOSIT_PRICE);
					beanMap.put(Param.DEPOSIT_PRICE,
							artBalance.depositPrice
							.add(artBalance.adjPrice));
					// 回収率
					if (artBalance.lastArtPrice.compareTo(new BigDecimal(
					"0")) != 0) {
						beanMap.put(Param.DEPOSIT_RATE,
								(artBalance.depositPrice
										.add(artBalance.adjPrice)).divide(
												artBalance.lastArtPrice,
												MathContext.DECIMAL64));
					}

					// 最終売上日、最終入金日を取得
					BeanMap latestSalesDeposit = findLatestSalesDepositDateByCustomerCode(
							targetCustomerCode, targetYmLastDate);
					beanMap.put(Param.LATEST_SALES_DATE, latestSalesDeposit
							.get(Param.LATEST_SALES_DATE));
					beanMap.put(Param.LATEST_DEPOSIT_DATE,
							latestSalesDeposit
							.get(Param.LATEST_DEPOSIT_DATE));

					beanList.add(beanMap);
				}
			}
			return beanList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 売掛残高一覧表に表示する値が無いか否かを返します.<br>
	 * (0円以外の金額があるかを見て判定します)
	 * @param artBalance 売掛残高エンティティ
	 * @return 表示する値が無いか否か
	 */
	private boolean isZeroPriceArtBalance(ArtBalance artBalance) {
		BigDecimal zeroPrice = new BigDecimal("0");

		if (artBalance.lastArtPrice.compareTo(zeroPrice) == 0
				&& artBalance.thisArtPrice.compareTo(zeroPrice) == 0
				&& artBalance.salesPrice.compareTo(zeroPrice) == 0
				&& artBalance.depositPrice.compareTo(zeroPrice) == 0
				&& artBalance.ctaxPrice.compareTo(zeroPrice) == 0
				&& artBalance.covPrice.compareTo(zeroPrice) == 0
				&& artBalance.rguPrice.compareTo(zeroPrice) == 0
				&& artBalance.dctPrice.compareTo(zeroPrice) == 0
				&& artBalance.etcPrice.compareTo(zeroPrice) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 顧客の最終売上日と最終入金日を返します.
	 * @param customerCode 顧客コード
	 * @param dateTo 対象年月日(入力月の月末)
	 * @return 検索結果
	 * @throws ServiceException
	 */
	public BeanMap findLatestSalesDepositDateByCustomerCode(
			String customerCode, String dateTo) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			param.put(Param.CUSTOMER_CODE, customerCode);
			param.put(Param.DATE_TO, dateTo);

			return this.selectBySqlFile(BeanMap.class,
					"report/FindLatestSalesDepositDateByCustomerCode.sql",
					param).getSingleResult();
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
		param.put(Param.OUTPUT_TARGET, null);
		param.put(Param.TARGET_DATE, null);
		param.put(Param.SUPPLIER_CODE, null);
		param.put(Param.CUSTOMER_CODE_FROM, null);
		param.put(Param.CUSTOMER_CODE_TO, null);
		return param;
	}

	/**
	 * 出力条件パラメータを設定して返します.
	 *
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	private Map<String, Object> setConditionParam(
			Map<String, Object> conditions, Map<String, Object> param) {

		// 出力対象
		if (conditions.containsKey(Param.OUTPUT_TARGET)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.OUTPUT_TARGET))) {
				param.put(Param.OUTPUT_TARGET, conditions
						.get(Param.OUTPUT_TARGET));
			}
		}

		// 対象年月
		if (conditions.containsKey(Param.TARGET_DATE)) {
			if (StringUtil
					.hasLength((String) conditions.get(Param.TARGET_DATE))) {
				String targetDate = (String) conditions.get(Param.TARGET_DATE);
				param.put(Param.TARGET_DATE, this.convertTargetYM(targetDate));
			}
		}

		// 仕入先コード
		if (conditions.containsKey(Param.SUPPLIER_CODE)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SUPPLIER_CODE))) {
				param.put(Param.SUPPLIER_CODE, conditions
						.get(Param.SUPPLIER_CODE));
			}
		}

		// 顧客コードFrom
		if (conditions.containsKey(Param.CUSTOMER_CODE_FROM)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.CUSTOMER_CODE_FROM))) {
				param.put(Param.CUSTOMER_CODE_FROM, conditions
						.get(Param.CUSTOMER_CODE_FROM));
			}
		}

		// 顧客コードTo
		if (conditions.containsKey(Param.CUSTOMER_CODE_TO)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.CUSTOMER_CODE_TO))) {
				param.put(Param.CUSTOMER_CODE_TO, conditions
						.get(Param.CUSTOMER_CODE_TO));
			}
		}

		return param;
	}

	/**
	 * 対象年月（YYYY/MM）を数値（YYYYMM）に変換します.
	 * @param targetDate 対象年月
	 * @return 変換された対象年月
	 */
	public int convertTargetYM(String targetDate) {
		String[] parts = targetDate.split("/");
		int yy = Integer.parseInt(parts[0]);
		int mm = Integer.parseInt(parts[1]);
		return (yy * 100 + mm);
	}
}
