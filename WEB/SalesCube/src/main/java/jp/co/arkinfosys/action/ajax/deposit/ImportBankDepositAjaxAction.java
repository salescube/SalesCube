/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.deposit;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.CommonAjaxResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.deposit.DepositImportExcelDto;
import jp.co.arkinfosys.dto.deposit.ImportBankDepositResultDto;
import jp.co.arkinfosys.form.deposit.ImportBankDepositForm;
import jp.co.arkinfosys.service.deposit.ImportBankDepositService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 銀行入金データ取込画面の処理を実行するアクションクラスです.
 *
 * @author Ark Information Systems
 */
public class ImportBankDepositAjaxAction extends CommonAjaxResources {
	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String RESULT = "searchResultList.jsp";
		public static final String EXCEL = "excel.jsp";
	}

	/**
	 * 添付ファイル名指定のための定数
	 */
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String ATTACHMENT_FORMAT = "attachment; filename=\"%1$s\"";
	public static final String ATTACHMENT_ENCODE = "UTF-8";

	/**
	 * 添付ファイル名を定義するクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class AttachFileName {
		public static final String FILENAME01 = "IMP_BANK.xls";
	}


	@ActionForm
	@Resource
	public ImportBankDepositForm importBankDepositForm;

	@Resource
	public ImportBankDepositService importBankDepositService;

	/**
	 * 検索結果リスト
	 */
	public List<BeanMap> listExcel = null;		//EXCEL用検索結果リスト

	/**
	 * 検索結果列情報リスト
	 */
	public List<DepositImportExcelDto> colInfoList = null;

	/**
	 * 添付ファイル名
	 */
	public String attachFileName;

	/**
	 * 初期表示処理を行います.
	 * @return 遷移URI
	 */
	@Execute(validator = false)
	public String index() {
		return Mapping.RESULT;
	}
	/**
	 * 銀行入金データ取込み結果リストを取得します.
	 * @return 遷移URI
	 */
	@Execute(validator = false)
	public String search() throws Exception {
		try {
			// 結果の取得
			importBankDepositForm.searchResultList =
				importBankDepositService.getImportResultList(
					importBankDepositForm.sortColumn,
					importBankDepositForm.sortOrderAsc,
					userDto.userId,
					importBankDepositForm.newDepositSlipIdStr);

			importBankDepositForm.searchResultCount = importBankDepositForm.searchResultList.size();
			importBankDepositForm.linkInputDeposit = this.userDto.isMenuUpdate( Constants.MENU_ID.INPUT_DEPOSIT );


		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return Mapping.RESULT;
	}

	/**
	 * 銀行入金データ取込み結果リストをEXCELに出力します（全件）.
	 * @return 遷移URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excelAll() throws Exception {
		try {

			importBankDepositForm.selectCount = 0;
			exec_excel();

		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return Mapping.EXCEL;
	}

	/**
	 * 銀行入金データ取込み結果リストをEXCELに出力します（一致のみ）.
	 * @return 遷移URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excelOK() throws Exception {
		try {

			importBankDepositForm.selectCount = 1;
			exec_excel();

		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return Mapping.EXCEL;
	}

	/**
	 * 銀行入金データ取込み結果リストをEXCELに出力します（不一致のみ）.
	 * @return 遷移URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excelErr() throws Exception {
		try {

			importBankDepositForm.selectCount = 2;
			exec_excel();

		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return Mapping.EXCEL;
	}

	/**
	 * Excel出力処理を行います.
	 * @return
	 * @throws Exception
	 */
	private void exec_excel()
		throws Exception
	{
		try {
			// 突合結果を取得
			importBankDepositForm.sortColumn = "";
			importBankDepositForm.sortOrderAsc = true;
			importBankDepositForm.newDepositSlipIdStr = "";
			List<ImportBankDepositResultDto> searchAllResultList =
				importBankDepositService.getImportResultList(
					importBankDepositForm.sortColumn,
					importBankDepositForm.sortOrderAsc,
					userDto.userId,
					importBankDepositForm.newDepositSlipIdStr);

			listExcel = new ArrayList<BeanMap>();
			for(ImportBankDepositResultDto resultList : searchAllResultList){
				BeanMap resultMap = new BeanMap();

				resultMap.put("status", resultList.status);
				resultMap.put("depositSlipId", resultList.depositSlipId);
				resultMap.put("customer", resultList.customer);
				resultMap.put("paymentDate", resultList.paymentDate);
				resultMap.put("paymentName", resultList.paymentName);
				resultMap.put("lastBillPrice", resultList.lastBillPrice);
				resultMap.put("paymentPrice", resultList.paymentPrice);
				resultMap.put("diffPrice", resultList.diffPrice);
				resultMap.put("changeName", resultList.changeName);
				resultMap.put("afterChangeName", resultList.afterChangeName);

				if(resultList.status.equals(Constants.BANK_DEPOSIT_CSV.STATUS_OLD)||
					resultList.status.equals(Constants.BANK_DEPOSIT_CSV.STATUS_NEW)){
					// 登録済み、新規登録　は、一致件数
					if((importBankDepositForm.selectCount == 1)||(importBankDepositForm.selectCount == 0))
						listExcel.add(resultMap);
				}
				else{
					// それ以外は、不一致件数
					if((importBankDepositForm.selectCount == 2)||(importBankDepositForm.selectCount == 0))
						listExcel.add(resultMap);
				}
			}
			// 添付ファイル名
			attachFileName = AttachFileName.FILENAME01;

			// カラム定義
			colInfoList = new ArrayList<DepositImportExcelDto>();
			colInfoList.add(new DepositImportExcelDto("status",0,"labels.slipStatus.status"));
			colInfoList.add(new DepositImportExcelDto("depositSlipId",0,"labels.depositSlipId"));
			colInfoList.add(new DepositImportExcelDto("customer",0,"labels.customer"));
			colInfoList.add(new DepositImportExcelDto("paymentDate",0,"labels.bank.deposit.PaymentDate"));
			colInfoList.add(new DepositImportExcelDto("paymentName",0,"labels.bank.deposit.PaymentName"));
			colInfoList.add(new DepositImportExcelDto("lastBillPrice",1,"labels.lastBillPrice"));
			colInfoList.add(new DepositImportExcelDto("paymentPrice",1,"labels.bank.deposit.PaymentPrice"));
			colInfoList.add(new DepositImportExcelDto("diffPrice",1,"labels.diffPrice"));
			colInfoList.add(new DepositImportExcelDto("changeName",0,"labels.changeName"));
			colInfoList.add(new DepositImportExcelDto("afterChangeName",0,"labels.changeName"));

			// 添付ファイル名設定
			String attach = String.format(ATTACHMENT_FORMAT, URLEncoder.encode(attachFileName,ATTACHMENT_ENCODE));
			httpResponse.setHeader(CONTENT_DISPOSITION, attach);

		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
