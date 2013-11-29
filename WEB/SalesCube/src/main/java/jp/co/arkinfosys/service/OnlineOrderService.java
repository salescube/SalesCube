/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.rorder.OnlineOrderWorkDto;
import jp.co.arkinfosys.entity.OnlineOrderWork;
import jp.co.arkinfosys.entity.join.OnlineOrderRelJoin;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * オンライン受注テーブルを扱うサービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OnlineOrderService extends AbstractService<OnlineOrderWork> {
	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String USER_ID = "userId";
		public static final String ONLINE_ORDER_ID = "onlineOrderId";
		public static final String ONLINE_ITEM_ID = "onlineItemId";
		public static final String SUPPLIER_DATE = "supplierDate";
		public static final String PAYMENT_DATE = "paymentDate";
		public static final String CUSTOMER_EMAIL = "customerEmail";
		public static final String CUSTOMER_NAME = "customerName";
		public static final String CUSTOMER_TEL = "customerTel";
		public static final String SKU = "sku";
		public static final String PRODUCT_NAME = "productName";
		public static final String QUANTITY = "quantity";
		public static final String CURRENCY = "currency";
		public static final String PRICE = "price";
		public static final String TAX_PRICE = "taxPrice";
		public static final String SHIPPING_PRICE = "shippingPrice";
		public static final String SHIPPING_TAX = "shippingTax";
		public static final String SHIP_SERVICE_LEVEL = "shipServiceLevel";
		public static final String RECIPIENT_NAME = "recipientName";
		public static final String ADDRESS_1 = "address1";
		public static final String ADDRESS_2 = "address2";
		public static final String ADDRESS_3 = "address3";
		public static final String CITY = "city";
		public static final String STATE = "state";
		public static final String ZIP_CODE = "zipCode";
		public static final String COUNTRY = "country";
		public static final String SHIP_TEL = "shipTel";
		public static final String DELIVERY_START_DATE = "deliveryStartDate";
		public static final String DELIVERY_END_DATE = "deliveryEndDate";
		public static final String DELIVERY_TIME_ZONE = "deliveryTimeZone";
		public static final String DELIVERY_INST = "deliveryInst";
		public static final String LINE_NO = "lineNo";

		public static final String STATUS = "status";

		public static final String RO_SLIP_ID = "roSlipId";
		public static final String RO_LINE_ID = "roLineId";

		public static final String LIMIT_ROW = "limitRow";
		public static final String OFFSET_ROW = "offsetRow";

		public static final String SORT_ORDER = "sortOrder";
		public static final String SORT_COLUMN = "sortColumn";
		public static final String LOAD_DATE = "loadDate";
	}

	/**
	 *
	 * カラム定義クラスです.
	 *
	 */
	public static class Column {
		public static final String RO_SLIP_ID = "RO_SLIP_ID";
		public static final String ONLINE_ORDER_ID = "ONLINE_ORDER_ID";
		public static final String SUPPLIER_DATE = "SUPPLIER_DATE";
		public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
		public static final String LOAD_DATE = "LOAD_DATE";
	}

	/**
	 * 受注伝票番号を指定して、オンライン受注データを取得します.
	 * @param onlineOrderId 受注伝票番号
	 * @return オンライン受注
	 * @throws ServiceException
	 */
	public List<OnlineOrderWork> findOnlineOrderWorkByRoId(String onlineOrderId) throws ServiceException {
		if (!StringUtil.hasLength(onlineOrderId)) {
			return new ArrayList<OnlineOrderWork>();
		}

		Map<String, Object> param = super.createSqlParam();

		// 検索条件
		param.put(Param.ONLINE_ORDER_ID, onlineOrderId);

		List<OnlineOrderWork> result = this.selectBySqlFile(OnlineOrderWork.class,
				"onlineorder/FindOnlineOrderWorkByRoIdAndUserId.sql", param).getResultList();
		if (result == null) {
			return new ArrayList<OnlineOrderWork>();
		}
		return result;
	}

	/**
	 * オンライン受注データ・オンライン受注関連データを取得します.
	 * @param limitRow 取得件数(LIMIT)
	 * @param offsetRow 取得開始位置(OFFSET)
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return オンライン受注データ・オンライン受注関連データエンティティのリスト
	 * @throws ServiceException
	 */
	public List<OnlineOrderRelJoin> findRoWorkRel(
			String limitRow, String offsetRow, String sortColumn, boolean sortOrderAsc)
			throws ServiceException {
		return findOnlineOrderWorkRelByUserId(null, limitRow, offsetRow, sortColumn, sortOrderAsc);
	}

	/**
	 * オンライン受注データ・オンライン受注関連データの件数を取得します.
	 * @return 件数
	 * @throws ServiceException
	 */
	public Integer findRoWorkRelCnt()
		throws ServiceException {
		return findRoWorkRelCntByUserId(null);
	}

	/**
	 * ユーザIDを指定して、オンライン受注データ・オンライン受注関連データの情報を取得します.<br>
	 * （受注ID、仕入日付、得意先名、受注伝票番号でGroupByされた結果が返されます）
	 *
	 * @param userId ユーザID
	 * @param limitRow 取得件数(LIMIT)
	 * @param offsetRow 取得開始位置(OFFSET)
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return オンライン受注データ・オンライン受注関連データエンティティのリスト
	 * @throws ServiceException
	 */
	public List<OnlineOrderRelJoin> findOnlineOrderWorkRelByUserId(
			String userId, String limitRow, String offsetRow, String sortColumn, boolean sortOrderAsc)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			// ユーザIDの制限を外す
			if( userId != null ){
				param.put(Param.USER_ID, userId);
			}

			// ソートカラムを設定する
			if (Param.STATUS.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN, Column.RO_SLIP_ID);
			} else if (Param.RO_SLIP_ID.equals(sortColumn)) {
					param.put(Param.SORT_COLUMN, Column.RO_SLIP_ID);
			} else if (Param.ONLINE_ORDER_ID.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN, Column.ONLINE_ORDER_ID);
			} else if (Param.SUPPLIER_DATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN, Column.SUPPLIER_DATE);
			} else if (Param.CUSTOMER_NAME.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN, Column.CUSTOMER_NAME);
			} else if (Param.LOAD_DATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN, Column.LOAD_DATE);
			}

			// ソートオーダーを設定する
			if (sortOrderAsc) {
				param.put(Param.SORT_ORDER, Constants.SQL.ASC);
			} else {
				param.put(Param.SORT_ORDER, Constants.SQL.DESC);
			}

			// リミットを設定する
			if(StringUtil.hasLength(limitRow)
					&& StringUtil.hasLength(offsetRow)) {
				param.put(Param.LIMIT_ROW, limitRow);
				param.put(Param.OFFSET_ROW, offsetRow);
			}

			return this.selectBySqlFile(OnlineOrderRelJoin.class,
					"onlineorder/FindOnlineOrderWorkRelByUserId.sql", param).getResultList();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * ユーザIDを指定して、オンライン受注データ・オンライン受注関連データの件数を取得します.<br>
	 * （受注ID、仕入日付、得意先名、受注伝票番号でGroupByされた結果が返されます）
	 * @param userId ユーザID
	 * @return 注文情報件数
	 * @throws ServiceException
	 */
	public Integer findRoWorkRelCntByUserId(String userId)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			if( userId != null ){
				param.put(Param.USER_ID, userId);
			}

			return this.selectBySqlFile(Integer.class,
					"onlineorder/FindOnlineOrderWorkRelCntByUserId.sql", param).getSingleResult();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * オンライン受注データを登録します.
	 * @param onlineOrderWorkDto オンライン受注データDTO
	 * @return 登録件数
	 * @throws ServiceException
	 */
	public int insertWork(OnlineOrderWorkDto onlineOrderWorkDto) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createWorkSqlParam(onlineOrderWorkDto);
			return this.updateBySqlFile("onlineorder/InsertOnlineOrderWork.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * オンライン受注データのSQLパラメータを作成します.
	 * @param onlineOrderWorkDto オンライン受注データDTO
	 * @return 設定済みパラメータ
	 */
	protected Map<String, Object> createWorkSqlParam(OnlineOrderWorkDto onlineOrderWorkDto) {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.USER_ID, onlineOrderWorkDto.userId);
		param.put(Param.ONLINE_ORDER_ID, onlineOrderWorkDto.onlineOrderId);
		param.put(Param.ONLINE_ITEM_ID, onlineOrderWorkDto.onlineItemId);
		param.put(Param.SUPPLIER_DATE, onlineOrderWorkDto.supplierDate);
		param.put(Param.PAYMENT_DATE, onlineOrderWorkDto.paymentDate);
		param.put(Param.CUSTOMER_EMAIL, onlineOrderWorkDto.customerEmail);
		param.put(Param.CUSTOMER_NAME, onlineOrderWorkDto.customerName);
		param.put(Param.CUSTOMER_TEL, onlineOrderWorkDto.customerTel);
		param.put(Param.SKU, onlineOrderWorkDto.sku);
		param.put(Param.PRODUCT_NAME, onlineOrderWorkDto.productName);
		param.put(Param.QUANTITY, onlineOrderWorkDto.quantity);
		param.put(Param.CURRENCY, onlineOrderWorkDto.currency);
		param.put(Param.PRICE, onlineOrderWorkDto.price);
		param.put(Param.TAX_PRICE, onlineOrderWorkDto.taxPrice);
		param.put(Param.SHIPPING_PRICE, onlineOrderWorkDto.shippingPrice);
		param.put(Param.SHIPPING_TAX, onlineOrderWorkDto.shippingTax);
		param.put(Param.SHIP_SERVICE_LEVEL, onlineOrderWorkDto.shipServiceLevel);
		param.put(Param.RECIPIENT_NAME, onlineOrderWorkDto.recipientName);
		param.put(Param.ADDRESS_1, onlineOrderWorkDto.address1);
		param.put(Param.ADDRESS_2, onlineOrderWorkDto.address2);
		param.put(Param.ADDRESS_3, onlineOrderWorkDto.address3);
		param.put(Param.CITY, onlineOrderWorkDto.city);
		param.put(Param.STATE, onlineOrderWorkDto.state);
		param.put(Param.ZIP_CODE, onlineOrderWorkDto.zipCode);
		param.put(Param.COUNTRY, onlineOrderWorkDto.country);
		param.put(Param.SHIP_TEL, onlineOrderWorkDto.shipTel);
		param.put(Param.DELIVERY_START_DATE, onlineOrderWorkDto.deliveryStartDate);
		param.put(Param.DELIVERY_END_DATE, onlineOrderWorkDto.deliveryEndDate);
		param.put(Param.DELIVERY_TIME_ZONE, onlineOrderWorkDto.deliveryTimeZone);
		param.put(Param.DELIVERY_INST, onlineOrderWorkDto.deliveryInst);
		param.put(Param.LINE_NO, onlineOrderWorkDto.lineNo);
		return param;
	}

	/**
	 * オンライン受注をすべて削除します.
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteWorksAll() throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			return this.updateBySqlFile("onlineorder/DeleteOnlineOrderWork.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 受注伝票番号に紐付くオンライン受注をすべて削除します.
	 * @param roId 受注伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteWorksByRoId(String roId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.ONLINE_ORDER_ID, roId);
			return this.updateBySqlFile("onlineorder/DeleteOnlineOrderWork.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

}
