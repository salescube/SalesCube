/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.CodFeeUtil;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.DiscountUtil;
import jp.co.arkinfosys.common.PostageUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.entity.CategoryTrn;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.CustomerAndDate;
import jp.co.arkinfosys.entity.CustomerRank;
import jp.co.arkinfosys.entity.PickingLine;
import jp.co.arkinfosys.entity.PickingList;
import jp.co.arkinfosys.entity.SalesLineTrn;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.TaxRate;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 売上伝票サービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SalesService extends
		AbstractSlipService<SalesSlipTrn, SalesSlipDto> {

	//発番用サービス
	public SeqMakerService seqMakerService;

	// 売上伝票明細行用サービス
	@Resource
	public SalesLineService salesLineService;

	@Resource
	private YmService ymService;

	@Resource
	private PickingService pickingService;

	// 出荷指示書明細行用サービス
	@Resource
	private PickingLineService pickingLineService;

	// 商品マスタ用サービス
	@Resource
	private ProductService productService;

	// 税率用サービス
	@Resource
	private TaxRateService taxRateService;

	// 受注伝票用サービス
	@Resource
	private RoSlipSalesService roSlipSalesService;

	// 顧客マスタ用サービス
	@Resource
	private CustomerService customerService;

	// カテゴリ用サービス
	@Resource
	protected CategoryService categoryService;

	// 顧客ランク用サービス
	@Resource
	protected CustomerRankService customerRankService;

	/**
	 * 日付の形式指定
	 */
	SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);

	/**
	 * SQLファイルのパラメータ名定義
	 */
	public static class Param {
		private static final String SORT_ORDER = "sortOrder"; // ソート方向
		private static final String ROW_COUNT = "rowCount"; // 取得件数
		private static final String OFFSET_ROW = "offsetRow"; // 取得件数
		public static final String CUSTOMER_CODE = "customerCode"; // 顧客コード
		public static final String SALES_DATE = "salesDate"; // 売上日
		public static final String SALES_DATE_FROM = "salesDateFrom"; // 売上日(範囲指定：FROM)
		public static final String SALES_DATE_TO = "salesDateTo"; // 売上日(範囲指定：TO)
		public static final String SALES_YM = "salesYm"; // 売上年月度
		public static final String SALES_SLIP_ID = "salesSlipId"; // 売上伝票番号
		public static final String SALES_LINE_ID = "salesLineId"; // 売上伝票行ID
		private static final String SORT_COLUMN_SALES_DATE = "sortColumnSalesDate"; // 売上日のソート条件
		private static final String STATUS = "status"; // 状態
		public static final String PRODUCT_CODE = "productCode";
		public static final String QUANTITY = "quantity";
		public static final String BILL_CUTOFF_DATE = "billCutoffDate"; // 請求締日
		public static final String BILL_PRINT_COUNT = "billPrintCount";
		public static final String DELIVERY_PRINT_COUNT = "deliveryPrintCount";
		public static final String TEMP_DELIVERY_PRINT_COUNT = "tempDeliveryPrintCount";
		public static final String SHIPPING_PRINT_COUNT = "shippingPrintCount";
		public static final String SI_PRINT_COUNT = "siPrintCount";
		public static final String ESTIMATE_PRINT_COUNT = "estimatePrintCount";
		public static final String DELBOR_PRINT_COUNT = "delborPrintCount";
		public static final String PO_PRINT_COUNT = "poPrintCount";
		public static final String BILL_ID = "billId";
		public static final String CUSTOMER_SLIP_NO = "customerSlipNo";
		public static final String IS_CONTAIN_CLOSE_LEAK = "isContainCloseLeak";
		public static final String LEAK_CHECK_CUTOFF_DATE = "leakCheckCutoffDate";
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
		public static final String SALES_CUTOFF_DATE = "salesCutoffDate"; // 売掛締日

	}

	public String[] params = { Param.SORT_ORDER, Param.ROW_COUNT,
			Param.OFFSET_ROW, Param.CUSTOMER_CODE, Param.SALES_DATE,
			Param.SALES_DATE_FROM, Param.SALES_DATE_TO, Param.SALES_YM,
			Param.SALES_SLIP_ID, Param.SALES_LINE_ID,
			Param.SORT_COLUMN_SALES_DATE, Param.STATUS, Param.PRODUCT_CODE,
			Param.QUANTITY, Param.BILL_CUTOFF_DATE, Param.BILL_PRINT_COUNT,
			Param.DELIVERY_PRINT_COUNT, Param.TEMP_DELIVERY_PRINT_COUNT,
			Param.SHIPPING_PRINT_COUNT, Param.SI_PRINT_COUNT,
			Param.ESTIMATE_PRINT_COUNT, Param.DELBOR_PRINT_COUNT,
			Param.PO_PRINT_COUNT, Param.BILL_ID, Param.CUSTOMER_SLIP_NO,
			Param.IS_CONTAIN_CLOSE_LEAK, Param.LEAK_CHECK_CUTOFF_DATE,
			Param.SALES_CM_CATEGORY, Param.SALES_CUTOFF_DATE };

	/**
	 * 納入先コードのカラム名
	 */
	public static final String COLUMN_SALES_DATE = "SALES_DATE";

	/**
	 * 入力データをもとに伝票情報を設定します.
	 * @param dto 売上伝票{@link SalesSlipDto}
	 * @throws ServiceException
	 */
	private void setSlipDataByForm(SalesSlipDto dto) throws ServiceException {

		// 伝票　状態フラグの初期値は未請求
		// 請求状態にある伝票は締っているので変更できないので常にこの値を入れる
		dto.status = SalesSlipTrn.STATUS_INIT;

		// 入出庫年度、月度、年月度を計算
		YmDto ymDto = ymService.getYm(dto.salesDate);
		dto.salesAnnual = ymDto.annual.toString();
		dto.salesMonthly = ymDto.monthly.toString();
		dto.salesYm = ymDto.ym.toString();

		// 伝票合計の計算
		calcTotal(dto);

	}

	/**
	 * 伝票合計値の計算をします.
	 * @param dto 売上伝票{@link SalesSlipDto}
	 */
	protected void calcTotal(SalesSlipDto dto) {
		// 数値計算の調整
		Double total = 0.0;
		Double tax = 0.0;
		Double gm = 0.0;
		HashMap<String, Double> taxMap = new HashMap<String, Double>();

		for (SalesLineDto lineDto : dto.getLineDtoList()) {
			if (lineDto.isBlank()) {
				continue;
			}

			Double tmpPrice = Double.parseDouble(lineDto.retailPrice
					.replaceAll(",", ""));
			total += tmpPrice;
			Double tmpGm = Double.parseDouble(lineDto.gm.replaceAll(",", ""));
			gm += tmpGm;

			// 課税区分を確認
			if (CategoryTrns.TAX_CATEGORY_FREE.equals(lineDto.taxCategory)) {
				// 免税
			} else if (CategoryTrns.TAX_CATEGORY_IMPOSITION.equals(lineDto.taxCategory)) {
				// 課税
				// 税率毎に加算
				Double price = taxMap.get(lineDto.ctaxRate);
				if (price == null) {
					taxMap.put(lineDto.ctaxRate, tmpPrice);
				} else {
					taxMap.put(lineDto.ctaxRate, tmpPrice + price);
				}
			} else if (CategoryTrns.TAX_CATEGORY_INCLUDED.equals(lineDto.taxCategory)) {
				// 内税
			}
		}
		// 税率毎に消費税を計算
		Set<Entry<String, Double>> entrySet = taxMap.entrySet(); //すべてのvalue
		Iterator<Entry<String, Double>> entryIte = entrySet.iterator();
		while (entryIte.hasNext()) { //ループ
			Map.Entry<String, Double> ent = entryIte.next(); //key=value
			if (ent.getKey() != null) {
				// 税率×金額
				if (StringUtil.hasLength(ent.getKey())) {
					Double rate = Double.valueOf(ent.getKey());
					Double thisTax = (ent.getValue() * (rate / 100.0)); // rateは％表記の値なので100.0で割る
					BigDecimal bd = DiscountUtil.getScaleValue(
							dto.taxFractCategory, 0, new BigDecimal(thisTax));
					if (bd != null) {
						tax = tax + bd.doubleValue();
					}
				}
			}
		}
		// 伝票合計消費税
		// 税転嫁を確認
		if (CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX
				.equals(dto.taxShiftCategory)) {
			// 区分名：税転嫁、区分コード名：内税
			dto.ctaxPriceTotal = null;
		} else if (CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL
				.equals(dto.taxShiftCategory)) {
			// 区分名：税転嫁、区分コード名：外税伝票計
			dto.ctaxPriceTotal = tax.toString();
		} else if (CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS
				.equals(dto.taxShiftCategory)) {
			// 区分名：税転嫁、区分コード名：外税締単位
			dto.ctaxPriceTotal = tax.toString();
		}
		// 伝票金額合計
		dto.priceTotal = total.toString();
		// 伝票合計粗利益
		dto.gmTotal = gm.toString();

	}

	/**
	 * 請求締の情報を設定します.
	 * @param ss　売上伝票（{@link SalesSlipTrn}）
	 * @param billId　請求書番号
	 * @param lastCutOffDate　請求締日
	 * @param cutoffPdate　請求処理日
	 * @throws ParseException
	 */
	protected void setCloseSalesSlipBill(SalesSlipTrn ss, Integer billId,
			String lastCutOffDate, Timestamp cutoffPdate) throws ParseException {
		// 状態フラグ
		ss.status = SalesSlipTrn.STATUS_FINISH;
		// 請求書番号
		ss.billId = billId;
		// 請求締日付
		ss.billCutoffDate = super.convertUtilDateToSqlDate(DF_YMD
				.parse(lastCutOffDate));
		// 請求締処理日
		ss.billCutoffPdate = cutoffPdate;
		// 売上請求書番号は設定しない
		// 締日グループはSQLで設定
		// 回収間隔もSQLで設定
	}

	/**
	 * 売掛締の情報を設定します.
	 * @param ss　売上伝票（{@link SalesSlipTrn}）
	 * @param artId　売掛残高番号
	 * @param lastCutOffDate　売掛締日
	 * @param cutoffPdate　売掛処理日
	 * @throws ParseException
	 */
	protected void setCloseSalesSlipArt(SalesSlipTrn ss, Integer artId,
			String lastCutOffDate, Timestamp cutoffPdate) throws ParseException {
		// 伝票が売掛以外の時には状態フラグを変更する
		if (!CategoryTrns.SALES_CM_CREDIT.equals(ss.salesCmCategory)) {
			ss.status = SalesSlipTrn.STATUS_FINISH;
		}
		// 売掛残高番号
		ss.artId = artId;
		// 売掛締日付
		ss.salesCutoffDate = super.convertUtilDateToSqlDate(DF_YMD
				.parse(lastCutOffDate));
		// 売掛締処理日
		ss.salesCutoffPdate = cutoffPdate;
		// 売上請求書番号は設定しない
		// 締日グループはSQLで設定
		// 回収間隔もSQLで設定
	}

	/**
	 * 請求締の情報をクリアします.
	 * @param ss　売上伝票（{@link SalesSlipTrn}）
	 * @throws ParseException
	 */
	protected void setReOpenSalesSlipBill(SalesSlipTrn ss)
			throws ParseException {
		// 状態フラグ
		ss.status = SalesSlipTrn.STATUS_INIT;
		// 請求書番号
		ss.billId = null;
		// 請求締日付
		ss.billCutoffDate = null;
		// 請求締処理日
		ss.billCutoffPdate = null;
	}

	/**
	 * 売掛締の情報をクリアします．
	 * @param ss　売上伝票（{@link SalesSlipTrn}）
	 * @throws ParseException
	 */
	protected void setReOpenSalesSlipArt(SalesSlipTrn ss) throws ParseException {
		// 伝票が売掛以外の時には状態フラグを変更する
		if (!CategoryTrns.SALES_CM_CREDIT.equals(ss.salesCmCategory)) {
			ss.status = SalesSlipTrn.STATUS_INIT;
		}
		// 売掛残高番号
		ss.artId = null;
		// 売掛締日付
		ss.salesCutoffDate = null;
		// 売掛締処理日
		ss.salesCutoffPdate = null;
	}

	/**
	 * エンティティ情報から登録用Mapオブジェクトを生成します.
	 * @param ss　売上伝票{@link SalesSlipTrn}
	 * @return　登録用マップ
	 */
	private Map<String, Object> createParamMap(SalesSlipTrn ss) {

		//MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		//アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, ss).execute();
		param.putAll(AFparam);

		//更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return param;
	}

	/**
	 * DTOからEntityへの変換します.
	 * @param dto　売上伝票{@link SalesSlipDto}
	 * @return　{@link SalesSlipTrn}
	 */
	public SalesSlipTrn createAndCopy(SalesSlipDto dto) {
		NumberConverter convUP = createUnitPriceConverter(dto.priceFractCategory);
		NumberConverter convTax = createTaxPriceConverter(dto.taxFractCategory);
		return Beans.createAndCopy(SalesSlipTrn.class, dto).converter(convUP,
				"priceTotal", "gmTotal").converter(convTax, "ctaxPriceTotal")
				.dateConverter(Constants.FORMAT.DATE, "billDate",
						"billCutoffDate", "salesDate", "deliveryDate",
						"salesCutoffDate").dateConverter(
						Constants.FORMAT.TIMESTAMP, "billCutoffPdate",
						"salesCutoffPdate", "creDatetm", "updDatetm").execute();
	}

	/**
	 * EntiryからDTOへの変換します.
	 * @param ss　売上伝票{@link SalesSlipTrn}
	 * @return　{@link SalesSlipDto}
	 */
	public SalesSlipDto createAndCopy(SalesSlipTrn ss) {
		NumberConverter convUP = createUnitPriceConverter(ss.priceFractCategory);
		NumberConverter convTax = createTaxPriceConverter(ss.taxFractCategory);
		return Beans.createAndCopy(SalesSlipDto.class, ss).converter(convUP,
				"priceTotal", "gmTotal").converter(convTax, "ctaxPriceTotal")
				.dateConverter(Constants.FORMAT.DATE, "billDate",
						"billCutoffDate", "salesDate", "deliveryDate",
						"salesCutoffDate").dateConverter(
						Constants.FORMAT.TIMESTAMP, "billCutoffPdate",
						"salesCutoffPdate", "creDatetm", "updDatetm").execute();
	}

	/**
	 * 指定した顧客の、指定日より後の売上伝票を検索して、伝票合計金額（税込）の合計を返します.<BR>
	 * 指定した伝票の伝票合計金額は除きます.
	 *
	 * @param customerCode 顧客コード
	 * @param startDate　伝票の検索開始日付（これより後）
	 * @param salesSlipId　除外する伝票番号
	 * @return　伝票合計金額の合計値（税込）
	 * @throws ServiceException
	 */
	public BigDecimal getSalesTotalPrice(String customerCode, Date startDate,
			String salesSlipId) throws ServiceException {

		List<SalesSlipTrn> salesList = findSalesSlipByCustomerCodeAndDate(
				customerCode, startDate);

		Double salesTotal = 0.0;

		// 読み飛ばし用伝票番号生成
		Integer ssId;
		if ((salesSlipId == null) || (salesSlipId.equals(""))) {
			ssId = -1;
		} else {
			ssId = Integer.parseInt(salesSlipId);
		}

		// 伝票合計金額の合計
		for (SalesSlipTrn sst : salesList) {
			// 指定伝票（今、編集中の伝票）を除外
			if (sst.salesSlipId.equals(ssId) == true) {
				continue;
			}
			if (sst.priceTotal != null) {
				salesTotal = salesTotal + sst.priceTotal.doubleValue();
			}
			if (sst.ctaxPriceTotal != null) {
				salesTotal = salesTotal + sst.ctaxPriceTotal.doubleValue();
			}
		}
		return new BigDecimal(salesTotal);
	}

	/**
	 *　顧客コードを指定して指定した日付【より後】に発行された売上伝票を売上日付の降順で取得します.
	 * @param customerCode 顧客コード
	 * @param startDate 指定日付
	 * @return {@link SalesSlipTrn}のリスト
	 * @throws ServiceException
	 */
	public List<SalesSlipTrn> findSalesSlipByCustomerCodeAndDate(
			String customerCode, Date startDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		if (startDate != null) {
			conditions.put(Param.SALES_DATE, DF_YMD.format(startDate));
		}
		conditions.put(Param.SORT_COLUMN_SALES_DATE, COLUMN_SALES_DATE);
		conditions.put(Param.SORT_ORDER, "DESC");

		return findByCondition(conditions, params, "sales/FindSalesSlip.sql");

	}

	/**
	 * 顧客コードを指定して売上日が指定した日付【以前】に発行された未請求の売上伝票を売上日付の降順で取得します.
	 * @param customerCode 顧客コード
	 * @param closeDate 締処理日付
	 * @param salesCmCategory 売上取引区分　＝SALES_CM_CASH_ON_DELIVERY：代引き（売掛以外）　＝SALES_CM_CREDIT：売掛
	 * @return {@link SalesSlipTrn}のリスト
	 * @throws ServiceException
	 */
	public List<SalesSlipTrn> findOpenSalesSlipByCustomerCode(
			String customerCode, String closeDate, String salesCmCategory)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 状態が未請求
		// 顧客コードが一致
		// 売上日が指定日以前
		// 売上日降順
		conditions.put(Param.STATUS, SalesSlipTrn.STATUS_INIT);
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SALES_DATE, closeDate);
		conditions.put(Param.SALES_CM_CATEGORY, salesCmCategory);
		conditions.put(Param.SORT_COLUMN_SALES_DATE, COLUMN_SALES_DATE);
		conditions.put(Param.SORT_ORDER, "DESC");

		return findByCondition(conditions, params,
				"sales/FindOpenSalesSlip.sql");

	}

	/**
	 *　顧客コードを指定して売上日が指定した日付【以前】に発行された未売掛締めの売上伝票を売上日付の降順で取得します.
	 * @param customerCode 顧客コード
	 * @param closeDate 締処理日付
	 * @return {@link SalesSlipTrn}のリスト
	 * @throws ServiceException
	 */
	public List<SalesSlipTrn> findArtOpenSalesSlipByCustomerCode(
			String customerCode, String closeDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 状態が未請求
		// 顧客コードが一致
		// 売上日が指定日以前
		// 売上日降順
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SALES_DATE, closeDate);
		conditions.put(Param.SALES_CUTOFF_DATE, null);
		conditions.put(Param.SORT_COLUMN_SALES_DATE, COLUMN_SALES_DATE);
		conditions.put(Param.SORT_ORDER, "DESC");

		return findByCondition(conditions, params,
				"sales/FindArtOpenSalesSlip.sql");

	}

	/**
	 * 顧客コードを指定して売上日が指定した日付期間となる売上伝票を売上日付の降順で取得します.
	 * @param customerCode 顧客コード
	 * @param dateFrom 期間の開始日
	 * @param dateTo 期間の終了日
	 * @param salesCutoffDate 売掛締日
	 * @return {@link SalesSlipTrn}のリスト
	 */
	public List<SalesSlipTrn> findSalesSlipByCustomerCodeBetweenDate(
			String customerCode, String dateFrom, String dateTo,
			String salesCutoffDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SORT_COLUMN_SALES_DATE, COLUMN_SALES_DATE);
		conditions.put(Param.SORT_ORDER, Constants.SQL.DESC);
		conditions.put(Param.SALES_DATE_FROM, dateFrom);
		conditions.put(Param.SALES_DATE_TO, dateTo);
		conditions.put(Param.SALES_CUTOFF_DATE, salesCutoffDate);

		return findByCondition(conditions, params,
				"sales/FindSalesSlipBetweenDate.sql");
	}

	/**
	 *　顧客コードと請求締日付を指定して請求済の売上伝票を売上日付の降順で取得します.
	 * @param customerCode 顧客コード
	 * @param lastCutOffDate 請求締日付
	 * @param salesCmCategory 売上取引区分 ＝SALES_CM_CASH_ON_DELIVERY：代引き（売掛以外）　　＝SALES_CM_CREDIT：売掛
	 * @return {@link SalesSlipTrn}のリスト
	 * @throws ServiceException
	 */
	public List<SalesSlipTrn> findCloseSalesSlipByCustomerCode(
			String customerCode, String lastCutOffDate, String salesCmCategory)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.STATUS, SalesSlipTrn.STATUS_FINISH);
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.BILL_CUTOFF_DATE, lastCutOffDate);
		conditions.put(Param.SALES_CM_CATEGORY, salesCmCategory);
		conditions.put(Param.SORT_COLUMN_SALES_DATE, COLUMN_SALES_DATE);
		conditions.put(Param.SORT_ORDER, "DESC");

		return findByCondition(conditions, params,
				"sales/FindOpenSalesSlip.sql");

	}

	/**
	 *　顧客コードと売掛締日付を指定して請求済の売上伝票を売上日付の降順で取得します.
	 * @param customerCode 顧客コード
	 * @param lastCutOffDate 請求締日付
	 * @return {@link SalesSlipTrn}のリスト
	 * @throws ServiceException
	 */
	public List<SalesSlipTrn> findArtCloseSalesSlipByCustomerCode(
			String customerCode, String lastCutOffDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SALES_CUTOFF_DATE, lastCutOffDate);
		conditions.put(Param.SORT_COLUMN_SALES_DATE, COLUMN_SALES_DATE);
		conditions.put(Param.SORT_ORDER, "DESC");

		return findByCondition(conditions, params,
				"sales/FindArtOpenSalesSlip.sql");

	}

	/**
	 *　売上伝票番号を指定して売上伝票を取得します.<BR>
	 * この関数で使用しているSQLは、SALES_CM_CATEGORYの扱いが特殊なので、他の処理に流用しないで下さい.
	 * @param salesSlipId 売上伝票番号
	 * @return 売上伝票のBeanMap
	 * @throws ServiceException
	 */
	public BeanMap findSalesSlipBySalesSlipIdSimple(String salesSlipId)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.SALES_SLIP_ID, salesSlipId);
			return this.selectBySqlFile(BeanMap.class,
					"sales/FindSalesSlipAndCat.sql", param).getSingleResult();
		} catch (SNonUniqueResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *　売上伝票番号を指定して売上伝票を取得します.<BR>
	 * この関数で使用しているSQLは、SALES_CM_CATEGORYの扱いが特殊なので、他の処理に流用しないで下さい.<BR>
	 * 売上日を＋１した日付が取得されます.
	 * @param salesSlipId 売上伝票番号
	 * @return 売上伝票のBeanMap
	 * @throws ServiceException
	 */
	public BeanMap findSalesSlipBySalesSlipIdSimpleAddDate(String salesSlipId)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.SALES_SLIP_ID, salesSlipId);
			return this.selectBySqlFile(BeanMap.class,
					"sales/FindSalesSlipAndCatAddDate.sql", param)
					.getSingleResult();
		} catch (SNonUniqueResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *　顧客コードを指定して最新の売上日を取得します.<BR>
	 *　見つからなかった時にはNULLを返します.
	 * @param customerCode 顧客コード
	 * @return 最終売上日
	 * @throws ServiceException
	 */
	public Date findLastSalesDate(String customerCode) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SORT_COLUMN_SALES_DATE, COLUMN_SALES_DATE);
		conditions.put(Param.SORT_ORDER, "DESC");
		conditions.put(Param.ROW_COUNT, 1);
		conditions.put(Param.OFFSET_ROW, 0);

		List<SalesSlipTrn> salesList = findByCondition(conditions, params,
				"sales/FindSalesSlip.sql");
		if (salesList.size() == 0) {
			return null;
		} else {
			return salesList.get(0).salesDate;
		}
	}

	/**
	 *　顧客コード、最終日付を指定して、締っていない伝票で、1日に複数伝票が存在するリストを返します.
	 *
	 * @param customerCode 顧客コード
	 * @param cutoffDate 最終日付
	 * @return {@link CustomerAndDate}のリスト
	 * @throws ServiceException
	 */
	public List<CustomerAndDate> findMultiSlipOneDay(String customerCode,
			String cutoffDate) throws ServiceException {
		try {
			Map<String, Object> params = super.createSqlParam();

			// 商品コード
			params.put(Param.CUSTOMER_CODE, customerCode);
			// 抽出期間
			params.put(Param.SALES_DATE, cutoffDate);

			return this.selectBySqlFile(CustomerAndDate.class,
					"sales/FindMultiSlipOneDay.sql", params).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 売上伝票とそれに紐付く明細行を締めます.<BR>
	 * この処理は履歴を残しません.
	 *
	 * @param salesSlipList　締める対象の伝票（{@link SalesSlipTrn}）リスト
	 * @param billId　請求書番号
	 * @param lastCutOffDate　請求締日
	 * @param cutoffPdate　締め処理日
	 * @return　更新した伝票行数
	 * @throws UnabledLockException
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public int closeSalesSlipBill(List<SalesSlipTrn> salesSlipList,
			Integer billId, String lastCutOffDate, Timestamp cutoffPdate)
			throws ServiceException, UnabledLockException, ParseException {

		int updateCount = 0;
		for (SalesSlipTrn ss : salesSlipList) {
			// 排他制御
			this.lockRecord(Param.SALES_SLIP_ID, ss.salesSlipId, ss.updDatetm,
					"sales/LockSlip.sql");

			// 明細エンティティの取得
			List<SalesLineTrn> slList = salesLineService
					.findSalesLineBySalesSlipId(ss.salesSlipId.toString());

			// 明細行の更新
			for (SalesLineTrn sl : slList) {
				// 存在したら常に更新
				sl.status = SalesLineTrn.STATUS_FINISH;
				if (salesLineService.updateRecord(sl) == 0) {
					throw new ServiceException("errors.system");
				}
			}

			// 伝票の更新
			setCloseSalesSlipBill(ss, billId, lastCutOffDate, cutoffPdate);

			// 伝票の更新
			this.updateBySqlFile("sales/UpdateSalesSlip.sql",
					createParamMap(ss)).execute();

			updateCount++;
		}
		return updateCount;
	}

	/**
	 * 売上伝票とそれに紐付く明細行を締解除します.<BR>
	 * この処理は履歴を残しません.
	 *
	 * @param salesSlipList　締解除する対象の伝票（{@link SalesSlipTrn}）リスト
	 * @return　更新した伝票行数
	 * @throws UnabledLockException
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public int reOpenSalesSlipBill(List<SalesSlipTrn> salesSlipList)
			throws ServiceException, UnabledLockException, ParseException {

		int updateCount = 0;
		for (SalesSlipTrn ss : salesSlipList) {
			// 排他制御
			this.lockRecord(Param.SALES_SLIP_ID, ss.salesSlipId, ss.updDatetm,
					"sales/LockSlip.sql");

			// 明細エンティティの取得
			List<SalesLineTrn> slList = salesLineService
					.findSalesLineBySalesSlipId(ss.salesSlipId.toString());

			// 明細行の更新
			for (SalesLineTrn sl : slList) {
				// 存在したら常に更新
				salesLineService.setReOpenSalesLine(sl);
				if (salesLineService.updateRecord(sl) == 0) {
					throw new ServiceException("errors.system");
				}
			}

			// 伝票の更新
			setReOpenSalesSlipBill(ss);

			this.updateBySqlFile("sales/UpdateSalesSlip.sql",
					createParamMap(ss)).execute();
			updateCount++;
		}
		return updateCount;
	}

	/**
	 * 売上伝票とそれに紐付く明細行を締めます.<BR>
	 * この処理は履歴を残しません.
	 *
	 * @param salesSlipList　締める対象の伝票（{@link SalesSlipTrn}）リスト
	 * @param artId　売掛残高番号
	 * @param lastCutOffDate　請求締日
	 * @param cutoffPdate　締め処理日
	 * @return　更新した伝票行数
	 * @throws UnabledLockException
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public int closeSalesSlipArt(List<SalesSlipTrn> salesSlipList,
			Integer artId, String lastCutOffDate, Timestamp cutoffPdate)
			throws ServiceException, UnabledLockException, ParseException {

		int updateCount = 0;
		for (SalesSlipTrn ss : salesSlipList) {
			// 排他制御
			this.lockRecord(Param.SALES_SLIP_ID, ss.salesSlipId, ss.updDatetm,
					"sales/LockSlip.sql");

			// 対象伝票が掛売以外だったら、ここで締める（掛売は請求締めしないとだめ）
			if (!CategoryTrns.SALES_CM_CREDIT.equals(ss.salesCmCategory)) {
				// 明細エンティティの取得
				List<SalesLineTrn> slList = salesLineService
						.findSalesLineBySalesSlipId(ss.salesSlipId.toString());

				// 明細行の更新
				for (SalesLineTrn sl : slList) {
					// 存在したら常に更新
					sl.status = SalesLineTrn.STATUS_FINISH;
					if (salesLineService.updateRecord(sl) == 0) {
						throw new ServiceException("errors.system");
					}
				}
			}

			// 伝票の更新
			setCloseSalesSlipArt(ss, artId, lastCutOffDate, cutoffPdate);

			// 伝票の更新
			if (this.updateBySqlFile("sales/UpdateSalesSlip.sql",
					createParamMap(ss)).execute() == 0) {
				throw new ServiceException("errors.system");
			}
			updateCount++;
		}
		return updateCount;
	}

	/**
	 * 売上伝票とそれに紐付く明細行を締解除します.
	 * この処理は履歴を残しません.
	 *
	 * @param salesSlipList　締解除する対象の伝票（{@link SalesSlipTrn}）リスト
	 * @return　更新した伝票行数
	 * @throws UnabledLockException
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public int reOpenSalesSlipArt(List<SalesSlipTrn> salesSlipList)
			throws ServiceException, UnabledLockException, ParseException {

		int updateCount = 0;
		for (SalesSlipTrn ss : salesSlipList) {
			// 排他制御
			this.lockRecord(Param.SALES_SLIP_ID, ss.salesSlipId, ss.updDatetm,
					"sales/LockSlip.sql");

			// 対象伝票が掛売以外だったら、ここで締め解除する（掛売は請求締め解除しないとだめ）
			if (!CategoryTrns.SALES_CM_CREDIT.equals(ss.salesCmCategory)) {
				// 明細エンティティの取得
				List<SalesLineTrn> slList = salesLineService
						.findSalesLineBySalesSlipId(ss.salesSlipId.toString());

				// 明細行の更新
				for (SalesLineTrn sl : slList) {
					// 存在したら常に更新
					salesLineService.setReOpenSalesLine(sl);
					if (salesLineService.updateRecord(sl) == 0) {
						throw new ServiceException("errors.system");
					}
				}
			}

			// 伝票の更新
			setReOpenSalesSlipArt(ss);

			if (this.updateBySqlFile("sales/UpdateSalesSlip.sql",
					createParamMap(ss)).execute() == 0) {
				throw new ServiceException("errors.system");
			}
			updateCount++;
		}
		return updateCount;
	}

	/**
	 * 特殊商品明細行の制御をします.
	 * @param dto　売上伝票{@link SalesSlipDto}
	 * @param customer　顧客情報{@link CustomerJoin}
	 * @throws ServiceException
	 */
	protected void insertSpecialProduct(SalesSlipDto dto, CustomerJoin customer)
			throws ServiceException {
		int codFeePriceLine = -1;
		int postagePriceLine = -1;
		int salesDiscountPriceLine = -1;

		SalesLineDto codFeePriceLineDto = null;
		SalesLineDto postagePriceLineDto = null;
		SalesLineDto salesDiscountPriceLineDto = null;

		// 自動計算対象特殊商品コードの存在チェック
		List<SalesLineDto> list = dto.getLineDtoList();
		for (int i = 0; i < list.size(); i++) {
			SalesLineDto lineDto = list.get(i);
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}
			if (Constants.EXCEPTIANAL_PRODUCT_CODE.COD_FEE_PRICE
					.equals(lineDto.productCode)) {
				codFeePriceLineDto = lineDto;
				codFeePriceLine = i;
			} else if (Constants.EXCEPTIANAL_PRODUCT_CODE.POSTAGE_PRICE
					.equals(lineDto.productCode)) {
				postagePriceLineDto = lineDto;
				postagePriceLine = i;
			} else if (Constants.EXCEPTIANAL_PRODUCT_CODE.SALES_DISCOUNT_PRICE
					.equals(lineDto.productCode)) {
				salesDiscountPriceLineDto = lineDto;
				salesDiscountPriceLine = i;
			}
		}

		// v1.3.1 消費税は伝票で選択された値を使用するよう修正
		// 消費税の取得
		//TaxRate taxRate = taxRateService.findTaxRateById(
		//		CategoryTrns.TAX_TYPE_CTAX, dto.salesDate);

		// 代引き手数料と送料と顧客ランク割引を含まない商品税抜き合計計算
		Double priceTotalNoCodFeeNoPostage = sumNotCodeFeeAndProstageAndSalesDiscount(
				dto, false);

		// 顧客ランクによる値引き
		CustomerRank customerRank = getCustomerRank(customer);
		if (customerRank != null) {
			// ----------------
			// 顧客ランク割引実施
			// ----------------
			// 送料無料の場合には送料を削除する
			if (CategoryTrns.POSTAGE_FREE.equals(customerRank.postageType)) {
				// 送料が追加されていたら削除する
				if (postagePriceLine != -1) {
					// 行削除するので対象行を調整
					// 顧客ランク割引行
					if ((postagePriceLine < salesDiscountPriceLine)
							&& (salesDiscountPriceLine != -1)) {
						salesDiscountPriceLine -= 1;
					}
					// 代引き手数料行
					if ((postagePriceLine < codFeePriceLine)
							&& (codFeePriceLine != -1)) {
						codFeePriceLine -= 1;
					}
					dto.getLineDtoList().remove(postagePriceLine);
				}
			} else {
				// 送料追加
				addPostagePrice(customer, priceTotalNoCodFeeNoPostage,
						postagePriceLineDto, postagePriceLine, dto);
			}
			// 代引き手数料と顧客ランク値引き（お取引実績による値引き）を含まない商品税抜き合計計算
			Double priceTotalNoCodFeeNoTax = sumNotCodeFeeAndSalesDiscount(dto,
					false);

			// 顧客ランク割引額を計算する
			addSalesDiscountPrice(priceTotalNoCodFeeNoTax,
					salesDiscountPriceLineDto, salesDiscountPriceLine, dto,
					customerRank);
		} else {
			// 送料追加
			addPostagePrice(customer, priceTotalNoCodFeeNoPostage,
					postagePriceLineDto, postagePriceLine, dto);
		}

		// 代引き手数料を含まない商品税込合計計算
		Double priceTotalNoCodFee = sumNotCodeFee(dto, true);

		// 代引き手数料追加
		addCodFee(customer, priceTotalNoCodFee, codFeePriceLineDto, codFeePriceLine, dto);
	}

	/**
	 * 顧客の売上金額計算に適用する顧客ランク情報を取得します(顧客ランク適用対象外の顧客の場合nullを返却します).
	 * @param customer 顧客情報（{@link CustomerJoin}）
	 * @return 売上計算に適用する顧客ランク情報（{@link CustomerRank}）
	 * @throws ServiceException
	 */
	protected CustomerRank getCustomerRank(CustomerJoin customer)
			throws ServiceException {
		CustomerRank customerRank = null;
		// 顧客ランク適用チェックがONの顧客が対象なので、それ以外の条件の顧客はnull返却する
		if (!Constants.FLAG.ON.equals(customer.customerUpdFlag)) {
			return customerRank;
		}
		if (StringUtil.hasLength(customer.customerRankCategory)) {
			customerRank = customerRankService
					.findById(customer.customerRankCategory);
		}
		return customerRank;
	}

	/**
	 * 顧客ランク割引明細行を追加します.
	 * @param normalTotalPrice　代引き手数料と顧客ランク値引き（お取引実績による値引き）を含まない商品税抜き合計金額
	 * @param salesDiscountPriceLineDto お取引実績による値引き明細行（{@link SalesLineDto}）
	 * @param lineNo　既存顧客ランク割引明細行の行数
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param customerRank　顧客ランク情報（{@link CustomerRank}）
	 * @throws ServiceException
	 */
	protected void addSalesDiscountPrice(Double normalTotalPrice,
			SalesLineDto salesDiscountPriceLineDto, int lineNo,
			SalesSlipDto dto, CustomerRank customerRank)
			throws ServiceException {
		if (normalTotalPrice == null
				|| new BigDecimal(normalTotalPrice).compareTo(BigDecimal.ZERO) <= 0) {
			// 代引き手数料以外の特殊コード商品を含む全明細の合計(税抜き)が0以下の場合は追加しない
			return;
		}

		// 自分自身を除く、全ての明細行の合計を計算
		Double priceTotal = 0.0;
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (int i = 0; i < salesLineList.size(); i++) {
			if (i == lineNo) {
				continue;
			}
			SalesLineDto lineDto = salesLineList.get(i);
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}
			// 代引き手数料は除く
			if (Constants.EXCEPTIANAL_PRODUCT_CODE.COD_FEE_PRICE
					.equals(lineDto.productCode))
				continue;

			Double price = Double.valueOf(lineDto.retailPrice);
			priceTotal += price;
		}
		// 割引率は％の値なので100.0で割る
		Double discountTotal = priceTotal * customerRank.rankRate.doubleValue()
				/ 100.0;

		// 割引額が無かったら何もしない
		if (discountTotal <= 0.0) {
			return;
		}

		// 割引明細を追加する
		SalesLineDto lineDto = (SalesLineDto) dto.createLineDto();
		// 状態
		lineDto.status = SalesLineTrn.STATUS_INIT;
		// 商品コード
		lineDto.productCode = Constants.EXCEPTIANAL_PRODUCT_CODE.SALES_DISCOUNT_PRICE;
		// 商品名
		ProductJoin product;
		try {
			product = productService.findById(lineDto.productCode);
		} catch (ServiceException e) {
			e.printStackTrace();
			String strLabel = MessageResourcesUtil
					.getMessage("labels.productCode");
			String strMsg = MessageResourcesUtil
					.getMessage("errors.line.invalid");
			strMsg = strMsg.replace("{0}", lineDto.lineNo);
			strMsg = strMsg.replace("{1}", strLabel);
			throw new ServiceException(strMsg);
		}
		lineDto.productAbstract = product.productName;
		// 数量
		lineDto.quantity = "1";

		// 完納を設定
		lineDto.deliveryProcessCategory = CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL;

		// 入り数
		if (product.packQuantity == null) {
			lineDto.packQuantity = "";
		} else {
			lineDto.packQuantity = product.packQuantity.toString();
		}

		// 金額
		discountTotal *= -1;
		BigDecimal bdPrice = DiscountUtil.getScaleValue(dto.priceFractCategory,
				0, new BigDecimal(discountTotal));
		lineDto.unitRetailPrice = bdPrice.toString();
		lineDto.unitCost = bdPrice.toString();
		lineDto.cost = bdPrice.toString();
		lineDto.retailPrice = bdPrice.toString();
		// 課税区分
		lineDto.taxCategory = product.taxCategory;
		// 税率
		// 税率は日付ではなく画面の消費税率選択で決定される
		//lineDto.ctaxRate = taxRate.taxRate.toString();
		lineDto.ctaxRate = dto.ctaxRate;

		// 消費税
		calcDetailLineTax(dto, lineDto, product);
		// 粗利益
		lineDto.gm = "0";

		// 設定
		if (lineNo == -1) {
			// 行数
			Integer size = validSize(dto) + 1;
			lineDto.salesSlipId = dto.salesSlipId;
			lineDto.lineNo = size.toString();
			dto.getLineDtoList().add(lineDto);
		} else {
			// 行数
			lineDto.salesLineId = salesDiscountPriceLineDto.salesLineId;
			lineDto.salesSlipId = dto.salesSlipId;
			lineDto.lineNo = Integer.toString(lineNo + 1);
			dto.getLineDtoList().set(lineNo, lineDto);
		}
	}

	/**
	 * 代引き手数料を明細に追加します.
	 * @param customer 顧客情報（{@link CustomerJoin}）
	 * @param normalTotalPrice 計算対象金額
	 * @param codFeePriceLineDto 代引き手数料の明細行（{@link SalesLineDto}）
	 * @param lineNo　代引き手数料が存在する行番号　－１はなし
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @throws ServiceException
	 */
	protected void addCodFee(CustomerJoin customer, Double normalTotalPrice,
			SalesLineDto codFeePriceLineDto, int lineNo, SalesSlipDto dto)
			throws ServiceException {
		if (normalTotalPrice == null) {
			// 計算対象が無い場合は、追加もしない
			return;
		}

		// 代引き手数料の取得
		String codFeePrice = CodFeeUtil.getCodFee(customer, normalTotalPrice);
		if (codFeePrice == null) {
			// 代引き以外は登録しない
			return;
		}

		SalesLineDto lineDto = (SalesLineDto) dto.createLineDto();

		// 状態
		lineDto.status = SalesLineTrn.STATUS_INIT;
		// 商品コード
		lineDto.productCode = Constants.EXCEPTIANAL_PRODUCT_CODE.COD_FEE_PRICE;
		// 商品名
		ProductJoin product;
		try {
			product = productService.findById(lineDto.productCode);
		} catch (ServiceException e) {
			e.printStackTrace();
			String strLabel = MessageResourcesUtil
					.getMessage("labels.productCode");
			throw new ServiceException(strLabel + lineDto.productCode);
		}
		lineDto.productAbstract = product.productName;
		// 数量
		lineDto.quantity = "1";

		// 完納を設定
		lineDto.deliveryProcessCategory = CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL;

		// 入り数
		if (product.packQuantity == null) {
			lineDto.packQuantity = "";
		} else {
			lineDto.packQuantity = product.packQuantity.toString();
		}

		// 金額
		lineDto.unitRetailPrice = codFeePrice;
		lineDto.unitCost = codFeePrice;
		lineDto.cost = codFeePrice;
		lineDto.retailPrice = codFeePrice;
		// 課税区分
		lineDto.taxCategory = product.taxCategory;
		// 税率
		// 税率は日付ではなく画面の消費税率選択で決定される
		//lineDto.ctaxRate = taxRate.taxRate.toString();
		lineDto.ctaxRate = dto.ctaxRate;

		// 消費税
		calcDetailLineTax(dto, lineDto, product);
		// 粗利益
		lineDto.gm = "0";

		// 設定
		if (codFeePriceLineDto == null) {
			// 行数
			Integer size = validSize(dto) + 1;
			lineDto.lineNo = size.toString();
			dto.getLineDtoList().add(lineDto);
		} else {
			lineDto.salesLineId = codFeePriceLineDto.salesLineId;
			lineDto.salesSlipId = codFeePriceLineDto.salesSlipId;
			// 行数
			lineDto.lineNo = Integer.toString(lineNo + 1);
			dto.getLineDtoList().set(lineNo, lineDto);
		}

	}

	/**
	 * 送料を明細に追加します.
	 * @param customer 顧客情報（{@link CustomerJoin}）
	 * @param normalTotalPrice 計算対象金額(税抜き）
	 * @param postageLineDto 送料の明細行（{@link SalesLineDto}）
	 * @param lineNo　送料が存在する行番号　－１はなし
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @throws ServiceException
	 */
	protected void addPostagePrice(CustomerJoin customer,
			Double normalTotalPrice, SalesLineDto postageLineDto, int lineNo,
			SalesSlipDto dto) throws ServiceException {
		if (normalTotalPrice == null) {
			// 計算対象が無い場合は、追加もしない
			return;
		}

		String postagePrice = null;

		// 自社マスタの送料区分が有料の場合、送料対象かどうか判別する
		if(super.mineDto.iniPostageType.equals(CategoryTrns.POSTAGE_PAY)){

			//送料対象金額 小数点以下を削除した後にStringに変換
			StringBuilder sb1 = new StringBuilder(super.mineDto.targetPostageCharges);
			int n1 = sb1.indexOf(".");
			String targetPostageCharges = new String(sb1.delete(n1, n1+4));

			// 送料対象金額未満は登録しない
			if( normalTotalPrice >= Integer.parseInt(targetPostageCharges)){

				return;
			}else{

				//送料 小数点以下を削除した後にStringに変換
				StringBuilder sb2 = new StringBuilder(super.mineDto.postage);
				int  n2 = sb2.indexOf(".");
				postagePrice = new String(sb2.delete(n2, n2+4));
			}
		}else{
			return;
		}

		SalesLineDto lineDto = (SalesLineDto) dto.createLineDto();

		// 状態
		lineDto.status = SalesLineTrn.STATUS_INIT;

		// 商品コード
		lineDto.productCode = Constants.EXCEPTIANAL_PRODUCT_CODE.POSTAGE_PRICE;

		// 商品名
		ProductJoin product;
		try {
			product = productService.findById(lineDto.productCode);
			if (product == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			String strLabel = MessageResourcesUtil
					.getMessage("labels.productCode");
			throw new ServiceException(strLabel + lineDto.productCode);
		}
		lineDto.productAbstract = product.productName;

		// 数量
		lineDto.quantity = "1";

		// 完納を設定
		lineDto.deliveryProcessCategory = CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL;

		// 入り数
		if (product.packQuantity == null) {
			lineDto.packQuantity = "";
		} else {
			lineDto.packQuantity = product.packQuantity.toString();
		}

		// 金額
		lineDto.unitRetailPrice = postagePrice;
		lineDto.unitCost = postagePrice;
		lineDto.cost = postagePrice;
		lineDto.retailPrice = postagePrice;
		// 課税区分
		lineDto.taxCategory = product.taxCategory;
		// 税率
		// 税率は日付ではなく画面の消費税率選択で決定される
		//lineDto.ctaxRate = taxRate.taxRate.toString();
		lineDto.ctaxRate = dto.ctaxRate;

		// 消費税
		calcDetailLineTax(dto, lineDto, product);

		// 粗利益
		lineDto.gm = "0";

		// 設定
		if (postageLineDto == null) {
			// 行数
			Integer size = validSize(dto) + 1;
			lineDto.salesSlipId = dto.salesSlipId;
			lineDto.lineNo = size.toString();
			dto.getLineDtoList().add(lineDto);
		} else {
			// 行数
			lineDto.salesLineId = postageLineDto.salesLineId;
			lineDto.lineNo = Integer.valueOf(lineNo + 1).toString();
			lineDto.salesSlipId = postageLineDto.salesSlipId;
			dto.getLineDtoList().set(lineNo, lineDto);
		}

	}

	/**
	 * 特殊商品コードも含めた合計金額を計算します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param tax 消費税分も加算するか否か
	 * @return 合計金額
	 */
	public Double sumAll(SalesSlipDto dto, boolean tax) {

		Double total = 0.0;
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (int i = 0; i < salesLineList.size(); i++) {
			SalesLineDto lineDto = salesLineList.get(i);
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}

			total += calculateRetailPrice(tax, lineDto);
		}
		return total;
	}

	/**
	 * 特殊商品コードを除く合計金額を計算します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param tax 消費税分も加算するか否か
	 * @return 合計金額
	 */
	protected Double sumNormalProduct(SalesSlipDto dto, boolean tax) {

		Double total = 0.0;
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (int i = 0; i < salesLineList.size(); i++) {
			SalesLineDto lineDto = salesLineList.get(i);
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}
			if (!isExceptianalProduct(lineDto.productCode)) {
				total += calculateRetailPrice(tax, lineDto);
			}
		}
		return total;
	}

	/**
	 * 代引き手数料と顧客ランク値引き（お取引実績による値引き）を除く合計金額を計算します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param tax 消費税分も加算するか否か
	 * @return 合計金額
	 */
	protected Double sumNotCodeFeeAndSalesDiscount(SalesSlipDto dto, boolean tax) {

		Double total = 0.0;
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (int i = 0; i < salesLineList.size(); i++) {
			SalesLineDto lineDto = salesLineList.get(i);
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}
			if (!Constants.EXCEPTIANAL_PRODUCT_CODE.COD_FEE_PRICE
					.equals(lineDto.productCode)
					&& !Constants.EXCEPTIANAL_PRODUCT_CODE.SALES_DISCOUNT_PRICE
							.equals(lineDto.productCode)) {
				total += calculateRetailPrice(tax, lineDto);
			}
		}
		return total;
	}

	/**
	 * 代引き手数料を除く合計金額を計算します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param tax 消費税分も加算するか否か
	 * @return 合計金額
	 */
	protected Double sumNotCodeFee(SalesSlipDto dto, boolean tax) {

		Double total = 0.0;
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (int i = 0; i < salesLineList.size(); i++) {
			SalesLineDto lineDto = salesLineList.get(i);
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}
			if (!Constants.EXCEPTIANAL_PRODUCT_CODE.COD_FEE_PRICE
					.equals(lineDto.productCode)) {
				total += calculateRetailPrice(tax, lineDto);
			}
		}
		return total;
	}

	/**
	 * 代引き手数料、送料、お取引実績による値引き（顧客ランク値引き）を除く合計金額を計算します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param tax 消費税分も加算するか否か
	 * @return 合計金額
	 */
	protected Double sumNotCodeFeeAndProstageAndSalesDiscount(SalesSlipDto dto,
			boolean tax) {

		Double total = 0.0;
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (int i = 0; i < salesLineList.size(); i++) {
			SalesLineDto lineDto = salesLineList.get(i);
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}
			if (!Constants.EXCEPTIANAL_PRODUCT_CODE.COD_FEE_PRICE
					.equals(lineDto.productCode)
					&& !Constants.EXCEPTIANAL_PRODUCT_CODE.POSTAGE_PRICE
							.equals(lineDto.productCode)
					&& !Constants.EXCEPTIANAL_PRODUCT_CODE.SALES_DISCOUNT_PRICE
							.equals(lineDto.productCode)) {
				total += calculateRetailPrice(tax, lineDto);
			}
		}
		return total;
	}

	/**
	 * 明細行の上代金額に消費税を加算します.
	 * @param tax 消費税分も加算するか否か
	 * @param lineDto 明細行（{@link SalesLineDto}）
	 * @return
	 */
	private Double calculateRetailPrice(boolean tax, SalesLineDto lineDto) {
		Double retailPrice = Double.valueOf(lineDto.retailPrice.replaceAll(",",
				""));
		if (tax && StringUtil.hasLength(lineDto.ctaxPrice)) {
			Double ctaxPrice = Double.valueOf(lineDto.ctaxPrice.replaceAll(",",
					""));
			// 税込金額合計
			return retailPrice + ctaxPrice;
		} else {
			return retailPrice;
		}
	}

	/**
	 * 特殊商品コードの判定を行います.
	 * @param productCode　商品コード
	 * @return true 特殊商品コードか否か
	 */
	public boolean isExceptianalProduct(String productCode) {

		for (int j = 0; j < Constants.EXCEPTIANAL_PRODUCT_CODE_LIST.length; j++) {
			if (Constants.EXCEPTIANAL_PRODUCT_CODE_LIST[j].equals(productCode)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 明細行の消費税金額を計算します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param lineDto 明細行（{@link SalesLineDto}）
	 * @param product 商品コード
	 * @throws ServiceException
	 */
	protected void calcDetailLineTax(SalesSlipDto dto, SalesLineDto lineDto,
			ProductJoin product) throws ServiceException {

		// 課税区分を確認
		if (CategoryTrns.TAX_CATEGORY_FREE.equals(product.taxCategory)) {
			// 免税
			lineDto.ctaxPrice = "";
		} else if (CategoryTrns.TAX_CATEGORY_IMPOSITION.equals(product.taxCategory)) {
			// 課税
			Double rate = Double.valueOf(lineDto.ctaxRate);
			Double price = Double.valueOf(lineDto.retailPrice);
			Double tax;
			if ((price != null) && (rate != null)) {
				tax = (rate / 100.0) * price; // rateは％表記の値なので100.0で割る
				BigDecimal bd = DiscountUtil.getScaleValue(
						dto.taxFractCategory, 0, new BigDecimal(tax));
				if (bd != null) {
					lineDto.ctaxPrice = bd.toString();
				} else {
					lineDto.ctaxPrice = "";
				}
			} else {
				throw new ServiceException("errors.system");
			}
		} else if (CategoryTrns.TAX_CATEGORY_INCLUDED.equals(product.taxCategory)) {
			// 内税
			lineDto.ctaxPrice = "";
		}
	}

	/**
	 * 空白行の除いた有効行数を返します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @return 空白行の除いた有効行数
	 */
	protected int validSize(SalesSlipDto dto) {
		int cnt = 0;
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (int i = 0; i < salesLineList.size(); i++) {
			SalesLineDto lineDto = salesLineList.get(i);
			if (!lineDto.isBlank()) {
				cnt++;
			}
		}
		return cnt;
	}

	/**
	 * 売上伝票の帳票出力カウンタを更新します.
	 * @param salesSlipId　伝票ID
	 * @param outputList　更新対象となるConstants.REPORT_TEMPLATEを入れたリスト 送り状印刷も含めてます
	 * @return　更新行数
	 * @throws ServiceException
	 */
	public int updatePrintCount(String salesSlipId, ArrayList<String> outputList)
			throws ServiceException {
		int SuccessedLineCount = 0;
		int billPrintCount = 0;
		int deliveryPrintCount = 0;
		int tempDeliveryPrintCount = 0;
		int shippingPrintCount = 0;
		int estimatePrintCount = 0;
		int delborPrintCount = 0;
		int poPrintCount = 0;
		int siPrintCount = 0;

		for (int i = 0; i < outputList.size(); i++) {
			String reportType = outputList.get(i);
			if (Constants.REPORT_TEMPLATE.REPORT_ID_A.equals(reportType)) {
				estimatePrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_B.equals(reportType)) {
				estimatePrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_C.equals(reportType)) {
				deliveryPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_D.equals(reportType)) {
				deliveryPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_E.equals(reportType)) {
				tempDeliveryPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_F.equals(reportType)) {
				delborPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_G.equals(reportType)) {
				billPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_H.equals(reportType)) {
				billPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_I.equals(reportType)) {
				billPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_J.equals(reportType)) {
				shippingPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_K.equals(reportType)) {
				shippingPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.REPORT_ID_L.equals(reportType)) {
				poPrintCount = 1;
			} else if (Constants.REPORT_TEMPLATE.SI.equals(reportType)) {
				siPrintCount = 1;
			}

		}

		//MAPの生成
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.SALES_SLIP_ID, salesSlipId);
		param.put(Param.BILL_PRINT_COUNT, billPrintCount);
		param.put(Param.DELIVERY_PRINT_COUNT, deliveryPrintCount);
		param.put(Param.TEMP_DELIVERY_PRINT_COUNT, tempDeliveryPrintCount);
		param.put(Param.SHIPPING_PRINT_COUNT, shippingPrintCount);
		param.put(Param.ESTIMATE_PRINT_COUNT, estimatePrintCount);
		param.put(Param.DELBOR_PRINT_COUNT, delborPrintCount);
		param.put(Param.PO_PRINT_COUNT, poPrintCount);
		param.put(Param.SI_PRINT_COUNT, siPrintCount);

		//SQLクエリを投げる
		SuccessedLineCount = this.updateBySqlFile(
				"sales/UpdateSalesSlipPrintCnt.sql", param).execute();

		return SuccessedLineCount;
	}

	/**
	 * 出荷指示書に対して不足している受注伝票情報を設定します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param pl  出荷指示書{@link PickingList}）
	 * @throws ServiceException
	 */
	protected void setROrderInfoForSave(SalesSlipDto dto, PickingList pl)
			throws ServiceException {

		if (!StringUtil.hasLength(dto.roSlipId)) {
			return;
		}

		try {
			ROrderSlipDto orderSlipDto = roSlipSalesService
					.loadBySlipId(dto.roSlipId);
			if (orderSlipDto == null) {
				throw new Exception();
			}
			// 受注日
			pl.roDate = super.convertUtilDateToSqlDate(DF_YMD
					.parse(orderSlipDto.roDate));

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("errors.system");
		}
	}

	/**
	 * 売上伝票に対して不足している顧客情報を設定します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param customer 顧客情報（{@link CustomerJoin}）
	 * @throws ServiceException
	 */
	protected void setCustomerInfoForSave(SalesSlipDto dto,
			CustomerJoin customer) throws ServiceException {

		if (!StringUtil.hasLength(dto.customerCode)) {
			return;
		}

		try {
			dto.customerZipCode = customer.customerZipCode;
			dto.customerAddress1 = customer.customerAddress1;
			dto.customerAddress2 = customer.customerAddress2;
			dto.customerPcName = customer.customerPcName;
			dto.customerTel = customer.customerTel;

			dto.customerUrl = customer.customerUrl;
			dto.customerOfficeName = customer.customerOfficeName;
			dto.customerOfficeKana = customer.customerOfficeKana;
			dto.customerAbbr = customer.customerAbbr;
			dto.customerDeptName = customer.customerDeptName;
			dto.customerPcPost = customer.customerPcPost;
			dto.customerPcKana = customer.customerPcKana;
			dto.customerPcPreCategory = customer.customerPcPreCategory;
			if (StringUtil.hasLength(customer.customerPcPreCategory)) {
				CategoryTrn categoryTrn = categoryService
						.findCategoryTrnByIdAndCode(Categories.PRE_TYPE,
								customer.customerPcPreCategory);
				if (categoryTrn != null) {
					dto.customerPcPre = categoryTrn.categoryCodeName;
				} else {
					dto.customerPcPre = "";
				}
			} else {
				dto.customerPcPre = "";
			}
			dto.customerFax = customer.customerFax;
			dto.customerEmail = customer.customerEmail;

			dto.priceFractCategory = customer.priceFractCategory;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("errors.system");
		}
	}

	/**
	 * 伝票内に存在する送料の合計を求めます.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @return　送料合計値
	 */
	protected Double sumTotalPostage(SalesSlipDto dto) {
		Double ret = 0.0;
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (SalesLineDto lineDto : salesLineList) {
			if (Constants.EXCEPTIANAL_PRODUCT_CODE.POSTAGE_PRICE
					.equals(lineDto.productCode)) {
				Double retailPrice = Double.valueOf(lineDto.retailPrice
						.replaceAll(",", ""));
				ret += retailPrice;
			}
		}
		return ret;
	}

	/**
	 * 売上伝票番号を指定して、売上伝票情報を取得します.
	 * @param id 売上伝票番号
	 * @return 売上伝票（{@link SalesSlipDto}）
	 * @throws ServiceException
	 */
	@Override
	public SalesSlipDto loadBySlipId(String id) throws ServiceException,
			UnabledLockException {

		// 伝票番号を指定して伝票を取得
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.SALES_SLIP_ID, id);

		List<SalesSlipTrn> ssList = findByCondition(conditions, params,
				"sales/FindSalesSlip.sql");

		SalesSlipDto dto = null;
		if (ssList.size() == 1) {
			dto = createAndCopy(ssList.get(0));
		} else {
			return null;
		}
		// 支払条件を生成
		dto.cutoffGroupCategory = dto.billCutoffGroup
				+ dto.paybackCycleCategory;

		// 売上単位の請求書の発行日有無(請求書日付有無)を設定する
		Customer customer = customerService
				.findCustomerByCode(dto.customerCode);
		dto.billDatePrint = customer.billDatePrint;

		return dto;
	}

	/**
	 * 売上伝票の新規登録・更新処理を行います.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @param abstractServices 保存で使用するサービス
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int save(SalesSlipDto dto, AbstractService<?>... abstractServices)
			throws ServiceException, UnabledLockException {

		if (dto.salesSlipId == null || dto.salesSlipId.length() == 0) {
			return insertRecord(dto);
		}
		return updateRecord(dto);
	}

	/**
	 * 売上伝票を登録します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(SalesSlipDto dto) throws ServiceException {
		Long newSlipId = -1L;

		try {
			CustomerJoin customer = customerService.findById(dto.customerCode);
			if (customer == null) {
				throw new Exception();
			}
			// 特殊商品コードの追加
			insertSpecialProduct(dto, customer);

			// 売上伝票番号の発番
			newSlipId = seqMakerService.nextval(SalesSlipTrn.TABLE_NAME);
			dto.salesSlipId = newSlipId.toString();

			// 請求日
			if (CategoryTrns.SALES_CM_CREDIT.equals(dto.salesCmCategory)) {
				dto.billDate = null;
			} else {
				dto.billDate = dto.salesDate;
			}

			// 売上伝票画面から値を設定（明細の集計等）
			setSlipDataByForm(dto);
			// 不足している顧客情報を設定
			setCustomerInfoForSave(dto, customer);
			// 出力カウンタを初期化
			dto.billPrintCount = "0";
			dto.deliveryPrintCount = "0";
			dto.tempDeliveryPrintCount = "0";
			dto.shippingPrintCount = "0";
			dto.siPrintCount = "0";
			dto.estimatePrintCount = "0";
			dto.poPrintCount = "0";
			dto.delborPrintCount = "0";

			// ActionFormをエンティティに変換
			SalesSlipTrn ss = createAndCopy(dto);

			// 売上伝票の追加
			return this.updateBySqlFile("sales/InsertSalesSlip.sql",
					createParamMap(ss)).execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 売上伝票を更新します.
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @return ロック結果
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(SalesSlipDto dto) throws UnabledLockException,
			ServiceException {
		try {
			CustomerJoin customer = customerService.findById(dto.customerCode);
			if (customer == null) {
				throw new Exception();
			}
			// 特殊商品コードの追加
			insertSpecialProduct(dto, customer);

			// 排他制御
			this.lockRecord(Param.SALES_SLIP_ID, dto.salesSlipId,
					dto.updDatetm, "sales/LockSlip.sql");

			// 入力データをもとに伝票情報を設定
			setSlipDataByForm(dto);
			// 不足している顧客情報を設定
			setCustomerInfoForSave(dto, customer);
			// DTOをEntityに変換
			SalesSlipTrn ss = createAndCopy(dto);

			// 伝票の更新
			return this.updateBySqlFile("sales/UpdateSalesSlip.sql",
					createParamMap(ss)).execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 *
	 * @param salesSlipId 売上伝票番号
	 * @param updDatetm 更新日時
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int deleteById(String salesSlipId, String updDatetm)
			throws ServiceException, UnabledLockException {
		try {
			// 排他制御
			this.lockRecord(Param.SALES_SLIP_ID, salesSlipId, updDatetm,
					"sales/LockSlip.sql");

			// 明細行が削除されている場合、受注伝票を更新できなくなるのでDBの状態を再取得
			SalesSlipDto dto = loadBySlipId(salesSlipId);
			dto.setLineDtoList(salesLineService.loadBySlip(dto));

			// 明細行からの更新
			List<SalesLineDto> salesLineList = dto.getLineDtoList();
			for (SalesLineDto lineDto : salesLineList) {
				SalesLineTrn sl = salesLineService.createAndCopy(
						dto.priceFractCategory, dto.taxFractCategory, lineDto);
				// 出荷指示書明細行の削除
				PickingLine pll = new PickingLine();
				pll.salesLineId = sl.salesLineId;
				pickingLineService.delete(pll);

				// 受注伝票明細行の更新
				if (sl.roLineId != null) {
					if (StringUtil.hasLength(sl.roLineId.toString())) {
						SalesLineTrn tmpSl = salesLineService.createAndCopy(
								dto.priceFractCategory, dto.taxFractCategory,
								lineDto);
						roSlipSalesService.deleteSlipLine(dto, tmpSl);
					}
				}
			}
			// DTOをエンティティに変換
			SalesSlipTrn ss = createAndCopy(dto);

			// 伝票の削除
			int count = this.updateBySqlFile("sales/DeleteSalesSlip.sql",
					createParamMap(ss)).execute();
			if (count == 0) {
				throw new ServiceException(getDbMessage("erroes.db.salesSlip",
						"erroes.db.delete", "(SalesService.delete 3)"));
			}
			// 出荷指示書の削除
			PickingList pl = new PickingList();
			pl.salesSlipId = ss.salesSlipId;
			if (pickingService.delete(pl) == 0) {
				throw new ServiceException(getDbMessage(
						"erroes.db.pickingList", "erroes.db.delete",
						"(SalesService.delete 4)"));
			}
			// 受注伝票の更新
			if (StringUtil.hasLength(dto.roSlipId)) {
				if (roSlipSalesService.updateSlipBySales(dto) == 0) {
					throw new ServiceException(getDbMessage("erroes.db.roSlip",
							"erroes.db.delete", "(SalesService.delete 5)"));
				}
			}

			return count;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 受注伝票を更新します.
	 * @param bInsert 新規登録か否か
	 * @param dto 売上伝票（{@link SalesSlipDto}）
	 * @return 更新件数
	 * @throws ServiceException
	 * @throws Exception
	 */
	public int saveROrder(boolean bInsert, SalesSlipDto dto)
			throws ServiceException, Exception {

		int totalCount = 0;
		if (bInsert) {
			// 出荷指示書の追加

			// 出荷指示書番号の発番
			PickingList pl = pickingService.createPickingList(dto);
			setROrderInfoForSave(dto, pl);
			int count = pickingService.insert(pl);
			if (count == 0) {
				if (bInsert) {
					throw new ServiceException(getDbMessage(
							"erroes.db.pickingList", "erroes.db.insert", ""));
				} else {
					throw new ServiceException(getDbMessage(
							"erroes.db.pickingList", "erroes.db.update", ""));
				}
			}
			totalCount += count;

			// 出荷指示書明細の登録 --------------------------------
			List<SalesLineDto> lineList = dto.getLineDtoList();
			for (SalesLineDto lineDto : lineList) {
				if (lineDto.isBlank()) {
					continue;
				}
				PickingLine pll = pickingLineService.createPickingLine(lineDto,
						pl.pickingListId.toString());
				// 出荷指示書は売上伝票をもとに作成するのでDTOは使用しない
				count = pickingLineService.insert(pll);
				if (count == 0) {
					if (bInsert) {
						throw new ServiceException(
								getDbMessage("erroes.db.pickingLine",
										"erroes.db.insert", ""));
					} else {
						throw new ServiceException(
								getDbMessage("erroes.db.pickingLine",
										"erroes.db.update", ""));
					}
				}
				totalCount += count;
			}
		} else {
			// 出荷指示書の取得
			boolean isPickingListExist = true;
			List<PickingList> plList = pickingService
					.findPickingListBySalesSlipId(dto.salesSlipId);
			if (plList.size() == 0) {
				// 移行データ
				isPickingListExist = false;
			}

			// 出荷指示書の更新
			if (isPickingListExist) {
				PickingList pl = new PickingList();
				pl.salesSlipId = Integer.valueOf(dto.salesSlipId);
				pl.pickingListId = plList.get(0).pickingListId;
				if (pickingService.update(pl) == 0) {
					throw new ServiceException(getDbMessage(
							"erroes.db.pickingList", "erroes.db.update",
							"(SalesService.update 9)"));
				}

				int count = 0;
				// 出荷指示書明細行の更新
				List<PickingLine> picklineList = pickingLineService.findPickingLineBySalesSlipId(dto.salesSlipId);

				List<SalesLineDto> lineList = dto.getLineDtoList();
				for (SalesLineDto lineDto : lineList) {
					if (lineDto.isBlank()) {
						continue;
					}
					boolean isExist = false;
					for (PickingLine pll : picklineList) {
						if( pll.salesLineId.toString().equals(lineDto.salesLineId)){
							isExist = true;

							count = pickingLineService.update(pll);
							if (count == 0) {
								throw new ServiceException(
										getDbMessage("erroes.db.pickingLine",
												"erroes.db.update", ""));
							}
							totalCount += count;
						}
					}
					if( !isExist ){
						PickingLine pll = pickingLineService.createPickingLine(lineDto,
								pl.pickingListId.toString());
						// 出荷指示書は売上伝票をもとに作成するのでDTOは使用しない
						count = pickingLineService.insert(pll);
						if (count == 0) {
							throw new ServiceException(
									getDbMessage("erroes.db.pickingLine",
											"erroes.db.update", ""));
						}
						totalCount += count;
					}
				}
				// 削除レコードの調整
				for (PickingLine pll : picklineList) {

					boolean isExist = false;
					for (SalesLineDto lineDto : lineList) {
						if (lineDto.isBlank()) {
							continue;
						}
						if( pll.salesLineId.toString().equals(lineDto.salesLineId)){
							isExist = true;
							break;
						}
					}
					if( !isExist ){
						count = pickingLineService.delete(pll);
						if (count == 0) {
							throw new ServiceException(
									getDbMessage("erroes.db.pickingLine",
											"erroes.db.update", ""));
						}
						totalCount += count;
					}
				}
			}
		}
		// 受注伝票の更新
		if (StringUtil.hasLength(dto.roSlipId)) {
			if (roSlipSalesService.updateSlipBySales(dto) == 0) {
				throw new ServiceException(getDbMessage("erroes.db.roSlip",
						"erroes.db.update", "(SalesService.update 10)"));
			}
		}
		return totalCount;
	}

	/**
	 * テーブル名を返します.
	 * @return 売上伝票テーブル名
	 */
	@Override
	protected String getTableName() {
		return "SALES_SLIP_TRN";
	}
	/**
	 * キーカラム名を返します.
	 * @return 売上伝票テーブルのキーカラム名
	 */
	@Override
	protected String getKeyColumnName() {
		return "SALES_SLIP_ID";
	}
}
