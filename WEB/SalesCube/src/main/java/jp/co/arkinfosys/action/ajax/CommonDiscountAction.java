/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.util.HashMap;

import javax.annotation.Resource;

import jp.co.arkinfosys.entity.Discount;
import jp.co.arkinfosys.form.ajax.CommonDiscountForm;
import jp.co.arkinfosys.service.DiscountService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 数量割引情報を取得するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonDiscountAction extends CommonAjaxResources {

	@ActionForm
	@Resource
	protected CommonDiscountForm commonDiscountForm;

	@Resource
	protected DiscountService discountService;

	/**
	 * 割引コードから数量割引情報を取得します.
	 * @return 数量割引情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getDiscountInfos() throws Exception {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			Discount discount = discountService.findById(commonDiscountForm.discountId);

			if (discount != null) {
				map.put(DiscountService.Param.DISCOUNT_ID,discount.discountId==null?"":discount.discountId);
				map.put(DiscountService.Param.DISCOUNT_NAME,discount.discountName==null?"":discount.discountName);
				map.put(DiscountService.Param.USE_FLAG,discount.useFlag==null?"":discount.useFlag);
				map.put(DiscountService.Param.REMARKS,discount.remarks==null?"":discount.remarks);
			} else {
				// 検索結果が空=Nullなら空文字列で返却する
				ResponseUtil.write("", "text/javascript");
				return null;
			}

			ResponseUtil.write(JSON.encode(map), "text/javascript");
			return null;
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}
}
