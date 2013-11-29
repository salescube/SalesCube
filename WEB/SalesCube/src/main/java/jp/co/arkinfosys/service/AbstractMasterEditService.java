/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

/**
 * マスタ情報編集の基底サービスクラスです.
 * @author Ark Information Systems
 *
 * @param <DTOCLASS>
 * @param <ENTITY>
 */
public abstract class AbstractMasterEditService<DTOCLASS, ENTITY> extends
		AbstractService<ENTITY> {

	/**
	 * マスタを登録します.
	 * @param dto マスタDTO
	 * @throws Exception
	 */
	public abstract void insertRecord(DTOCLASS dto) throws Exception;

	/**
	 * マスタを更新します.
	 * @param dto マスタDTO
	 * @throws Exception
	 */
	public abstract void updateRecord(DTOCLASS dto) throws Exception;

	/**
	 * マスタを削除します.
	 * @param dto マスタDTO
	 * @throws Exception
	 */
	public abstract void deleteRecord(DTOCLASS dto) throws Exception;

	/**
	 * 削除情報を更新します.
	 * @param value 削除キー
	 */
	public void updateAudit(String value) {
		super.updateAudit(this.getTableName(), this.getKeyColumnNames(),
				new String[] { value });
	}

	/**
	 * 削除情報を更新します.
	 * @param values 削除キーの配列
	 */
	public void updateAudit(String[] values) {
		super
				.updateAudit(this.getTableName(), this.getKeyColumnNames(),
						values);
	}

	/**
	 * テーブル名を返します.
	 * @return テーブル名
	 */
	protected abstract String getTableName();

	/**
	 * キーとなるカラム名を返します.
	 * @return カラム名の配列
	 */
	protected abstract String[] getKeyColumnNames();
}
