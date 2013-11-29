/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.entity.TaxRate;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.porder.InputPOrderForm;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.TaxRateService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 仕入先情報を取得するアクションクラスです.
 * @author Ark Information Systems
 */
public class CommonPOrderAction extends CommonResources {

	@ActionForm
	@Resource
	protected InputPOrderForm inputPOrderForm;

	@Resource
	private SupplierService supplierService;

	@Resource
	private TaxRateService taxRateService;

	@Resource
	private CommonProductAction commonProductAction;

	/**
	 * 日付の形式指定
	 */
	private SimpleDateFormat DF_YMD = new SimpleDateFormat(
			Constants.FORMAT.DATE);

	/**
	 * SQL用パラメータ
	 */
	private static class Param {
		public static final String SUPPLIER_NAME = "supplierName";
		public static final String SUPPLIER_KANA = "supplierKana";
		public static final String SUPPLIER_ZIP_CODE = "supplierZipCode";
		public static final String SUPPLIER_ADDRESS_1 = "supplierAddress1";
		public static final String SUPPLIER_ADDRESS_2 = "supplierAddress2";
		public static final String SUPPLIER_PC_NAME = "supplierPcName";
		public static final String SUPPLIER_PC_KANA = "supplierPcKana";
		public static final String SUPPLIER_PC_PRE_CATEGORY = "supplierPcPreCategory";
		public static final String SUPPLIER_PC_POST = "supplierPcPost";
		public static final String SUPPLIER_TEL = "supplierTel";
		public static final String SUPPLIER_FAX = "supplierFax";
		public static final String SUPPLIER_EMAIL = "supplierEmail";

		public static final String SUPPLIER_ABBR = "supplierAbbr";
		public static final String SUPPLIER_DEPT_NAME = "supplierDeptName";
		public static final String SUPPLIER_PC_PRE = "supplierPcPre";

		public static final String TAX_FRACT_CATEGORY = "taxFractCategory";
		public static final String PRICE_FRACT_CATEGORY = "priceFractCategory";
		public static final String UNIT_PRICE_DEC_ALIGNMENT = "unitPriceDecAlignment";
		public static final String DOL_UNIT_PRICE_DEC_ALIGNMENT = "dolUnitPriceDecAlignment";
		public static final String TAX_PRICE_DEC_ALIGNMENT = "taxPriceDecAlignment";

		public static final String C_UNIT_SIGN = "cUnitSign";

		public static final String RATE_ID = "rateId";
		public static final String TAX_SHIFT_CATEGORY = "taxShiftCategory";
	}

	/**
	 * 仕入先コードから仕入先情報を取得します.
	 * @return 仕入先情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getSupplierInfosByPost() throws Exception {

		getSupplierInfos();

		return null;
	}

	/**
	 * 仕入先コードから仕入先情報を取得します.
	 * @return 仕入先情報
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "getSupplierInfos/{tempSupplierCode}")
	public String getSupplierInfos() throws Exception {

		if (inputPOrderForm.tempSupplierCode.length() == 0) {
			ResponseUtil.write("", "text/javascript");
			return null;
		}

		SupplierJoin supplier;
		try {
			supplier = supplierService
					.findById(inputPOrderForm.tempSupplierCode);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		if (supplier != null) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

			map.put(CommonPOrderAction.Param.SUPPLIER_NAME,
					nullToEmpty(supplier.supplierName));
			map.put(CommonPOrderAction.Param.SUPPLIER_KANA,
					nullToEmpty(supplier.supplierKana));
			map.put(CommonPOrderAction.Param.SUPPLIER_ZIP_CODE,
					nullToEmpty(supplier.supplierZipCode));
			map.put(CommonPOrderAction.Param.SUPPLIER_ADDRESS_1,
					nullToEmpty(supplier.supplierAddress1));
			map.put(CommonPOrderAction.Param.SUPPLIER_ADDRESS_2,
					nullToEmpty(supplier.supplierAddress2));
			map.put(CommonPOrderAction.Param.SUPPLIER_PC_NAME,
					nullToEmpty(supplier.supplierPcName));
			map.put(CommonPOrderAction.Param.SUPPLIER_PC_KANA,
					nullToEmpty(supplier.supplierPcKana));
			map.put(CommonPOrderAction.Param.SUPPLIER_PC_PRE_CATEGORY,
					nullToEmpty(supplier.supplierPcPreCategory));
			map.put(CommonPOrderAction.Param.SUPPLIER_PC_POST,
					nullToEmpty(supplier.supplierPcPost));
			map.put(CommonPOrderAction.Param.SUPPLIER_TEL,
					nullToEmpty(supplier.supplierTel));
			map.put(CommonPOrderAction.Param.SUPPLIER_FAX,
					nullToEmpty(supplier.supplierFax));
			map.put(CommonPOrderAction.Param.SUPPLIER_EMAIL,
					nullToEmpty(supplier.supplierEmail));

			map.put(CommonPOrderAction.Param.SUPPLIER_ABBR,
					nullToEmpty(supplier.supplierAbbr));
			map.put(CommonPOrderAction.Param.SUPPLIER_DEPT_NAME,
					nullToEmpty(supplier.supplierDeptName));
			map.put(CommonPOrderAction.Param.SUPPLIER_PC_PRE,
					nullToEmpty(supplier.supplierPcPre));

			map.put(CommonPOrderAction.Param.TAX_FRACT_CATEGORY,
					nullToEmpty(supplier.taxFractCategory));
			map.put(CommonPOrderAction.Param.PRICE_FRACT_CATEGORY,
					nullToEmpty(supplier.priceFractCategory));
			map.put(CommonPOrderAction.Param.UNIT_PRICE_DEC_ALIGNMENT,
					nullToEmpty(supplier.unitPriceDecAlignment));
			map.put(CommonPOrderAction.Param.DOL_UNIT_PRICE_DEC_ALIGNMENT,
					nullToEmpty(supplier.dolUnitPriceDecAlignment));
			map.put(CommonPOrderAction.Param.TAX_PRICE_DEC_ALIGNMENT,
					nullToEmpty(supplier.taxPriceDecAlignment));

			map.put(CommonPOrderAction.Param.C_UNIT_SIGN,
					nullToEmpty(supplier.cUnitSign));

			map
					.put(CommonPOrderAction.Param.RATE_ID,
							IntToStr(supplier.rateId));
			map.put(CommonPOrderAction.Param.TAX_SHIFT_CATEGORY,
					nullToEmpty(supplier.taxShiftCategory));

			ResponseUtil.write(JSON.encode(map), "text/javascript");

		} else {
			ResponseUtil.write("", "text/javascript");
		}
		return null;
	}

	/**
	 * null文字列を空文字列に変換します.
	 * @param l_target 変換する文字列
	 * @return 変換した文字列
	 */
	private String nullToEmpty(String l_target) {
		return (l_target == null ? "" : l_target);
	}

    /**
     * Integerを文字列に変換します.<BR>
     * nullの場合は、空文字列を返します.
     * @param l_target 変換するInteger
     * @return 変換した文字列
     */
	private String IntToStr(Integer l_target) {
		return (l_target == null ? "" : l_target.toString());
	}

	/**
	 * 仕入先コードから仕入先最新レートを取得します.
	 * @return 仕入先最新レート
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getSupplierRate() throws Exception {

		SupplierJoin supplier;
		java.sql.Date sqlDate = new java.sql.Date(DF_YMD.parse(
				inputPOrderForm.targetDate).getTime());

		try {
			supplier = supplierService.findSupplierRateByCodeDate(
					inputPOrderForm.tempSupplierCode, sqlDate);
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
	public String lgetSupplierTaxRate(Integer lrateId,
			String ltaxShiftCategory, String ltargetDate) throws Exception {
		// 受け取った日付
		java.sql.Date sqlDate = new java.sql.Date(DF_YMD.parse(ltargetDate)
				.getTime());
		String result = "";
		// レートを持たない＝国内仕入先なら
		if (lrateId == null) {
			// かつ、税転嫁が外税伝票計あるいは外税締単位であるなら
			if (ltaxShiftCategory != null) {
				if ((CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL)
						.equals(ltaxShiftCategory)
						|| (CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS)
								.equals(ltaxShiftCategory)) {
					// レートを取得し返す
					try {
						TaxRate taxRate = taxRateService.findTaxRateById(
								CategoryTrns.TAX_TYPE_CTAX, sqlDate.toString());
						result = ((taxRate != null) ? taxRate.taxRate
								.toString() : "0.0");
					} catch (Exception e) {
						super.errorLog(e);
						throw e;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 仕入先コードから仕入先情報（仕入先税率を含む）を取得します.
	 * @return 仕入先情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getSupplierTaxRate() throws Exception {
		// 仕入先情報取得
		SupplierJoin supplier;
		try {
			supplier = supplierService
					.findById(inputPOrderForm.tempSupplierCode);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		String result = "";
		// 仕入先情報が取得できて
		if (supplier != null) {
			result = lgetSupplierTaxRate(supplier.rateId,
					supplier.taxShiftCategory, inputPOrderForm.targetDate);
		}
		ResponseUtil.write(result);
		return null;
	}

	/**
	 * 商品コードから商品情報を取得します.
	 * @return 商品情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getProductInfosByPost() throws Exception {
		commonProductAction.commonProductForm.productCode = inputPOrderForm.tempProductCode;
		commonProductAction.getProductInfos();
		return null;
	}

}
