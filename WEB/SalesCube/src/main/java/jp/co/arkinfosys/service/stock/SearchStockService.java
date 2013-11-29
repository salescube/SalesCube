/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.dto.stock.EadSlipLineJoinDto;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.join.EadSlipLineJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.ProductClassService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 入出庫検索サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchStockService extends AbstractService<EadSlipTrn> {
	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String SEARCH_TARGET = "searchTarget";

		public static final String OFFSET_ROW = "offsetRow";

		public static final String ROW_COUNT = "rowCount";
	}

    @Resource
    private CategoryService categoryService;

	@Resource
	private DetailDispItemService detailDispItemService;

	@Resource
	private EadService eadService;

	@Resource
	private ProductClassService productClassService;

	/**
	 * 受け取った検索結果を各ユーザの設定に応じた検索結果に変換します.
	 * @param eadSlipLineJoinDtoList 検索結果
	 * @param searchResultList 変換後検索結果
	 * @param searchTarget 検索対象
	 * @return 検索結果表示カラムリスト
	 */
	public List<DetailDispItemDto> createSearchStockResult(List<EadSlipLineJoinDto> eadSlipLineJoinDtoList,
			List<List<Object>> searchResultList,String searchTarget) throws ServiceException {
		try {
			// パラメータを作成する
			List<BeanMap> resultMapList = new ArrayList<BeanMap>();
			if(eadSlipLineJoinDtoList != null) {
				for(EadSlipLineJoinDto eadSlipLineJoinDto : eadSlipLineJoinDtoList) {
					BeanMap map = Beans.createAndCopy(BeanMap.class, eadSlipLineJoinDto).execute();
					map.put(EadService.Param.SLIP_ID, eadSlipLineJoinDto);
					map.put(EadService.Param.SRC_FUNC, eadSlipLineJoinDto);
					map.put(EadService.Param.EAD_SLIP_CATEGORY, eadSlipLineJoinDto);
					map.put(EadService.Param.EAD_CATEGORY, eadSlipLineJoinDto);
					map.put(EadService.Param.RACK_CODE, eadSlipLineJoinDto);
					resultMapList.add(map);
				}
			}

			// 検索結果に表示する列を取得する
			List<DetailDispItemDto> columnInfoList = detailDispItemService.createResult(resultMapList,
					searchResultList, Constants.MENU_ID.SEARCH_STOCK, searchTarget);

			return columnInfoList;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 検索条件を指定して結果件数を返します.
	 * @param params 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer getSearchStockResultCount(BeanMap params) throws ServiceException {
		try {
			Integer count = Integer.valueOf(0);

			// 検索対象を取得する
			String searchTarget = (String) params.get(EadService.Param.SEARCH_TARGET);

			// 伝票単位か明細単位か
			if(Constants.SEARCH_TARGET.VALUE_SLIP.equals(searchTarget)) {
				count = eadService.findEadSlipCntByCondition(params);
			} else if(Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {
				count = eadService.findEadSlipLineCntByCondition(params);
			}

			return count;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して入出庫伝票DTOのリストを返します.
	 * @param params 検索条件
	 * @return 入出庫伝票DTOリスト
	 * @throws ServiceException
	 */
	public List<EadSlipLineJoinDto> createEadSlipJoinDtoList(
			BeanMap params) throws ServiceException {
		try {
			// 検索対象を取得する
			String searchTarget = (String) params.get(EadService.Param.SEARCH_TARGET);

			// ソートカラムを設定
			String sortColumn = (String) params.get(EadService.Param.SORT_COLUMN);

			// ソート順を設定
			boolean sortOrderAsc = (Boolean) params.get(EadService.Param.SORT_ORDER_ASC);

			// 伝票単位か明細単位か
			List<EadSlipLineJoin> eadSlipLineJoinList = new ArrayList<EadSlipLineJoin>();
			if(Constants.SEARCH_TARGET.VALUE_SLIP.equals(searchTarget)) {
				// 検索を行う
				eadSlipLineJoinList = eadService.findEadSlipByCondition(
						params, sortColumn, sortOrderAsc);
			} else if(Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {
				// 検索を行う
				eadSlipLineJoinList = eadService.findEadSlipLineByCondition(
						params, sortColumn, sortOrderAsc);
			}

			// EadSlipLineJoinDtoリストを生成する
			List<EadSlipLineJoinDto> eadSlipLineJoinDtoList = new ArrayList<EadSlipLineJoinDto>();
			for(EadSlipLineJoin eadSlipLineJoin : eadSlipLineJoinList) {
				// Dtoを生成
				EadSlipLineJoinDto eadSlipLineJoinDto = Beans.createAndCopy(
						EadSlipLineJoinDto.class, eadSlipLineJoin).execute();

				// 伝票番号の設定
				if(Constants.SRC_FUNC.SALES.equals(eadSlipLineJoinDto.srcFunc)) {
					// 売上の場合
					eadSlipLineJoinDto.srcFuncName = Constants.SRC_FUNC.LABEL_SALES;
					eadSlipLineJoinDto.slipId = eadSlipLineJoinDto.salesSlipId;
					eadSlipLineJoinDto.menuValid = userDto.isMenuValid(Constants.MENU_ID.INPUT_SALES);
				} else if(Constants.SRC_FUNC.PURCHASE.equals(eadSlipLineJoinDto.srcFunc)) {
					// 仕入の場合
					eadSlipLineJoinDto.srcFuncName = Constants.SRC_FUNC.LABEL_PURCHASE;
					eadSlipLineJoinDto.slipId = eadSlipLineJoinDto.supplierSlipId;
					eadSlipLineJoinDto.menuValid = userDto.isMenuValid(Constants.MENU_ID.INPUT_PURCHASE);
				} else if(Constants.SRC_FUNC.STOCK.equals(eadSlipLineJoinDto.srcFunc)) {
					// 入出庫の場合
					eadSlipLineJoinDto.srcFuncName = eadSlipLineJoinDto.eadCategoryName;
					eadSlipLineJoinDto.slipId = eadSlipLineJoinDto.eadSlipId;
					eadSlipLineJoinDto.menuValid = userDto.isMenuValid(Constants.MENU_ID.INPUT_STOCK);
				} else if(Constants.SRC_FUNC.STOCK_TRANSFER.equals(eadSlipLineJoinDto.srcFunc)) {
					// 在庫移動の場合
					eadSlipLineJoinDto.srcFuncName = Constants.SRC_FUNC.LABEL_STOCK_TRANSFER;
					eadSlipLineJoinDto.slipId = eadSlipLineJoinDto.eadSlipId;
					eadSlipLineJoinDto.menuValid = userDto.isMenuValid(Constants.MENU_ID.INPUT_STOCK_TRANSFER);
				}
				if(Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {
					// 明細検索の場合
					eadSlipLineJoinDto.slipId =
						eadSlipLineJoinDto.slipId + " - " + eadSlipLineJoinDto.lineNo;
				}

				// リストに追加
				eadSlipLineJoinDtoList.add(eadSlipLineJoinDto);
			}

			return eadSlipLineJoinDtoList;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * カテゴリコードを受け取って、カテゴリ情報リストを返します.
	 * @param categoryId カテゴリコード
	 * @return カテゴリ情報リスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> findCategoryLabelValueBeanListById(
			int categoryId) throws ServiceException {
		return categoryService.findCategoryLabelValueBeanListById(categoryId);
	}

	/**
	 * 分類(大)リストを返します.
	 * @return 分類(大)リスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> findAllProductClass1LabelValueBeanList()
			throws ServiceException {
		return productClassService.findAllProductClass1LabelValueBeanList();
	}
}
