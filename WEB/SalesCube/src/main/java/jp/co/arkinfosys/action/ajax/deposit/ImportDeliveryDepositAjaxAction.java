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
import jp.co.arkinfosys.dto.deposit.ImportDeliveryDepositResultDto;
import jp.co.arkinfosys.form.deposit.ImportDeliveryDepositForm;
import jp.co.arkinfosys.service.deposit.ImportDeliveryDepositService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 配送業者入金データ取込画面の処理を実行するアクションクラスです.
 *
 * @author Ark Information Systems
 */
public class ImportDeliveryDepositAjaxAction extends CommonAjaxResources {

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
		public static final String FILENAME01 = "IMP_DELIVERY.xls";
	}

	@ActionForm
	@Resource
	private ImportDeliveryDepositForm importDeliveryDepositForm;

	@Resource
	private ImportDeliveryDepositService importDeliveryDepositService;

	/**
	 * 検索結果リスト
	 */
	public List<BeanMap> importResultList = null;		//EXCEL用検索結果リスト

	/**
	 * 検索結果列情報リスト
	 */
	public List<DepositImportExcelDto> columnInfoList = null;

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
	 * 配送業者入金データ取込み結果リストを取得します.
	 * @return 遷移URI
	 */
	@Execute(validator = false)
	public String search() throws Exception {
		try {

			importDeliveryDepositForm.searchResultList =
				importDeliveryDepositService.getImportResultList(importDeliveryDepositForm.sortColumn,
														importDeliveryDepositForm.sortOrderAsc,
														userDto.userId,
														importDeliveryDepositForm.newDepositSlipIdStr);
			// 件数
			importDeliveryDepositForm.searchResultCount = importDeliveryDepositForm.searchResultList.size();

			importDeliveryDepositForm.linkInputDeposit = this.userDto.isMenuUpdate( Constants.MENU_ID.INPUT_DEPOSIT );
			importDeliveryDepositForm.linkInputSales = this.userDto.isMenuUpdate( Constants.MENU_ID.INPUT_SALES);

		} catch (ServiceException e) {
			super.errorLog(e);

			// システム例外として処理する
			super.writeSystemErrorToResponse();
			return null;
		}
		return Mapping.RESULT;
	}

	/**
	 * 配送業者入金データ取込み結果リストをEXCELに出力します（全件）.
	 * @return 遷移URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excelAll() throws Exception {
		try {

			importDeliveryDepositForm.selectCount = 0;
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
	 * 配送業者入金データ取込み結果リストをEXCELに出力します（登録済み）.
	 * @return 遷移URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excelOK() throws Exception {
		try {

			importDeliveryDepositForm.selectCount = 1;
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
	 * 配送業者入金データ取込み結果リストをEXCELに出力します（エラーのみ）.
	 * @return 遷移URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excelErr() throws Exception {
		try {

			importDeliveryDepositForm.selectCount = 2;
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
	 * 配送業者入金データ取込み結果リストをEXCELに出力します（その他）.
	 * @return 遷移URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excelEtc() throws Exception {
		try {

			importDeliveryDepositForm.selectCount = 3;
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
			importDeliveryDepositForm.sortColumn = "";
			importDeliveryDepositForm.sortOrderAsc = true;
			importDeliveryDepositForm.newDepositSlipIdStr = "";
			List<ImportDeliveryDepositResultDto> searchAllResultList =
				importDeliveryDepositService.getImportResultList(
					importDeliveryDepositForm.sortColumn,
					importDeliveryDepositForm.sortOrderAsc,
					userDto.userId,
					importDeliveryDepositForm.newDepositSlipIdStr);

			importResultList = new ArrayList<BeanMap>();
			for(ImportDeliveryDepositResultDto resultList : searchAllResultList){

				List<BeanMap> invoiceList = new ArrayList<BeanMap>();
				List<BeanMap> depositList = new ArrayList<BeanMap>();

				boolean bPut = false;
				if(resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_INVOICE_ONLY)){
					// 送り状データのみ
					if((importDeliveryDepositForm.selectCount == 0)||(importDeliveryDepositForm.selectCount == 3)){
						invoiceList = importDeliveryDepositService.getInvoiceDataList(resultList.deliverySlipId);
						depositList.add(getInfoEmptyMap());
						bPut = true;
					}
				}
				else if(resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_DEPOSIT_ONLY)){
					// 入金データのみ
					if((importDeliveryDepositForm.selectCount == 0)||(importDeliveryDepositForm.selectCount == 2)){
						invoiceList.add(getInvoiceEmptyMap());
						depositList = importDeliveryDepositService.getDeliveryDepositList(resultList.deliverySlipId);
						bPut = true;
					}
				}
				else if(resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_OLD)||
						resultList.status.equals(Constants.DELIVERY_DEPOSIT_CSV.STATUS_NEW)){
					// // 登録済み（登録済み、新規登録）
					if((importDeliveryDepositForm.selectCount == 0)||(importDeliveryDepositForm.selectCount == 1)){
						invoiceList = importDeliveryDepositService.getInvoiceDataList(resultList.deliverySlipId);
						depositList = importDeliveryDepositService.getDeliveryDepositList(resultList.deliverySlipId);
						bPut = true;
					}
				}
				else{
					// 配送業者入金データのみ
					if((importDeliveryDepositForm.selectCount == 0)||(importDeliveryDepositForm.selectCount == 2)){
						invoiceList = importDeliveryDepositService.getInvoiceDataList(resultList.deliverySlipId);
						depositList = importDeliveryDepositService.getDeliveryDepositList(resultList.deliverySlipId);
						bPut = true;
					}
				}

				if(bPut){
					BeanMap resultMap = new BeanMap();

					// 伝票情報　セット
					resultMap.put("status", resultList.status);
					resultMap.put("salesSlipId", resultList.salesSlipId);
					resultMap.put("depositSlipId", resultList.depositSlipId);
					resultMap.put("customer", resultList.customer);
					resultMap.put("salesMoney", resultList.salesMoney);

					// 入金情報セット
					if(depositList.size() > 0 ){
						resultMap.putAll(depositList.get(0));
					}

					// 送り状情報セット
					if(invoiceList.size() > 0 ){
						resultMap.putAll(invoiceList.get(0));
					}

					importResultList.add(resultMap);
				}
			}

			// 添付ファイル名
			attachFileName = AttachFileName.FILENAME01;

			// 伝票情報カラム定義
			columnInfoList = new ArrayList<DepositImportExcelDto>();
			columnInfoList.add(new DepositImportExcelDto("status",0,"labels.slipStatus.status"));
			columnInfoList.add(new DepositImportExcelDto("salesSlipId",0,"labels.salesSlipId"));
			columnInfoList.add(new DepositImportExcelDto("depositSlipId",0,"labels.depositSlipId"));
			columnInfoList.add(new DepositImportExcelDto("customer",0,"labels.customer"));
			columnInfoList.add(new DepositImportExcelDto("salesMoney",2,"labels.salesMoney"));

			// 送り状情報カラム定義
			columnInfoList.add(new DepositImportExcelDto("paymentCategory",0,"labels.deliverydeposit.Info1"));
			columnInfoList.add(new DepositImportExcelDto("customerCode",0,"labels.deliverydeposit.Info2"));
			columnInfoList.add(new DepositImportExcelDto("deliverySlipId",0,"labels.deliverydeposit.Info3"));
			columnInfoList.add(new DepositImportExcelDto("dataCategory",0,"labels.deliverydeposit.Info4"));
			columnInfoList.add(new DepositImportExcelDto("changeCount",0,"labels.deliverydeposit.Info5"));
			columnInfoList.add(new DepositImportExcelDto("serviceCategory",0,"labels.deliverydeposit.Info6"));
			columnInfoList.add(new DepositImportExcelDto("settleCategory",0,"labels.deliverydeposit.Info7"));
			columnInfoList.add(new DepositImportExcelDto("deliveryDate",10,"labels.deliverydeposit.Info8"));
			columnInfoList.add(new DepositImportExcelDto("productPrice",2,"labels.deliverydeposit.Info9"));
			columnInfoList.add(new DepositImportExcelDto("codPrice",2,"labels.deliverydeposit.Info10"));
			columnInfoList.add(new DepositImportExcelDto("servicePrice",2,"labels.deliverydeposit.Info11"));
			columnInfoList.add(new DepositImportExcelDto("splitPrice",2,"labels.deliverydeposit.Info12"));
			columnInfoList.add(new DepositImportExcelDto("stampPrice",2,"labels.deliverydeposit.Info13"));
			columnInfoList.add(new DepositImportExcelDto("rgDate",10,"labels.deliverydeposit.Info14"));
			columnInfoList.add(new DepositImportExcelDto("rgSlipId",0,"labels.deliverydeposit.Info15"));

			// 配送業者入金データ情報カラム定義
			columnInfoList.add(new DepositImportExcelDto("customerCode",0,"labels.deliverydeposit.invoice.1"));
			columnInfoList.add(new DepositImportExcelDto("siType",0,"labels.deliverydeposit.invoice.2"));
			columnInfoList.add(new DepositImportExcelDto("cool",0,"labels.deliverydeposit.invoice.3"));
			columnInfoList.add(new DepositImportExcelDto("deliverySlipId",0,"labels.deliverydeposit.invoice.4"));
			columnInfoList.add(new DepositImportExcelDto("shipDate",10,"labels.deliverydeposit.invoice.5"));
			columnInfoList.add(new DepositImportExcelDto("deliveryDate",10,"labels.deliverydeposit.invoice.6"));
			columnInfoList.add(new DepositImportExcelDto("timeZone",0,"labels.deliverydeposit.invoice.7"));
			columnInfoList.add(new DepositImportExcelDto("deliveryCode",0,"labels.deliverydeposit.invoice.8"));
			columnInfoList.add(new DepositImportExcelDto("deliveryTel",0,"labels.deliverydeposit.invoice.9"));
			columnInfoList.add(new DepositImportExcelDto("deliveryTel2",0,"labels.deliverydeposit.invoice.10"));
			columnInfoList.add(new DepositImportExcelDto("deliveryZipCode",0,"labels.deliverydeposit.invoice.11"));
			columnInfoList.add(new DepositImportExcelDto("deliveryAddress",0,"labels.deliverydeposit.invoice.12"));
			columnInfoList.add(new DepositImportExcelDto("deliveryAddress2",0,"labels.deliverydeposit.invoice.13"));
			columnInfoList.add(new DepositImportExcelDto("deliveryOffice1",0,"labels.deliverydeposit.invoice.14"));
			columnInfoList.add(new DepositImportExcelDto("deliveryOffice2",0,"labels.deliverydeposit.invoice.15"));
			columnInfoList.add(new DepositImportExcelDto("deliveryName",0,"labels.deliverydeposit.invoice.16"));
			columnInfoList.add(new DepositImportExcelDto("deliveryKana",0,"labels.deliverydeposit.invoice.17"));
			columnInfoList.add(new DepositImportExcelDto("deliveryPre",0,"labels.deliverydeposit.invoice.18"));
			columnInfoList.add(new DepositImportExcelDto("ownerCode",0,"labels.deliverydeposit.invoice.19"));
			columnInfoList.add(new DepositImportExcelDto("ownerTel",0,"labels.deliverydeposit.invoice.20"));
			columnInfoList.add(new DepositImportExcelDto("ownerTel2",0,"labels.deliverydeposit.invoice.21"));
			columnInfoList.add(new DepositImportExcelDto("ownerZipCode",0,"labels.deliverydeposit.invoice.22"));
			columnInfoList.add(new DepositImportExcelDto("ownerAddress",0,"labels.deliverydeposit.invoice.23"));
			columnInfoList.add(new DepositImportExcelDto("ownerAddress2",0,"labels.deliverydeposit.invoice.24"));
			columnInfoList.add(new DepositImportExcelDto("ownerName",0,"labels.deliverydeposit.invoice.25"));
			columnInfoList.add(new DepositImportExcelDto("ownerKana",0,"labels.deliverydeposit.invoice.26"));
			columnInfoList.add(new DepositImportExcelDto("productCode1",0,"labels.deliverydeposit.invoice.27"));
			columnInfoList.add(new DepositImportExcelDto("productName1",0,"labels.deliverydeposit.invoice.28"));
			columnInfoList.add(new DepositImportExcelDto("productCode2",0,"labels.deliverydeposit.invoice.29"));
			columnInfoList.add(new DepositImportExcelDto("productName2",0,"labels.deliverydeposit.invoice.30"));
			columnInfoList.add(new DepositImportExcelDto("handle1",0,"labels.deliverydeposit.invoice.31"));
			columnInfoList.add(new DepositImportExcelDto("handle2",0,"labels.deliverydeposit.invoice.32"));
			columnInfoList.add(new DepositImportExcelDto("salesSlipId",0,"labels.deliverydeposit.invoice.33"));
			columnInfoList.add(new DepositImportExcelDto("collectPrice",0,"labels.deliverydeposit.invoice.34"));
			columnInfoList.add(new DepositImportExcelDto("ctaxPrice",0,"labels.deliverydeposit.invoice.35"));
			columnInfoList.add(new DepositImportExcelDto("layaway",0,"labels.deliverydeposit.invoice.36"));
			columnInfoList.add(new DepositImportExcelDto("officeCode",0,"labels.deliverydeposit.invoice.37"));
			columnInfoList.add(new DepositImportExcelDto("printCnt",0,"labels.deliverydeposit.invoice.38"));
			columnInfoList.add(new DepositImportExcelDto("numDispFlag",0,"labels.deliverydeposit.invoice.39"));
			columnInfoList.add(new DepositImportExcelDto("baCode",0,"labels.deliverydeposit.invoice.40"));
			columnInfoList.add(new DepositImportExcelDto("baType",0,"labels.deliverydeposit.invoice.41"));
			columnInfoList.add(new DepositImportExcelDto("fareNo",0,"labels.deliverydeposit.invoice.42"));
			columnInfoList.add(new DepositImportExcelDto("paymentSet",0,"labels.deliverydeposit.invoice.43"));
			columnInfoList.add(new DepositImportExcelDto("paymentNo",0,"labels.deliverydeposit.invoice.44"));
			columnInfoList.add(new DepositImportExcelDto("paymentNo1",0,"labels.deliverydeposit.invoice.45"));
			columnInfoList.add(new DepositImportExcelDto("paymentNo2",0,"labels.deliverydeposit.invoice.46"));
			columnInfoList.add(new DepositImportExcelDto("paymentNo3",0,"labels.deliverydeposit.invoice.47"));
			columnInfoList.add(new DepositImportExcelDto("emailUse",0,"labels.deliverydeposit.invoice.48"));
			columnInfoList.add(new DepositImportExcelDto("emailAddress",0,"labels.deliverydeposit.invoice.49"));
			columnInfoList.add(new DepositImportExcelDto("machineType",0,"labels.deliverydeposit.invoice.50"));
			columnInfoList.add(new DepositImportExcelDto("mailMessage",0,"labels.deliverydeposit.invoice.51"));
			columnInfoList.add(new DepositImportExcelDto("deliveryEmailUse",0,"labels.deliverydeposit.invoice.52"));
			columnInfoList.add(new DepositImportExcelDto("deliveryEmailAddress",0,"labels.deliverydeposit.invoice.53"));
			columnInfoList.add(new DepositImportExcelDto("deliveryEmailMessage",0,"labels.deliverydeposit.invoice.54"));
			columnInfoList.add(new DepositImportExcelDto("apsUse",0,"labels.deliverydeposit.invoice.55"));
			columnInfoList.add(new DepositImportExcelDto("qrPrintFlg",0,"labels.deliverydeposit.invoice.56"));
			columnInfoList.add(new DepositImportExcelDto("apsBillPrice",0,"labels.deliverydeposit.invoice.57"));
			columnInfoList.add(new DepositImportExcelDto("apsCtaxPrice",0,"labels.deliverydeposit.invoice.58"));
			columnInfoList.add(new DepositImportExcelDto("apsZipCode",0,"labels.deliverydeposit.invoice.59"));
			columnInfoList.add(new DepositImportExcelDto("apsAddress",0,"labels.deliverydeposit.invoice.60"));
			columnInfoList.add(new DepositImportExcelDto("apsAddress2",0,"labels.deliverydeposit.invoice.61"));
			columnInfoList.add(new DepositImportExcelDto("apsOffice1",0,"labels.deliverydeposit.invoice.62"));
			columnInfoList.add(new DepositImportExcelDto("apsOffice2",0,"labels.deliverydeposit.invoice.63"));
			columnInfoList.add(new DepositImportExcelDto("apsName",0,"labels.deliverydeposit.invoice.64"));
			columnInfoList.add(new DepositImportExcelDto("apsKana",0,"labels.deliverydeposit.invoice.65"));
			columnInfoList.add(new DepositImportExcelDto("apsQname",0,"labels.deliverydeposit.invoice.66"));
			columnInfoList.add(new DepositImportExcelDto("apsQzipCode",0,"labels.deliverydeposit.invoice.67"));
			columnInfoList.add(new DepositImportExcelDto("apsQaddress",0,"labels.deliverydeposit.invoice.68"));
			columnInfoList.add(new DepositImportExcelDto("apsQaddress2",0,"labels.deliverydeposit.invoice.69"));
			columnInfoList.add(new DepositImportExcelDto("apsQtel",0,"labels.deliverydeposit.invoice.70"));
			columnInfoList.add(new DepositImportExcelDto("apsNo",0,"labels.deliverydeposit.invoice.71"));
			columnInfoList.add(new DepositImportExcelDto("apsProductName",0,"labels.deliverydeposit.invoice.72"));
			columnInfoList.add(new DepositImportExcelDto("apsRemark",0,"labels.deliverydeposit.invoice.73"));

			// 添付ファイル名設定
			String attach = String.format(ATTACHMENT_FORMAT, URLEncoder.encode(attachFileName,ATTACHMENT_ENCODE));
			httpResponse.setHeader(CONTENT_DISPOSITION, attach);

		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	/**
	 * 空の送り状データを作成します.
	 * @return 空の送り状データ
	 */
	private BeanMap getInvoiceEmptyMap() {
		BeanMap EmpyMap = new BeanMap();
		EmpyMap.put("customerCode","");
		EmpyMap.put("siType","");
		EmpyMap.put("cool","");
		EmpyMap.put("deliverySlipId","");
		EmpyMap.put("shipDate","");
		EmpyMap.put("deliveryDate","");
		EmpyMap.put("timeZone","");
		EmpyMap.put("deliveryCode","");
		EmpyMap.put("deliveryTel","");
		EmpyMap.put("deliveryTel2","");
		EmpyMap.put("deliveryZipCode","");
		EmpyMap.put("deliveryAddress","");
		EmpyMap.put("deliveryAddress2","");
		EmpyMap.put("deliveryOffice1","");
		EmpyMap.put("deliveryOffice2","");
		EmpyMap.put("deliveryName","");
		EmpyMap.put("deliveryKana","");
		EmpyMap.put("deliveryPre","");
		EmpyMap.put("ownerCode","");
		EmpyMap.put("ownerTel","");
		EmpyMap.put("ownerTel2","");
		EmpyMap.put("ownerZipCode","");
		EmpyMap.put("ownerAddress","");
		EmpyMap.put("ownerAddress2","");
		EmpyMap.put("ownerName","");
		EmpyMap.put("ownerKana","");
		EmpyMap.put("productCode1","");
		EmpyMap.put("productName1","");
		EmpyMap.put("productCode2","");
		EmpyMap.put("productName2","");
		EmpyMap.put("handle1","");
		EmpyMap.put("handle2","");
		EmpyMap.put("salesSlipId","");
		EmpyMap.put("collectPrice","");
		EmpyMap.put("ctaxPrice","");
		EmpyMap.put("layaway","");
		EmpyMap.put("officeCode","");
		EmpyMap.put("printCnt","");
		EmpyMap.put("numDispFlag","");
		EmpyMap.put("baCode","");
		EmpyMap.put("baType","");
		EmpyMap.put("fareNo","");
		EmpyMap.put("paymentSet","");
		EmpyMap.put("paymentNo","");
		EmpyMap.put("paymentNo1","");
		EmpyMap.put("paymentNo2","");
		EmpyMap.put("paymentNo3","");
		EmpyMap.put("emailUse","");
		EmpyMap.put("emailAddress","");
		EmpyMap.put("machineType","");
		EmpyMap.put("mailMessage","");
		EmpyMap.put("deliveryEmailUse","");
		EmpyMap.put("deliveryEmailAddress","");
		EmpyMap.put("deliveryEmailMessage","");
		EmpyMap.put("apsUse","");
		EmpyMap.put("qrPrintFlg","");
		EmpyMap.put("apsBillPrice","");
		EmpyMap.put("apsCtaxPrice","");
		EmpyMap.put("apsZipCode","");
		EmpyMap.put("apsAddress","");
		EmpyMap.put("apsAddress2","");
		EmpyMap.put("apsOffice1","");
		EmpyMap.put("apsOffice2","");
		EmpyMap.put("apsName","");
		EmpyMap.put("apsKana","");
		EmpyMap.put("apsQname","");
		EmpyMap.put("apsQzipCode","");
		EmpyMap.put("apsQaddress","");
		EmpyMap.put("apsQaddress2","");
		EmpyMap.put("apsQtel","");
		EmpyMap.put("apsNo","");
		EmpyMap.put("apsProductName","");
		EmpyMap.put("apsRemark","");

		return EmpyMap;
	}

	/**
	 * 空の配送業者入金データを作成します.
	 * @return 空の配送業者入金データ
	 */
	private BeanMap getInfoEmptyMap() {
		BeanMap EmpyMap = new BeanMap();
		EmpyMap.put("paymentCategory","");
		EmpyMap.put("customerCode","");
		EmpyMap.put("deliverySlipId","");
		EmpyMap.put("dataCategory","");
		EmpyMap.put("changeCount","");
		EmpyMap.put("serviceCategory","");
		EmpyMap.put("settleCategory","");
		EmpyMap.put("deliveryDate","");
		EmpyMap.put("productPrice","");
		EmpyMap.put("codPrice","");
		EmpyMap.put("servicePrice","");
		EmpyMap.put("splitPrice","");
		EmpyMap.put("stampPrice","");
		EmpyMap.put("rgDate","");
		EmpyMap.put("rgSlipId","");

		return EmpyMap;
	}

}



