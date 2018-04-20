/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

/**
 * 区分マスタ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CategoryMstDto implements MasterEditDto {

	public String categoryId;

	public String name;
	/** 追加可能 */
	public boolean isInsert;

	/** 編集可能 */
	public boolean isUpdate;

	/** 削除可能 */
	public boolean isDelete;

	/** 全行削除可能 */
	public boolean isDeleteAll;

	/** 利用カラム */
	public String categoryDataType;

	/** 利用カラムタイトル */
	public String categoryTitle;

	/** 更新時間 */
	public String updDatetm;

	/** 追加フラグ */
	public String categoryAdd;

	/** 更新フラグ */
	public String categoryUpd;

	/** 削除フラグ */
	public String categoryDel;

	/**
	 * 区分ＩＤを取得します.
	 * @return　 区分ＩＤ
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.categoryId };
	}
}
