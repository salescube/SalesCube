/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.Menu;

/**
 * メニューマスタとロール設定のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class MenuJoin extends Menu {

	private static final long serialVersionUID = 1L;

	/**
	 * ロール設定マスタ
	 */
	public String validFlag;

}
