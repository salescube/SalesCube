/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.FileInfo;

/**
 * ファイル情報テーブルと社員マスタのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class FileInfoJoin extends FileInfo {

	private static final long serialVersionUID = 1L;

	/**
	 * 作成者名
	 */
	public String creUserName;

}
