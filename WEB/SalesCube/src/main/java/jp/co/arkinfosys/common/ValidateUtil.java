/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.validator.DateValidator;
import org.apache.struts.action.ActionMessage;


/**
 * 検証時に使用するユーティリティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public final class ValidateUtil {

	private static final DateValidator dateValidator = DateValidator.getInstance();

	/**
	 * 必須チェックを行います.<BR>
	 * (メッセージキーは「errors.required」固定).
	 * @param value 検証対象の値
	 * @param label 検証対象のラベル
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage required(String value, String label) {
		return required(value, "errors.required", new Object[] { label });
	}

	/**
	 * 必須チェックを行います.
	 * @param value 検証対象の値
	 * @param mssageKey エラーメッセージのキー値
	 * @param mssageValues エラーメッセージの埋め込み文字配列
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage required(String value, String mssageKey, Object[] mssageValues) {
		if(!StringUtil.hasLength(value)) {
			return new ActionMessage(mssageKey, mssageValues);
		}
		return null;
	}

	/**
	 * 最大長チェックを行います.<BR>
	 * (メッセージキーは「errors.maxlength」固定).
	 * @param value 検証対象の値
	 * @param length 長さ(この値を超えたらエラー)
	 * @param label 検証対象のラベル
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage maxlength(String value, int length, String label) {
		return maxlength(value, length, "errors.maxlength", new Object[] { label, length });
	}

	/**
	 * 最大長チェックを行います.
	 * @param value 検証対象の値
	 * @param length 長さ(この値を超えたらエラー)
	 * @param mssageKey エラーメッセージのキー値
	 * @param mssageValues エラーメッセージの埋め込み文字配列
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage maxlength(String value, int length, String mssageKey, Object[] mssageValues) {
		if(!StringUtil.hasLength(value)) {
			return null;
		}
		if(value.length() > length) {
			return new ActionMessage(mssageKey, mssageValues);
		}
		return null;
	}

	/**
	 * 最小長チェックを行います.<BR>
	 * (メッセージキーは「errors.minlength」固定).
	 * @param value 検証対象の値
	 * @param length 長さ(この値を下回ったらエラー)
	 * @param label 検証対象のラベル
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage minlength(String value, int length, String label) {
		return minlength(value, length, "errors.minlength", new Object[] { label, length });
	}

	/**
	 * 最小長チェックを行います.
	 * @param value 検証対象の値
	 * @param length 長さ(この値を下回ったらエラー)
	 * @param mssageKey エラーメッセージのキー値
	 * @param mssageValues エラーメッセージの埋め込み文字配列
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage minlength(String value, int length, String mssageKey, Object[] mssageValues) {
		if(!StringUtil.hasLength(value)) {
			return null;
		}
		if(value.length() < length) {
			return new ActionMessage(mssageKey, mssageValues);
		}
		return null;
	}

	/**
	 * 桁数チェックを行います.<BR>
	 * (メッセージキーは「errors.length」固定).
	 * @param value 検証対象の値
	 * @param length 長さ(この値と同じでなければエラー)
	 * @param label 検証対象のラベル
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage equalLength(String value, int length, String label) {
		return equalLength(value, length, "errors.length", new Object[] { label, length });
	}

	/**
	 * 桁数チェックを行います.
	 * @param value 検証対象の値
	 * @param length 長さ(この値と同じでなければエラー)
	 * @param mssageKey エラーメッセージのキー値
	 * @param mssageValues エラーメッセージの埋め込み文字配列
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage equalLength(String value, int length, String mssageKey, Object[] mssageValues) {
		if(!StringUtil.hasLength(value)) {
			return null;
		}
		if(value.length() != length) {
			return new ActionMessage(mssageKey, mssageValues);
		}
		return null;
	}

	/**
	 * 整数チェックを行います.<BR>
	 * (メッセージキーは「errors.integer」固定).
	 * @param value 検証対象の値
	 * @param label 検証対象のラベル
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage integerType(String value, String label) {
		return integerType(value, "errors.integer", new Object[] { label });
	}

	/**
	 * 整数チェックを行います.
	 * @param value 検証対象の値
	 * @param mssageKey エラーメッセージのキー値
	 * @param mssageValues エラーメッセージの埋め込み文字配列
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage integerType(String value, String mssageKey, Object[] mssageValues) {
		if(!StringUtil.hasLength(value)) {
			return null;
		}
		try {
			Integer.parseInt(value);
		} catch(NumberFormatException e) {
			return new ActionMessage(mssageKey, mssageValues);
		}
		return null;
	}

	/**
	 * 整数範囲チェックを行います.<BR>
	 * (メッセージキーは「errors.range」固定).
	 * @param value 検証対象の値
	 * @param label 検証対象のラベル
	 * @param min 最小値
	 * @param max 最大値
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage intRange(String value, Integer min, Integer max, String label) {
		return intRange(value, min, max, "errors.range", new Object[] { label, min, max });
	}

	/**
	 * 整数範囲チェックを行います.
	 * @param value 検証対象の値
	 * @param min 最小値(nullの場合、最小値のチェックはしない)
	 * @param max 最大値(nullの場合、最大値のチェックはしない)
	 * @param mssageKey エラーメッセージのキー値
	 * @param mssageValues エラーメッセージの埋め込み文字配列
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage intRange(String value, Integer min, Integer max, String mssageKey, Object[] mssageValues) {
		if(!StringUtil.hasLength(value)) {
			return null;
		}
		// 整数チェック
		ActionMessage err = integerType(value, mssageKey, mssageValues);
		if(err != null) {
			return err;
		}
		// 引数の範囲の確認
		if(min != null && max != null) {
			if(max.compareTo(min) < 0) {
				// minとmaxの指定が逆の場合、入れ替える
				Integer work = min;
				min = max;
				max = work;
			}
		}
		// 整数範囲チェック
		Integer intVal = Integer.parseInt(value);
		// 最小値チェック
		if(min != null) {
			if(intVal.compareTo(min) < 0) {
				return new ActionMessage(mssageKey, mssageValues);
			}
		}
		// 最大値チェック
		if(max != null) {
			if(intVal.compareTo(max) > 0) {
				return new ActionMessage(mssageKey, mssageValues);
			}
		}
		return null;
	}

	/**
	 * 日付チェックを行います.<BR>
	 * (メッセージキーは「errors.date」固定)。
	 * @param value 検証対象の値
	 * @param datePattern 日付フォーマット
	 * @param strict 厳密な型チェックをするかどうか
	 * @param label 検証対象のラベル
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage dateType(String value, String datePattern, boolean strict, String label) {
		return dateType(value, datePattern, strict, "errors.date", new Object[] { label });
	}

	/**
	 * 日付チェックを行います.
	 * @param value 検証対象の値
	 * @param datePattern 日付フォーマット
	 * @param strict 厳密な型チェックをするかどうか
	 * @param mssageKey エラーメッセージのキー値
	 * @param mssageValues エラーメッセージの埋め込み文字配列
	 * @return エラーがある場合はActionMessageを返し、ない場合はnullを返します
	 */
	public static ActionMessage dateType(String value, String datePattern, boolean strict,
			String mssageKey, Object[] mssageValues) {
		if(!StringUtil.hasLength(value)) {
			return null;
		}
		boolean ret = dateValidator.isValid(value, datePattern, strict);
		if(!ret) {
			return new ActionMessage(mssageKey, mssageValues);
		}
		return null;
	}

	/**
	 * 未来日かどうかを現在日と比較して調べます.
	 * @param datePattern 日付文字列
	 * @return 未来の時true、未来でない(現在日も含む)false、日付フォーマットエラーはnull返却
	 */
	public static Boolean dateIsFuture(String datePattern) {
		try {
			// 本日0時との比較を行う
			Calendar today = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);
			df.setLenient(true);
			Date dt = df.parse(datePattern);
			Calendar expire = Calendar.getInstance();
			expire.setTime(dt);
			if( expire.after(today) ) {
				return true;
			} else {
				return false;
			}
		} catch(ParseException e) {
			return null;
		}

	}


	/**
	 * 当月かどうかを現在年月と比較して調べます.
	 * @param datePattern 入力文字列
	 * @return 当月の時 false、当月でない true
	 */
	public static Boolean dateIsNotThisMoon(String datePattern) {

			Calendar calendar = Calendar.getInstance();
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String month =String.format("%1$02d", calendar.get(Calendar.MONTH) + 1);

			//当月かどうか比較
			if( (datePattern.substring(0,4).equals(year)) && (datePattern.substring(5,7).equals(month)) ) {
				//当月
				return false;
			} else {
				//当月以外
				return true;
			}
	}

	/**
	 * 年月度のその月の最終日を取得します.
	 * @param ymFormat　年月度文字列
	 * @return　最終日文字列
	 * @throws ParseException
	 */
	public static String getLastDateOfMonthFromYmFormat(String ymFormat) throws ParseException {
		// 売掛残高の出力対象年月度の最終日(月の最終日)
		Date targetYmLastDate;
		DateFormat dfDateYm = new SimpleDateFormat(Constants.FORMAT.DATEYM_SLASH);
		DateFormat dfDate = new SimpleDateFormat(Constants.FORMAT.DATE);
		// 最終請求締日の翌日を取得
		Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(dfDateYm.parse(ymFormat));
		cal.add(Calendar.MONTH, 1);			// 翌月の初日を取得
		cal.add(Calendar.DAY_OF_MONTH, -1);	// 翌月の初日の前日は、月の最終日となる
		targetYmLastDate = cal.getTime();

		return dfDate.format(targetYmLastDate);
	}

	/**
	 * NIP+NFP形式に一致する正規表現パターンを返します.
	 * @param numberOfIntegerPart　整数部桁数
	 * @param numberOfFractionalPart　小数部桁数
	 * @return　正規表現パターン
	 */
	private static final String NUMBER_MASK_DECIMAL(int numberOfIntegerPart , int numberOfFractionalPart){
		return ((numberOfIntegerPart>0 && numberOfFractionalPart>=0)?
				"^(?:-?\\s*(?:(?:[1-9]\\d{0," + String.valueOf(numberOfIntegerPart-1) + "})|(?:0))"
					+((numberOfFractionalPart>0)?"(?:\\.\\d{1," + String.valueOf(numberOfFractionalPart) + "})?":"")+")?$"
				:"");
	}

	/**
	 * DECIMAL型Checkを行います.
	 * @param value　チェック対象文字列
	 * @param label　エラーメッセージに出力する項目名ラベル
	 * @param numberOfIntegerPart　整数部桁数
	 * @param numberOfFractionalPart　小数部桁数
	 * @return　エラーメッセージ
	 */
	public static ActionMessage decimalType(String value, String label, int numberOfIntegerPart, int numberOfFractionalPart) {
		if(!value.matches(NUMBER_MASK_DECIMAL(numberOfIntegerPart,numberOfFractionalPart))){
			String maxAbsOfValue = BigDecimal.TEN.pow(numberOfIntegerPart).subtract(BigDecimal.ONE).toString()
				+ ((numberOfFractionalPart>0) ? ("." +
						BigDecimal.TEN.pow(numberOfFractionalPart).subtract(BigDecimal.ONE).toString()) : "");
			return new ActionMessage("errors.outOfRange"
								,label
								,"-" + maxAbsOfValue
								,maxAbsOfValue
								);
		}
		return null;
	}

	/**
	 * DECIMAL型Checkを行います.
	 * @param index　明細行の行数
	 * @param value　チェック対象文字列
	 * @param label　エラーメッセージに出力する項目名ラベル
	 * @param numberOfIntegerPart　整数部桁数
	 * @param numberOfFractionalPart　小数部桁数
	 * @return　エラーメッセージ
	 */
	public static ActionMessage decimalType(int index, String value, String label, int numberOfIntegerPart, int numberOfFractionalPart) {
		if(!value.matches(NUMBER_MASK_DECIMAL(numberOfIntegerPart,numberOfFractionalPart))){
			String maxAbsOfValue = BigDecimal.TEN.pow(numberOfIntegerPart).subtract(BigDecimal.ONE).toString()
			+ ((numberOfFractionalPart>0) ? ("." +
					BigDecimal.TEN.pow(numberOfFractionalPart).subtract(BigDecimal.ONE).toString()) : "");
			return new ActionMessage("errors.line.outOfRange"
								,index
								,label
								,"-" + maxAbsOfValue
								,maxAbsOfValue
								);
		}
		return null;
	}

}
