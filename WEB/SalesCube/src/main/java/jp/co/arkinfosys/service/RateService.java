/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.RateDto;
import jp.co.arkinfosys.dto.master.RateTrnDto;
import jp.co.arkinfosys.entity.Rate;
import jp.co.arkinfosys.entity.RateTrn;
import jp.co.arkinfosys.entity.join.RateJoin;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * レートマスタサービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class RateService extends AbstractMasterEditService<RateDto, RateJoin> implements
		MasterSearch<RateJoin> {

	@Resource
	private SeqMakerService seqMakerService;

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		/** レートID **/
		public static final String RATE_ID = "rateId";

		/** レートタイプ名称 */
		public static final String NAME = "name";

		/** 記号 */
		public static final String SIGN = "sign";

		/** 備考 */
		public static final String REMARKS = "remarks";

		/** 検索条件（期間の開始） */
		public static final String START_DATE_1 = "startDate1";

		/** 検索条件（期間の終了） */
		public static final String START_DATE_2 = "startDate2";

		/** レートデータの開始日 */
		public static final String START_DATE = "startDate";

		/** レートデータのレート */
		public static final String RATE = "rate";

		/** レートデータの備考 */
		public static final String TRN_REMARKS = "trnRemarks";

		private static final String SORT_ORDER = "sortOrder";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";

		private static final String SORT_COLUMN_RATE = "sortColumnRate";
	}

	private static final String COLUMN_RATE_ID = "RATE_ID";
	private static final String COLUMN_NAME = "NAME";
	private static final String COLUMN_RATE = "RATE";
	private static final String COLUMN_START_DATE = "START_DATE";
	private static final String COLUMN_REMARKS = "REMARKS";

	/**
	 * 全レート情報を取得します.
	 * @return レートエンティティのリスト
	 * @throws ServiceException
	 */
	public List<Rate> findAllRate() throws ServiceException {
		try {
			return this.selectBySqlFile(Rate.class, "rate/FindAllRate.sql",
					super.createSqlParam()).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * レートタイプIDとレートタイプ名称のMapを取得します.
	 * @return レートタイプIDとレートタイプ名称のMapオブジェクト
	 * @throws ServiceException
	 */
	public Map<String, String> findRateIdAndNameMap() throws ServiceException {
		Map<String, String> map = new HashMap<String, String>();
		List<Rate> rateList = findAllRate();
		for (Rate rate : rateList) {
			map.put(String.valueOf(rate.rateId), rate.name);
		}
		return map;
	}

	/**
	 * レートタイプIDを受け取ってレート情報を返します.
	 * @param rateId レートタイプID
	 * @return レート情報
	 * @throws ServiceException
	 */
	@Override
	public RateJoin findById(String rateId) throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(RateService.Param.RATE_ID, rateId);

		try {
			return this.selectBySqlFile(RateJoin.class, "rate/FindNowById.sql",
					param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * レートタイプIDを受け取ってレート情報を返します.
	 * @param rateId レートタイプID
	 * @return レート情報
	 * @throws ServiceException
	 */
	public RateJoin findByIdWithoutDate(String rateId) throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(RateService.Param.RATE_ID, rateId);

		try {
			return this.selectBySqlFile(RateJoin.class, "rate/FindById.sql",
					param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果件数を取得します.
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
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
					"rate/CountRateByCondition.sql", param).getSingleResult()
					.intValue();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件と件数範囲を指定してレート情報を取得します.
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得件数
	 * @param offset 取得開始位置
	 * @return レート情報のリスト
	 * @throws ServiceException
	 */
	@Override
	public List<RateJoin> findByConditionLimit(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		if (conditions == null) {
			return new ArrayList<RateJoin>();
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

			return this.selectBySqlFile(RateJoin.class,
					"rate/FindRateByConditionLimit.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定してレート情報を取得します.<br>
	 * 未使用です.
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @return レート情報リスト
	 * @throws ServiceException
	 */
	@Override
	public List<RateJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		// 未使用メソッド
		return new ArrayList<RateJoin>();
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.NAME, null);
		param.put(Param.REMARKS, null);
		param.put(Param.SIGN, null);
		param.put(Param.RATE, null);
		param.put(Param.START_DATE, null);
		param.put(Param.TRN_REMARKS, null);
		param.put(Param.SORT_ORDER, null);
		return param;
	}

	/**
	 * 検索条件パラメータに検索条件を設定します.
	 *
	 * @param param 検索条件パラメータ
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 */
	private void setCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {

		// レートID
		if (conditions.containsKey(Param.RATE_ID)) {
			param.put(Param.RATE_ID, conditions.get(Param.RATE_ID));
		}

		// レートタイプ名称
		if (conditions.containsKey(Param.NAME)) {
			param.put(Param.NAME, super
					.createPartialSearchCondition((String) conditions
							.get(Param.NAME)));
		}

		// 備考
		if (conditions.containsKey(Param.REMARKS)) {
			param.put(Param.REMARKS, super
					.createPartialSearchCondition((String) conditions
							.get(Param.REMARKS)));
		}



		// 検索期間（開始）
		if (conditions.containsKey(Param.START_DATE_1)) {
			param.put(Param.START_DATE_1, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.START_DATE_1)));

		}


		// 検索期間（開始）全角半角変換
		if (conditions.containsKey(Param.START_DATE_1)) {
			param.put(Param.START_DATE_1, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.START_DATE_1)));

		}

		// 検索期間（終了）
		if (conditions.containsKey(Param.START_DATE_2)) {
			param.put(Param.START_DATE_2, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.START_DATE_2)));
		}

		// 検索期間（終了）全角半角変換
		if (conditions.containsKey(Param.START_DATE_2)) {
			param.put(Param.START_DATE_2, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.START_DATE_2)));

		}

		// ソートカラムを設定する
		if (Param.RATE_ID.equals(sortColumn)) {
			// レートタイプID
			param.put(Param.SORT_COLUMN_RATE, COLUMN_RATE_ID);
		} else if (Param.NAME.equals(sortColumn)) {
			// レートタイプ名称
			param.put(Param.SORT_COLUMN_RATE, COLUMN_NAME);
		} else if (Param.RATE.equals(sortColumn)) {
			// レート
			param.put(Param.SORT_COLUMN_RATE, COLUMN_RATE);
		} else if (Param.START_DATE.equals(sortColumn)) {
			// 適用開始日
			param.put(Param.SORT_COLUMN_RATE, COLUMN_START_DATE);
		} else if (Param.REMARKS.equals(sortColumn)) {
			// 備考
			param.put(Param.SORT_COLUMN_RATE, COLUMN_REMARKS);
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * レートIDを指定してレート情報を取得します.
	 * @param rateId レートID
	 * @return レートエンティティのリスト
	 * @throws Exception
	 */
	public List<RateTrn> findRateTrnsByRateId(String rateId) throws Exception {
		if (rateId == null) {
			return new ArrayList<RateTrn>();
		}
		Map<String, Object> param = super.createSqlParam();
		this.setEmptyCondition(param);
		param.put(Param.RATE_ID, rateId);

		return this.selectBySqlFile(RateTrn.class,
				"rate/FindRateTrnsByRateId.sql", param).getResultList();
	}

	/**
	 * レートIDを指定して現在適用されている
	 * レート情報を取得します.
	 *
	 * @param rateId レートID
	 * @return レートエンティティ
	 * @throws Exception
	 */
	public RateTrn findRateTrnNowByRateId(String rateId) throws Exception {
		if (rateId == null) {
			return new RateTrn();
		}
		Map<String, Object> param = super.createSqlParam();
		this.setEmptyCondition(param);
		param.put(Param.RATE_ID, rateId);

		return this.selectBySqlFile(RateTrn.class,
				"rate/FindRateTrnNowByRateId.sql", param).getSingleResult();
	}

	/**
	 * レート情報DTOを指定してレート情報を削除します.
	 * @param dto レート情報DTO
	 * @throws Exception
	 */
	@Override
	public void deleteRecord(RateDto dto) throws Exception {
		// 排他制御
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.RATE_ID, dto.rateId);
		this.lockRecordBySqlFile("rate/LockRateByRateId.sql", param,
				dto.updDatetm);

		// レートデータを処理
		param.put(Param.START_DATE, null);
		List<RateTrn> list = this.selectBySqlFile(RateTrn.class,
				"rate/FindRateTrnsByRateId.sql", param).getResultList();
		for (RateTrn rateTrn : list) {
			// 削除
			param = super.createSqlParam();
			param.put(Param.RATE_ID, rateTrn.rateId);
			param.put(Param.START_DATE, rateTrn.startDate);

			super.updateAudit(RateTrn.TABLE_NAME, new String[] { Param.RATE_ID,
					Param.START_DATE }, new Object[] { rateTrn.rateId,
					rateTrn.startDate });
			this.updateBySqlFile("rate/DeleteRateTrn.sql", param).execute();
		}

		// 削除
		param = super.createSqlParam();
		param.put(Param.RATE_ID, dto.rateId);
		this.updateBySqlFile("rate/DeleteRate.sql", param).execute();

	}

	/**
	 * レート情報を登録します.
	 * @param dto レート情報DTO
	 * @throws Exception
	 */
	@Override
	public void insertRecord(RateDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 登録
		Map<String, Object> param = super.createSqlParam();

		// 自動発番
		long newRateId = seqMakerService.nextval(Rate.TABLE_NAME);
		dto.rateId = String.valueOf(newRateId);

		BeanMap rateInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();

		param.putAll(rateInfo);

		this.updateBySqlFile("rate/InsertRate.sql", param).execute();

		// レートデータを登録する
		if (dto.rateTrnList != null) {
			for (RateTrnDto trn : dto.rateTrnList) {
				param = super.createSqlParam();
				trn.rateId = dto.rateId;

				BeanMap trnInfo = Beans.createAndCopy(BeanMap.class, trn)
						.timestampConverter(Constants.FORMAT.TIMESTAMP)
						.dateConverter(Constants.FORMAT.DATE, "startDate")
						.dateConverter(Constants.FORMAT.DATE).execute();
				param.putAll(trnInfo);
				this.updateBySqlFile("rate/InsertRateTrn.sql", param).execute();
			}
		}
	}

	/**
	 * レート情報を更新します.
	 * @param dto レート情報DTO
	 * @throws Exception
	 */
	@Override
	public void updateRecord(RateDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 排他制御
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.RATE_ID, dto.rateId);
		this.lockRecordBySqlFile("rate/LockRateByRateId.sql", param,
				dto.updDatetm);

		// 更新
		param = super.createSqlParam();

		BeanMap rateInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP)
				.dateConverter(Constants.FORMAT.DATE, "startDate")
				.dateConverter(Constants.FORMAT.DATE).execute();

		param.putAll(rateInfo);
		this.updateBySqlFile("rate/UpdateRate.sql", param).execute();

		// レートデータを更新する

		// 削除済みデータをまず削除する
		String[] values = dto.deletedRateId.split(",");
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);
		if (values != null) {
			for (String val : values) {
				if (val == null || val.length() == 0) {
					continue;
				}
				String[] keyValues = val.split("-");

				param = super.createSqlParam();

				RateTrn trn = new RateTrn();
				trn.rateId = Integer.valueOf(keyValues[0]);
				trn.startDate = super.convertUtilDateToSqlDate(sdf
						.parse(keyValues[1]));

				BeanMap trnInfo = Beans.createAndCopy(BeanMap.class, trn)
						.timestampConverter(Constants.FORMAT.TIMESTAMP)
						.dateConverter(Constants.FORMAT.DATE).execute();

				param.putAll(trnInfo);

				// 削除
				super.updateAudit(RateTrn.TABLE_NAME, new String[] {
						Param.RATE_ID, Param.START_DATE }, new Object[] {
						trn.rateId, trn.startDate });
				this.updateBySqlFile("rate/DeleteRateTrn.sql", param).execute();
			}
		}
		if (dto.rateTrnList != null) {
			for (RateTrnDto trn : dto.rateTrnList) {

				boolean isNew = false;
				if (!StringUtil.hasLength(trn.rateId)) {
					isNew = true;
					trn.rateId = dto.rateId;
				}
				// 更新
				param = super.createSqlParam();
				BeanMap trnInfo = Beans.createAndCopy(BeanMap.class, trn)
						.timestampConverter(Constants.FORMAT.TIMESTAMP)
						.dateConverter(Constants.FORMAT.DATE, "startDate")
						.dateConverter(Constants.FORMAT.DATE).execute();
				param.putAll(trnInfo);
				int sqlResult = 0;
				if (!isNew) {
					sqlResult = this.updateBySqlFile("rate/UpdateRateTrn.sql",
							param).execute();
				}
				if (sqlResult == 0) {
					// レコードがなかったから追加
					this.updateBySqlFile("rate/InsertRateTrn.sql", param)
							.execute();
				}
			}
		}
	}

	/**
	 * キーカラム名を返します.
	 * @return レートマスタのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "RATE_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return レートマスタのテーブル名
	 */
	@Override
	protected String getTableName() {
		return RateJoin.TABLE_NAME;
	}

}
