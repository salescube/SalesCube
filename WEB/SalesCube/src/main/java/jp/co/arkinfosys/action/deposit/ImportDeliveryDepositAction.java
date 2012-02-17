/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.deposit;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractXSVUploadAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.ShipDepositWorkDto;
import jp.co.arkinfosys.dto.ShipWorkDto;
import jp.co.arkinfosys.dto.deposit.ImportDeliveryDepositResultDto;
import jp.co.arkinfosys.form.deposit.ImportDeliveryDepositForm;
import jp.co.arkinfosys.service.InvoiceDataWorkService;
import jp.co.arkinfosys.service.DeliveryDepositWorkService;
import jp.co.arkinfosys.service.deposit.ImportDeliveryDepositService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 配送業者入金データの取込処理を行うアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ImportDeliveryDepositAction extends AbstractXSVUploadAction {
	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "importDeliveryDeposit.jsp";
	}

	@ActionForm
	@Resource
	public ImportDeliveryDepositForm importDeliveryDepositForm;

	@Resource
	private DeliveryDepositWorkService deliveryDepositWorkService;

	@Resource
	private InvoiceDataWorkService invoiceDataWorkService;

	@Resource
	private ImportDeliveryDepositService importDeliveryDepositService;

	public boolean invoiceFlag;
	public boolean bNoData;

	/**
	 * 日付チェック用フォーマット
	 */
	private final String DATE_FORMAT = "yyyyMMdd";

	/**
	 * 初期表示処理を行います.
	 *
	 * @return 配送業者入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		try {
			this.init();
			importDeliveryDepositForm.newDepositSlipIdStr = "";
			importDeliveryDepositForm.searchResultList =
				importDeliveryDepositService.getImportResultList(
					importDeliveryDepositForm.sortColumn,
					importDeliveryDepositForm.sortOrderAsc,
					userDto.userId,
					importDeliveryDepositForm.newDepositSlipIdStr);
			importDeliveryDepositForm.searchResultCount = importDeliveryDepositForm.searchResultList.size();
			importDeliveryDepositForm.dispResultCount =importDeliveryDepositForm.searchResultCount;

			
			setListCount();

		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}

		return ImportDeliveryDepositAction.Mapping.INPUT;
	}

	/**
	 * 配送業者入金データ取込画面を再表示します.
	 * @return 配送業者入金データ取込画面のパス
	 */
	@Execute(validator = false, redirect = false)
	public String reinput(){
		return ImportDeliveryDepositAction.Mapping.INPUT;
	}

	/**
	 * 配送業者入金データを取込みます.
	 *
	 * @return 配送業者入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "reinput")
	public String upload() throws Exception {
		

		try {
			String userId = this.userDto.userId;


			invoiceFlag = false;
			
			deliveryDepositWorkService.deleteByUserId(userId);

			
			if (this.importDeliveryDepositForm.infoBoxFile.getFileSize() == 0) {
				
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.delivery.deposit.csv.format"));
				ActionMessagesUtil.addErrors(super.httpRequest, errors);
				return ImportDeliveryDepositAction.Mapping.INPUT;
			}

			bNoData = true;
			this.readXSV(this.importDeliveryDepositForm.infoBoxFile);

			if( this.isReadStop ){
				
				deliveryDepositWorkService.deleteByUserId(userId);
				invoiceDataWorkService.deleteByUserId(userId);
				return ImportDeliveryDepositAction.Mapping.INPUT;
			}
			
			if( bNoData ){
				
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.delivery.deposit.csv.format"));
				ActionMessagesUtil.addErrors(super.httpRequest, errors);
				return ImportDeliveryDepositAction.Mapping.INPUT;
			}

			
			invoiceFlag = true;
			invoiceDataWorkService.deleteByUserId(userId);
			
			if (this.importDeliveryDepositForm.invoiceFile.getFileSize() == 0) {
				
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.delivery.invoice.data.format"));
				ActionMessagesUtil.addErrors(super.httpRequest, errors);
				return ImportDeliveryDepositAction.Mapping.INPUT;
			}

			bNoData = true;
			this.readXSV(this.importDeliveryDepositForm.invoiceFile);

			if( this.isReadStop){
				
				deliveryDepositWorkService.deleteByUserId(userId);
				invoiceDataWorkService.deleteByUserId(userId);
				return ImportDeliveryDepositAction.Mapping.INPUT;
			}
			
			if( bNoData ){
				
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.delivery.invoice.data.format"));
				ActionMessagesUtil.addErrors(super.httpRequest, errors);
				return ImportDeliveryDepositAction.Mapping.INPUT;
			}
			

			
			List<ActionMessages> errorList = new ArrayList<ActionMessages>();
			importDeliveryDepositForm.newDepositSlipIdStr= importDeliveryDepositService.insertDeliveryDeposit( userId, importDeliveryDepositForm.bankId, errorList);

			
			importDeliveryDepositForm.searchResultList =
				importDeliveryDepositService.getImportResultList(
					importDeliveryDepositForm.sortColumn,
					importDeliveryDepositForm.sortOrderAsc,
					userDto.userId,
					importDeliveryDepositForm.newDepositSlipIdStr);

			importDeliveryDepositForm.searchResultCount = importDeliveryDepositForm.searchResultList.size();

			importDeliveryDepositForm.linkInputDeposit = this.userDto.isMenuUpdate( Constants.MENU_ID.INPUT_DEPOSIT );
			importDeliveryDepositForm.linkInputSales = this.userDto.isMenuUpdate( Constants.MENU_ID.INPUT_SALES);

			
			
			
			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("infos.import"));
			
			ActionMessagesUtil.addMessages(this.httpRequest, this.messages);
			for (ActionMessages errors : errorList) {
				ActionMessagesUtil.addErrors(super.httpRequest, errors);
			}

			importDeliveryDepositForm.dispResultCount =importDeliveryDepositForm.searchResultCount;

			
			setListCount();

		} catch (UnsupportedEncodingException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.file.encoding"));
			ActionMessagesUtil.addErrors(super.httpRequest, errors);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		return ImportDeliveryDepositAction.Mapping.INPUT;
	}

	/**
	 * 初期化処理です.
	 *
	 * @return 配送業者入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		try {
			this.init();
			importDeliveryDepositForm.newDepositSlipIdStr = "";
			importDeliveryDepositForm.searchResultList =
				importDeliveryDepositService.getImportResultList(
					importDeliveryDepositForm.sortColumn,
					importDeliveryDepositForm.sortOrderAsc,
					userDto.userId,
					importDeliveryDepositForm.newDepositSlipIdStr);
			importDeliveryDepositForm.searchResultCount = importDeliveryDepositForm.searchResultList.size();
			importDeliveryDepositForm.dispResultCount = importDeliveryDepositForm.searchResultCount;	

			
			setListCount();

		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}

		return ImportDeliveryDepositAction.Mapping.INPUT;
	}

	/**
	 * 画面初期設定です.
	 */
	private void init() throws ServiceException {
		invoiceFlag = false;
		importDeliveryDepositForm.sortColumn = "status";
		importDeliveryDepositForm.sortOrderAsc = false;

		importDeliveryDepositForm.linkInputDeposit = this.userDto.isMenuUpdate( Constants.MENU_ID.INPUT_DEPOSIT );
		importDeliveryDepositForm.linkInputSales = this.userDto.isMenuUpdate( Constants.MENU_ID.INPUT_SALES);
	}

	/**
	 * 読み込み行の処理です.<br>
	 * 送り状データフラグ(invoiceFlag)の値に応じて以下のいずれかのデータを取込みます。<br>
	 * ・配送業者入金データ<br>
	 * ・送り状データ
	 *
	 * @param index 行番号
	 * @param line 未使用
	 * @param values データ配列（１行分）
	 * @throws Exception
	 */
	@Override
	protected void processLine(int index, String line, String[] values)
			throws Exception {
		if (index == 1) {
			
			return;
		}
		if (!invoiceFlag) {
			
			ActionMessages errors = new ActionMessages();
			
			if (values == null
					|| values.length != Constants.DELIVERY_DEPOSIT_CSV.DEPOSIT_COLUMN_COUNT) {
				this.isReadStop = true;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.delivery.deposit.csv.format"));
				ActionMessagesUtil.addErrors(super.httpRequest, errors);
				return;
			}
			String userId = this.userDto.userId;

			ShipDepositWorkDto inDto = new ShipDepositWorkDto();
			
			inDto.userId = userId;
			inDto.paymentCategory = values[0];
			inDto.customerCode = values[1];
			inDto.deliverySlipId = values[2];
			inDto.dataCategory = values[3];
			inDto.changeCount = values[4];
			inDto.serviceCategory = values[5];
			inDto.settleCategory = values[6];
			inDto.deliveryDate = values[7];
			if (!this.isValidDate(DATE_FORMAT, inDto.deliveryDate)) {
				inDto.deliveryDate = null;
			}
			inDto.productPrice = values[8];
			inDto.codPrice = values[9];
			inDto.servicePrice = values[10];
			inDto.splitPrice = values[11];
			inDto.stampPrice = values[12];
			inDto.rgDate = values[13];
			if (!this.isValidDate(DATE_FORMAT,inDto.rgDate)) {
				inDto.rgDate = null;
			}
			inDto.rgSlipId = values[14];

			
			deliveryDepositWorkService.insertRecord(inDto);
			bNoData = false;
		} else {
			

			
			ActionMessages errors = new ActionMessages();
			
			if (values == null
					|| values.length != Constants.DELIVERY_DEPOSIT_CSV.INVOICE_COLUMN_COUNT) {
				this.isReadStop = true;
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.delivery.invoice.data.format"));
				ActionMessagesUtil.addErrors(super.httpRequest, errors);
				return;
			}

			String userId = this.userDto.userId;

			ShipWorkDto inDto = new ShipWorkDto();
			inDto.userId = userId;
			inDto.customerCode = values[0];
			inDto.siType = values[1];
			inDto.cool = values[2];
			inDto.deliverySlipId = values[3];
			inDto.shipDate = values[4];
			if (inDto.shipDate.length() == 0 || "0".equals(inDto.shipDate)) {
				inDto.shipDate = null;
			}
			inDto.deliveryDate = values[5];
			if (inDto.deliveryDate.length() == 0
					|| "0".equals(inDto.deliveryDate)) {
				inDto.deliveryDate = null;
			}
			inDto.timeZone = values[6];
			inDto.deliveryCode = values[7];
			inDto.deliveryTel = values[8];
			inDto.deliveryTel2 = values[9];
			inDto.deliveryZipCode = values[10];
			inDto.deliveryAddress = values[11];
			inDto.deliveryAddress2 = values[12];
			inDto.deliveryOffice1 = values[13];
			inDto.deliveryOffice2 = values[14];
			inDto.deliveryName = values[15];
			inDto.deliveryKana = values[16];
			inDto.deliveryPre = values[17];
			inDto.ownerCode = values[18];
			inDto.ownerTel = values[19];
			inDto.ownerTel2 = values[20];
			inDto.ownerZipCode = values[21];
			inDto.ownerAddress = values[22];
			inDto.ownerAddress2 = values[23];
			inDto.ownerName = values[24];
			inDto.ownerKana = values[25];
			inDto.productCode1 = values[26];
			inDto.productName1 = values[27];
			inDto.productCode2 = values[28];
			inDto.productName2 = values[29];
			inDto.handle1 = values[30];
			inDto.handle2 = values[31];
			inDto.salesSlipId = values[32];
			inDto.collectPrice = values[33];
			inDto.ctaxPrice = values[34];
			inDto.layaway = values[35];
			inDto.officeCode = values[36];
			inDto.printCnt = values[37];
			inDto.numDispFlag = values[38];
			inDto.baCode = values[39];
			inDto.baType = values[40];
			inDto.fareNo = values[41];
			inDto.paymentSet = values[42];
			inDto.paymentNo = values[43];
			inDto.paymentNo1 = values[44];
			inDto.paymentNo2 = values[45];
			inDto.paymentNo3 = values[46];
			inDto.emailUse = values[47];
			inDto.emailAddress = values[48];
			inDto.machineType = values[49];
			inDto.mailMessage = values[50];
			inDto.deliveryEmailUse = values[51];
			inDto.deliveryEmailAddress = values[52];
			inDto.deliveryEmailMessage = values[53];
			inDto.apsUse = values[54];
			inDto.qrPrintFlg = values[55];
			inDto.apsBillPrice = values[56];
			inDto.apsCtaxPrice = values[57];
			inDto.apsZipCode = values[58];
			inDto.apsAddress = values[59];
			inDto.apsAddress2 = values[60];
			inDto.apsOffice1 = values[61];
			inDto.apsOffice2 = values[62];
			inDto.apsName = values[63];
			inDto.apsKana = values[64];
			inDto.apsQname = values[65];
			inDto.apsQzipCode = values[66];
			inDto.apsQaddress = values[67];
			inDto.apsQaddress2 = values[68];
			inDto.apsQtel = values[69];
			inDto.apsNo = values[70];
			inDto.apsProductName = values[71];
			inDto.apsRemark = values[72];

			
			invoiceDataWorkService.insertRecord(inDto);
			bNoData = false;
		}

	}

	/**
	 * 配送業者入金データファイルの区切り文字を取得します.
	 * @return 区切り文字(カンマ)
	 */
	@Override
	protected String getSeparator() {
		return SEPARATOR.COMMA;
	}

	/**
	 * 処理結果の各種件数を設定します.
	 */
	
	public void setListCount(){
		
		int iOK = 0;
		int iNG = 0;
		int iEtc = 0;
		for(ImportDeliveryDepositResultDto resultList : importDeliveryDepositForm.searchResultList){

			if(resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_INVOICE_ONLY)){
				
				iEtc++;
			}
			else if(resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_OLD)||
					resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_NEW)){
				// 
				iOK++;
			}
			else{
				
				iNG++;
			}
		}

		importDeliveryDepositForm.importOKCount = iOK;
		importDeliveryDepositForm.importNGCount = iNG;
		importDeliveryDepositForm.importEtcCount = iEtc;
		importDeliveryDepositForm.selectCount = 0;


	}

	/**
	 * 検索結果を再表示します（登録済のみ）.
	 *
	 * @return 配送業者入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String resetListOK() throws Exception {

		
		importDeliveryDepositForm.selectCount = 1;

		String ret =  resetList(1);
		importDeliveryDepositForm.dispResultCount = importDeliveryDepositForm.importOKCount;	
		return ret;

	}

	/**
	 * 検索結果を再表示します（エラーのみ）.
	 *
	 * @return 配送業者入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String resetListErr() throws Exception {
		
		importDeliveryDepositForm.selectCount = 2;
		String ret = resetList(2);
		importDeliveryDepositForm.dispResultCount = importDeliveryDepositForm.importNGCount;	
		return ret;
	}

	/**
	 * 検索結果を再表示します（その他のみ）.
	 *
	 * @return 配送業者入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String resetListEtc() throws Exception {
		
		importDeliveryDepositForm.selectCount = 3;
		String ret =  resetList(3);
		importDeliveryDepositForm.dispResultCount = importDeliveryDepositForm.importEtcCount;	
		return ret;
	}

	/**
	 * 検索結果を再表示します（全件）.
	 *
	 * @return 配送業者入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String resetListAll() throws Exception {
		
		importDeliveryDepositForm.selectCount = 0;
		String ret =  resetList(0);
		importDeliveryDepositForm.dispResultCount = importDeliveryDepositForm.searchResultCount;	
		return ret;
	}

	/**
	 * 検索結果再表示用のリストを作成します.
	 *
	 * @param iList 0:全件　1: 一致　2: 不一致
	 * @return 入金データ取込画面のパス
	 * @throws Exception
	 */
	public String resetList(int iList) throws Exception {

		try {
			this.init();
			importDeliveryDepositForm.newDepositSlipIdStr = "";
			List<ImportDeliveryDepositResultDto> searchAllResultList =
				importDeliveryDepositService.getImportResultList(
					importDeliveryDepositForm.sortColumn,
					importDeliveryDepositForm.sortOrderAsc,
					userDto.userId,
					importDeliveryDepositForm.newDepositSlipIdStr);

			int iOK = 0;
			int iNG = 0;
			int iEtc = 0;
			importDeliveryDepositForm.searchResultList = new ArrayList<ImportDeliveryDepositResultDto>();

			for(ImportDeliveryDepositResultDto resultList : searchAllResultList){

				if(resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_INVOICE_ONLY)){
					
					iEtc++;
					if((iList == 0)||(iList == 3))
						importDeliveryDepositForm.searchResultList.add(resultList);
				}
				else if(resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_OLD)||
						resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_NEW)){
					// 
					iOK++;
					if((iList == 0)||(iList == 1))
						importDeliveryDepositForm.searchResultList.add(resultList);
				}
				else{
					
					iNG++;
					if((iList == 0)||(iList == 2))
						importDeliveryDepositForm.searchResultList.add(resultList);
				}
			}

			importDeliveryDepositForm.searchResultCount = searchAllResultList.size();
			importDeliveryDepositForm.importOKCount = iOK;
			importDeliveryDepositForm.importNGCount = iNG;
			importDeliveryDepositForm.importEtcCount = iEtc;

		} catch (ServiceException e) {
			e.printStackTrace();
			super.errorLog(e);
			throw new ServiceException(e);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return ImportDeliveryDepositAction.Mapping.INPUT;
	}

	/**
	 * 文字列が指定された日付型か判定する。
	 * @param fmt 日付型
	 * @param src 文字列
	 * @return true:文字列が日付型である false:文字列は日付型ではない
	 */
	private boolean isValidDate(String fmt, String src) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			sdf.setLenient(false);
			Date dt = sdf.parse(src);
			return (dt!=null);
		}
		catch (Exception e) {
			
		}
		return false;
	}

}
