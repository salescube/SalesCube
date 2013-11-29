/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import java.io.Serializable;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 在庫一覧表画面情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class OutputStockListFormDto implements Serializable {
    private static final long serialVersionUID = 1L;

	/**
	 * 商品の抽出条件１:ヶ月前～本日　で受注実績のあるもの
	 */
	public String periodMonth;

	/**
	 * 棚種別
	 */
	public String rackCategory;

	/**
	 * 棚種別名
	 */
	public String rackCategoryName;

	/**
	 * 商品の抽出条件２ラジオボタン
	 */
	public String radioCond2;

	/**
	 * 商品の抽出条件２:引当可能数
	 */
	public String allocatedQuantity;

	/**
	 * 商品の抽出条件２:引当可能数（カンマ付き）
	 */
	public String allocatedQuantityWithComma;

	/**
	 * 商品の抽出条件３:受注実績のない商品は除く
	 */
	public boolean excludeRoNotExists;

	/**
	 * 商品の抽出条件４:販売中止品(発注停止品)は除く
	 */
	public boolean excludeSalesCancel;

	/**
	 * 商品の抽出条件５:在庫管理しない商品（都度調達品）は除く
	 */
	public boolean excludeNotManagementStock;

	/**
	 * 商品の抽出条件６:重複可能な棚番の商品は除く
	 */
	public boolean excludeMultiRack;

    /**
     * 検索結果件数
     */
    public Integer searchResultCount;

	/**
	 * 検索結果リスト
	 */
	public List<OutputStockListDto> searchResultList;
}
