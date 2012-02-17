/*
 *  Copyright 2009-2010 Ark Information Systems.
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
		
		outputRecommendListForm.validInputPOrder = userDto
				.isMenuValid(Constants.MENU_ID.INPUT_PORDER);
		outputRecommendListForm.updateOutputRecommendList = userDto
				.isMenuUpdate(Constants.MENU_ID.OUTPUT_RECOMMEND_LIST);
		
		makeList();

		
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

			
			outputRecommendListForm.validInputPOrder = userDto
					.isMenuValid(Constants.MENU_ID.INPUT_PORDER);
			outputRecommendListForm.updateOutputRecommendList = userDto
					.isMenuUpdate(Constants.MENU_ID.OUTPUT_RECOMMEND_LIST);
			
			List<OutputRecommendOrderResultLine> orderResultList = new ArrayList<OutputRecommendOrderResultLine>();

			
			Map<String, Object> CommonParam = outputRecommendListService.createCommonParam();

			
			Map<String, Object> slipParam = new HashMap<String, Object>();
			slipParam.putAll(CommonParam);
			slipParam.put(OutputRecommendListService.SlipParam.SUPPLIER_CODE, outputRecommendListForm.supplierCode);
			slipParam.put(OutputRecommendListService.SlipParam.DELIVERY_DATE, outputRecommendListForm.deliveryDate);
			if(CategoryTrns.ENTRUST_PORDER.equals(outputRecommendListForm.poCategory)) {
				
				slipParam.put(OutputRecommendListService.SlipParam.TRANSPORT_CATEGORY, CategoryTrns.TRANSPORT_CATEGORY_ENTRUST);
			}
			else {
				slipParam.put(OutputRecommendListService.SlipParam.TRANSPORT_CATEGORY, outputRecommendListForm.transportCategory);
			}

			
			List<Map<String, Object>> lineParam = new ArrayList<Map<String, Object>>();

			
			OutputRecommendOrderResultLine orderResultLine = new OutputRecommendOrderResultLine();
			orderResultLine.poSlipId = null;
			orderResultLine.lines = new ArrayList<OutputRecommendOrderResultSlipLine>();
			
			int lineCount = 0;
			lineParam.clear();
			for(OutputRecommendListFormDto l_Result : outputRecommendListForm.searchResultList){
				
				if(Null2Bool(l_Result.validRow)){
					
					Map<String, Object> row_lineParam = new HashMap<String, Object>();
					row_lineParam.putAll(CommonParam);
					row_lineParam.put(OutputRecommendListService.SlipParam.PRODUCT_CODE, l_Result.productCode );
					row_lineParam.put(OutputRecommendListService.SlipParam.QUANTITY, l_Result.pOrderQuantity );
					
					lineParam.add(row_lineParam);
					
					OutputRecommendOrderResultLine.OutputRecommendOrderResultSlipLine orderResultSlipLine
								= new OutputRecommendOrderResultLine.OutputRecommendOrderResultSlipLine();
					orderResultSlipLine.productCode = l_Result.productCode;
					orderResultSlipLine.pOrderQuantity = l_Result.pOrderQuantity;
					orderResultLine.lines.add(orderResultSlipLine);
					
					lineCount++;
				}
				
				if(lineCount == InputPOrderForm.CONST_SLIP_LINE_MAX_DEFAULT){
					
					orderResultLine.poSlipId = String.valueOf(outputRecommendListService.createSlipByParam(slipParam, lineParam));
					orderResultLine.xlsFileName = getXlsFileName(orderResultLine.poSlipId);
					orderResultLine.pdfFileName = getPdfFileName(orderResultLine.poSlipId);
					orderResultLine.lineCount = lineCount;
					lineCount = 0;
					lineParam.clear();
					
					orderResultList.add(orderResultLine);
					orderResultLine = new OutputRecommendOrderResultLine();
					orderResultLine.poSlipId = null;
					orderResultLine.lines = new ArrayList<OutputRecommendOrderResultSlipLine>();
				}else if(lineCount > InputPOrderForm.CONST_SLIP_LINE_MAX_DEFAULT){
					return null;	
				}
			}
			
			if(lineCount > 0){
				
				orderResultLine.poSlipId = String.valueOf(outputRecommendListService.createSlipByParam(slipParam, lineParam));
				orderResultLine.xlsFileName = getXlsFileName(orderResultLine.poSlipId);
				orderResultLine.pdfFileName = getPdfFileName(orderResultLine.poSlipId);
				orderResultLine.lineCount = lineCount;
				orderResultList.add(orderResultLine);
			}
			
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
			
			List<CategoryJoin> categoryJoinList = this.categoryService
					.findCategoryJoinById(Categories.TRANSPORT_CATEGORY);
			for(CategoryJoin categoryTrnJoin : categoryJoinList) {

				
				if(CategoryTrns.TRANSPORT_CATEGORY_ENTRUST.equals(categoryTrnJoin.categoryCode)) {
					continue;
				}

				LabelValueBean bean = new LabelValueBean();
				bean.setValue(categoryTrnJoin.categoryCode);
				bean.setLabel(categoryTrnJoin.categoryCodeName);
				this.transportCategoryList.add(bean);
			}
			

			
			List<Supplier> l_supplierList = this.outputRecommendListService
				.findRecommendSuppliers();
			for(Supplier l_supplier : l_supplierList) {
				LabelValueBean bean = new LabelValueBean();
				bean.setValue(l_supplier.supplierCode);
				bean.setLabel(l_supplier.supplierName);
				this.supplierList.add(bean);
			}

			
			categoryJoinList = this.categoryService
					.findCategoryJoinById(Categories.PO_CATEGORY);
			for(CategoryJoin categoryTrnJoin : categoryJoinList) {
				LabelValueBean bean = new LabelValueBean();
				bean.setValue(categoryTrnJoin.categoryCode);
				bean.setLabel(categoryTrnJoin.categoryCodeName);
				this.poCategoryList.add(bean);
			}

			
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
