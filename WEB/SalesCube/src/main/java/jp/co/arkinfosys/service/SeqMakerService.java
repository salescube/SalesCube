/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.sql.SQLException;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import jp.co.arkinfosys.entity.SeqMaker;
import jp.co.arkinfosys.service.exception.ServiceException;
/**
 * シーケンス番号サービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SeqMakerService extends AbstractService<SeqMaker> {
	/**
	 *
	 * パラメータ定義サービスクラスです.
	 *
	 */
	public static class Param {
		public static final String TABLE_NAME = "tableName";

		public static final String ID = "id";

		public static final String WARNING_ID = "warningId";
	}

	/**
	 * 指定されたテーブルに対する次のシーケンス番号を取得します.
	 *
	 * @param tableName テーブル名
	 * @return シーケンス番号
	 * @throws SQLException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long nextval(String tableName) throws ServiceException {
		try {
			// 発番テーブルの該当するレコードをロックする
			Map<String, Object> param = super.createSqlParam();
			param.put(SeqMakerService.Param.TABLE_NAME, tableName);
			SeqMaker seq = this.selectBySqlFile(SeqMaker.class,
					"seqmaker/LockSequence.sql", param).getSingleResult();

			// 発番処理
			if (seq != null) {
				// ロック成功
				param = super.createSqlParam();
				param.put(SeqMakerService.Param.TABLE_NAME, tableName);
				param.put(SeqMakerService.Param.ID, seq.id + 1);
				this.updateBySqlFile("seqmaker/UpdateSequence.sql", param)
						.execute();
				return seq.id + 1;
			}

			// レコードなし
			param = super.createSqlParam();
			param.put(SeqMakerService.Param.TABLE_NAME, tableName);
			param.put(SeqMakerService.Param.ID, 1);
			param.put(SeqMakerService.Param.WARNING_ID, 0);
			this.updateBySqlFile("seqmaker/InsertSequence.sql", param)
					.execute();
			return 1;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
