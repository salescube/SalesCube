/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

/**
 * 商品分類マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ProductClassDto implements MasterEditDto {

	public String classCode1;

	public String className1;

	public String classCode2;

	public String className2;

	public String classCode3;

	public String className3;

	public String className;

	public String updDatetm;

	/**
	 * 大分類コードと中分類コードと小分類コードを取得します.
	 * @return　 文字列の配列　[大分類コード,中分類コード,小分類コード]
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.classCode1, this.classCode2, this.classCode3 };
	}
}
