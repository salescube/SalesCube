/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;

import java.io.InputStream;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.setting.FileInfoDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.setting.FileUploadForm;
import jp.co.arkinfosys.service.FileInfoService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.upload.S2MultipartRequestHandler;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * ファイル登録画面のアクションクラスです.
 * 
 * @author Ark Information Systems
 * 
 */
public class FileUploadAction extends AbstractSearchAction<FileInfoDto> {

	public static class Mapping extends AbstractSearchAction.Mapping {
		public static final String INPUT = "upload.jsp";
	}

	@ActionForm
	@Resource
	public FileUploadForm fileUploadForm;

	@Resource
	private FileInfoService fileInfoService;

	/**
	 * リクエストのサイズが設定された最大サイズを超えているか確認します.
	 * 
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doBeforeIndex()
	 */
	@Override
	protected void doBeforeIndex() throws Exception {
		SizeLimitExceededException e = (SizeLimitExceededException) super.httpRequest
				.getAttribute(S2MultipartRequestHandler.SIZE_EXCEPTION_KEY);
		if (e != null) {
			// 他のエラーは削除
			super.httpRequest.setAttribute(Globals.ERROR_KEY, null);
			// ファイルサイズエラーを書き込み
			super.messages.add(
					ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.upload.size", e
							.getPermittedSize()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
	
			// 読みかけのファイルの残りを読む
			InputStream is = null;
			try {
				is = this.httpRequest.getInputStream();
				byte[] buf = new byte[1024];
				while (is.read(buf) != -1) {
				}
				is.close();
				is = null;
			} catch (Exception ignore) {
			} finally {
				try {
					if(is != null) {
						is.close();
					}
				} catch (Exception ignore) {
				}
			}
		}
	}

	/**
	 * {@link Mapping#INPUT}で定義されたURI文字列を返します.
	 * 
	 * @return 画面遷移先のURI文字列
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getInputURIString()
	 */
	@Override
	protected String getInputURIString() {
		return Mapping.INPUT;
	}

	/**
	 * 
	 * @return {@link FileUploadForm}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getActionForm()
	 */
	@Override
	protected AbstractSearchForm<FileInfoDto> getActionForm() {
		return this.fileUploadForm;
	}

	/**
	 * 
	 * @return {@link MENU_ID#SETTING_FILE}
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#getSearchMenuID()
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SETTING_FILE;
	}

	/**
	 * 
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#createList()
	 */
	@Override
	protected void createList() throws ServiceException {
		// 公開範囲
		this.fileUploadForm.openLevelList
				.add(new LabelValueBean(MessageResourcesUtil
						.getMessage("labels.file.valid.limitation"),
						Constants.MENU_VALID_LEVEL.VALID_LIMITATION));
		this.fileUploadForm.openLevelList.add(new LabelValueBean(
				MessageResourcesUtil.getMessage("labels.file.valid.full"),
				Constants.MENU_VALID_LEVEL.VALID_FULL));
	}

	/**
	 * ファイルをアップロードします.<br>
	 * アップロード完了時に、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "index")
	public String upload() throws Exception {
		try {
			// ファイル情報登録
			FileInfoDto dto = Beans.createAndCopy(FileInfoDto.class,
					this.fileUploadForm).execute();
			this.fileInfoService.insertRecord(dto);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.insert"));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

			this.fileUploadForm.reset();
			this.createList();
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		} catch (Exception e) {
			super.errorLog(e);
			throw e;
		}
		return Mapping.INPUT;
	}
}
