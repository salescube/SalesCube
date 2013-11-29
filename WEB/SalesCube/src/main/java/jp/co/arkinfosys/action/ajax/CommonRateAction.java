/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.Rate;
import jp.co.arkinfosys.form.ajax.CommonRateForm;
import jp.co.arkinfosys.service.RateService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * レート情報を取得するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonRateAction extends CommonAjaxResources {

	@ActionForm
	@Resource
	protected CommonRateForm commonRateForm;

	@Resource
	protected RateService rateService;

	/**
	 * レートIDからレートマスタの情報を取得します.
	 * @return レートマスタ情報
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "getRateInfosByRateId/{rateId}")
	public String getRateInfosByRateId() throws Exception {

		// レートIDを指定しない場合は検索しません
		if (!StringUtil.hasLength(commonRateForm.rateId)) {
			ResponseUtil.write("", "text/javascript");
			return null;
		}

		try {

			Rate rate = rateService.findById(commonRateForm.rateId);

			// レートIDを指定した検索なので複数はかえらない
			if (rate != null) {

				// エンティティの内容をマップに展開
				BeanMap map = Beans.createAndCopy(BeanMap.class, rate)
						.dateConverter(Constants.FORMAT.TIMESTAMP, "creDatetm",
								"updDatetm").execute();

				// アクションフォームの内容（計算結果）をマップに展開
				BeanMap bmap = super.createBeanMapWithNullToEmpty(map);
				ResponseUtil.write(JSON.encode(bmap), "text/javascript");

			} else {
				ResponseUtil.write("", "text/javascript");
			}

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		return null;
	}


	/**
	 * 全通貨記号を取得します.
	 * @return 全通貨記号
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getAllRateSign() throws Exception {
		try{
			List<Rate> rateList = rateService.findAllRate();
			Map<String, Object> rateMap = new HashMap<String, Object>();
			for (Rate rate : rateList) {
				// レートIDをキーに、通貨記号
				rateMap.put(Integer.toString(rate.rateId), rate.sign);
			}

			if (rateList.size() != 0) {
				BeanMap map = super.createBeanMapWithNullToEmpty(rateMap);
				ResponseUtil.write(JSON.encode(map), "text/javascript");
			} else {
				ResponseUtil.write("", "text/javascript");
			}
		}catch(ServiceException e){
			super.errorLog(e);
			throw e;
		}
		return null;
	}


}
