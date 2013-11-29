/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * メニューマスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;

	public String menuId;

	public String caption;

	public String description;

	public String url;

	public String parentId;

	public Integer seq;

	public String validType;

	public String fontColor;

	public String bgColor;

	public String creFunc;

	public Timestamp creDatetm;

	public String creUser;

	public String updFunc;

	public Timestamp updDatetm;

	public String updUser;

}
