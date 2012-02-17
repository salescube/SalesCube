/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.deposit.DepositSlipDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.entity.Bill;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.DepositLine;
import jp.co.arkinfosys.entity.SalesLineTrn;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 請求サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class BillService extends AbstractService<Bill> {

	
	@Resource
	protected SalesService salesService;

	
	@Resource
	protected SalesLineService salesLineService;

	
	@Resource
	protected DepositSlipService depositSlipService;

	
	@Resource
	protected DepositLineService depositLineService;

	
	@Resource
	protected CustomerService customerService;

	@Resource
	protected CategoryService categoryService;

	@Resource
	private YmService ymService;

	@Resource
	private DeliveryService deliveryService;

	@Resource
	private BillAndArtService billAndArtService;

	@Resource
	private BillOldService billOldService;

	@Resource
	private BillReportService billReportService;

	
	public SeqMakerService seqMakerService;

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		
		private static final String SORT_ORDER = "sortOrder";
		
		private static final String ROW_COUNT = "rowCount";
		
		private static final String OFFSET_ROW = "offsetRow";

		
		public static final String CUSTOMER_CODE = "customerCode";
		
		public static final String CUSTOMER_NAME = "customerName";
		
		public static final String BILL_CUTOFF_DATE = "billCutoffDate";
		
		public static final String BILL_CRT_CATEGORY = "billCrtCategory";
		
		public static final String CUTOFF_GROUP = "cutoffGroup";
		
		public static final String PAYBACK_CYCLE_CATEGORY = "paybackCycleCategory";
		
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
		
		private static final String SORT_COLUMN_CUTOFF_DATE = "sortColumnBillCutoffDate";
		
		private static final String SORT_COLUMN_CUSTOMER_CODE = "sortColumnCustomerCode";
		
		private static final String BILL_YEAR = "billYear";
		
		private static final String BILL_MONTH = "billMonth";
		
		private static final String BILL_ID = "billId";
	}

	public String[] params = { Param.SORT_ORDER, Param.ROW_COUNT,
			Param.OFFSET_ROW, Param.CUSTOMER_CODE, Param.CUSTOMER_NAME,
			Param.BILL_CUTOFF_DATE, Param.BILL_CRT_CATEGORY,
			Param.CUTOFF_GROUP, Param.PAYBACK_CYCLE_CATEGORY,
			Param.SORT_COLUMN_CUTOFF_DATE, Param.SORT_COLUMN_CUSTOMER_CODE,
			Param.BILL_YEAR, Param.BILL_MONTH, Param.BILL_ID };

	public static final String SORT_COLUMN_BILL_CUTOFF_DATE = "BILL_CUTOFF_DATE";
	public static final String SORT_COLUMN_CUSTOMER_CODE = "CUSTOMER_CODE";
	public static final String OTHER_USER = "売掛以外のユーザ";

	private SimpleDateFormat DF_YMD = new SimpleDateFormat(
			Constants.FORMAT.DATE);

	/**
	 * 請求書情報を登録します.
	 * @param bill 請求書情報
	 * @return 登録した件数
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#insert(java.lang.Object)
	 */
	public int insert(Bill bill) {
		int SuccessedLineCount = 0;
		
		Map<String, Object> param = createParamMap(bill);

		
		SuccessedLineCount = this.updateBySqlFile("bill/InsertBill.sql", param)
				.execute();
		return SuccessedLineCount;
	}

	/**
	 * 請求書情報を更新します.
	 * @param bill 請求書情報
	 * @return 更新した件数
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#update(java.lang.Object)
	 */
	public int update(Bill bill) {
		int SuccessedLineCount = 0;
		
		Map<String, Object> param = createParamMap(bill);

		
		SuccessedLineCount = this.updateBySqlFile("bill/UpdateBill.sql", param)
				.execute();
		return SuccessedLineCount;
	}

	/**
	 * 請求書情報を削除します.
	 * @param bill 請求書情報
	 * @return 削除した件数
	 * @see org.seasar.extension.jdbc.service.S2AbstractService#delete(java.lang.Object)
	 */
	public int delete(Bill bill) {
		int SuccessedLineCount = 0;
		
		Map<String, Object> param = createParamMap(bill);

		super.updateAudit(Bill.TABLE_NAME, new String[] { Param.BILL_ID }, new Object[] { bill.billId });

		
		SuccessedLineCount = this.updateBySqlFile("bill/DeleteBill.sql", param)
				.execute();
		return SuccessedLineCount;
	}

	/**
	 * SQL用パラメータマップを作成します.
	 * @param bill 請求書情報
	 * @return パラメータマップ
	 */
	private Map<String, Object> createParamMap(Bill bill) {

		
		Map<String, Object> param = new HashMap<String, Object>();

		
		
		

		
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, bill).execute();
		param.putAll(AFparam);

		
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return param;
	}

	/**
	 * 処理対象伝票のロック状態を確認します.
	 * @param bill 請求書情報
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	protected void isLocked(Bill bill) throws ServiceException,
			UnabledLockException {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.BILL_ID, bill.billId);
		param
				.put(AbstractService.Param.LOCK_RECORD,
						AbstractService.FOR_UPDATE);
		lockRecordBySqlFile("bill/FindBill.sql", param, bill.updDatetm);
	}

	/**
	 * 次の伝票IDを採番します.
	 * @return 採番したID
	 * @throws Exception
	 */
	private Long getNextVal() throws Exception {

		Long newSlipId = -1L;
		
		try {
			newSlipId = seqMakerService.nextval(Bill.TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return newSlipId;
	}

	/**
	 * 顧客コードを指定して、請求書情報のリストを返します.
	 * @param customerCode 顧客コード
	 * @return 請求書情報{@link Bill}のリスト
	 * @throws ServiceException
	 */
	public List<Bill> findLastBillByCustomerCode(String customerCode)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		
		
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.BILL_CRT_CATEGORY, CategoryTrns.BILL_CRT_BILL);
		conditions.put(Param.SORT_COLUMN_CUTOFF_DATE,
				SORT_COLUMN_BILL_CUTOFF_DATE);
		conditions.put(Param.SORT_ORDER, Constants.SQL.DESC);
		conditions.put(Param.ROW_COUNT, "1");
		conditions.put(Param.OFFSET_ROW, "0");

		return findByCondition(conditions, params,
				"bill/FindLastBillByCustomer.sql");

	}

	/**
	 * 顧客コードおよび締日を指定して、請求書情報のリストを返します.
	 * @param customerCodeList 顧客コードリスト
	 * @param cutoffDate 締日
	 * @return 請求書情報{@link Bill}のリスト
	 * @throws ServiceException
	 */
	public List<Bill> findLastBillByCustomerCodeToCutoff(
			List<String> customerCodeList, String cutoffDate)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		
		
		conditions.put(Param.CUSTOMER_CODE, customerCodeList);
		conditions.put(Param.BILL_CUTOFF_DATE, cutoffDate);
		conditions.put(Param.BILL_CRT_CATEGORY, CategoryTrns.BILL_CRT_BILL);
		conditions.put(Param.SORT_COLUMN_CUTOFF_DATE,
				SORT_COLUMN_BILL_CUTOFF_DATE);
		conditions.put(Param.SORT_ORDER, Constants.SQL.DESC);
		conditions.put(Param.ROW_COUNT, "1");
		conditions.put(Param.OFFSET_ROW, "0");

		return findByCondition(conditions, params,
				"bill/FindLastBillByCustomerToCutoff.sql");

	}

	/**
	 * 掛売ユーザの請求締処理を行います.
	 * @param closeDate 締日
	 * @param customerCode 顧客コード
	 * @throws Exception
	 */
	public void closeBillArt(String closeDate, String customerCode)
			throws Exception {
		
		Customer customer = customerService.findCustomerByCode(customerCode);
		List<DeliveryAndPre> deliveryList = deliveryService
				.searchDeliveryByCompleteCustomerCode(customerCode);

		
		List<SalesSlipTrn> salesSlipList = salesService
				.findOpenSalesSlipByCustomerCode(customerCode, closeDate,
						CategoryTrns.SALES_CM_CREDIT);

		
		List<SalesLineTrn> salesLineList = salesLineService
				.findOpenSalesLineByCustomerCode(customerCode, closeDate,
						CategoryTrns.SALES_CM_CREDIT);

		
		List<DepositSlipDto> depositSlipList = depositSlipService
				.findOpenDepositSlipByCustomerCode(customerCode, closeDate,
						CategoryTrns.DEPOSIT_CATEGORY_TRANSFER);

		
		List<DepositLine> depositLineList = depositLineService
				.findOpenDepositLineByCustomerCode(customerCode, closeDate,
						CategoryTrns.DEPOSIT_CATEGORY_TRANSFER);

		
		List<Bill> billList = findLastBillByCustomerCode(customerCode);

		
		Bill newBill;
		if (billList.size() > 0) {
			newBill = createBill(closeDate, customer, deliveryList,
					salesSlipList, salesLineList, depositLineList, billList
							.get(0).thisBillPrice, true);
		} else {
			newBill = createBill(closeDate, customer, deliveryList,
					salesSlipList, salesLineList, depositLineList, null, true);
		}

		
		if (insert(newBill) == 0) {
			throw new ServiceException("errors.system");
		}
		
		newBill = findBillById(newBill.billId);
		if (newBill == null) {
			
			throw new ServiceException("errors.system");
		}

		
		customerService.updateLastCutoffDate(customerCode, closeDate);

		
		salesService.closeSalesSlipBill(salesSlipList, newBill.billId,
				closeDate, newBill.cutoffPdate);

		
		depositSlipService.closeDepositSlipBill(depositSlipList,
				newBill.billId, closeDate, newBill.cutoffPdate);

	}

	/**
	 * 請求締解除を行います.
	 * @param lastCutOffDate 最終締日
	 * @param customerCode 顧客コード
	 * @return 処理メッセージ
	 * @throws ServiceException
	 * @throws UnabledLockException
	 * @throws ParseException
	 * @throws SQLException
	 * @throws GeneralSecurityException
	 */
	public ActionMessage reOpenBillArt(String lastCutOffDate,
			String customerCode) throws ServiceException, UnabledLockException,
			ParseException, SQLException, GeneralSecurityException {

		ActionMessage msg = null;
		
		try {
			DF_YMD.parse(lastCutOffDate);
		} catch (Exception e) {
			
			
			return msg;
		}

		
		List<Bill> billList = findLastBillByCustomerCode(customerCode);
		if (billList.size() != 0) {
			Bill bill = billList.get(0);

			Date lcd = DF_YMD.parse(lastCutOffDate);
			if (bill.billCutoffDate != null && !lcd.equals(bill.billCutoffDate)) {
				Customer customer = customerService
						.findCustomerByCode(customerCode);

				String strLabel = MessageResourcesUtil
						.getMessage("labels.closeBill.notCredit");
				msg = new ActionMessage("errors.closeBill.existOther",
						customer.customerCode, customer.customerName, strLabel,
						lastCutOffDate);
				return msg;
			}
		}

		
		moveBill(customerCode, lastCutOffDate);

		
		List<SalesSlipTrn> salesSlipList = salesService
				.findCloseSalesSlipByCustomerCode(customerCode, lastCutOffDate,
						CategoryTrns.SALES_CM_CREDIT);

		
		List<DepositSlipDto> depositSlipList = depositSlipService
				.findCloseDepositSlipByCustomerCode(customerCode,
						lastCutOffDate, CategoryTrns.DEPOSIT_CATEGORY_TRANSFER);

		
		billList = findLastBillByCustomerCode(customerCode);

		
		if (billList.size() == 0) {
			
			customerService.updateLastCutoffDate(customerCode, null);
		} else {
			SimpleDateFormat DF_TIME = new SimpleDateFormat(
					Constants.FORMAT.TIMESTAMP);
			customerService.updateLastCutoffDate(customerCode, DF_TIME
					.format(billList.get(0).billCutoffDate));
		}
		
		salesService.reOpenSalesSlipBill(salesSlipList);

		
		depositSlipService.reOpenDepositSlipBill(depositSlipList);

		return msg;
	}

	/**
	 * 月次請求締の請求書を登録します.
	 * @param closeDate 請求締日
	 * @param customer 顧客コード
	 * @param deliveryList 顧客関連情報{@link DeliveryAndPre}のリスト
	 * @param salesSlipList 参照元売上伝票のリスト
	 * @param salesLineList 参照元売上伝票明細行のリスト
	 * @param depositLineList 参照元入金伝票明細行のリスト
	 * @param lastArtPrice 参照元直近の売掛残高
	 * @param isSeqMake 採番するか否か
	 * @return 請求書
	 * @throws Exception
	 */
	public Bill createBill(String closeDate, Customer customer,
			List<DeliveryAndPre> deliveryList,
			List<SalesSlipTrn> salesSlipList, List<SalesLineTrn> salesLineList,
			List<DepositLine> depositLineList, BigDecimal lastArtPrice,
			Boolean isSeqMake) throws Exception {

		Bill bill = new Bill();

		
		if (isSeqMake) {
			bill.billId = getNextVal().intValue();
		}
		
		bill.status = Bill.STATUS_INIT;
		
		YmDto ymDto = ymService.getYm(closeDate);
		
		if (ymDto != null) {
			bill.billYear = Short.valueOf(ymDto.annual.toString());
			bill.billMonth = Short.valueOf(ymDto.monthly.toString());
			bill.billYm = Integer.valueOf(ymDto.ym.toString());
		}
		
		bill.billCutoffDate = super.convertUtilDateToSqlDate(DF_YMD
				.parse(closeDate));
		
		bill.cutoffGroup = customer.cutoffGroup;
		
		bill.paybackCycleCategory = customer.paybackCycleCategory;
		
		Timestamp ts = new Timestamp(GregorianCalendar.getInstance()
				.getTimeInMillis());
		bill.cutoffPdate = ts;
		
		
		bill.baCode = deliveryList.get(0).deliveryCode;
		
		bill.customerCode = customer.customerCode;
		
		if (lastArtPrice == null) {
			
			bill.lastBillPrice = new BigDecimal(0);
		} else {
			bill.lastBillPrice = lastArtPrice;
		}
		
		bill.depositPrice = billAndArtService.getDepositPrice(depositLineList);
		
		
		bill.covPrice = bill.lastBillPrice.subtract(bill.depositPrice);
		
		bill.salesPrice = billAndArtService.getSalesPrice(salesLineList);
		
		
		
		bill.etcPrice = billAndArtService.getEtcPrice(salesLineList);

		
		if (CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX
				.equals(customer.taxShiftCategory)) {
			
			
			bill.ctaxPrice = null;
			
			bill.thisBillPrice = bill.covPrice.add(bill.salesPrice);
		} else {
			
			
			bill.ctaxPrice = billAndArtService.getCTaxPrice(customer,
					salesLineList);
			
			bill.thisBillPrice = bill.covPrice.add(bill.salesPrice
					.add(bill.ctaxPrice));
		}

		
		bill.slipNum = Short.parseShort(Integer.toString(salesSlipList.size()));
		
		
		
		bill.userId = this.userDto.userId;
		
		bill.userName = this.userDto.nameKnj;
		
		bill.paybackPlanDate = super.convertUtilDateToSqlDate(billAndArtService
				.getPayBackPlanDate(customer, closeDate));
		
		
		
		
		if ((bill.thisBillPrice.compareTo(new BigDecimal(0)) == 0)
				|| CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP
						.equals(customer.billPrintUnit)) {
			bill.billPrintCount = 1;
		} else {
			bill.billPrintCount = 0;
		}
		
		bill.billCrtCategory = CategoryTrns.BILL_CRT_BILL;
		
		bill.lastSalesDate = super.convertUtilDateToSqlDate(salesService
				.findLastSalesDate(customer.customerCode));

		return bill;
	}

	/**
	 * 月次請求締の請求書を削除します.
	 * @param customerCode 顧客コード
	 * @param lastCutOffDate 前回請求締日
	 * @throws ServiceException
	 */
	protected void deleteBill(String customerCode, String lastCutOffDate)
			throws ServiceException {

		Bill bill = findBillByCustomerCodeAndCutOffDate(customerCode,
				lastCutOffDate);
		if (delete(bill) == 0) {
			throw new ServiceException("errors.system");
		}
	}

	/**
	 * 月次請求締の請求書を退避テーブルに移動します.<br>
	 * 移動前に、請求書をPDFに出力します.
	 * @param customerCode 顧客コード
	 * @param lastCutOffDate 前回請求締日
	 * @throws ServiceException
	 */
	protected void moveBill(String customerCode, String lastCutOffDate)
			throws ServiceException {

		try {
			Bill bill = findBillByCustomerCodeAndCutOffDate(customerCode,
					lastCutOffDate);

			
			
			if (bill.lastPrintDate != null) {
				billReportService.useLastDate = true;

				
				billReportService.pdf(bill);

				
				if (billOldService.insert(bill) != 1) {
					throw new ServiceException("errors.system");
				}
			}
			
			if (delete(bill) == 0) {
				throw new ServiceException("errors.system");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("errors.system");
		}

	}

	/**
	 * 売上伝票入力時の請求書を登録します.
	 * @param dto 売上伝票情報
	 * @throws ServiceException
	 */
	public void insertBillSales(SalesSlipDto dto) throws ServiceException {

		Bill bill = initialize(dto);

		
		if (insert(bill) == 0) {
			throw new ServiceException("errors.system");
		}
	}

	/**
	 * 売上伝票入力時の請求書を更新します.
	 * @param dto 売上伝票情報
	 * @throws ServiceException
	 */
	public void updateBillSales(SalesSlipDto dto) throws ServiceException {

		Bill bill = initialize(dto);

		
		if (update(bill) == 0) {
			throw new ServiceException("errors.system");
		}
	}

	/**
	 * 売上伝票入力時の請求書を削除します.
	 * @param dto 売上伝票情報
	 * @throws ServiceException
	 */
	public void deleteBillSales(SalesSlipDto dto) throws ServiceException {

		Bill bill = new Bill();
		bill.billId = Integer.parseInt(dto.salesBillId);

		if (delete(bill) == 0) {
			throw new ServiceException("errors.system");
		}
	}

	/**
	 * 売上伝票入力内容で請求書を初期化します.
	 * @param dto 売上伝票情報
	 * @return 請求書情報
	 * @throws ServiceException
	 */
	public Bill initialize(SalesSlipDto dto) throws ServiceException {

		Bill bill = new Bill();

		
		bill.billId = Integer.parseInt(dto.salesBillId);

		
		bill.status = Bill.STATUS_INIT;

		
		YmDto ymDto = ymService.getYm(dto.salesDate);
		if (ymDto != null) {
			bill.billYear = Short.parseShort(ymDto.annual.toString());
			bill.billMonth = Short.parseShort(ymDto.monthly.toString());
			bill.billYm = ymDto.ym;
		}
		
		bill.billCutoffDate = null;

		
		bill.cutoffGroup = this.categoryService
				.cutoffGroupCategoryToCutoffGroup(dto.cutoffGroupCategory);
		bill.paybackCycleCategory = this.categoryService
				.cutoffGroupCategoryToPaybackCycleCategory(dto.cutoffGroupCategory);
		

		
		bill.cutoffPdate = null;

		
		bill.remarks = null;

		
		bill.baCode = dto.baCode;

		
		bill.customerCode = dto.customerCode;

		
		bill.lastBillPrice = null;

		
		bill.depositPrice = null;

		
		bill.adjPrice = null;

		
		bill.covPrice = null;

		
		NumberConverter convPrice = createUnitPriceConverter(dto.priceFractCategory);
		NumberConverter convTax = createTaxPriceConverter(dto.taxFractCategory);
		Number numPrice = (Number) convPrice.getAsObject(dto.priceTotal);
		if (numPrice == null) {
			bill.salesPrice = new BigDecimal(0);
		} else {
			bill.salesPrice = new BigDecimal(numPrice.toString());
		}

		if (CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX
				.equals(dto.taxShiftCategory)) {
			
			
			bill.ctaxPrice = null;
			
			bill.thisBillPrice = bill.salesPrice;
		} else {
			
			
			if (StringUtil.hasLength(dto.ctaxPriceTotal)) {
				Number numTax = (Number) convTax
						.getAsObject(dto.ctaxPriceTotal);
				if (numTax != null) {
					bill.ctaxPrice = new BigDecimal(numTax.toString());
				} else {
					bill.ctaxPrice = new BigDecimal(0);
				}
			} else {
				bill.ctaxPrice = new BigDecimal(0);
			}
			
			bill.thisBillPrice = bill.salesPrice.add(bill.ctaxPrice);
		}

		
		bill.rguPrice = null;

		
		bill.dctPrice = null;

		
		bill.etcPrice = null;

		
		bill.slipNum = 1;

		
		bill.codLastBillPrice = null;
		bill.codDepositPrice = null;
		bill.codAdjPrice = null;
		bill.codCovPrice = null;
		bill.codSalesPrice = null;
		bill.codCtaxPrice = null;
		bill.codRguPrice = null;
		bill.codDctPrice = null;
		bill.codEtcPrice = null;
		bill.codThisBillPrice = null;
		bill.codSlipNum = null;

		
		bill.userId = dto.userId;
		bill.userName = dto.userName;

		
		bill.paybackPlanDate = null;

		
		bill.lastPrintDate = null;

		
		bill.billPrintCount = 0;

		
		bill.billCrtCategory = CategoryTrns.BILL_CRT_SALES;

		Customer customer = customerService
				.findCustomerByCode(dto.customerCode);
		if (customer == null) {
			String strLabel = MessageResourcesUtil
					.getMessage("labels.customerCode");
			throw new ServiceException(strLabel);
		}

		
		try {
			bill.lastSalesDate = super.convertUtilDateToSqlDate(DF_YMD
					.parse(dto.salesDate));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new ServiceException("errors.system");
		}
		return bill;
	}

	/**
	 * 請求書IDを採番して設定します.
	 * @param dto 売上伝票情報
	 * @throws ServiceException
	 */
	public void setBillId(SalesSlipDto dto) throws ServiceException {
		try {
			dto.salesBillId = getNextVal().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("errors.system");
		}
	}

	/**
	 * 指定した年月に発行された月締め請求書のリストを返します.
	 * @param annual 年度
	 * @param monthly 月
	 * @return 月締め請求書のリスト
	 * @throws ServiceException
	 */
	public List<Bill> findMonthlyBill(Integer annual, Integer monthly)
			throws ServiceException {
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		
		
		conditions.put(Param.BILL_YEAR, annual);
		conditions.put(Param.BILL_MONTH, monthly);
		conditions.put(Param.SORT_COLUMN_CUSTOMER_CODE,
				SORT_COLUMN_CUSTOMER_CODE);
		conditions.put(Param.SORT_ORDER, Constants.SQL.DESC);

		return findByCondition(conditions, params, "bill/FindBillYM.sql");
	}

	/**
	 * 請求書IDを指定して、請求書情報を返します.
	 * @param billId 請求書ID
	 * @return 請求書情報
	 * @throws ServiceException
	 */
	public Bill findBillById(Integer billId) throws ServiceException {
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		
		
		conditions.put(Param.BILL_ID, billId);

		List<Bill> billList = findByCondition(conditions, params,
				"bill/FindLastBillByCustomer.sql");
		if (billList.size() != 1) {
			return null;
		}
		return billList.get(0);

	}

	/**
	 * 請求書IDを指定して、請求書情報のマップを返します.
	 * @param billId 請求書情報
	 * @return 請求書情報のマップ
	 * @throws ServiceException
	 */
	public BeanMap findBillByIdSimple(String billId) throws ServiceException {

		
		
		Bill bill = findBillById(Integer.valueOf(billId));
		return Beans.createAndCopy(BeanMap.class, bill).execute();
	}

	/**
	 * 顧客コードおよび請求締日を指定して、月締め請求書を取得します.
	 * @param customerCode 顧客コード
	 * @param cutOffDate　請求締日の文字列
	 * @return 請求書情報
	 * @throws ServiceException
	 */
	public Bill findBillByCustomerCodeAndCutOffDate(String customerCode,
			String cutOffDate) throws ServiceException {
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		
		
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.BILL_CUTOFF_DATE, cutOffDate);
		conditions.put(Param.BILL_CRT_CATEGORY, CategoryTrns.BILL_CRT_BILL);

		List<Bill> billList = findByCondition(conditions, params,
				"bill/FindLastBillByCustomerAndCutoff.sql");
		if (billList.size() != 1) {
			return null;
		}
		return billList.get(0);

	}

	/**
	 * 請求書の帳票出力カウンタおよび日付を更新します.
	 * @param billId　請求書番号
	 * @return　帳票出力カウンタ
	 * @throws ServiceException
	 */
	public int updatePrintCount(String billId) throws ServiceException {
		int SuccessedLineCount = 0;

		
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.BILL_ID, billId);

		
		SuccessedLineCount = this.updateBySqlFile(
				"bill/UpdateBillPrintCnt.sql", param).execute();

		return SuccessedLineCount;
	}

}
