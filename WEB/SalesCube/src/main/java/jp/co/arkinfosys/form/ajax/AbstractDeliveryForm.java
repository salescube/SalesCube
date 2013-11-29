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
 * 納入先・請求先情報を保持する基底アクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public abstract class AbstractDeliveryForm {
	/**
	 * 	納入先・請求先の検索条件となる顧客コード
	 */
	@Required
	@Mask(mask = Constants.CODE_MASK.CUSTOMER_MASK , msg = @Msg(key = "errors.invalid"), args = @Arg(key = "labels.customerCode", resource = true, position = 0))
	public String customerCode;
	/**
	 * 顧客名
	 */
	public String customerName;
	/**
	 *　売上取引区分
	 */
	public String salesCmCategory;
	/**
	 *　売上取引区分名
	 */
	public String salesCmCategoryName;

	// 得意先関連区分
	public String custRelCategory;

	/**
	 * 備考（顧客）
	 */
	public String customerRemarks;

	/**
	 * コメント（顧客）
	 */
	public String customerCommentData;

	/**
	 *
	 */
	public String deliveryCode;
	/**
	 *
	 */
	public String deliveryName;
	/**
	 *
	 */
	public String deliveryKana;
	/**
	 *
	 */
	public String deliveryOfficeName;
	/**
	 *
	 */
	public String deliveryOfficeKana;
	/**
	 *
	 */
	public String deliveryDeptName;
	/**
	 *
	 */
	public String deliveryZipCode;
	/**
	 *
	 */
	public String deliveryAddress1;
	/**
	 *
	 */
	public String deliveryAddress2;
	/**
	 *
	 */
	public String deliveryPcName;
	/**
	 *
	 */
	public String deliveryPcKana;
	/**
	 *
	 */
	public String deliveryPcPreCategory;
	/**
	 *
	 */
	public String deliveryTel;
	/**
	 *
	 */
	public String deliveryFax;
	/**
	 *
	 */
	public String deliveryEmail;
	/**
	 *
	 */
	public String deliveryUrl;
	/**
	 *
	 */
	public String remarks;
	/**
	 *
	 */
	public String creFunc;
	/**
	 *
	 */
	public String creDatetm;
	/**
	 *
	 */
	public String creUser;
	/**
	 *
	 */
	public String updFunc;
	/**
	 *
	 */
	public String updDatetm;
	/**
	 *
	 */
	public String updUser;

	// 敬称文字列　コードはDeliveryを参照のこと
	public String categoryCodeName;

	/**
	 * 顧客敬称
	 */
	public String customerPcPreCategoryName;
	/**
	 * 売上帳票区分
	 */
	public String salesSlipCategory;
	/**
	 * 請求書発行単位
	 */
	public String billPrintUnit;
}
