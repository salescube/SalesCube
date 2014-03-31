/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.setting;

import java.io.Serializable;

import jp.co.arkinfosys.dto.master.MasterEditDto;

import org.apache.struts.upload.FormFile;

/**
 * ファイル登録情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class FileInfoDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	//public Integer fileId;
	public String fileId;

	public String title;

	public String fileName;

	public String realFileName;

	public String fileSize;

	public String openLevel;

	public String openLevelName;

	public String creFunc;

	public String creDate;

	public String creDatetm;

	public String creUser;

	public String creUserName;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	public FormFile formFile;

	/**
	 * ファイルIDを取得します.
	 * @return ファイルID
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.fileId.toString() };
	}

}
