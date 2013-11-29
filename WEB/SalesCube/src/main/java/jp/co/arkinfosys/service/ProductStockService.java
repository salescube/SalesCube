/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.entity.ProductStockTrn;
import jp.co.arkinfosys.entity.join.EntrustPorderRestDetail;
import jp.co.arkinfosys.entity.join.EntrustStockDetail;
import jp.co.arkinfosys.entity.join.PorderRestDetail;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.entity.join.ProductSetJoin;
import jp.co.arkinfosys.entity.join.ProductStockJoin;
import jp.co.arkinfosys.entity.join.RorderRestDetail;
import jp.co.arkinfosys.entity.join.StockQuantity;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 商品在庫サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ProductStockService extends AbstractService<ProductStockTrn> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String RACK_CODE = "rackCode";
		public static final String PRODUCT_CODE = "productCode";
		public static final String STOCK_PDATE = "stockPdate";
		public static final String STOCK_ANNUAL = "stockAnnual";
		public static final String STOCK_MONTHLY = "stockMonthly";
		public static final String STOCK_YM = "stockYm";
		public static final String STOCK_NUM = "stockNum";
		public static final String ENTER_NUM = "enterNum";
		public static final String DISPATCH_NUM = "dispatchNum";
		public static final String REMARKS = "remarks";

		public static final String RACK_CATEGORY = "rackCategory";
		public static final String SET_TYPE_CATEGORY = "setTypeCategory";
	}

	@Resource
	private EadService eadService;

	@Resource
	private PoSlipService poSlipService;

	@Resource
	private RoSlipService roSlipService;

	@Resource
	private ProductService productService;

	@Resource
	private ProductSetService productSetService;

	@Resource
	private YmService ymService;

	/**
	 * 商品コードと棚番コードを指定して、前回の在庫締時点での自社倉庫における商品在庫数を返します.
	 * @param productCode 商品コード
	 * @param rackCode 棚番コード
	 * @return 商品在庫数
	 * @throws ServiceException
	 */
	public int countClosedQuantityByProductCode(String productCode,
			String rackCode) throws ServiceException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ProductStockService.Param.PRODUCT_CODE,
				new String[] { productCode });
		param.put(ProductStockService.Param.RACK_CODE, rackCode);
		param.put(ProductStockService.Param.RACK_CATEGORY, null);

		List<StockQuantity> quantity = this
				.countClosedQuantityByProductCode(param);
		for (StockQuantity q : quantity) {
			if (q != null && q.quantity != null) {
				return q.quantity.intValue();
			}
		}
		return 0;
	}

	/**
	 * 商品コードを指定して、前回の在庫締時点での自社倉庫における商品在庫数を返します.
	 * @param productCode 商品コード
	 * @return 商品在庫数
	 * @throws ServiceException
	 */
	public int countClosedQuantityByProductCode(String productCode)
			throws ServiceException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ProductStockService.Param.PRODUCT_CODE,
				new String[] { productCode });
		param.put(ProductStockService.Param.RACK_CODE, null);
		param.put(ProductStockService.Param.RACK_CATEGORY,
				CategoryTrns.RACK_CATEGORY_OWN);

		List<StockQuantity> quantity = this
				.countClosedQuantityByProductCode(param);
		for (StockQuantity q : quantity) {
			if (q != null && q.quantity != null) {
				return q.quantity.intValue();
			}
		}
		return 0;
	}

	/**
	 * 商品コードを指定して、前回の在庫締時点での委託棚における商品在庫数を返します.
	 * @param productCode 商品コード
	 * @return 商品在庫数
	 * @throws ServiceException
	 */
	public int countClosedEntrustQuantityByProductCode(String productCode)
			throws ServiceException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ProductStockService.Param.PRODUCT_CODE,
				new String[] { productCode });
		param.put(ProductStockService.Param.RACK_CODE, null);
		param.put(ProductStockService.Param.RACK_CATEGORY,
				CategoryTrns.RACK_CATEGORY_ENTRUST);

		List<StockQuantity> quantity = this
				.countClosedQuantityByProductCode(param);
		for (StockQuantity q : quantity) {
			if (q != null && q.quantity != null) {
				return q.quantity.intValue();
			}
		}
		return 0;
	}

	/**
	 * 商品コードを指定して、前回の在庫締時点での自社倉庫における商品在庫情報のリストを返します.
	 * @param productCode 商品コード
	 * @return 商品在庫情報{@link StockQuantity}のリスト
	 * @throws ServiceException
	 */
	public List<StockQuantity> countClosedQuantityByProductCode(
			String[] productCode) throws ServiceException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ProductStockService.Param.PRODUCT_CODE, productCode);
		param.put(ProductStockService.Param.RACK_CODE, null);
		param.put(ProductStockService.Param.RACK_CATEGORY,
				CategoryTrns.RACK_CATEGORY_OWN);

		return this.countClosedQuantityByProductCode(param);
	}

	/**
	 * 検索条件を指定して、商品在庫情報のリストを返します.
	 * @param conditions 検索条件のマップ
	 * @return 商品在庫情報{@link StockQuantity}のリスト
	 * @throws ServiceException
	 */
	private List<StockQuantity> countClosedQuantityByProductCode(
			Map<String, Object> conditions) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();

			// 棚区分コード
			if (conditions.containsKey(ProductStockService.Param.RACK_CATEGORY)) {
				params.put(ProductStockService.Param.RACK_CATEGORY, conditions
						.get(ProductStockService.Param.RACK_CATEGORY));
			}

			// 棚コード
			if (conditions.containsKey(ProductStockService.Param.RACK_CODE)) {
				params.put(ProductStockService.Param.RACK_CODE, conditions
						.get(ProductStockService.Param.RACK_CODE));
			}

			// 商品コード
			if (conditions.containsKey(ProductStockService.Param.PRODUCT_CODE)) {
				params.put(ProductStockService.Param.PRODUCT_CODE, conditions
						.get(ProductStockService.Param.PRODUCT_CODE));
			}

			return this
					.selectBySqlFile(
							StockQuantity.class,
							"productstock/CountClosedQuantityByProductCode.sql",
							params).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードを指定して、商品の在庫数情報を返します.
	 * @param productCode 商品コード
	 * @return 在庫数情報{@link StockInfoDto}
	 * @throws ServiceException
	 */
	public StockInfoDto calcStockQuantityByProductCode(String productCode)
			throws ServiceException {
		StockInfoDto result = new StockInfoDto();
		result.setQuantityFormatOptions(super.mineDto.productFractCategory, super.mineDto.numDecAlignment);
		if (!StringUtil.hasLength(productCode)) {
			return result;
		}

		try {
			// 商品情報を取得する
			ProductJoin product = this.productService
					.findById(productCode);
			if (product == null) {
				result.productCode = productCode;
				return result;
			}
			Beans.copy(product, result).execute();

			// 単品
			if (CategoryTrns.PRODUCT_SET_TYPE_SINGLE
					.equals(product.setTypeCategory)) {

				// 自社倉庫への当月入出庫分
				int currentMonthStock = this.eadService
						.countUnclosedQuantityByProductCode(productCode);

				// 自社倉庫の前回締め時点での在庫数
				int prevMonthStock = this
						.countClosedQuantityByProductCode(productCode);

				// 現在庫総数
				result.currentTotalQuantity = currentMonthStock
						+ prevMonthStock;

				// 委託在庫数
				result.entrustStockQuantity = this.eadService
						.countEntrustQuantityByProductCode(productCode);

				// 受注残数
				result.rorderRestQuantity = this.roSlipService
						.countRestQuantityByProductCode(productCode);

				// 発注残数
				result.porderRestQuantity = this.poSlipService
						.countRestQuantityByProductCode(productCode, false);

				// 船便発注残数
				result.porderRestQuantityShip = this.poSlipService
						.countRestQuantityByProductCode(productCode,
								CategoryTrns.TRANSPORT_CATEGORY_SHIP);

				// AIR便発注残数
				result.porderRestQuantityAir = this.poSlipService
						.countRestQuantityByProductCode(productCode,
								CategoryTrns.TRANSPORT_CATEGORY_AIR);

				// 宅配便発注残数
				result.porderRestQuantityDerivary = this.poSlipService
						.countRestQuantityByProductCode(productCode,
								CategoryTrns.TRANSPORT_CATEGORY_DELIVERY);

				// 委託残数
				result.entrustRestQuantity = this.poSlipService
						.countRestQuantityByProductCode(productCode, true);

				// 引当可能数
				result.possibleDrawQuantity = result.currentTotalQuantity
						- result.rorderRestQuantity;
			}
			// セット品
			else if (CategoryTrns.PRODUCT_SET_TYPE_SET
					.equals(product.setTypeCategory)) {
				// セット商品の内訳商品を全て取得する
				List<ProductSetJoin> productSetList = this.productSetService
						.findProductSetByProductCode(productCode);
				if (productSetList != null && productSetList.size() > 0) {

					String[] productCodeArray = new String[productSetList
							.size()];
					for (int i = 0; i < productSetList.size(); i++) {
						productCodeArray[i] = productSetList.get(i).productCode;
					}

					// 各商品の自社倉庫への当月入出庫分
					List<StockQuantity> stockQuantityList = this.eadService
							.countUnclosedQuantityByProductCode(productCodeArray);
					Map<String, BigDecimal> productQuantityMap = new HashMap<String, BigDecimal>();
					for (StockQuantity stockQuantity : stockQuantityList) {
						if (stockQuantity.quantity != null) {
							productQuantityMap.put(stockQuantity.productCode,
									stockQuantity.quantity);
						}
					}

					// 各商品の自社倉庫の前回締め時点での在庫数
					stockQuantityList = this
							.countClosedQuantityByProductCode(productCodeArray);
					for (StockQuantity stockQuantity : stockQuantityList) {
						if (stockQuantity.quantity != null) {
							BigDecimal dec = productQuantityMap
									.get(stockQuantity.productCode);
							if (dec != null) {
								productQuantityMap.put(
										stockQuantity.productCode, dec.add(
												stockQuantity.quantity,
												MathContext.UNLIMITED));
								continue;
							}
							productQuantityMap.put(stockQuantity.productCode,
									stockQuantity.quantity);
						}
					}

					// 在庫数量をセット数で割った場合に、最も小さい値となる商品の値を採用
					BigDecimal minQuantity = null;
					for (ProductSetJoin productSet : productSetList) {
						if (productQuantityMap
								.containsKey(productSet.productCode)) {
							BigDecimal temp = productQuantityMap.get(
									productSet.productCode).divide(
									productSet.quantity, RoundingMode.DOWN);
							if (minQuantity == null) {
								minQuantity = temp;
								continue;
							}

							if (minQuantity.compareTo(temp) > 0) {
								minQuantity = temp;
							}
							continue;
						}
						// 数量が存在しない商品があった時点で終了（セット品としての在庫が不成立）
						minQuantity = new BigDecimal(0);
						break;
					}

					// 現在庫総数
					result.currentTotalQuantity = minQuantity.intValue();


					// 子商品の受注残数を加味して引当可能数を算出する
					Iterator<Entry<String, BigDecimal>> entryIterator = productQuantityMap
							.entrySet().iterator();
					while (entryIterator.hasNext()) {
						Entry<String, BigDecimal> entry = entryIterator.next();
						String key = entry.getKey();
						// 受注残数
						int rest = this.roSlipService
								.countRestQuantityByProductCode(key);
						if (rest != 0) {
							productQuantityMap.put(key, entry.getValue()
									.subtract(new BigDecimal(rest)));
						}
					}

					// 在庫数量をセット数で割った場合に、最も小さい値となる商品の値を採用
					minQuantity = null;
					for (ProductSetJoin productSet : productSetList) {
						if (productQuantityMap
								.containsKey(productSet.productCode)) {
							BigDecimal temp = productQuantityMap.get(
									productSet.productCode).divide(
									productSet.quantity, RoundingMode.DOWN);
							if (minQuantity == null) {
								minQuantity = temp;
								continue;
							}

							if (minQuantity.compareTo(temp) > 0) {
								minQuantity = temp;
							}
							continue;
						}
						// 数量が存在しない商品があった時点で終了（セット品としての在庫が不成立）
						minQuantity = new BigDecimal(0);
						break;
					}

					// 引当可能数
					result.possibleDrawQuantity = minQuantity.intValue();
				}

				// 受注残数
				result.rorderRestQuantity = this.roSlipService
						.countRestQuantityByProductCode(productCode);
			}

			// 保有数
			result.holdingStockQuantity = result.currentTotalQuantity
					+ result.entrustStockQuantity + result.porderRestQuantity
					+ result.entrustRestQuantity - result.rorderRestQuantity;

			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 年月を指定して、商品在庫情報のリストを返します.
	 * @param stockYm 年月(YYYY/MM)
	 * @return 商品在庫情報{@link ProductStockJoin}のリスト
	 * @throws ServiceException
	 */
	public List<ProductStockJoin> findProductStockByYm(String stockYm)
			throws ServiceException {
		try {
			YmDto ymDto = ymService.getYm(stockYm + "/01");
			if (ymDto == null) {
				ServiceException se = new ServiceException(MessageResourcesUtil
						.getMessage("errors.system"));
				se.setStopOnError(true);
				throw se;
			}

			Map<String, Object> param = super.createSqlParam();
			param.put(Param.STOCK_YM, ymDto.ym);
			param.put(Param.SET_TYPE_CATEGORY,
					CategoryTrns.PRODUCT_SET_TYPE_SET);

			return this.selectBySqlFile(ProductStockJoin.class,
					"productstock/FindProductStockByYm.sql", param)
					.getResultList();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 商品コードおよび年月を指定して、指定年月の在庫締時点での商品在庫数を返します.
	 * @param stockYm 年月(YYYY/MM)
	 * @param productCode 商品コード
	 * @param rackCategory 棚カテゴリ
	 * @return 商品在庫数
	 * @throws ServiceException
	 */
	public Integer countClosedQuantityByCodeAndYm(String stockYm,
			String productCode, String rackCategory) throws ServiceException {
		try {
			YmDto ymDto = ymService.getYm(stockYm + "/01");
			if (ymDto == null) {
				ServiceException se = new ServiceException(MessageResourcesUtil
						.getMessage("errors.system"));
				se.setStopOnError(true);
				throw se;
			}

			Map<String, Object> param = super.createSqlParam();
			param.put(ProductStockService.Param.PRODUCT_CODE, productCode);
			param.put(ProductStockService.Param.RACK_CATEGORY, rackCategory);
			param.put(Param.STOCK_YM, ymDto.ym);

			Integer result = this.selectBySqlFile(Integer.class,
					"productstock/CountClosedQuantityByCodeAndYm.sql", param)
					.getSingleResult();
			if (result == null) {
				result = 0;
			}

			return result;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}


	/**
	 * 最終締日を返します.
	 * @return 最終締日
	 * @throws ServiceException
	 */
	public Date findMaxStockPDateDate() throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();

			return this.selectBySqlFile(Date.class,
					"productstock/FindMaxStockPDateDate.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品在庫情報を登録します.
	 * @param productStockTrn 商品在庫情報
	 * @return 登録した件数
	 * @throws ServiceException
	 */
	public int insertProductStock(ProductStockTrn productStockTrn)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createSqlParam(productStockTrn);
			return this.updateBySqlFile("productstock/InsertProductStock.sql",
					param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 商品在庫情報を削除します.
	 * @param productStockTrn 商品在庫情報
	 * @return 削除した件数
	 * @throws ServiceException
	 */
	public int deleteProductStockByCode(ProductStockTrn productStockTrn)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.RACK_CODE, productStockTrn.rackCode);
			param.put(Param.PRODUCT_CODE, productStockTrn.productCode);
			param.put(Param.STOCK_ANNUAL, productStockTrn.stockAnnual);
			param.put(Param.STOCK_MONTHLY, productStockTrn.stockMonthly);

			super
					.updateAudit(ProductStockTrn.TABLE_NAME, new String[] {
							Param.RACK_CODE, Param.PRODUCT_CODE,
							Param.STOCK_ANNUAL, Param.STOCK_MONTHLY },
							new Object[] { productStockTrn.rackCode,
									productStockTrn.productCode,
									productStockTrn.stockAnnual,
									productStockTrn.stockMonthly });
			return this.updateBySqlFile(
					"productstock/DeleteProductStockByCode.sql", param)
					.execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * SQLパラメータマップを作成します.
	 * @param productStockTrn 商品在庫情報
	 * @return パラメータマップ
	 */
	protected Map<String, Object> createSqlParam(ProductStockTrn productStockTrn) {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.RACK_CODE, productStockTrn.rackCode);
		param.put(Param.PRODUCT_CODE, productStockTrn.productCode);
		param.put(Param.STOCK_PDATE, productStockTrn.stockPdate);
		param.put(Param.STOCK_ANNUAL, productStockTrn.stockAnnual);
		param.put(Param.STOCK_MONTHLY, productStockTrn.stockMonthly);
		param.put(Param.STOCK_YM, productStockTrn.stockYm);
		param.put(Param.STOCK_NUM, productStockTrn.stockNum);
		param.put(Param.ENTER_NUM, productStockTrn.enterNum);
		param.put(Param.DISPATCH_NUM, productStockTrn.dispatchNum);
		param.put(Param.REMARKS, productStockTrn.remarks);
		return param;
	}

	/**
	 * 商品コード、棚番コードおよび在庫締処理日を指定して、商品在庫情報のリストを返します.
	 * @param rackCode 棚番コード
	 * @param productCode 商品コード
	 * @param stockPdate 在庫締処理日
	 * @return　、商品在庫情報{@link ProductStockTrn}のリスト
	 * @throws ServiceException
	 */
	public List<ProductStockTrn> findProductStockByCodeAndPdate(
			String rackCode, String productCode, String stockPdate)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			if (StringUtil.hasLength(rackCode)) {
				param.put(Param.RACK_CODE, rackCode);
			}
			if (StringUtil.hasLength(productCode)) {
				param.put(Param.PRODUCT_CODE, productCode);
			}
			if (StringUtil.hasLength(stockPdate)) {
				param.put(Param.STOCK_PDATE, stockPdate);
			}

			return this.selectBySqlFile(ProductStockTrn.class,
					"productstock/FindProductStockByCodeAndPdate.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードを指定して、受注残明細情報のリストを返します.
	 * @param productCode 商品コード
	 * @return 受注残明細情報{@link RorderRestDetail}のリスト
	 * @throws ServiceException
	 */
	public List<RorderRestDetail> findRorderRestDetailByProductCode(String productCode) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			if (StringUtil.hasLength(productCode)) {
				param.put(Param.PRODUCT_CODE, productCode);
			}

			// 指定商品コードの全親品番を取得する
			List<ProductSetJoin> setProductList = productSetService.findProductSetByChildProductCode(productCode);

			// 親品番を設定
			List<String> setProductCode = null;
			Iterator<ProductSetJoin> it = setProductList.iterator();
			while (it.hasNext()) {
				if (setProductCode==null) {
					setProductCode = new ArrayList<String>();
				}
				ProductSetJoin psj = it.next();
				setProductCode.add(psj.setProductCode);
			}
			param.put(ProductSetService.Param.SET_PRODUCT_CODE, setProductCode);

			return this.selectBySqlFile(RorderRestDetail.class,
					"productstock/FindRorderRestDetailByProductCode.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードを指定して、発注残明細情報のリストを返します.
	 * @param productCode 商品コード
	 * @return 発注残明細情報{@link PorderRestDetail}のリスト
	 * @throws ServiceException
	 */
	public List<PorderRestDetail> findPorderRestDetailByProductCode(String productCode) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			if (StringUtil.hasLength(productCode)) {
				param.put(Param.PRODUCT_CODE, productCode);
			}

			return this.selectBySqlFile(PorderRestDetail.class,
					"productstock/FindPorderRestDetailByProductCode.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードを指定して、委託発注残明細情報のリストを返します.
	 * @param productCode 商品コード
	 * @return 委託発注残明細情報{@link EntrustPorderRestDetail}のリスト
	 * @throws ServiceException
	 */
	public List<EntrustPorderRestDetail> findEntrustPorderRestDetailByProductCode(String productCode) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			if (StringUtil.hasLength(productCode)) {
				param.put(Param.PRODUCT_CODE, productCode);
			}

			return this.selectBySqlFile(EntrustPorderRestDetail.class,
					"productstock/FindEntrustPorderRestDetailByProductCode.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードを指定して、委託在庫明細情報のリストを返します.
	 * @param productCode 商品コード
	 * @return 委託在庫明細情報{@link EntrustStockDetail}のリスト
	 * @throws ServiceException
	 */
	public List<EntrustStockDetail> findEntrustStockDetailByProductCode(String productCode) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			if (StringUtil.hasLength(productCode)) {
				param.put(Param.PRODUCT_CODE, productCode);
			}

			return this.selectBySqlFile(EntrustStockDetail.class,
					"productstock/FindEntrustStockDetailByProductCode.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 在庫締情報が存在する商品の最終締状態の在庫情報のリストを返します.
	 * @return 在庫情報{@link ProductStockTrn}のリスト
	 * @throws ServiceException
	 */
	public List<ProductStockTrn> findLastProductStockTrn()
			throws ServiceException {
		try {
			return this.selectBySqlFile(ProductStockTrn.class,
					"productstock/FindLastProductStockTrn.sql", super.createSqlParam()).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
