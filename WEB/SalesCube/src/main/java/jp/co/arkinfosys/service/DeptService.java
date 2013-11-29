/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.setting.DeptDto;
import jp.co.arkinfosys.entity.Dept;
import jp.co.arkinfosys.entity.join.DeptJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 部門情報サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DeptService extends AbstractMasterEditService<DeptDto, DeptJoin> implements
		MasterSearch<DeptJoin> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String DEPT_ID = "deptId";

		public static final String PARENT_ID = "parentId";

		public static final String NAME = "name";

		public static final String SORT_COLUMN = "sortColumn";

		public static final String SORT_ORDER = "sortOrder";

		public static final String ROW_COUNT = "rowCount";

		public static final String OFFSET = "offsetRow";
	}

	/**
	 * すべての部門情報のリストを返します.
	 * @return 部門情報{@link Dept}のリスト
	 * @throws ServiceException
	 */
	public List<Dept> findAllDept() throws ServiceException {
		try {
			return this.selectBySqlFile(Dept.class, "dept/FindAllDept.sql",
					super.createSqlParam()).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 部門IDを指定して、部門情報を返します.
	 * @param deptId 部門ID
	 * @return 部門情報{@link DeptJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findById(java.lang.String)
	 */
	@Override
	public DeptJoin findById(String deptId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(DeptService.Param.DEPT_ID, deptId);

			return this.selectBySqlFile(DeptJoin.class,
					"dept/FindDeptByDeptId.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 親部門IDを指定して、部門情報のリストを返します.
	 * @param parentId 親部門ID
	 * @return 部門情報{@link Dept}のリスト
	 * @throws ServiceException
	 */
	public List<Dept> findByParentId(String parentId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(DeptService.Param.PARENT_ID, parentId);

			return this.selectBySqlFile(Dept.class,
					"dept/FindDeptByParentId.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @return 検索結果件数
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
					"dept/CountDeptByCondition.sql", param).getSingleResult()
					.intValue();
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
	 * @return {@link DeptJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByConditionLimit(java.util.Map, java.lang.String, boolean, int, int)
	 */
	@Override
	public List<DeptJoin> findByConditionLimit(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
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

			return this.selectBySqlFile(DeptJoin.class,
					"dept/FindDeptByConditionLimit.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @return {@link DeptJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByCondition(java.util.Map, java.lang.String, boolean)
	 */
	@Override
	public List<DeptJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		return new ArrayList<DeptJoin>();
		// 未使用メソッド
	}

	/**
	 * 部門IDを指定して、部門情報を削除します.
	 * @param dto 部門DTO
	 * @throws ServiceException
	 * @throws UnabledLockException
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#deleteRecord(java.lang.Object)
	 */
	@Override
	public void deleteRecord(DeptDto dto) throws ServiceException,
			UnabledLockException {

		try {
			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(DeptService.Param.DEPT_ID, dto.deptId);
			this.lockRecordBySqlFile("dept/LockDeptByDeptId.sql", param,
					dto.updDatetm);

			// 削除
			param = super.createSqlParam();
			param.put(DeptService.Param.DEPT_ID, dto.deptId);
			this.updateBySqlFile("dept/DeleteDeptByDeptId.sql", param)
					.execute();
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 部門情報を登録します.
	 * @param dto 部門DTO
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#insertRecord(java.lang.Object)
	 */
	@Override
	public void insertRecord(DeptDto dto) throws ServiceException {
		if (dto == null) {
			return;
		}
		try {
			// ユーザーの登録
			Map<String, Object> param = super.createSqlParam();
			BeanMap deptInfo = Beans
					.createAndCopy(BeanMap.class, dto)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE)
					.includes(DeptService.Param.DEPT_ID,
							DeptService.Param.PARENT_ID, DeptService.Param.NAME)
					.execute();
			param.putAll(deptInfo);

			this.updateBySqlFile("dept/InsertDept.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 部門情報を更新します.
	 * @param dto 部門DTO
	 * @throws ServiceException
	 * @throws UnabledLockException
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#updateRecord(java.lang.Object)
	 */
	@Override
	public void updateRecord(DeptDto dto) throws ServiceException,
			UnabledLockException {
		if (dto == null) {
			return;
		}
		try {
			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(DeptService.Param.DEPT_ID, dto.deptId);
			this.lockRecordBySqlFile("dept/LockDeptByDeptId.sql", param,
					dto.updDatetm);

			// ユーザーの登録
			param = super.createSqlParam();
			BeanMap deptInfo = Beans
					.createAndCopy(BeanMap.class, dto)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE)
					.includes(DeptService.Param.DEPT_ID,
							DeptService.Param.PARENT_ID, DeptService.Param.NAME)
					.execute();
			param.putAll(deptInfo);

			this.updateBySqlFile("dept/UpdateDept.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を設定します.
	 * @param conditions 検索条件値マップ
	 * @param sortColumn ソート条件
	 * @param sortOrderAsc 昇順か否か
	 * @param param 検索条件マップ
	 */
	private void setCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {
		// 部門ID
		if (conditions.containsKey(DeptService.Param.DEPT_ID)) {
			param.put(DeptService.Param.DEPT_ID, super
					.createPrefixSearchCondition((String) conditions
							.get(DeptService.Param.DEPT_ID)));
		}

		// 親部門ID
		if (conditions.containsKey(DeptService.Param.PARENT_ID)) {
			param.put(DeptService.Param.PARENT_ID, conditions
					.get(DeptService.Param.PARENT_ID));
		}

		// 部門名
		if (conditions.containsKey(DeptService.Param.NAME)) {
			param.put(DeptService.Param.NAME, super
					.createPartialSearchCondition((String) conditions
							.get(DeptService.Param.NAME)));
		}

		// ソートカラム名を設定する
		if (StringUtil.hasLength(sortColumn)) {
			param.put(DeptService.Param.SORT_COLUMN, StringUtil
					.convertColumnName(sortColumn));
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(DeptService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(DeptService.Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件マップ
	 */
	private void setEmptyCondition(Map<String, Object> param) {
		param.put(DeptService.Param.DEPT_ID, null);
		param.put(DeptService.Param.PARENT_ID, null);
		param.put(DeptService.Param.NAME, null);
	}

	/**
	 * エンティティからDTOへの変換を行います.
	 * @param deptJoinList 部門エンティティのリスト
	 * @return 部門DTOのリスト
	 */
	public List<DeptDto> convertEntityToDto(List<Dept> deptJoinList) {
		List<DeptDto> deptDtoList = new ArrayList<DeptDto>(0);
		if (deptJoinList == null) {
			return deptDtoList;
		}

		// 一時マップに格納する
		Map<String, DeptDto> temp = new HashMap<String, DeptDto>(0);
		for (Dept deptJoin : deptJoinList) {
			DeptDto dto = Beans.createAndCopy(DeptDto.class, deptJoin)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE).execute();
			temp.put(deptJoin.deptId, dto);
		}

		// 親子関係を構築する
		for (Dept deptJoin : deptJoinList) {
			if (StringUtil.hasLength(deptJoin.parentId)) {
				DeptDto parent = temp.get(deptJoin.parentId);
				if (parent == null) {
					continue;
				}

				// 親の子供として追加する
				if (parent.childDeptList == null) {
					parent.childDeptList = new ArrayList<DeptDto>(0);
				}
				parent.childDeptList.add(temp.get(deptJoin.deptId));
			}
		}
		deptDtoList.addAll(temp.values());

		return deptDtoList;
	}

	/**
	 *
	 * @return {DEPT_ID}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getKeyColumnNames()
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "DEPT_ID" };
	}

	/**
	 *
	 * @return {@link DeptJoin#TABLE_NAME}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getTableName()
	 */
	@Override
	protected String getTableName() {
		return DeptJoin.TABLE_NAME;
	}
}
