/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 定数を定義するクラスです.
 *
 * @author Ark Information Systems
 *
 */
public final class Constants {

	/**
	 * 文字コードセットに関する定数定義
	 */
	public static class CHARSET {
		public static final String SHIFT_JIS = "SHIFT_JIS";

		public static final String WINDOWS31J = "Windows-31J";

		public static final String UTF8 = "UTF-8";

		public static final String LATIN1 = "ISO-8859-1";
	}

	/**
	 * MimeTypeに関する定数定義
	 */
	public static final class MIME {
		/**
		 * PDF形式
		 */
		public static final String PDF = "application/pdf";

		/**
		 * Excel形式
		 */
		public static final String XLS = "application/vnd.ms-excel";

		/**
		 * 未知のバイナリデータ
		 */
		public static final String BIN = "application/octet-stream";
	}

	/**
	 * フラグに関する定数定義
	 *
	 */
	public static class FLAG {
		public static final String OFF = "0";

		public static final String ON = "1";
	}

	/**
	 * SQLに関する定数定義
	 */
	public static class SQL {
		public static final String ASC = "ASC";

		public static final String DESC = "DESC";
	}

	/**
	 * フォーマット定義
	 */
	public static class FORMAT {
		public static final String YEAR = "yyyy";

		public static final String DATE = "yyyy/MM/dd";

		public static final String DATE_DOT = "yyyy.MM.dd";

		public static final String TIMESTAMP = "yyyy/MM/dd HH:mm:ss.S";
		public static final String TIMESTAMP2 = "yyyy/MM/dd HH:mm:ss";

		public static final String TIMESTAMP_NOSEC = "yyyy/MM/dd HH:mm";

		public static final String DATEYM = "yyyyMM";

		public static final String DATEYM_SLASH = "yyyy/MM";

		public static final String HOUR = "HH";

		public static final String MINUTE = "mm";

		public static final String ISO8601_DATE = "yyyy-MM-dd'T'HH:mm:ss";

		public static final String ISO8601_DATE_TZ = "yyyy-MM-dd'T'HH:mm:ssz";

		public static final String CABLE_DIRECT_TIME = "yyyy/MM/dd HH:mm:ss";
	}

	/**
	 * メニューID定義
	 */
	public static class MENU_ID {
		// 見積入力
		public static final String INPUT_ESTIMATE = "0200";
		// 見積検索
		public static final String SEARCH_ESTIMATE = "0201";
		// 単価参照
		public static final String DISP_PRODUCT_PRICE_LIST = "0203";
		// 受注入力
		public static final String INPUT_RORDER = "0300";
		// 受注検索
		public static final String SEARCH_RORDER = "0301";
		// オンライン受注
		public static final String IMPORT_ONLINE_ORDER = "0303";
		// 売上入力
		public static final String INPUT_SALES = "0400";
		// 売上検索
		public static final String SEARCH_SALES = "0401";
		// 売上帳票発行
		public static final String OUTPUT_SALES_REPORT = "0402";
		// 送り状発行
		public static final String OUTPUT_SALES_INVOICE = "0403";
		// 請求検索
		public static final String SEARCH_BILL = "0500";
		// 請求締処理
		public static final String CLOSE_BILL = "0501";
		// 請求書発行
		public static final String MAKE_OUT_BILL = "0502";
		// 入金入力
		public static final String INPUT_DEPOSIT = "0600";
		// 入金検索
		public static final String SEARCH_DEPOSIT = "0601";
		// 配送業者入金取込
		public static final String IMPORT_DELIVERY_DEPOSIT = "0603";
		// 銀行入金取込
		public static final String IMPORT_BANK_DEPOSIT = "0604";
		// 発注入力
		public static final String INPUT_PORDER = "0700";
		// 発注検索
		public static final String SEARCH_PORDER = "0701";
		// 発注書発行
		public static final String MAKE_OUT_PORDER = "0702";
		// 補充発注推奨リスト
		public static final String OUTPUT_RECOMMEND_LIST = "0704";
		// 仕入入力
		public static final String INPUT_PURCHASE = "0800";
		// 仕入検索
		public static final String SEARCH_PURCHASE = "0801";
		// 支払入力
		public static final String INPUT_PAYMENT = "0900";
		// 支払検索
		public static final String SEARCH_PAYMENT = "0901";
		// 入出庫入力
		public static final String INPUT_STOCK = "1000";
		// 入出庫検索
		public static final String SEARCH_STOCK = "1001";
		// 在庫移動入力
		public static final String INPUT_STOCK_TRANSFER = "1002";
		// 在庫表出力
		public static final String OUTPUT_STOCK_REPORT = "1003";
		// 在庫一覧表
		public static final String OUTPUT_STOCK_LIST = "1004";
		// 在庫締処理
		public static final String CLOSE_STOCK = "1005";
		// 在庫締処理
		public static final String DISP_PRODUCT_STOCK_LIST = "1006";
		// 委託入出庫入力
		public static final String INPUT_ENTRUST_STOCK = "1007";
		// 委託入出庫検索
		public static final String SEARCH_ENTRUST_STOCK = "1008";
		// 残高一覧表出力
		public static final String OUTPUT_BALANCE_LIST = "1100";
		// 履歴参照
		public static final String REFERENCE_HISTORY = "1101";
		// 自社情報
		public static final String SETTING_COMPANY = "1200";
		// 在庫管理
		public static final String SETTING_STOCK = "1201";
		// 部門情報
		public static final String SETTING_DEPT = "1202";
		// 社員情報
		public static final String SETTING_USER = "1203";
		// おしらせ編集
		public static final String SETTING_NEWS = "1204";
		// ファイル登録
		public static final String SETTING_FILE = "1205";
		// パスワード変更
		public static final String SETTING_CHANGE_PASSWORD = "1206";
		// 商品マスタ
		public static final String MASTER_PRODUCT = "1300";
		// セット商品マスタ
		public static final String MASTER_PRODUCT_SET = "1301";
		// 顧客マスタ
		public static final String MASTER_CUSTOMER = "1302";
		// 仕入先マスタ
		public static final String MASTER_SUPPLIER = "1303";
		// 割引マスタ
		public static final String MASTER_DISCOUNT = "1305";
		// 棚番マスタ
		public static final String MASTER_RACK = "1306";
		// 倉庫マスタ
		public static final String MASTER_WAREHOUSE = "1307";
		// 区分マスタ
		public static final String MASTER_CATEGORY = "1309";
		// 税率マスタ
		public static final String MASTER_TAX_RATE = "1310";
		// 商品分類マスタ
		public static final String MASTER_PRODUCT_CLASS = "1311";
		// レートマスタ
		public static final String MASTER_RATE = "1313";
		// 顧客ランクマスタ */
		public static final String MASTER_CUSTOMER_RANK = "1314";
		// 銀行マスタ */
		public static final String MASTER_BANK = "1315";
		// ファイル参照
		public static final String REFERENCE_FILES = "1407";
		// 商品ダウンロード/アップロード
		public static final String MASTER_PRODUCT_UPDWN = "1500";
		// マスタリスト
		public static final String REFERENCE_MST = "1102";
	}

	/**
	 * 画面の権限定義
	 */
	public static class MENU_VALID_LEVEL {
		/**
		 * 無効
		 *
		 * 画面：無効<br>
		 * ファイル参照：不可
		 */
		public static final String INVALID = "0";

		/**
		 * 制限付き有効
		 *
		 * 画面：参照のみ<br>
		 * ファイル参照：全社員向けのみ可
		 */
		public static final String VALID_LIMITATION = "1";

		/**
		 * 制限なし有効
		 *
		 * 画面：更新可<br>
		 * ファイル参照：全て可
		 */
		public static final String VALID_FULL = "2";
	}

	/**
	 * 画面の権限タイプ(参照権限の有無)
	 */
	public static class MENU_VALID_TYPE {
		/**
		 * 無効と有効
		 */
		public static final String INVALID_VALID = "0";

		/**
		 * 無効と有効と参照
		 */
		public static final String INVALID_VALID_REFERENCE = "1";
	}

	/**
	 * バッチ実行タイプ定義
	 */
	public static class SCHEDULE_EXEC_TYPE {
		public static final int VALUE_DAYLY = 0;

		public static final String LABEL_DAYLY = "毎日";

		public static final int VALUE_MONTHLY = 1;

		public static final String LABEL_MONTHLY = "毎月";

		public static final int VALUE_LAST_DAY_MONTHLY = 2;

		public static final String LABEL_LAST_DAY_MONTHLY = "毎月最終日";
	}

	/**
	 * コード長定義
	 */
	public static class CODE_SIZE {
		public static final int PRODUCT = 20;

		public static final int CUSTOMER = 15;

		public static final int SUPPLIER = 9;

		public static final int RACK = 10;

		public static final int WAREHOUSE = 10;
	}

	/**
	 * DB内：BIGDECIMAL小数桁最大
	 * DECIMAL 12,3 15,3 等定義の固定値
	 */
	//数量用
	public final static int DECIMAL_NUM_ALIGN_MAX = 3;
	//金額用
	public final static int DECIMAL_PRICE_ALIGN_MAX = 3;
	//税用（仕様上使用しない）

	/**
	 * コードマスク
	 */
	public static class CODE_MASK {
		// 顧客コード
		public static final String CUSTOMER_MASK = "^(?:[\\x20-\\x7E]{0,"
				+ CODE_SIZE.CUSTOMER + "})$";
		// 商品コード
		public static final String PRODUCT_MASK = "^(?:[\\x20-\\x7E]{0,"
				+ CODE_SIZE.PRODUCT + "})$";
		// 仕入先コード
		public static final String SUPPLIER_MASK = "^(?:[\\x20-\\x7E]{0,"
				+ CODE_SIZE.SUPPLIER + "})$";
		// ふりがな（全半カタカナのみ）
		public static final String KANA_MASK = "^[ァ-ヶ｡-ﾟ+]+$";
		// ふりがな（全半カタカナとスペース）
		public static final String KANA_WITH_WHITESPACE_MASK = "^[\\sァ-ヶ｡-ﾟ+]+$";
		// 「ASCII文字」のみ
		public static final String ASCII_ONLY_MASK = "^[\u0020-\u007E]+$";
		// 「ひらがな」のみ
		public static final String ZENHIRA_ONLY_MASK = "^[\u3040-\u309F]+$";
		// 「カタカナ文字」のみ
		public static final String ZENKANA_ONLY_MASK = "^[\u30A0-\u30FF]+$";
		// 「半角カタカナ」のみ
		public static final String HANKANA_ONLY_MASK = "^[\uFF65-\uFF9F]+$";
		// 「全角ASCII文字」のみ
		public static final String ZENASCII_ONLY_MASK = "^[\uFF01-\uFF60]+$";
		// 「全角文字」のみ
		public static final String ZENKAKU_ONLY_MASK = "^[^ -~｡-ﾟ]*$";
		// ASCII文字でない文字が存在
		public static final String NOTASCII_EXIST_MASK = ".*[^\u0020-\u007E].*";
		// 半角英字でない文字が存在
		public static final String NOTHANALPHA_EXIST_MASK = ".*[^A-Za-z].*";
		// 半角数字でない文字が存在
		public static final String NOTHANNUM_EXIST_MASK = ".*[^0-9].*";
		// 半角カタカナが存在
		public static final String HANKANA_EXIST_MASK = ".*[\uFF65-\uFF9F].*";
		//半角のみ
		public static final String HANKAKU_MASK = "^[a-zA-Z0-9-/:-@\\[-\\^\\{-\\~]+$";
	}

	public static class NUMBER_MASK {
		// 設計書記載桁数

		// INTEGER 整数部桁数
		// 4形式（小数点以下切捨て仕様の数量）
		public static final String INTEGER4 = "^((-?[1-9]\\d{0,3})|(0))?$";
		// DECIMAL 整数部桁数+小数部桁数
		// 8+2形式（円単価等）
		public static final String DECIMAL8_2 = "^(?:-?(?:(?:[1-9]\\d{0,7})|(0))(?:\\.\\d{1,2})?)?$";
		// 11+2形式(金額(円)等)
		public static final String DECIMAL11_2 = "^(?:-?(?:(?:[1-9]\\d{0,10})|(0))(?:\\.\\d{1,2})?)?$";
		// 5+3形式(レートマスタ管理 レート定義DECIMAL(8,3))
		public static final String DECIMAL5_3 = "^(?:-?(?:(?:[1-9]\\d{0,4})|(0))(?:\\.\\d{1,3})?)?$";

		// テーブル定義書記載桁数
		public static final String DECIMAL12_3 = "^(?:-?(?:(?:[1-9]\\d{0,11})|(0))(?:\\.\\d{1,3})?)?$";
		public static final String DECIMAL15_3 = "^(?:-?(?:(?:[1-9]\\d{0,14})|(0))(?:\\.\\d{1,3})?)?$";

		// システム制約桁数(仮) MySQL の DECIMAL 最大幅(-はMに含まない)M=255,D=30(D<=M-2) に安全な224+30形式
		public static final String DECIMAL = "^(?:-?(?:(?:[1-9]\\d{0,223})|(0))(?:\\.\\d{1,30})?)?$";
	}

	/**
	 * 得意先関連区分
	 */
	public static class CUSTOMER_REL {
		// 納入先
		public static final String DELIVERY = "01";
		// 請求先
		public static final String BILLING = "02";
	}

	/**
	 * 登録元機能
	 */
	public static class SRC_FUNC {
		// 売上
		public static final String SALES = "0";
		// 仕入
		public static final String PURCHASE = "1";
		// 在庫入力
		public static final String STOCK = "2";
		// 在庫移動
		public static final String STOCK_TRANSFER = "3";
		// 売上
		public static final String LABEL_SALES = "売上";
		// 仕入
		public static final String LABEL_PURCHASE = "仕入";
		// 在庫入力
		public static final String LABEL_STOCK = "在庫入力";
		// 在庫移動
		public static final String LABEL_STOCK_TRANSFER = "在庫移動";
	}

	/**
	 * 検索対象プルダウン選択値 （LABELとVALUEをペアで登録）
	 */
	public static class SEARCH_TARGET {
		// VALUE：伝票
		public static final String VALUE_SLIP = "1";
		// VALUE：明細
		public static final String VALUE_LINE = "2";

		// LABEL：伝票
		public static final String LABEL_SLIP = "伝票";
		// LABEL：明細
		public static final String LABEL_LINE = "明細";
	}

	/**
	 * 残高一覧出力対象プルダウン選択値 （LABELとVALUEをペアで登録）
	 */
	public static class OUTPUT_BALANCE_TARGET {
		// VALUE:買掛残高一覧
		public static final String VALUE_PORDER = "1";
		// VALUE:売掛残高一覧
		public static final String VALUE_RORDER = "2";

		// LABEL:買掛残高一覧
		public static final String LABEL_PORDER = "買掛残高一覧表";
		// LABEL:売掛残高一覧
		public static final String LABEL_RORDER = "売掛残高一覧表";
	}

	/**
	 * 履歴参照出力対象プルダウン選択値 （LABELとVALUEをペアで登録）
	 */
	public static class REFERENCE_HISTORY_TARGET {
		// VALUE:見積
		public static final String VALUE_ESTIMATE = "1";
		// VALUE:受注
		public static final String VALUE_RORDER = "2";
		// VALUE:売上
		public static final String VALUE_SALES = "3";
		// VALUE:入金
		public static final String VALUE_DEPOSIT = "4";
		// VALUE:発注
		public static final String VALUE_PORDER = "5";
		// VALUE:仕入
		public static final String VALUE_PURCHASE = "6";
		// VALUE:支払
		public static final String VALUE_PAYMENT = "7";
		// VALUE:入出庫
		public static final String VALUE_STOCK = "8";
		// VALUE:顧客
		public static final String VALUE_CUSTOMER = "9";
		// VALUE:商品
		public static final String VALUE_PRODUCT = "10";
		// VALUE:仕入先
		public static final String VALUE_SUPPLIER = "11";
		// VALUE:社員
		public static final String VALUE_USER = "12";

		// LABEL:見積
		public static final String LABEL_ESTIMATE = "見積入力ログ";
		// LABEL:受注
		public static final String LABEL_RORDER = "受注入力ログ";
		// LABEL:売上
		public static final String LABEL_SALES = "売上入力ログ";
		// LABEL:入金
		public static final String LABEL_DEPOSIT = "入金入力ログ";
		// LABEL:発注
		public static final String LABEL_PORDER = "発注入力ログ";
		// LABEL:仕入
		public static final String LABEL_PURCHASE = "仕入入力ログ";
		// LABEL:支払
		public static final String LABEL_PAYMENT = "支払入力ログ";
		// LABEL:入出庫
		public static final String LABEL_STOCK = "入出庫入力ログ";
		// LABEL:顧客
		public static final String LABEL_CUSTOMER = "顧客マスタ変更履歴";
		// LABEL:商品
		public static final String LABEL_PRODUCT = "商品マスタ変更履歴";
		// LABEL:仕入先
		public static final String LABEL_SUPPLIER = "仕入先マスタ変更履歴";
		// LABEL:社員
		public static final String LABEL_USER = "社員情報変更履歴";
	}

	public static class REFERENCE_MST_ACTION_TYPE {
		// VALUE:指定なし
		public static final String VALUE_NONE =  "";

		// VALUE:INSERT
		public static final String VALUE_INSERT =  "INSERT";

		// VALUE:UPDATE
		public static final String VALUE_UPDATE =  "UPDATE";

		// VALUE_DELETE
		public static final String VALUE_DELETE =  "DELETE";

		// LABEL:指定なし
		public static final String LABEL_NONE =  "全て";

		// LABEL:INSERT
		public static final String LABEL_INSERT =  "INSERT";

		// LABEL:UPDATE
		public static final String LABEL_UPDATE =  "UPDATE";

		// LABEL_DELETE
		public static final String LABEL_DELETE =  "DELETE";

	}

	/**
	 * マスタリスト出力対象プルダウン選択値 （LABELとVALUEをペアで登録）
	 */
	public static class REFERENCE_MST_TARGET {
		// VALUE:顧客
		public static final String VALUE_CUSTOMER = "1";

		// LABEL:顧客
		public static final String LABEL_CUSTOMER = "顧客マスタリスト";
	}

	/**
	 * ファイルサイズ
	 */
	public static class FILE_SIZE {
		public static final int BLOCK_SIZE = 1024;

		public static final String KB = "KB";
	}

	/**
	 * 仕入伝票ステータス
	 */
	public static class STATUS_SUPPLIER_SLIP {
		// 未払い
		public static final String UNPAID = "0";
		// 支払中
		public static final String PAYING = "1";
		// 支払済
		public static final String PAID = "9";
	}

	/**
	 * 仕入伝票明細行ステータス
	 */
	public static class STATUS_SUPPLIER_LINE {
		// 未払い
		public static final String UNPAID = "0";
		// 支払済
		public static final String PAID = "9";
	}

	/**
	 * 支払伝票ステータス
	 */
	public static class STATUS_PAYMENT_SLIP {
		// 支払済
		public static final String PAID = "0";
		// 支払締め
		public static final String CUTOFF = "9";
	}

	/**
	 * 支払伝票明細行ステータス
	 */
	public static class STATUS_PAYMENT_LINE {
		// 支払済
		public static final String PAID = "0";
		// 支払締め
		public static final String CUTOFF = "9";
	}

	/**
	 * 入金伝票ステータス
	 */
	public static class STATUS_DEPOSIT_SLIP {
		// 入金
		public static final String PAID = "0";
		// 入金締め（請求締め）
		public static final String CUTOFF = "9";
	}

	/**
	 * 入金伝票明細行ステータス
	 */
	public static class STATUS_DEPOSIT_LINE {
		// 入金
		public static final String PAID = "0";
		// 入金締め（請求締め）
		public static final String CUTOFF = "9";
	}

	/**
	 * 発注伝票ステータス
	 */
	public static class STATUS_PORDER_SLIP {
		// 発注
		public static final String ORDERED = "0";
		// 委託在庫生産中
		public static final String NOW_ENTRUST_STOCK_MAKING = "2";
		// 仕入中
		public static final String NOWPURCHASING = "1";
		// 仕入完了
		public static final String PURCHASED = "9";
	}

	/**
	 * 発注伝票明細行ステータス
	 */
	public static class STATUS_PORDER_LINE {
		// 発注
		public static final String ORDERED = "0";
		// 委託在庫生産完了
		public static final String ENTRUST_STOCK_MAKED = "2";
		// 委託在庫出庫完了
		public static final String ENTRUST_STOCK_DELIVERED = "3";
		// 分納中
		public static final String NOWPURCHASING = "1";
		// 仕入完了
		public static final String PURCHASED = "9";
	}

	/**
	 * 受注伝票ステータス
	 */
	public static class STATUS_RORDER_SLIP {
		// 受注
		public static final String RECEIVED = "0";
		// 売上中
		public static final String SALES_NOW = "1";
		// 売上完了
		public static final String SALES_FINISH = "9";
	}

	/**
	 * 受注伝票明細行ステータス
	 */
	public static class STATUS_RORDER_LINE {
		// 受注
		public static final String RECEIVED = "0";
		// 分納中
		public static final String NOWPURCHASING = "1";
		// 売上完了
		public static final String SALES_FINISH = "9";
	}

	/**
	 * 特殊商品コード
	 */
	public static class EXCEPTIANAL_PRODUCT_CODE {
		// XXXXXXXXXA オンライン配送料
		public static final String ONLINE_DELIVERY_PRICE = "XXXXXXXXXA";
		// XXXXXXXXXC 配送料
		public static final String DELIVERY_PRICE = "XXXXXXXXXC";
		// XXXXXXXXXD オンライン手数料
		public static final String ONLINE_FEE_PRICE = "XXXXXXXXXD";
		// XXXXXXXXXE 海外チャーター費用
		public static final String OVERSEAS_CHARTER_PRICE = "XXXXXXXXXE";
		// XXXXXXXXXH システム送料手数料相殺値引き
		public static final String DELIVERY_DISCOUNT_PRICE = "XXXXXXXXXH";
		// XXXXXXXXXJ 超速便代
		public static final String CHOSOKU_PRICE = "XXXXXXXXXJ";
		// XXXXXXXXXK 手数料　未使用
		public static final String SERVICE_CHARGE = "XXXXXXXXXK";
		// XXXXXXXXXM 返品分値引き　未使用
		public static final String RGU_DISCOUNT_PRICE = "XXXXXXXXXM";
		// XXXXXXXXXV パートナー値引き　未使用
		public static final String PARTNER_DISCOUNT_PRICE = "XXXXXXXXXV";
		// XXXXXXXXXW クレジットカードシステム利用料　未使用
		public static final String CREDIT_CARD_FEE_PRICE = "XXXXXXXXXW";
		// XXXXXXXXXZ アイテム総数値引き
		public static final String ITEM_DISCOUNT_PRICE = "XXXXXXXXXZ";
		// ZZZZZZZZZX お取引実績による値引き 未使用！
		public static final String SALES_DISCOUNT_PRICE = "ZZZZZZZZZX";
		// ZZZZZZZZZY 代引手数料
		public static final String COD_FEE_PRICE = "ZZZZZZZZZY";
		// ZZZZZZZZZZ 送料
		public static final String POSTAGE_PRICE = "ZZZZZZZZZZ";
	}
	public static String[] EXCEPTIANAL_PRODUCT_CODE_LIST = {
			EXCEPTIANAL_PRODUCT_CODE.ONLINE_DELIVERY_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.DELIVERY_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.ONLINE_FEE_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.OVERSEAS_CHARTER_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.DELIVERY_DISCOUNT_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.CHOSOKU_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.SERVICE_CHARGE,
			EXCEPTIANAL_PRODUCT_CODE.RGU_DISCOUNT_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.PARTNER_DISCOUNT_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.CREDIT_CARD_FEE_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.ITEM_DISCOUNT_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.SALES_DISCOUNT_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.COD_FEE_PRICE,
			EXCEPTIANAL_PRODUCT_CODE.POSTAGE_PRICE
	};


	/**
	 * 初期値マスタ未設定時のデフォルト値
	 */
	public static class DEFAULT_INIT_VALUE {

		// 汎用－敬称 → １：様
		public static final String PC_PRE_CATEGORY = "1";
		// 汎用－税転嫁 → ３：外税締単位
		public static final String TAX_SHIFT_CATEGORY = "3";
		// 汎用－税端数処理 → ０：切り捨て
		public static final String TAX_FRACT_CATEGORY = "0";
		// 汎用－単価端数処理 → ０：切り捨て
		public static final String PRICE_FRACT_CATEGORY = "0";

		// 顧客マスタ－取引区分 → １：掛売
		public static final String CUSTOMER_SALES_CM_CATEGORY = CategoryTrns.SALES_CM_CREDIT;
		// 顧客マスタ－売上伝票種別 → ０：売上伝票（ページ６行）
		public static final String CUSTOMER_SALES_SLIP_CATEGORY = "0";

		// 仕入先マスタ－取引区分 → １：掛仕
		public static final String SUPPLIER_CM_CATEGORY = CategoryTrns.SUPPLIER_CM_CREDIT;
		// 仕入先マスタ－支払方法 → ３：振込
		public static final String SUPPLIER_PAYMENT_TYPE_CATEGORY = "3";
		// 仕入先マスタ－支払間隔 → ０：当月
		public static final String SUPPLIER_PAYMENT_CYCLE_CATEGORY = "0";
		// 仕入先マスタ－振込方法 → １：電信
		public static final String SUPPLIER_TRANSFER_TYPE_CATEGORY = "1";
		// 仕入先マスタ－手数料負担 → ０：当社負担
		public static final String SUPPLIER_SERVICE_CHARGE_CATEGORY = "0";
	}

	public static class CUTOFF_GROUP {
		// 支払条件：その他
		public static final String CUTOFF_GROUP_OTHERS = "999";
	}

	public static class ONLINE_CUSTOMER_CSV {

		// 項目数
		public static final int COLUMN_COUNT_MIN = 21;

		// 項目数
		public static final int COLUMN_COUNT_MAX = 46;

		// 顧客属性 => 1:新規登録法人
		public static final String CUSTOMER_REGISTER = "1";

		// 顧客属性 => 2:登録しない
		public static final String CUSTOMER_NOT_REGISTERED = "2";

		// 顧客属性 => 3:個人
		public static final String CUSTOMER_PERSONAL = "3";

		// 請求書送付先 => 1:お客様情報と同じ
		public static final String BILL_TO_SAME_CUSTOMER = "1";

		// 請求書送付先 => 3:経理課御中
		public static final String BILL_TO_ACCOUNTING_SECTION = "3";

		// 経理課
		public static final String BILL_TO_ACCOUNTING_SECTION_NAME = "経理課";

		// ケイリカ
		public static final String BILL_TO_ACCOUNTING_SECTION_KANA = "ケイリカ";

		// 支払条件マップ
		public static final Map<String, String> CUTOFF_GROUP_MAP = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("1", "101");
				put("2", "201");
				put("3", "251");
				put("4", "311");
				put("5", "312");
				put("6", "999");
				put("7", "999");
				put("8", "999");
			}
		};
	}

	/**
	 * オンラインデータ情報
	 */
	public static class ONLINE_ORDER_FILE {

		// 項目数
		public static final int COLUMN_COUNT = 29;
	}

	/**
	 * 郵便番号データファイル
	 *
	 */
	public static class ZIP_CODE_CSV {
		// 項目数
		public static final int COLUMN_COUNT = 15;

		public static final String CASE_1 = "ない場合";

		public static final String CASE_2 = "くる場合";
	}

	/**
	 * 帳票選択プルダウン選択値 （LABELとVALUEをペアで登録）
	 */
	public static class REPORT_SELECTION {
		// VALUE：全ての帳票
		public static final String VALUE_ALL = "1";
		// VALUE：請求書(個別発行で商品に同梱の場合)
		public static final String VALUE_BILL = "2";
		// VALUE：納品書
		public static final String VALUE_DELIVERY = "3";
		// VALUE：仮納品書
		public static final String VALUE_TEMP_DELIVERY = "4";
		// VALUE：ピッキングリスト
		public static final String VALUE_PICKING = "5";
		// VALUE：見積書
		public static final String VALUE_ESTIMATE = "6";
		// VALUE：納品書兼領収書
		public static final String VALUE_DELIVERY_RECEIPT = "7";

		// VALUE：ピッキングリスト・組立指示書
		public static final String VALUE_PICKING_CONSTRACTION = "8";
		// VALUE：納品書６行
		public static final String VALUE_DELIVERY_6 = "9";
		// VALUE：請求書(個別発行で郵送の場合)
		public static final String VALUE_BILL_POST = "10";

		// LABEL：全ての帳票
		public static final String LABEL_ALL = "全ての帳票";
		// LABEL：請求書
		public static final String LABEL_BILL = "請求書";
		// LABEL：納品書
		public static final String LABEL_DELIVERY = "納品書";
		// LABEL：仮納品書
		public static final String LABEL_TEMP_DELIVERY = "仮納品書";
		// LABEL：ピッキングリスト
		public static final String LABEL_PICKING = "ピッキングリスト";
		// LABEL：見積書
		public static final String LABEL_ESTIMATE = "見積書";
		// LABEL：納品書兼領収書
		public static final String LABEL_DELIVERY_RECEIPT = "納品書兼領収書";
	}

	/**
	 * 状態選択プルダウン選択値 （LABELとVALUEをペアで登録）
	 */
	public static class STATUS_SELECTION {
		// VALUE：全て
		public static final String VALUE_ALL = "1";
		// VALUE：未出力
		public static final String VALUE_UNOUTPUT = "2";
		// VALUE：出力済
		public static final String VALUE_OUTPUT = "3";

		// LABEL：全て
		public static final String LABEL_ALL = "全て";
		// LABEL：未出力
		public static final String LABEL_UNOUTPUT = "未出力";
		// LABEL：出力済
		public static final String LABEL_OUTPUT = "出力済";
	}

	public static class CATEGORY_DATA_TYPE {
		// なし
		public static final String CATEGORY_DATA_TYPE_NONE = "0";

		// 文字列
		public static final String CATEGORY_DATA_TYPE_STRING = "1";

		// 整数
		public static final String CATEGORY_DATA_TYPE_NUMBER = "2";

		// 小数
		public static final String CATEGORY_DATA_TYPE_FLOAT = "3";

		// 論理値
		public static final String CATEGORY_DATA_TYPE_BOOL = "4";
	}

	/**
	 * 棚番管理しない棚番名
	 *
	 */
	public static class RACK_NAME {
		// 重複登録許可
		public static final String MULTI_ON = "重複登録可能";

		// その他
		public static final String OTHERS = "その他";
	}


	/**
	 * 特殊顧客コード
	 */
	public static class EXCEPTIANAL_CUSTOMER_CODE {
		// 受注伝票において納入先情報を編集可能とする顧客：　空文字列不可
		public static final String ONLINE_ORDER = "undefined";
	}

	/**
	 * 出力帳票種別
	 */
	public static class REPORT_FORMAT{
		// Excel
		public static final String EXCEL = "excel";

		// PDF
		public static final String PDF = "pdf";
	}

	public static class REPORT_TEMPLATE{
		// 御見積書（売上画面）
		public static final String REPORT_ID_A = "0000A";
		// 御見積書（見積もり画面）
		public static final String REPORT_ID_B = "0000B";
		// 納品書（売上画面）
		public static final String REPORT_ID_C = "0000C";
		// 納品書６行（売上画面）
		public static final String REPORT_ID_D = "0000D";
		// 仮納品書（売上画面）
		public static final String REPORT_ID_E = "0000E";
		// 納品書兼領収書（売上画面）
		public static final String REPORT_ID_F = "0000F";
		// 請求書　個別発行で商品に同梱（売上画面）
		public static final String REPORT_ID_G = "0000G";
		// 請求書　個別発行で郵送（売上画面）
		public static final String REPORT_ID_H = "0000H";
		// 請求書　月締め発行（請求締画面）
		public static final String REPORT_ID_I = "0000I";
		// ピッキングリスト（売上画面）
		public static final String REPORT_ID_J = "0000J";
		// 組立指示書（売上画面）
		public static final String REPORT_ID_K = "0000K";
		// 注文書（発注画面）
		public static final String REPORT_ID_L = "0000L";
		// 送り状
		public static final String SI = "SI";
	}

	/**
	 * 送り状データファイル名
	 *
	 */
	public static class INVOICE_FILENAME{
		public static final String INVOICE_FILENAME = "送り状データ.xls";
	}

	/**
	 *  配送業者入金取込
	 *
	 */
	public static class DELIVERY_DEPOSIT_CSV{
		// 項目数
		public static final int DEPOSIT_COLUMN_COUNT = 15;
		public static final int INVOICE_COLUMN_COUNT = 73;

		// 状態
		public static final String STATUS_DEPOSIT_ONLY = "入金データのみ";
		public static final String STATUS_INVOICE_ONLY = "送り状のみ";
		public static final String STATUS_NOREL_SALES = "関連売上なし";
		public static final String STATUS_OLD = "登録済";
		public static final String STATUS_NEW = "新規登録";
		public static final String STATUS_DEL = "取込後削除";

		//配送業者入金データ データ区分「20（返品）」
		public static final String DATA_CATEGORY_RETURN_GOODS="20";

	}

	/**
	 *  銀行入金取込
	 *
	 */
	public static class BANK_DEPOSIT_CSV{
		// 項目数
		public static final int DEPOSIT_COLUMN_COUNT = 6;

		// 状態
		public static final String STATUS_UNKNOWN = "振込不明";
		public static final String STATUS_OLD = "登録済";
		public static final String STATUS_NEW = "新規登録";
		public static final String STATUS_NONPRICE = "金額不一致";

		public static final String CHANGE_NAME_NON = "無し";
		public static final String CHANGE_NAME_YES = "有り";
	}

	/**
	 * 数値の限界値
	 *
	 */
	public static class LIMIT_VALUE{
		// 金額 最大値
		public static final Integer PRICE_MAX = 999999999;
		// 金額 最小値
		public static final Integer PRICE_MIN = -999999999;
	}

	/**
	 * リストの最大値設定用
	 *
	 */
	public static class LIST_MAX{
		// 請求書印刷用
		public static final Integer BILL_PRINT = 300;

		// 発注書発行用
		public static final Integer PORDER_PRINT = 300;
	}

	/**
	 *  取引区分　コード名称
	 *
	 */
	public static class SALES_CM_CATEGORY_NAME{

		public static final String CATEGORY_CREDIT = "掛売";
		public static final String CATEGORY_CREDIT_CARD = "クレジット";
	}
	/**
	 * 請求書検索　チェックボックス選択時の値
	 */
	public static class SEARCH_BILL {
		// 繰越金：なし
		public static final String CARRY_OVER_ZERO = "2";
		// 繰越金：過入金⇒マイナス
		public static final String CARRY_OVER_MINUS = "1";
		// 繰越金：不足⇒プラス
		public static final String CARRY_OVER_PLUS = "3";
		// 今回請求額：あり
		public static final String BILL_PRICE_PLUS = "3";
		// 今回請求額：なし
		public static final String BILL_PRICE_ZERO = "2";
		// 今回請求額：過入金
		public static final String BILL_PRICE_MINUS = "1";
	}

	/**
	 *  取引区分　コード名称
	 *
	 */
	public static class BILL_CUTOFF_GROUP_NAME{

		public static final String CUTOFF_END = "月末";
	}

	/**
	 *  ページあたりの表示件数
	 *
	 */
	public static class PAGE_VIEW_COUNT{

		public static final Integer VIEW_10 = 100;
		public static final Integer VIEW_50 = 50;
		public static final Integer VIEW_100 = 100;
		public static final Integer VIEW_INIT = VIEW_100;
	}
	/**
	 *  有効無効フラグ　
	 *
	 */
	public static class VALID_FLAG{

		public static final String VALID = "1";
		public static final String INVALID = "0";
	}

	/**
	 * 税率が0%の場合の値
	 */
	public static class TAX_ZERO_VALUE{
		public static final String ZERO = "0.000";
		public static final String ZEROLABEL = "消費税なし";
	}
}
