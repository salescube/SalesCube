/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

/**
 * セット商品マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ProductSetDto implements MasterEditDto {

	/**
	 * セット商品コード
	 */
	public String setProductCode;

	/**
	 * セット商品名
	 */
	public String setProductName;

	/**
	 * セット内容商品コード
	 */
	public String productCode;

	/**
	 * オリジナルの内容商品コード
	 */
	public String originalProductCode;

	/**
	 * セット内容商品名
	 */
	public String productName;

	/**
	 * セット内容商品数量
	 */
	public String quantity;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	/**
	 * 削除済フラグ
	 */
	public boolean deleted;

	/**
	 * セット商品コードとセット内容商品コードを取得します.
	 * @return　 文字列の配列　[セット商品コード,セット内容商品]
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.setProductCode, this.productCode };
	}
}
