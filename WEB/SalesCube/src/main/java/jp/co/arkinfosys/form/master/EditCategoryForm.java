/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.CategoryDto;
import jp.co.arkinfosys.entity.Category;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 区分マスタ管理　区分名一覧の画面情報を持つアクションフォームです.
 *
 * @author Ark Information Systems
 *
 */
public class EditCategoryForm extends AbstractEditForm {

	/** 引数の区分ＩＤ */
	public String categoryId;

	/** 削除されたレコード（CSV）*/
	public String deletedDataId;

	/** ベースとなる区分マスタ */
	public Category category;

	/** 区分データのリスト */
	public List<CategoryDto> categoryTrnList;

	/**
	 * フォームを初期化します.<BR>
	 * 未使用です.
	 */
	@Override
	public void initialize() {
		// 削除がないので実装なし
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		int categoryCodeSize = category.categoryCodeSize.intValue();
		int categoryStrSize = category.categoryStrSize.intValue();

		String labelCategoryCode = MessageResourcesUtil.getMessage("labels.master.categoryCode");
		String labelCategoryCodeName = MessageResourcesUtil.getMessage("labels.master.categoryCodeName");
		String labelCategoryTitle = category.categoryTitle;

		int index = 1;
		if (categoryTrnList != null) {
			Map<String, CategoryDto> doubleCheck = new HashMap<String, CategoryDto>();
			for (CategoryDto dto : categoryTrnList) {
				//
				// 基本情報チェック
				//

				// 区分コード
				if (checkRequired(index, dto.categoryCode, labelCategoryCode, errors)) {

					//半角英数字
					if(isHalfWidthAlphanumeric(index, dto.categoryCode, categoryCodeSize, labelCategoryCode, errors)){
					
						// 区分コード　可変
						checkMaxLength(index, dto.categoryCode, categoryCodeSize, labelCategoryCode, errors);
					}
				}

				// 区分コード名称
				if (checkRequired(index, dto.categoryCodeName, labelCategoryCodeName, errors)) {

					// 区分コード名　60文字
					checkMaxLength(index, dto.categoryCodeName, 60, labelCategoryCodeName, errors);
				}

				//
				// タイプ別チェック
				//
				if (Constants.CATEGORY_DATA_TYPE.CATEGORY_DATA_TYPE_STRING.equals(category.categoryDataType)) {
					// 文字列

					// 必須チェック
					if (checkRequired(index, dto.categoryStr, labelCategoryTitle, errors)) {

						// 長さチェック　可変
						checkMaxLength(index, dto.categoryStr, categoryStrSize, labelCategoryTitle, errors);
					}

				} else if (Constants.CATEGORY_DATA_TYPE.CATEGORY_DATA_TYPE_NUMBER.equals(category.categoryDataType)) {
					// 整数

					// 必須チェック
					if (checkRequired(index, dto.categoryNum, labelCategoryTitle, errors)) {
						// 型チェック
						checkInteger(index, dto.categoryNum, labelCategoryTitle, errors);
					}
				} else if (Constants.CATEGORY_DATA_TYPE.CATEGORY_DATA_TYPE_FLOAT.equals(category.categoryDataType)) {
					// 小数

					// 必須チェック
					if (checkRequired(index, dto.categoryFlt, labelCategoryTitle, errors)) {
						// 型チェック
						checkFloat(index, dto.categoryFlt, labelCategoryTitle, errors);
					}
				}

				// 区分コード重複チェック
				if (doubleCheck.containsKey(dto.categoryCode) && StringUtil.hasLength(dto.categoryCode)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.doubleCategoryCode"));
					break;
				}
				doubleCheck.put(dto.categoryCode, dto);
				index++;
			}
		}
		return errors;
	}

	/**
	 * 区分データリストを初期化します.
	 */
	public void reset() {
		if (this.categoryTrnList == null) {
			this.categoryTrnList = new ArrayList<CategoryDto>();
		}
	}
}
