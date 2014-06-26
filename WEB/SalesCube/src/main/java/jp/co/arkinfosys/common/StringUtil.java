/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.validator.DateValidator;

/**
 * 文字列を処理するユーティリティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public final class StringUtil {

	/**
	 * 振込名義人変換元定義
	 */
	private static final char[] PAYMENT_NAME_SRC = { ' ', 'ー', 'ァ', 'ィ', 'ゥ', 'ェ', 'ォ', 'ッ', 'ャ', 'ュ', 'ョ' };

	/**
	 * 振込名義人変換先定義
	 */
	private static final char[] PAYMENT_NAME_DEST = { '　', '－', 'ア', 'イ', 'ウ', 'エ', 'オ', 'ツ', 'ヤ', 'ユ', 'ヨ' };

	/**
	 * 振込名義人削除文字列定義
	 */
	private static final String[] PAYMENT_NAME_DEL = { "　", "カブシキガイシヤ", "（カブ）", "カブ）", "（カブ", "（カ）", "（カ", "）カ", "カ）", "カ（", "．カ", "カ．",
														"ユウゲンガイシヤ", "（ユウ）", "ユウ）", "（ユウ", "（ユ）", "（ユ", "）ユ", "ユ）", "ユ（", "．ユ", "ユ．",
														"（ガク）", "（ガク", "ガク）", "．", "・", "（", "）", "" };

	/**
	 * URLエンコード時の禁則文字
	 */
	private static final String[] URL_ENC_TARGET={"/", "\\."};

	/**
	 * URLエンコード時の禁則文字代替
	 */
	private static final String[] URL_ENC_DMY={"-#0#-", "-#1#-"};

	/**
	 * 文字列がnullかつ空文字列ではない場合にtrueを返します.
	 * @param str　文字列
	 * @return　true:文字列がnullかつ空文字列ではない
	 */
	public static boolean hasLength(String str) {
		return str != null && str.length() > 0;
	}

	/**
	 * 引数の表示形式にフォーマットされた現在日を取得します.
	 * @param pattern　日付に適用する表示形式(例：yyyy/MM/dd)
	 * @return String フォーマットされた現在日
	 */
	public static String getCurrentDateString(String pattern) {
		Calendar ca = Calendar.getInstance();

		return getDateString(pattern, ca.getTime());
	}

	/**
	 * 引数の表示形式にフォーマットされた日付を取得します
	 * @param pattern　日付に適用する表示形式(例：yyyy/MM/dd)
	 * @param date 日付
	 * @return String フォーマットされた日付
	 */
	public static String getDateString(String pattern, Date date) {
		if (date == null) {
			return "";
		}
		String dateStr = "";
		SimpleDateFormat sdf = new SimpleDateFormat();
		try {
			sdf.applyPattern(pattern);
			dateStr = sdf.format(date);
		} catch (Exception e) {
			return "";
		}

		return dateStr;
	}

	/**
	 * カラム名を変換します（Entity→DB）.
	 * @param temp カラム名
	 * @return　変換したカラム名
	 */
	public static String convertColumnName(String temp) {
		if (temp.matches(".*([^a-zA-Z0-9]).*")) {
			return temp;
		}

		while (!(temp.equals(temp.replaceAll(
				"^([a-z][a-zA-Z0-9_]*[a-z0-9])([A-Z][a-zA-Z0-9_]*)$", "$1_$2")))) {
			temp = temp.replaceAll(
					"^([a-z][a-zA-Z0-9_]*[a-z0-9])([A-Z][a-zA-Z0-9_]*)$",
					"$1_$2");
		}

		return temp.toUpperCase();
	}

	/**
	 * 年月形式（yyyy/MM）の文字列かを判定します.
	 * @param arg 評価したい文字列
	 * @return 年月形式であるか否か
	 */
	public static boolean isYmString(String arg) {
		// 日にちは1日固定にして、日付型チェックを行う
		StringBuffer sb = new StringBuffer(arg);
		sb.append("/01");

		DateValidator validator = DateValidator.getInstance();
		return validator.isValid(sb.toString(), Constants.FORMAT.DATE, true);
	}

	/**
	 * オブジェクトをStringに変換します.<BR>
	 *  nullの場合はnullを返します.
	 * @param arg　オブジェクト
	 * @return Stringに変換したオブジェクト
	 */
	public static String valueOf(Object arg) {
		if (arg == null) {
			return null;
		}
		return String.valueOf(arg);
	}

	/**
	 * 先頭と末尾の半角空白・全角空白を削除します.
	 * @param str 文字列
	 * @return　空白を削除した文字列
	 */
	public static String trimBlank(String str) {
		if (str == null) {
			return str;
		}
		return str.replaceAll("^[\\s　]+", "").replaceAll("[\\s　]+$", "");
	}

	/**
	 * CSV値の先頭・末尾のダブルクウォートを削除します.
	 * @param value　CSV値
	 * @return　ダブルクウォートを削除した文字列
	 */
	public static String removeQuote(String value) {
		String result = value;
		if (result.startsWith("\"")) {
			result = result.substring(1);
		}
		if (result.endsWith("\"")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * 日付文字列をタイムゾーン部分を考慮しない形で Date型変換できるようタイムゾーン部分を削除した形式のStringにして返します.<BR>
	 * (例)「2010-01-20 15:20:25+09:00」を「2010-01-20 15:20:25」にします.
	 * @param str　日付文字列
	 * @return　変換した文字列
	 */
	public static String removeTimeZone(String str) {
		if (str == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer(str);
		int start = sb.lastIndexOf("+");
		int end = sb.length();
		if (start != -1) {
			sb.deleteCharAt(start);
			sb.delete(start, end);
		}

		return sb.toString();
	}

	/**
	 * 振込名義人の変換をします.
	 * @param str　振込名義人文字列
	 * @return　変換した振込名義人文字列
	 */
	public static String convertPaymentName(String str) {
		String result = str;

		// 前後の空白を削除する
		result = trimBlank(result);

		// nullの場合、処理しない
		if (result == null) {
			return null;
		}


		// 変換定義に従って変換する
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length(); i++) {
			char c = result.charAt(i);
			for(int j=0; j<PAYMENT_NAME_SRC.length; j++) {
				if(c == PAYMENT_NAME_SRC[j]) {
					c = PAYMENT_NAME_DEST[j];
				}
			}
			sb.append(c);
		}
		result =  sb.toString();

		// 削除文字列定義に従って文字列削除する
		for (int i = 0; i < PAYMENT_NAME_DEL.length; i++) {
			result = result.replace(PAYMENT_NAME_DEL[i], "");
		}

		return result;
	}

	/**
	 * 文字列中のスラッシュをエンコードします.
	 * @param val　文字列
	 * @return　スラッシュをエンコードした文字列
	 *
	 */
	public static String encodeSL(String val) {
		String ret = val;
		for (int i=0;i<URL_ENC_TARGET.length;i++) {
			String dmy = ret.replaceAll(StringUtil.URL_ENC_TARGET[i], StringUtil.URL_ENC_DMY[i]);
			ret = dmy;
		}
		return ret;
	}

	/**
	 * 文字列中のスラッシュをデコードします.
	 * @param val　文字列
	 * @return　スラッシュをデコードした文字列
	 */
	public static String decodeSL(String val) {
		String ret = val;
		for (int i=0;i<URL_ENC_TARGET.length;i++) {
			String dmy = ret.replaceAll(StringUtil.URL_ENC_DMY[i], StringUtil.URL_ENC_TARGET[i]);
			ret = dmy;
		}
		return ret;
	}

	/**
	 * 文字列をバイト数で切り出します.<BR>
	 * 指定バイト数境界を２バイト文字コードが跨ぐ場合には、1文字少なく返します.
	 * @param srcString　切り出し元文字列
	 * @param length　切り出しバイト数
	 * @return　切り出し結果
	 */
	public static String substringByte(String srcString,int length ){
		  int dstlength = 0;
		  for(int i = 0; i < srcString.length() ; i++){
			  dstlength += (srcString.charAt(i) <= 0xff ? 1 : 2);
			  if(dstlength == length){
				  return srcString.substring(0,i+1);
			  }else if(dstlength > length){
				  return srcString.substring(0,i);
			  }
		  }
		  return srcString;
		}

	/**
	 * 半角文字をを全角に変換します。.<BR>
	 * @param s　切り出し元文字列
	 * @return　半角数字
	 */
	  public static String hankakuNumberToZenkakuNumber(String s) {
			if (s == null) {
				return null;
			}
		    StringBuffer sb = new StringBuffer(s);
		    for (int i = 0; i < s.length(); i++) {
		      char c = s.charAt(i);
		      if (c >= '0' && c <= '9') {
		        sb.setCharAt(i, (char) (c - '0' + '０'));
		      }
		    }
		    return sb.toString();
		  }


		  /**
		   * 全角数字を半角に変換します。
		   * @param s 変換元文字列
		   * @return 変換後文字列
		   */
	 public static String zenkakuNumToHankaku(String s) {
			if (s == null) {
				return null;
			}
		    StringBuffer sb = new StringBuffer(s);
		    for (int i = 0; i < sb.length(); i++) {
		      char c = sb.charAt(i);
		      if (c >= '０' && c <= '９') {
		        sb.setCharAt(i, (char)(c - '０' + '0'));
		      }
		    }
		    return sb.toString();
	}

	  /**
	   * 現在日付を文字列で取得します。
	   *
	   * @return 日付文字列
	   */
    public static String getDateString(){

         Calendar cal = Calendar.getInstance();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
         String strDate = sdf.format(cal.getTime());

         return strDate;

     }


}
