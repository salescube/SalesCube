/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.CategoryDto;
import jp.co.arkinfosys.dto.master.CategoryGroupDto;
import jp.co.arkinfosys.dto.master.CategoryMstDto;
import jp.co.arkinfosys.entity.Category;
import jp.co.arkinfosys.entity.CategoryTrn;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * カテゴリのサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class CategoryService extends AbstractMasterEditService<CategoryDto,Category> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String CATEGORY_ID = "categoryId";
		public static final String CATEGORY_CODE = "categoryCode";
		public static final String GROUP_NAME = "groupName";
		public static final String CATEGORY_ADD = "categoryAdd";
		public static final String CATEGORY_UPD = "categoryUpd";
		public static final String CATEGORY_DEL = "categoryDel";

		public static final String SORT_ORDER_COLUMN = "sortOrderColumn";
	}

	// 権限の有無
	private static final String VALUE_TRUE = "1";
	private static final String VALUE_FALSE = "0";


	private static final String COLUMN_CATEGORY_ID = "CATEGORY_ID";
	private static final String COLUMN_GROUP_NAME = "GROUP_NAME";

	/**
	 * カテゴリIDを指定して、カテゴリ情報のリストを返します.
	 * @param categoryId カテゴリID
	 * @return カテゴリ情報({@link CategoryJoin})のリスト
	 * @throws ServiceException
	 */
	public List<CategoryJoin> findCategoryJoinById(int categoryId)
			throws ServiceException {

		Map<String, Object> param = super.createSqlParam();
		param.put(CategoryService.Param.CATEGORY_ID, categoryId);

		return this.selectBySqlFile(CategoryJoin.class,
				"category/FindCategoryJoinById.sql", param).getResultList();
	}

	/**
	 * カテゴリIDを指定して、カテゴリ情報のマップを返します.
	 * @param categoryId カテゴリID
	 * @return カテゴリ情報({@link CategoryJoin})のマップ
	 * @throws ServiceException
	 */
	public Map<String, CategoryJoin> findCategoryJoinMapById(int categoryId)
			throws ServiceException {

		List<CategoryJoin> list = findCategoryJoinById(categoryId);
		Map<String, CategoryJoin> map = new TreeMap<String, CategoryJoin>();
		for (CategoryJoin categoryJoin : list) {
			map.put(categoryJoin.categoryCode, categoryJoin);
		}

		return map;
	}

	/**
	 * カテゴリIDを指定して、カテゴリ名の{@link LableValueBean}リストを返します.
	 * @param categoryId カテゴリID
	 * @return カテゴリ名の{@link LableValueBean}リスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> findCategoryLabelValueBeanListById(
			int categoryId) throws ServiceException {

		List<CategoryJoin> list = findCategoryJoinById(categoryId);
		List<LabelValueBean> categoryList = new ArrayList<LabelValueBean>();

		for (CategoryJoin categoryJoin : list) {
			categoryList.add(new LabelValueBean(categoryJoin.categoryCodeName,
					categoryJoin.categoryCode));
		}

		return categoryList;
	}

	/**
	 * カテゴリIDおよびカテゴリコードを指定して、カテゴリ名を返します.
	 * @param categoryId カテゴリID
	 * @param categoryCode カテゴリコード
	 * @return カテゴリ名
	 * @throws ServiceException
	 */
	public String findCategoryNameByIdAndCode(int categoryId, String categoryCode) throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(CategoryService.Param.CATEGORY_ID, categoryId);
		param.put(CategoryService.Param.CATEGORY_CODE, categoryCode);

		List<CategoryTrn> result = this.selectBySqlFile(CategoryTrn.class,
				"category/FindCategoryNameByIdAndCode.sql", param).getResultList();

		if (result == null || result.size() == 0) {
			// 見つからなかったらnullを返す
			return null;
		}

		return result.get(0).categoryCodeName;
	}

	/**
	 * 更新可能なグループ情報のリストを返します.
	 * @return グループ情報({@link CategoryGroupDto})のリスト
	 * @throws ServiceException
	 */
	public List<CategoryGroupDto> findUpdatableGroups() throws ServiceException {
		List<CategoryGroupDto> result = new ArrayList<CategoryGroupDto>();

		// グループ名を検索する(DISTINCT)
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.SORT_ORDER_COLUMN, COLUMN_GROUP_NAME);

		List<Category> list = this.selectBySqlFile(Category.class,
				"category/FindUpdatableGroups.sql", param).getResultList();

		if (list == null || list.size() == 0) {
			return result;
		}

		// グループ名だけ抜き出す
		for (Category category : list) {
			CategoryGroupDto group = new CategoryGroupDto();
			group.name = category.groupName;
			result.add(group);
		}
		return result;
	}

	/**
	 * グループ名を指定して、カテゴリ情報のリストを返します.
	 * @param groupName グループ名
	 * @return カテゴリ情報({@link CategoryMstDto})のリスト
	 * @throws ServiceException
	 */
	public List<CategoryMstDto> findCategoryByGroupName(String groupName) throws ServiceException {
		List<CategoryMstDto> result = new ArrayList<CategoryMstDto>();

		// グループ名から、区分マスタのレコードを検索する
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.SORT_ORDER_COLUMN, COLUMN_CATEGORY_ID);
		param.put(Param.GROUP_NAME, groupName);

		List<Category> list = this.selectBySqlFile(Category.class, "category/FindCategoryByGroupName.sql", param).getResultList();

		if (list == null || list.size() == 0) {
			return result;
		}

		for (Category category : list) {
			CategoryMstDto dto = new CategoryMstDto();
			dto.categoryId = String.valueOf(category.categoryId);
			dto.name = category.categoryName;
			result.add(dto);
		}

		return result;
	}

	/**
	 * カテゴリIDを指定して、カテゴリ情報を返します.
	 * @param categoryId カテゴリID
	 * @return カテゴリ情報({@link Category})
	 * @throws ServiceException
	 */
	public Category findCategoryById(String categoryId) throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(CategoryService.Param.CATEGORY_ID, categoryId);

		List<Category> list = this.selectBySqlFile(Category.class,
				"category/FindCategoryById.sql", param).getResultList();

		if (list == null || list.size() != 1) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * カテゴリIDとカテゴリコードを指定して、カテゴリ情報を返します.
	 * @param categoryId カテゴリID
	 * @param categoryCode カテゴリコード
	 * @return カテゴリ情報{@link CategoryTrn}
	 * @throws ServiceException
	 */
	public CategoryTrn findCategoryTrnByIdAndCode(int categoryId, String categoryCode) throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(CategoryService.Param.CATEGORY_ID, categoryId);
		param.put(CategoryService.Param.CATEGORY_CODE, categoryCode);

		return this.selectBySqlFile(CategoryTrn.class,
				"category/FindCategoryTrnByIdAndCode.sql", param).getSingleResult();
	}

	/**
	 * カテゴリIDとカテゴリコードを指定して、画面に表示されていないカテゴリ情報を返します.
	 * @param categoryId カテゴリID
	 * @param categoryCode カテゴリコード
	 * @return カテゴリ情報{@link CategoryTrn}
	 * @throws ServiceException
	 */
	public CategoryTrn findCategoryTrnNoDspByIdAndCode(CategoryDto dto) throws ServiceException {

		Map<String, Object> param = super.createSqlParam();
		param.put(Param.CATEGORY_ID, dto.categoryId);
		param.put(Param.CATEGORY_CODE, dto.categoryCode);

		return this.selectBySqlFile(CategoryTrn.class,
				"category/FindCategoryTrnNoDspByIdAndCode.sql", param).getSingleResult();
	}

	/**
	 * 全ての区分マスタ情報を返します.<br>
	 * CategoryTrnとCategoryMstDtoのプロパティ名が対応していないため、
	 * BeanMapとSQLでプロパティ名を対応させます.
	 * @return 区分マスタ情報{@link CategoryMstDto}のリスト
	 * @throws ServiceException
	 */
	public List<CategoryMstDto> findAllCategoryMst() throws ServiceException {
		List<CategoryMstDto> resultList = new ArrayList<CategoryMstDto>();
		Map<String, Object> param = super.createSqlParam();

		List<BeanMap> list
				= this.selectBySqlFile(BeanMap.class, "category/FindAllCategory.sql", param).getResultList();

		if(list == null | list.size() <= 0){
			return resultList;
		}

		for(BeanMap bean : list){
			CategoryMstDto dto
				= Beans.createAndCopy(CategoryMstDto.class, bean)
					.dateConverter(Constants.FORMAT.TIMESTAMP, "updDatetm")
					.execute();
			resultList.add(dto);
		}
		return resultList;
	}

	/**
	 * 全ての区分マスタ情報を更新します.<br>
	 * @return 区分マスタ情報{@link CategoryMstDto}のリスト
	 * @throws ServiceException
	 */
	public void updateCategoryMst(List<CategoryMstDto> categoryMstDtoList) throws ServiceException,	UnabledLockException{
		if (categoryMstDtoList == null ||categoryMstDtoList == null) {
			return;
		}
		try {
			for(CategoryMstDto category : categoryMstDtoList){
				// 排他制御
				Map<String, Object> lockParam = super.createSqlParam();
				lockParam.put(Param.CATEGORY_ID, category.categoryId);
				super.lockRecordBySqlFile("category/LockCategoryByCategoryId.sql", lockParam,
						category.updDatetm);

				// 更新処理
				Map<String, Object> param = super.createSqlParam();
				param.put(Param.CATEGORY_ID, category.categoryId);
				param.put(Param.CATEGORY_ADD, category.isInsert == true ? VALUE_TRUE : VALUE_FALSE);
				param.put(Param.CATEGORY_UPD, category.isUpdate == true ? VALUE_TRUE : VALUE_FALSE);
				param.put(Param.CATEGORY_DEL, category.isDelete == true ? VALUE_TRUE : VALUE_FALSE);
				this.updateBySqlFile("category/UpdateCategoryRoleById.sql", param).execute();
			}
		} catch (UnabledLockException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}




	}

	/**
	 * カテゴリを削除します.
	 * @param dto {@link CategoryDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#deleteRecord(java.lang.Object)
	 */
	@Override
	public void deleteRecord(CategoryDto dto) throws Exception {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.CATEGORY_ID, dto.categoryId);
		param.put(Param.CATEGORY_CODE, dto.categoryCode);
		this.updateBySqlFile("category/DeleteCategoryTrn.sql", param).execute();
	}

	/**
	 * カテゴリを登録します.
	 * @param dto {@link CategoryDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#insertRecord(java.lang.Object)
	 */
	@Override
	public void insertRecord(CategoryDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 新規登録
		Map<String, Object> param = super.createSqlParam();
		BeanMap categoryMap = Beans.createAndCopy(BeanMap.class, dto)
								.timestampConverter(Constants.FORMAT.TIMESTAMP)
								.dateConverter(Constants.FORMAT.DATE)
								.execute();

		param.putAll(categoryMap);

		this.updateBySqlFile("category/InsertCategoryTrn.sql", param).execute();
	}

	/**
	 * カテゴリを更新します.
	 * @param dto {@link CategoryDto}
	 * @throws Exception
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#updateRecord(java.lang.Object)
	 */
	@Override
	public void updateRecord(CategoryDto dto) throws Exception {
		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(Param.CATEGORY_ID, dto.categoryId);
		lockParam.put(Param.CATEGORY_CODE, dto.categoryCode);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("category/LockCategoryTrn.sql", lockParam, dto.updDatetm);

		// 更新日付を書き換える
		this.updateBySqlFile("category/UpdateCategoryTrnUpdDatetm.sql", lockParam).execute();
	}

	/**
	 * カテゴリ更新日時を更新します.
	 * @param categoryId カテゴリID
	 * @param updDatetm 更新日時の文字列
	 * @throws Exception
	 */
	public void updateMasterRecordUpdDatetm(Integer categoryId, String updDatetm) throws Exception {
		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(Param.CATEGORY_ID, categoryId);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("category/LockCategoryByCategoryId.sql", lockParam, updDatetm);

		// 更新日付を書き換える
		this.updateBySqlFile("category/UpdateCategoryUpdDatetm.sql", lockParam).execute();
	}

	/**
	 * 締日グループと支払サイクルに分割する元のコードを締日グループに変換します.
	 * @param cutoffGroupCategory 締日グループと支払サイクルに分割する元のコード
	 * @return 締日グループ
	 */
	public String cutoffGroupCategoryToCutoffGroup(
			String cutoffGroupCategory) {
		if (StringUtil.hasLength(cutoffGroupCategory)) {
			return cutoffGroupCategory.substring(0, 2);
		}
		return "";
	}

	/**
	 * 締日グループと支払サイクルに分割する元のコードを支払サイクルに変換します.
	 * @param cutoffGroupCategory 締日グループと支払サイクルに分割する元のコード
	 * @return 支払サイクル
	 */
	public String cutoffGroupCategoryToPaybackCycleCategory(
			String cutoffGroupCategory) {
		if (StringUtil.hasLength(cutoffGroupCategory)) {
			return cutoffGroupCategory.substring(2, 3);
		}
		return "";
	}

	/**
	 *
	 * @return {CATEGORY_ID, CATEGORY_CODE}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getKeyColumnNames()
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "CATEGORY_ID", "CATEGORY_CODE" };
	}

	/**
	 *
	 * @return {@link CategoryTrn#TABLE_NAME}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getTableName()
	 */
	@Override
	protected String getTableName() {
		return CategoryTrn.TABLE_NAME;
	}
}
