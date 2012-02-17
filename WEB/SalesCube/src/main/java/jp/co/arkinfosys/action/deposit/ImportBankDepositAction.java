/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.deposit;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractXSVUploadAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.dto.deposit.BankDepositWorkDto;
import jp.co.arkinfosys.dto.deposit.ImportBankDepositResultDto;
import jp.co.arkinfosys.form.deposit.ImportBankDepositForm;
import jp.co.arkinfosys.service.BankDepositWorkService;
import jp.co.arkinfosys.service.deposit.ImportBankDepositService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 銀行入金データの取込処理を行うアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ImportBankDepositAction extends AbstractXSVUploadAction {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "importBankDeposit.jsp";
	}

	@ActionForm
	@Resource
	public ImportBankDepositForm importBankDepositForm;

	@Resource
	public BankDepositWorkService bankDepositWorkService;

	@Resource
	public ImportBankDepositService importBankDepositService;

	private List<BankDepositWorkDto> dtoList =  new ArrayList<BankDepositWorkDto>();

	/**
	 * デフォルトのコンストラクタです.
	 */
	public ImportBankDepositAction() {
		
		super.setCharacterSet(Constants.CHARSET.WINDOWS31J);
	}


	/**
	 * 初期表示処理を行います.
	 *
	 * @return 銀行入金データ取込画面のパス
	 * @exception Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		try {
			this.init();
			importBankDepositForm.newDepositSlipIdStr = "";
			
			importBankDepositForm.searchResultList =
				importBankDepositService.getImportResultList(
					importBankDepositForm.sortColumn,
					importBankDepositForm.sortOrderAsc,
					userDto.userId,
					importBankDepositForm.newDepositSlipIdStr);

			importBankDepositForm.searchResultCount = importBankDepositForm.searchResultList.size();
			importBankDepositForm.dispResultCount =importBankDepositForm.searchResultCount;

			
			setListCount();

		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}

		return ImportBankDepositAction.Mapping.INPUT;
	}

	/**
	 * 初期化処理です.
	 *
	 * @return 銀行入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		try {
			this.init();
			importBankDepositForm.newDepositSlipIdStr = "";
			
			importBankDepositForm.searchResultList =
				importBankDepositService.getImportResultList(
					importBankDepositForm.sortColumn,
					importBankDepositForm.sortOrderAsc,
					userDto.userId,
					importBankDepositForm.newDepositSlipIdStr);

			importBankDepositForm.searchResultCount = importBankDepositForm.searchResultList.size();
			importBankDepositForm.dispResultCount =importBankDepositForm.searchResultCount;

			
			setListCount();

		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}

		return ImportBankDepositAction.Mapping.INPUT;
	}

	/**
	 * 画面初期設定です.
	 * @throws ServiceException
	 */
	private void init() throws ServiceException {
		importBankDepositForm.csvFile = null;
		importBankDepositForm.sortColumn = "status";
		importBankDepositForm.sortOrderAsc = true;

		importBankDepositForm.linkInputDeposit = this.userDto.isMenuUpdate( Constants.MENU_ID.INPUT_DEPOSIT );
	}


	/**
	 * 銀行入金データ画面を再表示します.
	 * @return 銀行入金データ取込画面のパス
	 */
	@Execute(validator = false, redirect = false)
	public String reinput(){
		return ImportBankDepositAction.Mapping.INPUT;
	}

	/**
	 * 銀行入金データを取込みます.
	 *
	 * @return 銀行入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "reinput")
	public String upload() throws Exception {
		try {
			
			bankDepositWorkService.deleteByUserId(userDto.userId);
			
			readXSV(importBankDepositForm.csvFile);
			
			if(messages.size() != 0) {
				
				ActionMessagesUtil.addErrors(super.httpSession, messages);
				return "?redirect=true";
			}

			
			for(BankDepositWorkDto dto : dtoList) {
				bankDepositWorkService.insertRecord(dto);
			}
			
			
			importBankDepositForm.newDepositSlipIdStr = importBankDepositService.insertBankDeposit(userDto.userId, importBankDepositForm.bankId);
			
			importBankDepositForm.searchResultList =
				importBankDepositService.getImportResultList(
					importBankDepositForm.sortColumn,
					importBankDepositForm.sortOrderAsc,
					userDto.userId,
					importBankDepositForm.newDepositSlipIdStr);

			importBankDepositForm.searchResultCount = importBankDepositForm.searchResultList.size();

			importBankDepositForm.linkInputDeposit = this.userDto.isMenuUpdate( Constants.MENU_ID.INPUT_DEPOSIT );
			importBankDepositForm.dispResultCount = importBankDepositForm.searchResultCount;

			
			setListCount();

		} catch (UnsupportedEncodingException e) {
			addError(new ActionMessage("errors.file.encoding"));
			ActionMessagesUtil.addErrors(super.httpSession, messages);
		} catch (ServiceException e) {
			super.errorLog(e);

			
			if(e.isStopOnError()) {
				
				throw e;
			}
		}

		return ImportBankDepositAction.Mapping.INPUT;
	}

	/**
	 * 読み込み行の処理です.<br>
	 * 取り込んだ値をチェックして、エラーがなければInsert用のリストに追加します.
	 * @param index 行番号
	 * @param line 未使用
	 * @param values 銀行入金データ配列（１行分）
	 * @throws Exception
	 */
	@Override
	protected void processLine(int index, String line, String[] values)
			throws Exception {

		ActionMessage error;

		
		if(isErrorsMax()) {
			return;
		}

		
		if (values == null
				|| values.length != Constants.BANK_DEPOSIT_CSV.DEPOSIT_COLUMN_COUNT) {
			addError("errors.line.onlineorder.format",
					new Object[] { index, MessageResourcesUtil.getMessage("errors.onlineorder.reason.column") });
			return;
		}

		BankDepositWorkDto inDto = new BankDepositWorkDto();
		
		inDto.lineNo = String.valueOf(index);
		inDto.userId = userDto.userId;
		inDto.column1 = values[0];
		inDto.paymentDate = values[1];
		inDto.column2 = values[2];
		inDto.paymentType = values[3];
		inDto.paymentName = values[4];
		inDto.paymentPrice = values[5];

		

		
		error = ValidateUtil.dateType(inDto.paymentDate, Constants.FORMAT.DATE_DOT, false, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.bank.deposit.reason.paymentDate") });
		if(error != null) {
			addError(error);
		}

		
		error = ValidateUtil.integerType(inDto.paymentPrice, "errors.line.onlineorder.format",
				new Object[] { index, MessageResourcesUtil.getMessage("errors.bank.deposit.reason.paymentPrice") });
		if(error != null) {
			addError(error);
		}

		
		dtoList.add(inDto);
	}

	/**
	 * 銀行入金データファイルの区切り文字を取得します.
	 * @return 区切り文字(カンマ)
	 */
	@Override
	protected String getSeparator() {
		return SEPARATOR.COMMA;
	}

	/**
	 * 検索結果を再表示します（一致のみ）.
	 *
	 * @return 銀行入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String resetListOK() throws Exception {
		importBankDepositForm.selectCount = 1;

		String ret =  resetList(1);
		importBankDepositForm.dispResultCount = importBankDepositForm.importOKCount;	
		return ret;
	}

	/**
	 * 検索結果を再表示します（不一致のみ）.
	 *
	 * @return 銀行入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String resetListNG() throws Exception {
		importBankDepositForm.selectCount = 2;
		String ret =  resetList(2);
		importBankDepositForm.dispResultCount = importBankDepositForm.importNGCount;	
		return ret;
	}

	/**
	 * 検索結果を再表示します（全て）.
	 *
	 * @return 銀行入金データ取込画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String resetListAll() throws Exception {
		importBankDepositForm.selectCount = 0;
		String ret =  resetList(0);
		importBankDepositForm.dispResultCount = importBankDepositForm.searchResultCount;	
		return ret;
	}

	/**
	 * 検索結果再表示用のリストを作成します.
	 *
	 * @param iList 0:全件　1: 一致　2: 不一致
	 * @return 銀行入金データ取込画面のパス
	 * @throws Exception
	 */
	public String resetList(int iList) throws Exception {
		try {
			this.init();
			importBankDepositForm.newDepositSlipIdStr = "";
			
			List<ImportBankDepositResultDto> searchAllResultList =importBankDepositService.getImportResultList(importBankDepositForm.sortColumn,
					importBankDepositForm.sortOrderAsc,
					userDto.userId,
					importBankDepositForm.newDepositSlipIdStr);

			
			int iOK = 0;
			int iNG = 0;
			importBankDepositForm.searchResultList = new ArrayList<ImportBankDepositResultDto>();
			for(ImportBankDepositResultDto resultList : searchAllResultList) {

				if(resultList.status.equals(Constants.BANK_DEPOSIT_CSV.STATUS_OLD)||
					resultList.status.equals(Constants.BANK_DEPOSIT_CSV.STATUS_NEW)){
					
					iOK++;
					if((iList == 1)||(iList == 0))
						importBankDepositForm.searchResultList.add(resultList);
				}
				else{
					
					iNG++;
					if((iList == 2)||(iList == 0))
						importBankDepositForm.searchResultList.add(resultList);
				}
			}

			importBankDepositForm.searchResultCount = searchAllResultList.size();

			importBankDepositForm.importOKCount = iOK;
			importBankDepositForm.importNGCount = iNG;

		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}

		return ImportBankDepositAction.Mapping.INPUT;
	}


	/**
	 * 処理結果の各種件数を設定します.
	 */
	public void setListCount(){
		
		int iOK = 0;
		int iNG = 0;
		for(ImportBankDepositResultDto resultList : importBankDepositForm.searchResultList) {

			if(resultList.status.equals(Constants.BANK_DEPOSIT_CSV.STATUS_OLD)||
				resultList.status.equals(Constants.BANK_DEPOSIT_CSV.STATUS_NEW)){
				
				iOK++;
			}
			else{
				
				iNG++;
			}
		}
		importBankDepositForm.importOKCount = iOK;
		importBankDepositForm.importNGCount = iNG;
		importBankDepositForm.selectCount = 0;

	}
}