/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.dto.deposit.BankDepositRelDto;
import jp.co.arkinfosys.entity.BankDepositRel;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
*
* 銀行入金関連サービスクラスです.
* @author Ark Information Systems
*
*/
public class BankDepositRelService extends AbstractService<BankDepositRel>{
	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String LINE_NO = "lineNo";
		public static final String PAYMENT_DATE = "paymentDate";
		public static final String PAYMENT_NAME = "paymentName";
		public static final String PAYMENT_PRICE = "paymentPrice";
		public static final String DEPOSIT_SLIP_ID = "depositSlipId";

	}

	/**
	 * 銀行入金関連データを登録します.
	 * @param bankDepositRelDto 銀行入金関連データDTO
	 * @throws ServiceException
	 */
	public void insertRecord(BankDepositRelDto bankDepositRelDto) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			BeanMap bankDepositRelMap = Beans.createAndCopy(BeanMap.class, bankDepositRelDto).execute();
			param.putAll(bankDepositRelMap);

			// データ登録
			this.updateBySqlFile("bankdepositrel/InsertBankDepositRel.sql", param).execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 振込名義と振込日を指定して、銀行入金関連データを取得します.
	 * @param paymentName 振込名義
	 * @param paymentDate 振込日
	 * @param paymentPrice 振込金額
	 * @param lineNo 行番号
	 * @return 銀行入金関連データリスト
	 * @throws ServiceException
	 */
	public List<BankDepositRelDto> findByPaymentNameAndDate(String paymentName, String paymentDate, String paymentPrice, String lineNo)	throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.PAYMENT_NAME, paymentName);
			param.put(Param.PAYMENT_DATE, paymentDate);
			param.put(Param.PAYMENT_PRICE, paymentPrice);
			param.put(Param.LINE_NO, Integer.parseInt(lineNo));

			return this.selectBySqlFile(BankDepositRelDto.class,
					"bankdepositrel/FindByPaymentNameAndDate.sql", param)
					.getResultList();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 入金伝票番号を指定して、銀行入金関連レコードを削除します.
	 * @param depoditSlipId 削除対象入金伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteByDepositSlipId( String depoditSlipId ) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.DEPOSIT_SLIP_ID, depoditSlipId);

			return this.updateBySqlFile("bankdepositrel/DeleteBankDepositRel.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}