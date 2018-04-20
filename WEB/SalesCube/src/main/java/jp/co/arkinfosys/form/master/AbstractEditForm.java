/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.MessageResourcesUtil;
import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


/**
 * マスタ管理画面（登録・編集）の基底アクションフォームです.
 *
 * @author Ark Information Systems
 *
 */
public abstract class AbstractEditForm {

	/** 更新権限フラグ */
	public boolean isUpdate = true;
	/** 追加・編集モード */
	public boolean editMode = false;
	/** 作成日付 */
	public String creDatetm;
	/** 作成日付（表示用） */
	public String creDatetmShow;
	/** 更新日付（排他制御用） */
	public String updDatetm;
	/** 更新日付（表示用） */
	public String updDatetmShow;

	/**
	 * フォームを初期化します.
	 */
	public abstract void initialize();

	/**
	 * 必須チェックを行います.
	 * @param index　行数
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return エラーでないか否か
	 */
	protected boolean checkRequired(int index, String value, String label,
			ActionMessages errors) {
		if (!StringUtil.hasLength(value)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.required", index, label));
			return false;
		}
		return true;
	}

	/**
	 * 必須チェックを行います.
	 * @param value　値
	 * @param label　項目名
	 * @param  errors　表示するメッセージ
	 * @return エラーでないか否か
	 */
	protected boolean checkRequired(String value, String label, ActionMessages errors) {
		if (!StringUtil.hasLength(value)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.required", label));
			return false;
		}
		return true;
	}

	/**
	 * 最大長チェックを行います.
	 * @param value　値
	 * @param size　最大長
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return エラーでないか否か
	 */
	protected boolean checkMaxLength(String value, int size, String label,
			ActionMessages errors) {
		if (StringUtil.hasLength(value) && value.length() > size) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.maxlength", label, size));
			return false;
		}
		return true;
	}

	/**
	 * 最大長チェックを行います.
	 * @param index　行数
	 * @param value　値
	 * @param size　最大長
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return エラーでないか否か
	 */
	protected boolean checkMaxLength(int index, String value, int size,
			String label, ActionMessages errors) {
		if (StringUtil.hasLength(value) && value.length() > size) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.maxlength", index, label, size));
			return false;
		}
		return true;
	}

	/**
	 * 指定された長さかどうかのチェックを行います.
	 * @param value　値
	 * @param size　長さ
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkLength(String value, int size, String label,
			ActionMessages errors) {
		if (StringUtil.hasLength(value) && value.length() != size) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.length", label, size));
			return false;
		}
		return true;
	}

	/**
	 * 指定された長さかどうかのチェックを行います.
	 * @param index　行数
	 * @param value　値
	 * @param size　長さ
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkLength(int index, String value, int size, String label,
			ActionMessages errors) {
		if (StringUtil.hasLength(value) && value.length() != size) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.length", index, label, size));
			return false;
		}
		return true;
	}

	private static final String IntMinLimit = "-999999999";
	private static final String IntZero = "0";
	private static final String IntMaxLimit = "999999999";
	private static final BigDecimal BDIntMinLimit = new BigDecimal(IntMinLimit);
	private static final BigDecimal BDIntZero = new BigDecimal(IntZero);
	private static final BigDecimal BDIntMaxLimit = new BigDecimal(IntMaxLimit);

	/**
	 * 整数かどうか判定します.
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkInteger(String value, String label, ActionMessages errors) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			try{
				BigDecimal temp = new BigDecimal(value);
				if( BDIntMinLimit.compareTo(temp) > 0 || BDIntMaxLimit.compareTo(temp) < 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.outOfRange", label, IntMinLimit, IntMaxLimit));
					return false;
				}
			} catch (Exception er) {}
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.integer", label));
			return false;
		}
		return true;
	}

	/**
	 * 整数かどうか判定します.
	 * @param index　行数
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkInteger(int index, String value, String label, ActionMessages errors) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			try{
				BigDecimal temp = new BigDecimal(value);
				if( BDIntMinLimit.compareTo(temp) > 0 || BDIntMaxLimit.compareTo(temp) < 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.line.outOfRange", index, label, IntMinLimit, IntMaxLimit));
					return false;
				}
			} catch (Exception er) {}
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.integer", index, label));
			return false;
		}
		return true;
	}

	/**
	 * 正の整数かどうか判定します.
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkIntegerPlus(String value, String label, ActionMessages errors) {
		try {
			int val = Integer.parseInt(value);
			if (val <= 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.integer.plus", label));
				return false;
			}
		} catch (NumberFormatException e) {
			try{
				BigDecimal temp = new BigDecimal(value);
				if( BDIntZero.compareTo(temp) >= 0 || BDIntMaxLimit.compareTo(temp) < 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.outOfRange", label, IntZero, IntMaxLimit));
					return false;
				}
			} catch (Exception er) {}
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.integer", label));
			return false;
		}
		return true;
	}

	/**
	 * 正の整数かどうか判定します.
	 * @param index　行数
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkIntegerPlus(int index, String value, String label, ActionMessages errors) {
		try {
			Integer.parseInt(value);
			int val = Integer.parseInt(value);
			if (val <= 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.integer.plus", index, label));
				return false;
			}
		} catch (NumberFormatException e) {
			try{
				BigDecimal temp = new BigDecimal(value);
				if( BDIntZero.compareTo(temp) >= 0 || BDIntMaxLimit.compareTo(temp) < 0 ){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.line.outOfRange", index, label, IntZero, IntMaxLimit));
					return false;
				}
			} catch (Exception er) {}
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.integer", index, label));
			return false;
		}
		return true;
	}

	/**
	 * 整数でかつ範囲内に入っているか判定します.
	 * @param index　行数
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @param minValue　下限
	 * @param maxValue　上限
	 * @return　エラーでないか否か
	 */
	protected boolean checkIntegerRange(int index, String value, String label, ActionMessages errors, int minValue, int maxValue ) {
		try {
			Integer.parseInt(value);
			int val = Integer.parseInt(value);
			if ( val < minValue ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.range.eq.more", index, label, minValue));
				return false;
			}
			if ( maxValue < val ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.line.range.eq.less", index, label, maxValue));
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.integer", index, label));
			return false;
		}
	}

	/**
	 * 実数かどうか判定します.
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkFloat(String value, String label, ActionMessages errors) {
		try {
			Float.parseFloat(value);
			return true;
		} catch (NumberFormatException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.float", label));
			return false;
		}
	}

	/**
	 * 実数かどうか判定します.
	 * @param index　行数
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return エラーでないか否か
	 */
	protected boolean checkFloat(int index, String value, String label, ActionMessages errors) {
		try {
			Float.parseFloat(value);
			return true;
		} catch (NumberFormatException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.float", index, label));
			return false;
		}
	}

	/**
	 * DECIMAL型かどうか判定します.
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return エラーでないか否か
	 */
	protected boolean checkDecimal5_3(String value, String label, ActionMessages errors) {
		if(!value.matches(Constants.NUMBER_MASK.DECIMAL5_3)){
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.mismatchFormat"
								,label
								,MessageResourcesUtil.getMessage("words.format.decimal5_3")
								));
			return false;
		}
		return true;
	}

	/**
	 * DECIMAL型かどうか判定します.
	 * @param index　行数
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return エラーでないか否か
	 */
	protected boolean checkDecimal5_3(int index, String value, String label, ActionMessages errors) {
		if(!value.matches(Constants.NUMBER_MASK.DECIMAL5_3)){
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.line.mismatchFormat"
								,index
								,label
								,MessageResourcesUtil.getMessage("words.format.decimal5_3")
								));
			return false;
		}
		return true;
	}

	/**
	 * NIP+NFP形式に一致する正規表現パターンを返します.
	 * @param numberOfIntegerPart　整数部桁数
	 * @param numberOfFractionalPart　小数部桁数
	 * @return　正規表現パターン
	 */
	private static final String NUMBER_MASK_DECIMAL(int numberOfIntegerPart , int numberOfFractionalPart){
		return ((numberOfIntegerPart>0 && numberOfFractionalPart>0)?
				"^(?:-?(?:(?:[1-9]\\d{0," + String.valueOf(numberOfIntegerPart-1) + "})|(0))(?:\\.\\d{1," + String.valueOf(numberOfFractionalPart) + "})?)?$"
				:"");
	}

	/**
	 * DECIMAL型かどうか判定します.
	 * @param value　値
	 * @param label　項目名
	 * @param numberOfIntegerPart　整数部桁数
	 * @param numberOfFractionalPart　小数部桁数
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	public boolean checkDecimal(String value, String label, int numberOfIntegerPart , int numberOfFractionalPart, ActionMessages errors) {
		if(!value.matches(NUMBER_MASK_DECIMAL(numberOfIntegerPart,numberOfFractionalPart))){
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.mismatchFormat"
								,label
								,MessageResourcesUtil.getMessage("words.format.decimal",numberOfIntegerPart,numberOfFractionalPart)
								));
			return false;
		}
		return true;
	}

	/**
	 * DECIMAL型かどうか判定します.
	 * @param index　行数
	 * @param value　値
	 * @param label　項目名
	 * @param numberOfIntegerPart　整数部桁数
	 * @param numberOfFractionalPart　小数部桁数
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkDecimal(int index, String value, String label, int numberOfIntegerPart , int numberOfFractionalPart, ActionMessages errors) {
		if(!value.matches(NUMBER_MASK_DECIMAL(numberOfIntegerPart,numberOfFractionalPart))){
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.line.mismatchFormat"
								,index
								,label
								,MessageResourcesUtil.getMessage("words.format.decimal",numberOfIntegerPart,numberOfFractionalPart)
								));
			return false;
		}
		return true;
	}


	/**
	 * 指定された文字列が、半角英数字(記号含む)か否かを返します。
	 *
	 * @param value 処理対象となる文字列
	 * @return true:半角英数字である(もしくは対象文字がない), false:半角英数字でない
	 */
	protected boolean isHalfWidthAlphanumeric(int index, String value, int size,
				String label, ActionMessages errors) {

	    int len = value.length();
	    byte[] bytes = value.getBytes();
	    if ( len != bytes.length ){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.ascii", index, label));
	        return false;
	    }
	    return true;
	}

	/**
	 * 日付文字列かどうか判定します.
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkDate(String value, String label, ActionMessages errors) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);
		try {
			sdf.parse(value);
			return true;
		} catch (ParseException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.date", label));
			return false;
		}
	}

	/**
	 * 日付文字列かどうか判定します.
	 * @param index　行数
	 * @param value　値
	 * @param label　項目名
	 * @param errors　表示するメッセージ
	 * @return　エラーでないか否か
	 */
	protected boolean checkDate(int index, String value, String label, ActionMessages errors) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);
		try {
			sdf.parse(value);
			return true;
		} catch (ParseException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.date", index, label));
			return false;
		}
	}
}
