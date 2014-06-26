/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.ListUtil;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.purchase.PurchaseLineDto;
import jp.co.arkinfosys.dto.purchase.PurchaseSlipDto;
import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.Rack;
import jp.co.arkinfosys.entity.Supplier;
import jp.co.arkinfosys.entity.SupplierLineTrn;
import jp.co.arkinfosys.entity.SupplierSlipTrn;
import jp.co.arkinfosys.entity.join.PoLineTrnJoin;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.purchase.InputPurchaseForm;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.PoSlipService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.SupplierLineService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.SupplierSlipService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.InputStockPurchaseService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 仕入入力画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputPurchaseAction extends AbstractSlipEditAction<PurchaseSlipDto, PurchaseLineDto> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "inputPurchase.jsp";
		public static final String EDIT = "/purchase/inputPurchase/edit/";
		public static final String SEARCH = "/purchase/searchPurchase";
	}

	private String inputURIString = InputPurchaseAction.Mapping.INPUT;

	/** 明細行の最大行数 */
	private static final int MAX_LINE_ROW_COUNT = 35;

	/** 明細行のタブ移動可能項目数 */
	private static final int LINE_ELEMENT_COUNT = 9;

	@ActionForm
	@Resource
	private InputPurchaseForm inputPurchaseForm;

	@Resource
	private SupplierSlipService supplierSlipService;

	@Resource
	private SupplierLineService supplierLineService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductService productService;

	@Resource
	private RackService rackService;

	@Resource
	private SupplierService supplierService;

	@Resource
	private PoSlipService poSlipService;

	@Resource
	protected InputStockPurchaseService inputStockPurchaseService;

	/**
	 * 仕入明細区分の選択値リスト
	 */
	public List<LabelValueBean> supplierDetailCategoryList;

	/**
	 * 完納区分の選択値リスト
	 */
	public List<LabelValueBean> deliveryProcessCategoryList;

	/**
	 * 消費税率プルダウン
	 */
	public List<LabelValueBean> ctaxRateList;

	/**
	 *
	 * 伝票データを取得します.
	 * @return 読み込みできたか否か
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected boolean loadData() throws Exception, ServiceException {
		// DTOの取得　伝票のロード
		PurchaseSlipDto dto = supplierSlipService.loadBySlipId(inputPurchaseForm.supplierSlipId);
		if (dto == null){
			return false;
		}

		// 明細をロードする
		List<PurchaseLineDto> supplierLineTrnDtoList = supplierLineService.loadBySlip(dto);

		// 明細情報を伝票にセットする
		supplierSlipService.setLineData(dto,supplierLineTrnDtoList);

		// DTOの値をFormへコピー
		Beans.copy(dto, inputPurchaseForm).execute();//OK

		return true;
	}

	/**
	 * 登録/更新処理を行います.
	 *
	 * @return 遷移先URI
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#upsert()
	 */
	@Override
	public String upsert() throws Exception {
		String uriString = super.upsert();
		this.afterLoad();
		return uriString;
	}

	/**
	 * 伝票読み込み後の処理です.
	 * <p>
	 * 1:不要な明細行を削除<br>
	 * 2:関連伝票の状態を元にメッセージの表示と画面制御を実施.
	 * </p>
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected void afterLoad() throws Exception,
			ServiceException {
		// 明細行リストを取得する
		List<PurchaseLineDto> lineList = this.inputPurchaseForm.lineDtoList;

		// 空行を削除して新規リストを作成する
		List<PurchaseLineDto> newLineList = new ArrayList<PurchaseLineDto>();

		for(PurchaseLineDto lineDto : lineList) {
			// 空白行以外はリストに追加する
			if(!lineDto.isBlank()) {
				newLineList.add(lineDto);
			}
		}

		// 新リストをフォームに設定する
		this.inputPurchaseForm.lineDtoList = newLineList;

		// 複写元が委託在庫発注伝票の場合、明細の数量を無効化する必要があるため、Formにフラグをセットする
		PoSlipTrn poSlipTrnSingle = poSlipService
				.loadPOSlip(inputPurchaseForm.poSlipId);
		if(poSlipTrnSingle != null && CategoryTrns.TRANSPORT_CATEGORY_ENTRUST.equals(poSlipTrnSingle.transportCategory) ) {
			inputPurchaseForm.isEntrustPorder = true;
		} else {
			inputPurchaseForm.isEntrustPorder = false;
		}

		if( !Constants.STATUS_SUPPLIER_SLIP.UNPAID.equals(inputPurchaseForm.status)){
			// 発注状態以外は編集不可
			// 状態名を取得
			String categoryName = categoryService.findCategoryNameByIdAndCode(
					SlipStatusCategories.SUPPLIER_SLIP_STATUS, inputPurchaseForm.status);// statusはFormから取るように変更
			// 伝票名取得
			String strSlipLabel = MessageResourcesUtil.getMessage("erroes.db.supplierSlip");
			// 動作名取得
			String strActionLabel = MessageResourcesUtil.getMessage("words.action.edit");

			// メッセージに設定
			this.addMessage("infos.slip.lock", strSlipLabel, categoryName, strActionLabel);
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

			// 更新不可
			inputPurchaseForm.menuUpdate = false;

		} else if(StringUtil.hasLength(this.inputPurchaseForm.paymentCutoffDate)) {
			// 状態名を取得
			String categoryName = MessageResourcesUtil.getMessage("labels.alreadyPaymentCutoff");
			// 伝票名取得
			String strSlipLabel = MessageResourcesUtil.getMessage("erroes.db.supplierSlip");
			// 動作名取得
			String strActionLabel = MessageResourcesUtil.getMessage("words.action.edit");
			// メッセージに設定
			this.addMessage("infos.slip.lock", strSlipLabel, categoryName, strActionLabel);
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
			// 更新不可
			inputPurchaseForm.menuUpdate = false;

		}else{
			// ユーザの権限を設定
			inputPurchaseForm.menuUpdate = userDto
					.isMenuUpdate(Constants.MENU_ID.INPUT_PURCHASE);
		}

	}

	/**
	 * 登録/更新の前処理を行います.
	 * <p>
	 * ステータスの設定と入出庫伝票の締め状態を確認します.
	 * </p>
	 *
	 * @param insert 新規登録か否か
	 * @param dto 仕入伝票DTO
	 * @throws Exception
	 */
	@Override
	protected void beforeUpsert(boolean insert, AbstractSlipDto<PurchaseLineDto> dto)
			throws Exception {
		PurchaseSlipDto inDto = (PurchaseSlipDto)dto;

		if (inputPurchaseForm.isNewData()) {
			((PurchaseSlipDto)inDto).status = Constants.STATUS_SUPPLIER_SLIP.UNPAID;// 未払い
		}else{
			// 入出庫が締済みかどうか
			if(this.inputStockPurchaseService.existsClosedEadSlip(inDto)) {
				// 遷移先を設定
				this.inputURIString = Mapping.EDIT + inDto.supplierSlipId + "?redirect=true";
				ServiceException e = new ServiceException("errors.update.stockclosed");
				e.setStopOnError(false);
				throw e;
			}
		}
	}

	/**
	 * 登録/更新の後処理を行います.
	 * <p>
	 * 1:入出庫伝票の登録・更新<br>
	 * 2:ユーザ権限の設定
	 * </p>
	 * @param insert 新規登録か否か
	 * @param dto 仕入伝票DTO
	 * @throws Exception
	 */
	@Override
	protected void afterUpsert(boolean insert, AbstractSlipDto<PurchaseLineDto> dto)
			throws Exception {
		PurchaseSlipDto pdto = (PurchaseSlipDto)dto;
		supplierSlipService.afterUpsert(insert, pdto,supplierLineService.getDeletedLineStoreDto());

		if (pdto.newData == null || pdto.newData ) {
			// 入出庫伝票の登録
			inputStockPurchaseService.insert(pdto);
		} else {
			// 入出庫伝票の更新
			inputStockPurchaseService.update(pdto);
		}

		// ユーザの権限を設定
		inputPurchaseForm.menuUpdate = userDto
				.isMenuUpdate(Constants.MENU_ID.INPUT_PURCHASE);

	}

	/**
	 * 削除の前処理を行います.
	 * <p>
	 * 1:カンマ除去<br>
	 * 2:入出庫伝票の締め状態確認
	 * </p>
	 *
	 * @param dto 仕入伝票DTO
	 * @throws Exception
	 */
	@Override
	protected void beforeDelete(AbstractSlipDto<PurchaseLineDto> dto) throws Exception {
		// hiddenのカンマ除去
		deleteComma();

		// 入出庫が締済みかどうか
		if(this.inputStockPurchaseService.existsClosedEadSlip((PurchaseSlipDto)dto)) {
			ServiceException e = new ServiceException("errors.delete.stockclosed");
			this.inputURIString = Mapping.EDIT + inputPurchaseForm.supplierSlipId + "?redirect=true";
			throw e;
		}
	}

	/**
	 * 削除の後処理を行います.
	 * <p>
	 * 1:関連仕入伝票明細行のステータス更新<br>
	 * 2:発注伝票のステータス更新<br>
	 * 3:入出庫伝票の削除
	 * </p>
	 *
	 * @param d {@link PurchaseSlipDto}
	 * @throws Exception
	 */
	@Override
	protected void afterDelete(AbstractSlipDto<PurchaseLineDto> d) throws Exception {

		PurchaseSlipDto dto = (PurchaseSlipDto) d;

		// 関連仕入伝票の明細行ステータス更新
		supplierLineService.updateRelatedSlipLinesBySlip(dto);
		// 発注伝票のステータス更新
		supplierSlipService.updatePoStatus(dto);
		// 入出庫伝票を削除
		inputStockPurchaseService.delete(dto);
	}

	/**
	 * 指定された発注伝票番号の仕入情報を取得し、画面に表示します.
	 *
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute( urlPattern = "copySlipFromPorder/{copySlipId}", validator = false)
	public String copy() throws Exception {

		inputPurchaseForm.newData = true;
		return copySlipFromPorderFunc();
	}

	/**
	 * 指定された発注伝票番号の仕入情報を取得し、画面に表示します.
	 *
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String copySlipFromPorderLoad() throws Exception {
		return copySlipFromPorderFunc();
	}

	/**
	 * 指定された発注伝票番号の仕入情報を取得し、画面に表示します.
	 *
	 * @return 遷移先URI
	 * @throws Exception
	 */
	private String copySlipFromPorderFunc() throws Exception {

		// 抽象クラスのフレームワークのcopyの流れに合わない
		try {
			inputPurchaseForm.copySlipId = StringUtil.decodeSL(inputPurchaseForm.copySlipId);
			// copy元 発注伝票の取得
			PoSlipTrn poSlipTrnSingle = poSlipService
					.loadPOSlip(inputPurchaseForm.copySlipId);
			List<PoLineTrnJoin> poLineTrnList = poSlipService
					.loadPOLine(inputPurchaseForm.copySlipId);

			// プルダウンの初期化
			createList();

			// 発注番号が存在しない
			if(poSlipTrnSingle == null){
				// 発注番号をクリアする
				inputPurchaseForm.poSlipId = "";

				// ユーザの権限を設定
				inputPurchaseForm.menuUpdate = userDto
						.isMenuUpdate(Constants.MENU_ID.INPUT_PAYMENT);

				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.copy.notexist"));
				ActionMessagesUtil.addErrors(super.httpSession, super.messages);
				// 明細行の調整
				PurchaseSlipDto dto = (PurchaseSlipDto) this.createDTO();
				dto.setLineDtoList(inputPurchaseForm.lineDtoList);
				dto.fillList();
				return Mapping.INPUT;
			}

			this.inputPurchaseForm.reset();

			// 明細行の残数量が全て０ならエラー
			BigDecimal zero = new BigDecimal("0");
			boolean allZero = true;
			for (PoLineTrnJoin poLine : poLineTrnList) {
				// 委託在庫発注の場合は、委託出庫済みの明細以外は複写対象外とする
				if( CategoryTrns.TRANSPORT_CATEGORY_ENTRUST.equals(poSlipTrnSingle.transportCategory) && ! Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_DELIVERED.equals(poLine.status) ) {
					continue;
				}

				// 発注伝票の発注残数=0ならば、伝票複写しない
				if( poLine.restQuantity.compareTo(zero) ==  0 ){
					continue;
				}
				allZero = false;
				break;
			}
			if( allZero ){
				// 発注番号をクリアする
				inputPurchaseForm.poSlipId = "";

				// ユーザの権限を設定
				inputPurchaseForm.menuUpdate = userDto
						.isMenuUpdate(Constants.MENU_ID.INPUT_PAYMENT);

				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.copy.notexist"));

				ActionMessagesUtil.addErrors(super.httpSession, super.messages);
				// 明細行の調整
				PurchaseSlipDto dto = (PurchaseSlipDto) this.createDTO();
				dto.setLineDtoList(inputPurchaseForm.lineDtoList);
				dto.fillList();
				return Mapping.INPUT;
			}

			// 数量端数処理
			Converter numConv = new NumberConverter(
					super.mineDto.productFractCategory,
					super.mineDto.numDecAlignment, true);
			// 円単価端数処理
			Converter yenConv = new NumberConverter(
					poSlipTrnSingle.priceFractCategory, 0, true);
			// 外貨単価端数処理
			Converter dolConv = new NumberConverter(
					poSlipTrnSingle.priceFractCategory,
					super.mineDto.unitPriceDecAlignment, true);
			// レート
			Converter rateConv = new NumberConverter(
					CategoryTrns.FLACT_CATEGORY_DOWN, 2, false);

			// 伝票
			inputPurchaseForm.poSlipId = String
					.valueOf(poSlipTrnSingle.poSlipId);
			inputPurchaseForm.supplierDate = StringUtil
					.getCurrentDateString(Constants.FORMAT.DATE);
			inputPurchaseForm.deliveryDate = StringUtil.getDateString(
					Constants.FORMAT.DATE, poSlipTrnSingle.deliveryDate);
			inputPurchaseForm.remarks = poSlipTrnSingle.remarks;

			// 複写元が委託在庫発注伝票の場合、明細の数量を無効化する必要があるため、Formにフラグをセットする
			if( CategoryTrns.TRANSPORT_CATEGORY_ENTRUST.equals(poSlipTrnSingle.transportCategory) ) {
				inputPurchaseForm.isEntrustPorder = true;
			} else {
				inputPurchaseForm.isEntrustPorder = false;
			}

			// 消費税率
			if(poSlipTrnSingle.ctaxRate == null){
				inputPurchaseForm.ctaxRate = "";
			}else{
				inputPurchaseForm.ctaxRate = poSlipTrnSingle.ctaxRate.toString();
			}

			// 仕入先情報
			inputPurchaseForm.supplierCode = poSlipTrnSingle.supplierCode;
			inputPurchaseForm.supplierName = poSlipTrnSingle.supplierName;
			inputPurchaseForm.priceFractCategory = poSlipTrnSingle.priceFractCategory;
			inputPurchaseForm.taxShiftCategory = poSlipTrnSingle.taxShiftCategory;
			inputPurchaseForm.taxFractCategory = poSlipTrnSingle.taxFractCategory;
			inputPurchaseForm.supplierCmCategory = poSlipTrnSingle.supplierCmCategory;
			if (poSlipTrnSingle.rateId != null) {
				inputPurchaseForm.rateId = String
						.valueOf(poSlipTrnSingle.rateId);
				SupplierJoin supplierJoin = supplierService
						.findSupplierRateByCodeDate(
								poSlipTrnSingle.supplierCode,
								poSlipTrnSingle.poDate);
				if(supplierJoin != null) {
					inputPurchaseForm.rateName = supplierJoin.supplierRateName;
					inputPurchaseForm.sign = supplierJoin.cUnitSign;
					try {
						inputPurchaseForm.rate = rateConv.getAsString(new BigDecimal(supplierJoin.supplierRate));
					} catch (NumberFormatException e) {
						super.errorLog(e);
					}
				}
			}

			if (poSlipTrnSingle.priceTotal != null) {
				inputPurchaseForm.priceTotal = yenConv
						.getAsString(poSlipTrnSingle.priceTotal);
			}
			if (poSlipTrnSingle.ctaxTotal != null) {
				inputPurchaseForm.ctaxTotal = yenConv
						.getAsString(poSlipTrnSingle.ctaxTotal);
				if (poSlipTrnSingle.priceTotal != null) {
					inputPurchaseForm.nonTaxPriceTotal = yenConv
							.getAsString(poSlipTrnSingle.priceTotal
									.subtract(poSlipTrnSingle.ctaxTotal));
				}
			}
			if (poSlipTrnSingle.fePriceTotal != null) {
				inputPurchaseForm.fePriceTotal = dolConv
						.getAsString(poSlipTrnSingle.fePriceTotal);
			}

			// 明細
			inputPurchaseForm.lineDtoList = new ArrayList<PurchaseLineDto>();
			int lineNo = 0;

			for (PoLineTrnJoin poLineTrn : poLineTrnList) {
				// 委託在庫発注の場合は、委託出庫済みの明細以外は複写対象外とする
				if( CategoryTrns.TRANSPORT_CATEGORY_ENTRUST.equals(poSlipTrnSingle.transportCategory) && ! Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_DELIVERED.equals(poLineTrn.status) ) {
					continue;
				}
				// 発注伝票の発注残数=0ならば、伝票複写しない
//				if( poLineTrn.restQuantity.compareTo(zero) ==  0 ){
				// 発注伝票明細が、仕入完了ならば伝票複写しない
				if( poLineTrn.status.equals(Constants.STATUS_PORDER_LINE.PURCHASED)){
					continue;
				}

				if(poLineTrn.quantity.compareTo(poLineTrn.restQuantity) != 0) {
					// 発注数と残数が異なる場合には表示時に金額計算
					inputPurchaseForm.initCalc = true;
				}

				PurchaseLineDto dto = Beans.createAndCopy(
						PurchaseLineDto.class, poLineTrn).excludes(
						AbstractService.Param.CRE_FUNC,
						AbstractService.Param.CRE_DATETM,
						AbstractService.Param.CRE_USER,
						AbstractService.Param.UPD_FUNC,
						AbstractService.Param.UPD_DATETM,
						AbstractService.Param.UPD_USER).converter(numConv,
						SupplierSlipService.Param.QUANTITY).converter(yenConv,
						SupplierSlipService.Param.UNIT_PRICE,
						SupplierSlipService.Param.PRICE).converter(dolConv,
						SupplierSlipService.Param.DOL_UNIT_PRICE,
						SupplierSlipService.Param.DOL_PRICE).execute();
				dto.lineNo = String.valueOf(++lineNo);

				// 発注伝票の発注残数を数量にセットする
				dto.quantity = numConv.getAsString(poLineTrn.restQuantity);

				// 更新前数量に0をセットする
				dto.oldQuantity = "0";

				// 発注伝票の発注数をトータル数量にセットする
				dto.totalQuantity = String.valueOf(poLineTrn.quantity.doubleValue());
				dto.deliveryProcessCategory = CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL;

				// 発注残数をセットする
				dto.restQuantity = String.valueOf(poLineTrn.restQuantity);

				if (poLineTrn.ctaxRate != null) {
					inputPurchaseForm.ctaxRate = String
							.valueOf(poLineTrn.ctaxRate);
				}
				// 標準棚番、倉庫名を設定
				if( StringUtil.hasLength(poLineTrn.productCode)){
					ProductJoin pj = productService.findById(poLineTrn.productCode);
					if( pj != null ){
						dto.rackCode = pj.rackCode;
						dto.rackName = pj.rackName;
						dto.warehouseName = pj.warehouseName;
					}
				}
				inputPurchaseForm.lineDtoList.add(dto);
			}

			// 明細行の調整
			PurchaseSlipDto dto = (PurchaseSlipDto) this.createDTO();
			dto.setLineDtoList(inputPurchaseForm.lineDtoList);

			// ユーザの権限を設定
			inputPurchaseForm.userId = this.userDto.userId;
			inputPurchaseForm.userName = this.userDto.nameKnj;
			inputPurchaseForm.menuUpdate = userDto
					.isMenuUpdate(Constants.MENU_ID.INPUT_PURCHASE);

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		return Mapping.INPUT;
	}

	/**
	 * 登録時の仕入伝票と仕入伝票明細行のチェックを行います.
	 *
	 * @return アクションメッセージ
	 * @throws ServiceException
	 */
	public ActionMessages validateAtCreateSlip() throws ServiceException {
		ActionMessages errors = new ActionMessages();
		ActionMessage tempMsg;

		// hiddenのカンマ除去
		deleteComma();

		String labelSupplierCode = MessageResourcesUtil
				.getMessage("labels.supplierCode");// 仕入先
		String labelProductCode = MessageResourcesUtil
				.getMessage("labels.productCode");
		String labelQuantity = MessageResourcesUtil
				.getMessage("labels.quantity");
		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");
		String labelProductRemarks = MessageResourcesUtil
				.getMessage("labels.productRemarks");
		String labelRackCode = MessageResourcesUtil
				.getMessage("labels.rackCode");
		String labelUnitPrice = MessageResourcesUtil
				.getMessage("labels.unitPrice");
		String labelPrice = MessageResourcesUtil.getMessage("labels.price");
		String labelDolUnitPrice = MessageResourcesUtil
				.getMessage("labels.dolUnitPrice");
		String labelDolPrice = MessageResourcesUtil
				.getMessage("labels.dolPrice");
		String labelSupplierDate = MessageResourcesUtil
				.getMessage("labels.supplierDate");// 仕入日

		boolean inputLine = false;

		if (StringUtil.hasLength(inputPurchaseForm.supplierCode)) {
			// 仕入先情報を取得
			Supplier supplier = supplierService
					.findById(inputPurchaseForm.supplierCode);
			if (supplier == null) {
				// 仕入先コードが存在しない
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.dataNotExist", labelSupplierCode,
						inputPurchaseForm.supplierCode));
			}
		}

		// 仕入日の未来日チェック
		Boolean resultEadDateFutureCheck = ValidateUtil.dateIsFuture(inputPurchaseForm.supplierDate);
		if( resultEadDateFutureCheck != null && resultEadDateFutureCheck == true ) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.dateFuture", labelSupplierDate,
					inputPurchaseForm.supplierDate));
		}

		int plus = 0;
		int minus = 0;
		for (PurchaseLineDto supplierLineTrnDto : inputPurchaseForm.lineDtoList) {
			// 商品コードに入力がない場合は無視
			if (!StringUtil.hasLength(supplierLineTrnDto.productCode)) {
				continue;
			}

			// 明細行が1件以上、存在する
			inputLine = true;

			// 必須チェック

			// 商品コード
			if (!StringUtil.hasLength(supplierLineTrnDto.productCode)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required",
						supplierLineTrnDto.lineNo, labelProductCode));
			}
			// 数量
			if (!StringUtil.hasLength(supplierLineTrnDto.quantity)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required",
						supplierLineTrnDto.lineNo, labelQuantity));
			}else{
				ActionMessage am = jp.co.arkinfosys.common.ValidateUtil.integerType(supplierLineTrnDto.quantity, "errors.line.required");
				if( am == null ){
					Integer data = Integer.valueOf(supplierLineTrnDto.quantity);
					if( data < 0 ){
						minus++;
					}else if( data > 0 ){
						plus++;
					}
				}
			}
			//レートで国内外判断
			if(inputPurchaseForm.rateId == null || inputPurchaseForm.rateId.length() == 0){
			// 円単価
			if (!StringUtil.hasLength(supplierLineTrnDto.unitPrice)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required",
						supplierLineTrnDto.lineNo, labelUnitPrice));
			}
			// 金額(円)
			if (!StringUtil.hasLength(supplierLineTrnDto.price)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required",
						supplierLineTrnDto.lineNo, labelPrice));
			}
			}else{
			// 外貨単価
			if (!StringUtil.hasLength(supplierLineTrnDto.dolUnitPrice)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required",
						supplierLineTrnDto.lineNo, labelDolUnitPrice));
			}
			// 外貨金額
			if (!StringUtil.hasLength(supplierLineTrnDto.dolPrice)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required",
						supplierLineTrnDto.lineNo, labelDolPrice));
			}
			}
			// 長さチェック

			// 数量
			if (StringUtil.hasLength(supplierLineTrnDto.quantity)) {
				if (supplierLineTrnDto.quantity.length() > 6) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.maxlength",
									supplierLineTrnDto.lineNo,
									labelQuantity, "6"));
				}
			}
			// 備考
			if (StringUtil.hasLength(supplierLineTrnDto.remarks)) {
				if (supplierLineTrnDto.remarks.length() > 120) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.maxlength",
									supplierLineTrnDto.lineNo,
									labelRemarks, "120"));
				}
			}

			// 商品備考
			if (StringUtil.hasLength(supplierLineTrnDto.productRemarks)) {
				if (supplierLineTrnDto.productRemarks.length() > 120) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.maxlength",
									supplierLineTrnDto.lineNo,
									labelProductRemarks, "120"));
				}
			}

			// 型チェック

			// 数量
			if (!supplierLineTrnDto.quantity
					.matches(Constants.NUMBER_MASK.DECIMAL12_3)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.integer",
						supplierLineTrnDto.lineNo, labelQuantity));
			}
			else if(StringUtil.hasLength(supplierLineTrnDto.quantity)){
				// 数値０チェック add kaki 2010.04.21
				int iQuantity = ((Double)Double.parseDouble(supplierLineTrnDto.quantity)).intValue();
				if( iQuantity == 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.line.num0",
							supplierLineTrnDto.lineNo, labelQuantity));
				}

			}

			// 円単価
			tempMsg = ValidateUtil.decimalType(
					Integer.valueOf(supplierLineTrnDto.lineNo).intValue()
					, supplierLineTrnDto.unitPrice
					, labelUnitPrice, 9, 0);
			if(tempMsg != null){
				errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
			}
			else if(StringUtil.hasLength(supplierLineTrnDto.unitPrice)){
				// 数値０チェック add kaki 2010.04.21
				float funitCost = Float.parseFloat(supplierLineTrnDto.unitPrice);
				if( Double.compare( funitCost, 0 ) == 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.line.num0",
							supplierLineTrnDto.lineNo, labelUnitPrice));
				}
			}

			// 金額(円)
			tempMsg = ValidateUtil.decimalType(
					Integer.valueOf(supplierLineTrnDto.lineNo).intValue()
					, supplierLineTrnDto.price
					, labelPrice, 9, 0);
			if(tempMsg != null){
				errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
			}
			else if(StringUtil.hasLength(supplierLineTrnDto.price)){
				// 数値０チェック add kaki 2010.04.21
				float fprice = Float.parseFloat(supplierLineTrnDto.price);
				if( Double.compare( fprice, 0 ) == 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.line.num0",
							supplierLineTrnDto.lineNo, labelPrice));
				}
			}

			// 外貨単価
			tempMsg = ValidateUtil.decimalType(
					Integer.valueOf(supplierLineTrnDto.lineNo).intValue()
					, supplierLineTrnDto.dolUnitPrice
					, labelDolUnitPrice, 9, 3);
			if(tempMsg != null){
				errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
			}
			else if(StringUtil.hasLength(supplierLineTrnDto.dolUnitPrice)){
				// 数値０チェック add kaki 2010.04.21
				float fdolUnitPrice = Float.parseFloat(supplierLineTrnDto.dolUnitPrice);
				if( Double.compare( fdolUnitPrice, 0 ) == 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.line.num0",
							supplierLineTrnDto.lineNo, labelDolUnitPrice));
				}
			}

			// 外貨金額
			tempMsg = ValidateUtil.decimalType(
					Integer.valueOf(supplierLineTrnDto.lineNo).intValue()
					, supplierLineTrnDto.dolPrice
					, labelDolPrice, 9, 3);
			if(tempMsg != null){
				errors.add(ActionMessages.GLOBAL_MESSAGE,tempMsg);
			}
			else if(StringUtil.hasLength(supplierLineTrnDto.dolPrice)){
				float fdolPrice = Float.parseFloat(supplierLineTrnDto.dolPrice);
				if( Double.compare( fdolPrice, 0 ) == 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.line.num0",
							supplierLineTrnDto.lineNo, labelDolPrice));
				}
			}

			// 値チェック（マスタ存在確認）

			// 商品コード
			if (StringUtil.hasLength(supplierLineTrnDto.productCode)) {
				Product product = productService
						.findById(supplierLineTrnDto.productCode);
				if (product == null) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.invalid",
									supplierLineTrnDto.lineNo,
									labelProductCode));
				}
			}

			// 単品商品の場合は棚番必須
			ProductJoin pj = productService.findById(supplierLineTrnDto.productCode);
			if( pj == null ){
				String strLabel = MessageResourcesUtil.getMessage("labels.productCode");
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.dataNotExist",
						supplierLineTrnDto.lineNo, strLabel, supplierLineTrnDto.productCode));
			}else{
				if( CategoryTrns.PRODUCT_SET_TYPE_SINGLE.equals(pj.setTypeCategory)){
					// 棚番
					if (!StringUtil.hasLength(supplierLineTrnDto.rackCode)) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.line.required",
								supplierLineTrnDto.lineNo, labelRackCode));
					}else{
						Rack rack = rackService
								.findById(supplierLineTrnDto.rackCode);
						if (rack == null) {
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage("errors.line.invalid",
											supplierLineTrnDto.lineNo,
											labelRackCode));
						}
					}
				}
				// セット商品の場合はエラー
				if( pj.setTypeCategory.equals(CategoryTrns.PRODUCT_SET_TYPE_SET)){
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.line.product.set",supplierLineTrnDto.lineNo));
				}
			}
		}
		// 明細行が1行以上、存在するかどうか
		if (!inputLine) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.noline"));
		}

		return errors;
	}

	/**
	 * 明細行の最大行数を返します.
	 *
	 * @return 明細行の最大行数
	 */
	public int getMaxLineRowCount() {
		return MAX_LINE_ROW_COUNT;
	}

	/**
	 * 明細行のタブ移動可能項目数を返します.
	 *
	 * @return 明細行のタブ移動可能項目数
	 */
	public int getLineElementCount() {
		return LINE_ELEMENT_COUNT;
	}

	/**
	 * Hidden属性のカンマを除去します.
	 */
	protected void deleteComma() {
		for (PurchaseLineDto supplierLineTrnDto : inputPurchaseForm.lineDtoList) {
			if( StringUtil.hasLength(supplierLineTrnDto.oldQuantity) ){
				supplierLineTrnDto.oldQuantity = supplierLineTrnDto.oldQuantity.replaceAll(",", "");
			}
			if( StringUtil.hasLength(supplierLineTrnDto.restQuantity) ){
				supplierLineTrnDto.restQuantity = supplierLineTrnDto.restQuantity.replaceAll(",", "");
			}
		}
	}


	/**
	 * 新規仕入伝票DTOを作成します.
	 * @return {@link PurchaseSlipDto}
	 */
	@Override
	protected AbstractSlipDto<PurchaseLineDto> createDTO() {
		return new PurchaseSlipDto();
	}

	/**
	 * 画面リストを初期化します.
	 * @throws Exception
	 */
	@Override
	protected void createList() throws Exception {
		// 仕入明細区分のプルダウン(現在未使用)
		supplierDetailCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.PURCHASE_DETAIL);

		// 完納区分のプルダウン
		deliveryProcessCategoryList = categoryService
				.findCategoryLabelValueBeanListById(Categories.DELIVERY_PROCESS_CATEGORY);
		// 委託在庫発注伝票を複写した場合は、「完納区分=完納」以外の要素は削除する(完納しか選べないようにする)
		if (inputPurchaseForm.isEntrustPorder) {
			List<LabelValueBean> removeList = new ArrayList<LabelValueBean>();
			//完納以外の要素を取得する
			for(LabelValueBean labelValueBean : deliveryProcessCategoryList) {
				if( ! CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL.equals(labelValueBean.getValue()) ) {
					removeList.add(labelValueBean);
				}
			}
			//完納以外の要素を削除する
			for(LabelValueBean removeLabelValueBean : removeList) {
				deliveryProcessCategoryList.remove(removeLabelValueBean);
			}
		}

		// 消費税率プルダウンリスト
		this.ctaxRateList =  ListUtil.getRateTaxList(super.taxRateService);
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link InputPurchaseForm}
	 */
	@Override
	protected AbstractSlipEditForm<PurchaseLineDto> getActionForm() {
		return this.inputPurchaseForm;
	}

	/**
	 * 登録時に使用するサービスを返します.
	 * @return サービスリスト
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		return new AbstractService[] {inputStockPurchaseService} ;
	}

	/**
	 * 入力画面のURIを返します.
	 * @return 仕入入力画面のURI
	 */
	@Override
	protected String getInputURIString() {
		return inputURIString;
	}

	/**
	 * 明細行サービスを返します.
	 * @return {@link SupplierLineService}
	 */
	@Override
	protected AbstractLineService<SupplierLineTrn,PurchaseLineDto,PurchaseSlipDto> getLineService() {
		return this.supplierLineService;
	}

	/**
	 * 伝票サービスを返します.
	 * @return {@link SupplierSlipService}
	 */
	@Override
	protected AbstractSlipService<SupplierSlipTrn,PurchaseSlipDto> getSlipService() {
		return this.supplierSlipService;
	}

	/**
	 * 仕入伝票のラベルのキーを返します.
	 * @return 仕入伝票のラベルのキー
	 */
	@Override
	public String getSlipKeyLabel() {
		return "labels.report.hist.purchaseSlip.supplierSlipId";
	}

}
