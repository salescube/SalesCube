/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.Map;

import jp.co.arkinfosys.dto.deposit.DeliveryDepositRelDto;
import jp.co.arkinfosys.entity.DeliveryDepositRel;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 配送業者入金・入金伝票関連情報サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DeliveryDepositRelService extends AbstractService<DeliveryDepositRel>{
	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String DEPOSIT_SLIP_ID = "depositSlipId";

	}

	/**
	 * 配送業者入金・入金伝票関連情報を登録します.
	 * @param deliveryDepositRelDto 配送業者入金・入金伝票関連DTO
	 * @throws ServiceException
	 */
	public void insertRecord(DeliveryDepositRelDto deliveryDepositRelDto) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			BeanMap deliveryDepositRelMap = Beans.createAndCopy(BeanMap.class, deliveryDepositRelDto).execute();
			param.putAll(deliveryDepositRelMap);

			// データ登録
			this.updateBySqlFile("deliverydepositrel/InsertDeliveryDepositRel.sql", param).execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 入金伝票IDを指定して、配送業者入金・入金伝票関連情報を削除します.
	 * @param depoditSlipId 入金伝票ID
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteByDepositSlipId( String depoditSlipId ) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.DEPOSIT_SLIP_ID, depoditSlipId);

			return this.updateBySqlFile("deliverydepositrel/DeleteDeliveryDepositRel.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


}


