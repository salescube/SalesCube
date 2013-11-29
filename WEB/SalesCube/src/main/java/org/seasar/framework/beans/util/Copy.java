/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package org.seasar.framework.beans.util;

import java.util.Map;

import jp.co.arkinfosys.common.StringUtil;

/**
 * プロパティをコピーするクラスに文字列のトリム機能を追加する拡張です.
 *
 * @author Ark Information Systems
 *
 */
public class Copy extends AbstractCopy<Copy> {

	/**
	 * コピー元です。
	 */
	protected Object src;

	/**
	 * コピー先です。
	 */
	protected Object dest;

	/**
	 * 文字列のトリムを行うかどうかです
	 */
	protected boolean lrTrim = false;

	/**
	 * 文字列のプロパティに対して前後の空白除去処理を行います.
	 *
	 * @return このインスタンス自身
	 */
	public Copy lrTrim() {
		this.lrTrim = true;
		return this;
	}

	/**
	 * コンストラクタです.
	 * @param src　コピー元オブジェクト
	 * @param dest　コピー先オブジェクト
	 * @throws NullPointerException
	 */
	public Copy(Object src, Object dest) throws NullPointerException {
		if (src == null) {
			throw new NullPointerException("src");
		}
		if (dest == null) {
			throw new NullPointerException("dest");
		}
		this.src = src;
		this.dest = dest;
	}

	/**
	 *　プロパティをコピーします.
	 */
	@SuppressWarnings({"unchecked"})
	public void execute() {
		if (src instanceof Map && dest instanceof Map) {
			copyMapToMap((Map) src, (Map) dest);
		} else if (src instanceof Map) {
			copyMapToBean((Map) src, dest);
		} else if (dest instanceof Map) {
			copyBeanToMap(src, (Map) dest);
		} else {
			copyBeanToBean(src, dest);
		}
	}

	/**
	 * 値を変換します.
	 * @param value 値
	 * @param destPropertyName コピー先のプロパティ名
	 * @param destPropertyClass コピー先のプロパティクラス
	 * @return 変換後の値
	 *
	 */
	@Override
	protected Object convertValue(Object value, String destPropertyName,
			Class<?> destPropertyClass) {
		return super.convertValue(this.trim(value), destPropertyName,
				destPropertyClass);
	}

	/**
	 * 処理対象のオブジェクトが文字列ならトリムします.
	 * @param value　処理対象オブジェクト
	 * @return　トリムしたオブジェクト
	 */
	private Object trim(Object value) {
		if (value == null) {
			return value;
		}
		if (this.lrTrim && value instanceof String) {
			// トリム指定された文字列であればトリムする
			return StringUtil.trimBlank((String) value);
		}
		return value;
	}
}