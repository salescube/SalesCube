/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.payment;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.NumberUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.payment.InputPaymentDto;
import jp.co.arkinfosys.dto.payment.InputPaymentLineDto;
import jp.co.arkinfosys.entity.PaymentLineTrn;
import jp.co.arkinfosys.entity.PaymentSlipTrn;
import jp.co.arkinfosys.entity.Supplier;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.payment.InputPaymentForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.AptBalanceService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.RateService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.payment.InputPaymentLineService;
import jp.co.arkinfosys.service.payment.InputPaymentService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 支払入力画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class InputPaymentAction extends AbstractSlipEditAction<InputPaymentDto, InputPaymentLineDto> {
	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "inputPayment.jsp";
	}

	@ActionForm
	@Resource
	public InputPaymentForm inputPaymentForm;

	// Service
	@Resource
	private InputPaymentService inputPaymentService;

	@Resource
	private InputPaymentLineService inputPaymentLineService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private RateService rateService;

	@Resource
	private SupplierService supplierService;

	@Resource
	private AptBalanceService aptBalanceService;

	// 画面で使用する変数
	public List<LabelValueBean> paymentDetailList = new ArrayList<LabelValueBean>();
	public Map<String, CategoryJoin> supCategoryMap = new HashMap<String, CategoryJoin>();// 仕入明細区分
	public Map<String, String> rateMap = new HashMap<String, String>();// レート

	/**
	 * 伝票を複写します.<br>
	 * <p>
	 * 発注番号を元に発注伝票、仕入伝票、買掛残高情報を取得し、<br>
	 * 支払入力画面に設定します.
	 * </p>
	 *
	 * @return 支払入力画面のパス
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#copy()
	 */
	@Override
	public String copy() throws Exception {
		prepareForm();

		try {
			// 複写対象データ数を取得する
			int validSlipCount = inputPaymentService.searchUnpaidSupplierSlipCount(this.inputPaymentForm.poSlipId);

			if(validSlipCount <= 0) {
				// 発注番号をクリアする
				inputPaymentForm.poSlipId = "";
				super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.copy.notexist"));
				ActionMessagesUtil.addErrors(super.httpSession, super.messages);
				return InputPaymentAction.Mapping.INPUT;
			}

			// コピー元情報を取得する
			InputPaymentDto dto = inputPaymentService.findByPoSlipId(this.inputPaymentForm.poSlipId);

			if(dto == null) {
				// 発注番号をクリアする
				inputPaymentForm.poSlipId = "";
				super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.copy.notexist"));
				ActionMessagesUtil.addErrors(super.httpSession, super.messages);
				return InputPaymentAction.Mapping.INPUT;
			}

			// コピー元情報にフォーム内容を設定する
			dto.userName = this.inputPaymentForm.userName;
			dto.paymentDate = this.inputPaymentForm.paymentDate;

			// コピーを実行する
			Beans.copy(dto, this.inputPaymentForm).execute();

			// 買掛残高
			BigDecimal aptBalance = this.aptBalanceService.calcAptBalanceBySupplierCode(inputPaymentForm.supplierCode);
			if(aptBalance != null) {
				DecimalFormat df = NumberUtil.createDecimalFormat(dto.priceFractCategory, super.mineDto.unitPriceDecAlignment, true);
				inputPaymentForm.aptBalance = df.format(aptBalance);
			}

			// 初期値を設定する
			this.inputPaymentForm.initCopy();
			this.inputPaymentForm.newData = true;
			//支払日を初期値に設定する
			this.inputPaymentForm.paymentDate = "";

			createList();

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		return this.getInputURIString();
	}

	/**
	 * 伝票読み込み後に不要な明細行を削除します.
	 *
	 * @throws Exception
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#afterLoad()
	 */
	@Override
	protected void afterLoad() throws Exception, ServiceException {
		// 明細行リストを取得する
		List<InputPaymentLineDto> lineList =  this.inputPaymentForm.lineDtoList;

		// 空行を削除して新規リストを作成する
		List<InputPaymentLineDto> newLineList = new ArrayList<InputPaymentLineDto>();

		for(InputPaymentLineDto lineDto : lineList) {
			// 空白行以外はリストに追加する
			if(!lineDto.isBlank()) {
				newLineList.add(lineDto);
			}
		}

		// 新リストをフォームに設定する
		this.inputPaymentForm.lineDtoList = newLineList;
	}

	/**
	 * 登録/更新の前処理を行います.
	 * @param bInsert 新規登録か否か
	 * @param dto 支払伝票DTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#beforeUpsert(boolean, jp.co.arkinfosys.dto.AbstractSlipDto)
	 */
	@Override
	protected void beforeUpsert(boolean bInsert, AbstractSlipDto<InputPaymentLineDto> dto) throws Exception {
		InputPaymentDto paymentDto = (InputPaymentDto)dto;

		if(bInsert) {
			// 登録の場合
			paymentDto.status = Constants.STATUS_PAYMENT_SLIP.PAID;
			paymentDto.userId = super.userDto.userId;
			paymentDto.setLineDtoList(null);

		} else {
			// 更新の場合
			paymentDto.status = Constants.STATUS_PAYMENT_SLIP.PAID;
		}
	}

	/**
	 * 登録/更新処理を行います.
	 *
	 * @return 遷移先URI
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#upsert()
	 */
	@Override
	public String upsert() throws Exception {
		String uriString = super.upsert();
		this.afterLoad();
		return uriString;
	}

	/**
	 * 登録/更新の後処理を行います.
	 * @param bInsert 新規登録か否か
	 * @param dto 支払伝票DTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#afterUpsert(boolean, jp.co.arkinfosys.dto.AbstractSlipDto)
	 */
	@Override
	protected void afterUpsert(boolean bInsert, AbstractSlipDto<InputPaymentLineDto> dto) throws Exception {
		try {
			// 仕入伝票を更新する
			this.inputPaymentService.updateSupplierForUpdate((InputPaymentDto)dto);

		} catch(Exception e) {
			throw e;
		}
	}

	/**
	 * 削除する伝票に関連するデータをロックします.
	 * @param dto 削除する伝票のDTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#beforeDelete(jp.co.arkinfosys.dto.AbstractSlipDto)
	 */
	@Override
	protected void beforeDelete(AbstractSlipDto<InputPaymentLineDto> dto) throws Exception {
		// 支払伝票レコードをロックする
		this.inputPaymentService.lockPaymentRecord((InputPaymentDto)dto);

		// 仕入伝票レコードをロックする
		this.inputPaymentService.lockSupplierRecord((InputPaymentDto)dto);
	}

	/**
	 * 削除された支払伝票に紐づく仕入伝票を更新します.
	 * @param dto 削除された伝票のDTO
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#afterDelete(jp.co.arkinfosys.dto.AbstractSlipDto)
	 */
	@Override
	protected void afterDelete(AbstractSlipDto<InputPaymentLineDto> dto) throws Exception {
		// 仕入伝票を更新する
		this.inputPaymentService.updateSupplierForDelete((InputPaymentDto)dto);
	}

	/**
	 * 新規支払伝票DTOを作成します.
	 * @return {@link InputPaymentDto}
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#createDTO()
	 */
	@Override
	protected AbstractSlipDto<InputPaymentLineDto> createDTO() {
		return new InputPaymentDto();
	}

	/**
	 * 画面リストを初期化します.
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#createList()
	 */
	@Override
	protected void createList() throws Exception {
		try {
			// ユーザの権限を設定
			inputPaymentForm.menuUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_PAYMENT);

			// レート設定
			rateMap = rateService.findRateIdAndNameMap();
			inputPaymentForm.rateName = rateMap.get(inputPaymentForm.rateId);

			supCategoryMap = categoryService.findCategoryJoinMapById(Categories.PURCHASE_DETAIL);

			for(int i = 0; i < inputPaymentForm.lineDtoList.size(); i++) {
				InputPaymentLineDto slipLine = inputPaymentForm.lineDtoList.get(i);

				if(slipLine.supplierDetailCategory == null) {
					continue;
				}
				// 仕入明細区分名設定
				slipLine.supplierDetailCategoryName = (supCategoryMap.get(slipLine.supplierDetailCategory)).categoryCodeName;
			}

			// 支払明細区分ドロップダウン設定
			paymentDetailList = categoryService.findCategoryLabelValueBeanListById(Categories.PAYMENT_DETAIL);

		} catch(ServiceException e) {
			super.errorLog(e);
			super.httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link InputPaymentForm}
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#getActionForm()
	 */
	@Override
	protected AbstractSlipEditForm<InputPaymentLineDto> getActionForm() {
		return this.inputPaymentForm;
	}

	/**
	 * 登録時に使用するサービスを返します.<br>
	 * 未使用です.
	 * @return null
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#getAdditionalServiceOnSaveSlip()
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		return null;
	}

	/**
	 * 遷移先のURIを返します.
	 * @return 支払入力画面のURI
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#getInputURIString()
	 */
	@Override
	protected String getInputURIString() {
		return InputPaymentAction.Mapping.INPUT;
	}

	/**
	 * 明細行サービスを返します.
	 * @return {@link InputPaymentLineService}
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#getLineService()
	 */
	@Override
	protected AbstractLineService<PaymentLineTrn, InputPaymentLineDto, InputPaymentDto> getLineService() {
		return this.inputPaymentLineService;
	}

	/**
	 * 伝票サービスを返します.
	 * @return {@link InputPaymentService}
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#getSlipService()
	 */
	@Override
	protected AbstractSlipService<PaymentSlipTrn, InputPaymentDto> getSlipService() {
		return this.inputPaymentService;
	}

	/**
	 * 伝票データを取得します.
	 * @return 読み込みできたか否か
	 * @throws Exception
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#loadData()
	 */
	@Override
	protected boolean loadData() throws Exception, ServiceException {
		try {
			InputPaymentDto dto = (InputPaymentDto)this.inputPaymentService.loadBySlipId(inputPaymentForm.paymentSlipId);

			if(dto == null) {
				return false;
			}

			Beans.copy(dto, inputPaymentForm).execute();

			List<InputPaymentLineDto> lineList = this.inputPaymentLineService.loadBySlip(dto);
			this.inputPaymentForm.lineDtoList = lineList;

			// 買掛残高
			BigDecimal aptBalance = this.aptBalanceService.calcAptBalanceBySupplierCode(inputPaymentForm.supplierCode);
			if(aptBalance != null) {
				DecimalFormat df = NumberUtil.createDecimalFormat(dto.priceFractCategory, super.mineDto.unitPriceDecAlignment, true);
				inputPaymentForm.aptBalance = df.format(aptBalance);
			}


			inputPaymentForm.initLoad();
			createList();

			return true;

		} catch(ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}

	/**
	 * 支払伝票のラベルのキーを返します.
	 * @return 支払伝票のラベルのキー
	 * @see jp.co.arkinfosys.action.AbstractSlipEditAction#getSlipKeyLabel()
	 */
	@Override
	public String getSlipKeyLabel() {
		return "labels.report.hist.paymentSlip.paymentSlipId";
	}

	/**
	 * 登録時の支払伝票と支払伝票明細行のチェックを行います.
	 * @return アクションメッセージ
	 * @throws ServiceException
	 */
	public ActionMessages validateAtCreateSlip() throws ServiceException{
		ActionMessages errors = new ActionMessages();
		ActionMessage tempMsg;

		// メッセージに表示する文字列
		String labelSupplierCode = MessageResourcesUtil.getMessage("labels.supplierCode");// 仕入先
		String labelUnitPrice = MessageResourcesUtil.getMessage("labels.unitPrice");// 円単価
		String labelDolUnitPrice = MessageResourcesUtil.getMessage("labels.dolUnitPrice");// 外貨単価
		String labelPrice = MessageResourcesUtil.getMessage("labels.price");
		String labelDolPrice = MessageResourcesUtil.getMessage("labels.dolPrice");

		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");// 備考(支払伝票明細行)

		// チェック項目
		String supplierCode = inputPaymentForm.supplierCode;// 仕入先コード

		/**
		 * 【支払伝票】 ・必須チェック ・日付書式チェック ・長さチェック
		 * 【支払伝票明細行】 全てvalidate()内でチェック
		 */

		/********** 支払入力チェック **********/
		// ***** 仕入先コード *****
		// *** 必須チェック ***
		if("".equals(supplierCode)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", labelSupplierCode));

			// 仕入先コードが無い場合その後のチェックが行えないため、validateを終了する
			return errors;
		}

		// 仕入先情報を取得
		Supplier supplier = supplierService.findById(supplierCode);

		// *** 存在チェック ***/
		if(!"".equals(supplierCode)) {
			// 仕入先コードに入力がある場合のみチェック
			if(supplier == null || "".equals(supplier.supplierCode)) {
				// 仕入先コードが存在しない
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.dataNotExist", labelSupplierCode, supplierCode));
				return errors;
			}
		}

		/********** 支払入力明細行チェック **********/
		int checkedCount = 0;
		boolean NATIONAL_SUPPLIER = (supplier.rateId == null); // レートIDがnullなら国内仕入先
		for(int i = 0; i < inputPaymentForm.lineDtoList.size(); i++) {
			InputPaymentLineDto line = inputPaymentForm.lineDtoList.get(i);

			// 明細チェック数のカウント(チェック数が0件の時のエラー検出のため)
			if(line.checkPayLine) {
				checkedCount++;
			}

			// *** 必須チェック ***/
			if(NATIONAL_SUPPLIER) {
				// 仕入先が国内の場合、円単価の必須チェック
				if("".equals(line.unitPrice)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.required", i + 1, labelUnitPrice));
				}
			} else {
				// 仕入先が国外の場合、外貨単価の必須チェック
				if("".equals(line.dolUnitPrice)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.required", i + 1, labelDolUnitPrice));
				}
			}

			// *** 半角数値チェック ＆ 長さチェック(整数部 + 少数部が8+2以下) ***/

			// 円単価
			tempMsg = ValidateUtil.decimalType(i + 1, line.unitPrice, labelUnitPrice, 9, 0);
			if(tempMsg != null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, tempMsg);
			} else if(StringUtil.hasLength(line.unitPrice)) {
				// 数値０チェック
				float funitPrice = Float.parseFloat(line.unitPrice);
				if(Double.compare(funitPrice, 0) == 0) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.num0", i + 1, labelUnitPrice));
				}
			}

			// 外貨単価
			tempMsg = ValidateUtil.decimalType(i + 1, line.dolUnitPrice, labelDolUnitPrice, 9, 3);
			if(tempMsg != null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, tempMsg);
			} else if(StringUtil.hasLength(line.dolUnitPrice)) {
				// 数値０チェック
				float fdolUnitPrice = Float.parseFloat(line.dolUnitPrice);
				if(Double.compare(fdolUnitPrice, 0) == 0) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.num0", i + 1, labelDolUnitPrice));
				}
			}

			// 円金額
			tempMsg = ValidateUtil.decimalType(i + 1, line.price, labelPrice, 9, 0);
			if(tempMsg != null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, tempMsg);
			}

			// 外貨金額
			tempMsg = ValidateUtil.decimalType(i + 1, line.dolPrice, labelDolPrice, 9, 3);
			if(tempMsg != null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, tempMsg);
			}

			// *** 長さチェック ***/
			// 備考
			if(StringUtil.hasLength(line.remarks)) {
				if(line.remarks.length() > 50) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.maxbytelength", i + 1, labelRemarks, "50"));
				}
			}
		}

		// 登録時で、明細が選択されていない場合エラーとする
		if(checkedCount <= 0 && inputPaymentForm.isNew()) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.nocheck"));
		}

		return errors;
	}
}