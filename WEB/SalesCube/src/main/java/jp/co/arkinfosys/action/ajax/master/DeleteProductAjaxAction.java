/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.master.ProductDto;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.ajax.master.DeleteProductAjaxForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.ProductService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 商品マスタ画面(検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteProductAjaxAction extends
		AbstractDeleteAjaxAction<ProductDto, ProductJoin> {

	@Resource
	protected ProductService productService;

	@ActionForm
	@Resource
	protected DeleteProductAjaxForm deleteProductAjaxForm;

	/**
	 * 削除前のレコードのチェックを行います.
	 * @return 表示するメッセージ
	 */
	@Override
	protected ActionMessages checkRecord() throws Exception {
		ActionMessages messages = new ActionMessages();

		// 関連データの存在チェック
		Map<String, Object> result = this.productService
				.countRelations(this.deleteProductAjaxForm.productCode);

		Iterator<Entry<String, Object>> ite = result.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, Object> entry = ite.next();
			Number num = (Number) entry.getValue();
			if (num != null && num.longValue() > 0) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.db.delete.relation", MessageResourcesUtil
								.getMessage("erroes.db." + entry.getKey())));
			}
		}
		return messages;
	}

	/**
	 * 削除レコードを識別する情報を持った商品マスタDTOを返します.
	 * @return {@link ProductDto}
	 */
	@Override
	protected ProductDto getIdentifiedDto() {
		return Beans
				.createAndCopy(ProductDto.class, this.deleteProductAjaxForm)
				.execute();
	}

	/**
	 * 削除処理を行う商品マスタサービスを返します.
	 * @return {@link ProductService}
	 */
	@Override
	protected AbstractMasterEditService<ProductDto, ProductJoin> getService() {
		return this.productService;
	}

}
