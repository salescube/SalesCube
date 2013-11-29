/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.s2extend;

import java.text.DecimalFormat;
import java.text.ParseException;

import jp.co.arkinfosys.common.NumberUtil;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.exception.ParseRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * Beans#CopyメソッドまたはBeans#CreateAndCopyメソッドで数値型を変換する場合に指定するコンバータクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class NumberConverter implements Converter {

	protected String fract;

	protected int alignment;

	protected boolean comma;

	protected Class targetClass;

	protected DecimalFormat df;

	/**
	 * コンストラクタです.
	 *
	 * @param df
	 *            DecimalFormatクラスのオブジェクト
	 */
	public NumberConverter(DecimalFormat df) {
		this.targetClass = Number.class;
		this.df = df;
	}

	/**
	 * コンストラクタです.
	 *
	 * @param fract
	 *            端数処理コード
	 * @param alignment
	 *            小数桁数
	 * @param comma
	 *            3桁ごとカンマ付与
	 */
	public NumberConverter(String fract, int alignment, boolean comma) {
		this.fract = fract;
		this.alignment = alignment;
		this.comma = comma;
		this.targetClass = Number.class;
		this.df = NumberUtil.createDecimalFormat(fract, alignment, comma);
	}

	/**
	 * 変換結果を返します.
	 * @param value 変換対象文字列
	 * @return 変換結果
	 */
	public Object getAsObject(String value) {
		if (StringUtil.isEmpty(value)) {
			return null;
		}
		try {
			return this.df.parse(value);
		} catch (ParseException e) {
			throw new ParseRuntimeException(e);
		}

	}

	/**
	 * 変換結果を返します.
	 * @param value 変換対象オブジェクト
	 * @return 変換結果
	 */
	public String getAsString(Object value) {
		return this.df.format(value);
	}

	/**
	 * ターゲットクラスに適用出来るか否かを返します.
	 * @param clazz クラス
	 * @return 適用できるか否か
	 */
	@SuppressWarnings( { "unchecked" })
	public boolean isTarget(Class clazz) {
		return this.targetClass.isAssignableFrom(clazz);
	}

	/**
	 * ターゲットクラスを設定します.
	 * @param targetClass ターゲットクラス
	 */
	public void setTargetClass(Class targetClass) {
		this.targetClass = targetClass;
	}

	/**
	 * DecimalFormatクラスのオブジェクトを設定します.
	 * @param df DecimalFormatクラスのオブジェクト
	 */
	public void setDf(DecimalFormat df) {
		this.df = df;
	}
}
