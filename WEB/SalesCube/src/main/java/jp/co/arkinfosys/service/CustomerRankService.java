/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.CustomerRankDto;
import jp.co.arkinfosys.entity.CustomerRank;
import jp.co.arkinfosys.entity.CustomerRankSummary;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 顧客ランクのサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class CustomerRankService extends AbstractMasterEditService<CustomerRankDto, CustomerRank>
		implements MasterSearch<CustomerRank>
		 {

	@Resource
	private SeqMakerService seqMakerService;

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String RANK_CODE = "rankCode";
		public static final String RANK_NAME = "rankName";
		public static final String RANK_RATE = "rankRate";
		public static final String RANK_RATE_1 = "rankRate1";
		public static final String RANK_RATE_2 = "rankRate2";
		public static final String POSTAGE_TYPE = "postageType";
		public static final String SORT_COLUMN_RANK = "sortColumnRank";
		public static final String SORT_ORDER = "sortOrder";
		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET = "offsetRow";

	}

	private static final String COLUMN_RANK_CODE = "RANK_CODE";
	private static final String COLUMN_RANK_NAME = "RANK_NAME";
	private static final String COLUMN_RANK_RATE = "RANK_RATE";

	/**
	 * すべての顧客ランクを返します.
	 * @return 顧客ランクのリスト
	 * @throws ServiceException
	 */
	public List<CustomerRank> findAllCustomerRank() throws ServiceException {
		try {
			return this.selectBySqlFile(CustomerRank.class,
					"customerrank/FindAllCustomerRank.sql",
					super.createSqlParam()).getResultList();
		} catch (OrderByNotFoundRuntimeException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 顧客ランクを削除します.
	 * @param dto {@link CustomerRankDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#deleteRecord(java.lang.Object)
	 */
	@Override
	public void deleteRecord(CustomerRankDto dto) throws Exception {
		try {
			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.RANK_CODE, dto.rankCode);
			this.lockRecordBySqlFile("customerrank/LockCustomerRank.sql",
					param, dto.updDatetm);

			// 削除
			param = super.createSqlParam();
			param.put(Param.RANK_CODE, dto.rankCode);
			this.updateBySqlFile("customerrank/DeleteCustomerRank.sql", param)
					.execute();
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 顧客ランクを登録します.
	 * @param dto {@link CustomerRankDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#insertRecord(java.lang.Object)
	 */
	@Override
	public void insertRecord(CustomerRankDto dto) throws Exception {
		if (dto == null) {
			return;
		}
		try {
			// 顧客情報の登録
			Map<String, Object> param = super.createSqlParam();

			long newRankCode = seqMakerService.nextval(CustomerRank.TABLE_NAME);
			dto.rankCode = String.valueOf(newRankCode);

			// データ調整
			blankToNull(dto);

			BeanMap customerRankInfo = Beans.createAndCopy(BeanMap.class, dto)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE).execute();

			param.putAll(customerRankInfo);
			this.updateBySqlFile("customerrank/InsertCustomerRank.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 顧客ランクを更新します.
	 * @param dto {@link CustomerRankDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#updateRecord(java.lang.Object)
	 */
	@Override
	public void updateRecord(CustomerRankDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(Param.RANK_CODE, dto.rankCode);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("customerrank/LockCustomerRank.sql", lockParam,
				dto.updDatetm);

		// データ調整
		blankToNull(dto);

		// 顧客情報の更新
		Map<String, Object> param = super.createSqlParam();
		BeanMap customerRankInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();

		param.putAll(customerRankInfo);
		this.updateBySqlFile("customerrank/UpdateCustomerRank.sql", param)
				.execute();

	}

	/**
	 *
	 * @param rankCode ランクID
	 * @return {@link CustomerRank}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findById(java.lang.String)
	 */
	@Override
	public CustomerRank findById(String rankCode)
			throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		this.setEmptyCondition(param);

		param.put(Param.RANK_CODE, rankCode);
		CustomerRank result = this.selectBySqlFile(CustomerRank.class,
				"customerrank/FindCustomerRankByRankCode.sql", param)
				.getSingleResult();
		return result;
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @return 検索結果数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#countByCondition(java.util.Map)
	 */
	@Override
	public int countByCondition(Map<String, Object> conditions)
			throws ServiceException {
		if (conditions == null) {
			return 0;
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			this.setCondition(conditions, null, false, param);

			return this.selectBySqlFile(Integer.class,
					"customerrank/CountCustomerRankByCondition.sql", param)
					.getSingleResult().intValue();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得件数
	 * @param offset 取得開始位置
	 * @return {@link CustomerRank}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByConditionLimit(java.util.Map, java.lang.String, boolean, int, int)
	 */
	@Override
	public List<CustomerRank> findByConditionLimit(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		if (conditions == null) {
			return new ArrayList<CustomerRank>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			this.setCondition(conditions, sortColumn, sortOrderAsc, param);

			// LIMITを設定する
			if (rowCount > 0) {
				param.put(Param.ROW_COUNT, rowCount);
				param.put(Param.OFFSET, offset);
			}

			return this.selectBySqlFile(CustomerRank.class,
					"customerrank/FindCustomerRankByConditionLimit.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @return {@link CustomerRank}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByCondition(java.util.Map, java.lang.String, boolean)
	 */
	@Override
	public List<CustomerRank> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		return new ArrayList<CustomerRank>();
		// 未使用メソッド
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件マップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.RANK_CODE, null);
		param.put(Param.RANK_NAME, null);
		param.put(Param.RANK_RATE_1, null);
		param.put(Param.RANK_RATE_2, null);
		param.put(Param.SORT_COLUMN_RANK, null);
		param.put(Param.SORT_ORDER, null);
		return param;
	}

	/**
	 * 検索条件マップを設定します.
	 * @param conditions 検索条件値のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順か否か
	 * @param param 検索条件マップ
	 */
	private void setCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {
		// 顧客ランクコード
		if (conditions.containsKey(Param.RANK_CODE)) {
			param.put(Param.RANK_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(Param.RANK_CODE)));
		}

		// 顧客ランク名
		if (conditions.containsKey(Param.RANK_NAME)) {
			param.put(Param.RANK_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(Param.RANK_NAME)));
		}

		// 値引率１
		if (conditions.containsKey(Param.RANK_RATE_1)) {
			if (conditions.get(Param.RANK_RATE_1) != null) {
				param
						.put(Param.RANK_RATE_1, Float
								.parseFloat((String) conditions
										.get(Param.RANK_RATE_1)));
			}
		}

		// 値引率２
		if (conditions.containsKey(Param.RANK_RATE_2)) {
			if (conditions.get(Param.RANK_RATE_2) != null) {
				param
						.put(Param.RANK_RATE_2, Float
								.parseFloat((String) conditions
										.get(Param.RANK_RATE_2)));
			}
		}

		// 送料区分
		if (conditions.containsKey(Param.POSTAGE_TYPE)) {
			param.put(Param.POSTAGE_TYPE, conditions.get(Param.POSTAGE_TYPE));
		}
		// ソートカラムを設定する
		if (Param.RANK_CODE.equals(sortColumn)) {
			// 顧客ランクコード
			param.put(Param.SORT_COLUMN_RANK, COLUMN_RANK_CODE);
		} else if (Param.RANK_NAME.equals(sortColumn)) {
			// 顧客ランク名
			param.put(Param.SORT_COLUMN_RANK, COLUMN_RANK_NAME);
		} else if (Param.RANK_RATE.equals(sortColumn)) {
			// 値引率
			param.put(Param.SORT_COLUMN_RANK, COLUMN_RANK_RATE);
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * 空文字列をnullに変換します.
	 * @param dto 顧客ランクDTO
	 */
	private void blankToNull(CustomerRankDto dto) {
		if (!StringUtil.hasLength(dto.roCountFrom)) {
			dto.roCountFrom = null;
		}
		if (!StringUtil.hasLength(dto.roCountTo)) {
			dto.roCountTo = null;
		}
		if (!StringUtil.hasLength(dto.enrollTermFrom)) {
			dto.enrollTermFrom = null;
		}
		if (!StringUtil.hasLength(dto.enrollTermTo)) {
			dto.enrollTermTo = null;
		}
		if (!StringUtil.hasLength(dto.defectTermFrom)) {
			dto.defectTermFrom = null;
		}
		if (!StringUtil.hasLength(dto.defectTermTo)) {
			dto.defectTermTo = null;
		}
		if (!StringUtil.hasLength(dto.roMonthlyAvgFrom)) {
			dto.roMonthlyAvgFrom = null;
		}
		if (!StringUtil.hasLength(dto.roMonthlyAvgTo)) {
			dto.roMonthlyAvgTo = null;
		}
	}

	/**
	 * 顧客ランク集計データのリストを返します.
	 * @param conditions 検索条件のマップ
	 * @return 顧客ランク集計データのリスト
	 * @throws ServiceException
	 */
	public List<CustomerRankSummary> findCustomerRankSummary(
			Map<String, Object> conditions) throws ServiceException {

		Map<String, Object> param = super.createSqlParam();
		this.setEmptyCondition(param);

		this.setCondition(conditions, null, false, param);

		return this.selectBySqlFile(CustomerRankSummary.class,
				"customerrank/FindCustomerRankSummary.sql", param)
				.getResultList();
	}

	/**
	 *
	 * @return {RANK_CODE}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getKeyColumnNames()
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] {"RANK_CODE"};
	}

	/**
	 *
	 * @return {@link CustomerRank#TABLE_NAME}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getTableName()
	 */
	@Override
	protected String getTableName() {
		return CustomerRank.TABLE_NAME;
	}


	}
