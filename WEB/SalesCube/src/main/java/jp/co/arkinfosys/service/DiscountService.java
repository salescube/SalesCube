/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.DiscountDto;
import jp.co.arkinfosys.dto.master.DiscountTrnDto;
import jp.co.arkinfosys.entity.join.DiscountJoin;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 数量割引マスタサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DiscountService extends
		AbstractMasterEditService<DiscountDto, DiscountJoin> implements
		MasterSearch<DiscountJoin> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String DISCOUNT_ID = "discountId";

		public static final String DISCOUNT_NAME = "discountName";

		public static final String USE_FLAG = "useFlag";

		public static final String USE_FLAG_NAME = "useFlagName";

		public static final String REMARKS = "remarks";

		private static final String SORT_COLUMN_DISCOUNT = "sortColumnDiscount";

		private static final String CATEGORY_ID = "categoryId";

		private static final String SORT_ORDER = "sortOrder";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";
	}

	private static final String COLUMN_DISCOUNT_ID = "DISCOUNT_ID";

	private static final String COLUMN_DISCOUNT_NAME = "DISCOUNT_NAME";

	private static final String COLUMN_USE_FLAG = "USE_FLAG";

	private static final String COLUMN_REMARKS = "REMARKS";

	private static final String COLUMN_USE_FLAG_NAME = "USE_FLAG_NAME";

	/**
	 *
	 * @param discountId 割引ID
	 * @return {@link DiscountJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findById(java.lang.String)
	 */
	@Override
	public DiscountJoin findById(String discountId) throws ServiceException {
		if (discountId == null) {
			return null;
		}

		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);
			this.setCondition(param, null, false, param);
			param.put(DiscountService.Param.DISCOUNT_ID, discountId);

			return this.selectBySqlFile(DiscountJoin.class,
					"discount/FindDiscountById.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
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
					"discount/CountDiscountByCondition.sql", param)
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
	 * @return {@link DiscountJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByConditionLimit(java.util.Map, java.lang.String, boolean, int, int)
	 */
	@Override
	public List<DiscountJoin> findByConditionLimit(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		if (conditions == null) {
			return new ArrayList<DiscountJoin>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			this.setCondition(conditions, sortColumn, sortOrderAsc, param);

			// LIMITを設定する
			if (rowCount > 0) {
				param.put(DiscountService.Param.ROW_COUNT, rowCount);
				param.put(DiscountService.Param.OFFSET, offset);
			}

			return this.selectBySqlFile(DiscountJoin.class,
					"discount/FindDiscountByConditionLimit.sql", param)
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
	 * @return {@link DiscountJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByCondition(java.util.Map, java.lang.String, boolean)
	 */
	@Override
	public List<DiscountJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		if (conditions == null) {
			return new ArrayList<DiscountJoin>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			this.setCondition(conditions, sortColumn, sortOrderAsc, param);

			return this.selectBySqlFile(DiscountJoin.class,
					"discount/FindDiscountByCondition.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を設定します.
	 * @param conditions 検索条件値マップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param param 検索条件マップ
	 */
	private void setCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {
		// 割引ID
		if (conditions.containsKey(DiscountService.Param.DISCOUNT_ID)) {
			param.put(DiscountService.Param.DISCOUNT_ID, super
					.createPrefixSearchCondition((String) conditions
							.get(DiscountService.Param.DISCOUNT_ID)));
		}

		// 割引名
		if (conditions.containsKey(DiscountService.Param.DISCOUNT_NAME)) {
			param.put(DiscountService.Param.DISCOUNT_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(DiscountService.Param.DISCOUNT_NAME)));
		}

		// 割引有効フラグ
		if (conditions.containsKey(DiscountService.Param.USE_FLAG)) {
			param.put(DiscountService.Param.USE_FLAG, conditions
					.get(DiscountService.Param.USE_FLAG));
		}

		// 備考
		if (conditions.containsKey(DiscountService.Param.REMARKS)) {
			param.put(DiscountService.Param.REMARKS, super
					.createPartialSearchCondition((String) conditions
							.get(DiscountService.Param.REMARKS)));
		}

		// 区分IDは固定で有効無効フラグを指定する
		param.put(DiscountService.Param.CATEGORY_ID, Categories.USE_FLAG);

		// ソートカラムを設定する
		if (DiscountService.Param.DISCOUNT_ID.equals(sortColumn)) {
			// 割引ID
			param.put(DiscountService.Param.SORT_COLUMN_DISCOUNT,
					DiscountService.COLUMN_DISCOUNT_ID);
		} else if (DiscountService.Param.DISCOUNT_NAME.equals(sortColumn)) {
			// 割引名
			param.put(DiscountService.Param.SORT_COLUMN_DISCOUNT,
					DiscountService.COLUMN_DISCOUNT_NAME);
		} else if (DiscountService.Param.USE_FLAG.equals(sortColumn)) {
			// 割引有効フラグ
			param.put(DiscountService.Param.SORT_COLUMN_DISCOUNT,
					DiscountService.COLUMN_USE_FLAG);
		} else if (DiscountService.Param.USE_FLAG_NAME.equals(sortColumn)) {
			// 割引有効（名称）
			param.put(DiscountService.Param.SORT_COLUMN_DISCOUNT,
					DiscountService.COLUMN_USE_FLAG_NAME);
		} else if (DiscountService.Param.REMARKS.equals(sortColumn)) {
			// 備考
			param.put(DiscountService.Param.SORT_COLUMN_DISCOUNT,
					DiscountService.COLUMN_REMARKS);
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(DiscountService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(DiscountService.Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件マップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(DiscountService.Param.DISCOUNT_ID, null);
		param.put(DiscountService.Param.DISCOUNT_NAME, null);
		param.put(DiscountService.Param.USE_FLAG, null);
		param.put(DiscountService.Param.REMARKS, null);
		param.put(DiscountService.Param.SORT_COLUMN_DISCOUNT, null);
		param.put(DiscountService.Param.CATEGORY_ID, null);
		param.put(DiscountService.Param.SORT_ORDER, null);
		return param;
	}

	/**
	 * エンティティからDTOへの変換を行います.
	 * @param discountJoinList 割引エンティティのリスト
	 * @return 割引DTOのリスト
	 * @throws ServiceException
	 */
	public List<DiscountDto> convertDiscountJoinToDto(
			List<DiscountJoin> discountJoinList) throws ServiceException {
		if (discountJoinList == null) {
			return new ArrayList<DiscountDto>();
		}
		try {
			Map<String, DiscountDto> tempMap = new HashMap<String, DiscountDto>();
			List<DiscountDto> resultList = new ArrayList<DiscountDto>();

			for (DiscountJoin discountJoin : discountJoinList) {
				DiscountDto discountDto = tempMap.get(discountJoin.discountId);
				if (discountDto == null) {
					discountDto = new DiscountDto();
					tempMap.put(discountJoin.discountId, discountDto);
					resultList.add(discountDto);
					Beans.copy(discountJoin, discountDto).timestampConverter(
							Constants.FORMAT.TIMESTAMP).dateConverter(
							Constants.FORMAT.DATE).execute();
				}

				// 割引データの追加
				if (discountDto.discountTrnList == null) {
					discountDto.discountTrnList = new ArrayList<DiscountTrnDto>();
				}
				DiscountTrnDto discountTrnDto = new DiscountTrnDto();
				discountDto.discountTrnList.add(discountTrnDto);

				// 数量範囲用の数値フォーマット
				Converter convRange = new NumberConverter(null, 0, true);
				// 掛率用の数値フォーマット
				Converter convRate = new NumberConverter(null, 2, true);

				Beans.copy(discountJoin, discountTrnDto).converter(convRange,
						"dataFrom", "dataTo").converter(convRate,
						"discountRate").includes("discountDataId",
						"discountId", "lineNo", "dataFrom", "dataTo",
						"discountRate").execute();
			}

			return resultList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 割引情報を削除します.
	 * @param dto 割引DTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#deleteRecord(java.lang.Object)
	 */
	@Override
	public void deleteRecord(DiscountDto dto) throws Exception {
		deleteDiscountByDiscountId(dto.discountId, dto.updDatetm);
	}

	/**
	 * 割引コードを指定して、割引情報を削除します.
	 * @param discountId 割引ID
	 * @param timestamp 更新日時の文字列
	 * @throws Exception
	 */
	public void deleteDiscountByDiscountId(String discountId, String timestamp)
			throws Exception {
		// 排他制御
		Map<String, Object> param = super.createSqlParam();
		param.put(DiscountService.Param.DISCOUNT_ID, discountId);
		this.lockRecordBySqlFile("discount/LockDiscountByDiscountId.sql",
				param, timestamp);

		// 削除
		param = super.createSqlParam();
		param.put(DiscountService.Param.DISCOUNT_ID, discountId);
		this.updateBySqlFile("discount/DeleteDiscountByDiscountId.sql", param)
				.execute();
	}

	/**
	 * 割引情報を登録します.
	 * @param discountDto 割引DTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#insertRecord(java.lang.Object)
	 */
	public void insertRecord(DiscountDto discountDto) throws Exception {
		if (discountDto == null) {
			return;
		}

		// 登録
		Map<String, Object> param = super.createSqlParam();

		BeanMap discountInfo = Beans.createAndCopy(BeanMap.class, discountDto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();

		param.putAll(discountInfo);
		this.updateBySqlFile("discount/InsertDiscount.sql", param).execute();
	}

	/**
	 * 割引情報を更新します.
	 * @param discountDto 割引DTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#updateRecord(java.lang.Object)
	 */
	public void updateRecord(DiscountDto discountDto) throws Exception {
		if (discountDto == null) {
			return;
		}

		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(Param.DISCOUNT_ID, discountDto.discountId);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("discount/LockDiscountByDiscountId.sql", lockParam,
				discountDto.updDatetm);

		// 更新
		Map<String, Object> param = super.createSqlParam();
		BeanMap discountInfo = Beans.createAndCopy(BeanMap.class, discountDto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();

		param.putAll(discountInfo);
		this.updateBySqlFile("discount/UpdateDiscount.sql", param).execute();
	}

	/**
	 *
	 * @return {DISCOUNT_ID}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getKeyColumnNames()
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "DISCOUNT_ID" };
	}

	/**
	 *
	 * @return {@link DiscountJoin#TABLE_NAME}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getTableName()
	 */
	@Override
	protected String getTableName() {
		return DiscountJoin.TABLE_NAME;
	}
}
