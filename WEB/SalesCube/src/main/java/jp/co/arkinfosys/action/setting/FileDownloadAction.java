/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.service.FileInfoService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.Execute;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Required;

/**
 * ファイルダウンロードのアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class FileDownloadAction extends CommonResources {

	@Required
	@IntegerType
	public String fileId;

	@Resource
	private FileInfoService fileInfoService;

	/**
	 * ファイルをダウンロードします.
	 * @return null
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "download/{fileId}")
	public String download() throws Exception {
		try {
			this.fileInfoService.downloadFile(fileId, super.httpRequest,
					super.httpResponse);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		} catch (Exception e) {
			super.errorLog(e);
			throw e;
		}
		return null;
	}

}
