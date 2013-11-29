/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.deposit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.deposit.DeliveryDepositRelDto;
import jp.co.arkinfosys.dto.deposit.DepositLineDto;
import jp.co.arkinfosys.dto.deposit.DepositSlipDto;
import jp.co.arkinfosys.dto.deposit.ImportDeliveryDepositResultDto;
import jp.co.arkinfosys.dto.deposit.ImportDeliveryDepositResultSortDto;
import jp.co.arkinfosys.entity.Bank;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.entity.join.InvoiceDataWorkJoin;
import jp.co.arkinfosys.entity.join.DeliveryDepositWorkJoin;
import jp.co.arkinfosys.service.BankService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.DeliveryService;
import jp.co.arkinfosys.service.DepositLineService;
import jp.co.arkinfosys.service.DepositSlipService;
import jp.co.arkinfosys.service.DeliveryDepositRelService;
import jp.co.arkinfosys.service.InvoiceDataWorkService;
import jp.co.arkinfosys.service.DeliveryDepositWorkService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;

/**
 * 配送業者入金取込みサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ImportDeliveryDepositService extends DeliveryDepositWorkService {
	@Resource
	private DeliveryDepositWorkService deliveryDepositWorkService;

	@Resource
	private InvoiceDataWorkService invoiceDataWorkService;

	@Resource
	private CustomerService customerService;

	@Resource
	private DepositSlipService depositSlipService;

	@Resource
	private DepositLineService depositLineService;

	@Resource
	private DeliveryService deliveryService;

	@Resource
	private DeliveryDepositRelService deliveryDepositRelService;

	@Resource
	private BankService bankService;

	@Resource
	private YmService ymService;

	/**
	 * 日付の形式指定
	 */
	private static SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String STATUS = "status";
		public static final String SALES_SLIP_ID = "salesSlipId";
		public static final String DEPOSIT_SLIP_ID = "depositSlipId";
		public static final String DELIVERY_SLIP_ID = "deliverySlipId";
		public static final String CUSTOMER = "customer";
		public static final String DELIVERY_DATE = "deliveryDate";
		public static final String PRODUCT_PRICE = "productPrice";
		public static final String SALES_MONY = "salesMoney";

		public static final String SORT_COLUMN = "sortColumn";
		public static final String SORT_ORDER = "sortOrder";
		public static final String SORT_ORDER_ASC = "sortOrderAsc";
	}

	/**
	 * 配送業者入金データを登録します.
	 * @param userId ユーザID
	 * @param bankId 銀行ID
	 * @param errorList エラーリスト
	 * @return 新規に発番された入金伝票ID
	 * @throws Exception
	 */
	public  String insertDeliveryDeposit(String userId, String bankId, List<ActionMessages> errorList) throws Exception {

		String newDepositSlipIdStr = "";

		// 配送業者入金データと送り状データを伝票番号で突合する
		List<DeliveryDepositWorkJoin> depositList = deliveryDepositWorkService.findDeliveryDepositWorkByUserId(userId);
		List<InvoiceDataWorkJoin> invoiceDataWorkList = invoiceDataWorkService.findInvoiceDataWorkByUserId(userId);
		int depositIndex = 0;
		int index = 0;

		int depositSize = depositList.size();
		int invoiceSize = invoiceDataWorkList.size();

		DeliveryDepositWorkJoin deposit;
		InvoiceDataWorkJoin invoiceDataWork ;

		deposit = depositList.get(depositIndex);
		String deliverySlipId = deposit.deliverySlipId;
		long productPrice = deposit.productPrice.longValue();
		String dataCategory = deposit.dataCategory;
		Integer depositSlipId = deposit.depositSlipId;

		invoiceDataWork = invoiceDataWorkList.get(index);
		String invoiceDeliverySlipId = invoiceDataWork.deliverySlipId;
		String invoiceSalesSlipId = invoiceDataWork.invoiceSalesSlipId;
		Integer salesSlipId = invoiceDataWork.salesSlipId;
		String customerCode = invoiceDataWork.customerCode;

		int depositLineNo = 0;		// 入金伝票行番

		// 銀行マスタから取得
		Bank bankInfo = (Bank)bankService.findById(bankId);

		while ( depositIndex<depositSize ||  index<invoiceSize){
			// 配送業者入金データのデータ区分が「20（返品）」はスキップする
			if( deliverySlipId.length() > 0){
				if( Constants.DELIVERY_DEPOSIT_CSV.DATA_CATEGORY_RETURN_GOODS.equals(dataCategory)){
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.delivery.returnItem",deliverySlipId));
					errorList.add(errors);
					depositIndex++;
					if( depositIndex < depositSize){
						deposit = depositList.get(depositIndex);
						deliverySlipId = deposit.deliverySlipId.trim();
						productPrice = deposit.productPrice.longValue();
						dataCategory = deposit.dataCategory;
						depositSlipId = deposit.depositSlipId;
					}else{
						deliverySlipId = "";
					}
					continue;
				}
			}
			// 配送業者入金データと送り状データを伝票番号を比較
			int comp = deliverySlipId.compareTo(invoiceDeliverySlipId);
			if( deliverySlipId.length() == 0){
				comp = 1;
			}
			if( invoiceDeliverySlipId.length() == 0){
				comp = -1;
			}
			if( comp < 0 ){
				depositIndex++;
				if( depositIndex < depositSize){
					deposit = depositList.get(depositIndex);
					deliverySlipId = deposit.deliverySlipId.trim();
					productPrice = deposit.productPrice.longValue();
					dataCategory = deposit.dataCategory;
					depositSlipId = deposit.depositSlipId;
				}else{
					deliverySlipId = "";
				}
				continue;
			}else if( comp > 0 ) {
				// 「送り状のみ」画面表示へ
				index++;
				if( index < invoiceSize){
					invoiceDataWork = invoiceDataWorkList.get(index);
					invoiceDeliverySlipId = invoiceDataWork.deliverySlipId;
					invoiceSalesSlipId = invoiceDataWork.invoiceSalesSlipId;
					salesSlipId = invoiceDataWork.salesSlipId;
					customerCode = invoiceDataWork.customerCode;

				}else{
					invoiceDeliverySlipId = "";
				}
				continue;
			}

			// 伝票番号が一致する
			// 売上伝票があるか？
			if( salesSlipId == null ){
				// 「関連売上なし」
				depositIndex++;
				if( depositIndex < depositSize){
					deposit = depositList.get(depositIndex);
					deliverySlipId = deposit.deliverySlipId.trim();
					productPrice = deposit.productPrice.longValue();
					dataCategory = deposit.dataCategory;
					depositSlipId = deposit.depositSlipId;
				}else{
					deliverySlipId = "";
				}
				index++;
				if( index < invoiceSize){
					invoiceDataWork = invoiceDataWorkList.get(index);
					invoiceDeliverySlipId = invoiceDataWork.deliverySlipId;
					invoiceSalesSlipId = invoiceDataWork.invoiceSalesSlipId;
					salesSlipId = invoiceDataWork.salesSlipId;
					customerCode = invoiceDataWork.customerCode;
				}else{
					invoiceDeliverySlipId = "";
				}
				continue;
			}
			// 配送業者入金関連テーブルに伝票番号がなければ入金伝票を作成
			if( depositSlipId == null ){
				 long price = productPrice;

				// 「新規登録」 入金伝票を作成し、配送業者入金関連テーブルに登録する
				// 得意先情報を取得
				 Customer customerData = customerService.findCustomerByCode(customerCode);

				 DepositSlipDto dto = new DepositSlipDto();
				 dto.fillList();

				 dto.status = Constants.STATUS_DEPOSIT_SLIP.PAID;
				 dto.depositDate = DF_YMD.format(new Date());
				 dto.inputPdate = DF_YMD.format(new Date());
			     // 仕入日から仕入年度、仕入月度、仕入年月度を取得
				 YmDto ymDto = ymService.getYm(dto.depositDate);
				 if( ymDto != null ){
					 dto.depositAnnual = ymDto.annual.toString();
					 dto.depositMonthly = ymDto.monthly.toString();
					 dto.depositYm = ymDto.ym.toString();
				 }

				 dto.userId =  this.userDto.userId;
				 dto.userName = this.userDto.nameKnj;
				 dto.depositAbstract = "";
				 dto.remarks = "";
				 if( customerData != null){
					 dto.customerCode = customerData.customerCode;
					 dto.customerName = customerData.customerName;

					 dto.cutoffGroup  = customerData.cutoffGroup;
					 dto.paybackCycleCategory = customerData.paybackCycleCategory;

					 // 顧客コードを指定して請求先を取得する
					 List<DeliveryAndPre>baList =  deliveryService.searchDeliveryByCompleteCustomerCode(customerData.customerCode);
					 DeliveryAndPre baInfo = baList.get(0);

					 dto.baCode = baInfo.deliveryCode;
					 dto.baName = baInfo.deliveryName;
					 dto.baKana = baInfo.deliveryKana;
					 dto.baOfficeName = baInfo.deliveryOfficeName;
					 dto.baOfficeKana = baInfo.deliveryOfficeKana;
					 dto.baDeptName = baInfo.deliveryDeptName;
					 dto.baZipCode = baInfo.deliveryZipCode;
					 dto.baAddress1 = baInfo.deliveryAddress1;
					 dto.baAddress2 = baInfo.deliveryAddress2;
					 dto.baPcName = baInfo.deliveryPcName;
					 dto.baPcKana = baInfo.deliveryPcKana;
					 dto.baPcPre = baInfo.categoryCodeName;
					 dto.baPcPreCatrgory = baInfo.deliveryPcPreCategory;
					 dto.baTel = baInfo.deliveryTel;
					 dto.baFax = baInfo.deliveryFax;
					 dto.baEmail = baInfo.deliveryEmail;
					 dto.baUrl = baInfo.deliveryUrl;

					 dto.salesCmCategory = customerData.salesCmCategory;
					 dto.taxFractCategory = customerData.taxFractCategory;
					 dto.priceFractCategory = customerData.priceFractCategory;
				 }
				 dto.depositCategory = CategoryTrns.DEPOSIT_CATEGORY_CASH_ON_DELIVERY;	// 代引き

				 dto.depositTotal = Long.valueOf(price).toString();

				 dto.salesSlipId = invoiceSalesSlipId;
				 dto.depositMethodTypeCategory = CategoryTrns.DEPOSIT_METHOD_DELIVERY;


				 // 明細行
				 depositLineNo++;
				 DepositLineDto line = new DepositLineDto();
				 line.status = Constants.STATUS_DEPOSIT_LINE.PAID;
				 line.lineNo = String.valueOf(depositLineNo);
				 line.depositCategory = dto.depositCategory;

				 line.price = Long.valueOf(price).toString();
				 line.bankId = bankInfo.bankId.toString();
				 line.bankInfo = bankInfo.bankName + " " + bankInfo.storeName;

				 dto.getLineDtoList().add(line);
				 dto.removeBlankLine();

				 // 登録
				try {
					dto.depositTotal = Long.valueOf(price).toString();
					Long newSlipId = insertDeposit(dto, deliverySlipId, dataCategory);
					if( newDepositSlipIdStr.length() == 0){
						newDepositSlipIdStr = newSlipId.toString();
					}else{
						newDepositSlipIdStr = newDepositSlipIdStr + "," + newSlipId.toString();
					}
					depositLineNo = 0;
				} catch (ServiceException e) {
					throw(e);
				} catch (UnabledLockException e) {
					throw(e);
				} catch (Exception e) {
					throw(e);
				}

			}

			// 「登録済」
			depositIndex++;
			if( depositIndex < depositSize){
				deposit = depositList.get(depositIndex);
				deliverySlipId = deposit.deliverySlipId.trim();
				productPrice = deposit.productPrice.longValue();
				dataCategory = deposit.dataCategory;
				depositSlipId = deposit.depositSlipId;
			}else{
				deliverySlipId = "";
			}

			index++;
			if( index < invoiceSize){
				invoiceDataWork = invoiceDataWorkList.get(index);
				invoiceDeliverySlipId = invoiceDataWork.deliverySlipId;
				invoiceSalesSlipId = invoiceDataWork.invoiceSalesSlipId;
				salesSlipId = invoiceDataWork.salesSlipId;
				customerCode = invoiceDataWork.customerCode;
			}else{
				invoiceDeliverySlipId = "";
			}
		}
		return newDepositSlipIdStr;
	 }

	/**
	 * 配送業者入金関連テーブルにデータを登録します.
	 * @param dto 入金伝票DTO
	 * @param deliverySlipId 伝票番号
	 * @param dataCategory データ種類
	 * @return 新規に発番された入金伝票ID
	 * @throws Exception
	 */
	 private Long insertDeposit(DepositSlipDto dto, String deliverySlipId, String dataCategory) throws Exception {
		 Long newSlipId;
		 try {
			 depositSlipService.save(dto);
			 newSlipId = Long.valueOf(dto.getKeyValue());
			 depositLineService.save(dto, dto.getLineDtoList(), null);

			 // 配送業者入金関連テーブルに登録
			 DeliveryDepositRelDto rel = new DeliveryDepositRelDto();
			 rel.salesSlipId = dto.salesSlipId;
			 rel.depositSlipId = newSlipId.toString();
			 rel.deliverySlipId = deliverySlipId;
			 rel.dataCategory = dataCategory;

			 deliveryDepositRelService.insertRecord(rel);
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
	  * @param userId ユーザID
	  * @param newDepositSlipIdStr 新規に発番された入金伝票ID
	  * @return 取込結果リスト
	  * @throws ServiceException
	  */
	public List<ImportDeliveryDepositResultSortDto> getImportResultList(String userId ,String newDepositSlipIdStr )
			throws ServiceException
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
		List<ImportDeliveryDepositResultSortDto> resultList = new ArrayList<ImportDeliveryDepositResultSortDto>() ;

		// 配送業者入金データと送り状データを伝票番号で突合する
		List<DeliveryDepositWorkJoin> depositList = deliveryDepositWorkService.findDeliveryDepositWorkByUserId(userId);
		List<InvoiceDataWorkJoin> invoiceDataWorkList = invoiceDataWorkService.findInvoiceDataWorkByUserId(userId);
		int depositIndex = 0;
		int index = 0;

		int depositSize = depositList.size();
		int invoiceSize = invoiceDataWorkList.size();

		DeliveryDepositWorkJoin deposit;
		InvoiceDataWorkJoin invoiceDataWork ;

		String deliverySlipId = "";
		String deliveryData = "";
		long productPrice = 0;
		Integer relSalesSlipId = null;
		Integer depositSlipId = null;
		String dataCategory = "";
		long priceTotal = 0;

		// 日付の出力形式を設定
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		if ( depositSize > 0){
			deposit = depositList.get(depositIndex);
			deliverySlipId = deposit.deliverySlipId;
			if (deposit.deliveryDate!=null) {
				deliveryData = sdf.format(deposit.deliveryDate);
			}
			productPrice = deposit.productPrice.longValue();
			dataCategory = deposit.dataCategory;
			relSalesSlipId = deposit.salesSlipId;
			depositSlipId = deposit.depositSlipId;
			if(deposit.priceTotal == null ){
				priceTotal = 0;
			}else{
				priceTotal = deposit.priceTotal.longValue();
			}
		}

		String invoiceDeliverySlipId = "";
		Integer salesSlipId = null;
		String customerCode = "";
		if( invoiceSize > 0){
			invoiceDataWork = invoiceDataWorkList.get(index);
			invoiceDeliverySlipId = invoiceDataWork.deliverySlipId;
			salesSlipId = invoiceDataWork.salesSlipId;
			customerCode = invoiceDataWork.customerCode;
		}

		ImportDeliveryDepositResultSortDto  resultDto = new ImportDeliveryDepositResultSortDto();

		while ( depositIndex<depositSize ||  index<invoiceSize){
			// 配送業者入金データのデータ区分が「20（返品）」はエラーとしスキップする
			if( deliverySlipId.length() > 0){
				if( Constants.DELIVERY_DEPOSIT_CSV.DATA_CATEGORY_RETURN_GOODS.equals(dataCategory)){
					depositIndex++;
					if( depositIndex < depositSize){
						deposit = depositList.get(depositIndex);
						deliverySlipId = deposit.deliverySlipId;
						if (deposit.deliveryDate!=null) {
							deliveryData = sdf.format(deposit.deliveryDate);
						}
						productPrice = deposit.productPrice.longValue();
						dataCategory = deposit.dataCategory;
						relSalesSlipId = deposit.salesSlipId;
						depositSlipId = deposit.depositSlipId;
						if(deposit.priceTotal == null ){
							priceTotal = 0;
						}else{
							priceTotal = deposit.priceTotal.longValue();
						}
					}else{
						deliverySlipId = "";
					}
					continue;
				}
			}
			// 入金データと送り状データを伝票番号を比較
			int comp = deliverySlipId.compareTo(invoiceDeliverySlipId);
			if( deliverySlipId.length() == 0){
				comp = 1;
			}
			if( invoiceDeliverySlipId.length() == 0){
				comp = -1;
			}
			if( comp < 0 ){
				// 結果をセット
				resultDto = new ImportDeliveryDepositResultSortDto();
				resultDto.status = Constants.DELIVERY_DEPOSIT_CSV.STATUS_DEPOSIT_ONLY;
				resultDto.salesSlipId = "";
				resultDto.depositSlipId = "";
				resultDto.deliverySlipId = deliverySlipId;
				resultDto.customer = "";
				resultDto.deliveryDate = deliveryData;
				resultDto.productPrice = Long.valueOf(productPrice).toString();
				resultDto.salesMoney = "";
				resultList.add(resultDto);

				depositIndex++;
				if( depositIndex < depositSize){
					deposit = depositList.get(depositIndex);
					deliverySlipId = deposit.deliverySlipId.trim();
					productPrice = deposit.productPrice.longValue();
					relSalesSlipId = deposit.salesSlipId;
					depositSlipId = deposit.depositSlipId;
					if (deposit.deliveryDate!=null) {
						deliveryData = sdf.format(deposit.deliveryDate);
					}
					dataCategory = deposit.dataCategory;
					if(deposit.priceTotal == null ){
						priceTotal = 0;
					}else{
						priceTotal = deposit.priceTotal.longValue();
					}
				}else{
					deliverySlipId = "";
				}
				continue;
			}else if( comp > 0 ) {
				// 「送り状のみ」
				// 結果をセット
				resultDto = new ImportDeliveryDepositResultSortDto();
				resultDto.status = Constants.DELIVERY_DEPOSIT_CSV.STATUS_INVOICE_ONLY;
				resultDto.salesSlipId = "";
				resultDto.depositSlipId = "";
				resultDto.deliverySlipId = invoiceDeliverySlipId;
				resultDto.customer = "";
				resultDto.deliveryDate = "";
				resultDto.productPrice = "";
				resultDto.salesMoney = "";
				resultList.add(resultDto);

				index++;
				if( index < invoiceSize){
					invoiceDataWork = invoiceDataWorkList.get(index);
					invoiceDeliverySlipId = invoiceDataWork.deliverySlipId;
					salesSlipId = invoiceDataWork.salesSlipId;
					customerCode = invoiceDataWork.customerCode;

				}else{
					invoiceDeliverySlipId = "";
				}
				continue;
			}

			// 伝票番号が一致する
			// 売上伝票があるか？
			if( salesSlipId == null ){
				// 「関連売上なし」
				// 結果をセット
				resultDto = new ImportDeliveryDepositResultSortDto();
				resultDto.status = Constants.DELIVERY_DEPOSIT_CSV.STATUS_NOREL_SALES;
				resultDto.salesSlipId = "";
				resultDto.depositSlipId = "";
				resultDto.deliverySlipId = deliverySlipId;
				resultDto.customer = "";
				resultDto.deliveryDate = deliveryData;
				resultDto.productPrice = Long.valueOf(productPrice).toString();
				resultDto.salesMoney = "";
				resultList.add(resultDto);

				depositIndex++;
				if( depositIndex < depositSize){
					deposit = depositList.get(depositIndex);
					deliverySlipId = deposit.deliverySlipId.trim();
					productPrice = deposit.productPrice.longValue();
					relSalesSlipId = deposit.salesSlipId;
					depositSlipId = deposit.depositSlipId;
					if (deposit.deliveryDate!=null) {
						deliveryData = sdf.format(deposit.deliveryDate);
					}
					dataCategory = deposit.dataCategory;
					if(deposit.priceTotal == null ){
						priceTotal = 0;
					}else{
						priceTotal = deposit.priceTotal.longValue();
					}
				}else{
					deliverySlipId = "";
				}
				index++;
				if( index < invoiceSize){
					invoiceDataWork = invoiceDataWorkList.get(index);
					invoiceDeliverySlipId = invoiceDataWork.deliverySlipId;
					salesSlipId = invoiceDataWork.salesSlipId;
					customerCode = invoiceDataWork.customerCode;
				}else{
					invoiceDeliverySlipId = "";
				}
				continue;
			}

			// 配送業者入金関連テーブルに伝票番号がなければ入金伝票を作成
			// 登録済み
			// 得意先情報を取得
			Customer customerData = customerService.findCustomerByCode(customerCode);
			if( depositSlipId == null ){
				// 新規登録　こちらには来ないはず
				// 結果をセット
				resultDto = new ImportDeliveryDepositResultSortDto();
				resultDto.status = Constants.DELIVERY_DEPOSIT_CSV.STATUS_DEL;
				resultDto.salesMoney = "";
			}else{
				// 登録済み
				// 新規かどうか newDepositSlipIdMapに入金番号があれば新規
				resultDto = new ImportDeliveryDepositResultSortDto();
				Long key = Long.valueOf(depositSlipId.longValue());
				if( newDepositSlipIdMap != null && newDepositSlipIdMap.containsKey(key) ){
					// 新規
					resultDto.status = Constants.DELIVERY_DEPOSIT_CSV.STATUS_NEW;
					resultDto.salesMoney = Long.valueOf(priceTotal).toString();
				}else{
					// 登録済み
					resultDto.status = Constants.DELIVERY_DEPOSIT_CSV.STATUS_OLD;
					resultDto.salesMoney = "";
				}
			}

			// 結果をセット
			if(relSalesSlipId != null ){
				resultDto.salesSlipId = relSalesSlipId.toString();
			}else{
				resultDto.salesSlipId = "";
			}
			if( depositSlipId != null ){
				resultDto.depositSlipId = depositSlipId.toString();
			}else{
				resultDto.depositSlipId = "";
			}
			resultDto.deliverySlipId = deliverySlipId;
			resultDto.customer = "";
			if( customerData != null){
				resultDto.customer = customerData.customerName;
			}
			resultDto.deliveryDate = deliveryData;
			resultDto.productPrice =  Long.valueOf(productPrice).toString();
			resultList.add(resultDto);

			// 「登録済」
			depositIndex++;
			if( depositIndex < depositSize){
				deposit = depositList.get(depositIndex);
				deliverySlipId = deposit.deliverySlipId.trim();
				productPrice = deposit.productPrice.longValue();
				relSalesSlipId = deposit.salesSlipId;
				depositSlipId = deposit.depositSlipId;
				if (deposit.deliveryDate!=null) {
					deliveryData = sdf.format(deposit.deliveryDate);
				}
				dataCategory = deposit.dataCategory;
				if(deposit.priceTotal == null ){
					priceTotal = 0;
				}else{
					priceTotal = deposit.priceTotal.longValue();
				}
			}else{
				deliverySlipId = "";
			}

			index++;
			if( index < invoiceSize){
				invoiceDataWork = invoiceDataWorkList.get(index);
				invoiceDeliverySlipId = invoiceDataWork.deliverySlipId;
				salesSlipId = invoiceDataWork.salesSlipId;
				customerCode = invoiceDataWork.customerCode;
			}else{
				invoiceDeliverySlipId = "";
			}
		}
		return resultList;
	}

	/**
	 * 取込結果リストを返します.
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param userId ユーザID
	 * @param newDepositSlipIdStr 新規に発番された入金伝票ID
	 * @return 取込結果リスト
	 * @throws Exception
	 */
	public List<ImportDeliveryDepositResultDto> getImportResultList(String sortColumn,
														   boolean sortOrderAsc,
														   String userId ,
														   String newDepositSlipIdStr)
		throws Exception
	{
		List<ImportDeliveryDepositResultDto> resultSortList = new ArrayList<ImportDeliveryDepositResultDto>();

		try {
			List<ImportDeliveryDepositResultSortDto> resultList = getImportResultList(userDto.userId, newDepositSlipIdStr);

			ImportDeliveryDepositResultSortDto[] resultArray = new ImportDeliveryDepositResultSortDto[ resultList.size() ];

			int index = 0;
			// ソートする
			for (ImportDeliveryDepositResultSortDto result : resultList) {
				String key = "";
				if( Param.STATUS.equals(sortColumn)){
					key = result.status;
				}else if( Param.SALES_SLIP_ID.equals(sortColumn)){
					key = result.salesSlipId;
				}else if( Param.DEPOSIT_SLIP_ID.equals(sortColumn)){
					key = result.depositSlipId;
				}else if( Param.DELIVERY_SLIP_ID.equals(sortColumn)){
					key = result.deliverySlipId;
				}else if( Param.CUSTOMER.equals(sortColumn)){
					key = result.customer;
				}else if( Param.DELIVERY_DATE.equals(sortColumn)){
					key = result.deliveryDate;
				}else if( Param.PRODUCT_PRICE.equals(sortColumn)){
					key = result.productPrice;
				}else if( Param.SALES_MONY.equals(sortColumn)){
					key = result.salesMoney;
				}
				result.key = key;
				result.sortColumn = sortColumn;
				result.sortOrderAsc = sortOrderAsc;

				resultArray[index] = result;
				index++;
			}

			java.util.Arrays.sort( resultArray );

			for(int i = 0; i < resultArray.length; i++){
				ImportDeliveryDepositResultDto dto = resultArray[i];
				resultSortList.add(dto);
			}

			return resultSortList;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * 伝票番号より配送業者入金情報を取得します.
	 * @param SlipIdStr 伝票番号
	 * @return 配送業者入金情報
	 * @throws ServiceException
	 */
	public List<BeanMap> getDeliveryDepositList(String SlipIdStr) throws ServiceException {
		try {
			return deliveryDepositWorkService.findDeliveryDepositWorkBySlipId(SlipIdStr);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 伝票番号より送り状情報を取得します.
	 * @param deliverySlipId 伝票番号
	 * @return 送り状情報
	 * @throws ServiceException
	 */
	public List<BeanMap> getInvoiceDataList(String deliverySlipId) throws ServiceException {
		try {
			return invoiceDataWorkService.findInvoiceDataWorkBySlipId(deliverySlipId);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
