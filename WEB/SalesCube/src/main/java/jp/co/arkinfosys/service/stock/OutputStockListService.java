/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.stock.OutputStockListFormDto;
import jp.co.arkinfosys.dto.stock.ProductStockInfoDto;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.stock.OutputStockListForm;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;

/**
 * 在庫一覧表サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputStockListService extends AbstractService<EadSlipTrn> {

	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductService productService;

	@Resource
	private SupplierService supplierService;

	/**
	 * 在庫一覧表画面に設定された情報から商品在庫情報を取得して商品在庫DTOリストを作成します.
	 * @param outputStockListFormDto 在庫一覧表画面情報DTO
	 * @return 商品在庫DTOリスト
	 * @throws ServiceException
	 */
	public List<ProductStockInfoDto> createOutputStockListDtoList(OutputStockListFormDto outputStockListFormDto)
			throws ServiceException {
		try {
			// 該当商品の在庫情報の抽出と集計を行う
			Map<String, Object> conditions = new HashMap<String, Object>();

			// 期間条件
			conditions.put(ProductService.Param.AGGREGATE_MONTHS_RANGE, Integer.valueOf(outputStockListFormDto.periodMonth));

			// 除外条件の判定
			if(outputStockListFormDto.excludeRoNotExists) {
				conditions.put(ProductService.Param.RO_EXISTS, true);
			}
			if(outputStockListFormDto.excludeSalesCancel) {
				conditions.put(ProductService.Param.PRODUCT_STATUS_CATEGORY, CategoryTrns.PRODUCT_STATUS_ONSALE);
			}
			if(outputStockListFormDto.excludeNotManagementStock) {
				conditions.put(ProductService.Param.STOCK_CTL_CATEGORY, CategoryTrns.PRODUCT_STOCK_CTL_YES);
			}
			if(outputStockListFormDto.excludeMultiRack) {
				conditions.put(ProductService.Param.RACK_MULTI_FLAG, Constants.FLAG.OFF);
			}
			conditions.put(ProductService.Param.SET_TYPE_CATEGORY, CategoryTrns.PRODUCT_SET_TYPE_SINGLE);
			List<ProductStockInfoDto> dtoList = productService.aggregateProductStockInfoByCondition(conditions);

			if(!OutputStockListForm.RadioCond2.VALUE_0.equals(outputStockListFormDto.radioCond2)) {
				// 数量条件が「全て」以外の場合は条件による抽出
				Iterator<ProductStockInfoDto> ite = dtoList.iterator();
				while (ite.hasNext()) {
					ProductStockInfoDto dto = ite.next();

					if (OutputStockListForm.RadioCond2.VALUE_1
							.equals(outputStockListFormDto.radioCond2)) {
						// 保有数＜発注点
						if (!dto.isHoldingStockQuantityLessPoNum()) {
							ite.remove();
						}
					} else if (OutputStockListForm.RadioCond2.VALUE_2
							.equals(outputStockListFormDto.radioCond2)) {
						// 現在庫数＜発注点
						if (!dto.isCurrentStockQuantityLessPoNum()) {
							ite.remove();
						}
					} else if (OutputStockListForm.RadioCond2.VALUE_3
							.equals(outputStockListFormDto.radioCond2)) {
						// 引当可能数＜発注点（発注点がゼロのものは除く）
						if (!dto.isAvailableStockQuantityLessPoNum()) {
							ite.remove();
						}
					} else if (OutputStockListForm.RadioCond2.VALUE_4
							.equals(outputStockListFormDto.radioCond2)) {
						// 引当可能数＋発注残数＜発注点（発注点がゼロのものは除く）
						if (!dto
								.isAvailableStockQuantityAndRestQuantityPoLessPoNum()) {
							ite.remove();
						}
					} else if (OutputStockListForm.RadioCond2.VALUE_5
							.equals(outputStockListFormDto.radioCond2)) {
						// 引当可能数個以下
						if (!dto
								.isAvailableStockQuantityLess(Integer
										.parseInt(outputStockListFormDto.allocatedQuantity))) {
							ite.remove();
						}
					} else if (OutputStockListForm.RadioCond2.VALUE_6
							.equals(outputStockListFormDto.radioCond2)) {
						// 最大保有数超過品
						if (!dto.isOverMaxHoldingQuantity()) {
							ite.remove();
						}
					}
				}
			}

			// 外貨記号を設定
			for(ProductStockInfoDto dto : dtoList){
				//仕入先の外貨記号を取得
				SupplierJoin supRateJoin = supplierService.findSupplierRateByProductCode(dto.productCode);
				if(supRateJoin != null){
					dto.cUnitSign = supRateJoin.cUnitSign;
				}
			}

			return dtoList;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 棚種別リストを返します.
	 * @return 棚種別リスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> getRackCategoryList() throws ServiceException {
		return categoryService.findCategoryLabelValueBeanListById(Categories.RACK_CATEGORY);
	}

	/**
	 * カテゴリコードを受け取って、カテゴリ名を返します.
	 * @param categoryCode カテゴリコード
	 * @return カテゴリ名
	 * @throws ServiceException
	 */
	public String getRackCategoryName(String categoryCode)
			throws ServiceException {
		return categoryService.findCategoryNameByIdAndCode(Categories.RACK_CATEGORY, categoryCode);
	}
}
