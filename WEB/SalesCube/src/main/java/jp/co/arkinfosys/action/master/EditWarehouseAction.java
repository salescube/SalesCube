/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.common.MessageResourcesUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.RackDto;
import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.join.RackJoin;
import jp.co.arkinfosys.entity.join.WarehouseJoin;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.DeleteRackForm;
import jp.co.arkinfosys.form.master.EditRackForm;
import jp.co.arkinfosys.form.master.EditWarehouseForm;
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

/**
 * 倉庫編集画面のアクションクラスです.
 * 
 * @author Ark Information Systems
 * 
 */
public class EditWarehouseAction extends
		AbstractEditAction<WarehouseDto, WarehouseJoin> {

	@ActionForm
	@Resource
	public EditWarehouseForm editWarehouseForm;

	@Resource
	public WarehouseService warehouseService;

	@Resource
	public RackService rackService;

	/**
	 * 画面遷移用のマッピングクラスです.
	 * 
	 * @author Ark Information Systems
	 * 
	 */
	private static class Mapping {
		public static final String INPUT = "editWarehouse.jsp";
	}

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理終了後、{@link EditWarehouseAction#getInputURL()}で取得したURIに遷移します.
	 * 
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
	 * 処理終了後、{@link EditWarehouseAction#doEdit(String) doEdit()}で取得したURIに遷移します.
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{warehouseCode}")
	public String edit() throws Exception {
		this.editWarehouseForm.warehouseCode = StringUtil
				.decodeSL(this.editWarehouseForm.warehouseCode);
		return doEdit(getKey());
	}

	/**
	 * 登録処理を行います.<br>
	 * 処理終了後、{@link EditWarehouseAction#doInsert()}で取得したURIに遷移します.
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "index", stopOnValidationError = false)
	public String insert() throws Exception {
		initList();
		
		// 棚番のpkey違反を調べる
		int index = 0;
		for (EditRackForm editRackForm : editWarehouseForm.editRackList) {
			index++;
			if(!editRackForm.exist) {
				RackJoin record = rackService.findById(editRackForm.rackCode);
				if (record != null) {
					super.messages.add(ActionMessages.GLOBAL_MESSAGE, 
							new ActionMessage("errors.line.rack.already.exists", index));
				}
			}
		}
		// エラーがあったら
		if(super.messages.size() > 0) {
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			super.messages.clear();
			return getInputURL();
		}
		
		return doInsert();
	}
	
	@Override
	public void doInsertAfter(WarehouseDto dto) throws Exception{
		// 棚番登録
		for (EditRackForm editRackForm : editWarehouseForm.editRackList) {
			RackDto rackDto = Beans.createAndCopy(RackDto.class, editRackForm).execute();
			if(editRackForm.exist) {
				rackService.updateRecord(rackDto);
			}else {
				rackService.insertRecord(rackDto);
			}
		}
		
	}

	/**
	 * 更新処理を行います.<br>
	 * 処理終了後、{@link EditWarehouseAction#update()}で取得したURIに遷移します.
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "initEdit", stopOnValidationError = false)
	public String update() throws Exception {
		// 棚番のpkey違反を調べる
		int index = 0;
		for (EditRackForm editRackForm : editWarehouseForm.editRackList) {
			index++;
			if(!editRackForm.exist) {
				RackJoin record = rackService.findById(editRackForm.rackCode);
				if (record != null) {
					super.messages.add(ActionMessages.GLOBAL_MESSAGE, 
							new ActionMessage("errors.line.rack.already.exists", index));
				}
			}
		}
		// エラーがあったら
		if(super.messages.size() > 0) {
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			super.messages.clear();
			editWarehouseForm.editMode = true;
			return getInputURL();
		}
		
		return doUpdate();
	}
	
	/**
	 * 倉庫更新の後に棚番の更新を行う
	 * 
	 */
	@Override
	protected void doUpdateAfter(WarehouseDto dto) throws Exception {
		// 棚番削除
		for (DeleteRackForm deleteRackForm : editWarehouseForm.rackCodesHist) {
			RackDto rackDto = Beans.createAndCopy(RackDto.class, deleteRackForm).execute();
			
			// 関連データの存在チェック
			Map<String, Object> result = this.rackService
					.countRelations(deleteRackForm.rackCode);

			Iterator<Entry<String, Object>> ite = result.entrySet().iterator();
			while (ite.hasNext()) {
				Entry<String, Object> entry = ite.next();
				Number num = (Number) entry.getValue();
				if (num != null && num.longValue() > 0) {
					super.messages.add(
							ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.db.delete.relation",
									"棚番コード["
											+ deleteRackForm.rackCode
											+ "] は、"
											+ MessageResourcesUtil
													.getMessage("erroes.db."
															+ entry.getKey())));
				} else {
					rackService.deleteRecord(rackDto);
				}
			}
		}
		// 棚番登録
		for (EditRackForm editRackForm : editWarehouseForm.editRackList) {
			RackDto rackDto = Beans.createAndCopy(RackDto.class, editRackForm).execute();
			if(editRackForm.exist) {
				rackService.updateRecord(rackDto);
			}else {
				rackService.insertRecord(rackDto);
			}
		}

		// エラーを記憶
		if (super.messages.size() > 0) {

			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			super.messages.clear();
		}
	}

	/**
	 * 倉庫の削除時に関連のある棚を削除します.<br>
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String deleteRack() throws Exception {
		List<RackJoin> rackJoins = rackService
				.findByWarehouseId(editWarehouseForm.warehouseCode);
		for (RackJoin rackJoin : rackJoins) {
			// 関連データの存在チェック
			Map<String, Object> result = this.rackService
					.countRelations(rackJoin.rackCode);

			Iterator<Entry<String, Object>> ite = result.entrySet().iterator();
			while (ite.hasNext()) {
				Entry<String, Object> entry = ite.next();
				Number num = (Number) entry.getValue();
				if (num != null && num.longValue() > 0) {
					super.messages.add(
							ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.db.delete.relation",
									"棚番コード["
											+ rackJoin.rackCode
											+ "] は、"
											+ MessageResourcesUtil
													.getMessage("erroes.db."
															+ entry.getKey())));
				}
			}
		}

		// エラーを記憶
		if (super.messages.size() > 0) {

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.delete.warehouseRelRack"));

			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			super.messages.clear();

			editWarehouseForm.editMode = true;
			
			return getInputURL();
		}
		AuditInfo record = loadData(getKey());
		if (record == null) {
			return null;
		}
		WarehouseDto dto = Beans.createAndCopy(getDtoClass(), getActionForm())
				.execute();

		// 削除/更新処理
		rackService.controlRackWithWarehouse(dto, true);

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
		String returnText = delete();
		try {
			AuditInfo record = loadData(getKey());
			if (record == null) {
				return null;
			}
			WarehouseDto dto = Beans.createAndCopy(getDtoClass(),
					getActionForm()).execute();

			// 削除/更新処理
			rackService.controlRackWithWarehouse(dto, false);
		} catch (UnabledLockException e) {
			super.errorLog(e);

			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					e.getKey()));
			ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
			initList();
		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}
		return returnText;
	}

	/**
	 * 削除処理を行います.<br>
	 * 何かしらの問題があった場合、画面にメッセージを表示します.<br>
	 * 処理終了後、{@link EditWarehouseAction#getInputURL()}で取得したURIに遷移します.
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		return doDelete();
	}

	/**
	 * 
	 * @return {@link EditWarehouseForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editWarehouseForm;
	}

	/**
	 * 
	 * @return　プロパティキー文字列
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return "errors.warehouse.already.exists";
	}

	/**
	 * 
	 * @return {@link WarehouseDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<WarehouseDto> getDtoClass() {
		return WarehouseDto.class;
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
	 * @return 倉庫コード
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editWarehouseForm.warehouseCode;
	}

	/**
	 * 
	 * @return {@link MENU_ID#MASTER_RACK}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_WAREHOUSE;
	}

	/**
	 * 
	 * @return {@link WarehouseService}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<WarehouseDto, WarehouseJoin> getService() {
		return warehouseService;
	}

	/**
	 * 
	 * @param key
	 *            倉庫コード
	 * @return {@link WarehouseJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		WarehouseJoin result = warehouseService.findById(key);
		if(key != null) {
			List<RackJoin> rackJoinList= rackService.findByWarehouseId(key);
			for (RackJoin join : rackJoinList) {
				EditRackForm editRackForm = Beans.createAndCopy(EditRackForm.class, join).execute();
				DeleteRackForm deleteRackForm = new DeleteRackForm();
				Beans.copy(join, editRackForm).timestampConverter(
						Constants.FORMAT.TIMESTAMP)
						.dateConverter(Constants.FORMAT.DATE).execute();
				editRackForm.exist = true;
				deleteRackForm.rackCode = editRackForm.rackCode;
				deleteRackForm.updDatetm = editRackForm.updDatetm;
				result.rackCodesHist.add(deleteRackForm);
				result.editRackList.add(editRackForm);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param record
	 *            {@link WarehouseJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#setForm(jp.co.arkinfosys.entity.AuditInfo)
	 */
	@Override
	protected void setForm(AuditInfo record) throws ServiceException {
		if (record == null) {
			return;
		}

		Beans.copy(record, this.editWarehouseForm)
				.timestampConverter(Constants.FORMAT.TIMESTAMP)
				.dateConverter(Constants.FORMAT.DATE).execute();
	}

	/**
	 * バリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理終了後、{@link EditWarehouseAction#getInputURL()}で取得したURIに遷移します.
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws ServiceException
	 */
	@Execute(validator = false)
	public String initEdit() throws ServiceException {
		this.editWarehouseForm.editMode = true;
		initList();
		return getInputURL();
	}

}
