/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

/**
 * 区分データのDTOクラスです.
 *
 * @author Ark Information Systems
 */
public class CategoryDto implements MasterEditDto {
	/**
	 * 区分ＩＤ
	 */
	public String categoryId;

	/**
	 * 区分データコード
	 */
	public String categoryCode;

	/**
	 * 区分データ名
	 */
	public String categoryCodeName;

	/**
	 * 区分データ文字列
	 */
	public String categoryStr;

	/**
	 * 区分データ整数
	 */
	public String categoryNum;

	/**
	 * 区分データ小数
	 */
	public String categoryFlt;

	/**
	 * 区分データ論理
	 */
	public String categoryBool;

	/**
	 * 区分データ
	 */
	public String categoryDsp;

	/**
	 * 最終更新日付
	 */
	public String updDatetm;

	/**
	 * 区分ＩＤと区分データコードを取得します.
	 * @return　 文字列の配列　[区分ＩＤ,区分データコード]
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.categoryId, this.categoryCode };
	}
}
