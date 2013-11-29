/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.entity.join.RackJoin;
import jp.co.arkinfosys.form.ajax.master.DeleteRackAjaxForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.RackService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 棚番マスタ画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteRackAjaxAction extends
		AbstractDeleteAjaxAction<RackDto, RackJoin> {

	@ActionForm
	@Resource
	public DeleteRackAjaxForm deleteRackAjaxForm;

	@Resource
	public RackService rackService;

	/**
	 * 削除前のレコードのチェックを行います.
	 * @return 表示するメッセージ
	 */
	@Override
	protected ActionMessages checkRecord() throws Exception {
		ActionMessages messages = new ActionMessages();

		// 関連データの存在チェック
		Map<String, Object> result = this.rackService
				.countRelations(this.deleteRackAjaxForm.rackCode);

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
	 * 削除レコードを識別する情報を持った棚番マスタDTOを返します.
	 * @return {@link RackDto}
	 */
	@Override
	protected RackDto getIdentifiedDto() {
		return Beans.createAndCopy(RackDto.class, this.deleteRackAjaxForm)
				.execute();
	}

	/**
	 * 削除処理を行う棚番マスタサービスを返します.
	 * @return {@link RackService}
	 */
	@Override
	protected AbstractMasterEditService<RackDto, RackJoin> getService() {
		return this.rackService;
	}
}
