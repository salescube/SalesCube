/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;

import jp.co.arkinfosys.entity.Role;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 *
 * ロールサービスクラスです.
 *
 */
public class RoleService extends AbstractService<Role> {
	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String ROLE_ID = "roleId";
	}

	/**
	 * 全ロール情報を取得します.
	 * @return ロールエンティティのリスト
	 * @throws ServiceException
	 */
	public List<Role> findAllRole() throws ServiceException {
		try {
			return this.selectBySqlFile(Role.class, "role/FindAllRole.sql",
					super.createSqlParam()).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
