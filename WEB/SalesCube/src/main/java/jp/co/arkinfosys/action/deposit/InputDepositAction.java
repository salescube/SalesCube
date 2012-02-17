/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.deposit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSlipEditAction;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.SlipStatusCategories;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.ValidateUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.deposit.DepositLineDto;
import jp.co.arkinfosys.dto.deposit.DepositSlipDto;
import jp.co.arkinfosys.entity.BankDepositRel;
import jp.co.arkinfosys.entity.Bill;
import jp.co.arkinfosys.entity.DeliveryDepositRel;
import jp.co.arkinfosys.entity.DepositLine;
import jp.co.arkinfosys.entity.DepositSlip;
import jp.co.arkinfosys.entity.join.BankDwb;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.form.deposit.InputDepositForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.BankDepositRelService;
import jp.co.arkinfosys.service.BankService;
import jp.co.arkinfosys.service.BillOldService;
import jp.co.arkinfosys.service.BillService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.DeliveryDepositRelService;
import jp.co.arkinfosys.service.DeliveryService;
import jp.co.arkinfosys.service.DepositLineService;
import jp.co.arkinfosys.service.DepositSlipService;
import jp.co.arkinfosys.service.SalesService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 入金入力画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class InputDepositAction extends
		AbstractSlipEditAction<DepositSlipDto, DepositLineDto> {

	/**
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "inputDeposit.jsp";
	}

	
	@ActionForm
	@Resource
	public InputDepositForm inputDepositForm;

	
	@Resource
	private DepositSlipService depositSlipService;

	@Resource
	private DepositLineService depositLineService;

	@Resource
	protected CategoryService categoryService;

	@Resource
	protected BankService bankService;

	@Resource
	protected CustomerService customerService;

	@Resource
	protected DeliveryService deliveryService;

	@Resource
	public SalesService salesService;

	@Resource
	public BillService billService;

	@Resource
	public BillOldService billOldService;

	@Resource
	public DeliveryDepositRelService deliveryDepositRelService;

	@Resource
	public BankDepositRelService bankDepositRelService;

	
	
	public DepositSlip depositSlip;
	
	public List<DepositLine> depositLine;

	
	public List<LabelValueBean> depositCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> preTypeCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> salesCmCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> cutoffGroupCategoryList = new ArrayList<LabelValueBean>();

	
	public List<LabelValueBean> bankMstList = new ArrayList<LabelValueBean>();

	private boolean initCategoryFlag = false;

	/**
	 * 請求書からの入金初期表示処理を行います.
	 *
	 * @return 入金入力画面のパス
	 */
	@Execute(validator = false)
	public String inputByBill() {
		try {
			super.prepareForm();

			this.inputDepositForm.initialize();

			initCategoryList();

			loadBillSlip();

		} catch (ServiceException e) {
			if ("".equals(e.getMessage()) == false) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage(e.getMessage(), ""));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			} else {
				e.printStackTrace();
			}
		}
		return InputDepositAction.Mapping.INPUT;
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws ServiceException
	 */
	protected void initCategoryList() throws ServiceException {

		if (initCategoryFlag) {
			
			return;
		}
		
		createCategoryList(Categories.DEPOSIT_CATEGORY, depositCategoryList,
				false);

		
		createCategoryList(Categories.PRE_TYPE, preTypeCategoryList, true);

		
		createCategoryList(Categories.SALES_CM_CATEGORY, salesCmCategoryList,
				true);

		
		createCategoryList(Categories.CUTOFF_GROUP, cutoffGroupCategoryList,
				true);

		
		LabelValueBean beanB = new LabelValueBean();
		beanB.setValue("");
		beanB.setLabel(" ");
		this.bankMstList.add(beanB);
		
		List<BankDwb> bankList = this.bankService.selectBankDwbList();
		for (BankDwb bank : bankList) {
			beanB = new LabelValueBean();
			String value = this.bankService.getValue(bank);
			String name = this.bankService.getName(bank);
			beanB.setValue(value);
			beanB.setLabel(name);
			this.bankMstList.add(beanB);
		}
		initCategoryFlag = true;
	}

	/**
	 * 区分マスタからリストを生成します.
	 * @param categoryType 区分マスタコード
	 * @param list 作成するリスト
	 * @param emptyString 最初の空欄選択し有無
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
	 * 入金入力処理用バリデータです.
	 * @return エラーメッセージ
	 */
	public ActionErrors validateAtCreateSlip() {
		ActionErrors errors = new ActionErrors();
		ActionMessage error;

		
		if (customerService.isExistCustomerCode(inputDepositForm.customerCode) == false) {
			
			String strLabel = MessageResourcesUtil
					.getMessage("labels.customerCode");
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.dataNotExist", strLabel,
					inputDepositForm.customerCode));
		}

		
		if (!StringUtil.hasLength(inputDepositForm.depositCategory)) {
			String strLabel = MessageResourcesUtil
					.getMessage("labels.depositCategory");
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.required", strLabel));
		}

		int validCnt = 0;
		for (int i = 0; i < inputDepositForm.depLineList.size(); i++) {
			DepositLineDto lineDto = inputDepositForm.depLineList.get(i);

			if (lineDto.isBlank()) {
				continue;
			}

			validCnt++;
			
			if (StringUtil.hasLength(lineDto.remarks)) {
				error = ValidateUtil.maxlength(lineDto.remarks,
						InputDepositForm.remarksSize, "errors.line.length",
						new Object[] {
								i + 1,
								MessageResourcesUtil
										.getMessage("labels.remarks"),
								InputDepositForm.remarksSize });
				if (error != null) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, error);
				}
			}
			
			if (StringUtil.hasLength(lineDto.price)) {
				error = ValidateUtil
						.integerType(lineDto.price, "errors.line.integer",
								new Object[] {
										i + 1,
										MessageResourcesUtil
												.getMessage("labels.price") });
				if (error != null) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, error);
				} else {
					Double price = Double.parseDouble(lineDto.price);
					if ((price < Constants.LIMIT_VALUE.PRICE_MIN)
							|| (price > Constants.LIMIT_VALUE.PRICE_MAX)) {
						String strLabel = MessageResourcesUtil
								.getMessage("labels.price");
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.range",
										lineDto.lineNo, strLabel,
										Constants.LIMIT_VALUE.PRICE_MIN
												.toString(),
										Constants.LIMIT_VALUE.PRICE_MAX
												.toString()));
					}

					
					if (price == 0) {
						String strLabel = MessageResourcesUtil
								.getMessage("labels.price");
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage("errors.line.num0",
										lineDto.lineNo, strLabel));

					}
				}
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.required", i + 1, MessageResourcesUtil
								.getMessage("labels.price")));
			}
		}
		
		if (validCnt == 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.deposit.noline"));
		}
		return errors;
	}

	/**
	 * 指定された請求書を読み込んでアクションフォームを生成します.
	 * @throws ServiceException
	 */
	protected void loadBillSlip() throws ServiceException {

		
		Bill bill = billService.findBillById(Integer
				.parseInt(inputDepositForm.inputBillId));
		if (bill == null) {
			
			
			bill = billOldService.findBillById(Integer
					.parseInt(inputDepositForm.inputBillId));
			if (bill == null) {
				String strLabel = MessageResourcesUtil
						.getMessage("erroes.db.bill");

				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.notExist", strLabel));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return;
			}
		}
		inputDepositForm.initialize(bill);
		
		CustomerJoin cj = customerService.findById(bill.customerCode);
		
		List<DeliveryAndPre> deliveryList;
		try {
			deliveryList = deliveryService
					.searchDeliveryByCompleteCustomerCode(bill.customerCode);
			if (deliveryList.size() != 1) {
				throw new ServiceException("errors.dataNotFound");
			}
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		inputDepositForm.initialize(cj, deliveryList.get(0));

		
		List<Bill> billList = billService
				.findLastBillByCustomerCode(inputDepositForm.customerCode);
		Date startDate;
		if (billList.size() == 0) {
			startDate = null;
			inputDepositForm.lastBillingPrice = "";
		} else {
			startDate = billList.get(0).billCutoffDate;
			if (billList.get(0).thisBillPrice != null) {
				inputDepositForm.lastBillingPrice = billList.get(0).thisBillPrice
						.toString();
			} else {
				inputDepositForm.lastBillingPrice = "";
			}
		}

		
		inputDepositForm.nowPaybackPrice = depositSlipService
				.getDepositTotalPrice(inputDepositForm.customerCode, startDate,
						inputDepositForm.depositSlipId).toString();

		
		inputDepositForm.nowSalesPrice = salesService.getSalesTotalPrice(
				inputDepositForm.customerCode, startDate, null).toString();
	}

	/**
	 * プルダウンのコード値から名称を取得して設定します.
	 */
	private void setValueToName() {

		
		for (DepositLineDto lineDto : inputDepositForm.depLineList) {
			for (LabelValueBean lvb : bankMstList) {
				if (lvb.getValue().equals(lineDto.bankId)) {
					lineDto.bankInfo = lvb.getLabel();
				}
			}
		}
	}

	/**
	 * アクションフォームを返します.
	 * @return {@link InputDepositForm}
	 */
	@Override
	protected AbstractSlipEditForm<DepositLineDto> getActionForm() {
		return this.inputDepositForm;
	}

	/**
	 * 画面表示に使用しているプルダウン等の情報を作成します.
	 * @throws Exception
	 */
	@Override
	protected void createList() throws Exception {
		initCategoryList();
	}

	/**
	 * 新規入金入力データのDTOを返します.
	 * @return {@link DepositSlipDto}
	 */
	@Override
	protected AbstractSlipDto<DepositLineDto> createDTO() {
		return new DepositSlipDto();
	}


	/**
	 * 入力画面jspのURIを返します.
	 * @return 入金入力画面jspのURI
	 */
	@Override
	protected String getInputURIString() {
		return Mapping.INPUT;
	}

	/**
	 * 伝票サービスを返します.
	 * @return {@link DepositSlipService}
	 */
	@Override
	protected AbstractSlipService<DepositSlip, DepositSlipDto> getSlipService() {
		return this.depositSlipService;
	}

	/**
	 * 明細行サービスを返します.
	 * @return {@link DepositLineService}
	 */
	@Override
	protected AbstractLineService<DepositLine, DepositLineDto, DepositSlipDto> getLineService() {
		return this.depositLineService;
	}

	/**
	 * 入金入力で使用するサービスを返します.<br>
	 * 未使用です.
	 * @return null;
	 */
	@Override
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		return null;
	}

	/**
	 * 伝票データを取得します.
	 * @return 読み込みできたか否か
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected boolean loadData() throws Exception, ServiceException {

		
		
		DepositSlipDto dto = depositSlipService
				.loadBySlipId(inputDepositForm.depositSlipId);

		if (dto == null) {
			return false;
		}

		Beans.copy(dto, inputDepositForm).execute();

		
		List<DepositLineDto> dlList = depositLineService.loadBySlip(dto);
		dto.setLineDtoList(dlList);
		dto.fillList();
		inputDepositForm.setLineList(dto.getLineDtoList());

		
		List<Bill> billList = billService
				.findLastBillByCustomerCode(inputDepositForm.customerCode);
		Date startDate;
		if (billList.size() == 0) {
			startDate = null;
			inputDepositForm.lastBillingPrice = "";
		} else {
			startDate = billList.get(0).billCutoffDate;
			if (billList.get(0).thisBillPrice != null) {
				inputDepositForm.lastBillingPrice = billList.get(0).thisBillPrice
						.toString();
			} else {
				inputDepositForm.lastBillingPrice = "";
			}
		}

		
		inputDepositForm.nowPaybackPrice = depositSlipService
				.getDepositTotalPrice(inputDepositForm.customerCode, startDate,
						inputDepositForm.depositSlipId).toString();

		
		inputDepositForm.nowSalesPrice = salesService.getSalesTotalPrice(
				inputDepositForm.customerCode, startDate, null).toString();

		return true;
	}

	/**
	 * 例外発生時のラベルを返します.
	 * @return 例外発生時のラベル
	 */
	@Override
	public String getSlipKeyLabel() {
		return "erroes.db.depositSlip";
	}

	/**
	 * リストボックスの選択値から文字列を設定します.
	 * @param bInsert 登録/更新フラグ
	 * @param dto 入金伝票DTO
	 * @throws Exception
	 */
	@Override
	protected void beforeUpsert(boolean bInsert, AbstractSlipDto<DepositLineDto> dto)
			throws Exception {
		
		setValueToName();
	}

	/**
	 * 伝票のステータスに応じたメッセージを表示します.
	 * @throws Exception
	 * @throws ServiceException
	 */
	@Override
	protected void afterLoad() throws Exception, ServiceException {
		if (Constants.STATUS_DEPOSIT_SLIP.CUTOFF
				.equals(this.inputDepositForm.status)) {

			
			String categoryName = categoryService.findCategoryNameByIdAndCode(
					SlipStatusCategories.DEPOSIT_SLIP_STATUS,
					this.inputDepositForm.status);
			
			String strSlipLabel = MessageResourcesUtil
					.getMessage("erroes.db.depositSlip");
			
			String strActionLabel = MessageResourcesUtil
					.getMessage("words.action.edit");
			
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.slip.lock", strSlipLabel,
							categoryName, strActionLabel));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} else {
			if (StringUtil.hasLength(this.inputDepositForm.salesCutoffDate)) {

				
				String categoryName = MessageResourcesUtil
						.getMessage("labesl.closeArtBalance");
				
				String strSlipLabel = MessageResourcesUtil
						.getMessage("erroes.db.depositSlip");
				
				String strActionLabel = MessageResourcesUtil
						.getMessage("words.action.edit");
				
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("infos.slip.lock", strSlipLabel,
								categoryName, strActionLabel));
				ActionMessagesUtil.addMessages(super.httpRequest,
						super.messages);
			}
		}
	}

	/**
	 * 入金伝票に関連するデータを削除します.
	 * @param dto 入金伝票DTO
	 * @throws Exception
	 */
	@Override
	protected void afterDelete(AbstractSlipDto<DepositLineDto> dto) throws Exception {
		try {

			if (StringUtil.hasLength(inputDepositForm.depositSlipId)) {
				bankDepositRelService
						.updateAudit(
								BankDepositRel.TABLE_NAME,
								new String[] { BankDepositRelService.Param.DEPOSIT_SLIP_ID },
								new Object[] { inputDepositForm.depositSlipId });
				bankDepositRelService
						.deleteByDepositSlipId(inputDepositForm.depositSlipId);

				deliveryDepositRelService
						.updateAudit(
								DeliveryDepositRel.TABLE_NAME,
								new String[] { BankDepositRelService.Param.DEPOSIT_SLIP_ID },
								new Object[] { inputDepositForm.depositSlipId });
				deliveryDepositRelService
						.deleteByDepositSlipId(inputDepositForm.depositSlipId);
			}

			inputDepositForm.initializeSlip();

		} catch (ServiceException e) {
			if ("".equals(e.getMessage()) == false) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage(e.getMessage(), ""));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			} else {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.system", ""));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		}
	}

}
