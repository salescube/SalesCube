/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.common.Constants.CODE_SIZE;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.stock.EntrustEadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EntrustEadSlipTrnDto;
import jp.co.arkinfosys.entity.EntrustEadLineTrn;
import jp.co.arkinfosys.entity.EntrustEadSlipTrn;
import jp.co.arkinfosys.entity.PoLineTrn;
import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.entity.Product;
import jp.co.arkinfosys.entity.join.PoLineTrnJoin;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.stock.InputEntrustStockForm;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.PoSlipService;
import jp.co.arkinfosys.service.SupplierSlipService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.InputEntrustStockLineService;
import jp.co.arkinfosys.service.stock.InputEntrustStockService;

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
 * 委託入出庫入力画面のアクションです.
 * @author Ark Information Systems
 *
 */
public class InputEntrustStockAction extends AbstractSlipEditAction<EntrustEadSlipTrnDto, EntrustEadLineTrnDto>  {

	/**
	 * 画面遷移用のマッピングクラス
	 */
	public static class Mapping {
		public static final String INPUT = "inputEntrustStock.jsp";
	}

	/** 明細行の初期サイズ */
	private static final int INIT_LINE_SIZE = 0;

	/** 明細行の最大行数 */
	private static final int MAX_LINE_ROW_COUNT = 35;

	/** 明細行のタブ移動可能項目数 */
	private static final int LINE_ELEMENT_COUNT = 8;

	@ActionForm
	@Resource
	private InputEntrustStockForm inputEntrustStockForm;

	@Resource
	private InputEntrustStockService inputEntrustStockService;

	@Resource
	private InputEntrustStockLineService inputEntrustStockLineService;

	@Resource
	private PoSlipService poSlipService;

	/**
     * 入出庫区分の選択値リスト
     */
    public List<LabelValueBean> categoryList;

    /** セット分類：単品（JSPで使用） */
    public String productSetSingle = CategoryTrns.PRODUCT_SET_TYPE_SINGLE;

	/**
	 * 初期表示処理を行います.
	 * <p>
	 * 1:既存データ読み込みフラグをOFF<br>
	 * 2:親クラスの初期表示処理を呼出<br>
	 * 3:明細行削除
	 * </p>
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		// 既存データの読み込みフラグをOFFにする
		inputEntrustStockForm.isExistSlipRead = false;

		super.index();

		//明細行を消去
		inputEntrustStockForm.entrustEadLineTrnDtoList = null;

		return this.getInputURIString();

	}

	/**
	 * 指定された発注伝票番号の委託入出庫情報を取得する処理を呼び出します(URLパターンあり).
	 *
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute( urlPattern = "copySlipFromPorder/{copySlipId}", validator = false )
	public String copySlipFromPorder() throws Exception {
		return copy();
	}
	/**
	 * 指定された発注伝票番号の委託入出庫情報を取得する処理を呼び出します(URLパターンなし).
	 *
	 * @return　遷移先URI
	 * @throws Exception
	 */
	@Execute( validator = false)
	public String copySlipFromPorderLoad() throws Exception {
		return copy();
	}

	/**
	 * 指定された発注伝票番号の委託入出庫情報を取得して画面に表示します.
	 *
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Override
	public String copy() throws Exception {
		try {
			inputEntrustStockForm.copySlipId = StringUtil.decodeSL(inputEntrustStockForm.copySlipId);

			// 伝票複写ダイアログで選択された委託入出庫区分で、委託入出庫区分を初期化する
			inputEntrustStockForm.entrustEadCategory = inputEntrustStockForm.copySlipFixedEntrustEadCategory;

			// copy元 発注伝票の取得
			PoSlipTrn poSlipTrnSingle = poSlipService
					.loadPOSlip(inputEntrustStockForm.copySlipId);
			List<PoLineTrnJoin> poLineTrnList = poSlipService
					.loadPOLine(inputEntrustStockForm.copySlipId);

			// 委託在庫発注の伝票ではない場合、複写しない
			if(poSlipTrnSingle != null && ! CategoryTrns.TRANSPORT_CATEGORY_ENTRUST.equals(poSlipTrnSingle.transportCategory) )  {
				poSlipTrnSingle = null;
			}

			// 委託入出庫番号はクリアする
			inputEntrustStockForm.entrustEadSlipId = "";

			// 発注番号が存在しない
			if(poSlipTrnSingle == null){
				// 発注番号をクリアする
				inputEntrustStockForm.poSlipId = "";
				// 委託入出庫区分をクリアする
				inputEntrustStockForm.entrustEadCategory = "";

				// ユーザの権限を設定
				inputEntrustStockForm.menuUpdate = userDto
						.isMenuUpdate(Constants.MENU_ID.INPUT_ENTRUST_STOCK);

				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.copy.notexist"));
				ActionMessagesUtil.addErrors(super.httpSession, super.messages);

				// プルダウンの初期化
				createList();

				return Mapping.INPUT;
			}

//			this.inputEntrustStockForm.reset();

			// 複写対象の明細が存在しない場合エラーとする
			boolean allZero = true;
			for (PoLineTrnJoin poLine : poLineTrnList) {
				// 発注伝票明細が伝票複写対象でない場合伝票複写しない
				if( ! isCopyTargetLine(inputEntrustStockForm.entrustEadCategory, poLine.status ) ){
					continue;
				}
				allZero = false;
				break;
			}
			if( allZero ){
				// 発注番号をクリアする
				inputEntrustStockForm.poSlipId = "";
				// 委託入出庫区分をクリアする
				inputEntrustStockForm.entrustEadCategory = "";

				// ユーザの権限を設定
				inputEntrustStockForm.menuUpdate = userDto
						.isMenuUpdate(Constants.MENU_ID.INPUT_ENTRUST_STOCK);

				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.copy.notexist"));

				ActionMessagesUtil.addErrors(super.httpSession, super.messages);

				// プルダウンの初期化
				createList();

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

			// 伝票
			inputEntrustStockForm.poSlipId = String
					.valueOf(poSlipTrnSingle.poSlipId);
			inputEntrustStockForm.supplierDate = StringUtil
					.getCurrentDateString(Constants.FORMAT.DATE);
			inputEntrustStockForm.deliveryDate = StringUtil.getDateString(
					Constants.FORMAT.DATE, poSlipTrnSingle.deliveryDate);
//			inputEntrustStockForm.remarks = poSlipTrnSingle.remarks;

			// 仕入先情報
			inputEntrustStockForm.supplierCode = poSlipTrnSingle.supplierCode;
			inputEntrustStockForm.supplierName = poSlipTrnSingle.supplierName;
			inputEntrustStockForm.priceFractCategory = poSlipTrnSingle.priceFractCategory;
			inputEntrustStockForm.taxShiftCategory = poSlipTrnSingle.taxShiftCategory;
			inputEntrustStockForm.taxFractCategory = poSlipTrnSingle.taxFractCategory;
			inputEntrustStockForm.supplierCmCategory = poSlipTrnSingle.supplierCmCategory;

			// 明細
			inputEntrustStockForm.entrustEadLineTrnDtoList = new ArrayList<EntrustEadLineTrnDto>();
			int lineNo = 0;

			for (PoLineTrnJoin poLineTrn : poLineTrnList) {
				// 発注伝票明細が伝票複写対象でない場合伝票複写しない
				if( ! isCopyTargetLine(inputEntrustStockForm.entrustEadCategory, poLineTrn.status ) ){
					continue;
				}

				EntrustEadLineTrnDto dto = Beans.createAndCopy(
						EntrustEadLineTrnDto.class, poLineTrn).excludes(
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

				lineNo++;
				dto.lineNo = String.valueOf(lineNo);

				// 発注伝票の発注残数を数量にセットする
				dto.quantity = numConv.getAsString(poLineTrn.restQuantity);

				// 備考は複写しない
				dto.remarks = "";

				// 商品備考の取得
				ProductJoin pj = inputEntrustStockService.findProductByCode(poLineTrn.productCode);
				if( pj != null ) {
					dto.productRemarks = pj.remarks;
				}

				inputEntrustStockForm.entrustEadLineTrnDtoList.add(dto);
			}

			// リストを埋める
			AbstractSlipDto<EntrustEadLineTrnDto> dto = inputEntrustStockForm.copyToDto();
			dto.setLineDtoList(inputEntrustStockForm.getLineList());
			dto.fillList();
			inputEntrustStockForm.setLineList(dto.getLineDtoList());

			// ユーザの権限を設定
			inputEntrustStockForm.userId = this.userDto.userId;
			inputEntrustStockForm.userName = this.userDto.nameKnj;
			inputEntrustStockForm.menuUpdate = userDto
					.isMenuUpdate(Constants.MENU_ID.INPUT_ENTRUST_STOCK);

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		// 既存データの読み込みフラグをOFFにする
		inputEntrustStockForm.isExistSlipRead = false;

		// プルダウンの初期化
		createList();

		// 初期値を設定する
		inputEntrustStockForm.initCopy();

		return Mapping.INPUT;
	}

	/**
	 * 明細行のステータスから伝票複写対象とするかを判別します.
	 * @param entrustEadCategory 委託入出庫区分
	 * @param lineStatus 明細行のステータス
	 * @return 複写対象か否か
	 */
	private boolean isCopyTargetLine(String entrustEadCategory, String lineStatus) {

		if( CategoryTrns.ENTRUST_EAD_CATEGORY_ENTER.equals(entrustEadCategory) ) {
			// 委託入庫の場合
			if( Constants.STATUS_PORDER_LINE.ORDERED.equals(lineStatus) ) {
				return true;
			}
			return false;
		}

		if( CategoryTrns.ENTRUST_EAD_CATEGORY_DISPATCH.equals(entrustEadCategory) ) {
			// 委託出庫の場合
			if( Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_MAKED.equals(lineStatus) ) {
				return true;
			}
			return false;
		}

		return false;	// 委託入庫でも委託出庫でもない場合、falseにしてしまう。(通常ありえない)
	}

    /**
     * アクションフォームを初期化します.
     */
    protected void initForm() {
		// Formを初期化
		inputEntrustStockForm.reset();
		//inputEntrustStockForm.eadDate = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);
		inputEntrustStockForm.menuUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_STOCK);
		inputEntrustStockForm.entrustEadCategory = "";
		inputEntrustStockForm.userId = this.userDto.userId;
		inputEntrustStockForm.userName = this.userDto.nameKnj;
		inputEntrustStockForm.entrustEadLineTrnDtoList = new ArrayList<EntrustEadLineTrnDto>();
		for(int i=0;i<INIT_LINE_SIZE;i++) {
			EntrustEadLineTrnDto dto = new EntrustEadLineTrnDto();
			dto.lineNo = Integer.toString(i+1);
			inputEntrustStockForm.entrustEadLineTrnDtoList.add(dto);
		}
    }

    /**
     * 明細行の最大行数を返します.
     * @return 明細行の最大行数
     */
    public int getMaxLineRowCount() {
    	return MAX_LINE_ROW_COUNT;
    }

    /**
     * 明細行のタブ移動可能項目数を返します.
     * @return 明細行のタブ移動可能項目数
     */
    public int getLineElementCount() {
    	return LINE_ELEMENT_COUNT;
    }

    /**
     * エラーメッセージを追加します.
     * @param errors 被追加対象のエラーメッセージ
     * @param err 追加対象のエラーメッセージ
     */
    private void addError(ActionMessages errors, ActionMessage err) {
    	if(errors == null || err == null) {
    		return;
    	}
    	errors.add(ActionMessages.GLOBAL_MESSAGE, err);
    }

	/**
	 * アクションフォームを返します.
	 * @return {@link InputEntrustStockForm}
	 */
	protected AbstractSlipEditForm<EntrustEadLineTrnDto> getActionForm() {
		return this.inputEntrustStockForm;
	}

	/**
	 * 新規委託入出庫伝票DTOを作成します.
	 * @return {@link EntrustEadSlipTrnDto}
	 */
	@Override
	protected AbstractSlipDto<EntrustEadLineTrnDto> createDTO() {

		return new EntrustEadSlipTrnDto();
	}

	/**
	 * 入力画面のURIを返します.
	 * @return 委託入出庫入力画面のURI
	 */
	@Override
	protected String getInputURIString() {
		return InputEntrustStockAction.Mapping.INPUT;
	}

	/**
	 * 伝票サービスを返します.
	 * @return {@link InputEntrustStockService}
	 */
	@Override
	protected AbstractSlipService<EntrustEadSlipTrn, EntrustEadSlipTrnDto> getSlipService() {
		return this.inputEntrustStockService;
	}

	/**
	 * 登録時に使用するサービスを返します.
	 * @return サービスリスト
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		return new AbstractService[] { this.poSlipService };
	}

	/**
	 * 登録/更新の後処理を行います.<br>
	 * 未実装です.
	 * @param bInsert 新規登録か否か
	 * @param param {@link EntrustEadSlipTrnDto}
	 * @throws Exception
	 */
	@Override
	protected void afterUpsert(boolean bInsert, AbstractSlipDto<EntrustEadLineTrnDto> param) throws Exception {

	}

	/**
	 * 明細行サービスを返します.
	 * @return {@link InputEntrustStockLineService}
	 */
	@Override
	protected AbstractLineService<EntrustEadLineTrn,EntrustEadLineTrnDto,EntrustEadSlipTrnDto> getLineService() {
		return this.inputEntrustStockLineService;
	}

	/**
	 * 委託入出庫伝票のラベルのキーを返します.
	 * @return 委託入出庫伝票のラベルのキー
	 */
	@Override
	public String getSlipKeyLabel() {
		return "labels.entrustEadSlipId";
	}

	/**
	 * 画面リストを初期化します.
	 * @throws Exception
	 */
	@Override
	protected void createList() throws Exception {
	   	categoryList = inputEntrustStockService.getCategoryList();

    	// 明細行が複数行ある時は、委託入出庫区分の選択を変更できないようにする(選択中の区分以外の要素を削除する)
    	if(inputEntrustStockForm.entrustEadLineTrnDtoList != null && inputEntrustStockForm.entrustEadLineTrnDtoList.size() > 0 ) {

    		List<LabelValueBean> removeLabelList = new ArrayList<LabelValueBean>();
    		for(LabelValueBean labelValueBean : categoryList) {
    			if( ! inputEntrustStockForm.entrustEadCategory.equals(labelValueBean.getValue())) {
    				removeLabelList.add(labelValueBean);
    			}
    		}
    		for(LabelValueBean removelabelValueBean : removeLabelList) {
    			categoryList.remove(removelabelValueBean);
    		}
    	}else{
    		// 先頭に空白行を挿入
			LabelValueBean bean = new LabelValueBean();
			bean.setValue("");
			bean.setLabel("");
			categoryList.add(0,bean);
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
			// 権限の取得
			inputEntrustStockForm.menuUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_ENTRUST_STOCK);

			// DTOの取得
			EntrustEadSlipTrnDto entrustEadSlipTrnDto = (EntrustEadSlipTrnDto)inputEntrustStockService.loadBySlipId(inputEntrustStockForm.entrustEadSlipId);
			// 存在する委託入出庫伝票のみ参照
			if( entrustEadSlipTrnDto == null ){

				return false;
			}

			// DTOの値をFormへコピー
			Beans.copy(entrustEadSlipTrnDto, inputEntrustStockForm).execute();

			// 明細情報をロード
			List<EntrustEadLineTrnDto> lineList =entrustEadSlipTrnDto.getLineDtoList();
			inputEntrustStockForm.setLineList(lineList);

			// 委託出庫の場合は、印刷済みかどうかのメッセージを表示する
			if( CategoryTrns.ENTRUST_EAD_CATEGORY_DISPATCH.equals(inputEntrustStockForm.entrustEadCategory) ) {
				if(entrustEadSlipTrnDto.dispatchOrderPrintCount == null || "0".equals(entrustEadSlipTrnDto.dispatchOrderPrintCount) ) {
					//印刷未実施の伝票
					super.messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("infos.entrustDispatchPrint.yet"));
				} else {
					//印刷済み伝票
					super.messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("infos.entrustDispatchPrint.already"));
				}
				ActionMessagesUtil.addMessages(super.httpSession, super.messages);
			}

		} catch (ServiceException e) {
			super.errorLog(e);

			// 続行可能？
			if(e.isStopOnError()) {
				// システム例外として処理する
				throw e;
			}
		}

		// 既存データの読み込みフラグをONにする
		inputEntrustStockForm.isExistSlipRead = true;

		// 委託出庫の場合、委託出庫フラグをONにする
		if( CategoryTrns.ENTRUST_EAD_CATEGORY_DISPATCH.equals(inputEntrustStockForm.entrustEadCategory) ) {
			inputEntrustStockForm.isEntrustDispatch = true;
		}

		createList();

		return true;
	}

	/**
	 * 登録時の委託入出庫伝票と委託入出庫伝票明細行のチェックを行います.
	 * @return アクションメッセージ
	 * @throws ServiceException
	 */
	@Override
	public ActionMessages validateAtCreateSlip() throws ServiceException {
		ActionMessages errors = new ActionMessages();
		ActionMessage err;

		String labelProductCode = MessageResourcesUtil.getMessage("labels.productCode");
		String labelQuantity = MessageResourcesUtil.getMessage("labels.quantity");
		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");

		// 委託入出庫日の未来日チェック
		Boolean resultEadDateFutureCheck = ValidateUtil.dateIsFuture(inputEntrustStockForm.entrustEadDate);
		if( resultEadDateFutureCheck != null && resultEadDateFutureCheck == true ) {
			err = new ActionMessage("errors.dateFuture", MessageResourcesUtil.getMessage("labels.entrustEadDate"));
			addError(errors, err);
		}

		boolean inputLine = false;

		if(inputEntrustStockForm.entrustEadLineTrnDtoList != null) {
			for(EntrustEadLineTrnDto entrustEadLineTrnDto : inputEntrustStockForm.entrustEadLineTrnDtoList) {
				// チェックされていない行は処理しない(但し更新時はチェックしない)
				if ( ! inputEntrustStockForm.isExistSlipRead && entrustEadLineTrnDto.checkEadLine == null ) {
					continue;
				}

				// 商品コード
				if(!StringUtil.hasLength(entrustEadLineTrnDto.productCode)) {
					continue;
				}

				// 明細行が1件以上、存在する
				inputLine = true;

				// 必須チェック

				// 数量
				err = ValidateUtil.required(entrustEadLineTrnDto.quantity, "errors.line.required",
						new Object[] { entrustEadLineTrnDto.lineNo, labelQuantity });
				addError(errors, err);

				// 長さチェック

				// 商品コード
				if(StringUtil.hasLength(entrustEadLineTrnDto.productCode)) {
					err = ValidateUtil.maxlength(entrustEadLineTrnDto.productCode, CODE_SIZE.PRODUCT, "errors.line.maxlength",
							new Object[] { entrustEadLineTrnDto.lineNo, labelProductCode, Integer.toString( CODE_SIZE.PRODUCT ) });
					addError(errors, err);
				}
				// 備考
				if(StringUtil.hasLength(entrustEadLineTrnDto.remarks)) {
					err = ValidateUtil.maxlength(entrustEadLineTrnDto.remarks, 120, "errors.line.maxlength",
							new Object[] { entrustEadLineTrnDto.lineNo, labelRemarks, "120" });
					addError(errors, err);
				}

				// 型チェック

				// 数量
				boolean quantityType = false;
				if(StringUtil.hasLength(entrustEadLineTrnDto.quantity)) {
					err = ValidateUtil.intRange(entrustEadLineTrnDto.quantity, 1, null, labelQuantity);
					if(err == null) {
						quantityType = true;
					}
					if(!quantityType) {
						err = new ActionMessage("errors.line.integer.plus", entrustEadLineTrnDto.lineNo, labelQuantity);
						addError(errors, err);
					}
				}

				// 値チェック（マスタ存在確認）

				// 商品コード
				if(StringUtil.hasLength(entrustEadLineTrnDto.productCode)) {
					Product product = inputEntrustStockService.findProductByCode(entrustEadLineTrnDto.productCode);
					if(product == null) {
						err = new ActionMessage("errors.line.dataNotExist",
								entrustEadLineTrnDto.lineNo, labelProductCode, entrustEadLineTrnDto.productCode);
						addError(errors, err);
					}
				}

			}
		}

		// 明細行が1行以上、存在するかどうか
		if(!inputLine) {
			err = new ActionMessage("errors.nocheck");
			addError(errors, err);
		}

		super.messages.add(errors);

		return super.messages;
	}

	/**
	 * 伝票削除時のチェックを行います.
	 * <p>
	 * 1:委託出庫伝票削除時の仕入済チェック<br>
	 * 2:委託入庫伝票削除時の出庫済チェック
	 * </p>
	 *
	 * @return アクションメッセージを返します.
	 * @throws ServiceException
	 */
	@Override
	public ActionMessages validateAtDeleteSlip() throws ServiceException {

		// DTOの取得
		//EntrustEadSlipTrnDto entrustEadSlipTrnDto = Beans.createAndCopy(EntrustEadSlipTrnDto.class, inputEntrustStockForm).execute();
		// DTOの取得
		EntrustEadSlipTrnDto entrustEadSlipTrnDto = inputEntrustStockService.createEntrustEadSlipTrnDto(inputEntrustStockForm.entrustEadSlipId);

		int errRowNo=1;	// エラー箇所の行番号格納用
		boolean hasError = false;	//伝票状態のエラーチェックで、削除エラーの明細が見つかったかどうか

		// 委託出庫伝票削除の場合、明細が1件以上仕入されていた場合削除不可
		if( CategoryTrns.ENTRUST_EAD_CATEGORY_DISPATCH.equals(entrustEadSlipTrnDto.entrustEadCategory) ) {
			for(EntrustEadLineTrnDto entrustEadLineTrnDto : entrustEadSlipTrnDto.getLineDtoList()) {
				PoLineTrn poLineTrn = poSlipService.getPOLineTrnByPoLineId(entrustEadLineTrnDto.poLineId);
				if(Constants.STATUS_PORDER_LINE.NOWPURCHASING.equals(poLineTrn.status) || Constants.STATUS_PORDER_LINE.PURCHASED.equals(poLineTrn.status)) {
					// 削除不可メッセージ表示
					super.messages.add(ActionMessages.GLOBAL_MESSAGE,
										new ActionMessage("errors.delete.entrustPurchased", errRowNo));
					hasError = true;
				}
				errRowNo++;
			}
		}

		errRowNo=1;	// エラー箇所の行番号格納用
		// 委託入庫伝票削除の場合、明細が1件以上委託出庫されていた場合削除不可
		if( CategoryTrns.ENTRUST_EAD_CATEGORY_ENTER.equals(entrustEadSlipTrnDto.entrustEadCategory) ) {
			for(EntrustEadLineTrnDto entrustEadLineTrnDto : entrustEadSlipTrnDto.getLineDtoList()) {
				if( StringUtil.hasLength(entrustEadLineTrnDto.relEntrustEadLineId) ) {
					// 削除不可メッセージ表示
					super.messages.add(ActionMessages.GLOBAL_MESSAGE,
										new ActionMessage("errors.delete.entrustDelivered", errRowNo));
					hasError = true;
				}
			}
			errRowNo++;
		}

		// 伝票状態チェック処理でエラーが1件以上あった場合、エラー表示をする
		if(hasError) {
			ActionMessagesUtil.addErrors(super.httpSession, super.messages);
		}

		return super.messages;
	}
}
