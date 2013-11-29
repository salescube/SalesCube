/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 明細表示項目マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class DetailDispItem {

	@Id
	public String detailId;

	@Id
	public String target;

	@Id
	public String itemId;

	@Id
	public String itemName;

	public Integer seq;

	public String esslFlag;

	public String dispFlag;

	public String sortFlag;

	public String detailFlag;

	public Integer colWidth;

	public String textAlign;

	public Integer formatType;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
