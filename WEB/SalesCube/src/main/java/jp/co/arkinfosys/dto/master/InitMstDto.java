/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

import java.util.List;

import org.apache.struts.util.LabelValueBean;
/**
 * 初期値マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InitMstDto implements MasterEditDto {

	/** テーブル名 */
	public String tableName;

	/** カラム名 */
	public String columnName;

	/** 表示タイトル */
	public String title;

	/** 区分名 */
	public String categoryId;

	/** 利用カラム */
	public String useDataType;

	/** 文字サイズ */
	public Integer useStrSize;

	/** 文字 */
	public String strData;

	/** 数値 */
	public String numData;

	/** 小数値 */
	public String fltData;

	/** 備考 */
	public String remarks;

	/**
	 * 更新日時
	 */
	public String updDatetm;

	/**
	 * 更新ユーザー
	 */
	public String updUser;

	/**
	 * 区分データリスト
	 */
	public List<LabelValueBean> masterList;

	/**
	 * テーブル名とカラム名を取得します.
	 * @return　 文字列の配列　[テーブル名,カラム名]
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.tableName, this.columnName };
	}

}
