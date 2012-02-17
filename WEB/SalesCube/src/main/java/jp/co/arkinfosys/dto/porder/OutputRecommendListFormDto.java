/*
 *  Copyright 2009-2010 Ark Information Systems.
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

	
	public String supplierCode;
	
	public String supplierName;

	
	public String poSlipId;

	
	public Boolean validRow;
	
	public String productCode;
	
	public String  pOrderQuantity;
	
	public String poLot;
	
	public String leadTime;
	
	public String avgShipCount;
	
	public String salesStandardDeviation;
	
	public String stockQuantity;
	
	public String mineSafetyStock;
	
	public String poNum;
	
	public String entrustQuantity;
	
	public String entrustSafetyStock;
	
	public String entrustPoNum;
	
	public String holdQuantity;
	
	public String holdTerm;
	
	public String poRestQuantity;
	
	public String entrustRestQuantity;
	
	public String roRestQuantity;

}
