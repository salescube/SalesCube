/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.s2extend;

import java.lang.reflect.Method;

import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.struts.annotation.Execute;

/**
 * Executeアノテーションが宣言されたメソッドでのみ適用されるポイントカットクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ExecutePointcut extends PointcutImpl {

	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタです.
	 * @param targetClass クラス
	 * @throws EmptyRuntimeException
	 */
	public ExecutePointcut(Class targetClass) throws EmptyRuntimeException {
		super(targetClass);
	}

	/**
	 * コンストラクタです.
	 * @param method メソッド
	 */
	public ExecutePointcut(Method method) {
		super(method);
	}

	/**
	 * コンストラクタです.
	 * @param methodNames メソッド名配列
	 * @throws EmptyRuntimeException
	 */
	public ExecutePointcut(String[] methodNames) throws EmptyRuntimeException {
		super(methodNames);
	}

	/**
	 * メソッドに適用すべきか否かを返します.
	 * @param method メソッド
	 * @return メソッドに適用すべきか否か
	 */
	@Override
    public boolean isApplied(Method method) {
        return super.isApplied(method) &&
            method.isAnnotationPresent(Execute.class);
    }

}
