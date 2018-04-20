/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.report;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 履歴参照画面の情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class ReferenceHistoryFormDto implements Serializable  {
    private static final long serialVersionUID = 1L;

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
    public String recDateFrom;

    /**
     * 入力/記録日To
     */
    public String recDateTo;

    ////////// 見積入力
    /**
     * 見積日From
     */
    public String estimateDateFrom1;

    /**
     * 見積日To
     */
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
    public String shipDateFrom2;

    /**
     * 出荷日To
     */
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
    public String deliveryDateFrom6;

    /**
     * 納期To
     */
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
    public String paymentDateFrom7;

    /**
     * 支払日To
     */
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
    public String creDateFrom9;

    /**
     * 登録日To
     */
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
    public String creDateFrom10;

    /**
     * 登録日To
     */
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
    public String creDateFrom11;

    /**
     * 登録日To
     */
    public String creDateTo11;

    ////////// 社員マスタ
    /**
     * 登録日From
     */
    public String creDateFrom12;

    /**
     * 登録日To
     */
    public String creDateTo12;
}
