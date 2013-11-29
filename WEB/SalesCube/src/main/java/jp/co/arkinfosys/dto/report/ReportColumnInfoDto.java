/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.report;

/**
 * Excel出力用のカラム情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ReportColumnInfoDto {
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
	 * コンストラクタです.
	 * @param name カラム名
	 * @param format 表示形式
	 * @param propKey ラムタイトル用プロパティキー
	 */
	public ReportColumnInfoDto(String name,int format, String propKey) {
		this.name = name;
		this.format = format;
		this.propKey = propKey;
	}
}
