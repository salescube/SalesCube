/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.dto.deposit.BankDepositWorkDto;
import jp.co.arkinfosys.entity.BankDepositWork;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 銀行入金一時データサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class BankDepositWorkService extends AbstractService<BankDepositWork>{
	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String USER_ID = "userId";
	}

	/**
	 * ユーザIDを指定して銀行入金一時データを削除します.
	 *
	 * @param userId
	 * @throws ServiceException
	 */
	public void deleteByUserId(String userId) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param = super.createSqlParam();
			param.put(Param.USER_ID, userId);
			this.updateBySqlFile("bankdepositwork/DeleteBankDepositWorkByUserId.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 銀行入金一時データを登録します.
	 * @param bankDepositWorkDto 銀行入金一時データDTO
	 * @throws ServiceException
	 */
	public void insertRecord(BankDepositWorkDto bankDepositWorkDto) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			BeanMap bankDepositWorkMap = Beans.createAndCopy(BeanMap.class, bankDepositWorkDto).execute();
			param.putAll(bankDepositWorkMap);

			// データ登録
			this.updateBySqlFile("bankdepositwork/InsertBankDepositWork.sql", param).execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ユーザIDを指定して銀行入金一時データを取得します.
	 *
	 * @param userId ユーザID
	 * @return 銀行入金一時データDTOリスト
	 * @throws ServiceException
	 */
	public List<BankDepositWorkDto> findBankDepositWorkByUserId(String userId)
			throws ServiceException
	{
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.USER_ID, userId);

		return this.selectBySqlFile(BankDepositWorkDto.class,
				"bankdepositwork/FindBankDepositWorkByUserId.sql", param)
				.getResultList();
	}
}
