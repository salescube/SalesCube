/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package org.seasar.framework.beans.util;

import java.util.HashMap;
import java.util.Map;

import jp.co.arkinfosys.common.StringUtil;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ModifierUtil;

/**
 * JavaBeansやMapを生成しプロパティをコピーするクラスに文字列のトリム機能を追加する拡張です.
 *
 * @author Ark Information Systems
 *
 */
public class CreateAndCopy<T> extends AbstractCopy<CreateAndCopy<T>> {

	/**
	 * 作成対象クラス
	 */
	protected Class<T> destClass;

	/**
	 * コピー元です。
	 */
	protected Object src;

	/**
	 * 文字列のトリムを行うかどうかです
	 */
	protected boolean lrTrim = false;

	/**
	 * 文字列のプロパティに対して前後の空白除去処理を行います.
	 *
	 * @return このインスタンス自身
	 */
	public CreateAndCopy<T> lrTrim() {
		this.lrTrim = true;
		return this;
	}

	/**
	 * コンストラクタです.
	 * @param destClass　作成対象クラス
	 * @param src　コピー元
	 * @throws NullPointerException
	 */
	public CreateAndCopy(Class<T> destClass, Object src)
			throws NullPointerException {
		if (destClass == null) {
			throw new NullPointerException("destClass");
		}
		if (src == null) {
			throw new NullPointerException("src");
		}
		this.destClass = destClass;
		this.src = src;
	}

	/**
	 *　JavaBeansやMapを生成し、プロパティをコピーします.
	 * @return　作成結果
	 */
	@SuppressWarnings({"unchecked"})
	public T execute() {
		if (Map.class.isAssignableFrom(destClass)) {
			Map dest = null;
			if (ModifierUtil.isAbstract(destClass)) {
				dest = new HashMap();
			} else {
				dest = (Map) ClassUtil.newInstance(destClass);
			}
			if (src instanceof Map) {
				copyMapToMap((Map) src, dest);
			} else {
				copyBeanToMap(src, dest);
			}
			return (T) dest;
		}
		T dest = (T) ClassUtil.newInstance(destClass);
		if (src instanceof Map) {
			copyMapToBean((Map) src, dest);
		} else {
			copyBeanToBean(src, dest);
		}
		return dest;
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