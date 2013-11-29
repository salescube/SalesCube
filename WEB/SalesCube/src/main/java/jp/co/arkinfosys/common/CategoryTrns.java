/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

/**
 * 区分データの「区分コード」に該当する定数値を定義するクラスです.
 * @author Ark Information Systems
 *
 */
public final class CategoryTrns {

	/**
	 * 区分名：課税区分、区分コード名：課税
	 */
	public static final String TAX_CATEGORY_IMPOSITION = "0";

	/**
	 * 区分名：課税区分、区分コード名：課税（旧）
	 */
	public static final String TAX_CATEGORY_IMPOSITION_OLD = "1";

	/**
	 * 区分名：課税区分、区分コード名：非課税
	 */
	public static final String TAX_CATEGORY_NO = "2";

	/**
	 * 区分名：課税区分、区分コード名：対象外
	 */
	public static final String TAX_CATEGORY_OFF = "3";

	/**
	 * 区分名：課税区分、区分コード名：内税
	 */
	public static final String TAX_CATEGORY_INCLUDED = "4";

	/**
	 * 区分名：課税区分、区分コード名：免税
	 */
	public static final String TAX_CATEGORY_FREE = "5";

	/**
	 * 区分名：商品端数処理、区分コード名：切捨て
	 */
	public static final String FLACT_CATEGORY_DOWN = "0";

	/**
	 * 区分名：商品端数処理、区分コード名：四捨五入
	 */
	public static final String FLACT_CATEGORY_HALF_UP = "1";

	/**
	 * 区分名：商品端数処理、区分コード名：切り上げ
	 */
	public static final String FLACT_CATEGORY_UP = "2";

	/**
	 * 区分名：在庫管理区分、区分コード名：しない
	 */
	public static final String PRODUCT_STOCK_CTL_NO = "0";

	/**
	 * 区分名：在庫管理区分、区分コード名：する
	 */
	public static final String PRODUCT_STOCK_CTL_YES = "1";

	/**
	 * 区分名：単位コード、区分コード名：本
	 */
	public static final String PRODUCT_UNIT_HON = "1";

	/**
	 * 区分名：セット商品フラグ、区分コード名：単品
	 */
	public static final String PRODUCT_SET_TYPE_SINGLE = "0";

	/**
	 * 区分名：セット商品フラグ、区分コード名：セット品
	 */
	public static final String PRODUCT_SET_TYPE_SET = "1";

	/**
	 * 区分名：棚区分、区分コード名：自社棚
	 */
	public static final String RACK_CATEGORY_OWN = "1";

	/**
	 * 区分名：棚区分、区分コード名：預け棚
	 */
	public static final String RACK_CATEGORY_ENTRUST = "2";

	/**
	 * 区分名：売上取引区分、区分コード名：掛売
	 */
	public static final String SALES_CM_CREDIT = "0";

	/**
	 * 区分名：売上取引区分、区分コード名：現金
	 */
	public static final String SALES_CM_CASH = "1";

	/**
	 * 区分名：売上取引区分、区分コード名：サンプル
	 */
	public static final String SALES_CM_SAMPLE = "2";

	/**
	 * 区分名：売上取引区分、区分コード名：代引き
	 */
	public static final String SALES_CM_CASH_ON_DELIVERY = "3";

	/**
	 * 区分名：売上取引区分、区分コード名：クレジット
	 */
	public static final String SALES_CM_CREDIT_CARD = "4";

	/**
	 * 区分名：売上取引区分、区分コード名：先入金
	 */
	public static final String SALES_CM_PAY_FIRST = "5";

	/**
	 * 区分名：売上取引区分、区分コード名：Online
	 */
	public static final String SALES_CM_ONLINE = "6";

	/**
	 * 区分名：税転嫁、区分コード名：外税伝票計
	 */
	public static final String TAX_SHIFT_CATEGORY_SLIP_TOTAL = "1";

	/**
	 * 区分名：税転嫁、区分コード名：内税
	 */
	public static final String TAX_SHIFT_CATEGORY_INCLUDE_CTAX = "3";

	/**
	 * 区分名：税転嫁、区分コード名：外税締単位
	 */
	public static final String TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS = "0";

	/**
	 * 区分名：回収サイクル、区分コード名：当月
	 */
	public static final String PAYBACK_CYCLE_CATEGORY_0 = "0";

	/**
	 * 区分名：回収サイクル、区分コード名：翌月
	 */
	public static final String PAYBACK_CYCLE_CATEGORY_1 = "1";

	/**
	 * 区分名：回収サイクル、区分コード名：翌々月
	 */
	public static final String PAYBACK_CYCLE_CATEGORY_2 = "2";

	/**
	 * 区分名：回収サイクル、区分コード名：その他
	 */
	public static final String PAYBACK_CYCLE_CATEGORY_ETC = "9";

	/**
	 * 区分名：売上伝票種別、区分コード名：売上伝票（ページ6行）
	 */
	public static final String SALES_SLIP_CATEGORY_SALES_SLIP="0";

	/**
	 * 区分名：売上伝票種別、区分コード名：大学ｾｯﾄ(日付あり)
	 */
	public static final String SALES_SLIP_CATEGORY_UNIV_SET_D="3";

	/**
	 * 区分名：売上伝票種別、区分コード名：大学ｾｯﾄ(日付なし)
	 */
	public static final String SALES_SLIP_CATEGORY_UNIV_SET_N="4";

	/**
	 * 区分名：売上伝票種別、区分コード名：納品書/請求書(日付あり)
	 */
	public static final String SALES_SLIP_CATEGORY_BILL_D="5";

	/**
	 * 区分名：売上伝票種別、区分コード名：納品書/請求書(日付なし)
	 */
	public static final String SALES_SLIP_CATEGORY_BILL_N="6";

	/**
	 * 区分名：売上伝票種別、区分コード名：納品書/領収書
	 */
	public static final String SALES_SLIP_CATEGORY_RECEIPT="9";

	/**
	 * 区分名：請求書種別、区分コード名：発行しない
	 */
	public static final String BILL_CATEGORY_NO_PUBLISH = "0";

	/**
	 * 区分名：請求書種別、区分コード名：請求明細書(末/翌5日発行)
	 */
	public static final String BILL_CATEGORY_DETAIL5 = "1";

	/**
	 * 区分名：請求書種別、区分コード名：請求明細書
	 */
	public static final String BILL_CATEGORY_DETAIL = "2";

	/**
	 * 区分名：請求書種別、区分コード名：大学ｾｯﾄ
	 */
	public static final String BILL_CATEGORY_UNIV = "3";

	/**
	 * 区分名：請求書種別、区分コード名：納品書/請求書
	 */
	public static final String BILL_CATEGORY_DELBILL = "5";

	/**
	 * 区分名：仕入取引区分、区分コード名：掛仕
	 */
	public static final String SUPPLIER_CM_CREDIT = "0";

	/**
	 * 区分名：国内外区分、区分コード名：国内
	 */
	public static final String NATIONAL_CATEGORY_NATIONAL = "0";

	/**
	 * 区分名：国内外区分、区分コード名：国外
	 */
	public static final String NATIONAL_CATEGORY_FOREIGN = "1";

	/**
	 * 区分名：税区分、区分コード名：消費税
	 */
	public static final String TAX_TYPE_CTAX = "1";

	/**
	 * 区分名：配送業者区分、区分コード名：配送業者1
	 */
	public static final String DC_CATEGORY_1 = "1";

	/**
	 * 区分名：配送業者区分、区分コード名：配送業者2
	 */
	public static final String DC_CATEGORY_2 = "2";

	/**
	 * 区分名：入金区分コード、区分コード名：現金
	 */
	public static final String DEPOSIT_CATEGORY_CASH = "01";

	/**
	 * 区分名：入金区分コード、区分コード名：振込
	 */
	public static final String DEPOSIT_CATEGORY_TRANSFER = "03";

	/**
	 * 区分名：入金区分コード、区分コード名：代引き
	 */
	public static final String DEPOSIT_CATEGORY_CASH_ON_DELIVERY = "10";

	/**
	 * 区分名：入金区分コード、区分コード名：クレジット
	 */
	public static final String DEPOSIT_CATEGORY_CREDIT_CARD = "11";

	/**
	 * 区分名：入金区分コード、区分コード名：先入金
	 */
	public static final String DEPOSIT_CATEGORY_PAY_FIRST = "12";

	/**
	 * 区分名：運送便区分、区分コード名：船便
	 */
	public static final String TRANSPORT_CATEGORY_SHIP = "1";

	/**
	 * 区分名：運送便区分、区分コード名：AIR便
	 */
	public static final String TRANSPORT_CATEGORY_AIR = "2";

	/**
	 * 区分名：運送便区分、区分コード名：委託在庫
	 */
	public static final String TRANSPORT_CATEGORY_ENTRUST = "3";

	/**
	 * 区分名：運送便区分、区分コード名：宅急便
	 */
	public static final String TRANSPORT_CATEGORY_DELIVERY = "4";

	/**
	 * 区分名：完納区分、区分コード名：未納
	 */
	public static final String DELIVERY_PROCESS_CATEGORY_NONE = "0";

	/**
	 * 区分名：完納区分、区分コード名：分納中
	 */
	public static final String DELIVERY_PROCESS_CATEGORY_PARTIAL = "1";

	/**
	 * 区分名：完納区分、区分コード名：完納
	 */
	public static final String DELIVERY_PROCESS_CATEGORY_FULL = "2";

	/**
	 * 区分名：入出庫区分、区分コード名：入庫
	 */
	public static final String EAD_CATEGORY_ENTER = "1";

	/**
	 * 区分名：入出庫区分、区分コード名：出庫
	 */
	public static final String EAD_CATEGORY_DISPATCH = "2";

	/**
	 * 区分名：委託入出庫区分、区分コード名：委託入庫
	 */
	public static final String ENTRUST_EAD_CATEGORY_ENTER = "1";

	/**
	 * 区分名：委託入出庫区分、区分コード名：委託出庫
	 */
	public static final String ENTRUST_EAD_CATEGORY_DISPATCH = "2";

	/**
	 * 区分名：入出庫伝票区分、区分コード名：通常
	 */
	public static final String EAD_SLIP_CATEGORY_NORMAL = "1";

	/**
	 * 区分名：敬称、区分コード名：御中
	 */
	public static final String PREFIX_ONCHU = "0";

	/**
	 * 区分名：敬称、区分コード名：様
	 */
	public static final String PREFIX_SAMA = "1";

	/**
	 * 区分名：敬称、区分コード名：殿
	 */
	public static final String PREFIX_DONO = "2";

	/**
	 * 区分名：入金方法、区分コード名：取り込み以外
	 */
	public static final String DEPOSIT_METHOD_INPUT = "01";

	/**
	 * 区分名：入金方法、区分コード名：配送業者入金取り込み
	 */
	public static final String DEPOSIT_METHOD_DELIVERY = "02";

	/**
	 * 区分名：入金方法、区分コード名：銀行入金取り込み
	 */
	public static final String DEPOSIT_METHOD_BANK = "03";

	/**
	 * 区分名：分類状況、区分コード名：販売中
	 */
	public static final String PRODUCT_STATUS_ONSALE = "1";

	/**
	 * 区分名：分類状況、区分コード名：販売中止
	 */
	public static final String PRODUCT_STATUS_SALE_CANCEL = "2";

	/**
	 * 区分名：分類保管、区分コード名：自社在庫品
	 */
	public static final String PRODUCT_STOCK_INSTOCK = "1";

	/**
	 * 区分名：分類保管、区分コード名：他社在庫品
	 */
	public static final String PRODUCT_STOCK_ENTRUST = "2";

	/**
	 * 区分名：分類保管、区分コード名：取り寄せ品
	 */
	public static final String PRODUCT_STOCK_ORDER = "3";

	/**
	 * 区分名：分類調達、区分コード名：国内調達
	 */
	public static final String PRODUCT_PURVAY_DOMESTIC = "1";

	/**
	 * 区分名：分類標準、区分コード名：標準品
	 */
	public static final String PRODUCT_STANDARD_STD = "0";

	/**
	 * 区分名：分類標準、区分コード名：特注品
	 */
	public static final String PRODUCT_STANDARD_ODR = "1";

	/**
	 * 区分名：受注停止、区分コード名：取引停止
	 */
	public static final String RECIEVE_ORDER_STOP = "1";

	/**
	 * 区分名：受注停止、区分コード名：入金遅延
	 */
	public static final String RECIEVE_ORDER_DEPOSIT_LATE = "2";

	/**
	 * 区分名：請求書作成区分、区分コード名：売上伝票
	 */
	public static final String BILL_CRT_SALES = "01";

	/**
	 * 区分名：請求書作成区分、区分コード名：請求締め
	 */
	public static final String BILL_CRT_BILL = "02";

	/**
	 * 区分名：廃番品分類、区分コード名：廃番品以外
	 */
	public static final String PRODUCT_NOT_DISCARD = "1";

	/**
	 * 区分名：廃番品分類、区分コード名：廃番品
	 */
	public static final String PRODUCT_DISCARD = "2";

	/**
	 * 区分名：送料区分、区分コード名：送料有料
	 */
	public static final String POSTAGE_PAY = "0";

	/**
	 * 区分名：送料区分、区分コード名：送料無料
	 */
	public static final String POSTAGE_FREE = "1";

	/**
	 * 区分名：親子区分、区分コード名：親
	 */
	public static final String FAMILY_CATEGORY_PARENTS = "1";

	/**
	 * 区分名：親子区分、区分コード名：子
	 */
	public static final String FAMILY_CATEGORY_CHILD = "2";

	/**
	 * 区分名：回収方法、区分コード名：現金
	 */
	public static final String PAYBACK_TYPE_CASH = "1";

	/**
	 * 区分名：回収方法、区分コード名：小切手
	 */
	public static final String PAYBACK_TYPE_CHECK = "2";

	/**
	 * 区分名：回収方法、区分コード名：振込
	 */
	public static final String PAYBACK_TYPE_BANK = "3";

	/**
	 * 区分名：回収方法、区分コード名：手形
	 */
	public static final String PAYBACK_TYPE_NOTE = "4";

	/**
	 * 区分名：回収方法、区分コード名：その他
	 */
	public static final String PAYBACK_TYPE_OTHER = "5";

	/**
	 * 区分名：支払条件、区分コード名：10日締め翌月10日
	 */
	public static final String CUTOFF_GROUP_10_NEXT10 = "101";

	/**
	 * 区分名：支払条件、区分コード名：20日締め翌月20日
	 */
	public static final String CUTOFF_GROUP_20_NEXT20 = "201";

	/**
	 * 区分名：支払条件、区分コード名：25日締め翌月末
	 */
	public static final String CUTOFF_GROUP_25_NEXTEND = "251";

	/**
	 * 区分名：支払条件、区分コード名：月末締め翌月末
	 */
	public static final String CUTOFF_GROUP_END_NEXTEND = "311";

	/**
	 * 区分名：支払条件、区分コード名：月末締め翌々月5日
	 */
	public static final String CUTOFF_GROUP_END_NN05 = "312";

	/**
	 * 区分名：支払条件、区分コード名：その他
	 */
	public static final String CUTOFF_GROUP_OTHER = "999";

	/**
	 * 区分名：発注区分、区分コード名：都度発注
	 */
	public static final String IMMEDIATELY_PORDER = "1";

	/**
	 * 区分名：発注区分、区分コード名：委託発注
	 */
	public static final String ENTRUST_PORDER = "2";

	/**
	 * 区分名：都度発注区分、区分コード名：通常発注
	 */
	public static final String NORMAL_PORDER = "1";

	/**
	 * 区分名：都度発注区分、区分コード名：倉庫移動
	 */
	public static final String MOVE_ENTRUST_STOCK = "2";

	/**
	 * 区分名：支払方法、区分コード名：現金
	 */
	public static final String PAYMENT_TYPE_CASH = "1";

	/**
	 * 区分名：支払方法、区分コード名：小切手
	 */
	public static final String PAYMENT_TYPE_CHECK = "2";

	/**
	 * 区分名：支払方法、区分コード名：振込
	 */
	public static final String PAYMENT_TYPE_BANK = "3";

	/**
	 * 区分名：支払方法、区分コード名：手形
	 */
	public static final String PAYMENT_TYPE_NOTE = "4";

	/**
	 * 区分名：支払方法、区分コード名：その他
	 */
	public static final String PAYMENT_TYPE_OTHER = "5";

	/**
	 * 区分名：支払区分、区分コード名：現金
	 */
	public static final String PAYMENT_DETAIL_CASH = "1";

	/**
	 * 区分名：支払区分、区分コード名：小切手
	 */
	public static final String PAYMENT_DETAIL_CHECK = "2";

	/**
	 * 区分名：支払区分、区分コード名：振込
	 */
	public static final String PAYMENT_DETAIL_BANK = "3";

	/**
	 * 区分名：支払区分、区分コード名：手数料
	 */
	public static final String PAYMENT_DETAIL_FEE = "4";

	/**
	 * 区分名：支払区分、区分コード名：手形
	 */
	public static final String PAYMENT_DETAIL_NOTE = "5";

	/**
	 * 区分名：支払区分、区分コード名：その他
	 */
	public static final String PAYMENT_DETAIL_OTHER = "6";

	/**
	 * 区分名：支払区分、区分コード名：相殺
	 */
	public static final String PAYMENT_DETAIL_OFFSET = "7";

	/**
	 * 区分名：請求書発行単位、区分コード名：請求書なし
	 */
	public static final String BILL_PRINT_UNIT_NO_BILL = "0";

	/**
	 * 区分名：請求書発行単位、区分コード名：請求締め単位
	 */
	public static final String BILL_PRINT_UNIT_BILL_CLOSE = "1";

	/**
	 * 区分名：請求書発行単位、区分コード名：売上伝票単位
	 */
	public static final String BILL_PRINT_UNIT_SALES_SLIP = "2";

	/**
	 * 区分名：請求書日付有無、区分コード名：あり
	 */
	public static final String BILL_DATE_PRINT_ON = "1";

	/**
	 * 区分名：請求書日付有無、区分コード名：なし
	 */
	public static final String BILL_DATE_PRINT_OFF = "0";
}
