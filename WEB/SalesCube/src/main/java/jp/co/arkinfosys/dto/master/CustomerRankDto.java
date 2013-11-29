/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

/**
 * 顧客ランクマスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CustomerRankDto implements MasterEditDto {

	public String rankCode;

	public String rankName;

	public String rankRate;

	public String roCountFrom;

	public String enrollTermFrom;

	public String defectTermFrom;

	public String roMonthlyAvgFrom;

	public String postageType;

	public String remarks;

	public String updDatetm;

	public String roCountTo;

	public String enrollTermTo;

	public String defectTermTo;

	public String roMonthlyAvgTo;

	/**
	 * 顧客ランクコードを取得します.
	 * @return　 顧客ランクコード
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.rankCode };
	}

}
