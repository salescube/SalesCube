/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.ProductClassDto;
import jp.co.arkinfosys.entity.ProductClass;
import jp.co.arkinfosys.entity.join.ProductClassJoin;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 商品分類サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ProductClassService extends
		AbstractMasterEditService<ProductClassDto, ProductClass> implements
		MasterSearch<ProductClassJoin> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String CLASS_CODE_1 = "classCode1";

		public static final String CLASS_CODE_2 = "classCode2";

		public static final String CLASS_CODE_3 = "classCode3";

		public static final String CLASS_CODE = "classCode";

		public static final String CLASS_NAME = "className";

		public static final String TARGET_COLUMN = "targetColumn";

		private static final String SORT_COLUMN = "sortColumn";

		private static final String SORT_ORDER = "sortOrder";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";
	}

	public static final String COLUMN_CLASS_CODE_1 = "CLASS_CODE_1";
	public static final String COLUMN_CLASS_CODE_2 = "CLASS_CODE_2";
	public static final String COLUMN_CLASS_CODE_3 = "CLASS_CODE_3";

	/**
	 * すべての商品分類(大)情報のリストを返します.
	 * @return 商品分類情報{@link ProductClass}のリスト
	 * @throws ServiceException
	 */
	public List<ProductClass> findAllProductClass1() throws ServiceException {
		try {
			return this.selectBySqlFile(ProductClass.class,
					"productclass/FindAllProductClass1.sql",
					super.createSqlParam()).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 大分類コードを指定して、商品分類(中)情報のリストを返します.
	 * @param classCode1 大分類コード
	 * @return 商品分類情報{@link ProductClass}のリスト
	 * @throws ServiceException
	 */
	public List<ProductClass> findAllProductClass2(String classCode1)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.CLASS_CODE_1, classCode1);
			return this.selectBySqlFile(ProductClass.class,
					"productclass/FindAllProductClass2.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 大分類コードと中部分類コードを指定して、商品分類(小)情報のリストを返します.
	 * @param classCode1 大分類コード
	 * @param classCode2 中分類コード
	 * @return 商品分類情報{@link ProductClass}のリスト
	 * @throws ServiceException
	 */
	public List<ProductClass> findAllProductClass3(String classCode1,String classCode2)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.CLASS_CODE_1, classCode1);
			param.put(Param.CLASS_CODE_2, classCode2);
			return this.selectBySqlFile(ProductClass.class,
					"productclass/FindAllProductClass3.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、商品分類情報を返します.
	 * @param conditions 検索条件マップ
	 * @return 商品分類情報{@link ProductClass}のリスト
	 * @throws ServiceException
	 */
	public List<ProductClass> findProductClassByCondition(
			Map<String, Object> conditions) throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		this.setEmptyCondition(param);

		setCondition(conditions, param, null, true);

		return this.selectBySqlFile(ProductClass.class,
				"productclass/FindProductClassByCondition.sql", param)
				.getResultList();
	}

	/**
	 * 商品分類DTOを指定して、商品分類情報を返します.
	 * @param dto 商品分類DTO
	 * @return 商品分類情報{@link ProductClass}のリスト
	 * @throws ServiceException
	 */
	public ProductClass findProductClassByKey(ProductClassDto dto)
			throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		this.setEmptyCondition(param);
		param.put(Param.CLASS_CODE_1, dto.classCode1);
		param.put(Param.CLASS_CODE_2, dto.classCode2);
		param.put(Param.CLASS_CODE_3, dto.classCode3);

		return this.selectBySqlFile(ProductClass.class,
				"productclass/FindProductClassByCondition.sql", param)
				.getSingleResult();
	}

	/**
	 * 検索条件を設定します.
	 * @param conditions 検索条件値マップ
	 * @param param 検索条件マップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 */
	private void setCondition(Map<String, Object> conditions,
			Map<String, Object> param, String sortColumn, boolean sortOrderAsc) {
		// 商品分類コード（大）
		if (conditions.containsKey(ProductClassService.Param.CLASS_CODE_1)) {
			param.put(ProductClassService.Param.CLASS_CODE_1, conditions
					.get(ProductClassService.Param.CLASS_CODE_1));
		}

		// 商品分類コード（中）
		if (conditions.containsKey(ProductClassService.Param.CLASS_CODE_2)) {
			param.put(ProductClassService.Param.CLASS_CODE_2, conditions
					.get(ProductClassService.Param.CLASS_CODE_2));
		}

		// 商品分類コード（小）
		if (conditions.containsKey(ProductClassService.Param.CLASS_CODE_3)) {
			param.put(ProductClassService.Param.CLASS_CODE_3, conditions
					.get(ProductClassService.Param.CLASS_CODE_3));
		}

		// 商品分類コード（いずれか）
		if (conditions.containsKey(ProductClassService.Param.CLASS_CODE)) {
			param.put(ProductClassService.Param.CLASS_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ProductClassService.Param.CLASS_CODE)));
		}

		// 商品名
		if (conditions.containsKey(ProductClassService.Param.CLASS_NAME)) {
			param.put(ProductClassService.Param.CLASS_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(ProductClassService.Param.CLASS_NAME)));
		}

		// ソートカラム
		if (StringUtil.hasLength(sortColumn)) {
			param.put(ProductClassService.Param.SORT_COLUMN, super.convertVariableNameToColumnName(sortColumn));
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(ProductClassService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(ProductClassService.Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件マップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(ProductClassService.Param.CLASS_CODE_1, null);
		param.put(ProductClassService.Param.CLASS_CODE_2, null);
		param.put(ProductClassService.Param.CLASS_CODE_3, null);
		param.put(ProductClassService.Param.CLASS_CODE, null);
		param.put(ProductClassService.Param.CLASS_NAME, null);
		return param;
	}

	/**
	 * 分類(大)のプルダウン要素のリストを返します.
	 * @return プルダウン要素のリスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> findAllProductClass1LabelValueBeanList()
			throws ServiceException {

		List<ProductClass> list = findAllProductClass1();
		List<LabelValueBean> labelValueList = new ArrayList<LabelValueBean>();

		for (ProductClass productClass : list) {
			labelValueList.add(new LabelValueBean(productClass.className,
					productClass.classCode1));
		}

		return labelValueList;
	}

	/**
	 * 分類(中)のプルダウン要素のリストを返します.
	 * @param classCode1 大分類コード
	 * @return プルダウン要素のリスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> findAllProductClass2LabelValueBeanList(
			String classCode1) throws ServiceException {

		List<ProductClass> list = findAllProductClass2(classCode1);
		List<LabelValueBean> labelValueList = new ArrayList<LabelValueBean>();

		for (ProductClass productClass : list) {
			labelValueList.add(new LabelValueBean(productClass.className,
					productClass.classCode2));
		}

		return labelValueList;
	}

	/**
	 * 分類(中)のプルダウン要素のリストを返します.
	 * @param classCode1 大分類コード
	 * @return プルダウン要素のリスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> findAllProductClass3LabelValueBeanList(
			String classCode1,String classCode2) throws ServiceException {

		List<ProductClass> list = findAllProductClass3(classCode1,classCode2);
		List<LabelValueBean> labelValueList = new ArrayList<LabelValueBean>();

		for (ProductClass productClass : list) {
			labelValueList.add(new LabelValueBean(productClass.className,
					productClass.classCode3));
		}

		return labelValueList;
	}


	/**
	 * 次の商品コードを採番して返します.
	 * @param conditions 検索条件のマップ
	 * @return 商品コード
	 * @throws ServiceException
	 */
	public String getNextCode(Map<String, Object> conditions)
			throws ServiceException {
		if (conditions == null) {
			return "0000";
		}

		Map<String, Object> param = super.createSqlParam();
		this.setEmptyCondition(param);

		param.putAll(conditions);

		String maxValue = this.selectBySqlFile(String.class,
				"productclass/MaxProductClassCodeByCondition.sql", param)
				.getSingleResult();

		if (!StringUtil.hasLength(maxValue)) {
			maxValue = "0";
		}
		int max = Integer.parseInt(maxValue);

		// ４桁でパディング
		String nextVal = "0000" + (max + 1);
		nextVal = nextVal.substring(nextVal.length() - 4);
		return nextVal;
	}

	/**
	 * 検索条件に合致する件数を返します.
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

			this.setCondition(conditions, param, null, true);

			return this.selectBySqlFile(Integer.class,
					"productclass/CountProductClassByCondition.sql", param)
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
	 * @return {@link ProductClassJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByConditionLimit(java.util.Map, java.lang.String, boolean, int, int)
	 */
	@Override
	public List<ProductClassJoin> findByConditionLimit(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			setCondition(conditions, param, sortColumn, sortOrderAsc);

			// LIMITを設定する
			if (rowCount > 0) {
				param.put(Param.ROW_COUNT, rowCount);
				param.put(Param.OFFSET, offset);
			}

			return this.selectBySqlFile(ProductClassJoin.class,
					"productclass/FindProductClassByConditionLimit.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param id ID
	 * @return マスタ情報
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findById(java.lang.String)
	 */
	@Override
	public ProductClassJoin findById(String id) throws ServiceException {
		// 未使用メソッド
		return null;
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @return {@link ProductClassJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByCondition(java.util.Map, java.lang.String, boolean)
	 */
	@Override
	public List<ProductClassJoin> findByCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {
		// 未使用メソッド
		return new ArrayList<ProductClassJoin>();
	}

	/**
	 *
	 * @param dto {@link ProductClassDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#deleteRecord(java.lang.Object)
	 */
	@Override
	public void deleteRecord(ProductClassDto dto) throws Exception {
		// 排他制御
		// データを整形
		if (dto.classCode1 == null) {
			dto.classCode1 = "";
		}
		if (dto.classCode2 == null) {
			dto.classCode2 = "";
		}
		if (dto.classCode3 == null) {
			dto.classCode3 = "";
		}
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.CLASS_CODE_1, dto.classCode1);
		param.put(Param.CLASS_CODE_2, dto.classCode2);
		param.put(Param.CLASS_CODE_3, dto.classCode3);
		String timestamp = dto.updDatetm;
		this.lockRecordBySqlFile("productclass/LockProductClass.sql", param,
				timestamp);

		// 削除
		param = super.createSqlParam();
		param.put(Param.CLASS_CODE_1, dto.classCode1);
		param.put(Param.CLASS_CODE_2, dto.classCode2);
		param.put(Param.CLASS_CODE_3, dto.classCode3);
		this.updateBySqlFile("productclass/DeleteProductClass.sql", param)
				.execute();

	}

	/**
	 *
	 * @param dto {@link ProductClassDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#insertRecord(java.lang.Object)
	 */
	@Override
	public void insertRecord(ProductClassDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 登録
		Map<String, Object> param = super.createSqlParam();

		// データを整形
		if (dto.classCode1 == null) {
			dto.classCode1 = "";
		}
		if (dto.classCode2 == null) {
			dto.classCode2 = "";
		}
		if (dto.classCode3 == null) {
			dto.classCode3 = "";
		}

		BeanMap discountInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();

		param.putAll(discountInfo);
		this.updateBySqlFile("productclass/InsertProductClass.sql", param)
				.execute();
	}

	/**
	 *
	 * @param dto {@link ProductClassDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#updateRecord(java.lang.Object)
	 */
	@Override
	public void updateRecord(ProductClassDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// データを整形
		if (dto.classCode1 == null) {
			dto.classCode1 = "";
		}
		if (dto.classCode2 == null) {
			dto.classCode2 = "";
		}
		if (dto.classCode3 == null) {
			dto.classCode3 = "";
		}

		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(Param.CLASS_CODE_1, dto.classCode1);
		lockParam.put(Param.CLASS_CODE_2, dto.classCode2);
		lockParam.put(Param.CLASS_CODE_3, dto.classCode3);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("productclass/LockProductClass.sql", lockParam,
				dto.updDatetm);

		// 更新
		Map<String, Object> param = super.createSqlParam();
		BeanMap productClassInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();

		param.putAll(productClassInfo);
		this.updateBySqlFile("productclass/UpdateProductClass.sql", param)
				.execute();
	}

	/**
	 *
	 * @return {CLASS_CODE_1, CLASS_CODE_2, CLASS_CODE_3}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getKeyColumnNames()
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "CLASS_CODE_1", "CLASS_CODE_2", "CLASS_CODE_3" };
	}

	/**
	 *
	 * @return {@link ProductClass#TABLE_NAME}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getTableName()
	 */
	@Override
	protected String getTableName() {
		return ProductClass.TABLE_NAME;
	}

}
