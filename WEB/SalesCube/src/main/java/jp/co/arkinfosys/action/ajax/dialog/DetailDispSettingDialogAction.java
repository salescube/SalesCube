/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.ajax.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.CommonAjaxResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.DetailDispItemDto;
import jp.co.arkinfosys.form.ajax.dialog.DetailDispSettingDialogForm;
import jp.co.arkinfosys.service.DetailDispItemService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;
import net.arnx.jsonic.JSON;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 検索結果設定ダイアログの表示・更新処理アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DetailDispSettingDialogAction extends AbstractDialogAction {

	/**
	 * アクションフォームです.
	 */
	@ActionForm
	@Resource
	public DetailDispSettingDialogForm detailDispSettingDialogForm;

	/**
	 * 明細表示項目マスタに対するサービスクラスです.
	 */
	@Resource
	private DetailDispItemService detailDispItemService;

	/**
	 * 画面に対応する表示項目を明細表示項目マスタと明細表示項目設定マスタから取得して画面の初期表示設定を行います.
	 *
	 * @throws ServiceException 例外発生時
	 */
	@Override
	protected void createList() throws ServiceException {
		
		List<DetailDispItemDto> userCfgList = this.detailDispItemService
				.findDetailDispItemCfgByCondition(
						this.detailDispSettingDialogForm.menuId,
						this.detailDispSettingDialogForm.target);

		
		boolean existsUserCfg = false;
		Map<String, Object> userCfgMap = new HashMap<String, Object>();
		if (userCfgList.size() > 0) {
			existsUserCfg = true;

			for (int i = 0; i < userCfgList.size(); i++) {
				DetailDispItemDto cfgItem = userCfgList.get(i);
				if (i == 0) {
					
					this.detailDispSettingDialogForm.lockItemId = cfgItem.itemId;
					this.detailDispSettingDialogForm.lockUpdDatetm = cfgItem.updDatetm;
				}

				LabelValueBean bean = new LabelValueBean();
				bean.setLabel(cfgItem.getPrefixItemName());
				bean.setValue(cfgItem.itemId);
				this.detailDispSettingDialogForm.enabledItemList.add(bean);

				
				userCfgMap.put(bean.getValue(), bean);
			}
		}

		
		List<DetailDispItemDto> defaultList = this.detailDispItemService
				.findDetailDispItemByCondition(
						this.detailDispSettingDialogForm.menuId,
						this.detailDispSettingDialogForm.target, false);

		
		List<LabelValueBean> requiredList = new ArrayList<LabelValueBean>();
		for (DetailDispItemDto defaultItem : defaultList) {
			LabelValueBean bean = new LabelValueBean();
			bean.setLabel(defaultItem.getPrefixItemName());
			bean.setValue(defaultItem.itemId);

			
			if (Constants.FLAG.ON.equals(defaultItem.esslFlag)) {
				requiredList.add(bean);
			}

			
			if (!existsUserCfg) {
				
				if (Constants.FLAG.ON.equals(defaultItem.dispFlag)) {
					this.detailDispSettingDialogForm.enabledItemList.add(bean);
				} else if (Constants.FLAG.OFF.equals(defaultItem.dispFlag)) {
					this.detailDispSettingDialogForm.disabledItemList.add(bean);
				}
				continue;
			}

			
			if (!userCfgMap.containsKey(defaultItem.itemId)) {
				
				this.detailDispSettingDialogForm.disabledItemList.add(bean);
			}
		}

		
		if (Constants.SEARCH_TARGET.VALUE_SLIP
				.equals(this.detailDispSettingDialogForm.target)) {
			this.detailDispSettingDialogForm.targetName = Constants.SEARCH_TARGET.LABEL_SLIP;
		} else if (Constants.SEARCH_TARGET.VALUE_LINE
				.equals(this.detailDispSettingDialogForm.target)) {
			this.detailDispSettingDialogForm.targetName = Constants.SEARCH_TARGET.LABEL_LINE;
		}

		
		this.detailDispSettingDialogForm.originalEnabledItemList = JSON
				.encode(this.detailDispSettingDialogForm.enabledItemList);
		
		this.detailDispSettingDialogForm.originalDisabledItemList = JSON
				.encode(this.detailDispSettingDialogForm.disabledItemList);
		
		this.detailDispSettingDialogForm.requiredDispItemIdList = JSON
				.encode(requiredList);
	}

	/**
	 * 検索結果表示項目設定マスタに設定内容を保存するメソッドです.
	 *
	 * @return null
	 * @throws Exception 例外発生時
	 */
	@Execute(validator = true, validate = "validate", urlPattern = "update/{dialogId}", input = CommonAjaxResources.Mapping.ERROR_JSP)
	public String update() throws Exception {
		try {
			this.detailDispItemService.updateDetailDispItemCfg(
					this.detailDispSettingDialogForm.menuId,
					this.detailDispSettingDialogForm.target,
					this.detailDispSettingDialogForm.enabledDispItemList,
					this.detailDispSettingDialogForm.lockItemId,
					this.detailDispSettingDialogForm.lockUpdDatetm);
		} catch (UnabledLockException e) {
			super.errorLog(e);

			
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e
					.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, errors);

			return CommonAjaxResources.Mapping.ERROR_JSP;
		} catch (ServiceException e) {
			super.errorLog(e);

			
			super.writeSystemErrorToResponse();
		}
		return null;
	}
}
