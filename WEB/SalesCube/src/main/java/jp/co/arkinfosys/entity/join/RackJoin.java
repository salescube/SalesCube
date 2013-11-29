/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.Rack;

/**
 * 棚番マスタと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class RackJoin extends Rack {

	private static final long serialVersionUID = 1L;

	public String rackCategoryName;

	public boolean exist;
}
