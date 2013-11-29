/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.dto.stock.EntrustEadSlipLineJoinDto;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.EntrustEadService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 委託入出庫検索サービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchEntrustStockService extends
		AbstractService<EntrustEadSlipLineJoinDto> {

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
	private DetailDispItemService detailDispItemService;

	@Resource
	private EntrustEadService entrustEadService;

	/**
	 * 受け取った検索結果を各ユーザの設定に応じた検索結果に変換します.
	 *
	 * @param eadSlipLineJoinDtoList 検索結果
	 * @param searchResultList 変換後検索結果
	 * @param searchTarget 検索対象
	 * @return 検索結果表示カラムリスト
	 */
	public List<DetailDispItemDto> createSearchStockResult(
			List<EntrustEadSlipLineJoinDto> eadSlipLineJoinDtoList,
			List<List<Object>> searchResultList, String searchTarget)
			throws ServiceException {
		try {
			// パラメータを作成する
			List<BeanMap> resultMapList = new ArrayList<BeanMap>();
			if (eadSlipLineJoinDtoList != null) {
				for (EntrustEadSlipLineJoinDto entrustEadDto : eadSlipLineJoinDtoList) {
					BeanMap map = Beans.createAndCopy(BeanMap.class,
							entrustEadDto).execute();
					map.put(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadDto);
					map.put(EntrustEadService.Param.ENTRUST_EAD_LINE_ID_NO, entrustEadDto);
					map.put(EntrustEadService.Param.ENTRUST_EAD_CATEGORY, entrustEadDto);
					map.put(EntrustEadService.Param.REL_ENTRUST_EAD_SLIP_ID, entrustEadDto);
					map.put(EntrustEadService.Param.PO_SLIP_ID, entrustEadDto);
					map.put(EntrustEadService.Param.PO_LINE_ID_NO, entrustEadDto);
					resultMapList.add(map);
				}
			}

			// 検索結果に表示する列を取得する
			List<DetailDispItemDto> columnInfoList = detailDispItemService
					.createResult(resultMapList, searchResultList,
							Constants.MENU_ID.SEARCH_ENTRUST_STOCK,
							searchTarget);

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
	public Integer getSearchStockResultCount(BeanMap params)
			throws ServiceException {
		try {
			Integer count = Integer.valueOf(0);

			// 検索対象を取得する
			String searchTarget = (String) params.get(Param.SEARCH_TARGET);

			// 伝票単位か明細単位か
			if (Constants.SEARCH_TARGET.VALUE_SLIP.equals(searchTarget)) {
				count = entrustEadService.findEadSlipCntByCondition(params);
			} else if (Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {
				count = entrustEadService.findEadSlipLineCntByCondition(params);
			}

			return count;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して委託入出庫伝票DTOのリストを返します.
	 * @param params 検索条件
	 * @return 委託入出庫伝票DTO
	 * @throws ServiceException
	 */
	public List<EntrustEadSlipLineJoinDto> createEadSlipJoinDtoList(
			BeanMap params) throws ServiceException {
		try {
			// 検索対象を取得する
			String searchTarget = (String) params.get(Param.SEARCH_TARGET);

			// ソートカラムを設定
			String sortColumn = null;
			if (params.containsKey(EntrustEadService.Param.SORT_COLUMN)) {
				sortColumn = (String) params
						.get(EntrustEadService.Param.SORT_COLUMN);
			//} else {
			//	sortColumn = EntrustEadService.Param.ENTRUST_EAD_SLIP_ID;
			}

			// ソートカラムを設定
			boolean sortOrderAsc = true;
			if (params.containsKey(EntrustEadService.Param.SORT_ORDER_ASC)) {
				sortOrderAsc = (Boolean) params
						.get(EntrustEadService.Param.SORT_ORDER_ASC);
			}

			// 伝票単位か明細単位か
			List<?> resultList = null;
			if (Constants.SEARCH_TARGET.VALUE_SLIP.equals(searchTarget)) {
				// 検索を行う
				resultList = entrustEadService.findEadSlipByCondition(params,
						sortColumn, sortOrderAsc);
			} else if (Constants.SEARCH_TARGET.VALUE_LINE.equals(searchTarget)) {
				// 検索を行う
				resultList = entrustEadService.findEadSlipLineByCondition(
						params, sortColumn, sortOrderAsc);
			}
			else {
				return new ArrayList<EntrustEadSlipLineJoinDto>();
			}

			// EadSlipLineJoinDtoリストを生成する
			List<EntrustEadSlipLineJoinDto> resultDtoList = new ArrayList<EntrustEadSlipLineJoinDto>();
			for (Object resultLine : resultList) {
				// Dtoを生成
				EntrustEadSlipLineJoinDto entrustEadSlipTrnDto = Beans
						.createAndCopy(EntrustEadSlipLineJoinDto.class,
								resultLine)
						.dateConverter(Constants.FORMAT.DATE)
						.timestampConverter(Constants.FORMAT.TIMESTAMP)
						.execute();
				// リストに追加
				resultDtoList.add(entrustEadSlipTrnDto);
			}

			return resultDtoList;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}
}
