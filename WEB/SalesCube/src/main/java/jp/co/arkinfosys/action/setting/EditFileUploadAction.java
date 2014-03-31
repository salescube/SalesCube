package jp.co.arkinfosys.action.setting;

import javax.annotation.Resource;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.setting.FileInfoDto;
import jp.co.arkinfosys.entity.FileInfo;
import jp.co.arkinfosys.form.setting.FileUploadForm;
import jp.co.arkinfosys.service.FileInfoService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

/**
 * ファイル登録編集画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class EditFileUploadAction extends CommonResources {


	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "editFileUpload.jsp";
	}

	@ActionForm
	@Resource
	private FileUploadForm fileUploadForm;

	@Resource
	private FileInfoService fileInfoService;

	/**
	 * 新規登録時の初期化処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		try {
			this.init(null);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw new ServiceException(e);
		}

		return EditFileUploadAction.Mapping.INPUT;
	}

	/**
	 * 編集モード時の初期化処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{fileId}")
	public String edit() throws Exception {
		try {
			this.fileUploadForm.fileId = StringUtil.decodeSL(this.fileUploadForm.fileId);
			this.init(this.fileUploadForm.fileId);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		return EditFileUploadAction.Mapping.INPUT;
	}


	/**
	 * 新規登録処理 ファイルをアップロードします.<br>
	 * アップロード完了時に、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 *
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
*/
	@Execute(validator = true, validate = "validate", input = Mapping.INPUT)
	public String insert() throws Exception {
		try {
			// ファイル情報登録
			FileInfoDto dto = Beans.createAndCopy(FileInfoDto.class,
					this.fileUploadForm).execute();
			this.fileInfoService.insertRecord(dto);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.insert"));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);


			this.init(dto.fileId);

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		} catch (Exception e) {
			super.errorLog(e);
			throw e;
		}
		return Mapping.INPUT;
	}

	/**
	 * 更新処理を行います. ファイル以外<br>
	 * 更新完了時に、画面にメッセージを表示します.
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, input = EditFileUploadAction.Mapping.INPUT)
	public String update() throws Exception {
		try {

			FileInfoDto dto = Beans.createAndCopy(FileInfoDto.class, this.fileUploadForm)
					.execute();

			// ファイル登録情報更新
			this.fileInfoService.updateRecord(dto);

			this.init(this.fileUploadForm.fileId);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.update"));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);


		} catch (UnabledLockException e) {
			super.errorLog(e);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(e.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		return Mapping.INPUT;
	}

	/**
	 * 削除処理を行います.<br>
	 * 削除完了時および何かしら問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String delete() throws Exception {
		try {

			// ファイル情報削除
			this.fileInfoService.deleteRecord(Beans.createAndCopy(FileInfoDto.class, this.fileUploadForm).execute());
			this.init(null);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.delete"));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (UnabledLockException e) {
			super.errorLog(e);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(e.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
		return Mapping.INPUT;
	}


	/**
	 * リセット処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		if (this.fileUploadForm.editMode) {
			this.init(this.fileUploadForm.fileId);
		} else {
			this.init(null);
		}
		return EditFileUploadAction.Mapping.INPUT;
	}

	/**
	 * アクションフォームを初期化します.
	 * @param fileId ファイルID
	 * @throws ServiceException
	 */
	private void init(String fileId) throws ServiceException {
		this.fileUploadForm.reset();

		// 更新権限
		this.fileUploadForm.isUpdate = super.userDto
				.isMenuUpdate(Constants.MENU_ID.SETTING_FILE);


		// 公開範囲
		this.fileUploadForm.openLevelList
				.add(new LabelValueBean(MessageResourcesUtil
						.getMessage("labels.file.valid.limitation"),
						Constants.MENU_VALID_LEVEL.VALID_LIMITATION));
		this.fileUploadForm.openLevelList.add(new LabelValueBean(
				MessageResourcesUtil.getMessage("labels.file.valid.full"),
				Constants.MENU_VALID_LEVEL.VALID_FULL));


		if (!StringUtil.hasLength(fileId)) {
			return;
		}

		/*ファイル情報を取得してフォームにセットする*/
		FileInfo fileInfo = this.fileInfoService.findById(fileId);
		if (fileInfo == null) {
			return;
		}

		Beans.copy(fileInfo, this.fileUploadForm).timestampConverter(
				Constants.FORMAT.TIMESTAMP)
				.dateConverter(Constants.FORMAT.DATE).execute();


		// 画面表示用の更新
		this.fileUploadForm.creDatetmShow = StringUtil.getDateString(
				Constants.FORMAT.DATE, fileInfo.creDatetm);
		this.fileUploadForm.updDatetmShow = StringUtil.getDateString(
				Constants.FORMAT.DATE, fileInfo.updDatetm);


		this.fileUploadForm.editMode = true;
	}

}
