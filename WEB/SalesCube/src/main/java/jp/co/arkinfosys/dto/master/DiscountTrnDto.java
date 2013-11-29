/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.io.Serializable;
/**
 * 数量割引データ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DiscountTrnDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	public Integer discountDataId;

	public String discountId;

	public Integer lineNo;

	public String dataFrom;

	public String dataTo;

	public String discountRate;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	/**
	 * 数量割引コードと数量割引データIDを取得します.
	 * @return　 文字列の配列　[数量割引コード,数量割引データID]
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.discountId, this.discountDataId.toString() };
	}

}
