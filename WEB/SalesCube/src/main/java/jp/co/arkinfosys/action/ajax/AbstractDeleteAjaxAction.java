/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import jp.co.arkinfosys.dto.master.MasterEditDto;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * Ajaxによる削除処理の基底アクションクラスです.
 *
 * @author Ark Information Systems
 *
 * @param <DTOCLASS>
 * @param <ENTITY>
 */
public abstract class AbstractDeleteAjaxAction<DTOCLASS extends MasterEditDto, ENTITY> extends
		CommonAjaxResources {

	/**
	 * 削除処理を実行します.<br>
	 * <p>
	 * 以下の処理を実行します.<br>
	 * 1. 前処理.<br>
	 * 2. レコードチェック.<br>
	 * 3. 削除情報を更新.<br>
	 * 4. レコード削除.<br>
	 * 5. 後処理.
	 * @return 遷移先URI(エラー時)
	 * @throws Exception
	 */
	@Execute(validator = true, input = Mapping.ERROR_JSP)
	public String delete() throws Exception {
		try {
			this.doBeforeDelete();

			ActionMessages messages = this.checkRecord();
			if (messages.size() > 0) {
				ActionMessagesUtil.addErrors(super.httpRequest, messages);
				return Mapping.ERROR_JSP;
			}

			this.getService().updateAudit(this.getIdentifiedDto().getKeys());

			this.getService().deleteRecord(this.getIdentifiedDto());

			this.doAfterDelete();
		} catch (UnabledLockException e) {
			super.errorLog(e);

			this.doAfterLockFailure(e);

			return Mapping.ERROR_JSP;
		} catch (ServiceException e) {
			super.errorLog(e);

			this.doAfterError(e);

			return null;
		}
		return null;
	}

	/**
	 * 削除処理を行うサービスクラスを返します.
	 *
	 * @return 削除処理を行うサービスクラス
	 */
	protected abstract AbstractMasterEditService<DTOCLASS, ENTITY> getService();

	/**
	 * 削除レコードを識別する情報を持ったDTOクラスを返します.
	 *
	 * @return DTOクラス
	 */
	protected abstract DTOCLASS getIdentifiedDto();

	/**
	 * 削除前の処理を行います.<BR>
	 * デフォルト実装では何も処理しません.<BR>
	 * 必要に応じてオーバーライドしてください.
	 * @throws Exception
	 */
	protected void doBeforeDelete() throws Exception {
	}

	/**
	 * 削除前にレコードのチェック処理を行います.<BR>
	 * デフォルト実装では空のActionMessagesを返します.<BR>
	 * 必要に応じてオーバーライドしてください.
	 *
	 * @return ActionMessages
	 * @throws Exception
	 */
	protected ActionMessages checkRecord() throws Exception {
		return new ActionMessages();
	}

	/**
	 * 削除後の処理を行います.<BR>
	 * デフォルト実装では何も処理しません.<BR>
	 * 必要に応じてオーバーライドしてください.
	 *
	 * @throws Exception
	 */
	protected void doAfterDelete() throws Exception {
	}

	/**
	 * レコードのロックに失敗した場合の処理を行います.<BR>
	 * デフォルト実装ではエラーメッセージを設定します.<BR>
	 * 必要に応じてオーバーライドしてください.
	 * @param e 発生したException
	 * @throws Exception
	 */
	protected void doAfterLockFailure(UnabledLockException e) throws Exception {
		super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e
				.getKey()));
		ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
	}

	/**
	 * エラー発生時の処理を行います.<BR>
	 * デフォルト実装ではエラーレスポンスオブジェクトを構築します.<BR>
	 * 必要に応じてオーバーライドしてください.
	 *
	 * @param e 発生したException
	 * @throws Exception
	 */
	protected void doAfterError(Exception e) throws Exception {
		super.writeSystemErrorToResponse();
	}

}
