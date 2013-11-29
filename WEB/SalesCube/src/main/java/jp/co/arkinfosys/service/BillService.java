/*
 * Copyright 2009-2010 Ark Information Systems.
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

	// 売上伝票用サービス
	@Resource
	protected SalesService salesService;

	// 売上伝票明細行用サービス
	@Resource
	protected SalesLineService salesLineService;

	// 入金伝票用サービス
	@Resource
	protected DepositSlipService depositSlipService;

	// 入金伝票明細行用サービス
	@Resource
	protected DepositLineService depositLineService;

	// 顧客マスタ用サービス
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

	//発番用サービス
	public SeqMakerService seqMakerService;

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		// ソート方向
		private static final String SORT_ORDER = "sortOrder";
		// 取得件数
		private static final String ROW_COUNT = "rowCount";
		// 取得件数
		private static final String OFFSET_ROW = "offsetRow";

		// 顧客コード
		public static final String CUSTOMER_CODE = "customerCode";
		// 顧客名
		public static final String CUSTOMER_NAME = "customerName";
		// 請求締日
		public static final String BILL_CUTOFF_DATE = "billCutoffDate";
		// 請求書作成区分
		public static final String BILL_CRT_CATEGORY = "billCrtCategory";
		// 締日グループ
		public static final String CUTOFF_GROUP = "cutoffGroup";
		// 回収間隔
		public static final String PAYBACK_CYCLE_CATEGORY = "paybackCycleCategory";
		// 取引区分
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
		// 請求締日のソート条件
		private static final String SORT_COLUMN_CUTOFF_DATE = "sortColumnBillCutoffDate";
		// 顧客コードのソート条件
		private static final String SORT_COLUMN_CUSTOMER_CODE = "sortColumnCustomerCode";
		// 年度
		private static final String BILL_YEAR = "billYear";
		// 月
		private static final String BILL_MONTH = "billMonth";
		// 請求書番号
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
		//MAPの生成
		Map<String, Object> param = createParamMap(bill);

		//SQLクエリを投げる
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
		//MAPの生成
		Map<String, Object> param = createParamMap(bill);

		//SQLクエリを投げる
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
		//MAPの生成
		Map<String, Object> param = createParamMap(bill);

		super.updateAudit(Bill.TABLE_NAME, new String[] { Param.BILL_ID }, new Object[] { bill.billId });

		//SQLクエリを投げる
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

		//MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		//基礎となるカラム名(空で)をエンティティからPUT
		//		BeanMap FoundationParam = Beans.createAndCopy(BeanMap.class,this.depositLine).execute();
		//		param.putAll(FoundationParam);

		//アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, bill).execute();
		param.putAll(AFparam);

		//更新日時とかPUT
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
		//伝票番号の発番
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

		// 条件設定
		// 顧客コードが一致
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

		// 条件設定
		// 顧客コードが一致
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
		// 顧客マスタ、納入先取得
		Customer customer = customerService.findCustomerByCode(customerCode);
		List<DeliveryAndPre> deliveryList = deliveryService
				.searchDeliveryByCompleteCustomerCode(customerCode);

		// 売上伝票取得
		List<SalesSlipTrn> salesSlipList = salesService
				.findOpenSalesSlipByCustomerCode(customerCode, closeDate,
						CategoryTrns.SALES_CM_CREDIT);

		// 売上明細行取得
		List<SalesLineTrn> salesLineList = salesLineService
				.findOpenSalesLineByCustomerCode(customerCode, closeDate,
						CategoryTrns.SALES_CM_CREDIT);

		// 入金伝票取得
		List<DepositSlipDto> depositSlipList = depositSlipService
				.findOpenDepositSlipByCustomerCode(customerCode, closeDate,
						CategoryTrns.DEPOSIT_CATEGORY_TRANSFER);

		// 入金伝票明細行取得
		List<DepositLine> depositLineList = depositLineService
				.findOpenDepositLineByCustomerCode(customerCode, closeDate,
						CategoryTrns.DEPOSIT_CATEGORY_TRANSFER);

		// 前回請求額取得
		List<Bill> billList = findLastBillByCustomerCode(customerCode);

		// 請求情報作成
		Bill newBill;
		if (billList.size() > 0) {
			newBill = createBill(closeDate, customer, deliveryList,
					salesSlipList, salesLineList, depositLineList, billList
							.get(0).thisBillPrice, true);
		} else {
			newBill = createBill(closeDate, customer, deliveryList,
					salesSlipList, salesLineList, depositLineList, null, true);
		}

		// DBへ登録
		if (insert(newBill) == 0) {
			throw new ServiceException("errors.system");
		}
		// 更新後のデータを取得して返す
		newBill = findBillById(newBill.billId);
		if (newBill == null) {
			// 直前で登録しているのでありえないが一応チェック
			throw new ServiceException("errors.system");
		}

		// 顧客の最終締め処理日=ダイアログの締実行日を設定
		customerService.updateLastCutoffDate(customerCode, closeDate);

		// 売上伝票と紐付く明細行を締める
		salesService.closeSalesSlipBill(salesSlipList, newBill.billId,
				closeDate, newBill.cutoffPdate);

		// 入金伝票と紐付く明細行を締める
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
		// 前回締日の特定
		try {
			DF_YMD.parse(lastCutOffDate);
		} catch (Exception e) {
			// 内容がコメントで日付ではない場合には解除すべきデータが存在しないので、
			// 何もしない
			return msg;
		}

		// 最も直近の請求書の請求締日　！＝　最終締め日　の場合には、画面に表示されている最終締日は売掛以外で締められているのでエラーとする
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

		// 請求情報移動
		moveBill(customerCode, lastCutOffDate);

		// 指定日付で締っている売上伝票取得
		List<SalesSlipTrn> salesSlipList = salesService
				.findCloseSalesSlipByCustomerCode(customerCode, lastCutOffDate,
						CategoryTrns.SALES_CM_CREDIT);

		// 指定日付で締っている入金伝票取得
		List<DepositSlipDto> depositSlipList = depositSlipService
				.findCloseDepositSlipByCustomerCode(customerCode,
						lastCutOffDate, CategoryTrns.DEPOSIT_CATEGORY_TRANSFER);

		// 顧客に対する最新の請求書情報を再取得する
		billList = findLastBillByCustomerCode(customerCode);

		// 顧客の最終締め処理日を再設定
		if (billList.size() == 0) {
			// 請求書が存在していない場合はnullを設定する
			customerService.updateLastCutoffDate(customerCode, null);
		} else {
			SimpleDateFormat DF_TIME = new SimpleDateFormat(
					Constants.FORMAT.TIMESTAMP);
			customerService.updateLastCutoffDate(customerCode, DF_TIME
					.format(billList.get(0).billCutoffDate));
		}
		// 売上伝票と紐付く明細行を締解除する
		salesService.reOpenSalesSlipBill(salesSlipList);

		// 入金伝票と紐付く明細行を締解除する
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

		// 請求書番号
		if (isSeqMake) {
			bill.billId = getNextVal().intValue();
		}
		// 状態フラグ
		bill.status = Bill.STATUS_INIT;
		// 請求年度、月度、年月度を計算
		YmDto ymDto = ymService.getYm(closeDate);
		// 請求年
		if (ymDto != null) {
			bill.billYear = Short.valueOf(ymDto.annual.toString());
			bill.billMonth = Short.valueOf(ymDto.monthly.toString());
			bill.billYm = Integer.valueOf(ymDto.ym.toString());
		}
		// 請求締日
		bill.billCutoffDate = super.convertUtilDateToSqlDate(DF_YMD
				.parse(closeDate));
		// 締日グループ
		bill.cutoffGroup = customer.cutoffGroup;
		// 回収間隔
		bill.paybackCycleCategory = customer.paybackCycleCategory;
		// 請求締処理日
		Timestamp ts = new Timestamp(GregorianCalendar.getInstance()
				.getTimeInMillis());
		bill.cutoffPdate = ts;
		// 備考は未設定
		// 請求先コード
		bill.baCode = deliveryList.get(0).deliveryCode;
		// 得意先コード
		bill.customerCode = customer.customerCode;
		// 前回請求金額
		if (lastArtPrice == null) {
			// データが存在しない場合には請求額は０
			bill.lastBillPrice = new BigDecimal(0);
		} else {
			bill.lastBillPrice = lastArtPrice;
		}
		// 入金額
		bill.depositPrice = billAndArtService.getDepositPrice(depositLineList);
		// 調整金額は未設定
		// 繰越金額
		bill.covPrice = bill.lastBillPrice.subtract(bill.depositPrice);
		// 売上金額
		bill.salesPrice = billAndArtService.getSalesPrice(salesLineList);
		// 返品金額は未設定
		// 値引き金額は未設定
		// その他金額
		bill.etcPrice = billAndArtService.getEtcPrice(salesLineList);

		// 消費税額
		if (CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX
				.equals(customer.taxShiftCategory)) {
			// 区分名：税転嫁、区分コード名：内税
			// 消費税額
			bill.ctaxPrice = null;
			// 今回請求額 = 繰越金額＋売上金額
			bill.thisBillPrice = bill.covPrice.add(bill.salesPrice);
		} else {
			// 区分名：税転嫁、区分コード名：内税以外
			// 消費税額
			bill.ctaxPrice = billAndArtService.getCTaxPrice(customer,
					salesLineList);
			// 今回請求額 = 繰越金額＋売上金額＋消費税
			bill.thisBillPrice = bill.covPrice.add(bill.salesPrice
					.add(bill.ctaxPrice));
		}

		// 伝票枚数
		bill.slipNum = Short.parseShort(Integer.toString(salesSlipList.size()));
		// 代引前回請求金額、代引入金額、代引調整金額、代引繰越金額、代引売上金額、代引消費税額
		// 代引返品金額、代引値引金額、代引その他金額、代引今回請求金額、代引伝票枚数
		// 担当書コード
		bill.userId = this.userDto.userId;
		// 担当者名
		bill.userName = this.userDto.nameKnj;
		// 回収予定日
		bill.paybackPlanDate = super.convertUtilDateToSqlDate(billAndArtService
				.getPayBackPlanDate(customer, closeDate));
		// 最終請求書発行日は未設定
		// 請求書発行フラグ
		// 請求書発行単位が売上伝票単位（都度請求）か
		// 今回請求金額が「￥0」の場合も発行済とする
		if ((bill.thisBillPrice.compareTo(new BigDecimal(0)) == 0)
				|| CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP
						.equals(customer.billPrintUnit)) {
			bill.billPrintCount = 1;
		} else {
			bill.billPrintCount = 0;
		}
		// 請求書作成区分
		bill.billCrtCategory = CategoryTrns.BILL_CRT_BILL;
		// 最終売上日
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

			// 出力済の請求書のみ保存を行う
			// 保存するのは出力済の請求書のみ。出力カウンタは自動で１にする場合があるので、出力日付で判断
			if (bill.lastPrintDate != null) {
				billReportService.useLastDate = true;

				// PDFファイルの出力
				billReportService.pdf(bill);

				// 退避レコードの登録
				if (billOldService.insert(bill) != 1) {
					throw new ServiceException("errors.system");
				}
			}
			// 請求書レコードの削除
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

		// DBへ登録
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

		// DBへ登録
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

		// 請求書番号
		bill.billId = Integer.parseInt(dto.salesBillId);

		// 状態フラグ
		bill.status = Bill.STATUS_INIT;

		// 請求年月日
		YmDto ymDto = ymService.getYm(dto.salesDate);
		if (ymDto != null) {
			bill.billYear = Short.parseShort(ymDto.annual.toString());
			bill.billMonth = Short.parseShort(ymDto.monthly.toString());
			bill.billYm = ymDto.ym;
		}
		// 請求締日
		bill.billCutoffDate = null;

		// 締日グループ
		bill.cutoffGroup = this.categoryService
				.cutoffGroupCategoryToCutoffGroup(dto.cutoffGroupCategory);
		bill.paybackCycleCategory = this.categoryService
				.cutoffGroupCategoryToPaybackCycleCategory(dto.cutoffGroupCategory);
		//bill.cutoffGroup = null;

		// 請求締処理日
		bill.cutoffPdate = null;

		// 備考
		bill.remarks = null;

		// 請求先コード
		bill.baCode = dto.baCode;

		// 得意先コード
		bill.customerCode = dto.customerCode;

		// 前回請求金額
		bill.lastBillPrice = null;

		// 入金額
		bill.depositPrice = null;

		// 調整金額
		bill.adjPrice = null;

		// 繰越金額
		bill.covPrice = null;

		// 売上金額
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
			// 区分名：税転嫁、区分コード名：内税
			// 消費税額
			bill.ctaxPrice = null;
			// 今回請求額
			bill.thisBillPrice = bill.salesPrice;
		} else {
			// 区分名：税転嫁、区分コード名：内税以外
			// 消費税額
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
			// 今回請求額
			bill.thisBillPrice = bill.salesPrice.add(bill.ctaxPrice);
		}

		// 返品額
		bill.rguPrice = null;

		// 値引き金額
		bill.dctPrice = null;

		// その他金額
		bill.etcPrice = null;

		// 伝票枚数
		bill.slipNum = 1;

		// 代引き系カラム
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

		// 担当者コード
		bill.userId = dto.userId;
		bill.userName = dto.userName;

		// 回収予定日
		bill.paybackPlanDate = null;

		// 最終請求書発行日
		bill.lastPrintDate = null;

		// 請求書発行フラグ
		bill.billPrintCount = 0;

		// 請求書作成区分
		bill.billCrtCategory = CategoryTrns.BILL_CRT_SALES;

		Customer customer = customerService
				.findCustomerByCode(dto.customerCode);
		if (customer == null) {
			String strLabel = MessageResourcesUtil
					.getMessage("labels.customerCode");
			throw new ServiceException(strLabel);
		}

		// 最終売上日
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

		// 条件設定
		// 年度、月が一致
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

		// 条件設定
		// 年度、月が一致
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

		// 条件設定
		// 請求書番号が一致
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

		// 条件設定
		// 年度、月が一致
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

		//MAPの生成
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.BILL_ID, billId);

		//SQLクエリを投げる
		SuccessedLineCount = this.updateBySqlFile(
				"bill/UpdateBillPrintCnt.sql", param).execute();

		return SuccessedLineCount;
	}

}
