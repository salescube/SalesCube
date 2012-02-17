/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.EncryptUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.RoleDto;
import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.dto.setting.MenuDto;
import jp.co.arkinfosys.entity.GrantRole;
import jp.co.arkinfosys.entity.join.UserJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 *
 * ユーザ情報サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class UserService extends AbstractMasterEditService<UserDto, UserJoin> implements
		MasterSearch<UserJoin> {

	/**
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String USER_ID = "userId";

		public static final String NAME_KNJ = "nameKnj";

		public static final String NAME_KANA = "nameKana";

		public static final String DEPT_ID = "deptId";

		public static final String DEPT_NAME = "deptName";

		public static final String EMAIL = "email";

		public static final String PASSWORD = "password";

		public static final String PASSWORD_VALID_DAYS = "passwordValidDays";

		private static final String SORT_COLUMN_USER = "sortColumnUser";

		private static final String SORT_COLUMN_DEPT = "sortColumnDept";

		private static final String SORT_ORDER = "sortOrder";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";
	}

	private static final String COLUMN_USER_ID = "USER_ID";

	private static final String COLUMN_NAME_KNJ = "NAME_KNJ";

	private static final String COLUMN_NAME_KANA = "NAME_KANA";

	private static final String COLUMN_DEPT_NAME = "NAME";

	private static final String COLUMN_EMAIL = "EMAIL";

	/**
	 * ユーザIDを指定してユーザ情報を取得します.
	 *
	 * @param userId ユーザID
	 * @return ユーザ情報
	 * @throws ServiceException
	 */
	@Override
	public UserJoin findById(String userId) throws ServiceException {
		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, userId);

			return this.selectBySqlFile(UserJoin.class,
					"user/FindUserById.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ユーザIDとパスワードを指定してユーザ情報を取得します.
	 *
	 * @param userId ユーザID
	 * @param password パスワード
	 * @return ユーザ情報
	 * @throws ServiceException
	 */
	public UserJoin findUserByIdAndPassword(String userId, String password)
			throws ServiceException {
		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, userId);
			param
					.put(UserService.Param.PASSWORD, EncryptUtil
							.encrypt(password));

			return this.selectBySqlFile(UserJoin.class,
					"user/FindUserByIdAndPassword.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して検索結果件数を取得します.
	 *
	 * @param conditions 検索条件
	 * @return 検索結果件数
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

			
			this.setCondition(param, conditions, null, false);

			return this.selectBySqlFile(Integer.class,
					"user/CountUserByCondition.sql", param).getSingleResult()
					.intValue();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件と件数範囲を指定してユーザ情報を取得します.
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 取得件数
	 * @param offset 取得開始位置
	 * @return ユーザ情報のリスト
	 * @throws ServiceException
	 */
	@Override
	public List<UserJoin> findByConditionLimit(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		if (conditions == null) {
			return new ArrayList<UserJoin>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			
			this.setCondition(param, conditions, sortColumn, sortOrderAsc);

			
			if (rowCount > 0) {
				param.put(UserService.Param.ROW_COUNT, rowCount);
				param.put(UserService.Param.OFFSET, offset);
			}

			return this.selectBySqlFile(UserJoin.class,
					"user/FindUserByConditionLimit.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件、ソート対象カラム、ソート順を指定してユーザ情報を検索します.
	 *
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return ユーザ情報のリスト
	 * @throws ServiceException
	 */
	@Override
	public List<UserJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		if (conditions == null) {
			return new ArrayList<UserJoin>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			
			this.setCondition(param, conditions, sortColumn, sortOrderAsc);

			return this.selectBySqlFile(UserJoin.class,
					"user/FindUserByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 空の検索条件オブジェクトを作成します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(UserService.Param.USER_ID, null);
		param.put(UserService.Param.NAME_KNJ, null);
		param.put(UserService.Param.NAME_KANA, null);
		param.put(UserService.Param.DEPT_ID, null);
		param.put(UserService.Param.DEPT_NAME, null);
		param.put(RoleService.Param.ROLE_ID, null);
		param.put(UserService.Param.EMAIL, null);
		param.put(UserService.Param.PASSWORD, null);
		param.put(UserService.Param.PASSWORD_VALID_DAYS, null);
		param.put(UserService.Param.SORT_COLUMN_USER, null);
		param.put(UserService.Param.SORT_COLUMN_DEPT, null);
		param.put(UserService.Param.SORT_ORDER, null);
		param.put(UserService.Param.ROW_COUNT, null);
		param.put(UserService.Param.OFFSET, null);
		return param;
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
	private Map<String, Object> setCondition(Map<String, Object> param,
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) {
		
		if (conditions.containsKey(UserService.Param.USER_ID)) {
			param.put(UserService.Param.USER_ID, super
					.createPrefixSearchCondition((String) conditions
							.get(UserService.Param.USER_ID)));
		}

		
		if (conditions.containsKey(UserService.Param.NAME_KNJ)) {
			param.put(UserService.Param.NAME_KNJ, super
					.createPartialSearchCondition((String) conditions
							.get(UserService.Param.NAME_KNJ)));
		}

		
		if (conditions.containsKey(UserService.Param.NAME_KANA)) {
			param.put(UserService.Param.NAME_KANA, super
					.createPartialSearchCondition((String) conditions
							.get(UserService.Param.NAME_KANA)));
		}

		
		if (conditions.containsKey(UserService.Param.DEPT_ID)) {
			param.put(UserService.Param.DEPT_ID, conditions
					.get(UserService.Param.DEPT_ID));
		}

		
		if (conditions.containsKey(RoleService.Param.ROLE_ID)) {
			param.put(RoleService.Param.ROLE_ID, conditions
					.get(RoleService.Param.ROLE_ID));
		}

		
		if (conditions.containsKey(UserService.Param.EMAIL)) {
			param.put(UserService.Param.EMAIL, super
					.createPartialSearchCondition((String) conditions
							.get(UserService.Param.EMAIL)));
		}

		
		if (UserService.Param.USER_ID.equals(sortColumn)) {
			
			param.put(UserService.Param.SORT_COLUMN_USER,
					UserService.COLUMN_USER_ID);
		} else if (UserService.Param.NAME_KNJ.equals(sortColumn)) {
			
			param.put(UserService.Param.SORT_COLUMN_USER,
					UserService.COLUMN_NAME_KNJ);
		} else if (UserService.Param.NAME_KANA.equals(sortColumn)) {
			
			param.put(UserService.Param.SORT_COLUMN_USER,
					UserService.COLUMN_NAME_KANA);
		} else if (UserService.Param.DEPT_NAME.equals(sortColumn)) {
			
			param.put(UserService.Param.SORT_COLUMN_DEPT,
					UserService.COLUMN_DEPT_NAME);
		} else if (UserService.Param.EMAIL.equals(sortColumn)) {
			
			param.put(UserService.Param.SORT_COLUMN_USER,
					UserService.COLUMN_EMAIL);
		}

		
		if (sortOrderAsc) {
			param.put(UserService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(UserService.Param.SORT_ORDER, Constants.SQL.DESC);
		}

		return param;
	}

	/**
	 * ユーザ情報を登録します.
	 *
	 * @param dto ユーザ情報DTO
	 * @throws ServiceException
	 */
	@Override
	public void insertRecord(UserDto dto) throws ServiceException {
		if (dto == null || dto.menuDtoList == null) {
			return;
		}
		try {

			
			Map<String, Object> param = super.createSqlParam();
			BeanMap userInfo = Beans.createAndCopy(BeanMap.class, dto)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE).includes(
							UserService.Param.USER_ID,
							UserService.Param.NAME_KNJ,
							UserService.Param.NAME_KANA,
							UserService.Param.DEPT_ID, UserService.Param.EMAIL)
					.execute();
			param.putAll(userInfo);
			param.put(UserService.Param.PASSWORD, EncryptUtil
					.encrypt(dto.password));
			param.put(UserService.Param.PASSWORD_VALID_DAYS,
					super.mineDto.passwordValidDays);
			this.updateBySqlFile("user/InsertUser.sql", param).execute();

			
			param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, dto.userId);
			for (MenuDto menuDto : dto.menuDtoList) {
				if (Constants.MENU_VALID_LEVEL.INVALID
						.equals(menuDto.validFlag)) {
					
					continue;
				}

				BeanMap grantRoleInfo = Beans.createAndCopy(BeanMap.class,
						menuDto).includes(MenuService.Param.MENU_ID,
						MenuService.Param.VALID_FLAG).execute();
				param.putAll(grantRoleInfo);

				this.updateBySqlFile("user/InsertGrantRole.sql", param)
						.execute();
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ユーザ情報を更新します.
	 *
	 * @param dto ユーザ情報DTO
	 * @throws ServiceException
	 */
	@Override
	public void updateRecord(UserDto dto) throws ServiceException,
			UnabledLockException {
		if (dto == null || dto.menuDtoList == null) {
			return;
		}
		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, dto.userId);
			super.lockRecordBySqlFile("user/LockUserById.sql", param,
					dto.updDatetm);

			
			param = super.createSqlParam();
			BeanMap userInfo = Beans.createAndCopy(BeanMap.class, dto)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE).includes(
							UserService.Param.USER_ID,
							UserService.Param.NAME_KNJ,
							UserService.Param.NAME_KANA,
							UserService.Param.DEPT_ID, UserService.Param.EMAIL)
					.execute();
			param.putAll(userInfo);
			if (StringUtil.hasLength(dto.password)) {
				param.put(UserService.Param.PASSWORD, EncryptUtil
						.encrypt(dto.password));
				param.put(UserService.Param.PASSWORD_VALID_DAYS,
						super.mineDto.passwordValidDays);
			}
			this.updateBySqlFile("user/UpdateUser.sql", param).execute();

			
			param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, dto.userId);

			for (MenuDto originalMenuDto : dto.originalMenuDtoList) {
				for (MenuDto menuDto : dto.menuDtoList) {
					if (!originalMenuDto.menuId.equals(menuDto.menuId)) {
						continue;
					}

					if (originalMenuDto.validFlag.equals(menuDto.validFlag)) {
						
						continue;
					}

					if (Constants.MENU_VALID_LEVEL.INVALID
							.equals(originalMenuDto.validFlag)) {

						if (Constants.MENU_VALID_LEVEL.VALID_LIMITATION
								.equals(menuDto.validFlag)
								|| Constants.MENU_VALID_LEVEL.VALID_FULL
										.equals(menuDto.validFlag)) {
							
							param
									.put(MenuService.Param.MENU_ID,
											menuDto.menuId);
							param.put(MenuService.Param.VALID_FLAG,
									menuDto.validFlag);
							this.updateBySqlFile("user/InsertGrantRole.sql",
									param).execute();
						}

					} else if (Constants.MENU_VALID_LEVEL.VALID_LIMITATION
							.equals(originalMenuDto.validFlag)
							|| Constants.MENU_VALID_LEVEL.VALID_FULL
									.equals(originalMenuDto.validFlag)) {
						

						param.put(MenuService.Param.MENU_ID,
								originalMenuDto.menuId);
						param.put(MenuService.Param.VALID_FLAG,
								originalMenuDto.validFlag);
						this.updateBySqlFile(
								"user/DeleteGrantRoleByCondition.sql", param)
								.execute();

						if (Constants.MENU_VALID_LEVEL.INVALID
								.equals(menuDto.validFlag)) {
							
							break;
						}

						param.put(MenuService.Param.MENU_ID, menuDto.menuId);
						param.put(MenuService.Param.VALID_FLAG,
								menuDto.validFlag);
						this.updateBySqlFile("user/InsertGrantRole.sql", param)
								.execute();
					}
				}
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
	 * ユーザDTOを指定してユーザを削除します.
	 *
	 * @param dto ユーザ情報DTO
	 * @throws ServiceException
	 */
	@Override
	public void deleteRecord(UserDto dto) throws ServiceException,
			UnabledLockException {
		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, dto.userId);
			super.lockRecordBySqlFile("user/LockUserById.sql", param,
					dto.updDatetm);

			
			param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, dto.userId);
			this.updateBySqlFile("user/DeleteUserByUserId.sql", param)
					.execute();

			
			super.updateAudit(GrantRole.TABLE_NAME, this.getKeyColumnNames(), new String[] { dto.userId });

			
			param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, dto.userId);
			param.put(MenuService.Param.MENU_ID, null);
			this.updateBySqlFile("user/DeleteGrantRoleByCondition.sql", param)
					.execute();

		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ユーザIDとパスワードを指定して、ユーザのパスワードを更新します.
	 *
	 * @param userId ユーザID
	 * @param password パスワード
	 * @throws ServiceException
	 */
	public void updatePassword(String userId, String password)
			throws ServiceException {
		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, userId);
			param
					.put(UserService.Param.PASSWORD, EncryptUtil
							.encrypt(password));

			if (super.mineDto.passwordValidDays != null) {
				
				param.put(UserService.Param.PASSWORD_VALID_DAYS,
						super.mineDto.passwordValidDays);
			}
			this.updateBySqlFile("user/UpdateUser.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * {@link UserJoin}エンティティのリストを{@link UserDto}のリストに変換します.<br>
	 * 返されるリストにおけるユーザと権限の順序付けは、引数として渡される{@link UserJoin}リストの順序を維持します.
	 *
	 * @param userJoinList ユーザ情報エンティティのリスト
	 * @return ユーザ情報DTOのリスト
	 * @throws ServiceException
	 */
	public List<UserDto> convertUserJoinToDto(List<UserJoin> userJoinList)
			throws ServiceException {
		if (userJoinList == null) {
			return new ArrayList<UserDto>();
		}
		try {
			Map<String, UserDto> tempMap = new HashMap<String, UserDto>();
			List<UserDto> resultList = new ArrayList<UserDto>();

			for (UserJoin userJoin : userJoinList) {
				UserDto userDto = tempMap.get(userJoin.userId);
				if (userDto == null) {
					userDto = new UserDto();
					tempMap.put(userJoin.userId, userDto);
					resultList.add(userDto);
					Beans.copy(userJoin, userDto).timestampConverter(
							Constants.FORMAT.TIMESTAMP).dateConverter(
							Constants.FORMAT.DATE).execute();
				}

				
				if (userDto.roleDtoList == null) {
					userDto.roleDtoList = new ArrayList<RoleDto>();
				}
				RoleDto roleDto = new RoleDto();
				userDto.roleDtoList.add(roleDto);
				roleDto.roleId = userJoin.roleId;
				roleDto.roleName = userJoin.roleName;
			}

			return resultList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * キーカラム名を返します.
	 * @return ユーザ情報テーブルのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "USER_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return ユーザ情報テーブル名
	 */
	@Override
	protected String getTableName() {
		return UserJoin.TABLE_NAME;
	}
}
