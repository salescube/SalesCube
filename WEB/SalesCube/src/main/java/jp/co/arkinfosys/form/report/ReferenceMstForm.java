/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.report;

import jp.co.arkinfosys.common.StringUtil;

import org.apache.struts.action.ActionErrors;
import org.seasar.struts.annotation.DateType;

/**
 * マスタリスト画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class ReferenceMstForm {
	/**
	 * 検索対象
	 */
    public String outputTarget;

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
		creDateFrom9 = StringUtil.trimBlank(creDateFrom9);
		creDateTo9 = StringUtil.trimBlank(creDateTo9);
		creDateFrom10 = StringUtil.trimBlank(creDateFrom10);
		creDateTo10 = StringUtil.trimBlank(creDateTo10);
		creDateFrom11 = StringUtil.trimBlank(creDateFrom11);
		creDateTo11 = StringUtil.trimBlank(creDateTo11);
		creDateFrom12 = StringUtil.trimBlank(creDateFrom12);
		creDateTo12 = StringUtil.trimBlank(creDateTo12);

		ActionErrors errors = new ActionErrors();

		return errors;
	}
}
