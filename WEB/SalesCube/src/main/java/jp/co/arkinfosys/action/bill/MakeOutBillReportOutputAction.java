/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.bill;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractReportWriterAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.PrintUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto;
import jp.co.arkinfosys.entity.CategoryTrn;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.bill.MakeOutBillPrintForm;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.BillReportService;
import jp.co.arkinfosys.service.BillService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.DeliveryService;
import jp.co.arkinfosys.service.OutputSalesReportSheetLineService;
import jp.co.arkinfosys.service.SalesLineService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 請求書発行処理を行うアクションクラスです
 *
 * @author Ark Information Systems
 *
 */
public class MakeOutBillReportOutputAction extends AbstractReportWriterAction {
	@ActionForm
	@Resource
	public MakeOutBillPrintForm makeOutBillPrintForm;

	@Resource
	protected BillService billService;

	@Resource
	protected CustomerService customerService;

	@Resource
	protected DeliveryService deliveryService;

	@Resource
	protected SalesLineService salesLineService;

	@Resource
	protected AbstractService<SalesSlipTrn> salesService;

	@Resource
	protected CategoryService categoryService;

	@Resource
	protected BillReportService billReportService;

	/**
	 * ダウンロードファイル名定義
	 */
	public static final String FILE_PREFFIX = "Bill";

	@Resource
	protected OutputSalesReportSheetLineService outputSalesReportSheetLineService;

	/**
	 * 親クラスのExcel出力処理を呼び出し、DB上の帳票出力カウンタをインクリメントします.<BR>
	 * 標準実装ではExcel出力ボタンが表示されていないため、呼び出されることはありません.
	 *
	 * @return 親クラスのメソッドが返す値
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excel() throws Exception {

		String retVal;
		try {
			retVal = super.excel();
			updateDB();

		} catch (Exception e) {
			e.printStackTrace();
			super.errorLog(e);

			return null;
		}
		return retVal;
	}

	/**
	 * 親クラスのPDF出力処理を呼び出し、DB上の帳票出力カウンタをインクリメントします.
	 *
	 * @return 親クラスのメソッドが返す値
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String pdf() throws Exception {

		String retVal;
		try {
			retVal = super.pdf();
			updateDB();

		} catch (Exception e) {
			e.printStackTrace();
			super.errorLog(e);
			return null;
		}

		return retVal;
	}

	/**
	 * 請求締め解除を行った請求書をPDF形式で出力します.<BR>
	 * 通常の請求書は１ファイル中に複数の請求書をまとめて出力しますが、
	 * 請求締解除を行った請求書は、１請求書毎PDFファイル形式で保存されているため、
	 * それぞれが別ブラウザウインドウ上に表示されます.<BR>
	 * 出力対象の請求書番号は、urlPatternとして渡されます.<BR>
	 *
	 * @return 常にnullを返します
	 */
	@Execute(validator = false, urlPattern = "viewPdf/{billId}")
	public String viewPdf() {
		try {
			String billId = this.makeOutBillPrintForm.billId;
			this.billReportService.outputToPDF(this.httpResponse, billId);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 請求締め解除を行った請求書をExcel形式で出力します.<BR>
	 * 標準実装ではExcel出力ボタンが表示されていないため、呼び出されることはありません.<BR>
	 * また請求締解除請求をExcel形式で保存していないため、呼び出すとエラーとなります.<BR>
	 *
	 * @return 常にnullを返します
	 */
	@Execute(validator = false, urlPattern = "viewExcel/{billId}")
	public String viewExcel() {
		try {
			String billId = this.makeOutBillPrintForm.billId;
			this.billReportService.outputToEXCEL(this.httpResponse, billId);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * DB上の帳票出力カウンタをインクリメントします.
	 *
	 * @throws ServiceException
	 */
	protected void updateDB() throws ServiceException {
		for (int i = 0; i < this.makeOutBillPrintForm.rowDataPrint.size(); i++) {
			MakeOutBillSearchResultDto dto = this.makeOutBillPrintForm.rowDataPrint
					.get(i);
			this.billService.updatePrintCount(dto.billId);
		}
	}

	/**
	 * 請求書のレポートテンプレートIDを返します.<BR>
	 * この関数の呼び出し回数が、出力対象チェック数以上になった場合には、nullを返し処理を終了させます.
	 *
	 * @param index 取得するテンプレートのインデックス
	 * @return レポートテンプレートID
	 */
	@Override
	protected String getReportId(int index) {
		if (this.makeOutBillPrintForm.rowDataPrint.size() <= index) {
			return null;
		}
		return Constants.REPORT_TEMPLATE.REPORT_ID_I;
	}

	/**
	 * 拡張子を除いたファイル名を返します.
	 * @return 拡張子なしのファイル名
	 */
	@Override
	protected String getFilePreffix() {
		return FILE_PREFFIX;
	}

	/**
	 * 請求書情報を返します.<BR>
	 * 請求先の情報は、印刷実行時の顧客マスタの情報を使用します.
	 * @param index 取得する帳票のインデックス
	 * @return 請求書情報
	 * @throws ServiceException
	 */
	@Override
	protected BeanMap getSlip(int index) throws ServiceException {
		if (this.makeOutBillPrintForm.rowDataPrint.size() <= index) {
			return null;
		}

		BeanMap beanMapBill = this.billService
				.findBillByIdSimple(this.makeOutBillPrintForm.rowDataPrint
						.get(index).billId);
		// 常にマスタの請求先を使う
		String customerCode = beanMapBill.get("customerCode").toString();

		List<DeliveryAndPre> billList = this.deliveryService
				.searchDeliveryByCompleteCustomerCode(customerCode);
		if (billList.size() > 0) {
			DeliveryAndPre bill = billList.get(0);
			beanMapBill.put("baName", bill.deliveryName);
			beanMapBill.put("baOfficeName", bill.deliveryOfficeName);
			beanMapBill.put("baDeptName", bill.deliveryDeptName);
			beanMapBill.put("baPcName", bill.deliveryPcName);
			if (StringUtil.hasLength(bill.deliveryPcPreCategory)) {
				CategoryTrn categoryTrn = categoryService
						.findCategoryTrnByIdAndCode(Categories.PRE_TYPE,
								bill.deliveryPcPreCategory);
				if (categoryTrn != null) {
					beanMapBill.put("baPcPre", categoryTrn.categoryCodeName);
				}
			}
			beanMapBill.put("baZipCode", bill.deliveryZipCode);
			beanMapBill.put("baAddress1", bill.deliveryAddress1);
			beanMapBill.put("baAddress2", bill.deliveryAddress2);
			beanMapBill.put("baTel", bill.deliveryTel);
			beanMapBill.put("baFax", bill.deliveryFax);
		}
		return beanMapBill;
	}

	/**
	 * 請求書の明細情報を返します.<BR>
	 * 明細行が存在しない場合には、空欄の情報を１行分作成して返します.
	 * @param index 請求書のインデックス
	 * @return 請求書明細情報のリスト
	 * @throws ServiceException
	 */
	@Override
	protected List<BeanMap> getDetailList(int index) throws ServiceException {
		if (this.makeOutBillPrintForm.rowDataPrint.size() <= index) {
			return null;
		}

		List<BeanMap> lbm = this.salesLineService
				.findSalesLinesByBillIdSimple(this.makeOutBillPrintForm.rowDataPrint
						.get(index).billId);
		if (lbm.size() == 0) {
			BeanMap bm = new BeanMap();
			bm.put("salesDate", null);
			bm.put("salesSlipId", 0);
			bm.put("lineNo", 0);
			bm.put("productCode", "");
			bm.put("productAbstract", "");
			bm.put("quantity", null);
			bm.put("unitName", "");
			bm.put("unitRetailPrice", null);
			bm.put("retailPrice", null);
			bm.put("remarks", "");
			lbm.add(bm);
			return lbm;
		}
		PrintUtil.setSpaceToExceptianalProductCode(lbm);

		return lbm;
	}


}
