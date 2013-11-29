/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;


import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.ajax.CommonBulkForm;
import jp.co.arkinfosys.service.DiscountRelService;
import jp.co.arkinfosys.service.ProductService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.ResponseUtil;

/**
 * まとめ買い値引き情報を取得するアクションクラスです.
 *
 * @author Ark Information Systems
 */
public class CommonBulkRetailPriceAction extends CommonResources {

	@ActionForm
	@Resource
	public CommonBulkForm commonBulkForm;

	@Resource
	private DiscountRelService discountRelService;

	@Resource
	private ProductService productService;

	/**
	 * 商品コードと数量から割引単価を取得します.
	 * @return 割引単価
	 * @throws Exception
	 */
	@Execute(validator = false, stopOnValidationError = true, urlPattern = "getPrice" )
	public String getPrice() throws Exception {

		//受け取った商品コード
		if( !StringUtil.hasLength(commonBulkForm.bulkProductCode)){
			String strLabel = MessageResourcesUtil.getMessage("labels.productCode");
			if( super.messages.size() == 0 ){
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.invalid", strLabel));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			}
			return CommonAjaxResources.Mapping.ERROR_JSP;
		}
		if( !StringUtil.hasLength(commonBulkForm.bulkQuantity)){
			commonBulkForm.bulkQuantity = "0";
		}

		String result = "";
		//単価を取得し返す
		try {
			ProductJoin pj = productService.findById(commonBulkForm.bulkProductCode);
			if( pj != null ){
				if( pj.retailPrice != null ){
					commonBulkForm.bulkUnitRetailPrice = pj.retailPrice.toString();
				}else{
					// 上代未設定の時には０で計算
					commonBulkForm.bulkUnitRetailPrice = "0";
				}
			}else{
				String strLabel = MessageResourcesUtil.getMessage("labels.productCode");
				if( super.messages.size() == 0 ){
					super.messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.invalid", strLabel));
					ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				}
				return CommonAjaxResources.Mapping.ERROR_JSP;
			}

			Double quantity = Double.valueOf(commonBulkForm.bulkQuantity.replaceAll(",", ""));
			Double unitRetailPrice = Double.valueOf(commonBulkForm.bulkUnitRetailPrice.replaceAll(",", ""));
			Double bulkPrice = discountRelService.getBulkDiscountUnitPrice(quantity, unitRetailPrice, commonBulkForm.bulkProductCode);
			result = bulkPrice.toString();
		} catch (Exception e) {

			return CommonAjaxResources.Mapping.ERROR_JSP;
		}
		ResponseUtil.write(result);
		return null;
	}

	/**
	 * 商品コードから数量割引商品であるかを返します.
	 * @return 数量割引商品の場合は商品コード、対象外の場合は空文字列
	 * @throws Exception
	 */
	@Execute(validator = false, stopOnValidationError = true, urlPattern = "isDiscount" )
	public String isDiscount() throws Exception {

		//受け取った商品コード
		if( !StringUtil.hasLength(commonBulkForm.bulkProductCode)){
			String strLabel = MessageResourcesUtil.getMessage("labels.productCode");
			if( super.messages.size() == 0 ){
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.invalid", strLabel));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			}
			return CommonAjaxResources.Mapping.ERROR_JSP;
		}
		if( !StringUtil.hasLength(commonBulkForm.bulkQuantity)){
			commonBulkForm.bulkQuantity = "0";
		}

		String result = "";
		//割引対象である場合、
		try {
			ProductJoin pj = productService.findById(commonBulkForm.bulkProductCode);
			if( pj != null ){
				if( pj.retailPrice != null ){
					commonBulkForm.bulkUnitRetailPrice = pj.retailPrice.toString();
				}else{
					// 上代未設定の時には０で計算
					commonBulkForm.bulkUnitRetailPrice = "0";
				}
			}else{
				String strLabel = MessageResourcesUtil.getMessage("labels.productCode");
				if( super.messages.size() == 0 ){
					super.messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.invalid", strLabel));
					ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				}
				return CommonAjaxResources.Mapping.ERROR_JSP;
			}

			//数量割引商品であるか判断する
			Boolean isDiscount = discountRelService.isDiscountMstByProduct(commonBulkForm.bulkProductCode);
			if(isDiscount){
				result = commonBulkForm.bulkProductCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.errorLog(e);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.system"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return CommonAjaxResources.Mapping.ERROR_JSP;
		}
		ResponseUtil.write(result);
		return null;
	}
}
