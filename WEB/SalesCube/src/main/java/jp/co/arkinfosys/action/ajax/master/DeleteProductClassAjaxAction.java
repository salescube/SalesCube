/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.master.ProductClassDto;
import jp.co.arkinfosys.entity.ProductClass;
import jp.co.arkinfosys.form.ajax.master.DeleteProductClassAjaxForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.ProductClassService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 *
 * 分類マスタ画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteProductClassAjaxAction extends
		AbstractDeleteAjaxAction<ProductClassDto, ProductClass> {

	@ActionForm
	@Resource
	public DeleteProductClassAjaxForm deleteProductClassAjaxForm;

	@Resource
	public ProductClassService productClassService;

	/**
	 * 削除前のレコードのチェックを行います.
	 * @return 表示するメッセージ
	 */
	@Override
	protected ActionMessages checkRecord() throws Exception {
		ActionMessages messages = new ActionMessages();

		// 子分類の有無をチェック
		BeanMap params = Beans.createAndCopy(BeanMap.class,
				this.deleteProductClassAjaxForm).excludesWhitespace().execute();

		int count = this.productClassService.countByCondition(params);
		if (count > 1) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.productclass.childexists"));
		}
		return messages;
	}

	/**
	 * 削除レコードを識別する情報を持った分類マスタDTOを返します.
	 * @return {@link ProductClassDto}
	 */
	@Override
	protected ProductClassDto getIdentifiedDto() {
		return Beans.createAndCopy(ProductClassDto.class,
				this.deleteProductClassAjaxForm).execute();
	}

	/**
	 * 削除処理を行う分類マスタサービスを返します.
	 * @return {@link ProductClassService}
	 */
	@Override
	protected AbstractMasterEditService<ProductClassDto, ProductClass> getService() {
		return this.productClassService;
	}
}
