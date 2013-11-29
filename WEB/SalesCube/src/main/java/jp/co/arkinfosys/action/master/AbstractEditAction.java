/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.MasterEditDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * マスタ編集画面の基底アクションクラスです.
 * @author Ark Information Systems
 *
 * @param <DTOCLASS>
 * @param <ENTITY>
 */
public abstract class AbstractEditAction<DTOCLASS extends MasterEditDto, ENTITY> extends CommonResources {

	/**
	 * 初期化処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. アクションフォームに編集可否を設定<br>
	 * 2. マスタ情報を読み込む<br>
	 * 3. 取得した情報をアクションフォームに設定<br>
	 * 4. プルダウン要素を作成
	 * </p>
	 * @param key 取得するマスタデータのキー値
	 * @throws ServiceException
	 */
	protected void init(String key) throws ServiceException {

		AbstractEditForm form = getActionForm();
		form.isUpdate = super.userDto.isMenuUpdate(getMenuId());

		AuditInfo record = loadData(key);
		if (record != null) {
			setForm(record);
			
			form.creDatetmShow = this.getActionForm().creDatetm;
			form.updDatetmShow = this.getActionForm().updDatetm;
			
			//form.creDatetmShow = StringUtil.getDateString(Constants.FORMAT.DATE, record.creDatetm);
			//form.updDatetmShow = StringUtil.getDateString(Constants.FORMAT.DATE, record.updDatetm);
		}
		initList();
	}

	/**
	 * プルダウン要素を作成します.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @throws ServiceException
	 */
	protected void initList() throws ServiceException {
		// デフォルトでは何もしない
	}

	/**
	 * アクションフォームにマスタ情報を設定します.
	 * @param record 設定するマスタ情報
	 * @throws ServiceException
	 */
	protected void setForm(AuditInfo record) throws ServiceException {
		if (record == null) {
			return;
		}

		Beans.copy(record, this.getActionForm()).timestampConverter(
				Constants.FORMAT.TIMESTAMP)
				.dateConverter(Constants.FORMAT.DATE).execute();
	}

	/**
	 * マスタ情報を読み込みます.
	 * @param key 取得するマスタデータのキー値
	 * @return マスタ情報
	 * @throws ServiceException
	 */
	protected abstract AuditInfo loadData(String key) throws ServiceException;

	/**
	 * アクションフォームを返します.
	 * @return アクションフォーム
	 */
	protected abstract AbstractEditForm getActionForm();

	/**
	 * メニューIDを返します.
	 * @return メニューID
	 */
	protected abstract String getMenuId();

	/**
	 * 画面遷移先のURI文字列を返します.
	 * @return 画面遷移先
	 */
	protected abstract String getInputURL();

	/**
	 * キー値を返します.
	 * @return キー値
	 */
	protected abstract String getKey();

	/**
	 * キー値重複時のメッセージを定義するプロパティキーを返します.
	 * @return プロパティキー文字列
	 */
	protected abstract String getAlreadyExistsErrorKey();

	/**
	 * マスタ編集サービスを返します.
	 * @return マスタ編集サービス
	 */
	protected abstract AbstractMasterEditService<DTOCLASS, ENTITY> getService();

	/**
	 * マスタのDTOクラスを返します.
	 * @return マスタのDTOクラス
	 */
	protected abstract Class<DTOCLASS> getDtoClass();

	/**
	 * {@link AbstractEditAction#doInsert()}の登録後に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @param dto マスタのDTO
	 * @throws Exception
	 */
	protected void doInsertAfter(DTOCLASS dto) throws Exception {
		// デフォルト実装は何もしない
	}

	/**
	 * {@link AbstractEditAction#doUpdate()}の更新後に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @param dto マスタのDTO
	 * @throws Exception
	 */
	protected void doUpdateAfter(DTOCLASS dto) throws Exception {
		// デフォルト実装は何もしない
	}

	/**
	 * {@link AbstractEditAction#doDelete()}の削除後に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @param dto マスタのDTO
	 * @throws Exception
	 */
	protected void doDeleteAfter(DTOCLASS dto) throws Exception {
		// デフォルト実装は何もしない
	}

	/**
	 * 編集モードで初期化を行います.<br>
	 * 処理実行後、{@link AbstractEditAction#getInputURL()}で取得したURIに遷移します.
	 * @param key 取得するマスタデータのキー値
	 * @return 画面遷移先のURI文字列
	 * @throws ServiceException
	 */
	protected String doEdit(String key) throws ServiceException {
		try {
			getActionForm().editMode = true;
			this.init(key);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		return getInputURL();
	}

	/**
	 * 登録処理を行います.<br>
	 * 何かしらの問題があった場合、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link AbstractEditAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 * @throws ServiceException
	 */
	protected String doInsert() throws Exception, ServiceException {
		try {
			AuditInfo record = loadData(getKey());
			if (record != null) {
				this.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage(getAlreadyExistsErrorKey()));
				ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
				initList();
				return getInputURL();
			}

			DTOCLASS dto = Beans.createAndCopy(getDtoClass(), getActionForm()).execute();

			// 登録
			// 割引マスタ
			this.getService().insertRecord(dto);

			doInsertAfter(dto);

			// 編集モード
			this.getActionForm().editMode = true;

			init(getKey());

			// メッセージ設定
			this.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.insert"));
			ActionMessagesUtil.addMessages(this.httpRequest, this.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		return getInputURL();
	}

	/**
	 * 更新処理を行います.<br>
	 * 更新完了時およびロック失敗時、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link AbstractEditAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 * @throws ServiceException
	 */
	protected String doUpdate() throws Exception, ServiceException {
		try {
			AuditInfo record = loadData(getKey());
			if (record == null) {
				this.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dataNotFound"));
				ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
				initList();
				return getInputURL();
			}

			DTOCLASS dto = this.createDtoFromActionForm();

			// 更新
			this.getService().updateRecord(dto);

			this.doUpdateAfter(dto);

			getActionForm().editMode = true;
			this.init(getKey());

			// メッセージ設定
			this.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.update"));
			ActionMessagesUtil.addMessages(this.httpRequest, this.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		} catch (UnabledLockException e) {
			super.errorLog(e);

			// ロックエラー
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e
					.getKey()));
			ActionMessagesUtil.addErrors(this.httpRequest, errors);
			initList();
			getActionForm().editMode = true;
		}
		return getInputURL();
	}

	/**
	 * アクションフォームからDTOCLASSへの値コピーを行います.
	 * @return 値コピー後のDTOCLASS
	 */
	protected DTOCLASS createDtoFromActionForm() {
		return Beans.createAndCopy(this.getDtoClass(), this.getActionForm()).execute();
	}

	/**
	 * 削除処理を行います.<br>
	 * 削除完了時およびロック失敗時、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link AbstractEditAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	protected String doDelete() throws Exception {
		try {

			AuditInfo record = loadData(getKey());
			if (record == null) {
				this.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dataNotFound"));
				ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
				initList();
				return getInputURL();
			}

			DTOCLASS dto = Beans.createAndCopy(getDtoClass(), getActionForm()).execute();

			this.getService().updateAudit(dto.getKeys());

			// 削除処理
			this.getService().deleteRecord(dto);

			this.doDeleteAfter(dto);

			// 新規モード
			this.getActionForm().initialize();
			init(null);
			this.getActionForm().editMode = false;

			// メッセージ設定
			this.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.delete"));
			ActionMessagesUtil.addMessages(this.httpRequest, this.messages);
		} catch (UnabledLockException e) {
			super.errorLog(e);

			this.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(e.getKey()));
			ActionMessagesUtil.addErrors(this.httpRequest, this.messages);
			initList();
		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}
		return getInputURL();
	}
}
