/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.setting;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.setting.FileInfoDto;
import jp.co.arkinfosys.entity.join.FileInfoJoin;
import jp.co.arkinfosys.form.ajax.setting.DeleteFileAjaxForm;
import jp.co.arkinfosys.service.FileInfoService;
import jp.co.arkinfosys.service.AbstractMasterEditService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.ResponseUtil;

/**
 * ファイル登録画面の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteFileAjaxAction extends
		AbstractDeleteAjaxAction<FileInfoDto, FileInfoJoin> {

	@ActionForm
	@Resource
	public DeleteFileAjaxForm deleteFileAjaxForm;

	@Resource
	public FileInfoService fileInfoService;

	/**
	 * 削除後に必要な処理を実行します.<BR>
	 * 削除完了メッセージを表示するメッセージに設定します.
	 */
	@Override
	protected void doAfterDelete() throws Exception {
		ResponseUtil.write(MessageResourcesUtil.getMessage("infos.delete"));
	}

	/**
	 * 削除レコードを識別する情報を持ったファイル登録DTOを返します.
	 * @return {@link FileInfoDto}
	 */
	@Override
	protected FileInfoDto getIdentifiedDto() {
		return Beans.createAndCopy(FileInfoDto.class, this.deleteFileAjaxForm)
				.execute();
	}

	/**
	 * 削除処理を行うファイル操作サービスを返します.
	 * @return {@link FileInfoService}
	 */
	@Override
	protected AbstractMasterEditService<FileInfoDto, FileInfoJoin> getService() {
		return this.fileInfoService;
	}
}
