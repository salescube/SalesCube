/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.io.Serializable;
import java.util.List;

/**
 * 数量割引マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DiscountDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	public String discountId;

	public String discountName;

	public String remarks;

	public String useFlag;

	public String useFlagName;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	public List<DiscountTrnDto> discountTrnList;

	/**
	 * 数量割引コードを取得します.
	 * @return　 数量割引コード
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.discountId };
	}
}
