/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

/**
 * 伝票の基底サービスクラスです.
 * @author Ark Information Systems
 *
 * @param <ENTITY>
 * @param <DTOCLASS>
 */
public abstract class AbstractSlipService<ENTITY, DTOCLASS> extends
		AbstractService<ENTITY> {

	/**
	 * 伝票を読み込みます.
	 * @param id 伝票ID
	 * @return 伝票DTO
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public abstract DTOCLASS loadBySlipId(String id) throws ServiceException,
			UnabledLockException;

	/**
	 * 伝票を保存します.
	 * @param dto 伝票DTO
	 * @param abstractServices 処理内で使用するサービス
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public abstract int save(DTOCLASS dto,
			AbstractService<?>... abstractServices)
			throws ServiceException, UnabledLockException;

	/**
	 * 伝票を登録します.
	 * @param dto 伝票DTO
	 * @return 登録件数
	 * @throws ServiceException
	 */
	protected abstract int insertRecord(DTOCLASS dto) throws ServiceException;

	/**
	 * 伝票を更新します.
	 * @param dto 伝票DTO
	 * @return 更新件数
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	protected abstract int updateRecord(DTOCLASS dto)
			throws UnabledLockException, ServiceException;

	/**
	 * 伝票を削除します.
	 * @param id 伝票ID
	 * @param updDatetm 更新日時の文字列
	 * @return 削除件数
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public abstract int deleteById(String id, String updDatetm)
			throws ServiceException, UnabledLockException;

	/**
	 * レコードをロックします.
	 * @param scrID キーに使用するJava変数名
	 * @param keyValue 伝票ID
	 * @param updDatetm ロック可否判断に使用する日時の文字列
	 * @param lockSQLFileName 使用するSQLファイル名
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	protected int lockRecord(String scrID, Object keyValue, String updDatetm,
			String lockSQLFileName) throws ServiceException,
			UnabledLockException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.TIMESTAMP);
		Date d;
		try {
			d = sdf.parse(updDatetm);
			Timestamp t = new Timestamp(d.getTime());
			return this.lockRecord(scrID, keyValue, t, lockSQLFileName);
		} catch (ParseException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * レコードをロックします.
	 * @param scrID キーに使用するJava変数名
	 * @param keyValue 伝票ID
	 * @param updDatetm ロック可否判断に使用する日時
	 * @param lockSQLFileName 使用するSQLファイル名
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	protected int lockRecord(String scrID, Object keyValue,
			Timestamp updDatetm, String lockSQLFileName)
			throws ServiceException, UnabledLockException {
		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(scrID, keyValue);
		int lockResult = LockResult.SUCCEEDED;
		lockResult = lockRecordBySqlFile(lockSQLFileName, lockParam, updDatetm);
		return lockResult;
	}

	/**
	 * 削除情報を更新します.
	 * @param value 削除キー
	 */
	public void updateAudit(String value) {
		super.updateAudit(this.getTableName(), new String[] { this
				.getKeyColumnName() }, new String[] { value });
	}

	/**
	 * テーブル名を返します.
	 * @return テーブル名
	 */
	protected abstract String getTableName();

	/**
	 * キーとなるカラム名を返します.
	 * @return カラム名
	 */
	protected abstract String getKeyColumnName();
}
