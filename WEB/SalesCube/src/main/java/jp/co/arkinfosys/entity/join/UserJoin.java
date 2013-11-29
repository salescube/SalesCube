/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import jp.co.arkinfosys.entity.User;

/**
 * 社員マスタと部門マスタとロールマスタのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class UserJoin extends User {

	private static final long serialVersionUID = 1L;

	public String deptName;

	public String roleId;

	public String roleName;

}
