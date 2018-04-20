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
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.join.RackJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 棚番マスタサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class RackService extends AbstractMasterEditService<RackDto, RackJoin> implements
		MasterSearch<RackJoin> {

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String WAREHOUSE_CODE="warehouseCode";

		public static final String WAREHOUSE_NAME="warehouseName";

		public static final String WAREHOUSE_STATE="warehouseState";

		public static final String RACK_CODE = "rackCode";

		public static final String RACK_NAME = "rackName";

		public static final String RACK_CATEGORY = "rackCategory";

		public static final String EMPTY_RACK = "emptyRack";

		public static final String ZIP_CODE = "zipCode";

		public static final String ADDRESS_1 = "address1";

		public static final String ADDRESS_2 = "address2";

		public static final String RACK_PC_NAME = "rackPcName";

		public static final String RACK_TEL = "rackTel";

		public static final String RACK_FAX = "rackFax";

		public static final String RACK_EMAIL = "rackEmail";

		private static final String SORT_COLUMN_RACK = "sortColumnRack";

		private static final String CATEGORY_ID = "categoryId";

		private static final String SORT_ORDER = "sortOrder";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";

		private static final String PRODUCT_CODE = "productCode";

	}

	private static final String COLUMN_WAREHOUSE_CODE = "WAREHOUSE_CODE";

	private static final String COLUMN_WAREHOUSE_NAME = "WAREHOUSE_NAME";

	private static final String COLUMN_WAREHOUSE_STATE = "WAREHOUSE_STATE";

	private static final String COLUMN_RACK_CODE = "RACK_CODE";

	private static final String COLUMN_RACK_NAME = "RACK_NAME";

	private static final String COLUMN_RACK_CATEGORY = "RACK_CATEGORY";

	private static final String COLUMN_ZIP_CODE = "ZIP_CODE";

	private static final String COLUMN_ADDRESS_1 = "ADDRESS_1";

	private static final String COLUMN_ADDRESS_2 = "ADDRESS_2";

	private static final String COLUMN_RACK_PC_NAME = "RACK_PC_NAME";



	/**
	 * 棚番コードから棚番情報を取得します.
	 *
	 * @param rackCode 棚番コード
	 * @return 棚番情報
	 * @throws ServiceException
	 */
	@Override
	public RackJoin findById(String rackCode) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(RackService.Param.RACK_CODE, rackCode);

			return this.selectBySqlFile(RackJoin.class,
					"rack/FindRackByCode.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 倉庫コードから棚番情報を取得します.
	 *
	 * @param warehouseCode 倉庫コード
	 * @return 棚番情報
	 * @throws ServiceException
	 */
	public List<RackJoin> findByWarehouseId(String warehouseCode) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(RackService.Param.WAREHOUSE_CODE, warehouseCode);

			return this.selectBySqlFile(RackJoin.class,
					"rack/FindRackByWarehouseCode.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して棚番情報を取得します.
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 棚番情報のリスト
	 * @throws ServiceException
	 */
	@Override
	public List<RackJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		if (conditions == null) {
			return new ArrayList<RackJoin>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			setCondition(conditions, sortColumn, sortOrderAsc, param);

			return this.selectBySqlFile(RackJoin.class,
					"rack/FindRackByCondition.sql", param).getResultList();
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

			// 検索条件を設定する
			this.setCondition(conditions, null, false, param);

			return this.selectBySqlFile(Integer.class,
					"rack/CountRackByCondition.sql", param).getSingleResult()
					.intValue();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件と件数範囲を指定して棚番情報を取得します.
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得件数
	 * @param offset 取得開始位置
	 * @return 棚番情報のリスト
	 * @throws ServiceException
	 */
	@Override
	public List<RackJoin> findByConditionLimit(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		if (conditions == null) {
			return new ArrayList<RackJoin>();
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

			return this.selectBySqlFile(RackJoin.class,
					"rack/FindRackByConditionLimit.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
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
		// 棚コード
		if (conditions.containsKey(RackService.Param.RACK_CODE)) {
			param.put(RackService.Param.RACK_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(RackService.Param.RACK_CODE)));
		}

		// 棚名
		if (conditions.containsKey(RackService.Param.RACK_NAME)) {
			param.put(RackService.Param.RACK_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(RackService.Param.RACK_NAME)));
		}

		// 棚区分
		if (conditions.containsKey(RackService.Param.RACK_CATEGORY)) {
			param.put(RackService.Param.RACK_CATEGORY, conditions
					.get(RackService.Param.RACK_CATEGORY));
		}

		// 空き棚
		if (conditions.containsKey(RackService.Param.EMPTY_RACK)) {
			param.put(RackService.Param.EMPTY_RACK, conditions
					.get(RackService.Param.EMPTY_RACK));
		}

		// 倉庫コード
		if (conditions.containsKey(RackService.Param.WAREHOUSE_CODE)) {
			param.put(RackService.Param.WAREHOUSE_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(RackService.Param.WAREHOUSE_CODE)));
		}

		// 倉庫名
		if (conditions.containsKey(RackService.Param.WAREHOUSE_NAME)) {
			param.put(RackService.Param.WAREHOUSE_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(RackService.Param.WAREHOUSE_NAME)));
		}

		// 倉庫状態
		if (conditions.containsKey(RackService.Param.WAREHOUSE_STATE)) {
			param.put(RackService.Param.WAREHOUSE_STATE, conditions
					.get(RackService.Param.WAREHOUSE_STATE));
		}

		// 区分IDは固定で棚分類を指定する
		param.put(RackService.Param.CATEGORY_ID, Categories.RACK_CATEGORY);

		// ソートカラムを設定する
		if (RackService.Param.WAREHOUSE_CODE.equals(sortColumn)) {
			// 倉庫コード
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_WAREHOUSE_CODE);
		} else if (RackService.Param.WAREHOUSE_NAME.equals(sortColumn)) {
			// 倉庫名
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_WAREHOUSE_NAME);
		} else if (RackService.Param.WAREHOUSE_STATE.equals(sortColumn)) {
			// 倉庫状態
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_WAREHOUSE_STATE);
		} else if (RackService.Param.RACK_CODE.equals(sortColumn)) {
			// 棚コード
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_RACK_CODE);
		} else if (RackService.Param.RACK_NAME.equals(sortColumn)) {
			// 棚名
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_RACK_NAME);
		} else if (RackService.Param.RACK_CATEGORY.equals(sortColumn)) {
			// 棚分類
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_RACK_CATEGORY);
		} else if (RackService.Param.ZIP_CODE.equals(sortColumn)) {
			// 郵便番号
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_ZIP_CODE);
		} else if (RackService.Param.ADDRESS_1.equals(sortColumn)) {
			// 住所１
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_ADDRESS_1);
		} else if (RackService.Param.ADDRESS_2.equals(sortColumn)) {
			// 住所２
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_ADDRESS_2);
		} else if (RackService.Param.RACK_PC_NAME.equals(sortColumn)) {
			// 担当者
			param.put(RackService.Param.SORT_COLUMN_RACK,
					RackService.COLUMN_RACK_PC_NAME);
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(RackService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(RackService.Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(RackService.Param.RACK_CODE, null);
		param.put(RackService.Param.RACK_NAME, null);
		param.put(RackService.Param.RACK_CATEGORY, null);
		param.put(RackService.Param.SORT_COLUMN_RACK, null);
		param.put(RackService.Param.CATEGORY_ID, null);
		param.put(RackService.Param.SORT_ORDER, null);

		param.put(RackService.Param.WAREHOUSE_CODE, null);
		param.put(RackService.Param.WAREHOUSE_NAME, null);
		param.put(RackService.Param.WAREHOUSE_STATE, null);
		return param;
	}

	/**
	 * 棚番情報を登録します.
	 * @param dto 棚番情報DTO
	 * @throws Exception
	 */
	@Override
	public void insertRecord(RackDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 登録
		Map<String, Object> param = super.createSqlParam();

		BeanMap rackInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();

		param.putAll(rackInfo);
		this.updateBySqlFile("rack/InsertRack.sql", param).execute();
	}

	/**
	 * 棚番情報を更新します.
	 * @param dto 棚番情報
	 * @throws Exception
	 */
	@Override
	public void updateRecord(RackDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(Param.RACK_CODE, dto.rackCode);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("rack/LockRackByRackCode.sql", lockParam,
				dto.updDatetm);

		// 更新
		Map<String, Object> param = super.createSqlParam();
		BeanMap rackInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();

		param.putAll(rackInfo);
		this.updateBySqlFile("rack/UpdateRack.sql", param).execute();
	}


	/**
	 * 倉庫削除時に、棚情報を更新、削除します。
	 *
	 * @param dto 棚番情報
	 * @throws Exception
	 */
	public void controlRackWithWarehouse(WarehouseDto dto, boolean deleteFlag) throws Exception {
		if (dto == null) {
			return;
		}

		// パラメータ設定
		Map<String, Object> param = createSqlParam();
		param.put(Param.WAREHOUSE_CODE, dto.warehouseCode);

		if(deleteFlag) {
			// 削除
			this.updateBySqlFile("rack/DeleteRackByWarehouseCode.sql", param)
					.execute();
		} else {
			// 更新
			BeanMap rackInfo = Beans.createAndCopy(BeanMap.class, dto)
					.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
							Constants.FORMAT.DATE).execute();

			param.putAll(rackInfo);
			this.updateBySqlFile("rack/UpdateRackWhenDeleteWarehouse.sql", param).execute();
		}
	}

	/**
	 * 棚番情報DTOを指定して棚番情報を削除します.
	 * @param dto 棚番情報
	 * @throws Exception
	 */
	@Override
	public void deleteRecord(RackDto dto) throws Exception {
		try {
			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.RACK_CODE, dto.rackCode);
			this.lockRecordBySqlFile("rack/LockRackByRackCode.sql", param,
					dto.updDatetm);

			// 削除
			param = super.createSqlParam();
			param.put(Param.RACK_CODE, dto.rackCode);
			this.updateBySqlFile("rack/DeleteRackByRackCode.sql", param)
					.execute();
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	/**
	 * 棚番情報DTOのリストを指定して、棚番情報に紐づく商品コードの情報を追加します.
	 * @param dtoList 棚番情報DTOのリスト
	 * @throws ServiceException
	 */
	public void addProductInfoToRackDto(List<RackDto> dtoList)
			throws ServiceException {
		if (dtoList == null || dtoList.size() == 0) {
			return;
		}

		// 棚番コードの配列を作成する
		String[] rackCodes = new String[dtoList.size()];
		int i = 0;
		for (RackDto dto : dtoList) {
			rackCodes[i] = dto.rackCode;
			i++;
		}

		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.RACK_CODE, rackCodes);

			List<Product> productList = this.selectBySqlFile(Product.class,
					"rack/FindProductsByRackCode.sql", param).getResultList();
			for (RackDto dto : dtoList) {
				if (Constants.FLAG.ON.equals(dto.multiFlag)) {
					dto.duplicateList = new ArrayList<String>();
					dto.duplicateList.add("○");
				}

				for (Product p : productList) {

					// 重複許可棚の場合
					if (Constants.FLAG.ON.equals(dto.multiFlag)) {
						//商品コード・商品名を出さない
						break;
					}

					if (p.rackCode.equals(dto.rackCode)) {
						if (dto.productCodeList == null) {
							dto.duplicateList = new ArrayList<String>();
							dto.productCodeList = new ArrayList<String>();
							dto.productNameList = new ArrayList<String>();
						}
						dto.duplicateList.add("");
						dto.productCodeList.add(p.productCode);
						dto.productNameList.add(p.productName);
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 棚番コードを受け取って現在使用可能な棚番か否かを判定します.<br>
	 * (使用可能な棚番とは、商品に紐付きが無い棚番か、または重複可能な棚番のことです)
	 *
	 * @param rackCode 棚番コード
	 * @return 現在使用可能な棚番か否か
	 * @throws Exception
	 */
	public boolean checkPossibleRack(String productCode, String rackCode)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.RACK_CODE, rackCode);

			if (StringUtil.hasLength(productCode)) {
				param.put(Param.PRODUCT_CODE, productCode);
			}

			return this.selectBySqlFile(BeanMap.class,
					"rack/CheckPossibleRackByRackCode.sql", param)
					.getSingleResult() != null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 棚番コードを受け取って関連テーブルのデータ件数を返します.
	 *
	 * @param rackCode 棚番コード
	 * @return 関連テーブルのデータ件数
	 * @throws ServiceException
	 */
	public Map<String, Object> countRelations(String rackCode)
			throws ServiceException {
		try {
			// 関連データの存在チェック
			Map<String, Object> param = super.createSqlParam();
			param.put(RackService.Param.RACK_CODE, rackCode);
			BeanMap result = this.selectBySqlFile(BeanMap.class,
					"rack/CountRelations.sql", param).getSingleResult();

			HashMap<String, Object> temp = new HashMap<String, Object>();
			if (result == null) {
				return temp;
			}
			temp.putAll(result);
			return temp;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * キーカラム名を返します.
	 * @return 棚番マスタのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "RACK_CODE" };
	}

	/**
	 * テーブル名を返します.
	 * @return 棚番マスタのテーブル名
	 */
	@Override
	protected String getTableName() {
		return RackJoin.TABLE_NAME;
	}
}
