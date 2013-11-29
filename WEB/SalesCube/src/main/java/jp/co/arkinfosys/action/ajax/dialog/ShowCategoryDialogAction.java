/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.dialog;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.dto.master.CategoryDto;
import jp.co.arkinfosys.entity.join.CategoryJoin;
import jp.co.arkinfosys.form.ajax.dialog.ShowCategoryDialogForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 * 区分情報ダイアログを表示するアクションクラスです.<BR>
 * 見積入力画面で「納期または出荷日」を選択するために利用されます.
 *
 * @author Ark Information Systems
 *
 */
public class ShowCategoryDialogAction extends AbstractDialogAction {

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public ShowCategoryDialogForm showCategoryDialogForm;

	/**
	 * 区分マスタに対応するサービスクラスです.
	 */
	@Resource
	public CategoryService categoryService;

	/**
	 * 画面の選択項目を初期化します.
	 *
	 * @throws ServiceException サービス例外発生時
	 */
	@Override
	protected void createList() throws ServiceException {
		List<CategoryJoin> categoryDataList = this.categoryService
				.findCategoryJoinById(Integer
						.parseInt(this.showCategoryDialogForm.categoryId));
		showCategoryDialogForm.categoryDtoList = new ArrayList<CategoryDto>();
		for (CategoryJoin entity : categoryDataList) {
			CategoryDto dto = new CategoryDto();
			Beans.copy(entity, dto).execute();
			showCategoryDialogForm.categoryDtoList.add(dto);
		}
	}

}
