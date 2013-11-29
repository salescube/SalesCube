/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.DetailDispItem;

/**
 * 明細項目表示マスタと明細表示設定のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class DetailDispItemJoin extends DetailDispItem {

	public String userId;

}
