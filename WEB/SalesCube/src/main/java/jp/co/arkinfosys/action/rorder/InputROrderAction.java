/*
 * Copyright 2009-2010 Ark Information Systems.
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
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateDto;
import jp.co.arkinfosys.dto.estimate.InputEstimateLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.OnlineOrderWork;
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
import jp.co.arkinfosys.service.EstimateLineService;
import jp.co.arkinfosys.service.EstimateSheetService;
import jp.co.arkinfosys.service.OnlineOrderRelService;
import jp.co.arkinfosys.service.OnlineOrderService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.ProductStockService;
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

	/** 顧客納入先リスト(詳細) **/
	private List<DeliveryAndPre> deliveryPreList = new ArrayList<DeliveryAndPre>();

	/** 税転嫁リスト */
	public List<LabelValueBean> taxShiftCategoryList = new ArrayList<LabelValueBean>();

	// 完納区分リストの内容
	public List<LabelValueBean> statusCategoryList = new ArrayList<LabelValueBean>();

	// 配送業者リストの内容
	public List<LabelValueBean> dcCategoryList = new ArrayList<LabelValueBean>();

	// 配送時間帯リストの内容
	public List<LabelValueBean> dcTimeZoneCategoryList = new ArrayList<LabelValueBean>();

	/**
	 * 見積情報
	 */
	@Resource
	protected EstimateSheetService estimateSheetService;

	/**
	 * 見積情報明細
	 */
	@Resource
	protected EstimateLineService estimateLineService;


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
	 *  消費税率プルダウン
	 */
	public List<LabelValueBean> ctaxRateList;

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

			// 初期化関数
			this.inputROrderForm.initialize();
			this.inputROrderForm.initDc();
			this.inputROrderForm.initializeScreenInfo();
			initForms();

			// オンライン受注テーブルからデータを取得
			List<OnlineOrderWork> list = onlineOrderService
					.findOnlineOrderWorkByRoId(this.inputROrderForm.roSlipId);

			if (list.size() == 0) {
				// 検索結果がなかった
				// 該当する受注伝票が存在しない場合
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dataNotExist", "受注ＩＤ",
								inputROrderForm.roSlipId));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return Mapping.ONLINE_ORDER;
			}

			// アクションフォームをセットアップ
			this.inputROrderForm.setUp(list);


			// 顧客情報を取得
			Customer customer = customerService
					.findCustomerByCode(this.inputROrderForm.customerCode);

			if (customer == null) {
				// 該当する顧客が存在しない場合
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dataNotExist", "顧客データ",
								inputROrderForm.customerCode));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

				return Mapping.ONLINE_ORDER;
			}

			// 顧客情報を設定
			this.inputROrderForm.customerName = customer.customerName;
			this.inputROrderForm.taxShiftCategory = customer.taxShiftCategory;
			this.inputROrderForm.taxFractCategory = customer.taxFractCategory;
			this.inputROrderForm.priceFractCategory = customer.priceFractCategory;
			this.inputROrderForm.cutoffGroupCategory = customer.cutoffGroup
					+ customer.paybackCycleCategory;
			this.inputROrderForm.salesCmCategory = customer.salesCmCategory;
			this.inputROrderForm.customerRemarks = customer.remarks;
			this.inputROrderForm.customerCommentData = customer.commentData;

			// オンライン受注 配送料情報取得
			ProductJoin pjShip = productService
					.findById(Constants.EXCEPTIANAL_PRODUCT_CODE.ONLINE_DELIVERY_PRICE);
			// オンライン受注 配送料計算用
			BigDecimal sumOnlineShippingPrice = BigDecimal.ZERO;

			// 明細行
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

				// 以下、画面上hidden項目
				dto.supplierPcode = pj.supplierPcode;
				dto.roMaxNum = String.valueOf(pj.roMaxNum);
				StockInfoDto stockInfo = productStockService
						.calcStockQuantityByProductCode(dto.productCode);
				dto.possibleDrawQuantity = String
						.valueOf(stockInfo.possibleDrawQuantity);
				dto.taxCategory = pj.taxCategory;
				dto.ctaxRate = this.inputROrderForm.taxRate;
				dto.stockCtlCategory = pj.stockCtlCategory;
				dto.deletable = true; // この行は削除させない
				index++;
				this.inputROrderForm.lineList.add(dto);

				// 手数料(=オンライン配送料)を求める計算
				if ((orderLine.shippingPrice != null)) {
					sumOnlineShippingPrice = sumOnlineShippingPrice
							.add(orderLine.shippingPrice);
				}
			}

			// 手数料(=オンライン配送料)の追加
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

				// 税抜き金額を計算
				BigDecimal noTax = toNoTax(customer, sumOnlineShippingPrice,
						new BigDecimal(this.inputROrderForm.taxRate));
				dtoShip.unitCost = noTax.toString();
				dtoShip.cost = noTax.toString();
				dtoShip.unitRetailPrice = noTax.toString();
				dtoShip.retailPrice = noTax.toString();

				dtoShip.productRemarks = pjShip.remarks;
				dtoShip.eadRemarks = pjShip.eadRemarks;

				// 以下、画面上hidden項目
				dtoShip.supplierPcode = pjShip.supplierPcode;
				dtoShip.roMaxNum = String.valueOf(pjShip.roMaxNum);
				dtoShip.possibleDrawQuantity = "1";
				dtoShip.taxCategory = pjShip.taxCategory;
				dtoShip.ctaxRate = this.inputROrderForm.taxRate;
				dtoShip.deletable = true; // この行は削除させない

				index++;
				this.inputROrderForm.lineList.add(dtoShip);
			}

			// 最大数以下だったら明細行を作る
			ROrderSlipDto dto = (ROrderSlipDto) inputROrderForm.copyToDto();
			dto.fillList();
			dto.copyTo(inputROrderForm.lineList);

			// リストの初期化
			makeListByForm();

			// オンライン用の初期化（納入先欄が入力可能なことによる）
			initOnlineOrderForms();

			this.inputROrderForm.isImport = true;

			if (super.messages.size() > 0) {
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		// 入力画面を表示
		return Mapping.INPUT;
	}

	private static final int BigDecimalModeCodeIntError = -1;

	/**
	 * 端数処理モードを受け取ってBigDecimalでの表現に変換して返します.
	 * @param taxFractCategory 端数処理モード
	 * @return 引数の端数処理モードのBigDecimal表現
	 */
	private int bigDecimalModeCodeFromStringToInt(String taxFractCategory) {
		//切捨て
		if (CategoryTrns.FLACT_CATEGORY_DOWN.equals(taxFractCategory)) {
			return BigDecimal.ROUND_DOWN;
		}
		//切り上げ
		else if (CategoryTrns.FLACT_CATEGORY_UP.equals(taxFractCategory)) {
			return BigDecimal.ROUND_UP;
		}
		//四捨五入
		else if (CategoryTrns.FLACT_CATEGORY_HALF_UP.equals(taxFractCategory)) {
			return BigDecimal.ROUND_HALF_UP;
		}
		//なにこれ？
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
								new BigDecimal(100.0) //100.0だと割り切れないことは無いのですが念のため
								,
								mineDto.statsDecAlignment + 1 //+1：有効桁数確保
								,
								bigDecimalModeCodeFromStringToInt(CategoryTrns.FLACT_CATEGORY_HALF_UP))); //統計系数値は四捨五入固定

		//端数処理方式コード取得
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
		// 現在の税率を取得し、画面に設定する
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
		// 指定日の税率を取得し、画面に設定する
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

		// 税率等初期化
		if (!StringUtil.hasLength(inputROrderForm.taxFractCategory)) {
			inputROrderForm.taxFractCategory = mineDto.taxFractCategory;
		}
		if (!StringUtil.hasLength(inputROrderForm.taxShiftCategory)) {
			inputROrderForm.taxShiftCategory = mineDto.taxShiftCategory;
		}
		if (!StringUtil.hasLength(inputROrderForm.priceFractCategory)) {
			inputROrderForm.priceFractCategory = mineDto.priceFractCategory;
		}
		// 配送関連初期化
		initDc();

		if (Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER
				.equals(inputROrderForm.customerCode)) {
			// オンライン用の初期化
			initOnlineOrderForms();
		}
	}

	/**
	 * オンラインデータフラグをONにします.
	 * @throws Exception
	 */
	public void initOnlineOrderForms() throws Exception {
		// オンラインデータフラグをON
		this.inputROrderForm.isOnlineOrder = true;
	}

	/**
	 * 初期化関数です.<br>
	 * 税率、配送関連の値を初期化する他に、画面項目の制御を行います.
	 * @throws Exception
	 */
	public void initForms(ROrderSlipDto dto) throws Exception {
		// 同期トークンのセッションへの登録
		TokenProcessor.getInstance().saveToken(request);

		// プルダウンの生成
		makeList(dto);

		// 明細リストを初期行数にする

		if (!this.inputROrderForm.isImport
				&& inputROrderForm.lineList.size() == 0) {
			dto.fillList();
			dto.copyTo(inputROrderForm.lineList);
		}

		// 税率初期化
		initTax();

		// 「売上完了」の場合のみ、更新不可とする。
		inputROrderForm.statusUpdate = !(Constants.STATUS_RORDER_SLIP.SALES_FINISH
				.equals(dto.status));

		// 更新できない場合は削除もできない
		inputROrderForm.deletable = inputROrderForm.statusUpdate;

		// 税率等初期化
		inputROrderForm.taxFractCategory = dto.taxFractCategory;
		inputROrderForm.taxShiftCategory = dto.taxShiftCategory;
		inputROrderForm.priceFractCategory = dto.priceFractCategory;

		// 配送関連初期化
		initDc();

		if (Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER
				.equals(inputROrderForm.customerCode)) {
			// オンライン用の初期化
			initOnlineOrderForms();
		}
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します(伝票新規作成時).
	 * @throws Exception
	 */
	private void makeListByForm() throws Exception {
		// 税転嫁プルダウンの値
		String code = inputROrderForm.taxShiftCategory;
		String name = this.categoryService.findCategoryNameByIdAndCode(
				Categories.ART_TAX_SHIFT_CATEGORY, code);
		this.taxShiftCategoryList.add(new LabelValueBean(name, code));

		// 敬称プルダウンの値
		this.preTypeCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PRE_TYPE);
		this.preTypeCategoryList.add(0, new LabelValueBean("", ""));

		// 支払条件プルダウンの値
		code = inputROrderForm.cutoffGroupCategory;
		name = this.categoryService.findCategoryNameByIdAndCode(
				Categories.CUTOFF_GROUP, code);
		this.cutOffList.add(new LabelValueBean(name, code));

		// 取引区分プルダウンの値
		code = inputROrderForm.salesCmCategory;
		name = this.categoryService.findCategoryNameByIdAndCode(
				Categories.SALES_CM_CATEGORY, code);
		this.salesCmCategoryList.add(new LabelValueBean(name, code));

		// 配送業者プルダウンの値
		this.dcCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_CATEGORY);
		this.dcCategoryList.add(0, new LabelValueBean("", ""));

		// 配送時間帯プルダウンの値
		this.dcTimeZoneCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_TIMEZONE_CATEGORY);
		this.dcTimeZoneCategoryList.add(0, new LabelValueBean("", ""));

		List<DeliveryAndPre> list = this.deliveryService
				.searchDeliveryByCompleteCustomerCodeSortedByCreDate(inputROrderForm.customerCode);
		for (DeliveryAndPre delivery : list) {
			this.deliveryList.add(new LabelValueBean(delivery.deliveryName,
					delivery.deliveryCode));
		}

		// 完納区分　リスト作成
		createCategoryList(Categories.RO_LINE_STATUS, statusCategoryList, false);

		// 消費税率プルダウンリスト
		this.ctaxRateList =  ListUtil.getRateTaxNoBlankList(super.taxRateService);


	}

	/**
 	 * 画面表示に使用しているプルダウン等の情報を作成します(伝票更新時).
	 * @throws Exception
	 */
	private void makeList(ROrderSlipDto record) {
		try {
			// 税転嫁プルダウンの値
			taxShiftCategoryList = new ArrayList<LabelValueBean>();
			String code = record.taxShiftCategory;
			String name = this.categoryService.findCategoryNameByIdAndCode(
					Categories.ART_TAX_SHIFT_CATEGORY, code);
			this.taxShiftCategoryList.add(new LabelValueBean(name, code));

			// 敬称プルダウンの値
			preTypeCategoryList = new ArrayList<LabelValueBean>();
			this.preTypeCategoryList = categoryService
					.findCategoryLabelValueBeanListById(Categories.PRE_TYPE);
			this.preTypeCategoryList.add(0, new LabelValueBean("", ""));

			// 支払条件プルダウンの値
			cutOffList = new ArrayList<LabelValueBean>();
			code = record.cutoffGroup + record.paybackCycleCategory;
			name = this.categoryService.findCategoryNameByIdAndCode(
					Categories.CUTOFF_GROUP, code);
			this.cutOffList.add(new LabelValueBean(name, code));

			// 取引区分プルダウンの値
			salesCmCategoryList = new ArrayList<LabelValueBean>();
			code = record.salesCmCategory;
			name = this.categoryService.findCategoryNameByIdAndCode(
					Categories.SALES_CM_CATEGORY, code);
			this.salesCmCategoryList.add(new LabelValueBean(name, code));

			// 顧客納入先プルダウンの値
			deliveryList.clear();
			if (StringUtil.hasLength(record.customerCode)) {
				List<DeliveryAndPre> list = this.deliveryService
						.searchDeliveryByCompleteCustomerCodeSortedByCreDate(record.customerCode);
				for (DeliveryAndPre delivery : list) {
					this.deliveryList.add(new LabelValueBean(
							delivery.deliveryName, delivery.deliveryCode));
				}
			}

			// 完納区分　リスト作成
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

		// 配送業者プルダウンの値
		this.dcCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DC_CATEGORY);
		this.dcCategoryList.add(0, new LabelValueBean("", ""));

		// 配送時間帯プルダウンの値
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
		//inputROrderForm.initialize();
		inputROrderForm.upsertInitialize();
		try {
			// 存在する顧客か確認
			if (StringUtil.hasLength(this.inputROrderForm.customerCode)
					&& customerService
							.isExistCustomerCode(inputROrderForm.customerCode) == false) {
				// 顧客コードがXXのデータは存在しません
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
			// 存在する商品か確認
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
		// 明細行に存在する商品コードが実在するか確認する
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
			// 正常なデータが１件も無い時はエラー
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
			// そもそも更新不可の場合はチェックしない
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
		// 以下、画面上hidden項目
		dto.supplierPcode = pj.supplierPcode;
		dto.roMaxNum = "1";
		dto.possibleDrawQuantity = "1";
		dto.taxCategory = pj.taxCategory;
		dto.ctaxRate = this.inputROrderForm.taxRate;
		dto.deletable = false; // この行は削除させない

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

		// 配送業者名設定
		for (LabelValueBean lvb : dcCategoryList) {
			if (lvb.getValue().equals(inputROrderForm.dcCategory)) {
				inputROrderForm.dcName = lvb.getLabel();
				break;
			}
		}
		// 配送時間帯設定
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
//		if (inputROrderForm.isNewData()) {
			makeListByForm();
//		}
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
		// 伝票情報をロードする
		ROrderSlipDto dto = roSlipService
				.loadBySlipId(inputROrderForm.roSlipId);

		if (dto == null) {
			return false;
		}

		Beans.copy(dto, inputROrderForm).execute();

		// 明細情報をロードする
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

		// リストを初期化
		ROrderSlipDto dto = (ROrderSlipDto) inputROrderForm.copyToDto();
		initForms(dto);

		// 各行に引当可能数を設定
		for (ROrderLineDto lineDto : this.inputROrderForm.lineList) {
			if (!StringUtil.hasLength(lineDto.productCode)) {
				continue;
			}
			StockInfoDto stockInfo = productStockService
					.calcStockQuantityByProductCode(lineDto.productCode);
			lineDto.possibleDrawQuantity = String
					.valueOf(stockInfo.possibleDrawQuantity);
			// 完納状態を設定
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

			//hidden設定　未納数、数量
			lineDto.quantityDB = lineDto.quantity;
			lineDto.restQuantityDB = lineDto.restQuantity;
		}
		// オンライン関係のデータを確認
		setDeletableByOnlineOrder();

		if (!inputROrderForm.statusUpdate) {
			// メッセージを保存

			// 状態名を取得
			String categoryName = categoryService.findCategoryNameByIdAndCode(
					SlipStatusCategories.RO_SLIP_STATUS, dto.status);

			// 伝票名取得
			String strSlipLabel = MessageResourcesUtil
					.getMessage("erroes.db.roSlip");
			// 動作名取得
			String strActionLabel = MessageResourcesUtil
					.getMessage("words.action.edit");

			// メッセージに設定
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
		// オンライン連携レコードが存在する場合削除する
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

		// オンラインデータの登録の場合は、関連テーブルに登録し、オンライン受注テーブルを削除する
		if (this.inputROrderForm.isOnlineOrder) {
			// 関連テーブルに登録
			// 受付番号1件につき、1件だけ登録する。
			// 関連テーブルにデータが無かったら登録する
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

		// リストを初期化
		makeListByForm();
		setValueToName();

		ROrderSlipDto dto = (ROrderSlipDto) param;
		initForms(dto);
		dto.cutoffGroup = this.categoryService
				.cutoffGroupCategoryToCutoffGroup(inputROrderForm.cutoffGroupCategory);
		dto.paybackCycleCategory = this.categoryService
				.cutoffGroupCategoryToPaybackCycleCategory(inputROrderForm.cutoffGroupCategory);

		List<ROrderLineDto> lineList = inputROrderForm.lineList;

		// 明細行の状態フラグが全て「売上完了」の場合、伝票の状態フラグも「9:売上完了」とする。
		boolean isComp = true;
		for (int i = 0; i < lineList.size(); i++) {
			ROrderLineDto lineDto = lineList.get(i);
			if (!"9".equals(lineDto.status)) {
				isComp = false;
			}
		}
		if (isComp) {
			dto.status = "9"; // 売上完了
		}

		// 配送業者名称と配送時間帯名称を設定
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

	/**
	 * 指定された見積伝票番号から受注に関する情報を取得し、画面に表示します.
	 *
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute( urlPattern = "copyFromEstimate/{copySlipId}", validator = false)
	public String copyFromEstimate() throws Exception {
		return copy();
	}

	/**
	 * 伝票を複写します.<br>
	 * <p>
	 * 見積番号を元に受注伝票情報を取得し、<br>
	 * 受注入力画面に設定します.
	 * </p>
	 *
	 * @return 受注入力画面のパス
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#copy()
	 */
	@Override
	public String copy() throws Exception {
		prepareForm();

		try {
			// formの初期化
			inputROrderForm.initialize();

			// 見積伝票複写
			if( "".equals(inputROrderForm.copySlipId)){
				String strLabel = MessageResourcesUtil.getMessage("labels.estimateSheetId");
				addMessage("errors.notExist",strLabel);
				return Mapping.INPUT;
			}
			// 見積伝票番号から受注伝票を生成する
			if(!createROrderSlipByEstimate(inputROrderForm.copySlipId)) {
				inputROrderForm.reset();
			}

			// リスト作成
			this.createList();

			// 初期値を設定する
			inputROrderForm.initCopy();

			// ユーザの権限を設定
			inputROrderForm.menuUpdate = userDto
					.isMenuUpdate(Constants.MENU_ID.INPUT_RORDER);

			// 顧客納品先リストの作成
			if(StringUtil.hasLength( inputROrderForm.customerCode)){
				// 初期表示時に作成された納入先リストをクリアし、顧客の納入先を再取得する
				this.deliveryList.clear();
				createDeliveryList();
				if(deliveryList != null && deliveryList.size() > 0){
					// 顧客の納入先情報を設定する(先頭の項目を初期値とする)
					inputROrderForm.initializeDeliveryInfo(deliveryPreList.get(0), preTypeCategoryList);
				}
			}

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}


		return Mapping.INPUT;
	}

	/**
	 * 顧客納入先リストを作成します.<br>
	 * 顧客コードが設定されていない時には、空行だけの納入先リストを作成します.<BR>
	 * 顧客コードが設定されている場合には、顧客マスタの納入先リストを作成します.
	 * @throws ServiceException
	 */
	protected void createDeliveryList() throws ServiceException{
		if( !StringUtil.hasLength( inputROrderForm.customerCode ) ){
			LabelValueBean bean = new LabelValueBean();
			bean.setValue("");
			bean.setLabel("");
			deliveryList.add(bean);
			return;
		}

		try {
			deliveryPreList =
				deliveryService.searchDeliveryListByCompleteCustomerCode(
						inputROrderForm.customerCode );

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
	 * 見積伝票を検索し受注伝票のアクションフォームに設定します.<BR>
	 *
	 * 1. 見積伝票を検索します。見つからなかった場合には、エラーメッセージを登録し処理を終了します.<BR>
	 * 2. 見積伝票の明細行を取得します.<BR>
	 * 3. 受注伝票に設定されている顧客を検索し、見つからなかった場合には、エラーメッセージを登録します.<BR>
	 * 4. 受注伝票の内容をアクションフォームに設定します.<BR>
	 * 5. 受注伝票に存在しない情報を検索し、アクションフォームに設定します.<BR>
	 *
	 * @param roSlipId 検索対象見積伝票番号
	 * @return 正常に設定できたか否か
	 * @throws ServiceException
	 */
	protected boolean createROrderSlipByEstimate( String estimateSlipId ) throws ServiceException {

		// 見積伝票を読み込む
		InputEstimateDto estimateDto = estimateSheetService.loadBySlipId(estimateSlipId);
		if (estimateDto == null){
			// 見積番号が存在しない場合、エラーメッセージを表示
			addMessage("errors.copy.notexist");
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return false;
		}
		List<InputEstimateLineDto> lineList = estimateLineService.loadBySlip(estimateDto);
		estimateDto.setLineDtoList(lineList);

		// 顧客情報を取得
		Customer customer = null;
		if( StringUtil.hasLength(estimateDto.customerCode) ){
			customer = customerService.findCustomerByCode(estimateDto.customerCode);
			if(customer == null){
				// 顧客コードがXXのデータは存在しません
				String strLabel = MessageResourcesUtil.getMessage("labels.customerCode");
				addMessage( "errors.dataNotExist", strLabel, inputROrderForm.customerCode );
			}
		}

		// 受注伝票用ActionFormに展開する
		inputROrderForm.initialize( estimateDto, customer );

		// 関連情報を取得
		ROrderSlipDto dto = (ROrderSlipDto) inputROrderForm.copyToDto();
		dto.copyFrom(inputROrderForm.lineList);
		List<ROrderLineDto> roLineList = dto.getLineDtoList();
		for (ROrderLineDto lineDto : roLineList) {
			roLineService.setProductStockInfoFromCopy(lineDto);	// ここで、不足する商品情報と在庫情報を設定する
		}

		return true;
	}
}
