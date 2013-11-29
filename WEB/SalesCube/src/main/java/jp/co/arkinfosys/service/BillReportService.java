/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.PrintUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.Bill;
import jp.co.arkinfosys.entity.CategoryTrn;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 請求書出力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class BillReportService extends AbstractReportService<Bill> {

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

	/**
	 * ダウンロードファイル名定義
	 */
	public static final String FILE_PREFFIX = "Bill";

	@Resource
	protected OutputSalesReportSheetLineService outputSalesReportSheetLineService;

	protected Bill bill;

	public boolean useLastDate = false;

	/**
	 * Excelファイルを出力します.<br>
	 * 戻り値の遷移先URIは必ずnullが返されます.
	 * @param bill 請求書エンティティ
	 * @return 遷移先URI
	 * @throws Exception
	 */
	public String excel(Bill bill) throws Exception {

		String retVal;
		try {
			this.bill = bill;
			retVal = super.excel();

		} catch (Exception e) {
			e.printStackTrace();

			// システム例外として処理する
			return null;
		}
		return retVal;
	}

	/**
	 * PDFファイルを出力します.
	 * @param bill 請求書エンティティ
	 * @return 遷移先URI
	 * @throws Exception
	 */
	public String pdf(Bill bill) throws Exception {

		String retVal;
		try {
			this.bill = bill;
			retVal = super.pdf();

		} catch (Exception e) {
			e.printStackTrace();

			// システム例外として処理する
			throw e;
		}

		return retVal;
	}

	/**
	 * 帳票テンプレートIDを返します.
	 *
	 * @return 帳票テンプレートID
	 */
	@Override
	protected String getReportId() {
		return Constants.REPORT_TEMPLATE.REPORT_ID_I;
	}


	/**
	 * 帳票テンプレートIDを返します.
	 *
	 * @param index 出力する帳票のインデックス
	 * @return 帳票テンプレートID
	 */
	@Override
	protected String getReportId(int index) {
		if ( 1 <= index) {
			return null;
		}// else{
		return Constants.REPORT_TEMPLATE.REPORT_ID_I;
		// }
	}

	/**
	 * 実ファイル名を返します.
	 * @param index 出力する帳票のインデックス
	 * @return 実ファイル名
	 */
	@Override
	protected String getRealFilePreffix(int index) {
		if ( 1 <= index) {
			return null;
		}
		return bill.billId.toString();
	}

	/**
	 * 拡張子を除くファイル名を返します.
	 * @return 拡張子を除くファイル名
	 */
	@Override
	protected String getFilePreffix() {
		return FILE_PREFFIX;
	}

	/**
	 * 伝票データを返します.
	 *
	 * @param index 出力する帳票のインデックス
	 * @return 伝票データ
	 * @throws ServiceException
	 */
	@Override
	protected BeanMap getSlip(int index) throws ServiceException {
		if ( 1 <= index) {
			return null;
		}

		BeanMap beanMapBill = Beans.createAndCopy(BeanMap.class, bill).execute();

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
					beanMapBill
							.put("baPcPre", categoryTrn.categoryCodeName);
				}
			}
			beanMapBill.put("baZipCode", bill.deliveryZipCode);
			beanMapBill.put("baAddress1", bill.deliveryAddress1);
			beanMapBill.put("baAddress2", bill.deliveryAddress2);
			beanMapBill.put("baTel", bill.deliveryTel);
			beanMapBill.put("baFax", bill.deliveryFax);
		}
		if( useLastDate ){
			beanMapBill.put("useLastDate", useLastDate);
		}
		return beanMapBill;
	}

	/**
	 * 明細行データを返します.
	 *
	 * @param index 出力する帳票のインデックス
	 * @return 明細行のリスト
	 * @throws ServiceException
	 */
	@Override
	protected List<BeanMap> getDetailList(int index) throws ServiceException {
		if ( 1 <= index) {
			return null;
		}// else{
		List<BeanMap> lbm = this.salesLineService
				.findSalesLinesByBillIdSimple( bill.billId.toString() );
		if (lbm.size() == 0) {
			BeanMap bm = new BeanMap();
			bm.put("salesDate", null);
			bm.put("salesSlipId", 0);
			bm.put("salesLineNo", 0);
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
