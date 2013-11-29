/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.master.CustomerDto;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.form.ajax.master.DeleteCustomerAjaxForm;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.AbstractMasterEditService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 顧客マスタ画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 */
public class DeleteCustomerAjaxAction extends
		AbstractDeleteAjaxAction<CustomerDto, CustomerJoin> {

	@ActionForm
	@Resource
	public DeleteCustomerAjaxForm deleteCustomerAjaxForm;

	@Resource
	public CustomerService customerService;

	/**
	 * 削除前のレコードのチェックを行います.
	 * @return 表示するメッセージ
	 */
	@Override
	protected ActionMessages checkRecord() throws Exception {
		ActionMessages messages = new ActionMessages();

		// 関連データの存在チェック
		Map<String, Object> result = this.customerService
				.countRelations(this.deleteCustomerAjaxForm.customerCode);

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
	 * 削除レコードを識別する情報を持った顧客情報マスタDTOを返します.
	 * @return {@link CustomerDto}
	 */
	@Override
	protected CustomerDto getIdentifiedDto() {
		return Beans.createAndCopy(CustomerDto.class,
				this.deleteCustomerAjaxForm).execute();
	}

	/**
	 * 削除処理を行う顧客サービスを返します.
	 * @return {@link CustomerService}
	 */
	@Override
	protected AbstractMasterEditService<CustomerDto, CustomerJoin> getService() {
		return this.customerService;
	}
}
