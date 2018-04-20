/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.ProductSetDto;
import jp.co.arkinfosys.entity.ProductSet;
import jp.co.arkinfosys.entity.join.ProductSetJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * セット商品サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ProductSetService extends AbstractMasterEditService<ProductSetDto, ProductSetJoin>
		implements MasterSearch<ProductSetJoin> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		/**
		 * セット商品コード （親の商品コード）
		 */
		public static final String SET_PRODUCT_CODE = "setProductCode";

		/**
		 * セット商品名 （親の商品名）
		 */
		public static final String SET_PRODUCT_NAME = "setProductName";

		/**
		 * 商品コード （子の商品コード）
		 */
		public static final String PRODUCT_CODE = "productCode";

		/**
		 * 商品名 （子の商品名）
		 */
		public static final String PRODUCT_NAME = "productName";

		/**
		 * 数量
		 */
		public static final String QUANTITY = "quantity";

		private static final String SET_TYPE_CATEGORY = "setTypeCategory";

		private static final String SORT_COLUMN = "sortColumn";

		private static final String SORT_ORDER = "sortOrder";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";

		private static final String SET_TYPE_CATEGORY_SINGLE = "setTypeCategorySingle";

		private static final String SET_TYPE_CATEGORY_SET = "setTypeCategorySet";

		/**
		 * 検索結果に内訳商品情報を含めるか否か(取得件数が変わります)
		 */
		private static final String RESULT_CONTAIN_CHILDREN = "resultContainChildren";
	}

	/**
	 * 親商品コードを指定して、セット商品情報のリストを返します.
	 * @param setProductCode 親商品コード
	 * @return セット商品情報{@link ProductSetJoin}のリスト
	 * @throws ServiceException
	 */
	public List<ProductSetJoin> findProductSetByProductCode(
			String setProductCode) throws ServiceException {
		try {
			if (!StringUtil.hasLength(setProductCode)) {
				return new ArrayList<ProductSetJoin>();
			}

			Map<String, Object> param = super.createSqlParam();
			param = this.setEmptyCondition(param);

			// 親商品コード
			param.put(ProductSetService.Param.SET_PRODUCT_CODE, setProductCode);

			return this.selectBySqlFile(ProductSetJoin.class,
					"productset/FindProductSetByProductCode.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 子商品コードを指定して、セット商品情報を返します.
	 * @param productCode 子商品コード
	 * @return セット商品情報{@link ProductSetJoin}のリスト
	 * @throws ServiceException
	 */
	public List<ProductSetJoin> findProductSetByChildProductCode(
			String productCode) throws ServiceException {
		try {
			if (!StringUtil.hasLength(productCode)) {
				return new ArrayList<ProductSetJoin>();
			}

			Map<String, Object> param = super.createSqlParam();
			param = this.setEmptyCondition(param);

			// 子商品コード
			param.put(ProductSetService.Param.PRODUCT_CODE, productCode);

			return this.selectBySqlFile(ProductSetJoin.class,
					"productset/FindProductSetByChildProductCode.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param id ID
	 * @return {@link ProductSetJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findById(java.lang.String)
	 */
	@Override
	public ProductSetJoin findById(String id) throws ServiceException {
		// 未使用メソッド
		return null;
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

			// 検索条件を設定する
			this.setCondition(conditions, null, false, param);

			return this.selectBySqlFile(Integer.class,
					"productset/CountProductSetByCondition.sql", param)
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
	 * @return {@link ProductSetJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByConditionLimit(java.util.Map, java.lang.String, boolean, int, int)
	 */
	@Override
	public List<ProductSetJoin> findByConditionLimit(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		try {
			// 条件作成
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			// 検索条件を設定
			this.setCondition(conditions, sortColumn, sortOrderAsc, param);

			// LIMITを設定する
			if (rowCount > 0) {
				param.put(Param.ROW_COUNT, rowCount);
				param.put(Param.OFFSET, offset);
			}

			// 検索を行う
			return this.selectBySqlFile(ProductSetJoin.class,
					"productset/FindProductSetByConditionLimit.sql", param)
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
	 * @return {@link ProductSetJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByCondition(java.util.Map, java.lang.String, boolean)
	 */
	@Override
	public List<ProductSetJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		// 未使用メソッド
		return null;
	}

	/**
	 * 検索条件を設定します.
	 * @param conditions 検索条件値のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param param 検索条件のマップ
	 */
	private void setCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {
		if (conditions == null) {
			return;
		}

		// セット商品コード
		if (conditions.containsKey(ProductSetService.Param.SET_PRODUCT_CODE)) {
			param.put(ProductSetService.Param.SET_PRODUCT_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ProductSetService.Param.SET_PRODUCT_CODE)));
		}

		// 商品コード
		if (conditions.containsKey(ProductSetService.Param.PRODUCT_CODE)) {
			param.put(ProductSetService.Param.PRODUCT_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ProductSetService.Param.PRODUCT_CODE)));
		}

		// セット商品名
		if (conditions.containsKey(ProductSetService.Param.SET_PRODUCT_NAME)) {
			param.put(ProductSetService.Param.SET_PRODUCT_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(ProductSetService.Param.SET_PRODUCT_NAME)));
		}

		// 商品名
		if (conditions.containsKey(ProductSetService.Param.PRODUCT_NAME)) {
			param.put(ProductSetService.Param.PRODUCT_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(ProductSetService.Param.PRODUCT_NAME)));
		}

		// ソートカラムを設定する
		if (StringUtil.hasLength(sortColumn)) {
			param.put(ProductSetService.Param.SORT_COLUMN, StringUtil
					.convertColumnName(sortColumn));

			// ソートオーダーを設定する
			if (sortOrderAsc) {
				param
						.put(ProductSetService.Param.SORT_ORDER,
								Constants.SQL.ASC);
			} else {
				param.put(ProductSetService.Param.SORT_ORDER,
						Constants.SQL.DESC);
			}
		}
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件のマップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(ProductSetService.Param.SET_PRODUCT_CODE, null);
		param.put(ProductSetService.Param.SET_PRODUCT_NAME, null);
		param.put(ProductSetService.Param.PRODUCT_CODE, null);
		param.put(ProductSetService.Param.PRODUCT_NAME, null);
		param.put(ProductSetService.Param.QUANTITY, null);
		param.put(ProductSetService.Param.SET_TYPE_CATEGORY, null);
		param.put(ProductSetService.Param.SORT_COLUMN, null);
		param.put(ProductSetService.Param.SORT_ORDER, null);
		param.put(ProductSetService.Param.ROW_COUNT, null);
		param.put(ProductSetService.Param.OFFSET, null);
		param.put(ProductSetService.Param.SET_TYPE_CATEGORY_SINGLE,
				CategoryTrns.PRODUCT_SET_TYPE_SINGLE);
		param.put(ProductSetService.Param.SET_TYPE_CATEGORY_SET,
				CategoryTrns.PRODUCT_SET_TYPE_SET);
		param.put(ProductSetService.Param.RESULT_CONTAIN_CHILDREN, null);
		return param;
	}

	/**
	 *
	 * @param dto {@link ProductSetDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#insertRecord(java.lang.Object)
	 */
	@Override
	public void insertRecord(ProductSetDto dto) throws Exception {
		Map<String, Object> param = super.createSqlParam();

		BeanMap map = Beans.createAndCopy(BeanMap.class, dto).includes(
				Param.SET_PRODUCT_CODE, Param.PRODUCT_CODE, Param.QUANTITY)
				.execute();
		// 数量の全角半角変換
		if (map.containsKey(Param.QUANTITY)) {
			map.put(Param.QUANTITY, StringUtil.zenkakuNumToHankaku((String)map.get(Param.QUANTITY)));
		}
		param.putAll(map);

		// 1行インサートする
		super.updateBySqlFile("productset/InsertProductSet.sql", param)
				.execute();
	}

	/**
	 *
	 * @param dto {@link ProductSetDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#deleteRecord(java.lang.Object)
	 */
	@Override
	public void deleteRecord(ProductSetDto dto) throws Exception {
		// 排他制御
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.SET_PRODUCT_CODE, dto.setProductCode);
		param.put(Param.PRODUCT_CODE, dto.productCode);
		super.lockRecordBySqlFile(
				"productset/LockProductSetByProductCode.sql",
				param, dto.updDatetm);

		BeanMap map = Beans.createAndCopy(BeanMap.class, dto).includes(
				Param.SET_PRODUCT_CODE, Param.PRODUCT_CODE).execute();
		param.putAll(map);

		super.updateAudit(ProductSet.TABLE_NAME, new String[] {
				Param.SET_PRODUCT_CODE, Param.PRODUCT_CODE }, new Object[] {
				dto.setProductCode, dto.productCode });

		super.updateBySqlFile(
				"productset/DeleteProductSetByCode.sql", param).execute();
	}

	/**
	 *
	 * @param dto {@link ProductSetDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#updateRecord(java.lang.Object)
	 */
	@Override
	public void updateRecord(ProductSetDto dto) throws Exception {
		try {
			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.SET_PRODUCT_CODE, dto.setProductCode);
			param.put(Param.PRODUCT_CODE, dto.productCode);
			super.lockRecordBySqlFile(
					"productset/LockProductSetByProductCode.sql",
					param, dto.updDatetm);

			param = super.createSqlParam();
			BeanMap map = Beans.createAndCopy(BeanMap.class, dto).includes(
					Param.SET_PRODUCT_CODE, Param.PRODUCT_CODE,
					Param.QUANTITY).execute();
			// 数量の全角半角変換
			if (map.containsKey(Param.QUANTITY)) {
				map.put(Param.QUANTITY, StringUtil.zenkakuNumToHankaku((String)map.get(Param.QUANTITY)));
			}
			param.putAll(map);

			// 1行更新する
			super.updateBySqlFile(
					"productset/UpdateProductSet.sql", param).execute();
		} catch (UnabledLockException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @return {{@link Param#SET_PRODUCT_CODE}, {@link Param#PRODUCT_CODE}}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getKeyColumnNames()
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { Param.SET_PRODUCT_CODE, Param.PRODUCT_CODE };
	}

	/**
	 *
	 * @return PRODUCT_SET_MST
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getTableName()
	 */
	@Override
	protected String getTableName() {
		return "PRODUCT_SET_MST";
	}

}
