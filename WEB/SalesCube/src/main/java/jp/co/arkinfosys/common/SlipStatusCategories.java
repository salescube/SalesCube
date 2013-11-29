/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

/**
 * 伝票のステータスを定義するクラスです.
 * @author Ark Information Systems
 *
 */
public class SlipStatusCategories {
	/**
	 * 区分名：発注伝票状態
	 */
	public static final int PO_SLIP_STATUS = 100;

	/**
	 * 区分名：発注伝票明細行状態
	 */
	public static final int PO_LINE_STATUS = 101;

	/**
	 * 区分名：仕入伝票状態
	 */
	public static final int SUPPLIER_SLIP_STATUS = 102;

	/**
	 * 区分名：仕入伝票明細行状態
	 */
	public static final int SUPPLIER_LINE_STATUS = 103;

	/**
	 * 区分名：支払伝票状態
	 */
	public static final int PAYMENT_SLIP_STATUS = 104;

	/**
	 * 区分名：支払伝票明細行状態
	 */
	public static final int PAYMENT_LINE_STATUS = 105;

	/**
	 * 区分名：受注伝票状態
	 */
	public static final int RO_SLIP_STATUS = 106;

	/**
	 * 区分名：受注明細行状態
	 */
	public static final int RO_LINE_STATUS = 107;

	/**
	 * 区分名：売上伝票状態
	 */
	public static final int SALES_SLIP_STATUS = 108;

	/**
	 * 区分名：売上伝票明細行状態
	 */
	public static final int SALES_LINE_STATUS = 109;

	/**
	 * 区分名：請求伝票状態
	 */
	public static final int BILL_SLIP_STATUS = 110;

	/**
	 * 区分名：入金伝票状態
	 */
	public static final int DEPOSIT_SLIP_STATUS = 111;

	/**
	 * 区分名：入金伝票明細行状態
	 */
	public static final int DEPOSIT_LINE_STATUS = 112;

}
