/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.sql.Date;

import jp.co.arkinfosys.entity.PoSlipTrn;
/**
 *　発注伝票と仕入伝票と支払伝票のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class PoSlipTrnJoin extends PoSlipTrn {

	private static final long serialVersionUID = 1L;

	/**
	 * 紐づく支払伝票の支払日
	 */
	public Date paymentDate;

	/**
	 * 支払状態文字列
	 */
	public String paymentStatus;

	/**
	 * 仕入先レート（明細行より）
	 */
	public String supplierRate;

	/**
	 * 伝票作成時点でのレートIDから引ける現在のレートマスタの通貨単位(矛盾)
	 */
	public String cUnitSign;
}
