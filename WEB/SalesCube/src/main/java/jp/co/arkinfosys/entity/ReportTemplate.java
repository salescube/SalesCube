/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 帳票テンプレートマスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class ReportTemplate {
	@Id
	public String reportId;

	public String description;

	public String path;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;
}
