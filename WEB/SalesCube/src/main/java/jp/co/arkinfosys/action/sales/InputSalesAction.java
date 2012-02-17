/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.sales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.CheckUtil;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.DiscountUtil;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.SalesLineTrn;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.sales.InputSalesForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.BillService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.DeliveryService;
import jp.co.arkinfosys.service.DepositSlipService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.RoLineService;
import jp.co.arkinfosys.service.RoSlipSalesService;
import jp.co.arkinfosys.service.SalesLineService;
import jp.co.arkinfosys.service.SalesService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.InputStockSalesService;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 売上伝票入力処理を実行するアクションクラスです
 *
 * @author Ark Information Systems
 *
 */
public class InputSalesAction extends AbstractSlipEditAction<SalesSlipDto, SalesLineDto> {
	/**
	 * 画面遷移用のマッピングクラス
	 */
	public static class Mapping {
		public static final String INPUT = "inputSales.jsp";
		public static final String INIT = "init";
	}

	
	@ActionForm
	@Resource
	public InputSalesForm inputSalesForm;

	
	@Resource
	private SalesService	salesService;

	@Resource
	private SalesLineService	salesLineService;

	@Resource
	protected CustomerService customerService;

	@Resource
	protected BillService billService;

	
	@Resource
	public ProductService productService;

	@Resource
	protected RoSlipSalesService roSlipSalesService;

	@Resource
	protected RoLineService roLineService;

	@Resource
	protected CategoryService categoryService;

	@Resource
	protected DeliveryService deliveryService;

	@Resource
	protected InputStockSalesService inputStockSalesService;

	@Resource
	protected DepositSlipService depositSlipService;

	
	
	public List<LabelValueBean> dcCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> dcTimeZoneCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> taxShiftCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> cutoffGroupCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> salesCmCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> deliveryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> preTypeCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> delivertProcessCategoryList = new ArrayList<LabelValueBean>();

	static private String RORDER_SLIP = "RORDER";
	static private String SALES_SLIP = "SALES";

	/**
	 * 伝票複写を行います.<br>
	 * <p>
	 * 受注伝票から複写処理を行うように拡張しています.<br>
	 * 以下の処理を行います.<br>
	 * 1. アクションフォームを初期化.<br>
	 * 2.1 複写対象が売上伝票の場合には、親クラスのcopyメソッドを呼び出します（実際に呼ばれる事はありません）.<br>
	 * 2.2 複写対象が受注伝票の場合には、3以降の処理を行います.<br>
	 * 2.3 複写対象がそれ以外の場合には、例外処理を呼び出します.<br>
	 *    以降は、複写対象が受注伝票の場合.
	 * 3. 受注伝票番号が設定されていない場合、エラーメッセージを登録して入力画面に遷移します.<br>
	 * 3. 受注伝票を複写して、売上伝票を作成します.<br>
	 * 4. プルダウンの要素を作成します.<br>
	 * </p>
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Override
	public String copy() throws Exception {
		prepareForm();
		inputSalesForm.initialize();

		try {

			if( SALES_SLIP.equals(inputSalesForm.copySlipName)){
				return super.copy();
			}else if( RORDER_SLIP.equals(inputSalesForm.copySlipName)){
				
				if( "".equals(inputSalesForm.copySlipId)){
					String strLabel = MessageResourcesUtil.getMessage("labels.roSlipId");
					addMessage("errors.notExist",strLabel);
					return Mapping.INPUT;
				}
				
				if(!createSalesSlipByRo(inputSalesForm.copySlipId)) {
					inputSalesForm.clear();
				}

			}else{
				throw new ServiceException("errors.system");
			}

			
			this.createList();

			
			
			if( StringUtil.hasLength( inputSalesForm.customerCode ) ){
				createDeliveryList();
			}
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		return Mapping.INPUT;
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.<br>
	 * 作成済の時は何もしません.<br>
	 * 配送業者リストを作成します.<br>
	 * 配送時間帯リストを作成します.<br>
	 * 税転嫁リストを作成します.<br>
	 * 支払条件リストを作成します.<br>
	 * 取引区分リストを作成します.<br>
	 * 敬称区分リストを作成します.<br>
	 * 完納区分リストを作成します.<br>
	 * 作成済にします.<br>
	 * @throws ServiceException
	 */
	protected void initCategoryList() throws ServiceException {

		
		if( inputSalesForm.initCategory == false ){
			return;
		}

		
		createCategoryList(Categories.DC_CATEGORY, dcCategoryList, true );

		
		createCategoryList(Categories.DC_TIMEZONE_CATEGORY, dcTimeZoneCategoryList, true );

		
		createCategoryList(Categories.ART_TAX_SHIFT_CATEGORY, taxShiftCategoryList, true );

		
		createCategoryList(Categories.CUTOFF_GROUP, cutoffGroupCategoryList, true );

		
		createCategoryList(Categories.SALES_CM_CATEGORY, salesCmCategoryList, true );

		
		createCategoryList(Categories.PRE_TYPE, preTypeCategoryList, true );

		
		createCategoryList(Categories.DELIVERY_PROCESS_CATEGORY, delivertProcessCategoryList, true );

		
		inputSalesForm.initCategory = false;
	}

	/**
	 * 区分マスタからリストを生成します.
	 * @param categoryType　区分マスタコード
	 * @param list 作成するリスト
	 * @param emptyString 最初の空欄選択肢があるか否か
	 * @throws ServiceException
	 */
	protected void createCategoryList(int categoryType, List<LabelValueBean> list , boolean emptyString) throws ServiceException{
		List<CategoryJoin> categoryJoinList =
			this.categoryService.findCategoryJoinById(categoryType);
		if( emptyString == true ){
			LabelValueBean bean = new LabelValueBean();
			bean.setValue("");
			bean.setLabel("");
			list.add(bean);
		}
		for (CategoryJoin categoryTrnJoin : categoryJoinList) {
			LabelValueBean bean = new LabelValueBean();
			bean.setValue(categoryTrnJoin.categoryCode);
			bean.setLabel(categoryTrnJoin.categoryCodeName);
			list.add(bean);
		}
	}
	/**
	 * 入力伝票の明細行の内容を確認します.<br>
	 * 空行の場合はチェックを行いません.<br>
	 * 指定された伝票の明細行に関連する商品の情報を取得します.<br>
	 * 存在しない商品が含まれている場合にはエラーメッセージを追加します.<br>
	 * 入力データの型が正しい事を確認し、異常値の場合にはエラーメッセージを追加します.<br>
	 * 数量が0の場合は、エラーメッセージを追加します.<br>
	 * 特殊商品コードでは無い場合には、仕入単価、仕入金額の型が正しい事を確認し、異常値の場合にはエラーメッセージを追加します.<br>
	 * 棚番が必須の商品の場合には、設定状況を確認し指定されていない場合には、エラーメッセージを追加します.<br>
	 * 数値が妥当な範囲内であるか確認し、異常値の場合にはエラーメッセージを追加します.<br>
	 * 取引区分がクレジットの場合に、分納しようとした場合には、エラーメッセージを追加します.<br>
	 * 正常なデータが１件もない場合には、エラーメッセージを追加します.<br>
	 *
	 * @param isf　ActionForm
	 * @return 処理継続可能であるか否か
	 * @throws ServiceException
	 */
	protected boolean checkLines(InputSalesForm isf ) throws ServiceException {

		int plus = 0;
		int minus = 0;
		int nCount = 0;
		boolean isCredit = false;
		
		for( SalesLineDto lineDto : isf.salesLineList ){
			if (lineDto.isBlank()) {
				continue;
			}
			ProductJoin pj = productService.findById(lineDto.productCode);
			if( pj == null ){
				String strLabel = MessageResourcesUtil.getMessage("labels.productCode");
				addMessage( "errors.line.dataNotExist", lineDto.lineNo, strLabel, lineDto.productCode );
			}else{
				nCount++;
			}

			
			if( checkInt(lineDto.quantity, lineDto.lineNo,
							"labels.quantity") ){
				Integer data = Integer.valueOf(lineDto.quantity);
				if( data < 0 ){
					minus++;
				}else if( data > 0 ){
					plus++;
				}
			}

			
			checkInt(lineDto.deliveryProcessCategory, lineDto.lineNo,
							"labels.deliveryProcessCategory");
			
			checkDouble(lineDto.unitCost, lineDto.lineNo,
							"labels.unitCost");
			
			Double dblCost = checkDouble(lineDto.cost, lineDto.lineNo,
							"labels.cost");
			
			checkDouble(lineDto.unitRetailPrice, lineDto.lineNo,
							"labels.unitRetailPrice");
			
			Double dblRetailPrice = checkDouble(lineDto.retailPrice, lineDto.lineNo,
							"labels.retailPrice");

			
			
			checkNum0Int(lineDto.quantity, lineDto.lineNo, "labels.quantity");

			if(!DiscountUtil.isExceptianalProduct( lineDto.productCode )) {
				
				checkNum0Double(lineDto.unitCost, lineDto.lineNo,
								"labels.unitCost");
				
				checkNum0Double(lineDto.cost, lineDto.lineNo,	"labels.cost");
			}
			
			checkNum0Int(lineDto.unitRetailPrice, lineDto.lineNo,
							"labels.unitRetailPrice");
			
			checkNum0Double(lineDto.retailPrice, lineDto.lineNo,
							"labels.retailPrice");

			
			if( pj != null ){
				lineDto.stockCtlCategory = pj.stockCtlCategory;
				if( CheckUtil.isRackCheck(pj) ){
					if( !StringUtil.hasLength(lineDto.rackCodeSrc)){
						String strLabel = MessageResourcesUtil.getMessage("labels.master.rackCode");
						addMessage( "errors.line.required", lineDto.lineNo, strLabel );
					}
				}
			}
			
			if( dblCost != null ){
				if(( dblCost < Constants.LIMIT_VALUE.PRICE_MIN )
				 ||( dblCost > Constants.LIMIT_VALUE.PRICE_MAX )){
					String strLabel = MessageResourcesUtil.getMessage("labels.cost");
					addMessage( "errors.line.range", lineDto.lineNo,
							strLabel,
							Constants.LIMIT_VALUE.PRICE_MIN.toString(),
							Constants.LIMIT_VALUE.PRICE_MAX.toString());
				}
			}
			if( dblRetailPrice != null ){
				if(( dblRetailPrice < Constants.LIMIT_VALUE.PRICE_MIN )
				 ||( dblRetailPrice > Constants.LIMIT_VALUE.PRICE_MAX )){
					String strLabel = MessageResourcesUtil.getMessage("labels.salesMoney");
					addMessage( "errors.line.range", lineDto.lineNo,
							strLabel,
							Constants.LIMIT_VALUE.PRICE_MIN.toString(),
							Constants.LIMIT_VALUE.PRICE_MAX.toString());
				}
			}
			if (StringUtil.hasLength(lineDto.remarks)
					&& lineDto.remarks.length() > 120) {
				String strLabel = MessageResourcesUtil.getMessage("labels.remarks");
				addMessage( "errors.line.maxlength", lineDto.lineNo,
						strLabel, "120" );
			}
			if (StringUtil.hasLength(lineDto.eadRemarks)
					&& lineDto.eadRemarks.length() > 120) {
				String strLabel = MessageResourcesUtil.getMessage("labels.eadRemarks");
				addMessage( "errors.line.maxlength", lineDto.lineNo,
						strLabel, "120" );
			}
			if (StringUtil.hasLength(lineDto.productRemarks)
					&& lineDto.eadRemarks.length() > 120) {
				String strLabel = MessageResourcesUtil.getMessage("labels.productRemarks");
				addMessage( "errors.line.maxlength", lineDto.lineNo,
						strLabel, "120" );
			}
			
			if( CategoryTrns.SALES_CM_CREDIT_CARD.equals( isf.salesCmCategory )
					&&(CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL.equals(lineDto.deliveryProcessCategory))){
				isCredit = true;
			}
		}

		
		if( isCredit){
			addMessage( "errors.valid.credit" );
		}
		if( nCount == 0 ){
			
			addMessage( "errors.noValidLine" );
			return false;
		}
		return true;

	}

	/**
	 * 顧客納入先リストを作成します.<br>
	 * 顧客コードが設定されていない時には、空行だけの納入先リストを作成します.<BR>
	 * 顧客コードが設定されている場合には、顧客マスタの納入先リストを作成します.
	 * @throws ServiceException
	 */
	protected void createDeliveryList() throws ServiceException{
		if( !StringUtil.hasLength( inputSalesForm.customerCode ) ){
			LabelValueBean bean = new LabelValueBean();
			bean.setValue("");
			bean.setLabel("");
			deliveryList.add(bean);
			return;
		}
		List<DeliveryAndPre> deliveryPreList;
		try {
			deliveryPreList =
				deliveryService.searchDeliveryListByCompleteCustomerCode(
					inputSalesForm.customerCode );

			LabelValueBean bean;
			for (DeliveryAndPre dap : deliveryPreList) {
				bean = new LabelValueBean();
				bean.setValue(dap.deliveryCode);
				bean.setLabel(dap.deliveryName);
				deliveryList.add(bean);
			}
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}
	/**
	 * 指定した文字列が整数か確認して、異常値の場合にはエラーメッセ―ジを登録します.
	 * @param value　文字列
	 * @return　整数か否か
	 */
	protected boolean checkInt( String value, String no, String label ){
		if( checkEmpty( value, no, label) == false ){	return false;	}
		try {
			String value2 = value.replaceAll(",", "");
			Integer.parseInt(value2);
		} catch (Exception e) {
			String strLabel = MessageResourcesUtil.getMessage(label);
			addMessage( "errors.line.integer", no, strLabel );
			return false;
		}
		return true;
	}
	/**
	 * 指定した文字列が小数か確認して、異常値の場合にはエラーメッセ―ジを登録します.
	 * @param value 文字列
	 * @return 文字列を変換したDouble値
	 */
	protected Double checkDouble( String value, String no, String label ){
		if( checkEmpty( value, no, label) == false ){	return null;	}
		try {
			String value2 = value.replaceAll(",", "");
			return Double.parseDouble(value2);
		} catch (Exception e) {
			String strLabel = MessageResourcesUtil.getMessage(label);
			addMessage( "errors.line.float", no, strLabel );
			return null;
		}
	}
	/**
	 * 指定した文字列が数値の場合にはtrueを返します.
	 * 整数か小数かは気にしません.
	 * @param value　文字列
	 * @return　数値か否か
	 */
	protected boolean checkEmpty( String value, String no, String label){
		if(( value == null)||( "".equals(value) == true )){
			String strLabel = MessageResourcesUtil.getMessage(label);
			addMessage( "errors.line.required", no, strLabel );
			return false;
		}
		return true;
	}

	/**
	 * 指定した文字列が整数で０であるか確認して、異常値の場合にはエラーメッセ―ジを登録します.
	 * @param value チェック対象値
	 * @param no 行番号
	 * @param label チェック対象項目名
	 * @return 正常値か否か
	 */
	protected boolean checkNum0Int( String value, String no, String label ){
		if(( value == null)||( "".equals(value) == true )){	return false;	}

		try {
			String value2 = value.replaceAll(",", "");
			Integer.parseInt(value2);
			int ivalue2 = Integer.parseInt(value2);
			if( ivalue2 == 0 ){
				String strLabel = MessageResourcesUtil.getMessage(label);
				addMessage( "errors.line.num0", no, strLabel );
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 指定した文字列が小数で０であるか確認して、異常値の場合にはエラーメッセ―ジを登録します.
	 * @param value チェック対象値
	 * @param no 行番号
	 * @param label チェック対象項目名
	 * @return 正常値か否か
	 */
	protected boolean checkNum0Double( String value, String no, String label ){
		if(( value == null)||( "".equals(value) == true )){	return false;	}
		try {
			String value2 = value.replaceAll(",", "");
			double dvalue2 = Double.parseDouble(value2);
			if( Double.compare( dvalue2, 0 ) == 0 ){
				String strLabel = MessageResourcesUtil.getMessage(label);
				addMessage( "errors.line.num0", no, strLabel );
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * プルダウンリストの値から文字列を取得してDTOに設定します.
	 *
	 * @param d 売上伝票用DTO
	 */
	private void setValueToName(SalesSlipDto d) {

		
		for(LabelValueBean lvb : dcCategoryList ){
			if( lvb.getValue().equals(inputSalesForm.dcCategory)){
				d.dcName = lvb.getLabel();
				break;
			}
		}
		
		for(LabelValueBean lvb : dcTimeZoneCategoryList ){
			if( lvb.getValue().equals(inputSalesForm.dcTimezoneCategory)){
				d.dcTimezone = lvb.getLabel();
				break;
			}
		}
		
		for(LabelValueBean lvb : preTypeCategoryList ){
			if( lvb.getValue().equals(inputSalesForm.deliveryPcPreCategory)){
				d.deliveryPcPre = lvb.getLabel();
				break;
			}
		}
		
		for(LabelValueBean lvb : preTypeCategoryList ){
			if( lvb.getValue().equals(inputSalesForm.baPcPreCategory)){
				d.baPcPre = lvb.getLabel();
				break;
			}
		}
	}

	/**
	 * 受注伝票を検索し売上伝票のアクションフォームに設定します.<BR>
	 *
	 * 1. 受注伝票を検索します。見つからなかった場合には、エラーメッセージを登録し処理を終了します.<BR>
	 * 2. 受注伝票の明細行を取得します.<BR>
	 * 3. 受注残数が設定されていない明細行は複写対象としません.<BR>
	 * 4. 完納区分が「完了」である明細行は複写対象としません.<BR>
	 * 5. 複写可能な明細行が存在しない場合には、エラーメッセージを登録し処理を終了します.<BR>
	 * 6. 受注伝票に設定されている顧客を検索し、見つからなかった場合には、エラーメッセージを登録します.<BR>
	 * 7. 受注伝票の内容をアクションフォームに設定します.<BR>
	 * 8. 受注伝票に存在しない情報を検索し、アクションフォームに設定します.<BR>
	 * 9. 顧客の最新の請求先情報を顧客マスタから取得し直して、アクションフォームに設定します.<BR>
	 *
	 * @param roSlipId 検索対象受注伝票番号
	 * @return 正常に設定できたか否か
	 * @throws ServiceException
	 */
	protected boolean createSalesSlipByRo( String roSlipId ) throws ServiceException {

		
		ROrderSlipDto rosDto = roSlipSalesService.loadBySlipId(roSlipId);
		if (rosDto == null){
			
			addMessage("errors.copy.notexist");
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return false;
		}

		List<ROrderLineDto> lineList = roLineService.loadBySlip(rosDto);
		rosDto.setLineDtoList(lineList);

		boolean brestQuantity = false;
		
		for( ROrderLineDto rolDto : rosDto.getLineDtoList() ){
			if( !StringUtil.hasLength( rolDto.restQuantity ) ){
				continue;
			}
			try {
				if(!Constants.STATUS_RORDER_LINE.SALES_FINISH.equals(rolDto.status)){
					
					brestQuantity = true;
					break;
				}
			} catch (Exception e) {
				continue;
			}
		}

		if (!brestQuantity) {
			
			addMessage("errors.copy.notexist");
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return false;
		}

		
		Customer customer = customerService.findCustomerByCode(rosDto.customerCode);
		if( customer == null ){
			
			String strLabel = MessageResourcesUtil.getMessage("labels.customerCode");
			addMessage( "errors.dataNotExist", strLabel, inputSalesForm.customerCode );
		}

		
		inputSalesForm.initialize( rosDto, customer );

		
		SalesSlipDto dto = (SalesSlipDto) inputSalesForm.copyToDto();
		dto.copyFrom(inputSalesForm.salesLineList);
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (SalesLineDto lineDto : salesLineList) {
			salesLineService.setStockInfoForm(lineDto);
		}

		
		List<DeliveryAndPre> delList = deliveryService.searchDeliveryByCompleteCustomerCode(rosDto.customerCode);
		if( delList.size() == 0 ){
			String strLabel = MessageResourcesUtil.getMessage("labels.customerCode");
			throw new ServiceException(strLabel);
		}else{
			
			inputSalesForm.initialize( delList.get(0) );
		}

		return true;
	}

	/**
	 * 顧客マスタの顧客情報と売上伝票の納入先情報が一致するか確認します.<BR>
	 * @param customer 顧客マスタエンティティ
	 * @param isf 売上伝票入力アクションフォーム
	 * @return 顧客マスタの顧客情報と売上伝票の納入先情報が一致するか否か
	 */
	protected boolean sameCheckCustomerAndDelivery(Customer customer,InputSalesForm isf) {
		if( !isf.deliveryName.equals(customer.customerName) ){			
			return false;
		}
		if( !isf.deliveryOfficeName.equals(customer.customerOfficeName) ){	
			return false;
		}
		if( !isf.deliveryDeptName.equals(customer.customerDeptName) ){		
			return false;
		}
		if( !isf.deliveryZipCode.equals(customer.customerZipCode) ){		
			return false;
		}
		if( !isf.deliveryAddress1.equals(customer.customerAddress1) ){		
			return false;
		}
		if( !isf.deliveryAddress2.equals(customer.customerAddress2) ){		
			return false;
		}
		if( !isf.deliveryPcName.equals(customer.customerPcName) ){		
			return false;
		}
		if( !isf.deliveryPcPreCategory.equals(customer.customerPcPreCategory) ){	
			return false;
		}
		if( !isf.deliveryTel.equals(customer.customerTel) ){			
			return false;
		}
		if( !isf.deliveryFax.equals(customer.customerFax) ){			
			return false;
		}
		return true;
	}

	/**
	 * 不要な明細行を削除します.<BR>
	 */
	private void deleteBlankLine() {
		
		for( int i = this.inputSalesForm.salesLineList.size()-1 ; i >= 0 ; i-- ){
			SalesLineDto lineDto = this.inputSalesForm.salesLineList.get(i);
			if (lineDto.isBlank()) {
				this.inputSalesForm.salesLineList.remove(i);
			}
		}
	}

	/**
	 * アクションフォームを返します.<br>
	 * @return InputSalesForm形式のアクションフォーム
	 */
	@Override
	protected AbstractSlipEditForm<SalesLineDto> getActionForm() {
		return this.inputSalesForm;
	}
	/**
	 * プルダウンの要素を作成します.<br>
	 * 区分情報を作成します.<br>
	 * 顧客納入先リストを作成します.<br>
	 *
	 * @throws Exception
	 */
	@Override
	protected void createList() throws Exception {
		
		initCategoryList();

		
		createDeliveryList();
	}

	/**
	 * 初期表示用のDTOを作成します.
	 * @return SalesSlipDto形式のDTO
	 */
	@Override
	protected AbstractSlipDto<SalesLineDto> createDTO() {
		return new SalesSlipDto();
	}

	/**
	 * 画面遷移先のURI文字列を返します.
	 * @return 売上伝票入力画面のURI文字列
	 */
	@Override
	protected String getInputURIString() {
		return Mapping.INPUT;
	}

	/**
	 * 伝票サービスクラスを返します.
	 * @return 売上伝票サービス
	 */
	@Override
	protected AbstractSlipService<SalesSlipTrn, SalesSlipDto> getSlipService() {
		return this.salesService;
	}

	/**
	 * 明細サービスクラスを返します.
	 * @return 売上明細行サービス
	 */
	@Override
	protected AbstractLineService<SalesLineTrn, SalesLineDto, SalesSlipDto> getLineService() {
		return this.salesLineService;
	}

	/**
	 * {@link AbstractSlipEditAction#upsert()}の保存処理で必要な追加サービスの配列を返します.<br>
	 * @return 受注伝票サービスを設定した配列
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		return new AbstractService[] { this.roSlipSalesService };
	}

	/**
	 * 登録または更新処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. 親クラスのupsertを呼び出します.<br>
	 * 2. 不要な明細行を削除します.<br>
	 * </p>
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Override
	@Execute(validator = true, validate="validate, @, validateAtCreateSlip", stopOnValidationError = false, input = "errorInit")
	public String upsert() throws Exception {
		String ret = super.upsert();
		deleteBlankLine();
		return ret;
	}

	/**
	 * 売上伝票を読み込みます.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. 売上伝票を読み込みアクションフォームに設定します.<br>
	 * 2. 顧客マスタを読み込みアクションフォームに設定します.<br>
	 * 3. 顧客情報と納入先情報が一致する時には、仮納品書を出力しません.<BR>
	 * 4. 顧客納品先リストを作成します.<BR>
	 * 5. 不要な明細行を削除します.<br>
	 * </p>
	 *
	 * @return 読み込みできたか否か
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected boolean loadData() throws Exception, ServiceException {
		
		SalesSlipDto dto = salesService.loadBySlipId(inputSalesForm.salesSlipId);

		if (dto == null) {
			inputSalesForm.clear();
			return false;
		}

		Beans.copy(dto, inputSalesForm).execute();

		
		List<SalesLineDto> lineList = salesLineService.loadBySlip(dto);
		dto.setLineDtoList(lineList);
		dto.fillList();
		inputSalesForm.setLineList(lineList);

		inputSalesForm.initLoad();

		Customer customer = customerService.findCustomerByCode(inputSalesForm.customerCode);
		if( customer != null ){
			inputSalesForm.initialize(customer);
			
			inputSalesForm.reportEFlag = !sameCheckCustomerAndDelivery(customer,inputSalesForm);
		}

		
		
		createDeliveryList();

		deleteBlankLine();

		return true;
	}

	/**
	 * {@link AbstractSlipEditAction#load()}の読み込み後に必要な処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. 不要な明細行を削除します.<br>
	 * 2. 売上伝票のステータスが「請求完了」の場合には、編集不可とします.<BR>
	 * 3. 売上伝票が売掛締め処理済みの場合には、編集不可とします.<BR>
	 * </p>
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected void afterLoad() throws Exception,
			ServiceException {

		deleteBlankLine();

		if( SalesSlipTrn.STATUS_FINISH.equals( this.inputSalesForm.status) ){

			
			String categoryName = categoryService.findCategoryNameByIdAndCode(
					SlipStatusCategories.SALES_SLIP_STATUS, this.inputSalesForm.status );
			
			String strSlipLabel = MessageResourcesUtil.getMessage("erroes.db.salesSlip");
			
			String strActionLabel = MessageResourcesUtil.getMessage("words.action.edit");
			
			addMessage("infos.slip.lock", strSlipLabel, categoryName, strActionLabel);
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

			this.inputSalesForm.menuUpdate = false;
		}else{
			if( StringUtil.hasLength(this.inputSalesForm.salesCutoffDate) ){

				
				String categoryName = MessageResourcesUtil.getMessage("labesl.closeArtBalance");
				
				String strSlipLabel = MessageResourcesUtil.getMessage("erroes.db.salesSlip");
				
				String strActionLabel = MessageResourcesUtil.getMessage("words.action.edit");
				
				addMessage("infos.slip.lock", strSlipLabel, categoryName, strActionLabel);
				ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

				this.inputSalesForm.menuUpdate = false;
			}
		}
	}

	/**
	 * {@link AbstractSlipEditAction#upsert()}の保存前に必要な処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. リストボックスの選択値から選択対象の文字列を売上伝票DTOに設定します.<br>
	 * 2. 請求書発行単位が売上伝票の場合、売上請求書番号を請求書サービスに対して設定します.<BR>
	 * 3. それ以外の場合には、アクションフォームの売上請求書番号を初期化します.<BR>
	 * </p>
	 * @param bInsert 新規登録か否か
	 * @param d 売上伝票のDTO
	 * @throws Exception
	 */
	@Override
	protected void beforeUpsert(boolean bInsert, AbstractSlipDto<SalesLineDto> d)
			throws Exception {

		SalesSlipDto dto = (SalesSlipDto) d;
		if (!bInsert) {
			
			if(this.inputStockSalesService.existsClosedEadSlip((SalesSlipDto) dto)) {
			}
		}
		
		setValueToName(dto);

		
		if( CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP.equals(inputSalesForm.billPrintUnit) ) {
			if (bInsert) {
				billService.setBillId(dto);
			} else 	if( !StringUtil.hasLength(inputSalesForm.salesBillId )){
				billService.setBillId(dto);
			}
		} else {
			if (!bInsert && StringUtil.hasLength(inputSalesForm.salesBillId)) {
				
				inputSalesForm.salesBillId = null;
			}
		}
	}

	/**
	 * {@link AbstractSlipEditAction#upsert()}保存後に必要な処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. 請求書発行単位が売上伝票の場合、売上請求書を登録・更新します.<BR>
	 * 2. 請求書発行単位が売上伝票以外で売上請求書番号が設定されている場合は、売上請求書を削除します.<BR>
	 * 3. 関連する受注伝票を更新します.<BR>
	 * 4. 入出庫伝票を登録・更新します.<BR>
	 * </p>
	 * @param bInsert 新規登録か否か
	 * @param d 売上伝票のDTO
	 * @throws Exception
	 */
	@Override
	protected void afterUpsert(boolean bInsert, AbstractSlipDto<SalesLineDto> d)
			throws Exception {

		SalesSlipDto dto = (SalesSlipDto) d;
		if( CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP.equals(inputSalesForm.billPrintUnit) ) {
			if (bInsert || !StringUtil.hasLength(inputSalesForm.salesBillId)) {
				
				
				billService.insertBillSales(dto);
			}else{
				


				
				billService.updateBillSales(dto);
			}
		} else {
			if (!bInsert && StringUtil.hasLength(inputSalesForm.salesBillId)) {
				
				SalesSlipDto dto2 = (SalesSlipDto) this.createDTO();
				dto2.salesBillId = inputSalesForm.salesBillId;
				
				billService.deleteBillSales(dto2);
			}
		}
		this.salesService.saveROrder(bInsert, (SalesSlipDto) dto);

		if (bInsert) {

			
			inputStockSalesService.insert(dto);

		} else {
			
			inputStockSalesService.update(dto);
		}

		ActionMessagesUtil.addMessages(this.httpRequest, this.messages);
	}

	/**
	 * 伝票登録時のバリデートを行います.<BR>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. 更新の場合、売上伝票に紐付く入出庫伝票が在庫締め済の場合には、エラーメッセージを登録します.<BR>
	 * 2. 顧客マスタに対応する情報が存在しない場合には、エラーメッセージを登録します.<BR>
	 * 3. 明細行のバリデートを行います.<BR>
	 * </p>
	 * @return 表示するメッセージ
	 * @throws ServiceException
	 */
	public ActionMessages validateAtCreateSlip() throws ServiceException{
		SalesSlipDto dto = (SalesSlipDto) inputSalesForm.copyToDto();
		if (!inputSalesForm.isNewData()) {
			if(this.inputStockSalesService.existsClosedEadSlip(dto)) {
				addMessage( "errors.update.stockclosed" );
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			}
		}

		try {
			
			Customer customer = customerService.findCustomerByCode(inputSalesForm.customerCode);
			if( customer == null ){
				
				String strLabel = MessageResourcesUtil.getMessage("labels.customerCode");
				addMessage( "errors.dataNotExist", strLabel, inputSalesForm.customerCode );
			}
		} catch (Exception e) {
			addMessage( "errors.system" );
		}
		try {
			
			checkLines(inputSalesForm);

		} catch (ServiceException e) {
			if (!"".equals(e.getMessage())) {
				addMessage(e.getMessage());
			} else {
				addMessage( "errors.system" );
			}
		} catch (Exception e) {
			addMessage( "errors.system" );
		}

		return messages;
	}

	/**
	 * 伝票削除時のバリデートを行います.<BR>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. 更新の場合、売上伝票に紐付く入出庫伝票が在庫締め済の場合には、エラーメッセージを登録します<BR>
	 * </p>
	 * @return 表示するメッセージ
	 * @throws ServiceException
	 */
	@Override
	public ActionMessages validateAtDeleteSlip() throws ServiceException {
		SalesSlipDto dto = (SalesSlipDto) inputSalesForm.copyToDto();
		
		if(this.inputStockSalesService.existsClosedEadSlip(dto)) {
			addMessage( "errors.delete.stockclosed" );
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		}
		return super.validateAtDeleteSlip();
	}

	/**
	 * {@link AbstractSlipEditAction#delete()}削除後に必要な処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. 請求書発行単位が売上伝票の場合、関連する売上請求書を削除します.<BR>
	 * 2. 関連する入出庫伝票を削除します.<BR>
	 * 3. 取引区分が「現金」の場合、関連する入金伝票を削除します.<BR>
	 * 4. アクションフォームを初期化します.
	 * </p>
	 * @param d 伝票のDTO
	 * @throws Exception
	 */
	@Override
	protected void afterDelete(AbstractSlipDto<SalesLineDto> d) throws Exception {

		SalesSlipDto dto = (SalesSlipDto) d;

		boolean doBill = false;	

		
		if( CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP.equals(inputSalesForm.billPrintUnit) ) {
			doBill = true;
		}

		if( doBill ){
			billService.deleteBillSales(dto);
		}

		inputStockSalesService.delete(dto);

		
		
		
		if( CategoryTrns.SALES_CM_CASH.equals(inputSalesForm.salesCmCategory) ){
			depositSlipService.deleteBySales(dto);
		}

		inputSalesForm.clear();
	}

	/**
	 * 伝票名を返します.
	 * @return 売上伝票名
	 */
	@Override
	public String getSlipKeyLabel() {
		return "labels.salesSlipId";
	}

}
