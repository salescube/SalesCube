/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * ファイル情報テーブルのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class FileInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "FILE_INFO";

	@Id
	public Integer fileId;

	public String title;

	public String fileName;

	public String realFileName;

	public Integer fileSize;

	public String openLevel;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
