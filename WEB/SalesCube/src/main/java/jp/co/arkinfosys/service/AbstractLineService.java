/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * 明細行の基底サービスクラスです.
 * @author Ark Information Systems
 *
 * @param <ENTITY>
 * @param <LINEDTOCLASS>
 * @param <SLIPDTOCLASS>
 */
public abstract class AbstractLineService<ENTITY, LINEDTOCLASS extends AbstractLineDto, SLIPDTOCLASS extends AbstractSlipDto<LINEDTOCLASS>>
		extends AbstractService<ENTITY> {

	/**
	 * 伝票情報を読み込みます.
	 * @param dto 伝票DTO
	 * @return 明細行DTOのリスト
	 * @throws ServiceException
	 */
	public abstract List<LINEDTOCLASS> loadBySlip(SLIPDTOCLASS dto)
			throws ServiceException;

	/**
	 * 明細行を保存します.
	 * @param slipDto 伝票DTO
	 * @param lineList 明細行DTOのリスト
	 * @param deletedLineIds 削除する明細行IDのカンマ区切り文字列
	 * @param abstractServices 処理内で使用するサービス
	 * @throws ServiceException
	 */
	public abstract void save(SLIPDTOCLASS slipDto, List<LINEDTOCLASS> lineList,
			String deletedLineIds,
			AbstractService<?>... abstractServices) throws ServiceException;

	/**
	 * 明細行を登録します.
	 * @param entity 明細行エンティティ
	 * @return 登録した件数
	 * @throws ServiceException
	 */
	protected abstract int insertRecord(ENTITY entity) throws ServiceException;

	/**
	 * 明細行を更新します.
	 * @param entity 明細行エンティティ
	 * @return 更新した件数
	 * @throws ServiceException
	 */
	protected abstract int updateRecord(ENTITY entity) throws ServiceException;

	/**
	 * 伝票IDを指定して明細行を削除します.
	 * @param id 伝票ID
	 * @return 削除した件数
	 * @throws ServiceException
	 */
	public abstract int deleteRecords(String id) throws ServiceException;

	/**
	 * 明細IDを指定して明細行を削除します.
	 * @param ids 明細IDの配列
	 * @return 削除件数
	 * @throws ServiceException
	 */
	protected abstract int deleteRecordsByLineId(String[] ids)
			throws ServiceException;

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
