/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.dto.ShipDepositWorkDto;
import jp.co.arkinfosys.entity.DeliveryDepositWork;
import jp.co.arkinfosys.entity.join.DeliveryDepositWorkJoin;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 配送業者入金のサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DeliveryDepositWorkService extends AbstractService<DeliveryDepositWork>{
	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String USER_ID = "userId";
		public static final String PAYMENT_CATEGORY = "paymentCategory";
		public static final String CUSTOMER_CODE = "customerCode";
		public static final String DELIVERY_SLIP_ID = "deliverySlipId";
		public static final String DATA_CATEGORY = "dataCategory";
		public static final String CHANGE_COUNT = "changeCount";
		public static final String SERVICE_CATEGORY = "serviceCategory";
		public static final String SETTLE_CATEGORY = "settleCategory";
		public static final String DELIVERY_DATE = "deliveryDate";
		public static final String PRODUCT_PRICE = "productPrice";
		public static final String COD_PRICE = "productPrice";
		public static final String SERVICE_PRICE = "codPrice";
		public static final String SPLIT_PRICE = "servicePrice";
		public static final String STAMP_PRICE = "splitPrice";
		public static final String RG_DATE = "rgDate";
		public static final String RG_SLIP_ID = "rgSlipId";
	}

	/**
	 * ユーザIDを指定して、配送業者入金情報を削除します.
	 * @param userId ユーザID
	 * @throws ServiceException
	 */
	public void deleteByUserId(String userId) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param = super.createSqlParam();
			param.put(UserService.Param.USER_ID, userId);
			this.updateBySqlFile("deliverydepositwork/DeleteDeliveryDepositWorkByUserId.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 配送業者入金情報を登録します.
	 * @param shipDepositWorkDto 配送業者入金DTO
	 * @throws ServiceException
	 */
	public void insertRecord(ShipDepositWorkDto shipDepositWorkDto) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			BeanMap shipDepositWorkMap = Beans.createAndCopy(BeanMap.class, shipDepositWorkDto).execute();
			param.putAll(shipDepositWorkMap);

			// データ登録
			this.updateBySqlFile("deliverydepositwork/InsertDeliveryDepositWork.sql", param).execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ユーザIDを指定して、配送業者入金情報のリストを返します.
	 * @param userId ユーザID
	 * @return 配送業者入金情報{@link DeliveryDepositWorkJoin}のリスト
	 * @throws ServiceException
	 */
	public List<DeliveryDepositWorkJoin> findDeliveryDepositWorkByUserId(String userId)
			throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(UserService.Param.USER_ID, userId);

		return this.selectBySqlFile(DeliveryDepositWorkJoin.class,
				"deliverydepositwork/FindDeliveryDepositWorkByUserId.sql", param)
				.getResultList();
	}

	/**
	 * 伝票IDを指定して、配送業者入金情報を返します.
	 * @param slipId 伝票ID
	 * @return 配送業者入金情報のリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findDeliveryDepositWorkBySlipId(String slipId)
			throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.DELIVERY_SLIP_ID, slipId);

		List<BeanMap> InfoBoxList = this.selectBySqlFile(BeanMap.class,
				"deliverydepositwork/FindDeliveryDepositWorkBySlipId.sql", param)
				.getResultList();
		return InfoBoxList;
	}
}

