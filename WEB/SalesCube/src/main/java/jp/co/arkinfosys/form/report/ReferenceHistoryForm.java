/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.report;

import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.DateType;

/**
 * 履歴参照画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class ReferenceHistoryForm {
	/**
	 * 検索対象
	 */
    public String outputTarget;

    /**
     * アクションタイプ
     */
    public String actionType;

    /**
     * 入力/記録日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String recDateFrom;

    /**
     * 入力/記録日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String recDateTo;

    ////////// 見積入力
    /**
     * 見積日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String estimateDateFrom1;

    /**
     * 見積日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String estimateDateTo1;

    ////////// 受注入力
    /**
     * 顧客コードFrom
     */
    public String customerCodeFrom2;

    /**
     * 顧客コードTo
     */
    public String customerCodeTo2;

    /**
     * 商品コードFrom
     */
    public String productCodeFrom2;

    /**
     * 商品コードTo
     */
    public String productCodeTo2;

    /**
     * 出荷日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String shipDateFrom2;

    /**
     * 出荷日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String shipDateTo2;

    ////////// 売上入力
    /**
     * 顧客コードFrom
     */
    public String customerCodeFrom3;

    /**
     * 顧客コードTo
     */
    public String customerCodeTo3;

    /**
     * 商品コードFrom
     */
    public String productCodeFrom3;

    /**
     * 商品コードTo
     */
    public String productCodeTo3;

    ////////// 入金入力
    /**
     * 顧客コードFrom
     */
    public String customerCodeFrom4;

    /**
     * 顧客コードTo
     */
    public String customerCodeTo4;

    ////////// 発注入力
    /**
     * 仕入先コードFrom
     */
    public String supplierCodeFrom5;

    /**
     * 仕入先コードTo
     */
    public String supplierCodeTo5;

    /**
     * 商品コードFrom
     */
    public String productCodeFrom5;

    /**
     * 商品コードTo
     */
    public String productCodeTo5;

    ////////// 仕入入力
    /**
     * 仕入先コードFrom
     */
    public String supplierCodeFrom6;

    /**
     * 仕入先コードTo
     */
    public String supplierCodeTo6;

    /**
     * 商品コードFrom
     */
    public String productCodeFrom6;

    /**
     * 商品コードTo
     */
    public String productCodeTo6;

    /**
     * 納期From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String deliveryDateFrom6;

    /**
     * 納期To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String deliveryDateTo6;

    ////////// 支払入力
    /**
     * 仕入先コードFrom
     */
    public String supplierCodeFrom7;

    /**
     * 仕入先コードTo
     */
    public String supplierCodeTo7;

    /**
     * 商品コードFrom
     */
    public String productCodeFrom7;

    /**
     * 商品コードTo
     */
    public String productCodeTo7;

    /**
     * 支払日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String paymentDateFrom7;

    /**
     * 支払日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String paymentDateTo7;

    ////////// 入出庫入力
    /**
     * 商品コードFrom
     */
    public String productCodeFrom8;

    /**
     * 商品コードTo
     */
    public String productCodeTo8;

    /**
     * 入出庫伝票区分
     */
    public String[] eadSlipCategory8;

    ////////// 顧客マスタ
    /**
     * 顧客コードFrom
     */
    public String customerCodeFrom9;

    /**
     * 顧客コードTo
     */
    public String customerCodeTo9;

    /**
     * 登録日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String creDateFrom9;

    /**
     * 登録日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String creDateTo9;

    ////////// 商品マスタ
    /**
     * 商品コードFrom
     */
    public String productCodeFrom10;

    /**
     * 商品コードTo
     */
    public String productCodeTo10;

    /**
     * 登録日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String creDateFrom10;

    /**
     * 登録日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String creDateTo10;

    ////////// 仕入先マスタ
    /**
     * 仕入先コードFrom
     */
    public String supplierCodeFrom11;

    /**
     * 仕入先コードTo
     */
    public String supplierCodeTo11;

    /**
     * 登録日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String creDateFrom11;

    /**
     * 登録日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String creDateTo11;

    ////////// 社員マスタ
    /**
     * 登録日From
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String creDateFrom12;

    /**
     * 登録日To
     */
    @DateType(datePatternStrict = "yyyy/MM/dd")
    public String creDateTo12;

    /**
	 * 全フィールドのクリアを行います,
	 */
	public void reset() {
		outputTarget = null;
		actionType = null;
		recDateFrom = null;
		recDateTo = null;
		estimateDateFrom1 = null;
		estimateDateTo1 = null;

		customerCodeFrom2 = null;
		customerCodeTo2 = null;
		productCodeFrom2 = null;
		productCodeTo2 = null;
		shipDateFrom2 = null;
		shipDateTo2 = null;

		customerCodeFrom3 = null;
		customerCodeTo3 = null;
		productCodeFrom3 = null;
		productCodeTo3 = null;

		customerCodeFrom4 = null;
		customerCodeTo4 = null;

		supplierCodeFrom5 = null;
		supplierCodeTo5 = null;
		productCodeFrom5 = null;
		productCodeTo5 = null;

		supplierCodeFrom6 = null;
		supplierCodeTo6 = null;
		productCodeFrom6 = null;
		productCodeTo6 = null;
		deliveryDateFrom6 = null;
		deliveryDateTo6 = null;

		supplierCodeFrom7 = null;
		supplierCodeTo7 = null;
		productCodeFrom7 = null;
		productCodeTo7 = null;
		paymentDateFrom7 = null;
		paymentDateTo7 = null;

		productCodeFrom8 = null;
		productCodeTo8 = null;
		eadSlipCategory8 = new String[0];

		customerCodeFrom9 = null;
		customerCodeTo9 = null;
		creDateFrom9 = null;
		creDateTo9 = null;

		productCodeFrom10 = null;
		productCodeTo10 = null;
		creDateFrom10 = null;
		creDateTo10 = null;

		supplierCodeFrom11 = null;
		supplierCodeTo11 = null;
		creDateFrom11 = null;
		creDateTo11 = null;

		creDateFrom12 = null;
		creDateTo12 = null;
	}

	/**
	 * Excel出力時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionErrors validate() {
		// 数値項目，日付項目のトリム
		recDateFrom = StringUtil.trimBlank(recDateFrom);
		recDateTo = StringUtil.trimBlank(recDateTo);
		estimateDateFrom1 = StringUtil.trimBlank(estimateDateFrom1);
		estimateDateTo1 = StringUtil.trimBlank(estimateDateTo1);
		shipDateFrom2 = StringUtil.trimBlank(shipDateFrom2);
		shipDateTo2 = StringUtil.trimBlank(shipDateTo2);
		deliveryDateFrom6 = StringUtil.trimBlank(deliveryDateFrom6);
		deliveryDateTo6 = StringUtil.trimBlank(deliveryDateTo6);
		paymentDateFrom7 = StringUtil.trimBlank(paymentDateFrom7);
		paymentDateTo7 = StringUtil.trimBlank(paymentDateTo7);
		creDateFrom9 = StringUtil.trimBlank(creDateFrom9);
		creDateTo9 = StringUtil.trimBlank(creDateTo9);
		creDateFrom10 = StringUtil.trimBlank(creDateFrom10);
		creDateTo10 = StringUtil.trimBlank(creDateTo10);
		creDateFrom11 = StringUtil.trimBlank(creDateFrom11);
		creDateTo11 = StringUtil.trimBlank(creDateTo11);
		creDateFrom12 = StringUtil.trimBlank(creDateFrom12);
		creDateTo12 = StringUtil.trimBlank(creDateTo12);

		ActionErrors errors = new ActionErrors();

		if(!StringUtil.hasLength(recDateFrom) && !StringUtil.hasLength(recDateTo)) {
			// 検索条件未入力の場合
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.report.needRecDateRange"));
		}

		return errors;
	}
}
