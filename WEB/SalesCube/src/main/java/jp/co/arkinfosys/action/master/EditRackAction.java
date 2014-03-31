/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.Warehouse;
import jp.co.arkinfosys.entity.join.RackJoin;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditRackForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.WarehouseService;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 棚番編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditRackAction extends AbstractEditAction<RackDto, RackJoin> {

	@ActionForm
	@Resource
	public EditRackForm editRackForm;

	@Resource
	public RackService rackService;

	@Resource
	public CategoryService categoryService;

	@Resource
	public WarehouseService warehouseService;

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	private static class Mapping {
		public static final String INPUT = "editRack.jsp";
	}

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditRackAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		initList();
		super.init(null);
		return getInputURL();
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditRackAction#doEdit(String) doEdit()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{rackCode}")
	public String edit() throws Exception {
		this.editRackForm.rackCode = StringUtil.decodeSL(this.editRackForm.rackCode);
		return doEdit(getKey());
	}

	/**
	 * 登録処理を行います.<br>
	 * 処理終了後、{@link EditRackAction#doInsert()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "index", stopOnValidationError = false)
	public String insert() throws Exception {
		initList();

		if(this.editRackForm.warehouseCode != ""){
			//存在しない倉庫の場合エラーとする
			Warehouse warehouse = this.warehouseService.findById(this.editRackForm.warehouseCode);
			if (warehouse == null) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.warehouse.not.exist"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return EditRackAction.Mapping.INPUT;
			}
		}
		return doInsert();
	}

	/**
	 * 更新処理を行います.<br>
	 * 処理終了後、{@link EditRackAction#update()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "initEdit", stopOnValidationError = false)
	public String update() throws Exception {

		if(this.editRackForm.warehouseCode != ""){
			//存在しない倉庫の場合エラーとする
			Warehouse warehouse = this.warehouseService.findById(this.editRackForm.warehouseCode);
			if (warehouse == null) {
				this.editRackForm.editMode = true;
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.warehouse.not.exist"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return EditRackAction.Mapping.INPUT;
			}
		}
		return doUpdate();
	}

	/**
	 * 削除処理を行います.<br>
	 * 何かしらの問題があった場合、画面にメッセージを表示します.<br>
	 * 処理終了後、{@link EditRackAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		// 関連データの存在チェック
		Map<String, Object> result = this.rackService
				.countRelations(this.editRackForm.rackCode);

		Iterator<Entry<String, Object>> ite = result.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, Object> entry = ite.next();
			Number num = (Number) entry.getValue();
			if (num != null && num.longValue() > 0) {
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.db.delete.relation",
								MessageResourcesUtil
										.getMessage("erroes.db."
												+ entry.getKey())));
			}
		}
		if(super.messages.size() > 0) {
			ActionMessagesUtil.addErrors(super.httpRequest,
					super.messages);
			initList();
			this.editRackForm.editMode = true;
			return getInputURL();
		}
		return doDelete();

	}

	/**
	 *
	 * @return {@link EditRackForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editRackForm;
	}

	/**
	 *
	 * @return　プロパティキー文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return "errors.rack.already.exists";
	}

	/**
	 *
	 * @return {@link RackDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<RackDto> getDtoClass() {
		return RackDto.class;
	}

	/**
	 *
	 * @return {@link Mapping#INPUT}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getInputURL()
	 */
	@Override
	protected String getInputURL() {
		return Mapping.INPUT;
	}

	/**
	 *
	 * @return 棚番コード
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editRackForm.rackCode;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_RACK}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_RACK;
	}

	/**
	 *
	 * @return {@link RackService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<RackDto, RackJoin> getService() {
		return rackService;
	}

	/**
	 *
	 * @param key 棚番コード
	 * @return {@link RackJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		RackJoin result = rackService.findById(key);
		return result;
	}

	/**
	 *
	 * @param record {@link RackJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#setForm(jp.co.arkinfosys.entity.AuditInfo)
	 */
	@Override
	protected void setForm(AuditInfo record) throws ServiceException {
		if (record == null) {
			return;
		}

		Beans.copy(record, this.editRackForm).timestampConverter(
				Constants.FORMAT.TIMESTAMP)
				.dateConverter(Constants.FORMAT.DATE).execute();
	}

	/**
	 * バリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理終了後、{@link EditRackAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws ServiceException
	 */
	@Execute(validator = false)
	public String initEdit() throws ServiceException {
		this.editRackForm.editMode = true;
		initList();
		return getInputURL();
	}

}
