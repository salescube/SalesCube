/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import java.io.Serializable;

/**
 * 在庫一覧表検索結果行情報を管理するDTOクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputStockListDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品コード
     */
    public String productCode;

    /**
     * 商品名
     */
    public String productName;

    /**
     * 棚番コード
     */
    public String rackCode;

    /**
     * 現在庫数
     */
    public String stockQuantity;

    /**
     * 受注残数
     */
    public String rOrderRestQuantity;

    /**
     * 引当可能数
     */
    public String allocatableQuantity;

    /**
     * 船便発注残数
     */
    public String pOrderRestQuantityShip;

    /**
     * AIR便発注残数
     */
    public String pOrderRestQuantityAir;

    /**
     * 宅配便発注残数
     */
    public String pOrderRestQuantityDelivery;

    /**
     * 発注残数
     */
    public String pOrderRestQuantity;

    /**
     * 委託在庫発注残数
     */
    public String pOrderQuantityEntrust;

    /**
     * 委託在庫数
     */
    public String stockQuantityEntrust;

    /**
     * 最短入荷日
     */
    public String minDeliveryDate;

    /**
     * 保有数
     */
    public String holdQuantity;

    /**
     * 受注数量
     */
    public String rOrderQuantity;

    /**
     * 出荷数量
     */
    public String salesQuantity;

    /**
     * 平均出荷数量
     */
    public String avgSalesQuantity;

    /**
     * 出荷数偏差
     */
    public String deviationSalesQuantity;

    /**
     * 過去最大数
     */
    public String maxMonthlyQuantity;

    /**
     * 発注点
     */
    public String orderPoint;

    /**
     * 発注ロット
     */
    public String orderLot;

    /**
     * 売上金額
     */
    public String salesMoney;

    /**
     * 売単価
     */
    public String salesPrice;

    /**
     * 仕入単価（外貨）
     */
    public String supplierPriceDol;

    /**
     * 仕入単価（円）
     */
    public String supplierPriceYen;

    /**
     * 粗利益
     */
    public String grossMargin;

    /**
     * 在庫高
     */
    public String stockPrice;

    /**
     * 粗利率
     */
    public String grossMarginRatio;

    /**
     * 在庫回転率
     */
    public String stockRotationRatio;

    /**
     * 交差比率
     */
    public String intersectionRatio;
}
