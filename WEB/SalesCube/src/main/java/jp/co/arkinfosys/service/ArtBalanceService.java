/*
 * Copyright 2009-2010 Ark Information Systems.
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

	// 発番用サービス
	public SeqMakerService seqMakerService;

	@Resource
	private YmService ymService;

	@Resource
	private BillAndArtService	billAndArtService;

	/**
	 * SQLファイルのパラメータ名定義
	 */
	public static class Param {
		private static final String SORT_ORDER = "sortOrder"; // ソート方向
		private static final String ART_BALANCE_ID = "artBalanceId"; // 売掛残高番号
		private static final String ART_ANNUAL = "artAnnual"; // 売掛残高年度
		private static final String ART_MONTHLY = "artMonthly"; // 売掛残高月度
		private static final String ART_CUTOFF_DATE = "artCutoffDate"; // 売掛締日
		private static final String CUSTOMER_CODE = "customerCode"; // 顧客コード
		private static final String SORT_COLUMN_CUTOFF_DATE = "sortColumnCutoffDate"; // 行番号のソート条件
		private static final String ROW_COUNT = "rowCount"; // 取得件数
		private static final String OFFSET_ROW = "offsetRow"; // 取得件数
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
		// 伝票番号の発番
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
		// SQLクエリを投げる
		return this.updateBySqlFile(
				"artbalance/InsertArtBalance.sql", createParamMap(ab)).execute();
	}
	
	/**
	 * エンティティ情報でDBから削除します.
	 *
	 * @param ab 売掛残高エンティティ
	 *
	 * @return　実行行数
	 */
	public int delete(ArtBalance ab) {
		// SQLクエリを投げる
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

		// MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		// 基礎となるカラム名(空で)をエンティティからPUT
		// BeanMap FoundationParam =
		// Beans.createAndCopy(BeanMap.class,this.depositLine).execute();
		// param.putAll(FoundationParam);

		// アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, ab).execute();
		param.putAll(AFparam);

		// 更新日時とかPUT
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

		// 条件設定
		// 顧客コードが一致
		// 請求締日の降順で１件取得
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

		// 条件設定
		// 顧客コード、請求締日が一致
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

		// 条件設定
		// 顧客コード、請求締日が一致
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

		// 条件設定
		// 顧客コードが一致、指定日以前(一致も含む)
		// 請求締日の降順で１件取得
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

		// 締め処理
		// 締める対象は、
		// 対象期間に売上が存在している　あるいは
		// 対象期間に入金が存在している　あるいは
		// 前回売掛残高に繰越金額が存在している　顧客
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
		// 顧客マスタ、納入先取得
		Customer customer = customerService.findCustomerByCode(customerCode);
		List<DeliveryAndPre> deliveryList =
			deliveryService.searchDeliveryByCompleteCustomerCode( customerCode );

		// 売上伝票取得 締っていない　対象期間の　全ての売上データが対象
		List<SalesSlipTrn> salesSlipList = salesService.findArtOpenSalesSlipByCustomerCode(customerCode, closeDate);

		// 売上明細行取得 締っていない　対象期間の　全ての売上データが対象
		List<SalesLineTrn> salesLineList = salesLineService.findArtOpenSalesLineByCustomerCode(customerCode, closeDate);

		// 入金伝票取得 締っていない　対象期間の　全ての入金データが対象
		List<DepositSlipDto> depositSlipList = depositSlipService.findArtOpenDepositSlipByCustomerCode(customerCode, closeDate);

		// 入金伝票明細行取得 締っていない　対象期間の　全ての入金データが対象
		List<DepositLine> depositLineList = depositLineService.findArtOpenDepositLineByCustomerCode(customerCode, closeDate);

		// 前回請求額取得
		List<ArtBalance> artList = findLastArtBalanceByCustomerCode(customerCode);

		// 売掛残高情報作成
		ArtBalance newArtBalance = null;
		if(artList.size() > 0) {
			newArtBalance = createArtBalance( closeDate, customer, deliveryList, salesSlipList, salesLineList, depositLineList, artList.get(0).thisArtPrice, true );
		} else {
			newArtBalance = createArtBalance( closeDate, customer, deliveryList, salesSlipList, salesLineList, depositLineList, null, true );
		}

		// DBへ登録
		if( insert(newArtBalance) == 0 ){
			throw new ServiceException("errors.system");
		}
		// 更新後のデータを取得して返す
		newArtBalance = findArtBalanceById( newArtBalance.artBalanceId );
		if( newArtBalance == null ){
			// 直前で登録しているのでありえないが一応チェック
			throw new ServiceException("errors.system");
		}

		// 顧客の最終締め処理日=ダイアログの締実行日を設定
		customerService.updateLastSalesCutoffDate(customerCode,closeDate);

		// 売上伝票と、それに紐付く明細行を締める
		salesService.closeSalesSlipArt(salesSlipList,newArtBalance.artBalanceId,closeDate,newArtBalance.artCutoffPdate);

		// 入金伝票と、それに紐付く明細行を締める
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

		// 売掛残高番号
		if(isSeqMake) {
			artBalance.artBalanceId = getNextVal().intValue();
		}
		// 請求年度、月度、年月度を計算
		YmDto ymDto = ymService.getYm(closeDate);
		// 請求年
		if( ymDto != null ){
			// 売掛残高年度
			artBalance.artAnnual = Short.valueOf( ymDto.annual.toString() );
			// 売掛残高月度
			artBalance.artMonthly = Short.valueOf( ymDto.monthly.toString() );
			// 売掛残高年月度
			artBalance.artYm = Integer.valueOf( ymDto.ym.toString() );
		}
		// 売掛締日
		artBalance.artCutoffDate = super.convertUtilDateToSqlDate(DF_YMD.parse(closeDate));;
		// 担当者コード
		artBalance.userId = this.userDto.userId;
		// 担当者名
		artBalance.userName = this.userDto.nameKnj;
		// 請求先コード
		artBalance.baCode = deliveryList.get(0).deliveryCode;
		// 請求先名
		artBalance.baName = deliveryList.get(0).deliveryName;
		// 得意先コード
		artBalance.customerCode = customer.customerCode;
		// 得意先名
		artBalance.customerName = customer.customerName;
		// 売上取引区分
		artBalance.salesCmCategory = customer.salesCmCategory;
		// 前回請求金額
		if( lastArtPrice == null ){
			// データが存在しない場合には請求額は０
			artBalance.lastArtPrice = new BigDecimal(0);
		}else{
			artBalance.lastArtPrice = lastArtPrice;
		}
		// 入金額
		artBalance.depositPrice = billAndArtService.getDepositPrice(depositLineList);;
		// 調整金額は未設定
		// 繰越金額
		artBalance.covPrice = artBalance.lastArtPrice.subtract( artBalance.depositPrice );
		// 売上金額
		artBalance.salesPrice = billAndArtService.getSalesPrice(salesLineList);
		// 消費税額
		if( CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX.equals(customer.taxShiftCategory)) {
			// 区分名：税転嫁、区分コード名：内税
			// 消費税額
			artBalance.ctaxPrice = null;
			// 今回売掛金額 = 繰越金額＋売上金額
			artBalance.thisArtPrice = artBalance.covPrice.add(artBalance.salesPrice);
		}else{
			// 区分名：税転嫁、区分コード名：内税以外
			// 消費税額
			artBalance.ctaxPrice = billAndArtService.getCTaxPrice(customer, salesLineList);
			// 今回売掛金額 = 繰越金額＋売上金額＋消費税
			artBalance.thisArtPrice = artBalance.covPrice.add(artBalance.salesPrice.add(artBalance.ctaxPrice));
		}
		// 返品金額,	値引金額は未設定
		// その他金額
		artBalance.etcPrice = billAndArtService.getEtcPrice(salesLineList);
		// 粗利金額は未設定
		// 締日グループ
		artBalance.artCutoffGroup = customer.cutoffGroup;
		// 回収間隔
		artBalance.paybackCycleCategory = customer.paybackCycleCategory;
		// 伝票枚数
		artBalance.salesSlipNum = Short.parseShort( Integer.toString(salesSlipList.size()) );
		// 売掛締め処理日
		Timestamp ts = new Timestamp(GregorianCalendar.getInstance().getTimeInMillis());
		artBalance.artCutoffPdate = ts;
		// 入金現金,入金小切手,入金振込,入金手数料,入金手形,入金相殺,入金その他金額は未設定
		// 親子区分
		artBalance.familyCategory = CategoryTrns.FAMILY_CATEGORY_PARENTS;
		// 納入先数
		artBalance.deliveryPlaceNum = 1;
		// 備考は未設定

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

		// 前回締日の特定
		try {
			DF_YMD.parse(lastCutOffDate);
		} catch (ParseException e) {
			// 内容がコメントで日付ではない場合には解除すべきデータが存在しないので、
			// 何もしない
			return msgs;
		}

		// 締め処理
		// 売掛以外
		// 締める対象は、顧客の売上区分が売掛以外　かつ
		// 前回締日の売掛残高データが存在している　顧客
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

		// 顧客マスタ、納入先取得
//		Customer customer = customerService.findCustomerByCode(customerCode);
//		List<DeliveryAndPre> deliveryList =
//			deliveryService.searchDeliveryByCompleteCustomerCode( customerCode );

		// 指定日付で締っている売上伝票取得
		List<SalesSlipTrn> salesSlipList = salesService.findArtCloseSalesSlipByCustomerCode(customerCode, lastCutOffDate);

		// 指定日付で締っている入金伝票取得
		List<DepositSlipDto> depositSlipList = depositSlipService.findArtCloseDepositSlipByCustomerCode(customerCode, lastCutOffDate);

		// 売掛残高情報削除
		deleteArtBalance( customerCode, lastCutOffDate);

		// 顧客に対する最新の売掛残高を取得する
		List<ArtBalance> artList = findLastArtBalanceByCustomerCode(customerCode);

		// 顧客の最終締め処理日を再設定
		if( artList.size() == 0 ){
			// 売掛残高が存在していない場合はnullを設定する
			customerService.updateLastSalesCutoffDate(customerCode,null);
		}else{
			SimpleDateFormat DF_TIME = new SimpleDateFormat(Constants.FORMAT.TIMESTAMP);
			customerService.updateLastSalesCutoffDate(customerCode,DF_TIME.format(artList.get(0).artCutoffDate));
		}
		// 売上伝票と紐付く明細行を締解除する
		salesService.reOpenSalesSlipArt(salesSlipList);

		// 入金伝票と紐付く明細行を締解除する
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
			// 現在処理中の顧客の、最終の請求書が移行データの場合、同一日の売掛残高が無い場合がある。その場合は売掛締解除はせずに請求締め解除のみを行う。
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
		// 顧客マスタ、納入先取得
		Customer customer = customerService.findCustomerByCode(customerCode);
		List<DeliveryAndPre> deliveryList =
			deliveryService.searchDeliveryByCompleteCustomerCode( customerCode );

		// 指定日の1ヶ月前を取得
		String monthAgoDate = get1MonthAgoLastDate(targetDate);

		// 指定日より1ヶ月前の売掛金額を計算する
		BigDecimal artPrice;
		artPrice = getArtPrice(monthAgoDate, customerCode, customer, deliveryList);

		// 指定日より1ヶ月前から指定日までの売上と入金を使用して現在の売掛残高レコードを取得する

		String monthAgoTomorrowDate = null;
		monthAgoTomorrowDate = getTomorrowDate(monthAgoDate);

		// 売上伝票取得
		List<SalesSlipTrn> salesSlipList = salesService.findSalesSlipByCustomerCodeBetweenDate(customerCode, monthAgoTomorrowDate, targetDate, null );

		// 売上明細行取得
		List<SalesLineTrn> salesLineList = salesLineService.findSalesLineByCustomerCodeBetweenDate(customerCode, monthAgoTomorrowDate, targetDate, null );

		// 入金伝票明細行取得
		List<DepositLine> depositLineList = depositLineService.findDepositLineByCustomerCodeBetweenDate(customerCode, monthAgoTomorrowDate, targetDate, null);

		// 売掛残高情報作成
		ArtBalance ret = createArtBalance( targetDate, customer, deliveryList, salesSlipList, salesLineList, depositLineList, artPrice, false );

		// 基本空のカラムに0をセットする
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

		//過去の売掛残高の内、最も最近のものを取得する
		List<ArtBalance> lastArtBalanceList = findLastArtBalanceByCustomerCodeBeforeTargetDate(customerCode, targetDate);

		// 取得した売掛残高の売掛締め日の翌日を取得する
		String lastArtCutoffDate;
		if(lastArtBalanceList != null && lastArtBalanceList.size() > 0 ) {
			lastArtCutoffDate = getTodayDate(lastArtBalanceList.get(0).artCutoffDate);
		} else {
			lastArtCutoffDate = "";
		}

		// 売上伝票取得
		List<SalesSlipTrn> salesSlipList = salesService.findSalesSlipByCustomerCodeBetweenDate(customerCode, null, targetDate, lastArtCutoffDate );

		// 売上明細行取得
		List<SalesLineTrn> salesLineList = salesLineService.findSalesLineByCustomerCodeBetweenDate(customerCode, null, targetDate, lastArtCutoffDate );

		// 入金伝票明細行取得
		List<DepositLine> depositLineList = depositLineService.findDepositLineByCustomerCodeBetweenDate(customerCode, null, targetDate, lastArtCutoffDate );

		// 請求情報作成（このデータはDBには登録しません）
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
		Date returnDate = null;	// 最終請求締日の翌日
		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);

		// 最終請求締日の翌日を取得
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
		Date returnDate = null;	// 最終請求締日の翌日
		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);

		// 最終請求締日の翌日を取得
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
		Date returnDate = null;	// 最終請求締日の翌日
		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);

		// 最終請求締日の翌日を取得
		Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(df.parse(targetDate));
		cal.set(Calendar.DAY_OF_MONTH , 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		returnDate = cal.getTime();

		return df.format(returnDate);
	}

}
