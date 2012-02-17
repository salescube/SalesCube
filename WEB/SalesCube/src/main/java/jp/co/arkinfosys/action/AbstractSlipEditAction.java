/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action;

import java.util.List;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 伝票入力画面の基底アクションクラスです.
 * @author Ark Information Systems
 *
 * @param <DTOCLASS>
 * @param <LINEDTOCLASS>
 */
public abstract class AbstractSlipEditAction<DTOCLASS extends AbstractSlipDto<LINEDTOCLASS>, LINEDTOCLASS extends AbstractLineDto> extends CommonResources {

	/**
	 * 初期表示処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. アクションフォームを初期化<br>
	 * 2. プルダウン要素を作成<br>
	 * 3. DTOクラスを生成し、明細行をアクションフォームに設定<br>
	 * 4. アクションフォームデフォルト値を設定
	 * </p>
	 * 処理実行後、{@link AbstractSlipEditAction#getInputURIString()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator=false)
	public String index() throws Exception {

		AbstractSlipEditForm<LINEDTOCLASS> form = this.getActionForm();
		prepareForm();

		
		form.reset();
		form.initialize();
		form.initializeScreenInfo();

		
		this.createList();

		
		AbstractSlipDto<LINEDTOCLASS> dto = this.createDTO();

		
		dto.fillList();
		form.setLineList(dto.getLineDtoList());

		
		form.setDefaultSelected(dto);

		form.newData = true;

		return this.getInputURIString();
	}

	/**
	 * 伝票を読み込みます.<br>
	 * 伝票情報が存在しない場合、画面にメッセージを表示します.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. アクションフォームを初期化<br>
	 * 2. プルダウン要素を作成<br>
	 * 3. 伝票情報を読み込む<br>
	 * 4. 伝票情報が存在する場合はアクションフォームに取得情報を設定後、後処理<br>
	 *    存在しない場合は新規入力としてアクションフォームに設定
	 * </p>
	 * 伝票情報が存在する場合は{@link AbstractSlipEditAction#getInputURIString()}、存在しない場合は{@link AbstractSlipEditAction#index()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String load() throws Exception {
		AbstractSlipEditForm<LINEDTOCLASS> form = this.getActionForm();
		prepareForm();
		form.initialize();
		this.createList();
		try {
			if( !loadData()){

				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.dataNotFound"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

				form.newData = true;
				form.setKeyValue("");
				
				return index();
			} else {
				form.newData = false;

				
				AbstractSlipDto<LINEDTOCLASS> dto = form.copyToDto();
				dto.setLineDtoList(form.getLineList());
				dto.fillList();
				form.setLineList(dto.getLineDtoList());

				afterLoad();
			}
		} catch (Exception e) {
			e.printStackTrace();

			return index();
		}

		return this.getInputURIString();
	}

	/**
	 * 登録ボタン押下後エラーになった際の初期化処理を行います.<br>
	 * 処理実行後、{@link AbstractSlipEditAction#getInputURIString()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator=false)
	public String errorInit() throws Exception {

		
		AbstractSlipEditForm<LINEDTOCLASS> form = this.getActionForm();
		prepareForm();
		form.initialize();

		
		this.createList();

		return this.getInputURIString();
	}

	/**
	 * 登録または更新処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. アクションフォームからDTOに値をコピー<br>
	 * 2. 保存前処理<br>
	 * 3. 明細リストから未入力の明細を削除<br>
	 * 4. 伝票を保存<br>
	 * 5. 明細を保存<br>
	 * 6. 保存後処理<br>
	 * 7. 登録伝票を読み込む<br>
	 * 8. 読み込み後処理<br>
	 * 9. 明細行を規定行数に補完<br>
	 * 10.登録完了メッセージを表示<br>
	 * </p>
	 * 処理実行後、{@link AbstractSlipEditAction#getInputURIString()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Execute(validator = true, validate="validate, @, validateAtCreateSlip", stopOnValidationError = false, input = "errorInit")
	public String upsert() throws Exception {

		AbstractSlipEditForm<LINEDTOCLASS> form = this.getActionForm();
		prepareForm();

		try {

			
			AbstractSlipDto<LINEDTOCLASS> dto = form.copyToDto();

			
			form.initialize();
			this.createList();

			
			boolean bInsert = form.isNewData();

			
			this.beforeUpsert(bInsert, dto);

			
			dto.copyFrom(form.getLineList());
			dto.removeBlankLine();

			
			AbstractSlipService service = this.getSlipService();
			service.save(dto,this.getAdditionalServiceOnSaveSlip());

			
			form.setKeyValue(dto.getKeyValue());

			
			AbstractLineService lineService = this.getLineService();
			lineService.save(dto, dto.getLineDtoList(), form.deleteLineIds,this.getAdditionalServiceOnSaveSlip());

			
			form.deleteLineIds = "";

			
			this.afterUpsert(bInsert, dto);

			
			this.loadData();
			this.afterLoad();

			
			dto.setLineDtoList(form.getLineList());
			dto.fillList();
			form.setLineList(dto.getLineDtoList());

			form.newData = false;

			
			if( bInsert){
				addMessage("infos.insert");
			}else{
				addMessage("infos.update");
			}

			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

		} catch (ServiceException e) {
			e.printStackTrace();
			super.errorLog(e);
			
			if(e.isStopOnError()) {
				
				throw e;
			}else{
				addMessage(e.getMessage());
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

				
				return this.getInputURIString();
			}
		} catch (UnabledLockException e) {
			e.printStackTrace();
			super.errorLog(e);
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(e.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return "errorInit";
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return this.getInputURIString();
	}

	/**
	 * 削除処理を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. アクションフォームからDTOに値をコピー<br>
	 * 2. 前処理<br>
	 * 3. 伝票のオーディット情報を更新<br>
	 * 4. 伝票を削除<br>
	 * 5. 明細リストから未入力の明細を削除<br>
	 * 6. 明細のオーディット情報を更新<br>
	 * 7. 明細を削除<br>
	 * 8. 後処理<br>
	 * 9. プルダウン要素を作成<br>
	 * 10.削除完了メッセージを表示
	 * </p>
	 * 処理実行後、{@link AbstractSlipEditAction#index()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator=true, validate="@,validateAtDeleteSlip", stopOnValidationError = false, input = "errorInit")
	public String delete() throws Exception {

		AbstractSlipEditForm<LINEDTOCLASS> form = this.getActionForm();
		prepareForm();
		try {
			AbstractSlipDto<LINEDTOCLASS> dto = form.copyToDto();

			
			this.beforeDelete(dto);

			
			this.getSlipService().updateAudit(dto.getKeyValue());

			
			this.getSlipService().deleteById(dto.getKeyValue(), this.getActionForm().updDatetm);

			
			dto.removeBlankLine();

			
			this.getLineService().updateAudit(dto.getKeyValue());

			
			this.getLineService().deleteRecords(dto.getKeyValue());


			
			this.afterDelete(dto);

			
			createList();

			
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.delete"));

			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

			return index();
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		} catch (UnabledLockException e) {
			super.errorLog(e);
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(e.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return "errorInit";
		} catch (Exception e) {
			super.errorLog(e);
			throw e;
		}
	}

	/**
	 * 伝票複写を行います.<br>
	 * <p>
	 * 以下の処理を行います.<br>
	 * 1. アクションフォームからDTOに値をコピー<br>
	 * 2. {@link DTOCLASS#getKeyValue()}で取得した伝票IDを読み込む<br>
	 * 3. アクションフォームに値を設定<br>
	 * 4. アクションフォームをコピー時初期化
	 * </p>
	 * 処理実行後、{@link AbstractSlipEditAction#getInputURIString()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Execute(validator = false)
	public String copy() throws Exception {
		AbstractSlipEditForm<LINEDTOCLASS> form = this.getActionForm();
		prepareForm();
		try {
			form.initialize();

			DTOCLASS dto = (DTOCLASS)form.copyToDto();

			
			if (dto.getKeyValue() == null || dto.getKeyValue().length() == 0) {
				String strLabel = MessageResourcesUtil.getMessage(this.getSlipKeyLabel());
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.notExist", strLabel));
				ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
				return this.getInputURIString();
			}
			dto = this.getSlipService().loadBySlipId(dto.getKeyValue());
			Beans.copy(dto, form).execute();

			
			List<LINEDTOCLASS> lineDtoList = this.getLineService().loadBySlip(dto);
			form.setLineList(lineDtoList);

			
			form.initCopy();

			createList();

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		return this.getInputURIString();
	}

	/**
	 * フォームの準備を行います.<br>
	 * デフォルト実装ではアクションフォームにユーザ、自社情報、税率計算サービスを設定します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 */
	protected void prepareForm() {
		AbstractSlipEditForm<LINEDTOCLASS> form = this.getActionForm();
		form.userDto = this.userDto;
		form.mineDto = this.mineDto;
		form.taxRateService = this.taxRateService;
	}

	/**
	 * アクションフォームを返します.
	 * @return アクションフォーム
	 */
	protected abstract AbstractSlipEditForm<LINEDTOCLASS> getActionForm();

	/**
	 * プルダウンの要素を作成します.
	 * @throws Exception
	 */
	protected abstract void createList() throws Exception;

	/**
	 * 初期表示用のDTOを作成します.
	 * @return 伝票のDTO
	 */
	protected abstract AbstractSlipDto<LINEDTOCLASS> createDTO();

	/**
	 * 画面遷移先のURI文字列を返します.
	 * @return 画面遷移先のURI文字列
	 */
	protected abstract String getInputURIString();

	/**
	 * 伝票サービスクラスを返します.
	 * @return 伝票サービス
	 */
	protected abstract AbstractSlipService<?, DTOCLASS> getSlipService();

	/**
	 * 明細サービスクラスを返します.
	 * @return 明細サービス
	 */
	protected abstract AbstractLineService<?, LINEDTOCLASS, DTOCLASS> getLineService();

	/**
	 * {@link AbstractSlipEditAction#upsert()}の保存処理で必要な追加サービスの配列を返します.<br>
	 * デフォルト実装ではnullを返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @return 追加サービスの配列
	 */
	protected AbstractService<?>[] getAdditionalServiceOnSaveSlip() {
		
		return null;
	}

	/**
	 * {@link AbstractSlipEditAction#upsert()}の保存前に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @param bInsert 新規登録か否か
	 * @param dto 伝票のDTO
	 * @throws Exception
	 */
	protected void beforeUpsert(boolean bInsert, AbstractSlipDto<LINEDTOCLASS> dto) throws Exception {
		
	}

	/**
	 * {@link AbstractSlipEditAction#upsert()}保存後に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @param bInsert 新規登録か否か
	 * @param dto 伝票のDTO
	 * @throws Exception
	 */
	protected void afterUpsert(boolean bInsert, AbstractSlipDto<LINEDTOCLASS> dto) throws Exception {
		
	}

	/**
	 * {@link AbstractSlipEditAction#delete()}の削除前に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @param dto 伝票のDTO
	 * @throws Exception
	 */
	protected void beforeDelete(AbstractSlipDto<LINEDTOCLASS> dto) throws Exception {
		
	}

	/**
	 * {@link AbstractSlipEditAction#delete()}削除後に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @param dto 伝票のDTO
	 * @throws Exception
	 */
	protected void afterDelete(AbstractSlipDto<LINEDTOCLASS> dto) throws Exception {
		
	}

	/**
	 * 伝票を読み込みます.
	 * @return 読み込みできたか否か
	 * @throws Exception
	 * @throws ServiceException
	 */
	protected abstract boolean loadData() throws Exception, ServiceException;

	/**
	 * {@link AbstractSlipEditAction#load()}の読み込み後に必要な処理を行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @throws Exception
	 * @throws ServiceException
	 */
	protected void afterLoad() throws Exception, ServiceException {

	}

	/**
	 * 伝票登録時のバリデートを行います.
	 * @return 表示するメッセージ
	 * @throws ServiceException
	 */
	public ActionMessages validateAtCreateSlip() throws ServiceException {
		return super.messages;
	}

	/**
	 * 伝票削除時のバリデートを行います.
	 * @return 表示するメッセージ
	 * @throws ServiceException
	 */
	public ActionMessages validateAtDeleteSlip() throws ServiceException {
		return super.messages;
	}

	/**
	 * 伝票名を返します.
	 * @return 伝票名
	 */
	public abstract String getSlipKeyLabel();

	/**
	 * {@link ActionMessage}を生成してメッセージリストに追加します.
	 * @param arg {@link ActionMessage}生成に使用する引数
	 */
	protected void addMessage(String... arg) {

		
		if(( arg[0].indexOf("errors.") < 0 )&&( arg[0].indexOf("infos.") < 0 )){
			this.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage( "errors.none", arg[0]));
		}else{
			switch (arg.length) {
			case 1:
				this.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0] ));
				break;
			case 2:
				this.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0], arg[1]));
				break;
			case 3:
				this.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0], arg[1], arg[2]));
				break;
			case 4:
				this.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0], arg[1], arg[2], arg[3]));
				break;
			case 5:
				this.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0], arg[1], arg[2], arg[3], arg[4]));
				break;
			default:
				this.messages.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage( arg[0] ));
				break;
			}
		}
	}
}
