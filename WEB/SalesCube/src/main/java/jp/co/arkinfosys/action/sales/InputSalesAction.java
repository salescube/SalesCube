/*
 * Copyright 2009-2010 Ark Information Systems.
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
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.Rack;
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
import jp.co.arkinfosys.service.RackService;
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

	// 画面とAction間でデータをやり取りするオブジェクトを定義する
	@ActionForm
	@Resource
	public InputSalesForm inputSalesForm;

	// ビジネスロジックを定義したオブジェクト
	@Resource
	private SalesService	salesService;

	@Resource
	private SalesLineService	salesLineService;

	@Resource
	protected CustomerService customerService;

	@Resource
	protected BillService billService;

	// 商品マスタ用サービス
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

    @Resource
    protected RackService rackService;


	// 画面表示に使用するオブジェクト
	// 配送業者リストの内容
	public List<LabelValueBean> dcCategoryList = new ArrayList<LabelValueBean>();

	// 配送時間帯リストの内容
	public List<LabelValueBean> dcTimeZoneCategoryList = new ArrayList<LabelValueBean>();

	// 税転嫁リストの内容
	public List<LabelValueBean> taxShiftCategoryList = new ArrayList<LabelValueBean>();

	// 支払条件リストの内容
	public List<LabelValueBean> cutoffGroupCategoryList = new ArrayList<LabelValueBean>();

	// 取引区分リストの内容
	public List<LabelValueBean> salesCmCategoryList = new ArrayList<LabelValueBean>();

	// 顧客納品先リストの内容
	public List<LabelValueBean> deliveryList = new ArrayList<LabelValueBean>();

	// 敬称区分リストの内容
	public List<LabelValueBean> preTypeCategoryList = new ArrayList<LabelValueBean>();

	// 完納区分リストの内容
	public List<LabelValueBean> delivertProcessCategoryList = new ArrayList<LabelValueBean>();

	// 消費税率プルダウン
	public List<LabelValueBean> ctaxRateList = new ArrayList<LabelValueBean>();

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
				// 受注伝票複写
				if( "".equals(inputSalesForm.copySlipId)){
					String strLabel = MessageResourcesUtil.getMessage("labels.roSlipId");
					addMessage("errors.notExist",strLabel);
					return Mapping.INPUT;
				}
				// 受注伝票番号から売上伝票を生成する
				if(!createSalesSlipByRo(inputSalesForm.copySlipId)) {
					inputSalesForm.clear();
				}

			}else{
				throw new ServiceException("errors.system");
			}

			// リスト作成
			this.createList();

			// 顧客納品先リストの作成
			// 顧客納品先は顧客が決まるまで作成できないので、初期状態とする
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

		// 作成済の時は何もしない
		if( inputSalesForm.initCategory == false ){
			return;
		}

		// 配送業者リストの作成
		createCategoryList(Categories.DC_CATEGORY, dcCategoryList, true );

		// 配送時間帯リストの作成
		createCategoryList(Categories.DC_TIMEZONE_CATEGORY, dcTimeZoneCategoryList, true );

		// 税転嫁リストの作成
		createCategoryList(Categories.ART_TAX_SHIFT_CATEGORY, taxShiftCategoryList, true );

		// 支払条件リストの作成
		createCategoryList(Categories.CUTOFF_GROUP, cutoffGroupCategoryList, true );

		// 取引区分リストの作成
		createCategoryList(Categories.SALES_CM_CATEGORY, salesCmCategoryList, true );

		// 敬称区分リストの作成
		createCategoryList(Categories.PRE_TYPE, preTypeCategoryList, true );

		// 完納区分リストの作成
		createCategoryList(Categories.DELIVERY_PROCESS_CATEGORY, delivertProcessCategoryList, true );

		// カテゴリを作成済
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
		// 明細行に存在する商品コードが実在するか確認する
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

			//棚番チェックは特殊商品コードは対象外とする
			if( CheckUtil.isRackCheck(pj) ){
				//棚番コードが設定されている場合のみ
				if(StringUtil.hasLength(lineDto.rackCodeSrc)){

				// 商品コードに紐づいている棚情報が実在するか確認する
					Rack rack = rackService.findById(lineDto.rackCodeSrc);

					if( rack == null ){
						String strLabel = MessageResourcesUtil.getMessage("labels.productCode");
						addMessage( "errors.line.rackNotExist", lineDto.lineNo, strLabel, lineDto.productCode );
					}
				}
			}
			// 数量
			if( checkInt(lineDto.quantity, lineDto.lineNo,
							"labels.quantity") ){
				Integer data = Integer.valueOf(lineDto.quantity);
				if( data < 0 ){
					minus++;
				}else if( data > 0 ){
					plus++;
				}
			}

			// 完納区分
			checkInt(lineDto.deliveryProcessCategory, lineDto.lineNo,
							"labels.deliveryProcessCategory");
			// 仕入単価
			checkDouble(lineDto.unitCost, lineDto.lineNo,
							"labels.unitCost");
			// 仕入金額
			Double dblCost = checkDouble(lineDto.cost, lineDto.lineNo,
							"labels.cost");
			// 売上単価
			checkDouble(lineDto.unitRetailPrice, lineDto.lineNo,
							"labels.unitRetailPrice");
			// 売上金額
			Double dblRetailPrice = checkDouble(lineDto.retailPrice, lineDto.lineNo,
							"labels.retailPrice");

			// 数値０チェック
			// 数量
			checkNum0Int(lineDto.quantity, lineDto.lineNo, "labels.quantity");

			if(!DiscountUtil.isExceptianalProduct( lineDto.productCode )) {
				// 仕入単価
				checkNum0Double(lineDto.unitCost, lineDto.lineNo,
								"labels.unitCost");
				// 仕入金額
				checkNum0Double(lineDto.cost, lineDto.lineNo,	"labels.cost");
			}
			// 売上単価
			checkNum0Int(lineDto.unitRetailPrice, lineDto.lineNo,
							"labels.unitRetailPrice");
			// 売上金額
			checkNum0Double(lineDto.retailPrice, lineDto.lineNo,
							"labels.retailPrice");

			// 棚番 特殊商品コードの行は処理しない
			if( pj != null ){
				lineDto.stockCtlCategory = pj.stockCtlCategory;
				if( CheckUtil.isRackCheck(pj) ){
					if( !StringUtil.hasLength(lineDto.rackCodeSrc)){
						String strLabel = MessageResourcesUtil.getMessage("labels.master.rackCode");
						addMessage( "errors.line.required", lineDto.lineNo, strLabel );
					}
				}
			}
			// 計算によって生成される項目の最大最小値チェック
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
			// クレジットの場合は、分納できない。
			if( CategoryTrns.SALES_CM_CREDIT_CARD.equals( isf.salesCmCategory )
					&&(CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL.equals(lineDto.deliveryProcessCategory))){
				isCredit = true;
			}
		}

		// クレジットの場合は、分納できない。
		if( isCredit){
			addMessage( "errors.valid.credit" );
		}
		if( nCount == 0 ){
			// 正常なデータが１件も無い時はエラー
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

		// 配送業者名設定
		for(LabelValueBean lvb : dcCategoryList ){
			if( lvb.getValue().equals(inputSalesForm.dcCategory)){
				d.dcName = lvb.getLabel();
				break;
			}
		}
		// 配送時間帯設定
		for(LabelValueBean lvb : dcTimeZoneCategoryList ){
			if( lvb.getValue().equals(inputSalesForm.dcTimezoneCategory)){
				d.dcTimezone = lvb.getLabel();
				break;
			}
		}
		// 納入先敬称名設定
		for(LabelValueBean lvb : preTypeCategoryList ){
			if( lvb.getValue().equals(inputSalesForm.deliveryPcPreCategory)){
				d.deliveryPcPre = lvb.getLabel();
				break;
			}
		}
		// 請求先敬称名設定
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

		// 受注伝票を読み込む
		ROrderSlipDto rosDto = roSlipSalesService.loadBySlipId(roSlipId);
		if (rosDto == null){
			// 受注番号が存在しない場合、エラーメッセージを表示
			addMessage("errors.copy.notexist");
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return false;
		}

		List<ROrderLineDto> lineList = roLineService.loadBySlip(rosDto);
		rosDto.setLineDtoList(lineList);

		boolean brestQuantity = false;
		// 伝票複写時の条件の確認
		for( ROrderLineDto rolDto : rosDto.getLineDtoList() ){
			if( !StringUtil.hasLength( rolDto.restQuantity ) ){
				continue;
			}
			try {
				if(!Constants.STATUS_RORDER_LINE.SALES_FINISH.equals(rolDto.status)){
					// 完納区分が「完了」である場合は、取り込まない
					brestQuantity = true;
					break;
				}
			} catch (Exception e) {
				continue;
			}
		}

		if (!brestQuantity) {
			// コピー可能な受注番号が存在しない場合、エラーメッセージを表示
			addMessage("errors.copy.notexist");
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return false;
		}

		// 顧客情報を取得
		Customer customer = customerService.findCustomerByCode(rosDto.customerCode);
		if( customer == null ){
			// 顧客コードがXXのデータは存在しません
			String strLabel = MessageResourcesUtil.getMessage("labels.customerCode");
			addMessage( "errors.dataNotExist", strLabel, inputSalesForm.customerCode );
		}

		// 受注伝票用ActionFormに展開する
		inputSalesForm.initialize( rosDto, customer );

		// 関連情報を取得
		SalesSlipDto dto = (SalesSlipDto) inputSalesForm.copyToDto();
		dto.copyFrom(inputSalesForm.salesLineList);
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (SalesLineDto lineDto : salesLineList) {
			salesLineService.setStockInfoForm(lineDto);
		}

		// 顧客の請求先情報を取得する
		List<DeliveryAndPre> delList = deliveryService.searchDeliveryByCompleteCustomerCode(rosDto.customerCode);
		if( delList.size() == 0 ){
			String strLabel = MessageResourcesUtil.getMessage("labels.customerCode");
			throw new ServiceException(strLabel);
		}else{
			// 請求情報を初期化
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
		if( !isf.deliveryName.equals(customer.customerName) ){			// 納入先名
			return false;
		}
		if( !isf.deliveryOfficeName.equals(customer.customerOfficeName) ){	// 納入先事業所名
			return false;
		}
		if( !isf.deliveryDeptName.equals(customer.customerDeptName) ){		// 納入先部署名
			return false;
		}
		if( !isf.deliveryZipCode.equals(customer.customerZipCode) ){		// 納入先郵便番号
			return false;
		}
		if( !isf.deliveryAddress1.equals(customer.customerAddress1) ){		// 納入先住所１
			return false;
		}
		if( !isf.deliveryAddress2.equals(customer.customerAddress2) ){		// 納入先住所２
			return false;
		}
		if( !isf.deliveryPcName.equals(customer.customerPcName) ){		// 納入先担当者名
			return false;
		}
		if( !isf.deliveryPcPreCategory.equals(customer.customerPcPreCategory) ){	// 納入先敬称コード
			return false;
		}
		if( !isf.deliveryTel.equals(customer.customerTel) ){			// 納入先電話番号
			return false;
		}
		if( !isf.deliveryFax.equals(customer.customerFax) ){			// 納入先ＦＡＸ番号
			return false;
		}
		return true;
	}

	/**
	 * 不要な明細行を削除します.<BR>
	 */
	private void deleteBlankLine() {
		// 不要な明細行を削除
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
		// 区分情報を作成する
		initCategoryList();

		// 空欄で作成
		createDeliveryList();

		// 消費税率プルダウンリスト
		this.ctaxRateList =  ListUtil.getRateTaxNoBlankList(super.taxRateService);
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
		// 伝票情報をロードする
		SalesSlipDto dto = salesService.loadBySlipId(inputSalesForm.salesSlipId);

		if (dto == null) {
			inputSalesForm.clear();
			return false;
		}

		Beans.copy(dto, inputSalesForm).execute();

		// 明細情報をロードする
		List<SalesLineDto> lineList = salesLineService.loadBySlip(dto);
		dto.setLineDtoList(lineList);
		dto.fillList();
		inputSalesForm.setLineList(lineList);

		inputSalesForm.initLoad();

		Customer customer = customerService.findCustomerByCode(inputSalesForm.customerCode);
		if( customer != null ){
			inputSalesForm.initialize(customer);
			// 仮納品書の出力フラグ設定（顧客情報と納入先が同じ時には出力しない）
			inputSalesForm.reportEFlag = !sameCheckCustomerAndDelivery(customer,inputSalesForm);
		}

		// 顧客納品先リストの作成
		// 顧客納品先は顧客が決まるまで作成できないので、初期状態とする
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

			// 状態名を取得
			String categoryName = categoryService.findCategoryNameByIdAndCode(
					SlipStatusCategories.SALES_SLIP_STATUS, this.inputSalesForm.status );
			// 伝票名取得
			String strSlipLabel = MessageResourcesUtil.getMessage("erroes.db.salesSlip");
			// 動作名取得
			String strActionLabel = MessageResourcesUtil.getMessage("words.action.edit");
			// メッセージに設定
			addMessage("infos.slip.lock", strSlipLabel, categoryName, strActionLabel);
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

			this.inputSalesForm.menuUpdate = false;
		}else{
			if( StringUtil.hasLength(this.inputSalesForm.salesCutoffDate) ){

				// 状態名を取得
				String categoryName = MessageResourcesUtil.getMessage("labesl.closeArtBalance");
				// 伝票名取得
				String strSlipLabel = MessageResourcesUtil.getMessage("erroes.db.salesSlip");
				// 動作名取得
				String strActionLabel = MessageResourcesUtil.getMessage("words.action.edit");
				// メッセージに設定
				addMessage("infos.slip.lock", strSlipLabel, categoryName, strActionLabel);
				ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

				this.inputSalesForm.menuUpdate = false;
			}
		}

		inputSalesForm.setSalesDateTaxRate();
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
			// 入出庫が締済みかどうか
			if(this.inputStockSalesService.existsClosedEadSlip((SalesSlipDto) dto)) {
			}
		}
		// リストボックスの選択値から文字列を設定
		setValueToName(dto);

		// 請求書発行単位が売上伝票の場合、請求書を作成する
		if( CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP.equals(inputSalesForm.billPrintUnit) ) {
			if (bInsert) {
				billService.setBillId(dto);
			} else 	if( !StringUtil.hasLength(inputSalesForm.salesBillId )){
				billService.setBillId(dto);
			}
		} else {
			if (!bInsert && StringUtil.hasLength(inputSalesForm.salesBillId)) {
				// 伝票情報の更新
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
				// 顧客の属性が　売掛　→　売掛以外　に変更された　または　新規レコード
				//　売上請求書情報の作成
				billService.insertBillSales(dto);
			}else{
				// 顧客の属性は売掛以外から　変更されていない


				// 売上請求書情報の更新
				billService.updateBillSales(dto);
			}
		} else {
			if (!bInsert && StringUtil.hasLength(inputSalesForm.salesBillId)) {
				// 顧客の属性は売掛から　変更されていない
				SalesSlipDto dto2 = (SalesSlipDto) this.createDTO();
				dto2.salesBillId = inputSalesForm.salesBillId;
				// 売上請求書情報の削除
				billService.deleteBillSales(dto2);
			}
		}
		this.salesService.saveROrder(bInsert, (SalesSlipDto) dto);

		if (bInsert) {

			// 入出庫伝票の登録
			inputStockSalesService.insert(dto);

		} else {
			// 入出庫伝票の更新
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
			// 存在する顧客か確認
			Customer customer = customerService.findCustomerByCode(inputSalesForm.customerCode);
			if( customer == null ){
				// 顧客コードがXXのデータは存在しません
				String strLabel = MessageResourcesUtil.getMessage("labels.customerCode");
				addMessage( "errors.dataNotExist", strLabel, inputSalesForm.customerCode );
			}
		} catch (Exception e) {
			addMessage( "errors.system" );
		}
		try {
			// 存在する商品か確認
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
		// 入出庫が締済みかどうか
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

		boolean doBill = false;	// 売上伝票単位の請求書を作成するかどうか

		// 請求書発行単位が売上伝票の場合、請求書を作成する
		if( CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP.equals(inputSalesForm.billPrintUnit) ) {
			doBill = true;
		}

		if( doBill ){
			billService.deleteBillSales(dto);
		}

		inputStockSalesService.delete(dto);

		//
		// 入金伝票の削除
		// 取引区分が「現金」の場合のみ
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
