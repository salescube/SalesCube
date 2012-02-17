/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.ajax;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.Bill;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.ajax.CommonDepositForm;
import jp.co.arkinfosys.service.BillService;
import jp.co.arkinfosys.service.DeliveryService;
import jp.co.arkinfosys.service.DepositSlipService;
import jp.co.arkinfosys.service.SalesService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 請求先情報を取得するアクションクラスです.<BR>
 * 請求先情報だけでなく、指定顧客の入金情報も取得します.
 *
 * @author Ark Information Systems
 *
 */
public class CommonDepositAction extends CommonAjaxResources {

	public static class Param {
		public static final String LAST_BILLING_PRICE = "lastBillingPrice";
		public static final String NOW_PAYBACK_PRICE = "nowPaybackPrice";
		public static final String NOW_SALES_PRICE = "nowSalesPrice";
	}

	@ActionForm
	@Resource
	public CommonDepositForm commonDepositForm;

	@Resource
	public DepositSlipService depositSlipService;

	@Resource
	public SalesService salesService;

	@Resource
	public BillService billService;

	@Resource
	protected DeliveryService deliveryService;

	
	private Date startDate;

	/**
	 * 顧客コードから請求先情報を取得します.
	 * @return 請求先情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getDeliveryInfosByCustomerCode() throws Exception {

		
		if("".equals(commonDepositForm.customerCode)){
			ResponseUtil.write("", "text/javascript");
			return null;
		}

		
		List<DeliveryAndPre> deliveryList;

		try {

			
			deliveryList =
				deliveryService.searchDeliveryByCompleteCustomerCode(
					commonDepositForm.customerCode );

			
			if (deliveryList.size() == 1) {
				DeliveryAndPre dap = deliveryList.get(0);
				commonDepositForm.deliveryCode = dap.deliveryCode;
				commonDepositForm.deliveryName = dap.deliveryName;
				commonDepositForm.deliveryKana = dap.deliveryKana;
				commonDepositForm.deliveryOfficeName = dap.deliveryOfficeName;
				commonDepositForm.deliveryOfficeKana = dap.deliveryOfficeKana;
				commonDepositForm.deliveryDeptName = dap.deliveryDeptName;
				commonDepositForm.deliveryZipCode = dap.deliveryZipCode;
				commonDepositForm.deliveryAddress1 = dap.deliveryAddress1;
				commonDepositForm.deliveryAddress2 = dap.deliveryAddress2;
				commonDepositForm.deliveryPcName = dap.deliveryPcName;
				commonDepositForm.deliveryPcKana = dap.deliveryPcKana;
				commonDepositForm.deliveryPcPreCategory = dap.deliveryPcPreCategory;
				commonDepositForm.deliveryTel = dap.deliveryTel;
				commonDepositForm.deliveryFax = dap.deliveryFax;
				commonDepositForm.deliveryEmail = dap.deliveryEmail;
				commonDepositForm.deliveryUrl = dap.deliveryUrl;
				commonDepositForm.remarks = dap.remarks;
				commonDepositForm.categoryCodeName = dap.categoryCodeName;
				commonDepositForm.customerName = dap.customerName;
				commonDepositForm.cutoffGroup = dap.cutoffGroup;
				commonDepositForm.paybackCycleCategory = dap.paybackCycleCategory;
				commonDepositForm.taxShiftCategory = dap.taxShiftCategory;
				commonDepositForm.taxFractCategory = dap.taxFractCategory;
				commonDepositForm.priceFractCategory = dap.priceFractCategory;
				commonDepositForm.salesCmCategory = dap.salesCmCategory;
				commonDepositForm.salesCmCategoryName = dap.salesCmCategoryName;
				commonDepositForm.customerRoCategory = dap.customerRoCategory;
				commonDepositForm.customerRemarks = dap.customerRemarks;
				commonDepositForm.customerCommentData = dap.customerCommentData;
				commonDepositForm.customerPcPreCategoryName = dap.customerPcPreCategoryName;
				commonDepositForm.salesSlipCategory = dap.salesSlipCategory;
			} else {
				ResponseUtil.write("", "text/javascript");
				return null;
			}

			
			searchBill();

			
			commonDepositForm.nowPaybackPrice =
				depositSlipService.getDepositTotalPrice(
						commonDepositForm.customerCode,
						startDate, commonDepositForm.depositSlipId).toString();

			
			commonDepositForm.nowSalesPrice =
				salesService.getSalesTotalPrice(
						commonDepositForm.customerCode,
					startDate, null ).toString();

			
			if( StringUtil.hasLength( commonDepositForm.nowSalesPrice ) == false ){
				commonDepositForm.nowSalesPrice = "0";
			}
			
			BeanMap bmap = super.createBeanMapWithNullToEmpty(commonDepositForm);

			
			ResponseUtil.write(JSON.encode(bmap), "text/javascript");

		} catch (ServiceException e) {
			e.printStackTrace();
			super.errorLog(e);
			ResponseUtil.write("", "text/javascript");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			super.errorLog(e);
			ResponseUtil.write("", "text/javascript");
			return null;
		}
		return null;
	}

	/**
	 * 直近の請求書情報から請求書日付を設定します.
	 * @throws ServiceException
	 */
	protected void searchBill() throws ServiceException {

		
		List<Bill> billList =
			billService.findLastBillByCustomerCode(commonDepositForm.customerCode);

		if( billList.size() == 0){
			this.startDate = null;
			commonDepositForm.lastBillingPrice = "";
		}else{
			this.startDate = billList.get(0).billCutoffDate;
			if( billList.get(0).thisBillPrice != null ){
				commonDepositForm.lastBillingPrice = billList.get(0).thisBillPrice.toString();
			}else{
				commonDepositForm.lastBillingPrice = "";
			}
		}
	}


}
