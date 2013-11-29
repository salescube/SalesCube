/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

import jp.co.arkinfosys.common.Constants;

/**
 * 商品マスタ管理:履歴出力のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
@Component(instance=InstanceType.SESSION)

public class OutputProductHistForm {
	@Required
	@Maxlength(maxlength = Constants.CODE_SIZE.PRODUCT)
	public String productCode; // 商品コード

	@Required
	@Maxlength(maxlength = 60)
	public String productName; // 商品名

	/**
	 * フォームを初期化します.
	 */
	public void initialize() {

	}
}