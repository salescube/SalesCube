/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.porder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.porder.InputPOrderLineDto;
import jp.co.arkinfosys.dto.porder.InputPOrderSlipDto;
import jp.co.arkinfosys.entity.PoLineTrn;
import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.entity.Supplier;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.porder.InputPOrderForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.porder.InputPOrderLineService;
import jp.co.arkinfosys.service.porder.InputPOrderSlipService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;
/**
 * 発注入力画面のアクションクラスです.
 * @author Ark Information Systems
 */
public class InputPOrderAction extends AbstractSlipEditAction<InputPOrderSlipDto, InputPOrderLineDto> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "inputPOrder.jsp";

	}

	/**
	 * 運送便区分リスト
	 */
	public List<LabelValueBean> transportCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 敬称リスト
	 */
	public List<LabelValueBean> preTypeCategoryList = new ArrayList<LabelValueBean>();
	
	/**
	 * 消費税率プルダウン
	 */
	public List<LabelValueBean> ctaxRateList = new ArrayList<LabelValueBean>();

	/**
	 * アクションフォーム
	 */
	@ActionForm
	@Resource
	protected InputPOrderForm inputPOrderForm;

	/**
	 * 区分情報
	 */
	@Resource
	private CategoryService categoryService;

	/**
	 * 発注入力専用サービス
	 */
	@Resource
	private InputPOrderSlipService inputPOrderSlipService;

	@Resource
	private InputPOrderLineService inputPOrderLineService;

	@Resource
	private SupplierService supplierService;

	@Resource
	protected HttpServletRequest request;

	@Resource
	private ProductService productService;

	//セッション
	public HttpSession session;

	/**
	 * アクションフォームを返します.
	 * @return {@link InputPOrderForm}
	 */
	protected AbstractSlipEditForm<InputPOrderLineDto> getActionForm() {
		return this.inputPOrderForm;
	}

	/**
	 * 新規発注伝票DTOを作成します.
	 * @return {@link InputPOrderSlipDto}
	 */
	@Override
	protected AbstractSlipDto<InputPOrderLineDto> createDTO() {
		return new InputPOrderSlipDto();
	}

	/**
	 * 遷移先のURIを返します.
	 * @return 発注入力画面のURI
	 */
	@Override
	protected String getInputURIString() {
		return InputPOrderAction.Mapping.INPUT;
	}

	/**
	 * 伝票サービスを返します.
	 * @return {@link InputPOrderSlipService}
	 */
	@Override
	protected AbstractSlipService<PoSlipTrn,InputPOrderSlipDto> getSlipService() {
		return this.inputPOrderSlipService;
	}

	/**
	 * 登録時に使用するサービスを返します.
	 * @return サービスリスト
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		return new AbstractService[] { this.productService };
	}

	/**
	 * 登録/更新の後処理を行います.<br>
	 * 未使用です.
	 * @param bInsert 新規登録か否か
	 * @param param 支払伝票DTO
	 * @throws Exception
	 */
	@Override
	protected void afterUpsert(boolean bInsert, AbstractSlipDto<InputPOrderLineDto> param) throws Exception {
	}

	/**
	 * 明細行サービスを返します.
	 * @return {@link InputPOrderLineService}
	 */
	@Override
	protected AbstractLineService<PoLineTrn,InputPOrderLineDto,InputPOrderSlipDto> getLineService() {
		return this.inputPOrderLineService;
	}

	/**
	 * 発注伝票のラベルのキーを返します.
	 * @return 発注伝票のラベルのキー
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#getSlipKeyLabel()
	 */
	@Override
	public String getSlipKeyLabel() {
		return "labels.poSlipId";
	}


	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws ServiceException
	 */
	@Override
	protected void createList() throws ServiceException{
		try {
			//運送便区分プルダウンの値
			List<CategoryJoin> categoryJoinList = this.categoryService
					.findCategoryJoinById(Categories.TRANSPORT_CATEGORY);
			for (CategoryJoin categoryTrnJoin : categoryJoinList) {
				LabelValueBean bean = new LabelValueBean();
				bean.setValue(categoryTrnJoin.categoryCode);
				bean.setLabel(categoryTrnJoin.categoryCodeName);
				this.transportCategoryList.add(bean);
			}

			//敬称プルダウンの値
			categoryJoinList = this.categoryService
				.findCategoryJoinById(Categories.PRE_TYPE);
			for (CategoryJoin categoryTrnJoin : categoryJoinList) {
				LabelValueBean bean = new LabelValueBean();
				bean.setValue(categoryTrnJoin.categoryCode);
				bean.setLabel(categoryTrnJoin.categoryCodeName);
				this.preTypeCategoryList.add(bean);
			}
			//通貨単位のリスト
			inputPOrderForm.cUnitSignList = supplierService.getCUnitSignList();
			
			// 消費税率プルダウンリスト
			this.ctaxRateList =  ListUtil.getRateTaxList(super.taxRateService);

			//モード設定
			inputPOrderForm.lockMode = false;
			//権限による制御
			setValidMode();
			//制御統合
			inputPOrderForm.lockMode = (inputPOrderForm.lockMode || inputPOrderForm.ROMode);


		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}


	/**
	 * 伝票データを取得します.
	 * @return 読み込みできたか否か
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected boolean loadData() throws Exception, ServiceException {
		try {

			// 発注伝票をロードする
			InputPOrderSlipDto dto = (InputPOrderSlipDto) inputPOrderSlipService
					.loadBySlipId(inputPOrderForm.poSlipId);
			if( dto == null ){
				return false;
			}
			
			dto.taxRate = dto.ctaxRate;

			Beans.copy(dto, inputPOrderForm).execute();

			// 発注明細情報をロードする
			List<InputPOrderLineDto> lineDtoList = inputPOrderLineService.loadBySlip(dto);
			inputPOrderForm.poLineList = lineDtoList;

			inputPOrderForm.initLoad();

			setCannotEditSlipReason();

			//伝票状態による制御
			inputPOrderForm.lockMode = (inputPOrderForm.lockMode ||
				((inputPOrderForm.status != null)?
						((Constants.STATUS_PORDER_SLIP.PURCHASED).equals(inputPOrderForm.status)):false));

			return true;

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}

	/**
	 * 発注伝票と発注伝票明細行のチェックを行います.
	 * @return アクションメッセージ
	 * @throws ServiceException
	 */
	public ActionMessages validateAtCreateSlip() throws ServiceException{
		ActionMessages errors = new ActionMessages();
		ActionMessage tempMsg;

		//多重登録防止用：同期トークンをチェック
//		if(!TokenProcessor.getInstance().isTokenValid(request,true)){
//			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.multipleAction"));
//		}
		//伝票部チェック
		//伝票納期(不正なら空文字列にしておく)
		String[] mssageValues = new String[1];
		mssageValues[0] = MessageResourcesUtil.getMessage("labels.deliveryDate");
		tempMsg = ValidateUtil.dateType(this.inputPOrderForm.deliveryDate, Constants.FORMAT.DATE, true, "errors.date",mssageValues );
		String SlipDeliveryDate = this.inputPOrderForm.deliveryDate;
		if(tempMsg != null){
			errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
			SlipDeliveryDate = "";
		}
		try {
			if(StringUtil.hasLength(inputPOrderForm.supplierCode)){
				Supplier supplier = supplierService.findById(inputPOrderForm.supplierCode);
				if( supplier == null ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.invalid", MessageResourcesUtil
									.getMessage("labels.supplierCode")));
				}
			}
		} catch (ServiceException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.invalid", MessageResourcesUtil
							.getMessage("labels.supplierCode")));
		}


		//明細行部チェック

		//有効行カウンタ
		int validLinesCount = 0;
		// 商品コード存在チェック
		for (InputPOrderLineDto line : this.inputPOrderForm.poLineList ) {
			//有効行チェッカ
			boolean validLineCheck = true;

			if( line.isBlank() ){
				continue;
			}
			//商品コード書式チェック
			if(!line.productCode.matches(Constants.CODE_MASK.PRODUCT_MASK)){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.line.invalid",line.lineNo,MessageResourcesUtil.getMessage("labels.productCode")));
				validLineCheck = false;
			}
			//相手先商品コード書式チェック
			if(!line.supplierPcode.matches(Constants.CODE_MASK.PRODUCT_MASK)){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.line.invalid",line.lineNo,MessageResourcesUtil.getMessage("labels.productCode")));
				validLineCheck = false;
			}
			// 商品マスタ存在チェック
			// 商品がセット品の場合は、エラー
			ProductJoin pj = productService.findById(line.productCode);
			if( pj != null ){
				if( pj.setTypeCategory.equals(CategoryTrns.PRODUCT_SET_TYPE_SET)){
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.product.set",line.lineNo));
					validLineCheck = false;
				}
			}else{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.invalid", line.lineNo, MessageResourcesUtil
								.getMessage("labels.productCode")));
				validLineCheck = false;
			}
			//数量
			if(!StringUtil.hasLength(line.quantity)){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.line.required",line.lineNo,MessageResourcesUtil.getMessage("labels.quantity")));
				validLineCheck = false;
			}else {
				try{
					int iQuantity = ((Double)Double.parseDouble(line.quantity)).intValue();
					if( iQuantity == 0 ){
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.num0",line.lineNo,MessageResourcesUtil.getMessage("labels.quantity")));
						validLineCheck = false;
					}
					tempMsg = ValidateUtil.decimalType(Integer.valueOf(line.lineNo), line.quantity, MessageResourcesUtil.getMessage("labels.quantity"), 12, 3);
					if( tempMsg != null ){
						errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
						validLineCheck = false;
					}
					// 運送便区分「委託在庫」で赤伝は許容しない
					if(CategoryTrns.TRANSPORT_CATEGORY_ENTRUST.equals(this.inputPOrderForm.transportCategory) && iQuantity < 0) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.num.entrust",line.lineNo));
						validLineCheck = false;
					}
				}catch (Exception e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.float", line.lineNo,
									MessageResourcesUtil.getMessage("labels.quantity")));
				}
			}

			//円単価
			if((!StringUtil.hasLength(this.inputPOrderForm.rateId)) && (!StringUtil.hasLength(line.unitPrice))){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.line.required",line.lineNo,MessageResourcesUtil.getMessage("labels.unitPrice")));
				validLineCheck = false;
			}
			tempMsg = ValidateUtil.decimalType(Integer.valueOf(line.lineNo), line.unitPrice, MessageResourcesUtil.getMessage("labels.unitPrice"), 9, 0);
			if(tempMsg != null){
				errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
				validLineCheck = false;
			}
			else if(StringUtil.hasLength(line.unitPrice)){
				float funitPrice = Float.parseFloat(line.unitPrice);
				if( Double.compare( funitPrice, 0 ) == 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.num0",line.lineNo,MessageResourcesUtil.getMessage("labels.unitPrice")));
					validLineCheck = false;
				}
			}

			//円金額
			if((!StringUtil.hasLength(this.inputPOrderForm.rateId)) && (!StringUtil.hasLength(line.price))){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.line.required",line.lineNo,MessageResourcesUtil.getMessage("labels.price")));
				validLineCheck = false;
			}
			tempMsg = ValidateUtil.decimalType(Integer.valueOf(line.lineNo), line.price, MessageResourcesUtil.getMessage("labels.price"), 9, 0);
			if(tempMsg != null){
				errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
				validLineCheck = false;
			}
			else if(StringUtil.hasLength(line.price)){
				// 円金額 数値０チェック　2010.04.21 add kaki
				float fprice = Float.parseFloat(line.price);
				if( Double.compare( fprice, 0 ) == 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.num0",line.lineNo,MessageResourcesUtil.getMessage("labels.price")));
					validLineCheck = false;
				}
			}

			//外貨単価
			if((StringUtil.hasLength(this.inputPOrderForm.rateId)) && (!StringUtil.hasLength(line.dolUnitPrice))){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.line.required",line.lineNo,MessageResourcesUtil.getMessage("labels.dolUnitPrice")));
				validLineCheck = false;
			}
			tempMsg = ValidateUtil.decimalType(Integer.valueOf(line.lineNo), line.dolUnitPrice, MessageResourcesUtil.getMessage("labels.dolUnitPrice"), 9, 3);
			if(tempMsg != null){
				errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
				validLineCheck = false;
			}
			else if(StringUtil.hasLength(line.dolUnitPrice)){
				// 外貨単価 数値０チェック　2010.04.21 add kaki
				float fdolUnitPrice = Float.parseFloat(line.dolUnitPrice);
				if( Double.compare( fdolUnitPrice, 0) == 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.num0",Integer.valueOf(line.lineNo),MessageResourcesUtil.getMessage("labels.dolUnitPrice")));
					validLineCheck = false;
				}
			}
			//外貨金額
			if((StringUtil.hasLength(this.inputPOrderForm.rateId)) && (!StringUtil.hasLength(line.dolPrice))){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.line.required",line.lineNo,MessageResourcesUtil.getMessage("labels.dolPrice")));
				validLineCheck = false;
			}
			tempMsg = ValidateUtil.decimalType(Integer.valueOf(line.lineNo), line.dolPrice, MessageResourcesUtil.getMessage("labels.dolPrice"), 9, 3);
			if(tempMsg != null){
				errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
				validLineCheck = false;
			}
			else if(StringUtil.hasLength(line.dolPrice)){
				// 外貨金額 数値０チェック　2010.04.21 add kaki
				float fdolPrice = Float.parseFloat(line.dolPrice);
				if( Double.compare(fdolPrice, 0) == 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.num0",line.lineNo,MessageResourcesUtil.getMessage("labels.dolPrice")));
					validLineCheck = false;
				}
			}

			//納期
			if( !StringUtil.hasLength(line.deliveryDate) ){
				if( StringUtil.hasLength(SlipDeliveryDate) ){
					//伝票納期からの複写
					line.deliveryDate = SlipDeliveryDate;
				}else{
					//複写できない＝伝票の納期が不正
					validLineCheck = false;
				}
			}else{
				mssageValues = new String[2];
				mssageValues[0] = line.lineNo;
				mssageValues[1] = MessageResourcesUtil.getMessage("labels.deliveryDate");
				tempMsg = ValidateUtil.dateType(line.deliveryDate, Constants.FORMAT.DATE, true, "errors.line.date",mssageValues );
				if(tempMsg != null){
					errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
					validLineCheck = false;
				}
			}
			//備考
			if( (StringUtil.hasLength(line.remarks) && (line.remarks.length()>InputPOrderForm.ML_REMARK))){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( "errors.line.maxlength",line.lineNo,MessageResourcesUtil.getMessage("labels.remarks"),InputPOrderForm.ML_REMARK ));
				validLineCheck = false;
			}
			//商品備考
			if( (StringUtil.hasLength(line.productAbstract) && (line.productAbstract.length()>InputPOrderForm.ML_PRODUCT_REMARK))){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( "errors.line.maxlength",line.lineNo,MessageResourcesUtil.getMessage("labels.productRemarks"),InputPOrderForm.ML_PRODUCT_REMARK ));
				validLineCheck = false;
			}
			if( validLineCheck  ){ validLinesCount++; }
		}
		//有効行が0のときはメッセージを出す
		if(validLinesCount==0){
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.noValidLine"));
		}

		//伝票合計金額（円）
		tempMsg = ValidateUtil.decimalType(this.inputPOrderForm.priceTotal, MessageResourcesUtil.getMessage("labels.priceTotal"), 9, 0);
		if(tempMsg != null){
			errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
		}
		//伝票合計消費税
		tempMsg = ValidateUtil.decimalType(this.inputPOrderForm.ctaxTotal, MessageResourcesUtil.getMessage("labels.ctaxTotal"), 9, 0);
		if(tempMsg != null){
			errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
		}
		//伝票合計外貨金額
		tempMsg = ValidateUtil.decimalType(this.inputPOrderForm.fePriceTotal, MessageResourcesUtil.getMessage("labels.fePriceTotal"), 9, 3);
		if(tempMsg != null){
			errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
		}
		return errors;
	}

	/**
	 * 制御理由テキストを設定します.
	 */
	private void setCannotEditSlipReason(){
        ActionMessages messages = new ActionMessages();
        if(inputPOrderForm.status!=null){
        	//仕入完了
        	if((Constants.STATUS_PORDER_SLIP.PURCHASED).equals(inputPOrderForm.status)){
        		messages.add(ActionMessages.GLOBAL_MESSAGE,
        				new ActionMessage("infos.cannotEditSlip"
        						,MessageResourcesUtil.getMessage("words.reason.slipStatus.purchased")
        				));
        	}
        }
        //ユーザ権限が参照のみ
        if(inputPOrderForm.ROMode){
        	messages.add(ActionMessages.GLOBAL_MESSAGE,
    				new ActionMessage("infos.cannotEditSlip"
    						,MessageResourcesUtil.getMessage("words.reason.userRole.validLimitation")
    				));
        }
        //メッセージを投げる
        ActionMessagesUtil.addMessages(session, messages);
	}

	/**
	 * 制御フラグを設定します.
	 */
	private void setValidMode(){
		inputPOrderForm.ROMode = !(userDto.isMenuUpdate(Constants.MENU_ID.INPUT_PORDER));
	}

}
