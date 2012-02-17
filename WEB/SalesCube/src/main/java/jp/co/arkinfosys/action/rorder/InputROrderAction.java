/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.rorder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.entity.OnlineOrderWork;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.RoLineTrn;
import jp.co.arkinfosys.entity.RoSlipTrn;
import jp.co.arkinfosys.entity.TaxRate;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.rorder.InputROrderForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.DeliveryService;
import jp.co.arkinfosys.service.OnlineOrderService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.ProductStockService;
import jp.co.arkinfosys.service.OnlineOrderRelService;
import jp.co.arkinfosys.service.RoLineService;
import jp.co.arkinfosys.service.RoSlipService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.TokenProcessor;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 受注入力画面の表示アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputROrderAction extends
		AbstractSlipEditAction<ROrderSlipDto, ROrderLineDto> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INIT = "/rorder/inputROrder";
		public static final String INPUT = "inputROrder.jsp";
		public static final String EDIT = "/rorder/inputROrder/edit/";
		public static final String SEARCH = "/rorder/searchROrder";
		public static final String ONLINE_ORDER = "/rorder/importOnlineOrder";
	}


	public HttpSession session;

	@Resource
	protected HttpServletRequest request;

	/** アクションフォーム */
	@ActionForm
	@Resource
	protected InputROrderForm inputROrderForm;


	/** 敬称リスト */
	public List<LabelValueBean> preTypeCategoryList = new ArrayList<LabelValueBean>();

	/** 支払条件リスト */
	public List<LabelValueBean> cutOffList = new ArrayList<LabelValueBean>();

	/** 取引区分リスト */
	public List<LabelValueBean> salesCmCategoryList = new ArrayList<LabelValueBean>();

	/** 顧客納入先リスト */
	public List<LabelValueBean> deliveryList = new ArrayList<LabelValueBean>();

	/** 税転嫁リスト */
	public List<LabelValueBean> taxShiftCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> statusCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> dcCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> dcTimeZoneCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 区分情報
	 */
	@Resource
	protected CategoryService categoryService;

	/**
	 * 納入先情報
	 */
	@Resource
	protected DeliveryService deliveryService;

	/**
	 * 受注伝票情報
	 */
	@Resource
	protected RoSlipService roSlipService;

	/**
	 * 受注明細情報
	 */
	@Resource
	protected RoLineService roLineService;

	/**
	 * 顧客情報
	 */
	@Resource
	protected CustomerService customerService;

	/**
	 * 商品情報
	 */
	@Resource
	protected ProductService productService;

	/**
	 * オンライン受注情報
	 */
	@Resource
	protected OnlineOrderService onlineOrderService;

	/**
	 * オンライン関連情報
	 */
	@Resource
	protected OnlineOrderRelService onlineOrderRelService;

	/**
	 * 商品在庫情報
	 */
	@Resource
	protected ProductStockService productStockService;

	/**
	 * オンライン受注データの表示を行います.
	 * <p>
	 * オンライン受注データ取込画面で選択されたオンライン受注データと顧客情報から<br>
	 * 受注伝票情報を作成し、受注入力画面に表示します.
	 * </p>
	 */
	@Execute(validator = false)
	public String online() throws Exception {

		try {

			
			this.inputROrderForm.initialize();
			this.inputROrderForm.initDc();
			this.inputROrderForm.initializeScreenInfo();
			initForms();

			
			List<OnlineOrderWork> list = onlineOrderService
					.findOnlineOrderWorkByRoId(this.inputROrderForm.roSlipId);

			if (list.size() == 0) {
				
				
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dataNotExist", "受注ＩＤ",
								inputROrderForm.roSlipId));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return Mapping.ONLINE_ORDER;
			}

			
			this.inputROrderForm.setUp(list);


			
			Customer customer = customerService
					.findCustomerByCode(this.inputROrderForm.customerCode);

			if (customer == null) {
				
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dataNotExist", "顧客データ",
								inputROrderForm.customerCode));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

				return Mapping.ONLINE_ORDER;
			}

			
			this.inputROrderForm.customerName = customer.customerName;
			this.inputROrderForm.taxShiftCategory = customer.taxShiftCategory;
			this.inputROrderForm.taxFractCategory = customer.taxFractCategory;
			this.inputROrderForm.priceFractCategory = customer.priceFractCategory;
			this.inputROrderForm.cutoffGroupCategory = customer.cutoffGroup
					+ customer.paybackCycleCategory;
			this.inputROrderForm.salesCmCategory = customer.salesCmCategory;
			this.inputROrderForm.customerRemarks = customer.remarks;
			this.inputROrderForm.customerCommentData = customer.commentData;

			
			ProductJoin pjShip = productService
					.findById(Constants.EXCEPTIANAL_PRODUCT_CODE.ONLINE_DELIVERY_PRICE);
			
			BigDecimal sumOnlineShippingPrice = BigDecimal.ZERO;

			
			int index = 1;
			this.inputROrderForm.lineList = new ArrayList<ROrderLineDto>();
			for (OnlineOrderWork orderLine : list) {
				ROrderLineDto dto = new ROrderLineDto();
				ProductJoin pj = productService
						.findProductByOnlinePCode(orderLine.sku);
				if (pj == null) {
					String strLabel = MessageResourcesUtil
							.getMessage("labels.product.csv.onlinePcode");
					super.messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.dataNotExist", strLabel,
									orderLine.sku));
					continue;
				}
				initTax(orderLine.paymentDate);
				dto.lineNo = String.valueOf(index);
				dto.roSlipId = "";
				dto.roItemId = orderLine.onlineItemId;
				dto.productCode = pj.productCode;
				dto.productAbstract = pj.productName;
				dto.rackCodeSrc = pj.rackCode;
				dto.quantity = orderLine.quantity.toString();
				dto.restQuantity = dto.quantity;
				dto.status = this.inputROrderForm.defaultStatusCode;
				dto.statusName = this.inputROrderForm.defaultStatusName;
				if (pj.supplierPriceYen != null) {
					dto.unitCost = pj.supplierPriceYen.toString();
					dto.cost = (pj.supplierPriceYen.multiply(orderLine.quantity))
							.toString();
				} else {
					dto.unitCost = "";
					dto.cost = "";
				}
				if (pj.retailPrice != null) {
					dto.unitRetailPrice = pj.retailPrice.toString();
					dto.retailPrice = (pj.retailPrice.multiply(orderLine.quantity))
							.toString();
				} else {
					dto.unitRetailPrice = orderLine.price.divide(orderLine.quantity,
							MathContext.DECIMAL64).toString();
					dto.retailPrice = orderLine.price.toString();
				}
				dto.productRemarks = pj.remarks;
				dto.eadRemarks = pj.eadRemarks;

				
				dto.supplierPcode = pj.supplierPcode;
				dto.roMaxNum = String.valueOf(pj.roMaxNum);
				StockInfoDto stockInfo = productStockService
						.calcStockQuantityByProductCode(dto.productCode);
				dto.possibleDrawQuantity = String
						.valueOf(stockInfo.possibleDrawQuantity);
				dto.taxCategory = pj.taxCategory;
				dto.ctaxRate = this.inputROrderForm.taxRate;
				dto.stockCtlCategory = pj.stockCtlCategory;
				dto.deletable = true; 
				index++;
				this.inputROrderForm.lineList.add(dto);

				
				if ((orderLine.shippingPrice != null)) {
					sumOnlineShippingPrice = sumOnlineShippingPrice
							.add(orderLine.shippingPrice);
				}
			}

			
			if ((sumOnlineShippingPrice.compareTo(BigDecimal.ZERO) != 0)) {
				ROrderLineDto dtoShip = new ROrderLineDto();
				dtoShip.lineNo = String.valueOf(index);
				dtoShip.roSlipId = "";
				dtoShip.roItemId = pjShip.onlinePcode;
				dtoShip.productCode = pjShip.productCode;
				dtoShip.productAbstract = pjShip.productName;
				dtoShip.rackCodeSrc = pjShip.rackCode;
				dtoShip.quantity = "1";
				dtoShip.restQuantity = "1";
				dtoShip.status = this.inputROrderForm.defaultStatusCode;
				dtoShip.statusName = this.inputROrderForm.defaultStatusName;

				
				BigDecimal noTax = toNoTax(customer, sumOnlineShippingPrice,
						new BigDecimal(this.inputROrderForm.taxRate));
				dtoShip.unitCost = noTax.toString();
				dtoShip.cost = noTax.toString();
				dtoShip.unitRetailPrice = noTax.toString();
				dtoShip.retailPrice = noTax.toString();

				dtoShip.productRemarks = pjShip.remarks;
				dtoShip.eadRemarks = pjShip.eadRemarks;

				
				dtoShip.supplierPcode = pjShip.supplierPcode;
				dtoShip.roMaxNum = String.valueOf(pjShip.roMaxNum);
				dtoShip.possibleDrawQuantity = "1";
				dtoShip.taxCategory = pjShip.taxCategory;
				dtoShip.ctaxRate = this.inputROrderForm.taxRate;
				dtoShip.deletable = true; 

				index++;
				this.inputROrderForm.lineList.add(dtoShip);
			}

			
			ROrderSlipDto dto = (ROrderSlipDto) inputROrderForm.copyToDto();
			dto.fillList();
			dto.copyTo(inputROrderForm.lineList);

			
			makeListByForm();

			
			initOnlineOrderForms();

			this.inputROrderForm.isImport = true;

			if (super.messages.size() > 0) {
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		
		return Mapping.INPUT;
	}

	private static final int BigDecimalModeCodeIntError = -1;

	/**
	 * 端数処理モードを受け取ってBigDecimalでの表現に変換して返します.
	 * @param taxFractCategory 端数処理モード
	 * @return 引数の端数処理モードのBigDecimal表現
	 */
	private int bigDecimalModeCodeFromStringToInt(String taxFractCategory) {
		
		if (CategoryTrns.FLACT_CATEGORY_DOWN.equals(taxFractCategory)) {
			return BigDecimal.ROUND_DOWN;
		}
		
		else if (CategoryTrns.FLACT_CATEGORY_UP.equals(taxFractCategory)) {
			return BigDecimal.ROUND_UP;
		}
		
		else if (CategoryTrns.FLACT_CATEGORY_HALF_UP.equals(taxFractCategory)) {
			return BigDecimal.ROUND_HALF_UP;
		}
		
		return BigDecimalModeCodeIntError;
	}

	/**
	 * 税込の金額を税抜の金額に変換して返します.
	 * @param customer 顧客情報
	 * @param taxIn 税込の金額
	 * @param taxRate 税率
	 * @return 税抜の金額
	 */
	protected BigDecimal toNoTax(Customer customer, BigDecimal taxIn,
			BigDecimal taxRate) {
		BigDecimal rate = new BigDecimal(1.0);
		rate = rate
				.add(taxRate
						.divide(
								new BigDecimal(100.0) 
								,
								mineDto.statsDecAlignment + 1 
								,
								bigDecimalModeCodeFromStringToInt(CategoryTrns.FLACT_CATEGORY_HALF_UP))); 

		
		int BDCode = bigDecimalModeCodeFromStringToInt(customer.taxFractCategory);
		if (BDCode == BigDecimalModeCodeIntError) {
			return null;
		}
		return taxIn.divide(rate, 0, BDCode);

	}

	/**
	 * 現在の税率を取得し、画面に設定します.
	 * @throws Exception
	 */
	private void initTax() throws Exception {
		
		initTax(new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * 指定日の税率を取得し、画面に設定します.
	 * @param date 指定日
	 * @throws Exception
	 */
	private void initTax(Timestamp date) throws Exception {

		if (date == null) {
			initTax();
			return;
		}
		
		TaxRate tx;
		SimpleDateFormat DF_TIME = new SimpleDateFormat(Constants.FORMAT.DATE);
		tx = taxRateService.findTaxRateById(CategoryTrns.TAX_TYPE_CTAX, DF_TIME
				.format(date));
		inputROrderForm.taxRate = tx.taxRate.toString();
	}

	/**
	 * 初期化関数です.<br>
	 * 税率、配送関連の値を初期化します.
	 * @throws Exception
	 */
	public void initForms() throws Exception {

		
		if (!StringUtil.hasLength(inputROrderForm.taxFractCategory)) {
			inputROrderForm.taxFractCategory = mineDto.taxFractCategory;
		}
		if (!StringUtil.hasLength(inputROrderForm.taxShiftCategory)) {
			inputROrderForm.taxShiftCategory = mineDto.taxShiftCategory;
		}
		if (!StringUtil.hasLength(inputROrderForm.priceFractCategory)) {
			inputROrderForm.priceFractCategory = mineDto.priceFractCategory;
		}
		
		initDc();

		if (Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER
				.equals(inputROrderForm.customerCode)) {
			
			initOnlineOrderForms();
		}
	}

	/**
	 * オンラインデータフラグをONにします.
	 * @throws Exception
	 */
	public void initOnlineOrderForms() throws Exception {
		
		this.inputROrderForm.isOnlineOrder = true;
	}

	/**
	 * 初期化関数です.<br>
	 * 税率、配送関連の値を初期化する他に、画面項目の制御を行います.
	 * @throws Exception
	 */
	public void initForms(ROrderSlipDto dto) throws Exception {
		
		TokenProcessor.getInstance().saveToken(request);

		
		makeList(dto);

		

		if (!this.inputROrderForm.isImport
				&& inputROrderForm.lineList.size() == 0) {
			dto.fillList();
			dto.copyTo(inputROrderForm.lineList);
		}

		
		initTax();

		
		inputROrderForm.statusUpdate = !(Constants.STATUS_RORDER_SLIP.SALES_FINISH
				.equals(dto.status));

		
		inputROrderForm.deletable = inputROrderForm.statusUpdate;

		
		inputROrderForm.taxFractCategory = dto.taxFractCategory;
		inputROrderForm.taxShiftCategory = dto.taxShiftCategory;
		inputROrderForm.priceFractCategory = dto.priceFractCategory;

		
		initDc();

		if (Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER
				.equals(inputROrderForm.customerCode)) {
			
			initOnlineOrderForms();
		}
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します(伝票新規作成時).
	 * @throws Exception
	 */
	private void makeListByForm() throws Exception {
		
		String code = inputROrderForm.taxShiftCategory;
		String name = this.categoryService.findCategoryNameByIdAndCode(
				Categories.ART_TAX_SHIFT_CATEGORY, code);
		this.taxShiftCategoryList.add(new LabelValueBean(name, code));

		
		this.preTypeCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PRE_TYPE);
		this.preTypeCategoryList.add(0, new LabelValueBean("", ""));

		
		code = inputROrderForm.cutoffGroupCategory;
		name = this.categoryService.findCategoryNameByIdAndCode(
				Categories.CUTOFF_GROUP, code);
		this.cutOffList.add(new LabelValueBean(name, code));

		
		code = inputROrderForm.salesCmCategory;
		name = this.categoryService.findCategoryNameByIdAndCode(
				Categories.SALES_CM_CATEGORY, code);
		this.salesCmCategoryList.add(new LabelValueBean(name, code));

		
		this.dcCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_CATEGORY);
		this.dcCategoryList.add(0, new LabelValueBean("", ""));

		
		this.dcTimeZoneCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_TIMEZONE_CATEGORY);
		this.dcTimeZoneCategoryList.add(0, new LabelValueBean("", ""));

		List<DeliveryAndPre> list = this.deliveryService
				.searchDeliveryByCompleteCustomerCodeSortedByCreDate(inputROrderForm.customerCode);
		for (DeliveryAndPre delivery : list) {
			this.deliveryList.add(new LabelValueBean(delivery.deliveryName,
					delivery.deliveryCode));
		}

		
		createCategoryList(Categories.RO_LINE_STATUS, statusCategoryList, false);
	}

	/**
 	 * 画面表示に使用しているプルダウン等の情報を作成します(伝票更新時).
	 * @throws Exception
	 */
	private void makeList(ROrderSlipDto record) {
		try {
			
			taxShiftCategoryList = new ArrayList<LabelValueBean>();
			String code = record.taxShiftCategory;
			String name = this.categoryService.findCategoryNameByIdAndCode(
					Categories.ART_TAX_SHIFT_CATEGORY, code);
			this.taxShiftCategoryList.add(new LabelValueBean(name, code));

			
			preTypeCategoryList = new ArrayList<LabelValueBean>();
			this.preTypeCategoryList = categoryService
					.findCategoryLabelValueBeanListById(Categories.PRE_TYPE);
			this.preTypeCategoryList.add(0, new LabelValueBean("", ""));

			
			cutOffList = new ArrayList<LabelValueBean>();
			code = record.cutoffGroup + record.paybackCycleCategory;
			name = this.categoryService.findCategoryNameByIdAndCode(
					Categories.CUTOFF_GROUP, code);
			this.cutOffList.add(new LabelValueBean(name, code));

			
			salesCmCategoryList = new ArrayList<LabelValueBean>();
			code = record.salesCmCategory;
			name = this.categoryService.findCategoryNameByIdAndCode(
					Categories.SALES_CM_CATEGORY, code);
			this.salesCmCategoryList.add(new LabelValueBean(name, code));

			if (StringUtil.hasLength(record.customerCode)) {
				List<DeliveryAndPre> list = this.deliveryService
						.searchDeliveryByCompleteCustomerCodeSortedByCreDate(record.customerCode);
				for (DeliveryAndPre delivery : list) {
					this.deliveryList.add(new LabelValueBean(
							delivery.deliveryName, delivery.deliveryCode));
				}
			}

			
			createCategoryList(Categories.RO_LINE_STATUS, statusCategoryList,
					false);

		} catch (ServiceException e) {
			super.errorLog(e);
		}
	}

	/**
	 * 配送関連のプルダウンを初期化します.
	 * @throws ServiceException
	 */
	protected void initDc() throws ServiceException {

		
		this.dcCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_CATEGORY);
		this.dcCategoryList.add(0, new LabelValueBean("", ""));

		
		this.dcTimeZoneCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_TIMEZONE_CATEGORY);
		this.dcTimeZoneCategoryList.add(0, new LabelValueBean("", ""));

	}

	/**
	 * 登録時の受注伝票と受注伝票明細行のチェックを行います.
	 *
	 * @return アクションメッセージ
	 * @throws ServiceException
	 */
	@Override
	public ActionMessages validateAtCreateSlip() throws ServiceException {

		prepareForm();
		inputROrderForm.initialize();
		try {
			
			if (StringUtil.hasLength(this.inputROrderForm.customerCode)
					&& customerService
							.isExistCustomerCode(inputROrderForm.customerCode) == false) {
				
				String strLabel = MessageResourcesUtil
						.getMessage("labels.customerCode");
				addMessage("errors.dataNotExist", strLabel,
						inputROrderForm.customerCode);
			}
		} catch (Exception e) {
			errProc(e);
			addMessage("errors.system");
		}
		try {
			
			checkProducts();

		} catch (ServiceException e) {
			errProc(e);
			if (e.getMessage().length() > 0) {
				addMessage(e.getMessage());
			} else {
				addMessage("errors.system");
			}
		} catch (Exception e) {
			errProc(e);
			addMessage("errors.system");
		}

		return super.messages;
	}

	/**
	 * エラーログを出力します.
	 *
	 * @param e 例外オブジェクト
	 */
	private void errProc(Exception e) {
		e.printStackTrace();
		super.errorLog(e);
	}

	/**
	 * 指定された伝票の明細行に関連する商品の情報を取得します.<br>
	 * 存在しない商品が含まれている場合にはエラーメッセージを追加して終了します.
	 *
	 * @return true:正常なデータが１件以上ある場合
	 * false:全て異常データの場合
	 * @throws ServiceException
	 */
	protected boolean checkProducts() throws ServiceException {

		int nCount = 0;
		
		for (ROrderLineDto dto : this.inputROrderForm.lineList) {
			if (!StringUtil.hasLength(dto.productCode)) {
				continue;
			}
			ProductJoin pj = productService.findById(dto.productCode);
			if (pj == null) {
				String strLabel = MessageResourcesUtil
						.getMessage("labels.productCode");
				addMessage("errors.dataNotExist", strLabel, dto.productCode);
			} else {
				nCount++;
			}
		}
		if (nCount == 0) {
			
			return false;
		}
		return true;
	}

	/**
	 * オンライン受注データかどうかチェックし、フラグを設定します.
	 * @throws Exception
	 */
	protected void setDeletableByOnlineOrder() throws Exception {
		if (!this.inputROrderForm.menuUpdate) {
			
			return;
		}

		if (!this.inputROrderForm.isImport) {
			this.inputROrderForm.isImport = onlineOrderRelService
					.hasRecordByROrderSlip(this.inputROrderForm.roSlipId);
		}
	}

	/**
	 * オンライン受注データを削除します.
	 * @throws Exception
	 */
	protected void deleteOnlineOrderRelRec() throws Exception {
		if (onlineOrderRelService.hasRecordByROrderSlip(this.inputROrderForm.roSlipId)) {
			for (ROrderLineDto dto : this.inputROrderForm.lineList) {
				onlineOrderRelService.deleteOnlineOrderRel(dto);
			}
		}
	}

	/**
	 * アイテム総数値引きデータを生成して返します.
	 * @param index　行番号
	 * @param price　値引き金額
	 * @return　生成したDTO
	 * @throws ServiceException
	 */
	protected ROrderLineDto getItemDiscountDto(int index, BigDecimal price)
			throws ServiceException {
		ROrderLineDto dto = new ROrderLineDto();
		ProductJoin pj = productService
				.findById(Constants.EXCEPTIANAL_PRODUCT_CODE.ITEM_DISCOUNT_PRICE);
		if (pj == null) {
			String strLabel = MessageResourcesUtil
					.getMessage("labels.product.csv.productCode");
			super.messages
					.add(
							ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.dataNotExist",
									strLabel,
									Constants.EXCEPTIANAL_PRODUCT_CODE.ITEM_DISCOUNT_PRICE));
			return null;
		}

		dto.lineNo = String.valueOf(index);
		dto.roSlipId = "";
		dto.productCode = pj.productCode;
		dto.productAbstract = pj.productName;
		dto.rackCodeSrc = pj.rackCode;
		dto.quantity = "1";
		dto.restQuantity = "1";
		dto.status = this.inputROrderForm.defaultStatusCode;
		dto.statusName = this.inputROrderForm.defaultStatusName;
		dto.unitCost = price.toString();
		dto.cost = price.toString();
		dto.unitRetailPrice = price.toString();
		dto.retailPrice = price.toString();
		dto.productRemarks = pj.remarks;
		dto.eadRemarks = pj.eadRemarks;
		
		dto.supplierPcode = pj.supplierPcode;
		dto.roMaxNum = "1";
		dto.possibleDrawQuantity = "1";
		dto.taxCategory = pj.taxCategory;
		dto.ctaxRate = this.inputROrderForm.taxRate;
		dto.deletable = false; 

		return dto;
	}

	/**
	 * 区分マスタからリストを生成します.
	 * @param categoryType　区分マスタコード
	 * @param list 作成するリスト
	 * @param emptyString 最初の空欄選択の有無
	 * @throws ServiceException
	 */
	protected void createCategoryList(int categoryType,
			List<LabelValueBean> list, boolean emptyString)
			throws ServiceException {
		List<CategoryJoin> categoryJoinList = this.categoryService
				.findCategoryJoinById(categoryType);
		if (emptyString == true) {
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
	 * プルダウンのコード値から名称を取得して設定します.
	 */
	private void setValueToName() {

		
		for (LabelValueBean lvb : dcCategoryList) {
			if (lvb.getValue().equals(inputROrderForm.dcCategory)) {
				inputROrderForm.dcName = lvb.getLabel();
				break;
			}
		}
		
		for (LabelValueBean lvb : dcTimeZoneCategoryList) {
			if (lvb.getValue().equals(inputROrderForm.dcTimezoneCategory)) {
				inputROrderForm.dcTimezone = lvb.getLabel();
				break;
			}
		}
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link InputROrderForm}
	 */
	@Override
	protected AbstractSlipEditForm<ROrderLineDto> getActionForm() {
		return this.inputROrderForm;
	}

	/**
	 * 画面リストを初期化します.
	 * @throws Exception
	 */
	@Override
	protected void createList() throws Exception {
		if (inputROrderForm.isNewData()) {
			makeListByForm();
		}
	}

	/**
	 * 新規受注伝票DTOを作成します.
	 * @return {@link ROrderSlipDto}
	 */
	@Override
	protected AbstractSlipDto<ROrderLineDto> createDTO() {
		return new ROrderSlipDto();
	}

	/**
	 * 入力画面のURIを返します.
	 * @return 受注入力画面のURI
	 */
	@Override
	protected String getInputURIString() {
		return Mapping.INPUT;
	}

	/**
	 * 伝票サービスを返します.
	 * @return {@link RoSlipService}
	 */
	@Override
	protected AbstractSlipService<RoSlipTrn,ROrderSlipDto> getSlipService() {
		return this.roSlipService;
	}

	/**
	 * 明細行サービスを返します.
	 * @return {@link RoLineService}
	 */
	@Override
	protected AbstractLineService<RoLineTrn,ROrderLineDto,ROrderSlipDto> getLineService() {
		return this.roLineService;
	}

	/**
	 * 登録時に使用するサービスを返します.<br>
	 * 未使用です.
	 * @return サービスリスト
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		return new AbstractService[0];
	}

	/**
	 * 伝票データを取得します.
	 * @return 読み込みできたか否か
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected boolean loadData() throws Exception, ServiceException {
		
		ROrderSlipDto dto = roSlipService
				.loadBySlipId(inputROrderForm.roSlipId);

		if (dto == null) {
			return false;
		}

		Beans.copy(dto, inputROrderForm).execute();

		
		List<ROrderLineDto> lineList = roLineService.loadBySlip(dto);
		dto.setLineDtoList(lineList);
		dto.fillList();
		inputROrderForm.setLineList(lineList);

		return true;
	}

	/**
	 * フォームにサービスを設定します.
	 */
	@Override
	protected void prepareForm() {
		super.prepareForm();

		this.inputROrderForm.categoryService = this.categoryService;
	}

	/**
	 * 伝票読み込み後の処理です.
	 * <p>
	 * 1:リスト初期化<br>
	 * 2:明細行のステータス設定<br>
	 * 3:オンライン受注データチェック<br>
	 * 4:ステータスに応じたメッセージ設定
	 * </p>
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected void afterLoad() throws Exception,
			ServiceException {

		
		ROrderSlipDto dto = (ROrderSlipDto) inputROrderForm.copyToDto();
		initForms(dto);

		
		for (ROrderLineDto lineDto : this.inputROrderForm.lineList) {
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}
			StockInfoDto stockInfo = productStockService
					.calcStockQuantityByProductCode(lineDto.productCode);
			lineDto.possibleDrawQuantity = String
					.valueOf(stockInfo.possibleDrawQuantity);
			
			if (Constants.STATUS_RORDER_LINE.RECEIVED.equals(lineDto.status)) {
				lineDto.statusName = this.inputROrderForm.defaultStatusName;
			} else if (Constants.STATUS_RORDER_LINE.NOWPURCHASING
					.equals(lineDto.status)) {
				lineDto.deletable = false;
				lineDto.statusName = this.inputROrderForm.nowPurchasingStatusName;
			} else if (Constants.STATUS_RORDER_LINE.SALES_FINISH
					.equals(lineDto.status)) {
				lineDto.deletable = false;
				lineDto.statusName = this.inputROrderForm.finishStatusName;
			}

			
			lineDto.quantityDB = lineDto.quantity;
			lineDto.restQuantityDB = lineDto.restQuantity;
		}
		
		setDeletableByOnlineOrder();

		if (!inputROrderForm.statusUpdate) {
			

			
			String categoryName = categoryService.findCategoryNameByIdAndCode(
					SlipStatusCategories.RO_SLIP_STATUS, dto.status);

			
			String strSlipLabel = MessageResourcesUtil
					.getMessage("erroes.db.roSlip");
			
			String strActionLabel = MessageResourcesUtil
					.getMessage("words.action.edit");

			
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.slip.lock", strSlipLabel,
							categoryName, strActionLabel));
		}

		ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
	}

	/**
	 * 削除の後処理を行います.<br>
	 * オンライン受注データを削除します.
	 * @param dto {@link ROrderSlipDto}
	 * @throws Exception
	 */
	@Override
	protected void afterDelete(AbstractSlipDto<ROrderLineDto> dto) throws Exception {
		
		deleteOnlineOrderRelRec();
	}

	/**
	 * 登録/更新の後処理を行います.<br>
	 * オンライン受注データ関連データの登録・削除を行います.
	 * @param bInsert 新規登録か否か
	 * @param dto {@link ROrderSlipDto}
	 * @throws Exception
	 */
	@Override
	protected void afterUpsert(boolean bInsert, AbstractSlipDto<ROrderLineDto> dto)
			throws Exception {

		
		if (this.inputROrderForm.isOnlineOrder) {
			
			
			
			if (!onlineOrderRelService
					.hasRecordByROrderSlip(this.inputROrderForm.roSlipId)) {
				OnlineOrderWork work = new OnlineOrderWork();
				ROrderLineDto rorderlineDto = this.inputROrderForm.lineList
						.get(0);
				work.onlineOrderId = this.inputROrderForm.onlineOrderId;
				work.onlineItemId = this.inputROrderForm.onlineOrderId;
				onlineOrderRelService.insertOnlineOrderRel(rorderlineDto, work);
			}
		}
	}

	/**
	 * 登録/更新の前処理を行います.
	 * <p>
	 * 1:画面項目の初期化<br>
	 * 2:明細行の初期化
	 * </p>
	 * @param bInsert 新規登録か否か
	 * @param param {@link ROrderSlipDto}
	 * @throws Exception
	 */
	@Override
	protected void beforeUpsert(boolean bInsert, AbstractSlipDto<ROrderLineDto> param)
			throws Exception {

		
		makeListByForm();
		setValueToName();

		ROrderSlipDto dto = (ROrderSlipDto) param;
		initForms(dto);
		dto.cutoffGroup = this.categoryService
				.cutoffGroupCategoryToCutoffGroup(inputROrderForm.cutoffGroupCategory);
		dto.paybackCycleCategory = this.categoryService
				.cutoffGroupCategoryToPaybackCycleCategory(inputROrderForm.cutoffGroupCategory);

		List<ROrderLineDto> lineList = inputROrderForm.lineList;

		
		boolean isComp = true;
		for (int i = 0; i < lineList.size(); i++) {
			ROrderLineDto lineDto = lineList.get(i);
			if (!"9".equals(lineDto.status)) {
				isComp = false;
			}
		}
		if (isComp) {
			dto.status = "9"; 
		}

		
		dto.dcName = this.inputROrderForm.dcName;
		dto.dcTimezone = this.inputROrderForm.dcTimezone;
	}

	/**
	 * 受注伝票のラベルのキーを返します.
	 * @return 受注伝票のラベルのキー
	 */
	@Override
	public String getSlipKeyLabel() {
		return "labels.report.hist.roSlip.roSlipId";
	}
}
