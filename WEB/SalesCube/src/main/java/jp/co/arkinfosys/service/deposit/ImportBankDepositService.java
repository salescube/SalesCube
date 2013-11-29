/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.deposit;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.deposit.BankDepositRelDto;
import jp.co.arkinfosys.dto.deposit.BankDepositWorkDto;
import jp.co.arkinfosys.dto.deposit.DepositLineDto;
import jp.co.arkinfosys.dto.deposit.DepositSlipDto;
import jp.co.arkinfosys.dto.deposit.ImportBankDepositResultDto;
import jp.co.arkinfosys.dto.deposit.ImportBankDepositResultSortDto;
import jp.co.arkinfosys.entity.Bank;
import jp.co.arkinfosys.entity.BankDepositWork;
import jp.co.arkinfosys.entity.Bill;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.BankDepositRelService;
import jp.co.arkinfosys.service.BankDepositWorkService;
import jp.co.arkinfosys.service.BankService;
import jp.co.arkinfosys.service.BillService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.DeliveryService;
import jp.co.arkinfosys.service.DepositLineService;
import jp.co.arkinfosys.service.DepositSlipService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

/**
 * 銀行入金取込みサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ImportBankDepositService extends AbstractService<BankDepositWork>{

	@Resource
	public BankDepositWorkService bankDepositWorkService;

	@Resource
	public BankDepositRelService bankDepositRelService;

	@Resource
	public CustomerService customerService;

	@Resource
	private DeliveryService deliveryService;

	@Resource
	private BankService bankService;

	@Resource
	private DepositSlipService depositSlipService;

	@Resource
	private DepositLineService depositLineService;

	@Resource
	private BillService billService;

	@Resource
	private YmService ymService;

	/**
	 * 日付の形式指定
	 */
	private static SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String STATUS = "status";
		public static final String DEPOSIT_SLIP_ID = "depositSlipId";
		public static final String CUSTOMER = "customer";
		public static final String PAYMENT_DATE= "paymentDate";
		public static final String PAYMENT_NAME = "paymentName";
		public static final String LAST_BILL_PRICE = "lastBillPrice";
		public static final String PAYMENT_PRICE = "paymentPrice";
		public static final String DIFF_PRICE = "diffPrice";
		public static final String CHANGE_NAME = "changeName";
		public static final String AFTER_CHANGE_NAME= "afterChangeName";

		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_ORDER = "sortOrder";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";
	}

	/**
	 * 銀行入金データを登録します.
	 * <p>
	 * 振込金額に一致する請求金額がある場合のみ処理対象とします.<br>
	 * 金額が一致する請求がある場合、銀行入金データを登録します.
	 * </p>
	 *
	 * @param userId ユーザID
	 * @param bankId 銀行ID
	 * @return 新規に発番された入金伝票ID
	 * @throws Exception
	 */
	// 登録処理
	public  String insertBankDeposit(String userId, String bankId) throws Exception {

		String newDepositSlipIdStr = "";

		// 銀行入金データと得意先マスタを突合する

		// 銀行入金データを取得
		List<BankDepositWorkDto> inputList = bankDepositWorkService.findBankDepositWorkByUserId(userId);

		// 銀行マスタから取得
		Bank bankInfo = (Bank)bankService.findById(bankId);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		for(BankDepositWorkDto dto : inputList) {
			String paymentDate = StringUtil.getDateString(Constants.FORMAT.DATE, format.parse(dto.paymentDate));
			String paymentName = dto.paymentName;
			String paymentPrice = dto.paymentPrice;
			String lineNo = dto.lineNo;

			// 【銀行入金データ】振り込み名義を変換する
			// 	①【銀行入金データ】振り込み名義を全文字を全角にする
			//	②　①の文字列の前後スペースを削除
			//	③　②の文字列を変換テーブルで変換する
			String newPaymentName = StringUtil.convertPaymentName(paymentName);

			// 【得意先マスタ】を振り込み名義で検索する
			List<CustomerJoin> customerList = customerService.findByPaymentName(newPaymentName);
			if( customerList == null || customerList.size() == 0) {
				continue;
			}

			// 銀行入金関連テーブルを振り込み名義と振り込み日と振り込み金額、行番号で検索する
			List<BankDepositRelDto> bankDepositRelList =
				bankDepositRelService.findByPaymentNameAndDate(paymentName, paymentDate, paymentPrice, lineNo);
			if( bankDepositRelList != null && bankDepositRelList.size() > 0) {
				continue;
			}

			// 【銀行入金データ】振り込み金額と請求金額一致しない場合取り込まない 2010/05/24 add kaki
			// 前回請求額
			List<String> customerCodeList = new ArrayList<String>();
			for(Customer customer : customerList) {
				customerCodeList.add(customer.customerCode);
			}
			List<Bill> billList = billService.findLastBillByCustomerCodeToCutoff(customerCodeList, paymentDate);

			// 金額の一致する請求を確認
			boolean priceMatch = false;
			String customerCode = null;
			for(Bill bill : billList) {
				if(bill.thisBillPrice.compareTo(new BigDecimal(paymentPrice)) == 0) {
					priceMatch = true;
					customerCode = bill.customerCode;
					break;
				}
			}
			if(!priceMatch) {
				// 一致する請求なし
				continue;
			}

			// 入金伝票を登録する
			// 「新規登録」 入金伝票を作成し、銀行入金関連テーブルに登録する
			// 得意先情報を取得
			 Customer customerData = customerService.findCustomerByCode(customerCode);

			 DepositSlipDto depositSlipDto = new DepositSlipDto();
			 depositSlipDto.fillList();

			 depositSlipDto.status = Constants.STATUS_DEPOSIT_SLIP.PAID;
			 depositSlipDto.depositDate = paymentDate;
			 depositSlipDto.inputPdate = DF_YMD.format(new Date());
		     // 入力処理日から仕入年度、仕入月度、仕入年月度を取得
			 YmDto ymDto = ymService.getYm(depositSlipDto.inputPdate);
			 if( ymDto != null ){
				 depositSlipDto.depositAnnual = ymDto.annual.toString();
				 depositSlipDto.depositMonthly = ymDto.monthly.toString();
				 depositSlipDto.depositYm = ymDto.ym.toString();
			 }

			 depositSlipDto.userId =  this.userDto.userId;
			 depositSlipDto.userName = this.userDto.nameKnj;
			 depositSlipDto.depositAbstract = null;
			 depositSlipDto.remarks = null;
			 if( customerData != null){
				 depositSlipDto.customerCode = customerData.customerCode;
				 depositSlipDto.customerName = customerData.customerName;

				 depositSlipDto.cutoffGroup  = customerData.cutoffGroup;
				 depositSlipDto.paybackCycleCategory = customerData.paybackCycleCategory;

				 // 顧客コードを指定して請求先を取得する
				 List<DeliveryAndPre>baList =  deliveryService.searchDeliveryByCompleteCustomerCode(customerData.customerCode);
				 DeliveryAndPre baInfo = baList.get(0);

				 depositSlipDto.baCode = baInfo.deliveryCode;
				 depositSlipDto.baName = baInfo.deliveryName;
				 depositSlipDto.baKana = baInfo.deliveryKana;
				 depositSlipDto.baOfficeName = baInfo.deliveryOfficeName;
				 depositSlipDto.baOfficeKana = baInfo.deliveryOfficeKana;
				 depositSlipDto.baDeptName = baInfo.deliveryDeptName;
				 depositSlipDto.baZipCode = baInfo.deliveryZipCode;
				 depositSlipDto.baAddress1 = baInfo.deliveryAddress1;
				 depositSlipDto.baAddress2 = baInfo.deliveryAddress2;
				 depositSlipDto.baPcName = baInfo.deliveryPcName;
				 depositSlipDto.baPcKana = baInfo.deliveryPcKana;
				 depositSlipDto.baPcPre = baInfo.categoryCodeName;
				 depositSlipDto.baPcPreCatrgory = baInfo.deliveryPcPreCategory;
				 depositSlipDto.baTel = baInfo.deliveryTel;
				 depositSlipDto.baFax = baInfo.deliveryFax;
				 depositSlipDto.baEmail = baInfo.deliveryEmail;
				 depositSlipDto.baUrl = baInfo.deliveryUrl;

				 depositSlipDto.salesCmCategory = customerData.salesCmCategory;
				 depositSlipDto.taxFractCategory = customerData.taxFractCategory;
				 depositSlipDto.priceFractCategory = customerData.priceFractCategory;
			 }
			 depositSlipDto.depositCategory = CategoryTrns.DEPOSIT_CATEGORY_TRANSFER;	// 振込み
			 Double dPrice = Double.parseDouble(dto.paymentPrice);
			 int iPrice =  dPrice.intValue();
			 depositSlipDto.depositTotal = Integer.valueOf(iPrice).toString();
			 depositSlipDto.depositMethodTypeCategory = CategoryTrns.DEPOSIT_METHOD_BANK;

			 // 明細行
			 DepositLineDto line = new DepositLineDto();
			 line.status = Constants.STATUS_DEPOSIT_LINE.PAID;
			 line.lineNo = String.valueOf(1);
			 line.depositCategory = depositSlipDto.depositCategory;

			 line.price = depositSlipDto.depositTotal;
			 line.bankId = bankInfo.bankId.toString();
			 line.bankInfo = bankInfo.bankName + " " + bankInfo.storeName;

			 depositSlipDto.getLineDtoList().add(line);
			 depositSlipDto.removeBlankLine();

			 // 登録
			try {
				Long newSlipId = this.insert(depositSlipDto, paymentDate, paymentName, lineNo);
				if( newDepositSlipIdStr.length() == 0){
					newDepositSlipIdStr = newSlipId.toString();
				}else{
					newDepositSlipIdStr = newDepositSlipIdStr + "," + newSlipId.toString();
				}

			} catch (ServiceException e) {
				throw(e);
			} catch (UnabledLockException e) {
				throw(e);
			} catch (Exception e) {
				throw(e);
			}



		}
		return newDepositSlipIdStr;
	}

	/**
	 * 銀行入金データをデータベースに登録します.
	 * @param dto 入金伝票のDTOクラスです.
	 * @param paymentDate 支払日
	 * @param paymentName 振込名義
	 * @param lineNo 行番号
	 * @return 新規に発番された入金伝票ID
	 * @throws Exception
	 */
	 private Long insert(DepositSlipDto dto, String paymentDate, String paymentName, String lineNo) throws Exception {
		 Long newSlipId;
		 try {
			 depositSlipService.save(dto);
			 newSlipId = Long.valueOf(dto.getKeyValue());
			 depositLineService.save(dto, dto.getLineDtoList(), null);

			// 銀行入金関連テーブルにinsertする
			 BankDepositRelDto rel = new BankDepositRelDto();
			 rel.depositSlipId = newSlipId.toString();
			 rel.paymentDate = paymentDate;
			 rel.paymentName= paymentName;
			 rel.lineNo = lineNo;

			 bankDepositRelService.insertRecord(rel);

		} catch (ServiceException e) {
			throw(e);
		} catch (UnabledLockException e) {
			throw(e);
		} catch (Exception e) {
			throw(e);
		}

		return newSlipId;
	 }

	/**
	 * 取込結果リストを返します.
	 *
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param userId ユーザID
	 * @param newDepositSlipIdStr 新規入金伝票ID
	 * @return 取込結果リスト
	 * @throws ServiceException
	 */
	 public List<ImportBankDepositResultDto> getImportResultList(String sortColumn,
			boolean sortOrderAsc, String userId, String newDepositSlipIdStr)
			throws ServiceException {
		try {
			List<ImportBankDepositResultDto> resultSortList = new ArrayList<ImportBankDepositResultDto>();

			List<ImportBankDepositResultSortDto> resultList = this
					.getImportResultList(userDto.userId, newDepositSlipIdStr);

			ImportBankDepositResultSortDto[] resultArray = new ImportBankDepositResultSortDto[resultList
					.size()];

			int index = 0;
			// ソートする
			for (ImportBankDepositResultSortDto result : resultList) {
				String key = "";
				if (Param.STATUS.equals(sortColumn)) {
					key = result.status;
				} else if (Param.DEPOSIT_SLIP_ID.equals(sortColumn)) {
					key = result.depositSlipId;
				} else if (Param.CUSTOMER.equals(sortColumn)) {
					key = result.customer;
				} else if (Param.PAYMENT_DATE.equals(sortColumn)) {
					key = result.paymentDate;
				} else if (Param.PAYMENT_NAME.equals(sortColumn)) {
					key = result.paymentName;
				} else if (Param.LAST_BILL_PRICE.equals(sortColumn)) {
					key = result.lastBillPrice;
				} else if (Param.PAYMENT_PRICE.equals(sortColumn)) {
					key = result.paymentPrice;
				} else if (Param.DIFF_PRICE.equals(sortColumn)) {
					key = result.diffPrice;
				} else if (Param.CHANGE_NAME.equals(sortColumn)) {
					key = result.changeName;
				} else if (Param.AFTER_CHANGE_NAME.equals(sortColumn)) {
					key = result.afterChangeName;
				}

				result.key = key;
				result.sortColumn = sortColumn;
				result.sortOrderAsc = sortOrderAsc;

				resultArray[index] = result;
				index++;
			}

			java.util.Arrays.sort(resultArray);

			for (int i = 0; i < resultArray.length; i++) {
				ImportBankDepositResultDto dto = resultArray[i];
				resultSortList.add(dto);
			}

			return resultSortList;
		} catch (ParseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 取込結果リストを返します.
	 *
	 * @param userId ユーザID
	 * @param newDepositSlipIdStr 新規入金伝票ID
	 * @return 取込結果リスト
	 * @throws ServiceException
	 * @throws ParseException
	 */
	private List<ImportBankDepositResultSortDto> getImportResultList(String userId ,String newDepositSlipIdStr )
			throws ServiceException, ParseException
	{
		// 新規入金伝票番号マップの作成
		Map<Long, Long> newDepositSlipIdMap = new HashMap<Long, Long>();
		if( newDepositSlipIdStr.length() > 0 ){
			String tmpString = newDepositSlipIdStr;
			String[] strAry = tmpString.split(",");
			for (int i=0; i<strAry.length; i++) {
			      Long newSlipId = Long.valueOf(strAry[i]);
			      newDepositSlipIdMap.put(newSlipId, newSlipId);
			}
		}

		List<ImportBankDepositResultSortDto> resultList = new ArrayList<ImportBankDepositResultSortDto>() ;
		// 銀行入金データと得意先マスタを突合する

		// 銀行入金データを取得
		List<BankDepositWorkDto> inputList = bankDepositWorkService.findBankDepositWorkByUserId(userId);

		ImportBankDepositResultSortDto  resultDto = new ImportBankDepositResultSortDto();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		for(BankDepositWorkDto dto : inputList) {
			String paymentDate = StringUtil.getDateString(Constants.FORMAT.DATE, format.parse(dto.paymentDate));
			String paymentName = dto.paymentName;
			String paymentPrice = dto.paymentPrice;
			String lineNo = dto.lineNo;

			// 【銀行入金データ】振り込み名義を変換する
			// 	①【銀行入金データ】振り込み名義を全文字を全角にする
			//	②　①の文字列の前後スペースを削除
			//	③　②の文字列を変換テーブルで変換する
			String newPaymentName = StringUtil.convertPaymentName(paymentName);

			// 【得意先マスタ】を振り込み名義で検索する
			List<CustomerJoin> customerList = customerService.findByPaymentName(newPaymentName);
			if( customerList == null || customerList.size() == 0) {
				// 結果をセット
				resultDto = new ImportBankDepositResultSortDto();
				resultDto.status = Constants.BANK_DEPOSIT_CSV.STATUS_UNKNOWN;
				resultDto.depositSlipId = "";
				resultDto.customer="";
				resultDto.paymentDate = paymentDate;
				resultDto.paymentName = paymentName;
				resultDto.lastBillPrice= "";
				resultDto.paymentPrice = paymentPrice;
				resultDto.diffPrice = "";
				if( paymentName.equals(newPaymentName)){
					resultDto.changeName = Constants.BANK_DEPOSIT_CSV.CHANGE_NAME_NON;
					resultDto.afterChangeName = "";
				}else{
					resultDto.changeName = Constants.BANK_DEPOSIT_CSV.CHANGE_NAME_YES;
					resultDto.afterChangeName = newPaymentName;
				}
				resultList.add(resultDto);
				continue;
			}

			// 前回請求額
			List<String> customerCodeList = new ArrayList<String>();
			for(Customer customer : customerList) {
				customerCodeList.add(customer.customerCode);
			}
			List<Bill> billList = billService.findLastBillByCustomerCodeToCutoff(customerCodeList, paymentDate);

			// 金額の一致する請求を確認
			boolean priceMatch = false;
			String lastBillPrice = null;
			String customerCode = null;
			String diffPrice = null;
			for(Bill bill : billList) {
				if(bill.thisBillPrice.compareTo(new BigDecimal(paymentPrice)) == 0) {
					priceMatch = true;
					customerCode = bill.customerCode;
					lastBillPrice = bill.thisBillPrice.toString();
					diffPrice = "0";
					break;
				}
			}
			if(!priceMatch) {
				// 一致する請求が無い場合は先頭顧客
				customerCode = ((Customer)customerList.get(0)).customerCode;
			}

			// 得意先情報を取得
			Customer customerData = customerService.findCustomerByCode(customerCode);

			// 【銀行入金データ】振り込み金額と請求金額一致しない場合、エラー[金額不一致]2010/05/24 add kaki
			if(!priceMatch){
				// 結果をセット
				resultDto = new ImportBankDepositResultSortDto();
				resultDto.status = Constants.BANK_DEPOSIT_CSV.STATUS_NONPRICE;
				resultDto.depositSlipId = "";
				resultDto.customer = customerData.customerName;
				resultDto.paymentDate = paymentDate;
				resultDto.paymentName = paymentName;

				if( billList.size() == 0 ) {
					lastBillPrice = "0";
				}
				else {
					// 一致する請求が無いので先頭の請求
					lastBillPrice = billList.get(0).thisBillPrice.toString();
				}

				BigDecimal lastBill = new BigDecimal(lastBillPrice);
				BigDecimal pay = new BigDecimal(dto.paymentPrice);
				BigDecimal diff = lastBill.subtract(pay);
				resultDto.lastBillPrice= lastBillPrice;
				resultDto.diffPrice = diff.toString();

				resultDto.paymentPrice = paymentPrice;

				if( paymentName.equals(newPaymentName)){
					resultDto.changeName = Constants.BANK_DEPOSIT_CSV.CHANGE_NAME_NON;
					resultDto.afterChangeName = "";
				}else{
					resultDto.changeName = Constants.BANK_DEPOSIT_CSV.CHANGE_NAME_YES;
					resultDto.afterChangeName = newPaymentName;
				}
				resultList.add(resultDto);
				continue;
			}

			// 銀行入金関連テーブルを振り込み名義と振り込み日と振り込み金額、行番号で検索する
			List<BankDepositRelDto> bankDepositRelList =
					bankDepositRelService.findByPaymentNameAndDate(paymentName, paymentDate, paymentPrice, lineNo);
			if( bankDepositRelList == null || bankDepositRelList.size() == 0) {
				// 新規登録　こちらには来ないはず
				continue;
			}

			BankDepositRelDto bankDepositRel = (BankDepositRelDto)bankDepositRelList.get(0);

			// 新規かどうか newDepositSlipIdMapに入金番号があれば新規
			resultDto = new ImportBankDepositResultSortDto();
			Long key = Long.valueOf(bankDepositRel.depositSlipId);
			if( newDepositSlipIdMap != null && newDepositSlipIdMap.containsKey(key) ){
				// 新規
				resultDto.status = Constants.BANK_DEPOSIT_CSV.STATUS_NEW;
			}else{
				// 登録済み
				resultDto.status = Constants.BANK_DEPOSIT_CSV.STATUS_OLD;
			}

			resultDto.depositSlipId = bankDepositRel.depositSlipId;
			resultDto.customer = customerData.customerName;
			resultDto.paymentDate = paymentDate;
			resultDto.paymentName = paymentName;
			resultDto.lastBillPrice= lastBillPrice;
			resultDto.paymentPrice = paymentPrice;
			resultDto.diffPrice = diffPrice;
			if( paymentName.equals(newPaymentName)){
				resultDto.changeName = Constants.BANK_DEPOSIT_CSV.CHANGE_NAME_NON;
				resultDto.afterChangeName = "";
			}else{
				resultDto.changeName = Constants.BANK_DEPOSIT_CSV.CHANGE_NAME_YES;
				resultDto.afterChangeName = newPaymentName;
			}
			resultList.add(resultDto);
		}
		return resultList;
	}

}
