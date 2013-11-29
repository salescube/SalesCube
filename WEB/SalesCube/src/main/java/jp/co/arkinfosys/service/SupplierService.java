/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.SupplierDto;
import jp.co.arkinfosys.entity.Supplier;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 仕入先マスタサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class SupplierService extends AbstractMasterEditService<SupplierDto, SupplierJoin>
		implements MasterSearch<SupplierJoin> {

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		/** 仕入先コード **/
		public static final String SUPPLIER_CODE = "supplierCode";

		/** 仕入先名 **/
		public static final String SUPPLIER_NAME = "supplierName";

		/** 仕入先名カナ **/
		public static final String SUPPLIER_KANA = "supplierKana";

		/** 仕入先担当者名 **/
		public static final String SUPPLIER_PC_NAME = "supplierPcName";

		/** 仕入取引区分コード **/
		public static final String SUPPLIER_CM_CATEGORY = "supplierCmCategory";

		/** 仕入取引区分コード名 **/
		public static final String SUPPLIER_CM_CATEGORY_NAME = "supplierCmCategoryName";

		/** 備考 **/
		public static final String REMARKS = "remarks";

		/** 最終締処理日 **/
		public static final String LAST_CUTOFF_DATE = "lastCutoffDate";

		/** 仕入取引区分の区分ID **/
		private static final String CATEGORY_ID = "categoryId";
		/** 敬称の区分ID **/
		public static final String PRE_TYPE_CATEGORY_ID = "preTypeCategoryId";
		/** 単価端数処理方式区分ID **/
		public static final String PRICE_FRACT_CATEGORY_ID = "priceFractCategoryId";
		/** 税端数処理方式区分ID **/
		public static final String TAX_FRACT_CATEGORY_ID = "taxFractCategoryId";

		/** ソートカラム名 **/
		private static final String SORT_COLUMN = "sortColumn";

		/** ソートオーダー **/
		private static final String SORT_ORDER = "sortOrder";

		/** レート取得対象日（主に発注日） **/
		private static final String TARGET_DATE = "targetDate";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";

		/** 商品コード */
		private static final String PRODUCT_CODE = "productCode";

	}

	/**
	 * 仕入先コードのカラム名
	 */
	private static final String COLUMN_SUPPLIER_CODE = "SUPPLIER_CODE";

	/**
	 * 仕入先名のカラム名
	 */
	private static final String COLUMN_SUPPLIER_NAME = "SUPPLIER_NAME";

	/**
	 * 仕入先担当者名
	 */
	private static final String COLUMN_SUPPLIER_PC_NAME = "SUPPLIER_PC_NAME";

	/**
	 * 仕入先取引区分コード名のカラム名（区分データの別名）
	 */
	private static final String SUPPLIER_CM_CATEGORY_NAME = "SUPPLIER_CM_CATEGORY_NAME";

	/**
	 * 仕入取引区分コードのカラム名
	 */
	private static final String COLUMN_SUPPLIER_CM_CATEGORY = "supplierCmCategory";

	/**
	 * 仕入先コードから仕入先情報を取得します.
	 *
	 * @param SupplierCode 仕入先コード
	 * @return 仕入先情報
	 * @throws ServiceException
	 */
	@Override
	public SupplierJoin findById(String SupplierCode) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(SupplierService.Param.SUPPLIER_CODE, SupplierCode);
			param.put(SupplierService.Param.PRE_TYPE_CATEGORY_ID,
					Categories.PRE_TYPE);
			param.put(SupplierService.Param.PRICE_FRACT_CATEGORY_ID,
					Categories.PRICE_FRACT_CATEGORY);
			param.put(SupplierService.Param.TAX_FRACT_CATEGORY_ID,
					Categories.TAX_FRACT_CATEGORY);
			return this.selectBySqlFile(SupplierJoin.class,
					"supplier/FindSupplierByCode.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 通貨単位を取得します.<br>
	 * (仕入先通貨単位対応のため)
	 * @return 通貨単位文字列のリスト
	 * @throws ServiceException
	 */
	public List<String> getCUnitSignList() throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			return this.selectBySqlFile(String.class,
					"supplier/FindCUnitSigns.sql", param).getResultList();
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
					"supplier/CountSupplierByCondition.sql", param)
					.getSingleResult().intValue();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件と件数範囲を指定して仕入先情報を取得します.
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得件数
	 * @param offset 取得開始位置
	 * @return 仕入先情報のリスト
	 * @throws ServiceException
	 */
	@Override
	public List<SupplierJoin> findByConditionLimit(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		try {

			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			setCondition(conditions, sortColumn, sortOrderAsc, param);

			// LIMITを設定する
			if (rowCount > 0) {
				param.put(Param.ROW_COUNT, rowCount);
				param.put(Param.OFFSET, offset);
			}

			return this.selectBySqlFile(SupplierJoin.class,
					"supplier/FindSupplierByConditionLimit.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して仕入先情報を取得します.
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 仕入先情報リスト
	 * @throws ServiceException
	 */
	@Override
	public List<SupplierJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		try {

			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			setCondition(conditions, sortColumn, sortOrderAsc, param);

			return this.selectBySqlFile(SupplierJoin.class,
					"supplier/FindSupplierByCondition.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 *
	 * @param param 検索条件パラメータ
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	private void setCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {
		// 仕入先コード
		if (conditions.containsKey(SupplierService.Param.SUPPLIER_CODE)) {
			param.put(SupplierService.Param.SUPPLIER_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(SupplierService.Param.SUPPLIER_CODE)));
		}

		// 仕入先名
		if (conditions.containsKey(SupplierService.Param.SUPPLIER_NAME)) {
			param.put(SupplierService.Param.SUPPLIER_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(SupplierService.Param.SUPPLIER_NAME)));
		}

		// 仕入先名カナ
		if (conditions.containsKey(SupplierService.Param.SUPPLIER_KANA)) {
			param.put(SupplierService.Param.SUPPLIER_KANA, super
					.createPartialSearchCondition((String) conditions
							.get(SupplierService.Param.SUPPLIER_KANA)));
		}

		// 備考
		if (conditions.containsKey(SupplierService.Param.REMARKS)) {
			param.put(SupplierService.Param.REMARKS, super
					.createPartialSearchCondition((String) conditions
							.get(SupplierService.Param.REMARKS)));
		}

		// 区分IDは固定で仕入取引区分を指定する
		param.put(SupplierService.Param.CATEGORY_ID,
				Categories.SUPPLIER_CM_CATEGORY);

		// ソートカラムを設定する
		if (SupplierService.Param.SUPPLIER_CODE.equals(sortColumn)) {
			// 仕入先コード
			param.put(SupplierService.Param.SORT_COLUMN,
					SupplierService.COLUMN_SUPPLIER_CODE);
		} else if (SupplierService.Param.SUPPLIER_NAME.equals(sortColumn)) {
			// 仕入先名
			param.put(SupplierService.Param.SORT_COLUMN,
					SupplierService.COLUMN_SUPPLIER_NAME);
		} else if (SupplierService.Param.SUPPLIER_CM_CATEGORY
				.equals(sortColumn)) {
			// 仕入先担当者名
			param.put(SupplierService.Param.SORT_COLUMN,
					SupplierService.COLUMN_SUPPLIER_CM_CATEGORY);
		} else if (SupplierService.Param.SUPPLIER_PC_NAME.equals(sortColumn)) {
			// 仕入取引区分
			param.put(SupplierService.Param.SORT_COLUMN,
					SupplierService.COLUMN_SUPPLIER_PC_NAME);
		} else if (SupplierService.Param.SUPPLIER_CM_CATEGORY_NAME
				.equals(sortColumn)) {
			// 仕入先取引区分名
			param.put(SupplierService.Param.SORT_COLUMN,
					SupplierService.SUPPLIER_CM_CATEGORY_NAME);
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(SupplierService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(SupplierService.Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(SupplierService.Param.SUPPLIER_CODE, null);
		param.put(SupplierService.Param.SUPPLIER_NAME, null);
		param.put(SupplierService.Param.SUPPLIER_KANA, null);
		param.put(SupplierService.Param.SUPPLIER_CM_CATEGORY, null);
		param.put(SupplierService.Param.REMARKS, null);
		param.put(SupplierService.Param.SORT_COLUMN, null);
		param.put(SupplierService.Param.SORT_ORDER, null);
		return param;
	}

	/**
	 * 仕入先コードと対象日から仕入先最新レート情報を取得します.
	 * @param supplierCode 仕入先コード
	 * @param targeDate 対象日
	 * @return 仕入先最新レート情報
	 * @throws ServiceException
	 */
	public SupplierJoin findSupplierRateByCodeDate(String supplierCode,
			Date targeDate) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(SupplierService.Param.SUPPLIER_CODE, supplierCode);
			param.put(SupplierService.Param.TARGET_DATE, targeDate);

			return this.selectBySqlFile(SupplierJoin.class,
					"supplier/FindSupplierRateByCodeDate.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 全ての仕入先を取得します.
	 * @return 仕入先エンティティのリスト
	 * @throws ServiceException
	 */
	public List<Supplier> findAllSupplier() throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			return this.selectBySqlFile(Supplier.class,
					"supplier/FindAllSuppliere.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 仕入先情報DTOを指定して仕入先情報を削除します.
	 * @param dto 仕入先情報DTO
	 * @throws Exception
	 */
	@Override
	public void deleteRecord(SupplierDto dto) throws Exception {
		deleteSupplierBySupplierCode(dto.supplierCode, dto.updDatetm);
	}

	/**
	 * 仕入先コードを指定して仕入先情報を削除します.
	 * @param supplierCode 仕入先コード
	 * @param timestamp 更新日時
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public void deleteSupplierBySupplierCode(String supplierCode,
			String timestamp) throws ServiceException, UnabledLockException {

		try {
			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.SUPPLIER_CODE, supplierCode);
			this.lockRecordBySqlFile("supplier/LockSupplierBySupplierCode.sql",
					param, timestamp);

			// 削除
			param = super.createSqlParam();
			param.put(Param.SUPPLIER_CODE, supplierCode);
			this.updateBySqlFile("supplier/DeleteSupplierBySupplierCode.sql",
					param).execute();
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 仕入先情報を登録します.
	 *
	 * @param supplierDto 仕入先メンテナンスの画面入力情報
	 * @throws ServiceException
	 */
	public void insertRecord(SupplierDto supplierDto) throws ServiceException {
		if (supplierDto == null) {
			return;
		}
		try {
			// 顧客情報の登録
			Map<String, Object> param = super.createSqlParam();

			BeanMap supplierInfo = Beans.createAndCopy(BeanMap.class,
					supplierDto).excludes(AbstractService.Param.CRE_FUNC,
					AbstractService.Param.CRE_DATETM,
					AbstractService.Param.CRE_USER,
					AbstractService.Param.UPD_FUNC,
					AbstractService.Param.UPD_DATETM,
					AbstractService.Param.UPD_USER).timestampConverter(
					Constants.FORMAT.DATE,
					SupplierService.Param.LAST_CUTOFF_DATE).execute();

			param.putAll(supplierInfo);
			this.updateBySqlFile("supplier/InsertSupplier.sql", param)
					.execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 仕入先情報を更新します.
	 *
	 * @param supplierDto 仕入先メンテナンスの画面入力情報
	 * @throws ServiceException
	 */
	public void updateRecord(SupplierDto supplierDto) throws Exception {
		if (supplierDto == null) {
			return;
		}

		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(Param.SUPPLIER_CODE, supplierDto.supplierCode);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("supplier/LockSupplierBySupplierCode.sql",
				lockParam, supplierDto.updDatetm);

		// 顧客情報の更新
		Map<String, Object> param = super.createSqlParam();
		BeanMap supplierInfo = Beans.createAndCopy(BeanMap.class, supplierDto)
				.excludes(AbstractService.Param.CRE_FUNC,
						AbstractService.Param.CRE_DATETM,
						AbstractService.Param.CRE_USER,
						AbstractService.Param.UPD_FUNC,
						AbstractService.Param.UPD_DATETM,
						AbstractService.Param.UPD_USER).timestampConverter(
						Constants.FORMAT.DATE,
						SupplierService.Param.LAST_CUTOFF_DATE).execute();

		param.putAll(supplierInfo);
		this.updateBySqlFile("supplier/UpdateSupplier.sql", param).execute();
	}

	/**
	 * 最終締日更新処理を行います.
	 *
	 * @param supplierCode 仕入先コード
	 * @param cutoffDate 締年月日
	 */
	public void updateLastCutoffDate(String supplierCode, Date cutoffDate) {
		// 最終締処理日(LAST_CUTOFF_DATE)のみ更新するため、super.createSqlParam()を使用しない
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(DomainService.Param.DOMAIN_ID, this.domainDto.domainId);
		param.put(Param.SUPPLIER_CODE, supplierCode);
		param.put(Param.LAST_CUTOFF_DATE, cutoffDate);

		this.updateBySqlFile("supplier/UpdateLastCutoffDateBySupplierCode.sql",
				param).execute();
	}

	/**
	 * キーカラム名を返します.
	 * @return 仕入先情報テーブルのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "SUPPLIER_CODE" };
	}

	/**
	 * テーブル名を返します.
	 * @return 仕入先情報テーブル名
	 */
	@Override
	protected String getTableName() {
		return SupplierJoin.TABLE_NAME;
	}

	/**
	 * 商品コードを指定して仕入先レート情報を取得します.
	 * @param productCode 商品コード
	 * @return 仕入先レート情報
	 * @throws ServiceException
	 */
	public SupplierJoin findSupplierRateByProductCode(String productCode) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(SupplierService.Param.PRODUCT_CODE, productCode);

			return this.selectBySqlFile(SupplierJoin.class,
					"supplier/FindSupplierRateByProductCode.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 仕入先コードを指定して仕入先レート情報を取得します.
	 * @param supplierCode 仕入先コード
	 * @return 仕入先レート情報
	 * @throws ServiceException
	 */
	public SupplierJoin findSupplierRateBySupplierCode(String supplierCode) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(SupplierService.Param.SUPPLIER_CODE, supplierCode);

			return this.selectBySqlFile(SupplierJoin.class,
					"supplier/FindSupplierRateBySupplierCode.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
