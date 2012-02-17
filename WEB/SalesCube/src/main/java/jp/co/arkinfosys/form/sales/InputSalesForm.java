/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.sales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.dto.rorder.ROrderSlipDto;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.RoLineTrn;
import jp.co.arkinfosys.entity.RoSlipTrn;
import jp.co.arkinfosys.entity.SalesLineTrn;
import jp.co.arkinfosys.entity.SalesSlipTrn;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.DoubleType;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.annotation.ShortType;
import org.seasar.struts.annotation.Validwhen;
import org.seasar.struts.util.MessageResourcesUtil;


/**
 * 売上入力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class InputSalesForm extends AbstractSlipEditForm<SalesLineDto> {
	

	public String salesSlipId;				
	public String status;					
    @ShortType
	public String salesAnnual;				
    @ShortType
	public String salesMonthly;				
    @IntegerType
	public String salesYm;					
	@Maxlength(maxlength = 10 )
	public String roSlipId;					
	public String billId;					
	public String salesBillId;				
    @DateType(datePattern = Constants.FORMAT.DATE)
	public String billDate;					
	public String billCutoffGroup;			
	public String paybackCycleCategory;		
    @DateType(datePattern = Constants.FORMAT.DATE)
	public String billCutoffDate;			
	public String billCutoffPdate;			
    @Required(arg0 = @Arg(key = "labels.salesDate"))
    @DateType(datePattern = Constants.FORMAT.DATE)
	public String salesDate;				
    @DateType(datePattern = Constants.FORMAT.DATE,
    		msg = @Msg(key = "errors.date"),
    		arg0 = @Arg(key = "labels.deliveryDate2", resource = true, position = 0))
	public String deliveryDate;				
	@Maxlength(maxlength = 30 )
	public String receptNo;					
	@Maxlength(maxlength = 30 )
	public String customerSlipNo;			
	public String salesCmCategory;			
	public String custsalesCmCategory;		
    @DateType(datePattern = Constants.FORMAT.DATE)
	public String salesCutoffDate;			
	public String salesCutoffPdate;			
	public String userId;					
	public String userName;					
	@Maxlength(maxlength = 120 )
	public String remarks;					
	public String pickingRemarks;			
	public String dcCategory;				
	public String dcName;					
	public String dcTimezoneCategory;		
	public String dcTimezone;				
	@Required
	@Mask(mask = Constants.CODE_MASK.CUSTOMER_MASK)
	public String customerCode;				
	public String customerName;				
	public String customerRemarks;			
	public String customerCommentData;		
    @Validwhen(test = "((customerCode == '" + Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER + "') or (*this* != null))", msg = @Msg(key = "errors.required"), args = @Arg(key = "labels.deliveryCode", resource = false, position = 1))
	public String deliveryCode;				
	public String deliveryName;				
	public String deliveryKana;				
	public String deliveryOfficeName;		
	public String deliveryOfficeKana;		
	public String deliveryDeptName;			
	public String deliveryZipCode;			
	public String deliveryAddress1;			
	public String deliveryAddress2;			
	public String deliveryPcName;			
	public String deliveryPcKana;			
	public String deliveryPcPreCategory;	
	public String deliveryPcPre;			
	public String deliveryTel;				
	public String deliveryFax;				
	public String deliveryEmail;			
	public String deliveryUrl;				
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
	public String baPcPreCategory;			
	public String baPcPre;					
	public String baTel;					
	public String baFax;					
	public String baEmail;					
	public String baUrl;					
    @DoubleType
	public String ctaxPriceTotal;			
    @DoubleType
	public String priceTotal;				
    @DoubleType
	public String gmTotal;					
	public String codSc;					
    @IntegerType
	public String billPrintCount;			
    @IntegerType
	public String deliveryPrintCount;		
    @IntegerType
	public String tempDeliveryPrintCount;	
    @IntegerType
	public String shippingPrintCount;		
    @IntegerType
	public String siPrintCount;				
	public String artId;					

	public String estimatePrintCount;
	public String delborPrintCount;
	public String poPrintCount;
    
	public String customerUrl;
	public String customerOfficeName;
	public String customerOfficeKana;
	public String customerAbbr;
	public String customerDeptName;
	public String customerPcPost;
	public String customerPcKana;
	public String customerPcPreCategory;
	public String customerPcPre;
	public String customerFax;
	public String customerEmail;


    public String adlabel;					
	public String disclaimer;				
	public String creFunc;					
	public String creDatetm;				
	public String creUser;					
	public String updFunc;					


	public String cutoffGroupCategory;	
	public String gmTotalPer;			
	public String total;				

	
	public String pickingListId;
	public String roDate;					
	public String customerPcName;
	public String customerZipCode;
	public String customerAddress1;
	public String customerAddress2;
	public String customerTel;
	public String printDate;

	
	public String roUpdDatetm;				

	
	public String billPrintUnit;			
	public String billDatePrint;			
	public String tempDeliverySlipFlag;		


	
	public String copySlipName;		
	public String copySlipId;		

	
	public List<SalesLineDto> salesLineList = new ArrayList<SalesLineDto>();

	
	public boolean initCategory = true;			

	
	public int initLineDefault = 6;

	
	public int lineElementCount = 15;

	
	public int initLineSize = 35;

	
	
	public String TAX_CATEGORY_IMPOSITION = CategoryTrns.TAX_CATEGORY_IMPOSITION;
	
	public String TAX_CATEGORY_IMPOSITION_OLD = CategoryTrns.TAX_CATEGORY_IMPOSITION_OLD;
	
	public String TAX_CATEGORY_INCLUDED = CategoryTrns.TAX_CATEGORY_INCLUDED;
	
	public String TAX_SHIFT_CATEGORY_INCLUDE_CTAX = CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX;

	
	public String DELIVERY_PROCESS_CATEGORY_NONE = CategoryTrns.DELIVERY_PROCESS_CATEGORY_NONE;
	public String DELIVERY_PROCESS_CATEGORY_PARTIAL = CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL;
	public String DELIVERY_PROCESS_CATEGORY_FULL = CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL;

	
	public RoSlipTrn roSlipTrn;
	public List<RoLineTrn> roLineList = new ArrayList<RoLineTrn>();

	
	public Boolean reportEFlag;

	/**
	 * フォームを初期化状態にします.
	 */
	public void clear() {
		salesSlipId = "";			
		status = SalesSlipTrn.STATUS_INIT;	
		salesAnnual = "";			
		salesMonthly = "";			
		salesYm = "";				
		roSlipId = "";				
		billId = "";				
		salesBillId = "";			
		billDate = "";				
		billCutoffGroup = "";		
		paybackCycleCategory = "";	
		billCutoffDate = "";		
		billCutoffPdate = "";		
									
		salesDate = ""; 

		deliveryDate = "";			
		receptNo = "";				
		customerSlipNo = "";		
		salesCmCategory = "";		
		custsalesCmCategory = "";	
		salesCutoffDate = "";		
		salesCutoffPdate = "";		
		userId = this.userDto.userId;		
		userName = this.userDto.nameKnj;	
		remarks = "";				
		pickingRemarks = "";		
		dcCategory = CategoryTrns.DC_CATEGORY_1;			
		dcName = "";				
		dcTimezoneCategory = "";	
		dcTimezone = "";			
		customerCode = "";			
		customerName = "";			
		customerRemarks = "";		
		customerCommentData = "";	
		deliveryCode = "";			
		deliveryName = "";			
		deliveryKana = "";			
		deliveryOfficeName = "";	
		deliveryOfficeKana = "";	
		deliveryDeptName = "";		
		deliveryZipCode = "";		
		deliveryAddress1 = "";		
		deliveryAddress2 = "";		
		deliveryPcName = "";		
		deliveryPcKana = "";		
		deliveryPcPreCategory = "";	
		deliveryPcPre = "";			
		deliveryTel = "";			
		deliveryFax = "";			
		deliveryEmail = "";			
		deliveryUrl = "";			
		baCode = "";				
		baName = "";				
		baKana = "";				
		baOfficeName = "";			
		baOfficeKana = "";			
		baDeptName = "";			
		baZipCode = "";				
		baAddress1 = "";			
		baAddress2 = "";			
		baPcName = "";				
		baPcKana = "";				
		baPcPreCategory = "";		
		baPcPre = "";				
		baTel = "";					
		baFax = "";					
		baEmail = "";				
		baUrl = "";					
		taxShiftCategory = "";		
		ctaxPriceTotal = "";		
		priceTotal = "";			
		gmTotal = "";				
		codSc = "";					
		billPrintCount = "0";		
		deliveryPrintCount = "0";	
		tempDeliveryPrintCount = "0";
		shippingPrintCount = "0";	
		siPrintCount = "0";			
		adlabel = "";				
									
		disclaimer = MessageResourcesUtil.getMessage("labels.disclaimer");
		creFunc = "";				
		creDatetm = "";				
		creUser = "";				
		updFunc = "";				

		reportEFlag = false;		

		for (SalesLineDto lineDto : this.salesLineList) {
			lineDto.status = SalesLineTrn.STATUS_INIT;
		}
	}

	/**
	 * 受注伝票から売上伝票を初期化します.
	 * @param rosDto　受注伝票情報
	 * @param customer　顧客マスタ情報
	 */
	public void initialize( ROrderSlipDto rosDto, Customer customer ) {

		salesSlipId = "";			
		status = SalesSlipTrn.STATUS_INIT;	
		salesAnnual = "";			
		salesMonthly = "";			
		salesYm = "";				
		roSlipId = rosDto.roSlipId;	
		billId = "";				
		salesBillId = "";			
		billDate = "";				
		billCutoffGroup = rosDto.cutoffGroup;	
		paybackCycleCategory = rosDto.paybackCycleCategory;	
		cutoffGroupCategory = billCutoffGroup + paybackCycleCategory;
		billCutoffDate = "";		
		billCutoffPdate = "";		
									
		salesDate = rosDto.shipDate;
		deliveryDate = rosDto.deliveryDate;			
		receptNo = rosDto.receptNo;				
		customerSlipNo = rosDto.customerSlipNo;		
		salesCmCategory = rosDto.salesCmCategory;	
		custsalesCmCategory = rosDto.salesCmCategory;	
		salesCutoffDate = "";		
		salesCutoffPdate = "";		
		userId = this.userDto.userId;		
		userName = this.userDto.nameKnj;	
		remarks = rosDto.remarks;	
		pickingRemarks = "";		
		dcCategory = ( rosDto.dcCategory == null ) ? CategoryTrns.DC_CATEGORY_1 : rosDto.dcCategory;	
		dcName = ( rosDto.dcName == null ) ? "" : rosDto.dcName;											
		dcTimezoneCategory = ( rosDto.dcTimezoneCategory == null ) ? "" : rosDto.dcTimezoneCategory;		
		dcTimezone = ( rosDto.dcTimezone == null ) ? "" : rosDto.dcTimezone;								
		customerCode = rosDto.customerCode;			
		customerName = rosDto.customerName;			
		customerRemarks = rosDto.customerRemarks;	
		customerCommentData = rosDto.customerCommentData; 
		deliveryCode = rosDto.deliveryCode;			
		deliveryName = rosDto.deliveryName;			
		deliveryKana = rosDto.deliveryKana;			
		deliveryOfficeName = rosDto.deliveryOfficeName;	
		deliveryOfficeKana = rosDto.deliveryOfficeKana;	
		deliveryDeptName = rosDto.deliveryDeptName;		
		deliveryZipCode = rosDto.deliveryZipCode;		
		deliveryAddress1 = rosDto.deliveryAddress1;		
		deliveryAddress2 = rosDto.deliveryAddress2;		
		deliveryPcName = rosDto.deliveryPcName;		
		deliveryPcKana = rosDto.deliveryPcKana;		
		deliveryPcPreCategory = rosDto.deliveryPcPreCategory;	
		deliveryPcPre = rosDto.deliveryPcPre;			
		deliveryTel = rosDto.deliveryTel;			
		deliveryFax = rosDto.deliveryFax;			
		deliveryEmail = rosDto.deliveryEmail;			
		deliveryUrl = rosDto.deliveryUrl;			

		taxShiftCategory = rosDto.taxShiftCategory;		
		taxFractCategory = rosDto.taxFractCategory;		
		priceFractCategory = rosDto.priceFractCategory;	
		ctaxPriceTotal = rosDto.ctaxPriceTotal;		
		priceTotal = rosDto.priceTotal;			
		gmTotal = "";				
		codSc = rosDto.codSc;					
		billPrintCount = "0";		
		deliveryPrintCount = "0";	
		tempDeliveryPrintCount = "0";
		shippingPrintCount = "0";	
		siPrintCount = "0";			
		adlabel = rosDto.customerName + customer.customerPcPreCategoryName;		
									
		disclaimer = MessageResourcesUtil.getMessage("labels.disclaimer");
		creFunc = "";				
		creDatetm = "";				
		creUser = "";				
		updFunc = "";				
		updDatetm = "";				
		updUser = "";				

		roUpdDatetm = rosDto.updDatetm;	
		roDate = rosDto.roDate;		
		reportEFlag = false;		

		billPrintUnit = customer.billPrintUnit;		
		billDatePrint = customer.billDatePrint;		

		
		salesLineList.clear();
		for( ROrderLineDto rolDto : rosDto.getLineDtoList() ){
			try {
				
				if(Constants.STATUS_RORDER_LINE.SALES_FINISH.equals(rolDto.status)){
					continue;
				}
			} catch (Exception e) {
				continue;
			}

			SalesLineDto lineDto = new SalesLineDto();
			lineDto.initialize(rolDto);
			lineDto.lineNo = String.valueOf( salesLineList.size() + 1 );

			
			lineDto.stockCtlCategory = rolDto.stockCtlCategory;
			lineDto.possibleDrawQuantity = rolDto.possibleDrawQuantity;

			salesLineList.add(lineDto);
		}
	}

	/**
	 * 伝票印刷用の初期化を行います.
	 * @param customer　顧客情報
	 */
	public void initialize( Customer customer ) {
		billPrintUnit = customer.billPrintUnit;				
		billDatePrint = customer.billDatePrint;				
		tempDeliverySlipFlag = customer.tempDeliverySlipFlag;
	}

	/**
	 * 請求先情報の初期化を行います.
	 * @param dap　請求先情報
	 */
	public void initialize( DeliveryAndPre dap ) {
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
		baPcPreCategory = dap.deliveryPcPreCategory;
		baPcPre = dap.categoryCodeName;				
		baTel = dap.deliveryTel;					
		baFax = dap.deliveryFax;					
		baEmail = dap.deliveryEmail;				
		baUrl = dap.deliveryUrl;					
	}

	/**
	 * 登録エラーが発生した時の処理を行います.<BR>
	 * 登録時のみで、更新時は関係しません.<BR>
	 * 伝票番号が設定されているとJSP側が更新とみなしてUPDATEを呼び出してしまうので、伝票番号をクリアします.
	 */
	public void initializeForError() {
		this.salesSlipId = "";
	}

	/**
	 * @return {@link MENU_ID#INPUT_SALES}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_SALES;
	}

	/**
	 * 入力担当者を設定します.
	 */
	@Override
	public void initializeScreenInfo() {
		
		this.userId = userDto.userId;
		this.userName = userDto.nameKnj;
	}

	/**
	 * @param dto {@link SalesSlipDto}
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<SalesLineDto> dto) {
		this.dcCategory = CategoryTrns.DC_CATEGORY_1;
	}

	/**
	 * @return {@link SalesSlipDto}
	 */
	@Override
	public AbstractSlipDto<SalesLineDto> copyToDto() {
		return Beans.createAndCopy(SalesSlipDto.class, this).execute();
	}

	/**
	 * 売上伝票番号を設定します.
	 * @param keyValue 売上伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.salesSlipId = keyValue;
	}

	/**
	 * @return {@link SalesLineDto}のリスト
	 */
	@Override
	public List<SalesLineDto> getLineList() {
		return this.salesLineList;
	}

	/**
	 * @param lineList {@link SalesLineDto}のリスト
	 */
	@Override
	public void setLineList(List<SalesLineDto> lineList) {
		this.salesLineList = lineList;
	}

	@Override
	public void initCopy() throws ServiceException {
		salesSlipId = "";					
		status = SalesSlipTrn.STATUS_INIT;	
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);
		salesDate = sdf.format(new Date(GregorianCalendar.getInstance().getTimeInMillis()));
		
		roSlipId = "";
		userId = this.userDto.userId;				
		userName = this.userDto.nameKnj;			

		for (SalesLineDto lineDto : this.salesLineList) {
			lineDto.salesLineId = "";					
			lineDto.status = SalesLineTrn.STATUS_INIT;	
			lineDto.salesSlipId = "";					
			lineDto.roLineId = "";						

		}
	}

	/**
	 * 請求完了かどうか判定します.
	 * @return　請求完了か否か
	 */
	public boolean isClosed() {
		return SalesSlipTrn.STATUS_FINISH.equals(status);
	}

}
