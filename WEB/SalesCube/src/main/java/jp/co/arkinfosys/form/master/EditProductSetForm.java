/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.master;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.ProductSetDto;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * セット商品画面（登録・編集）のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class EditProductSetForm extends AbstractEditForm {

	@Required
	@Mask(mask = Constants.CODE_MASK.PRODUCT_MASK)
	public String setProductCode; 

	public String setProductName; 

	public List<ProductSetDto> childProductList; 

	public int childProductCount;

	public String productCode; 

	public int initLineNum; 

	public int maxLineNum; 

	public String productFractCategory;

	public String numDecAlignment;

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {

		ActionMessages err = new ActionMessages();

		
		if (this.childProductList != null && this.childProductList.size() > 1) {
			Map<String, String> map = new HashMap<String, String>();
			for (ProductSetDto dto : this.childProductList) {
				if (!StringUtil.hasLength(dto.productCode)) {
					
					continue;
				}
				if (dto.deleted) {
					
					continue;
				}

				if (map.containsKey(dto.productCode)) {
					
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.doubleProductCode"));
					break;
				}
				map.put(dto.productCode, dto.productCode);
			}
		}

		
		for (ProductSetDto dto : this.childProductList) {
			if (!StringUtil.hasLength(dto.productCode)) {
				continue;
			}

			
			if (!StringUtil.hasLength(dto.quantity)) {
				err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.required", MessageResourcesUtil
								.getMessage("labels.quantity")));
				break;
			}

			
			try {
				new BigDecimal(dto.quantity);
			} catch (NumberFormatException e) {
				err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.float", MessageResourcesUtil
								.getMessage("labels.quantity")));
				break;
			}
		}

		return err;
	}

	/**
	 * フォームを初期化します.
	 */
	public void reset() {
		this.setProductCode = null;
		this.setProductName = null;
		this.childProductList = new ArrayList<ProductSetDto>();
		this.childProductCount = 0;
		this.creDatetmShow = null;
		this.updDatetmShow = null;
		this.productCode = null;
		this.updDatetm = null;
		this.productFractCategory = null;
		this.numDecAlignment = null;
	}

	/**
	 * フォームを初期化します. <BR>
	 * 未使用です.
	 */
	@Override
	public void initialize() {
	}

}
