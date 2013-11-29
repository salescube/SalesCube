/*
 * Copyright 2009-2010 Ark Information Systems.
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
	public String setProductCode; // セット商品コード

	public String setProductName; // セット商品名

	public List<ProductSetDto> childProductList; // 子商品リスト

	public int childProductCount;

	public String productCode; // 商品コード(排他制御用)

	public int initLineNum; // 初期表示の行数

	public int maxLineNum; // 最大行数

	public String productFractCategory;

	public String numDecAlignment;

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {

		ActionMessages err = new ActionMessages();

		// 商品コード重複チェック
		if (this.childProductList != null && this.childProductList.size() > 1) {
			Map<String, String> map = new HashMap<String, String>();
			for (ProductSetDto dto : this.childProductList) {
				if (!StringUtil.hasLength(dto.productCode)) {
					// 商品コードが入力されていない行はチェックしない
					continue;
				}
				if (dto.deleted) {
					// 削除行は重複チェックしない
					continue;
				}

				if (map.containsKey(dto.productCode)) {
					// 商品コード重複
					err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.doubleProductCode"));
					break;
				}
				map.put(dto.productCode, dto.productCode);
			}
		}

		// 必須チェック
		for (ProductSetDto dto : this.childProductList) {
			if (!StringUtil.hasLength(dto.productCode)) {
				continue;
			}

			// 数量必須
			if (!StringUtil.hasLength(dto.quantity)) {
				err.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.required", MessageResourcesUtil
								.getMessage("labels.quantity")));
				break;
			}

			// 数量型
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
