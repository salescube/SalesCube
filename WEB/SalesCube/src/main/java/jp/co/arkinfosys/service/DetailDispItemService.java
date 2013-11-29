/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.entity.DetailDispItem;
import jp.co.arkinfosys.entity.join.DetailDispItemJoin;
import jp.co.arkinfosys.service.exception.UnabledLockException;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 明細表示項目サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DetailDispItemService extends AbstractService<DetailDispItem> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String DETAIL_ID = "detailId";
		public static final String USER_ID = "userId";
		public static final String TARGET = "target";
		public static final String SEQ = "seq";
		public static final String ITEM_ID = "itemId";
		public static final String DISP_FLAG = "dispFlag";
	}

	/**
	 * 明細IDを指定して、表示列の初期設定を返します.
	 * @param detailId 明細ID
	 * @param target 検索対象
	 * @param displayOnly 表示設定項目のみか否か
	 * @return 検索結果列情報{@link DetailDispItemDto}のリスト
	 */
	public List<DetailDispItemDto> findDetailDispItemByCondition(
			String detailId, String target, boolean displayOnly) {
		Map<String, Object> param = super.createSqlParam();
		param.put(DetailDispItemService.Param.DETAIL_ID, detailId);
		param.put(DetailDispItemService.Param.TARGET, target);

		param.put(DetailDispItemService.Param.DISP_FLAG, null);
		if (displayOnly) {
			// 表示項目のみ
			param.put(DetailDispItemService.Param.DISP_FLAG, Constants.FLAG.ON);
		}

		List<DetailDispItem> entityList = this.selectBySqlFile(
				DetailDispItem.class,
				"detaildisp/FindDetailDispItemByCondition.sql", param)
				.getResultList();

		// EntityからDTOに変換
		List<DetailDispItemDto> dtoList = new ArrayList<DetailDispItemDto>();
		for (DetailDispItem entity : entityList) {
			DetailDispItemDto dto = new DetailDispItemDto();
			Beans.copy(entity, dto).timestampConverter(
					Constants.FORMAT.TIMESTAMP).dateConverter(
					Constants.FORMAT.DATE).execute();
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * 明細IDを指定して、表示列のユーザ設定を返します.
	 * @param detailId 明細ID
	 * @param target 検索対象
	 * @return 検索結果列情報{@link DetailDispItemDto}のリスト
	 * @throws ServiceException
	 */
	public List<DetailDispItemDto> findDetailDispItemCfgByCondition(
			String detailId, String target) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(DetailDispItemService.Param.DETAIL_ID, detailId);
			param.put(DetailDispItemService.Param.USER_ID, this.userDto.userId);
			param.put(DetailDispItemService.Param.TARGET, target);

			List<DetailDispItemJoin> entityList = this.selectBySqlFile(
					DetailDispItemJoin.class,
					"detaildisp/FindDetailDispItemCfgByCondition.sql", param)
					.getResultList();

			// EntityからDTOに変換
			List<DetailDispItemDto> dtoList = new ArrayList<DetailDispItemDto>();
			for (DetailDispItemJoin entity : entityList) {
				DetailDispItemDto dto = new DetailDispItemDto();
				Beans.copy(entity, dto).timestampConverter(
						Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();
				dtoList.add(dto);
			}

			return dtoList;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索結果表示情報のリストを作成します.
	 * @param resultMapList 表示列のリスト
	 * @param searchResultList 検索結果のリスト
	 * @param detailId 明細ID
	 * @param target 検索対象
	 * @return 検索結果表示情報{@link DetailDispItemDto}のリスト
	 * @throws ServiceException
	 */
	public List<DetailDispItemDto> createResult(List<BeanMap> resultMapList,
			List<List<Object>> searchResultList, String detailId, String target)
			throws ServiceException {
		// 検索結果に表示する列を取得する

		// ユーザに設定されている表示列
		List<DetailDispItemDto> columnInfoList = getUserSetColumns(detailId,
				target);
		if (columnInfoList == null || columnInfoList.size() == 0) {
			// 初期設定の表示列
			columnInfoList = getInitColumns(detailId, target);
		}

		// 検索前の初期表示時は不要
		if (resultMapList != null && resultMapList.size() > 0) {
			createSearchResultList(resultMapList, columnInfoList,
					searchResultList);
		}
		return columnInfoList;
	}

	/**
	 * ユーザに設定されている検索結果表示情報のリストを返します.
	 * @param detailId 明細ID
	 * @param target 検索対象
	 * @return 検索結果表示情報{@link DetailDispItemDto}のリスト
	 * @throws ServiceException
	 */
	private List<DetailDispItemDto> getUserSetColumns(String detailId,
			String target) throws ServiceException {
		List<DetailDispItemDto> dtoList = findDetailDispItemCfgByCondition(
				detailId, target);
		return dtoList;
	}

	/**
	 * 初期設定の検索結果表示情報のリストを返します.
	 * @param detailId 明細ID
	 * @param target 検索対象
	 * @return 検索結果表示情報{@link DetailDispItemDto}のリスト
	 */
	private List<DetailDispItemDto> getInitColumns(String detailId,
			String target) {

		List<DetailDispItemDto> dtoList = findDetailDispItemByCondition(
				detailId, target, true);
		return dtoList;
	}

	/**
	 * 検索結果のリストを作成します.
	 * @param resultMapList 表示列のマップ
	 * @param columnInfoList 検索結果表示情報のリスト
	 * @param searchResultList 検索結果のリスト
	 * @throws ServiceException
	 */
	public void createSearchResultList(List<BeanMap> resultMapList,
			List<DetailDispItemDto> columnInfoList,
			List<List<Object>> searchResultList) throws ServiceException {

		for (BeanMap resultMap : resultMapList) {

			// 設定されている列を順に指定して、検索結果の値を取得し、リストに追加する
			List<Object> list = new ArrayList<Object>();
			for (DetailDispItemDto dto : columnInfoList) {

				// カラムを指定して検索結果から値を取得し、検索結果リストに追加する
				if(resultMap.containsKey(dto.itemId)) {
					list.add(resultMap.get(dto.itemId));
				}
				else {
					list.add("");
				}

			}
			searchResultList.add(list);
		}
	}

	/**
	 * 明細ID、項目IDおよび検索対象を指定して、ログインユーザの検索結果設定を更新します.
	 * @param detailId 明細ID
	 * @param target 検索対象
	 * @param itemId 項目ID
	 * @param lockItemId ロック用項目ID
	 * @param lockUpdDatetm ロック用更新日時の文字列
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	public void updateDetailDispItemCfg(String detailId, String target,
			String[] itemId, String lockItemId, String lockUpdDatetm)
			throws UnabledLockException, ServiceException {
		if (itemId == null) {
			return;
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(DetailDispItemService.Param.DETAIL_ID, detailId);
			param.put(DetailDispItemService.Param.USER_ID, this.userDto.userId);
			param.put(DetailDispItemService.Param.TARGET, target);

			if (lockItemId != null && lockUpdDatetm != null) {
				// 過去の設定情報がある場合は排他制御
				param.put(DetailDispItemService.Param.ITEM_ID, lockItemId);

				int lockResult = super.lockRecordBySqlFile(
						"detaildisp/LockDetailDispItemCfgByCondition.sql",
						param, lockUpdDatetm);
				if (lockResult != LockResult.SUCCEEDED) {
					throw new ServiceException("");
				}

				// 既存のレコードを削除する
				this.updateBySqlFile(
						"detaildisp/DeleteDetailDispItemCfgByCondition.sql",
						param).execute();
			}

			// 新規レコードを挿入する
			for (int i = 0; i < itemId.length; i++) {
				if (itemId[i] == null) {
					continue;
				}
				param.put(DetailDispItemService.Param.SEQ, i + 1);
				param.put(DetailDispItemService.Param.ITEM_ID, itemId[i]);

				this.updateBySqlFile("detaildisp/InsertDetailDispItemCfg.sql",
						param).execute();
			}
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
