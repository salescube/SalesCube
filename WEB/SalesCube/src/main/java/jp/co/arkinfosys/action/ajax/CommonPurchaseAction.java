/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.TaxRate;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.purchase.InputPurchaseForm;
import jp.co.arkinfosys.service.PoSlipService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.TaxRateService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 仕入先情報を取得するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonPurchaseAction extends CommonResources {

	@ActionForm
	@Resource
	protected InputPurchaseForm inputPurchaseForm;

	@Resource
	protected SupplierService supplierService;

	@Resource
	protected TaxRateService taxRateService;

	@Resource
	private PoSlipService poSlipService;

	/**
	 * 日付の形式指定
	 */
	SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);

	/**
	 * 仕入先コードと仕入日から仕入先最新レートを取得します.
	 * @return 仕入先最新レート
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getSupplierRate() throws Exception {

		SupplierJoin supplier;
		java.sql.Date sqlDate = new java.sql.Date(DF_YMD.parse(
				inputPurchaseForm.targetDate).getTime());

		try {
			supplier = supplierService.findSupplierRateByCodeDate(
					inputPurchaseForm.tempSupplierCode, sqlDate);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		String temp = "";

		if (supplier != null) {
			temp = supplier.supplierRate;
		}

		ResponseUtil.write(temp);
		return null;
	}

	/**
	 * 仕入先コードと日付から仕入先税率を取得します.
	 * @return 仕入先税率
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getSupplierTaxRate() throws Exception {

		SupplierJoin supplier;

		// 受け取った日付
		java.sql.Date sqlDate = new java.sql.Date(DF_YMD.parse(
				inputPurchaseForm.targetDate).getTime());

		// 仕入先情報取得
		try {
			supplier = supplierService
					.findById(inputPurchaseForm.tempSupplierCode);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		String result = "";
		// 仕入先情報が取得できて
		if (supplier != null) {
			// 税転嫁が外税伝票計あるいは外税締単位であるなら
			if (supplier.taxShiftCategory != null) {
				if (CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL
						.equals(supplier.taxShiftCategory)
						|| CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS
								.equals(supplier.taxShiftCategory)) {
					// レートを取得し返す
					TaxRate taxRate = taxRateService.findTaxRateById(
							CategoryTrns.TAX_TYPE_CTAX, sqlDate.toString());
					result = ((taxRate != null) ? taxRate.taxRate.toString()
							: "0.0");
				}
			}
		}
		ResponseUtil.write(result);
		return null;

	}

	/**
	 * 発注伝票行IDから明細の残数を取得します.
	 */
	@Execute(validator = true, urlPattern = "getRestQuantityByPoLineId/{tempPoLineId}", input = CommonAjaxResources.Mapping.ERROR_JSP)
	public String getRestQuantityByPoLineId() throws Exception {

		// 発注伝票行IDを指定しない場合は検索しません
		if (!StringUtil.hasLength(inputPurchaseForm.tempPoLineId)) {
			ResponseUtil.write("");
			return null;
		}

		String result = "";
		try {
			BigDecimal restQunatity = poSlipService
					.getRestQuantityByPoLineId(inputPurchaseForm.tempPoLineId);
			result = restQunatity.toString();
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		ResponseUtil.write(result);
		return null;
	}
}
