/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.common.MessageResourcesUtil;
import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.entity.join.RackJoin;
import jp.co.arkinfosys.entity.join.WarehouseJoin;
import jp.co.arkinfosys.form.ajax.master.DeleteWarehouseAjaxForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.WarehouseService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

public class DeleteWarehouseAjaxAction extends
		AbstractDeleteAjaxAction<WarehouseDto, WarehouseJoin> {

	@ActionForm
	@Resource
	public DeleteWarehouseAjaxForm deleteWarehouseAjaxForm;

	@Resource
	public WarehouseService warehouseService;

	@Resource
	public RackService rackService;

	@Override
	protected AbstractMasterEditService<WarehouseDto, WarehouseJoin> getService() {
		return this.warehouseService;
	}

	/**
	 * 倉庫の削除時に関連のある棚を更新します.<br>
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String deleteRack() throws Exception {
		try {
			ActionMessages messages = this.checkRecord();
			if (messages.size() > 0) {
				ActionMessagesUtil.addErrors(super.httpRequest, messages);
				return Mapping.ERROR_JSP;
			}

			rackService.updateAudit(this.getIdentifiedDto().getKeys());

			List<RackJoin> rackJoins = rackService.findByWarehouseId(this
					.getIdentifiedDto().warehouseCode);
			for (RackJoin rackJoin : rackJoins) {
				// 関連データの存在チェック
				Map<String, Object> result = this.rackService
						.countRelations(rackJoin.rackCode);

				Iterator<Entry<String, Object>> ite = result.entrySet()
						.iterator();
				while (ite.hasNext()) {
					Entry<String, Object> entry = ite.next();
					Number num = (Number) entry.getValue();
					if (num != null && num.longValue() > 0) {
						super.messages
								.add(ActionMessages.GLOBAL_MESSAGE,
										new ActionMessage(
												"errors.db.delete.relation",
												"棚番コード["
														+ rackJoin.rackCode
														+ "] は、"
														+ MessageResourcesUtil.getMessage("erroes.db."
																+ entry.getKey())));
					}
				}
			}

			// エラーを記憶
			if (super.messages.size() > 0) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.delete.warehouseRelRack"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

				return Mapping.ERROR_JSP;
			}
			// 削除/更新処理
			rackService.controlRackWithWarehouse(this.getIdentifiedDto(), true);

		} catch (UnabledLockException e) {
			super.errorLog(e);

			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					e.getKey()));
			ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}
		return delete();
	}

	/**
	 * 倉庫の削除時に関連のある棚を更新します.<br>
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String updateRack() throws Exception {
		try {
			ActionMessages messages = this.checkRecord();
			if (messages.size() > 0) {
				ActionMessagesUtil.addErrors(super.httpRequest, messages);
				return Mapping.ERROR_JSP;
			}

			rackService.updateAudit(this.getIdentifiedDto().getKeys());

			// 削除/更新処理
			rackService
					.controlRackWithWarehouse(this.getIdentifiedDto(), false);
		} catch (UnabledLockException e) {
			super.errorLog(e);

			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					e.getKey()));
			ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}
		return delete();
	}

	@Override
	protected WarehouseDto getIdentifiedDto() {
		return Beans.createAndCopy(WarehouseDto.class,
				this.deleteWarehouseAjaxForm).execute();
	}

}
