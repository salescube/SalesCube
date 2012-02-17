/*
 *  Copyright 2009-2010 Ark Information Systems.
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

	
	public List<LabelValueBean> paymentDetailList = new ArrayList<LabelValueBean>();
	public Map<String, CategoryJoin> supCategoryMap = new HashMap<String, CategoryJoin>();
	public Map<String, String> rateMap = new HashMap<String, String>();

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
			
			int validSlipCount = inputPaymentService.searchUnpaidSupplierSlipCount(this.inputPaymentForm.poSlipId);

			if(validSlipCount <= 0) {
				
				inputPaymentForm.poSlipId = "";
				super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.copy.notexist"));
				ActionMessagesUtil.addErrors(super.httpSession, super.messages);
				return InputPaymentAction.Mapping.INPUT;
			}

			
			InputPaymentDto dto = inputPaymentService.findByPoSlipId(this.inputPaymentForm.poSlipId);

			if(dto == null) {
				
				inputPaymentForm.poSlipId = "";
				super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.copy.notexist"));
				ActionMessagesUtil.addErrors(super.httpSession, super.messages);
				return InputPaymentAction.Mapping.INPUT;
			}

			
			dto.userName = this.inputPaymentForm.userName;
			dto.paymentDate = this.inputPaymentForm.paymentDate;

			
			Beans.copy(dto, this.inputPaymentForm).execute();

			
			BigDecimal aptBalance = this.aptBalanceService.calcAptBalanceBySupplierCode(inputPaymentForm.supplierCode);
			if(aptBalance != null) {
				DecimalFormat df = NumberUtil.createDecimalFormat(dto.priceFractCategory, super.mineDto.unitPriceDecAlignment, true);
				inputPaymentForm.aptBalance = df.format(aptBalance);
			}

			
			this.inputPaymentForm.initCopy();

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
		
		List<InputPaymentLineDto> lineList =  this.inputPaymentForm.lineDtoList;

		
		List<InputPaymentLineDto> newLineList = new ArrayList<InputPaymentLineDto>();

		for(InputPaymentLineDto lineDto : lineList) {
			
			if(!lineDto.isBlank()) {
				newLineList.add(lineDto);
			}
		}

		
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
			
			paymentDto.status = Constants.STATUS_PAYMENT_SLIP.PAID;
			paymentDto.userId = super.userDto.userId;
			paymentDto.setLineDtoList(null);

		} else {
			
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
		
		this.inputPaymentService.lockPaymentRecord((InputPaymentDto)dto);

		
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
			
			inputPaymentForm.menuUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_PAYMENT);

			
			rateMap = rateService.findRateIdAndNameMap();
			inputPaymentForm.rateName = rateMap.get(inputPaymentForm.rateId);

			supCategoryMap = categoryService.findCategoryJoinMapById(Categories.PURCHASE_DETAIL);

			for(int i = 0; i < inputPaymentForm.lineDtoList.size(); i++) {
				InputPaymentLineDto slipLine = inputPaymentForm.lineDtoList.get(i);

				if(slipLine.supplierDetailCategory == null) {
					continue;
				}
				
				slipLine.supplierDetailCategoryName = (supCategoryMap.get(slipLine.supplierDetailCategory)).categoryCodeName;
			}

			
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

			
			BigDecimal aptBalance = this.aptBalanceService.calcAptBalanceBySupplierCode(inputPaymentForm.supplierCode);
			if(aptBalance != null) {
				DecimalFormat df = NumberUtil.createDecimalFormat(dto.priceFractCategory, super.mineDto.unitPriceDecAlignment, true);
				inputPaymentForm.aptBalance = df.format(aptBalance);
			}

			
			this.inputPaymentForm.supplierTaxRate = dto.supplierTaxRate;

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

		
		String labelSupplierCode = MessageResourcesUtil.getMessage("labels.supplierCode");
		String labelUnitPrice = MessageResourcesUtil.getMessage("labels.unitPrice");
		String labelDolUnitPrice = MessageResourcesUtil.getMessage("labels.dolUnitPrice");
		String labelPrice = MessageResourcesUtil.getMessage("labels.price");
		String labelDolPrice = MessageResourcesUtil.getMessage("labels.dolPrice");

		String labelRemarks = MessageResourcesUtil.getMessage("labels.remarks");

		
		String supplierCode = inputPaymentForm.supplierCode;

		/**
		 * 【支払伝票】 ・必須チェック ・日付書式チェック ・長さチェック
		 * 【支払伝票明細行】 全てvalidate()内でチェック
		 */

		/********** 支払入力チェック **********/
		
		
		if("".equals(supplierCode)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", labelSupplierCode));

			
			return errors;
		}

		
		Supplier supplier = supplierService.findById(supplierCode);

		
		if(!"".equals(supplierCode)) {
			
			if(supplier == null || "".equals(supplier.supplierCode)) {
				
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.dataNotExist", labelSupplierCode, supplierCode));
				return errors;
			}
		}

		/********** 支払入力明細行チェック **********/
		int checkedCount = 0;
		boolean NATIONAL_SUPPLIER = (supplier.rateId == null); 
		for(int i = 0; i < inputPaymentForm.lineDtoList.size(); i++) {
			InputPaymentLineDto line = inputPaymentForm.lineDtoList.get(i);

			
			if(line.checkPayLine) {
				checkedCount++;
			}

			
			if(NATIONAL_SUPPLIER) {
				
				if("".equals(line.unitPrice)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.required", i + 1, labelUnitPrice));
				}
			} else {
				
				if("".equals(line.dolUnitPrice)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.required", i + 1, labelDolUnitPrice));
				}
			}

			

			
			tempMsg = ValidateUtil.decimalType(i + 1, line.unitPrice, labelUnitPrice, 9, 0);
			if(tempMsg != null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, tempMsg);
			} else if(StringUtil.hasLength(line.unitPrice)) {
				
				float funitPrice = Float.parseFloat(line.unitPrice);
				if(Double.compare(funitPrice, 0) == 0) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.num0", i + 1, labelUnitPrice));
				}
			}

			
			tempMsg = ValidateUtil.decimalType(i + 1, line.dolUnitPrice, labelDolUnitPrice, 9, 3);
			if(tempMsg != null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, tempMsg);
			} else if(StringUtil.hasLength(line.dolUnitPrice)) {
				
				float fdolUnitPrice = Float.parseFloat(line.dolUnitPrice);
				if(Double.compare(fdolUnitPrice, 0) == 0) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.num0", i + 1, labelDolUnitPrice));
				}
			}

			
			tempMsg = ValidateUtil.decimalType(i + 1, line.price, labelPrice, 9, 0);
			if(tempMsg != null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, tempMsg);
			}

			
			tempMsg = ValidateUtil.decimalType(i + 1, line.dolPrice, labelDolPrice, 9, 3);
			if(tempMsg != null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, tempMsg);
			}

			
			
			if(StringUtil.hasLength(line.remarks)) {
				if(line.remarks.length() > 50) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.line.maxbytelength", i + 1, labelRemarks, "50"));
				}
			}
		}

		
		if(checkedCount <= 0 && inputPaymentForm.isNew()) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.nocheck"));
		}

		return errors;
	}
}