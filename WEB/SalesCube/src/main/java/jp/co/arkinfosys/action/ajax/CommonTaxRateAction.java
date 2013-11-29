/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;


import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.TaxRate;
import jp.co.arkinfosys.form.ajax.CommonDateForm;
import jp.co.arkinfosys.service.TaxRateService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.ResponseUtil;

/**
 * 税率情報を取得するアクションクラスです.
 * @author Ark Information Systems
 */
public class CommonTaxRateAction extends CommonResources {

	@ActionForm
	@Resource
	protected CommonDateForm commonDateForm;

	@Resource
	private TaxRateService taxRateService;

	/**
	 * 日付の形式指定
	 */
	private SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);

	/**
	 * 日付から税率を取得します.
	 * @return 税率
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "getTaxRateByDate/{targetDate}")
	public String getTaxRateByDate() throws Exception {

		//受け取った日付
		if( !StringUtil.hasLength(commonDateForm.targetDate)){
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.condition.insufficiency"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return CommonAjaxResources.Mapping.ERROR_JSP;
		}

		String result = "";
		//レートを取得し返す
		try {
			String date = commonDateForm.targetDate.replaceAll("\\*", "/");
			java.sql.Date sqlDate = new java.sql.Date(DF_YMD.parse(date).getTime());
			TaxRate taxRate = taxRateService.findTaxRateById(CategoryTrns.TAX_TYPE_CTAX, sqlDate.toString());
			result = ((taxRate!=null)?taxRate.taxRate.toString():"0.0");
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
