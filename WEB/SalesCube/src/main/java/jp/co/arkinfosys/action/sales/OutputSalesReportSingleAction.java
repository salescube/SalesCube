/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.sales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.PrintUtil;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.form.ajax.sales.InputSalesReportForm;
import jp.co.arkinfosys.service.PickingLineService;
import jp.co.arkinfosys.service.PickingService;
import jp.co.arkinfosys.service.SalesLineService;
import jp.co.arkinfosys.service.SalesService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 売上伝票入力画面でPDF出力するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputSalesReportSingleAction extends
		OutputSalesReportResultAction {
	@ActionForm
	@Resource
	public InputSalesReportForm inputSalesReportForm;

	@Resource
	protected SalesService salesService;

	@Resource
	protected SalesLineService salesLineService;

	@Resource
	protected PickingService pickingService;

	@Resource
	protected PickingLineService pickingLineService;

	/**
	 * ダウンロードファイル名定義
	 */
	public static final String FILE_PREFFIX = "Sales";

	private ArrayList<String> outputTypeList;

	/**
	 * PDF形式で売上帳票を作成し、レスポンスに出力します.<br>
	 * 帳票を発行した売上伝票の出力情報を更新します.
	 *
	 * @return 処理結果文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String pdf() throws Exception {

		try {
			initOutput();

			updateDB();

		} catch (Exception e) {
			e.printStackTrace();
			super.errorLog(e);

			return null;
		}

		return super.pdf();
	}

	/**
	 * 帳票出力対象リストを生成します.
	 */
	protected void initOutput() {
		outputTypeList = new ArrayList<String>();
		if (inputSalesReportForm.typeJ) {
			outputTypeList.add(Constants.REPORT_TEMPLATE.REPORT_ID_J);
			outputTypeList.add(Constants.REPORT_TEMPLATE.REPORT_ID_K);
		}
		if (inputSalesReportForm.typeA) {
			outputTypeList.add(Constants.REPORT_TEMPLATE.REPORT_ID_A);
		}
		if (inputSalesReportForm.typeC) {
			outputTypeList.add(Constants.REPORT_TEMPLATE.REPORT_ID_C);
		}
		if (inputSalesReportForm.typeD) {
			outputTypeList.add(Constants.REPORT_TEMPLATE.REPORT_ID_D);
		}
		if (inputSalesReportForm.typeF) {
			outputTypeList.add(Constants.REPORT_TEMPLATE.REPORT_ID_F);
		}
		if (inputSalesReportForm.typeE) {
			outputTypeList.add(Constants.REPORT_TEMPLATE.REPORT_ID_E);
		}
		if (inputSalesReportForm.typeG) {
			outputTypeList.add(Constants.REPORT_TEMPLATE.REPORT_ID_G);
		}
		if (inputSalesReportForm.typeH) {
			outputTypeList.add(Constants.REPORT_TEMPLATE.REPORT_ID_H);
		}
	}

	/**
	 * 帳票出力カウンタを更新します.<BR>
	 * @throws ServiceException
	 */
	protected void updateDB() throws ServiceException {

		salesService.updatePrintCount(inputSalesReportForm.printId,
				outputTypeList);

	}

	/**
	 * レポートテンプレートIDを返します.<BR>
	 * 出力件数を超えた場合にはnullを返します.<br>
	 * @param index 取得するテンプレートのインデックス
	 * @return レポートテンプレートID
	 */
	@Override
	protected String getReportId(int index) {
		if (outputTypeList.size() <= index) {
			return null;
		}
		return outputTypeList.get(index);
	}

	/**
	 * 拡張子を除いたファイル名を返します.<BR>
	 * @return Salesとなる文字列
	 */
	@Override
	protected String getFilePreffix() {
		return FILE_PREFFIX;
	}

	/**
	 * 出力する帳票の種類に応じた伝票情報を返します.<BR>
	 * 出力件数を超えた場合にはnullを返します.<br>
	 * 出力対象が納品書兼領収書の場合、取引区分が「クレジット」であるか否かによってクレジット決済コメントの表示フラグを制御します.<BR>
	 * 出力対象が納品書の場合、下部のコメント（【請求書発行】お客様締日～）を設定します.<BR>
	 * 出力帳票が納品書か仮納品書の場合、「納品書に消費税額が含まれていないので請求書で別途請求する」旨の注意書きを.<BR>
	 * 請求書発行単位が売上伝票単位の場合は出力せず、請求締め単位の場合は出力します.<BR>
	 * @param index 取得する帳票のインデックス
	 * @return 伝票情報
	 * @throws ServiceException
	 */
	@Override
	protected BeanMap getSlip(int index) throws ServiceException {
		if (outputTypeList.size() <= index) {
			return null;
		}

		BeanMap beanMap;

		if (Constants.REPORT_TEMPLATE.REPORT_ID_J.equals(outputTypeList
				.get(index))
				|| Constants.REPORT_TEMPLATE.REPORT_ID_K.equals(outputTypeList
						.get(index))) {
			beanMap = this.pickingService
					.findPickingListBySalesSlipIdSimple(inputSalesReportForm.printId);
		} else if (Constants.REPORT_TEMPLATE.REPORT_ID_C.equals(outputTypeList
				.get(index))
				|| Constants.REPORT_TEMPLATE.REPORT_ID_G.equals(outputTypeList
						.get(index))) {
			beanMap = this.salesService
					.findSalesSlipBySalesSlipIdSimpleAddDate(inputSalesReportForm.printId);
			// （帳票パラメータの名前付けに合わせて）salesCmCategoryの値をcategoryCodeNameに入れ替える
			beanMap.put("salesCmCategory", beanMap.get("categoryCodeName")
					.toString());
		} else {
			beanMap = this.salesService
					.findSalesSlipBySalesSlipIdSimple(inputSalesReportForm.printId);
			// （帳票パラメータの名前付けに合わせて）salesCmCategoryの値をcategoryCodeNameに入れ替える
			beanMap.put("salesCmCategory", beanMap.get("categoryCodeName")
					.toString());
		}
		if (beanMap != null) {
			// 顧客を取得
			Customer customer = super.customerService
					.findCustomerByCode(beanMap.get("customerCode").toString());
			if (customer == null) {
				return null; // このパターンはない
			}

			// 日付表示フラグを設定する
			if (CategoryTrns.BILL_DATE_PRINT_ON.equals(customer.billDatePrint)) {
				beanMap.put(DISP_DATE_FLAG, true);
			} else {
				beanMap.put(DISP_DATE_FLAG, false);
			}
			// 2010.04.23 add kaki 納品書兼領収書にて、クレジット決済の場合のみコメントを表示
			if (Constants.REPORT_TEMPLATE.REPORT_ID_F.equals(outputTypeList
					.get(index))) {
				// 取引区分＝「クレジット」の場合のみ
				if (Constants.SALES_CM_CATEGORY_NAME.CATEGORY_CREDIT_CARD
						.equals(beanMap.get("categoryCodeName"))) {
					beanMap.put(DISP_CREDIT_CMT, true);
				} else {
					beanMap.put(DISP_CREDIT_CMT, false);
				}
			}
			// 納品書(D)にて、下部のコメント（【請求書発行】お客様締日～）を設定する。
			if (Constants.REPORT_TEMPLATE.REPORT_ID_D.equals(outputTypeList
					.get(index))) {
				// 請求書発行単位=請求締め単位の場合締日を設定する
				if (CategoryTrns.BILL_PRINT_UNIT_BILL_CLOSE
						.equals(customer.billPrintUnit)) {
					if (beanMap.get("billCutoffGroup").equals("31")) {
						beanMap.put(BILL_CUTOFF_DATE, "("
								+ Constants.BILL_CUTOFF_GROUP_NAME.CUTOFF_END
								+ ")");
					} else if (beanMap.get("billCutoffGroup").equals("99")) {
						beanMap.put(BILL_CUTOFF_DATE, "");
					} else {
						beanMap.put(BILL_CUTOFF_DATE, "("
								+ beanMap.get("billCutoffGroup") + "日)");
					}
				} else {
					beanMap.put(BILL_CUTOFF_DATE, "");
				}
			}
			if ((Constants.REPORT_TEMPLATE.REPORT_ID_D.equals(outputTypeList
					.get(index)))
					|| (Constants.REPORT_TEMPLATE.REPORT_ID_E
							.equals(outputTypeList.get(index)))) {
				//出力帳票が納品書か仮納品書の場合
				beanMap.put(ID_HEAD_CLM, "");

				// テンプレートDのフッタ部分(Page Footer)にある「納品書に消費税額が含まれていないので請求書で別途請求する」旨の注意書きを出力するかどうかを設定する
				if (CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP
						.equals(customer.billPrintUnit)) {
					beanMap.put(REPORT_D_FOOTER, "0"); // 請求書発行単位が売上伝票単位の場合は出力しない
				} else if (CategoryTrns.BILL_PRINT_UNIT_BILL_CLOSE
						.equals(customer.billPrintUnit)) {
					beanMap.put(REPORT_D_FOOTER, "1"); // 請求書発行単位が請求締め単位の場合は出力する
				}
			}

		}
		return beanMap;
	}

	/**
	 * 明細情報を返します.<BR>
	 * 出力件数を超えた場合にはnullを返します.<br>
	 * ピッキングリストの場合、出荷指示明細行以外から取得する現在のデータを設定します.<BR>
	 * 組み立て指示書の場合、セット商品情報を展開して設定します.<BR>
	 * @param index 取得する帳票のインデックス
	 * @return 明細情報のリスト
	 * @throws ServiceException
	 */
	@Override
	protected List<BeanMap> getDetailList(int index) throws ServiceException {
		if (outputTypeList.size() <= index) {
			return null;
		}

		List<BeanMap> beanMapList;
		List<BeanMap> tempBeanMapList;

		if (Constants.REPORT_TEMPLATE.REPORT_ID_J.equals(outputTypeList
				.get(index))) {

			beanMapList = this.pickingLineService
					.findPickingLineBySalesSlipIdSimple(inputSalesReportForm.printId);
			// 出荷指示明細行以外から取得するデータを設定
			addPickingData(beanMapList);
			PrintUtil.removeSpaceToExceptianalProductLine(beanMapList);

		} else if (Constants.REPORT_TEMPLATE.REPORT_ID_K.equals(outputTypeList
				.get(index))) {

			tempBeanMapList = this.pickingLineService
					.findPickingLineSetBySalesSlipIdSimple(inputSalesReportForm.printId);
			beanMapList = createAssembleInstructiongData(tempBeanMapList);
			PrintUtil.removeSpaceToExceptianalProductLine(beanMapList);

		} else {

			beanMapList = this.salesLineService
					.findSalesLinesBySalesSlipIdSimple(inputSalesReportForm.printId);
			PrintUtil.setSpaceToExceptianalProductCode(beanMapList);
		}
		return beanMapList;
	}
}
