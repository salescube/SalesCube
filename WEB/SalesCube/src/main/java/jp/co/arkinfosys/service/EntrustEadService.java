/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.stock.EntrustEadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EntrustEadSlipTrnDto;
import jp.co.arkinfosys.entity.EntrustEadLineTrn;
import jp.co.arkinfosys.entity.EntrustEadSlipTrn;
import jp.co.arkinfosys.entity.join.EntrustEadSlipLineJoin;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;

/**
 * 委託入出庫伝票・明細行サービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class EntrustEadService extends AbstractService<EntrustEadSlipTrn> {
	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String ENTRUST_EAD_SLIP_ID = "entrustEadSlipId";
		public static final String ENTRUST_EAD_DATE = "entrustEadDate";
		public static final String ENTRUST_EAD_DATE_FROM = "entrustEadDateFrom";
		public static final String ENTRUST_EAD_DATE_TO = "entrustEadDateTo";
		public static final String ENTRUST_EAD_ANNUAL = "entrustEadAnnual";
		public static final String ENTRUST_EAD_MONTHLY = "entrustEadMonthly";
		public static final String ENTRUST_EAD_YM = "entrustEadYm";
		public static final String USER_ID = "userId";
		public static final String USER_NAME = "userName";
		public static final String ENTRUST_EAD_CATEGORY = "entrustEadCategory";
		public static final String ENTRUST_EAD_CATEGORY_ENTER = "entrustEadCategoryEnter";
		public static final String ENTRUST_EAD_CATEGORY_DISPATCH = "entrustEadCategoryDispatch";
		public static final String ENTRUST_EAD_CATEGORY_DISPATCH_NO_PRINT = "entrustEadCategoryDispatchNoPrint";
		public static final String REMARKS = "remarks";
		public static final String PO_SLIP_ID = "poSlipId";
		public static final String DISPATCH_ORDER_PRINT_COUNT = "dispatchOrderPrintCount";
		public static final String ENTRUST_EAD_LINE_ID = "entrustEadLineId";
		public static final String LINE_NO = "lineNo";
		public static final String PRODUCT_CODE = "productCode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String QUANTITY = "quantity";
		public static final String PO_LINE_ID = "poLineId";
		public static final String REL_ENTRUST_EAD_LINE_ID = "relEntrustEadLineId";
		public static final String REL_ENTRUST_EAD_SLIP_ID = "relEntrustEadSlipId";

		public static final String ENTRUST_EAD_LINE_ID_NO = "entrustEadLineIdNo";
		public static final String PO_LINE_ID_NO = "poLineIdNo";

		public static final String SUPPLIER_CODE = "supplierCode";
		public static final String SUPPLIER_NAME = "supplierName";
		public static final String PRODUCT1 = "product1";
		public static final String PRODUCT2 = "product2";
		public static final String PRODUCT3 = "product3";

		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET_ROW = "offsetRow";

		private static final String ENTRUST_EAD_CATEGORY_ID = "entrustEadCategoryId";

		public static final String SORT_ORDER_ASC = "sortOrderAsc";
		public static final String SORT_ORDER = "sortOrder";
		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_COLUMN_SLIP = "sortColumnSlip";
		public static final String SORT_COLUMN_LINE = "sortColumnLine";
	}

	/**
	 *
	 * カラム定義クラスです.
	 *
	 */
	public static class Column {
		public static final String PO_LINE_ID_NO = "poLineIdNo";
		public static final String ENTRUST_EAD_SLIP_ID = "entrustEadLineIdNo";

		public static final String SORT_PO_SLIP_ID = "SORT_PO_SLIP_ID";
		public static final String SORT_PO_LINE_NO = "SORT_PO_LINE_NO";
		public static final String SORT_ENTRUST_EAD_SLIP_ID = "SORT_ENTRUST_EAD_SLIP_ID";
		public static final String SORT_ENTRUST_EAD_LINE_NO = "SORT_ENTRUST_EAD_LINE_NO";
	}


	/**
	 *
	 * テーブル名定義クラスです.
	 *
	 */
	public static class Table {
		/** テーブル名：入出庫伝票 */
		public static final String ENTRUST_EAD_SLIP_TRN = "ENTRUST_EAD_SLIP_TRN";
		/** テーブル名：入出庫伝票明細 */
		public static final String ENTRUST_EAD_LINE_TRN = "ENTRUST_EAD_LINE_TRN";
		/** テーブル名：入出庫伝票履歴 */
		public static final String ENTRUST_EAD_SLIP_TRN_HIST = "ENTRUST_EAD_SLIP_TRN_HIST";
		/** テーブル名：入出庫伝票明細履歴 */
		public static final String ENTRUST_EAD_LINE_TRN_HIST = "ENTRUST_EAD_LINE_TRN_HIST";
	}

    @Resource
    protected PoSlipService poSlipService;


	/**
	 * 委託入出庫伝票と明細行を登録します.
	 * <p>
	 * フレームワーク化に伴い、伝票と明細行の登録は別々に呼び出されるようになりました.<br>
	 * 本メソッドは既存の非フレームワーク化処理との互換性のために残してあります.
	 * </p>
	 *
	 * @param entrustEadSlipTrnDto 委託入出庫伝票DTO
	 * @throws ServiceException
	 * @deprecated 推奨されません.フレームワークから呼び出す場合は{@link EntrustEadService#insertSlip(EntrustEadSlipTrn entrustEadSlipTrn) insertSlip}と{@link EntrustEadService#insertLine(EntrustEadLineTrn entrustEadSlipTrn) insertLine}を使用して下さい.
	 *
	 */
	public void insertSlipAndLine(EntrustEadSlipTrnDto entrustEadSlipTrnDto)
			throws ServiceException {
		try {
			// 入出庫伝票の処理
			EntrustEadSlipTrn entrustEadSlipTrn = Beans.createAndCopy(EntrustEadSlipTrn.class,
					entrustEadSlipTrnDto).execute();

			// Insert
			insertSlip(entrustEadSlipTrn);

			// 入出庫伝票明細の処理
			for (EntrustEadLineTrnDto entrustEadLineTrnDto : entrustEadSlipTrnDto.getLineDtoList()) {
				// チェックされていない行は処理しない
				if ( entrustEadLineTrnDto.checkEadLine == null ) {
					continue;
				}
				EntrustEadLineTrn entrustEadLineTrn = Beans.createAndCopy(EntrustEadLineTrn.class,
						entrustEadLineTrnDto).execute();

				// Insert
				insertLine(entrustEadLineTrn);

				// 委託出庫入力の場合、委託入出庫明細の「関連委託入出庫伝票行ID」を設定する(入庫明細・出庫明細両方)
				if( CategoryTrns.ENTRUST_EAD_CATEGORY_DISPATCH.equals(entrustEadLineTrnDto.entrustEadCategory) ) {
					updateRelEentrustEadLineIdByPoLineId(entrustEadLineTrn.poLineId );
				}

				// 発注伝票明細の状態を変更する
				if(CategoryTrns.ENTRUST_EAD_CATEGORY_ENTER.equals(entrustEadLineTrnDto.entrustEadCategory)) {
					// 選択されている委託入出庫区分が入庫の場合、明細ステータスを委託在庫生産完了にする
					poSlipService.updatePOrderLineTrnStatusByPoLineId(entrustEadLineTrnDto.poLineId, Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_MAKED , entrustEadLineTrnDto.quantity);
				} else {
					// 選択されている委託入出庫区分が出庫の場合、明細ステータスを委託在庫出庫完了にする
					poSlipService.updatePOrderLineTrnStatusByPoLineId(entrustEadLineTrnDto.poLineId, Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_DELIVERED , entrustEadLineTrnDto.quantity);
				}
			}

			// 発注伝票の状態を変更する(更新済みの明細状態を再集計してメソッド内で自動的に伝票状態を判別し、設定される)
			poSlipService.updatePOrderTrnStatusByPoSlipId(entrustEadSlipTrnDto.poSlipId);

		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入庫明細、委託出庫明細に、関連委託入出庫伝票行IDを設定します.
	 * @param poLineId 発注伝票行ID
	 * @throws ServiceException
	 */
	public void updateRelEentrustEadLineIdByPoLineId( Integer poLineId ) throws ServiceException  {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.PO_LINE_ID, poLineId );
			this.updateBySqlFile("entrustead/UpdateRelEentrustEadLineIdByPoLineId.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票を登録します.
	 * @param entrustEadSlipTrn　委託入出庫伝票エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	public int insertSlip(EntrustEadSlipTrn entrustEadSlipTrn) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createSlipSqlParam(entrustEadSlipTrn);
			return this.updateBySqlFile("entrustead/InsertSlip.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票明細行を登録します.
	 * @param entrustEadLineTrn　委託入出庫伝票明細行エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	public int insertLine(EntrustEadLineTrn entrustEadLineTrn) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createLineSqlParam(entrustEadLineTrn);
			return this.updateBySqlFile("entrustead/InsertLine.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票を更新します.
	 * @param entrustEadSlipTrn 委託入出庫伝票エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int updateSlipByEntrustEadSlipId(
			EntrustEadSlipTrn entrustEadSlipTrn ) throws ServiceException {

		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createSlipSqlParam(entrustEadSlipTrn);
			return this.updateBySqlFile("entrustead/UpdateSlip.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票明細行を更新します.
	 * @param entrustEadLineTrn 委託入出庫伝票明細行エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int updateLineByEntrustEadLineId(
			EntrustEadLineTrn entrustEadLineTrn) throws ServiceException {

		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createLineSqlParam(entrustEadLineTrn);
			return this.updateBySqlFile("entrustead/UpdateLine.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 伝票作成時のSQLパラメータを作成します.
	 * @param entrustEadSlipTrn 委託入出庫伝票エンティティ
	 * @return 伝票作成時のSQLパラメータ
	 */
	protected Map<String, Object> createSlipSqlParam(EntrustEadSlipTrn entrustEadSlipTrn) {
		Map<String, Object> param = super.createSqlParam();
		param.put(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadSlipTrn.entrustEadSlipId);
		param.put(EntrustEadService.Param.ENTRUST_EAD_DATE, entrustEadSlipTrn.entrustEadDate);
		param.put(EntrustEadService.Param.ENTRUST_EAD_ANNUAL, entrustEadSlipTrn.entrustEadAnnual);
		param.put(EntrustEadService.Param.ENTRUST_EAD_MONTHLY, entrustEadSlipTrn.entrustEadMonthly);
		param.put(EntrustEadService.Param.ENTRUST_EAD_YM, entrustEadSlipTrn.entrustEadYm);
		param.put(EntrustEadService.Param.USER_ID, entrustEadSlipTrn.userId);
		param.put(EntrustEadService.Param.USER_NAME, entrustEadSlipTrn.userName);
		param.put(EntrustEadService.Param.ENTRUST_EAD_CATEGORY, entrustEadSlipTrn.entrustEadCategory);
		param.put(EntrustEadService.Param.REMARKS, entrustEadSlipTrn.remarks);
		param.put(EntrustEadService.Param.PO_SLIP_ID, entrustEadSlipTrn.poSlipId);
		param.put(EntrustEadService.Param.DISPATCH_ORDER_PRINT_COUNT, entrustEadSlipTrn.dispatchOrderPrintCount);
		return param;
	}

	/**
	 * 明細作成時のSQLパラメータを作成します.
	 * @param entrustEadLineTrn 委託入出庫伝票明細行エンティティ
	 * @return 明細作成時のSQLパラメータ
	 */
	protected Map<String, Object> createLineSqlParam(EntrustEadLineTrn entrustEadLineTrn) {
		Map<String, Object> param = super.createSqlParam();
		param.put(EntrustEadService.Param.ENTRUST_EAD_LINE_ID, entrustEadLineTrn.entrustEadLineId);
		param.put(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadLineTrn.entrustEadSlipId);
		param.put(EntrustEadService.Param.LINE_NO, entrustEadLineTrn.lineNo);
		param.put(EntrustEadService.Param.PRODUCT_CODE, entrustEadLineTrn.productCode);
		param.put(EntrustEadService.Param.PRODUCT_ABSTRACT, entrustEadLineTrn.productAbstract);
		param.put(EntrustEadService.Param.QUANTITY, entrustEadLineTrn.quantity);
		param.put(EntrustEadService.Param.REMARKS, entrustEadLineTrn.remarks);
		param.put(EntrustEadService.Param.PO_LINE_ID, entrustEadLineTrn.poLineId);
		param.put(EntrustEadService.Param.REL_ENTRUST_EAD_LINE_ID, entrustEadLineTrn.relEntrustEadLineId);
		param.put(EntrustEadService.Param.ENTRUST_EAD_CATEGORY, entrustEadLineTrn.entrustEadCategory);
		return param;
	}

	/**
	 * 委託入出庫伝票番号を指定して委託入出庫伝票情報を取得します.
	 * @param entrustEadSlipId 委託入出庫伝票番号
	 * @return 委託入出庫伝票エンティティ
	 * @throws ServiceException
	 */
	public EntrustEadSlipTrn findSlipByEntrustEadSlipId(Integer entrustEadSlipId)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadSlipId);

			return this.selectBySqlFile(EntrustEadSlipTrn.class,
					"entrustead/FindSlipByEntrustEadSlipId.sql", param).getSingleResult();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票番号を指定して委託入出庫伝票明細行情報を取得します.
	 * @param entrustEadSlipId 委託入出庫伝票番号
	 * @return 委託入出庫伝票明細行エンティティリスト
	 * @throws ServiceException
	 */
	public List<EntrustEadLineTrn> findLineByEntrustEadSlipId(Integer entrustEadSlipId)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadSlipId);

			return this.selectBySqlFile(EntrustEadLineTrn.class,
					"entrustead/FindLineByEntrustEadSlipId.sql", param).getResultList();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票明細行を削除します.
	 * @param entrustEadLineId 委託入出庫伝票行ID
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteLineByEntrustEadLineId(Integer entrustEadLineId)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EntrustEadService.Param.ENTRUST_EAD_LINE_ID,
					entrustEadLineId);

			super.updateAudit(EntrustEadLineTrn.TABLE_NAME,
					new String[] { Param.ENTRUST_EAD_LINE_ID },
					new Object[] { entrustEadLineId });

			return this.updateBySqlFile(
					"entrustead/DeleteLineByEntrustEadLineId.sql", param)
					.execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託出庫指示書印刷回数をインクリメントします.
	 * @param entrustEadSlipId 委託入出庫伝票番号
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int incrementDispatchOrderPrintCount(String entrustEadSlipId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadSlipId);

			return this.updateBySqlFile("entrustead/IncrementDispatchOrderPrintCount.sql", param)
					.execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}

	}

	/**
	 * 検索条件を指定して結果リストを取得します.
	 *
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 委託入出庫伝票エンティティのリスト
	 * @throws ServiceException
	 */
	public List<EntrustEadSlipTrn> findEadSlipByCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param, sortColumn, sortOrderAsc);

			return this.selectBySqlFile(EntrustEadSlipTrn.class,
					"entrustead/FindSlipByCondition.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して委託入出庫伝票の件数を取得します.
	 *
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer findEadSlipCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param, null, true);

			return this.selectBySqlFile(Integer.class,
					"entrustead/FindSlipCntByCondition.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果リストを取得します.
	 *
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 委託入出庫伝票と委託入出庫伝票明細行のエンティティのリスト
	 * @throws ServiceException
	 */
	public List<EntrustEadSlipLineJoin> findEadSlipLineByCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param, sortColumn, sortOrderAsc);

			// 特定のカラムがsortキーに指定された場合は伝票、行番をキーにする
			if (Column.ENTRUST_EAD_SLIP_ID.equals(sortColumn)) {
				// 委託入出庫番号 - 行
				param.put(Param.SORT_COLUMN_SLIP, Column.SORT_ENTRUST_EAD_SLIP_ID);
				param.put(Param.SORT_COLUMN_LINE, Column.SORT_ENTRUST_EAD_LINE_NO);
				param.put(Param.SORT_COLUMN, null);
			}else if(Column.PO_LINE_ID_NO.equals(sortColumn)){
				// 発注伝票番号 - 行
				param.put(Param.SORT_COLUMN_SLIP, Column.SORT_PO_SLIP_ID);
				param.put(Param.SORT_COLUMN_LINE, Column.SORT_PO_LINE_NO);
				param.put(Param.SORT_COLUMN, null);
			}

			return this.selectBySqlFile(EntrustEadSlipLineJoin.class,
					"entrustead/FindSlipLineByCondition.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して委託入出庫伝票明細行の結果件数を取得します.
	 *
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	public Integer findEadSlipLineCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param, null, true);

			return this.selectBySqlFile(Integer.class,
					"entrustead/FindSlipLineCntByCondition.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	private Map<String, Object> setConditionParam(
			Map<String, Object> conditions, Map<String, Object> param,
			String sortColumn, boolean sortOrderAsc) {
		// 委託入出庫伝票番号
		if (conditions.containsKey(Param.ENTRUST_EAD_SLIP_ID)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.ENTRUST_EAD_SLIP_ID))) {
				param.put(Param.ENTRUST_EAD_SLIP_ID, new Integer(
						(String) conditions.get(Param.ENTRUST_EAD_SLIP_ID)));
			}
		}

		// 委託入出庫区分
		this.setEntrustEadCategoryCondition(conditions, param);

		// 入力担当者名
		if (conditions.containsKey(Param.USER_NAME)) {
			if (StringUtil.hasLength((String) conditions.get(Param.USER_NAME))) {
				param.put(Param.USER_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(Param.USER_NAME)));
			}
		}

		// 委託入出庫日（開始）
		if (conditions.containsKey(Param.ENTRUST_EAD_DATE_FROM)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.ENTRUST_EAD_DATE_FROM))) {
				param.put(Param.ENTRUST_EAD_DATE_FROM, (String) conditions
						.get(Param.ENTRUST_EAD_DATE_FROM));
			}
		}

		// 委託入出庫日（開始）全角半角変換
		if (conditions.containsKey(Param.ENTRUST_EAD_DATE_FROM)) {
			param.put(Param.ENTRUST_EAD_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.ENTRUST_EAD_DATE_FROM)));

		}


		// 委託入出庫日（終了）
		if (conditions.containsKey(Param.ENTRUST_EAD_DATE_TO)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.ENTRUST_EAD_DATE_TO))) {
				param.put(Param.ENTRUST_EAD_DATE_TO, (String) conditions
						.get(Param.ENTRUST_EAD_DATE_TO));
			}
		}

		// 委託入出庫日（終了）（開始）全角半角変換
		if (conditions.containsKey(Param.ENTRUST_EAD_DATE_TO)) {
			param.put(Param.ENTRUST_EAD_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.ENTRUST_EAD_DATE_TO)));

		}

		// 備考
		if (conditions.containsKey(Param.REMARKS)) {
			if (StringUtil.hasLength((String) conditions.get(Param.REMARKS))) {
				param.put(Param.REMARKS, super
						.createPartialSearchCondition((String) conditions
								.get(Param.REMARKS)));
			}
		}

		// 仕入先コード
		if (conditions.containsKey(Param.SUPPLIER_CODE)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SUPPLIER_CODE))) {
				param.put(Param.SUPPLIER_CODE, super
						.createPrefixSearchCondition((String) conditions
								.get(Param.SUPPLIER_CODE)));
			}
		}

		// 仕入先名
		if (conditions.containsKey(Param.SUPPLIER_NAME)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.SUPPLIER_NAME))) {
				param.put(Param.SUPPLIER_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(Param.SUPPLIER_NAME)));
			}
		}

		// 商品コード
		if (conditions.containsKey(Param.PRODUCT_CODE)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.PRODUCT_CODE))) {
				param.put(Param.PRODUCT_CODE, super
						.createPrefixSearchCondition((String) conditions
								.get(Param.PRODUCT_CODE)));
			}
		}

		// 商品名
		if (conditions.containsKey(Param.PRODUCT_ABSTRACT)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.PRODUCT_ABSTRACT))) {
				param.put(Param.PRODUCT_ABSTRACT, super
						.createPartialSearchCondition((String) conditions
								.get(Param.PRODUCT_ABSTRACT)));
			}
		}

		// 分類（大）
		if (conditions.containsKey(Param.PRODUCT1)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PRODUCT1))) {
				param.put(Param.PRODUCT1, (String) conditions
						.get(Param.PRODUCT1));
			}
		}

		// 分類（中）
		if (conditions.containsKey(Param.PRODUCT2)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PRODUCT2))) {
				param.put(Param.PRODUCT2, (String) conditions
						.get(Param.PRODUCT2));
			}
		}

		// 分類（小）
		if (conditions.containsKey(Param.PRODUCT3)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PRODUCT3))) {
				param.put(Param.PRODUCT3, (String) conditions
						.get(Param.PRODUCT3));
			}
		}

		// 発注伝票番号
		if (conditions.containsKey(Param.PO_SLIP_ID)) {
			if (StringUtil.hasLength((String) conditions.get(Param.PO_SLIP_ID))) {
				param.put(Param.PO_SLIP_ID, new Integer((String)conditions.get(Param.PO_SLIP_ID)));
			}
		}

		if (StringUtil.hasLength(sortColumn)) {
			param.put(Param.SORT_COLUMN, StringUtil
					.convertColumnName(sortColumn));
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER, Constants.SQL.DESC);
		}

		// 表示件数を設定する
		if (conditions.containsKey(Param.ROW_COUNT)) {
			param.put(Param.ROW_COUNT, conditions
					.get(Param.ROW_COUNT));
		}

		// オフセットを設定する
		if (conditions.containsKey(Param.OFFSET_ROW)) {
			param.put(Param.OFFSET_ROW, conditions.get(Param.OFFSET_ROW));
		}
		return param;
	}

	/**
	 * 委託入出庫区分に関わる検索条件パラメータを設定します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 */
	private void setEntrustEadCategoryCondition(Map<String, Object> conditions,
			Map<String, Object> param) {
		// 委託入出庫区分
		List<String> categoryList = new ArrayList<String>();

		Boolean check = (Boolean) conditions
				.get(Param.ENTRUST_EAD_CATEGORY_ENTER);
		if (check != null && check.booleanValue()) {
			categoryList.add(CategoryTrns.ENTRUST_EAD_CATEGORY_ENTER);
		}

		check = (Boolean) conditions.get(Param.ENTRUST_EAD_CATEGORY_DISPATCH);
		if (check != null && check.booleanValue()) {
			categoryList.add(CategoryTrns.ENTRUST_EAD_CATEGORY_DISPATCH);
		} else {
			check = (Boolean) conditions
					.get(Param.ENTRUST_EAD_CATEGORY_DISPATCH_NO_PRINT);
			if (check != null && check.booleanValue()) {
				param.put(Param.ENTRUST_EAD_CATEGORY_DISPATCH_NO_PRINT,
						Boolean.TRUE);
			}
		}

		if (categoryList.size() > 0) {
			param.put(Param.ENTRUST_EAD_CATEGORY, categoryList);
		}
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.ENTRUST_EAD_SLIP_ID, null);
		param.put(Param.ENTRUST_EAD_DATE, null);
		param.put(Param.ENTRUST_EAD_DATE_FROM, null);
		param.put(Param.ENTRUST_EAD_DATE_TO, null);
		param.put(Param.ENTRUST_EAD_ANNUAL, null);
		param.put(Param.ENTRUST_EAD_MONTHLY, null);
		param.put(Param.ENTRUST_EAD_YM, null);
		param.put(Param.USER_ID, null);
		param.put(Param.USER_NAME, null);
		param.put(Param.ENTRUST_EAD_CATEGORY, null);
		param.put(Param.ENTRUST_EAD_CATEGORY_ENTER, null);
		param.put(Param.ENTRUST_EAD_CATEGORY_DISPATCH, null);
		param.put(Param.ENTRUST_EAD_CATEGORY_DISPATCH_NO_PRINT, null);
		param.put(Param.REMARKS, null);
		param.put(Param.PO_SLIP_ID, null);
		param.put(Param.ENTRUST_EAD_LINE_ID, null);
		param.put(Param.LINE_NO, null);
		param.put(Param.PRODUCT_CODE, null);
		param.put(Param.PRODUCT_ABSTRACT, null);
		param.put(Param.QUANTITY, null);
		param.put(Param.PO_LINE_ID, null);
		param.put(Param.REL_ENTRUST_EAD_LINE_ID, null);
		param.put(Param.SUPPLIER_CODE, null);
		param.put(Param.SUPPLIER_NAME, null);
		param.put(Param.PRODUCT1, null);
		param.put(Param.PRODUCT2, null);
		param.put(Param.PRODUCT3, null);
		param.put(Param.ROW_COUNT, null);
		param.put(Param.OFFSET_ROW, null);
		param.put(Param.SORT_ORDER, null);
		param.put(Param.SORT_COLUMN, null);
		param.put(Param.ENTRUST_EAD_CATEGORY_ID,
				Categories.ENTRUST_EAD_CATEGORY);
		param.put(Param.ENTRUST_EAD_CATEGORY_DISPATCH,
				CategoryTrns.ENTRUST_EAD_CATEGORY_DISPATCH);
		param.put(Param.SORT_COLUMN_SLIP, null);
		param.put(Param.SORT_COLUMN_LINE, null);
		return param;
	}

}
