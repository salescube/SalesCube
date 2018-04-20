/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadLineTrn;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.join.EadSlipLineJoin;
import jp.co.arkinfosys.entity.join.StockQuantity;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 入出庫伝票・明細行サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class EadService extends AbstractService<EadSlipTrn> {

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String EAD_SLIP_ID = "eadSlipId";
		public static final String EAD_DATE = "eadDate";
		public static final String EAD_ANNUAL = "eadAnnual";
		public static final String EAD_MONTHLY = "eadMonthly";
		public static final String EAD_YM = "eadYm";
		public static final String USER_ID = "userId";
		public static final String USER_NAME = "userName";
		public static final String EAD_SLIP_CATEGORY = "eadSlipCategory";
		public static final String EAD_CATEGORY = "eadCategory";
		public static final String REMARKS = "remarks";
		public static final String SRC_FUNC = "srcFunc";
		public static final String SALES_SLIP_ID = "salesSlipId";
		public static final String SUPPLIER_SLIP_ID = "supplierSlipId";
		public static final String MOVE_DEPOSIT_SLIP_ID = "moveDepositSlipId";
		public static final String STOCK_PDATE = "stockPdate";
		public static final String EAD_LINE_ID = "eadLineId";
		public static final String LINE_NO = "lineNo";
		public static final String PRODUCT_CODE = "productCode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String RACK_CODE = "rackCode";
		public static final String RACK_NAME = "rackName";
		public static final String QUANTITY = "quantity";
		public static final String SALES_LINE_ID = "salesLineId";
		public static final String SUPPLIER_LINE_ID = "supplierLineId";

		private static final String RACK_CATEGORY = "rackCategory";
		private static final String E_CATEGORY = "eCategory";
		private static final String D_CATEGORY = "dCategory";

		public static final String EAD_SLIP_CATEGORY_ID = "eadSlipCategoryId";
		public static final String EAD_CATEGORY_ID = "eadCategoryId";
		public static final String SLIP_ID = "slipId";
		public static final String NO_TARGET = "noTarget";
		public static final String NO_TARGET_EAD_CATEGORY = "noTargetEadSlipCategory";
		public static final String NO_TARGET_SRC_FUNC = "noTargetSrcFunc";
		public static final String SEARCH_TARGET = "searchTarget";
		public static final String SRC_FUNC_STOCK = "srcFuncStock";
		public static final String SRC_FUNC_STOCKTRANSFER = "srcFuncStockTransfer";
		public static final String SRC_FUNC_SALES = "srcFuncSales";
		public static final String SRC_FUNC_PURCHASE = "srcFuncPurchase";
		public static final String SRC_SLIP_ID = "srcSlipId";
		public static final String EAD_DATE_FROM = "eadDateFrom";
		public static final String EAD_DATE_TO = "eadDateTo";
		public static final String SUPPLIER_CODE = "supplierCode";
		public static final String SUPPLIER_NAME = "supplierName";
		public static final String PRODUCT1 = "product1";
		public static final String PRODUCT2 = "product2";
		public static final String PRODUCT3 = "product3";
		public static final String SORT_ORDER = "sortOrder";
		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET_ROW = "offsetRow";

		public static final String SORT_ORDER_ASC = "sortOrderAsc";
		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_COLUMN_SLIP = "sortColumnSlip";
		public static final String SORT_COLUMN_LINE = "sortColumnLine";
		public static final String SORT_COLUMN_SRC_FUNC = "sortColumnSrcFunc";
		public static final String SORT_COLUMN_PRODUCT_ABSTRACT = "sortColumnProductAbstract";
	}

	/**
	 *
	 * カラム定義クラスです.
	 */
	public static class Column {
		// 伝票
		public static final String SRC_FUNC = "SRC_FUNC";
		public static final String EAD_SLIP_ID = "EAD_SLIP_ID";
		public static final String EAD_DATE = "EAD_DATE";
		public static final String STOCK_PDATE = "STOCK_PDATE";
		public static final String EAD_SLIP_CATEGORY = "EAD_SLIP_CATEGORY";
		public static final String EAD_CATEGORY = "EAD_CATEGORY";
		public static final String USER_NAME = "USER_NAME";

		// 明細
		public static final String PRODUCT_CODE = "PRODUCT_CODE";
		public static final String PRODUCT_NAME = "PRODUCT_NAME";
		public static final String RACK_CODE = "RACK_CODE";
		public static final String QUANTITY = "QUANTITY";
	}

	/**
	 *
     * テーブル名定義クラスです.
	 *
	 */
	public static class Table {
		/** テーブル名：入出庫伝票 */
		public static final String EAD_SLIP_TRN = "EAD_SLIP_TRN";
		/** テーブル名：入出庫伝票明細 */
		public static final String EAD_LINE_TRN = "EAD_LINE_TRN";
		/** テーブル名：入出庫伝票履歴 */
		public static final String EAD_SLIP_TRN_HIST = "EAD_SLIP_TRN_HIST";
		/** テーブル名：入出庫伝票明細履歴 */
		public static final String EAD_LINE_TRN_HIST = "EAD_LINE_TRN_HIST";
	}

	/**
	 * 入出庫伝票番号を指定して入出庫伝票情報を取得します.
	 * @param eadSlipId 入出庫伝票番号
	 * @return 入出庫伝票エンティティ
	 * @throws ServiceException
	 */
	public EadSlipTrn findSlipByEadSlipId(Integer eadSlipId)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.EAD_SLIP_ID, eadSlipId);

			return this.selectBySqlFile(EadSlipTrn.class,
					"ead/FindSlipByEadSlipId.sql", param).getSingleResult();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 売上伝票番号を指定して入出庫伝票情報を取得します.
	 * @param salesSlipId 売上伝票番号
	 * @return 入出庫伝票情報
	 * @throws ServiceException
	 */
	public EadSlipTrn findSlipBySalesSlipId(Integer salesSlipId)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.SALES_SLIP_ID, salesSlipId);

			return this.selectBySqlFile(EadSlipTrn.class,
					"ead/FindSlipBySalesSlipId.sql", param).getSingleResult();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 仕入伝票番号を指定して入出庫伝票情報を取得します.
	 * @param supplierSlipId 仕入伝票番号
	 * @return 入出庫伝票情報
	 * @throws ServiceException
	 */
	public EadSlipTrn findSlipBySupplierSlipId(Integer supplierSlipId)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.SUPPLIER_SLIP_ID, supplierSlipId);

			return this.selectBySqlFile(EadSlipTrn.class,
					"ead/FindSlipBySupplierSlipId.sql", param).getSingleResult();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票番号を指定して入出庫明細行情報リストを取得します.
	 * @param eadSlipId 入出庫伝票番号
	 * @return 入出庫明細行エンティティリスト
	 * @throws ServiceException
	 */
	public List<EadLineTrn> findLineByEadSlipId(Integer eadSlipId)
			throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.EAD_SLIP_ID, eadSlipId);

			return this.selectBySqlFile(EadLineTrn.class,
					"ead/FindLineByEadSlipId.sql", param).getResultList();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票と明細行を登録します.
	 * <p>
	 * フレームワーク化に伴い、伝票と明細行の登録は別々に呼び出されるようになりました.<br>
	 * 本メソッドは既存の非フレームワーク化処理との互換性のために残してあります.
	 * </p>
	 *
	 * @param eadSlipTrnDto 入出庫伝票DTO
	 * @throws ServiceException
	 * @deprecated 推奨されません.フレームワークから呼び出す場合は{@link EadService#insertSlip(EadSlipTrn eadSlipTrn) insertSlip}と{@link EadService#insertLine(EadLineTrn eadLineTrn) insertLine}を使用して下さい.
	 */
	public void insertSlipAndLine(EadSlipTrnDto eadSlipTrnDto)
			throws ServiceException {
		try {
			// 入出庫伝票の処理
			EadSlipTrn eadSlipTrn = Beans.createAndCopy(EadSlipTrn.class,
					eadSlipTrnDto).execute();

			// Insert
			insertSlip(eadSlipTrn);

			// 入出庫伝票明細の処理
			for (EadLineTrnDto eadLineTrnDto : eadSlipTrnDto.getLineDtoList()) {
				// 商品コードに入力のない行は処理しない
				if (!StringUtil.hasLength(eadLineTrnDto.productCode)) {
					continue;
				}
				EadLineTrn eadLineTrn = Beans.createAndCopy(EadLineTrn.class,
						eadLineTrnDto).execute();

				// Insert
				insertLine(eadLineTrn);
			}
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票を登録します.
	 * @param eadSlipTrn 入出庫伝票エンティティ
	 * @return 登録件数.
	 * @throws ServiceException
	 */
	public int insertSlip(EadSlipTrn eadSlipTrn) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createSlipSqlParam(eadSlipTrn);
			return this.updateBySqlFile("ead/InsertSlip.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票明細行を登録します.
	 * @param eadLineTrn 入出庫伝票明細行エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	public int insertLine(EadLineTrn eadLineTrn) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createLineSqlParam(eadLineTrn);
			return this.updateBySqlFile("ead/InsertLine.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票を更新します.
	 * @param eadSlipTrn 入出庫伝票エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int updateSlip(EadSlipTrn eadSlipTrn) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createSlipSqlParam(eadSlipTrn);
			return this.updateBySqlFile("ead/UpdateSlip.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票明細行を更新します.
	 * @param eadLineTrn 入出庫伝票明細行エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int updateLine(EadLineTrn eadLineTrn) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createLineSqlParam(eadLineTrn);
			return this.updateBySqlFile("ead/UpdateLine.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票番号を指定して入出庫伝票を削除します.<br>
	 * 伝票削除情報の更新も行います.
	 * @param eadSlipId 入出庫伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteSlipByEadSlipId(Integer eadSlipId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.EAD_SLIP_ID, eadSlipId);

			super.updateAudit(EadSlipTrn.TABLE_NAME,
					new String[] { Param.EAD_SLIP_ID },
					new Object[] { eadSlipId });

			return this.updateBySqlFile("ead/DeleteSlipByEadSlipId.sql", param)
					.execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票明細行を削除します.<br>
	 * 明細行削除情報の更新も行います.
	 * @param eadLineId 入出庫伝票行ID
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int deleteLineByEadLineId(Integer eadLineId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.EAD_LINE_ID, eadLineId);

			super.updateAudit(EadLineTrn.TABLE_NAME,
					new String[] { Param.EAD_LINE_ID },
					new Object[] { eadLineId });

			return this.updateBySqlFile("ead/DeleteLineByEadLineId.sql", param)
					.execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票番号を指定して入出庫伝票明細行を削除します.
	 * @param eadSlipId 入出庫伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteLineByEadSlipId(String eadSlipId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.EAD_SLIP_ID, eadSlipId);

			return this.updateBySqlFile("ead/DeleteLineByEadSlipId.sql", param)
					.execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 伝票作成時のSQLパラメータを作成します.
	 * @param eadSlipTrn 入出庫伝票エンティティ
	 * @return 伝票作成時のSQLパラメータ
	 */
	protected Map<String, Object> createSlipSqlParam(EadSlipTrn eadSlipTrn) {
		Map<String, Object> param = super.createSqlParam();
		param.put(EadService.Param.EAD_SLIP_ID, eadSlipTrn.eadSlipId);
		param.put(EadService.Param.EAD_DATE, eadSlipTrn.eadDate);
		param.put(EadService.Param.EAD_ANNUAL, eadSlipTrn.eadAnnual);
		param.put(EadService.Param.EAD_MONTHLY, eadSlipTrn.eadMonthly);
		param.put(EadService.Param.EAD_YM, eadSlipTrn.eadYm);
		param.put(EadService.Param.USER_ID, eadSlipTrn.userId);
		param.put(EadService.Param.USER_NAME, eadSlipTrn.userName);
		param.put(EadService.Param.EAD_SLIP_CATEGORY,
				eadSlipTrn.eadSlipCategory);
		param.put(EadService.Param.EAD_CATEGORY, eadSlipTrn.eadCategory);
		param.put(EadService.Param.REMARKS, eadSlipTrn.remarks);
		param.put(EadService.Param.SRC_FUNC, eadSlipTrn.srcFunc);
		param.put(EadService.Param.SALES_SLIP_ID, eadSlipTrn.salesSlipId);
		param.put(EadService.Param.SUPPLIER_SLIP_ID, eadSlipTrn.supplierSlipId);
		param.put(EadService.Param.MOVE_DEPOSIT_SLIP_ID,
				eadSlipTrn.moveDepositSlipId);
		param.put(EadService.Param.STOCK_PDATE, eadSlipTrn.stockPdate);
		return param;
	}

	/**
	 * 明細作成時のSQLパラメータを作成します.
	 * @param eadLineTrn 入出庫伝票エンティティ
	 * @return 明細作成時のSQLパラメータ
	 */
	protected Map<String, Object> createLineSqlParam(EadLineTrn eadLineTrn) {
		Map<String, Object> param = super.createSqlParam();
		param.put(EadService.Param.EAD_LINE_ID, eadLineTrn.eadLineId);
		param.put(EadService.Param.EAD_SLIP_ID, eadLineTrn.eadSlipId);
		param.put(EadService.Param.LINE_NO, eadLineTrn.lineNo);
		param.put(EadService.Param.PRODUCT_CODE, eadLineTrn.productCode);
		param.put(EadService.Param.PRODUCT_ABSTRACT, eadLineTrn.productAbstract);
		param.put(EadService.Param.RACK_CODE, eadLineTrn.rackCode);
		param.put(EadService.Param.RACK_NAME, eadLineTrn.rackName);
		param.put(EadService.Param.QUANTITY, eadLineTrn.quantity);
		param.put(EadService.Param.REMARKS, eadLineTrn.remarks);
		param.put(EadService.Param.SALES_LINE_ID, eadLineTrn.salesLineId);
		param.put(EadService.Param.SUPPLIER_LINE_ID, eadLineTrn.supplierLineId);
		return param;
	}

	/**
	 * 商品コードを指定して、締処理されていない（当月分の）入庫数をカウントして取得します.
	 *
	 * @param productCode 商品コード
	 * @param ownRack 自社棚か否か
	 * @return 締処理されていない入庫数
	 * @throws ServiceException
	 */
	public int countUnclosedQuantityByProductCode(String productCode,
			boolean ownRack) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();
			// 商品コード
			params.put(EadService.Param.PRODUCT_CODE, productCode);

			// 入出庫区分
			params.put(EadService.Param.E_CATEGORY,
					CategoryTrns.EAD_CATEGORY_ENTER);
			params.put(EadService.Param.D_CATEGORY,
					CategoryTrns.EAD_CATEGORY_DISPATCH);

			if (ownRack) {
				// 自社棚
				params.put(RackService.Param.RACK_CATEGORY,
						CategoryTrns.RACK_CATEGORY_OWN);
			} else {
				// 預け棚
				params.put(RackService.Param.RACK_CATEGORY,
						CategoryTrns.RACK_CATEGORY_ENTRUST);
			}

			BeanMap result = this.selectBySqlFile(BeanMap.class,
					"ead/CountUnclosedQuantityByProductCode.sql", params)
					.getSingleResult();

			if (result != null && result.containsKey(EadService.Param.QUANTITY)) {
				return ((Number) result.get(EadService.Param.QUANTITY))
						.intValue();
			}
			return 0;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果リストを返します.
	 *
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 入出庫伝票と入出庫伝票明細行エンティティのリスト
	 * @throws ServiceException
	 */
	public List<EadSlipLineJoin> findEadSlipByCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			// ソートカラムを設定する
			if (Param.SRC_FUNC.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SRC_FUNC, Column.SRC_FUNC);
			} else if (Param.EAD_SLIP_ID.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.EAD_SLIP_ID);
			} else if (Param.EAD_DATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.EAD_DATE);
			} else if (Param.STOCK_PDATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.STOCK_PDATE);
			} else if (Param.EAD_SLIP_CATEGORY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.EAD_SLIP_CATEGORY);
			} else if (Param.EAD_CATEGORY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.EAD_CATEGORY);
			} else if (Param.USER_NAME.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.USER_NAME);
			} else if (Param.PRODUCT_CODE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.PRODUCT_CODE);
			} else if (Param.PRODUCT_ABSTRACT.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_PRODUCT_ABSTRACT, Column.PRODUCT_NAME);
			} else if (Param.RACK_CODE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.RACK_CODE);
			} else if (Param.QUANTITY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.QUANTITY);
			}

			// ソートオーダーを設定する
			if (sortOrderAsc) {
				param.put(Param.SORT_ORDER, Constants.SQL.ASC);
			} else {
				param.put(Param.SORT_ORDER, Constants.SQL.DESC);
			}

			return this.selectBySqlFile(EadSlipLineJoin.class,
					"ead/FindSlipByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果件数を返します.
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
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"ead/FindSlipCntByCondition.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果リストを返します.
	 *
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 入出庫伝票と入出庫伝票明細行エンティティのリスト
	 * @throws ServiceException
	 */
	public List<EadSlipLineJoin> findEadSlipLineByCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			// ソートカラムを設定する
			if (Param.SRC_FUNC.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SRC_FUNC, Column.SRC_FUNC);
			} else if (Param.EAD_SLIP_ID.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.EAD_SLIP_ID);
			} else if (Param.EAD_DATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.EAD_DATE);
			} else if (Param.STOCK_PDATE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.STOCK_PDATE);
			} else if (Param.EAD_SLIP_CATEGORY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.EAD_SLIP_CATEGORY);
			} else if (Param.EAD_CATEGORY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.EAD_CATEGORY);
			} else if (Param.USER_NAME.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_SLIP, Column.USER_NAME);
			} else if (Param.PRODUCT_CODE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.PRODUCT_CODE);
			} else if (Param.PRODUCT_ABSTRACT.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_PRODUCT_ABSTRACT, Column.PRODUCT_NAME);
			} else if (Param.RACK_CODE.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.RACK_CODE);
			} else if (Param.QUANTITY.equals(sortColumn)) {
				param.put(Param.SORT_COLUMN_LINE, Column.QUANTITY);
			}

			// ソートオーダーを設定する
			if (sortOrderAsc) {
				param.put(Param.SORT_ORDER, Constants.SQL.ASC);
			} else {
				param.put(Param.SORT_ORDER, Constants.SQL.DESC);
			}

			return this.selectBySqlFile(EadSlipLineJoin.class,
					"ead/FindSlipLineByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果件数を返します.
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
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"ead/FindSlipLineCntByCondition.sql", param)
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
			Map<String, Object> conditions, Map<String, Object> param) {
		// 入出庫区分の区分コードを設定
		param.put(Param.EAD_SLIP_CATEGORY_ID, Categories.EAD_SLIP_CATEGORY);
		param.put(Param.EAD_CATEGORY_ID, Categories.EAD_CATEGORY);

		// 対象外(入出庫区分=入庫 かつ 登録機能=在庫移動)
		if (conditions.containsKey(Param.NO_TARGET)) {
			param.put(Param.NO_TARGET, Param.NO_TARGET);
			param.put(Param.NO_TARGET_EAD_CATEGORY,
					CategoryTrns.EAD_CATEGORY_ENTER);
			param.put(Param.NO_TARGET_SRC_FUNC,
					Constants.SRC_FUNC.STOCK_TRANSFER);
		}

		// 登録元伝票番号
		if (conditions.containsKey(Param.SRC_SLIP_ID)) {
			if (StringUtil.hasLength((String) conditions.get(Param.SRC_SLIP_ID))) {
				param.put(Param.SRC_SLIP_ID, new Integer((String) conditions.get(Param.SRC_SLIP_ID)));
			}
		}

		// 登録元機能
		List<String> srcFuncList = new ArrayList<String>();
		if (conditions.containsKey(Param.SRC_FUNC_STOCK)) {
			if ((Boolean) conditions.get(Param.SRC_FUNC_STOCK)) {
				srcFuncList.add(Constants.SRC_FUNC.STOCK);
			}
		}
		if (conditions.containsKey(Param.SRC_FUNC_STOCKTRANSFER)) {
			if ((Boolean) conditions.get(Param.SRC_FUNC_STOCKTRANSFER)) {
				srcFuncList.add(Constants.SRC_FUNC.STOCK_TRANSFER);
			}
		}
		if (conditions.containsKey(Param.SRC_FUNC_SALES)) {
			if ((Boolean) conditions.get(Param.SRC_FUNC_SALES)) {
				srcFuncList.add(Constants.SRC_FUNC.SALES);
			}
		}
		if (conditions.containsKey(Param.SRC_FUNC_PURCHASE)) {
			if ((Boolean) conditions.get(Param.SRC_FUNC_PURCHASE)) {
				srcFuncList.add(Constants.SRC_FUNC.PURCHASE);
			}
		}
		if (srcFuncList.size() != 0) {
			param.put(Param.SRC_FUNC, srcFuncList);
		}

		// 担当者名
		if (conditions.containsKey(Param.USER_NAME)) {
			if (StringUtil.hasLength((String) conditions.get(Param.USER_NAME))) {
				param.put(Param.USER_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(Param.USER_NAME)));
			}
		}

		// 入出庫伝票区分
		if (conditions.containsKey(Param.EAD_SLIP_CATEGORY)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.EAD_SLIP_CATEGORY))) {
				param.put(Param.EAD_SLIP_CATEGORY, (String) conditions
						.get(Param.EAD_SLIP_CATEGORY));
			}
		}

		// 入出庫日（開始）
		if (conditions.containsKey(Param.EAD_DATE_FROM)) {
			if (StringUtil.hasLength((String) conditions
					.get(Param.EAD_DATE_FROM))) {
				param.put(Param.EAD_DATE_FROM, (String) conditions
						.get(Param.EAD_DATE_FROM));
			}
		}

		// 入出庫日（開始）全角半角変換
		if (conditions.containsKey(Param.EAD_DATE_FROM)) {
			param.put(Param.EAD_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.EAD_DATE_FROM)));

		}

		// 入出庫日（終了）
		if (conditions.containsKey(Param.EAD_DATE_TO)) {
			if (StringUtil
					.hasLength((String) conditions.get(Param.EAD_DATE_TO))) {
				param.put(Param.EAD_DATE_TO, (String) conditions
						.get(Param.EAD_DATE_TO));
			}
		}

		// 入出庫日（終了）全角半角変換
		if (conditions.containsKey(Param.EAD_DATE_TO)) {
			param.put(Param.EAD_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.EAD_DATE_TO)));

		}

		// 理由
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

		// 棚番コード
		if (conditions.containsKey(Param.RACK_CODE)) {
			if (StringUtil.hasLength((String) conditions.get(Param.RACK_CODE))) {
				param.put(Param.RACK_CODE, super
						.createPrefixSearchCondition((String) conditions
								.get(Param.RACK_CODE)));
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

		// 売上伝票番号
		if (conditions.containsKey(Param.SALES_SLIP_ID)) {
			if (StringUtil.hasLength((String) conditions.get(Param.SALES_SLIP_ID))) {
				param.put(Param.SALES_SLIP_ID, (String) conditions
						.get(Param.SALES_SLIP_ID));
			}
		}

		// 売上伝票明細行番号
		if (conditions.containsKey(Param.SALES_LINE_ID)) {
			if (StringUtil.hasLength((String) conditions.get(Param.SALES_LINE_ID))) {
				param.put(Param.SALES_LINE_ID, (String) conditions
						.get(Param.SALES_LINE_ID));
			}
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
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.SEARCH_TARGET, null);
		param.put(Param.SRC_FUNC, null);
		param.put(Param.SRC_SLIP_ID, null);
		param.put(Param.EAD_SLIP_CATEGORY, null);
		param.put(Param.USER_ID, null);
		param.put(Param.EAD_DATE_FROM, null);
		param.put(Param.EAD_DATE_TO, null);
		param.put(Param.REMARKS, null);
		param.put(Param.PRODUCT_CODE, null);
		param.put(Param.PRODUCT_ABSTRACT, null);
		param.put(Param.SUPPLIER_CODE, null);
		param.put(Param.SUPPLIER_NAME, null);
		param.put(Param.RACK_CODE, null);
		param.put(Param.PRODUCT1, null);
		param.put(Param.PRODUCT2, null);
		param.put(Param.PRODUCT3, null);
		param.put(Param.SORT_ORDER, null);
		param.put(Param.SALES_SLIP_ID, null);
		param.put(Param.SALES_LINE_ID, null);
		param.put(Param.ROW_COUNT, null);
		param.put(Param.OFFSET_ROW, null);
		return param;
	}

	/**
	 * 商品コードと棚番コードをキーに、締処理されていない入庫数を取得します.
	 *
	 * @param productCode 商品コード
	 * @param rackCode 棚番コード
	 * @return 締処理されていない入庫数
	 * @throws ServiceException
	 */
	public int countUnclosedQuantityByProductCode(String productCode,
			String rackCode) throws ServiceException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(EadService.Param.PRODUCT_CODE, new String[] { productCode });
		param.put(EadService.Param.RACK_CODE, rackCode);
		param.put(EadService.Param.RACK_CATEGORY, null);

		List<StockQuantity> quantity = this
				.countUnclosedQuantityByProductCode(param);
		for (StockQuantity q : quantity) {
			if (q != null && q.quantity != null) {
				return q.quantity.intValue();
			}
		}
		return 0;
	}

	/**
	 * 商品コードをキーに、自社倉庫における締処理されていない入庫数を取得します.
	 *
	 * @param productCode 商品コード
	 * @return 自社倉庫における締処理されていない入庫数
	 * @throws ServiceException
	 */
	public int countUnclosedQuantityByProductCode(String productCode)
			throws ServiceException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(EadService.Param.PRODUCT_CODE, new String[] { productCode });
		param.put(EadService.Param.RACK_CODE, null);
		param.put(EadService.Param.RACK_CATEGORY,
				CategoryTrns.RACK_CATEGORY_OWN);

		List<StockQuantity> quantity = this
				.countUnclosedQuantityByProductCode(param);
		for (StockQuantity q : quantity) {
			if (q != null && q.quantity != null) {
				return q.quantity.intValue();
			}
		}
		return 0;
	}

	/**
	 * 商品コードをキーに、委託在庫数を取得します.
	 *
	 * @param productCode 商品コード
	 * @return 委託在庫数
	 * @throws ServiceException
	 */
	public int countEntrustQuantityByProductCode(String productCode)
			throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();
			params.put(EadService.Param.PRODUCT_CODE, productCode);

			List<StockQuantity> quantity = this.selectBySqlFile(
					StockQuantity.class,
					"ead/CountEntrustQuantityByProductCode.sql", params)
					.getResultList();
			for (StockQuantity q : quantity) {
				if (q != null && q.quantity != null) {
					return q.quantity.intValue();
				}
			}
			return 0;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 商品コードをキーに、自社倉庫における締処理されていない入庫数を取得します.
	 *
	 * @param productCode 商品コード
	 * @return 自社倉庫における締処理されていない入庫数
	 * @throws ServiceException
	 */
	public List<StockQuantity> countUnclosedQuantityByProductCode(
			String[] productCode) throws ServiceException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(EadService.Param.PRODUCT_CODE, productCode);
		param.put(EadService.Param.RACK_CODE, null);
		param.put(EadService.Param.RACK_CATEGORY,
				CategoryTrns.RACK_CATEGORY_OWN);

		return this.countUnclosedQuantityByProductCode(param);
	}

	/**
	 * 検索条件を指定して、締処理されていない入庫数を取得します.
	 * @param conditions 検索条件
	 * @return 締処理されていない入庫数
	 * @throws ServiceException
	 */
	private List<StockQuantity> countUnclosedQuantityByProductCode(
			Map<String, Object> conditions) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();

			// 棚区分コード
			if (conditions.containsKey(EadService.Param.RACK_CATEGORY)) {
				params.put(EadService.Param.RACK_CATEGORY, conditions
						.get(EadService.Param.RACK_CATEGORY));
			}

			// 棚コード
			if (conditions.containsKey(EadService.Param.RACK_CODE)) {
				params.put(EadService.Param.RACK_CODE, conditions
						.get(EadService.Param.RACK_CODE));
			}

			// 商品コード
			if (conditions.containsKey(EadService.Param.PRODUCT_CODE)) {
				params.put(EadService.Param.PRODUCT_CODE, conditions
						.get(EadService.Param.PRODUCT_CODE));
			}

			// 入出庫区分
			params.put(EadService.Param.E_CATEGORY,
					CategoryTrns.EAD_CATEGORY_ENTER);
			params.put(EadService.Param.D_CATEGORY,
					CategoryTrns.EAD_CATEGORY_DISPATCH);

			return this.selectBySqlFile(StockQuantity.class,
					"ead/CountUnclosedQuantityByProductCode.sql", params)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 棚番と商品コードと在庫締処理日をキーに、締処理されていない入出庫数を取得します.
	 * @param rackCode 棚番
	 * @param productCode 商品コード
	 * @param cutoffdate 在庫締処理日
	 * @return 締処理されていない入出庫数
	 * @throws ServiceException
	 */
	public List<EadSlipLineJoin> countUnclosedQuantityByCodeAndPdate(
			String rackCode, String productCode, String cutoffdate) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();
			// 棚コード
			if (rackCode != null) {
				params.put(EadService.Param.RACK_CODE, rackCode);
			}
			// 商品コード
			if (productCode != null) {
				params.put(EadService.Param.PRODUCT_CODE, productCode);
			}
			// 在庫締処理日
			params.put(Param.EAD_DATE, cutoffdate);

			return this.selectBySqlFile(EadSlipLineJoin.class,
					"ead/CountUnclosedQuantityByCodeAndPdate.sql", params)
					.getResultList();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 入出庫伝票の在庫締処理日の更新を行います.
	 * @param stockPdate 締処理日
	 * @param eadSlipId 入出庫伝票番号
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int updateSlipStockPdateByEadSlipId(String stockPdate, Integer eadSlipId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.STOCK_PDATE, stockPdate);
			param.put(Param.EAD_SLIP_ID, eadSlipId);
			return this.updateBySqlFile("ead/UpdateSlipStockPdateByEadSlipId.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 在庫締処理日を指定して、入出庫伝票の結果リストを返します.
	 * @param rackCode 棚番
	 * @param productCode 商品コード
	 * @param eadDate 入出庫日
	 * @param stockPdate 在庫締処理日
	 * @return 入出庫伝票の結果リスト
	 * @throws ServiceException
	 */
	public List<EadSlipTrn> findEadSlipByCodeAndPdate(
			String rackCode, String productCode, String eadDate, String stockPdate) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			if(StringUtil.hasLength(rackCode)) {
				param.put(Param.RACK_CODE, rackCode);
			}
			if(StringUtil.hasLength(productCode)) {
				param.put(Param.PRODUCT_CODE, productCode);
			}
			if(StringUtil.hasLength(eadDate)) {
				param.put(Param.EAD_DATE, eadDate);
			}
			param.put(Param.STOCK_PDATE, stockPdate);
			return this.selectBySqlFile(EadSlipTrn.class,
					"ead/FindSlipByCodeAndPdate.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 在庫締処理日を指定して、棚番と商品コードの結果リストを返します.
	 * @param eadDate 入出庫日
	 * @param stockPdate 在庫締処理日
	 * @return 棚番と商品コードの結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findRackAndProductByStockPdate(String eadDate, String stockPdate)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			if(StringUtil.hasLength(eadDate)) {
				param.put(Param.EAD_DATE, eadDate);
			}
			param.put(Param.STOCK_PDATE, stockPdate);
			return this.selectBySqlFile(BeanMap.class,
					"ead/FindRackAndProductByStockPdate.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
