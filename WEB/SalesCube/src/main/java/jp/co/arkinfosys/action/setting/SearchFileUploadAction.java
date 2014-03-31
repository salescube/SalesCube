package jp.co.arkinfosys.action.setting;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.upload.S2MultipartRequestHandler;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.setting.FileInfoDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.service.FileInfoService;
import jp.co.arkinfosys.service.exception.ServiceException;

import jp.co.arkinfosys.form.setting.SearchFileForm;

public class SearchFileUploadAction extends AbstractSearchAction<FileInfoDto> {

	@ActionForm
	@Resource
	public SearchFileForm SearchFileForm;

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
	 * ソート条件を設定します.
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() throws Exception {
		this.SearchFileForm.sortColumn = FileInfoService.Param.FILE_ID;
		this.SearchFileForm.sortOrderAsc = true;
	}

	@Override
	protected String getSearchMenuID() {
		//
		return Constants.MENU_ID.SETTING_FILE;
		//return null;
	}

	@Override
	protected void createList() throws ServiceException {
		// 公開範囲
		this.SearchFileForm.openLevelList
				.add(new LabelValueBean(MessageResourcesUtil
						.getMessage("labels.file.valid.limitation"),
						Constants.MENU_VALID_LEVEL.VALID_LIMITATION));
		this.SearchFileForm.openLevelList.add(new LabelValueBean(
				MessageResourcesUtil.getMessage("labels.file.valid.full"),
				Constants.MENU_VALID_LEVEL.VALID_FULL));

		this.SearchFileForm.openLevelList.add(0, new LabelValueBean());

	}

	@Override
	protected AbstractSearchForm<FileInfoDto> getActionForm() {
		return this.SearchFileForm;

	}

}
