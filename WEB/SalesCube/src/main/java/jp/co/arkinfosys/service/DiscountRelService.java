/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.Discount;
import jp.co.arkinfosys.entity.DiscountRel;
import jp.co.arkinfosys.entity.join.DiscountRelJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

/**
 * 数量割引関連サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DiscountRelService extends AbstractService<DiscountRelJoin> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param extends AbstractService.Param {
		public static final String DISCOUNT_ID = "discountId";
		private static final String PRODUCT_CODE = "productCode"; // 商品コード
		private static final String QUANTITY = "quantity"; // 数量
		private static final String ROW_COUNT = "rowCount"; // 取得件数
		private static final String OFFSET_ROW = "offsetRow"; // 取得件数

	}

	public String[] params = { Param.PRODUCT_CODE, Param.QUANTITY,
			Param.ROW_COUNT, Param.OFFSET_ROW };

	/**
	 * 商品コードおよび数量を指定して、割引情報を返します.
	 * @param productCode 商品コード
	 * @param quantity 数量
	 * @return 割引情報{@link DiscountRelJoin}
	 * @throws ServiceException
	 */
	public DiscountRelJoin findDiscountTrnByProductAndQuantity(
			String productCode, String quantity) throws ServiceException {

		if (!StringUtil.hasLength(productCode)) {
			return null;
		}
		if (!StringUtil.hasLength(quantity)) {
			return null;
		}
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 商品コードが一致
		// 数量が範囲内で１件取得
		conditions.put(Param.PRODUCT_CODE, productCode);
		conditions.put(Param.QUANTITY, quantity);

		List<DiscountRelJoin> listRel = findByCondition(conditions, params,
				"discount/FindDiscountByProductQ.sql");
		if (listRel.size() != 1) {
			return null;
		}
		return listRel.get(0);
	}

	/**
	 * 商品コードを指定して、割引マスタ情報を返します.
	 * @param productCode 商品コード
	 * @return 割引マスタ情報{@link Discount}
	 * @throws ServiceException
	 */
	public Discount findDiscountMstByProduct(String productCode)
			throws ServiceException {

		if (!StringUtil.hasLength(productCode)) {
			return null;
		}
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 商品コードが一致
		// 数量が範囲内で１件取得
		conditions.put(Param.PRODUCT_CODE, productCode);

		List<DiscountRelJoin> listRel = findByCondition(conditions, params,
				"discount/FindDiscountMstByProductCode.sql");
		if (listRel.size() != 1) {
			return null;
		}
		return listRel.get(0);
	}

	/**
	 * 割引情報を登録します.
	 * @param productCode 商品コード
	 * @param discountId 割引ID
	 * @return 登録した件数
	 * @throws ServiceException
	 */
	public int insertDiscountRel(String productCode, String discountId)
			throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();
			params.put(Param.DISCOUNT_ID, discountId);
			params.put(Param.PRODUCT_CODE, productCode);

			int result = super.updateBySqlFile(
					"discount/InsertDiscountRel.sql", params).execute();
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 割引情報を更新します.
	 * @param productCode 商品コード
	 * @param discountId 割引ID
	 * @param updDatetm 更新日時の文字列
	 * @return 更新した件数
	 * @throws ServiceException
	 */
	public int updateDiscountRel(String productCode, String discountId,
			String updDatetm) throws ServiceException {
		try {
			// 排他制御
			Map<String, Object> params = super.createSqlParam();
			params.put(Param.PRODUCT_CODE, productCode);
			params.put(Param.DISCOUNT_ID, null);
			super.lockRecordBySqlFile(
					"discount/LockDiscountRelByDiscountIdAndProductCode.sql",
					params, updDatetm);

			params.put(Param.DISCOUNT_ID, discountId);
			int result = super.updateBySqlFile(
					"discount/UpdateDiscountRel.sql", params).execute();
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 数量割引リレーション情報を削除する
	 *
	 * @param productCode
	 * @param discountId
	 *            数量割引IDを指定する、nullが指定された場合は削除対象を商品に紐づく全数量割引とする
	 * @return
	 * @throws ServiceException
	 */
	/**
	 * 割引情報を削除します.
	 * @param productCode 商品コード
	 * @param discountId 割引ID (null指定時は商品コードに紐づく全割引が対象)
	 * @param updDatetm 更新日時の文字列
	 * @return 削除した件数
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public int deleteDiscountRel(String productCode, String discountId,
			String updDatetm) throws ServiceException, UnabledLockException {
		try {
			// 排他制御
			Map<String, Object> params = super.createSqlParam();
			params.put(Param.PRODUCT_CODE, productCode);
			params.put(Param.DISCOUNT_ID, discountId);
			super.lockRecordBySqlFile(
					"discount/LockDiscountRelByDiscountIdAndProductCode.sql",
					params, updDatetm);

			super.updateAudit(DiscountRel.TABLE_NAME, new String[] {
					Param.DISCOUNT_ID, Param.PRODUCT_CODE }, new Object[] {
					discountId, productCode });

			return super.updateBySqlFile(
					"discount/DeleteDiscountRelByDiscountIdAndProductCode.sql",
					params).execute();
		} catch (UnabledLockException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * まとめ買い値引きによる商品単価を返します.<br>
	 * 値引かない場合には、引数として渡される商品単価を返します.
	 * @param quantity 数量
	 * @param unitRetailPrice 商品単価
	 * @param productCode 商品コード
	 * @return 割引適応後の商品単価
	 */
	public Double getBulkDiscountUnitPrice(Double quantity,
			Double unitRetailPrice, String productCode) {
		// 割引額
		if (!StringUtil.hasLength(productCode)) {
			return unitRetailPrice;
		}
		// 数量割引対象チェック
		// 割引マスタに対象データがあるか確認
		DiscountRelJoin discountRelJoin;
		try {
			discountRelJoin = findDiscountTrnByProductAndQuantity(productCode,
					quantity.toString());
		} catch (ServiceException e) {
			e.printStackTrace();
			// 無かったら最初の単価を返す
			return unitRetailPrice;
		}
		if (discountRelJoin != null) {
			// あったら、割り引いた単価を返す
			Double rate = discountRelJoin.discountRate.doubleValue();
			Double discountUnitRetailPrice = unitRetailPrice
					- (unitRetailPrice * rate / 100.0); // レートは％値が入っている
			// 単価は円単位で切り捨てる
			BigDecimal bd = new BigDecimal(discountUnitRetailPrice);
			// 単価は円単位で切り上げる　2010.05.17 update kaki
			return bd.setScale(0, BigDecimal.ROUND_UP).doubleValue();
		} else {
			// 無かったら最初の単価を返す
			return unitRetailPrice;
		}
	}

	/**
	 * まとめ買い値引きの対象か否かを判断します.
	 * @param quantity 数量
	 * @param unitRetailPrice 商品単価
	 * @param productCode 商品コード
	 * @return まとめ買い値引きの対象か否か
	 */
	public Boolean isBulkDiscountUnit(Double quantity, Double unitRetailPrice,
			String productCode) {
		// 割引額
		if (!StringUtil.hasLength(productCode)) {
			return false;
		}
		// 数量割引対象チェック
		// 割引マスタに対象データがあるか確認
		DiscountRelJoin discountRelJoin;
		try {
			discountRelJoin = findDiscountTrnByProductAndQuantity(productCode,
					quantity.toString());
		} catch (ServiceException e) {
			e.printStackTrace();
			// 無かったら最初の単価を返す
			return false;
		}
		if (discountRelJoin != null) {
			// あったら、割引率を取得し、0より大きい場合は、対象
			Double rate = discountRelJoin.discountRate.doubleValue();
			if (Double.compare(rate, 0) > 0) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 商品コードを指定して、数量割引商品かどうか判断します。
	 * @param productCode 商品コード
	 * @return 数量割引商品かどうか
	 * @throws ServiceException
	 */
	public Boolean isDiscountMstByProduct(String productCode)
			throws ServiceException {

		if (!StringUtil.hasLength(productCode)) {
			return false;
		}
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 商品コードが一致
		// 数量が範囲内で１件取得
		conditions.put(Param.PRODUCT_CODE, productCode);

		List<DiscountRelJoin> listRel = findByCondition(conditions, params,
				"discount/FindDiscountMstByProductCode.sql");
		if (listRel.size() != 1) {
			return false;
		}
		return true;
	}

}
