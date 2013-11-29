/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.NumberUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.ajax.CommonSupplierForm;
import jp.co.arkinfosys.service.AptBalanceService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 仕入先情報を取得するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonSupplierAction extends CommonAjaxResources {

	@ActionForm
	@Resource
	protected CommonSupplierForm commonSupplierForm;

	@Resource
	protected SupplierService supplierService;

	@Resource
	protected AptBalanceService aptBalanceService;

	/**
	 * 仕入先コードから仕入先情報を取得します.
	 */
	@Execute(validator = true, input = CommonAjaxResources.Mapping.ERROR_JSP)
	public String getSupplierInfosBySupplierCode() throws Exception {

		// 仕入先コードを指定しない場合は検索しません
		if (!StringUtil.hasLength(commonSupplierForm.supplierCode)) {
			ResponseUtil.write("");
			return null;
		}

		try {

			SupplierJoin sup = supplierService
					.findById(commonSupplierForm.supplierCode);

			// 納入先コードを指定した検索なので複数はかえらない
			if (sup != null) {

				// エンティティの内容をマップに展開
				BeanMap map = Beans.createAndCopy(BeanMap.class, sup)
						.dateConverter(Constants.FORMAT.TIMESTAMP,
								"lastCutoffDate", "creDatetm", "updDatetm")
						.execute();

				BigDecimal aptBalance = aptBalanceService
						.calcAptBalanceBySupplierCode(commonSupplierForm.supplierCode);
				DecimalFormat df = NumberUtil.createDecimalFormat(
						sup.priceFractCategory,
						super.mineDto.unitPriceDecAlignment, true);
				map.put("aptBalance", aptBalance != null ? df
						.format(aptBalance) : "");

				// アクションフォームの内容（計算結果）をマップに展開
				BeanMap bmap = super.createBeanMapWithNullToEmpty(map);
				ResponseUtil.write(JSON.encode(bmap));

			} else {
				ResponseUtil.write("", "text/javascript");
			}

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		return null;
	}
}
