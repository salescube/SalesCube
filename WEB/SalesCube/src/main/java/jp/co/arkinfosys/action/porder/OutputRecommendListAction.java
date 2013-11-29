/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.porder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.dto.porder.OutputRecommendListFormDto;
import jp.co.arkinfosys.dto.porder.OutputRecommendOrderResultLine;
import jp.co.arkinfosys.dto.porder.OutputRecommendOrderResultLine.OutputRecommendOrderResultSlipLine;
import jp.co.arkinfosys.entity.Supplier;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.form.porder.InputPOrderForm;
import jp.co.arkinfosys.form.porder.OutputRecommendListForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.porder.OutputRecommendListService;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
/**
 * 補充発注推奨リスト出力画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputRecommendListAction extends CommonResources {

	@ActionForm
	@Resource
	protected OutputRecommendListForm outputRecommendListForm;

	/**
	 * 画面専用サービス
	 */
	@Resource
	private OutputRecommendListService outputRecommendListService;

	/**
	 * 区分情報
	 */
	@Resource
	private CategoryService categoryService;

	/**
	 * ファイル名表示用
	 */
//	@Resource
//	protected MakeOutPOrderResultOutputAction pOrderReportWriterAjaxAction;

	/**
	 * 仕入先リスト
	 */
	public List<LabelValueBean> supplierList = new ArrayList<LabelValueBean>();

	/**
	 * 発注区分リスト
	 */
	public List<LabelValueBean> poCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 都度発注区分リスト
	 */
	public List<LabelValueBean> immediatelyPOCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 運送便区分リスト
	 */
	public List<LabelValueBean> transportCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 検索結果列情報リスト
	 */
	public List<DetailDispItemDto> columnInfoList = new ArrayList<DetailDispItemDto>();

	/**
	 * ファイル名の装飾
	 */
	private static final String fileNamePrefix = "ORDER";
	private static final String fileNameSuffix = "";
	private static final String pdfFileNameExt = ".pdf";
	private static final String xlsFileNameExt = ".xls";

	/**
	 * Excelファイル名を返します(外部呼出し用).
	 * @param slipId 伝票番号
	 * @return ファイル名
	 */
	public String getXlsFileName(String slipId) {
		return fileNamePrefix + slipId + fileNameSuffix + xlsFileNameExt;
	}

	/**
	 * PDFファイル名を返します(外部呼出し用).
	 * @param slipId 伝票番号
	 * @return ファイル名
	 */
	public String getPdfFileName(String slipId) {
		return fileNamePrefix + slipId + fileNameSuffix + pdfFileNameExt;
	}

	/**
	 * 初期表示処理です.
	 *
	 * @return 遷移先URI
	 */
	@Execute(validator = false)
	public String index() {
		//画面権限情報取得
		outputRecommendListForm.validInputPOrder = userDto
				.isMenuValid(Constants.MENU_ID.INPUT_PORDER);
		outputRecommendListForm.updateOutputRecommendList = userDto
				.isMenuUpdate(Constants.MENU_ID.OUTPUT_RECOMMEND_LIST);
		//プルダウンの初期化
		makeList();

		// ソート情報保持リストの取得
		columnInfoList = outputRecommendListService.getColumnInfoList();

		return "outputRecommendList.jsp";
	}

	/**
	 * 発注処理を行います.
	 * @return 遷移先URI
	 */
	@Execute(validator = true, validate="@,validate", stopOnValidationError=false,input="index", redirect=false)
	public String order() {
		try {

			//画面権限情報取得
			outputRecommendListForm.validInputPOrder = userDto
					.isMenuValid(Constants.MENU_ID.INPUT_PORDER);
			outputRecommendListForm.updateOutputRecommendList = userDto
					.isMenuUpdate(Constants.MENU_ID.OUTPUT_RECOMMEND_LIST);
			//結果画面のリ スト用
			List<OutputRecommendOrderResultLine> orderResultList = new ArrayList<OutputRecommendOrderResultLine>();

			// ユーザ名とかドメインとか
			Map<String, Object> CommonParam = outputRecommendListService.createCommonParam();

			//伝票パラメータの生成
			Map<String, Object> slipParam = new HashMap<String, Object>();
			slipParam.putAll(CommonParam);
			slipParam.put(OutputRecommendListService.SlipParam.SUPPLIER_CODE, outputRecommendListForm.supplierCode);
			slipParam.put(OutputRecommendListService.SlipParam.DELIVERY_DATE, outputRecommendListForm.deliveryDate);
			if(CategoryTrns.ENTRUST_PORDER.equals(outputRecommendListForm.poCategory)) {
				// 発注区分が委託発注の場合は「委託在庫」
				slipParam.put(OutputRecommendListService.SlipParam.TRANSPORT_CATEGORY, CategoryTrns.TRANSPORT_CATEGORY_ENTRUST);
			}
			else {
				slipParam.put(OutputRecommendListService.SlipParam.TRANSPORT_CATEGORY, outputRecommendListForm.transportCategory);
			}

			//明細行パラメータ
			List<Map<String, Object>> lineParam = new ArrayList<Map<String, Object>>();

			//結果画面のリスト各行のデータ
			OutputRecommendOrderResultLine orderResultLine = new OutputRecommendOrderResultLine();
			orderResultLine.poSlipId = null;
			orderResultLine.lines = new ArrayList<OutputRecommendOrderResultSlipLine>();
			//明細行分ける？
			int lineCount = 0;
			lineParam.clear();
			for(OutputRecommendListFormDto l_Result : outputRecommendListForm.searchResultList){
				//明細行用のデータを抽出する？
				if(Null2Bool(l_Result.validRow)){
					//行内データを行内パラメータに
					Map<String, Object> row_lineParam = new HashMap<String, Object>();
					row_lineParam.putAll(CommonParam);
					row_lineParam.put(OutputRecommendListService.SlipParam.PRODUCT_CODE, l_Result.productCode );
					row_lineParam.put(OutputRecommendListService.SlipParam.QUANTITY, l_Result.pOrderQuantity );
					//明細行パラメータに追加
					lineParam.add(row_lineParam);
					//結果画面用に記録
					OutputRecommendOrderResultLine.OutputRecommendOrderResultSlipLine orderResultSlipLine
								= new OutputRecommendOrderResultLine.OutputRecommendOrderResultSlipLine();
					orderResultSlipLine.productCode = l_Result.productCode;
					orderResultSlipLine.pOrderQuantity = l_Result.pOrderQuantity;
					orderResultLine.lines.add(orderResultSlipLine);
					//行抽出できた
					lineCount++;
				}
				//明細行がもういっぱいです。途中で伝票作ります。
				if(lineCount == InputPOrderForm.CONST_SLIP_LINE_MAX_DEFAULT){
					//伝票発行と伝票番号の取得
					orderResultLine.poSlipId = String.valueOf(outputRecommendListService.createSlipByParam(slipParam, lineParam));
					orderResultLine.xlsFileName = getXlsFileName(orderResultLine.poSlipId);
					orderResultLine.pdfFileName = getPdfFileName(orderResultLine.poSlipId);
					orderResultLine.lineCount = lineCount;
					lineCount = 0;
					lineParam.clear();
					//結果用リスト
					orderResultList.add(orderResultLine);
					orderResultLine = new OutputRecommendOrderResultLine();
					orderResultLine.poSlipId = null;
					orderResultLine.lines = new ArrayList<OutputRecommendOrderResultSlipLine>();
				}else if(lineCount > InputPOrderForm.CONST_SLIP_LINE_MAX_DEFAULT){
					return null;	//え？なんでここが呼ばれるの？
				}
			}
			//残りの伝票を作ります。
			if(lineCount > 0){
				//伝票発行と伝票番号の取得
				orderResultLine.poSlipId = String.valueOf(outputRecommendListService.createSlipByParam(slipParam, lineParam));
				orderResultLine.xlsFileName = getXlsFileName(orderResultLine.poSlipId);
				orderResultLine.pdfFileName = getPdfFileName(orderResultLine.poSlipId);
				orderResultLine.lineCount = lineCount;
				orderResultList.add(orderResultLine);
			}
			//結果を渡す
			outputRecommendListForm.orderResultList = orderResultList;
			return "orderResult.jsp";
		} catch (Exception e) {
			super.errorLog(e);
			return null;
		}
	}

	/**
	 * 画面上のプルダウンを作成します.
	 */
	private void makeList(){
		try {
			//運送便区分プルダウンの値
			List<CategoryJoin> categoryJoinList = this.categoryService
					.findCategoryJoinById(Categories.TRANSPORT_CATEGORY);
			for(CategoryJoin categoryTrnJoin : categoryJoinList) {

				// 補充発注画面では、運送便プルダウンでは委託在庫を選択できないようにする。(委託在庫発注は運送便プルダウンではなく、発注区分プルダウンにて行うため。)
				if(CategoryTrns.TRANSPORT_CATEGORY_ENTRUST.equals(categoryTrnJoin.categoryCode)) {
					continue;
				}

				LabelValueBean bean = new LabelValueBean();
				bean.setValue(categoryTrnJoin.categoryCode);
				bean.setLabel(categoryTrnJoin.categoryCodeName);
				this.transportCategoryList.add(bean);
			}
			//this.transportCategoryList.add(0, new LabelValueBean());

			//仕入先プルダウン
			List<Supplier> l_supplierList = this.outputRecommendListService
				.findRecommendSuppliers();
			for(Supplier l_supplier : l_supplierList) {
				LabelValueBean bean = new LabelValueBean();
				bean.setValue(l_supplier.supplierCode);
				bean.setLabel(l_supplier.supplierName);
				this.supplierList.add(bean);
			}

			//発注区分プルダウンの値
			categoryJoinList = this.categoryService
					.findCategoryJoinById(Categories.PO_CATEGORY);
			for(CategoryJoin categoryTrnJoin : categoryJoinList) {
				LabelValueBean bean = new LabelValueBean();
				bean.setValue(categoryTrnJoin.categoryCode);
				bean.setLabel(categoryTrnJoin.categoryCodeName);
				this.poCategoryList.add(bean);
			}

			//都度発注区分プルダウンの値
			categoryJoinList = this.categoryService
					.findCategoryJoinById(Categories.IMMEDIATELY_PO_CATEGORY);
			for(CategoryJoin categoryTrnJoin : categoryJoinList) {
				LabelValueBean bean = new LabelValueBean();
				bean.setValue(categoryTrnJoin.categoryCode);
				bean.setLabel(categoryTrnJoin.categoryCodeName);
				this.immediatelyPOCategoryList.add(bean);
			}

		} catch (ServiceException e) {
			super.errorLog(e);
		}
	}

	/**
	 * Boolean型の引数の値をboolean型に変換します.
	 * @param l_Bool 変換対象のBoolean値
	 * @return 引数のBoolean値のboolean表現を返します.<br>
	 * 引数のBoolean値がNullの場合、falseを返します.
	 */
    private boolean Null2Bool(Boolean l_Bool) {
    	return (l_Bool!=null?l_Bool.booleanValue():false);
 	}

}
