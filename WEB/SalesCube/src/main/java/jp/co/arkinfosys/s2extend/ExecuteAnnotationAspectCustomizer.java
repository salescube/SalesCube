/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.s2extend;

import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.container.customizer.AspectCustomizer;
import org.seasar.framework.util.StringUtil;

/**
 * Executeアノテーションが宣言されたメソッドにのみアスペクトを適用するカスタマイザクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ExecuteAnnotationAspectCustomizer extends AspectCustomizer {

	private String pointcut;

	/**
	 * ポイントカットを設定します.
	 * @param pointcut ポイントカット
	 */
	@Override
	public void setPointcut(String pointcut) {
		this.pointcut = pointcut;
	}

	/**
	 * ポイントカットを作成します.
	 * @return ポイントカット
	 */
	@Override
	protected Pointcut createPointcut() {
		if (!StringUtil.isEmpty(this.pointcut)) {
			String[] methodNames = StringUtil.split(pointcut, ", \n");
			return new ExecutePointcut(methodNames);
		}

		if (super.targetInterface != null) {
			return new ExecutePointcut(super.targetInterface);
		}

		return null;
	}

}
