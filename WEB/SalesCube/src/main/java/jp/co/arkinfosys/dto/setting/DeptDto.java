/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.dto.master.MasterEditDto;

/**
 * 部門情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeptDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	public String deptId;

	public String name;

	public String parentId;

	public String parentName;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	/**
	 * 子供部門リスト
	 */
	public List<DeptDto> childDeptList;

	/**
	 * 子孫の部門を全て返します
	 * @return List<DeptDto>　部門情報リスト
	 */
	public List<DeptDto> getDescDetp() {
		if (this.childDeptList == null) {
			return new ArrayList<DeptDto>(0);
		}

		List<DeptDto> descDeptList = new ArrayList<DeptDto>();
		for (DeptDto dto : childDeptList) {
			descDeptList.add(dto);
			// 子供の子孫リストを全て追加する
			descDeptList.addAll(dto.getDescDetp());
		}
		return descDeptList;
	}

	/**
	 * 部門IDを取得します.
	 * @return 部門ID
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.deptId };
	}
}
