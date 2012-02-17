/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.entity.PoLineStatusCnt;
import jp.co.arkinfosys.entity.PoLineTrn;
import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.entity.join.PoLineTrnJoin;
import jp.co.arkinfosys.entity.join.PoSlipTrnJoin;
import jp.co.arkinfosys.entity.join.StockQuantity;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.porder.InputPOrderSlipService;

import org.seasar.framework.beans.util.BeanMap;

/**
 * 発注伝票サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class PoSlipService extends AbstractService<PoSlipTrn> {
	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		private static final String PRODUCT_CODE = "productCode";



		private static final String TRANSPORT_CATEGORY = "transportCategory";

		private static final String REST_QUANTITY = "restQuantity";

		private static final String PO_DATE = "poDate";

		private static final String PO_LINE_ID = "poLineId";

		public static final String PO_SLIP_ID = "poSlipId";

		public static final String PO_LINE_STATUS = "poLineStatusCategory";

		
		public static final String COND_ORDERED = "condOrderd"; 
		public static final String COND_ENTRUST_MAKED = "condEntrustMaked"; 
		public static final String COND_ENTRUST_DELIVERED = "condEntrustDelivered"; 
		public static final String COND_NOWPURCHASING = "condNowPurchasing"; 
		public static final String COND_PURCHASED = "condPurchased"; 

	}

	@Resource
	SeqMakerService seqMakerService;

	/**
	 * 発注伝票明細行を更新します.<br>
	 * (ステータスと残数量を更新します)
	 * @param poLineId 発注伝票明細行ID
	 * @param status ステータス
	 * @param restQuantity 残数量
	 * @throws ServiceException
	 */
	public void updatePOrderLineTrnStatusByPoLineId( String poLineId, String status, String restQuantity) throws ServiceException {
		Map<String, Object> param = null;

		
		param = super.createSqlParam();
		param.put(SupplierSlipService.Param.PO_LINE_ID, poLineId);
		param.put(SupplierSlipService.Param.STATUS, status);
		param.put(SupplierSlipService.Param.REST_QUANTITY, restQuantity);
		this.updateBySqlFile(
				"porder/UpdatePOrderLineTrnStatusByPoLineId.sql",
				param).execute();
	}

	/**
	 * 発注伝票明細行を更新します.<br>
	 * (ステータスを更新します)
	 * @param poLineId 発注伝票明細行ID
	 * @param status ステータス
	 * @throws ServiceException
	 */
	public void updatePOrderLineTrnStatusByPoLineId( String poLineId, String status) throws ServiceException {
		Map<String, Object> param = null;

		
		param = super.createSqlParam();
		param.put(SupplierSlipService.Param.PO_LINE_ID, poLineId);
		param.put(SupplierSlipService.Param.STATUS, status);
		this.updateBySqlFile(
				"porder/UpdatePOrderLineTrnStatusByPoLineId2.sql",
				param).execute();
	}

	/**
	 * DBに登録済みの全ての発注明細のステータスを調べ、それらの状態から発注伝票のステータスを更新します.
	 * @param poSlipId 発注伝票番号
	 * @throws ServiceException
	 */
	public void updatePOrderTrnStatusByPoSlipId( String poSlipId ) throws ServiceException {
		Map<String, Object> param = super.createSqlParam();

		
		
		
		param.put(PoSlipService.Param.PO_SLIP_ID, poSlipId);

		
		param.put(Param.COND_ORDERED,
				Constants.STATUS_PORDER_LINE.ORDERED);
		param.put(Param.COND_ENTRUST_MAKED,
				Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_MAKED);
		param.put(Param.COND_ENTRUST_DELIVERED,
				Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_DELIVERED);
		param.put(Param.COND_NOWPURCHASING,
				Constants.STATUS_PORDER_LINE.NOWPURCHASING);
		param.put(Param.COND_PURCHASED,
				Constants.STATUS_PORDER_LINE.PURCHASED);

		
		PoLineStatusCnt statusCnt = this.selectBySqlFile(PoLineStatusCnt.class,
				"porder/CountPoLineTrnStatusByPOLineId.sql", param)
				.getSingleResult();

		String slipStatus = Constants.STATUS_PORDER_SLIP.ORDERED;
		int allCnt = 0;
		int orderdCnt = 0; 
		int entrustStockMakedCnt = 0; 
		int entrustStockDeliveredCnt = 0; 
		int nowpurchasedCnt = 0; 
		int purchasedCnt = 0; 

		if (statusCnt != null) {
			if (statusCnt.allCnt != null) {
				allCnt = statusCnt.allCnt.intValue();
			}
			if (statusCnt.orderdCnt != null) {
				orderdCnt = statusCnt.orderdCnt.intValue();
			}
			if (statusCnt.entrustStockMakedCnt != null) {
				entrustStockMakedCnt = statusCnt.entrustStockMakedCnt.intValue();
			}
			if (statusCnt.entrustStockDeliveredCnt != null) {
				entrustStockDeliveredCnt = statusCnt.entrustStockDeliveredCnt.intValue();
			}
			if (statusCnt.nowpurchasedCnt != null) {
				nowpurchasedCnt = statusCnt.nowpurchasedCnt.intValue();
			}
			if (statusCnt.purchasedCnt != null) {
				purchasedCnt = statusCnt.purchasedCnt.intValue();
			}
		}
		if (allCnt > 0) {
			if (allCnt == purchasedCnt) {
				
				slipStatus = Constants.STATUS_PORDER_SLIP.PURCHASED;
			} else if (allCnt == orderdCnt) {
				
				slipStatus = Constants.STATUS_PORDER_SLIP.ORDERED;
			} else if (purchasedCnt > 0 || nowpurchasedCnt > 0) {
				
				slipStatus = Constants.STATUS_PORDER_SLIP.NOWPURCHASING;
			} else if ( entrustStockMakedCnt > 0 || entrustStockDeliveredCnt > 0 ) {
				
				slipStatus = Constants.STATUS_PORDER_SLIP.NOW_ENTRUST_STOCK_MAKING;
			}
		}

		param.put(SupplierSlipService.Param.STATUS, slipStatus);

		
		this.updateBySqlFile(
				"porder/UpdatePOrderTrnStatusByPoSlipId.sql", param)
				.execute();
	}

	/**
	 * 商品コードと委託か否かを指定して、発注残数を取得します.
	 *
	 * @param productCode 商品コード
	 * @param entrust 委託か否か
	 * @return 発注残数
	 * @throws ServiceException
	 */
	public int countRestQuantityByProductCode(String productCode,
			boolean entrust) throws ServiceException {
		try {
			List<String> transportCategories = new ArrayList<String>();
			if (entrust) {
				
				transportCategories.add(CategoryTrns.TRANSPORT_CATEGORY_ENTRUST);
			} else {
				
				transportCategories.add(CategoryTrns.TRANSPORT_CATEGORY_AIR);
				transportCategories.add(CategoryTrns.TRANSPORT_CATEGORY_SHIP);
				transportCategories.add(CategoryTrns.TRANSPORT_CATEGORY_DELIVERY);
			}
			return countRestQuantityByProductCode(productCode, transportCategories);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードと運送便区分を指定して、発注残数を取得します.
	 *
	 * @param productCode 商品コード
	 * @param transportCategories 運送便区分リスト
	 * @return 発注残数
	 * @throws ServiceException
	 */
	public int countRestQuantityByProductCode(String productCode,
			List<String> transportCategories) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();
			
			params.put(PoSlipService.Param.PRODUCT_CODE, productCode);

			
			params.put(PoSlipService.Param.TRANSPORT_CATEGORY,
					transportCategories);

			StockQuantity result = this.selectBySqlFile(StockQuantity.class,
					"porder/CountRestQuantityByProductCode.sql", params)
					.getSingleResult();

			if (result != null && result.quantity != null) {
				return result.quantity.intValue();
			}
			return 0;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードと運送便区分を指定して、発注残数を取得します.
	 *
	 * @param productCode 商品コード
	 * @param transportCategory 運送便区分
	 * @return 発注残数
	 * @throws ServiceException
	 */
	public int countRestQuantityByProductCode(String productCode,
			String transportCategory) throws ServiceException {
		try {
			List<String> transportCategories = new ArrayList<String>();
			transportCategories.add(transportCategory);
			return countRestQuantityByProductCode(productCode, transportCategories);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 未納の発注伝票のうち最短の納期を取得します.
	 * @param productCode 商品コード
	 * @param poDate 発注日
	 * @return 最短の納期
	 * @throws ServiceException
	 */
	public Date findMinDeliveryDateByProductCodeAndPoDate(
			String productCode, String poDate) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();

			
			params.put(Param.PRODUCT_CODE, productCode);
			
			params.put(Param.PO_DATE, poDate);
			
			params.put(Param.REST_QUANTITY, 0);

			return this.selectBySqlFile(Date.class,
					"porder/FindMinDeliveryDateByProductCodeAndPoDate.sql", params)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	/**
	 * 仕入伝票行IDを指定して、発注残数を取得します.
	 *
	 * @param poLineId 仕入伝票行ID
	 * @return 発注残数
	 * @throws ServiceException
	 */
	public BigDecimal getRestQuantityByPoLineId(String poLineId) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();
			
			params.put(Param.PO_LINE_ID, poLineId);

			StockQuantity result = this.selectBySqlFile(StockQuantity.class,
					"porder/GetRestQuantityByPoLineId.sql", params)
					.getSingleResult();
			if (result != null && result.quantity != null) {
				return result.quantity;
			}
			return new BigDecimal(0);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 仕入伝票行IDを指定して、発注伝票明細行情報を取得します.
	 *
	 * @param poLineId 仕入伝票行ID
	 * @return 発注伝票明細行エンティティ
	 * @throws ServiceException
	 */
	public PoLineTrn getPOLineTrnByPoLineId(String poLineId) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();
			
			params.put(Param.PO_LINE_ID, poLineId);

			return  this.selectBySqlFile(PoLineTrn.class,
					"porder/FindPOrderLineByPOLineId.sql", params)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 発注伝票番号を指定して、発注伝票情報を取得します.
	 * @param poSlipId 発注伝票番号
	 * @return 発注伝票情報
	 * @throws ServiceException
	 */
	public PoSlipTrnJoin findPoSlipByPoSlipId(String poSlipId) throws ServiceException {
		PoSlipTrnJoin poSlipTrnSingle = null;

		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(InputPOrderSlipService.Param.PO_SLIP_ID, poSlipId);

			poSlipTrnSingle = this.selectBySqlFile(PoSlipTrnJoin.class,
					"porder/FindPOrderSlipByPOSlipId.sql", param)
					.getSingleResult();

			return poSlipTrnSingle;
		} catch (Exception e) {
			
			throw new ServiceException(e);
		}
	}

	/**
	 * 発注伝票番号を指定して、発注伝票情報を取得します.
	 * @param poSlipId 発注伝票番号
	 * @return 発注伝票情報
	 * @throws ServiceException
	 */
	public PoSlipTrn loadPOSlip(String poSlipId){
		PoSlipTrn poSlipTrnSingle = null;

		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(PoSlipService.Param.PO_SLIP_ID, poSlipId);

			poSlipTrnSingle = this.selectBySqlFile(PoSlipTrn.class,
					"porder/FindPOrderSlipByPOSlipId.sql", param).getSingleResult();
		} catch (Exception e) {
			return poSlipTrnSingle;
			
		}
		return poSlipTrnSingle;
	}

	/**
	 * 発注伝票番号を指定して、発注伝票明細行情報を取得します.
	 * @param poSlipId 発注伝票番号
	 * @return 発注伝票明細行情報リスト
	 * @throws ServiceException
	 */
	public List<PoLineTrnJoin> loadPOLine(String poSlipId) throws ServiceException{

		List<PoLineTrnJoin> poLineTrnList = null;
		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(PoSlipService.Param.PO_SLIP_ID, poSlipId);

			param.put(PoSlipService.Param.PO_LINE_STATUS, SlipStatusCategories.PO_LINE_STATUS);

			poLineTrnList = selectBySqlFile(PoLineTrnJoin.class,
					"porder/FindPOrderLineByPOSlipId.sql", param).getResultList();

		} catch (Exception e) {
			return poLineTrnList;
			
		}
		return poLineTrnList;
	}

	/**
	 * 商品コードを指定して、仕入予定を取得します.
	 * @param productCode 商品コード
	 * @return 仕入予定リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findPurchaseSchedule(String productCode) throws ServiceException{
		try {
			
			Map<String, Object> param = super.createSqlParam();
			param.put(PoSlipService.Param.PRODUCT_CODE, productCode);

			return this.selectBySqlFile(BeanMap.class,
					"porder/FindPurchaseSchedule.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
