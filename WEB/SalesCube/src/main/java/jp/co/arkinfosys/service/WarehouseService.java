/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.entity.join.WarehouseJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 倉庫マスタサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class WarehouseService extends AbstractMasterEditService<WarehouseDto, WarehouseJoin> implements
		MasterSearch<WarehouseJoin> {

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String WAREHOUSE_CODE="warehouseCode";

		public static final String WAREHOUSE_NAME="warehouseName";
		
		public static final String WAREHOUSE_ZIP_CODE="warehouseZipCode";
		
		public static final String WAREHOUSE_ADDRESS1="warehouseAddress1";
		
		public static final String WAREHOUSE_ADDRESS2="warehouseAddress2";
		
		public static final String WAREHOUSE_TEL="warehouseTel";
		
		public static final String WAREHOUSE_FAX="warehouseFax";
		
		public static final String MANAGER_NAME="managerName";
		
		public static final String MANAGER_KANA="managerKana";
		
		public static final String MANAGER_TEL="managerTel";
		
		public static final String MANAGER_FAX="managerFax";
		
		public static final String MANAGER_EMAIL="managerEmail";
		
		public static final String WAREHOUSE_STATE="warehouseState";

		private static final String SORT_COLUMN_WAREHOUSE = "sortColumnWarehouse";

		private static final String SORT_ORDER = "sortOrder";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";
	}

	private static final String COLUMN_WAREHOUSE_CODE = "WAREHOUSE_CODE";

	private static final String COLUMN_WAREHOUSE_NAME = "WAREHOUSE_NAME";

	private static final String COLUMN_WAREHOUSE_STATE = "WAREHOUSE_STATE";

	private static final String COLUMN_WAREHOUSE_ZIP_CODE = "WAREHOUSE_ZIP_CODE";

	private static final String COLUMN_WAREHOUSE_ADDRESS1 = "WAREHOUSE_ADDRESS1";

	private static final String COLUMN_WAREHOUSE_ADDRESS2 = "WAREHOUSE_ADDRESS2";

	private static final String COLUMN_MANAGER_NAME = "MANAGER_NAME";
	

	/**
	 * 検索条件を指定して結果件数を取得します.<br>
	 * 未使用です.
	 * @param conditions 検索条件
	 * @return 0(固定値)
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

			// 検索条件を設定する
			this.setCondition(conditions, null, false, param);

			return this.selectBySqlFile(Integer.class,
					"warehouse/CountWarehouseByCondition.sql", param).getSingleResult()
					.intValue();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して住所情報を取得します.<br>
	 * 未使用です.
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 表示数
	 * @param offset 表示ページ数
	 * @return 郵便番号エンティティのリスト
	 * @throws ServiceException
	 */
	@Override
	public List<WarehouseJoin> findByConditionLimit(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		if (conditions == null) {
			return new ArrayList<WarehouseJoin>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			setCondition(conditions, sortColumn, sortOrderAsc, param);

			// LIMITを設定する
			if (rowCount > 0) {
				param.put(Param.ROW_COUNT, rowCount);
				param.put(Param.OFFSET, offset);
			}

			return this.selectBySqlFile(WarehouseJoin.class,
					"warehouse/FindWarehouseByConditionLimit.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	/**
	 * 検索条件を指定して倉庫情報を取得します.<br>
	 * 未使用です.
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 表示数
	 * @param offset 表示ページ数
	 * @return 倉庫エンティティのリスト
	 * @throws ServiceException
	 */
	@Override
	public List<WarehouseJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		if (conditions == null) {
			return new ArrayList<WarehouseJoin>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			setCondition(conditions, sortColumn, sortOrderAsc, param);

			return this.selectBySqlFile(WarehouseJoin.class,
					"warehouse/FindWarehouseByCondition.sql", param).getResultList();
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
		param.put(WarehouseService.Param.WAREHOUSE_CODE, null);
		param.put(WarehouseService.Param.WAREHOUSE_NAME, null);
		param.put(WarehouseService.Param.WAREHOUSE_STATE, null);
		param.put(WarehouseService.Param.SORT_COLUMN_WAREHOUSE, null);
		param.put(WarehouseService.Param.SORT_ORDER, null);
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
		// 倉庫コード
		if (conditions.containsKey(WarehouseService.Param.WAREHOUSE_CODE)) {
			param.put(WarehouseService.Param.WAREHOUSE_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(WarehouseService.Param.WAREHOUSE_CODE)));
		}

		// 倉庫名
		if (conditions.containsKey(WarehouseService.Param.WAREHOUSE_NAME)) {
			param.put(WarehouseService.Param.WAREHOUSE_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(WarehouseService.Param.WAREHOUSE_NAME)));
		}

		// 倉庫状態
		if (conditions.containsKey(WarehouseService.Param.WAREHOUSE_STATE)) {
			param.put(WarehouseService.Param.WAREHOUSE_STATE, conditions
					.get(WarehouseService.Param.WAREHOUSE_STATE));
		}

		// ソートカラムを設定する
		if (WarehouseService.Param.WAREHOUSE_CODE.equals(sortColumn)) {
			// 倉庫コード
			param.put(WarehouseService.Param.SORT_COLUMN_WAREHOUSE,
					WarehouseService.COLUMN_WAREHOUSE_CODE);
		} else if (WarehouseService.Param.WAREHOUSE_NAME.equals(sortColumn)) {
			// 倉庫名
			param.put(WarehouseService.Param.SORT_COLUMN_WAREHOUSE,
					WarehouseService.COLUMN_WAREHOUSE_NAME);
		} else if (WarehouseService.Param.WAREHOUSE_STATE.equals(sortColumn)) {
			// 倉庫状態
			param.put(WarehouseService.Param.SORT_COLUMN_WAREHOUSE,
					WarehouseService.COLUMN_WAREHOUSE_STATE);
		} else if (WarehouseService.Param.WAREHOUSE_ZIP_CODE.equals(sortColumn)) {
			// 郵便番号
			param.put(WarehouseService.Param.SORT_COLUMN_WAREHOUSE,
					WarehouseService.COLUMN_WAREHOUSE_ZIP_CODE);
		} else if (WarehouseService.Param.WAREHOUSE_ADDRESS1.equals(sortColumn)) {
			// 住所１
			param.put(WarehouseService.Param.SORT_COLUMN_WAREHOUSE,
					WarehouseService.COLUMN_WAREHOUSE_ADDRESS1);
		} else if (WarehouseService.Param.WAREHOUSE_ADDRESS2.equals(sortColumn)) {
			// 住所２
			param.put(WarehouseService.Param.SORT_COLUMN_WAREHOUSE,
					WarehouseService.COLUMN_WAREHOUSE_ADDRESS2);
		} else if (WarehouseService.Param.MANAGER_NAME.equals(sortColumn)) {
			// 担当者
			param.put(WarehouseService.Param.SORT_COLUMN_WAREHOUSE,
					WarehouseService.COLUMN_MANAGER_NAME);
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(WarehouseService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(WarehouseService.Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}
	
	/**
	 * 倉庫CODEを指定して、倉庫マスタ情報を取得します.<br>
	 * @param warehouseCode 倉庫CODE
	 * @return 倉庫エンティティ
	 * @throws ServiceException
	 */
	@Override
	public WarehouseJoin findById(String warehouseCode) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(WarehouseService.Param.WAREHOUSE_CODE, warehouseCode);

			return this.selectBySqlFile(WarehouseJoin.class,
					"warehouse/FindWarehouseByCode.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void insertRecord(WarehouseDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 登録
		Map<String, Object> param = super.createSqlParam();

		BeanMap warehouseInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE)
				.excludes(AbstractService.Param.CRE_FUNC,
						AbstractService.Param.CRE_DATETM,
						AbstractService.Param.CRE_USER,
						AbstractService.Param.UPD_FUNC,
						AbstractService.Param.UPD_DATETM,
						AbstractService.Param.UPD_USER).execute();
		param.putAll(warehouseInfo);
		this.updateBySqlFile("warehouse/InsertWarehouse.sql", param).execute();
	}

	@Override
	public void updateRecord(WarehouseDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(Param.WAREHOUSE_CODE, dto.warehouseCode);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("warehouse/LockWarehouseByWarehouseCode.sql", lockParam,
				dto.updDatetm);

		// 更新
		Map<String, Object> param = super.createSqlParam();
		BeanMap warehouseInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE)
				.excludes(AbstractService.Param.CRE_FUNC,
						AbstractService.Param.CRE_DATETM,
						AbstractService.Param.CRE_USER,
						AbstractService.Param.UPD_FUNC,
						AbstractService.Param.UPD_DATETM,
						AbstractService.Param.UPD_USER).execute();

		param.putAll(warehouseInfo);
		this.updateBySqlFile("warehouse/UpdateWarehouse.sql", param).execute();
	}

	@Override
	public void deleteRecord(WarehouseDto dto) throws Exception {
		try {
			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.WAREHOUSE_CODE, dto.warehouseCode);
			this.lockRecordBySqlFile("warehouse/LockWarehouseByWarehouseCode.sql", param,
					dto.updDatetm);

			// 削除
			param = super.createSqlParam();
			param.put(Param.WAREHOUSE_CODE, dto.warehouseCode);
			this.updateBySqlFile("warehouse/DeleteWarehouseByWarehouseCode.sql", param)
					.execute();
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}		
	}

	@Override
	protected String getTableName() {
		return WarehouseJoin.TABLE_NAME;
	}

	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "WAREHOUSE_CODE" };
	}


}
