/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.dto.ShipWorkDto;
import jp.co.arkinfosys.entity.InvoiceDataWork;
import jp.co.arkinfosys.entity.join.InvoiceDataWorkJoin;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 送り状データサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InvoiceDataWorkService extends AbstractService<InvoiceDataWork>{
	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String USER_ID = "userId";
		public static final String DELIVERY_SLIP_ID = "deliverySlipId";
	}

	/**
	 * ユーザIDを指定して、送り状データを削除します.
	 * @param userId ユーザID
	 * @throws ServiceException
	 */
	public void deleteByUserId(String userId) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, userId);
			this.updateBySqlFile("invoicedatawork/DeleteInvoiceDataWorkByUserId.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 送り状データを追加します.
	 * @param shipWorkDto 送り状データDTO
	 * @throws ServiceException
	 */
	public void insertRecord(ShipWorkDto shipWorkDto) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			BeanMap shipWorkDtoMap = Beans.createAndCopy(BeanMap.class, shipWorkDto).execute();
			param.putAll(shipWorkDtoMap);

			// データ登録
			this.updateBySqlFile("invoicedatawork/InsertInvoiceDataWork.sql", param).execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ユーザIDを指定して、送り状情報のリストを返します.
	 * @param userId ユーザID
	 * @return 送り状情報{@link InvoiceDataWorkJoin}のリスト
	 * @throws ServiceException
	 */
	public List<InvoiceDataWorkJoin> findInvoiceDataWorkByUserId(String userId)
			throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(UserService.Param.USER_ID, userId);

		return this.selectBySqlFile(InvoiceDataWorkJoin.class,
				"invoicedatawork/FindInvoiceDataWorkByUserId.sql", param)
				.getResultList();
	}

	/**
	 * 伝票IDを指定して、送り状情報のマップを返します.
	 * @param deliverySlipId 伝票ID
	 * @return 送り状情報のマップ
	 * @throws ServiceException
	 */
	public List<BeanMap> findInvoiceDataWorkBySlipId(String deliverySlipId)
			throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.DELIVERY_SLIP_ID, deliverySlipId);

		return this.selectBySqlFile(BeanMap.class, "invoicedatawork/FindInvoiceDataWorkBySlipId.sql", param).getResultList();
	}}
