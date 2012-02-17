/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service.stock;

import java.math.BigDecimal;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.util.MessageResourcesUtil;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.ProductStockTrn;
import jp.co.arkinfosys.entity.join.EadSlipLineJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.ProductStockService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

/**
 * 在庫締処理サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class CloseStockService extends AbstractService<EadSlipTrn> {

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String RACK_CODE = "rackCode";
		public static final String PRODUCT_CODE = "productCode";
	}

	@Resource
	private EadService eadService;

	@Resource
	private ProductStockService productStockService;

	@Resource
	private YmService ymService;

	/**
	 * 締日の最大値を取得します.
	 * @return 最終締処理日
	 * @throws ServiceException
	 */
	public Date findMaxStockPDateDate()
			throws ServiceException {
		return productStockService.findMaxStockPDateDate();
	}

	/**
	 * 在庫締処理を実行します.
	 * @param cutoffDate 締処理日
	 * @throws ServiceException
	 */
	public Integer close(String cutoffDate)
			throws ServiceException {
		try {
			
			List<BeanMap> eadRackAndProductList =
				eadService.findRackAndProductByStockPdate(cutoffDate, null);

			
			List<ProductStockTrn> lastProductStockList =
				productStockService.findLastProductStockTrn();

			
			Map<String, ProductStockTrn> lastProductStockMap = new HashMap<String, ProductStockTrn>();
			for(ProductStockTrn lastProductStock : lastProductStockList) {
				lastProductStockMap.put(lastProductStock.rackCode.toUpperCase() + lastProductStock.productCode.toUpperCase(), lastProductStock);
			}

			
			Map<Integer, EadSlipTrn> slipMap = new HashMap<Integer, EadSlipTrn>();
			for(BeanMap rackAndProduct : eadRackAndProductList) {
				ProductStockTrn lastProductStock = null;

				
				String rackCode = (String) rackAndProduct.get(Param.RACK_CODE);
				String productCode = (String) rackAndProduct.get(Param.PRODUCT_CODE);

				
				String key = rackCode.toUpperCase() + productCode.toUpperCase();
				if(lastProductStockMap.containsKey(key)) {
					lastProductStock = lastProductStockMap.get(key);
					lastProductStockMap.remove(key);
				}

				Map<Integer, EadSlipTrn> result = createProductStock(rackCode, productCode, lastProductStock, cutoffDate);
				for(EadSlipTrn eadSlipTrn : result.values()) {
					slipMap.put(eadSlipTrn.eadSlipId, eadSlipTrn);
				}
			}

			
			Set<String> keySet = lastProductStockMap.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				ProductStockTrn lastProductStock = lastProductStockMap.get(key);
				
				if (lastProductStock.stockNum.compareTo(BigDecimal.ZERO)!=0) {
					String rackCode = lastProductStock.rackCode;
					String productCode = lastProductStock.productCode;
					Map<Integer, EadSlipTrn> result = createProductStock(rackCode, productCode, lastProductStock, cutoffDate);
					for(EadSlipTrn eadSlipTrn : result.values()) {
						slipMap.put(eadSlipTrn.eadSlipId, eadSlipTrn);
					}
				}
			}

			
			for(EadSlipTrn eadSlipTrn : slipMap.values()) {
				
				Map<String, Object> param = super.createSqlParam();
				param.put(EadService.Param.EAD_SLIP_ID, eadSlipTrn.eadSlipId);
				lockRecordBySqlFile("ead/LockSlipByEadSlipId.sql",
						param, eadSlipTrn.updDatetm);
				
				eadService.updateSlipStockPdateByEadSlipId(
						cutoffDate, eadSlipTrn.eadSlipId);
			}

			return slipMap.values()==null?0:slipMap.values().size();
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 在庫締処理を解除します.
	 * @throws ServiceException
	 */
	public void reopen()
			throws ServiceException, UnabledLockException {
		try {
			
			String lastCutoffDate = StringUtil.getDateString(Constants.FORMAT.DATE, findMaxStockPDateDate());
			if(!StringUtil.hasLength(lastCutoffDate)) {
				lastCutoffDate = null;
			}

			
			List<ProductStockTrn> productStockList =
				productStockService.findProductStockByCodeAndPdate(null, null, lastCutoffDate);
			for(ProductStockTrn productStockTrn : productStockList) {
				
				Map<String, Object> param = super.createSqlParam();
				param.put(ProductStockService.Param.RACK_CODE, productStockTrn.rackCode);
				param.put(ProductStockService.Param.PRODUCT_CODE, productStockTrn.productCode);
				param.put(ProductStockService.Param.STOCK_ANNUAL, productStockTrn.stockAnnual);
				param.put(ProductStockService.Param.STOCK_MONTHLY, productStockTrn.stockMonthly);
				lockRecordBySqlFile("productstock/LockProductStockByCode.sql",
						param, productStockTrn.updDatetm);
				
				productStockService.deleteProductStockByCode(productStockTrn);
			}

			
			List<EadSlipTrn> eadSlipList =
				eadService.findEadSlipByCodeAndPdate(null, null, null, lastCutoffDate);
			for(EadSlipTrn eadSlipTrn : eadSlipList) {
				
				Map<String, Object> param = super.createSqlParam();
				param.put(EadService.Param.EAD_SLIP_ID, eadSlipTrn.eadSlipId);
				lockRecordBySqlFile("ead/LockSlipByEadSlipId.sql",
						param, eadSlipTrn.updDatetm);
				
				eadService.updateSlipStockPdateByEadSlipId(null, eadSlipTrn.eadSlipId);
			}
		} catch(ServiceException e) {
			throw e;
		} catch (UnabledLockException e) {
			throw e;
		} catch(Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 在庫締処理を行い、商品在庫情報を登録します.
	 * @param rackCode 棚番
	 * @param productCode 商品コード
	 * @param lastProductStock 最終締処理日
	 * @param cutoffDate 締処理日
	 * @return 処理した伝票情報
	 * @throws ServiceException
	 */
	public Map<Integer, EadSlipTrn> createProductStock(String rackCode, String productCode,
			ProductStockTrn lastProductStock, String cutoffDate) throws ServiceException {
		try {
			
			Map<Integer, EadSlipTrn> result = new HashMap<Integer, EadSlipTrn>();

			BigDecimal lastStockNum = BigDecimal.ZERO;
			if(lastProductStock != null) {
				lastStockNum = lastProductStock.stockNum;
			}

			
			List<EadSlipLineJoin> productQuantityList =
				eadService.countUnclosedQuantityByCodeAndPdate(rackCode, productCode, cutoffDate);
			BigDecimal enterQuantity = BigDecimal.ZERO;
			BigDecimal dispatchQuantity = BigDecimal.ZERO;
			for(EadSlipLineJoin productQuantity : productQuantityList) {
				
				if(CategoryTrns.EAD_CATEGORY_ENTER.equals(productQuantity.eadCategory)) {
					
					enterQuantity = productQuantity.quantity;
				} else if(CategoryTrns.EAD_CATEGORY_DISPATCH.equals(productQuantity.eadCategory)) {
					
					dispatchQuantity = productQuantity.quantity;
				}
				
				List<EadSlipTrn> eadSlipList = eadService.findEadSlipByCodeAndPdate(
							productQuantity.rackCode, productQuantity.productCode, cutoffDate, null);
				for(EadSlipTrn eadSlipTrn : eadSlipList) {
					
					result.put(eadSlipTrn.eadSlipId, eadSlipTrn);
				}
			}

			
			YmDto ymDto = ymService.getYm(cutoffDate);
			if(ymDto == null) {
				ServiceException se = new ServiceException(
						MessageResourcesUtil.getMessage("errors.system"));
				se.setStopOnError(true);
				throw se;
			}

			
			ProductStockTrn productStockTrn = new ProductStockTrn();
			productStockTrn.rackCode = rackCode;
			productStockTrn.productCode = productCode;
			DateFormat foramt = new SimpleDateFormat(Constants.FORMAT.DATE);
			productStockTrn.stockPdate = super.convertUtilDateToSqlDate(foramt.parse(cutoffDate));
			productStockTrn.stockAnnual = ymDto.annual.shortValue();
			productStockTrn.stockMonthly = ymDto.monthly.shortValue();
			productStockTrn.stockYm = ymDto.ym;
			productStockTrn.stockNum = lastStockNum.add(enterQuantity.subtract(dispatchQuantity));
			productStockTrn.enterNum = enterQuantity;
			productStockTrn.dispatchNum = dispatchQuantity;
			productStockTrn.remarks = "";

			
			productStockService.insertProductStock(productStockTrn);

			return result;
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 年度、月度、年月度を返します.
	 * @param inputDate 日付(java.util.Date)
	 * @return 年度、月度、年月度（{@link YmDto}）
	 * @throws ServiceException
	 */
	public YmDto getYm(Date inputDate) throws ServiceException {
		return ymService.getYm(inputDate);
	}
}
