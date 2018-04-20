/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.sales;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.sales.OutputSalesSearchResultDto;
import jp.co.arkinfosys.dto.sales.SalesCategoryDto;
import jp.co.arkinfosys.entity.Delivery;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 *
 * 売上帳票発行検索サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class SearchOutputSalesReportService extends AbstractService<SalesSlipTrn> {


	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String SALES_DATE_FROM="salesDateFrom";
		public static final String SALES_DATE_TO="salesDateTo";
		public static final String RO_SLIP_ID="roSlipId";
		public static final String RO_SLIP_ID_FROM="roSlipIdFrom";
		public static final String RO_SLIP_ID_TO="roSlipIdTo";
		public static final String SALES_SLIP_ID_FROM="salesSlipIdFrom";
		public static final String SALES_SLIP_ID_TO="salesSlipIdTo";
		public static final String RECEPT_NO="receptNo";
		public static final String SALES_CATEGORY_LIST="salesCategoryList";
		public static final String EXCLUDING_OUTPUT_ALL="excludingOutputAll";
		public static final String BILL_PRINT_COUNT="billPrintCount";
		public static final String DELIVERY_PRINT_COUNT="deliveryPrintCount";
		public static final String TEMP_DELIVERY_PRINT_COUNT="tempDeliveryPrintCount";
		public static final String SHIPPING_PRINT_COUNT="shippingPrintCount";
		public static final String CUST_REL_CATEGORY = "custRelCategory";
		public static final String CUSTOMER_CODE = "customerCode";
		public static final String ROW_COUNT = "rowCount";

		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";
		public static final String OFFSET_ROW = "offsetRow";

		/**
		 * 取引区分：現金(固定値)
		 */
		public static final String CASH_CATEGORY = "cashCategory";

	}

	/**
	 * 検索条件を指定して検索結果件数を取得します.
	 * @param params 検索条件
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	public Integer getSearchResultCount(BeanMap params) throws ServiceException {
		try {
			return findSlipCntByCondition(params);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	/**
	 * 検索条件を指定して結果件数を取得します.
	 *
	 * @param conditions 検索条件
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	public Integer findSlipCntByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(Integer.class,
					"sales/FindSalesReportCntByConditions.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索結果の変換を行います.
	 *
	 * @param beanMapList 変換前検索結果(List<BeanMap>)
	 * @return 変換後検索結果(List<{@link OutputSalesSearchResultDto}>)
	 */
	public List<OutputSalesSearchResultDto> convertToDto(List<BeanMap> beanMapList) throws ServiceException {
		try {
			List<OutputSalesSearchResultDto> resultList = new ArrayList<OutputSalesSearchResultDto>();
			for (BeanMap resultMap : beanMapList) {
				// DTOに変換
				OutputSalesSearchResultDto dto =
					Beans.createAndCopy(OutputSalesSearchResultDto.class,resultMap)
					.dateConverter(Constants.FORMAT.DATE)
					.execute();
				setResultDispInfo(dto);
				resultList.add(dto);
			}

			return resultList;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.SALES_DATE_FROM, null);
		param.put(Param.SALES_DATE_TO, null);
		param.put(Param.RO_SLIP_ID_FROM, null);
		param.put(Param.RO_SLIP_ID_TO, null);
		param.put(Param.SALES_SLIP_ID_FROM, null);
		param.put(Param.SALES_SLIP_ID_TO, null);
		param.put(Param.RECEPT_NO, null);
		param.put(Param.SALES_CATEGORY_LIST, null);
		param.put(Param.BILL_PRINT_COUNT, null);
		param.put(Param.DELIVERY_PRINT_COUNT, null);
		param.put(Param.TEMP_DELIVERY_PRINT_COUNT, null);
		param.put(Param.SHIPPING_PRINT_COUNT, null);
		param.put(Param.EXCLUDING_OUTPUT_ALL, null);
		param.put(Param.SORT_COLUMN, null);
		param.put(Param.SORT_ORDER_ASC, true);
		param.put(Param.OFFSET_ROW, 0);
		param.put(Param.CUST_REL_CATEGORY, Constants.CUSTOMER_REL.DELIVERY);
		param.put(Param.CASH_CATEGORY, CategoryTrns.SALES_CM_CASH);
		return param;
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param conditions 検索条件
	 * @param param 検索条件パラメータ
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> setConditionParam(
			Map<String, Object> conditions, Map<String, Object> param) {

		// 売上日From
		if (conditions.containsKey(Param.SALES_DATE_FROM)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SALES_DATE_FROM))) {
				param.put(Param.SALES_DATE_FROM,(String)conditions.get(Param.SALES_DATE_FROM));
			}
		}

		// 売上日From全角半角変換
		if (conditions.containsKey(Param.SALES_DATE_FROM)) {
			param.put(Param.SALES_DATE_FROM, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.SALES_DATE_FROM)));

		}

		// 売上日To
		if (conditions.containsKey(Param.SALES_DATE_TO)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SALES_DATE_TO))) {
				param.put(Param.SALES_DATE_TO,(String)conditions.get(Param.SALES_DATE_TO));
			}
		}

		// 売上日To全角半角変換
		if (conditions.containsKey(Param.SALES_DATE_TO)) {
			param.put(Param.SALES_DATE_TO, StringUtil.zenkakuNumToHankaku((String) conditions
					.get(Param.SALES_DATE_TO)));

		}

		// 発注番号From
		if (conditions.containsKey(Param.RO_SLIP_ID_FROM)) {
			if (StringUtil.hasLength((String)conditions.get(Param.RO_SLIP_ID_FROM))) {
				param.put(Param.RO_SLIP_ID_FROM,new Long((String)conditions.get(Param.RO_SLIP_ID_FROM)));
			}
		}

		// 発注番号To
		if (conditions.containsKey(Param.RO_SLIP_ID_TO)) {
			if (StringUtil.hasLength((String)conditions.get(Param.RO_SLIP_ID_TO))) {
				param.put(Param.RO_SLIP_ID_TO,new Long((String)conditions.get(Param.RO_SLIP_ID_TO)));
			}
		}

		// 売上番号From
		if (conditions.containsKey(Param.SALES_SLIP_ID_FROM)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SALES_SLIP_ID_FROM))) {
				param.put(Param.SALES_SLIP_ID_FROM,new Long((String)conditions.get(Param.SALES_SLIP_ID_FROM)));
			}
		}

		// 売上番号To
		if (conditions.containsKey(Param.SALES_SLIP_ID_TO)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SALES_SLIP_ID_TO))) {
				param.put(Param.SALES_SLIP_ID_TO,new Long((String)conditions.get(Param.SALES_SLIP_ID_TO)));
			}
		}

		// 受付番号
		if (conditions.containsKey(Param.RECEPT_NO)) {
			if (StringUtil.hasLength((String)conditions.get(Param.RECEPT_NO))) {
				param.put(Param.RECEPT_NO,(String)conditions.get(Param.RECEPT_NO) + '%');
			}
		}

		// 取引区分
		if (conditions.containsKey(Param.SALES_CATEGORY_LIST)) {
			List<SalesCategoryDto> categoryList = (List<SalesCategoryDto>)conditions.get(Param.SALES_CATEGORY_LIST);
			if(categoryList.size() > 0){
				param.put(Param.SALES_CATEGORY_LIST, categoryList);
			}
		}

		// 「全て出力済を除く」チェックボックス
		if (conditions.containsKey(Param.EXCLUDING_OUTPUT_ALL)) {
			if ((Boolean)conditions.get(Param.EXCLUDING_OUTPUT_ALL)) {
				param.put(Param.EXCLUDING_OUTPUT_ALL,"true");
			}
		}

		// ソートカラムを設定する
		if (conditions.containsKey(Param.SORT_COLUMN)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SORT_COLUMN))) {
				param.put(Param.SORT_COLUMN,
						StringUtil.convertColumnName((String)conditions.get(Param.SORT_COLUMN)));
			}
		}

		// ソートオーダーを設定する
		Boolean sortOrderAsc = (Boolean)conditions.get(Param.SORT_ORDER_ASC);
		if (sortOrderAsc != null && sortOrderAsc) {
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER_ASC, Constants.SQL.DESC);
		}

		// 表示件数を設定する
		if (conditions.containsKey(Param.ROW_COUNT)) {
			param.put(Param.ROW_COUNT,
					conditions.get(Param.ROW_COUNT));
		}

		// オフセットを設定する
		if (conditions.containsKey(Param.OFFSET_ROW)) {
			param.put(Param.OFFSET_ROW,conditions.get(Param.OFFSET_ROW));
		}

		return param;
	}

	/**
	 * 検索条件を指定して結果リストを返します.
	 * @param conditions 検索条件
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findSlipByCondition(Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class,
					"sales/FindSalesReportByConditions.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 得意先コードを指定して、最も古い納入先情報を取得します.
	 *
	 * @param customerCode 得意先コード
	 * @return 最も古い納入先情報
	 * @throws ServiceException
	 */
	public Delivery findOldestDeliveryByCustomerCode(String customerCode) throws ServiceException{
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.CUST_REL_CATEGORY, Constants.CUSTOMER_REL.DELIVERY);// 01:納入先
			param.put(Param.CUSTOMER_CODE, customerCode);// 01:納入先

			return this.selectBySqlFile(Delivery.class,
					"sales/FindOldestDeliveryByCustomerCode.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索結果の出力対象チェックボックスの表示状態を設定します.
	 * <p>
	 * 以下の6種類のチェックボックスについて制御します.<br>
	 * 1.見積書<br>
	 * 2.請求書<br>
	 * 3.納品書<br>
	 * 4.仮納品書<br>
	 * 5.ピッキングリスト<br>
	 * 6.納品書兼領収書
	 * </p>
	 */
	private void setResultDispInfo(OutputSalesSearchResultDto dto) throws ServiceException{
		// 仮納品書チェックボックスの制御
		if(StringUtil.hasLength(dto.customerCode)){

			if(dto.firstDeliveryCode != null &&
					!dto.firstDeliveryCode.equals("true")) {
				// 売上伝票の納入先コードが、関連する納入先マスタの中で最も古いものでない場合、
				// 帳票出力チェックボックスを表示する
				// 仮納品書
				dto.isTempDeliveryCheckDisp = true;
				dto.fileTempDelivery = dto.REPORT_FILE_E;

				// 仮納品書が発行対象である
				dto.tempDeliveryOutputFlag = true;

				// 仮納品書が発行済であればフラグを立てる
				if(Integer.parseInt(dto.tempDeliveryPrintCount) > 0){
					dto.tempDeliveryOutput = true;
				}
			}
		}

		//全て出力済みかどうかのフラグ(とりあえずtrueにして、一つでも出力していない物があればfalseに変更する)
		dto.allOutput = true;

		// ピッキングリストは常に有効
		dto.isPickingListCheckDisp = true;
		dto.filePickingList = dto.REPORT_FILE_J + "," + dto.REPORT_FILE_K;
		if(Integer.parseInt(dto.shippingPrintCount) <= 0) {
			dto.allOutput = false;
		}

		// 納品書は「請求書発行単位：発行しない」以外の場合（請求書を発行する場合）のみ発行可能。
		if( CategoryTrns.SALES_CM_CREDIT.equals(dto.salesCmCategory) && !CategoryTrns.BILL_PRINT_UNIT_NO_BILL.equals(dto.billPrintUnit)) {
			dto.isDeliveryCheckDisp = true;
			dto.fileDelivery = dto.REPORT_FILE_D;
			if(Integer.parseInt(dto.deliveryPrintCount) <= 0) {
				dto.allOutput = false;
			}
		}

		if( !CategoryTrns.SALES_CM_CREDIT.equals(dto.salesCmCategory) || CategoryTrns.BILL_PRINT_UNIT_NO_BILL.equals(dto.billPrintUnit)) {
			// 請求書を発行しない場合は、納品書兼領収書を有効にする
			dto.isDeliveryReceiptCheckDisp = true;
			dto.fileDeliveryReceipt = dto.REPORT_FILE_F;
			if(Integer.parseInt(dto.delborPrintCount) <= 0) {
				dto.allOutput = false;
			}
		} else if( CategoryTrns.SALES_CM_CREDIT.equals(dto.salesCmCategory) && CategoryTrns.BILL_PRINT_UNIT_BILL_CLOSE.equals(dto.billPrintUnit)) {
			// 請求締め単位で請求書を発行する場合は、売上時に特別に発行する帳票はなし
		} else if( CategoryTrns.SALES_CM_CREDIT.equals(dto.salesCmCategory) && CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP.equals(dto.billPrintUnit)) {
			// 売上伝票単位の請求書を発行する場合
			dto.isBillCheckDisp = true;
			dto.fileBill = dto.REPORT_FILE_G;

			// 売上伝票単位の請求書発行日を出力するかどうかを設定する
			if( CategoryTrns.BILL_DATE_PRINT_ON.equals(dto.billDatePrint) ) {
				dto.dispDateFlag = true;
			} else {
				dto.dispDateFlag = false;
			}

			if(Integer.parseInt(dto.billPrintCount) <= 0) {
				dto.allOutput = false;
			}
		}
	}
}
