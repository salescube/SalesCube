/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.deposit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.deposit.DepositLineDto;
import jp.co.arkinfosys.dto.deposit.DepositSlipDto;
import jp.co.arkinfosys.entity.Bill;
import jp.co.arkinfosys.entity.DepositSlip;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

/**
 * 入金入力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class InputDepositForm extends AbstractSlipEditForm<DepositLineDto> {
	
	public String depositSlipId; 
	public String status; 
	@Required
	@DateType
	public String depositDate; 
	@DateType
	public String inputPdate; 
	public String depositAnnual; 
	public String depositMonthly; 
	public String depositYm; 
	public String userId; 
	public String userName; 
	@Maxlength(maxlength = 50, arg0 = @Arg(key = "labels.tekiyou"))
	public String depositAbstract; 
	@Maxlength(maxlength = 120, arg0 = @Arg(key = "labels.remarks"))
	public String remarks; 
	@Required
	@Mask(mask = Constants.CODE_MASK.CUSTOMER_MASK, msg = @Msg(key = "errors.invalid"), args = @Arg(key = "labels.customerCode", resource = true, position = 0))
	public String customerCode; 
	public String customerName; 
	public String cutoffGroup; 
	public String paybackCycleCategory; 
	public String customerRemarks; 
	public String customerCommentData; 
	public String baCode; 
	public String baName; 
	public String baKana; 
	public String baOfficeName; 
	public String baOfficeKana; 
	public String baDeptName; 
	public String baZipCode; 
	public String baAddress1; 
	public String baAddress2; 
	public String baPcName; 
	public String baPcKana; 
	public String baPcPreCatrgory; 
	public String baPcPre; 
	public String baTel; 
	public String baFax; 
	public String baEmail; 
	public String baUrl; 
	public String salesCmCategory; 
	public String salesCmCategoryName; 
	@Required
	public String depositCategory; 
	public String depositTotal; 
	public String billId; 
	@DateType
	public String billCutoffDate; 
	@DateType
	public String billCutoffPdate; 
	public String artId; 
	public String salesSlipId; 
	public String depositMethodTypeCategory; 
	public String creFunc; 
	@DateType
	public String creDatetm; 
	public String creUser; 
	public String updFunc; 
	public String salesCutoffDate; 
	public String salesCutoffPdate; 

	
	public String lastBillingPrice; 
	public String nowPaybackPrice; 
	public String nowSalesPrice; 
	public String billingBalancePrice; 

	
	public String copySlipName; 
	public String copySlipId; 

	
	public List<DepositLineDto> depLineList = new ArrayList<DepositLineDto>();

	public String inputBillId; 

	
	public int procType = 2;

	
	public int lineElementCount = 3;

	
	public static final int remarksSize = 120;

	
	private String initDepositCategory = CategoryTrns.DEPOSIT_CATEGORY_CASH;

	
	public String customerIsExist;

	/**
	 * 請求書から入金伝票を作成する場合の初期化を行います.
	 * @param bill　請求書
	 */
	public void initialize(Bill bill) {

		
		initializeSlip();

		
		customerIsExist = "";

		DepositSlipDto dto = (DepositSlipDto) this.copyToDto();
		dto.setLineDtoList(this.depLineList);
		dto.fillList();

		
		DepositLineDto lineDto = depLineList.get(0);
		lineDto.price = bill.thisBillPrice.toString();

	}

	/**
	 * 請求書から入金伝票を初期化する場合の顧客情報を設定します.
	 * @param cj 顧客情報
	 * @param dap 請求先情報
	 */
	public void initialize(CustomerJoin cj, DeliveryAndPre dap) {

		customerCode = cj.customerCode; 
		customerName = cj.customerName; 
		cutoffGroup = cj.cutoffGroup; 
		paybackCycleCategory = cj.paybackCycleCategory; 
		customerRemarks = cj.remarks; 
		customerCommentData = cj.commentData; 
		baCode = dap.deliveryCode; 
		baName = dap.deliveryName; 
		baKana = dap.deliveryKana; 
		baOfficeName = dap.deliveryOfficeName; 
		baOfficeKana = dap.deliveryOfficeKana; 
		baDeptName = dap.deliveryDeptName; 
		baZipCode = dap.deliveryZipCode; 
		baAddress1 = dap.deliveryAddress1; 
		baAddress2 = dap.deliveryAddress2; 
		baPcName = dap.deliveryPcName; 
		baPcKana = dap.deliveryPcKana; 
		baPcPreCatrgory = dap.deliveryPcPreCategory; 
		baPcPre = dap.customerPcPreCategoryName; 
		baTel = dap.deliveryTel; 
		baFax = dap.deliveryFax; 
		baEmail = dap.deliveryEmail; 
		baUrl = dap.deliveryUrl; 
		salesCmCategory = cj.salesCmCategory; 
		salesCmCategoryName = cj.categoryCodeName3; 
		taxFractCategory = cj.taxFractCategory;
		priceFractCategory = cj.priceFractCategory;
	}

	/**
	 * 初期化処理を行います.
	 */
	public void initializeSlip() {
		
		depositSlipId = ""; 
		status = DepositSlip.STATUS_INIT; 
		
		depositDate = ""; 
		
		inputPdate = ""; 
		userId = this.userDto.userId; 
		userName = this.userDto.nameKnj; 
		depositAbstract = ""; 
		customerCode = ""; 
		customerName = ""; 
		cutoffGroup = ""; 
		paybackCycleCategory = ""; 
		customerRemarks = ""; 
		customerCommentData = ""; 
		baName = ""; 
		baOfficeName = ""; 
		baDeptName = ""; 
		baZipCode = ""; 
		baAddress1 = ""; 
		baAddress2 = ""; 
		baPcName = ""; 
		baPcKana = ""; 
		baPcPreCatrgory = ""; 
		baPcPre = ""; 
		baTel = ""; 
		baFax = ""; 
		baEmail = ""; 
		
		depositCategory = this.initDepositCategory;

		
		depositMethodTypeCategory = CategoryTrns.DEPOSIT_METHOD_INPUT;

		lastBillingPrice = ""; 
		nowPaybackPrice = ""; 
		nowSalesPrice = ""; 
		billingBalancePrice = ""; 

	}

	/**
	 * 支払条件コードを返します.
	 * @return　支払条件コード
	 */
	public String getCutoffGroupCategory() {
		return this.cutoffGroup + this.paybackCycleCategory;
	}

	/**
	 * @return {@link MENU_ID#INPUT_DEPOSIT}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_DEPOSIT;
	}

	/**
	 * 明細行をクリアし、入力担当者を設定します.
	 */
	@Override
	public void initializeScreenInfo() {
		for (DepositLineDto lineDto : this.depLineList) {
			
			lineDto.initialize();
		}

		
		this.userId = this.userDto.userId;
		this.userName = this.userDto.nameKnj;
	}

	/**
	 * @param dto {@link DepositSlipDto}
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<DepositLineDto> dto) {
		depositMethodTypeCategory = CategoryTrns.DEPOSIT_METHOD_INPUT;
	}

	/**
	 * @return {@link DepositSlipDto}
	 */
	@Override
	public AbstractSlipDto<DepositLineDto> copyToDto() {
		return Beans.createAndCopy(DepositSlipDto.class, this).execute();
	}

	/**
	 * 入金伝票番号を設定します.
	 * @param keyValue 入金伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.depositSlipId = keyValue;
	}

	/**
	 * @return {@link DepositLineDto}のリスト
	 */
	@Override
	public List<DepositLineDto> getLineList() {
		return this.depLineList;
	}

	/**
	 * @param lineList {@link DepositLineDto}のリスト
	 */
	@Override
	public void setLineList(List<DepositLineDto> lineList) {
		this.depLineList = lineList;
	}

	@Override
	public void initCopy() throws ServiceException {
		depositSlipId = ""; 
		status = DepositSlip.STATUS_INIT; 
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);

		
		depositDate = sdf.format(new Date(GregorianCalendar.getInstance()
				.getTimeInMillis()));
		
		inputPdate = sdf.format(new Date(GregorianCalendar.getInstance()
				.getTimeInMillis()));
		userId = this.userDto.userId; 
		userName = this.userDto.nameKnj; 

		
		depositMethodTypeCategory = CategoryTrns.DEPOSIT_METHOD_INPUT;

		for (DepositLineDto lineDto : this.depLineList) {
			
			lineDto.initForCopy();
		}
	}

	/**
	 * 伝票の状態を判定します.
	 * @return　入金締終了状態か否か
	 */
	public boolean isClosed() {
		return DepositSlip.STATUS_CLOSE.equals(status)
				|| StringUtil.hasLength(salesCutoffDate);
	}
}
