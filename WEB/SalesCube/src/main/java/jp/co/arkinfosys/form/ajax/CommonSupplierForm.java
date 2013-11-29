/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax;

import jp.co.arkinfosys.common.Constants;

import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;


/**
 * 仕入先コード情報を保持するアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonSupplierForm {
	/**
	 * 	仕入先検索条件となる仕入先コード
	 */
	@Required
	@Mask(mask = Constants.CODE_MASK.SUPPLIER_MASK , msg = @Msg(key = "errors.invalid"), args = @Arg(key = "labels.supplierCode", resource = true, position = 0))
//	@Validwhen(test = "(customerIsExist == 1)", msg = @Msg(key = "errors.invalid"), args = @Arg(key = "labels.customerCode", resource = true, position = 0))
	public String supplierCode;


}
