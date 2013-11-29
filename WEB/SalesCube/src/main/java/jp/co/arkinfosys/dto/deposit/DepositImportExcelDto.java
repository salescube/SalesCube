/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.deposit;

/**
 * 配送業者入金データ取込画面の結果リスト行の要素のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DepositImportExcelDto {
	/**
	 * カラム名
	 */
	public String name;

	/**
	 * 表示形式（0：無加工/1：整数/2：実数/10：年月日/11：年月日時分秒）
	 */
	public int format;

	/**
	 * カラムタイトル用プロパティキー
	 */
	public String propKey;

	/**
	 *
	 * @param name　列名
	 * @param format　表示形式
	 * @param propKey　カラムタイトル用プロパティキー
	 */
	public DepositImportExcelDto(String name,int format, String propKey) {
		this.name = name;
		this.format = format;
		this.propKey = propKey;
	}
}
