/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.deposit.DepositSlipDto;
import jp.co.arkinfosys.entity.ArtBalance;
import jp.co.arkinfosys.entity.CloseCustomer;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.DepositLine;
import jp.co.arkinfosys.entity.SalesLineTrn;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 *
 * 売掛処理サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ArtBalanceService extends AbstractService<ArtBalance> {

	@Resource
	CustomerService customerService;

	@Resource
	DeliveryService deliveryService;

	@Resource
	SalesService salesService;

	@Resource
	SalesLineService salesLineService;

	@Resource
	DepositSlipService depositSlipService;

	@Resource
	DepositLineService depositLineService;

	@Resource
	private CloseCustomerService closeCustomerService;

	
	public SeqMakerService seqMakerService;

	@Resource
	private YmService ymService;

	@Resource
	private BillAndArtService	billAndArtService;

	/**
	 * SQLファイルのパラメータ名定義
	 */
	public static class Param {
		private static final String SORT_ORDER = "sortOrder"; 
		private static final String ART_BALANCE_ID = "artBalanceId"; 
		private static final String ART_ANNUAL = "artAnnual"; 
		private static final String ART_MONTHLY = "artMonthly"; 
		private static final String ART_CUTOFF_DATE = "artCutoffDate"; 
		private static final String CUSTOMER_CODE = "customerCode"; 
		private static final String SORT_COLUMN_CUTOFF_DATE = "sortColumnCutoffDate"; 
		private static final String ROW_COUNT = "rowCount"; 
		private static final String OFFSET_ROW = "offsetRow"; 
		public static final String ART_CUTOFF_DATE_TO = "artCutoffDateTo";

	}

	public String[] params = { Param.SORT_ORDER, Param.ART_BALANCE_ID,
			Param.ART_ANNUAL, Param.ART_MONTHLY, Param.ART_CUTOFF_DATE,
			Param.CUSTOMER_CODE, Param.SORT_COLUMN_CUTOFF_DATE,
			Param.ROW_COUNT, Param.OFFSET_ROW, Param.ART_CUTOFF_DATE_TO };

	/**
	 * 納入先コードのカラム名
	 */
	private static final String COLUMN_ART_CUTOFF_DATE = "ART_CUTOFF_DATE";

	private SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);

	/**
	 * 売掛残高テーブルの登録用売掛残高番号を返します.
	 * @return 売掛残高番号
	 * @throws Exception
	 */
	public Long getNextVal() throws Exception {

		Long newSlipId = -1L;
		
		try {
			newSlipId = seqMakerService.nextval(ArtBalance.TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return newSlipId;
	}

	/**
	 * エンティティ情報をDBに登録します.
	 *
	 * @param ab 売掛残高エンティティ
	 *
	 * @return　実行行数
	 */
	public int insert(ArtBalance ab) {
		
		return this.updateBySqlFile(
				"artbalance/InsertArtBalance.sql", createParamMap(ab)).execute();
	}

	/**
	 * エンティティ情報でDBを更新します.
	 *
	 * @param ab 売掛残高エンティティ
	 *
	 * @return　実行行数
	 */
	public int update(ArtBalance ab) {
		
		return this.updateBySqlFile(
				"artbalance/UpdateArtBalance.sql", createParamMap(ab)).execute();
	}

	/**
	 * エンティティ情報でDBから削除します.
	 *
	 * @param ab 売掛残高エンティティ
	 *
	 * @return　実行行数
	 */
	public int delete(ArtBalance ab) {
		
		return this.updateBySqlFile(
				"artbalance/DeleteArtBalance.sql", createParamMap(ab)).execute();
	}

	/**
	 * 売掛残高エンティティ情報から登録用Mapオブジェクトを生成します.
	 *
	 * @param ab 売掛残高エンティティ
	 * @return　登録用マップ
	 */
	private Map<String, Object> createParamMap(ArtBalance ab) {

		
		Map<String, Object> param = new HashMap<String, Object>();

		
		
		
		

		
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, ab).execute();
		param.putAll(AFparam);

		
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return param;
	}

	/**
	 *　顧客コードを指定して直近の売掛残高を１件取得します.
	 *
	 * @param customerCode 顧客コード
	 * @return 売掛残高エンティティリスト
	 * @throws ServiceException
	 */
	public List<ArtBalance> findLastArtBalanceByCustomerCode(String customerCode)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		
		
		
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SORT_COLUMN_CUTOFF_DATE, COLUMN_ART_CUTOFF_DATE);
		conditions.put(Param.SORT_ORDER, Constants.SQL.DESC);
		conditions.put(Param.ROW_COUNT, "1");
		conditions.put(Param.OFFSET_ROW, "0");

		return findByCondition(conditions, params,
				"artbalance/FindLastArtBalanceByCustomer.sql");

	}

	/**
	 * 顧客コードと請求締日を指定して月締め請求書を取得します.<BR>
	 * 検索結果が１件では無かった場合には、nullを返します.
	 *
	 * @param customerCode 顧客コード
	 * @param cutOffDate 請求締日
	 * @return 検索結果請求書エンティティ
	 * @throws ServiceException
	 */
	public ArtBalance findArtBalanceByCustomerCodeAndCutOffDate(
			String customerCode, String cutOffDate) throws ServiceException {
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		
		
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.ART_CUTOFF_DATE, cutOffDate);

		List<ArtBalance> artList = findByCondition(conditions, params,
				"artbalance/FindLastArtBalanceByCustomer.sql");
		if (artList.size() != 1) {
			return null;
		}
		return artList.get(0);
	}

	/**
	 * 顧客コードと請求締日を指定して月締め請求書を取得します.
	 *
	 * @param customerCode
	 *            　顧客コード
	 * @param cutOffDate
	 *            　請求締日
	 * @return 検索結果請求書エンティティ　存在しない場合にはnull
	 * @throws ServiceException
	 */
	/**
	 * 顧客コードと請求締日を指定して月締め請求書を取得します.
	 * @param artBalanceId 売掛残高番号
	 * @return 売掛残高エンティティ
	 * @throws ServiceException
	 */
	public ArtBalance findArtBalanceById(Integer artBalanceId)
			throws ServiceException {
		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		
		
		conditions.put(Param.ART_BALANCE_ID, artBalanceId);

		List<ArtBalance> artList = findByCondition(conditions, params,
				"artbalance/FindLastArtBalanceByCustomer.sql");
		if (artList.size() != 1) {
			return null;
		}
		return artList.get(0);
	}

	/**
	 *　顧客コードを指定して直近の売掛残高を１件取得します.
	 * @param customerCode　顧客コード
	 * @param targetDate　指定日
	 * @return　売掛残高エンティティリスト
	 * @throws ServiceException
	 */
	public List<ArtBalance> findLastArtBalanceByCustomerCodeBeforeTargetDate(
			String customerCode, String targetDate) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		
		
		
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.SORT_COLUMN_CUTOFF_DATE, COLUMN_ART_CUTOFF_DATE);
		conditions.put(Param.SORT_ORDER, Constants.SQL.DESC);
		conditions.put(Param.ROW_COUNT, "1");
		conditions.put(Param.OFFSET_ROW, "0");
		conditions.put(Param.ART_CUTOFF_DATE_TO, targetDate);

		return findByCondition(conditions, params,
				"artbalance/FindLastArtBalanceByCustomer.sql");
	}

	/**
	 * 全ユーザの売掛締処理を行います.
	 * @param closeDate　締日
	 * @throws Exception
	 */
	public ActionMessages closeAll(String closeDate)
		throws Exception {
		ActionMessages  msgs = new ActionMessages ();

		
		
		
		
		
		List<CloseCustomer> customerList =
			closeCustomerService.findCloseArtBalanceCustomer(closeDate);
		for( CloseCustomer customer : customerList ){
			close( closeDate, customer.customerCode );
		}
		return msgs;
	}


	/**
	 * 掛売締め対象となったユーザ顧客単位の売掛締処理を行います.<br>
	 * 以下の処理を行います<br>
	 * 1. 売上伝票・明細を締める<br>
	 * 2. 入金伝票・明細を締める<br>
	 * 3. 売掛残高情報を登録する<br>
	 * @param closeDate　締日
	 * @param customerCode　顧客コード
	 * @throws Exception
	 */
	protected void close(String closeDate, String customerCode ) throws Exception {
		
		Customer customer = customerService.findCustomerByCode(customerCode);
		List<DeliveryAndPre> deliveryList =
			deliveryService.searchDeliveryByCompleteCustomerCode( customerCode );

		
		List<SalesSlipTrn> salesSlipList = salesService.findArtOpenSalesSlipByCustomerCode(customerCode, closeDate);

		
		List<SalesLineTrn> salesLineList = salesLineService.findArtOpenSalesLineByCustomerCode(customerCode, closeDate);

		
		List<DepositSlipDto> depositSlipList = depositSlipService.findArtOpenDepositSlipByCustomerCode(customerCode, closeDate);

		
		List<DepositLine> depositLineList = depositLineService.findArtOpenDepositLineByCustomerCode(customerCode, closeDate);

		
		List<ArtBalance> artList = findLastArtBalanceByCustomerCode(customerCode);

		
		ArtBalance newArtBalance = null;
		if(artList.size() > 0) {
			newArtBalance = createArtBalance( closeDate, customer, deliveryList, salesSlipList, salesLineList, depositLineList, artList.get(0).thisArtPrice, true );
		} else {
			newArtBalance = createArtBalance( closeDate, customer, deliveryList, salesSlipList, salesLineList, depositLineList, null, true );
		}

		
		if( insert(newArtBalance) == 0 ){
			throw new ServiceException("errors.system");
		}
		
		newArtBalance = findArtBalanceById( newArtBalance.artBalanceId );
		if( newArtBalance == null ){
			
			throw new ServiceException("errors.system");
		}

		
		customerService.updateLastSalesCutoffDate(customerCode,closeDate);

		
		salesService.closeSalesSlipArt(salesSlipList,newArtBalance.artBalanceId,closeDate,newArtBalance.artCutoffPdate);

		
		depositSlipService.closeDepositSlipArt(depositSlipList,newArtBalance.artBalanceId,closeDate,newArtBalance.artCutoffPdate );

	}

	/**
	 * 月次請求締に対応した売掛残高を作成しDBに登録します.
	 * @param closeDate 締日
	 * @param customer 顧客エンティティ
	 * @param deliveryList 納入先エンティティのリスト
	 * @param salesSlipList 売上伝票エンティティのリスト
	 * @param salesLineList 売上明細エンティティのリスト
	 * @param depositLineList 入金明細エンティティのリスト
	 * @param lastArtPrice 前回請求金額
	 * @param isSeqMake 売掛残高番号を発番するか否か
	 * @return 売掛残高エンティティ
	 * @throws Exception
	 */
	protected ArtBalance createArtBalance( String closeDate, Customer customer,
			List<DeliveryAndPre> deliveryList,
			List<SalesSlipTrn> salesSlipList,List<SalesLineTrn> salesLineList,
			List<DepositLine> depositLineList,
			BigDecimal lastArtPrice,
			Boolean isSeqMake ) throws Exception{

		ArtBalance artBalance = new ArtBalance();

		
		if(isSeqMake) {
			artBalance.artBalanceId = getNextVal().intValue();
		}
		
		YmDto ymDto = ymService.getYm(closeDate);
		
		if( ymDto != null ){
			
			artBalance.artAnnual = Short.valueOf( ymDto.annual.toString() );
			
			artBalance.artMonthly = Short.valueOf( ymDto.monthly.toString() );
			
			artBalance.artYm = Integer.valueOf( ymDto.ym.toString() );
		}
		
		artBalance.artCutoffDate = super.convertUtilDateToSqlDate(DF_YMD.parse(closeDate));;
		
		artBalance.userId = this.userDto.userId;
		
		artBalance.userName = this.userDto.nameKnj;
		
		artBalance.baCode = deliveryList.get(0).deliveryCode;
		
		artBalance.baName = deliveryList.get(0).deliveryName;
		
		artBalance.customerCode = customer.customerCode;
		
		artBalance.customerName = customer.customerName;
		
		artBalance.salesCmCategory = customer.salesCmCategory;
		
		if( lastArtPrice == null ){
			
			artBalance.lastArtPrice = new BigDecimal(0);
		}else{
			artBalance.lastArtPrice = lastArtPrice;
		}
		
		artBalance.depositPrice = billAndArtService.getDepositPrice(depositLineList);;
		
		
		artBalance.covPrice = artBalance.lastArtPrice.subtract( artBalance.depositPrice );
		
		artBalance.salesPrice = billAndArtService.getSalesPrice(salesLineList);
		
		if( CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX.equals(customer.taxShiftCategory)) {
			
			
			artBalance.ctaxPrice = null;
			
			artBalance.thisArtPrice = artBalance.covPrice.add(artBalance.salesPrice);
		}else{
			
			
			artBalance.ctaxPrice = billAndArtService.getCTaxPrice(customer, salesLineList);
			
			artBalance.thisArtPrice = artBalance.covPrice.add(artBalance.salesPrice.add(artBalance.ctaxPrice));
		}
		
		
		artBalance.etcPrice = billAndArtService.getEtcPrice(salesLineList);
		
		
		artBalance.artCutoffGroup = customer.cutoffGroup;
		
		artBalance.paybackCycleCategory = customer.paybackCycleCategory;
		
		artBalance.salesSlipNum = Short.parseShort( Integer.toString(salesSlipList.size()) );
		
		Timestamp ts = new Timestamp(GregorianCalendar.getInstance().getTimeInMillis());
		artBalance.artCutoffPdate = ts;
		
		
		artBalance.familyCategory = CategoryTrns.FAMILY_CATEGORY_PARENTS;
		
		artBalance.deliveryPlaceNum = 1;
		

		return artBalance;
	}

	/**
	 * 売掛以外の売掛締解除処理を行います.
	 * @param lastCutOffDate　前回締日
	 * @return 警告メッセージ
	 * @throws ServiceException
	 * @throws ParseException
	 * @throws UnabledLockException
	 * @throws GeneralSecurityException
	 * @throws SQLException
	 */
	public ActionMessages  reOpenAll(String lastCutOffDate ) throws ServiceException, UnabledLockException, ParseException, SQLException, GeneralSecurityException {
		ActionMessages  msgs = new ActionMessages ();

		
		try {
			DF_YMD.parse(lastCutOffDate);
		} catch (ParseException e) {
			
			
			return msgs;
		}

		
		
		
		
		List<CloseCustomer> customerList = closeCustomerService.findReOpenArtBalanceCustomer(lastCutOffDate);
		for( CloseCustomer customer : customerList ){
			reOpen( lastCutOffDate, customer.customerCode );
		}
		return msgs;
	}
	/**
	 * 売掛以外顧客単位の請求締解除処理を行います.<br>
	 * 以下の処理を行います.<br>
	 * 1. 売上伝票・明細行の締解除.<br>
	 * 2. 入金伝票・明細行の締解除.<br>
	 * 3. 売掛残高情報の削除.<br>
	 * 4. 顧客マスタの最終締処理日の再設定.
	 * @param lastCutOffDate　前回締日
	 * @param customerCode　顧客コード
	 * @throws ServiceException
	 * @throws ParseException
	 * @throws UnabledLockException
	 * @throws GeneralSecurityException
	 * @throws SQLException
	 */
	protected void reOpen(String lastCutOffDate, String customerCode ) throws ServiceException, UnabledLockException, ParseException, SQLException, GeneralSecurityException {

		




		
		List<SalesSlipTrn> salesSlipList = salesService.findArtCloseSalesSlipByCustomerCode(customerCode, lastCutOffDate);

		
		List<DepositSlipDto> depositSlipList = depositSlipService.findArtCloseDepositSlipByCustomerCode(customerCode, lastCutOffDate);

		
		deleteArtBalance( customerCode, lastCutOffDate);

		
		List<ArtBalance> artList = findLastArtBalanceByCustomerCode(customerCode);

		
		if( artList.size() == 0 ){
			
			customerService.updateLastSalesCutoffDate(customerCode,null);
		}else{
			SimpleDateFormat DF_TIME = new SimpleDateFormat(Constants.FORMAT.TIMESTAMP);
			customerService.updateLastSalesCutoffDate(customerCode,DF_TIME.format(artList.get(0).artCutoffDate));
		}
		
		salesService.reOpenSalesSlipArt(salesSlipList);

		
		depositSlipService.reOpenDepositSlipArt(depositSlipList);

	}
	/**
	 * 売掛残高を削除します.
	 * @param customerCode　顧客コード
	 * @param lastCutOffDate　前回請求締日
	 * @throws ServiceException
	 */
	protected void deleteArtBalance(String customerCode, String lastCutOffDate)
			throws ServiceException {
		ArtBalance artBalance = findArtBalanceByCustomerCodeAndCutOffDate(
				customerCode, lastCutOffDate);
		if (artBalance == null) {
			
			return;
		}
		super.updateAudit(ArtBalance.TABLE_NAME, new String[] { super
				.convertVariableNameToColumnName(Param.ART_BALANCE_ID) },
				new Object[] { artBalance.artBalanceId });
		if (delete(artBalance) == 0) {
			throw new ServiceException("errors.system");
		}
	}


	/**
	 * 日指定にて、その時点の売掛残高エンティティを返します.<br>
	 * (指定日当日を含んで集計します)
	 * @param targetDate 指定日
	 * @param customerCode　顧客コード
	 * @return 売掛残高エンティティ
	 * @throws Exception
	 */
	public ArtBalance getArtBalanceByDate(String targetDate, String customerCode ) throws Exception {
		
		Customer customer = customerService.findCustomerByCode(customerCode);
		List<DeliveryAndPre> deliveryList =
			deliveryService.searchDeliveryByCompleteCustomerCode( customerCode );

		
		String monthAgoDate = get1MonthAgoLastDate(targetDate);

		
		BigDecimal artPrice;
		artPrice = getArtPrice(monthAgoDate, customerCode, customer, deliveryList);

		

		String monthAgoTomorrowDate = null;
		monthAgoTomorrowDate = getTomorrowDate(monthAgoDate);

		
		List<SalesSlipTrn> salesSlipList = salesService.findSalesSlipByCustomerCodeBetweenDate(customerCode, monthAgoTomorrowDate, targetDate, null );

		
		List<SalesLineTrn> salesLineList = salesLineService.findSalesLineByCustomerCodeBetweenDate(customerCode, monthAgoTomorrowDate, targetDate, null );

		
		List<DepositLine> depositLineList = depositLineService.findDepositLineByCustomerCodeBetweenDate(customerCode, monthAgoTomorrowDate, targetDate, null);

		
		ArtBalance ret = createArtBalance( targetDate, customer, deliveryList, salesSlipList, salesLineList, depositLineList, artPrice, false );

		
		if(ret.dctPrice == null)
			ret.dctPrice = new BigDecimal("0");
		if(ret.rguPrice == null)
			ret.rguPrice = new BigDecimal("0");
		if(ret.etcPrice == null)
			ret.etcPrice = new BigDecimal("0");
		if(ret.adjPrice == null)
			ret.adjPrice = new BigDecimal("0");
		if(ret.depositInst == null)
			ret.depositInst = new BigDecimal("0");

		return ret;
	}

	/**
	 * 指定された日付時点の売掛残高を返します.
	 * @param targetDate 指定日
	 * @param customerCode 顧客コード
	 * @param customer 顧客エンティティ
	 * @param deliveryList 納入先エンティティリスト
	 * @return 売掛残高
	 * @throws ServiceException
	 * @throws Exception
	 */
	private BigDecimal getArtPrice(String targetDate, String customerCode, Customer customer, List<DeliveryAndPre> deliveryList) throws ServiceException, Exception {

		
		List<ArtBalance> lastArtBalanceList = findLastArtBalanceByCustomerCodeBeforeTargetDate(customerCode, targetDate);

		
		String lastArtCutoffDate;
		if(lastArtBalanceList != null && lastArtBalanceList.size() > 0 ) {
			lastArtCutoffDate = getTodayDate(lastArtBalanceList.get(0).artCutoffDate);
		} else {
			lastArtCutoffDate = "";
		}

		
		List<SalesSlipTrn> salesSlipList = salesService.findSalesSlipByCustomerCodeBetweenDate(customerCode, null, targetDate, lastArtCutoffDate );

		
		List<SalesLineTrn> salesLineList = salesLineService.findSalesLineByCustomerCodeBetweenDate(customerCode, null, targetDate, lastArtCutoffDate );

		
		List<DepositLine> depositLineList = depositLineService.findDepositLineByCustomerCodeBetweenDate(customerCode, null, targetDate, lastArtCutoffDate );

		
		if(lastArtBalanceList.size() > 0) {
			return createArtBalance( targetDate, customer, deliveryList, salesSlipList, salesLineList, depositLineList, lastArtBalanceList.get(0).thisArtPrice, false ).thisArtPrice;
		} else {
			return createArtBalance( targetDate, customer, deliveryList, salesSlipList, salesLineList, depositLineList, null, false ).thisArtPrice;
		}
	}
	/**
	 * 当日文字列を返します(Date⇒String)
	 * @param targetDate 指定日
	 * @return 指定日文字列
	 */
	private String getTodayDate(Date targetDate) {
		Date returnDate = null;	
		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);

		
		Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(targetDate);
		returnDate = cal.getTime();

		return df.format(returnDate);
	}

	/**
	 * 翌日文字列を返します.
	 * @param targetDate 指定日
	 * @return 指定日の翌日文字列
	 */
	private String getTomorrowDate(String targetDate) throws ParseException {
		Date returnDate = null;	
		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);

		
		Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(df.parse(targetDate));
		cal.add(Calendar.DAY_OF_MONTH, 1);
		returnDate = cal.getTime();

		return df.format(returnDate);
	}

	/**
	 * 指定された日にちの前月の最終日を返します.
	 * @param targetDate 指定日
	 * @return 指定された日にちの前月の最終日文字列
	 * @throws ParseException
	 */
	private String get1MonthAgoLastDate(String targetDate) throws ParseException {
		Date returnDate = null;	
		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);

		
		Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(df.parse(targetDate));
		cal.set(Calendar.DAY_OF_MONTH , 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		returnDate = cal.getTime();

		return df.format(returnDate);
	}

}
