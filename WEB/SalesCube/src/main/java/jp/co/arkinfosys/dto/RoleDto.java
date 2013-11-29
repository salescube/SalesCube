/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

import java.io.Serializable;

/**
 * 権限情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class RoleDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public String roleId;

	public String roleName;

	public String remarks;

}
