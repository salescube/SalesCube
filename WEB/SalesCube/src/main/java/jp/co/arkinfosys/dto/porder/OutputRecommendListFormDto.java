/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.porder;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
/**
 * 補充発注推奨リスト出力画面の検索結果リスト行情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class OutputRecommendListFormDto implements Serializable {

	private static final long serialVersionUID = 1L;

	//商品仕入先コード
	public String supplierCode;
	//商品仕入先名
	public String supplierName;

	// 発注伝票番号
	public String poSlipId;

	//発注可否
	public Boolean validRow;
	//商品コード
	public String productCode;
	//発注数量
	public String  pOrderQuantity;
	//発注ロット
	public String poLot;
	//リードタイム
	public String leadTime;
	//平均出庫数
	public String avgShipCount;
	//出荷数標準偏差
	public String salesStandardDeviation;
	//現在庫数
	public String stockQuantity;
	//自社安全在庫数
	public String mineSafetyStock;
	//発注点
	public String poNum;
	//委託在庫
	public String entrustQuantity;
	//委託安全在庫数
	public String entrustSafetyStock;
	//委託発注数
	public String entrustPoNum;
	//保有数
	public String holdQuantity;
	//保有月数
	public String holdTerm;
	//発注残
	public String poRestQuantity;
	//委託残
	public String entrustRestQuantity;
	//受注残
	public String roRestQuantity;

}
