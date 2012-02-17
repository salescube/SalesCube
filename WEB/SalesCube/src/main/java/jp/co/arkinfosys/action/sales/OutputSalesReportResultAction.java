/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.sales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractReportWriterAction;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.PrintUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.dto.sales.OutputReportDataDto;
import jp.co.arkinfosys.dto.sales.OutputReportParamDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.entity.join.ProductSetJoin;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.OutputSalesReportSheetLineService;
import jp.co.arkinfosys.service.OutputSalesReportSheetService;
import jp.co.arkinfosys.service.PoSlipService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.ProductSetService;
import jp.co.arkinfosys.service.ProductStockService;
import jp.co.arkinfosys.service.SalesService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Execute;

/**
 * 売上帳票発行画面の検索結果をPDF出力するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputSalesReportResultAction extends AbstractReportWriterAction {

	/**
	 * キー項目
	 */
	public static class Key {
		public static final String PICKING_LIST_ID = "pickingListId";
		public static final String SET_TYPE_CATEGORY = "setTypeCategory";
		public static final String PRODUCT_CODE = "productCode";
		public static final String SET_PRODUCT_CODE = "setProductCode";
		public static final String QUANTITY= "quantity";
		public static final String STOCK_NUM = "stockNum";
		public static final String RACK_CODE_SRC = "rackCodeSrc";
		public static final String PURCHASE_DATE_1 = "purchaseDate1";
		public static final String PURCHASE_NUM_1 = "purchaseNum1";
		public static final String PURCHASE_DATE_2 = "purchaseDate2";
		public static final String PURCHASE_NUM_2 = "purchaseNum2";
		public static final String STOCK_CTL_CATEGORY = "stockCtlCategory";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String PICKING_REMARKS = "pickingRemarks";
	}

	/**
	 * 帳票テンプレートID
	 */
	public static final String REPORT_ID_A = Constants.REPORT_TEMPLATE.REPORT_ID_A;
	public static final String REPORT_ID_C = Constants.REPORT_TEMPLATE.REPORT_ID_C;
	public static final String REPORT_ID_D = Constants.REPORT_TEMPLATE.REPORT_ID_D;
	public static final String REPORT_ID_E = Constants.REPORT_TEMPLATE.REPORT_ID_E;
	public static final String REPORT_ID_F = Constants.REPORT_TEMPLATE.REPORT_ID_F;
	public static final String REPORT_ID_G = Constants.REPORT_TEMPLATE.REPORT_ID_G;
	public static final String REPORT_ID_H = Constants.REPORT_TEMPLATE.REPORT_ID_H;
	public static final String REPORT_ID_J = Constants.REPORT_TEMPLATE.REPORT_ID_J;
	public static final String REPORT_ID_K = Constants.REPORT_TEMPLATE.REPORT_ID_K;

	/**
	 * ダウンロードファイル名定義
	 */
	private static final String FILE_PREFFIX = "SALES_REPORT_";

	/**
	 * 日付表示フラグのキー値
	 */
	protected final String DISP_DATE_FLAG = "DISP_DATE_FLAG";

	/**
	 * クレジット決済コメント表示フラグのキー値
	 */
	protected final String DISP_CREDIT_CMT = "DISP_CREDIT_CMT";

	/**
	 * 請求書コメントのキー値
	 */
	protected final String BILL_CUTOFF_DATE  = "billCutOffDate";

	/**
	 * 納品書番号の先頭文字
	 */
	protected final String ID_HEAD_CLM  = "ID_HEAD_CLM";

	/**
	 * セット品の子商品に対して渡すダミーカテゴリ
	 */
	protected final String SET_CHILD  = "C";

	/**
	 * 納品書フッタ固定文言出力フラグ
	 */
	protected final String REPORT_D_FOOTER  = "reportDFooter";

	private String printKind;
    public List<OutputReportParamDto> resultList = new ArrayList<OutputReportParamDto>();
    public List<OutputReportDataDto> reportDataList = new ArrayList<OutputReportDataDto>();
    public Map<Integer,Integer> pickingListIdMap = new HashMap<Integer,Integer>();

	@Resource
	protected OutputSalesReportSheetService outputSalesReportSheetService;

	@Resource
	protected OutputSalesReportSheetLineService outputSalesReportSheetLineService;

	@Resource
	private SalesService salesService;

	@Resource
	private ProductStockService productStockService;

	@Resource
	private PoSlipService poSlipService;

	@Resource
	private ProductSetService productSetService;

	@Resource
	private ProductService productService;

	@Resource
	protected CustomerService customerService;

	/**
	 * 当日日付を元に拡張子を除いたファイル名を返します.<BR>
	 * @return SALES_REPORT_YYYY_MM_DD_hhmmとなる文字列
	 */
	@Override
	protected String getFilePreffix() {
		return FILE_PREFFIX
		+ StringUtil.getCurrentDateString(Constants.FORMAT.DATE)
		+ "_"
		+ StringUtil.getCurrentDateString(Constants.FORMAT.HOUR)
		+ StringUtil.getCurrentDateString(Constants.FORMAT.MINUTE);
	}

	/**
	 * 実ファイル名を返します.<br>
	 * 出力件数を超えた場合にはnullを返します.<br>
	 * @param index 取得する帳票のインデックス
	 * @return 出力ファイル名
	 */
	@Override
	protected String getRealFilePreffix(int index){
		
		if(index >= reportDataList.size()){
			return null;
		}

		return reportDataList.get(index).outputFileName;
	}

	/**
	 * レポートテンプレートIDを返します.<BR>
	 * 出力件数を超えた場合にはnullを返します.<br>
	 * @param index 取得するテンプレートのインデックス
	 * @return レポートテンプレートID
	 */
	@Override
	protected String getReportId(int index) {
		
		if(index >= reportDataList.size()){
			return null;
		}
		return reportDataList.get(index).reportFile;
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
		
		if(index >= reportDataList.size()){
			return null;
		}

		
		BeanMap params = Beans.createAndCopy(BeanMap.class, reportDataList.get(index)).execute();

		
		String reportFile = reportDataList.get(index).reportFile;
		BeanMap beanMap;

		
		if(REPORT_ID_J.equals(reportFile)
				|| REPORT_ID_K.equals(reportFile)){
			beanMap = outputSalesReportSheetService.findPickingSheetByCondition(params);

			
			if(beanMap == null){
				return null;
			}

			
			pickingListIdMap.put(index, (Integer)beanMap.get("pickingListId"));
		}else if(REPORT_ID_C.equals(reportFile)
					|| REPORT_ID_G.equals(reportFile)){
			beanMap = outputSalesReportSheetService.findSalesSheetByConditionAddDate(params);
		}else{
			beanMap = outputSalesReportSheetService.findSalesSheetByCondition(params);
		}

		
		beanMap.put(DISP_DATE_FLAG, reportDataList.get(index).dispDateFlag);

		
		if(REPORT_ID_F.equals(reportFile)){
			if(Constants.SALES_CM_CATEGORY_NAME.CATEGORY_CREDIT_CARD.equals(beanMap.get("salesCmCategory"))){
				beanMap.put(DISP_CREDIT_CMT, true);
			}
			else{
				beanMap.put(DISP_CREDIT_CMT, false);
			}
		}

		Customer customer = customerService.findCustomerByCode(beanMap.get("customerCode").toString());

		
		if(REPORT_ID_D.equals(reportFile)){
			
			if( CategoryTrns.BILL_PRINT_UNIT_BILL_CLOSE.equals(customer.billPrintUnit) ){
				if(beanMap.get("billCutoffGroup").equals("31")){
					beanMap.put(BILL_CUTOFF_DATE, "(" + Constants.BILL_CUTOFF_GROUP_NAME.CUTOFF_END + ")");
				}else if(beanMap.get("billCutoffGroup").equals("99")){
					beanMap.put(BILL_CUTOFF_DATE, "");
				}
				else{
					beanMap.put(BILL_CUTOFF_DATE, "(" + beanMap.get("billCutoffGroup")+ "日)");
				}
			}
			else{
				beanMap.put(BILL_CUTOFF_DATE, "");
			}
		}
		if((REPORT_ID_D.equals(reportFile))||(REPORT_ID_E.equals(reportFile))){
			
			beanMap.put(ID_HEAD_CLM, "");

			
			
			if( CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP.equals(customer.billPrintUnit) ) {
				beanMap.put(REPORT_D_FOOTER, "0");	
			} else if( CategoryTrns.BILL_PRINT_UNIT_BILL_CLOSE.equals(customer.billPrintUnit) ) {
				beanMap.put(REPORT_D_FOOTER, "1");	
			}
		}

		return beanMap;
	}

	/**
	 * 明細情報を返します.<BR>
	 * 出力件数を超えた場合にはnullを返します.<br>
	 * ピッキングリストの場合、出荷指示明細行以外から取得する現在のデータを設定します<BR>
	 * 組み立て指示書の場合、セット商品情報を展開して設定します.<BR>
	 * @param index 取得する帳票のインデックス
	 * @return 明細情報のリスト
	 * @throws ServiceException
	 */
	@Override
	protected List<BeanMap> getDetailList(int index) throws ServiceException {
		
		if(index >= reportDataList.size()){
			return null;
		}

		
		BeanMap params = Beans.createAndCopy(BeanMap.class, reportDataList.get(index)).execute();

		
		String reportFile = reportDataList.get(index).reportFile;
		List<BeanMap> beanMapList;
		List<BeanMap> tempBeanMapList;

		if(REPORT_ID_J.equals(reportFile)){
			params.put(Key.PICKING_LIST_ID, pickingListIdMap.get(index));
			beanMapList = outputSalesReportSheetLineService.findPickingLineSheetByCondition(params);

			
			addPickingData(beanMapList);
			PrintUtil.removeSpaceToExceptianalProductLine(beanMapList);
		}else if(REPORT_ID_K.equals(reportFile)){
			params.put(Key.PICKING_LIST_ID, pickingListIdMap.get(index));
			params.put(Key.SET_TYPE_CATEGORY, CategoryTrns.PRODUCT_SET_TYPE_SET);
			tempBeanMapList = outputSalesReportSheetLineService.findPickingLineSheetByCondition(params);
			beanMapList = createAssembleInstructiongData(tempBeanMapList);
			PrintUtil.removeSpaceToExceptianalProductLine(beanMapList);
		}else{
			beanMapList = outputSalesReportSheetLineService.findSalesLineSheetByCondition(params);
			PrintUtil.setSpaceToExceptianalProductCode(beanMapList);
		}

		return beanMapList;
	}

	/**
	 * ファイル出力のためのデータを作成します.<BR>
	 *
	 */
	private void createReportFileData(){
		
		for(int i=0;i<resultList.size();i++){
			OutputReportParamDto paramDto = resultList.get(i);

			
			paramDto.createFileCommaToList();

			
			if(paramDto.reportFileList == null
					|| paramDto.reportFileList.size() <= 0){
				continue;
			}

			for(int j=0;j<paramDto.reportFileList.size();j++){
				OutputReportDataDto dataDto = new OutputReportDataDto();
				dataDto.salesSlipId = paramDto.salesSlipId;
				dataDto.roSlipId = paramDto.roSlipId;
				dataDto.reportFile = paramDto.reportFileList.get(j);

				if(paramDto.dispDateFlag == true){
					dataDto.dispDateFlag = Boolean.TRUE;
				}else{
					dataDto.dispDateFlag = Boolean.FALSE;
				}

				
				if(Constants.REPORT_FORMAT.EXCEL.equals(printKind)
						&& j+1 == paramDto.reportFileList.size()){
					dataDto.outputFileName = "SALES" + dataDto.salesSlipId;
				}

				reportDataList.add(dataDto);
			}
		}

	    Collections.sort(reportDataList, new Comparator<OutputReportDataDto>(){
	    			public int compare(OutputReportDataDto ordd1, OutputReportDataDto ordd2) {
	    				if( StringUtil.hasLength(ordd1.roSlipId) ){
		    				if( StringUtil.hasLength(ordd2.roSlipId) ){
			    				if( ordd1.roSlipId.equals(ordd2.roSlipId) ){
			    					return Double.valueOf(ordd1.salesSlipId).compareTo(Double.valueOf(ordd2.salesSlipId));
			    				}else{
			    					return Double.valueOf(ordd1.roSlipId).compareTo(Double.valueOf(ordd2.roSlipId));
			    				}
		    				}else{
		    					return 1;
		    				}
	    				}else{
		    				if( StringUtil.hasLength(ordd2.roSlipId) ){
		    					return -1;
		    				}else{
		    					return Double.valueOf(ordd1.salesSlipId).compareTo(Double.valueOf(ordd2.salesSlipId));
		    				}
	    				}
	    			}
	    		}
	    );
	}

	/**
	 * Excel形式で売上帳票を作成し、レスポンスに出力します.<br>
	 * 実際に呼ばれる事はありません.<br>
	 * @return 処理結果文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String excel() throws Exception {
		this.printKind = Constants.REPORT_FORMAT.EXCEL;

		
		createReportFileData();

		
		updatePrintCount();

		
		return super.excel();
	}

	/**
	 * PDF形式で売上帳票を作成し、レスポンスに出力します.<br>
	 * @return 処理結果文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String pdf() throws Exception {
		this.printKind = Constants.REPORT_FORMAT.PDF;

		
		createReportFileData();

		
		updatePrintCount();

		
		return super.pdf();
	}

	/**
	 * 帳票を発行した売上伝票の出力情報を更新します.
	 *
	 * @throws Exception
	 */
	private void updatePrintCount() throws ServiceException{
		for(OutputReportParamDto dto : resultList){
			salesService.updatePrintCount(dto.salesSlipId, dto.reportFileList);
		}
	}

	/**
	 * ピッキングリスト出力に必要な動的データを追加します.<BR>
	 * セット品の場合には、子のリストを生成します.
	 *
	 * @param beanMapList 出力対象明細行情報
	 * @throws ServiceException
	 */
	protected void addPickingData(List<BeanMap> beanMapList) throws ServiceException{
		int i = 0;
		for(i = 0 ; i < beanMapList.size() ; i++ ){
			BeanMap beanMap = beanMapList.get(i);
			String setTypeCategory = (String)beanMap.get(Key.SET_TYPE_CATEGORY);
			String productCode = (String)beanMap.get(Key.PRODUCT_CODE); 
			if( SET_CHILD.equals(setTypeCategory)){
				continue;
			}

			
			beanMap.put(Key.PURCHASE_DATE_1, null);
			beanMap.put(Key.PURCHASE_NUM_1, null);
			beanMap.put(Key.PURCHASE_DATE_2, null);
			beanMap.put(Key.PURCHASE_NUM_2, null);

			
			if(!CategoryTrns.PRODUCT_SET_TYPE_SET.equals(setTypeCategory)){
				List<BeanMap> scheduleList = poSlipService.findPurchaseSchedule(productCode);
				for(BeanMap schedule : scheduleList) {
					beanMap.putAll(schedule);
					break;
				}
			}
			
			BigDecimal quantity = (BigDecimal)beanMap.get(Key.QUANTITY);

			
			ProductJoin pj = productService.findById(productCode);
			beanMap.put(Key.STOCK_NUM, null);
			if( pj != null ){
				if( CategoryTrns.PRODUCT_STOCK_CTL_YES.equals(pj.stockCtlCategory)){ 
					StockInfoDto dto = productStockService.calcStockQuantityByProductCode(productCode);
					
					beanMap.put(Key.STOCK_NUM, new BigDecimal(dto.currentTotalQuantity).add(quantity));
				}
			}
			
			if(CategoryTrns.PRODUCT_SET_TYPE_SET.equals(setTypeCategory)){
				List<BeanMap> childBeanMapList = addPickingChildData(beanMap);
				int parentIndex = beanMapList.indexOf(beanMap);
				parentIndex++;
				for(BeanMap childBeanMap : childBeanMapList){
					beanMapList.add(parentIndex++, childBeanMap);
				}
			}
		}
	}

	/**
	 * ピッキングリスト出力に必要なセット品の子部品リストを作ります.
	 *
	 * @param parentBeanMap 親の明細情報
	 * @return 子部品の明細情報リスト
	 * @throws ServiceException
	 */
	protected List<BeanMap> addPickingChildData(BeanMap parentBeanMap) throws ServiceException{

		
		List<BeanMap> resultBeanList = new ArrayList<BeanMap>();
		try{

			String setProductCode = (String)parentBeanMap.get(Key.PRODUCT_CODE); 
			List<ProductSetJoin> productSetList = productSetService.findProductSetByProductCode(setProductCode);
			for(ProductSetJoin productSet : productSetList){
				BeanMap beanMap = new BeanMap();
				
				beanMap.put(Key.PRODUCT_CODE, null);
				beanMap.put(Key.QUANTITY, null);
				beanMap.put(Key.RACK_CODE_SRC, null);
				beanMap.put(Key.PURCHASE_DATE_1, null);
				beanMap.put(Key.PURCHASE_NUM_1, null);
				beanMap.put(Key.PURCHASE_DATE_2, null);
				beanMap.put(Key.PURCHASE_NUM_2, null);
				beanMap.put(Key.STOCK_NUM, null);
				beanMap.put(Key.PICKING_REMARKS, null);

				
				beanMap.put(Key.SET_TYPE_CATEGORY,SET_CHILD);
				
				beanMap.put(Key.PRODUCT_CODE,productSet.productCode);
				
				BigDecimal parentQuantity = (BigDecimal)parentBeanMap.get(Key.QUANTITY);
				beanMap.put(Key.QUANTITY, productSet.quantity.multiply( parentQuantity ) );

				
				List<BeanMap> scheduleList = poSlipService.findPurchaseSchedule(productSet.productCode);
				for(BeanMap schedule : scheduleList) {
					beanMap.putAll(schedule);
					break;
				}

				ProductJoin pj = productService.findById(productSet.productCode);
				if( pj != null ){
					
					String rackCode = pj.rackCode;
					beanMap.put(Key.RACK_CODE_SRC, rackCode);
					
					beanMap.put(Key.PRODUCT_ABSTRACT, pj.productName);
					
					beanMap.put(Key.PICKING_REMARKS, pj.eadRemarks);

					
					if( CategoryTrns.PRODUCT_STOCK_CTL_YES.equals(pj.stockCtlCategory)){ 
						StockInfoDto dto = productStockService.calcStockQuantityByProductCode(productSet.productCode);
						
						beanMap.put(Key.STOCK_NUM, new BigDecimal(dto.currentTotalQuantity).add(productSet.quantity.multiply( parentQuantity )));
					}else{
						beanMap.put(Key.STOCK_NUM, null);
					}
				}

				resultBeanList.add(beanMap);
			}

		}catch (Exception e) {
			throw new ServiceException(e);
		}
		return resultBeanList;
	}

	/**
	 * 組み立て指示書出力に必要なデータを返します.<BR>
	 * 在庫管理する商品の場合には、現在庫数を設定します.
	 * @param paramBeanMapList　出荷指示書明細行データリスト
	 * @throws Exception
	 */
	protected List<BeanMap> createAssembleInstructiongData(List<BeanMap> paramBeanMapList) throws ServiceException{
		
		List<BeanMap> resultBeanList = new ArrayList<BeanMap>();

		
		for(BeanMap paramBeanMap : paramBeanMapList){
			String setProductCode = (String)paramBeanMap.get(Key.PRODUCT_CODE); 
			List<ProductSetJoin> productSetList = productSetService.findProductSetByProductCode(setProductCode);
			Boolean parentSet = false;
			for(ProductSetJoin productSet : productSetList){
				BeanMap beanMap = new BeanMap();
				
				beanMap.put(Key.SET_PRODUCT_CODE, null);
				beanMap.put(Key.PRODUCT_CODE, null);
				beanMap.put(Key.QUANTITY, null);
				beanMap.put(Key.RACK_CODE_SRC, null);
				beanMap.put(Key.PURCHASE_DATE_1, null);
				beanMap.put(Key.PURCHASE_NUM_1, null);
				beanMap.put(Key.PURCHASE_DATE_2, null);
				beanMap.put(Key.PURCHASE_NUM_2, null);
				beanMap.put(Key.STOCK_NUM, null);
				
				BigDecimal parentQuantity = (BigDecimal)paramBeanMap.get(Key.QUANTITY);

				
				if( !parentSet ){
					
					beanMap.put(Key.SET_PRODUCT_CODE,setProductCode);
					beanMap.put(Key.PRODUCT_CODE, "");
					beanMap.put(Key.QUANTITY, new BigDecimal(1));
					beanMap.put(Key.RACK_CODE_SRC, "");
					resultBeanList.add(beanMap);
					beanMap = new BeanMap();
					beanMap.put(Key.SET_PRODUCT_CODE, null);
					parentSet = true;
				}
				
				beanMap.put(Key.PRODUCT_CODE,productSet.productCode);

				
				List<BeanMap> scheduleList = poSlipService.findPurchaseSchedule(productSet.productCode);
				for(BeanMap schedule : scheduleList) {
					beanMap.putAll(schedule);
					break;
				}

				ProductJoin pj = productService.findById(productSet.productCode);
				if( pj != null ){
					
					String rackCode = pj.rackCode;
					beanMap.put(Key.RACK_CODE_SRC, rackCode);

					
					if( CategoryTrns.PRODUCT_STOCK_CTL_YES.equals(pj.stockCtlCategory)){ 
						StockInfoDto dto = productStockService.calcStockQuantityByProductCode(productSet.productCode);
						beanMap.put(Key.STOCK_NUM, new BigDecimal(dto.currentTotalQuantity).add(parentQuantity.multiply( productSet.quantity )));
					}else{
						beanMap.put(Key.STOCK_NUM, null);
					}
				}

				
				beanMap.put(Key.QUANTITY, productSet.quantity);

				resultBeanList.add(beanMap);
			}

		}
		return resultBeanList;
	}
}
