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
 * 顧客マスタ管理:履歴出力のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
@Component(instance=InstanceType.SESSION)

public class OutputCustomerHistForm {
	/** 顧客コード */
	@Required
	@Maxlength(maxlength=Constants.CODE_SIZE.CUSTOMER)
	public String customerCode;

	/** 顧客名 */
	@Required
	@Maxlength(maxlength=60)
	public String customerName;

	/**
	 * フォームを初期化します.
	 */
	public void initialize() {

	}
}
