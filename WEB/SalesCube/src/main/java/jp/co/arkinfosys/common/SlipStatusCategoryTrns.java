/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

/**
 * 伝票の状態フラグを定義するクラスです.
 * @author Ark Information Systems
 *
 */
public class SlipStatusCategoryTrns {

	/**
	 * 区分名：発注伝票状態、区分コード名：発注
	 */
	public static final String PO_SLIP_NEW = "0";

	/**
	 * 区分名：発注伝票状態、区分コード名：仕入中
	 */
	public static final String PO_SLIP_PROCESSING = "1";

	/**
	 * 区分名：発注伝票状態、区分コード名：仕入完了
	 */
	public static final String PO_SLIP_PROCESSED = "9";

	/**
	 * 区分名：仕入伝票状態、区分コード名：未払い
	 */
	public static final String SUPPLIER_SLIP_UNPAID = "0";

	/**
	 * 区分名：仕入伝票状態、区分コード名：支払済
	 */
	public static final String SUPPLIER_SLIP_PAID = "9";

	/**
	 * 区分名：受注明細行状態、区分コード名：未納
	 */
	public static final String RO_LINE_NEW = "0";

	/**
	 * 区分名：受注明細行状態、区分コード名：分納中
	 */
	public static final String RO_LINE_PROCESSING = "1";

	/**
	 * 区分名：受注明細行状態、区分コード名：売上完了
	 */
	public static final String RO_LINE_PROCESSED = "9";

}
