/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import jp.co.arkinfosys.entity.ProductClass;

/**
 * 商品分類マスタ（３階層）のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ProductClassJoin extends ProductClass {
	private static final long serialVersionUID = 1L;

	public String className1;
	public String className2;
	public String className3;
}
