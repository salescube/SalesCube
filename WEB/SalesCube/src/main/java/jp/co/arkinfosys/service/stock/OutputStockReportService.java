/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.NumberUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.stock.ProductStockJoinDto;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.join.ProductStockJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.ProductStockService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;

/**
 * 在庫残高表サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputStockReportService extends AbstractService<EadSlipTrn> {

	@Resource
	private ProductStockService productStockService;

	/**
	 * 年月度を指定して検索結果リストを返します.
	 * @param stockYm 年月度
	 * @return 検索結果リスト
	 * @throws ServiceException
	 */
	public List<ProductStockJoinDto> createProductStockDto(String stockYm)
			throws ServiceException {
		try {
			List<ProductStockJoin> resultList = productStockService.findProductStockByYm(stockYm);
			List<ProductStockJoinDto> dtoList = new ArrayList<ProductStockJoinDto>();

			for(ProductStockJoin productStockJoin : resultList) {
				ProductStockJoinDto productStockJoinDto =
					Beans.createAndCopy(ProductStockJoinDto.class, productStockJoin).excludes("allStockNum", "supplierPriceYen").execute();

				// 在庫数
				if(productStockJoin.allStockNum != null) {
					productStockJoin.allStockNum =
						productStockJoin.allStockNum.setScale(super.mineDto.numDecAlignment, NumberUtil.getRoundingMode(super.mineDto.productFractCategory));
					productStockJoinDto.allStockNum = productStockJoin.allStockNum.toString();
				}

				// 仕入単価
				if(productStockJoin.supplierPriceYen != null) {
					productStockJoin.supplierPriceYen =
						productStockJoin.supplierPriceYen.setScale(0, BigDecimal.ROUND_DOWN);
					productStockJoinDto.supplierPriceYen = productStockJoin.supplierPriceYen.toString();
				}

				// 在庫高
				BigDecimal stockPrice = null;
				if(productStockJoin.supplierPriceYen != null && productStockJoin.allStockNum != null) {
					stockPrice = productStockJoin.allStockNum;
					stockPrice = stockPrice.multiply(productStockJoin.supplierPriceYen);
					stockPrice = stockPrice.setScale(0, BigDecimal.ROUND_DOWN);
				}
				productStockJoinDto.stockPrice = StringUtil.valueOf(stockPrice);

				dtoList.add(productStockJoinDto);
			}

			return dtoList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
