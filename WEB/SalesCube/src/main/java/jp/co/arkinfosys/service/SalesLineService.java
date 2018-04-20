/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.SalesLineTrn;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 売上伝票明細行サービスクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SalesLineService extends
		AbstractLineService<SalesLineTrn, SalesLineDto, SalesSlipDto> {

	/** 発番用サービス */
	public SeqMakerService seqMakerService;

	/** 商品マスタ用サービス */
	@Resource
	private ProductService productService;

	/** 受注伝票用サービス */
	@Resource
	private RoSlipService roSlipService;

	/** 受注伝票明細用サービス */
	@Resource
	private RoLineService roLineService;

	/** 入出庫伝票用サービス */
	@Resource
	private ProductStockService productStockService;

	/**
	 * SQLファイルのパラメータ名定義
	 */
	public static class Param {
		private static final String SORT_ORDER = "sortOrder"; // ソート方向
		private static final String ROW_COUNT = "rowCount"; // 取得件数
		private static final String OFFSET_ROW = "offsetRow"; // 取得件数
		public static final String SALES_LINE_ID = "salesLineId";
		public static final String SALES_LINE_IDS = "salesLineIds";
		public static final String SALES_SLIP_ID = "salesSlipId";
		public static final String LINE_NO = "lineNo";
		private static final String SORT_COLUMN_LINE_NO = "sortColumnLineNo"; // 行番号のソート条件
		private static final String STATUS = "status"; // 状態
		private static final String SLIP_STATUS = "slipStatus"; // 伝票状態
		private static final String CUSTOMER_CODE = "customerCode"; // 顧客番号
		public static final String SALES_DATE = "salesDate"; // 売上日
		public static final String SALES_DATE_FROM = "salesDateFrom"; // 売上日(範囲指定FROM)
		public static final String SALES_DATE_TO = "salesDateTo"; // 売上日(範囲指定TO)
		private static final String SORT_COLUMN_SLIP_ID = "sortColumnSlipId"; // 伝票番号のソート条件
		public static final String BILL_CUTOFF_DATE = "billCutoffDate"; // 請求締日
		public static final String BILL_ID = "billId"; // 請求書番号
		public static final String RO_LINE_ID = "roLineId";
		public static final String QUANTITY = "quantity"; // 数量
		public static final String IS_CONTAIN_CLOSE_LEAK = "isContainCloseLeak";
		public static final String LEAK_CHECK_CUTOFF_DATE = "leakCheckCutoffDate";
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
		public static final String SALES_CUTOFF_DATE = "salesCutoffDate"; // 売掛締日
	}

	/**
	 * 初期化用
	 */
	public String[] params = { Param.SORT_ORDER, Param.ROW_COUNT,
			Param.OFFSET_ROW, Param.SALES_LINE_ID, Param.SALES_SLIP_ID,
			Param.LINE_NO, Param.SORT_COLUMN_LINE_NO, Param.STATUS,
			Param.SLIP_STATUS, Param.CUSTOMER_CODE, Param.SALES_DATE,
			Param.SALES_DATE_FROM, Param.SALES_DATE_TO,
			Param.SORT_COLUMN_SLIP_ID, Param.BILL_CUTOFF_DATE, Param.BILL_ID,
			Param.RO_LINE_ID, Param.IS_CONTAIN_CLOSE_LEAK,
			Param.LEAK_CHECK_CUTOFF_DATE, Param.SALES_CM_CATEGORY,
			Param.SALES_CUTOFF_DATE };

	/** 売上明細の行番号のカラム名 */
	public static final String SORT_COLUMN_SALES_LINE_NO = "LINE_NO";

	/** 売上伝票番号のカラム名 */
	public static final String SORT_COLUMN_SALES_SLIP_ID = "SALES_SLIP_ID";

	/**
	 * エンティティ情報から登録用Mapオブジェクトを生成します.
	 *
	 * @param sl　売上伝票明細行{@link SalesLineTrn}
	 * @return　登録用マップ
	 */
	private Map<String, Object> createParamMap(SalesLineTrn sl) {

		// MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		// アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, sl).execute();
		param.putAll(AFparam);

		// 更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return param;
	}

	/**
	 * DTOからEntityへの変換します.
	 *
	 * @param unitFract 単価端数処理区分
	 * @param taxFract 税端数処理区分
	 * @param lineDto　明細行{@link SalesLineDto}
	 * @return　エンティティ{@link SalesLineTrn}
	 */
	public SalesLineTrn createAndCopy(String unitFract, String taxFract,
			SalesLineDto lineDto) {
		NumberConverter convSP = createStatusPriceConverter();
		NumberConverter convPD = createProductNumConverter();
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createTaxPriceConverter(taxFract);
		return Beans
				.createAndCopy(SalesLineTrn.class, lineDto)
				.converter(convSP, "gm")
				.converter(convPD, "quantity")
				.converter(convUP, "unitPrice", "unitRetailPrice",
						"retailPrice", "retailPrice", "unitCost", "cost",
						"unitCost")
				.converter(convTax, "ctaxPrice")
				.dateConverter(Constants.FORMAT.TIMESTAMP, "creDatetm",
						"updDatetm").execute();
	}

	/**
	 * EntiryからDTOへの変換します.
	 * @param unitFract 単価端数処理区分
	 * @param taxFract 税端数処理区分
	 * @param ss 明細行{@link SalesLineTrn}
	 * @return　DTO{@link SalesLineDto}
	 */
	public SalesLineDto createAndCopy(String unitFract, String taxFract,
			SalesLineTrn ss) {
		NumberConverter convSP = createStatusPriceConverter();
		NumberConverter convPD = createProductNumConverter();
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createTaxPriceConverter(taxFract);
		return Beans
				.createAndCopy(SalesLineDto.class, ss)
				.converter(convSP, "gm")
				.converter(convPD, "quantity")
				.converter(convUP, "unitPrice", "unitRetailPrice",
						"retailPrice", "retailPrice", "unitCost", "cost",
						"unitCost")
				.converter(convTax, "ctaxPrice")
				.dateConverter(Constants.FORMAT.TIMESTAMP, "creDatetm",
						"updDatetm").execute();
	}

	/**
	 * DTOからEntiryへ値のコピーをします．
	 * @param unitFract 単価端数処理区分
	 * @param taxFract 税端数処理区分
	 * @param lineDto　コピー元{@link SalesLineDto}
	 * @param ss　コピー先{@link SalesLineTrn}
	 */
	public void copy(String unitFract, String taxFract, SalesLineDto lineDto,
			SalesLineTrn ss) {
		NumberConverter convSP = createStatusPriceConverter();
		NumberConverter convPD = createProductNumConverter();
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createTaxPriceConverter(taxFract);
		Beans.copy(lineDto, ss)
				.converter(convSP, "gm")
				.converter(convPD, "quantity")
				.converter(convUP, "unitPrice", "unitRetailPrice",
						"retailPrice", "retailPrice", "unitCost", "cost",
						"unitCost")
				.converter(convTax, "ctaxPrice")
				.dateConverter(Constants.FORMAT.TIMESTAMP, "creDatetm",
						"updDatetm").execute();
	}

	/**
	 * EntiryからDTOへ値のコピーをします．
	 * @param unitFract 単価端数処理区分
	 * @param taxFract 税端数処理区分
	 * @param ss　コピー元{@link SalesLineTrn}
	 * @param lineDto　コピー先{@link SalesLineDto}
	 */
	public void copy(String unitFract, String taxFract, SalesLineTrn ss,
			SalesLineDto lineDto) {
		NumberConverter convSP = createStatusPriceConverter();
		NumberConverter convPD = createProductNumConverter();
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createTaxPriceConverter(taxFract);
		Beans.copy(ss, lineDto)
				.converter(convSP, "gm")
				.converter(convPD, "quantity")
				.converter(convUP, "unitPrice", "unitRetailPrice",
						"retailPrice", "retailPrice", "unitCost", "cost",
						"unitCost")
				.converter(convTax, "ctaxPrice")
				.dateConverter(Constants.FORMAT.TIMESTAMP, "creDatetm",
						"updDatetm").execute();
	}

	/**
	 * 請求締解除処理時の明細行を設定します.
	 *
	 * @param sl　明細行{@link SalesLineTrn}
	 *
	 */
	public void setReOpenSalesLine(SalesLineTrn sl) {
		sl.status = SalesLineTrn.STATUS_INIT;
	}

	/**
	 * 　売上伝票番号を指定して売上伝票の明細行を行番号昇順で取得します.
	 *
	 * @param salesSlipId　売上伝票番号
	 * @return 明細行{@link SalesLineTrn}のリスト
	 * @throws ServiceException
	 */
	public List<SalesLineTrn> findSalesLineBySalesSlipId(String salesSlipId)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.SALES_SLIP_ID, salesSlipId);
		conditions.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_SALES_LINE_NO);
		conditions.put(Param.SORT_ORDER, "ASC");

		return findByCondition(conditions, params, "sales/FindSalesLine.sql");

	}

	/**
	 * 　売上伝票明細番号を指定して売上伝票の明細行を取得します.
	 *
	 * @param salesLineId 売上伝票明細番号
	 * @return 明細行{@link SalesLineTrn}
	 * @throws ServiceException
	 */
	public SalesLineTrn findSalesLineBySalesLineId(String salesLineId)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.SALES_LINE_ID, salesLineId);

		List<SalesLineTrn> list = findByCondition(conditions, params, "sales/FindSalesLine.sql");
		if( list != null && list.size() > 0 ){
			return list.get(0);
		}
		return null;

	}

	/**
	 * 　売上伝票番号を指定して売上伝票の明細行を行番号昇順で取得します.
	 *
	 * @param salesSlipId　売上伝票番号
	 * @return 明細行のBeanMapリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findSalesLinesBySalesSlipIdSimple(String salesSlipId)
			throws ServiceException {

		// 条件設定
		// 顧客コードが一致
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.SALES_SLIP_ID, salesSlipId);
			param.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_SALES_LINE_NO);
			param.put(Param.SORT_ORDER, Constants.SQL.ASC);
			return this.selectBySqlFile(BeanMap.class,
					"sales/FindSalesLineForSalesBill.sql", param)
					.getResultList();
		} catch (SNonUniqueResultException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 　請求書番号を指定して紐付く売上伝票の明細行を売上伝票番号、行番号昇順で取得します.
	 *
	 * @param billId　請求書番号
	 * @return 明細行のBeanMapリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findSalesLinesByBillIdSimple(String billId)
			throws ServiceException {

		// 条件設定
		// 顧客コードが一致
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.BILL_ID, billId);
			return this.selectBySqlFile(BeanMap.class,
					"sales/FindSalesLineForBill.sql", param).getResultList();
		} catch (SNonUniqueResultException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 　顧客コードを指定して売上日が指定した日付【以前】に発行された 未請求の売上伝票に紐付く明細行を売上伝票番号、行番号昇順で取得します.
	 *
	 * @param customerCode　顧客コード
	 * @param closeDate　 締処理日付
	 * @param salesCmCategory　売上取引区分 ＝SALES_CM_CASH_ON_DELIVERY：代引き（売掛以外）　 ＝SALES_CM_CREDIT：売掛
	 * @return 明細行{@link SalesLineTrn}のリスト
	 * @throws ServiceException
	 */
	public List<SalesLineTrn> findOpenSalesLineByCustomerCode(
			String customerCode, String closeDate, String salesCmCategory)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.STATUS, SalesLineTrn.STATUS_INIT);
		conditions.put(Param.SLIP_STATUS, SalesSlipTrn.STATUS_INIT);
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SALES_DATE, closeDate);
		conditions.put(Param.SALES_CM_CATEGORY, salesCmCategory);
		conditions.put(Param.SORT_COLUMN_SLIP_ID, SORT_COLUMN_SALES_SLIP_ID);
		conditions.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_SALES_LINE_NO);
		conditions.put(Param.SORT_ORDER, "ASC");

		return findByCondition(conditions, params,
				"sales/FindOpenSalesLine.sql");

	}

	/**
	 * 　顧客コードを指定して売上日が指定した日付【以前】に発行された 未売掛締めの売上伝票に紐付く明細行を売上伝票番号、行番号昇順で取得します.
	 *
	 * @param customerCode 顧客コード
	 * @param closeDate　締処理日付
	 * @return 明細行{@link SalesLineTrn}のリスト
	 * @throws ServiceException
	 */
	public List<SalesLineTrn> findArtOpenSalesLineByCustomerCode(
			String customerCode, String closeDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SALES_DATE, closeDate);
		conditions.put(Param.SALES_CUTOFF_DATE, null);
		conditions.put(Param.SORT_COLUMN_SLIP_ID, SORT_COLUMN_SALES_SLIP_ID);
		conditions.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_SALES_LINE_NO);
		conditions.put(Param.SORT_ORDER, "ASC");

		return findByCondition(conditions, params,
				"sales/FindArtOpenSalesLine.sql");

	}

	/**
	 * 　顧客コードを指定して売上日が指定した日付【以前】に発行された 未請求の売上伝票に紐付く明細行を売上伝票番号、行番号昇順で取得します.
	 *
	 * @param customerCode 顧客コード
	 * @param fromDate 期間の開始日
	 * @param toDate 期間の終了日
	 * @param salesCutoffDate 売掛締日
	 * @return 明細行{@link SalesLineTrn}のリスト
	 */
	public List<SalesLineTrn> findSalesLineByCustomerCodeBetweenDate(
			String customerCode, String fromDate, String toDate,
			String salesCutoffDate) throws ServiceException {
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.STATUS, SalesLineTrn.STATUS_INIT);
		conditions.put(Param.SLIP_STATUS, SalesSlipTrn.STATUS_INIT);
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SORT_COLUMN_SLIP_ID, SORT_COLUMN_SALES_SLIP_ID);
		conditions.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_SALES_LINE_NO);
		conditions.put(Param.SORT_ORDER, Constants.SQL.ASC);
		conditions.put(Param.SALES_DATE_FROM, fromDate);
		conditions.put(Param.SALES_DATE_TO, toDate);
		conditions.put(Param.SALES_CUTOFF_DATE, salesCutoffDate);

		return findByCondition(conditions, params,
				"sales/FindSalesLineBetweenDate.sql");
	}

	/**
	 * 　顧客コードと請求締日を指定して請求済の売上伝票に紐付く明細行を売上伝票番号、行番号昇順で取得します.
	 *
	 * @param customerCode　顧客コード
	 * @param lastCutOffDate　請求締日
	 * @return 明細行{@link SalesLineTrn}のリスト
	 * @throws ServiceException
	 */
	public List<SalesLineTrn> findCloseSalesLineByCustomerCode(
			String customerCode, String lastCutOffDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.STATUS, SalesLineTrn.STATUS_FINISH);
		conditions.put(Param.SLIP_STATUS, SalesSlipTrn.STATUS_FINISH);
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.BILL_CUTOFF_DATE, lastCutOffDate);
		conditions.put(Param.SORT_COLUMN_SLIP_ID, SORT_COLUMN_SALES_SLIP_ID);
		conditions.put(Param.SORT_COLUMN_LINE_NO, SORT_COLUMN_SALES_LINE_NO);

		return findByCondition(conditions, params,
				"sales/FindOpenSalesLine.sql");

	}

	/**
	 * 指定した受注伝票明細行に対して最も新しい売上伝票の売上日を返します.<BR>
	 * 指定した売上伝票は除きます（Form上で書き換えられている可能性があるため）.<BR>
	 * この情報が見つかるのは分納の時だけです.
	 *
	 * @param salesSlipId　売上伝票番号
	 * @param roLineId　受注伝票明細行ID
	 * @return 最終売上（出荷）日
	 * @throws ServiceException
	 */
	public String getLastShipDate(String salesSlipId, String roLineId)
			throws ServiceException {

		try {
			Map<String, Object> conditions = createSqlParam();

			// 条件設定
			// 受注伝票明細行ID
			conditions.put(Param.SALES_SLIP_ID, salesSlipId);
			conditions.put(Param.RO_LINE_ID, roLineId);
			Date lastDate = this.selectBySqlFile(Date.class,
					"sales/FindLastShipDate.sql", conditions).getSingleResult();
			if (lastDate != null) {
				SimpleDateFormat DF_YMD = new SimpleDateFormat(
						Constants.FORMAT.DATE);
				return DF_YMD.format(lastDate);
			} else {
				return null;
			}
		} catch (Exception e) {
			// 対象データが存在しない
			return null;
		}
	}
	/**
	 * 売上伝票DTOを指定して、売上伝票明細行情報を取得します.
	 * @param dto　売上伝票（{@link SalesSlipDto}）
	 * @return  明細行{@link SalesLineDto}のリスト
	 * @throws ServiceException
	 */
	@Override
	public List<SalesLineDto> loadBySlip(SalesSlipDto dto)
			throws ServiceException {
		List<SalesLineTrn> slList = this
				.findSalesLineBySalesSlipId(dto.salesSlipId);

		ROrderSlipDto rosDto = roSlipService.loadBySlipId(dto.roSlipId);
		if (rosDto != null) {
			List<ROrderLineDto> lineList = roLineService.loadBySlip(rosDto);
			rosDto.setLineDtoList(lineList);
		}

		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		if (salesLineList == null) {
			salesLineList = new ArrayList<SalesLineDto>();
		} else {
			salesLineList.clear();
		}

		if (slList != null) {
			int index = 1;
			for (SalesLineTrn sl : slList) {
				SalesLineDto lineDto = this.createAndCopy(
						dto.priceFractCategory, dto.taxFractCategory, sl);
				lineDto.lineNo = String.valueOf(index);
				index++;
				lineDto.bkQuantity = lineDto.quantity.replaceAll(",", "");
				setProductInfo(lineDto);
				setStockInfo(lineDto);
				if (dto.roSlipId != null && !dto.roSlipId.equals("")) {
					for (ROrderLineDto rolDto : rosDto.getLineDtoList()) {
						if (lineDto.roLineId != null
								&& !lineDto.roLineId.equals("")) {
							if (lineDto.roLineId.equals(rolDto.roLineId)) {
								lineDto.roQuantity = rolDto.restQuantity;
							}
						}
					}
				}

				salesLineList.add(lineDto);
			}
		}

		return salesLineList;
	}

	/**
	 * 売上伝票明細行を保存します.
	 * @param slipDto 売上伝票（{@link SalesSlipDto}）
	 * @param lineList 売上伝票明細行（{@link SalesLineDto}）リスト
	 * @param deletedLineIds 削除対象売上伝票明細行ID文字列
	 * @param abstractServices 保存で使用するサービス
	 * @throws ServiceException
	 */
	@Override
	public void save(SalesSlipDto slipDto, List<SalesLineDto> lineList,
			String deletedLineIds
			, AbstractService<?>... abstractServices) throws ServiceException {

		RoSlipSalesService roSlipSalesService = (RoSlipSalesService) abstractServices[0];

		short lineNo = 0;
		for (SalesLineDto lineDto : lineList) {

			// 入力内容が存在する行だけを登録対象とする
			if (lineDto.isBlank()) {
				continue;
			}
			lineNo++;
			lineDto.lineNo = String.valueOf(lineNo);

			// 売上明細の登録 --------------------------------

			if (lineDto.salesLineId == null
					|| lineDto.salesLineId.length() == 0) {
				// 伝票番号の発番
				Long newLineId = seqMakerService
						.nextval(SalesLineTrn.TABLE_NAME);

				lineDto.initForDB(newLineId, slipDto.salesSlipId);
			}
			setProductInfoForSave(lineDto);

			SalesLineTrn sl = this.createAndCopy(slipDto.priceFractCategory,
					slipDto.taxFractCategory, lineDto);

			int count = updateRecord(sl);
			if (count == 0) {
				count = insertRecord(sl);
				try {
					// 受注伝票明細行を更新する
					if( StringUtil.hasLength(lineDto.roLineId) ){
						roSlipSalesService.insertSlipLine(slipDto, lineDto);
					}
				} catch (UnabledLockException e) {
					e.printStackTrace();
					throw new ServiceException(e);
				}
			}else{
				try {
					// 受注伝票明細行を更新する
					if( StringUtil.hasLength(lineDto.roLineId) ){
						roSlipSalesService.updateSlipLine(slipDto, lineDto);
					}
				} catch (UnabledLockException e) {
					e.printStackTrace();
					throw new ServiceException(e);
				}
			}
		}
		if (deletedLineIds != null && deletedLineIds.length() > 0) {
			String[] ids = deletedLineIds.split(",");
			try {
				// 削除する前に、削除対象データを取得して受注伝票明細行を更新する
				for( int i=0 ; i<ids.length ; i++ ){
					if( !StringUtil.hasLength(ids[i])){
						continue;
					}
					SalesLineTrn slt = this.findSalesLineBySalesLineId(ids[i]);
					if( slt.roLineId != null ){
						roSlipSalesService.deleteSlipLine(slipDto, slt);
					}
				}
			} catch (UnabledLockException e) {
				e.printStackTrace();
				throw new ServiceException(e);
			}
			deleteRecordsByLineId(ids);
		}
	}
	/**
	 * 売上伝票明細行を登録します.
	 * @param entity 売上伝票明細行（{@link SalesLineTrn}）
	 * @return 登録件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(SalesLineTrn entity) throws ServiceException {
		return this.updateBySqlFile("sales/InsertSalesLine.sql",
				createParamMap(entity)).execute();
	}
	/**
	 * 売上伝票明細行を更新します.
	 * @param entity 売上伝票明細行（{@link SalesLineTrn}）
	 * @return 更新件数
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(SalesLineTrn entity) throws ServiceException {
		return this.updateBySqlFile("sales/UpdateSalesLine.sql",
				createParamMap(entity)).execute();
	}

	/**
	 * 売上伝票明細行IDを指定して明細行を削除します.
	 * @param id 売上伝票明細行ID
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	public int deleteRecords(String id) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.SALES_SLIP_ID, id);
			return this.updateBySqlFile("sales/DeleteLinesBySlipId.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}
	/**
	 * 売上伝票明細行IDを複数指定して明細行を削除します.
	 *
	 * @param ids 削除対象の売上伝票明細行IDリスト
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	protected int deleteRecordsByLineId(String[] ids) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.SALES_LINE_IDS, ids);
			return this
					.updateBySqlFile("sales/DeleteLinesByLineIds.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 明細行に対して不足している商品情報を設定します.
	 *
	 * @param lineDto　売上伝票明細行（{@link SalesLineDto}）
	 * @throws ServiceException
	 */
	protected void setProductInfoForSave(SalesLineDto lineDto)
			throws ServiceException {

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
		// 得意先商品コード
		lineDto.customerPcode = product.onlinePcode;

		// 単位コード
		lineDto.unitCategory = product.unitCategory;

		// 単位名
		lineDto.unitName = product.unitCategoryName;

		// 入り数
		if (product.packQuantity == null) {
			lineDto.packQuantity = "";
		} else {
			lineDto.packQuantity = product.packQuantity.toString();
		}

	}

	/**
	 * 商品情報を設定します.<BR>
	 * 受注限度数 在庫管理区分 商品が削除されている場合には、0を設定します.
	 *
	 * @param lineDto　売上伝票明細行（{@link SalesLineDto}）
	 * @throws ServiceException
	 */
	protected void setProductInfo(SalesLineDto lineDto) throws ServiceException {

		Product product;
		try {
			product = productService.findById(lineDto.productCode);
			if ((product != null) && (product.roMaxNum != null)) {
				lineDto.roMaxNum = product.roMaxNum.toString();
			} else {
				lineDto.roMaxNum = "0";
			}
			if (product != null) {
				lineDto.stockCtlCategory = product.stockCtlCategory;
			} else {
				lineDto.stockCtlCategory = "";
			}
		} catch (ServiceException e) {
			lineDto.roMaxNum = "0";
			lineDto.stockCtlCategory = "";
		}
	}

	/**
	 * 売上伝票明細行の在庫情報を設定します.
	 *
	 * @param lineDto　売上伝票明細行（{@link SalesLineDto}）
	 * @throws ServiceException
	 */
	protected void setStockInfo(SalesLineDto lineDto) throws ServiceException {

		// 各商品ごとに引当可能数を計算する
		StockInfoDto stockInfo = productStockService
				.calcStockQuantityByProductCode(lineDto.productCode);
		if (stockInfo != null) {
			lineDto.possibleDrawQuantity = String
					.valueOf(stockInfo.possibleDrawQuantity);
		} else {
			lineDto.possibleDrawQuantity = "0";
		}
	}

	/**
	 * 受注伝票明細行の在庫情報を設定します.
	 *
	 * @param lineDto　受注伝票明細行（{@link ROrderLineDto}）
	 * @throws ServiceException
	 */
	protected void setStockInfo(ROrderLineDto lineDto) throws ServiceException {

		// 各商品ごとに引当可能数を計算する
		StockInfoDto stockInfo = productStockService
				.calcStockQuantityByProductCode(lineDto.productCode);
		if (stockInfo != null) {
			lineDto.possibleDrawQuantity = String
					.valueOf(stockInfo.possibleDrawQuantity);
		} else {
			lineDto.possibleDrawQuantity = "0";
		}
	}

	/**
	 * 設定された伝票に関連する情報を全て読み込んでDTOに設定します.
	 *
	 * @param lineDto　売上伝票明細行（{@link SalesLineDto}）
	 * @throws ServiceException
	 */
	public void setStockInfoForm(SalesLineDto lineDto) throws ServiceException {

		// 商品マスタを参照して在庫数等を設定
		setProductInfo(lineDto);

		// 引当可能数設定
		setStockInfo(lineDto);
	}

	/**
	 * キーカラム名を返します.
	 * @return 売上伝票明細行テーブルのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "SALES_SLIP_ID", "SALES_LINE_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return 売上伝票明細行テーブル名
	 */
	@Override
	protected String getTableName() {
		return "SALES_LINE_TRN";
	}
}
