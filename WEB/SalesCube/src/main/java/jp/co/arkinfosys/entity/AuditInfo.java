/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Timestamp;

/**
 * レコード更新情報を保持するエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public abstract class AuditInfo {

	/** レコード作成機能 */
	public String creFunc;
	/** レコード作成日時 */
	public Timestamp creDatetm;
	/** レコード作成者 */
	public String creUser;
	/** レコード更新機能 */
	public String updFunc;
	/** レコード更新日時 */
	public Timestamp updDatetm;
	/** レコード更新者 */
	public String updUser;

}
