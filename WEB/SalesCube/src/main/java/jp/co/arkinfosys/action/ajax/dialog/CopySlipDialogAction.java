/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.CommonAjaxResources;
import jp.co.arkinfosys.common.ConfigUtil;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.SlipStatusCategoryTrns;
import jp.co.arkinfosys.entity.join.POrderSlipLineJoin;
import jp.co.arkinfosys.form.ajax.dialog.CopySlipDepositConditionForm;
import jp.co.arkinfosys.form.ajax.dialog.CopySlipDialogForm;
import jp.co.arkinfosys.form.ajax.dialog.CopySlipEntrustPOrderConditionForm;
import jp.co.arkinfosys.form.ajax.dialog.CopySlipEstimateConditionForm;
import jp.co.arkinfosys.form.ajax.dialog.CopySlipPOrderConditionForm;
import jp.co.arkinfosys.form.ajax.dialog.CopySlipROrderConditionForm;
import jp.co.arkinfosys.form.ajax.dialog.CopySlipSalesConditionForm;
import jp.co.arkinfosys.form.ajax.dialog.CopySlipSupplierConditionForm;
import jp.co.arkinfosys.service.EstimateSheetService;
import jp.co.arkinfosys.service.ROrderService;
import jp.co.arkinfosys.service.deposit.SearchDepositService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.porder.SearchPOrderService;
import jp.co.arkinfosys.service.purchase.SearchPurchaseService;
import jp.co.arkinfosys.service.sales.SearchSalesService;
import jp.co.arkinfosys.service.stock.InputEntrustStockService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 伝票呼出ダイアログの表示・検索処理アクションです
 *
 * @author Ark Information Systems
 *
 */
public class CopySlipDialogAction extends AbstractDialogAction {

	/**
	 * 画面遷移用のマッピングを定義するクラスです.
	 */
	protected static class Mapping extends AbstractDialogAction.Mapping {
		/**
		 * 見積伝票の検索結果JSPパスです.
		 */
		public static final String ESTIMATE_RESULT = "result/estimate.jsp";

		/**
		 * 受注伝票の検索結果JSPパスです.
		 */
		public static final String RORDER_RESULT = "result/rorder.jsp";

		/**
		 * 売上伝票の検索結果JSPパスです.
		 */
		public static final String SALES_RESULT = "result/sales.jsp";

		/**
		 * 入金伝票の検索結果JSPパスです.
		 */
		public static final String DEPOSIT_RESULT = "result/deposit.jsp";

		/**
		 * 発注伝票の検索結果JSPパスです.
		 */
		public static final String PORDER_RESULT = "result/porder.jsp";

		/**
		 * 仕入伝票の検索結果JSPパスです.
		 */
		public static final String SUPPLIER_RESULT = "result/supplier.jsp";

		/**
		 * 発注伝票（委託）の検索結果JSPパスです.
		 */
		public static final String ENTRUST_PORDER_RESULT = "result/entrustPorder.jsp";
	}

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public CopySlipDialogForm copySlipDialogForm;

	/**
	 * 見積伝票に対するサービスクラスです.
	 */
	@Resource
	private EstimateSheetService estimateSheetService;

	/**
	 * 受注伝票に対するサービスクラスです.
	 */
	@Resource
	private ROrderService rorderService;

	/**
	 * 売上伝票に対するサービスクラスです.
	 */
	@Resource
	private SearchSalesService searchSalesService;

	/**
	 * 入金伝票に対するサービスクラスです.
	 */
	@Resource
	private SearchDepositService searchDepositService;

	/**
	 * 発注伝票に対するサービスクラスです.
	 */
	@Resource
	private SearchPOrderService searchPOrderService;

	/**
	 * 仕入伝票に対するサービスクラスです.
	 */
	@Resource
	private SearchPurchaseService searchPurchaseService;

	/**
	 * 委託入出庫伝票に対するサービスクラスです.
	 */
	@Resource
	private InputEntrustStockService inputEntrustStockService;

	/**
	 * 委託入出庫区分プルダウンの内容を初期化します.
	 */
	@Override
	protected void createList() throws ServiceException {
		this.copySlipDialogForm.entrustCategoryList = inputEntrustStockService.getCategoryList();
		this.copySlipDialogForm.entrustCategoryList.add(0, new LabelValueBean());
	}

	/**
	 * 伝票呼出ダイアログの検索処理を行うメソッドです.
	 *
	 * @return 検索結果のJPSパス
	 */
	@Execute(validator = true, validate = "validate", urlPattern = "search/{dialogId}", input = Mapping.ERROR_JSP)
	public String search() {
		SearchHelper<?, ?> helper = null;
		String jsp = null;

		try {
			if (CopySlipEstimateConditionForm.SLIP_NAME
					.equals(this.copySlipDialogForm.slipName)) {
				helper = new EstimateSearchHelper();
				jsp = Mapping.ESTIMATE_RESULT;
			} else if (CopySlipROrderConditionForm.SLIP_NAME
					.equals(this.copySlipDialogForm.slipName)) {
				helper = new ROrderSearchHelper();
				jsp = Mapping.RORDER_RESULT;
			} else if (CopySlipSalesConditionForm.SLIP_NAME
					.equals(this.copySlipDialogForm.slipName)) {
				helper = new SalesSearchHelper();
				jsp = Mapping.SALES_RESULT;
			} else if (CopySlipDepositConditionForm.SLIP_NAME
					.equals(this.copySlipDialogForm.slipName)) {
				helper = new DepositSearchHelper();
				jsp = Mapping.DEPOSIT_RESULT;
			} else if (CopySlipPOrderConditionForm.SLIP_NAME
					.equals(this.copySlipDialogForm.slipName)) {
				helper = new POrderSearchHelper();
				jsp = Mapping.PORDER_RESULT;
			} else if (CopySlipSupplierConditionForm.SLIP_NAME
					.equals(this.copySlipDialogForm.slipName)) {
				helper = new SupplierSearchHelper();
				jsp = Mapping.SUPPLIER_RESULT;
			} else if (CopySlipEntrustPOrderConditionForm.SLIP_NAME
					.equals(this.copySlipDialogForm.slipName)) {
				helper = new EntrustPOrderSearchHelper();
				jsp = Mapping.ENTRUST_PORDER_RESULT;
			}

			if (helper != null) {
				// 検索する
				this.copySlipDialogForm.searchResultList = helper.search();
				// 検索結果を表示用に処理する
				this.copySlipDialogForm.searchResultCount = this.copySlipDialogForm.searchResultList.size();
			}
		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}

		return jsp;
	}

	/**
	 * 各伝票毎の検索処理の違いを吸収する検索基底クラスです.
	 *
	 * @param <DTO> 検索結果DTOクラス
	 * @param <ENTITY> 検索対象のエンティティクラス
	 */
	private abstract class SearchHelper<DTO, ENTITY> {

		/**
		 * 伝票の検索を行う処理を行う抽象メソッドです.
		 *
		 * @return DTOクラスのリスト
		 * @throws ServiceException 検索エラー発生時
		 */
		public abstract List<DTO> search() throws ServiceException;

		/**
		 * エンティティクラスのリストをDTOクラスのリストに変換するメソッドです.<br>
		 * 同時に、コンフィグファイル appconfig.dicon で設定された検索結果の最大表示件数に従ってリストの要素数を限定します.
		 *
		 * @param rawList 検索結果のエンティティリスト
		 * @return 検索結果のDTOリスト
		 */
		@SuppressWarnings("unchecked")
		protected List<DTO> entytyToDto(List<ENTITY> rawList) {
			// 検索結果の最大表示件数を取得する
			int threshold = 100;
			Integer thresholdObj = (Integer) ConfigUtil
					.getConfigValue("SearchResultThreshold");
			if (thresholdObj != null) {
				threshold = thresholdObj.intValue();
			}

			List<ENTITY> tempList = rawList;
			if (tempList.size() > threshold) {
				// 検索結果件数の最大数をオーバー
				messages.add("resultThreshold", new ActionMessage(
						CommonAjaxResources.SEARCH_THRESHOLD_OVER, tempList
								.size(), threshold));
				ActionMessagesUtil.saveMessages(httpRequest, messages);

				// 表示件数を絞る
				tempList = rawList.subList(0, threshold);
			}

			// 表示用オブジェクトに変換
			List<DTO> resultList = new ArrayList<DTO>();
			Class<DTO> c = (Class<DTO>) this.getGenericDTOClass();
			for (ENTITY entity : tempList) {
				DTO dto = Beans.createAndCopy(c, entity).timestampConverter(
						Constants.FORMAT.DATE).dateConverter(
						Constants.FORMAT.DATE).execute();
				resultList.add(dto);
			}

			return resultList;
		}

		/**
		 * Genericsで指定されるDTOのクラスオブジェクトを取得するメソッドです.
		 *
		 * @return DTOのクラスオブジェクト
		 */
		private Type getGenericDTOClass() {
			Type t = this.getClass().getGenericSuperclass();
			if (t == null || !(t instanceof ParameterizedType)) {
				return null;
			}

			t = ((ParameterizedType) t).getActualTypeArguments()[0];
			if (t instanceof ParameterizedType) {
				return ((ParameterizedType) t).getRawType();
			}
			return t;
		}
	}

	/**
	 * 見積伝票の検索を行うクラスです.
	 */
	private class EstimateSearchHelper extends
			SearchHelper<HashMap<String, Object>, BeanMap> {
		public List<HashMap<String, Object>> search() throws ServiceException {
			Map<String, Object> conditions = Beans.createAndCopy(BeanMap.class,
					copySlipDialogForm.estimateCondition).excludesWhitespace()
					.lrTrim().execute();

			conditions.put(EstimateSheetService.Param.SORT_ORDER_ASC, false);
			conditions.put(EstimateSheetService.Param.SORT_COLUMN,
					EstimateSheetService.COLUMN_ESTIMATE_DATE);
			conditions = new HashMap<String, Object>(conditions);

			// 検索する(顧客が紐づかない見積伝票は対象外)
			List<BeanMap> rawList = estimateSheetService
					.findEstimateSheetFromCopySlipByCondition(conditions);
			return super.entytyToDto(rawList);
		}
	}

	/**
	 * 受注伝票の検索を行うクラスです.
	 */
	private class ROrderSearchHelper extends
			SearchHelper<HashMap<String, Object>, BeanMap> {
		public List<HashMap<String, Object>> search() throws ServiceException {
			Map<String, Object> conditions = Beans.createAndCopy(BeanMap.class,
					copySlipDialogForm.rorderCondition).excludesWhitespace()
					.lrTrim().execute();

			conditions.put(ROrderService.Param.SORT_ORDER_ASC, false);
			conditions.put(ROrderService.Param.SORT_COLUMN,
					ROrderService.Param.RO_DATE);
			conditions = new HashMap<String, Object>(conditions);

			// 検索する
			List<BeanMap> rawList = rorderService
					.findSlipByCondition(conditions);
			return super.entytyToDto(rawList);
		}
	}

	/**
	 * 売上伝票の検索を行うクラスです.
	 */
	private class SalesSearchHelper extends
			SearchHelper<HashMap<String, Object>, BeanMap> {
		public List<HashMap<String, Object>> search() throws ServiceException {
			Map<String, Object> conditions = Beans.createAndCopy(BeanMap.class,
					copySlipDialogForm.salesCondition).excludesWhitespace()
					.lrTrim().execute();

			conditions.put(SearchSalesService.Param.SORT_ORDER_ASC, false);
			conditions.put(SearchSalesService.Param.SORT_COLUMN,
					SearchSalesService.Param.SALES_DATE);
			conditions = new HashMap<String, Object>(conditions);

			List<BeanMap> rawList = searchSalesService
					.findSlipByCondition(conditions);
			return super.entytyToDto(rawList);
		}
	}

	/**
	 * 入金伝票の検索を行うクラスです.
	 */
	private class DepositSearchHelper extends
			SearchHelper<HashMap<String, Object>, BeanMap> {
		public List<HashMap<String, Object>> search() throws ServiceException {
			Map<String, Object> conditions = Beans.createAndCopy(BeanMap.class,
					copySlipDialogForm.depositCondition).excludesWhitespace()
					.lrTrim().execute();

			conditions.put(SearchDepositService.Param.SORT_ORDER_ASC, false);
			conditions.put(SearchDepositService.Param.SORT_COLUMN,
					SearchDepositService.Param.DEPOSIT_DATE);
			conditions = new HashMap<String, Object>(conditions);

			List<BeanMap> rawList = searchDepositService
					.findSlipByCondition(conditions);
			return super.entytyToDto(rawList);
		}
	}

	/**
	 * 発注伝票の検索を行うクラスです.
	 */
	private class POrderSearchHelper extends
			SearchHelper<HashMap<String, Object>, POrderSlipLineJoin> {
		public List<HashMap<String, Object>> search() throws ServiceException {
			Map<String, Object> conditions = Beans.createAndCopy(BeanMap.class,
					copySlipDialogForm.porderCondition).excludesWhitespace()
					.lrTrim().execute();
			conditions = new HashMap<String, Object>(conditions);

			List<POrderSlipLineJoin> rawList = searchPOrderService
					.findPOrderSlipByCondition(conditions,
							SearchPOrderService.Param.PO_DATE, false);

			List<HashMap<String, Object>> resultList = super
					.entytyToDto(rawList);
			for (HashMap<String, Object> record : resultList) {
				if (SlipStatusCategoryTrns.PO_SLIP_PROCESSED.equals(record
						.get(SearchPOrderService.Param.STATUS))) {
					record.put(SearchPOrderService.Param.STATUS, Boolean.TRUE);
				} else {
					record.put(SearchPOrderService.Param.STATUS, Boolean.FALSE);
				}
			}

			return resultList;
		}
	}

	/**
	 * 発注伝票（委託）の検索を行うクラスです.
	 */
	private class EntrustPOrderSearchHelper extends
			SearchHelper<HashMap<String, Object>, POrderSlipLineJoin> {
		public List<HashMap<String, Object>> search() throws ServiceException {
			Map<String, Object> conditions = Beans.createAndCopy(BeanMap.class,
					copySlipDialogForm.entrustPorderCondition)
					.excludesWhitespace().lrTrim().execute();
			conditions = new HashMap<String, Object>(conditions);

			String targetPoLineStatus = (String) conditions
					.get("targetPoLineStatus");

			// メイン画面の委託入出庫区分が入庫か出庫かによって、検索する委託発注伝票の条件を変える
			if (Constants.STATUS_PORDER_LINE.ORDERED.equals(targetPoLineStatus)) {
				// 委託入庫の場合は、委託発注残(仕入もされていない委託在庫発注伝票)を検索する
				conditions.put(SearchPOrderService.Param.ENTRUST_PO_REST,
						Boolean.TRUE);
			} else if (Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_MAKED
					.equals(targetPoLineStatus)) {
				// 委託出庫の場合は、委託入庫済みの委託在庫発注伝票を検索する
				conditions.put(SearchPOrderService.Param.ENTRUST_PO_MAKED,
						Boolean.TRUE);
			} else if (Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_DELIVERED
					.equals(targetPoLineStatus)) {
				// 仕入入力からの呼出の場合は、委託出庫済みの委託在庫発注伝票を検索する
				conditions.put(SearchPOrderService.Param.ENTRUST_PO_DELIVERED,
						Boolean.TRUE);
			} else {
				return null;
			}

			List<POrderSlipLineJoin> rawList = searchPOrderService
					.findPOrderSlipByCondition(conditions,
							SearchPOrderService.Param.PO_DATE, false);

			List<HashMap<String, Object>> resultList = super
					.entytyToDto(rawList);
			for (HashMap<String, Object> record : resultList) {
				if (SlipStatusCategoryTrns.PO_SLIP_PROCESSED.equals(record
						.get(SearchPOrderService.Param.STATUS))) {
					record.put(SearchPOrderService.Param.STATUS, Boolean.TRUE);
				} else {
					record.put(SearchPOrderService.Param.STATUS, Boolean.FALSE);
				}
			}

			return resultList;
		}
	}

	/**
	 * 仕入伝票の検索を行うクラスです.
	 */
	private class SupplierSearchHelper extends
			SearchHelper<HashMap<String, Object>, BeanMap> {

		public List<HashMap<String, Object>> search() throws ServiceException {
			Map<String, Object> conditions = Beans.createAndCopy(BeanMap.class,
					copySlipDialogForm.supplierCondition).excludesWhitespace()
					.lrTrim().execute();

			conditions.put(SearchPurchaseService.Param.SORT_ORDER_ASC, false);
			conditions.put(SearchPurchaseService.Param.SORT_COLUMN,
					SearchPurchaseService.Param.SUPPLIER_DATE);

			if (copySlipDialogForm.supplierCondition.unPaidFlag) {
				// 未払いのみ
				List<String> statusList = new ArrayList<String>();
				statusList.add(SlipStatusCategoryTrns.SUPPLIER_SLIP_UNPAID);
				conditions.put(SearchPurchaseService.Param.STATUS, statusList);
			}
			conditions = new HashMap<String, Object>(conditions);

			List<BeanMap> rawList = searchPurchaseService
					.findSlipByCondition(conditions);
			return super.entytyToDto(rawList);
		}

	}
}
